package elements;

import actions.PlayAction;
import actions.RemoveAction;
import actions.SeenToggleAction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import misc.Utils;

public final class ButtonHolder extends JPanel
{

  private static final long serialVersionUID = 1L;
  private final CButton cButton;
  private final JTable table;

  public ButtonHolder(CButton cButtonArg)
  {
    super(new BorderLayout());
    ((BorderLayout) this.getLayout()).setHgap(60);
    setOpaque(false);

    cButton = cButtonArg;

    final String[] columnNames =
    {
      "Title",
    };

    final TreeSet<MediaElement> medias = cButton.getMedias();
    final Object[][] data = new Object[medias.size()][1];
    int i = 0;

    for (MediaElement media : medias)
    {
      data[i][0] = media;
      i++;
    }

    table = new JTable(data, columnNames);

    table.setRowHeight(40);

    table.setShowVerticalLines(false);
    table.setGridColor(Color.gray);
    table.setForeground(Color.white);
    table.setOpaque(false);

    final Object[] mediasArray = medias.toArray();
    final CCellEditor cCellEditor = new CCellEditor(table, mediasArray);
    table.getColumnModel().getColumn(0).setCellEditor(cCellEditor);
    table.getColumnModel().getColumn(0).setCellRenderer(new CCellRenderer(mediasArray));

    table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "play");
    table.getActionMap().put("play", new PlayAction());

    table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "toggleSeen");
    table.getActionMap().put("toggleSeen", new SeenToggleAction());

    table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "remove");
    table.getActionMap().put("remove", new RemoveAction());

    table.addMouseListener(cCellEditor);

    TableModel model = table.getModel();
    TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
    table.setRowSorter(sorter);

    filter("");

    JPanel tableHolder = new JPanel();
    tableHolder.setOpaque(false);
    tableHolder.setLayout(new BoxLayout(tableHolder, BoxLayout.PAGE_AXIS));
    tableHolder.add(Box.createVerticalGlue());
    tableHolder.add(table);
    tableHolder.add(Box.createVerticalGlue());
    if (medias.size() < 3)
      tableHolder.add(Box.createRigidArea(new Dimension(1, 60)));
    if (medias.size() <= 6)
      setMaximumSize(new Dimension(9999, Utils.ICON_DIMENSION.height));

    JPanel pictureHolder = new JPanel();
    pictureHolder.setOpaque(false);
    pictureHolder.setLayout(new BoxLayout(pictureHolder, BoxLayout.PAGE_AXIS));
    pictureHolder.add(cButton);
    pictureHolder.add(Box.createVerticalGlue());

    JPanel imdbHolder = new JPanel();
    imdbHolder.setOpaque(false);
    imdbHolder.setLayout(new BoxLayout(imdbHolder, BoxLayout.PAGE_AXIS));

    final boolean hasInfos = cButton.getImdbInfos()[0] != null;
    if (hasInfos)
    {
      String[] infos =
      {
        "<html><p style=\"margin-top: 0px;\">"
        + cButton.getImdbInfos()[1]
        + " <b>·</b> "
        + cButton.getImdbInfos()[2]
        + " <b>·</b> "
        + cButton.getImdbInfos()[3]
        + "</p></html>",
        cButton.getImdbInfos()[4]
      };

      for (String string : infos)
      {
        JLabel jLabel = new JLabel(string, SwingConstants.RIGHT);
        jLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        jLabel.setForeground(Color.WHITE);
        imdbHolder.add(jLabel);
      }

      String star = "<div style=\""
                    + "width: 50px; height: 45px; padding-top: 17px; text-align: center;"
                    + "display: inline-block;"
                    + "background-repeat: no-repeat; background-image: url(file:resources/star.png);\">"
                    + cButton.getImdbInfos()[5]
                    + "</div><div style=\"margin-bottom: 15px;\"></div>";

      JButton jButton = new JButton("<html><body>" + star + "</body></html>");
      jButton.setHorizontalAlignment(SwingConstants.RIGHT);
      jButton.setOpaque(false);
      jButton.setContentAreaFilled(false);
      jButton.setBorderPainted(false);
      jButton.setBorder(new EmptyBorder(0, 0, 0, 0));
      jButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
      jButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
      jButton.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          openWebpage(cButton.getImdbInfos()[6]);
        }
      });

      imdbHolder.add(jButton);
      imdbHolder.add(Box.createVerticalGlue());
    }
    else
    {
      final JButton jButton = new JButton("Get IMDb Infos");
      jButton.addActionListener(new ActionListener()
      {

        @Override
        public void actionPerformed(ActionEvent e)
        {
          jButton.setEnabled(false);
          IMDbDownloader.downloadIMDBinfos(cButton);
        }
      });
      imdbHolder.add(jButton);
    }

    this.add(tableHolder, BorderLayout.CENTER);
    this.add(pictureHolder, BorderLayout.LINE_START);
    this.add(imdbHolder, BorderLayout.LINE_END);

    if (hasInfos)
    {
      JLabel title = new JLabel("<html><h1 style=\"margin: 0px;\">"
                                + cButton.getImdbInfos()[0]
                                + "</h1></html>", SwingConstants.RIGHT);
      title.setAlignmentX(Component.RIGHT_ALIGNMENT);
      title.setForeground(Color.WHITE);
      this.add(title, BorderLayout.PAGE_START);
    }
  }

  public static void openWebpage(String url)
  {
    try
    {
      URI uri = new URI(url);
      Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
      if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
        desktop.browse(uri);
    } catch (URISyntaxException ex)
    {
      Logger.getLogger(ButtonHolder.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex)
    {
      Logger.getLogger(ButtonHolder.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void filter(final String search)
  {
    RowFilter<TableModel, Object> rf = new RowFilter<TableModel, Object>()
    {
      @Override
      public boolean include(RowFilter.Entry<? extends TableModel, ? extends Object> entry)
      {
        final MediaElement elt = (MediaElement) entry.getModel().getValueAt((Integer) entry.getIdentifier(), 0);
        return elt.getVisible() && (search.equals("") || elt.getName().toLowerCase().contains(search.toLowerCase()));
      }
    };
    ((TableRowSorter<TableModel>) table.getRowSorter()).setRowFilter(rf);
  }

  @Override
  public String toString()
  {
    return cButton.toString();
  }

  public int getRowCount()
  {
    return table.getRowCount();
  }
}
