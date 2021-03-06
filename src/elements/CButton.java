package elements;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.TreeSet;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import misc.Utils;

public class CButton extends JButton implements Comparable<CButton>, Externalizable
{

  private static final long serialVersionUID = 1L;

  private TreeSet<MediaElement> medias;
  private String name;
  private final String[] imdbInfos;

  public CButton()
  {
    super();
    medias = new TreeSet<>();
    imdbInfos = new String[7];
    this.name = "Loading...";
  }

  public CButton(String name)
  {
    super();
    medias = new TreeSet<>();
    this.name = Utils.formatName(name);
    imdbInfos = new String[7];

    initialize();
  }

  private void initialize()
  {
    setForeground(Color.white);
    setOpaque(false);
    setContentAreaFilled(false);
    setBorderPainted(false);

    if (name != null)
    {
      this.setText(name);
      this.setToolTipText(name);
    }

    setVerticalTextPosition(SwingConstants.BOTTOM);
    setHorizontalTextPosition(SwingConstants.CENTER);
    setPreferredSize(Utils.ICON_DIMENSION);
    setMaximumSize(Utils.ICON_DIMENSION);
    setMinimumSize(Utils.ICON_DIMENSION);

    addMouseListener(new FileListListener(this));

    if (this.getIcon() == null)
    {
      setIcon(Utils.getUnknownIcon());
      if (medias.size() > 0)
        downloadIcon();
    }
  }

  public void downloadIcon()
  {
    new IconDownloader(this).execute();
  }

  public void setImage(BufferedImage newImage)
  {
    if (newImage != null)
      this.setIcon(new ImageIcon(newImage));
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
      return this.name.compareTo(o.getText());
    else
      return compareTo;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException
  {
    out.writeObject(this.getText());
    if (this.getIcon() == Utils.getUnknownIcon())
      out.writeObject(null);
    else
      out.writeObject(this.getIcon());
    out.writeObject(medias);
    out.writeObject(getImdbInfos());
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
  {
    name = (String) in.readObject();
    this.setIcon((Icon) in.readObject());
    medias = (TreeSet<MediaElement>) in.readObject();
    for (MediaElement mediaElement : medias)
      mediaElement.setParent(this);
    String[] infos = (String[]) in.readObject();
    for (int i = 0; i < imdbInfos.length; i++)
      imdbInfos[i] = infos[i];

    initialize();
  }

  public void addMedia(String name, String path)
  {
    getMedias().add(new MediaElement(name, path, getName(), false, this));
  }

  /**
   * @return the medias
   */
  public TreeSet<MediaElement> getMedias()
  {
    return medias;
  }

  /**
   * @return the imdbInfos
   */
  public String[] getImdbInfos()
  {
    return imdbInfos;
  }

}
