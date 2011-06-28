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
package cz.cuni.mff.ksi.jinfer.attrstats.experiments;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ConstructionHeuristic;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ExperimentListener;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.Quality;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.QualityMeasurement;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.TerminationCriterion;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.IGGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class Experiment implements IGGeneratorCallback {

  // TODO vektor Known optimum
  // what does it depend on? Input file, alpha, beta

  private final InputFile file;
  /** Number of vertices and edges in the graph representation of this file. */
  private Pair<Integer, Integer> graphRepresentation;

  private final int poolSize;

  private final double alpha;
  private final double beta;

  private final ConstructionHeuristic constructionHeuristic;
  /**
   * List of improvement heuristics to be run in a loop until the termination
   * criterion is met.
   */
  private final List<ImprovementHeuristic> improvementHeuristics;

  private final QualityMeasurement measurement;

  private final TerminationCriterion terminationCriterion;

  /** Time of the experiment start, absolute, in ms. */
  private long startTime;
  /** Total time of the experiment run, in ms. */
  private long totalTime;
  private Double knownOptimum;
  private Quality highestQuality = Quality.ZERO;
  private Quality finalQuality;
  private Pair<Boolean, String> terminationReason;
  private AMModel model;

  /** Result of the construction heuristic run. */
  private HeuristicResult constructionResult;
  /** List of improvement heuristic runs results. */
  private final List<HeuristicResult> improvementResults = new ArrayList<HeuristicResult>();

  private final List<ExperimentListener> listeners = new ArrayList<ExperimentListener>();

  /**
   * TODO vektor Comment!
   *
   * @param fileName
   * @param constructionHeuristic
   * @param improvementHeuristics
   * @param measurement
   * @param terminationCriterion
   */
  public Experiment(final InputFile file,
          final int poolSize,
          final double alpha,
          final double beta,
          final ConstructionHeuristic constructionHeuristic,
          final List<ImprovementHeuristic> improvementHeuristics,
          final QualityMeasurement measurement,
          final TerminationCriterion terminationCriterion) {
    this.file = file;
    this.poolSize = poolSize;
    this.alpha = alpha;
    this.beta = beta;
    this.constructionHeuristic = constructionHeuristic;
    this.improvementHeuristics = new ArrayList<ImprovementHeuristic>(improvementHeuristics);
    this.measurement = measurement;
    this.terminationCriterion = terminationCriterion;
  }

  /**
   * Returns a string report of this whole experiment, its setting and results.
   *
   * @return String report of this experiment.
   */
  public String getReport() {
    final StringBuilder ret = new StringBuilder();
    ret.append(SystemInfo.getInfo())
        .append("\n\nConfiguration:")
        .append("\nFile name: ").append(file.getName()).append(" (").append(file.getSize()).append(" b)")
        .append("\nalpha: ").append(alpha).append(", beta: ").append(beta)
        .append("\n\nResults:")
        .append("\nStart time: ").append(new Date(startTime))
        .append("\nTotal time spent: ").append(totalTime).append(" ms")
        .append("\nFinal quality: ").append(finalQuality.getText())
        .append("\nHighest quality: ").append(highestQuality.getText())
        .append("\nConstruction phase: ")
        .append("\n  Algorithm: ").append(constructionHeuristic.getModuleDescription())
        .append("\n    Time taken: ").append(constructionResult.getTime()).append(" ms")
        .append("\n    Pool size: ").append(constructionResult.getPoolSize())
        .append("\n    Quality: ").append(constructionResult.getQuality().getText())
        .append("\nImprovement phase: ");

    int i = 0;
    for (final HeuristicResult result : improvementResults) {
      ret.append("\n  pass #").append(i + 1).append(": ")
        .append("\n  Algorithm: ").append(improvementHeuristics.get(i % improvementHeuristics.size()).getModuleDescription())
        .append("\n    Time taken: ").append(result.getTime()).append(" ms")
        .append("\n    Time since start: ").append(result.getTotalTime()).append(" ms")
        .append("\n    Pool size: ").append(result.getPoolSize())
        .append("\n    Quality: ").append(result.getQuality().getText());
      i++;
    }

    ret.append("\nTermination reason: ").append(terminationReason.getSecond());

    return ret.toString();
  }

  /**
   * TODO vektor Comment!
   *
   * @param el v
   */
  public void addListener(final ExperimentListener el) {
    listeners.add(el);
  }

  /**
   * TODO vektor Comment!
   *
   * @throws InterruptedException
   */
  public void run() throws InterruptedException {
    // get IG from the file
    final IGGenerator igg = ModuleSelectionHelper.lookupImpl(IGGenerator.class, "Basic_IG_Generator");
    final Input input = new Input();
    input.getDocuments().add(file.getFile());
    igg.start(input, this);
  }

  @Override
  public void finished(final List<Element> grammar) {
    // create the model
    model = new AMModel(grammar);

    // take the time before CH
    startTime = time();

    try {
      // run the CH
      constructionHeuristic.start(Experiment.this, new HeuristicCallback() {

        @Override
        public void finished(final List<IdSet> feasiblePool) {
          // take the time after CH
          final long constructionTime = delta(startTime);
          // get the incumbent solution and its quality
          final Pair<IdSet, Quality> incumbent = ExperimentalUtils.getBest(Experiment.this, feasiblePool);
          if (incumbent.getSecond().getScalar() >= highestQuality.getScalar()) {
            highestQuality = incumbent.getSecond();
          }
          constructionResult = new HeuristicResult(constructionTime, constructionTime, feasiblePool.size(), incumbent.getSecond());

          // while not termination criterion
          // TODO vektor If optimum found, stop here
          final long totTime = delta(startTime);
          final Pair<Boolean, String> termination = terminationCriterion.terminate(Experiment.this, totTime, feasiblePool);
          if (termination.getFirst().booleanValue()) {
            terminationReason = termination;
            notifyFinished(totTime, incumbent.getSecond());
            return;
          }

          runImprovement(feasiblePool, 0);
        }
      });
    } catch (final InterruptedException e) {
    }
  }

  private void runImprovement(final List<IdSet> feasiblePool, final int iteration) {
    final ImprovementHeuristic current = improvementHeuristics.get(iteration % improvementHeuristics.size());
    //   take the time before IH
    final long ihStartTime = time();
    //   run the IH
    try {
      current.start(Experiment.this, feasiblePool, new HeuristicCallback() {

        @Override
        public void finished(final List<IdSet> feasiblePool) {
          //   take the time after IH
          final long improvementTime = delta(ihStartTime);
          final long totalTime = delta(startTime);
          // get the incumbent solution and its quality
          final Pair<IdSet, Quality> incumbent = ExperimentalUtils.getBest(Experiment.this, feasiblePool);
          if (incumbent.getSecond().getScalar() >= highestQuality.getScalar()) {
            highestQuality = incumbent.getSecond();
          }
          improvementResults.add(new HeuristicResult(improvementTime, totalTime, feasiblePool.size(), incumbent.getSecond()));

          // while not termination criterion
          final long totTime = delta(startTime);
          final Pair<Boolean, String> termination = terminationCriterion.terminate(Experiment.this, totTime, feasiblePool);
          if (termination.getFirst().booleanValue()) {
            terminationReason = termination;
            notifyFinished(totTime, incumbent.getSecond());
            return;
          }

          runImprovement(feasiblePool, iteration + 1);
        }
      });
    } catch (final InterruptedException e) {
    }
  }

  private void notifyFinished(final long totalTime, final Quality finalQuality) {
    this.totalTime = totalTime;
    this.finalQuality = finalQuality;
    for (final ExperimentListener el : listeners) {
      el.experimentFinished(this);
    }
  }

  // --- TIMING ----------------------------------------------------------------

  private static long time() {
    return System.nanoTime() / 1000000;
  }

  private static long delta(final long from) {
    return time() - from;
  }

  // --- GETTERS / SETTERS -----------------------------------------------------

  public int getPoolSize() {
    return poolSize;
  }

  public AMModel getModel() {
    return model;
  }

  public double getAlpha() {
    return alpha;
  }
  public double getBeta() {
    return beta;
  }

  public QualityMeasurement getQualityMeasurement() {
    return measurement;
  }

  public Double getKnownOptimum() {
    return knownOptimum;
  }

  public void setKnownOptimum(Double knownOptimum) {
    this.knownOptimum = knownOptimum;
  }
}
