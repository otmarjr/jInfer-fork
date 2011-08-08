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
package cz.cuni.mff.ksi.jinfer.iss.experiments;

import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentListener;
import cz.cuni.mff.ksi.jinfer.iss.heuristics.construction.glpk.GlpkUtils;
import cz.cuni.mff.ksi.jinfer.iss.utils.Constants;
import java.io.File;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Abstract class representing an experiment set. After it gets the list of
 * experiments (via {@link ExperimentParameters}) to run, it starts to execute
 * them sequentially.
 *
 * @author vektor
 */
public abstract class AbstractExperimentSet {

  private final Object monitor = new Object();

  /**
   * Should return the name of this experimental set.
   *
   * @return Name of this set.
   */
  public abstract String getName();

  /**
   * Should return the list of experiment parameters to be run in this set.
   *
   * @return List of experiment parameters that will be sequentially run in this
   * experimental set.
   */
  protected abstract List<ExperimentParameters> getExperiments();

  private static final Logger LOG = Logger.getLogger(AbstractExperimentSet.class);

  public void run() throws InterruptedException {
    int i = 0;
    for (final ExperimentParameters params : getExperiments()) {
      final Experiment e = new Experiment(params);
      final int index = i++;
      e.addListener(new ExperimentListener() {

        @Override
        public void experimentFinished(final Experiment e) {
          logResults(e, index);
          synchronized (monitor) {
            monitor.notifyAll();
          }
        }
      });
      final Thread t = new Thread(new Runnable() {

        @Override
        public void run() {
          try {
            e.run();
          }
          catch (final InterruptedException ex) {
            LOG.error("Interrupted", ex);
          }
        }
      }, "Experiment Runner");
      t.start();

      synchronized (monitor) {
        monitor.wait();
      }
    }
  }

  private void logResults(final Experiment e, final int index) {
    final File rootDir = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName());
    if (!rootDir.exists()) {
      rootDir.mkdirs();
    }

    final File output = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName() + "/" + index + ".txt");

    final StringBuilder ret = new StringBuilder();
    ret.append(SystemInfo.getInfo()).append('\n')
        .append(e.getReport()).append('\n')
        .append(e.getCsv()).append('\n')
        .append(e.getWinner());
    GlpkUtils.writeInput(output, ret.toString());
  }
}
