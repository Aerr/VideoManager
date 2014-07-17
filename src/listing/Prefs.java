package listing;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Prefs
{

  private static class PrefsHolder
  {

    private final static Prefs instance = new Prefs();
  }

  public static Prefs getInstance()
  {
    return PrefsHolder.instance;
  }

  private final Preferences prefs;

  private Prefs()
  {
    prefs = Preferences.userRoot().node(this.getClass().getName());
    try
    {
      prefs.clear();
    } catch (BackingStoreException ex)
    {
      Logger.getLogger(Prefs.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public Preferences getPrefs()
  {
    return prefs;
  }

}
