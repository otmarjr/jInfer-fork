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
package cz.cuni.mff.ksi.jinfer.base.utils;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.TaskListener;

/**
 * Utility class for asynchronous invocation of a task.
 *
 * @author vektor
 */
public final class AsynchronousUtils {

  private AsynchronousUtils() {
  }

  /**
   * Executes the specified task asynchronously and shows a NBP progress bar
   * for it.
   *
   * @param task Task to execute.
   * @param taskName Name of the task. Will be displayed next to its NBP
   * progress bar.
   */
  public static void runAsync(final Runnable task, final String taskName) {
    final RequestProcessor rp = new RequestProcessor("interruptible", 1, true);
    final RequestProcessor.Task theTask = rp.create(task);
    final ProgressHandle handle = ProgressHandleFactory.createHandle(taskName, theTask);
    theTask.addTaskListener(new TaskListener() {

      @Override
      public void taskFinished(final Task task) {
        handle.finish();
      }
    });
    handle.start();
    theTask.schedule(0);
  }

}
