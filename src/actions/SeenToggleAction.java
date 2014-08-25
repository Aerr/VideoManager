/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import elements.MediaElement;
import javax.swing.JTable;

/**
 *
 * @author aerr
 */
public class SeenToggleAction extends CAbstractAction
{

  public SeenToggleAction()
  {
    super();
  }

  public SeenToggleAction(MediaElement media, JTable parentTable)
  {
    super(media, parentTable);
  }

  @Override
  public void actionPerformed(JTable table)
  {
    if (table != null)
    {
      final MediaElement[] selected = getSelected(table);
      boolean toSet = !selected[0].getSeen();

      for (MediaElement mediaElement : selected)
        mediaElement.setSeen(toSet);
    }
    else
      media.toggleSeen();
  }

}
