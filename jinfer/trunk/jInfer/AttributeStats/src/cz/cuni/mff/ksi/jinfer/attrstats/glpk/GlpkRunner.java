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
import java.util.Calendar;
import org.apache.log4j.Logger;

/**
 * Utility class for GLPK invocation.
 *
 * @author vektor
 */
public final class GlpkRunner {

  private GlpkRunner() {

  }

  private static final Logger LOG = Logger.getLogger(GlpkRunner.class);

  private static final String INPUT = "glpk_input.txt";
  private static final String OUTPUT = "glpk_output.txt";
  private static final String TMP = System.getProperty("java.io.tmpdir");
  /** Time limit for GLPK run in seconds. */
  private static final int TMLIM = 60;

  /**
   * Constructs the GLPK problem formulation from the provided model, runs
   * GLPK optimalization and returns the resulting file in a string. This can
   * be directly displayed for verification or parsed for actual AMs appearing
   * in the "optimal" ID set.
   *
   * @param model Model to find "optimal" ID set in.
   * @return String representation of GLPK optimalization output.
   */
  public static String run(final AMModel model) throws InterruptedException {
    final File input = new File(TMP + INPUT);
    final File output = new File(TMP + OUTPUT);
    PrintWriter pw = null;
    final StringBuilder ret = new StringBuilder();
    try {
      pw = new PrintWriter(input);
      pw.write(GlpkInputGenerator.generateGlpkInput(model));
    } catch (final IOException e) {
      throw new RuntimeException(e);
    } finally {
      pw.close();
    }

    LOG.info("Time limit set to " + TMLIM + " seconds.");

    try {
      final long startTime = Calendar.getInstance().getTimeInMillis();

      final ProcessBuilder processBuilder = new ProcessBuilder(Arrays.asList(
              GlpkUtils.getPath(),
              "--math",
              "--tmlim",
              String.valueOf(TMLIM),
              TMP + INPUT,
              "-o",
              TMP + OUTPUT));
      final Process process = processBuilder.start();

      final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        ret.append(line).append('\n');
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
      }

      process.getOutputStream().close();

      final BufferedReader outputReader = new BufferedReader(new FileReader(output));
      while ((line = outputReader.readLine()) != null) {
        ret.append(line).append('\n');
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }
      }

      input.delete();
      output.delete();

      LOG.info("GLPK run took " + (Calendar.getInstance().getTimeInMillis() - startTime) + " ms.");

      return ret.toString();
    } catch (final IOException e) {
      LOG.error("Error running GLPK.", e);
      return ret.toString() + "\nRunning GLPK encountered an error, check log for details.";
    }
  }

}
