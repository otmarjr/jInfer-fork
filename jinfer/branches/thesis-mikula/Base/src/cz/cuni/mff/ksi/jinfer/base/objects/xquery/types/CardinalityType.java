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
package cz.cuni.mff.ksi.jinfer.base.objects.xquery.types;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.types.AbstractType;
import cz.cuni.mff.ksi.jinfer.base.objects.xquery.syntaxtree.nodes.Cardinality;

/**
 * An abstract class representing types assigned with a cardinality.
 * @see Cardinality
 * @author rio
 */
public abstract class CardinalityType extends AbstractType {
  
  private final Cardinality cardinality;
  
  protected CardinalityType(final Cardinality cardinality) {
    this.cardinality = cardinality;
  }
  
  /**
   * Returns the cardinality of this type.
   * @return 
   */
  public Cardinality getCardinality() {
    return cardinality;
  }
}
