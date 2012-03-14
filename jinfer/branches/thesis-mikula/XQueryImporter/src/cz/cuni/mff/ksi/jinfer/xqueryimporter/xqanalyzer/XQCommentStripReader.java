/*
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

/*
 * This code originates from Jiří Schejbal's master thesis. Jiří Schejbal
 * is also the author of the original version of this code.
 * With his approval, we use his code in jInfer and we slightly modify it to
 * suit our cause.
 */
package cz.cuni.mff.ksi.jinfer.xqueryimporter.xqanalyzer;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

/**
 * This class can strip comments from XQuery program.
 * 
 * It replaces all characters within character sequence beginning with
 * <code>(:</code> and ending with <code>:)</code> including, with spaces. It
 * also guards nesting of comments.
 *
 * @author Jiri Schejbal
 */
public class XQCommentStripReader extends FilterReader {

  /**
   * Number of nested comments.
   */
  private int nesting = 0;
  /**
   * Last read character.
   */
  private char lastChar = 0;

  /**
   * Creates a new XQCommentStripReader.
   *
   * @param in Input reader the comments are stripped from.
   */
  public XQCommentStripReader(Reader in) {
    super(in);
  }

  /**
   * Reads characters into a portion of an array.
   *
   * @exception  IOException  If an I/O error occurs
   */
  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    int size = super.read(cbuf, off, len);
    if (size != -1) {
      for (int i = 0; i < size; i++) {
        char c = cbuf[i + off];
        switch (c) {
          case ':':
            if (lastChar == '(') {
              nesting++;
              c = 0;
              if (i != 0) {
                cbuf[i - 1 + off] = ' ';
                cbuf[i + off] = ' ';
              }
            } else if (nesting > 0) {
              cbuf[i + off] = ' ';
            }
            break;
          case ')':
            if (lastChar == ':') {
              cbuf[i + off] = ' ';
              if (nesting > 0) {
                nesting--;
              }
            } else if (nesting > 0) {
              cbuf[i + off] = ' ';
            }
            break;
          default:
            if (nesting > 0) {
              cbuf[i + off] = ' ';
            }
        }
        lastChar = c;
      }
    }
    return size;
  }

  /**
   * Gets number of nested comments.
   *
   * If the number is greater than zero after the whole program was read, this
   * situation presents an unterminated comment.
   *
   * @return Number of nested comments.
   */
  public int getNesting() {
    return nesting;
  }

  /**
   * Tells whether this stream supports the mark() operation.
   *
   * @return <code>true</code> if mark() operation is supported,
   *      <code>false</code> otherwise.
   */
  @Override
  public boolean markSupported() {
    return false;
  }

  /**
   * This method is not supported in the XQCommentStripReader.
   */
  @Override
  public void mark(int readAheadLimit) throws IOException {
    throw new IOException("XQCommentStripReader does not support the mark"
            + "() operation.");
  }
}
