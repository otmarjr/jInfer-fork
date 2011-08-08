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
 * Comparator of attribute mappings based on their weights in the context of
 * the provided experiment.
 *
 * @author vektor
 */
public class WeightComparator implements Comparator<AttributeMappingId> {

  private final AMModel model;
  private final double alpha;
  private final double beta;

  public WeightComparator(final AMModel model, final double alpha, final double beta) {
    this.model = model;
    this.alpha = alpha;
    this.beta = beta;
  }

  @Override
  public int compare(final AttributeMappingId o1, final AttributeMappingId o2) {
    final Double weight1 = model.weight(o1, alpha, beta);
    final Double weight2 = model.weight(o2, alpha, beta);
    return -weight1.compareTo(weight2);
  }
}
