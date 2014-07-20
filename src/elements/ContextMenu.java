/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import database.DatabaseSaver;
import database.FilePlayer;
import java.awt.Color;
import java.awt.Component;
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
    play.setAccelerator(KeyStroke.getKeyStroke('p'));
    play.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        new FilePlayer(item).execute();
      }
    });
    add(play);

    final JMenuItem playList = new JMenuItem("Play media and following");
    playList.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        final Component[] components = item.getParent().getParent().getComponents();
        int i = 0;
        for (Component component : components)
        {
          if (component == item.getParent())
            break;
          i++;
        }

        final CButton[] files = new CButton[components.length - i];
        for (int j = 0; i < components.length; i++, j++)
          files[j] = (CButton) ((ButtonHolder) components[i]).getComponent(0);
        new FilePlayer(files).execute();
      }
    });
    add(playList);

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
