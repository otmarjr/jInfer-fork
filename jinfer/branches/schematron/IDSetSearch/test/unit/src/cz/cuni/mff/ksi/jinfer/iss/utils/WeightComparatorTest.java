/*
 * Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.iss.utils;

import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.TestUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class WeightComparatorTest {

  /**
   * Test of compare method, of class WeightComparator.
   */
  @Test
  public void testCompare() {
    System.out.println("compare");

    final List<Element> grammar = new ArrayList<Element>();
    grammar.add(TestUtils.getElementWithAttribute("e", "a", "d"));
    grammar.add(TestUtils.getElementWithAttribute("e1", "a1", "e"));
    grammar.add(TestUtils.getElementWithAttribute("e1", "a1", "f"));
    grammar.add(TestUtils.getElementWithAttribute("e2", "a2", "g"));
    grammar.add(TestUtils.getElementWithAttribute("e2", "a2", "g"));
    grammar.add(TestUtils.getElementWithAttribute("e2", "a2", "g"));

    final AMModel model = new AMModel(grammar);

    final List<AttributeMappingId> amis = new ArrayList<AttributeMappingId>(2);
    final AttributeMappingId ami1 = new AttributeMappingId("e", "a");
    amis.add(ami1);
    final AttributeMappingId ami2 = new AttributeMappingId("e1", "a1");
    amis.add(ami2);

    Collections.sort(amis, new WeightComparator(model, 1, 1));

    // ami2 is heavier than ami1, so we expect it to be first
    assertEquals(amis.get(0), ami2);
    assertEquals(amis.get(1), ami1);

    final AttributeMappingId ami3 = new AttributeMappingId("e2", "a2");
    amis.add(ami3);

    Collections.sort(amis, new WeightComparator(model, 1, 1));
    // ami3 is the heaviest
    assertEquals(amis.get(0), ami3);
    assertEquals(amis.get(1), ami2);
    assertEquals(amis.get(2), ami1);
  }
}
