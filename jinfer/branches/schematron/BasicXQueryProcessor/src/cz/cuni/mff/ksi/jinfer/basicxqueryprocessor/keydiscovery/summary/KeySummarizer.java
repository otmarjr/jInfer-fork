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
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.negativeuniqueness.NegativeUniquenessStatement;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.weightedkeys.WeightedKey;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.utils.PathTypeUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides summarization of weighted keys and negative uniqueness
 * statements. It results into a set of {@link SummarizedKey}s. For details
 * on how the summarization works, refer to the PDF documentation.
 * 
 * How to:
 * 1. Create an instance from specified weighted keys and negative uniqueness statements.
 * 2. Run {@link #process()} method.
 * 3. Retrieve the summarized keys by {@link #getSummarizedKeys()} method.
 * 
 * @see WeightedKey
 * @see NegativeUniquenessStatement
 * @author rio
 */
public class KeySummarizer {
  
  private final Map<Key, SummarizedKey> summarizedKeys = new HashMap<Key, SummarizedKey>();
  //private final Map<ForeignKey, SummarizedInfo> foreignKeys = new HashMap<ForeignKey, SummarizedInfo>();
  
  private final List<WeightedKey> weightedKeys;
  private final List<NegativeUniquenessStatement> negativeUniquenessStatements;
  private int keysMaxCount = 0;
  private int keysMaxWeight = 0;
  private int keysTotalCount = 0;
  
  public KeySummarizer(final List<WeightedKey> weightedKeys, final List<NegativeUniquenessStatement> negativeUniquenessStatements) {
    this.weightedKeys = weightedKeys;
    this.negativeUniquenessStatements = negativeUniquenessStatements;
  }
  
  public void process() {
    for (final WeightedKey weightedKey : weightedKeys) {
      processWeightedKey(weightedKey);
    }
    
    computeKeyCoverage();
    
    for (final SummarizedKey summarizedKey : summarizedKeys.values()) {
      summarizedKey.normalize(keysMaxCount, keysMaxWeight, keysTotalCount);
    }
  }
  
  public Collection<SummarizedKey> getSummarizedKeys() {
    return summarizedKeys.values();
  }
  
  private void processWeightedKey(final WeightedKey weightedKey) {
    final Key key = weightedKey.getKey();
    
    if (!summarizedKeys.containsKey(key)) {
      final SummarizedKey summarizedKey = new SummarizedKey(key);
      summarizedKeys.put(key, summarizedKey);
    }
    
    final SummarizedKey summarizedKey = summarizedKeys.get(key);
    int weight = weightedKey.getWeight();
    
    for (final NegativeUniquenessStatement nus : negativeUniquenessStatements) {
      if ((nus.getContextPath() == null && key.getContextPath() == null || nus.getContextPath().equals(key.getContextPath()))
              && nus.getTargetPath().equals(PathTypeUtils.join(key.getTargetPath(), key.getKeyPath()))) {
        weight -= nus.getWeight();
      }
    }
    
    summarizedKey.add(weight);
    
    keysTotalCount += 1;
    final int count = summarizedKey.getCount();
    checkMaxCount(count);
    final int sWeight = summarizedKey.getWeight();
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
  
  private void computeKeyCoverage() {
    for (final Key key1 : summarizedKeys.keySet()) {
      for (final Key key2 : summarizedKeys.keySet()) {
        if (key1 == key2) {
          continue;
        }
        
        if (!key1.getTargetPath().equals(key2.getTargetPath())
                || !key1.getKeyPath().equals(key2.getKeyPath())) {
          continue;
        }
        
        assert(!key1.getContextPath().equals(key2.getContextPath()));
        
        if (key1.getContextPath().covers(key2.getContextPath())) {
          final SummarizedKey summarizedKey1 = summarizedKeys.get(key1);
          final SummarizedKey summarizedKey2 = summarizedKeys.get(key2);
          
          final int weight1 = summarizedKey1.getWeight();
          final int count1 = summarizedKey1.getCount();
          final int weight2 = summarizedKey2.getWeight();
          final int count2 = summarizedKey2.getCount();
          
          if (weight1 > 0) {
            summarizedKey2.setCount(count2 + count1);
            summarizedKey2.setWeight(weight2 + weight1);
            keysTotalCount += count1;
            checkMaxCount(summarizedKey2.getCount());
            checkMaxWeight(summarizedKey2.getWeight());
          }
          
          if (weight2 < 0) {
            summarizedKey1.setCount(count1 + count2);
            summarizedKey1.setWeight(weight1 + weight2);
            keysTotalCount += count2;
            checkMaxCount(summarizedKey1.getCount());
            checkMaxWeight(summarizedKey1.getWeight());
          }
        }
      }
    }
  }

}
