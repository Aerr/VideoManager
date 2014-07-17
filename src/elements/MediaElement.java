/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.swing.ImageIcon;

public class MediaElement implements Externalizable
{
  private static final long serialVersionUID = 1L;

  private String name;
  private String path;
  private String parentFolder;
  private String tag;
  private boolean seen;
  private ImageIcon icon;

  public MediaElement()
  {
  }

  public MediaElement(String name, String path, String parentFolder, String tag, boolean seen)
  {
    this.name = name;
    this.path = path;
    this.parentFolder = parentFolder;
    this.tag = tag;
    this.seen = seen;
    this.icon = null;
  }

  public MediaElement(String name, String path, String parentFolder, boolean seen)
  {
    this(name, path, parentFolder, "", seen);
  }

  /**
   * @return the seen
   */
  public boolean getSeen()
  {
    return seen;
  }

  /**
   * @param seen the seen to set
   */
  public void setSeen(boolean seen)
  {
    this.seen = seen;
  }

  /**
   * @return the path
   */
  public String getPath()
  {
    return path;
  }

  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }

  /**
   * @return the parentFolder
   */
  public String getParentFolder()
  {
    return parentFolder;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException
  {
    out.writeObject(name);
    out.writeObject(path);
    out.writeObject(parentFolder);
    out.writeObject(tag);
    out.writeBoolean(seen);
    out.writeObject(icon);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
  {
    this.name = (String) in.readObject();
    this.path = (String) in.readObject();
    this.parentFolder = (String) in.readObject();
    this.tag = (String) in.readObject();
    this.seen = in.readBoolean();
    this.icon = (ImageIcon) in.readObject();
  }

  /**
   * @return the icon
   */
  public ImageIcon getIcon()
  {
    return icon;
  }

  /**
   * @param icon the icon to set
   */
  public void setIcon(ImageIcon icon)
  {
    this.icon = icon;
  }
}
