/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import elements.CButton;
import java.io.FileInputStream;
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
}
