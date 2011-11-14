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
package cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk;

import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ConstructionHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Construction heuristic - encapsulation of the GLPK approach to finding
 * ID sets.
 *
 * The way to achieve different solutions ({@link IdSet}s) returned by GLPK
 * is via randomizing the order in which AMs appear in its input.
 *
 * @author vektor
 */
public class Glpk implements ConstructionHeuristic {

  private final int timeLimit;

  /**
   * Constructs a new instance of this heuristic with unlimited run time.
   */
  public Glpk() {
    this(0);
  }

  /**
   * Constructs a new instance of this heuristic and sets the time limit.
   *
   * @param timeLimit Time limit (in seconds) of the GLPK run.
   */
  public Glpk(final int timeLimit) {
    this.timeLimit = timeLimit;
  }

  @Override
  public void start(final Experiment experiment,
        final HeuristicCallback callback) throws InterruptedException {
    final List<IdSet> initialSolutions = new ArrayList<IdSet>(experiment.getPoolSize());
    for (int i = 0; i < experiment.getPoolSize(); i++) {
      initialSolutions.add(GlpkOutputParser.getIDSet(GlpkRunner.run(experiment.getModel(), experiment.getAlpha(), experiment.getBeta(), timeLimit), experiment.getModel()));
    }
    callback.finished(initialSolutions);
  }

  @Override
  public String getName() {
    return "GLPK";
  }

  @Override
  public String getDisplayName() {
    return getName();
  }

  @Override
  public String getModuleDescription() {
    return getName() + ", limit = " + timeLimit + " s";
  }

}
