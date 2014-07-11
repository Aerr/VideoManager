/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package elements;

public class MediaElement
{

  private String name;
  private String path;
  private String parentFolder;
  private String tag;
  private boolean seen;

  public MediaElement(String name, String path, String parentFolder, String tag, boolean seen)
  {
    this.name = name;
    this.path = path;
    this.parentFolder = parentFolder;
    this.tag = tag;
    this.seen = seen;
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

}
