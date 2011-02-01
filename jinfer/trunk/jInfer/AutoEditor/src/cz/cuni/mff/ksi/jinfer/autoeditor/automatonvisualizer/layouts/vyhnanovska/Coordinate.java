/*
 *  Copyright (C) 2010 rio
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

package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.vyhnanovska;

/**
 * Representation of 2D coordinate.
 *
 * @author Julie Vyhnanovska, rio
 */
public class Coordinate {
  private int x;
  private int y;

  /**
   * Creates default coordinate [0,0].
   */
  public Coordinate() {
    x = 0;
    y = 0;
  }

  /**
   * Creates coordinate with specified values.
   *
   * @param x
   * @param y
   */
  public Coordinate(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Sets values of coordinate.
   *
   * @param x
   * @param y
   */
  public void setLocation(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean equals(final Coordinate other) {
    return (this.x == other.x && this.y == other.y);
  }

  @Override
  public String toString() {
    return "X=" + x + " Y=" + y;
  }
}
