package gui;

import database.DatabaseManager;
import database.DatabaseSaver;
import database.FilePlayer;
import elements.ButtonHolder;
import elements.CButton;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import listing.FileWalker;
import listing.Prefs;
import misc.Utils;

public final class Gui extends JFrame
{

  private static final long serialVersionUID = 1L;
  private final JPanel jPanel;
  private final JButton stopButton;
  private JTextField searchBar;

  /**
   * @return the stopButton
   */
  public JButton getStopButton()
  {
    return stopButton;
  }

  /**
   * @return the jPanel
   */
  public JPanel getjPanel()
  {
    return jPanel;
  }

  private static class GuiHolder
  {

    private final static Gui instance = new Gui();
  }

  public static Gui getInstance()
  {
    return GuiHolder.instance;
  }

  private Gui()
  {
    setTitle("Video Manager");
    CustomUIManager.setUIFont();

    setMinimumSize(new Dimension(1024, 768));
    setPreferredSize(new Dimension(1024, 768));
    setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    shutdownHook();

    jPanel = new JPanel();
    final JScrollPane mediaGrid = mediaGridInit();

    JMenuBar jMenuBar = new JMenuBar();

    searchBarInit();
    jMenuBar.add(searchBar);
    JButton refresh = refreshButtonInit();

    jMenuBar.add(refresh);
    stopButton = new JButton("Stop");

    stopButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        FilePlayer.cancelCurrentPlayer();
      }
    });
    stopButton.setVisible(false);

    jMenuBar.add(stopButton);

    setJMenuBar(jMenuBar);

    add(mediaGrid);

    searchBar.setText(Prefs.getInstance().getPrefs().get("Last-Prefs-SearchBar", ""));
    initDatabase();

    pack();
    setVisible(true);
  }

  private void initDatabase()
  {
    boolean load_database = DatabaseManager.load_database();
    if (!load_database)
      Prefs.getInstance().clear();
    FileWalker.getInstance().removeUnexistingEntries();
    FileWalker.getInstance().getFiles("/home/aerr/Téléchargements/");
    reloadList();
    updateSearchBar();
    new DatabaseSaver().execute();
  }

  public void updateSearchBar()
  {
    searchBar.getCaretListeners()[0].caretUpdate(null);
  }

  private void shutdownHook()
  {
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        if (FilePlayer.getSaver() != null && !FilePlayer.getSaver().isDone())
          try
          {
            FilePlayer.getSaver().get();
          } catch (InterruptedException | ExecutionException ex)
          {
            Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
          }
      }
    }));
  }

  private JButton refreshButtonInit()
  {
    final JButton refresh = new JButton("Refresh");
    refresh.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        initDatabase();
      }
    });
    return refresh;
  }

  private void searchBarInit()
  {
    searchBar = new JTextField();
    searchBar.addCaretListener(new CaretListener()
    {

      @Override
      public void caretUpdate(CaretEvent arg0)
      {
        reloadList();
        boolean removed = false;
        for (Component component : Gui.this.getjPanel().getComponents())
          if (component.getClass() == ButtonHolder.class)
          {
            ButtonHolder holder = (ButtonHolder) component;
            if (holder.toString().toLowerCase().contains(searchBar.getText().toLowerCase()))
              continue;
            holder.filter(searchBar.getText());
            removed = holder.getRowCount() <= 0;
            if (removed)
              Gui.this.getjPanel().remove(component);
          }
          else if (removed)
          {
            Gui.this.getjPanel().remove(component);
            removed = false;
          }
        Prefs.getInstance().getPrefs().put("Last-Prefs-SearchBar", searchBar.getText());
      }
    }
    );
  }

  private JScrollPane mediaGridInit()
  {
    jPanel.setBackground(new Color(39, 39, 39));
    BoxLayout layout = new BoxLayout(jPanel, BoxLayout.PAGE_AXIS);
    jPanel.setLayout(layout);
    jPanel.setBorder(BorderFactory.createEmptyBorder(Utils.GUI_INSET, Utils.GUI_INSET / 2,
                                                     Utils.GUI_INSET, Utils.GUI_INSET / 2));

    final JScrollPane jScrollPane = new JScrollPane(jPanel);
    jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    jScrollPane.setBackground(null);

    return jScrollPane;
  }

  public void reloadList()
  {
    final TreeSet<CButton> files = FileWalker.getInstance().getSetButtons();
    populateList(files);
  }

  private void populateList(final TreeSet<CButton> files)
  {
    jPanel.removeAll();

    for (CButton cButton : files)
    {
      if (cButton.isVisible())
        jPanel.add(new ButtonHolder(cButton));
      jPanel.add(Box.createRigidArea(new Dimension(1, 50)));
    }

    revalidate();
    repaint();
  }
}
