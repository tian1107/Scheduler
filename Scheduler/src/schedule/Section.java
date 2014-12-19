package schedule;

import java.util.ArrayList;

import schedule.timing.Timeslot;

public class Section {
	private ArrayList<Timeslot> times;
	private String course;
	private String section;
	private int id;
	private int demand;
	private int available;
	
	public Section()
	{
		
	}
	
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
	
	/**
	 * Compares if the sections are of the same course, and of the same time
	 * @param s the section to be compared to
	 * @return true if they are equivalent
	 */
	public boolean isEquivalent(Section s)
	{
		return false;
	}
}
