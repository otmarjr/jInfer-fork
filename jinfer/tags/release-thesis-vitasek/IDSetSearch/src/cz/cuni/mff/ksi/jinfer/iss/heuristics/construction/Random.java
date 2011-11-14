/*
 * Copyright (C) 2011 vektor
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
package cz.cuni.mff.ksi.jinfer.iss.heuristics.construction;

import cz.cuni.mff.ksi.jinfer.iss.utils.MappingUtils;
import cz.cuni.mff.ksi.jinfer.iss.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.ConstructionHeuristic;
import cz.cuni.mff.ksi.jinfer.iss.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.iss.utils.Utils;
import java.util.ArrayList;
import java.util.List;

/**
 * A trivial construction heuristics. Incrementally builds the solution by
 * adding random AMs that don't break the ID set property.
 *
 * @author vektor
 */
public class Random implements ConstructionHeuristic {

  @Override
  public void start(final Experiment experiment,
        final HeuristicCallback callback) throws InterruptedException {
    final List<IdSet> ret = new ArrayList<IdSet>(experiment.getPoolSize());
    final List<AttributeMappingId> candidates = MappingUtils.getCandidates(experiment.getModel());

    for (int i = 0; i < experiment.getPoolSize(); i++) {
      final List<AttributeMappingId> idSet = new ArrayList<AttributeMappingId>();
      final List<AttributeMappingId> cs = new ArrayList<AttributeMappingId>(candidates);

      while (!cs.isEmpty()) {
        // pick one from cs at random
        final AttributeMappingId random = cs.get((int)(cs.size() * Math.random()));
        // if possible, add it to the ID set
        if (MappingUtils.isIDset(Utils.append(idSet, random), experiment.getModel())) {
          idSet.add(random);
        }
        // remove it from cs
        cs.remove(random);
      }

      ret.add(new IdSet(idSet));
    }

    callback.finished(ret);
  }

  @Override
  public String getName() {
    return "Random";
  }

  @Override
  public String getDisplayName() {
    return getName();
  }

  @Override
  public String getModuleDescription() {
    return getName();
  }

}
