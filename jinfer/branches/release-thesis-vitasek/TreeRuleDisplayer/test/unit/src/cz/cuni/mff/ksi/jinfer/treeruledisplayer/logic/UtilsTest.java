/*
 *  Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.treeruledisplayer.logic;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractNamedNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.utils.TestUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sviro
 */
public class UtilsTest {

  /**
   * Test of getVertexLabel method, when regexp parameter is null.
   */
  @Test
  public void testGetVertexLabelNull() {
    System.out.println("getVertexLabelNull");
    Regexp<? extends AbstractNamedNode> regexp = null;
    boolean trim = true;
    String expResult = null;
    String result = Utils.getVertexLabel(regexp, trim);
    assertEquals(expResult, result);
  }

  /**
   * Test of getVertexLabel method, when regexp name is empty.
   */
  @Test
  public void testGetVertexLabelEmpty() {
    System.out.println("getVertexLabelEmpty");
    Regexp<? extends AbstractNamedNode> regexp = Regexp.<AbstractNamedNode>getToken(TestUtils.getElement(""));
    boolean trim = true;
    String expResult = "";
    String result = Utils.getVertexLabel(regexp, trim);
    assertEquals(expResult, result);
  }

  /**
   * Test of getVertexLabel method, when regexp name's length is limit - 1.
   */
  @Test
  public void testGetVertexLabelUnderLimit() {
    System.out.println("getVertexLabelUnderLimit");
    StringBuilder builder = new StringBuilder(Utils.VERTEX_LABEL_MAX_LENGHT);
    for (int i = 0; i < Utils.VERTEX_LABEL_MAX_LENGHT - 1; i++) {
      builder.append("a");
    }
    Regexp<? extends AbstractNamedNode> regexp = Regexp.<AbstractNamedNode>getToken(TestUtils.getElement(builder.toString()));
    boolean trim = true;
    String expResult = builder.toString();
    String result = Utils.getVertexLabel(regexp, trim);
    assertEquals(expResult, result);
  }

  /**
   * Test of getVertexLabel method, when regexp name's length is limit.
   */
  @Test
  public void testGetVertexLabelLimit() {
    System.out.println("getVertexLabelLimit");
    StringBuilder builder = new StringBuilder(Utils.VERTEX_LABEL_MAX_LENGHT);
    for (int i = 0; i < Utils.VERTEX_LABEL_MAX_LENGHT; i++) {
      builder.append("a");
    }
    Regexp<? extends AbstractNamedNode> regexp = Regexp.<AbstractNamedNode>getToken(TestUtils.getElement(builder.toString()));
    boolean trim = true;
    String expResult = builder.toString();
    String result = Utils.getVertexLabel(regexp, trim);
    assertEquals(expResult, result);
  }

  /**
   * Test of getVertexLabel method, when regexp name's length is limit + 1.
   */
  @Test
  public void testGetVertexLabelOverLimit() {
    System.out.println("getVertexLabelLimit");
    StringBuilder builder = new StringBuilder(Utils.VERTEX_LABEL_MAX_LENGHT);
    for (int i = 0; i < Utils.VERTEX_LABEL_MAX_LENGHT; i++) {
      builder.append("a");
    }
    Regexp<? extends AbstractNamedNode> regexp = Regexp.<AbstractNamedNode>getToken(TestUtils.getElement(builder.toString() + "b"));
    boolean trim = true;
    String expResult = builder.toString() + "...";
    String result = Utils.getVertexLabel(regexp, trim);
    assertEquals(expResult, result);
  }

  /**
   * Test of getVertexLabel method, when regexp is simpleData and name length is limit + 1.
   */
  @Test
  public void testGetVertexLabelSimpleData() {
    System.out.println("getVertexLabelSimpleData");
    StringBuilder builder = new StringBuilder(Utils.VERTEX_LABEL_MAX_LENGHT);
    for (int i = 0; i < Utils.VERTEX_LABEL_MAX_LENGHT; i++) {
      builder.append("a");
    }
    Regexp<? extends AbstractNamedNode> regexp = Regexp.<AbstractNamedNode>getToken(TestUtils.getSimpleDataByName(builder.toString() + "b"));
    boolean trim = true;
    String expResult = "\"" + builder.toString() + "...\"";
    String result = Utils.getVertexLabel(regexp, trim);
    assertEquals(expResult, result);
  }
}
