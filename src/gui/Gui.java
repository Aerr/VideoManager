package gui;

import database.DatabaseManager;
import database.DatabaseSaver;
import database.FilePlayer;
import elements.ButtonHolder;
import elements.CButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import listing.FileWalker;
import listing.Prefs;
import misc.Utils;

public final class Gui extends JFrame
{

  private static final long serialVersionUID = 1L;
  private String currentFolder = "";
  private final JPanel jPanel;

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

    JSplitPane jSplitPane = splitPaneInit();

    jPanel = new JPanel();
    final JScrollPane mediaGrid = mediaGridInit();
    jSplitPane.setRightComponent(mediaGrid);

    JMenuBar jMenuBar = new JMenuBar();
    JTextField searchBar = searchBarInit();

    jMenuBar.add(searchBar);
    JButton refresh = refreshButtonInit(searchBar);

    jMenuBar.add(refresh);

    setJMenuBar(jMenuBar);

    JScrollPane jScrollPane1 = new javax.swing.JScrollPane();

    DatabaseManager.load_database();
    FileWalker.getInstance().getFiles("/home/aerr/Téléchargements/",
                                      TreeExplorer.getInstance().getExplorerRoot());

    jScrollPane1.setViewportView(TreeExplorer.getInstance());

    jSplitPane.setLeftComponent(jScrollPane1);

    add(jSplitPane);

    populateList("All");

    searchBar.setText(Prefs.getInstance().getPrefs().get("Last-Prefs-SearchBar", ""));
    searchBar.getCaretListeners()[0].caretUpdate(null);

    new DatabaseSaver().execute();
    pack();
    setVisible(true);
  }

  private JButton refreshButtonInit(final JTextField searchBar)
  {
    final JButton refresh = new JButton("Refresh");
    refresh.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        Prefs.getInstance().clear();
        FileWalker.getInstance().getFiles("/home/aerr/Téléchargements/",
                                          TreeExplorer.getInstance().getExplorerRoot());
        populateList("All");
        searchBar.getCaretListeners()[0].caretUpdate(null);

        new DatabaseSaver().execute();
      }
    });
    return refresh;
  }

  private JTextField searchBarInit()
  {
    final JTextField searchBar = new JTextField();
    searchBar.addCaretListener(new CaretListener()
    {

      @Override
      public void caretUpdate(CaretEvent arg0)
      {
        TreeSet<CButton> get = FileWalker.getInstance().getSetButtons().get(currentFolder);
        if (searchBar.getText().equals(""))
          populateList(get);

        get = new TreeSet(get);
        for (Iterator<CButton> it = get.iterator(); it.hasNext();)
        {
          CButton cButton = it.next();
          if (!cButton.toString().toLowerCase().contains(
                  searchBar.getText().toLowerCase()))
            it.remove();
        }

        populateList(get);

        Prefs.getInstance().getPrefs().put("Last-Prefs-SearchBar", searchBar.getText());
      }
    });
    return searchBar;
  }

  private JScrollPane mediaGridInit()
  {
    jPanel.setBackground(new Color(39, 39, 39));
    final GridLayout gridLayout = new GridLayout(0, 4);
    gridLayout.setVgap(Utils.GUI_VGAP);
    gridLayout.setHgap(Utils.GUI_HGAP);
    jPanel.setBorder(BorderFactory.createEmptyBorder(Utils.GUI_INSET, Utils.GUI_INSET / 2,
                                                     Utils.GUI_INSET, Utils.GUI_INSET / 2));
    jPanel.setLayout(gridLayout);
    addComponentListener(new MainComponentListener(this, gridLayout));

    final JScrollPane jScrollPane = new JScrollPane(jPanel);
    jScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    jScrollPane.setBackground(null);

    return jScrollPane;
  }

  private JSplitPane splitPaneInit()
  {
    JSplitPane jSplitPane = new javax.swing.JSplitPane();
    jSplitPane.setOneTouchExpandable(true);
    jSplitPane.setDividerLocation(110);
    jSplitPane.setDividerSize(9);
    jSplitPane.setUI(new BasicSplitPaneUI());
    return jSplitPane;
  }

  public void populateList(String folder)
  {
    if (currentFolder.equals(folder))
      return;

    final TreeSet<CButton> files = FileWalker.getInstance().getSetButtons().get(folder);
    if (files == null)
      return;

    populateList(files);
    currentFolder = folder;
  }

  private void populateList(final TreeSet<CButton> files)
  {
    jPanel.removeAll();

    for (CButton cButton : files)
      jPanel.add(new ButtonHolder(cButton));

    revalidate();

    final int columns = ((GridLayout) jPanel.getLayout()).getColumns();
    if (jPanel.getComponentCount() > 0)
      while (jPanel.getComponent(0).getSize().height > Utils.ICON_DIMENSION.height)
      {
        for (int i = 0; i < columns; i++)
          jPanel.add(new ButtonHolder());
        revalidate();
      }

    repaint();
  }
}
