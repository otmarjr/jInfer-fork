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
 * This class represents the detail information about node kind test.
 * 
 * These information are: name, type and nillable type.
 *
 * @author Jiri Schejbal
 */
public class KindTestSuffix {

  private String name;
  private String type = null;
  private Boolean isNillable = null;

  public KindTestSuffix(String name, String type) {
    assert (name != null);
    this.name = name;
    this.type = type;
  }

  public KindTestSuffix(String name, NillableTypeName type) {
    assert (name != null);
    this.name = name;
    if (type != null) {
      this.type = type.getName();
      isNillable = type.getIsNillable();
    }
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public Boolean getIsNillable() {
    return isNillable;
  }
}
