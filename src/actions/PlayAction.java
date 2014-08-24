/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import database.FilePlayer;
import elements.MediaElement;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTable;

/**
 *
 * @author aerr
 */
public class PlayAction extends AbstractAction
{

  private final Object[] medias;

  public PlayAction(Object[] mediasArray)
  {
    this.medias = mediasArray;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    JTable table = (JTable) e.getSource();
    new FilePlayer(getSelected(table)).execute();
  }

  private MediaElement getSelected(JTable table)
  {
    return (MediaElement) medias[table.convertRowIndexToModel(table.getSelectedRow())];
  }

}
