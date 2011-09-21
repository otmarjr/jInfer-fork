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
package cz.cuni.mff.ksi.jinfer.iss.experiments.data;

import java.io.File;

/**
 * Class representing an file being used as an input in an experiment.
 *
 * @author vektor
 */
public class InputFile {

  /** The file itself. */
  private final File file;

  /** General characteristic of the file. */
  private final FileCharacteristics characteristics;

  /**
   * Full constructor.
   *
   * @param file String representing the file name.
   * @param characteristics General characteristic of the file.
   */
  public InputFile(final String file, final FileCharacteristics characteristics) {
    this(new File(file), characteristics);
  }

  /**
   * Full constructor.
   *
   * @param file Object representing the file itself.
   * @param characteristics General characteristic of the file.
   */
  public InputFile(final File file, final FileCharacteristics characteristics) {
    this.file = file;
    this.characteristics = characteristics;
  }

  /**
   * Partial constructor. Assumes the file to be of unknown type
   * ({@link FileCharacteristics#UNKNOWN}).
   *
   * @param file String representing the file name.
   */
  public InputFile(final String file) {
    this(new File(file), FileCharacteristics.UNKNOWN);
  }

  /**
   * Partial constructor. Assumes the file to be of unknown type
   * ({@link FileCharacteristics#UNKNOWN}).
   *
   * @param file Object representing the file itself.
   */
  public InputFile(final File file) {
    this(file, FileCharacteristics.UNKNOWN);
  }

  /**
   * Returns the object representing this file.
   */
  public File getFile() {
    return file;
  }

  /**
   * Returns the general characteristic of this file.
   */
  public FileCharacteristics getCharacteristics() {
    return characteristics;
  }

  /**
   * Returns the name (no path, see {@link File#getName()}) of this file.
   */
  public String getName() {
    return file.getName();
  }

  /**
   * Returns the size of this input file in bytes.
   *
   * @return Size in bytes.
   */
  public long getSize() {
    return file.length();
  }

}
