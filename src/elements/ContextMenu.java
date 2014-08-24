/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import database.DatabaseSaver;
import database.FilePlayer;
import gui.Gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import misc.Utils;

public class ContextMenu extends JPopupMenu
{

  private final MediaElement media;
  private final JTable table;

  public ContextMenu(final MediaElement item, JTable table)
  {
    this.media = item;
    this.table = table;

    setBorder(BorderFactory.createLineBorder(Color.black));

    playButton();
    playListButton();
    seenButton();

    addSeparator();

    renameButton();

    removeButton();

    Utils.setCurrentContextMenu(this);
  }

  @Override
  protected void firePopupMenuWillBecomeInvisible()
  {
    super.firePopupMenuWillBecomeInvisible();
    refreshTable();
  }


  private void renameButton()
  {
    JMenuItem renameItem = new JMenuItem("Rename");
    renameItem.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent evt)
      {
        table.editCellAt(table.getSelectedRow(), 0);
        if (table.getEditorComponent() != null)
          table.getEditorComponent().requestFocus();
      }
    });
    add(renameItem);
  }

  private void refreshTable()
  {
    table.revalidate();
    table.repaint();
  }

  private void removeButton()
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
          getMedia().setVisible(false);
          Gui.getInstance().updateSearchBar();
          new DatabaseSaver().execute();
        }
      }
    }
    );

    add(removeItem);
  }

  private void seenButton()
  {
    final JMenuItem seenToggle = new JMenuItem("Toggle seen");
    seenToggle.setAccelerator(KeyStroke.getKeyStroke('s'));
    seenToggle.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        getMedia().setSeen(!media.getSeen());
        refreshTable();
        new DatabaseSaver().execute();
      }
    });
    add(seenToggle);
  }

  private void playListButton()
  {
    final JMenuItem playList = new JMenuItem("Play media and following");
    playList.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        final CButton cButton = getMedia().getParent();
        new FilePlayer(cButton.getMedias().toArray(new MediaElement[cButton.getMedias().size()])).execute();
      }
    });
    add(playList);
  }

  private void playButton()
  {
    final JMenuItem play = new JMenuItem("Play media");
    play.setAccelerator(KeyStroke.getKeyStroke('p'));
    play.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        new FilePlayer(getMedia()).execute();
      }
    });
    add(play);
  }

  /**
   * @return the media
   */
  public MediaElement getMedia()
  {
    return media;
  }
}
