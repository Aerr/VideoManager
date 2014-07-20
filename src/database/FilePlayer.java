/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import elements.CButton;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.swing.SwingWorker;

/**
 *
 * @author aerr
 */
public class FilePlayer extends SwingWorker<Void, Void>
{

  /**
   * @return the saver
   */
  public static DatabaseSaver getSaver()
  {
    return saver;
  }

  private final CButton[] medias;
  private static DatabaseSaver saver;

  public FilePlayer(final CButton... medias)
  {
    super();
    this.medias = medias;
  }

  @Override
  protected Void doInBackground() throws Exception
  {
    for (CButton item : medias)
    {
      String name = item.getPath();

      item.setSeen(true);

      if (saver != null && !saver.isDone())
        saver.cancel(true);

      saver = new DatabaseSaver();
      saver.execute();

      try
      {
        if (System.getProperty("os.name").equals("Linux"))
        {
          final Process process = Runtime.getRuntime().exec(new String[]
          {
            "bash", "-c", "vlc \"" + name + "\""
                          + " vlc://exit --play-and-exit"
          });
          process.waitFor();
        }
        else
          Desktop.getDesktop().open(new File(name));
      } catch (IOException | InterruptedException exception)
      {
        System.err.println("Could not open: " + name + System.lineSeparator());
      }
    }
    return null;
  }

  @Override
  protected void done()
  {
    super.done();

  }
}
