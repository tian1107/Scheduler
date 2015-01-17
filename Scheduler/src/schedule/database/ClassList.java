package schedule.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import schedule.classes.Section;

public class ClassList
{

	protected ArrayList<Section>	sections;

	public ClassList(String[] shortNames) throws IOException
	{
		sections = new ArrayList<Section>();

		for (String s : shortNames)
		{
			ArrayList<Section> courseSections = new ArrayList<Section>();

			Document x = Jsoup.parse(new File("Z:/scheduler/test/eee.html"),
					"utf-8", "");
			Elements classes = x.select("#tbl_schedule tbody tr");

			boolean needsMerge = false;

			Iterator<Element> iter = classes.iterator();
			while (iter.hasNext())
			{
				Element current = iter.next();

				Section newSection = new Section(current);
				// TODO For multiple blocks, temp solution
				int blocks = Integer.parseInt(current.child(0).attr("rowspan"));
				while (--blocks > 0)
					iter.next();

				if (newSection.doesContainCourse(s))
				{
					if (newSection.getUnits() <= 0.0f)
					{
						needsMerge = true;
					}

					courseSections.add(newSection);
				}
			}

			if (needsMerge)
			{
				ArrayList<Section> retainList = new ArrayList<Section>();

				for (Section subsection : courseSections)
				{
					for (Section mainSection : courseSections)
					{
						if (subsection.isSubsectionOf(mainSection))
						{
							subsection.mergeSection(mainSection);
							retainList.add(subsection);
							break;
						}
					}
				}

				sections.addAll(retainList);
			}
			else
			{
				sections.addAll(courseSections);
			}
		}
	}

	public void removeUseless()
	{
		Iterator<Section> i = sections.iterator();
		while (i.hasNext())
		{
			Section current = i.next();
			if (current.getProbability() <= 0.0f)
				i.remove();
			else if (current.isDissolved()) i.remove();
		}
	}

	public ArrayList<Section> getList()
	{
		return sections;
	}

	public Section getSection(String title)
	{
		String[] strings = title.split(" ");
		return getSection(strings[0] + " " + strings[1], strings[2]);
	}

	public Section getSection(String subject, String section)
	{
		for (Section s : sections)
		{
			if (s.getCourse().equalsIgnoreCase(subject)
					&& s.getSection().equalsIgnoreCase(section)) return s;
		}
		return null;
	}

	// Testing
	public static void main(String[] args) throws Exception
	{
		ClassList list = new ClassList(new String[] { "EEE 35", "EEE 23",
				"eee 52" });
		list.removeUseless();

		for (Section finalSections : list.sections)
		{
			System.out.println(finalSections);
		}
	}
}
