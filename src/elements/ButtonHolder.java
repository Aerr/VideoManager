package elements;

import actions.PlayAction;
import actions.RemoveAction;
import actions.SeenToggleAction;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.TreeSet;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import misc.Utils;

public final class ButtonHolder extends JPanel
{

  private static final long serialVersionUID = 1L;
  private final CButton cButton;
  private final JTable table;

  public ButtonHolder(CButton cButton)
  {
    super(new BorderLayout());
    ((BorderLayout) this.getLayout()).setHgap(60);
    setBackground(null);

    this.cButton = cButton;

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
    table.setBackground(null);

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
    tableHolder.setBackground(null);
    tableHolder.setLayout(new BoxLayout(tableHolder, BoxLayout.PAGE_AXIS));
    tableHolder.add(Box.createVerticalGlue());
    tableHolder.add(table);
    tableHolder.add(Box.createVerticalGlue());
    if (medias.size() < 3)
      tableHolder.add(Box.createRigidArea(new Dimension(1, 60)));
    if (medias.size() <= 6)
      setMaximumSize(new Dimension(9999, Utils.ICON_DIMENSION.height));


    JPanel pictureHolder = new JPanel();
    pictureHolder.setBackground(null);
    pictureHolder.setLayout(new BoxLayout(pictureHolder, BoxLayout.PAGE_AXIS));
    pictureHolder.add(cButton);
    pictureHolder.add(Box.createVerticalGlue());

    this.add(tableHolder, BorderLayout.CENTER);
    this.add(pictureHolder, BorderLayout.LINE_START);
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
