package elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import listing.FileWalker;

public class IconDownloader extends SwingWorker<Void, Void>
{

    private CButton button;
    private String resultURL;
    private boolean isFolder;

    public IconDownloader(CButton button, boolean isFolder)
    {
        super();
        this.button = button;
        this.isFolder = isFolder;
    }

    @Override
    protected Void doInBackground() throws Exception
    {
        String name = button.getText();
        if (isFolder)
        {
            button.setImage(ImageIO.read(new File("res/folder.jpg")));
            return null;
        }
        File f = new File("thumbs/" + name.hashCode() + ".jpg");
        if (f.exists() && !f.isDirectory())
        {
            button.setImage(ImageIO.read(f));
            return null;
        }

        InputStream is = null;
        StringBuilder res = new StringBuilder();
        try
        {
            System.out.println("For: " + name + " - Folder: " + isFolder);
            name = FileWalker.getPrefix(name, "E0", "E1", "E2");
            System.out.println(name);
            URL url = new URL("https://www.bing.com/images/search?q=" + name.replace(' ', '+')
                    + "+poster");

            is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = null;
            while ((line = br.readLine()) != null)
            {
                res.append(line);
            }

        } catch (MalformedURLException mue)
        {
            mue.printStackTrace();
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        } finally
        {
            try
            {
                if (is != null)
                    is.close();
            } catch (IOException ioe)
            {
                // nothing to see here
            }
        }

        resultURL = res.toString();
        resultURL = resultURL.substring(resultURL.indexOf("imgurl:&quot;") + "imgurl:&quot;".length());
        resultURL = resultURL.substring(0, resultURL.indexOf("&quot;,"));

        button.setImage(ImageIO.read(new URL(resultURL)));

        return null;
    }

    @Override
    protected void done()
    {
        super.done();

    }

}
