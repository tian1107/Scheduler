package schedule.timing;

/**
 * A 24-hour representation of time higher bits: hour, lower bits: minutes
 * 
 * @author Ian Christian Fernandez
 *
 */
public class Time implements Comparable<Time>
{
	private int	time;

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

	public void setHour(int hour)
	{
		this.time = (hour & 0xff) << 8 | (getMinute() & 0xff);
	}

	public int getMinute()
	{
		return time & 0xff;
	}

	public void setMinute(int minute)
	{
		this.time = (getHour() & 0xff) << 8 | (minute & 0xff);
	}

	protected int getValue()
	{
		return time;
	}

	@Override
	public String toString()
	{
		return String.format("%02d:%02d", getHour(), getMinute());
	}

	public String to12HourString()
	{
		return String.format("%d:%02d%s", getHour() % 12 != 0 ? getHour() % 12
				: 12, getMinute(), getHour() / 12 > 0 ? "pm" : "am");
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof Time)) return false;
		return this.time == ((Time) obj).time;
	}

	@Override
	public int hashCode()
	{
		return time;
	}

	@Override
	public int compareTo(Time o)
	{
		return this.time - o.time;
	}
}
