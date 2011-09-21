/*
 * Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.iss.experiments.sets;

import cz.cuni.mff.ksi.jinfer.iss.ExperimentAction;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Crossover;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Mutation;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.RandomRemove;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.RemoveWorst;
import java.util.Arrays;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;

/**
 * Experiment testing the performance of Scenario 1: RR, MUT, RR, CX, RW.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class ChainedIHs1 extends AbstractChainedIHs {

  @Override
  public String getName() {
    return "Chained IHs 1";
  }

  @Override
  protected List<ImprovementHeuristic> getIHs() {
    return Arrays.<ImprovementHeuristic>asList(
            new RandomRemove(0.1),
            new Mutation(0.1, 1),
            new RandomRemove(0.1),
            new Crossover(0.1, 1),
            new RemoveWorst());
  }


  @Override
  protected void notifyFinishedAll() {
    ExperimentAction.runExperiment(new ChainedIHs2().getName(), 0);
  }
}
