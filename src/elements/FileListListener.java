package elements;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class FileListListener extends MouseAdapter
{

  private final CButton item;

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
      if (e.getClickCount() == 2)
      {
        String name = item.getPath();

        try
        {
          if (System.getProperty("os.name").equals("Linux"))
            Runtime.getRuntime().exec(new String[]
            {
              "bash", "-c", "vlc \"" + item.getPath() + "\""
            });
          else
            Desktop.getDesktop().open(new File(name));
        } catch (IOException exception)
        {
          System.err.println("Could not open: " + name + System.lineSeparator());
        }

        item.setSeen();
        item.setForeground(Color.LIGHT_GRAY.darker());
      }
  }
}
