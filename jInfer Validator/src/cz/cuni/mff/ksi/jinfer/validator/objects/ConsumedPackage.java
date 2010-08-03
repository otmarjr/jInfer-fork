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
package cz.cuni.mff.ksi.jinfer.validator.objects;

public class ConsumedPackage {

  private final String module;

  private final String file;

  private final String name;

  public ConsumedPackage(final String module, final String file,
          final String name) {
    this.module = module;
    this.file = file;
    this.name = name;
  }

  public String getModule() {
    return module;
  }

  public String getFile() {
    return file;
  }

  public String getName() {
    return name;
  }

}
