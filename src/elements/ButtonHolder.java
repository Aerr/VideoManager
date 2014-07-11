package elements;

import javax.swing.JPanel;

public class ButtonHolder extends JPanel
{

  private static final long serialVersionUID = 1L;
  private final CButton cButton;

  public ButtonHolder(CButton cButton)
  {
    setBackground(null);
    this.cButton = cButton;
    add(this.cButton);
  }

  @Override
  public String toString()
  {
    return cButton.toString();
  }
}
