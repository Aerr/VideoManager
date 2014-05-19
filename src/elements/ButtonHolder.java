package elements;

import javax.swing.JPanel;

public class ButtonHolder extends JPanel
{
    private static final long serialVersionUID = 1L;
    private CButton cButton;

    public ButtonHolder(String text, String path)
    {
        setBackground(null);
        cButton = new CButton(text, path);
        add(cButton);
    }

    public ButtonHolder(String text, String path, boolean isFolder)
    {
        setBackground(null);
        cButton = new CButton(text, path, isFolder);
        add(cButton);
    }

    @Override
    public String toString()
    {
        return cButton.toString();
    }
}
