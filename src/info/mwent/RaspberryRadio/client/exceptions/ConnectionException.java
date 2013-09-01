package info.mwent.RaspberryRadio.client.exceptions;

public class ConnectionException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -151230255470129727L;

	public ConnectionException()
	{
	}

	public ConnectionException(String message)
	{
		super(message);
	}

	public ConnectionException(Throwable cause)
	{
		super(cause);
	}

	public ConnectionException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
