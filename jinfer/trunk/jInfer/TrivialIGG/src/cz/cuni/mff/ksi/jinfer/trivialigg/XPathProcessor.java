/*
 *  Copyright (C) 2010 vitasek
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
package cz.cuni.mff.ksi.jinfer.trivialigg;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.jaxen.saxpath.SAXPathException;
import org.jaxen.saxpath.XPathReader;
import org.jaxen.saxpath.helpers.XPathReaderFactory;

/**
 * Contains logic for IG retrieval from XPath 1.0 queries.
 *
 * @author vektor
 */
public final class XPathProcessor {

  private static final Logger LOG = Logger.getLogger(XPathProcessor.class);

  private XPathProcessor() {
  }

  /**
   * Parses the file containing a list of XPath queris and returns the
   * IG rules contained within.
   *
   * @param f File containing a list of XPath queries. One query per line, lines
   * beginning with # are ignored.
   * @return List of IG rules retrieved from it.
   */
  public static List<AbstractNode> process(final File f) {
    final List<AbstractNode> ret = new ArrayList<AbstractNode>();
    try {
      final DataInputStream in = new DataInputStream(new FileInputStream(f));
      final BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      while ((strLine = br.readLine()) != null) {
        if (strLine.charAt(0) != '#') {
          ret.addAll(parsePath(strLine));
        }
      }
      in.close();
    } catch (final Exception e) {
      LOG.error("Error reading file " + f, e);
    }
    return ret;
  }

  private static List<AbstractNode> parsePath(final String path) {
    try {
      final XPathReader xr = XPathReaderFactory.createReader();
      final XPathHandlerImpl xh = new XPathHandlerImpl();
      xr.setXPathHandler(xh);
      xr.parse(path);
      return xh.getRules();
    } catch (SAXPathException ex) {
      LOG.error("Error parsing the path: " + path, ex);
    }

    return new ArrayList<AbstractNode>(0);
  }
}
