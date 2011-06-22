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
package cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction.fidax;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ConstructionHeuristic;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;
import java.util.Arrays;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class Fidax implements ConstructionHeuristic {

  private final double alpha;
  private final double beta;

  public Fidax(final double alpha, final double beta) {
    this.alpha = alpha;
    this.beta = beta;
  }

  @Override
  public void start(final AMModel model, final int poolSize,
        final HeuristicCallback callback) throws InterruptedException {
    final IdSet solution = FidaxAlgorithm.findIDSet(model, alpha, beta);
    callback.finished(Arrays.asList(solution));
  }

}
