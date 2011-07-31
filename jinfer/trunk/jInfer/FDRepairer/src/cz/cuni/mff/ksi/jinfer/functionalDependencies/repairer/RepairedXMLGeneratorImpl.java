/*
 * Copyright (C) 2011 sviro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.functionalDependencies.repairer;

import cz.cuni.mff.ksi.jinfer.functionalDependencies.RXMLTree;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.RepairedXMLGenerator;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.RepairedXMLGeneratorCallback;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * Implementation of RepairedXMLGenerator interface.
 * @author sviro
 */
@ServiceProvider(service = RepairedXMLGenerator.class)
public class RepairedXMLGeneratorImpl implements RepairedXMLGenerator {
  
  private static final Logger LOG = Logger.getLogger(RXMLTree.class);

  @Override
  public void start(final List<RXMLTree> repairedTrees, final RepairedXMLGeneratorCallback callback) throws InterruptedException {
    final List<String> result = new ArrayList<String>();
    
    for (RXMLTree rXMLTree : repairedTrees) {
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      result.add(transformTreeToXML(rXMLTree));
    }
    
    callback.finished(result);
  }
  
  private String transformTreeToXML(final RXMLTree tree) {
    try {
      final TransformerFactory transfac = TransformerFactory.newInstance();
      transfac.setAttribute("indent-number", Integer.valueOf(4));
      final Transformer trans = transfac.newTransformer();
      trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      trans.setOutputProperty(OutputKeys.INDENT, "yes");
      
      //create string from xml tree
      final StringWriter sw = new StringWriter();
      final StreamResult result = new StreamResult(sw);
      final DOMSource source = new DOMSource(tree.getDocument());
      trans.transform(source, result);
      return sw.toString();
    } catch (TransformerException ex) {
      LOG.error(ex);
      throw new RuntimeException("Something goes wrong while transforming tree into XML.");
    }
  }
  
}
