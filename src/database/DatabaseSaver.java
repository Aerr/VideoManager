/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import database.LocationsManager.Location;
import gui.Gui;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;
import javax.swing.SwingWorker;
import listing.FileWalker;

public class DatabaseSaver extends SwingWorker<Void, Void>
{

  @Override
  protected Void doInBackground() throws Exception
  {
    try
    {
      final String name = ((Location) Gui.getInstance().getjComboBox().getSelectedItem()).getPath();
      FileOutputStream fos = new FileOutputStream(name.hashCode() + ".db");
      GZIPOutputStream gzos = new GZIPOutputStream(fos);
      try (ObjectOutputStream oos = new ObjectOutputStream(gzos))
      {
        oos.writeObject(FileWalker.getInstance().getSetButtons());
        oos.flush();
      }
    } catch (java.io.IOException ex)
    {
      Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
}
