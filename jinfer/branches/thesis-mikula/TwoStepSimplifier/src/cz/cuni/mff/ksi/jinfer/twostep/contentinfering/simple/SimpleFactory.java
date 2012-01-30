/*
 * Copyright (C) 2011 anti
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
package cz.cuni.mff.ksi.jinfer.twostep.contentinfering.simple;

import cz.cuni.mff.ksi.jinfer.twostep.contentinfering.ContentInferrer;
import cz.cuni.mff.ksi.jinfer.twostep.contentinfering.ContentInferrerFactory;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
@ServiceProvider(service = ContentInferrerFactory.class)
public class SimpleFactory implements ContentInferrerFactory {
  private ContentInferrer singleton = null;
  private static final Logger LOG = Logger.getLogger(SimpleFactory.class);
  
  /**
   * Name of submodule
   */
  public static final String NAME = "TwoStepContentInferrerSimple";
  /**
   * Name presented to user.
   */
  public static final String DISPLAY_NAME = "Simple";
  
  @Override
  public ContentInferrer create() {
    if (singleton != null) {
      return singleton;
    }
    LOG.debug("Creating new " + NAME);
    singleton = new Simple();
    return singleton;
  }

  @Override
  public String getName() {
    return NAME;
  }
  

  @Override
  public String getDisplayName() {
    return DISPLAY_NAME;
  }

  @Override
  public String getModuleDescription() {
    return NAME;
  }

  @Override
  public List<String> getCapabilities() {
    return Collections.<String>emptyList(); 
  }

  @Override
  public String getUserModuleDescription() {
    return "Simple heuristic to obtain XSD primitive types: string, boolean, decimal, ...";
  }
}
