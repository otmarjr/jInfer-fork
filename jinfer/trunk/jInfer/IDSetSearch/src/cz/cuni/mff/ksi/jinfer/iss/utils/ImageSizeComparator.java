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
package cz.cuni.mff.ksi.jinfer.iss.utils;

import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import java.util.Comparator;

/**
 * Comparator of attribute mappings based on the size of their images in the
 * provided model.
 *
 * @author vektor
 */
public class ImageSizeComparator implements Comparator<AttributeMappingId> {

  private final AMModel model;

  /**
   * Constructor.
   *
   * @param Model in context of which the AMs should be compared.
   */
  public ImageSizeComparator(final AMModel model) {
    this.model = model;
  }

  @Override
  public int compare(final AttributeMappingId o1, final AttributeMappingId o2) {
    final Integer size1 = Integer.valueOf(model.getAMs().get(o1).size());
    final Integer size2 = Integer.valueOf(model.getAMs().get(o2).size());
    return -size1.compareTo(size2);
  }
}
