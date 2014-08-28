
import gui.Gui;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main
{

  public static void main(String[] args)
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException ex)
    {
      Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex)
    {
      Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex)
    {
      Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
    } catch (UnsupportedLookAndFeelException ex)
    {
      Logger.getLogger(Gui.class.getName()).log(Level.SEVERE, null, ex);
    }
    Gui.getInstance();
  }
}
