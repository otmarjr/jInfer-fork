/*
 *  Copyright (C) 2011 sviro
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

package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.graphviz.properties;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.openide.util.NbPreferences;

/**
 *
 * @author sviro
 */
public class GraphvizUtils {

  public static final String BINARY_PATH_PROP = "dot.binary";
  public static final String BINARY_PATH_DEFAULT = "";

  public static String getPath() {
    return NbPreferences.forModule(GraphvizLayoutPanel.class).get(BINARY_PATH_PROP, BINARY_PATH_DEFAULT);
  }

  public static boolean isBinaryValid() {
    return isBinaryValid(getPath());
  }

  public static boolean isBinaryValid(String pathToBinary) {
    if ("".equals(pathToBinary)) {
      return true;
    }

    File binaryFile = new File(pathToBinary);
    if (binaryFile.isFile() && binaryFile.canExecute()) {
      ProcessBuilder pb = new ProcessBuilder(pathToBinary, "-V");
      try {
        Process process = pb.start();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String output = errorReader.readLine();
        if (output != null && output.startsWith("dot - graphviz version")) {
          return true;
        }

      } catch (IOException ex) {
        return false;
      }
    }

    return false;
  }

}