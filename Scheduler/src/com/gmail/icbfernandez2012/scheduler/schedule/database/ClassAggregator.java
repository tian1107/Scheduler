package com.gmail.icbfernandez2012.scheduler.schedule.database;

import java.util.ArrayList;

import com.gmail.icbfernandez2012.scheduler.schedule.classes.Enlistable;

public interface ClassAggregator
{

	public ArrayList<Enlistable> getCourseEnlistables(String course);
}
