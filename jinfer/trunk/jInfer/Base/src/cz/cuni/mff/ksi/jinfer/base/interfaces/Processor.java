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

import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import java.io.InputStream;
import java.util.List;

/**
 * Interface of any class that can take an InputStream and produce {@link List} of elements T
 * from it. The class must work as a singleton, subsequent calls to
 * {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor#process(InputStream)}
 * will be on the same instance.
 * 
 * @param <T> Type of items returned in List by {@link #process(java.io.InputStream) } method.
 * @author vektor
 */
public interface Processor<T> {

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
   * {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor#getExtension()}.
   * 
   * @return True if this processor can process files with extensions
   * different from what 
   * {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor#getExtension()}
   * reports. False otherwise.
   */
  boolean processUndefined();

  /**
   * Returns the {@link List} of type defined for this processor which represents the input.
   * 
   * @param s Input of any arbitrary type.
   * @return Rules contained within. Empty list if there are no rules or an error occurs.
   * @throws InterruptedException When user interrupts the processing operation at any moment.
   */
  List<T> process(final InputStream s) throws InterruptedException;
  
  /**
   * Get {@link Class} of the type this processor returns by process method within {@link List}.
   * @return {@link Class} of the type this processor returns by process method within {@link List}.
   */
  Class<?> getResultType();
}
