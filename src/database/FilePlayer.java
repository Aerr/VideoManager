/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import elements.MediaElement;
import gui.Gui;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

  private final MediaElement[] medias;
  private static DatabaseSaver saver;
  private static FilePlayer currentPlayer;
  private static boolean cancelPlayer;

  public FilePlayer(final MediaElement... medias)
  {
    super();
    this.medias = medias;
    FilePlayer.cancelPlayer = false;
    cancelPreviousPlayer();
  }

  @Override
  protected Void doInBackground() throws Exception
  {

    currentPlayer = this;
    if (medias.length > 1)
      Gui.getInstance().getStopButton().setVisible(true);

    for (MediaElement item : medias)
    {
      if (cancelPlayer)
        break;

      if (!item.getVisible())
        continue;

      String name = item.getPath();

      item.setSeen(true);

      if (saver != null && !saver.isDone())
        saver.cancel(true);

      saver = new DatabaseSaver();
      saver.execute();

      if (cancelPlayer)
        break;

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

  private void cancelPreviousPlayer()
  {
    FilePlayer.cancelPlayer = true;
    if (currentPlayer != null && !currentPlayer.isDone())
    {
      currentPlayer.cancel(false);
      try
      {
        if (currentPlayer != null)
          currentPlayer.get();
      } catch (InterruptedException | ExecutionException ex)
      {
        Logger.getLogger(FilePlayer.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    FilePlayer.cancelPlayer = false;
  }

  public static void cancelCurrentPlayer()
  {
    cancelPlayer = true;
    Gui.getInstance().getStopButton().setVisible(false);
  }

  @Override
  protected void done()
  {
    super.done();
    currentPlayer = null;
    Gui.getInstance().getStopButton().setVisible(false);
  }
}
