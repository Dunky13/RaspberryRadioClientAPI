package info.mwent.RaspberryRadio;

import info.mwent.RaspberryRadio.client.Exceptions.ConnectionException;
import info.mwent.RaspberryRadio.client.Exceptions.LoginException;
import info.mwent.RaspberryRadio.shared.CommandStationList;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import android.os.AsyncTask;
import android.util.Log;

public class AndroidAPI implements API
{

	private String _host;
	private int _port;

	private ClientAPI ca;

	/**
	 * Set the server and and port where the commands should be sent to
	 * 
	 * @param host
	 *            a string of IP Adresses
	 * @param port
	 *            a port number in a range of 1025 to 65535
	 */
	public AndroidAPI(String host, int port)
	{
		_host = host;
		_port = port;
		ca = new ClientAPI(_host, _port);
	}

	/**
	 * Try to connect to the server with an infinite timeout
	 * 
	 * @param username
	 *            The username to login with
	 * @param password
	 *            The password to login with
	 * @throws LoginException
	 * @throws ConnectionException
	 */
	public void connect(String username, String password) throws LoginException, ConnectionException
	{
		connect(username, password, 0);
	}

	/**
	 * Try to connect to the server with a (in)finite timeout
	 * <p>
	 * Exits if an error occurs <br>
	 * Possible errors:
	 * <ul>
	 * <li>Server unknown</li>
	 * <li>Could not open connection to server</li>
	 * <li>Cannot login with the provided username and password</li>
	 * </ul>
	 * 
	 * @param username
	 *            The username to login with
	 * @param password
	 *            The password to login with
	 * @param timeout
	 *            The time to try connect to the server in miliseconds
	 * @throws LoginException
	 * @throws ConnectionException
	 */
	public void connect(String username, String password, int timeOut) throws LoginException, ConnectionException
	{
		AsyncTask<String, Void, Exception> task = new AsyncTask<String, Void, Exception>()
		{
			@Override
			protected Exception doInBackground(String... params)
			{
				if (params.length != 3)
					return null;
				int timeOut = 0;
				try
				{
					timeOut = Integer.parseInt(params[2]);
				}
				catch (NumberFormatException e)
				{
				}
				try
				{
					ca.connect(params[0], params[1], timeOut);
				}
				catch (LoginException e)
				{
					return e;
				}
				catch (ConnectionException e)
				{
					return e;
				}
				return null;
			}
		};
		try
		{
			Exception e = task.execute(username, password, timeOut + "").get();
			if (e != null)
			{
				if (e.getClass().equals(LoginException.class))
					throw (LoginException)e;
				else if (e.getClass().equals(ConnectionException.class))
					throw (ConnectionException)e;
			}
		}
		catch (InterruptedException e)
		{
			Log.e("LOGIN InterruptedException", e.getMessage());

		}
		catch (ExecutionException e)
		{
			Log.e("LOGIN ExecutionException", e.getMessage());
		}
	}

	/**
	 * Disconnect from the server.<br>
	 * If connected tell the server to close the connection
	 */
	public void disconnect()
	{
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected Void doInBackground(Void... params)
			{
				if (ca.isConnected())
					ca.disconnect();
				return null;
			}
		};
		task.execute();
	}

	/**
	 * Play the currently selected station.
	 * 
	 * @return {@link String} value of the currently playing song
	 */
	public String play()
	{
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>()
		{
			@Override
			protected String doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					return ca.play();
				}
				return "";
			}
		};
		try
		{
			return task.execute().get();
		}
		catch (InterruptedException e)
		{
			Log.e("PLAY InterruptedException", e.getMessage());
			return "";
		}
		catch (ExecutionException e)
		{
			Log.e("PLAY ExecutionException", e.getMessage());
			return "";
		}
	}

	/**
	 * Stops the currently selected station.
	 * 
	 * @return {@link String} value of the currently playing song
	 */
	public String stop()
	{
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>()
		{
			@Override
			protected String doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					return ca.stop();
				}
				return "";
			}
		};
		try
		{
			return task.execute().get();
		}
		catch (InterruptedException e)
		{
			Log.e("STOP InterruptedException", e.getMessage());
			return "";
		}
		catch (ExecutionException e)
		{
			Log.e("STOP ExecutionException", e.getMessage());
			return "";
		}
	}

	/**
	 * Goes to the next station and play that station
	 * 
	 * @return {@link String} value of the currently playing song
	 */
	public String next()
	{
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>()
		{
			@Override
			protected String doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					return ca.next();
				}
				return "";
			}
		};
		try
		{
			return task.execute().get();
		}
		catch (InterruptedException e)
		{
			Log.e("NEXT InterruptedException", e.getMessage());
			return "";
		}
		catch (ExecutionException e)
		{
			Log.e("NEXT ExecutionException", e.getMessage());
			return "";
		}
	}

	/**
	 * Goes to the previous station and play that station
	 * 
	 * @return {@link String} value of the currently playing song
	 */
	public String prev()
	{
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>()
		{
			@Override
			protected String doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					return ca.prev();
				}
				return "";
			}
		};
		try
		{
			return task.execute().get();
		}
		catch (InterruptedException e)
		{
			Log.e("PREV InterruptedException", e.getMessage());
			return "";
		}
		catch (ExecutionException e)
		{
			Log.e("PREV ExecutionException", e.getMessage());
			return "";
		}
	}

	/**
	 * @return {@link String} value of the currently playing song
	 */
	public String getCurrent()
	{
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>()
		{
			@Override
			protected String doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					return ca.getCurrent();
				}
				return "";
			}
		};
		try
		{
			return task.execute().get();
		}
		catch (InterruptedException e)
		{
			Log.e("UPDATE InterruptedException", e.getMessage());
			return "";
		}
		catch (ExecutionException e)
		{
			Log.e("UPDATE ExecutionException", e.getMessage());
			return "";
		}
	}

	/**
	 * @return {@link List<String>} containing the stations for the currently
	 *         connected server.
	 */
	public List<String> getListStations()
	{
		AsyncTask<Void, Void, List<String>> task = new AsyncTask<Void, Void, List<String>>()
		{
			@Override
			protected List<String> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					return ca.getListStations();
				}
				return new ArrayList<String>();
			}
		};
		try
		{
			return task.execute().get();
		}
		catch (InterruptedException e)
		{
			Log.e("STATION LIST InterruptedException", e.getMessage());
			return new ArrayList<String>();
		}
		catch (ExecutionException e)
		{
			Log.e("STATION LIST ExecutionException", e.getMessage());
			return new ArrayList<String>();
		}
	}

	/**
	 * @return {@link List<String>} containing the stations for the currently
	 *         connected server.
	 */
	public List<String> getListSongs()
	{
		AsyncTask<Void, Void, List<String>> task = new AsyncTask<Void, Void, List<String>>()
		{
			@Override
			protected List<String> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					return ca.getListSongs();
				}
				return new ArrayList<String>();
			}
		};
		try
		{
			return task.execute().get();
		}
		catch (InterruptedException e)
		{
			Log.e("STATION LIST InterruptedException", e.getMessage());
			return new ArrayList<String>();
		}
		catch (ExecutionException e)
		{
			Log.e("STATION LIST ExecutionException", e.getMessage());
			return new ArrayList<String>();
		}
	}

	/**
	 * @return {@link List<String>} containing the stations for the currently
	 *         connected server.
	 */
	public List<CommandStationList> getListAll()
	{
		AsyncTask<Void, Void, List<CommandStationList>> task = new AsyncTask<Void, Void, List<CommandStationList>>()
		{
			@Override
			protected List<CommandStationList> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					return ca.getListAll();
				}
				return new ArrayList<CommandStationList>();
			}
		};
		try
		{
			return task.execute().get();
		}
		catch (InterruptedException e)
		{
			Log.e("STATION LIST InterruptedException", e.getMessage());
			return new ArrayList<CommandStationList>();
		}
		catch (ExecutionException e)
		{
			Log.e("STATION LIST ExecutionException", e.getMessage());
			return new ArrayList<CommandStationList>();
		}
	}

	/**
	 * Set the Volume to a certain percentage
	 * 
	 * @param percentage
	 *            {@link double} value between 0.0 and 100.0
	 */
	public void setVolume(double percentage)
	{
		AsyncTask<Double, Void, Void> task = new AsyncTask<Double, Void, Void>()
		{
			@Override
			protected Void doInBackground(Double... params)
			{
				if (params.length != 1)
					return null;
				if (ca.isConnected())
				{
					ca.setVolume(params[0]);
				}
				return null;
			}
		};
		task.execute(percentage);
	}

	/**
	 * @param name
	 *            {@link String} of station name
	 * @param url
	 *            {@link String} of station url
	 * @return {@link String} value of the currently playing song
	 */
	public String add(String name, String url)
	{
		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>()
		{
			@Override
			protected String doInBackground(String... params)
			{
				if (ca.isConnected())
				{
					return ca.add(params[0], params[1]);
				}
				return "";
			}
		};
		try
		{
			return task.execute(name, url).get();
		}
		catch (InterruptedException e)
		{
			Log.e("ADD URL InterruptedException", e.getMessage());
			return "";
		}
		catch (ExecutionException e)
		{
			Log.e("ADD URL ExecutionException", e.getMessage());
			return "";
		}
	}

	/**
	 * @param url
	 *            {@link String} of station url
	 * @return {@link String} value of the currently playing song
	 */
	public String add(String url)
	{
		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>()
		{
			@Override
			protected String doInBackground(String... params)
			{
				if (ca.isConnected())
				{
					return ca.add(params[0]);
				}
				return "";
			}
		};
		try
		{
			return task.execute(url).get();
		}
		catch (InterruptedException e)
		{
			Log.e("ADD URL InterruptedException", e.getMessage());
			return "";
		}
		catch (ExecutionException e)
		{
			Log.e("ADD URL ExecutionException", e.getMessage());
			return "";
		}
	}

	/**
	 * Removes a station at the given position.
	 * 
	 * @param station
	 *            {@link int} value of the list position
	 * @return {@link String} value of the currently playing song
	 */
	public String removeStation(int station)
	{
		AsyncTask<Integer, Void, String> task = new AsyncTask<Integer, Void, String>()
		{
			@Override
			protected String doInBackground(Integer... params)
			{
				if (params.length != 1)
					return "";
				if (ca.isConnected())
				{
					return ca.removeStation(params[0]);
				}
				return "";
			}
		};
		try
		{
			return task.execute(station).get();
		}
		catch (InterruptedException e)
		{
			Log.e("REMOVE STATION InterruptedException", e.getMessage());
			return "";
		}
		catch (ExecutionException e)
		{
			Log.e("REMOVE STATION ExecutionException", e.getMessage());
			return "";
		}
	}

	/**
	 * Set the currently playing station the the given position.
	 * 
	 * @param station
	 *            {@link int} value of the list position
	 * @return {@link String} value of the currently playing song
	 */
	public String setStation(int station)
	{
		AsyncTask<Integer, Void, String> task = new AsyncTask<Integer, Void, String>()
		{
			@Override
			protected String doInBackground(Integer... params)
			{
				if (params.length != 1)
					return "";
				if (ca.isConnected())
				{
					return ca.setStation(params[0]);
				}
				return "";
			}
		};
		try
		{
			return task.execute(station).get();
		}
		catch (InterruptedException e)
		{
			Log.e("SET STATION InterruptedException", e.getMessage());
			return "";
		}
		catch (ExecutionException e)
		{
			Log.e("SET STATION ExecutionException", e.getMessage());
			return "";
		}
	}

	/**
	 * Tries to fetch the album cover from Last.FM using their API.<br>
	 * This method is only called if the method {@link #enableAlbumCovers()} is
	 * called
	 * 
	 * @return {@link URL} that is the album image, {@link null} if no image was
	 *         found
	 */
	public URL getAlbumCoverURL()
	{
		AsyncTask<Void, Void, URL> task = new AsyncTask<Void, Void, URL>()
		{
			@Override
			protected URL doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					return ca.getAlbumCoverURL();
				}
				return null;
			}
		};
		try
		{
			return task.execute().get();
		}
		catch (InterruptedException e)
		{
			Log.e("GET ALBUM InterruptedException", e.getMessage());
			return null;
		}
		catch (ExecutionException e)
		{
			Log.e("GET ALBUM ExecutionException", e.getMessage());
			return null;
		}
	}

	/**
	 * Tries to fetch the album cover from Last.FM using their API.<br>
	 * This method is only called if the method {@link #enableAlbumCovers()} is
	 * called
	 * 
	 * @return {@link URL} that is the album image, {@link null} if no image was
	 *         found
	 */
	public String getAlbumCover()
	{
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>()
		{
			@Override
			protected String doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					return ca.getAlbumCover();
				}
				return null;
			}
		};
		try
		{
			return task.execute().get();
		}
		catch (InterruptedException e)
		{
			Log.e("GET ALBUM InterruptedException", e.getMessage());
			return null;
		}
		catch (ExecutionException e)
		{
			Log.e("GET ALBUM ExecutionException", e.getMessage());
			return null;
		}
	}

	/**
	 * @return {@link boolean} Value to check if the Server is playing any songs
	 */
	public boolean isPlaying()
	{
		AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>()
		{
			@Override
			protected Boolean doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					return ca.isPlaying();
				}
				return false;
			}
		};
		try
		{
			return task.execute().get();
		}
		catch (InterruptedException e)
		{
			Log.e("GET ALBUM InterruptedException", e.getMessage());
			return false;
		}
		catch (ExecutionException e)
		{
			Log.e("GET ALBUM ExecutionException", e.getMessage());
			return false;
		}
	}

	/**
	 * @return {@link boolean} Value to check if the Client is connected to the
	 *         Server
	 */
	public boolean isConnected()
	{
		return ca.isConnected();
	}

	/**
	 * Enable the album covers, this has to be called explicitly.
	 */
	public void enableAlbumCovers()
	{
		ca.enableAlbumCovers();
	}

	/**
	 * Get a boolean to see if albumCovers are enabled
	 * 
	 * @return {@link boolean} value of if the album should be showed
	 */
	public boolean isAlbumCoversEnabled()
	{
		return ca.isAlbumCoversEnabled();
	}

	//	@Override
	//	public String getCurrent()
	//	{
	//		// TODO Auto-generated method stub
	//		return null;
	//	}

	//	@Override
	//	public List<String> getListStations()
	//	{
	//		// TODO Auto-generated method stub
	//		return null;
	//	}

	//	@Override
	//	public List<String> getListSongs()
	//	{
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//
	//	@Override
	//	public List<CommandStationList> getListAll()
	//	{
	//		// TODO Auto-generated method stub
	//		return null;
	//	}

}
