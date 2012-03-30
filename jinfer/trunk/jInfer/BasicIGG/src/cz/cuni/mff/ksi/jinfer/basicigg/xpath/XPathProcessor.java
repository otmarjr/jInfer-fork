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
package cz.cuni.mff.ksi.jinfer.basicigg.xpath;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.RunningProject;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.basicigg.properties.BasicIGGPropertiesPanel;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.saxpath.XPathReader;
import org.jaxen.saxpath.helpers.XPathReaderFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor}
 * providing logic for IG retrieval from XPath 1.0 queries.
 *
 * @author vektor
 */
@ServiceProvider(service = Processor.class)
public class XPathProcessor implements Processor<Element> {

  private static final Logger LOG = Logger.getLogger(XPathProcessor.class);

  @Override
  public String getExtension() {
    return "xpath";
  }

  @Override
  public boolean processUndefined() {
    return false;
  }

  @Override
  public FolderType getFolder() {
    return FolderType.QUERY;
  }

  /**
   * Parses the file containing a list of XPath queries and returns the
   * IG rules contained within.
   *
   * @param s File containing a list of XPath queries. One query per line, lines
   * beginning with "#" are ignored.
   * @return List of IG rules retrieved from it.
   */
  @Override
  public List<Element> process(final InputStream s) {
    final List<Element> ret = new ArrayList<Element>();
    try {
      final DataInputStream in = new DataInputStream(s);
      final BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine = br.readLine();
      while (strLine != null) {
        if (!BaseUtils.isEmpty(strLine.trim())
                && strLine.trim().charAt(0) != '#') {
          ret.addAll(parsePath(strLine));
        }
        strLine = br.readLine();
      }
      in.close();
      return ret;
    } catch (final Exception e) {
      if (Boolean.parseBoolean(RunningProject.getActiveProjectProps(BasicIGGPropertiesPanel.NAME).getProperty(BasicIGGPropertiesPanel.STOP_ON_ERROR_PROP, BasicIGGPropertiesPanel.STOP_ON_ERROR_DEFAULT))) {
        throw new RuntimeException("Error reading file " + s, e);
      } else {
        LOG.warn("Error reading file " + s + ", ignoring and going on.", e);
        return Collections.emptyList();
      }
    }
  }

  private static List<Element> parsePath(final String path) {
    try {
      final XPathReader xr = XPathReaderFactory.createReader();
      final XPathHandlerImpl xh = new XPathHandlerImpl();
      xr.setXPathHandler(xh);
      xr.parse(path);
      return xh.getRules();
    } catch (final SAXPathException e) {
      if (Boolean.parseBoolean(RunningProject.getActiveProjectProps(BasicIGGPropertiesPanel.NAME).getProperty(BasicIGGPropertiesPanel.STOP_ON_ERROR_PROP, BasicIGGPropertiesPanel.STOP_ON_ERROR_DEFAULT))) {
        throw new RuntimeException("Error parsing the path: " + path, e);
      } else {
        LOG.warn("Error parsing the path: " + path + ", ignoring and going on.", e);
        return Collections.emptyList();
      }
    }
  }

    @Override
    public Class<?> getResultType() {
        return Element.class;
    }

}
