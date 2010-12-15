/*
 *  Copyright (C) 2010 reseto
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
package cz.cuni.mff.ksi.jinfer.base.options;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

/**
 * Panel controller for Logger option panel.
 * 
 * @author reseto
 */
@OptionsPanelController.SubRegistration(location = "jInfer",
                                        displayName = "#AdvancedOption_DisplayName_Logger",
                                        keywords = "#AdvancedOption_Keywords_Logger",
                                        keywordsCategory = "jInfer/Logger")
public final class LoggerOptionsPanelController extends OptionsPanelController {

  private LoggerPanel panel;
  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
  private boolean changed;

  public void update() {
    getPanel().load();
    changed = false;
  }

  public void applyChanges() {
    getPanel().store();
    changed = false;
  }

  public void cancel() {
    // need not do anything special, if no changes have been persisted yet
  }

  public boolean isValid() {
    return getPanel().valid();
  }

  public boolean isChanged() {
    return changed;
  }

  public HelpCtx getHelpCtx() {
    return null; // new HelpCtx("...ID") if you have a help set
  }

  public JComponent getComponent(final Lookup masterLookup) {
    return getPanel();
  }

  public void addPropertyChangeListener(final PropertyChangeListener l) {
    pcs.addPropertyChangeListener(l);
  }

  public void removePropertyChangeListener(final PropertyChangeListener l) {
    pcs.removePropertyChangeListener(l);
  }

  private LoggerPanel getPanel() {
    if (panel == null) {
      panel = new LoggerPanel(this);
    }
    return panel;
  }

  public void changed() {
    if (!changed) {
      changed = true;
      pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
    }
    pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
  }
}
