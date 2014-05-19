package listing;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import elements.ButtonHolder;

public class FileWalker
{
    public static ArrayList<ButtonHolder> getFiles(String path, JPanel jPanel)
    {
        ArrayList<ButtonHolder> holdersList = new ArrayList<>();
        walk(path, jPanel, holdersList);
        return holdersList;
    }

    private static void walk(String path, JPanel jPanel, ArrayList<ButtonHolder> holdersList)
    {
        File rootFolder = new File(path);
        File[] list = rootFolder.listFiles();

        if (list == null)
            return;

        for (File f : list)
        {
            String file = f.getAbsoluteFile().toString();

            System.out.println(file);

            file = getSuffix(file, "\\");
            file = getPrefix(file, "[", "1080p", "720p", "x264", "HDTV", "FASTSUB", "VOSTFR", "MULTI",
                    "FINAL", "REPACK", "FRENCH");
            file = file.replace('.', ' ').trim();

            if (f.isDirectory())
                jPanel.add(new ButtonHolder(file, f.getAbsolutePath().toString(), true));
            else if (f.getAbsoluteFile().toString().endsWith(".mkv")
                    || f.getAbsoluteFile().toString().endsWith(".mp4"))
            {
                ButtonHolder buttonHolder = new ButtonHolder(file, f.getAbsolutePath().toString(), false);
                jPanel.add(buttonHolder);
                holdersList.add(buttonHolder);
            }
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
}