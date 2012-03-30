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

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.ForeignKey;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.keys.Key;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.PathType;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.joinpatterns.ClassifiedJoinPattern;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.keydiscovery.joinpatterns.JoinPattern;
import cz.cuni.mff.ksi.jinfer.basicxqueryprocessor.utils.ContextPathFinder;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to create weighted keys from classified join pattern occurrences.
 * 
 * How to:
 * 1. Create an instance by passing classified join patterns to the constructor.
 * 2. Run {@link #process} method.
 * 3. Retrieves the created keys and foreign keys by the getter methods.
 * 
 * @see ClassifiedJoinPattern
 * @see WeightedKey
 * @see WeightedForeignKey
 * @author rio
 */
public class WeightedKeysCreator {

  private final List<ClassifiedJoinPattern> classifiedJoinPatterns;
  private final List<WeightedKey> weightedKeys = new ArrayList<WeightedKey>();
  private final List<WeightedForeignKey> weightedForeignKeys = new ArrayList<WeightedForeignKey>();

  public WeightedKeysCreator(final List<ClassifiedJoinPattern> classifiedJoinPatterns) {
    this.classifiedJoinPatterns = classifiedJoinPatterns;
  }

  public void process() {
    for (final ClassifiedJoinPattern classifiedJoinPattern : classifiedJoinPatterns) {
      processClassifiedJoinPatternToKeys(classifiedJoinPattern, weightedKeys, weightedForeignKeys);
    }
  }

  public List<WeightedKey> getWeightedKeys() {
    return weightedKeys;
  }

  public List<WeightedForeignKey> getWeightedForeignKeys() {
    return weightedForeignKeys;
  }

  private static void processClassifiedJoinPatternToKeys(final ClassifiedJoinPattern classifiedJoinPattern, final List<WeightedKey> weightedKeys, final List<WeightedForeignKey> weightedForeignKeys) {
    final JoinPattern jp = classifiedJoinPattern.getJoinPattern();
    final ClassifiedJoinPattern.Type type = classifiedJoinPattern.getType();
    final int weight = classifiedJoinPattern.getWeight();

    PathType P1 = jp.getP1();
    PathType P2 = jp.getP2();
    PathType C = null;
    final ContextPathFinder cpf = new ContextPathFinder(P1, P2);
    if (cpf.haveCommonContext()) {
      C = cpf.getContextPath();
      P1 = cpf.getNewPath1();
      P2 = cpf.getNewPath2();
    }

    if (type == ClassifiedJoinPattern.Type.O1) {
      final Key key = new Key(C, P1, jp.getL1());
      final Key notKey = new Key(C, P2, jp.getL2());
      final ForeignKey foreignKey = new ForeignKey(key, P2, jp.getL2());

      final WeightedKey wKey = new WeightedKey(key, weight);
      final WeightedKey wNotKey = new WeightedKey(notKey, weight * (-1));
      final WeightedForeignKey wForeignKey = new WeightedForeignKey(foreignKey, weight);

      weightedKeys.add(wKey);
      weightedKeys.add(wNotKey);
      weightedForeignKeys.add(wForeignKey);
    } else {
      final Key key = new Key(C, P2, jp.getL2());
      final ForeignKey foreignKey = new ForeignKey(key, P1, jp.getL1());

      final WeightedKey wKey = new WeightedKey(key, weight);
      final WeightedForeignKey wForeignKey = new WeightedForeignKey(foreignKey, weight);

      weightedKeys.add(wKey);
      weightedForeignKeys.add(wForeignKey);
    }
  }
  
}
