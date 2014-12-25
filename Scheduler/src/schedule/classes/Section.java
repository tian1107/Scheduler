package schedule.classes;

import java.util.ArrayList;

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
	private int demand;
	private int available;
	
	public Section()
	{
		
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
}
