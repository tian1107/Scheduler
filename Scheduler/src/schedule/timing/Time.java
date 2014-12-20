package schedule.timing;

/**
 * A 24-hour representation of time
 * higher bits: hour, lower bits: minutes
 * @author Ian Christian Fernandez
 *
 */
public class Time {
	private int time;
	
	public Time(int time)
	{
		this.time = time;
	}
	
	public Time(int hour, int minute)
	{
		this.time = (hour & 0xff) << 8 | (minute & 0xff);
	}
	
	public int getHour()
	{
		return time >> 8;
	}
	
	public int getMinute()
	{
		return time & 0xff;
	}
	
	protected int getValue()
	{
		return time;
	}
}
