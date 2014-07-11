package gui;

import elements.CButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
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

public final class Gui extends JFrame
{

  private static final long serialVersionUID = 1L;
  private JPanel jPanel;
  private ArrayList<CButton> holdersList;

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
    setMinimumSize(new Dimension(1024, 768));
    setPreferredSize(new Dimension(1024, 768));

    JSplitPane jSplitPane = new javax.swing.JSplitPane();
    jSplitPane.setOneTouchExpandable(true);
    jSplitPane.setDividerLocation(110);
    jSplitPane.setDividerSize(9);
    jSplitPane.setUI(new BasicSplitPaneUI());

    jPanel = new JPanel();
    jPanel.setBackground(new Color(39, 39, 39));
    final GridLayout gridLayout = new GridLayout(0, 4);
    jPanel.setLayout(gridLayout);

    addComponentListener(new MainComponentListener(this, gridLayout));

    setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    final JScrollPane jScrollPane = new JScrollPane(jPanel);
    jScrollPane.setBackground(null);

    jSplitPane.setRightComponent(jScrollPane);

    JMenuBar jMenuBar = new JMenuBar();
    final JTextField jTextField = new JTextField(100);

    jTextField.addCaretListener(new CaretListener()
    {

      @Override
      public void caretUpdate(CaretEvent arg0)
      {
        jPanel.removeAll();

        for (CButton c : holdersList)
          if (jTextField.getText().equals("") || c.toString().contains(jTextField.getText()))
            jPanel.add(c);

        jPanel.revalidate();
      }
    });
    jMenuBar.add(jTextField);

    setJMenuBar(jMenuBar);

    JScrollPane jScrollPane1 = new javax.swing.JScrollPane();

    populateList("/home/aerr/Téléchargements");

    jScrollPane1.setViewportView(TreeExplorer.getInstance());

    jSplitPane.setLeftComponent(jScrollPane1);

    add(jSplitPane);
    pack();
    setVisible(true);
  }

  public void populateList(String path)
  {
    jPanel.removeAll();
    holdersList = FileWalker.getFiles(path, jPanel, TreeExplorer.getInstance().getExplorerRoot());

    revalidate();
    repaint();
  }
}
