package com.experitest.auto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.testng.annotations.*;

import com.experitest.client.*;

public class BaseTest
{
	protected Client client = null;
	protected GridClient gridClient = null;
	protected Properties cloudProperties = new Properties();

	public void init(String deviceQuery, String testName) throws Exception
	{
		initCloudProperties();
		String accessKey = getProperty("accessKey", cloudProperties);
//		if (accessKey != null && !accessKey.isEmpty())
//		{
//			gridClient = new GridClient(accessKey, getProperty("url", cloudProperties));
//		}
//		else
		{
			gridClient = new GridClient(getProperty("username", cloudProperties),
					getProperty("password", cloudProperties), getProperty("project", cloudProperties),
					getProperty("url", cloudProperties));
		}
		client = gridClient.lockDeviceForExecution(testName, adhocDevice(deviceQuery), 30, 300000);
		File reporterDir = new File(System.getProperty("user.dir"), "reports");
		reporterDir.mkdirs();
		client.setReporter("xml", reporterDir.getAbsolutePath(), testName);
	}

	protected String getProperty(String property, Properties props) throws FileNotFoundException, IOException
	{
		if (System.getProperty(property) != null)
		{
			return System.getProperty(property);
		}
		else if (System.getenv().containsKey(property))
		{
			return System.getenv(property);
		}
		else if (props != null)
		{
			return props.getProperty(property);
		}
		return null;
	}

	private String adhocDevice(String deviceQuery)
	{
		try
		{
			File adhocProperties = new File(getMobileDataFolder(), "adhoc.properties");
			if (adhocProperties.exists())
			{
				Properties prop = new Properties();
				FileReader reader = new FileReader(adhocProperties);
				try
				{
					prop.load(reader);
				}
				finally
				{
					reader.close();
				}
				return "@serialnumber='" + prop.getProperty("serial") + "'";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return deviceQuery;
	}

	private void initCloudProperties() throws FileNotFoundException, IOException
	{
		FileReader fr = new FileReader("cloud.properties");
		cloudProperties.load(fr);
		fr.close();
	}

	@After
	@AfterMethod
	public void tearDown()
	{
		// Generates a report of the test case.
		// For more information -
		// https://docs.experitest.com/display/public/SA/Report+Of+Executed+Test
		client.generateReport(false);
		// Releases the client so that other clients can approach the agent in
		// the near future.
		client.releaseClient();
	}

	public static File getMobileDataFolder()
	{
		String OS = System.getProperty("os.name").toUpperCase();
		File jarLocation;
		if (OS.contains("WIN"))
		{
			jarLocation = new File(System.getenv("APPDATA"), ".mobiledata");
		}
		else
		{
			jarLocation = new File(System.getProperty("user.home") + "/Library/Application " + "Support",
					".mobiledata");
		}
		return jarLocation;
	}

}
