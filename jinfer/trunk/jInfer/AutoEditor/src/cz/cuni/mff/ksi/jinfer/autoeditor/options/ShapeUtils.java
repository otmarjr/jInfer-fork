/*
 *  Copyright (C) 2011 rio
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

package cz.cuni.mff.ksi.jinfer.autoeditor.options;

import cz.cuni.mff.ksi.jinfer.base.objects.VertexShape;
import org.openide.util.NbPreferences;

/**
 * Utility class for AutoEditor node shapes.
 * @author rio
 */
public final class ShapeUtils {

  public static final String REGULAR_NODE_SHAPE_PROP = "regularnode.shape";
  public static final VertexShape REGULAR_NODE_SHAPE_DEFAULT = VertexShape.CIRCLE;
  public static final String SUPERINITIAL_NODE_SHAPE_PROP = "superinitialnode.shape";
  public static final VertexShape SUPERINITIAL_NODE_SHAPE_DEFAULT = VertexShape.ROUNDED_SQUARE;
  public static final String SUPERFINAL_NODE_SHAPE_PROP = "superfinalnode.shape";
  public static final VertexShape SUPERFINAL_NODE_SHAPE_DEFAULT = VertexShape.ROUNDED_SQUARE;

  private ShapeUtils() {
  }

  /**
   * Get {@link VertexShape} property value for a particular property.
   * @param property Name of the property to get the value for.
   * @param defaultValue Default value to be returned, if no value is saved for the particular property.
   * @return Property value for the particular property if exists, otherwise defaultValue.
   */
  public static VertexShape getShapeProperty(final String property, final VertexShape defaultValue) {
    return VertexShape.valueOf(NbPreferences.forModule(AutoEditorPanel.class).get(property, defaultValue.name()));
  }

}
