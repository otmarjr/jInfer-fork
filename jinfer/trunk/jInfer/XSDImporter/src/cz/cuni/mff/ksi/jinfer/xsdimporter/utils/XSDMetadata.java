/*
 *  Copyright (C) 2010 reseto
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cuni.mff.ksi.jinfer.xsdimporter.utils;

import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;

/**
 *
 * @author reseto
 */
public class XSDMetadata {

  private RegexpInterval interval = null;
  private String ref = null;

  public RegexpInterval getInterval() {
    return interval;
  }

  public void setInterval(final RegexpInterval interval) {
    this.interval = interval;
  }

  public String getRef() {
    return ref;
  }

  public void setRef(final String ref) {
    this.ref = ref;
  }

  public boolean hasOccurence() {
    return (interval != null);
  }
}
