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
package cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes;

/**
 * This class represents a string literal.
 *
 * @author Jiri Schejbal
 */
public class StringLiteral {

  private QuotMark quotMark;
  private String value;
  private String[][] entityRefs = {
    {"lt", "<"},
    {"gt", ">"},
    {"quot", "\""},
    {"apos", "'"},
    {"amp", "&"}
  };

  public String resolvePredEntityRef(String string) {
    String result = string;
    for (String[] entityRef : entityRefs) {
      result = result.replaceAll("&" + entityRef[0] + ";", entityRef[1]);
    }
    return result;
  }

  public StringLiteral(String value) {
    assert (!value.isEmpty());
    char quotChar = value.charAt(0);
    assert (quotChar == '"' || quotChar == '\'');
    this.value = resolvePredEntityRef(
            value.substring(1, value.length() - 1));
    if (quotChar == '"') {
      quotMark = QuotMark.DOUBLE;
      this.value = this.value.replaceAll("\"\"", "\"");
    } else {
      quotMark = QuotMark.SINGLE;
      this.value = this.value.replaceAll("''", "'");
    }
  }

  public String getValue() {
    return value;
  }

  public QuotMark getQuotMark() {
    return quotMark;
  }
}
