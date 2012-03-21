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

import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.LocalBranching;
import cz.cuni.mff.ksi.jinfer.iss.utils.MappingUtils;
import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import java.io.IOException;
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

  private static final String TEMPLATE = "cz/cuni/mff/ksi/jinfer/iss/heuristics/construction/glpk/GlpkInputTemplate.txt";
  private static final String TEMPLATE_LB = "cz/cuni/mff/ksi/jinfer/iss/heuristics/construction/glpk/GlpkInputTemplateLB.txt";
  private static final String CONSTRAINT = "s.t. c{index}: x['{mapping1}'] + x['{mapping2}'] <= 1;";
  private static final String FIX_TO_1 = "s.t. f{index}: x['{mapping}'] = 1;";
  private static final String FIX_TO_0 = "s.t. f{index}: x['{mapping}'] = 0;";

  /**
   * Generates a GLPK MathProg representation of the ID set optimization for the
   * provided model, in this variant specifically for Local Branching heuristic.
   *
   * <p>
   * Please see {@link GlpkInputGenerator#generateGlpkInput(AMModel, List, double, double) }
   * for general info.
   * </p>
   *
   * <p>
   * This variant will add one more constraint to ensure the
   * Hamming distance from the incumbent solution in the form:<br/>
   * <code>SUM<sub>I</sub> (1 - am<sub>i</sub>) + SUM<sub>J</sub> am<sub>j</sub> &lt;= k</code><br/>
   * where <code>I</code> is the set of AMs in the incumbent solution,
   * <code>J</code> is the set of remaining AMs in the model.
   * </p>
   *
   * @param model Model from which the problem formulation should be generated.
   * @param alpha Weight of the attribute mapping <cite>support</cite> in its total weight.
   * @param beta Weight of the attribute mapping <cite>coverage</cite> in its total weight.
   * @param incumbentAMs List of AMs contained in the currently incumbent
   *   solution.
   * @param k Maximal Hamming distance from the specified incumbent solution.
   *
   * @return String representation of the problem formulation in MathProg
   * language, that can be directly passed to GLPK Solver.
   *
   * @see LocalBranching
   */
  public static String generateGlpkInput(final AMModel model,
          final double alpha, final double beta,
          final List<AttributeMappingId> incumbentAMs, final long k)
          throws IOException, InterruptedException {
    final long start = Utils.time();

    final List<AttributeMappingId> candidates = MappingUtils.getCandidates(model);
    Collections.shuffle(candidates);

    final StringBuilder mappings = new StringBuilder();
    final StringBuilder weights = new StringBuilder();
    final StringBuilder incumbent = new StringBuilder();
    final StringBuilder remaining = new StringBuilder();

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
      if (incumbentAMs.contains(mapping)) {
        incumbent.append(name).append('\n');
      }
      else {
        remaining.append(name).append('\n');
      }
    }

    final String ret = FileUtils.loadTemplate(TEMPLATE_LB).toString()
            .replace("{constraints}", getConstraints(candidates, Collections.<AttributeMappingId>emptyList(), model))
            .replace("{mappings}", mappings)
            .replace("{weights}", weights)
            .replace("{k}", String.valueOf(k))
            .replace("{incumbent}", incumbent)
            .replace("{remaining}", remaining);

    LOG.debug("GLPK input generation took " + Utils.delta(start) + " ms.");

    return ret;
  }

  /**
   * @see GlpkInputGenerator#generateGlpkInput(cz.cuni.mff.ksi.jinfer.iss.objects.AMModel, java.util.List, double, double)
   */
  public static String generateGlpkInput(final AMModel model,
          final double alpha, final double beta)
          throws IOException, InterruptedException {
    return generateGlpkInput(model, Collections.<AttributeMappingId>emptyList(), alpha, beta);
  }

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
   * @param fixed List of attribute mappings that should be present in the
   * solution (their value is fixed to 1).
   *
   * @return String representation of the problem formulation in MathProg
   * language, that can be directly passed to GLPK Solver.
   */
  public static String generateGlpkInput(final AMModel model,
          final List<AttributeMappingId> fixed,
          final double alpha, final double beta)
          throws IOException, InterruptedException {
    final long start = Utils.time();

    final List<AttributeMappingId> candidates = MappingUtils.getCandidates(model);
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

    final String ret = FileUtils.loadTemplate(TEMPLATE).toString()
            .replace("{constraints}", getConstraints(candidates, fixed, model))
            .replace("{mappings}", mappings)
            .replace("{weights}", weights);

    if (!BaseUtils.isEmpty(fixed)) {
      LOG.info("Fixed to 1: " + fixed.size());
    }

    LOG.debug("GLPK input generation took " + Utils.delta(start) + " ms.");

    return ret;
  }

  private static String getConstraints(
          final List<AttributeMappingId> candidates,
          final List<AttributeMappingId> fixed, final AMModel model)
          throws InterruptedException {
    final StringBuilder ret = new StringBuilder();

    int fixedIndex = 0;
    for (final AttributeMappingId mapping : fixed) {
      if (candidates.contains(mapping)) {
        fixedIndex = addFixedConstraint(FIX_TO_1, ret, fixedIndex, mapping);
      }
    }

    int constraintIndex = 0;

    for (final AttributeMappingId mapping1 : candidates) {
      for (final AttributeMappingId mapping2 : candidates) {
        if (Thread.interrupted()) {
          throw new InterruptedException();
        }

        if (MappingUtils.mappingsCollide(mapping1, mapping2, model)
                && mapping1.compareTo(mapping2) < 0) {

          final boolean fixed1 = fixed.contains(mapping1);
          final boolean fixed2 = fixed.contains(mapping2);
          if (fixed1 || fixed2) {
            if (fixed1) {
              fixedIndex = addFixedConstraint(FIX_TO_0, ret, fixedIndex, mapping2);
            }
            if (fixed2) {
              fixedIndex = addFixedConstraint(FIX_TO_0, ret, fixedIndex, mapping1);
            }
          }
          else {
            constraintIndex++;
            final String constraint = CONSTRAINT
                    .replace("{index}", String.valueOf(constraintIndex))
                    .replace("{mapping1}", GlpkUtils.getName(mapping1))
                    .replace("{mapping2}", GlpkUtils.getName(mapping2));
            ret.append(constraint).append('\n');
          }
        }
      }
    }

    return ret.toString();
  }

  private static int addFixedConstraint(final String template, final StringBuilder sb, final int index, final AttributeMappingId mapping) {
    final String fix = template
            .replace("{index}", String.valueOf(index + 1))
            .replace("{mapping}", GlpkUtils.getName(mapping));
    sb.append(fix).append('\n');
    return index + 1;
  }
}
