package schedule.database;

import java.util.ArrayList;

import schedule.classes.Enlistable;

public interface ClassAggregator {
	
	public ArrayList<Enlistable> getCourseEnlistables(String course);
}
