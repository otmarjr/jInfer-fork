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

import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentSet;
import cz.cuni.mff.ksi.jinfer.base.utils.FileUtils;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ExperimentListener;
import cz.cuni.mff.ksi.jinfer.iss.utils.Constants;
import java.io.File;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Abstract implementation of an experiment set. After it gets the list of
 * experiments (via {@link ExperimentParameters}) to run, it starts to execute
 * them sequentially.
 *
 * @author vektor
 */
public abstract class AbstractExperimentSet implements ExperimentSet {

  private final Object monitor = new Object();

  private boolean abort;

  /**
   * Should return the list of experiment parameters to be run in this set.
   *
   * @return List of experiment parameters that will be sequentially run in this
   * experimental set.
   */
  protected abstract List<ExperimentParameters> getExperiments();

  protected static final int POOL_SIZE = 10;

  protected final File resultCsv = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName() + "/result.txt");

  private static final Logger LOG = Logger.getLogger(AbstractExperimentSet.class);

  @Override
  public void run(final int from) throws InterruptedException {
    if (from == 0) {
      notifyStart();
    }
    else {
      LOG.info("Running from index " + from);
    }
    abort = false;
    final List<ExperimentParameters> experiments = getExperiments();
    final int totalCount = experiments.size();
    LOG.info("Got " + totalCount + " experiment parameter sets");
    for (int i = from; i < totalCount; i++) {
      if (abort) {
        return;
      }
      final Experiment e = new Experiment(experiments.get(i));
      e.addListener(new Listener(i));
      final Thread t = new Thread(new Runnable() {

        @Override
        public void run() {
          try {
            e.run();
          }
          catch (final InterruptedException ex) {
            LOG.error("Interrupted", ex);
          }
          catch (final Exception ex) {
            LOG.error("Exception while running the experiment, details in the log file", ex);
            abort = true;
            synchronized (monitor) {
              monitor.notifyAll();
            }
          }
        }
      }, "Experiment Runner");

      LOG.debug("Running iteration #" + (i + 1) + "/" + totalCount);
      t.start();

      synchronized (monitor) {
        monitor.wait();
      }
    }

    Constants.RESTART.delete();

    LOG.debug("Experiment set finished.");

    notifyFinishedAll();
  }

  private class Listener implements ExperimentListener {

    private final int iteration;

    public Listener(final int iteration) {
      this.iteration = iteration;
    }

    @Override
    public void experimentFinished(final Experiment e) {
      LOG.debug("Iteration #" + (iteration + 1) + " finished");
      FileUtils.appendString("\n" + iteration, Constants.RESTART);
      notifyFinished(e, iteration);
      synchronized (monitor) {
        monitor.notifyAll();
      }
    }
  }

  // --- Lifecycle -------------------------------------------------------------

  /**
   * Notification that the experiment set run is starting (this does not happen
   * if we start from index higher than 0). We do not want to force descendants
   * to override this, thus it is not abstract.
   */
  protected void notifyStart() {

  }

  /**
   * Notification that the current iteration has finished. This implementation
   * writes the full report providied by the experiment that has finished in
   * a file named with the iteration (123.txt). It is OK to override this method
   * and not call super, if this logic is unnecessary.
   *
   * @param e Object representing the experiment that has currently finished running.
   * @param iteration Iteration - index in the list of all the experiments in
   * this set - that was just finished. Note that the numbering starts from 0.
   */
  protected void notifyFinished(final Experiment e, final int iteration) {
    final File output = new File(Constants.TEST_OUTPUT_ROOT + "/" + getName() + "/" + iteration + ".txt");

    final StringBuilder ret = new StringBuilder();
    ret.append(SystemInfo.getInfo()).append('\n')
        .append(e.getReport()).append('\n')
        .append(e.getCsv()).append('\n')
        .append(e.getWinner());
    FileUtils.writeString(ret.toString(), output);
  }

  /**
   * Notification that the experiment set has finished. We do not want to
   * force descendants to override this, thus it is not abstract.
   */
  protected void notifyFinishedAll() {

  }

  @Override
  public String getDisplayName() {
    return getName();
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }
}
