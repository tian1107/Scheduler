package schedule.classes;

/**
 * 
 * @author Ian Christian Fernandez
 *
 */
public abstract class Enlistable {
	
	/**
	 * Should return whether this class is counted as this course
	 * @param course the course to check
	 * @return if this class is counted as the course
	 */
	public abstract boolean doesContainCourse(String name);
	
	/**
	 * 
	 * @param e 
	 * @return true if two classes are the same, that is, same course/s and same time
	 */
	public abstract boolean isEquivalentTo(Enlistable e);
	
	/**
	 * 
	 * @param e
	 * @return true if two classes are the very same, that is, equivalent, and same section
	 */
	public abstract boolean isEqualTo(Enlistable e);
	
	/**
	 * 
	 * @return a priori probability that this will be received by the student
	 */
	public abstract float getProbability();
	
	/**
	 * Check if these two enlistables conflict. equivalent enlistables conflict
	 * @param e
	 * @return
	 */
	public abstract boolean doesConflictWith(Enlistable e);
	
	/**
	 * 
	 * @return The number of enlistment slots this enlistable takes.
	 */
	public abstract int numEnlistingSlots();
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		
		if(!(obj instanceof Enlistable)) return false;
		
		return isEqualTo((Enlistable) obj);
	}
}
