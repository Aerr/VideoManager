/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.FontUIResource;

/**
 *
 * @author aerr
 */
public class CustomUIManager
{

  public static void setUIFont()
  {
    UIManager.put("Table.focusCellHighlightBorder", new BorderUIResource.EmptyBorderUIResource(1, 1, 1, 1));

    Font createFont;
    try
    {
      createFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/OpenSans-Semibold.ttf"));
    } catch (IOException | FontFormatException ex)
    {
      Logger.getLogger(CustomUIManager.class.getName()).log(Level.SEVERE, null, ex);
      return;
    }

    createFont = createFont.deriveFont(14F);
    FontUIResource f = new FontUIResource(createFont);
    java.util.Enumeration keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements())
    {
      Object key = keys.nextElement();
      Object value = UIManager.get(key);
      if (value != null && value instanceof FontUIResource)
        UIManager.put(key, f);
    }
  }

}
