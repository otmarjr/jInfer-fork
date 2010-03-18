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
package cz.cuni.mff.ksi.jinfer.fileselector;

import cz.cuni.mff.ksi.jinfer.base.interfaces.FileSelection;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author sviro
 */
public class FileModel implements FileSelection {

  private final Input input;

  public FileModel() {
    input = new Input(new ArrayList<File>(), new ArrayList<File>(), new ArrayList<File>());
  }

  @Override
  public Input getInput() {
    return input;
  }

}
