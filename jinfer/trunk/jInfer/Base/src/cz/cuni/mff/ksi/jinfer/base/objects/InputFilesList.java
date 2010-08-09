/*
 *  Copyright (C) 2010 sviro
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
package cz.cuni.mff.ksi.jinfer.base.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author sviro
 */
public class InputFilesList extends ArrayList<File> {

  public InputFilesList() {
    super();
  }

  @Override
  public boolean add(final File e) {
    //TODO sviro add file listener
    if (e == null) {
      throw new NullPointerException();
    }
    if (this.contains(e)) {
      return false;
    }
    return super.add(e);
  }

  @Override
  public void add(final int index, final File element) {
    throw new UnsupportedOperationException("This operation is not supported in InputFilesList.");
  }

  @Override
  public boolean addAll(final Collection<? extends File> c) {
    //TODO sviro add file listener
    List<File> list = new InputFilesList();
    for (File file : c) {
      list.add(file);
    }
    return super.addAll(list);
  }

  @Override
  public boolean addAll(int index,
          Collection<? extends File> c) {
    throw new UnsupportedOperationException("This operation is not supported in InputFilesList.");
  }

  @Override
  public void clear() {
    //TODO sviro remove file listener
    super.clear();
  }

  @Override
  public boolean remove(Object o) {
    //TODO sviro remove file listener
    return super.remove(o);
  }

  @Override
  public File remove(int index) {
    //TODO sviro remove file listener
    return super.remove(index);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    //TODO sviro remove file listener
    return super.removeAll(c);
  }

  @Override
  public File set(int index, File element) {
    throw new UnsupportedOperationException("This operation is not supported in InputFilesList.");
  }
}
