package elements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import listing.Prefs;

public class CButton extends JButton
{
    private static final long serialVersionUID = 1L;
    private String path;
    private boolean isFolder;

    public boolean isFolder()
    {
        return isFolder;
    }

    public CButton(String text, String path, boolean isFolder)
    {
        super(text);
        Dimension dimension = new Dimension(125, 200);

        setForeground(Color.white);
        setBackground(null);
        setBorder(null);

        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setPreferredSize(dimension);
        setMaximumSize(dimension);
        setMinimumSize(dimension);
        setToolTipText(text);

        this.path = path;
        this.isFolder = isFolder;

        new IconDownloader(this, isFolder).execute();

        addMouseListener(new FileListListener(this));

        if (Prefs.getInstance().getPrefs().getBoolean(Integer.toString(path.hashCode()), false))
            this.setForeground(Color.LIGHT_GRAY);
    }

    public CButton(String text, String path)
    {
        this(text, path, false);
    }

    public void setImage(BufferedImage newImage)
    {
        if (newImage != null)
        {
            if (!isFolder)
            {

                Dimension dim = getRatio(newImage.getWidth(), newImage.getHeight());

                BufferedImage image = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = image.createGraphics();

                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.drawImage(newImage, 0, 0, dim.width, dim.height, null);
                g2.dispose();

                saveFile("thumbs/" + getText().hashCode(), image);
                setIcon(new ImageIcon(image));
            }
            else
                setIcon(new ImageIcon(newImage));

            revalidate();
            repaint();
        }
    }

    public void saveFile(final String filename, final BufferedImage image)
    {
        File file = new File(filename + ".jpg");
        try
        {
            ImageIO.write(image, "jpg", file);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private Dimension getRatio(int imageWidth, int imageHeight)
    {
        float ratio = (float) imageHeight / (float) imageWidth;
        int width = 0;
        int height = 0;

        height = getMaximumSize().height - 25;
        width = (int) (height / ratio);

        return new Dimension(width, height);
    }

    public String getPath()
    {
        return path;
    }

    @Override
    public String toString()
    {
        return getText();
    }
}
