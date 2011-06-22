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
package cz.cuni.mff.ksi.jinfer.attrstats.heuristics.improvement;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.HeuristicCallback;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.interfaces.ImprovementHeuristic;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class RandomRemove implements ImprovementHeuristic {

  private final double ratio;

  public RandomRemove(final double ratio) {
    super();
    if (ratio < 0 || ratio >= 1) {
      throw new IllegalArgumentException("Invalid ratio: " + ratio);
    }
    this.ratio = ratio;
  }

  @Override
  public void start(final AMModel model, final List<IdSet> feasiblePool,
        final HeuristicCallback callback) throws InterruptedException {

    final List<IdSet> ret = new ArrayList<IdSet>(feasiblePool.size());
    for (final IdSet idSet : feasiblePool) {
      final int toRemove = (int)Math.round(idSet.getMappings().size() * ratio);

      final List<AttributeMappingId> mappings = new ArrayList<AttributeMappingId>(idSet.getMappings());
      for (int i = 0; i < toRemove; i++) {
        mappings.remove((int)(Math.random() * mappings.size()));
      }

      ret.add(new IdSet(mappings));
    }

    callback.finished(ret);
  }

  @Override
  public String getName() {
    return "RandomRemove";
  }

  @Override
  public String getDisplayName() {
    return "Random Remove";
  }

  @Override
  public String getModuleDescription() {
    return getName() + ", ratio = " + ratio;
  }

}
