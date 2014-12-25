package schedule.classes;

import java.util.ArrayList;

/**
 * Sections of the same course that have the same timeslot, essentially they are the same
 * @author Ian Christian Fernandez
 *
 */
public class Multisection implements Enlistable {
	private ArrayList<Section> sections;
	
	public Multisection(Section s)
	{
		sections = new ArrayList<Section>();
		sections.add(s);
	}
	
	public boolean addSection(Section s)
	{
		
		return false;
	}
	
	@Override
	public float getProbability() {
		float notProbability = 1.0f;
		for(Section s: sections)
		{
			notProbability *= (1 - s.getProbability());
		}
		
		return 1 - notProbability;
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
		return sections.size();
	}
}
