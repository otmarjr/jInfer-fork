/*
 * Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.functionalDependencies.modelGenerator;

import cz.cuni.mff.ksi.jinfer.functionalDependencies.weights.ObjectFactory;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.weights.Tweights;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.weights.Tweight;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sviro
 */
public class WeightsProcessorTest {

  public WeightsProcessorTest() {
  }

  @Test(expected = InterruptedException.class)
  public void testProcessNull() throws InterruptedException {
    System.out.println("processNull");
    final List<Tweight> expResult = new ArrayList<Tweight>(0);
    final List<Tweight> result = new WeightsProcessor().process(null);
    assertEquals(expResult, result);
  }

  @Test(expected = InterruptedException.class)
  public void testProcessEmpty() throws InterruptedException {
    System.out.println("processEmpty");
    final InputStream s = new ByteArrayInputStream("".getBytes());
    final List<Tweight> result = new WeightsProcessor().process(s);
  }
  
  private static final String WT1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
"<weights>" +
    "<weight>" +
        "<path>//bib/book/written_by/author</path>" +
        "<value>1.2</value>" +
    "</weight>" +
    "<weight>" +
        "<path>//bib/book/written_by</path>" +
        "<value>0.3</value>" +
    "</weight>" +
"</weights>";

  /**
   * Test of process method, of class WeightsProcessor.
   */
  @Test
  public void testProcess() throws Exception {
    System.out.println("process");
    final InputStream s = new ByteArrayInputStream(WT1.getBytes());
    List<Tweight> result = new WeightsProcessor().process(s);
    List<Tweight> expected = getWT1expected();
    assertEquals(expected.size(), result.size());
    assertEquals(expected, result);
  }

  private List<Tweight> getWT1expected() {
    ObjectFactory objectFactory = new ObjectFactory();
    Tweights weights = objectFactory.createTweights();
    List<Tweight> weightList = weights.getWeight();
    
    Tweight weight = objectFactory.createTweight();
    weight.setPath("//bib/book/written_by/author");
    weight.setValue(new BigDecimal("1.2"));
    Tweight weight1 = objectFactory.createTweight();
    weight1.setPath("//bib/book/written_by");
    weight1.setValue(new BigDecimal("0.3"));
    
    weightList.add(weight);
    weightList.add(weight1);
    
    return weightList;
  }
}
