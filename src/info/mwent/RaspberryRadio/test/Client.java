package info.mwent.RaspberryRadio.test;

import info.mwent.RaspberryRadio.ClientAPI;
import info.mwent.RaspberryRadio.client.exceptions.ConnectionException;
import info.mwent.RaspberryRadio.client.exceptions.LoginException;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Client
{
	public static void main(String[] args) throws IOException
	{

		ClientAPI ca = new ClientAPI("192.168.0.100", 6584);
		//		ClientAPI ca = new ClientAPI("mwent.info", 6584);
		try
		{
			ca.connect("root", "Admin");
		}
		catch (LoginException e)
		{
			e.printStackTrace();
		}
		catch (ConnectionException e)
		{
			e.printStackTrace();
		}

		//		ca.
		//		System.out.println(ca.getUpdate());
		//		System.out.println(ca.listAll());
		//		System.out.println(ca.getUpdate());
		//		System.out.println(ca.listSongs());
		//		System.out.println(ca.getUpdate());

		//		System.out.println(ca.listStations());

		//		System.out.println(ca.setStation(1));

		ca.disconnect();
	}

	public static void showAlbum(URL albumCover)
	{
		if (albumCover == null)
			albumCover = Client.class.getClass().getResource("/images/emptyAlbum.jpg");
		final URL url = albumCover;
		//System.out.println(url);
		new Thread(new Runnable()
		{
			public void run()
			{
				ImageFrame frame;
				frame = new ImageFrame(url);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		}).start();
	}
}

class ImageFrame extends JFrame
{
	private static final long serialVersionUID = 4590166352953979326L;
	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT = 300;

	public ImageFrame(URL url)
	{
		setTitle("Album");
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		ImageComponent component = new ImageComponent(url);
		add(component);

	}

}

class ImageComponent extends JComponent
{
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	private Image image;

	public ImageComponent(URL url)
	{
		try
		{
			image = ImageIO.read(url);

		}
		catch (IOException e)
		{
		}
	}

	public void paintComponent(Graphics g)
	{
		if (image != null)
			g.drawImage(image, 0, 0, this);
	}

}
