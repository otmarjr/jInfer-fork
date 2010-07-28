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

import cz.cuni.mff.ksi.jinfer.validator.utils.FileHelper;
import cz.cuni.mff.ksi.jinfer.validator.utils.Utils;
import cz.cuni.mff.ksi.jinfer.validator.objects.ImportantFiles;
import cz.cuni.mff.ksi.jinfer.validator.objects.Remark;
import cz.cuni.mff.ksi.jinfer.validator.objects.Severity;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Logic {

  private static final List<String> AUTHORS = Arrays.asList("vektor", "sviro", "anti", "rio");

  private Logic() {
  }

  public static List<Remark> checkSuite(final String ant,
          final String projectRoot, final boolean compile) {
    final List<Remark> ret = new ArrayList<Remark>();

    if (compile) {
      ret.addAll(getCompilationRemarks(ant, projectRoot));
    }

    for (final File project : FileHelper.getModuleNames(projectRoot)) {
      ret.addAll(checkModule(project));
    }
    return ret;
  }

  private static List<Remark> checkModule(final File module) {
    final List<Remark> ret = new ArrayList<Remark>();
    final List<File> moduleFiles = FileHelper.getModuleFiles(module);

    for (final File file : moduleFiles) {
      ret.addAll(checkFile(module.getName(), file));
    }

    // check manifest.mf
    final File manifestFile = new File(module.getAbsolutePath()
            .concat("/").concat(ImportantFiles.MANIFEST.getLocation()));
    final String manifest = FileHelper.readFileAsString(manifestFile);
    if (!manifest.contains("OpenIDE-Module-Implementation-Version:")) {
      ret.add(new Remark(module.getName(), manifestFile,
              0, Severity.ERROR, "Module implementation version not found"));
    }

    // check project.properties
    final File projectFile = new File(module.getAbsolutePath()
            .concat("/").concat(ImportantFiles.PROJECT_PROPERTIES.getLocation()));
    final String project = FileHelper.readFileAsString(projectFile);

    if (!project.contains("license.file=../gpl30.txt")) {
      ret.add(new Remark(module.getName(), projectFile,
              0, Severity.ERROR, "Module license file not specified"));
    }
    if (!project.contains("nbm.homepage=")) {
      ret.add(new Remark(module.getName(), projectFile,
              0, Severity.ERROR, "Project homepage not specified"));
    }
    if (!project.contains("nbm.module.author=")) {
      ret.add(new Remark(module.getName(), projectFile,
              0, Severity.ERROR, "Project author(s) not specified"));
    }
    if (!project.contains("project.license=gpl30")) {
      ret.add(new Remark(module.getName(), projectFile,
              0, Severity.ERROR, "Module license not specified/incorrect"));
    }
    if (!project.contains("spec.version.base=")) {
      ret.add(new Remark(module.getName(), projectFile,
              0, Severity.ERROR, "Module specification version not found"));
    }

    return ret;
  }

  private static List<Remark> checkFile(final String module, final File file) {
    final List<Remark> ret = new ArrayList<Remark>();

    final String fileStr = FileHelper.readFileAsString(file);

    if (!fileStr.startsWith("/*")) {
      ret.add(new Remark(module, file,
              0, Severity.WARNING, "License comment not found"));
    }

    final List<String> lines = FileHelper.getFileLines(fileStr);

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

    if (fileAuthor != null && !AUTHORS.contains(fileAuthor)) {
      ret.add(new Remark(module, file, 0, Severity.ERROR, "File author unknown: " + fileAuthor));
    }

    if (classAuthor != null && !AUTHORS.contains(classAuthor)) {
      ret.add(new Remark(module, file, 0, Severity.ERROR, "Class author unknown: " + classAuthor));
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
              Utils.getExceptionStackTraceAsString(err)));
    }
    return ret;
  }
}
