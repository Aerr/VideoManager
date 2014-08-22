/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import database.FilePlayer;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author aerr
 */
public class CCellEditor extends DefaultCellEditor
{

  private final JTable table;
  private final Object[] medias;

  public CCellEditor(JTable table, Object[] medias)
  {
    super(new JTextField());
    this.table = table;
    this.medias = medias;
  }

  @Override
  public boolean isCellEditable(EventObject anEvent)
  {
    if (anEvent.getClass() == java.awt.event.ActionEvent.class)
      return true;
    if (anEvent.getClass() == MouseEvent.class)
    {
      MouseEvent e = (MouseEvent) anEvent;
      if (e.getClickCount() == 3)
        return true;
      else if (e.getClickCount() == 2)
      {
        MediaElement selected = (MediaElement) medias[table.convertRowIndexToModel(table.getSelectedRow())];
        new FilePlayer(selected).execute();
      }
    }
    return false;
  }

}
