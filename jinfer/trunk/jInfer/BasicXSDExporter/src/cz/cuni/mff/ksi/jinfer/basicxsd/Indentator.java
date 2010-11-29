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

/**
 * Helper class keeping an inserted text and an indentation level, providing methods
 * to alter the level and to append another text. Formatted text is retrieved by
 * {@link #toString()} method.
 *
 * @author rio
 */
public class Indentator {

  private final StringBuilder builder;
  private int indentationLevel = 0;
  private final int spacesPerIndent;
  

  /**
   * Constructs an instance with zero level of indentation.
   *
   * @param spacesPerIndent Number of space characters per one level of indentation.
   */
  public Indentator(final int spacesPerIndent) {
    this.builder = new StringBuilder();
    this.spacesPerIndent = spacesPerIndent;
  }

  /**
   * Indent specified text and append it. No new line character is inserted, this is responsibility
   * of a caller.
   *
   * @param string Text to indent and append.
   */
  public void indent(final String string) {
    builder.append(makeIndentation());
    builder.append(string);
  }

  /**
   * Appends specified text without indentation. No new line character is inserted, this is responsibility
   * of a caller.
   *
   * @param string Text to append.
   */
  public void append(final String string) {
    builder.append(string);
  }

  @Override
  public String toString() {
    return builder.toString();
  }

  /**
   * Increases the indentation level per one.
   */
  public void increaseIndentation() {
    indentationLevel += spacesPerIndent;
  }

  /**
   * decreases the indentation level per one.
   */
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
