package com.gmail.icbfernandez2012.scheduler.schedule.database.aggregator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gmail.icbfernandez2012.scheduler.schedule.classes.Section;

public class LocalClassAggregator implements ClassAggregator
{
	private HashMap<String, Document>	files;

	public LocalClassAggregator(String[] files)
	{
		this.files = new HashMap<String, Document>();

		for (String file : files)
		{
			addFile(file);
		}
	}

	public void addFile(String file)
	{
		try
		{
			this.files.put(file, Jsoup.parse(new File(file), "utf-8", ""));
		} catch (IOException e)
		{
			System.err.println("Error on file: " + file);
		}
	}

	public void removeFile(String file)
	{
		files.remove(file);
	}

	@Override
	public ArrayList<Section> getCourseEnlistables(String course)
	{
		ArrayList<Section> sections = new ArrayList<Section>();
		boolean needsMerge = false;
		
		for (Document x : files.values())
		{
			Elements classes = x.select("#tbl_schedule tbody tr");

			Iterator<Element> iter = classes.iterator();
			while (iter.hasNext())
			{
				Element current = iter.next();

				Section newSection = new Section(current);
				// TODO For multiple blocks, temp solution
				int blocks = Integer.parseInt(current.child(0).attr("rowspan"));
				while (--blocks > 0)
					iter.next();

				if (newSection.doesContainCourse(course))
				{
					if (newSection.getUnits() <= 0.0f)
					{
						needsMerge = true;
					}

					sections.add(newSection);
				}
			}
		}

		if (needsMerge)
		{
			ArrayList<Section> retainList = new ArrayList<Section>();

			for (Section subsection : sections)
			{
				for (Section mainSection : sections)
				{
					if (subsection.isSubsectionOf(mainSection))
					{
						subsection.mergeSection(mainSection);
						retainList.add(subsection);
						break;
					}
				}
			}

			return retainList;
		}
		else
		{
			return sections;
		}
	}

}
