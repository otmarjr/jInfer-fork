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

import cz.cuni.mff.ksi.jinfer.validator.objects.ConsumedPackage;
import cz.cuni.mff.ksi.jinfer.validator.objects.FileInfo;
import cz.cuni.mff.ksi.jinfer.validator.utils.FileHelper;
import cz.cuni.mff.ksi.jinfer.validator.objects.ImportantFiles;
import cz.cuni.mff.ksi.jinfer.validator.objects.ModuleInfo;
import cz.cuni.mff.ksi.jinfer.validator.objects.ProvidedPackage;
import cz.cuni.mff.ksi.jinfer.validator.objects.Remark;
import cz.cuni.mff.ksi.jinfer.validator.objects.Severity;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Logic {

  private static final List<String> AUTHORS = Arrays.asList("vektor", "sviro", "anti", "rio", "riacik", "reseto", "Julie Vyhnanovska");

  private Logic() {
  }

  public static List<Remark> checkSuite(final String ant,
          final String projectRoot, final boolean compile) {
    final List<Remark> ret = new ArrayList<Remark>();

    if (compile) {
      ret.addAll(CallToAnt.getCompilationRemarks(ant, projectRoot));
    }

    final List<ProvidedPackage> providedPackages = new ArrayList<ProvidedPackage>();
    final List<ConsumedPackage> consumedPackages = new ArrayList<ConsumedPackage>();
    for (final File project : FileHelper.getModuleNames(projectRoot)) {
      final ModuleInfo info = checkModule(project);
      ret.addAll(info.getRemarks());
      providedPackages.addAll(info.getProvidedPackages());
      consumedPackages.addAll(info.getConsumedPackages());
    }

    for (final ProvidedPackage providedPackage : providedPackages) {
      boolean found = false;
      for (final ConsumedPackage consumedPackage : consumedPackages) {
        if (consumedPackage.getName().contains(providedPackage.getName())
                && !consumedPackage.getModule().equals(providedPackage.getModule())) {
          found = true;
          break;
        }
      }
      if (!found) {
        ret.add(new Remark(providedPackage.getModule(), null, null,
                Severity.ERROR, "Package " + providedPackage.getName() +
                " is provided but never used outside"));
      }
    }

    return ret;
  }

  private static ModuleInfo checkModule(final File module) {
    final ModuleInfo ret = new ModuleInfo();
    final List<File> moduleFiles = FileHelper.getModuleFiles(module);

    for (final File file : moduleFiles) {
      final FileInfo info = checkFile(module.getName(), file);
      ret.getRemarks().addAll(info.getRemarks());
      ret.getConsumedPackages().addAll(info.getConsumedPackages());
    }

    // check manifest.mf
    final File manifestFile = new File(module.getAbsolutePath()
            .concat("/").concat(ImportantFiles.MANIFEST.getLocation()));
    final String manifest = FileHelper.readFileAsString(manifestFile);
    if (!manifest.contains("OpenIDE-Module-Implementation-Version:")) {
      ret.getRemarks().add(new Remark(module.getName(), manifestFile,
              null, Severity.ERROR, "Module implementation version not found"));
    }

    // check project.properties
    final File projectFile = new File(module.getAbsolutePath()
            .concat("/").concat(ImportantFiles.PROJECT_PROPERTIES.getLocation()));
    final String project = FileHelper.readFileAsString(projectFile);

    if (!project.contains("license.file=../gpl30.txt")) {
      ret.getRemarks().add(new Remark(module.getName(), projectFile,
              null, Severity.ERROR, "Module license file not specified (license.file)"));
    }
    if (!project.contains("nbm.homepage=http://jinfer.sourceforge.net/")) {
      ret.getRemarks().add(new Remark(module.getName(), projectFile,
              null, Severity.ERROR, "Project homepage not specified correctly (nbm.homepage)"));
    }
    if (!project.contains("nbm.module.author=")) {
      ret.getRemarks().add(new Remark(module.getName(), projectFile,
              null, Severity.ERROR, "Project author(s) not specified (nbm.module.author)"));
    }
    if (!project.contains("project.license=gpl30")) {
      ret.getRemarks().add(new Remark(module.getName(), projectFile,
              null, Severity.ERROR, "Module license not specified/incorrect (project.license)"));
    }
    if (!project.contains("spec.version.base=")) {
      ret.getRemarks().add(new Remark(module.getName(), projectFile,
              null, Severity.ERROR, "Module specification version not found (spec.version.base)"));
    }

    // check project.xml
    final File projectXmlFile = new File(module.getAbsolutePath()
            .concat("/").concat(ImportantFiles.PROJECT_XML.getLocation()));
    final String projectXml = FileHelper.readFileAsString(projectXmlFile);
    final List<String> lines = FileHelper.getFileLines(projectXml);
    for (final String line : lines) {
      if (line.contains("<package>") && line.contains("</package>")
              && line.contains("cz.cuni.mff.ksi.jinfer")) {
        ret.getProvidedPackages().add(new ProvidedPackage(
                module.getName(),
                line.substring(line.indexOf("<package>") + 9, line.indexOf("</package>"))));
      }
    }

    return ret;
  }

  private static FileInfo checkFile(final String module, final File file) {
    final FileInfo ret = new FileInfo();

    final String fileStr = FileHelper.readFileAsString(file);

    if (!fileStr.startsWith("/*")) {
      ret.getRemarks().add(new Remark(module, file,
              null, Severity.WARNING, "License comment not found"));
    }

    if (fileStr.startsWith("/**") && !isSpecialCase(file)) {
      ret.getRemarks().add(new Remark(module, file,
              null, Severity.WARNING, "File starts with JavaDoc"));
    }

    final List<String> lines = FileHelper.getFileLines(fileStr);

    int lineNum = 1;
    String fileAuthor = null;
    String classAuthor = null;
    for (final String line : lines) {
      // TODOs
      if (line.contains("TODO")) {
        ret.getRemarks().add(new Remark(module, file, lineNum, Severity.TODO, line.substring(line.indexOf("TODO"))));
      }
      if (line.contains("FIXME")) {
        ret.getRemarks().add(new Remark(module, file, lineNum, Severity.WARNING, line.substring(line.indexOf("FIXME"))));
      }
      if (line.contains("XXX")) {
        ret.getRemarks().add(new Remark(module, file, lineNum, Severity.WARNING, line.substring(line.indexOf("XXX"))));
      }

      // license, author etc
      if (line.contains("Copyright (C)")) {
        fileAuthor = line.substring(line.lastIndexOf(' ') + 1);
      }
      if (line.contains("@author")) {
        classAuthor = line.substring(line.lastIndexOf(' ') + 1);
      }

      // imports
      try {
        if (line.trim().startsWith("import ")) {
        ret.getConsumedPackages().add(new ConsumedPackage(module, file.getName(),
                line.substring(line.indexOf("import ") + 7, line.indexOf(";"))));
        }
      } catch (IndexOutOfBoundsException e) {
        System.err.println("Index out of bounds when checking for imports in file " + file.getName());
        e.printStackTrace();
        System.err.println("Try to clean and build jInfer project.");
      }

      lineNum++;
    }

    if (fileAuthor != null && classAuthor != null
            && !fileAuthor.equals(classAuthor)) {
      ret.getRemarks().add(new Remark(module, file, null, Severity.ERROR, "File and class authors differ: " + fileAuthor + " vs. " + classAuthor));
    } else {
      if (fileAuthor == null && !isSpecialCase(file)) {
        ret.getRemarks().add(new Remark(module, file, null, Severity.ERROR, "File has no author in license comment"));
      }
      if (classAuthor == null && !isSpecialCase(file)) {
        ret.getRemarks().add(new Remark(module, file, null, Severity.ERROR, "Class has no author in JavaDoc"));
      }
    }

    if (fileAuthor != null && !AUTHORS.contains(fileAuthor)) {
      ret.getRemarks().add(new Remark(module, file, null, Severity.ERROR, "File author unknown: " + fileAuthor));
    }

    if (classAuthor != null && !AUTHORS.contains(classAuthor)) {
      ret.getRemarks().add(new Remark(module, file, null, Severity.ERROR, "Class author unknown: " + classAuthor));
    }

    return ret;
  }

  private static boolean isSpecialCase(final File file) {
    if (file.getName().equals("package-info.java")) {
      return true;
    }
    return false;
  }
}