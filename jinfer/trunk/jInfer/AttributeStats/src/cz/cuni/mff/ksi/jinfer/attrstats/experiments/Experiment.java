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

import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.util.List;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class Experiment {

  private String hwInfo;
  private String osInfo;
  private String javaInfo;
  private String glpkInfo;

  private Object[] settings;

  private String fileName;
  private int fileSize;
  private FileCharacteristics characteristics;
  /** Number of vertices and edges in the graph representation of this file. */
  private Pair<Integer, Integer> graphRepresentation;

  private String constructionHeuristic;
  /**
   * List of improvement heuristics to be run in a loop until the termination
   * criterion is met.
   */
  private List<String> improvementHeuristic;

  private String terminationCriterion;

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
    // TODO vektor
    return "";
  }

}
