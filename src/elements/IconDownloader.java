package elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.SwingWorker;
import listing.FileWalker;

public class IconDownloader extends SwingWorker<Void, Void>
{

  private CButton button;
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
      button.setImage(ImageIO.read(f));
      return null;
    }

    InputStream is = null;
    StringBuilder res = new StringBuilder();
    try
    {
      String name = FileWalker.getPrefix(button.getText(), "E0", "E1", "E2");
      URL url = new URL("https://www.bing.com/images/search?q=" + name.replace(' ', '+')
                        + "+poster");

      is = url.openStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));

      String line = null;
      while ((line = br.readLine()) != null)
        res.append(line);

    } catch (IOException e)
    {
    } finally
    {
      try
      {
        if (is != null)
          is.close();
      } catch (IOException e)
      {
      }
    }

    resultURL = res.toString();
    resultURL = resultURL.substring(resultURL.indexOf("imgurl:&quot;") + "imgurl:&quot;".length());
    resultURL = resultURL.substring(0, resultURL.indexOf("&quot;,"));

    button.setImage(ImageIO.read(new URL(resultURL)));

    return null;
  }

  @Override
  protected void done()
  {
    super.done();

  }

}
