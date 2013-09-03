package info.mwent.RaspberryRadio;

import info.mwent.RaspberryRadio.client.exceptions.ConnectionException;
import info.mwent.RaspberryRadio.client.exceptions.DisconnectException;
import info.mwent.RaspberryRadio.client.exceptions.LoginException;
import info.mwent.RaspberryRadio.shared.CommandStationList;
import java.net.URL;
import java.util.List;

public interface API
{

	public void connect(String username, String password) throws LoginException, ConnectionException;

	public void connect(String username, String password, int timeOut) throws LoginException, ConnectionException,
		DisconnectException;

	public void disconnect() throws DisconnectException;

	public boolean isConnected();

	public boolean isPlaying() throws DisconnectException;

	public void enableAlbumCovers();

	public boolean isAlbumCoversEnabled();

	public String play() throws DisconnectException;

	public String stop() throws DisconnectException;

	public String next() throws DisconnectException;

	public String prev() throws DisconnectException;

	public String getCurrent() throws DisconnectException;

	public String add(String name, String url) throws DisconnectException;

	public String add(String url) throws DisconnectException;

	public String setStation(int station) throws DisconnectException;

	public String removeStation(int station) throws DisconnectException;

	public String getAlbumCover() throws DisconnectException;

	public URL getAlbumCoverURL() throws DisconnectException;

	public List<String> getListStations() throws DisconnectException;

	public List<String> getListSongs() throws DisconnectException;

	public List<CommandStationList> getListAll() throws DisconnectException;

	public void setVolume(double percentage) throws DisconnectException;

}
