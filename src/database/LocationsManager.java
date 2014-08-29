/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aerr
 */
public class LocationsManager
{

  static public class Location
  {
    private final String name;
    private final String path;

    public Location(File file)
    {
      this.name = file.getName();
      this.path = file.getAbsolutePath();
    }
    public Location(String name, String path)
    {
      this.name = name;
      this.path = path;
    }

    /**
     * @return the name
     */
    public String getName()
    {
      return name;
    }

    /**
     * @return the path
     */
    public String getPath()
    {
      return path;
    }

    @Override
    public String toString()
    {
      return name;
    }

    public String toExport()
    {
      return name + ":|:" + path + System.lineSeparator();
    }

  }

  static public Location[] getLocations()
  {
    Charset charset = Charset.forName("UTF-8");
    try (BufferedReader reader = Files.newBufferedReader(new File("VMlocations.ini").toPath(), charset))
    {
      String line;
      StringBuilder builder = new StringBuilder();

      while ((line = reader.readLine()) != null)
        builder.append(line).append(System.lineSeparator());
      final String[] split = builder.toString().split(System.lineSeparator());
      final Location[] result = new Location[split.length];

      for (int i = 0; i < split.length; i++)
      {
        final String[] parts = split[i].split(":|:");
        result[i] = new Location(parts[0], parts[2]);
      }

      return result;
    } catch (IOException x)
    {
      System.err.format("IOException: %s%n", x);
    }
    return null;
  }

  static public void addLocation(Location newLocation)
  {
    Charset charset = Charset.forName("UTF-8");
    final File file = new File("VMlocations.ini");

    if (!file.exists())
      try
      {
        file.createNewFile();
      } catch (IOException ex)
      {
        Logger.getLogger(LocationsManager.class.getName()).log(Level.SEVERE, null, ex);
      }

    try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), charset, StandardOpenOption.APPEND))
    {
      writer.write(newLocation.toExport(), 0, newLocation.toExport().length());
    } catch (IOException x)
    {
      System.err.format("IOException: %s%n", x);
    }
  }

}
