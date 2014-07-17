package elements;

import database.DatabaseSaver;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class FileListListener extends MouseAdapter
{

  private final CButton item;
  private DatabaseSaver saver;

  public FileListListener(CButton cButton)
  {
    super();
    this.item = cButton;
  }

  @Override
  public void mouseClicked(MouseEvent e)
  {
    super.mouseClicked(e);
    boolean wasSeen = item.getSeen();
    if (e.getButton() == MouseEvent.BUTTON1)
    {
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
      }
    }
    else
    {
      PopUpDemo menu = new PopUpDemo();
      menu.show(e.getComponent(), e.getX(), e.getY());
    }
    if (wasSeen)
      return;

    if (saver != null && !saver.isDone())
      saver.cancel(true);
    saver = new DatabaseSaver();
    saver.execute();
  }
}
