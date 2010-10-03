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

package cz.cuni.mff.ksi.jinfer.basicxsd;

// TODO rio comment

/** TODO rio translate
 * Stara sa o odsadenie riadkov.
 *
 * @author rio
 */
public class Indentator {

  private final StringBuilder builder;
  private int indentationLevel = 0;
  private final int spacesPerIndent;
  

  /** Constructor.
   */
  public Indentator(final int spacesPerIndent) {
    this.builder = new StringBuilder();
    this.spacesPerIndent = spacesPerIndent;
  }

  public void indent(final String string) {
    builder.append(makeIndentation());
    builder.append(string);
  }

  public void append(final String string) {
    builder.append(string);
  }

  @Override
  public String toString() {
    return builder.toString();
  }

  public void increaseIndentation() {
    indentationLevel += spacesPerIndent;
  }

  public void decreaseIndentation() {
    assert (indentationLevel >= spacesPerIndent);
    indentationLevel -= spacesPerIndent;
  }

  private String makeIndentation() {
    final StringBuilder indentationBuilder = new StringBuilder();

    for (int i = 0; i < indentationLevel; i++) {
      indentationBuilder.append(' ');
    }

    return indentationBuilder.toString();
  }
}
