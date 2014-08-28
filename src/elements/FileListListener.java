package elements;

import gui.EditAlbum;
import database.FilePlayer;
import gui.Gui;
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
      if (e.getClickCount() == 2)
        new FilePlayer(item.getMedias().toArray(new MediaElement[item.getMedias().size()])).execute();
    }
    else if (e.getButton() == MouseEvent.BUTTON3)
    {
      EditAlbum dialog = new EditAlbum(Gui.getInstance(), item);
      dialog.setVisible(true);
    }
  }
}
