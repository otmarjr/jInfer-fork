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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.summary;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.Key;

/**
 * A representation of keys with their summarization information; count, sum of
 * weights, and normalized weight. Instances are created by {@link KeySummarizer}.
 * 
 * TODO rio Refactor. This concept of summarized key classes with normalize method
 * is a little weird :)
 * 
 * @author rio
 */
public class SummarizedKey {

  private final Key key;
  private int count = 0;
  private int weight = 0;
  private double normalizedWeight = 0;
  
  public SummarizedKey(final Key key) {
    this.key = key;
  }

  public void add(final int weight) {
    this.weight += weight;
    ++count;
  }

  public void normalize(final int maxCount, final int maxWeight, final int totalCount) {
    normalizedWeight = (double) weight / maxWeight * (1 - ((double)maxCount - count) / totalCount);
  }
  
  public Key getKey() {
    return key;
  }

  public int getCount() {
    return count;
  }

  public int getWeight() {
    return weight;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public double getNormalizedWeight() {
    return normalizedWeight;
  }
  
}
