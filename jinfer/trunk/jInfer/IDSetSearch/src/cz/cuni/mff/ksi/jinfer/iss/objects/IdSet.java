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
package cz.cuni.mff.ksi.jinfer.iss.objects;

import cz.cuni.mff.ksi.jinfer.iss.utils.MappingUtils;
import java.util.Collections;
import java.util.List;

/**
 * Class representing an ID set. Basically it's a list of attribute mappings.
 * Also, it keeps a flag whether this ID set is known to be optimal or not.
 *
 * @author vektor
 */
public class IdSet {

  private final List<AttributeMappingId> mappings;

  private final boolean optimal;

  /** An empty ID set. */
  public static final IdSet EMPTY = new IdSet(Collections.<AttributeMappingId>emptyList());

  /**
   * Full constructor.
   *
   * @param mappings List of attribute mappings constituting this ID set.
   * @param optimal Flag whether this ID set is known to be optimal or not.
   */
  public IdSet(final List<AttributeMappingId> mappings, final boolean optimal) {
    super();
    this.mappings = mappings;
    this.optimal = optimal;
  }

  /**
   * Partial constructor. Assumes this ID set to not be known optimal. It still
   * <cite>might</cite> be optimal, it's just not <cite>known</cite>.
   *
   * @param mappings List of attribute mappings constituting this ID set.
   */
  public IdSet(final List<AttributeMappingId> mappings) {
    this(mappings, false);
  }

  public List<AttributeMappingId> getMappings() {
    return mappings;
  }

  public boolean isOptimal() {
    return optimal;
  }

  public boolean isValid(final AMModel model) {
    return MappingUtils.isIDset(mappings, model);
  }

}
