package elements;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.SwingWorker;
import misc.Utils;

public class IconDownloader extends SwingWorker<Void, Void>
{

  private final CButton button;
  private final JButton sideIcon;
  private final String customSearch;
  private final URL directURL;

  static final String start = "\"url\":\"";
  static final String end = "\",\"visibleUrl\":";

  public IconDownloader(CButton button)
  {
    super();
    this.button = button;
    this.sideIcon = null;
    this.customSearch = null;
    this.directURL = null;
  }

  public IconDownloader(CButton button, JButton sideIcon)
  {
    super();
    this.button = button;
    this.sideIcon = sideIcon;
    this.customSearch = null;
    this.directURL = null;
  }

  public IconDownloader(CButton button, JButton sideIcon, String search)
  {
    super();
    this.button = button;
    this.sideIcon = sideIcon;
    this.customSearch = search;
    this.directURL = null;
  }

  public IconDownloader(CButton button, JButton sideIcon, URL directURL)
  {
    super();
    this.button = button;
    this.sideIcon = sideIcon;
    this.customSearch = null;
    this.directURL = directURL;
  }

  @Override
  protected Void doInBackground()
  {
    BufferedImage newImage;
    String[] split = null;
    if (directURL != null)
      return directDownload();

    try
    {

      String search = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
      if (customSearch != null)
        search += customSearch.replace(" ", "+");
      else
        search += button.getText().replace(" ", "+");

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

    } catch (IOException ex)
    {
      Logger.getLogger(IconDownloader.class.getName()).log(Level.SEVERE, null, ex);
    }

    for (String string : split)
    {
      try
      {
        newImage = ImageIO.read(new URL(string.substring(0, string.indexOf(end))));
      } catch (IOException e)
      {
        continue;
      }

      BufferedImage image = transform(newImage);


      button.setImage(image);

      return null;
    }

    button.setIcon(Utils.getUnknownIcon());

    return null;
  }

  private BufferedImage transform(BufferedImage newImage)
  {
    Dimension dim = getRatio(newImage.getWidth(), newImage.getHeight());
    BufferedImage image = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = image.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.drawImage(newImage, 0, 0, dim.width, dim.height, null);
    g2.dispose();

    return image;
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
    if (sideIcon != null)
      sideIcon.setIcon(button.getIcon());
  }

  private Void directDownload()
  {
    BufferedImage newImage;
    try
    {
      newImage = ImageIO.read(directURL);
    } catch (IOException e)
    {
      return null;
    }

    BufferedImage image = transform(newImage);

    button.setImage(image);

    return null;
  }

}
