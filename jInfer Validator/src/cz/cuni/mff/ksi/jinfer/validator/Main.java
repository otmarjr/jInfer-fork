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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public final class Main {

  // TODO vektor Refactor
  // TODO vektor Create GUI

  private static final String ANT = "C:\\Program Files\\NetBeans 6.9\\java\\ant\\bin\\ant.bat";
  private static final String PROJECT_ROOT = "C:\\Documents and Settings\\vitasek\\My Documents\\Sukromne\\jinfer";
  private static final List<String> NOT_MODULES = Arrays.asList(".svn", "build", "nbproject");  

  private Main() {
  }

  public static void main(final String[] args) {
    for (final Remark r : checkSuite(ANT, PROJECT_ROOT)) {
      out(r.toString());
    }
  }

  private static List<Remark> checkSuite(final String ant,
          final String projectRoot) {
    final List<Remark> ret = new ArrayList<Remark>();

    ret.addAll(getCompilationRemarks(ant, projectRoot));

    for (final File project : getModuleNames(projectRoot)) {
      ret.addAll(checkModule(project));
    }
    return ret;
  }

  private static List<Remark> checkModule(final File module) {
    final List<Remark> ret = new ArrayList<Remark>();
    final List<File> moduleFiles = getModuleFiles(module);

    for (final File file : moduleFiles) {
      ret.addAll(checkFile(module.getName(), file));
    }

    // TODO vektor Check for missing project metadata - add as ERROR

    return ret;
  }

  private static List<Remark> checkFile(final String module, final File file) {
    final List<Remark> ret = new ArrayList<Remark>();

    final String fileStr = FileHelper.readFileAsString(file.getAbsolutePath());

    if (!fileStr.startsWith("/*")) {
      ret.add(new Remark(module, file,
              0, Severity.WARNING, "License comment not found"));
    }

    final List<String> lines = getFileLines(fileStr);

    int lineNum = 1;
    String fileAuthor = null;
    String classAuthor = null;
    for (final String line : lines) {
      // TODOs
      if (line.contains("TODO")) {
        ret.add(new Remark(module, file, lineNum, Severity.WARNING, line.substring(line.indexOf("TODO"))));
      }
      if (line.contains("FIXME")) {
        ret.add(new Remark(module, file, lineNum, Severity.WARNING, line.substring(line.indexOf("FIXME"))));
      }
      if (line.contains("XXX")) {
        ret.add(new Remark(module, file, lineNum, Severity.WARNING, line.substring(line.indexOf("XXX"))));
      }

      // license, author etc
      if (line.contains("Copyright (C)")) {
        fileAuthor = line.substring(line.lastIndexOf(' ') + 1);
      }
      if (line.contains("@author")) {
        classAuthor = line.substring(line.lastIndexOf(' ') + 1);
      }

      lineNum++;
    }

    if (fileAuthor != null && classAuthor != null
            && !fileAuthor.equals(classAuthor)) {
      ret.add(new Remark(module, file, 0, Severity.ERROR, "File and class authors differ: " + fileAuthor + " vs. " + classAuthor));
    } else {
      if (fileAuthor == null) {
        ret.add(new Remark(module, file, 0, Severity.ERROR, "File has no author in license comment"));
      }
      if (classAuthor == null) {
        ret.add(new Remark(module, file, 0, Severity.ERROR, "Class has no author in JavaDoc"));
      }
    }
    return ret;
  }

  private static List<String> getFileLines(final String fileStr) {
    final List<String> ret = new ArrayList<String>();
    final StringTokenizer st = new StringTokenizer(fileStr, "\r\n");
    while (st.hasMoreTokens()) {
      ret.add(st.nextToken());
    }
    return ret;
  }

  private static List<File> getModuleFiles(final File module) {
    final List<File> ret = new ArrayList<File>();

    for (final File f : FileHelper.getFiles(module)) {
      if (f.isFile() && f.getName().toLowerCase().endsWith(".java")) {
        ret.add(f);
      }
    }

    return ret;
  }

  private static List<File> getModuleNames(final String projectRoot) {
    final List<File> ret = new ArrayList<File>();
    for (final File child : new File(projectRoot).listFiles()) {
      if (child.isDirectory() && !NOT_MODULES.contains(child.getName())) {
        ret.add(child);
      }
    }
    return ret;
  }

  private static List<Remark> getCompilationRemarks(final String ant,
          final String projectRoot) {

    final List<Remark> ret = new ArrayList<Remark>();
    final String antExe =
            "\"" + ant + "\" -f \"" + projectRoot + "\\build.xml\" clean build";
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
              getExceptionStackTraceAsString(err)));
    }
    return ret;
  }

  private static void out(final String s) {
    System.out.println(s);
  }

  public static String getExceptionStackTraceAsString(final Exception exception) {
    final StringWriter sw = new StringWriter();
    exception.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }
}
