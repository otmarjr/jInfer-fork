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
package cz.cuni.mff.ksi.jinfer.validator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class FileHelper {

  private FileHelper() {
  }

  public static List<File> getFiles(
          final File dir) {
    final List<File> result = new ArrayList<File>();
    final File[] filesAndDirs = dir.listFiles();
    final List<File> filesDirs = Arrays.asList(filesAndDirs);
    for (File file : filesDirs) {
      result.add(file);
      if (!file.isFile()) {
        result.addAll(getFiles(file));
      }
    }
    return result;
  }

  public static String readFileAsString(final String filePath) {
    final byte[] buffer = new byte[(int) new File(filePath).length()];
    BufferedInputStream f = null;
    try {
      f = new BufferedInputStream(new FileInputStream(filePath));
      f.read(buffer);
    } catch (final Exception e) {
    } finally {
      if (f != null) {
        try {
          f.close();
        } catch (IOException ignored) {
        }
      }
    }
    return new String(buffer);
  }
}
