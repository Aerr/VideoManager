/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing;

import elements.CButton;
import elements.MediaElement;
import java.io.File;
import java.util.Iterator;
import java.util.TreeSet;
import javax.swing.tree.DefaultMutableTreeNode;
import misc.Tuple;
import misc.Utils;

/**
 *
 * @author aerr
 */
public class FileWalker
{

  private TreeSet<CButton> setButtons;

  private FileWalker()
  {
    setButtons = new TreeSet();
  }

  public static FileWalker getInstance()
  {
    return FileWalkerHolder.INSTANCE;
  }

  /**
   * @return the setButtons
   */
  public TreeSet<CButton> getSetButtons()
  {
    return setButtons;
  }

  /**
   * @param setButtons the setButtons to set
   */
  public void setSetButtons(TreeSet<CButton> setButtons)
  {
    this.setButtons = setButtons;
  }

  private static class FileWalkerHolder
  {

    private static final FileWalker INSTANCE = new FileWalker();
  }

  public TreeSet<CButton> getFiles(String path)
  {
    walk(path, null);
    return setButtons;
  }

  protected void walk(String path, CButton buttonsList)
  {
    File rootFolder = new File(path);
    File[] list = rootFolder.listFiles();

    if (list == null)
      return;

    String fileSeparator = "\\";
    if (System.getProperty("os.name").equals("Linux"))
      fileSeparator = "/";
    for (File f : list)
    {
      final String fileToString = f.getAbsoluteFile().toString();
      String file = getCleanName(fileToString, fileSeparator);

      if (f.isDirectory())
        if (buttonsList == null)
        {
          final CButton cButton = new CButton(file);
          walk(f.getAbsolutePath(), cButton);
          if (cButton.getMedias().size() > 0)
          {
            setButtons.add(cButton);
            cButton.downloadIcon();
          }
        }
        else
          walk(f.getAbsolutePath(), buttonsList);
      else if ((file = isVideo(fileToString, file, Utils.EXTENSIONS)) != null)
        if (!Prefs.getInstance().getPrefs().getBoolean(getStringHashcode(fileToString), false))
        {
          if (buttonsList == null)
          {
            final CButton cButton = new CButton(file);
            cButton.addMedia(file.trim(), fileToString);
            cButton.downloadIcon();
            setButtons.add(cButton);
          }
          else
            buttonsList.addMedia(file.trim(), fileToString);

          Prefs.getInstance().getPrefs().putBoolean(getStringHashcode(fileToString), true);
        }
    }
  }

  public void removeUnexistingEntries()
  {
    for (Iterator<CButton> it = setButtons.iterator(); it.hasNext();)
    {
      CButton cButton = it.next();
      for (Iterator<MediaElement> it1 = cButton.getMedias().iterator(); it1.hasNext();)
      {
        MediaElement mediaElement = it1.next();
        if (mediaElement.getVisible())
          if (!new File(mediaElement.getPath()).exists())
          {
            Prefs.getInstance().getPrefs().remove(getStringHashcode(mediaElement.getPath()));
            it1.remove();
          }
      }

      if (cButton.getMedias().isEmpty())
        it.remove();
    }
  }

  private static String getStringHashcode(final String fileToString)
  {
    return Integer.toString(fileToString.hashCode());
  }

  private String getCleanName(final String fileToString, String fileSeparator)
  {
    if (fileToString == null)
      return null;
    String file = getSuffix(fileToString, fileSeparator);
    file = Utils.getPrefix(file, Utils.DUMP_KEYWORDS);
    file = file.replace('.', ' ').trim();
    return file;
  }

  private Tuple rootToTuple(DefaultMutableTreeNode treeRoot)
  {
    return (Tuple) treeRoot.getUserObject();
  }

  private String getSuffix(String src, String... delimiter)
  {
    int i = Utils.findIndex(src, delimiter);

    if (i == -1)
      return src;
    else
      return src.substring(i + 1);
  }

  private String isVideo(String path, String filename, String... extensions)
  {
    for (String ext : extensions)
      if (path.endsWith(ext))
        return filename.replace(ext.substring(1), "");

    return null;
  }
}
