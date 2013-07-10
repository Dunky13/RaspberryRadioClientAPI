package info.mwent.RaspberryRadio.client.Exceptions;

import info.mwent.RaspberryRadio.shared.Commands;

public class CommandCombo
{

	private Commands _original;
	private Commands _response;

	public CommandCombo(Commands original, Commands response)
	{
		_original = original;
		_response = response;
	}

	public Commands get_original()
	{
		return _original;
	}

	public Commands get_response()
	{
		return _response;
	}

}
