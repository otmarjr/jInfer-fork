/*
 * Copyright (C) 2012 rio
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
package cz.cuni.mff.ksi.jinfer.xqueryimporter;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.ModuleNode;
import cz.cuni.mff.ksi.jinfer.xqanalyzer.XQConverter;
import cz.cuni.mff.ksi.jinfer.xqanalyzer.XQParseException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * 
 * @author rio
 */
@ServiceProvider(service = Processor.class)
public class XQueryProcessor implements Processor<ModuleNode> {
  
  private static final Logger LOG = Logger.getLogger(XQueryProcessor.class);

  @Override
  public String getExtension() {
    return "xq";
  }

  @Override
  public FolderType getFolder() {
    return FolderType.QUERY;
  }

  @Override
  public Class<?> getResultType() {
    return ModuleNode.class;
  }

  @Override
  public List<ModuleNode> process(InputStream s) throws InterruptedException {
    final XQConverter converter = new XQConverter(s);
    try {
      converter.convert();
    } catch (final XQParseException exc) {
      // TODO rio
    }
    final List<ModuleNode> result = new LinkedList<ModuleNode>();
    result.add(converter.getModuleNode());
    return result;
  }

  @Override
  public boolean processUndefined() {
    // TODO rio
    return false;
  }
  
  
}
