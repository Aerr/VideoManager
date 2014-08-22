/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import database.DatabaseSaver;
import gui.Gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

class ContextMenu extends JPopupMenu
{

  public ContextMenu(final CButton item)
  {
    setBorder(BorderFactory.createLineBorder(Color.black));

    playButton(item);
    playListButton(item);
    seenButton(item);

    addSeparator();

    renameButton(item);

    add(new JMenuItem("Change image"));
    removeButton(item);
  }

  private void renameButton(final CButton item)
  {
//    JMenuItem renameItem = new JMenuItem("Rename");
//    renameItem.addActionListener(new ActionListener()
//    {
//      @Override
//      public void actionPerformed(ActionEvent evt)
//      {
//        String name = JOptionPane.showInputDialog(Gui.getInstance(),
//                                                  "Rename the media?", item.getMediaName());
//        if (name != null)
//        {
//          item.setMediaName(name);
//          new DatabaseSaver().execute();
//        }
//      }
//    });
//    add(renameItem);
  }

  private void removeButton(final CButton item)
  {
    JMenuItem removeItem = new JMenuItem("Remove");;
    removeItem.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        String text = "<html><p>Are you sure you want to remove this media from your library?</p>"
                      + "<p><font size=-2>(The file will not be removed from your drive)</font></p></html>";

        int confirmation = JOptionPane.showConfirmDialog(null, new JLabel(text),
                                                         "Confirm", JOptionPane.OK_CANCEL_OPTION);
        if (confirmation == JOptionPane.OK_OPTION)
        {
          item.setVisible(false);
          Gui.getInstance().updateSearchBar();
          new DatabaseSaver().execute();
        }
      }
    }
    );

    add(removeItem);
  }

  private void seenButton(final CButton item)
  {
//    final JMenuItem seenToggle = new JMenuItem("Toggle seen");
//    seenToggle.setAccelerator(KeyStroke.getKeyStroke('s'));
//    seenToggle.addActionListener(new ActionListener()
//    {
//      @Override
//      public void actionPerformed(ActionEvent e)
//      {
//        item.setSeen(!item.getSeen());
//        new DatabaseSaver().execute();
//      }
//    });
//    add(seenToggle);
  }

  private void playListButton(final CButton item)
  {
//    final JMenuItem playList = new JMenuItem("Play media and following");
//    playList.addActionListener(new ActionListener()
//    {
//      @Override
//      public void actionPerformed(ActionEvent e)
//      {
//        final Component[] components = item.getParent().getParent().getComponents();
//        int i = 0;
//        for (Component component : components)
//        {
//          if (component == item.getParent())
//            break;
//          i++;
//        }
//
//        final CButton[] files = new CButton[components.length - i];
//        for (int j = 0; i < components.length; i++, j++)
//          files[j] = (CButton) ((ButtonHolder) components[i]).getComponent(0);
//        new FilePlayer(files).execute();
//      }
//    });
//    add(playList);
  }

  private void playButton(final CButton item)
  {
//    final JMenuItem play = new JMenuItem("Play media");
//    play.setAccelerator(KeyStroke.getKeyStroke('p'));
//    play.addActionListener(new ActionListener()
//    {
//      @Override
//      public void actionPerformed(ActionEvent e)
//      {
//        new FilePlayer(item).execute();
//      }
//    });
//    add(play);
  }
}
