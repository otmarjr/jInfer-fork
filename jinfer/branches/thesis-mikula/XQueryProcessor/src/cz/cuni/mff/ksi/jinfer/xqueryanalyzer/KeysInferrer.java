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

import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.joinpatterns.ClassifiedJoinPattern;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.joinpatterns.JoinPattern;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.ForeignKey;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.Key;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.KeySummarizer;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.NegativeUniquenessStatement;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.WeightedForeignKey;
import cz.cuni.mff.ksi.jinfer.xqueryanalyzer.keys.WeightedKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author rio
 */
public class KeysInferrer {

  private final List<JoinPattern> joinPatterns = new ArrayList<JoinPattern>();
  private final List<NegativeUniquenessStatement> negativeUniquenessStatements = new ArrayList<NegativeUniquenessStatement>();
  
  public KeysInferrer() {
    
  }
  
  public void addJoinPatterns(final List<JoinPattern> joinPatterns) {
    this.joinPatterns.addAll(joinPatterns);
  }
  
  public void addNegativeUniquenessStatements(final List<NegativeUniquenessStatement> negativeUniquenessStatements) {
    this.negativeUniquenessStatements.addAll(negativeUniquenessStatements);
  }
  
  private Map<Key, KeySummarizer.SummarizedInfo> summarizedKeys;
  private Map<Key, Set<ForeignKey>> keysToForeignKeys;
  
  public void summarize() {
    final List<ClassifiedJoinPattern> classifiedJoinPatterns = JoinPatternsClassifier.classify(joinPatterns);
    final WeightedKeysCreator weightedKeysCreator = new WeightedKeysCreator(classifiedJoinPatterns);
    weightedKeysCreator.process();
    
    summarizedKeys = summarizeKeys(weightedKeysCreator.getWeightedKeys(), negativeUniquenessStatements);
    
    keysToForeignKeys = new HashMap<Key, Set<ForeignKey>>();
    for (final WeightedForeignKey wfk : weightedKeysCreator.getWeightedForeignKeys()) {
      final ForeignKey fk = wfk.getKey();
      final Key k = fk.getKey();
      
      assert(summarizedKeys.containsKey(k));
      
      if (keysToForeignKeys.containsKey(k)) {
        keysToForeignKeys.get(k).add(fk);
      } else {
        final Set<ForeignKey> fkList = new LinkedHashSet<ForeignKey>();
        fkList.add(fk);
        keysToForeignKeys.put(k, fkList);
      }
    }
  }

  public Map<Key, KeySummarizer.SummarizedInfo> getKeys() {
    return summarizedKeys;
  }
  
  public Map<Key, Set<ForeignKey>> getForeignKeys() {
    return keysToForeignKeys;
  }
  
  private static Map<Key, KeySummarizer.SummarizedInfo> summarizeKeys(List<WeightedKey> wKeys, List<NegativeUniquenessStatement> nuss) {
    final KeySummarizer ks = new KeySummarizer();
    for (final WeightedKey wk : wKeys) {
      ks.summarize(wk, nuss);
    }
    return ks.getSummarizedKeys();
  }
}
