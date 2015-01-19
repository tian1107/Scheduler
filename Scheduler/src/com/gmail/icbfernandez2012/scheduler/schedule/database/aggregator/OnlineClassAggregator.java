package com.gmail.icbfernandez2012.scheduler.schedule.database.aggregator;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.gmail.icbfernandez2012.scheduler.configuration.Configuration;
import com.gmail.icbfernandez2012.scheduler.schedule.classes.Section;

public class OnlineClassAggregator implements ClassAggregator, FilenameFilter
{
	private LocalClassAggregator	aggregator;
	long							lastUpdate;
	long							time;

	public OnlineClassAggregator()
	{
		reloadCache();
	}

	private void reloadCache()
	{
		File dir = new File("cache/");
		aggregator = new LocalClassAggregator(dir.listFiles(this));
		lastUpdate = Long.parseLong(Configuration.INSTANCE.getProperties().getProperty("lastCache", "0"));
		time = Long.parseLong(Configuration.INSTANCE.getProperties().getProperty("cacheInvalidationTime", "3600"));
	}

	@Override
	public ArrayList<Section> getCourseEnlistables(String course)
	{
		if ((Calendar.getInstance().getTimeInMillis() / 1000L) - lastUpdate > time) update();

		String main = course.split(" ")[0];

		if (!aggregator.files.containsKey(main + ".html")) try
		{
			getMain(main);
		} catch (IOException e)
		{
			e.printStackTrace();
		}

		return aggregator.getCourseEnlistables(course);
	}

	public void update()
	{
		System.err.println("Updated! Connected to the internet!");
		lastUpdate = Calendar.getInstance().getTimeInMillis() / 1000L;
		Configuration.INSTANCE.getProperties().setProperty("lastCache", String.valueOf(lastUpdate));

		Set<String> files = aggregator.files.keySet();
		for (String file : files)
		{
			try
			{
				getMain(file.substring(0, file.lastIndexOf('.')));
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void getMain(String main) throws IOException
	{
		if (main.isEmpty()) return;

		System.err.println("New main!" + main);
		Connection.Response html = Jsoup.connect(getBaseLocation(Calendar.getInstance()) + main).execute();

		if (html.statusCode() != 200) return;

		File toWrite = new File("cache/" + main + ".html");

		FileWriter write = new FileWriter(toWrite);
		write.write(html.body());
		write.close();

		aggregator.addFile("cache/" + main + ".html");
	}

	@Override
	public boolean accept(File dir, String name)
	{
		return name.endsWith(".html");
	}

	/**
	 * Enlistment periods: <br />
	 * Midyear : May - June (counted on previous year)<br />
	 * 1st semester : July - November<br />
	 * 2nd semester : December - April<br />
	 * 
	 * @return base location of stuff
	 */
	public static String getBaseLocation(Calendar current)
	{
		int year = current.get(Calendar.YEAR);
		int month = current.get(Calendar.MONTH);
		int semester = 1;
		switch (month)
		{
			case Calendar.MAY:
			case Calendar.JUNE:
				semester = 3;
				year--;
				break;
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.SEPTEMBER:
			case Calendar.OCTOBER:
			case Calendar.NOVEMBER:
				semester = 1;
				break;
			case Calendar.DECEMBER:
				year++;
			case Calendar.JANUARY:
			case Calendar.FEBRUARY:
			case Calendar.MARCH:
			case Calendar.APRIL:
				year--;
				semester = 2;
				break;
		}
		return String.format("http://127.0.0.1:8000/schedule/1%d%d/", year, semester);
	}
}
