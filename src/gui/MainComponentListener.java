package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

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
        gridLayout.setColumns(parent.getSize().width / 150);
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