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

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.Quality;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class HeuristicResult {

  /** Time taken in this heuristics run in milliseconds. */
  private final long time;
  /** TODO vektor Comment! */
  private final int poolSize;
  /** Quality of the solution found in this run. */
  private final Quality quality;

  // TODO vektor Do I want the IdSet to be here too?

  /**
   * Full constructor.
   *
   * @param time Time taken in this heuristics run in milliseconds.
   * @param quality Quality of the solution found in this run.
   */
  public HeuristicResult(final long time, final int poolSize, final Quality quality) {
    this.time = time;
    this.poolSize = poolSize;
    this.quality = quality;
  }

  public long getTime() {
    return time;
  }

  public int getPoolSize() {
    return poolSize;
  }

  public Quality getQuality() {
    return quality;
  }

}
