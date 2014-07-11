package listing;

import elements.ButtonHolder;
import elements.CButton;
import elements.MediaElement;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import misc.Tuple;

public class FileWalker
{

  private static final HashMap<String, ArrayList<CButton>> setButtons = new HashMap();

  public static ArrayList<CButton> getFiles(String path, JPanel jPanel, DefaultMutableTreeNode treeRoot)
  {
    setButtons.put("root", new ArrayList<CButton>());
    new File("thumbs").mkdirs();
    walk(path, jPanel, treeRoot, null);
    return setButtons.get("root");
  }

  private static void walk(String path, JPanel jPanel, DefaultMutableTreeNode treeRoot, ArrayList<CButton> buttonsList)
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
      String file = fileToString;

      file = getSuffix(file, fileSeparator);
      file = getPrefix(file, "[", "1080p", "720p", "x264", "HDTV", "FASTSUB", "VOSTFR", "MULTI",
                       "FINAL", "REPACK", "FRENCH", "COMPLETE");
      file = file.replace('.', ' ').trim();

      if (f.isDirectory())
      {
        final DefaultMutableTreeNode node = new DefaultMutableTreeNode(new Tuple(file, f.getAbsolutePath()));
        treeRoot.add(node);
        setButtons.put(file, new ArrayList<CButton>());
        walk(f.getAbsolutePath(), jPanel, node, setButtons.get("file"));
      }
      else if (fileToString.endsWith(".mkv") || fileToString.endsWith(".mp4"))
      {
        final CButton cButton = new CButton(new MediaElement(file, fileToString,
                                                             (String) rootToTuple(treeRoot).y, false));

        jPanel.add(new ButtonHolder(cButton));
        if (buttonsList != null)
          buttonsList.add(cButton);
        setButtons.get("root").add(cButton);
      }
    }
  }

  private static Tuple rootToTuple(DefaultMutableTreeNode treeRoot)
  {
    return (Tuple) treeRoot.getUserObject();
  }

  public static String getPrefix(String src, String... delimiter)
  {
    int i = findIndex(src, delimiter);

    if (i == -1)
      return src;
    else
      return src.substring(0, i);
  }

  private static String getSuffix(String src, String... delimiter)
  {
    int i = findIndex(src, delimiter);

    if (i == -1)
      return src;
    else
      return src.substring(i + 1);
  }

  private static int findIndex(String src, String... delimiter)
  {
    int i = -1;
    for (String s : delimiter)
    {
      int lastIndexOf = src.toUpperCase().lastIndexOf(s.toUpperCase());
      if ((lastIndexOf > 0) && (lastIndexOf < (src.length() - 1)))
        i = (i == -1) ? lastIndexOf : Math.min(i, lastIndexOf);
    }
    return i;
  }

  /**
   * @return the setButtons
   */
  public static HashMap<String, ArrayList<CButton>> getSetButtons()
  {
    return setButtons;
  }
}
