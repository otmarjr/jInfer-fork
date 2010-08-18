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
package cz.cuni.mff.ksi.jinfer.projecttype;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author sviro
 */
public class InputFilesList extends ArrayList<File> {

  private static final String UNSUPPORTED_OPERATION = "This operation is not supported in InputFilesList.";
  private final InputFilesListener listener;

  public InputFilesList() {
    super();
    this.listener = new InputFilesListener(this);
  }

  @Override
  public boolean add(final File e) {
    if (e == null) {
      throw new NullPointerException();
    }
    if (this.contains(e)) {
      return false;
    }
    boolean result = super.add(e);
    if (result) {
      FileUtil.addFileChangeListener(listener, e);
    }
    return result;
  }

  @Override
  public void add(final int index, final File element) {
    throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
  }

  @Override
  public boolean addAll(final Collection<? extends File> c) {
    final List<File> list = new InputFilesList();
    for (File file : c) {
      if (!this.contains(file)) {
        if (list.add(file)) {
          FileUtil.addFileChangeListener(listener, file);
        }
      }
    }
    return super.addAll(list);
  }

  @Override
  public boolean addAll(final int index, final Collection<? extends File> c) {
    throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
  }

  @Override
  public void clear() {
    for (File file : this) {
      FileUtil.removeFileChangeListener(listener, file);
    }
    super.clear();
  }

  @Override
  public boolean remove(final Object o) {
    final boolean result = super.remove(o);
    if (result) {
      FileUtil.removeFileChangeListener(listener, (File) o);
    }

    return result;
  }

  @Override
  public File remove(final int index) {
    final File result = super.remove(index);

    FileUtil.removeFileChangeListener(listener, result);

    return result;
  }

  @Override
  public boolean removeAll(final Collection<?> c) {
    throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
  }

  @Override
  public File set(final int index, final File element) {
    throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
  }

  public InputFilesListener getListener() {
    return listener;
  }
}
