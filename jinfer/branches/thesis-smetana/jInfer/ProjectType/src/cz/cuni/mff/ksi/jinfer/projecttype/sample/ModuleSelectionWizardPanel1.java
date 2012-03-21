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

import cz.cuni.mff.ksi.jinfer.base.utils.ModuleProperties;
import cz.cuni.mff.ksi.jinfer.runner.properties.ModuleSelectionPropertiesPanel;
import java.awt.Component;
import java.util.Properties;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
/**
 * This class defines second step in new jInfer project wizard.
 * @author sviro
 */
public class ModuleSelectionWizardPanel1 implements WizardDescriptor.Panel {
  /**
   * Name of the property in WizardDescriptor to save selected modules to be set in new project.
   */
  public static final String MODULE_SELECTION_PROPS = "moduleSelectionProps";

  /**
   * The visual component that displays this panel. If you need to access the
   * component from this class, just use getComponent().
   */
  private ModuleSelectionPropertiesPanel component;
  private Properties properties;

  // Get the visual component for the panel. In this template, the component
  // is kept separate. This can be more efficient: if the wizard is created
  // but never displayed, or not all panels are displayed, it is better to
  // create only those which really need to be visible.
  @Override
  public Component getComponent() {
    if (component == null) {
      component = new ModuleSelectionPropertiesPanel(new ModuleProperties(ModuleSelectionPropertiesPanel.NAME,
              getProperties()));
      component.setName(NbBundle.getMessage(JinferTemplateWizardIterator.class, "LBL_SetModulesStep"));
    }
    return component;
  }

  @Override
  public HelpCtx getHelp() {
    // Show no Help button for this panel:
    return HelpCtx.DEFAULT_HELP;
    // If you have context help:
    // return new HelpCtx(SampleWizardPanel1.class);
  }

  @Override
  public boolean isValid() {
    // If it is always OK to press Next or Finish, then:
    return true;
    // If it depends on some condition (form filled out...), then:
    // return someCondition();
    // and when this condition changes (last form field filled in...) then:
    // fireChangeEvent();
    // and uncomment the complicated stuff below.
  }

  @Override
  public final void addChangeListener(final ChangeListener l) {
    //do nothing
  }

  @Override
  public final void removeChangeListener(final ChangeListener l) {
    //do nothing
  }
  /*
  private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0
  public final void addChangeListener(ChangeListener l) {
  synchronized (listeners) {
  listeners.add(l);
  }
  }
  public final void removeChangeListener(ChangeListener l) {
  synchronized (listeners) {
  listeners.remove(l);
  }
  }
  protected final void fireChangeEvent() {
  Iterator<ChangeListener> it;
  synchronized (listeners) {
  it = new HashSet<ChangeListener>(listeners).iterator();
  }
  ChangeEvent ev = new ChangeEvent(this);
  while (it.hasNext()) {
  it.next().stateChanged(ev);
  }
  }
   */

  // You can use a settings object to keep track of state. Normally the
  // settings object will be the WizardDescriptor, so you can use
  // WizardDescriptor.getProperty & putProperty to store information entered
  // by the user.
  @Override
  public void readSettings(final Object settings) {
    getComponent();
    component.load();
  }

  @Override
  public void storeSettings(final Object settings) {
    getComponent();
    component.store();
    final WizardDescriptor d = (WizardDescriptor) settings;
    d.putProperty( MODULE_SELECTION_PROPS, properties);
  }

  private Properties getProperties() {
    if (properties == null) {
      properties = new Properties();
    }
    return properties;
  }
}
