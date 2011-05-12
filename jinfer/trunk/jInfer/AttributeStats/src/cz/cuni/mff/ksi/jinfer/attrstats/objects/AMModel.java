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
package cz.cuni.mff.ksi.jinfer.attrstats.objects;

import cz.cuni.mff.ksi.jinfer.attrstats.logic.MappingUtils;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.base.utils.BaseUtils;
import cz.cuni.mff.ksi.jinfer.base.utils.CloneHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.tree.TreeNode;

/**
 * TODO vektor Comment!
 *
 * @author vektor
 */
public class AMModel {

  private final List<Element> grammar;

  private List<Triplet> flat = null;

  private TreeNode tree = null;

  private final Map<AttributeMappingId, AttributeMapping> mappings = new HashMap<AttributeMappingId, AttributeMapping>();

  public AMModel(final List<Element> grammar) {
    if (BaseUtils.isEmpty(grammar)) {
      throw new IllegalArgumentException("Grammar must not be empty");
    }
    final CloneHelper ch = new CloneHelper();
    this.grammar = ch.cloneGrammar(grammar);
  }

  public List<Triplet> getFlat() {
    if (flat == null) {
      flat = MappingUtils.extractFlat(grammar);
    }
    return flat;
  }

  public TreeNode getTree() {
    if (tree == null) {
      tree = MappingUtils.createTree(grammar);
    }
    return tree;
  }

  public Map<AttributeMappingId, AttributeMapping> getAMs() {
    if (mappings.isEmpty()) {
      for (final Triplet t : getFlat()) {
        final AttributeMappingId mapping = new AttributeMappingId(t.getElement(), t.getAttribute());
        if (!mappings.containsKey(mapping)) {
          mappings.put(mapping, new AttributeMapping(mapping));
        }
        mappings.get(mapping).getImage().add(t.getValue());
      }
    }
    return mappings;
  }

}
