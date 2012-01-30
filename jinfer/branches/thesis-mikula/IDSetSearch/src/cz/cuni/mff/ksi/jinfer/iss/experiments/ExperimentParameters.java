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
package cz.cuni.mff.ksi.jinfer.iss.experiments;

import cz.cuni.mff.ksi.jinfer.iss.experiments.data.InputFile;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ConstructionHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.QualityMeasurement;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.TerminationCriterion;
import java.util.List;

/**
 * Class encapsulating all the variable parameters of the experiment.
 *
 * @author vektor
 */
public class ExperimentParameters {

  private final InputFile file;
  private final int poolSize;
  private final double alpha;
  private final double beta;
  private final Double knownOptimum;
  private final ConstructionHeuristic constructionHeuristic;
  private final List<ImprovementHeuristic> improvementHeuristics;
  private final QualityMeasurement measurement;
  private final TerminationCriterion terminationCriterion;

  /**
   * Almost full constructor.
   *
   * @param file File to run the experiment on.
   * @param poolSize Requested size of the pool from construction heuristic.
   * @param alpha Weight parameter alpha.
   * @param beta Weight parameter beta.
   * @param constructionHeuristic Requested construction heuristic.
   * @param improvementHeuristics List of requested improvement heuristics. They
   * will be run in a loop until the termination criterion is met.
   * @param measurement Something to measure the quality of the solutions.
   * @param terminationCriterion Termination criterion. Experiment will stop
   * running as soon as this tells it to.
   */
  public ExperimentParameters(final InputFile file, final int poolSize,
          final double alpha, final double beta,
          final ConstructionHeuristic constructionHeuristic,
          final List<ImprovementHeuristic> improvementHeuristics,
          final QualityMeasurement measurement,
          final TerminationCriterion terminationCriterion) {
    this(file, poolSize, alpha, beta, null, constructionHeuristic,
            improvementHeuristics, measurement, terminationCriterion);
  }

  /**
   * Full constructor.
   *
   * @param file File to run the experiment on.
   * @param poolSize Requested size of the pool from construction heuristic.
   * @param alpha Weight parameter alpha.
   * @param beta Weight parameter beta.
   * @param knownOptimum Known optimum or <code>null</code> if unavailable.
   * @param constructionHeuristic Requested construction heuristic.
   * @param improvementHeuristics List of requested improvement heuristics. They
   * will be run in a loop until the termination criterion is met.
   * @param measurement Something to measure the quality of the solutions.
   * @param terminationCriterion Termination criterion. Experiment will stop
   * running as soon as this tells it to.
   */
  public ExperimentParameters(final InputFile file, final int poolSize,
          final double alpha, final double beta,
          final Double knownOptimum,
          final ConstructionHeuristic constructionHeuristic,
          final List<ImprovementHeuristic> improvementHeuristics,
          final QualityMeasurement measurement,
          final TerminationCriterion terminationCriterion) {
    super();
    this.file = file;
    this.poolSize = poolSize;
    this.alpha = alpha;
    this.beta = beta;
    this.knownOptimum = knownOptimum;
    this.constructionHeuristic = constructionHeuristic;
    this.improvementHeuristics = improvementHeuristics;
    this.measurement = measurement;
    this.terminationCriterion = terminationCriterion;
  }

  public InputFile getFile() {
    return file;
  }

  public int getPoolSize() {
    return poolSize;
  }

  public double getAlpha() {
    return alpha;
  }

  public double getBeta() {
    return beta;
  }

  public Double getKnownOptimum() {
    return knownOptimum;
  }

  public ConstructionHeuristic getConstructionHeuristic() {
    return constructionHeuristic;
  }

  public List<ImprovementHeuristic> getImprovementHeuristics() {
    return improvementHeuristics;
  }

  public QualityMeasurement getMeasurement() {
    return measurement;
  }

  public TerminationCriterion getTerminationCriterion() {
    return terminationCriterion;
  }

}
