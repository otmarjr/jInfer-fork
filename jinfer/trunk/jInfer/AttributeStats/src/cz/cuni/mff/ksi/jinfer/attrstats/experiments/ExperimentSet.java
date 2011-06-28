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
package cz.cuni.mff.ksi.jinfer.attrstats.experiments;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ExperimentListener;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public abstract class ExperimentSet {

  protected abstract List<ExperimentParameters> getExperiments();

  private List<ExperimentParameters> cache = null;

  private static final Logger LOG = Logger.getLogger(ExperimentSet.class);

  public void run(final int startIndex) {
    final List<ExperimentParameters> set = getCached();

    if (startIndex >= set.size()) {
      return;
    }

    final ExperimentParameters params = set.get(startIndex);

    final Experiment e = new Experiment(params);
    e.addListener(new ExperimentListener() {

      @Override
      public void experimentFinished(final Experiment e) {
        LOG.info(e.getCsv());
        run(startIndex + 1);
      }
    });
    try {
      e.run();
    } catch (final InterruptedException ex) {
    }
  }

  private List<ExperimentParameters> getCached() {
    if (cache == null) {
      cache = new ArrayList<ExperimentParameters>(getExperiments());
    }
    return cache;
  }

}
