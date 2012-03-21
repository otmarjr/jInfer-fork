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
package cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.regexptostringsize;

import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.utils.CollectionToString;
import cz.cuni.mff.ksi.jinfer.twostep.processing.automatonmergingstate.evaluating.RegexpEvaluator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * TODO anti Comment!
 *
 * @author anti
 */
public class ToStringSize<T> implements RegexpEvaluator<T> {

  private static final Logger LOG = Logger.getLogger(ToStringSize.class);

  @Override
  public void setInputStrings(List<List<T>> inputStrings) {
  }

  private static String getDelimiter(final RegexpType t) {
    switch (t) {
      case CONCATENATION:
        return ",";
      case ALTERNATION:
        return "|";
      case PERMUTATION:
        return "&";
      default:
        throw new IllegalStateException("Invalid regexp type at this point: " + t);
    }
  }

  private String rToString(Regexp<T> x) {
    if (x.getType() != null) {
      switch (x.getType()) {
        case TOKEN:
          return "T" + x.getInterval().toString();
        case CONCATENATION:
        case ALTERNATION:
        case PERMUTATION:
          return CollectionToString.colToString(x.getChildren(), getDelimiter(x.getType()),
                  new CollectionToString.ToString<Regexp<T>>() {

                    @Override
                    public String toString(final Regexp<T> t) {
                      return t.toString();
                    }
                  })
                  + x.getInterval().toString();
        case LAMBDA:
          return "\u03BB";
        default:
          throw new IllegalArgumentException("Unknown enum member " + x.getType());
      }
    } else {
      return "RegexpType=null";
    }
  }

  @Override
  public double evaluate(Regexp<T> x) throws InterruptedException {
    LOG.debug("Evalvator tostring: " + rToString(x));
    return rToString(x).length();
  }
}
