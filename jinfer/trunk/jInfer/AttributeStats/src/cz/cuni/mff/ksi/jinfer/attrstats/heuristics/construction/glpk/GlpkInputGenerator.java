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
package cz.cuni.mff.ksi.jinfer.attrstats.heuristics.construction.glpk;

import cz.cuni.mff.ksi.jinfer.attrstats.MappingUtils;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.base.objects.Pair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Utility class for conversion from {@link AMModel} to GLPK MathProg plaintext
 * format.
 *
 * @author vektor
 */
public final class GlpkInputGenerator {

  private GlpkInputGenerator() {

  }

  private static final Logger LOG = Logger.getLogger(GlpkInputGenerator.class);

  private static final String TEMPLATE = "/cz/cuni/mff/ksi/jinfer/attrstats/heuristics/construction/glpk/GlpkInputTemplate.txt";
  private static final String CONSTRAINT = "s.t. c{index}: x['{mapping1}'] + x['{mapping2}'] <= 1;";

  /**
   * Generates a GLPK MathProg representation of the ID set optimization for the
   * provided model.
   *
   * In this representation, we need to provide variables, constraints and the
   * objective function.
   * <ul>
   *   <li>Every <cite>candidate</cite> attribute mapping corresponds to one
   *      binary variable (<code>am<sub>i</sub></code>).</li>
   *   <li>For each two AMs <code>am<sub>i</sub></code>,
   *      <code>am<sub>j</sub></code> that cannot be in the same ID set together,
   *      a constraint is added: <code>am<sub>i</sub> + am<sub>j</sub> &lt;= 1</code>.
   *      Two AMs cannot be in the same ID set together, if:
   *      <ul>
   *        <li>Their type (element name) is the same, or</li>
   *        <li>Their images intersect.</li>
   *      </ul>
   *   </li>
   *   <li>The objective function is a weighted sum of active variables, where
   *      their weight is defined by {@link MappingUtils#weight(AttributeMappingId, AMModel, double, double) }.</li>
   * </ul>
   *
   * @param model Model from which the problem formulation should be generated.
   * @param alpha Weight of the attribute mapping <cite>support</cite> in its total weight.
   * @param beta Weight of the attribute mapping <cite>coverage</cite> in its total weight.
   *
   * @return String representation of the problem formulation in MathProg
   * language, that can be directly passed to GLPK Solver.
   */
  public static String generateGlpkInput(final AMModel model,
          final double alpha, final double beta) throws InterruptedException {
    final long startTime = Calendar.getInstance().getTimeInMillis();
    final List<AttributeMappingId> candidates = new ArrayList<AttributeMappingId>();
    for (final AttributeMappingId mapping : model.getAMs().keySet()) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      if (MappingUtils.isCandidateMapping(mapping, model)) {
        candidates.add(mapping);
      }
    }

    Collections.shuffle(candidates);

    final StringBuilder mappings = new StringBuilder();
    final StringBuilder weights = new StringBuilder();

    for (final AttributeMappingId mapping : candidates) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      final String name = GlpkUtils.getName(mapping);
      mappings.append(name).append('\n');
      weights.append(name)
              .append(' ')
              .append(model.weight(mapping, alpha, beta))
              .append('\n');
    }

    try {
      final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(TEMPLATE);
      final BufferedReader br = new BufferedReader(new InputStreamReader(is));
      final StringBuilder ret = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        ret.append(line).append('\n');
      }

      final Pair<CharSequence, Integer> constraints = getConstraints(candidates, model);

      final String result = ret.toString()
              .replace("{constraints}", constraints.getFirst())
              .replace("{mappings}", mappings)
              .replace("{weights}", weights);

      LOG.info("Graph interpretation: " + candidates.size() + " vertices, "
              + constraints.getSecond().intValue() + " edges.");
      LOG.info("Input generator took "
              + (Calendar.getInstance().getTimeInMillis() - startTime) + " ms.");
      LOG.info("Input length is " + result.length() + " chars.");

      return result;
    } catch (final IOException ex) {
      LOG.error("Exception occured while creating GLPK input.", ex);
      return null;
    }
  }

  private static Pair<CharSequence, Integer> getConstraints(
          final List<AttributeMappingId> candidates, final AMModel model)
          throws InterruptedException {
    final StringBuilder ret = new StringBuilder();

    int index = 0;
    for (final AttributeMappingId mapping1 : candidates) {
      for (final AttributeMappingId mapping2 : candidates) {
        if (mapping1.equals(mapping2)) {
          continue;
        }

        if (Thread.interrupted()) {
          throw new InterruptedException();
        }

        if (mappingsCollide(mapping1, mapping2, model)) {
          index++;
          final String constraint = CONSTRAINT
                  .replace("{index}", String.valueOf(index))
                  .replace("{mapping1}", GlpkUtils.getName(mapping1))
                  .replace("{mapping2}", GlpkUtils.getName(mapping2));
          ret.append(constraint).append('\n');
        }
      }
    }

    return new Pair<CharSequence, Integer>(ret, Integer.valueOf(index));
  }

  private static boolean mappingsCollide(final AttributeMappingId mapping1,
          final AttributeMappingId mapping2, final AMModel model) {
    if (mapping1.getElement().equals(mapping2.getElement())) {
      return true;
    }
    return MappingUtils.imagesIntersect(mapping1, mapping2, model);
  }

}
