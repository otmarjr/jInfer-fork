/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.base.interfaces.inference;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Capabilities;
import cz.cuni.mff.ksi.jinfer.base.interfaces.NamedModule;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import java.util.List;

/**
 * Interface of a SchemaGenerator module.
 *
 * <p>Schema Generator is the last module in the inference process. Its task is
 * to transform the simplified grammar (still a list of rules) into a textual
 * schema representation. Schema may be in any format or language, such as
 * DTD or XSD. After the schema is created, it is sent to the Runner module
 * via the callback.</p>
 *
 * @author vektor
 */
public interface SchemaGenerator extends NamedModule, Capabilities {

  /**
   * Start the schema export process. This method is called by the Runner module
   * as the last stage of inference.
   * 
   * @param grammar Simplified grammar to be exported as XML schema.
   * @param callback A callback object. After the schema is created, it must be
   *  returned to the caller by invoking the <code>finished()</code>
   *  method on this object.
   * @throws InterruptedException 
   */
  void start(final List<Element> grammar,
          final SchemaGeneratorCallback callback) throws InterruptedException;
}
