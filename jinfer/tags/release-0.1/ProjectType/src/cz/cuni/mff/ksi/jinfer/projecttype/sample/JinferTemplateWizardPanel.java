/*
 *  Copyright (C) 2010 sviro
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
package cz.cuni.mff.ksi.jinfer.projecttype.sample;

import java.awt.Component;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;

/**
 * Panel just asking for basic info.
 */
public class JinferTemplateWizardPanel implements WizardDescriptor.Panel,
        WizardDescriptor.ValidatingPanel, WizardDescriptor.FinishablePanel {

  private WizardDescriptor wizardDescriptor;
  private JinferTemplatePanelVisual component;

  public Component getComponent() {
    if (component == null) {
      component = new JinferTemplatePanelVisual(this);
      component.setName(NbBundle.getMessage(JinferTemplateWizardPanel.class, "LBL_CreateProjectStep"));
    }
    return component;
  }

  public HelpCtx getHelp() {
    return new HelpCtx(JinferTemplateWizardPanel.class);
  }

  public boolean isValid() {
    getComponent();
    return component.valid(wizardDescriptor);
  }
  private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0

  public final void addChangeListener(final ChangeListener l) {
    synchronized (listeners) {
      listeners.add(l);
    }
  }

  public final void removeChangeListener(final ChangeListener l) {
    synchronized (listeners) {
      listeners.remove(l);
    }
  }

  protected final void fireChangeEvent() {
    Set<ChangeListener> ls;
    synchronized (listeners) {
      ls = new HashSet<ChangeListener>(listeners);
    }
    final ChangeEvent ev = new ChangeEvent(this);
    for (ChangeListener l : ls) {
      l.stateChanged(ev);
    }
  }

  public void readSettings(final Object settings) {
    wizardDescriptor = (WizardDescriptor) settings;
    component.read(wizardDescriptor);
  }

  public void storeSettings(final Object settings) {
    final WizardDescriptor d = (WizardDescriptor) settings;
    component.store(d);
  }

  public boolean isFinishPanel() {
    return true;
  }

  public void validate() throws WizardValidationException {
    getComponent();
    component.validate(wizardDescriptor);
  }
}
