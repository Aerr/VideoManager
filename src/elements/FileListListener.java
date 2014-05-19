package elements;

import gui.Gui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import listing.Prefs;

public class FileListListener extends MouseAdapter
{
    private CButton item;

    public FileListListener(CButton cButton)
    {
        super();
        this.item = cButton;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        super.mouseClicked(e);
        if (e.getButton() == MouseEvent.BUTTON1)
        {
            String name = item.getPath();
            if (item.isFolder())
                Gui.getInstance().populateList(name);
            else
            {
                try
                {
                    Desktop.getDesktop().open(new File(name));
                    Prefs.getInstance().getPrefs().putBoolean(Integer.toString(name.hashCode()), true);
                    item.setForeground(Color.LIGHT_GRAY);
                } catch (IOException exception)
                {
                    System.err.println("Could not open: " + name + System.lineSeparator());
                }
            }
        }
    }
}
