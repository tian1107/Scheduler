package com.gmail.icbfernandez2012.scheduler.schedule.timing;

/**
 * This is
 * 
 * @author Ian Christian Fernandez
 *
 */
public class Timeslot
{

	public static final byte		SUNDAY			= 64;
	public static final byte		MONDAY			= 32;
	public static final byte		TUESDAY			= 16;
	public static final byte		WEDNESDAY		= 8;
	public static final byte		THURSDAY		= 4;
	public static final byte		FRIDAY			= 2;
	public static final byte		SATURDAY		= 1;

	public static final String[]	dayNames		= { "Unknown", "Sunday", "Monday", "Tuesday", "Wednesday",
			"Thursday", "Friday", "Saturday"		};
	public static final String[]	dayShortNames	= { "Err", "Su", "M", "T", "W", "Th", "F", "S" };

	private byte					days;
	private Time					from;
	private Time					to;

	public Timeslot(String parse)
	{
		parse.trim();
		String[] parts = parse.split(" ", 2);

		days = 0;
		from = new Time(0);
		to = new Time(0);

		if (parts.length != 2) return;

		days = 0;
		dayParse(parts[0]);
		timeParse(parts[1]);
	}

	public boolean doesIntersectWith(Timeslot timeslot)
	{
		// Check if they are in the same day
		if ((this.days & timeslot.days) != 0)
		{
			// Check for intersection
			return (this.from.getValue() < timeslot.to.getValue() && this.to.getValue() > timeslot.from.getValue());
		}

		return false;
	}

	private void dayParse(String days)
	{
		days = days.toLowerCase() + " ";
		for (int i = 0; i < days.length(); i++)
		{
			char current = days.charAt(i);

			if (current == 'm')
			{
				this.days |= MONDAY;
			}
			else if (current == 't')
			{
				if (days.charAt(++i) == 'h')
				{
					this.days |= THURSDAY;
				}
				else
				{
					this.days |= TUESDAY;
					i--;
				}
			}
			else if (current == 'w')
			{
				this.days |= WEDNESDAY;
			}
			else if (current == 'f')
			{
				this.days |= FRIDAY;
			}
			else if (current == 's')
			{
				if (days.charAt(++i) == 'u')
				{
					this.days |= SUNDAY;
				}
				else
				{
					this.days |= SATURDAY;
					i--;
				}
			}
		}
	}

	private void timeParse(String time)
	{
		from = new Time(0, 0);
		to = new Time(0, 0);

		time = time.toLowerCase().trim();
		int state = 0;
		boolean sameMeridiem = true;
		for (int i = 0; i < time.length(); i++)
		{
			char current = time.charAt(i);
			if (state == 0) // Starting time's hour
			{
				if (Character.isDigit(current))
				{
					from.setHour(from.getHour() * 10 + current - '0');
				}
				else if (current == ':')
					state = 1; // get the minutes next
				else if (current == '-')
				{
					state = 10; // get the final time next
					sameMeridiem = true;
				}
				else
				{
					sameMeridiem = false;
					state = 2; // get the meridiem next
					i--;
				}
			}
			else if (state == 1) // Starting time's minute
			{
				if (Character.isDigit(current))
				{
					from.setMinute(from.getMinute() * 10 + current - '0');
				}
				else if (current == '-')
				{
					sameMeridiem = true;
					state = 10; // get the final time next
				}
				else
				{
					sameMeridiem = false;
					state = 2; // get meridiem
					i--;
				}
			}
			else if (state == 2)
			{
				if (current == 'p' && from.getHour() <= 12)
					from.setHour((from.getHour() + 12) % 24);
				else if (current == '-') state = 10;
			}
			else if (state == 10) // final time's hour
			{
				if (Character.isDigit(current))
				{
					to.setHour(to.getHour() * 10 + current - '0');
				}
				else if (current == ':')
					state = 11; // get the minutes next
				else
				{
					state = 12; // get meridiem next
					i--;
				}
			}
			else if (state == 11) // final time's minute
			{
				if (Character.isDigit(current))
				{
					to.setMinute(to.getMinute() * 10 + current - '0');
				}
				else
				{
					state = 12; // get meridiem
					i--;
				}
			}
			else if (state == 12)
			{
				if (current == 'p')
				{
					if (to.getHour() < 12) to.setHour((to.getHour() + 12) % 24);
					if (sameMeridiem && from.getHour() < 12) from.setHour((from.getHour() + 12) % 24);
				}
			}
		}
	}

	@Override
	public String toString()
	{
		String value = "";
		for (int i = 7; i >= 0; i--)
		{
			if ((days & 1 << i) != 0) value += dayNames[7 - i] + " ";
		}

		value += from.toString() + " to " + to.toString();

		return value;
	}

	public String toShortString()
	{
		String value = "";
		for (int i = 7; i >= 0; i--)
		{
			if ((days & 1 << i) != 0) value += dayShortNames[7 - i];
		}

		value += " " + from.to12HourString() + "-" + to.to12HourString();

		return value;
	}

	public byte getDays()
	{
		return days;
	}

	public Time getFrom()
	{
		return from;
	}

	public Time getTo()
	{
		return to;
	}
}
