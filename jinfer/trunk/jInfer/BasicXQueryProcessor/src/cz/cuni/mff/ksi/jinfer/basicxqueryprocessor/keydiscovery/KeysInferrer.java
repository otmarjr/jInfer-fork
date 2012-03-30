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
package cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.ForeignKey;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.Key;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.joinpatterns.ClassifiedJoinPattern;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.joinpatterns.JoinPattern;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.joinpatterns.JoinPatternsClassifier;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.negativeuniqueness.NegativeUniquenessStatement;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.summary.KeySummarizer;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.summary.SummarizedKey;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.weightedkeys.WeightedForeignKey;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.weightedkeys.WeightedKey;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.weightedkeys.WeightedKeysCreator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A main class responsible for the key discovery.
 * 
 * An instance of this class should be used as follows:
 *  - Creation.
 *  - For each syntax tree, join patterns and negative uniqueness statements found
 *    in the tree should be added to this instance using {@link #addJoinPatterns(java.util.List)}
 *    and {@link #addNegativeUniquenessStatements(java.util.List)} methods.
 *  - After processing of all syntax tree, {@link #process()} should be called.
 *  - Inferred keys and foreign keys can be retrieved by {@link #getKeys()} and
 *    {@link #getForeignKeys()} methods.
 * 
 * @see JoinPattern
 * @see NegativeUniquenessStatement
 * @see Key
 * @see ForeignKey
 * @see KeySummarizer
 * 
 * @author rio
 */
public class KeysInferrer {

  private final List<JoinPattern> joinPatterns = new ArrayList<JoinPattern>();
  private final List<NegativeUniquenessStatement> negativeUniquenessStatements = new ArrayList<NegativeUniquenessStatement>();
  
  private Collection<SummarizedKey> summarizedKeys;
  private Map<Key, Set<ForeignKey>> keysToForeignKeys;
  
  /**
   * Adds join patterns to the instance for a future summarization.
   * @param joinPatterns 
   */
  public void addJoinPatterns(final List<JoinPattern> joinPatterns) {
    this.joinPatterns.addAll(joinPatterns);
  }
  
  /**
   * Adds negative uniqueness statements to the instance for a future summarization.
   * @param negativeUniquenessStatements 
   */
  public void addNegativeUniquenessStatements(final List<NegativeUniquenessStatement> negativeUniquenessStatements) {
    this.negativeUniquenessStatements.addAll(negativeUniquenessStatements);
  }
  
  /**
   * Should be called after all join patterns and negative uniqueness statements
   * were added. After the call, getters for keys can be used.
   */
  public void summarize() {
    final List<ClassifiedJoinPattern> classifiedJoinPatterns = JoinPatternsClassifier.classify(joinPatterns);
    final WeightedKeysCreator weightedKeysCreator = new WeightedKeysCreator(classifiedJoinPatterns);
    weightedKeysCreator.process();
    
    summarizedKeys = summarizeKeys(weightedKeysCreator.getWeightedKeys(), negativeUniquenessStatements);
    
    keysToForeignKeys = new HashMap<Key, Set<ForeignKey>>();
    for (final WeightedForeignKey wfk : weightedKeysCreator.getWeightedForeignKeys()) {
      final ForeignKey fk = wfk.getKey();
      final Key k = fk.getKey();
      
      if (keysToForeignKeys.containsKey(k)) {
        keysToForeignKeys.get(k).add(fk);
      } else {
        final Set<ForeignKey> fkList = new LinkedHashSet<ForeignKey>();
        fkList.add(fk);
        keysToForeignKeys.put(k, fkList);
      }
    }
  }

  /**
   * Retrieves inferred keys after the summarization.
   */
  public Collection<SummarizedKey> getKeys() {
    return summarizedKeys;
  }
  
  /**
   * Retrieves inferred foreign keys after the summarization.
   */
  public Map<Key, Set<ForeignKey>> getForeignKeys() {
    return keysToForeignKeys;
  }
  
  private static Collection<SummarizedKey> summarizeKeys(List<WeightedKey> weightedKeys, List<NegativeUniquenessStatement> negativeUniquenessStatements) {
    final KeySummarizer ks = new KeySummarizer(weightedKeys, negativeUniquenessStatements);
    ks.process();
    return ks.getSummarizedKeys();
  }
}
