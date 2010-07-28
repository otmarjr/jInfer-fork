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
package cz.cuni.mff.ksi.jinfer.validator;

public final class Main {

  // TODO vektor Create GUI

  private static final String ANT = "C:\\Program Files\\NetBeans 6.9\\java\\ant\\bin\\ant.bat";
  private static final String PROJECT_ROOT = "C:\\Documents and Settings\\vitasek\\My Documents\\Sukromne\\jinfer";

  private Main() {
  }

  public static void main(final String[] args) {
    for (final Remark r : Logic.checkSuite(ANT, PROJECT_ROOT)) {
      Utils.out(r.toString());
    }
  }

}
