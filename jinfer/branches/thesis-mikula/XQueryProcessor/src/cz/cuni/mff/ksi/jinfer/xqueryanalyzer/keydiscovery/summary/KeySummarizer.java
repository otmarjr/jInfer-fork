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
package cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keydiscovery.summary;

import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keydiscovery.negativeuniqueness.NegativeUniquenessStatement;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.Key;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.StepExprNode;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.NormalizedPathType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keydiscovery.weightedkeys.WeightedKey;
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
    private double normalizedWeight = 0;
    
    public SummarizedInfo() {
      count = 0;
      weight = 0;
    }
    
    public void add(final int weight) {
      this.weight += weight;
      ++count;
    }
    
    public void normalize(final int maxCount, final int maxWeight, final int totalCount) {
      normalizedWeight = (double)weight / maxWeight * (1 - (maxCount - count) / totalCount);
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
  
  public void summarize(final WeightedKey key, final List<NegativeUniquenessStatement> nuss) {
    if (!keys.containsKey(key.getKey())) {
      final SummarizedInfo si = new SummarizedInfo();
      keys.put(key.getKey(), si);
    }
    
    final SummarizedInfo si = keys.get(key.getKey());
    int weight = key.getWeight();
    
    for (final NegativeUniquenessStatement nus : nuss) {
      if ((nus.getContextPath() == null && key.getKey().getContextPath() == null || nus.getContextPath().equals(key.getKey().getContextPath()))
              && nus.getTargetPath().equals(key.getKey().getTargetPath())) {
        weight -= nus.getWeight();
      }
    }
    
    si.add(weight);
    
    keysTotalCount += 1;
    final int count = si.getCount();
    checkMaxCount(count);
    final int sWeight = si.getWeight();
    checkMaxWeight(sWeight);
  }
  
  private void checkMaxCount(final int count) {
    if (keysMaxCount < count) {
      keysMaxCount = count;
    }
  }
  
  private void checkMaxWeight(final int weight) {
    if (keysMaxWeight < Math.abs(weight)) {
      keysMaxWeight = Math.abs(weight);
    }
  }
  
  /*public void summarize(final WeightedForeignKey key) {
    if (foreignKeys.containsKey(key.getKey())) {
      foreignKeys.get(key.getKey()).add(key.getWeight());
    }
  }*/
  
  public Map<Key, SummarizedInfo> getSummarizedKeys() {
    computeKeyCoverage();
    
    for (final SummarizedInfo si : keys.values()) {
      si.normalize(keysMaxCount, keysMaxWeight, keysTotalCount);
    }
    return keys;
  }
  
  private void computeKeyCoverage() {
    for (final Key key1 : keys.keySet()) {
      for (final Key key2 : keys.keySet()) {
        if (key1 == key2) {
          continue;
        }
        
        if (!key1.getTargetPath().equals(key2.getTargetPath())
                || !key1.getKeyPath().equals(key2.getKeyPath())) {
          continue;
        }
        
        assert(!key1.getContextPath().equals(key2.getContextPath()));
        
        if (key1.getContextPath().covers(key2.getContextPath())) {
          final SummarizedInfo si1 = keys.get(key1);
          final SummarizedInfo si2 = keys.get(key2);
          
          final int weight1 = si1.getWeight();
          final int count1 = si1.getCount();
          final int weight2 = si2.getWeight();
          final int count2 = si2.getCount();
          
          if (weight1 > 0) {
            si2.setCount(count2 + count1);
            si2.setWeight(weight2 + weight1);
            keysTotalCount += count1;
            checkMaxCount(si2.getCount());
            checkMaxWeight(si2.getWeight());
          }
          
          if (weight2 < 0) {
            si1.setCount(count1 + count2);
            si1.setWeight(weight1 + weight2);
            keysTotalCount += count2;
            checkMaxCount(si1.getCount());
            checkMaxWeight(si1.getWeight());
          }
        }
      }
    }
  }

}
