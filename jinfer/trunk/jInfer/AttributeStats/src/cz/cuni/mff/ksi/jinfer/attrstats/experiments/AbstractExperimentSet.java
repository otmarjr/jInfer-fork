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

  protected abstract List<ExperimentParameters> getExperiments();

  private static final Logger LOG = Logger.getLogger(AbstractExperimentSet.class);

  public void run() throws InterruptedException {
    for (final ExperimentParameters params : getExperiments()) {
      final Experiment e = new Experiment(params);
      e.addListener(new ExperimentListener() {

        @Override
        public void experimentFinished(final Experiment e) {
          LOG.info(e.getReport());
          // LOG.info(e.getCsv());
          // LOG.info(e.getWinner());
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
}
