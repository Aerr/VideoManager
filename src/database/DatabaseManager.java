/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import elements.CButton;
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;
import listing.FileWalker;

/**
 *
 * @author aerr
 */
public class DatabaseManager
{

  private static DatabaseSaver saver;

  static public boolean load_database()
  {
    try
    {
      FileInputStream file = new FileInputStream("db-files");
      GZIPInputStream gzis = new GZIPInputStream(file);
      try (ObjectInputStream ois = new ObjectInputStream(gzis))
      {
        HashMap<String, TreeSet<CButton>> setButtons = (HashMap<String, TreeSet<CButton>>) ois.readObject();
        FileWalker.getInstance().setSetButtons(setButtons);
      }
    } catch (java.io.IOException | ClassNotFoundException e)
    {
      return false;
    }
    return true;
  }

  public static void playFile(final CButton item)
  {
    String name = item.getPath();

    try
    {
      if (System.getProperty("os.name").equals("Linux"))
        Runtime.getRuntime().exec(new String[]
        {
          "bash", "-c", "vlc \"" + item.getPath() + "\""
        });
      else
        Desktop.getDesktop().open(new File(name));
    } catch (IOException exception)
    {
      System.err.println("Could not open: " + name + System.lineSeparator());
    }

    item.setSeen(true);

    if (saver != null && !saver.isDone())
      saver.cancel(true);
    saver = new DatabaseSaver();
    saver.execute();
  }
}
