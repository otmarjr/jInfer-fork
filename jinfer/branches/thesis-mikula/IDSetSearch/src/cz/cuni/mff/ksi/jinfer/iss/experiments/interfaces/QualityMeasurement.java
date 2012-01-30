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
package cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces;

import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Quality;
import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;

/**
 * Interface for measuring quality of a certain solution ({@link IdSet})
 * in the context of an experiment.
 *
 * @author vektor
 */
public interface QualityMeasurement {

  /**
   * Returns the quality of the provided solution in the context of the provided
   * experiment.
   *
   * @param experiment Experiment in context of which to measure the quality.
   * @param solution Solution for which the quality should be calculated.
   * @return Quality of the solution in the context of the experiment.
   */
  Quality measure(final Experiment experiment, final IdSet solution);

}
