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

  private final JTable parentTable;
  protected MediaElement media;

  public CAbstractAction()
  {
    this.parentTable = null;
  }

  public CAbstractAction(MediaElement media, JTable parentTable)
  {
    this.parentTable = parentTable;
    this.media = media;
  }

  MediaElement[] getSelected(JTable table)
  {
    if (table == null)
      table = parentTable;

    MediaElement[] res = new MediaElement[table.getSelectedRowCount()];
    for (int i = 0; i < table.getSelectedRowCount(); i++)
      res[i] = (MediaElement) table.getModel()
              .getValueAt(table.convertRowIndexToModel(table.getSelectedRows()[i]), 0);
    return res;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    final Object source = e.getSource();
    JTable table = (source.getClass() == JTable.class) ? (JTable) source : null;
    actionPerformed(table);

    new DatabaseSaver().execute();

    if (table == null)
      return;

    table.revalidate();
    table.repaint();
  }

  abstract public void actionPerformed(JTable table);
}
