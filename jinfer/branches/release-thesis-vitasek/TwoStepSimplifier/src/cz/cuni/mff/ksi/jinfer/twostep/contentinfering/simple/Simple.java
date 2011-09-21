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

import cz.cuni.mff.ksi.jinfer.base.interfaces.nodes.ContentNode;
import cz.cuni.mff.ksi.jinfer.twostep.contentinfering.ContentInferrer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class Simple implements ContentInferrer {
  @Override
  public String inferContentType(List<ContentNode> nodes) {
    Map<ContentNode, String> whatCouldBe = new HashMap<ContentNode, String>();
    for (ContentNode node : nodes) {
      
    }
    return "";
  }
  
}
