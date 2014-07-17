package elements;

import javax.swing.JPanel;
import misc.Utils;

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
    setBackground(null);

    setPreferredSize(Utils.ICON_DIMENSION);

    this.cButton = cButton;
    if (this.cButton != null)
      add(this.cButton);
  }

  @Override
  public String toString()
  {
    return cButton.toString();
  }
}
