package info.mwent.RaspberryRadio.shared;

import java.io.Serializable;
import java.util.Arrays;
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

	public CommandAPI getCommand()
	{
		return _command;
	}

	public Object[] getValues()
	{
		return _values;
	}

	public void setValues(Object[] _values)
	{
		this._values = _values;
	}

	@Override
	public String toString()
	{
		return _command + " - " + Arrays.asList(_values);
	}

}
