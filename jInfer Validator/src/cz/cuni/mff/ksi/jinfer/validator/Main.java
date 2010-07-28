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
package cz.cuni.mff.ksi.jinfer.validator;

import cz.cuni.mff.ksi.jinfer.validator.objects.Remark;
import java.awt.EventQueue;
import javax.swing.UIManager;

public final class Main {

  private Main() {
  }

  public static void main(final String[] args) {
    if (args.length == 2) {
      commandLine(args);
    }
    else {
      runGUI();
    }
  }

  private static void commandLine(final String[] args) {
    for (final Remark r : Logic.checkSuite(args[0], args[1], true)) {
      System.out.println(r.toString());
    }
  }

  private static void runGUI() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (final Exception ex) {
    }
    EventQueue.invokeLater(new Runnable() {

      @Override
      public void run() {
        new MainWnd().setVisible(true);
      }
    });
  }
}
