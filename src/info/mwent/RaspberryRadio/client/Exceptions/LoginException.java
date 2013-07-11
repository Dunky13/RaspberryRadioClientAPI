package info.mwent.RaspberryRadio.client.exceptions;

public class LoginException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2037220022577398165L;

	public LoginException()
	{
	}

	public LoginException(String message)
	{
		super(message);
	}

	public LoginException(Throwable cause)
	{
		super(cause);
	}

	public LoginException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
