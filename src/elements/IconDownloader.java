package elements;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.SwingWorker;
import misc.Utils;

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
    InputStream is = null;
    StringBuilder res = new StringBuilder();

    try
    {
      String search = button.getText() + "+poster";
      URL url = new URL("https://www.bing.com/images/search?q="
                        + search.replace(" ", "+")
                        + "&qft=+filterui:aspect-tall+filterui:imagesize-medium");

      is = url.openStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));

      String line = null;
      while ((line = br.readLine()) != null)
        res.append(line);

    } catch (IOException e)
    {
      Logger.getLogger(IconDownloader.class.getName()).log(Level.SEVERE, null, e);
      System.out.println(button.getText() + "+poster");
    } finally
    {
      try
      {
        if (is != null)
          is.close();
      } catch (IOException e)
      {
        Logger.getLogger(IconDownloader.class.getName()).log(Level.SEVERE, null, e);
        System.out.println(button.getText() + "+poster");
      }
    }

    resultURL = res.toString();
    resultURL = resultURL.substring(resultURL.indexOf("imgurl:&quot;") + "imgurl:&quot;".length());
    final int indexOf = resultURL.indexOf("&quot;,");
    BufferedImage newImage;
    if (indexOf >= 0)
      try
      {
        resultURL = resultURL.substring(0, indexOf);
        final URL url = new URL(resultURL);
        newImage = ImageIO.read(url);
      } catch (IOException e)
      {
        Logger.getLogger(IconDownloader.class.getName()).log(Level.SEVERE, null, e);
        button.setImage(ImageIO.read(new File("resources/unknown.jpg")));
        return null;
      }
    else
    {
      button.setImage(ImageIO.read(new File("resources/unknown.jpg")));
      return null;
    }

    Dimension dim = getRatio(newImage.getWidth(), newImage.getHeight());

    BufferedImage image = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = image.createGraphics();

    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2.drawImage(newImage, 0, 0, dim.width, dim.height, null);
    g2.dispose();

    button.setImage(image);

    return null;
  }

  private Dimension getRatio(int imageWidth, int imageHeight)
  {
    float ratio = (float) imageHeight / (float) imageWidth;
    int height = Utils.ICON_DIMENSION.height - 25;
    int width = (int) (height / ratio);

    return new Dimension(width, height);
  }

  @Override
  protected void done()
  {
    super.done();
  }

}
