package info.mwent.RaspberryRadio;

import info.mwent.RaspberryRadio.client.exceptions.ConnectionException;
import info.mwent.RaspberryRadio.client.exceptions.DisconnectException;
import info.mwent.RaspberryRadio.client.exceptions.LoginException;
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
	 * @throws DisconnectException
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
		AsyncTask<String, Void, AsyncTaskResult<Void>> task = new AsyncTask<String, Void, AsyncTaskResult<Void>>()
		{
			@Override
			protected AsyncTaskResult<Void> doInBackground(String... params)
			{
				if (params.length != 3)
					return new AsyncTaskResult<Void>((Void)null);
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
					return new AsyncTaskResult<Void>(e);
				}
				catch (ConnectionException e)
				{
					return new AsyncTaskResult<Void>(e);
				}
				return new AsyncTaskResult<Void>((Void)null);
			}
		};
		try
		{
			AsyncTaskResult<Void> res = task.execute(username, password, timeOut + "").get();
			Exception e = res.getError();
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
	 * 
	 * @throws DisconnectException
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
	 * @throws DisconnectException
	 */
	public String play() throws DisconnectException
	{
		AsyncTask<Void, Void, AsyncTaskResult<String>> task = new AsyncTask<Void, Void, AsyncTaskResult<String>>()
		{
			@Override
			protected AsyncTaskResult<String> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<String>(ca.play());
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<String>(e);
					}
				}
				return new AsyncTaskResult<String>("");
			}
		};
		try
		{
			AsyncTaskResult<String> res = task.execute().get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			return "";
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
	 * @throws DisconnectException
	 */
	public String stop() throws DisconnectException
	{
		AsyncTask<Void, Void, AsyncTaskResult<String>> task = new AsyncTask<Void, Void, AsyncTaskResult<String>>()
		{
			@Override
			protected AsyncTaskResult<String> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<String>(ca.stop());
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<String>(e);
					}
				}
				return new AsyncTaskResult<String>("");
			}
		};
		try
		{
			AsyncTaskResult<String> res = task.execute().get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			return "";
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
	 * @throws DisconnectException
	 */
	public String next() throws DisconnectException
	{
		AsyncTask<Void, Void, AsyncTaskResult<String>> task = new AsyncTask<Void, Void, AsyncTaskResult<String>>()
		{
			@Override
			protected AsyncTaskResult<String> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<String>(ca.next());
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<String>(e);
					}
				}
				return new AsyncTaskResult<String>("");
			}
		};
		try
		{
			AsyncTaskResult<String> res = task.execute().get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			return "";
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
	 * @throws DisconnectException
	 */
	public String prev() throws DisconnectException
	{
		AsyncTask<Void, Void, AsyncTaskResult<String>> task = new AsyncTask<Void, Void, AsyncTaskResult<String>>()
		{
			@Override
			protected AsyncTaskResult<String> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<String>(ca.prev());
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<String>(e);
					}
				}
				return new AsyncTaskResult<String>("");
			}
		};
		try
		{
			AsyncTaskResult<String> res = task.execute().get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			return "";
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
	 * @throws DisconnectException
	 */
	public String getCurrent() throws DisconnectException
	{
		AsyncTask<Void, Void, AsyncTaskResult<String>> task = new AsyncTask<Void, Void, AsyncTaskResult<String>>()
		{
			@Override
			protected AsyncTaskResult<String> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<String>(ca.getCurrent());
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<String>(e);
					}
				}
				return new AsyncTaskResult<String>("");
			}
		};
		try
		{
			AsyncTaskResult<String> res = task.execute().get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			return "";
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
	 * @throws DisconnectException
	 */
	public List<String> getListStations() throws DisconnectException
	{
		AsyncTask<Void, Void, AsyncTaskResult<List<String>>> task = new AsyncTask<Void, Void, AsyncTaskResult<List<String>>>()
		{
			@Override
			protected AsyncTaskResult<List<String>> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<List<String>>(ca.getListStations());
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<List<String>>(e);
					}
				}
				return new AsyncTaskResult<List<String>>(new ArrayList<String>());
			}
		};
		try
		{
			AsyncTaskResult<List<String>> res = task.execute().get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			//shouldn't happen
			return new ArrayList<String>();
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
	 * @throws DisconnectException
	 */
	public List<String> getListSongs() throws DisconnectException
	{
		AsyncTask<Void, Void, AsyncTaskResult<List<String>>> task = new AsyncTask<Void, Void, AsyncTaskResult<List<String>>>()
		{
			@Override
			protected AsyncTaskResult<List<String>> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<List<String>>(ca.getListSongs());
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<List<String>>(e);
					}
				}
				return new AsyncTaskResult<List<String>>(new ArrayList<String>());
			}
		};
		try
		{
			AsyncTaskResult<List<String>> res = task.execute().get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			//shouldn't happen
			return new ArrayList<String>();
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
	 * @throws DisconnectException
	 */
	public List<CommandStationList> getListAll() throws DisconnectException
	{
		AsyncTask<Void, Void, AsyncTaskResult<List<CommandStationList>>> task = new AsyncTask<Void, Void, AsyncTaskResult<List<CommandStationList>>>()
		{
			@Override
			protected AsyncTaskResult<List<CommandStationList>> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<List<CommandStationList>>(ca.getListAll());
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<List<CommandStationList>>(e);
					}
				}
				return new AsyncTaskResult<List<CommandStationList>>(new ArrayList<CommandStationList>());
			}
		};
		try
		{
			AsyncTaskResult<List<CommandStationList>> res = task.execute().get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			//shouldn't happen
			return new ArrayList<CommandStationList>();
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
	 * @throws DisconnectException
	 */
	public void setVolume(double percentage) throws DisconnectException
	{
		AsyncTask<Double, Void, AsyncTaskResult<Void>> task = new AsyncTask<Double, Void, AsyncTaskResult<Void>>()
		{
			@Override
			protected AsyncTaskResult<Void> doInBackground(Double... params)
			{
				if (params.length != 1)
					return new AsyncTaskResult<Void>((Void)null);
				if (ca.isConnected())
				{
					try
					{
						ca.setVolume(params[0]);
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<Void>(e);
					}

				}
				return new AsyncTaskResult<Void>((Void)null);
			}
		};

		try
		{
			AsyncTaskResult<Void> res = task.execute(percentage).get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
		}
		catch (InterruptedException e1)
		{
		}
		catch (ExecutionException e1)
		{
		}
	}

	/**
	 * @param name
	 *            {@link String} of station name
	 * @param url
	 *            {@link String} of station url
	 * @return {@link String} value of the currently playing song
	 * @throws DisconnectException
	 */
	public String add(String name, String url) throws DisconnectException
	{

		AsyncTask<String, Void, AsyncTaskResult<String>> task = new AsyncTask<String, Void, AsyncTaskResult<String>>()
		{
			@Override
			protected AsyncTaskResult<String> doInBackground(String... params)
			{
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<String>(ca.add(params[0], params[1]));
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<String>(e);
					}
				}
				return new AsyncTaskResult<String>("");
			}
		};
		try
		{
			AsyncTaskResult<String> res = task.execute(name, url).get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			return "";
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
	 * @throws DisconnectException
	 */
	public String add(String url) throws DisconnectException
	{
		AsyncTask<String, Void, AsyncTaskResult<String>> task = new AsyncTask<String, Void, AsyncTaskResult<String>>()
		{
			@Override
			protected AsyncTaskResult<String> doInBackground(String... params)
			{
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<String>(ca.add(params[0]));
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<String>(e);
					}
				}
				return new AsyncTaskResult<String>("");
			}
		};
		try
		{
			AsyncTaskResult<String> res = task.execute(url).get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			return "";
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
	 * @throws DisconnectException
	 */
	public String removeStation(int station) throws DisconnectException
	{
		AsyncTask<Integer, Void, AsyncTaskResult<String>> task = new AsyncTask<Integer, Void, AsyncTaskResult<String>>()
		{
			@Override
			protected AsyncTaskResult<String> doInBackground(Integer... params)
			{
				if (params.length != 1)
					return new AsyncTaskResult<String>("");
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<String>(ca.removeStation(params[0]));
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<String>(e);
					}
				}
				return new AsyncTaskResult<String>("");
			}
		};
		try
		{
			AsyncTaskResult<String> res = task.execute(station).get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			return "";
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
	 * @throws DisconnectException
	 */
	public String setStation(int station) throws DisconnectException
	{
		AsyncTask<Integer, Void, AsyncTaskResult<String>> task = new AsyncTask<Integer, Void, AsyncTaskResult<String>>()
		{
			@Override
			protected AsyncTaskResult<String> doInBackground(Integer... params)
			{
				if (params.length != 1)
					return new AsyncTaskResult<String>("");
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<String>(ca.setStation(params[0]));
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<String>(e);
					}
				}
				return new AsyncTaskResult<String>("");
			}
		};
		try
		{
			AsyncTaskResult<String> res = task.execute(station).get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			return "";
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
	 * @throws DisconnectException
	 */
	public URL getAlbumCoverURL() throws DisconnectException
	{
		AsyncTask<Void, Void, AsyncTaskResult<URL>> task = new AsyncTask<Void, Void, AsyncTaskResult<URL>>()
		{
			@Override
			protected AsyncTaskResult<URL> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<URL>(ca.getAlbumCoverURL());
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<URL>(e);
					}
				}
				return new AsyncTaskResult<URL>((URL)null);
			}
		};
		try
		{
			AsyncTaskResult<URL> res = task.execute().get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			return null;
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
	 * @throws DisconnectException
	 */
	public String getAlbumCover() throws DisconnectException
	{
		AsyncTask<Void, Void, AsyncTaskResult<String>> task = new AsyncTask<Void, Void, AsyncTaskResult<String>>()
		{
			@Override
			protected AsyncTaskResult<String> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<String>(ca.getAlbumCover());
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<String>(e);
					}
				}
				return new AsyncTaskResult<String>("");
			}
		};
		try
		{
			AsyncTaskResult<String> res = task.execute().get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			return "";
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
	 * @throws DisconnectException
	 */
	public boolean isPlaying() throws DisconnectException
	{
		AsyncTask<Void, Void, AsyncTaskResult<Boolean>> task = new AsyncTask<Void, Void, AsyncTaskResult<Boolean>>()
		{
			@Override
			protected AsyncTaskResult<Boolean> doInBackground(Void... params)
			{
				if (ca.isConnected())
				{
					try
					{
						return new AsyncTaskResult<Boolean>(ca.isPlaying());
					}
					catch (DisconnectException e)
					{
						return new AsyncTaskResult<Boolean>(e);
					}
				}
				return new AsyncTaskResult<Boolean>(false);
			}
		};
		try
		{
			AsyncTaskResult<Boolean> res = task.execute().get();
			Exception e = res.getError();
			if (e != null && e.getClass().equals(DisconnectException.class))
				throw (DisconnectException)e;
			if (res.getResult() != null)
				return res.getResult();

			return false;
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
}
