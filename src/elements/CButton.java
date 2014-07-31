package elements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import misc.Utils;

public class CButton extends JButton implements Comparable<CButton>, Externalizable
{

  private static final long serialVersionUID = 1L;
  MediaElement element;

  public CButton()
  {
    super();
  }

  public CButton(MediaElement element)
  {
    super();
    this.element = element;

    initialize();
  }

  private void initialize()
  {
    setForeground(Color.white);
    setBackground(null);
    setBorder(null);

    if (element != null)
    {
      this.setText(element.getName());
      this.setToolTipText(this.element.getName());

      setSeen();
      this.setIcon(element.getIcon());

    }
    if (this.getIcon() == null)
      new IconDownloader(this).execute();

    setVerticalTextPosition(SwingConstants.BOTTOM);
    setHorizontalTextPosition(SwingConstants.CENTER);
    setPreferredSize(Utils.ICON_DIMENSION);
    setMaximumSize(Utils.ICON_DIMENSION);
    setMinimumSize(Utils.ICON_DIMENSION);

    addMouseListener(new FileListListener(this));

  }

  public void setImage(BufferedImage newImage, boolean isNew)
  {
    if (newImage != null)
    {
      if (isNew)
      {
        Dimension dim = getRatio(newImage.getWidth(), newImage.getHeight());

        BufferedImage image = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(newImage, 0, 0, dim.width, dim.height, null);
        g2.dispose();

//        saveFile("thumbs/" + getText().hashCode(), image);
        element.setIcon(new ImageIcon(image));
      }
      else
        element.setIcon(new ImageIcon(newImage));

      this.setIcon(element.getIcon());

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

  public String getMediaName()
  {
    return element.getName();
  }

  public void setMediaName(String name)
  {
    element.setName(name);
    this.setText(element.getName());
    this.setToolTipText(this.element.getName());
  }

  public void setSeen()
  {
    this.setSeen(element.getSeen());
  }

  public void setSeen(boolean isSeen)
  {
    element.setSeen(isSeen);

    if (isSeen)
      this.setForeground(Color.LIGHT_GRAY.darker());
    else
      this.setForeground(Color.LIGHT_GRAY);
  }

  public boolean getSeen()
  {
    return element.getSeen();
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

  @Override
  public void writeExternal(ObjectOutput out) throws IOException
  {
    out.writeObject(this.element);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
  {
    this.element = (MediaElement) in.readObject();
    initialize();
  }
}
