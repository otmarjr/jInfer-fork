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
package cz.cuni.mff.ksi.jinfer.modularsimplifier.options;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

@OptionsPanelController.SubRegistration(location = "jInfer",
displayName = "#OptionsCategory_Name_Config",
keywords = "#OptionsCategory_Keywords_Config",
keywordsCategory = "Config",
position = 1)
public final class ConfigOptionsPanelController extends OptionsPanelController {

  private ConfigPanel panel;
  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
  private boolean changed;

  @Override
  public void update() {
    getPanel().load();
    changed = false;
  }

  @Override
  public void applyChanges() {
    getPanel().store();
    changed = false;
  }

  @Override
  public void cancel() {
    // need not do anything special, if no changes have been persisted yet
  }

  @Override
  public boolean isValid() {
    return getPanel().valid();
  }

  @Override
  public boolean isChanged() {
    return changed;
  }

  @Override
  public HelpCtx getHelpCtx() {
    return null; // new HelpCtx("...ID") if you have a help set
  }

  @Override
  public JComponent getComponent(final Lookup masterLookup) {
    return getPanel();
  }

  @Override
  public void addPropertyChangeListener(final PropertyChangeListener l) {
    pcs.addPropertyChangeListener(l);
  }

  @Override
  public void removePropertyChangeListener(final PropertyChangeListener l) {
    pcs.removePropertyChangeListener(l);
  }

  private ConfigPanel getPanel() {
    if (panel == null) {
      panel = new ConfigPanel();
    }
    return panel;
  }
}
