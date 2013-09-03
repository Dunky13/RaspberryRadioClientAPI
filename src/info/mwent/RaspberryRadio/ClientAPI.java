package info.mwent.RaspberryRadio;

import info.mwent.RaspberryRadio.client.GoogleResults;
import info.mwent.RaspberryRadio.client.GoogleResults.ResponseData;
import info.mwent.RaspberryRadio.client.GoogleResults.Result;
import info.mwent.RaspberryRadio.client.exceptions.ConnectionException;
import info.mwent.RaspberryRadio.client.exceptions.DisconnectException;
import info.mwent.RaspberryRadio.client.exceptions.LoginException;
import info.mwent.RaspberryRadio.shared.CommandAPI;
import info.mwent.RaspberryRadio.shared.CommandStationList;
import info.mwent.RaspberryRadio.shared.Commands;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

public class ClientAPI implements API
{
	private String _host;
	private int _port;

	private Socket _socket;

	private BufferedReader _from;
	private PrintWriter _to;
	private Gson _gson;

	private boolean getAlbumCover;

	private boolean _connected = false;

	ThreadPoolExecutor _exec;

	/**
	 * Set the server and and port where the commands should be sent to
	 * 
	 * @param host
	 *            a string of IP Adresses
	 * @param port
	 *            a port number in a range of 1025 to 65535
	 */
	public ClientAPI(String host, int port)
	{
		_host = host;
		_port = Math.max(Math.min(port, 65535), 1025);
		getAlbumCover = false;
		_exec = new ThreadPoolExecutor(2, 2, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

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
	 * @throws DisconnectException
	 */
	public void connect(String username, String password, int timeout) throws LoginException, ConnectionException
	{
		try
		{
			_socket = new Socket(_host, _port);
			if (timeout > 0)
				_socket.setSoTimeout(timeout);
			_to = new PrintWriter(_socket.getOutputStream(), true);
			_from = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			_gson = new GsonBuilder().disableHtmlEscaping().create();
			_connected = true;
			try
			{
				message(new Commands(CommandAPI.HANDSHAKE));
				login(username, password);
			}
			catch (DisconnectException e)
			{
				//WTF SHOULDN'T HAPPPEN
				e.printStackTrace();
			}
		}
		catch (UnknownHostException e)
		{
			disconnect();
			throw new ConnectionException("Could not determine the IP address");
		}
		catch (IOException e)
		{
			disconnect();
			throw new ConnectionException("The server is probably offline");
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
		if (_connected)
		{
			try
			{
				message(new Commands(CommandAPI.BYE));
			}
			catch (DisconnectException e)
			{
				//WTF SHOULDN'T HAPPEN
			}
			_exec.shutdownNow();
		}
		if (_to != null)
			_to.close();
		if (_from != null)
			try
			{
				_from.close();
			}
			catch (IOException e2)
			{
				e2.printStackTrace();
			}
		if (_socket != null)
			try
			{
				_socket.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		_connected = false;
	}

	/**
	 * @return {@link boolean} Value to check if the Client is connected to the
	 *         Server
	 */
	public boolean isConnected()
	{
		return _connected;
	}

	/**
	 * @return {@link boolean} Value to check if the Server is playing any songs
	 * @throws DisconnectException
	 */
	public boolean isPlaying() throws DisconnectException
	{
		Commands c = message(new Commands(CommandAPI.PLAYING));
		return c.getCommand().equals(CommandAPI.PLAYING);
	}

	/**
	 * Enable the album covers, this has to be called explicitly.
	 */
	public void enableAlbumCovers()
	{
		getAlbumCover = true;
	}

	/**
	 * Get a boolean to see if albumCovers are enabled
	 * 
	 * @return {@link boolean} value of if the album should be showed
	 */
	public boolean isAlbumCoversEnabled()
	{
		return getAlbumCover;
	}

	/**
	 * Play the currently selected station.
	 * 
	 * @return {@link String} value of the currently playing song
	 * @throws DisconnectException
	 */
	public String play() throws DisconnectException
	{
		Commands c = message(new Commands(CommandAPI.PLAY));
		Object[] objVals = c.getValues();
		if (objVals.length > 0)
			return (String)objVals[0];
		System.err.println(c.getCommand().toString() + " - it tried to start station, but failed");
		return "";
	}

	/**
	 * Stops the currently selected station.
	 * 
	 * @return {@link String} value of the currently playing song
	 * @throws DisconnectException
	 */
	public String stop() throws DisconnectException
	{
		Commands c = message(new Commands(CommandAPI.STOP));
		Object[] objVals = c.getValues();
		if (objVals.length > 0)
			return (String)objVals[0];
		System.err.println(c.getCommand().toString() + " - it tried to stop station, but failed");
		return "";
	}

	/**
	 * Goes to the next station and play that station
	 * 
	 * @return {@link String} value of the currently playing song
	 * @throws DisconnectException
	 */
	public String next() throws DisconnectException
	{
		Commands c = message(new Commands(CommandAPI.NEXT));
		Object[] objVals = c.getValues();
		if (objVals.length > 0)
			return (String)objVals[0];
		System.err.println(c.getCommand().toString() + " - it tried to get next station, but failed");
		return "";
	}

	/**
	 * Goes to the previous station and play that station
	 * 
	 * @return {@link String} value of the currently playing song
	 * @throws DisconnectException
	 */
	public String prev() throws DisconnectException
	{
		Commands c = message(new Commands(CommandAPI.PREV));
		Object[] objVals = c.getValues();
		if (objVals.length > 0)
			return (String)objVals[0];
		System.err.println(c.getCommand().toString() + " - it tried to get previous station, but failed");
		return "";
	}

	/**
	 * @return {@link String} value of the currently playing song
	 * @throws DisconnectException
	 */
	public String getCurrent() throws DisconnectException
	{
		Commands c = message(new Commands(CommandAPI.CURRENT));
		Object[] objVals = c.getValues();
		if (objVals.length > 0)
			return (String)objVals[0];
		System.err.println(c.getCommand().toString() + " - it tried to update, but failed");
		return "";
	}

	/**
	 * @return {@link List<String>} containing the stations for the currently
	 *         connected server.
	 * @throws DisconnectException
	 */
	public List<CommandStationList> getListAll() throws DisconnectException
	{
		Commands c = message(new Commands(CommandAPI.LIST));
		Object[] objVals = c.getValues();
		List<CommandStationList> list = new ArrayList<CommandStationList>();
		for (Object o : objVals)
		{
			if (o.getClass().equals(LinkedTreeMap.class))
			{
				CommandStationList station = _gson.fromJson(_gson.toJson(o), CommandStationList.class);
				list.add(station);
			}
		}
		return list;
	}

	/**
	 * @return {@link List<String>} containing the stations for the currently
	 *         connected server.
	 * @throws DisconnectException
	 */
	public List<String> getListStations() throws DisconnectException
	{
		Commands c = message(new Commands(CommandAPI.STATION_LIST));
		Object[] objVals = c.getValues();
		List<String> list = new ArrayList<String>();
		for (Object o : objVals)
		{
			if (o.getClass().equals(String.class))
			{
				list.add((String)o);
			}
		}
		return list;
	}

	/**
	 * @return {@link List<String>} containing the currently playing songs on
	 *         the stations for the currently connected server.
	 * @throws DisconnectException
	 */
	public List<String> getListSongs() throws DisconnectException
	{
		Commands c = message(new Commands(CommandAPI.SONG_LIST));
		Object[] objVals = c.getValues();
		List<String> list = new ArrayList<String>();
		for (Object o : objVals)
		{
			if (o.getClass().equals(String.class))
			{
				list.add((String)o);
			}
		}
		return list;
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
		percentage = Math.max(0.0, Math.min(percentage, 100.0));
		Commands c = message(new Commands(CommandAPI.VOLUME, percentage));
		if (!c.getCommand().equals(CommandAPI.VOLUME))
			System.err.println(c.getCommand() + " gave an error: " + c.getValues()[0].toString());
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
		Commands c = message(new Commands(CommandAPI.ADD, url, name));
		Object[] objVals = c.getValues();
		if (objVals.length > 0)
			return (String)objVals[0];
		System.err.println(c.getCommand().toString() + " - it tried to get previous station, but failed");
		return "";
	}

	/**
	 * @param url
	 *            {@link String} of station url
	 * @return {@link String} value of the currently playing song
	 * @throws DisconnectException
	 */
	public String add(String url) throws DisconnectException
	{
		Commands c = message(new Commands(CommandAPI.ADD, url));
		Object[] objVals = c.getValues();
		if (objVals.length > 0)
			return (String)objVals[0];
		System.err.println(c.getCommand().toString() + " - it tried to get previous station, but failed");
		return "";
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
		Commands c = message(new Commands(CommandAPI.DELETE, station));
		Object[] objVals = c.getValues();
		if (objVals.length > 0)
			return (String)objVals[0];
		System.err.println(c.getCommand().toString() + " - it tried to get previous station, but failed");
		return "";
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
		Commands c = message(new Commands(CommandAPI.SPECIFIC, station));
		Object[] objVals = c.getValues();
		if (objVals.length > 0)
			return (String)objVals[0];
		System.err.println(c.getCommand().toString() + " - it tried to get previous station, but failed");
		return "";
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

		String url = getAlbumCover();
		if (url == null || url.contains("default_album_medium"))
			return null;
		else
		{
			try
			{
				return new URL(url);
			}
			catch (MalformedURLException e)
			{
				return null;
			}
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
		if (getAlbumCover)
		{
			String update = getCurrent();

			String url = null;

			if (url == null || url.contains("default_album_medium"))
				url = getImageFromGoogle(update);
			return url;

		}
		return null;

	}

	private String getImageFromGoogle(String update)
	{
		String google = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&imgsz=medium&q=";
		String charset = "UTF-8";

		URL url;
		try
		{
			url = new URL(google + URLEncoder.encode("album cover " + update, charset));
			//			System.out.println(url.toString());
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), charset));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null)
			{
				sb.append(line);
			}
			return findResult(sb.toString());
		}
		catch (MalformedURLException e)
		{
		}
		catch (UnsupportedEncodingException e)
		{
		}
		catch (IOException e)
		{
		}
		return null;
	}

	private static String findResult(String string)
	{
		GoogleResults res = new Gson().fromJson(string, GoogleResults.class);
		if (res == null)
			return null;
		ResponseData rd = res.getResponseData();
		if (rd == null)
			return null;
		List<Result> results = rd.getResults();
		for (Result result : results)
		{
			if (result.isSized(300, 300))
				return result.getUrl();
		}
		for (Result result : results)
		{
			if (result.isSquared())
				return result.getUrl();
		}
		return results.get(0).getUrl();
	}

	private void login(String username, String password) throws LoginException, DisconnectException
	{
		Commands c = message(new Commands(CommandAPI.LOGIN, username, password));
		if (!c.getCommand().equals(CommandAPI.LOGIN_SUCCESSFULL))
		{
			System.err.println(c);
			throw new LoginException((String)c.getValues()[0]);
		}

	}

	/**
	 * 
	 * @param objArr
	 *            - one or more objects are sent to the server
	 * @return returns the last response as an Object
	 */
	private Commands message(final Commands command) throws DisconnectException
	{
		if (!_connected)
		{
			System.err.println("System wasn't connected, please connect");
			disconnect();
			throw new DisconnectException("The server has disconnected");
			//			System.exit(-1);
		}

		try
		{
			Commands ret = _exec.submit(new Callable<Commands>()
			{

				@Override
				public Commands call() throws Exception
				{
					return runCommand(command);
				}
			}).get();
			return ret;
		}
		catch (InterruptedException e)
		{
			System.err.println(e.getMessage());
			return new Commands(CommandAPI.ERROR, e.getMessage());
		}
		catch (ExecutionException e)
		{
			System.err.println(e.getMessage());
			return new Commands(CommandAPI.ERROR, e.getMessage());
		}

	}

	private Commands runCommand(Commands obj) throws IOException
	{
		Commands fromServer = null;
		Commands fromUser = obj;
		String to = _gson.toJson(obj);
		if (fromUser != null)
		{
			_to.println(to);
		}

		String from = _from.readLine();
		fromServer = _gson.fromJson(from, Commands.class);
		if (fromServer == null)
			fromServer = new Commands(CommandAPI.ERROR, from);
		return fromServer;
	}
}
