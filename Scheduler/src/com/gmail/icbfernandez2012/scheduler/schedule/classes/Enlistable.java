package com.gmail.icbfernandez2012.scheduler.schedule.classes;

import java.util.Collection;

import com.gmail.icbfernandez2012.scheduler.schedule.timing.Timeslot;

/**
 * 
 * @author Ian Christian Fernandez
 *
 */
public abstract class Enlistable
{

	/**
	 * Should return whether this class is counted as this course
	 * 
	 * @param course
	 *            the course to check
	 * @return if this class is counted as the course
	 */
	public abstract boolean doesContainCourse(String name);

	/**
	 * 
	 * @param e
	 * @return true if two classes are the same, that is, same course/s and same
	 *         time
	 */
	public abstract boolean isEquivalentTo(Enlistable e);

	/**
	 * 
	 * @param e
	 * @return true if two classes are the very same, that is, equivalent, and
	 *         same section
	 */
	public abstract boolean isEqualTo(Enlistable e);

	/**
	 * 
	 * @return a priori probability that this will be received by the student
	 */
	public abstract float getProbability();

	/**
	 * Check if these two enlistables conflict. equivalent enlistables conflict
	 * 
	 * @param e
	 * @return
	 */
	public boolean doesConflictWith(Enlistable e)
	{
		// Check if the same
		if (e.equals(this)) return false;

		// Check if times are conflicting
		for (Timeslot s : e.getTimes())
		{
			for (Timeslot t : getTimes())
			{
				if (s.doesIntersectWith(t)) return true;
			}
		}

		// Check if subjects are the same
		for (String s : e.getCourses())
		{
			for (String t : getCourses())
			{
				if (s.equalsIgnoreCase(t)) return true;
			}
		}

		return false;
	}

	/**
	 * 
	 * @return The number of enlistment slots this enlistable takes.
	 */
	public abstract int numEnlistingSlots();

	public abstract Collection<Timeslot> getTimes();

	public abstract String[] getCourses();

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if (obj == this) return true;

		if (!(obj instanceof Enlistable)) return false;

		return isEqualTo((Enlistable) obj);
	}
}
