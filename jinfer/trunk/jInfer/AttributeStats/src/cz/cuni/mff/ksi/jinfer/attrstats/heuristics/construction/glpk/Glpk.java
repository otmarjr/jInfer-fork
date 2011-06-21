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
package cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction.glpk;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ConstructionHeuristic;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class Glpk implements ConstructionHeuristic {

  private final double alpha;
  private final double beta;
  private final int timeLimit;

  public Glpk(final double alpha, final double beta, final int timeLimit) {
    this.alpha = alpha;
    this.beta = beta;
    this.timeLimit = timeLimit;
  }

  @Override
  public void start(final AMModel model, final HeuristicCallback callback)
          throws InterruptedException {
    callback.finished(GlpkOutputParser.getIDSet(GlpkRunner.run(model, alpha, beta, timeLimit), model));
  }

}
