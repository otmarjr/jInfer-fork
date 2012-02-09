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
package cz.cuni.mff.ksi.jinfer.xqueryanalyzer;

import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.SchemaGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.XQueryAnalyzer;
import cz.cuni.mff.ksi.jinfer.base.interfaces.inference.XQueryAnalyzerCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.InferenceDataHolder;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO rio
 * @author rio
 */
@ServiceProvider(service = XQueryAnalyzer.class)
public class XQueryAnalyzerImpl implements XQueryAnalyzer {
  
  private final static Logger LOG = Logger.getLogger(XQueryAnalyzerImpl.class);

  @Override
  public void start(InferenceDataHolder idh, XQueryAnalyzerCallback callback) throws InterruptedException {
    LOG.debug(idh.getXQuerySyntaxTrees());
    callback.finished(idh);
  }

  @Override
  public String getDisplayName() {
    // TODO rio
    return "XQueryAnalyzer";
  }

  @Override
  public String getModuleDescription() {
    // TODO rio
    return "XQueryAnalyzer";
  }

  @Override
  public String getName() {
    // TODO rio
    return "XQueryAnalyzer";
  }

  @Override
  public List<String> getCapabilities() {
    // TODO rio
    return new LinkedList<String>();
  }
  
  
}
