/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import static elements.IconDownloader.start;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author aerr
 */
public class IMDbDownloader extends SwingWorker<Void, Void>
{

  private final CButton button;

  public IMDbDownloader(CButton button)
  {
    this.button = button;
  }

  @Override
  protected Void doInBackground() throws Exception
  {
    String[] split = null;

    String search = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
    search += button.getName().replace(" ", "+") + "+imdb";
    URL url = new URL(search);

    URLConnection connection = url.openConnection();

    String line;
    StringBuilder builder = new StringBuilder();
    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    while ((line = reader.readLine()) != null)
      builder.append(line);

    String imageUrl = builder.toString();
    imageUrl = imageUrl.substring(imageUrl.indexOf(start) + start.length());
    split = imageUrl.split("\"url\":\"");

    for (String string : split)
      if (string.contains("www.imdb.com"))
      {
        System.out.println(string);
        break;
      }

    return null;
  }

  public static void main(String[] args)
  {
    try
    {
      String search = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
      search += "American Hustle (2013)".replace(" ", "+") + "+imdb";
      URL url = new URL(search);

      URLConnection connection = url.openConnection();

      String line;
      StringBuilder builder = new StringBuilder();
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      while ((line = reader.readLine()) != null)
        builder.append(line);

      JSONObject obj = new JSONObject(builder.toString());
      final JSONArray arr = obj.getJSONObject("responseData").getJSONArray("results");
      for (int i = 0; i < arr.length(); i++)
      {
        String string = arr.getJSONObject(i).getString("url");
        if (string.contains("www.imdb.com"))
        {
          final int start = string.indexOf("/tt");
          if (start >= 0)
          {
            search = "http://www.omdbapi.com/?i=" + string.substring(start + 1, string.length() - 1);
            url = new URL(search);
            connection = url.openConnection();

            builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null)
              builder.append(line);

            obj = new JSONObject(builder.toString());
            String[] imdbInfos =
            {
              obj.getString("Title"),
              obj.getString("Year"),
              obj.getString("Runtime"),
              obj.getString("Genre"),
              obj.getString("Director"),
              obj.getString("imdbRating")
            };
            for (String s : imdbInfos)
              System.out.println(s);
            break;
          }
        }
      }
    } catch (MalformedURLException ex)
    {
      Logger.getLogger(IMDbDownloader.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex)
    {
      Logger.getLogger(IMDbDownloader.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
