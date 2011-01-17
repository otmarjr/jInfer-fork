/*
 *  Copyright (C) 2010 anti
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

package cz.cuni.mff.ksi.jinfer.twostep;

import java.util.List;

/**
 * Interface giving access to parameters of module, if it supports "parameters"
 * capability.
 *
 * @author anti
 */
public interface ModuleParameters {
  List<String> getParameterNames();
  String getParameterDisplayDescription(final String parameterName);
  String getParameterDefaultValue(final String parameterName);
  void setParameter(final String parameterName, final int newValue);
}
