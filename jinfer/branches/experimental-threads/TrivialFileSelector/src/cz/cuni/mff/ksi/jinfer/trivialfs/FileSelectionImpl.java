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
package cz.cuni.mff.ksi.jinfer.trivialfs;

import cz.cuni.mff.ksi.jinfer.base.interfaces.FileSelection;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;

/**
 * A trivial implementation of FileSelector module.
 *
 * @author vektor
 */
public class FileSelectionImpl implements FileSelection {

  @Override
  public Input getInput() {
    return new Input(
            Arrays.asList(new File("C:\\domain.xml"), new File("C:\\domain2.xml")),
            Collections.<File>emptyList(),
            Collections.<File>emptyList());
  }

}
