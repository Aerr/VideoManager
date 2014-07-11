/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.awt.Dimension;

/**
 *
 * @author aerr
 */
public class Utils
{

  public static final Dimension ICON_DIMENSION = new Dimension(125, 200);
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
}
