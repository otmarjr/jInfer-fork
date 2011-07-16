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
package cz.cuni.mff.ksi.jinfer.functionalDependencies.newRepairer;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.InitialModel;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.Path;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.PathAnswer;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.RXMLTree;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.Tuple;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.TupleFactory;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.RepairPicker;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.Repairer;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.RepairerCallback;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.properties.RepairerPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.repairer.Repair;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.repairer.RepairFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.w3c.dom.Node;

/**
 *
 * @author sviro
 */
@ServiceProvider(service = Repairer.class)
public class NewRepairerImpl implements Repairer {

  private static final Logger LOG = Logger.getLogger(NewRepairerImpl.class);
  private int newValueID = 0;
  private RepairPicker repairPicker = null;

  @Override
  public void start(InitialModel model, RepairerCallback callback) throws InterruptedException {
    LOG.info("This is NEW repairer");
    List<RXMLTree> result = new ArrayList<RXMLTree>();

    final Properties prop = RunningProject.getActiveProjectProps(RepairerPropertiesPanel.NAME);
    double coeffK = Double.parseDouble(prop.getProperty(RepairerPropertiesPanel.COEFF_K_PROP, RepairerPropertiesPanel.COEFF_K_DEFAULT));
    double thresholdT = getThreshold(prop);

    List<FD> functionalDependencies = model.getFunctionalDependencies();
    for (RXMLTree rXMLTree : model.getTrees()) {
      if (rXMLTree.isFDDefinedForTree(functionalDependencies)) {
        rXMLTree.setThresholdT(thresholdT);
        RXMLTree repairedTree = repairRXMLTree(rXMLTree, functionalDependencies, coeffK);
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

  private RXMLTree repairRXMLTree(RXMLTree rXMLTree, List<FD> functionalDependencies, final double coeffK) throws InterruptedException {
    while (rXMLTree.isInconsistent(functionalDependencies)) {
      repairRXMLTree2(rXMLTree, functionalDependencies, coeffK);
    }

    return rXMLTree;
  }

  private void repairRXMLTree2(RXMLTree rXMLTree, List<FD> functionalDependencies, final double coeffK) throws InterruptedException {
    List<Repair> repairs = new ArrayList<Repair>();
    for (FD fd : functionalDependencies) {
      if (!rXMLTree.isSatisfyingFDThesis(fd)) {
        LOG.debug("XML is inconsistent to FD " + fd.toString());
        List<Pair<Tuple, Tuple>> tuplePairNotSatisfyingFD = TupleFactory.getTuplePairNotSatisfyingFDThesis(rXMLTree, fd);
        for (Pair<Tuple, Tuple> tuplePair : tuplePairNotSatisfyingFD) {
          repairs.addAll(computeRepairs(rXMLTree, tuplePair, fd, coeffK));
        }
      }
    }

    if (!repairs.isEmpty()) {
      Repair minimalRepair = getRepairFromPicker(rXMLTree);

      rXMLTree.applyRepair(minimalRepair);
      rXMLTree.clearRepairs(minimalRepair);
    }
  }

  private Repair getRepairFromPicker(final RXMLTree tree) throws InterruptedException {
    if (repairPicker == null) {
      final Properties prop = RunningProject.getActiveProjectProps(RepairerPropertiesPanel.NAME);
      repairPicker = ModuleSelectionHelper.lookupImpl(RepairPicker.class, prop.getProperty(RepairerPropertiesPanel.REPAIR_PICKER_PROP, RepairerPropertiesPanel.REPAIR_PICKER_DEFAULT));
    }
    return repairPicker.getRepair(tree);
  }

  private Collection<Repair> computeRepairs(RXMLTree tree, Pair<Tuple, Tuple> tuplePair, FD fd, final double coeffK) {
    Set<Repair> result = new HashSet<Repair>();
    RepairGroup repairGroup = new RepairGroup(fd);

    Path rightPath = fd.getRightSidePaths().getPathObj();
    PathAnswer t1Answer = tree.getPathAnswerForTuple(rightPath, tuplePair.getFirst(), false);
    PathAnswer t2Answer = tree.getPathAnswerForTuple(rightPath, tuplePair.getSecond(), false);

    if (rightPath.isStringPath()) {
      if (!t1Answer.isEmpty()) {
        result.add(new Repair(t1Answer.getTupleNodeAnswer(), t2Answer.getTupleValueAnswer(), tree, coeffK, rightPath.getPathValue()));
      }
      if (!t2Answer.isEmpty()) {
        result.add(new Repair(t2Answer.getTupleNodeAnswer(), t1Answer.getTupleValueAnswer(), tree, coeffK, rightPath.getPathValue()));
      }
    } else {
      if (!t1Answer.isEmpty()) {
        result.add(new Repair(t1Answer.getTupleNodeAnswer(), tree, coeffK, rightPath.getPathValue()));
      }
      if (!t2Answer.isEmpty()) {
        result.add(new Repair(t2Answer.getTupleNodeAnswer(), tree, coeffK, rightPath.getPathValue()));
      }
    }

    for (Path path : fd.getLeftSidePaths().getPaths()) {
      t1Answer = tree.getPathAnswerForTuple(path, tuplePair.getFirst(), false);
      t2Answer = tree.getPathAnswerForTuple(path, tuplePair.getSecond(), false);

      if (path.isStringPath()) {
        result.add(new Repair(t1Answer.getTupleNodeAnswer(), getNewValue(), tree, coeffK, path.getPathValue()));
        result.add(new Repair(t2Answer.getTupleNodeAnswer(), getNewValue(), tree, coeffK, path.getPathValue()));
      } else {
        result.add(new Repair(t1Answer.getTupleNodeAnswer(), tree, coeffK, path.getPathValue()));
        result.add(new Repair(t2Answer.getTupleNodeAnswer(), tree, coeffK, path.getPathValue()));
      }
    }

    repairGroup.addRepairs(result);
    tree.addRepairGroup(repairGroup);

    return result;
  }

  private List<Repair> removeNonMinimalRepairs(RXMLTree rXMLTree, List<Repair> repairs) {
    List<Repair> result = new ArrayList<Repair>();

    while (!repairs.isEmpty()) {
      Repair repair = repairs.get(0);

      List<Repair> minRepairs = getMinimalRepairs(repairs, repair);
      result.addAll(minRepairs);
    }

    return result;
  }

  private Repair mergeRepairs(List<Repair> minimalRepairs) {
    Repair result = new Repair();

    //value merge
    for (Repair repair : minimalRepairs) {
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
    List<Repair> reliabilityRepairs = RepairFactory.getReliabilityRepairs(minimalRepairs);
    for (Repair repair : reliabilityRepairs) {
      result.addUnreliableNodes(repair.getUnreliableNodes());
    }

    List<Pair<Repair, Repair>> valuePairs = RepairFactory.getValuePairs(minimalRepairs);
    for (Pair<Repair, Repair> valuePair : valuePairs) {
      Repair firstRepair = valuePair.getFirst();
      Repair secondRepair = valuePair.getSecond();
      addUnreliableNodesFromValues(firstRepair, secondRepair, result);
      addUnreliableNodesFromValues(secondRepair, firstRepair, result);
    }

    return result;
  }

  private void addUnreliableNodesFromValues(Repair firstRepair, Repair secondRepair, Repair result) {
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

  private List<String> getValuesFromRepairs(List<Repair> minimalRepairs, Node node) {
    List<String> result = new ArrayList<String>();

    for (Repair repair : minimalRepairs) {
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

  private List<Repair> getMinimalRepairs(List<Repair> repairs, Repair repair) {
    List<Repair> result = new ArrayList<Repair>();
    Set<Repair> repairsToRemove = new HashSet<Repair>();

    result.add(repair);

    for (Repair repair1 : repairs) {
      if (Repair.COMPARE_EQUAL == repair1.compareTo(result.get(0))) {
        result.add(repair1);
      } else if (Repair.COMPARE_SMALLER == repair1.compareTo(result.get(0))) {
        repairsToRemove.addAll(result);
        result.clear();
        result.add(repair1);
      } else if (Repair.COMPARE_GREATER == repair1.compareTo(result.get(0))) {
        repairsToRemove.add(repair1);
      }
    }

    repairsToRemove.addAll(result);
    repairs.removeAll(repairsToRemove);

    return result;
  }

  @Override
  public String getName() {
    return "new_repairer";
  }

  @Override
  public String getDisplayName() {
    return "Thesis repairer";
  }

  @Override
  public String getModuleDescription() {
    return "This repairer is an implementation from thesis.";
  }

  private static double getThreshold(Properties prop) {
    double result = Double.parseDouble(prop.getProperty(RepairerPropertiesPanel.THRESHOLD_T_PROP, RepairerPropertiesPanel.THRESHOLD_T_DEFAULT));
    if (!(result > 0) || !(result <= 1)) {
      LOG.warn("The value " + result + " of the threshold t is out of range (0,1], will be used default value 1.");
      result = Double.parseDouble(RepairerPropertiesPanel.THRESHOLD_T_DEFAULT);
    }

    return result;
  }
}