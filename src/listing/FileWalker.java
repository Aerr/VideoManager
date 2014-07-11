/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package listing;

import elements.ButtonHolder;
import elements.CButton;
import elements.MediaElement;
import java.io.File;
import java.util.HashMap;
import java.util.TreeSet;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import misc.Tuple;
import misc.Utils;
import static misc.Utils.getPrefix;

/**
 *
 * @author aerr
 */
public class FileWalker
{

  private final HashMap<String, TreeSet<CButton>> setButtons;

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

  private static class FileWalkerHolder
  {

    private static final FileWalker INSTANCE = new FileWalker();
  }

  public TreeSet<CButton> getFiles(String path, JPanel jPanel, DefaultMutableTreeNode treeRoot)
  {
    setButtons.put("All", new TreeSet<CButton>());
    new File("thumbs").mkdirs();
    walk(path, jPanel, treeRoot, null);
    return setButtons.get("All");
  }

  private void walk(String path, JPanel jPanel, DefaultMutableTreeNode treeRoot, TreeSet<CButton> buttonsList)
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
        setButtons.put(file, new TreeSet<CButton>());
        walk(f.getAbsolutePath(), jPanel, node, setButtons.get(file));
      }
      else if ((file = isVideo(fileToString, file, Utils.EXTENSIONS)) != null)
      {
        final CButton cButton = new CButton(
                new MediaElement(file.trim(), fileToString,
                                 getCleanName((String) rootToTuple(treeRoot).y, fileSeparator),
                                 false));

        jPanel.add(new ButtonHolder(cButton));
        if (buttonsList != null)
          buttonsList.add(cButton);
        setButtons.get("All").add(cButton);
      }
    }
  }

  private String getCleanName(final String fileToString, String fileSeparator)
  {
    if (fileToString == null)
      return null;
    String file = getSuffix(fileToString, fileSeparator);
    file = getPrefix(file, Utils.DUMP_KEYWORDS);
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
