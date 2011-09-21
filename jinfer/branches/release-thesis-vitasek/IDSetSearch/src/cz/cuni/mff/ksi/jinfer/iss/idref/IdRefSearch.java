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
package cz.cuni.mff.ksi.jinfer.iss.idref;

import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility class to find IDREF attributes in a AM model with known ID set.
 *
 * @author vektor
 */
public final class IdRefSearch {

  private IdRefSearch() {

  }

  /**
   * For a provided AM model and known ID set returns a list of attribute
   * mapping IDs that can be declared IDREF.
   *
   * An attribute mapping can be declared IDREF, if its image is a subset
   * of the combined image of all AMs in the ID set.
   *
   * @param model AM model in context of which the search is performed.
   * @param idSet ID set for which to find the IDREF AMs.
   *
   * @return List of potential IDREF AMs.
   */
  public static List<AttributeMappingId> getIdRefList(final AMModel model, final IdSet idSet) {
    final List<AttributeMappingId> ret = new ArrayList<AttributeMappingId>();

    final List<AttributeMappingId> candidates = new ArrayList<AttributeMappingId>(model.getAMs().keySet());
    candidates.removeAll(idSet.getMappings());

    final Set<String> idSetImage = new HashSet<String>();
    for (final AttributeMappingId mapping : idSet.getMappings()) {
      idSetImage.addAll(model.getAMs().get(mapping).getImage());
    }

    for (final AttributeMappingId mapping : candidates) {
      if (BaseUtils.isSubset(model.getAMs().get(mapping).getImage(), idSetImage)) {
        ret.add(mapping);
      }
    }

    return ret;
  }

}
