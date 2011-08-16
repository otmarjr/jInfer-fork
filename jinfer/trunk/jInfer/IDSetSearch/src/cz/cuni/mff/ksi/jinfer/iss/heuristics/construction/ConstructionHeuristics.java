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
package cz.cuni.mff.ksi.jinfer.iss.heuristics.construction;

import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ConstructionHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.fidax.Fidax;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.Glpk;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public enum ConstructionHeuristics {

  NULL(Null.class),
  RANDOM(Random.class),
  FUZZY(Fuzzy.class),
  INCREMENTAL(Incremental.class),
  REMOVAL(Removal.class),
  FIDAX(Fidax.class),
  GLPK(Glpk.class);

  private final Class<? extends ConstructionHeuristic> clazz;

  private ConstructionHeuristics(final Class<? extends ConstructionHeuristic> clazz) {
    this.clazz = clazz;
  }

  public Class<? extends ConstructionHeuristic> getClazz() {
    return clazz;
  }

}
