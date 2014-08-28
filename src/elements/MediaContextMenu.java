/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import actions.PlayAction;
import actions.RemoveAction;
import actions.SeenToggleAction;
import database.FilePlayer;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import misc.Utils;

public class MediaContextMenu extends JPopupMenu
{

  private final MediaElement media;
  private final JTable table;

  public MediaContextMenu(final MediaElement item, JTable table)
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

    Utils.setCurrentContextMenu(item);
  }

  @Override
  protected void firePopupMenuWillBecomeInvisible()
  {
    super.firePopupMenuWillBecomeInvisible();
    Utils.setCurrentContextMenu(null);
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
    removeItem.addActionListener(new RemoveAction(media, table));

    add(removeItem);
  }

  private void seenButton()
  {
    final JMenuItem seenToggle = new JMenuItem("Toggle seen");
    seenToggle.setAccelerator(KeyStroke.getKeyStroke('s'));
    seenToggle.addActionListener(new SeenToggleAction(media, table));
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
    play.setAccelerator(KeyStroke.getKeyStroke("Enter"));
    play.addActionListener(new PlayAction(media, table));
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
