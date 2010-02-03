package cz.cuni.mff.vektor.example.client;

import cz.cuni.mff.vektor.example.base.AppInterface;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

public final class SomeAction implements ActionListener {

  public void actionPerformed(ActionEvent e) {
    Lookup lkp = Lookups.forPath("AppInterfaceProviders");

    for (AppInterface mi : lkp.lookupAll(AppInterface.class)) {
      JOptionPane.showMessageDialog(null, mi.doSomething());
    }
  }
}
