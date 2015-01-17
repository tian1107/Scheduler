package com.gmail.icbfernandez2012.scheduler.schedule.database.aggregator;

import java.util.ArrayList;

import com.gmail.icbfernandez2012.scheduler.schedule.classes.Section;

public interface ClassAggregator
{

	public ArrayList<Section> getCourseEnlistables(String course);
}
