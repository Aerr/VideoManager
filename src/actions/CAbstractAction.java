/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package actions;

import database.DatabaseSaver;
import elements.MediaElement;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTable;

/**
 *
 * @author aerr
 */
public abstract class CAbstractAction extends AbstractAction
{

  private final Object[] medias;

  public CAbstractAction(Object[] mediasArray)
  {
    this.medias = mediasArray;
  }

  MediaElement[] getSelected(JTable table)
  {
    MediaElement[] res = new MediaElement[table.getSelectedRowCount()];
    for (int i = 0; i < table.getSelectedRowCount(); i++)
      res[i] = (MediaElement) medias[table.convertRowIndexToModel(table.getSelectedRows()[i])];
    return res;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    JTable table = (JTable) e.getSource();

    actionPerformed(table);

    new DatabaseSaver().execute();
    table.revalidate();
    table.repaint();
  }

  abstract public void actionPerformed(JTable table);
}
