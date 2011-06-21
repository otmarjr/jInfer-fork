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
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class Experiment implements IGGeneratorCallback {

  private String hwInfo;
  private String osInfo;
  private String javaInfo;
  private String glpkInfo;

  private Object[] settings;

  private String fileName;
  private long fileSize;
  private FileCharacteristics characteristics;
  /** Number of vertices and edges in the graph representation of this file. */
  private Pair<Integer, Integer> graphRepresentation;

  private ConstructionHeuristic constructionHeuristic;
  /**
   * List of improvement heuristics to be run in a loop until the termination
   * criterion is met.
   */
  private List<ImprovementHeuristic> improvementHeuristics;

  private QualityMeasurement measurement;

  private TerminationCriterion terminationCriterion;

  /** Time of the experiment start, absolute, in ms. */
  private long startTime;
  /** Total time of the experiment run, in ms. */
  private long totalTime;
  private Quality finalQuality;
  private AMModel model;

  /** Result of the construction heuristic run. */
  private HeuristicResult constructionResult;
  /** List of improvement heuristic runs results. */
  private List<HeuristicResult> improvementResults;

  /**
   * TODO vektor Comment!
   */
  public void fillMiscInfo() {
    // TODO vektor Fill out hwInfo, osInfo, javaInfo, glpkInfo, ...
  }

  /**
   * Returns a string report of this whole experiment, its setting and results.
   *
   * @return String report of this experiment.
   */
  public String getReport() {
    final StringBuilder ret = new StringBuilder();
    ret.append("Configuration:")
        .append("\nFile name: ").append(fileName).append(" (").append(fileSize).append(" b)")
        .append("\n\nResults:")
        .append("\nStart time: ").append(new Date(startTime))
        .append("\nTotal time spent: ").append(totalTime)
        .append("\nFinal quality: ").append(finalQuality.getScalar());
    return ret.toString();
  }

  public void run() throws InterruptedException {
    // get IG from the file
    final IGGenerator igg = ModuleSelectionHelper.lookupImpl(IGGenerator.class, "Basic_IG_Generator");
    final Input input = new Input();
    final File inputFile = new File(fileName);
    fileSize = inputFile.length();
    input.getDocuments().add(inputFile);
    igg.start(input, this);
  }

  @Override
  public void finished(final List<Element> grammar) {
    // create the model
    model = new AMModel(grammar);

    // take the time before CH
    startTime = Calendar.getInstance().getTimeInMillis();

    try {
      // run the CH
      constructionHeuristic.start(model, new HeuristicCallback() {

        @Override
        public void finished(final IdSet idSet) {
          // take the time after CH
          final long constructionTime = (Calendar.getInstance().getTimeInMillis() - startTime);
          // measure the quality
          final Quality constructionQuality = measurement.measure(model, idSet);
          constructionResult = new HeuristicResult(constructionTime, constructionQuality);

          // while not termination criterion
          final long totTime = Calendar.getInstance().getTimeInMillis() - startTime;
          if (terminationCriterion.terminate(totTime, idSet)) {
            totalTime = totTime;
            finalQuality = constructionQuality;
            return;
          }

          runImprovement(idSet, 0);
        }
      });
    } catch (final InterruptedException e) {
    }
  }

  private void runImprovement(final IdSet incumbent, final int iteration) {
    final ImprovementHeuristic current = improvementHeuristics.get(iteration % improvementHeuristics.size());
    //   take the time before IH
    final long ihStartTime = Calendar.getInstance().getTimeInMillis();
    //   run the IH
    try {
      current.start(model, incumbent, new HeuristicCallback() {

        @Override
        public void finished(final IdSet idSet) {
          //   take the time after IH
          final long improvementTime = (Calendar.getInstance().getTimeInMillis() - ihStartTime);
          //   measure the quality
          final Quality improvementQuality = measurement.measure(model, idSet);
          improvementResults.add(new HeuristicResult(improvementTime, improvementQuality));

          // while not termination criterion
          final long totTime = Calendar.getInstance().getTimeInMillis() - startTime;
          if (terminationCriterion.terminate(totTime, idSet)) {
            totalTime = totTime;
            finalQuality = improvementQuality;
            return;
          }

          runImprovement(incumbent, iteration + 1);
        }
      });
    } catch (final InterruptedException e) {
    }
  }
}
