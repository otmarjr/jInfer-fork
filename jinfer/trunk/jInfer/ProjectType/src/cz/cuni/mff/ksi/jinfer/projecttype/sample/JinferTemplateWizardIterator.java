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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *  Iterator for new jInfer project wizard. This iterator creates panels for each step
 *  of the wizard and creates new project.
 * 
 * @author sviro
 */
public class JinferTemplateWizardIterator implements WizardDescriptor./*Progress*/InstantiatingIterator {

  private int index;
  private WizardDescriptor.Panel[] panels;
  private WizardDescriptor wiz;

  /**
   * Create new instance of this class.
   * @return New instance of this class.
   */
  public static JinferTemplateWizardIterator createIterator() {
    return new JinferTemplateWizardIterator();
  }

  private WizardDescriptor.Panel[] createPanels() {
    return new WizardDescriptor.Panel[]{
              new JinferTemplateWizardPanel(), new ModuleSelectionWizardPanel1()};
  }

  private String[] createSteps() {
    return new String[]{
              NbBundle.getMessage(JinferTemplateWizardIterator.class, "LBL_CreateProjectStep"),
              NbBundle.getMessage(JinferTemplateWizardIterator.class, "LBL_SetModulesStep")
            };
  }

  @Override
  public Set<FileObject> instantiate(/*ProgressHandle handle*/) throws IOException {
    final Set<FileObject> resultSet = new LinkedHashSet<FileObject>();
    final File dirF = FileUtil.normalizeFile((File) wiz.getProperty("projdir"));
    dirF.mkdirs();

    final FileObject template = Templates.getTemplate(wiz);
    final FileObject dir = FileUtil.toFileObject(dirF);
    unZipFile(template.getInputStream(), dir);
    setModules(dir, wiz);

    // Always open top dir as a project:
    resultSet.add(dir);
    // Look for nested projects to open as well:
    final Enumeration<? extends FileObject> e = dir.getFolders(true);
    while (e.hasMoreElements()) {
      final FileObject subfolder = e.nextElement();
      if (ProjectManager.getDefault().isProject(subfolder)) {
        resultSet.add(subfolder);
      }
    }

    final File parent = dirF.getParentFile();
    if (parent != null && parent.exists()) {
      ProjectChooser.setProjectsFolder(parent);
    }

    return resultSet;
  }

  @Override
  public void initialize(final WizardDescriptor wiz) {
    this.wiz = wiz;
    index = 0;
    panels = createPanels();
    // Make sure list of steps is accurate.
    String[] steps = createSteps();
    for (int i = 0; i < panels.length; i++) {
      final Component c = panels[i].getComponent();
      if (steps[i] == null) {
        // Default step name to component name of panel.
        // Mainly useful for getting the name of the target
        // chooser to appear in the list of steps.
        steps[i] = c.getName();
      }
      if (c instanceof JComponent) { // assume Swing components
        final JComponent jc = (JComponent) c;
        // Step #.
        jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, Integer.valueOf(i));
        // Step name (actually the whole list for reference).
        jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
      }
    }
  }

  @Override
  public void uninitialize(final WizardDescriptor wiz) {
    this.wiz.putProperty("projdir", null);
    this.wiz.putProperty("name", null);
    this.wiz = null;
    panels = null;
  }

  @Override
  public String name() {
    return MessageFormat.format("{0} of {1}",
            new Object[]{Integer.valueOf(index + 1), Integer.valueOf(panels.length)});
  }

  @Override
  public boolean hasNext() {
    return index < panels.length - 1;
  }

  @Override
  public boolean hasPrevious() {
    return index > 0;
  }

  @Override
  public void nextPanel() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    index++;
  }

  @Override
  public void previousPanel() {
    if (!hasPrevious()) {
      throw new NoSuchElementException();
    }
    index--;
  }

  @Override
  public WizardDescriptor.Panel current() {
    return panels[index];
  }

  // If nothing unusual changes in the middle of the wizard, simply:
  @Override
  public final void addChangeListener(final ChangeListener l) {
    //do nothing
  }

  @Override
  public final void removeChangeListener(final ChangeListener l) {
    //do nothing
  }

  private static void unZipFile(final InputStream source, final FileObject projectRoot) throws IOException {
    try {
      final ZipInputStream str = new ZipInputStream(source);
      ZipEntry entry;
      while ((entry = str.getNextEntry()) != null) {  //NOPMD
        if (entry.isDirectory()) {
          FileUtil.createFolder(projectRoot, entry.getName());
        } else {
          final FileObject fo = FileUtil.createData(projectRoot, entry.getName());
          if ("nbproject/project.xml".equals(entry.getName())) {
            // Special handling for setting name of Ant-based projects; customize as needed:
            filterProjectXML(fo, str, projectRoot.getName());
          } else {
            writeFile(str, fo);
          }
        }
      }
    } finally {
      source.close();
    }
  }

  private static void writeFile(final ZipInputStream str, final FileObject fo) throws IOException {
    final OutputStream out = fo.getOutputStream();
    try {
      FileUtil.copy(str, out);
    } finally {
      out.close();
    }
  }

  private static void filterProjectXML(final FileObject fo, final ZipInputStream str, final String name) throws IOException {
    try {
      final ByteArrayOutputStream baos = new ByteArrayOutputStream();
      FileUtil.copy(str, baos);
      final Document doc = XMLUtil.parse(new InputSource(new ByteArrayInputStream(baos.toByteArray())), false, false, null, null);
      final NodeList nl = doc.getDocumentElement().getElementsByTagName("name");
      if (nl != null) {
        for (int i = 0; i < nl.getLength(); i++) {
          final Element el = (Element) nl.item(i);
          if (el.getParentNode() != null && "data".equals(el.getParentNode().getNodeName())) {
            final NodeList nl2 = el.getChildNodes();
            if (nl2.getLength() > 0) {
              nl2.item(0).setNodeValue(name);
            }
            break;
          }
        }
      }
      final OutputStream out = fo.getOutputStream();
      try {
        XMLUtil.write(doc, out, "UTF-8");
      } finally {
        out.close();
      }
    } catch (Exception ex) {
      Exceptions.printStackTrace(ex);
      writeFile(str, fo);
    }

  }

  private static void setModules(final FileObject dir, final WizardDescriptor wiz) {
    final Properties properties = (Properties) wiz.getProperty(ModuleSelectionWizardPanel1.MODULE_SELECTION_PROPS);
    if (properties != null) {
      final FileObject jinferProjectDir = dir.getFileObject("jinferproject");
      try {
        final FileObject projectProperties = jinferProjectDir.createData("project", "properties");
        properties.store(new FileOutputStream(FileUtil.toFile(projectProperties)), null);
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  }
}
