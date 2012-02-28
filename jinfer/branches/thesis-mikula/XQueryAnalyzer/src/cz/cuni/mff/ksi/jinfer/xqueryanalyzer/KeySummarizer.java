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
package cz.cuni.mff.ksi.jinfer.xqueryanalyzer;

import cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types.TypeFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rio
 */
public class KeySummarizer {
  
  private final Map<Key, SummarizedInfo> keys = new HashMap<Key, SummarizedInfo>();
  //private final Map<ForeignKey, SummarizedInfo> foreignKeys = new HashMap<ForeignKey, SummarizedInfo>();
  
  private int keysMaxCount = 0;
  private int keysMaxWeight = 0;
  private int keysTotalCount = 0;
  
  public class SummarizedInfo {
    
    private int count;
    private int weight;
    private int normalizedWeight = 0;
    
    public SummarizedInfo() {
      count = 0;
      weight = 0;
    }
    
    public void add(final int weight) {
      this.weight += weight;
      ++count;
    }
    
    public void normalize(final int maxCount, final int maxWeight, final int totalCount) {
      normalizedWeight = weight / maxWeight * (1 - (maxCount - count) / totalCount);
    }

    public int getCount() {
      return count;
    }

    public int getWeight() {
      return weight;
    }

    public int getNormalizedWeight() {
      return normalizedWeight;
    }
    
  }
  
  public void summarize(final WeightedKey key, final List<NegativeUniquenessStatement> nuss) {
    if (!keys.containsKey(key.getKey())) {
      final SummarizedInfo si = new SummarizedInfo();
      keys.put(key.getKey(), si);
    }
    
    final SummarizedInfo si = keys.get(key.getKey());
    int weight = key.getWeight();
    
    for (final NegativeUniquenessStatement nus : nuss) {
      if (nus.getContextPath().equals(TypeFactory.createPathType(key.getKey().getContextPath()))
              && nus.getKeyPath().equals(TypeFactory.createPathType(key.getKey().getKeyPath()))) {
        weight -= nus.getWeight();
      }
    }
    
    si.add(weight);
    
    keysTotalCount += 1;
    final int count = si.getCount();
    if (keysMaxCount < count) {
      keysMaxCount = count;
    }
    final int sWeight = si.getWeight();
    if (keysMaxWeight < Math.abs(sWeight)) {
      keysMaxWeight = Math.abs(sWeight);
    }
  }
  
  /*public void summarize(final WeightedForeignKey key) {
    if (foreignKeys.containsKey(key.getKey())) {
      foreignKeys.get(key.getKey()).add(key.getWeight());
    }
  }*/
  
  public Map<Key, SummarizedInfo> getSummarizedKeys() {
    for (final SummarizedInfo si : keys.values()) {
      si.normalize(keysMaxCount, keysMaxWeight, keysTotalCount);
    }
    return keys;
  }
}
