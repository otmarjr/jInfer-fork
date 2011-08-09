/*
 * Copyright (C) 2011 vektor
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.base.interfaces;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;

/**
 * Interface of a module providing search for ID/IDREF attributes, as well as
 * attribute statistics computation and display.
 *
 * @author vektor
 */
public interface IDSetSearch extends NamedModule {

  /**
   * Shows a panel with tools to search for ID/IDREF attributes in the provided
   * grammar.
   *
   * @param panelName Title of the panel that will be displayed.
   * @param grammar Grammar to work on.
   */
  void showIDSetPanel(final String panelName, final List<Element> grammar);

  /**
   * Returns a graph representation of the specified grammar in GraphViz language.
   *
   * Please see
   * {@link cz.cuni.mff.ksi.jinfer.iss.utils.GraphUtils#getGraphVizInput(AMModel)}
   * for details.
   *
   * @param grammar Grammar to create GraphViz input from.
   * @return String representation of the GraphViz input.
   */
  String getGraphVizInput(final List<Element> grammar);

}
