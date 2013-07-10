package info.mwent.RaspberryRadio.client;

import java.util.List;

public class GoogleResults
{

	private ResponseData responseData;

	public ResponseData getResponseData()
	{
		return responseData;
	}

	public void setResponseData(ResponseData responseData)
	{
		this.responseData = responseData;
	}

	public String toString()
	{
		return "ResponseData[" + responseData + "]";
	}

	public static class ResponseData
	{
		private List<Result> results;

		public List<Result> getResults()
		{
			return results;
		}

		public void setResults(List<Result> results)
		{
			this.results = results;
		}

		public String toString()
		{
			return "Results[" + results + "]";
		}
	}

	public static class Result
	{
		private String url;
		private String title;
		private int width;
		private int height;

		public String getUrl()
		{
			return url;
		}

		public String getTitle()
		{
			return title;
		}

		public int getWidth()
		{
			return width;
		}

		public int getHeight()
		{
			return height;
		}

		public void setUrl(String url)
		{
			this.url = url;
		}

		public void setTitle(String title)
		{
			this.title = title;
		}

		public void setWidth(int width)
		{
			this.width = width;
		}

		public void setHeight(int height)
		{
			this.height = height;
		}

		public String toString()
		{
			return "Result\r\n\turl:" + url + "\r\n\twidth:" + width + "\r\n\theight:" + height + "\r\n\ttitle:" + title + "]";
		}

		public boolean isSized(int width, int height)
		{
			return this.width == width && this.height == height;
		}

		public boolean isSquared()
		{
			return this.width == this.height;
		}
	}

}