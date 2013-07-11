package info.mwent.RaspberryRadio;

import info.mwent.RaspberryRadio.client.exceptions.ConnectionException;
import info.mwent.RaspberryRadio.client.exceptions.LoginException;
import info.mwent.RaspberryRadio.shared.CommandStationList;
import java.net.URL;
import java.util.List;

public interface API
{

	public void connect(String username, String password) throws LoginException, ConnectionException;

	public void connect(String username, String password, int timeOut) throws LoginException, ConnectionException;

	public void disconnect();

	public boolean isConnected();

	public boolean isPlaying();

	public void enableAlbumCovers();

	public boolean isAlbumCoversEnabled();

	public String play();

	public String stop();

	public String next();

	public String prev();

	public String getCurrent();

	public String add(String name, String url);

	public String add(String url);

	public String setStation(int station);

	public String removeStation(int station);

	public String getAlbumCover();

	public URL getAlbumCoverURL();

	public List<String> getListStations();

	public List<String> getListSongs();

	public List<CommandStationList> getListAll();

	public void setVolume(double percentage);

}
