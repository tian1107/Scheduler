package schedule.timing;

/**
 * This is 
 * @author Ian Christian
 *
 */
public class Timeslot {
	
	private byte days;	//most signficant bit not used; S M T W Th F S order
	private Time from;
	private Time to;
	
	public Timeslot(String parse)
	{
		days = 0;
		from = new Time(0, 0);
		to = new Time (0, 0);
	}
	
	public boolean doesIntersectWith(Timeslot timeslot)
	{
		//Check if they are in the same day
		if((this.days & timeslot.days) != 0)
		{
			//Check for intersection
			return (this.from.getValue() <= timeslot.to.getValue() && this.to.getValue() >= timeslot.from.getValue());  
		}
		
		return false;
	}
}
