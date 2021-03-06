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
import cz.cuni.mff.ksi.jinfer.iss.objects.AMModel;
import cz.cuni.mff.ksi.jinfer.iss.objects.AttributeMappingId;
import cz.cuni.mff.ksi.jinfer.iss.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.iss.utils.WeightComparator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A simple construction heuristic. Orders all AMs according to their weight,
 * creates a solution containing all AMs (and thus probably violating the ID set
 * condition) and starting from the least valuable tries to remove them from
 * the solution. As soon as the solution becomes an ID set, it is returned.
 *
 * Note that the pool created by this heuristic contains only one solution
 * ({@link IdSet}).
 *
 * @author vektor
 */
public class Removal implements ConstructionHeuristic {

  @Override
  public void start(final Experiment experiment,
        final HeuristicCallback callback) throws InterruptedException {

    final AMModel model = experiment.getModel();
    final List<AttributeMappingId> candidates = MappingUtils.getCandidates(model);
    Collections.sort(candidates, new WeightComparator(experiment.getModel(), experiment.getAlpha(), experiment.getBeta()));
    final List<AttributeMappingId> reverse = new ArrayList<AttributeMappingId>(candidates);
    Collections.reverse(reverse);

    for (final AttributeMappingId id : reverse) {
      if (MappingUtils.isIDset(candidates, model)) {
        break;
      }
      candidates.remove(id);
    }

    final IdSet solution = new IdSet(candidates);
    callback.finished(Arrays.asList(solution));
  }

  @Override
  public String getName() {
    return "Removal";
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
