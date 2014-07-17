/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import database.DatabaseManager;
import database.DatabaseSaver;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

class ContextMenu extends JPopupMenu
{
  public ContextMenu(final CButton item)
  {
    setBorder(BorderFactory.createLineBorder(Color.black));
    final JMenuItem play = new JMenuItem("Play media");
    play.setAccelerator(KeyStroke.getKeyStroke('P'));
    play.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        DatabaseManager.playFile(item);
        new DatabaseSaver().execute();
      }
    });
    add(play);

    final JMenuItem seenToggle = new JMenuItem("Toggle seen");
    seenToggle.setAccelerator(KeyStroke.getKeyStroke('s'));
    seenToggle.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        item.setSeen(!item.getSeen());
        new DatabaseSaver().execute();
      }
    });
    add(seenToggle);

    addSeparator();
    add(new JMenuItem("Rename"));
    add(new JMenuItem("Change image"));
    add(new JMenuItem("Remove"));
  }
}
