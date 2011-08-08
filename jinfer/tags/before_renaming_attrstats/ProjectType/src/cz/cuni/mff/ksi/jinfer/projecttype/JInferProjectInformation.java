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
package cz.cuni.mff.ksi.jinfer.projecttype;

import java.beans.PropertyChangeListener;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.openide.util.ImageUtilities;

/**
 * Provides general information about jInfer project.
 * 
 * @author sviro
 */
public class JInferProjectInformation implements ProjectInformation {

  private final JInferProject project;

  public JInferProjectInformation(final JInferProject project) {
    this.project = project;
  }

  @Override
  public String getName() {
    return project.getProjectDirectory().getName();
  }

  @Override
  public String getDisplayName() {
    return project.getLookup().lookup(Properties.class).getProperty(JInferProject.JINFER_PROJECT_NAME_PROPERTY, getName());
  }

  @Override
  public Icon getIcon() {
    return new ImageIcon(ImageUtilities.loadImage(
            "cz/cuni/mff/ksi/jinfer/projecttype/graphics/icon16.png"));
  }

  @Override
  public Project getProject() {
    return project;
  }

  @Override
  public void addPropertyChangeListener(final PropertyChangeListener pl) {
    //do nothing
  }

  @Override
  public void removePropertyChangeListener(final PropertyChangeListener pl) {
    //do nothing
  }
}
