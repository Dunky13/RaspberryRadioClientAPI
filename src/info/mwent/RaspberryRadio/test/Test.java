package info.mwent.RaspberryRadio.test;

import info.mwent.RaspberryRadio.client.GoogleResults;
import info.mwent.RaspberryRadio.client.GoogleResults.Result;
import java.util.List;
import com.google.gson.Gson;

public class Test
{

	String apiKey = "74f59adde6c42de6d01a96a1ba104c67";

	String pls = "http://yp.shoutcast.com/sbin/tunein-station.pls?id=108900";
	String m3u = "http://dir.xiph.org/listen/825543/listen.m3u";
	String mp3 = "http://goo.gl/6Ie5H";

	//SECRET: 561b5085ef3f183d
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		new Test();
	}

	@SuppressWarnings("unused")
	private void start()
	{
		//		String s = ClientAPI.getImageFromGoogle("anouk - birds");
		String s = "";
		System.out.println(s);
		GoogleResults res = new Gson().fromJson(s, GoogleResults.class);
		List<Result> results = res.getResponseData().getResults();
		for (Result result : results)
		{
			if (result.isSized(300, 300))
			{
				System.out.println(result.getUrl());
				break;
			}
		}
		//		System.out.println(res.toString());

	}
}
