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

import cz.cuni.mff.ksi.jinfer.functionalDependencies.InitialModel;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.RXMLTree;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.Repairer;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.interfaces.RepairerCallback;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author sviro
 */
@ServiceProvider(service = Repairer.class)
public class RepairerImpl implements Repairer {

  private static final Logger LOG = Logger.getLogger(RepairerImpl.class);
  
  @Override
  public void start(InitialModel model, RepairerCallback callback) throws InterruptedException {
    List<RXMLTree> result = new ArrayList<RXMLTree>();
    
    List<FD> functionalDependencies = model.getFunctionalDependencies();
    for (RXMLTree rXMLTree : model.getTrees()) {
      RXMLTree repairedTree = repairRXMLTree(rXMLTree, functionalDependencies);
      if (repairedTree != null) {
        result.add(repairedTree);
      }
    }
    callback.finished(result);
    return;
  }

  private RXMLTree repairRXMLTree(RXMLTree rXMLTree, List<FD> functionalDependencies) {
    for (FD fd : functionalDependencies) {
      if (!rXMLTree.isSatisfyingFD(fd)) {
//        List<Pair<Tuple, Tuple>> tuplePairs = TupleFactory.getTuplePairs(rXMLTree.getTuples());
        LOG.debug("XML is inconsistent to FD.");
      }
    }
    
    return null;
  }
  
}
