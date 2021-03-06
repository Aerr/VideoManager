/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import elements.MediaElement;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author aerr
 */
public class Utils
{

  public static final Dimension ICON_DIMENSION = new Dimension(150, 240);
  public static final int GUI_INSET = 25;
  public static final int GUI_VGAP = 40;
  public static final int GUI_HGAP = 25;
  public static final String[] DUMP_KEYWORDS =
  {
    "[", "1080p", "720p", "x264", "HDTV", "FASTSUB", "VOSTFR", "MULTI",
    "FINAL", "REPACK", "FRENCH", "COMPLETE"
  };
  public static final String[] EXTENSIONS =
  {
    ".avi", ".mkv", ".mp4"
  };
  private static MediaElement currentContextMenu;
  private static ImageIcon unknownIcon;

  static
  {
    try
    {
      unknownIcon = new ImageIcon(ImageIO.read(new File("resources/unknown.jpg")));
    } catch (IOException ex)
    {
      Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static String getPrefix(String src, String... delimiter)
  {
    int i = findIndex(src, delimiter);

    if (i == -1)
      return src;
    else
      return src.substring(0, i);
  }

  public static int findIndex(String src, String... delimiter)
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

  public static String formatName(String toFormat)
  {
    toFormat = toFormat.substring(0, 1).toUpperCase() + toFormat.substring(1);
    toFormat = toFormat.replaceAll("[s]([0-9])", "S$1");
    toFormat = toFormat.replaceAll("[e]([0-9])", "E$1");
    toFormat = toFormat.replaceAll("_", " ");

    return toFormat;
  }

  /**
   * @return the currentContextMenu
   */
  public static MediaElement getCurrentContextMenu()
  {
    return currentContextMenu;
  }

  public static boolean isContextMenuDisplayed()
  {
    return currentContextMenu != null;
  }

  public static boolean isContextMenuDisplayed(MediaElement media)
  {
    if (media == null)
      return false;
    return currentContextMenu != null && currentContextMenu == media;
  }

  public static void setCurrentContextMenu(MediaElement media)
  {
    currentContextMenu = media;
  }

  /**
   * @return the unknownIcon
   */
  public static ImageIcon getUnknownIcon()
  {
    return unknownIcon;
  }
}
