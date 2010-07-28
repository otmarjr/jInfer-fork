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
package cz.cuni.mff.ksi.jinfer.validator.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public final class FileHelper {

  private static final List<String> NOT_MODULES = Arrays.asList(".svn", "build", "nbproject");

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

  public static String readFileAsString(final File filePath) {
    final byte[] buffer = new byte[(int) filePath.length()];
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

  public static List<String> getFileLines(final String fileStr) {
    final List<String> ret = new ArrayList<String>();
    final StringTokenizer st = new StringTokenizer(fileStr, "\r\n");
    while (st.hasMoreTokens()) {
      ret.add(st.nextToken());
    }
    return ret;
  }

  public static List<File> getModuleFiles(final File module) {
    final List<File> ret = new ArrayList<File>();

    for (final File f : FileHelper.getFiles(module)) {
      if (f.isFile() && f.getName().toLowerCase().endsWith(".java")) {
        ret.add(f);
      }
    }

    return ret;
  }

  public static List<File> getModuleNames(final String projectRoot) {
    final List<File> ret = new ArrayList<File>();
    for (final File child : new File(projectRoot).listFiles()) {
      if (child.isDirectory() && !NOT_MODULES.contains(child.getName())) {
        ret.add(child);
      }
    }
    return ret;
  }
}
