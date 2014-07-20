/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing;

import elements.CButton;
import elements.MediaElement;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

  private HashMap<String, TreeSet<CButton>> setButtons;

  private FileWalker()
  {
    setButtons = new HashMap();
  }

  public static FileWalker getInstance()
  {
    return FileWalkerHolder.INSTANCE;
  }

  /**
   * @return the setButtons
   */
  public HashMap<String, TreeSet<CButton>> getSetButtons()
  {
    return setButtons;
  }

  /**
   * @param setButtons the setButtons to set
   */
  public void setSetButtons(HashMap<String, TreeSet<CButton>> setButtons)
  {
    this.setButtons = setButtons;
  }

  private static class FileWalkerHolder
  {

    private static final FileWalker INSTANCE = new FileWalker();
  }

  public TreeSet<CButton> getFiles(String path, DefaultMutableTreeNode treeRoot)
  {
    putNew("All");
    new File("thumbs").mkdirs();
    walk(path, treeRoot, null);
    return setButtons.get("All");
  }

  private void putNew(final String newKey)
  {
    if (setButtons.get(newKey) == null)
      setButtons.put(newKey, new TreeSet<CButton>());
  }

  protected void walk(String path, DefaultMutableTreeNode treeRoot, TreeSet<CButton> buttonsList)
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
      {
        final DefaultMutableTreeNode node = new DefaultMutableTreeNode(new Tuple(file, f.getAbsolutePath()));
        treeRoot.add(node);
        putNew(file);
        walk(f.getAbsolutePath(), node, setButtons.get(file));
      }
      else if ((file = isVideo(fileToString, file, Utils.EXTENSIONS)) != null)
        if (!Prefs.getInstance().getPrefs().getBoolean(getStringHashcode(fileToString), false))
        {
          final CButton cButton = new CButton(
                  new MediaElement(file.trim(), fileToString,
                                   getCleanName((String) rootToTuple(treeRoot).y, fileSeparator),
                                   false));

          if (buttonsList != null)
            buttonsList.add(cButton);
          setButtons.get("All").add(cButton);
          Prefs.getInstance().getPrefs().putBoolean(getStringHashcode(fileToString), true);
        }
    }
  }

  public void removeUnexistingEntries()
  {
    for (Map.Entry<String, TreeSet<CButton>> entry : setButtons.entrySet())
    {
      TreeSet<CButton> treeSet = entry.getValue();
      for (Iterator<CButton> it = treeSet.iterator(); it.hasNext();)
      {
        CButton cButton = it.next();
        if (!new File(cButton.getPath()).exists())
        {
          Prefs.getInstance().getPrefs().remove(getStringHashcode(cButton.getPath()));
          it.remove();
        }
      }
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
