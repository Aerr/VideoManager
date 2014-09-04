/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import database.DatabaseSaver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.SwingWorker;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author aerr
 */
public class IMDbDownloader extends SwingWorker<Void, Void>
{

  public static void downloadIMDBinfos(CButton button)
  {
    new IMDbDownloader(button).execute();
  }

  private final CButton button;

  public IMDbDownloader(CButton button)
  {
    this.button = button;
  }

  @Override
  protected Void doInBackground()
  {

    String search = "https://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
    search += button.getText().replace(" ", "+") + "+imdb";
    try
    {
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
        if (string.contains("title/tt"))
        {
          final int start = string.indexOf("/tt");
          if (start >= 0)
          {
            string = string.substring(start + 1);
            search = "http://www.omdbapi.com/?i=" + string.substring(0, string.indexOf("/"));
            url = new URL(search);
            connection = url.openConnection();

            builder = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null)
              builder.append(line);

            obj = new JSONObject(builder.toString());
            button.getImdbInfos()[0] = obj.getString("Title");
            button.getImdbInfos()[1] = obj.getString("Year");
            button.getImdbInfos()[2] = obj.getString("Runtime");
            button.getImdbInfos()[3] = obj.getString("Genre");
            button.getImdbInfos()[4] = obj.getString("Director");
            button.getImdbInfos()[5] = obj.getString("imdbRating");
            button.getImdbInfos()[6] = string;
            DatabaseSaver.save();
            ListManager.reloadList(true);
            break;
          }
        }
      }
    } catch (MalformedURLException ex)
    {
      System.err.println(search);
    } catch (IOException ex)
    {
      System.err.println(search);
    }
    return null;
  }
}
