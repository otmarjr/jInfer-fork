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
package cz.cuni.mff.ksi.jinfer.base.interfaces;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import java.io.InputStream;
import java.util.List;

/**
 * Interface of any class that can take an InputStream and produce IG rules 
 * from it. The class must work as a singleton, subsequent calls to
 * {@see cz.cuni.mff.ksi.jinfer.base.interfaces.Processor#process()}
 * will be on the same instance.
 * 
 * @author vektor
 */
public interface Processor {

  /**
   * Which folder does this processor operate on.
   *
   * @return Folder on which this processor operates.
   */
  FolderType getFolder();

  /**
   * Which extension can this processor process.
   *
   * @return File extension this processor can process.
   */
  String getExtension();

  /**
   * Whether this processor can process also different extensions than that
   * specified in 
   * {@see cz.cuni.mff.ksi.jinfer.base.interfaces.Processor#getExtension()}.
   * 
   * @return True if this processor can process files with extensions
   * different from what 
   * {@see cz.cuni.mff.ksi.jinfer.base.interfaces.Processor#getExtension()}
   * reports. False otherwise.
   */
  boolean processUndefined();

  /**
   * Returns the rules contained in the input.
   * 
   * @param s Input of any arbitrary type.
   * @return Rules contained within. Empty list if there are no rules or an error occurs.
   */
  List<Element> process(final InputStream s);
}
