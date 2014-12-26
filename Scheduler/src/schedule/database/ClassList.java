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

public class ClassList {

	protected ArrayList<Section> sections;
	
	public ClassList(String [] shortNames) throws IOException
	{
		sections = new ArrayList<Section>();
		
		for(String s : shortNames)
		{
			ArrayList<Section> courseSections = new ArrayList<Section>();
			
			Document x = Jsoup.parse(new File("Z:/scheduler/test/eee.html"), "utf-8", "");
			Elements classes = x.select("#tbl_schedule tbody tr");
			
			boolean needsMerge = false;
			
			Iterator<Element> iter = classes.iterator();
			while(iter.hasNext())
			{
				Section newSection = new Section(iter.next());
				
				if(newSection.doesContainCourse(s))
				{
					if(newSection.getUnits() <= 0.0f)
					{
						needsMerge = true;
					}
					courseSections.add(newSection);
				}
			}
			
			if (needsMerge)
			{
				ArrayList<Section> retainList = new ArrayList<Section>();
				
				for(Section subsection: courseSections)
				{
					for(Section mainSection: courseSections)
					{
						if(subsection.isSubsectionOf(mainSection))
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
				sections.addAll(courseSections);
			
		}
		
		for(Section finalSections: sections)
			System.out.println(finalSections);
	}
	
	//Testing
	public static void main(String [] args) throws Exception
	{
		ClassList list = new ClassList(new String[]{"EEE 35", "EEE 23", "eee 52"});
	}
}
