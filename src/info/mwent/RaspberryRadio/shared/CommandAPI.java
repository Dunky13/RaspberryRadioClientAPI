package info.mwent.RaspberryRadio.shared;

import java.io.Serializable;

public enum CommandAPI implements Serializable
{
	ADD,
	ADD_ERROR,
	BYE,
	CURRENT,
	DELETE,
	DELETE_ERROR,
	ERROR,
	HANDSHAKE,
	LIST,
	SONG_LIST,
	STATION_LIST,
	LOGIN,
	LOGIN_ERROR,
	LOGIN_SUCCESSFULL,
	NEXT,
	PLAY,
	PLAYING,
	PLAYING_ERROR,
	PREV,
	SPECIFIC,
	SPECIFIC_ERROR,
	STOP,
	VOLUME,
	VOLUME_ERROR;

	public static CommandAPI fromString(String command)
	{
		if (command != null)
		{
			for (CommandAPI c : CommandAPI.values())
			{
				if (command.equalsIgnoreCase(c.toString()))
				{
					return c;
				}
			}
		}
		return ERROR;
	}
}