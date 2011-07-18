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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

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
    List<RepairCandidate> repairs = new ArrayList<RepairCandidate>();
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
      RepairCandidate minimalRepair = getRepairFromPicker(rXMLTree);

      rXMLTree.applyRepair(minimalRepair);
      rXMLTree.clearRepairs(minimalRepair);
    }
  }

  private RepairCandidate getRepairFromPicker(final RXMLTree tree) throws InterruptedException {
    if (repairPicker == null) {
      final Properties prop = RunningProject.getActiveProjectProps(RepairerPropertiesPanel.NAME);
      repairPicker = ModuleSelectionHelper.lookupImpl(RepairPicker.class, prop.getProperty(RepairerPropertiesPanel.REPAIR_PICKER_PROP, RepairerPropertiesPanel.REPAIR_PICKER_DEFAULT));
    }
    return repairPicker.getRepair(tree);
  }

  private Collection<RepairCandidate> computeRepairs(RXMLTree tree, Pair<Tuple, Tuple> tuplePair, FD fd, final double coeffK) {
    Set<RepairCandidate> result = new HashSet<RepairCandidate>();
    RepairGroup repairGroup = new RepairGroup(fd);

    Path rightPath = fd.getRightSidePaths().getPathObj();
    PathAnswer t1Answer = tree.getPathAnswerForTuple(rightPath, tuplePair.getFirst(), false);
    PathAnswer t2Answer = tree.getPathAnswerForTuple(rightPath, tuplePair.getSecond(), false);

    if (rightPath.isStringPath()) {
      if (!t1Answer.isEmpty()) {
        result.add(new RepairCandidate(t1Answer.getTupleNodeAnswer(), t2Answer.getTupleValueAnswer(), tree, coeffK, rightPath.getPathValue()));
      }
      if (!t2Answer.isEmpty()) {
        result.add(new RepairCandidate(t2Answer.getTupleNodeAnswer(), t1Answer.getTupleValueAnswer(), tree, coeffK, rightPath.getPathValue()));
      }
    } else {
      if (!t1Answer.isEmpty()) {
        result.add(new RepairCandidate(t1Answer.getTupleNodeAnswer(), tree, coeffK, rightPath.getPathValue()));
      }
      if (!t2Answer.isEmpty()) {
        result.add(new RepairCandidate(t2Answer.getTupleNodeAnswer(), tree, coeffK, rightPath.getPathValue()));
      }
    }

    for (Path path : fd.getLeftSidePaths().getPaths()) {
      t1Answer = tree.getPathAnswerForTuple(path, tuplePair.getFirst(), false);
      t2Answer = tree.getPathAnswerForTuple(path, tuplePair.getSecond(), false);

      if (path.isStringPath()) {
        result.add(new RepairCandidate(t1Answer.getTupleNodeAnswer(), getNewValue(), tree, coeffK, path.getPathValue()));
        result.add(new RepairCandidate(t2Answer.getTupleNodeAnswer(), getNewValue(), tree, coeffK, path.getPathValue()));
      } else {
        result.add(new RepairCandidate(t1Answer.getTupleNodeAnswer(), tree, coeffK, path.getPathValue()));
        result.add(new RepairCandidate(t2Answer.getTupleNodeAnswer(), tree, coeffK, path.getPathValue()));
      }
    }

    repairGroup.addRepairs(result);
    tree.addRepairGroup(repairGroup);

    return result;
  }

  private String getNewValue() {
    return "newValue" + newValueID++;
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
