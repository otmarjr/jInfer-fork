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
package cz.cuni.mff.ksi.jinfer.twostep;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class TwoStepSimplifierFactoryTest {


  /**
   * Test of getCapabilities method, of class TwoStepSimplifierFactory.
   */
  @Test
  public void testGetCapabilities() {
    System.out.println("getCapabilities");
    TwoStepSimplifierFactory instance = new TwoStepSimplifierFactory();
    List<String> result = instance.getCapabilities();
    assertTrue(result.contains("can.handle.complex.regexps"));
  }
}
