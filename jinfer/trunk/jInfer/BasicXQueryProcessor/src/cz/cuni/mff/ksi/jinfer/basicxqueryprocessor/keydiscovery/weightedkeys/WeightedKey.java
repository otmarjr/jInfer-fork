/*
 * Copyright (C) 2012 rio
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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.weightedkeys;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.Key;

/**
 * A simple representation of weighted keys.
 * @author rio
 */
public class WeightedKey {
  
  private final Key key;
  private final int weight;
  
  public WeightedKey(final Key key, final int weight) {
    this.key = key;
    if (key.isPositive()) {
      this.weight = weight;
    } else {
      this.weight = 0 - weight;
    }
  }
  
  public int getWeight() {
    return weight;
  }

  public Key getKey() {
    return key;
  }
  
}
