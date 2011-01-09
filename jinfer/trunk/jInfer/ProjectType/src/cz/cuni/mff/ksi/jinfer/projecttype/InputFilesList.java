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
import org.openide.nodes.Children;

/**
 * Array list of {@link File} types that contains no duplicate elements.
 * @author sviro
 */
public class InputFilesList extends ArrayList<File> {

  private static final long serialVersionUID = 353231345;

  private static final String UNSUPPORTED_OPERATION = "This operation is not supported in InputFilesList.";
  private final InputFilesListener listener;
  private Children fileChildren = null;

  public InputFilesList() {
    super();
    this.listener = new InputFilesListener(this);
  }

  /**
   * Appends the specified element to the end of this list if this element is not already part of this list.
   * @param e Element to be appended to this list.
   * @return <tt>true</tt> (as specified by {@link Collection#add})
   */
  @Override
  public boolean add(final File e) {
    if (e == null) {
      throw new NullPointerException(); //NOPMD
    }
    if (this.contains(e)) {
      return false;
    }
    final boolean result = super.add(e);
    if (result) {
      FileUtil.addFileChangeListener(listener, e);
    }
    return result;
  }

  /**
   * This method is unsupported.
   * @param index
   * @param element
   * @throws UnsupportedOperationException {@inheritDoc}
   */
  @Override
  public void add(final int index, final File element) {
    throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
  }

  /**
   * Appends all of the elements in the specified collection which are not already in this list to the end of
   * this list, in the order that they are returned by the
   * specified collection's Iterator.  The behavior of this operation is
   * undefined if the specified collection is modified while the operation
   * is in progress.  (This implies that the behavior of this call is
   * undefined if the specified collection is this list, and this
   * list is nonempty.)
   *
   * @param c Collection containing elements to be added to this list
   * @return <tt>true</tt> if this list changed as a result of the call
   * @throws NullPointerException if the specified collection is null
   */
  @Override
  public boolean addAll(final Collection<? extends File> c) {
    if (c == null) {
      throw new NullPointerException(); //NOPMD
    }

    final List<File> list = new InputFilesList();
    for (File file : c) {
      if (!this.contains(file) && list.add(file)) {
        FileUtil.addFileChangeListener(listener, file);
      }
    }
    return super.addAll(list);
  }

  /**
   * This method is unsupported.
   * @param index
   * @param c
   * @return
   * @throws UnsupportedOperationException {@inheritDoc}
   */
  @Override
  public boolean addAll(final int index, final Collection<? extends File> c) {
    throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
  }

  /**
   * Removes all of the elements from this list. The list will
   * be empty after this call returns.
   */
  @Override
  public void clear() {
    for (File file : this) {
      FileUtil.removeFileChangeListener(listener, file);
    }
    super.clear();
  }

  /**
   * Removes the first occurrence of the specified element from this list,
   * if it is present.  If the list does not contain the element, it is
   * unchanged.  More formally, removes the element with the lowest index
   * <tt>i</tt> such that
   * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
   * (if such an element exists).  Returns <tt>true</tt> if this list
   * contained the specified element (or equivalently, if this list
   * changed as a result of the call).
   *
   * @param o Element to be removed from this list, if present
   * @return <tt>true</tt> if this list contained the specified element
   */
  @Override
  public boolean remove(final Object o) {
    final boolean result = super.remove(o);
    if (result) {
      FileUtil.removeFileChangeListener(listener, (File) o);
    }

    return result;
  }

  /**
   * Removes the element at the specified position in this list.
   * Shifts any subsequent elements to the left (subtracts one from their
   * indices).
   *
   * @param index The index of the element to be removed
   * @return The element that was removed from the list
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  @Override
  public File remove(final int index) {
    final File result = super.remove(index);

    FileUtil.removeFileChangeListener(listener, result);

    return result;
  }

  /**
   * This method is unsupported.
   * @param c
   * @return
   * @throws UnsupportedOperationException {@inheritDoc}
   */
  @Override
  public boolean removeAll(final Collection<?> c) {
    throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
  }

  /**
   * This method is unsupported.
   * @param index
   * @param element
   * @return
   * @throws UnsupportedOperationException {@inheritDoc}
   */
  @Override
  public File set(final int index, final File element) {
    throw new UnsupportedOperationException(UNSUPPORTED_OPERATION);
  }

  /**
   * Get {@link Children} represented by this list.
   * @return {@link Children} represented by this list.
   */
  public Children getFileChildren() {
    return fileChildren;
  }

  /**
   * Set {@link Children} represented by this list.
   * @param fileChildren {@link Children} to be represented by this list.
   */
  public void setFileChildren(final Children fileChildren) {
    this.fileChildren = fileChildren;
  }
}
