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

public class MediaElement implements Comparable<MediaElement>, Externalizable
{
  private static final long serialVersionUID = 1L;

  private String name;
  private String path;
  private String tag;
  private boolean seen;
  private boolean visible = true;
  private CButton parent;

  public MediaElement()
  {
  }

  public MediaElement(String name, String path, String tag, boolean seen, CButton parent)
  {
    this.name = name;
    this.path = path;
    this.tag = tag;
    this.seen = seen;
    this.parent = parent;
  }

  public MediaElement(String name, String path, boolean seen, CButton parent)
  {
    this(name, path, "", seen, parent);
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

  public void toggleSeen()
  {
    seen = !seen;
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
   * @param name the name to set
   */
  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public void writeExternal(ObjectOutput out) throws IOException
  {
    out.writeObject(name);
    out.writeObject(path);
    out.writeObject(tag);
    out.writeBoolean(seen);
    out.writeBoolean(visible);
  }

  @Override
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
  {
    this.name = (String) in.readObject();
    this.path = (String) in.readObject();
    this.tag = (String) in.readObject();
    this.seen = in.readBoolean();
    this.visible = in.readBoolean();
  }

  /**
   * @return the visible
   */
  public boolean getVisible()
  {
    return visible;
  }

  void setVisible(boolean b)
  {
    visible = b;
  }

  @Override
  public int compareTo(MediaElement o)
  {
    final int compareTo = this.name.compareTo(o.getName());

    if (this != o && compareTo == 0)
      return this.name.compareTo(o.getName());
    else
      return compareTo;
  }

  @Override
  public String toString()
  {
    return name;
  }

  /**
   * @return the parent
   */
  public CButton getParent()
  {
    return parent;
  }

  /**
   * @param parent the parent to set
   */
  public void setParent(CButton parent)
  {
    this.parent = parent;
  }
}
