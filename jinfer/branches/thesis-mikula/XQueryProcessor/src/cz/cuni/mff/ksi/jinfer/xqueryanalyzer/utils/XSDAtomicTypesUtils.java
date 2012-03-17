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
package cz.cuni.mff.ksi.jinfer.xqueryanalyzer.utils;

import cz.cuni.mff.ksi.jinfer.base.objects.xsd.XSDBuiltinAtomicType;

/**
 *
 * @author rio
 */
public class XSDAtomicTypesUtils {

  public static XSDBuiltinAtomicType selectMoreSpecific(final XSDBuiltinAtomicType type1, final XSDBuiltinAtomicType type2) {
    if (type1 == XSDBuiltinAtomicType.STRING) {
      return type2;
    }
    
    if (type2 == XSDBuiltinAtomicType.STRING) {
      return type1;
    }
    
    if (type1 == type2) {
      return type1;
    }
    
    ////////////////////////////////////////////////
    
    if (type1 == XSDBuiltinAtomicType.DOUBLE) {
      if (type2 == XSDBuiltinAtomicType.FLOAT
              || type2 == XSDBuiltinAtomicType.DECIMAL
              || type2 == XSDBuiltinAtomicType.INTEGER
              || type2 == XSDBuiltinAtomicType.LONG
              || type2 == XSDBuiltinAtomicType.INT
              || type2 == XSDBuiltinAtomicType.SHORT
              || type2 == XSDBuiltinAtomicType.BYTE
              || type2 == XSDBuiltinAtomicType.NON_POSITIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.NEGATIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.NON_NEGATIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.POSITIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.UNSIGNED_BYTE
              || type2 == XSDBuiltinAtomicType.UNSIGNED_LONG
              || type2 == XSDBuiltinAtomicType.UNSIGNED_INT
              || type2 == XSDBuiltinAtomicType.SHORT) {
        return type2;
      }
    }
    
    if (type2 == XSDBuiltinAtomicType.DOUBLE) {
      if (type1 == XSDBuiltinAtomicType.FLOAT
              || type1 == XSDBuiltinAtomicType.DECIMAL
              || type1 == XSDBuiltinAtomicType.INTEGER
              || type1 == XSDBuiltinAtomicType.LONG
              || type1 == XSDBuiltinAtomicType.INT
              || type1 == XSDBuiltinAtomicType.SHORT
              || type1 == XSDBuiltinAtomicType.BYTE
              || type1 == XSDBuiltinAtomicType.NON_POSITIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.NEGATIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.NON_NEGATIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.POSITIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.UNSIGNED_BYTE
              || type1 == XSDBuiltinAtomicType.UNSIGNED_LONG
              || type1 == XSDBuiltinAtomicType.UNSIGNED_INT
              || type1 == XSDBuiltinAtomicType.SHORT) {
        return type1;
      }
    }
    
    ////////////////////////////////////////////////
    
    if (type1 == XSDBuiltinAtomicType.FLOAT) {
      if (type2 == XSDBuiltinAtomicType.DECIMAL
              || type2 == XSDBuiltinAtomicType.INTEGER
              || type2 == XSDBuiltinAtomicType.LONG
              || type2 == XSDBuiltinAtomicType.INT
              || type2 == XSDBuiltinAtomicType.SHORT
              || type2 == XSDBuiltinAtomicType.BYTE
              || type2 == XSDBuiltinAtomicType.NON_POSITIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.NEGATIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.NON_NEGATIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.POSITIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.UNSIGNED_BYTE
              || type2 == XSDBuiltinAtomicType.UNSIGNED_LONG
              || type2 == XSDBuiltinAtomicType.UNSIGNED_INT
              || type2 == XSDBuiltinAtomicType.SHORT) {
        return type2;
      }
    }
    
    if (type2 == XSDBuiltinAtomicType.FLOAT) {
      if (type1 == XSDBuiltinAtomicType.DECIMAL
              || type1 == XSDBuiltinAtomicType.INTEGER
              || type1 == XSDBuiltinAtomicType.LONG
              || type1 == XSDBuiltinAtomicType.INT
              || type1 == XSDBuiltinAtomicType.SHORT
              || type1 == XSDBuiltinAtomicType.BYTE
              || type1 == XSDBuiltinAtomicType.NON_POSITIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.NEGATIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.NON_NEGATIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.POSITIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.UNSIGNED_BYTE
              || type1 == XSDBuiltinAtomicType.UNSIGNED_LONG
              || type1 == XSDBuiltinAtomicType.UNSIGNED_INT
              || type1 == XSDBuiltinAtomicType.SHORT) {
        return type1;
      }
    }
    
    ////////////////////////////////////////////////
    
    if (type1 == XSDBuiltinAtomicType.FLOAT) {
      if (type2 == XSDBuiltinAtomicType.DECIMAL
              || type2 == XSDBuiltinAtomicType.INTEGER
              || type2 == XSDBuiltinAtomicType.LONG
              || type2 == XSDBuiltinAtomicType.INT
              || type2 == XSDBuiltinAtomicType.SHORT
              || type2 == XSDBuiltinAtomicType.BYTE
              || type2 == XSDBuiltinAtomicType.NON_POSITIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.NEGATIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.NON_NEGATIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.POSITIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.UNSIGNED_BYTE
              || type2 == XSDBuiltinAtomicType.UNSIGNED_LONG
              || type2 == XSDBuiltinAtomicType.UNSIGNED_INT
              || type2 == XSDBuiltinAtomicType.SHORT) {
        return type2;
      }
    }
    
    if (type2 == XSDBuiltinAtomicType.FLOAT) {
      if (type1 == XSDBuiltinAtomicType.DECIMAL
              || type1 == XSDBuiltinAtomicType.INTEGER
              || type1 == XSDBuiltinAtomicType.LONG
              || type1 == XSDBuiltinAtomicType.INT
              || type1 == XSDBuiltinAtomicType.SHORT
              || type1 == XSDBuiltinAtomicType.BYTE
              || type1 == XSDBuiltinAtomicType.NON_POSITIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.NEGATIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.NON_NEGATIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.POSITIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.UNSIGNED_BYTE
              || type1 == XSDBuiltinAtomicType.UNSIGNED_LONG
              || type1 == XSDBuiltinAtomicType.UNSIGNED_INT
              || type1 == XSDBuiltinAtomicType.SHORT) {
        return type1;
      }
    }
    
    ////////////////////////////////////////////////
    
    if (type1 == XSDBuiltinAtomicType.DECIMAL) {
      if (type2 == XSDBuiltinAtomicType.INTEGER
              || type2 == XSDBuiltinAtomicType.LONG
              || type2 == XSDBuiltinAtomicType.INT
              || type2 == XSDBuiltinAtomicType.SHORT
              || type2 == XSDBuiltinAtomicType.BYTE
              || type2 == XSDBuiltinAtomicType.NON_POSITIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.NEGATIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.NON_NEGATIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.POSITIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.UNSIGNED_BYTE
              || type2 == XSDBuiltinAtomicType.UNSIGNED_LONG
              || type2 == XSDBuiltinAtomicType.UNSIGNED_INT
              || type2 == XSDBuiltinAtomicType.SHORT) {
        return type2;
      }
    }
    
    if (type2 == XSDBuiltinAtomicType.DECIMAL) {
      if (type1 == XSDBuiltinAtomicType.INTEGER
              || type1 == XSDBuiltinAtomicType.LONG
              || type1 == XSDBuiltinAtomicType.INT
              || type1 == XSDBuiltinAtomicType.SHORT
              || type1 == XSDBuiltinAtomicType.BYTE
              || type1 == XSDBuiltinAtomicType.NON_POSITIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.NEGATIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.NON_NEGATIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.POSITIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.UNSIGNED_BYTE
              || type1 == XSDBuiltinAtomicType.UNSIGNED_LONG
              || type1 == XSDBuiltinAtomicType.UNSIGNED_INT
              || type1 == XSDBuiltinAtomicType.SHORT) {
        return type1;
      }
    }
    
    ////////////////////////////////////////////////
    
    if (type1 == XSDBuiltinAtomicType.INTEGER) {
      if (type2 == XSDBuiltinAtomicType.LONG
              || type2 == XSDBuiltinAtomicType.INT
              || type2 == XSDBuiltinAtomicType.SHORT
              || type2 == XSDBuiltinAtomicType.BYTE
              || type2 == XSDBuiltinAtomicType.NON_POSITIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.NEGATIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.NON_NEGATIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.POSITIVE_INTEGER
              || type2 == XSDBuiltinAtomicType.UNSIGNED_BYTE
              || type2 == XSDBuiltinAtomicType.UNSIGNED_LONG
              || type2 == XSDBuiltinAtomicType.UNSIGNED_INT
              || type2 == XSDBuiltinAtomicType.SHORT) {
        return type2;
      }
    }
    
    if (type2 == XSDBuiltinAtomicType.INTEGER) {
      if (type1 == XSDBuiltinAtomicType.LONG
              || type1 == XSDBuiltinAtomicType.INT
              || type1 == XSDBuiltinAtomicType.SHORT
              || type1 == XSDBuiltinAtomicType.BYTE
              || type1 == XSDBuiltinAtomicType.NON_POSITIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.NEGATIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.NON_NEGATIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.POSITIVE_INTEGER
              || type1 == XSDBuiltinAtomicType.UNSIGNED_BYTE
              || type1 == XSDBuiltinAtomicType.UNSIGNED_LONG
              || type1 == XSDBuiltinAtomicType.UNSIGNED_INT
              || type1 == XSDBuiltinAtomicType.SHORT) {
        return type1;
      }
    }
    
    ////////////////////////////////////////////////
    
    // TODO rio Nekompletne, ale asi bude stacit, pre nase ucely.
    return XSDBuiltinAtomicType.STRING;
  }
}
