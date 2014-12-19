package schedule;

import java.util.ArrayList;

/**
 * Sections of the same course that have the same timeslot, essentially they are the same
 * @author Ian Christian
 *
 */
public class Multisection extends Section {
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
}
