package info.mwent.RaspberryRadio.shared;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class CommandStationList implements Serializable
{

	private static final long serialVersionUID = -2308330680911403400L;

	@SerializedName("name")
	private String _name;
	@SerializedName("host")
	private String _host;
	@SerializedName("position")
	private int _pos;

	public CommandStationList(String name, String host, int position)
	{
		_name = name;
		_host = host;
		_pos = position;
	}

	public String getName()
	{
		return _name;
	}

	public String getHost()
	{
		return _host;
	}

	public int getPos()
	{
		return _pos;
	}
}
