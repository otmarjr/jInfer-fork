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
package cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk;

import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
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

  /**
   * @see GlpkRunner#run(cz.cuni.mff.ksi.jinfer.iss.objects.AMModel, java.util.List, double, double, int)
   */
  public static String run(final AMModel model,
          final double alpha, final double beta, final int timeLimit)
          throws InterruptedException {
    return run(model, Collections.<AttributeMappingId>emptyList(), alpha, beta, timeLimit);
  }

  /**
   * Constructs the GLPK problem formulation from the provided model, runs
   * GLPK optimalization and returns the resulting file in a string. This can
   * be directly displayed for verification or parsed for actual AMs appearing
   * in the "optimal" ID set.
   *
   * @param model Model to find "optimal" ID set in.
   * @param fixed List of attribute mappings that should be present in the
   * solution.
   * @param alpha Weight of the attribute mapping <cite>support</cite> in its total weight.
   * @param beta Weight of the attribute mapping <cite>coverage</cite> in its total weight.
   * @param timeLimit Time limit for this GLPK run in seconds.
   *
   * @return String representation of GLPK optimalization output.
   */
  public static String run(final AMModel model,
          final List<AttributeMappingId> fixed,
          final double alpha, final double beta, final int timeLimit)
          throws InterruptedException {
    try {
      return run(GlpkInputGenerator.generateGlpkInput(model, fixed, alpha, beta), timeLimit);
    }
    catch (final IOException e) {
      LOG.error("Error creating GLPK input.", e);
      return "Creating GLPK input encountered an error, check log for details.";
    }
  }

  /**
   * Runs GLPK optimalization on the specified input (has to be already in
   * MathProg format) and returns the resulting file in a string. This can
   * be directly displayed for verification or parsed for actual AMs appearing
   * in the "optimal" ID set.
   *
   * @param inputString String representation of the GLPK input in MathProg
   *   format.
   * @param timeLimit Time limit for this GLPK run in seconds.
   *
   * @return String representation of GLPK optimalization output.
   */
  public static String run(final String inputString, final int timeLimit)
        throws InterruptedException {
    final File inputFile = new File(GlpkUtils.INPUT);
    final File outputFile = new File(GlpkUtils.OUTPUT);

    inputFile.delete();
    outputFile.delete();

    FileUtils.writeString(inputString, inputFile);

    final StringBuilder ret = new StringBuilder();
    try {
      final long startTime = Calendar.getInstance().getTimeInMillis();

      final ProcessBuilder processBuilder = new ProcessBuilder(GlpkUtils.getParameters(timeLimit));
      final Process process = processBuilder.start();
      FileUtils.readerToBuilder(new InputStreamReader(process.getInputStream()), ret);
      process.getOutputStream().close();
      FileUtils.readerToBuilder(new FileReader(outputFile), ret);

      inputFile.delete();
      outputFile.delete();

      LOG.info("GLPK run took " + (Calendar.getInstance().getTimeInMillis() - startTime) + " ms.");

      return ret.toString();
    } catch (final IOException e) {
      LOG.error("Error running GLPK.", e);
      return ret.toString() + "\nRunning GLPK encountered an error, check log for details.";
    }
  }
}
