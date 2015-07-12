package com.gmail.icbfernandez2012.scheduler.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration
{
	public static final Configuration	INSTANCE	= new Configuration();

	Properties							props;

	public Configuration()
	{
		props = new Properties();
		reload();
	}

	public void reload()
	{
		File file = new File("./cache/config.ini");
		InputStream input = null;

		if (!file.exists())
		{
			fillProperties();
			return;
		}

		try
		{
			input = new FileInputStream(file);
			props.load(input);
			props.setProperty("cacheInvalidationTime", "36000");
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (input != null) try
			{
				input.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void fillProperties()
	{
		// props.setProperty("lastCache",
		// String.valueOf(System.currentTimeMillis() / 1000L));
		props.setProperty("lastCache", "0");
		props.setProperty("cacheInvalidationTime", "36000");
		props.setProperty("list", "");
		props.setProperty("selList", "");
	}

	public Properties getProperties()
	{
		return props;
	}

	public void save()
	{
		File file = new File("./cache/config.ini");
		file.getParentFile().mkdirs();
		if (!file.exists()) try
		{
			file.createNewFile();
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}

		OutputStream out = null;
		try
		{
			out = new FileOutputStream(file);
			props.store(out, "Scheduler v1.0");
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			if (out != null)
			{
				try
				{
					out.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
