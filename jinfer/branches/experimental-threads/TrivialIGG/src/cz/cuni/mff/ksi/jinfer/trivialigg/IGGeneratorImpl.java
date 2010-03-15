/*
 *  Copyright (C) 2010 vektor
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
package cz.cuni.mff.ksi.jinfer.trivialigg;

import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGenerator;
import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A trivial implementation of IGGenerator module. Works only with XML documents.
 * 
 * @author vektor
 */
public class IGGeneratorImpl implements IGGenerator {

  private static final Logger LOG = Logger.getLogger(IGGeneratorImpl.class.getCanonicalName());

  @Override
  public String getModuleName() {
    return "Trivial IG Generator";
  }

  @Override
  public void start(final Input input, final IGGeneratorCallback callback) {
    final List<ParserThread> threads = new ArrayList<ParserThread>(input.getDocuments().size());

    for (final File f : input.getDocuments()) {
      final ParserThread t = new ParserThread(f);
      threads.add(t);
      t.start();
    }

    final List<AbstractNode> rules = new ArrayList<AbstractNode>();
    for (final ParserThread t : threads) {
      try {
        t.join();
        rules.addAll(t.getHandler().getRules());
      } catch (InterruptedException ex) {
        LOG.log(Level.SEVERE, "Parser thread timeouted", ex);
      }
    }

    callback.finished(rules);
  }

}
