/*
 *  Copyright (C) 2011 sviro
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
package cz.cuni.mff.ksi.jinfer.autoeditor.options;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

/**
 * Options controller of the AutoEditor module.
 * @author sviro
 */
@OptionsPanelController.SubRegistration(
  location = "jInfer",
  displayName = "#AdvancedOption_DisplayName_AutoEditor",
  keywords = "#AdvancedOption_Keywords_AutoEditor",
  keywordsCategory = "jInfer/AutoEditor",
  position = 10, id = "autoEditor")
public final class AutoEditorController extends OptionsPanelController {

  private AutoEditorPanel panel;
  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
  private boolean valuesChanged;

  @Override
  public void update() {
    getPanel().load();
    valuesChanged = false;
  }

  @Override
  public void applyChanges() {
    getPanel().store();
    valuesChanged = false;
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
    return valuesChanged;
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

  private AutoEditorPanel getPanel() {
    if (panel == null) {
      panel = new AutoEditorPanel();
    }
    return panel;
  }

  /**
   * Call to indicate that some field is changed in options panel.
   */
  public void changed() {
    if (!valuesChanged) {
      valuesChanged = true;
      pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
    }
    pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
  }
}
