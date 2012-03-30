/*
 *  Copyright (C) 2011 reseto
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

package cz.cuni.mff.ksi.jinfer.xsdimportsax.utils;

import org.xml.sax.SAXException;

/**
 * Helper class to provide checking for thread interrupt,
 * specialized for SAX parser.
 * SAX parser can only throw {@link SAXException },
 * therefore we must wrap the interrupt and check for it specifically.
 * @author reseto
 */
public final class SAXInterruptChecker {

  private SAXInterruptChecker() {
  }

  /**
   * Checks if this thread has been interrupted and throws an InterruptedException
   * accordingly or does nothing.
   * @throws SAXInterruptedException
   */
  public static void checkInterrupt() throws SAXInterruptedException {
    if (Thread.interrupted()) {
      throw new SAXInterruptedException();
    }
  }
}
