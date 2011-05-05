/*
 *  Copyright (C) 2011 anti
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

package cz.cuni.mff.ksi.jinfer.basicdtd;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for {@link SchemaGeneratorImpl}
 *
 * @author anti
 */
public class SchemaGeneratorImplTest {

    public SchemaGeneratorImplTest() {
    }

  /**
   * Test of start method, of class SchemaGeneratorImpl.
   */
  @Test
  public void testStart1() throws Exception {
    System.out.println("start alt 2,5");
    
    Element c = Element.getMutable();
    c.setName("c");
    c.getSubnodes().setType(RegexpType.LAMBDA);
    c.getSubnodes().setImmutable();

    Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    Regexp<AbstractStructuralNode> rb = Regexp.<AbstractStructuralNode>getToken(b);
    Regexp<AbstractStructuralNode> rc = Regexp.<AbstractStructuralNode>getToken(c);

    Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.ALTERNATION);
    treeBase.getSubnodes().getChildren().add(rb);
    treeBase.getSubnodes().getChildren().add(rc);
    treeBase.getSubnodes().setInterval(RegexpInterval.getBounded(2, 5));
    
    
    List<Element> grammar = new ArrayList<Element>();
    grammar.add(treeBase);
    
    SchemaGeneratorCallback callback = new SchemaGeneratorCallback() {

      @Override
      public void finished(String schema, String extension) {
        assertEquals("<!-- %generated% -->\n<!ELEMENT a ((b|c),(b|c),(b|c)?,(b|c)?,(b|c)?)>\n", schema);
      }
    };
    SchemaGeneratorImpl instance = new SchemaGeneratorImpl();
    instance.start(grammar, callback);
  }


  /**
   * Test of start method, of class SchemaGeneratorImpl.
   */
  @Test
  public void testStart2() throws Exception {
    System.out.println("start alt 2,inf");

    Element c = Element.getMutable();
    c.setName("c");
    c.getSubnodes().setType(RegexpType.LAMBDA);
    c.getSubnodes().setImmutable();

    Element b = Element.getMutable();
    b.setName("b");
    b.getSubnodes().setType(RegexpType.LAMBDA);
    b.getSubnodes().setImmutable();

    Regexp<AbstractStructuralNode> rb = Regexp.<AbstractStructuralNode>getToken(b);
    Regexp<AbstractStructuralNode> rc = Regexp.<AbstractStructuralNode>getToken(c);

    Element treeBase = Element.getMutable();
    treeBase.setName("a");
    treeBase.getSubnodes().setType(RegexpType.ALTERNATION);
    treeBase.getSubnodes().getChildren().add(rb);
    treeBase.getSubnodes().getChildren().add(rc);
    treeBase.getSubnodes().setInterval(RegexpInterval.getUnbounded(2));


    List<Element> grammar = new ArrayList<Element>();
    grammar.add(treeBase);

    SchemaGeneratorCallback callback = new SchemaGeneratorCallback() {

      @Override
      public void finished(String schema, String extension) {
        assertEquals("<!-- %generated% -->\n<!ELEMENT a ((b|c),(b|c)+)>\n", schema);
      }
    };
    SchemaGeneratorImpl instance = new SchemaGeneratorImpl();
    instance.start(grammar, callback);
  }
}