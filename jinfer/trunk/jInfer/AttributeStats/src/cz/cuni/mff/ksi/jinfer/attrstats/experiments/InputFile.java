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
package cz.cuni.mff.ksi.jinfer.attrstats.experiments;

import java.io.File;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class InputFile {

  private final File file;

  private final FileCharacteristics characteristics;

  public InputFile(final String file, final FileCharacteristics characteristics) {
    this(new File(file), characteristics);
  }

  public InputFile(final File file, final FileCharacteristics characteristics) {
    this.file = file;
    this.characteristics = characteristics;
  }

  public InputFile(final String file) {
    this(new File(file), FileCharacteristics.UNKNOWN);
  }

  public InputFile(final File file) {
    this(file, FileCharacteristics.UNKNOWN);
  }

  public File getFile() {
    return file;
  }

  public FileCharacteristics getCharacteristics() {
    return characteristics;
  }

  public String getName() {
    return file.getName();
  }

  public long getSize() {
    return file.length();
  }

}
