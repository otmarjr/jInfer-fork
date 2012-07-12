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

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentListener;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Quality;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.QualityMeasurement;
import cz.cuni.mff.ksi.jinfer.iss.idref.IdRefSearch;
import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.iss.utils.GraphUtils;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.IGGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.iss.utils.MappingUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static cz.cuni.mff.ksi.jinfer.iss.utils.Utils.time;
import static cz.cuni.mff.ksi.jinfer.iss.utils.Utils.delta;

/**
 * Class encapsulating an ID set search experiment. After all the input
 * parameters are set and information about the system is collected, the ID
 * set search is run and evaluated.
 *
 * @author vektor
 */
public class Experiment {

  private final ExperimentParameters params;

  /** Time of the heuristic run start, absolute, in ms. */
  private long startTime;
  /** Total time of the heuristic run, in ms. */
  private long totalTime;
  /** Time it took to extract the grammar, in ms. */
  private long grammarTime;
  /** Time it took to build the AM model, in ms. */
  private long modelTime;
  private Quality highestQuality = Quality.ZERO;
  private Pair<Boolean, String> terminationReason;
  private AMModel model;

  /** Result of the construction heuristic run. */
  private HeuristicResult constructionResult;
  /** List of improvement heuristic runs results. */
  private final List<HeuristicResult> improvementResults = new ArrayList<HeuristicResult>();

  private final List<ExperimentListener> listeners = new ArrayList<ExperimentListener>();

  public Experiment(final ExperimentParameters params) {
    this.params = params;
  }

  /**
   * Returns a string report of this whole experiment, its setting and results.
   *
   * @return String report of this experiment.
   */
  public String getReport() {
    final StringBuilder ret = new StringBuilder();
    final Pair<Integer, Integer> graph = GraphUtils.getGraphRepresentation(model);
    ret.append("Configuration:")
        .append("\nFile name: ").append(params.getFile().getName()).append(" (").append(params.getFile().getSize()).append(" b)")
        .append("\n  Graph representation: ").append(graph.getFirst()).append(" vertices, ").append(graph.getSecond()).append(" edges")
        .append("\nalpha: ").append(params.getAlpha()).append(", beta: ").append(params.getBeta())
        .append("\n\nResults:")
        .append("\n  Time spent extracting the grammar: ").append(grammarTime).append(" ms")
        .append("\n  Time spent building the model: ").append(modelTime).append(" ms")
        .append("\n  Time spent running the heuristic: ").append(totalTime).append(" ms")
        .append("\nFinal quality: ");
    if (BaseUtils.isEmpty(improvementResults)) {
      ret.append(constructionResult.getQuality().getText());
    }
    else {
      ret.append(improvementResults.get(improvementResults.size() - 1).getQuality().getText());
    }
    ret.append("\nHighest quality: ").append(highestQuality.getText())
        .append("\nConstruction phase: ")
        .append("\n  Algorithm: ").append(params.getConstructionHeuristic().getModuleDescription())
        .append("\n    Time taken: ").append(constructionResult.getTime()).append(" ms")
        .append("\n    Time since start: ").append(constructionResult.getTotalTime()).append(" ms")
        .append("\n    Pool size: ").append(constructionResult.getPoolSize())
        .append("\n    Quality: ").append(constructionResult.getQuality().getText())
        .append("\nImprovement phase: ");

    int i = 0;
    for (final HeuristicResult result : improvementResults) {
      ret.append("\n  pass #").append(i + 1).append(": ")
        .append("\n  Algorithm: ").append(params.getImprovementHeuristics().get(i % params.getImprovementHeuristics().size()).getModuleDescription())
        .append("\n    Time taken: ").append(result.getTime()).append(" ms")
        .append("\n    Time since start: ").append(result.getTotalTime()).append(" ms")
        .append("\n    Pool size: ").append(result.getPoolSize())
        .append("\n    Quality: ").append(result.getQuality().getText());
      i++;
    }

    ret.append("\nTermination reason: ").append(terminationReason.getSecond()).append('\n');

    return ret.toString();
  }

  public String getCsv() {
    final StringBuilder ret = new StringBuilder();
    ret.append("\nTime,Quality,AMs\n")
        .append(constructionResult.getTotalTime()).append(',')
        .append(constructionResult.getQuality().getScalar()).append(',')
        .append(constructionResult.getQuality().getAmCount());
    for (final HeuristicResult result : improvementResults) {
      ret.append('\n')
          .append(result.getTotalTime()).append(',')
          .append(result.getQuality().getScalar()).append(',')
          .append(result.getQuality().getAmCount());
    }
    return ret.toString();
  }

  /**
   * Writes the best ID set.
   *
   * @return String representation of the best ID set in CSV format.
   */
  public String getWinner() {
    Quality bestQuality = constructionResult.getQuality();
    IdSet bestSolution = constructionResult.getIncumbent();

    for (final HeuristicResult ih : improvementResults) {
      if (ih.getQuality().getScalar() > bestQuality.getScalar()) {
        bestQuality = ih.getQuality();
        bestSolution = ih.getIncumbent();
      }
    }

    final List<AttributeMappingId> mappings = new ArrayList<AttributeMappingId>(bestSolution.getMappings());
    Collections.sort(mappings);

    final StringBuilder ret = new StringBuilder();
    ret.append("\nID\nElement,Attribute,Weight");

    for (final AttributeMappingId mapping : mappings) {
      ret.append('\n')
          .append(mapping.getElement())
          .append(',')
          .append(mapping.getAttribute())
          .append(',')
          .append(model.weight(mapping, getAlpha(), getBeta()));
    }

    final List<AttributeMappingId> idRefList = IdRefSearch.getIdRefList(model, bestSolution);
    ret.append("\nIDREF\nElement,Attribute");

    for (final AttributeMappingId mapping : idRefList) {
      ret.append('\n')
          .append(mapping.getElement())
          .append(',')
          .append(mapping.getAttribute());
    }

    return ret.toString();
  }

  /**
   * Adds a "experiment finished" listener.
   *
   * @param el Listener which will receive a notification when this experiment
   * finishes.
   */
  public void addListener(final ExperimentListener el) {
    listeners.add(el);
  }

  /**
   * Runs this experiment. This function returns almost immediately, as the run
   * itself is asynchronous. Use {@link ExperimentListener} to be notified about
   * the experiment finish.
   *
   * @throws InterruptedException
   */
  public void run() throws InterruptedException {
    final IGGenerator igg = ModuleSelectionHelper.lookupImpl(IGGenerator.class, "Basic_IG_Generator");
    final Input input = new Input();
    input.getDocuments().add(params.getFile().getFile());
    final long grammarStartTime = time();
    igg.start(input, new IGGeneratorCallback() {

      @Override
      public void finished(final List<Element> grammar) {
        grammarTime = delta(grammarStartTime);
        final long modelStartTime = time();
        model = new AMModel(grammar);
        // this triggers all the lazy operations in model to see how long it takes to build it fully
        model.getFlat();
        model.getTree();
        model.getAMs();
        model.getTypes();
        modelTime = delta(modelStartTime);
        startTime = time();
        try {
          params.getConstructionHeuristic().start(Experiment.this, new Callback(-1, startTime));
        } catch (final InterruptedException e) {
        }
      }
    });
  }

  private void runImprovement(final List<IdSet> feasiblePool, final int iteration) {
    final ImprovementHeuristic current = params.getImprovementHeuristics().get(iteration % params.getImprovementHeuristics().size());
    try {
      current.start(Experiment.this, feasiblePool, new Callback(iteration, time()));
    } catch (final InterruptedException e) {
    }
  }

  private class Callback implements HeuristicCallback {

    /**
     * Number of this iteration. -1 is construction heuristic run. 0, 1, ...
     * are improvement heuristics runs.
     */
    private final int iteration;
    /** Time in milliseconds when this iteration started. */
    private final long iterationStartTime;

    public Callback(final int iteration, final long iterationStartTime) {
      this.iteration = iteration;
      this.iterationStartTime = iterationStartTime;
    }

    @Override
    public void finished(final List<IdSet> feasiblePool) {
      final long timeTaken = delta(iterationStartTime);
      final long totalTime = delta(startTime);

      final Pair<IdSet, Quality> incumbent = Utils.getBest(Experiment.this, feasiblePool);
      if (incumbent.getSecond().getScalar() >= highestQuality.getScalar()) {
        highestQuality = incumbent.getSecond();
      }
      final HeuristicResult result = new HeuristicResult(timeTaken, totalTime, feasiblePool.size(), incumbent.getFirst(), incumbent.getSecond());
      if (iteration == -1) {
        constructionResult = result;
      }
      else {
        improvementResults.add(result);
      }

      assert(MappingUtils.isIDset(incumbent.getFirst().getMappings(), model));

      final long totTime = delta(startTime);
      final Pair<Boolean, String> termination = params.getTerminationCriterion().terminate(Experiment.this, totTime, feasiblePool);
      if (termination.getFirst().booleanValue()) {
        notifyFinished(termination, totTime);
        return;
      }

      runImprovement(feasiblePool, iteration + 1);
    }
  }

  private void notifyFinished(final Pair<Boolean, String> terminationReason,
          final long totalTime) {
    this.terminationReason = terminationReason;
    this.totalTime = totalTime;
    for (final ExperimentListener el : listeners) {
      el.experimentFinished(this);
    }
  }

  // --- GETTERS ---------------------------------------------------------------

  public int getPoolSize() {
    return params.getPoolSize();
  }

  public AMModel getModel() {
    return model;
  }

  public double getAlpha() {
    return params.getAlpha();
  }
  public double getBeta() {
    return params.getBeta();
  }

  public QualityMeasurement getQualityMeasurement() {
    return params.getMeasurement();
  }

  public Double getKnownOptimum() {
    return params.getKnownOptimum();
  }

  public Quality getHighestQuality() {
    return highestQuality;
  }

  public long getGrammarTime() {
    return grammarTime;
  }

  public long getModelTime() {
    return modelTime;
  }

  public long getTotalTime() {
    return totalTime;
  }

  public HeuristicResult getConstructionResult() {
    return constructionResult;
  }

  public Pair<Boolean, String> getTerminationReason() {
    return terminationReason;
  }
}
