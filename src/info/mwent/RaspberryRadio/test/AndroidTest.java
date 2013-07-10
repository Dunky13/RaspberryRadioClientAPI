package info.mwent.RaspberryRadio.test;

import info.mwent.RaspberryRadio.client.AndroidClient;
import info.mwent.RaspberryRadio.client.Exceptions.ConnectionException;
import info.mwent.RaspberryRadio.client.Exceptions.LoginException;

public class AndroidTest
{
	AndroidClient ac;
	String host = "mwent.info";
	int port = 6584;
	String username = "root";
	String password = "Admin";

	public static void main(String[] args)
	{
		new AndroidTest().start();
	}

	public AndroidTest()
	{
		ac = new AndroidClient(host, port);
		try
		{
			ac.connect(username, password);
		}
		catch (LoginException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ConnectionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void start()
	{
		System.out.println(ac.listAll());
		System.out.println(ac.getUpdate());
	}

}
