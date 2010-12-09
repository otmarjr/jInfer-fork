/*
 *  Copyright (C) 2010 reseto
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

package cz.cuni.mff.ksi.jinfer.xsdimporter.utils;

/**
 * Exception thrown when an error occurs during the import of XSD Schema.
 * 
 * @author reseto
 */
public class XSDException extends RuntimeException {

  private static final long serialVersionUID = 3534453468l;

  /**
   * Creates an exception with detailed message and its cause.
   * @param message Detail message explaining the cause of exception.
   * @param cause The cause.
   */
  public XSDException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates an exception with a message describing the error that occurred.
   * @param message Detail message.
   */
  public XSDException(final String message) {
    super(message);
  }

}
