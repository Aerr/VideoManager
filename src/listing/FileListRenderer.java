package listing;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class FileListRenderer extends DefaultTreeCellRenderer
{

  private static final long serialVersionUID = 1L;

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                boolean leaf, int row, boolean hasFocus)
  {
    JLabel result = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
                                                                hasFocus);

    if (Prefs.getInstance().getPrefs().getBoolean(Integer.toString(result.getText().hashCode()), false))
      result.setForeground(Color.LIGHT_GRAY);

    return result;
  }
}
