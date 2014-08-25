package elements;

import actions.PlayAction;
import actions.SeenToggleAction;
import java.awt.BorderLayout;
import java.awt.Color;
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

public class ButtonHolder extends JPanel
{

  private static final long serialVersionUID = 1L;
  private final CButton cButton;

  public ButtonHolder()
  {
    this(null);
  }

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

    final JTable table = new JTable(data, columnNames);

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

    table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "toggleSeen");
    table.getActionMap().put("toggleSeen", new SeenToggleAction());

    table.addMouseListener(cCellEditor);

    TableModel model = table.getModel();
    TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);

    RowFilter<TableModel, Object> rf = new RowFilter<TableModel, Object>()
    {
      @Override
      public boolean include(RowFilter.Entry<? extends TableModel, ? extends Object> entry)
      {
        final MediaElement elt = (MediaElement) entry.getModel().getValueAt((Integer) entry.getIdentifier(), 0);
        return elt.getVisible();
      }
    };
    sorter.setRowFilter(rf);

    table.setRowSorter(sorter);

    this.add(table, BorderLayout.CENTER);

    JPanel holder = new JPanel();
    holder.setBackground(null);
    holder.setLayout(new BoxLayout(holder, BoxLayout.PAGE_AXIS));
    holder.add(cButton);
    holder.add(Box.createVerticalGlue());
    this.add(holder, BorderLayout.LINE_START);
  }

  @Override
  public String toString()
  {
    return cButton.toString();
  }
}
