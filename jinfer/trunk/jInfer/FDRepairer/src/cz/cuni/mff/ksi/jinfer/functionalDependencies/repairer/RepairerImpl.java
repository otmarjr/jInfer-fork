/*
 * Copyright (C) 2011 sviro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.functionalDependencies.repairer;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.InitialModel;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.Path;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.PathAnswer;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.RXMLTree;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.Tuple;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.TupleFactory;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.Repairer;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.RepairerCallback;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.w3c.dom.Node;

/**
 *
 * @author sviro
 */
@ServiceProvider(service = Repairer.class)
public class RepairerImpl implements Repairer {

  private static final Logger LOG = Logger.getLogger(RepairerImpl.class);
  private int newValueID = 0;

  @Override
  public void start(InitialModel model, RepairerCallback callback) throws InterruptedException {
    List<RXMLTree> result = new ArrayList<RXMLTree>();

    List<FD> functionalDependencies = model.getFunctionalDependencies();
    for (RXMLTree rXMLTree : model.getTrees()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      if (rXMLTree.isFDDefinedForTree(functionalDependencies)) {
        RXMLTree repairedTree = repairRXMLTree(rXMLTree, functionalDependencies);
        if (repairedTree != null) {
          result.add(repairedTree);
        }
      } else {
        LOG.error("Some of the functional dependencies is not defined for XML tree.");
      }
    }
    callback.finished(result);
    return;
  }

  private RXMLTree repairRXMLTree(RXMLTree rXMLTree, List<FD> functionalDependencies) throws InterruptedException {
    List<RepairImpl> repairs = new ArrayList<RepairImpl>();
    for (FD fd : functionalDependencies) {
      LOG.info("Start checking inconsistency of FD: " + fd);
      if (!rXMLTree.isSatisfyingFD(fd)) {
        LOG.info("XML is inconsistent to FD.");
        List<Pair<Tuple, Tuple>> tuplePairNotSatisfyingFD = TupleFactory.getTuplePairNotSatisfyingFD(rXMLTree, fd);
        for (Pair<Tuple, Tuple> tuplePair : tuplePairNotSatisfyingFD) {
          if (Thread.interrupted()) {
            throw new InterruptedException();
          }
          repairs.addAll(computeRepairs(rXMLTree, tuplePair, fd));
        }
      }
    }

    if (!repairs.isEmpty()) {
      List<RepairImpl> minimalRepairs = removeNonMinimalRepairs(rXMLTree, repairs);
      RepairImpl repair = mergeRepairs(minimalRepairs);

      rXMLTree.applyRepair(repair);
    }

    return rXMLTree;
  }

  private List<RepairImpl> computeRepairs(RXMLTree tree, Pair<Tuple, Tuple> tuplePair, FD fd) {
    LOG.debug("Starting computing repairs.");
    List<RepairImpl> result = new ArrayList<RepairImpl>();

    Path rightPath = fd.getRightSidePaths().getPathObj();
    PathAnswer t1Answer = tree.getPathAnswerForTuple(rightPath, tuplePair.getFirst(), false);
    PathAnswer t2Answer = tree.getPathAnswerForTuple(rightPath, tuplePair.getSecond(), false);

    if (rightPath.isStringPath()) {
      if (!t1Answer.isEmpty() && !t2Answer.isEmpty()) {
        result.add(new RepairImpl(t1Answer.getTupleNodeAnswer(), t2Answer.getTupleValueAnswer()));
      } else if (!t1Answer.isEmpty()) {
        result.add(new RepairImpl(t1Answer.getTupleNodeAnswer(), t2Answer.getTupleValueAnswer()));
      } else if (!t2Answer.isEmpty()) {
        result.add(new RepairImpl(t2Answer.getTupleNodeAnswer(), t1Answer.getTupleValueAnswer()));
      }
    } else {
      if (!t1Answer.isEmpty() && !t2Answer.isEmpty()) {
        result.add(new RepairImpl(t1Answer.getTupleNodeAnswer()));
      } else if (!t1Answer.isEmpty()) {
        result.add(new RepairImpl(t1Answer.getTupleNodeAnswer()));
      } else if (!t2Answer.isEmpty()) {
        result.add(new RepairImpl(t2Answer.getTupleNodeAnswer()));
      }
    }

    for (Path path : fd.getLeftSidePaths().getPaths()) {
      t1Answer = tree.getPathAnswerForTuple(path, tuplePair.getFirst(), false);
      t2Answer = tree.getPathAnswerForTuple(path, tuplePair.getSecond(), false);

      if (path.isStringPath()) {
        result.add(new RepairImpl(t1Answer.getTupleNodeAnswer(), getNewValue()));
//        result.add(new Repair(t2Answer.getTupleNodeAnswer(), getNewValue()));
      } else {
        result.add(new RepairImpl(t1Answer.getTupleNodeAnswer()));
//        result.add(new Repair(t2Answer.getTupleNodeAnswer()));
      }
    }
    LOG.debug("Repairs have been created.");
    return result;
  }

  private List<RepairImpl> removeNonMinimalRepairs(RXMLTree rXMLTree, List<RepairImpl> repairs) throws InterruptedException {
    LOG.debug("Starting removing non minimal repairs.");
    List<RepairImpl> result = new ArrayList<RepairImpl>();

    while (!repairs.isEmpty()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      RepairImpl repair = repairs.get(0);

      List<RepairImpl> minRepairs = getMinimalRepairs(repairs, repair);
      result.addAll(minRepairs);
    }

    LOG.debug("Non minimal repairs have been removed.");
    return result;
  }

  private RepairImpl mergeRepairs(List<RepairImpl> minimalRepairs) throws InterruptedException {
    LOG.debug("Starting merging repairs.");
    RepairImpl result = new RepairImpl();

    //value merge
    for (RepairImpl repair : minimalRepairs) {
      for (Node node : repair.getValueNodes().keySet()) {
        if (!result.getValueNodes().containsKey(node)) {
          String value = repair.getValueNodes().get(node);
          List<String> nodeValues = getValuesFromRepairs(minimalRepairs, node);
          if (nodeValues.size() == 1 || allValuesEquals(nodeValues, value)) {
            result.addValueNode(node, value);
          }
        }
      }
    }

    //reliability merge
    List<RepairImpl> reliabilityRepairs = RepairFactory.getReliabilityRepairs(minimalRepairs);
    for (RepairImpl repair : reliabilityRepairs) {
      result.addUnreliableNodes(repair.getUnreliableNodes());
    }

    List<Pair<RepairImpl, RepairImpl>> valuePairs = RepairFactory.getValuePairs(minimalRepairs);
    for (Pair<RepairImpl, RepairImpl> valuePair : valuePairs) {
      RepairImpl firstRepair = valuePair.getFirst();
      RepairImpl secondRepair = valuePair.getSecond();
      addUnreliableNodesFromValues(firstRepair, secondRepair, result);
      addUnreliableNodesFromValues(secondRepair, firstRepair, result);
    }

    LOG.debug("Repairs have been merged.");
    return result;
  }

  private void addUnreliableNodesFromValues(RepairImpl firstRepair, RepairImpl secondRepair, RepairImpl result) {
    for (Node node : firstRepair.getValueNodes().keySet()) {
      if (!result.getUnreliableNodes().contains(node)) {
        String firstValue = firstRepair.getValueNodes().get(node);
        if (secondRepair.getValueNodes().containsKey(node) && firstValue.equals(secondRepair.getValueNodes().get(node))) {
          result.addUnreliableNode(node);
        }
      }
    }
  }

  private String getNewValue() {
    return "newValue" + newValueID++;
  }

  private List<String> getValuesFromRepairs(List<RepairImpl> minimalRepairs, Node node) {
    List<String> result = new ArrayList<String>();

    for (RepairImpl repair : minimalRepairs) {
      if (repair.getValueNodes().containsKey(node)) {
        result.add(repair.getValueNodes().get(node));
      }
    }

    return result;
  }

  private boolean allValuesEquals(List<String> nodeValues, String value) {
    for (String nodeValue : nodeValues) {
      if (!value.equals(nodeValue)) {
        return false;
      }
    }

    return true;
  }

  private List<RepairImpl> getMinimalRepairs(List<RepairImpl> repairs, RepairImpl repair) {
    List<RepairImpl> result = new ArrayList<RepairImpl>();
    Set<RepairImpl> repairsToRemove = new HashSet<RepairImpl>();

    result.add(repair);

    for (RepairImpl repair1 : repairs) {
      int compared = repair1.compareTo(result.get(0));
      if (RepairImpl.COMPARE_EQUAL == compared) {
        result.add(repair1);
      } else if (RepairImpl.COMPARE_SMALLER ==  compared) {
        repairsToRemove.addAll(result);
        result.clear();
        result.add(repair1);
      } else if (RepairImpl.COMPARE_GREATER == compared) {
        repairsToRemove.add(repair1);
      }
    }

    repairsToRemove.addAll(result);
    repairs.removeAll(repairsToRemove);

    return result;
  }

  @Override
  public String getName() {
    return "old_repairer";
  }

  @Override
  public String getDisplayName() {
    return "Generic repairer";
  }

  @Override
  public String getModuleDescription() {
    return "This repairer is implemented from the paper.";
  }
}
