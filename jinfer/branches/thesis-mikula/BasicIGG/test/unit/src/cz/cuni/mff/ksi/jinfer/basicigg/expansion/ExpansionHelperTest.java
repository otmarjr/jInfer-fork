/*
 *  Copyright (C) 2010 vektor
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.basicigg.expansion;

import java.util.Arrays;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class ExpansionHelperTest {

  @Test(expected = IllegalArgumentException.class)
  public void testApplyIntervalNull1() {
    System.out.println("applyIntervalNull1");
    ExpansionHelper.applyInterval(null, RegexpInterval.getOnce());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplyIntervalNull2() {
    System.out.println("applyIntervalNull2");
    ExpansionHelper.applyInterval(Collections.<List<String>>emptyList(), null);
  }

  @Test
  public void testApplyIntervalOnce() {
    System.out.println("applyIntervalOnce");
    final List<List<String>> input = getWords();
    final RegexpInterval ri = RegexpInterval.getOnce();
    final List<List<String>> expResult = getWords();
    final List<List<String>> result = ExpansionHelper.applyInterval(input, ri);
    assertEquals(expResult, result);
  }

  @Test
  public void testApplyIntervalOptional() {
    System.out.println("applyIntervalOptional");
    final List<List<String>> input = getWords();
    final RegexpInterval ri = RegexpInterval.getOptional();
    final List<List<String>> expResult = new ArrayList<List<String>>();
    expResult.add(Collections.<String>emptyList());
    expResult.addAll(getWords());
    final List<List<String>> result = ExpansionHelper.applyInterval(input, ri);
    assertEquals(expResult, result);
  }

  @Test
  public void testApplyIntervalKleeneStar() {
    System.out.println("applyIntervalKleeneStar");
    final List<List<String>> input = getWords();
    final RegexpInterval ri = RegexpInterval.getKleeneStar();
    final List<List<String>> result = ExpansionHelper.applyInterval(input, ri);
    assertEquals(5, result.size());
    assertEquals(Collections.emptyList(), result.get(0));
    assertEquals(getWords().get(0), result.get(1));
    assertEquals(getWords().get(1), result.get(3));
    assertEquals(BaseUtils.cloneList(getWords().get(0), 3), result.get(2));
    assertEquals(BaseUtils.cloneList(getWords().get(1), 3), result.get(4));
  }

  @Test
  public void testApplyIntervalKleeneCross() {
    System.out.println("applyIntervalKleeneCross");
    final List<List<String>> input = getWords();
    final RegexpInterval ri = RegexpInterval.getKleeneCross();
    final List<List<String>> result = ExpansionHelper.applyInterval(input, ri);
    assertEquals(4, result.size());
    assertEquals(getWords().get(0), result.get(0));
    assertEquals(getWords().get(1), result.get(2));
    assertEquals(BaseUtils.cloneList(getWords().get(0), 3), result.get(1));
    assertEquals(BaseUtils.cloneList(getWords().get(1), 3), result.get(3));
  }

  @Test
  public void testApplyIntervalBounded() {
    System.out.println("applyIntervalBounded");
    final List<List<String>> input = getWords();
    final RegexpInterval ri = RegexpInterval.getBounded(3, 5);
    final List<List<String>> result = ExpansionHelper.applyInterval(input, ri);
    assertEquals(4, result.size());
    assertEquals(BaseUtils.cloneList(getWords().get(0), 3), result.get(0));
    assertEquals(BaseUtils.cloneList(getWords().get(0), 5), result.get(1));
    assertEquals(BaseUtils.cloneList(getWords().get(1), 3), result.get(2));
    assertEquals(BaseUtils.cloneList(getWords().get(1), 5), result.get(3));
  }

  @Test
  public void testApplyIntervalBoundedZero() {
    System.out.println("applyIntervalBoundedZero");
    final List<List<String>> input = getWords();
    final RegexpInterval ri = RegexpInterval.getBounded(0, 5);
    final List<List<String>> result = ExpansionHelper.applyInterval(input, ri);
    assertEquals(5, result.size());
    assertEquals(Collections.emptyList(), result.get(0));
    assertEquals(getWords().get(0), result.get(1));
    assertEquals(getWords().get(1), result.get(3));
    assertEquals(BaseUtils.cloneList(getWords().get(0), 5), result.get(2));
    assertEquals(BaseUtils.cloneList(getWords().get(1), 5), result.get(4));
  }

  @SuppressWarnings("unchecked")
  private static List<List<String>> getWords() {
    return Arrays.asList(
             Arrays.<String>asList("a", "b"),
             Arrays.<String>asList("c", "d", "e"));
  }

  @Test
  public void testGetEmptyConcat() {
    System.out.println("getEmptyConcat");
    final Regexp<String> result = ExpansionHelper.<String>getEmptyConcat();
    assertTrue(result.isConcatenation());
    assertEquals(null, result.getContent());
    assertEquals(0, result.getChildren().size());
    assertTrue(result.getInterval().isOnce());
  }
}
