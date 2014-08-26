/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import database.DatabaseSaver;
import database.FilePlayer;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author aerr
 */
public class CCellEditor extends DefaultCellEditor implements MouseListener
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
    // Normally: from context menu -> Rename
    if (anEvent == null)
      return beforeEdition();
    // Normally: from F2 press
    if (anEvent.getClass() == java.awt.event.ActionEvent.class)
      return beforeEdition();

    if (anEvent.getClass() == MouseEvent.class)
    {
      MouseEvent e = (MouseEvent) anEvent;
      if (e.getClickCount() == 2)
        new FilePlayer(getSelected()).execute();
    }
    return false;
  }

  private boolean beforeEdition()
  {
    final int selectedRow = table.getSelectedRows()[table.getSelectedRowCount() - 1];
    table.setRowSelectionInterval(selectedRow, selectedRow);
    return true;
  }

  @Override
  protected void fireEditingStopped()
  {
    super.fireEditingStopped();

    final String newName = table.getModel().getValueAt(table.getSelectedRow(), 0).toString();
    getSelected().setName(newName);
    new DatabaseSaver().execute();
  }

  private MediaElement getSelected()
  {
    return (MediaElement) medias[table.convertRowIndexToModel(table.getSelectedRow())];
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
    if (e.getButton() == MouseEvent.BUTTON3)
    {
      int row = table.rowAtPoint(e.getPoint());
      if (table.getSelectedRowCount() == 0)
        table.setRowSelectionInterval(row, row);
      MediaContextMenu menu = new MediaContextMenu(getSelected(), table);
      menu.show(e.getComponent(), e.getX(), e.getY());
    }
  }

  @Override
  public void mousePressed(MouseEvent e)
  {
  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
  }

  @Override
  public void mouseEntered(MouseEvent e)
  {
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
  }

}
