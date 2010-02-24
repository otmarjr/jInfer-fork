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

import cz.cuni.mff.ksi.jinfer.base.interfaces.IGGeneratorCallback;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractNode;
import cz.cuni.mff.ksi.jinfer.base.objects.Element;
import cz.cuni.mff.ksi.jinfer.base.objects.Input;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Example runner for TrivialIGG, should be removed when the whole framework is operational.
 *
 * @author vektor
 */
public final class TestRun implements ActionListener {

  // TODO remove this Action

  @Override
  public void actionPerformed(ActionEvent e) {
    final IGGeneratorImpl i = new IGGeneratorImpl();
    final List<File> docs = new ArrayList<File>();
    docs.add(new File("C:\\domain.xml"));
    i.start(new Input(docs, null, null), new IGGeneratorCallback() {

      @Override
      public void finished(final List<AbstractNode> grammar) {
        final StringBuilder ret = new StringBuilder();
        for (final AbstractNode node : grammar) {
          ret.append(node.getName()).append(" = ");
          if (node instanceof Element) {
            for (final AbstractNode child : ((Element)node).getSubnodes()) {
              ret.append(child.getName()).append(", ");
            }
          }
          ret.append('\n');
        }
        JOptionPane.showMessageDialog(null, ret.toString());
      }
    });
  }
}
