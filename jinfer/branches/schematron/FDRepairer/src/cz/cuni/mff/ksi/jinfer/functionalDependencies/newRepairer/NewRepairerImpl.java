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
import cz.cuni.mff.ksi.jinfer.functionalDependencies.RepairStatistics;
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
 * Implementation of the Repairer module for the thesis algorithm.
 * 
 * @author sviro
 */
@ServiceProvider(service = Repairer.class)
public class NewRepairerImpl implements Repairer {
  
  public static final String NAME = "new_repairer";

  private static final String NEW_VALUE = "newValue";
  private static final Logger LOG = Logger.getLogger(NewRepairerImpl.class);
  private RepairPicker repairPicker = null;

  @Override
  public void start(final InitialModel model, final RepairerCallback callback) throws InterruptedException {
    LOG.info("This is NEW repairer");
    final List<RXMLTree> result = new ArrayList<RXMLTree>();

    final Properties prop = RunningProject.getActiveProjectProps(RepairerPropertiesPanel.NAME);
    final double coeffK = Double.parseDouble(prop.getProperty(RepairerPropertiesPanel.COEFF_K_PROP, RepairerPropertiesPanel.COEFF_K_DEFAULT));
    final double thresholdT = getThreshold(prop);
    repairPicker = ModuleSelectionHelper.lookupImpl(RepairPicker.class, prop.getProperty(RepairerPropertiesPanel.REPAIR_PICKER_PROP, RepairerPropertiesPanel.REPAIR_PICKER_DEFAULT));

    final List<FD> functionalDependencies = model.getFunctionalDependencies();
    for (RXMLTree rXMLTree : model.getTrees()) {
      if (rXMLTree.isFDDefinedForTree(functionalDependencies)) {
        rXMLTree.setThresholdT(thresholdT);
        final RXMLTree repairedTree = repairRXMLTree(rXMLTree, functionalDependencies, coeffK);
        if (repairedTree != null) {
          result.add(repairedTree);
        }
      } else {
        LOG.error("Some of the functional dependencies is not defined for XML tree.");
      }
    }
    callback.finished(result);
  }

  private RXMLTree repairRXMLTree(final RXMLTree rXMLTree, final List<FD> functionalDependencies, final double coeffK) throws InterruptedException {
    final RepairStatistics repairStats = new RepairStatistics();

    while (rXMLTree.isInconsistent(functionalDependencies)) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      repairRXMLTree2(rXMLTree, functionalDependencies, coeffK, repairStats);
    }


    printStatistics(repairStats);
    return rXMLTree;
  }

  private void repairRXMLTree2(final RXMLTree rXMLTree, final List<FD> functionalDependencies, final double coeffK, final RepairStatistics repairStats) throws InterruptedException {
    final List<RepairCandidate> repairs = new ArrayList<RepairCandidate>();
    for (FD fd : functionalDependencies) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      if (!rXMLTree.isSatisfyingFDThesis(fd)) {
        LOG.debug("XML is inconsistent to FD " + fd.toString());
        final List<Pair<Tuple, Tuple>> tuplePairNotSatisfyingFD = TupleFactory.getTuplePairNotSatisfyingFDThesis(rXMLTree, fd);
        for (Pair<Tuple, Tuple> tuplePair : tuplePairNotSatisfyingFD) {
          repairs.addAll(computeRepairs(rXMLTree, tuplePair, fd, coeffK));
        }
      }
    }

    if (!repairs.isEmpty()) {

      if (repairPicker instanceof RepairPickerImpl) {
        while (!rXMLTree.isRGEmpty()) {
          final RepairCandidate minimalRepair = getRepairFromPicker(rXMLTree);
          rXMLTree.invalidateSidePathAnswers(minimalRepair.getTuplePair().getFirst());
          rXMLTree.invalidateSidePathAnswers(minimalRepair.getTuplePair().getSecond());
          if (!rXMLTree.isTuplePairSatisfyingFDThesis(minimalRepair.getTuplePair(), minimalRepair.getFD())) {
            repairStats.setRepairGroup(rXMLTree.getRepairGroups().size());
            repairStats.collectData(minimalRepair);
            rXMLTree.applyRepair(minimalRepair);
            rXMLTree.clearRepairs(minimalRepair, false);
          }
        }
      } else {
        repairStats.setRepairGroup(rXMLTree.getRepairGroups().size());
        LOG.debug("Repair groups: " + rXMLTree.getRepairGroups().size());
        final RepairCandidate minimalRepair = getRepairFromPicker(rXMLTree);
        repairStats.collectData(minimalRepair);

        rXMLTree.applyRepair(minimalRepair);
        rXMLTree.clearRepairs(minimalRepair, true);
      }
    }
  }

  private RepairCandidate getRepairFromPicker(final RXMLTree tree) throws InterruptedException {
    return repairPicker.getRepair(tree);
  }

  private Collection<RepairCandidate> computeRepairs(final RXMLTree tree, final Pair<Tuple, Tuple> tuplePair, final FD fd, final double coeffK) {
    final Set<RepairCandidate> result = new HashSet<RepairCandidate>();
    final RepairGroup repairGroup = new RepairGroup(fd);

    final Path rightPath = fd.getRightSidePaths().getPathObj();
    PathAnswer t1Answer = tree.getPathAnswerForTuple(rightPath, tuplePair.getFirst(), false);
    PathAnswer t2Answer = tree.getPathAnswerForTuple(rightPath, tuplePair.getSecond(), false);

    if (rightPath.isStringPath()) {
      if (!t1Answer.isEmpty()) {
        result.add(new RepairCandidate(tuplePair, t1Answer.getTupleNodeAnswer(), t2Answer.getTupleValueAnswer(), tree, coeffK, rightPath.getPathValue(), false));
      }
      if (!t2Answer.isEmpty()) {
        result.add(new RepairCandidate(tuplePair, t2Answer.getTupleNodeAnswer(), t1Answer.getTupleValueAnswer(), tree, coeffK, rightPath.getPathValue(), false));
      }
    } else {
      if (!t1Answer.isEmpty()) {
        result.add(new RepairCandidate(tuplePair, t1Answer.getTupleNodeAnswer(), tree, coeffK, rightPath.getPathValue()));
      }
      if (!t2Answer.isEmpty()) {
        result.add(new RepairCandidate(tuplePair, t2Answer.getTupleNodeAnswer(), tree, coeffK, rightPath.getPathValue()));
      }
    }

    for (Path path : fd.getLeftSidePaths().getPaths()) {
      t1Answer = tree.getPathAnswerForTuple(path, tuplePair.getFirst(), false);
      t2Answer = tree.getPathAnswerForTuple(path, tuplePair.getSecond(), false);

      if (path.isStringPath()) {
        result.add(new RepairCandidate(tuplePair, t1Answer.getTupleNodeAnswer(), NEW_VALUE, tree, coeffK, path.getPathValue(), true));
        result.add(new RepairCandidate(tuplePair, t2Answer.getTupleNodeAnswer(), NEW_VALUE, tree, coeffK, path.getPathValue(), true));
      } else {
        result.add(new RepairCandidate(tuplePair, t1Answer.getTupleNodeAnswer(), tree, coeffK, path.getPathValue()));
        result.add(new RepairCandidate(tuplePair, t2Answer.getTupleNodeAnswer(), tree, coeffK, path.getPathValue()));
      }
    }

    repairGroup.addRepairs(result);
    tree.addRepairGroup(repairGroup);

    return result;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getDisplayName() {
    return "Thesis repairer";
  }

  @Override
  public String getModuleDescription() {
    return "This repairer is an implementation from thesis.";
  }

  private static double getThreshold(final Properties prop) {
    double result = Double.parseDouble(prop.getProperty(RepairerPropertiesPanel.THRESHOLD_T_PROP, RepairerPropertiesPanel.THRESHOLD_T_DEFAULT));
    if (!(result > 0) || !(result <= 1)) {
      LOG.warn("The value " + result + " of the threshold t is out of range (0,1], will be used default value 1.");
      result = Double.parseDouble(RepairerPropertiesPanel.THRESHOLD_T_DEFAULT);
    }

    return result;
  }

  private void printStatistics(final RepairStatistics repairStats) {
    LOG.info("Repair Statistics:");

    LOG.info("Starting RG #: " + repairStats.getStartingRG());
    LOG.info("Repaired RG #: " + repairStats.getRGCount());

    LOG.info("Unreliable nodes #: " + repairStats.getUnreliableCount());
    LOG.info("Value nodes - new values #: " + repairStats.getNewValueCount());
    LOG.info("Value nodes - changed values #: " + repairStats.getValueChangesCount());

    LOG.info("---------- End of Statistics. ----------");
  }
}
