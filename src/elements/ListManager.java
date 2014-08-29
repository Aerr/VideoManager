/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elements;

import gui.Gui;
import java.awt.Component;
import java.awt.Dimension;
import java.util.TreeSet;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import listing.FileWalker;
import listing.Prefs;

/**
 *
 * @author aerr
 */
public class ListManager
{

  private static Gui gui = Gui.getInstance();
  private static JPanel jPanel;
  private static final JTextField searchBar = new JTextField();
  private static Component[] defaultList;

  public static void searchBarUpdate()
  {
    reloadList();
  }

  public static JTextField searchBarInit(final JPanel jPanelParent)
  {
    jPanel = jPanelParent;
    searchBar.getDocument().addDocumentListener(new DocumentListener()
    {

      @Override
      public void insertUpdate(DocumentEvent e)
      {
        ListManager.reloadList();
      }

      @Override
      public void removeUpdate(DocumentEvent e)
      {
        ListManager.reloadList();
      }

      @Override
      public void changedUpdate(DocumentEvent e)
      {
      }
    });

    return searchBar;
  }

  private static void reloadList()
  {
    reloadList(false);
  }

  public static void reloadList(boolean outside)
  {
    if (outside || defaultList == null)
      repopulateList();
    else
    {
      jPanel.removeAll();
      for (Component component : defaultList)
        jPanel.add(component);
    }

    boolean removed = false;
    for (Component component : jPanel.getComponents())
      if (component.getClass() == ButtonHolder.class)
      {
        ButtonHolder holder = (ButtonHolder) component;
        holder.filter("");
        if (holder.toString().toLowerCase().contains(searchBar.getText().toLowerCase()))
          continue;
        holder.filter(searchBar.getText());
        removed = holder.getRowCount() <= 0;
        if (removed)
          jPanel.remove(component);
      }
      else if (removed)
      {
        jPanel.remove(component);
        removed = false;
      }

    Prefs.getInstance().getPrefs().put("Last-Prefs-SearchBar", searchBar.getText());
    repaint();
  }

  private static void repopulateList()
  {
    final TreeSet<CButton> files = FileWalker.getInstance().getSetButtons();
    jPanel.removeAll();
    for (CButton cButton : files)
      if (cButton.isVisible())
      {
        jPanel.add(new ButtonHolder(cButton));
        jPanel.add(Box.createRigidArea(new Dimension(1, 50)));
      }
    defaultList = jPanel.getComponents();
  }

  private static void repaint()
  {
    if (getGui() != null)
    {
      gui.revalidate();
      gui.repaint();
    }
  }

  /**
   * @return the gui
   */
  public static Gui getGui()
  {
    if (gui == null)
      gui = Gui.getInstance();
    return gui;
  }
}
