/*
 * Copyright (C) 2011 anti
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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.conditiontesting.skstrings;

import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class SKBucketTest {

  public SKBucketTest() {
  }

  /**
   * Test of preceede method, of class SKBucket.
   */
  @Test
  public void testPreceede() {
    System.out.println("preceede");
    SKBucket<String> instance = new SKBucket<String>();
    instance.add(new Step<String>("a", null, null, 16,16), 0.1);
    instance.add(new Step<String>("b", null, null, 17,17), 0.2);
    instance.add(new Step<String>("c", null, null, 18,18), 0.3);
    instance.preceede(new Step<String>("x", null, null, 19,19), 0.5);
    assertEquals(instance.getSKStrings().get(0).getProbability(), 0.05, 0);
    assertEquals(instance.getSKStrings().get(1).getProbability(), 0.1,0);
    assertEquals(instance.getSKStrings().get(2).getProbability(), 0.15,0);
    assertEquals(instance.getSKStrings().get(0).getStr().size(), 2);
    assertEquals(instance.getSKStrings().get(1).getStr().size(), 2);
    assertEquals(instance.getSKStrings().get(2).getStr().size(), 2);
    assertEquals(instance.getSKStrings().get(0).getStr().getFirst().getAcceptSymbol(), "x");
    assertEquals(instance.getSKStrings().get(0).getStr().getLast().getAcceptSymbol(), "a");
    assertEquals(instance.getSKStrings().get(1).getStr().getFirst().getAcceptSymbol(), "x");
    assertEquals(instance.getSKStrings().get(1).getStr().getLast().getAcceptSymbol(), "b");
    assertEquals(instance.getSKStrings().get(2).getStr().getFirst().getAcceptSymbol(), "x");
    assertEquals(instance.getSKStrings().get(2).getStr().getLast().getAcceptSymbol(), "c");
  }

  /**
   * Test of getMostProbable method, of class SKBucket.
   */
  @Test
  public void testGetMostProbable() {
    System.out.println("getMostProbable");
    SKBucket<String> instance = new SKBucket<String>();
    instance.add(new Step<String>("b", null, null, 16,16), 0.1);
    instance.add(new Step<String>("y", null, null, 6,6), 0.6);
    instance.add(new Step<String>("b", null, null, 16,16), 0.3);
    instance.add(new Step<String>("b", null, null, 16,16), 0.2);
    instance.add(new SKString<String>(
            new Step<String>("x", null, null, 7,7), 0.7
            ));
    instance.add(new Step<String>("a", null, null, 16,16), 0.4);
    SKBucket<String> instance_subbucket = new SKBucket<String>();
    instance_subbucket.add(new Step<String>("z", null, null, 5,5), 0.5);
    instance.addAll(instance_subbucket);
    SKBucket<String> result = instance.getMostProbable(0.01);
    assertEquals(result.getSKStrings().size(), 1);
    assertEquals(result.getSKStrings().get(0).getStr().getFirst().getAcceptSymbol(), "x");
    result = instance.getMostProbable(0.5);
    assertEquals(result.getSKStrings().size(), 1);
    assertEquals(result.getSKStrings().get(0).getStr().getFirst().getAcceptSymbol(), "x");
    result = instance.getMostProbable(0.7);
    assertEquals(result.getSKStrings().size(), 1);
    assertEquals(result.getSKStrings().get(0).getStr().getFirst().getAcceptSymbol(), "x");
    result = instance.getMostProbable(0.8);
    assertEquals(result.getSKStrings().size(), 2);
    assertEquals(result.getSKStrings().get(0).getStr().getFirst().getAcceptSymbol(), "x");
    assertEquals(result.getSKStrings().get(1).getStr().getFirst().getAcceptSymbol(), "y");
    result = instance.getMostProbable(1.31);
    assertEquals(result.getSKStrings().size(), 3);
    assertEquals(result.getSKStrings().get(0).getStr().getFirst().getAcceptSymbol(), "x");
    assertEquals(result.getSKStrings().get(1).getStr().getFirst().getAcceptSymbol(), "y");
    assertEquals(result.getSKStrings().get(2).getStr().getFirst().getAcceptSymbol(), "z");
  }

  /**
   * Test of areSubset method, of class SKBucket.
   */
  @Test
  public void testAreSubset() {
    System.out.println("areSubset");
    SKBucket<String> anotherBucket = new SKBucket<String>();
    SKBucket<String> instance = new SKBucket<String>();
    instance.add(new SKString<String>(
            new Step<String>("x", null, null, 7,7), 0.7
            ));
    instance.add(new Step<String>("y", null, null, 6,6), 0.6);
    SKBucket<String> instance_subbucket = new SKBucket<String>();
    instance_subbucket.add(new Step<String>("z", null, null, 5,5), 0.5);
    instance.addAll(instance_subbucket);

    // empty bucket is not a subset
    assertFalse(anotherBucket.areSubset(instance));
    anotherBucket.add(new Step<String>("x", null, null, 17, 17), 0.77);
    assertTrue(anotherBucket.areSubset(instance));
    anotherBucket.add(new Step<String>("y", null, null, 16,16), 0.66);
    assertTrue(anotherBucket.areSubset(instance));
    anotherBucket.add(new Step<String>("z", null, null, 15,15), 0.55);
    assertTrue(anotherBucket.areSubset(instance));
    anotherBucket.add(new Step<String>("z", null, null, 15,15), 0.55);
    assertTrue(anotherBucket.areSubset(instance));
    assertEquals(4, anotherBucket.getSKStrings().size());
    anotherBucket.add(new Step<String>("a", null, null, 15,15), 0.55);
    assertFalse(anotherBucket.areSubset(instance));

  }
}
