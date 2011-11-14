/*
 *  Copyright (C) 2011 sviro
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

import java.awt.Color;
import org.openide.util.NbPreferences;

/**
 * Utility class for Automaton Editor node colors.
 * @author sviro
 */
public final class ColorUtils {

  public static final String BG_COLOR_PROP = "background.color";
  public static final Color BG_COLOR_DEFAULT = Color.decode("-1");
  public static final String NODE_COLOR_PROP = "node.color";
  public static final Color NODE_COLOR_DEFAULT = Color.decode("-13861729");
  public static final String FINAL_COLOR_PROP = "final.color";
  public static final Color FINAL_COLOR_DEFAULT = Color.decode("-1936099");
  public static final String PICKED_COLOR_PROP = "picked.color";
  public static final Color PICKED_COLOR_DEFAULT = Color.yellow;

  private ColorUtils() {
  }

  /**
   * Get {@link Color} property value for particular property.
   * @param property Property for which to get value.
   * @param defaultValue Default value to be returned, if no value is saved for particular property.
   * @return Property value if exists for particular property, otherwise is returned defaultValue.
   */
  public static Color getColorProperty(final String property, final Color defaultValue) {
    return Color.decode(NbPreferences.forModule(AutoEditorPanel.class).get(property, String.valueOf(defaultValue.getRGB())));
  }

  /**
   * Get background color of canvas where automaton is displayed.
   * @return Background color of canvas.
   */
  public static Color getBackgroundColor() {
    return getColorProperty(BG_COLOR_PROP, BG_COLOR_DEFAULT);
  }
}
