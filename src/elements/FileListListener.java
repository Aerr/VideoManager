package elements;

import database.DatabaseManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    {
      if (e.getClickCount() == 1)
        DatabaseManager.playFile(item);
    }
    else
    {
      ContextMenu menu = new ContextMenu(item);
      menu.show(e.getComponent(), e.getX(), e.getY());
    }
  }
}
