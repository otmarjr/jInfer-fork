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

import cz.cuni.mff.ksi.jinfer.attrstats.glpk.options.GlpkPanel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Calendar;
import org.apache.log4j.Logger;
import org.openide.util.NbPreferences;

/**
 * Utility class for GLPK invocation.
 *
 * @author vektor
 */
public final class GlpkRunner {

  private GlpkRunner() {

  }

  private static final Logger LOG = Logger.getLogger(GlpkRunner.class);

  private static final String TMP = System.getProperty("java.io.tmpdir");
  private static final String INPUT = TMP + "glpk_input.txt";
  private static final String OUTPUT = TMP + "glpk_output.txt";

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
    final File input = new File(INPUT);
    final File output = new File(OUTPUT);
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

    final int timeLimit = NbPreferences.forModule(GlpkPanel.class)
            .getInt(GlpkUtils.TIME_LIMIT_PROP, GlpkUtils.TIME_LIMIT_DEFAULT);

    try {
      final long startTime = Calendar.getInstance().getTimeInMillis();

      final ProcessBuilder processBuilder = new ProcessBuilder(getParameters(timeLimit));
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

  private static String[] getParameters(final int timeLimit) {
    if (timeLimit == 0) {
      LOG.info("Run time not limited.");
      return new String[] {
              GlpkUtils.getPath(),
              "--math",
              INPUT,
              "-o",
              OUTPUT
      };
    }
    LOG.info("Time limit set to " + timeLimit + " seconds.");
    return new String[] {
              GlpkUtils.getPath(),
              "--math",
              "--tmlim",
              String.valueOf(timeLimit),
              INPUT,
              "-o",
              OUTPUT
    };
  }

}
