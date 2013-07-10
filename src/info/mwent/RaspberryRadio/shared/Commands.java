package info.mwent.RaspberryRadio.shared;

import java.io.Serializable;
import com.google.gson.annotations.SerializedName;

public class Commands implements Serializable
{
	private static final long serialVersionUID = -3935201425160686084L;

	@SerializedName("command")
	private CommandAPI _command;
	@SerializedName("values")
	private Object[] _values;

	public Commands(CommandAPI c, Object... values)
	{
		_command = c;
		_values = values;
	}

	public CommandAPI get_command()
	{
		return _command;
	}

	public Object[] get_values()
	{
		return _values;
	}

	public void set_values(Object[] _values)
	{
		this._values = _values;
	}

}
