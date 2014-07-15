package elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

public class IconDownloader extends SwingWorker<Void, Void>
{

  private final CButton button;
  private String resultURL;

  public IconDownloader(CButton button)
  {
    super();
    this.button = button;
  }

  @Override
  protected Void doInBackground() throws Exception
  {
    File f = new File("thumbs/" + button.getText().hashCode() + ".jpg");
    if (f.exists() && !f.isDirectory())
    {
      button.setImage(ImageIO.read(f), false);
      return null;
    }

    InputStream is = null;
    StringBuilder res = new StringBuilder();
    try
    {
      String search = button.getParentFolder() + "+" + button.getText() + "+poster";
      URL url = new URL("https://www.bing.com/images/search?q=" + search.replace(" ", "+"));

      is = url.openStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));

      String line = null;
      while ((line = br.readLine()) != null)
        res.append(line);

    } catch (IOException e)
    {
      e.printStackTrace();
    } finally
    {
      try
      {
        if (is != null)
          is.close();
      } catch (IOException e)
      {
        e.printStackTrace();
      }
    }

    resultURL = res.toString();
    resultURL = resultURL.substring(resultURL.indexOf("imgurl:&quot;") + "imgurl:&quot;".length());
    final int indexOf = resultURL.indexOf("&quot;,");
    try
    {
      resultURL = resultURL.substring(0, indexOf);
      button.setImage(ImageIO.read(new URL(resultURL)), true);
    } catch (IOException e)
    {
      button.setImage(ImageIO.read(new File("unknown.png")), true);
    }

    return null;
  }

  @Override
  protected void done()
  {
    super.done();

  }

}
