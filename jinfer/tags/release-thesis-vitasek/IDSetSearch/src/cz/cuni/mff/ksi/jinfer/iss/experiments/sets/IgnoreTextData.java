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
package cz.cuni.mff.ksi.jinfer.iss.experiments.sets;

import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.basicigg.properties.BasicIGGPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.iss.experiments.AbstractExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.ExperimentParameters;
import cz.cuni.mff.ksi.jinfer.iss.experiments.data.OfficialTestData;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.quality.Weight;
import cz.cuni.mff.ksi.jinfer.iss.experiments.termination.TimeIterations;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.Glpk;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.improvement.Identity;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.openide.util.lookup.ServiceProvider;

/**
 * This experiment compares performance when ignoring text data.
 *
 * @author vektor
 */
@ServiceProvider(service = ExperimentSet.class)
public class IgnoreTextData extends AbstractExperimentSet {

  @Override
  public String getName() {
    return "Ignore Text Data";
  }

  @Override
  protected List<ExperimentParameters> getExperiments() {
    final List<ImprovementHeuristic> improvement = Arrays.<ImprovementHeuristic>asList(new Identity());

    final List<ExperimentParameters> ret = new ArrayList<ExperimentParameters>(Utils.getIterations());

    for (int i = 0; i < Utils.getIterations() * 2; i++) {
      ret.add(new ExperimentParameters(OfficialTestData.XMAp.getFile(),
              1, 1, 1, OfficialTestData.XMAp.getKnownOptimum(),
              new Glpk(1), improvement, new Weight(), TimeIterations.NULL));
    }

    return ret;
  }

  @Override
  protected void notifyStart() {
    final Properties p = new Properties();
    p.setProperty(BasicIGGPropertiesPanel.KEEP_ATTRIBUTES_PROP, Boolean.TRUE.toString());
    RunningProject.setDefaultProperties(p);
  }

  @Override
  protected void notifyFinished(final Experiment e, final int iteration) {
    final StringBuilder sb = new StringBuilder();
    sb.append(e.getGrammarTime())
      .append('\t')
      .append(e.getModelTime())
      .append('\n');
    FileUtils.appendString(sb.toString(), resultCsv);

    if (iteration == Utils.getIterations() - 1) {
      final Properties p = new Properties();
      p.setProperty(BasicIGGPropertiesPanel.KEEP_ATTRIBUTES_PROP, Boolean.FALSE.toString());
      RunningProject.setDefaultProperties(p);
    }
  }

  @Override
  protected void notifyFinishedAll() {
    final Properties p = new Properties();
    p.setProperty(BasicIGGPropertiesPanel.KEEP_ATTRIBUTES_PROP, Boolean.TRUE.toString());
    RunningProject.setDefaultProperties(p);
  }

}
