package schedule.classes;

import java.util.ArrayList;

import org.jsoup.nodes.Element;

import schedule.timing.Timeslot;

/**
 * 
 * @author Ian Christian Fernandez
 *
 */
public class Section implements Enlistable{
	private ArrayList<Timeslot> times;
	private String course;
	private String section;
	private int id;
	private float units;
	private int demand;
	private int available;
	
	public Section(Element fromHtml)
	{
		times = new ArrayList<Timeslot>();
		id = Integer.parseInt(fromHtml.child(0).html());
		String [] sectionFull = fromHtml.child(1).html().split("(?<!\\G\\S+)\\s");
		course = sectionFull[0];
		section = sectionFull[1];
		units = Float.parseFloat(fromHtml.child(2).html());
		
		String [] timeStrings = fromHtml.child(3).html().split("<br>")[0].split(";");
		
		for(int i = 0; i < timeStrings.length; i++)
		{
			times.add(new Timeslot(timeStrings[i].trim()));
		}
		
		System.out.printf("%d %s(%.1f): %s\n", id, course, units, section);
		
		for(Timeslot time : times)
		{
			System.out.println(time);
		}
	}
	
	@Override
	/**
	 * Calculates the a priori probability that this section would be enlisted to the student  
	 * @return the probability between 0.0f and 1.0f
	 */
	public float getProbability()
	{
		if(demand < available)
			return 1.0f;
		else
			return available / demand;
	}

	@Override
	public boolean doesContainCourse(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEquivalentTo(Enlistable e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doesConflictWith(Enlistable e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int numEnlistingSlots() {
		// TODO Auto-generated method stub
		return 1;
	}
	
	@Override
	public String toString() {
		
		return course + section;
	};
}
