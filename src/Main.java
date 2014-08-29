
import gui.Gui;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;

public class Main
{

  public static void main(String[] args)
  {
    try
    {
      if (System.getProperty("os.name").equals("Linux"))
      {
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
          if ("Nimbus".equals(info.getName()))
          {
            UIManager.setLookAndFeel(info.getClassName());
            UIDefaults defaults = UIManager.getLookAndFeelDefaults();

            defaults.put("nimbusBase", new ColorUIResource(99, 99, 99));

//            defaults.put("control", new ColorUIResource(210, 0, 0));
//            defaults.put("info", new ColorUIResource(210, 0, 0));
//            defaults.put("nimbusAlertYellow", new ColorUIResource(210, 0, 0));
//            defaults.put("nimbusDisabledText", new ColorUIResource(210, 0, 0));
//            defaults.put("nimbusFocus", new ColorUIResource(210, 0, 0));
//            defaults.put("nimbusGreen", new ColorUIResource(210, 0, 0));
//            defaults.put("nimbusInfoBlue", new ColorUIResource(210, 0, 0));
//            defaults.put("nimbusLightBackground", new ColorUIResource(210, 0, 0));
//            defaults.put("nimbusOrange", new ColorUIResource(210, 0, 0));
//            defaults.put("nimbusRed", new ColorUIResource(210, 0, 0));
//            defaults.put("nimbusSelectedText		", new ColorUIResource(210, 0, 0));
//            defaults.put("nimbusSelectionBackground", new ColorUIResource(210, 0, 0));
//            defaults.put("text", new ColorUIResource(210, 0, 0));
//
//            defaults.put("activeCaption", new ColorUIResource(210, 0, 0));
//            defaults.put("background", new ColorUIResource(210, 0, 0));
//            defaults.put("controlDkShadow", new ColorUIResource(210, 0, 0));
//            defaults.put("controlHighlight", new ColorUIResource(210, 0, 0));
//            defaults.put("controlLHighlight", new ColorUIResource(210, 0, 0));
//            defaults.put("controlShadow", new ColorUIResource(210, 0, 0));
//            defaults.put("controlText", new ColorUIResource(210, 0, 0));
//            defaults.put("desktop", new ColorUIResource(210, 0, 0));
//            defaults.put("inactiveCaption", new ColorUIResource(210, 0, 0));
//            defaults.put("infoText", new ColorUIResource(210, 0, 0));
//            defaults.put("menu", new ColorUIResource(210, 0, 0));
//            defaults.put("menuText", new ColorUIResource(210, 0, 0));
//            defaults.put("nimbusBlueGrey", new ColorUIResource(210, 0, 0));
//            defaults.put("nimbusBorder", new ColorUIResource(210, 0, 0));
//            defaults.put("nimbusSelection", new ColorUIResource(210, 0, 0));
//            defaults.put("scrollbar", new ColorUIResource(210, 0, 0));
//            defaults.put("textBackground", new ColorUIResource(210, 0, 0));
//            defaults.put("textForeground", new ColorUIResource(210, 0, 0));
//            defaults.put("textHighlight", new ColorUIResource(210, 0, 0));
//            defaults.put("textHighlightText", new ColorUIResource(210, 0, 0));
//            defaults.put("textInactiveText", new ColorUIResource(210, 0, 0));

            defaults.put("Table.gridColor", new Color(214, 217, 223));
            defaults.put("Table.showGrid", true);

            break;
          }
      }
      else
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
