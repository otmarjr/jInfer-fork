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
package cz.cuni.mff.ksi.jinfer.attrstats.experiments.quality;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.Quality;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.QualityMeasurement;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class Weight implements QualityMeasurement {

  private final double alpha;
  private final double beta;

  public Weight(final double alpha, final double beta) {
    this.alpha = alpha;
    this.beta = beta;
  }

  @Override
  public Quality measure(final AMModel model, final IdSet solution) {
    return new Quality() {

      @Override
      public double getScalar() {
        return model.weight(solution.getMappings(), alpha, beta);
      }

      @Override
      public boolean isOptimal() {
        return solution.isOptimal();
      }

      @Override
      public String getText() {
        return String.valueOf(getScalar()) + " (" + solution.getMappings().size() + " AMs)";
      }
    };
  }

}
