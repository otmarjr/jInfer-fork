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
package cz.cuni.mff.ksi.jinfer.xqueryanalyzer;

import cz.cuni.mff.ksi.jinfer.base.objects.xquery.xqueryprocessor.types.XSDType;

/**
 *
 * @author rio
 */
public class XSDAtomicTypesUtils {

  public static XSDType.XSDAtomicType selectMoreSpecific(final XSDType.XSDAtomicType type1, final XSDType.XSDAtomicType type2) {
    if (type1 == XSDType.XSDAtomicType.STRING) {
      return type2;
    }
    
    if (type2 == XSDType.XSDAtomicType.STRING) {
      return type1;
    }
    
    if (type1 == type2) {
      return type1;
    }
    
    ////////////////////////////////////////////////
    
    if (type1 == XSDType.XSDAtomicType.DOUBLE) {
      if (type2 == XSDType.XSDAtomicType.FLOAT
              || type2 == XSDType.XSDAtomicType.DECIMAL
              || type2 == XSDType.XSDAtomicType.INTEGER
              || type2 == XSDType.XSDAtomicType.LONG
              || type2 == XSDType.XSDAtomicType.INT
              || type2 == XSDType.XSDAtomicType.SHORT
              || type2 == XSDType.XSDAtomicType.BYTE
              || type2 == XSDType.XSDAtomicType.NON_POSITIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.NEGATIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.NON_NEGATIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.POSITIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.UNSIGNED_BYTE
              || type2 == XSDType.XSDAtomicType.UNSIGNED_LONG
              || type2 == XSDType.XSDAtomicType.UNSIGNED_INT
              || type2 == XSDType.XSDAtomicType.SHORT) {
        return type2;
      }
    }
    
    if (type2 == XSDType.XSDAtomicType.DOUBLE) {
      if (type1 == XSDType.XSDAtomicType.FLOAT
              || type1 == XSDType.XSDAtomicType.DECIMAL
              || type1 == XSDType.XSDAtomicType.INTEGER
              || type1 == XSDType.XSDAtomicType.LONG
              || type1 == XSDType.XSDAtomicType.INT
              || type1 == XSDType.XSDAtomicType.SHORT
              || type1 == XSDType.XSDAtomicType.BYTE
              || type1 == XSDType.XSDAtomicType.NON_POSITIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.NEGATIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.NON_NEGATIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.POSITIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.UNSIGNED_BYTE
              || type1 == XSDType.XSDAtomicType.UNSIGNED_LONG
              || type1 == XSDType.XSDAtomicType.UNSIGNED_INT
              || type1 == XSDType.XSDAtomicType.SHORT) {
        return type1;
      }
    }
    
    ////////////////////////////////////////////////
    
    if (type1 == XSDType.XSDAtomicType.FLOAT) {
      if (type2 == XSDType.XSDAtomicType.DECIMAL
              || type2 == XSDType.XSDAtomicType.INTEGER
              || type2 == XSDType.XSDAtomicType.LONG
              || type2 == XSDType.XSDAtomicType.INT
              || type2 == XSDType.XSDAtomicType.SHORT
              || type2 == XSDType.XSDAtomicType.BYTE
              || type2 == XSDType.XSDAtomicType.NON_POSITIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.NEGATIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.NON_NEGATIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.POSITIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.UNSIGNED_BYTE
              || type2 == XSDType.XSDAtomicType.UNSIGNED_LONG
              || type2 == XSDType.XSDAtomicType.UNSIGNED_INT
              || type2 == XSDType.XSDAtomicType.SHORT) {
        return type2;
      }
    }
    
    if (type2 == XSDType.XSDAtomicType.FLOAT) {
      if (type1 == XSDType.XSDAtomicType.DECIMAL
              || type1 == XSDType.XSDAtomicType.INTEGER
              || type1 == XSDType.XSDAtomicType.LONG
              || type1 == XSDType.XSDAtomicType.INT
              || type1 == XSDType.XSDAtomicType.SHORT
              || type1 == XSDType.XSDAtomicType.BYTE
              || type1 == XSDType.XSDAtomicType.NON_POSITIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.NEGATIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.NON_NEGATIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.POSITIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.UNSIGNED_BYTE
              || type1 == XSDType.XSDAtomicType.UNSIGNED_LONG
              || type1 == XSDType.XSDAtomicType.UNSIGNED_INT
              || type1 == XSDType.XSDAtomicType.SHORT) {
        return type1;
      }
    }
    
    ////////////////////////////////////////////////
    
    if (type1 == XSDType.XSDAtomicType.FLOAT) {
      if (type2 == XSDType.XSDAtomicType.DECIMAL
              || type2 == XSDType.XSDAtomicType.INTEGER
              || type2 == XSDType.XSDAtomicType.LONG
              || type2 == XSDType.XSDAtomicType.INT
              || type2 == XSDType.XSDAtomicType.SHORT
              || type2 == XSDType.XSDAtomicType.BYTE
              || type2 == XSDType.XSDAtomicType.NON_POSITIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.NEGATIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.NON_NEGATIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.POSITIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.UNSIGNED_BYTE
              || type2 == XSDType.XSDAtomicType.UNSIGNED_LONG
              || type2 == XSDType.XSDAtomicType.UNSIGNED_INT
              || type2 == XSDType.XSDAtomicType.SHORT) {
        return type2;
      }
    }
    
    if (type2 == XSDType.XSDAtomicType.FLOAT) {
      if (type1 == XSDType.XSDAtomicType.DECIMAL
              || type1 == XSDType.XSDAtomicType.INTEGER
              || type1 == XSDType.XSDAtomicType.LONG
              || type1 == XSDType.XSDAtomicType.INT
              || type1 == XSDType.XSDAtomicType.SHORT
              || type1 == XSDType.XSDAtomicType.BYTE
              || type1 == XSDType.XSDAtomicType.NON_POSITIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.NEGATIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.NON_NEGATIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.POSITIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.UNSIGNED_BYTE
              || type1 == XSDType.XSDAtomicType.UNSIGNED_LONG
              || type1 == XSDType.XSDAtomicType.UNSIGNED_INT
              || type1 == XSDType.XSDAtomicType.SHORT) {
        return type1;
      }
    }
    
    ////////////////////////////////////////////////
    
    if (type1 == XSDType.XSDAtomicType.DECIMAL) {
      if (type2 == XSDType.XSDAtomicType.INTEGER
              || type2 == XSDType.XSDAtomicType.LONG
              || type2 == XSDType.XSDAtomicType.INT
              || type2 == XSDType.XSDAtomicType.SHORT
              || type2 == XSDType.XSDAtomicType.BYTE
              || type2 == XSDType.XSDAtomicType.NON_POSITIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.NEGATIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.NON_NEGATIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.POSITIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.UNSIGNED_BYTE
              || type2 == XSDType.XSDAtomicType.UNSIGNED_LONG
              || type2 == XSDType.XSDAtomicType.UNSIGNED_INT
              || type2 == XSDType.XSDAtomicType.SHORT) {
        return type2;
      }
    }
    
    if (type2 == XSDType.XSDAtomicType.DECIMAL) {
      if (type1 == XSDType.XSDAtomicType.INTEGER
              || type1 == XSDType.XSDAtomicType.LONG
              || type1 == XSDType.XSDAtomicType.INT
              || type1 == XSDType.XSDAtomicType.SHORT
              || type1 == XSDType.XSDAtomicType.BYTE
              || type1 == XSDType.XSDAtomicType.NON_POSITIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.NEGATIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.NON_NEGATIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.POSITIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.UNSIGNED_BYTE
              || type1 == XSDType.XSDAtomicType.UNSIGNED_LONG
              || type1 == XSDType.XSDAtomicType.UNSIGNED_INT
              || type1 == XSDType.XSDAtomicType.SHORT) {
        return type1;
      }
    }
    
    ////////////////////////////////////////////////
    
    if (type1 == XSDType.XSDAtomicType.INTEGER) {
      if (type2 == XSDType.XSDAtomicType.LONG
              || type2 == XSDType.XSDAtomicType.INT
              || type2 == XSDType.XSDAtomicType.SHORT
              || type2 == XSDType.XSDAtomicType.BYTE
              || type2 == XSDType.XSDAtomicType.NON_POSITIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.NEGATIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.NON_NEGATIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.POSITIVE_INTEGER
              || type2 == XSDType.XSDAtomicType.UNSIGNED_BYTE
              || type2 == XSDType.XSDAtomicType.UNSIGNED_LONG
              || type2 == XSDType.XSDAtomicType.UNSIGNED_INT
              || type2 == XSDType.XSDAtomicType.SHORT) {
        return type2;
      }
    }
    
    if (type2 == XSDType.XSDAtomicType.INTEGER) {
      if (type1 == XSDType.XSDAtomicType.LONG
              || type1 == XSDType.XSDAtomicType.INT
              || type1 == XSDType.XSDAtomicType.SHORT
              || type1 == XSDType.XSDAtomicType.BYTE
              || type1 == XSDType.XSDAtomicType.NON_POSITIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.NEGATIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.NON_NEGATIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.POSITIVE_INTEGER
              || type1 == XSDType.XSDAtomicType.UNSIGNED_BYTE
              || type1 == XSDType.XSDAtomicType.UNSIGNED_LONG
              || type1 == XSDType.XSDAtomicType.UNSIGNED_INT
              || type1 == XSDType.XSDAtomicType.SHORT) {
        return type1;
      }
    }
    
    ////////////////////////////////////////////////
    
    // TODO rio Nekompletne, ale asi bude stacit, pre nase ucely.
    return XSDType.XSDAtomicType.STRING;
  }
}
