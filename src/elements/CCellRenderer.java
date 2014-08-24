/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import misc.Utils;

/**
 *
 * @author aerr
 */
public class CCellRenderer extends DefaultTableCellRenderer
{
  private final Object[] medias;

  public CCellRenderer(Object[] medias)
  {
    this.medias = medias;
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
  {
    MediaElement media = (MediaElement) medias[table.convertRowIndexToModel(row)];
    isSelected = isSelected && (table.hasFocus() || Utils.isContextMenuDisplayed(media));
    final Component superResult = super.getTableCellRendererComponent(table, value,
                                                                      isSelected, hasFocus, row, column);

    if (isSelected)
      superResult.setBackground(Color.darkGray);
    else
      superResult.setBackground(null);

    if (media != null)
      if (media.getSeen())
        superResult.setForeground(Color.gray);
      else
        superResult.setForeground(Color.white);

    return superResult;
  }

}
