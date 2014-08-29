/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import elements.ListManager;
import elements.MediaElement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class RemoveAction extends CAbstractAction
{

  @Override
  public void actionPerformed(JTable table)
  {
    String text = "<html><p>Are you sure you want to remove this media(s) from your library?</p>"
                  + "<p><font size=-2>(The file(s) will not be removed from your drive)</font></p></html>";

    int confirmation = JOptionPane.showConfirmDialog(null, new JLabel(text),
                                                     "Confirm", JOptionPane.OK_CANCEL_OPTION);
    if (confirmation == JOptionPane.OK_OPTION)
      if (table != null)
        for (MediaElement mediaElement : getSelected(table))
          mediaElement.setVisible(false);
      else
        media.setVisible(false);

    ListManager.searchBarUpdate();
  }

  public RemoveAction()
  {
    super();
  }

  public RemoveAction(MediaElement media, JTable parentTable)
  {
    super(media, parentTable);
  }

}
