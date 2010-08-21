/*
 *  Copyright (C) 2010 vektor
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.ruledisplayer;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Cluster;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import java.util.List;
import org.openide.windows.WindowManager;

/**
 * Class with some static methods to display a list of rules or their clusters.
 * 
 * @author vektor
 */
public final class RuleDisplayer {

  private RuleDisplayer() {
  }

  public static void showRulesAsync(final String panelName, final List<AbstractNode> rules, final boolean render) {
    if (!render || BaseUtils.isEmpty(rules)) {
      return;
    }
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        RuleDisplayerTopComponent.findInstance().createNewPanel(panelName).setRules(rules);
      }
    });
  }

  public static void showClustersAsync(final String panelName, final List<Cluster> clusters, final boolean render) {
    if (!render || BaseUtils.isEmpty(clusters)) {
      return;
    }
    WindowManager.getDefault().invokeWhenUIReady(new Runnable() {

      @Override
      public void run() {
        RuleDisplayerTopComponent.findInstance().createNewPanel(panelName).setClusters(clusters);
      }
    });
  }
}
