package gui;

import database.DatabaseManager;
import database.DatabaseSaver;
import database.FilePlayer;
import database.LocationsManager;
import database.LocationsManager.Location;
import elements.ListManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import listing.FileWalker;
import listing.Prefs;
import misc.CustomUIManager;
import misc.Utils;

public final class Gui extends JFrame
{

  private static final long serialVersionUID = 1L;
  private final JPanel jPanel;
  private final JButton stopButton;
  private JTextField searchBar;
  private final JComboBox jComboBox;

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

  /**
   * @return the jComboBox
   */
  public JComboBox getjComboBox()
  {
    return jComboBox;
  }

// <editor-fold defaultstate="collapsed" desc="Singleton">
  private static class GuiHolder
  {

    private final static Gui instance = new Gui();
  }

  public static Gui getInstance()
  {
    return GuiHolder.instance;
  }
// </editor-fold>

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

    searchBar = ListManager.searchBarInit(jPanel);
    jMenuBar.add(searchBar);
    JButton refresh = refreshButtonInit();

    final JFileChooser fileChooser = new JFileChooser();
    fileChooser.setApproveButtonText("Select");
    fileChooser.setApproveButtonMnemonic('s');
    fileChooser.setCurrentDirectory(new java.io.File("."));
    fileChooser.setDialogTitle("Add a new location");
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setAcceptAllFileFilterUsed(false);

    final LocationsManager.Location[] locations = LocationsManager.getLocations();
    final String add_new_location = "Add new location...";

    jComboBox = new JComboBox();
    jComboBox.addItem("");
    if (locations != null)
      for (LocationsManager.Location location : locations)
        jComboBox.addItem(location);

    jComboBox.addItem(add_new_location);

    jComboBox.addItemListener(new ItemListener()
    {
      @Override
      public void itemStateChanged(ItemEvent e)
      {
        if (e.getItem() == add_new_location && e.getStateChange() == ItemEvent.SELECTED)
        {
          int returnVal = fileChooser.showOpenDialog(Gui.this);
          if (returnVal == JFileChooser.APPROVE_OPTION)
          {
            final int itemCount = jComboBox.getItemCount();
            final File selectedFile = fileChooser.getSelectedFile();

            for (int i = 0; i < itemCount; i++)
            {
              final Object itemAt = jComboBox.getItemAt(i);
              if (itemAt.getClass() == Location.class)
              {
                Location it = (Location) itemAt;
                if (it.getPath().equals(selectedFile.getAbsolutePath()))
                {
                  jComboBox.setSelectedItem(it);
                  return;
                }
              }
            }

            final LocationsManager.Location location = new Location(selectedFile);
            LocationsManager.addLocation(location);
            jComboBox.insertItemAt(location, itemCount - 1);
            if (itemCount == 2 && jComboBox.getItemAt(0).toString().isEmpty())
              jComboBox.removeItemAt(0);
            jComboBox.setSelectedItem(location);
            initDatabase(location);
          }
        }
        else if (e.getStateChange() == ItemEvent.SELECTED)
          initDatabase((Location) e.getItem());
      }
    });

    jMenuBar.add(jComboBox);
    if (locations != null)
      jComboBox.removeItemAt(0);

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

    pack();
    setVisible(true);
  }

  private void initDatabase(Location location)
  {
    boolean load_database = DatabaseManager.load_database(location.getPath());
    if (!load_database)
      Prefs.getInstance().clear();
    FileWalker.getInstance().removeUnexistingEntries();
    FileWalker.getInstance().getFiles(location.getPath());
    ListManager.searchBarUpdate();
    new DatabaseSaver().execute();
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
        initDatabase((Location) getjComboBox().getSelectedItem());
      }
    });
    return refresh;
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
}
