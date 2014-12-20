package schedule.classes;

public interface Enlistable {
	
	/**
	 * Should return whether this class is counted as this course
	 * @param course the course to check
	 * @return if this class is counted as the course
	 */
	public boolean doesContainCourse(Course course);
	
	/**
	 * 
	 * @param e 
	 * @return true if two classes are the same, that is, same course/s and same time
	 */
	public boolean isEquivalentTo(Enlistable e);
	
	/**
	 * 
	 * @return a priori probability that this will be received by the student
	 */
	public float getProbability();
	
	/**
	 * Check if these two enlistables conflict. equivalent enlistables conflict
	 * @param e
	 * @return
	 */
	public boolean doesConflictWith(Enlistable e);
	
	/**
	 * 
	 * @return The number of enlistment slots this enlistable takes.
	 */
	public int numEnlistingSlots();
}
