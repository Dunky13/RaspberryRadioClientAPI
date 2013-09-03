package info.mwent.RaspberryRadio.client.exceptions;

public class DisconnectException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8928030050498685515L;

	public DisconnectException()
	{
	}

	public DisconnectException(String message)
	{
		super(message);
	}

	public DisconnectException(Throwable cause)
	{
		super(cause);
	}

	public DisconnectException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
