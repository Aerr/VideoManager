/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import database.FilePlayer;
import elements.MediaElement;
import javax.swing.JTable;

/**
 *
 * @author aerr
 */
public class PlayAction extends CAbstractAction
{

  public PlayAction()
  {
    super();
  }

  public PlayAction(MediaElement media, JTable parentTable)
  {
    super(media, parentTable);
  }

  @Override
  public void actionPerformed(JTable table)
  {
    final MediaElement[] selected = getSelected(table);
    if (table != null || selected.length > 1)
      new FilePlayer(selected).execute();
    else
      new FilePlayer(media).execute();
  }

}
