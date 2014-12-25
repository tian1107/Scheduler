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
			Document x = Jsoup.parse(new File("Z:/scheduler/test/eee.html"), "utf-8", "");
			Elements classes = x.select("#tbl_schedule tbody tr");
			
			Iterator<Element> iter = classes.iterator();
			while(iter.hasNext())
			{
				sections.add(new Section(iter.next()));
			}
			
		}
	}
	
	//Testing
	public static void main(String [] args) throws Exception
	{
		ClassList list = new ClassList(new String[]{"EEE 35"});
	}
}
