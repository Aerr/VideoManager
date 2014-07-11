package elements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import misc.Utils;

public class CButton extends JButton implements Comparable<CButton>
{

  private static final long serialVersionUID = 1L;
  MediaElement element;

  public CButton(MediaElement element)
  {
    super(element.getName());
    this.element = element;

    setForeground(Color.white);
    setBackground(null);
    setBorder(null);

    setVerticalTextPosition(SwingConstants.BOTTOM);
    setHorizontalTextPosition(SwingConstants.CENTER);
    setPreferredSize(Utils.ICON_DIMENSION);
    setMaximumSize(Utils.ICON_DIMENSION);
    setMinimumSize(Utils.ICON_DIMENSION);
    setToolTipText(element.getName());

    new IconDownloader(this).execute();

    addMouseListener(new FileListListener(this));

    if (this.element.getSeen())
      this.setForeground(Color.LIGHT_GRAY.darker());
  }

  public void setImage(BufferedImage newImage)
  {
    if (newImage != null)
    {
      Dimension dim = getRatio(newImage.getWidth(), newImage.getHeight());

      BufferedImage image = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
      Graphics2D g2 = image.createGraphics();

      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                          RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      g2.drawImage(newImage, 0, 0, dim.width, dim.height, null);
      g2.dispose();

      saveFile("thumbs/" + getText().hashCode(), image);
      setIcon(new ImageIcon(image));

      revalidate();
      repaint();
    }
  }

  public void saveFile(final String filename, final BufferedImage image)
  {
    File file = new File(filename + ".jpg");
    try
    {
      ImageIO.write(image, "jpg", file);
    } catch (IOException e)
    {
    }
  }

  private Dimension getRatio(int imageWidth, int imageHeight)
  {
    float ratio = (float) imageHeight / (float) imageWidth;
    int height = getMaximumSize().height - 25;
    int width = (int) (height / ratio);

    return new Dimension(width, height);
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    if (element.getSeen())
    {
      g.setColor(new Color(255, 255, 255, 128));
      g.fillRect(0, 1, getWidth(), getHeight() - 22);
    }
  }

  public String getPath()
  {
    return this.element.getPath();
  }

  public String getParentFolder()
  {
    return this.element.getParentFolder();
  }

  @Override
  public String toString()
  {
    return getText();
  }

  @Override
  public int compareTo(CButton o)
  {
    final int compareTo = this.getText().compareTo(o.getText());

    if (this != o && compareTo == 0)
      return this.getPath().compareTo(o.getPath());
    else
      return compareTo;
  }
}
