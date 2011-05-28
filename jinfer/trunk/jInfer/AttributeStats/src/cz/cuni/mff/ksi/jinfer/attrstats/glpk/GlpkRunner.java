/*
 * Copyright (C) 2011 vektor
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.attrstats.glpk;

import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public final class GlpkRunner {

  private GlpkRunner() {

  }

  private static final String TMP = System.getProperty("java.io.tmpdir");

  public static String run(final AMModel model) {
    PrintWriter pw = null;
    final StringBuilder ret = new StringBuilder();
    try {
      final String input = GlpkInputGenerator.generateGlpkInput(model);
      pw = new PrintWriter(TMP + "/glpk_input.txt");
      pw.write(input);
    } catch (final IOException ex) {
      ex.printStackTrace();
    } finally {
      pw.close();
    }

    try {
      final ProcessBuilder processBuilder = new ProcessBuilder(Arrays.asList(GlpkUtils.getPath(), "--math", TMP + "glpk_input.txt", "-o", TMP + "glpk_output.txt"));
      final Process process = processBuilder.start();

      final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
      process.getOutputStream().close();

      String line;
      while ((line = reader.readLine()) != null) {
        ret.append(line).append('\n');
      }

      final BufferedReader outputReader = new BufferedReader(new FileReader(TMP + "glpk_output.txt"));
      while ((line = outputReader.readLine()) != null) {
        ret.append(line).append('\n');
      }

      final File input = new File(TMP + "glpk_input.txt");
      input.delete();
      final File output = new File(TMP + "glpk_output.txt");
      output.delete();

      return ret.toString();
    } catch (final IOException e) {
      e.printStackTrace();
    }

    return null;
  }

}
