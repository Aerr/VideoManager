package listing;
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
        // clear();
	}

	@SuppressWarnings("unused")
	private void clear()
	{
		try
		{
			prefs.clear();
		}
		catch (BackingStoreException e)
		{
			e.printStackTrace();
		}
	}

	public Preferences getPrefs()
	{
		return prefs;
	}

}
