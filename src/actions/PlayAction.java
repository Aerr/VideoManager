/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actions;

import database.FilePlayer;
import javax.swing.JTable;

/**
 *
 * @author aerr
 */
public class PlayAction extends CAbstractAction
{

  public PlayAction(Object[] mediasArray)
  {
    super(mediasArray);
  }

  @Override
  public void actionPerformed(JTable table)
  {
    new FilePlayer(getSelected(table)).execute();
  }

}
