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
package cz.cuni.mff.ksi.jinfer.base.xqueryanalyzer.types;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.xqanalyser.Cardinality;

/**
 *
 * @author rio
 */
public class NodeType extends CardinalityType {
  
  public enum NType {
    ANY_KIND,
    ATTRIBUTE,
    COMMENT,
    DOCUMENT,
    ELEMENT,
    PROCESSING_INSTRUCTION,
    SCHEMA_ATTRIBUTE,
    SCHEMA_ELEMENT,
    TEXT; 
  }
  
  private final NType nType;
  
  public NodeType(final NType nType, final Cardinality cardinality) {
    super(cardinality);
    this.nType = nType;
  }

  @Override
  public Category getCategory() {
    return Category.NODE;
  }

  @Override
  public boolean isNumeric() {
    return false;
  }
  
  public NType getNType() {
    return nType;
  }
}
