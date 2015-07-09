package com.gmail.icbfernandez2012.scheduler.schedule.classes;

import java.util.ArrayList;

import org.jsoup.nodes.Element;

import com.gmail.icbfernandez2012.scheduler.schedule.timing.Timeslot;

/**
 * 
 * @author Ian Christian Fernandez
 *
 */
public class Section extends Enlistable
{
	private ArrayList<Timeslot>	times;
	private String				course;
	private String				section;
	private int					id;
	private float				units;
	private int					demand;
	private int					available;
	private boolean				overbook;
	private boolean				dissolve;
	private int					subBlocks;

	public Section(Element fromHtml)
	{
		times = new ArrayList<Timeslot>();
		id = Integer.parseInt(fromHtml.child(0).html());
		String[] sectionFull = fromHtml.child(1).html().split("(?<!\\G\\S+)\\s");
		course = sectionFull[0];
		section = sectionFull[1];
		units = Float.parseFloat(fromHtml.child(2).html());
		dissolve = false;
		overbook = false;

		subBlocks = Integer.parseInt(fromHtml.child(0).attr("rowspan"));

		Element noBlock = fromHtml;
		if (subBlocks > 1) noBlock = fromHtml.parent().child(fromHtml.elementSiblingIndex() + subBlocks - 1);

		String[] timeStrings = fromHtml.child(3).html().split("<br>")[0].split(";");

		for (int i = 0; i < timeStrings.length; i++)
		{
			times.add(new Timeslot(timeStrings[i].trim()));
		}

		if (fromHtml.child(3).text().toLowerCase().contains("dissolve")) dissolve = true;

		String avString = "";
		if (subBlocks > 1)
		{
			avString = noBlock.child(1).select("strong").html();
		}
		else
			avString = fromHtml.child(5).select("strong").html();

		try
		{
			available = Integer.parseInt(avString);
		} catch (NumberFormatException ex)
		{
			available = 0;
		}

		if (avString.contains("OVERBOOKED")) overbook = true;
		if (avString.contains("DISSOLVED")) dissolve = true;

		demand = 0;
		if (!avString.contains("DISSOLVED"))
		{
			if (subBlocks > 1)
				demand = Integer.parseInt(noBlock.child(2).html());
			else
				demand = Integer.parseInt(fromHtml.child(6).html());
		}
	}

	public void mergeSection(Section s)
	{
		if (isSubsectionOf(s))
		{
			this.times.addAll(s.times);
			this.units += s.units;
		}
	}

	public boolean isSubsectionOf(Section s)
	{
		if (!s.course.equalsIgnoreCase(this.course)) return false;
		if (s.section.equalsIgnoreCase(this.section)) return false;

		if (this.section.startsWith(s.section)) return true;

		return false;
	}

	@Override
	public float getProbability()
	{
		if (demand < available)
			return 1.0f;
		else
			return (float) available / (demand + 1);
	}

	@Override
	public boolean doesContainCourse(String name)
	{
		return course.equalsIgnoreCase(name);
	}

	@Override
	public boolean isEquivalentTo(Enlistable e)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int numEnlistingSlots()
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String toString()
	{

		String value = String.format("%d %s(%.1f): %s, (%d)(%d) %s %s\n", id, course, units, section, available,
				demand, overbook ? "Overbooked" : "", dissolve ? "Dissolved" : "");

		for (Timeslot time : times)
		{
			value += time.toString() + "\n";
		}

		return value;
	}

	public String getTimeString()
	{
		String value = "";
		for (Timeslot time : times)
			value += time.toShortString() + "; ";
		return value;
	}

	@Override
	public ArrayList<Timeslot> getTimes()
	{
		return times;
	}

	public String getCourse()
	{
		return course;
	}

	public int getId()
	{
		return id;
	}

	public String getSection()
	{
		return section;
	}

	public float getUnits()
	{
		return units;
	};

	public boolean isOverbooked()
	{
		return overbook;
	}

	public boolean isDissolved()
	{
		return dissolve;
	}

	@Override
	public boolean isEqualTo(Enlistable e)
	{
		return e instanceof Section && ((Section) e).id == this.id;
	}

	@Override
	public String[] getCourses()
	{
		return new String[] { course };
	}
}
