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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author vektor
 */
@SuppressWarnings("PMD.SystemPrintln")
public class WeightedRandomGeneratorTest {

  private static final int ITERATIONS = 1000000;

  @Test(expected = IllegalArgumentException.class)
  public void testNextNegative() {
    System.out.println("nextNegative");
    new WeightedRandomGenerator(new double[]{1, -1});
  }

  @Test
  public void testNext0() {
    System.out.println("next0");
    final WeightedRandomGenerator instance = new WeightedRandomGenerator(new double[] {1});

    int sum = 0;
    for (int i = 0; i < ITERATIONS; i++) {
      final int num = instance.next();
      assertSame(0, num);
      sum += num;
    }

    assertEquals(0, (double)sum / ITERATIONS, 0.01);
  }

  @Test
  public void testNext1() {
    System.out.println("next1");
    final WeightedRandomGenerator instance = new WeightedRandomGenerator(new double[] {1, 3});

    int sum = 0;
    for (int i = 0; i < ITERATIONS; i++) {
      final int num = instance.next();
      assertTrue(num == 0 || num == 1);
      sum += num;
    }

    assertEquals(0.75, (double)sum / ITERATIONS, 0.01);
  }

  @Test
  public void testNext2() {
    System.out.println("next2");
    final WeightedRandomGenerator instance = new WeightedRandomGenerator(new double[] {1, 1, 1});

    int sum = 0;
    for (int i = 0; i < ITERATIONS; i++) {
      final int num = instance.next();
      assertTrue(num == 0 || num == 1 || num == 2);
      sum += num;
    }

    assertEquals(1, (double)sum / ITERATIONS, 0.01);
  }

  @Test
  public void testNext3() {
    System.out.println("next3");
    final WeightedRandomGenerator instance = new WeightedRandomGenerator(new double[] {1, 0, 1});

    int sum = 0;
    for (int i = 0; i < ITERATIONS; i++) {
      final int num = instance.next();
      assertTrue(num == 0 || num == 2);
      sum += num;
    }

    assertEquals(1, (double)sum / ITERATIONS, 0.01);
  }

  @Test
  public void testNext4() {
    System.out.println("next4");
    final WeightedRandomGenerator instance = new WeightedRandomGenerator(new double[] {1, 1, 0});

    int sum = 0;
    for (int i = 0; i < ITERATIONS; i++) {
      final int num = instance.next();
      assertTrue(num == 0 || num == 1);
      sum += num;
    }

    assertEquals(0.5, (double)sum / ITERATIONS, 0.01);
  }
}
