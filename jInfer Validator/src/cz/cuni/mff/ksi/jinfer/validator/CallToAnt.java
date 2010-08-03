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

import cz.cuni.mff.ksi.jinfer.validator.objects.Remark;
import cz.cuni.mff.ksi.jinfer.validator.objects.Severity;
import cz.cuni.mff.ksi.jinfer.validator.utils.Utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A call out to Call to Arms:)
 */
public final class CallToAnt {

  private CallToAnt() {
  }

  public static List<Remark> fixDependencies(final String ant,
          final String projectRoot) {
    final List<Remark> ret = new ArrayList<Remark>();
    final String antExe = getAntExeString(ant, projectRoot, "fix-all-deps");

    try {
      String line;
      final Process p = Runtime.getRuntime().exec(antExe,
              null,
              new File(projectRoot));

      final BufferedReader input =
              new BufferedReader(new InputStreamReader(p.getInputStream()));
      final BufferedReader err =
              new BufferedReader(new InputStreamReader(p.getInputStream()));
      while ((line = input.readLine()) != null) {
        System.out.println(line);
      }
      while ((line = err.readLine()) != null) {
        ret.add(Remark.getError(line));
      }
      input.close();
    } catch (IOException ex) {
      ret.add(new Remark("fix-all-deps", null, null, Severity.ERROR,
              Utils.getExceptionStackTraceAsString(ex)));
    }
    return ret;
  }

  public static List<Remark> getCompilationRemarks(final String ant,
          final String projectRoot) {

    final List<Remark> ret = new ArrayList<Remark>();
    final String antExe =
            getAntExeString(ant, projectRoot, "clean build");
    try {
      String line;
      final Process p = Runtime.getRuntime().exec(antExe,
              null,
              new File(projectRoot));

      final BufferedReader input =
              new BufferedReader(new InputStreamReader(p.getInputStream()));
      final BufferedReader err =
              new BufferedReader(new InputStreamReader(p.getInputStream()));
      while ((line = input.readLine()) != null) {
        if (line.contains("Warning: ")) {
          ret.add(Remark.getWarning(line));
        } else if (line.contains("Error: ")) {
          ret.add(Remark.getError(line));
        }
      }
      while ((line = err.readLine()) != null) {
        ret.add(Remark.getError(line));
      }
      input.close();
    } catch (final Exception err) {
      ret.add(new Remark("compile", null, null, Severity.ERROR,
              Utils.getExceptionStackTraceAsString(err)));
    }
    return ret;
  }

  private static String getAntExeString(final String ant,
          final String projectRoot, final String targets) {
    return "\"" + ant + "\" -f \"" + projectRoot + "\\build.xml\" " + targets;
  }

}
