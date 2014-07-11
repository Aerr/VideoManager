package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import misc.Utils;

class MainComponentListener implements ComponentListener
{

  private final GridLayout gridLayout;
  private Container parent;

  MainComponentListener(Container parent, GridLayout gridLayout)
  {
    this.gridLayout = gridLayout;
    this.parent = parent;
  }

  @Override
  public void componentShown(ComponentEvent e)
  {
  }

  @Override
  public void componentResized(ComponentEvent e)
  {
    final int width = parent.getSize().width;
    if (width < 463)
      gridLayout.setColumns(1);
    else
    {
      int newColumns = (width) / (Utils.ICON_DIMENSION.width + 25);
      newColumns = (width - Utils.GUI_HGAP * (newColumns - 1) * 2) / (Utils.ICON_DIMENSION.width + 25);
      gridLayout.setColumns(newColumns);
    }
  }

  @Override
  public void componentMoved(ComponentEvent e)
  {
  }

  @Override
  public void componentHidden(ComponentEvent e)
  {
  }
}
