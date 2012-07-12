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
package cz.cuni.mff.ksi.jinfer.base.interfaces.xquery;

/**
 * Interface for types used by XQuery Processor module.
 * 
 * There are four types:
 *  - XSD built-in type, for example xs:string, xs:int.
 *  - Node type, for example element, document.
 *  - Path type, for example $var/subnode/text().
 *  - Unknown type, representing the rest.
 * 
 * @author rio
 */
public interface Type {
  
  public enum Category {
    XSD_BUILT_IN,
    NODE,
    PATH,
    UNKNOWN;
  }
  
  /**
   * Returns the category of this type.
   */
  public Category getCategory();

  /**
   * Is this type numeric? Numeric types are XSD built-in atomic types that can
   * can cast to double.
   */
  public boolean isNumeric(); // TODO rio Do we want/need?
  
  /**
   * Is this type one of XSD built-in atomic types?
   */
  public boolean isXsdBuiltinType();
  
  /**
   * Is this type of the node type category?
   */
  public boolean isNodeType();
  
  /**
   * Is this type of the path type category?
   */
  public boolean isPathType();
  
  /**
   * Is this type of the unknown category?
   */
  public boolean isUnknownType();
}
