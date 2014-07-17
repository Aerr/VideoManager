/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

class PopUpDemo extends JPopupMenu
{

  public PopUpDemo()
  {
    setBorder(BorderFactory.createLineBorder(Color.black));
    add(new JMenuItem("Read"));
    add(new JMenuItem("Mark as seen"));
    add(new JMenuItem("Rename"));
    add(new JMenuItem("Change image"));
  }
}
