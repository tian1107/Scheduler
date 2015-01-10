package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.agilemore.agilegrid.AgileGrid;
import org.agilemore.agilegrid.DefaultContentProvider;
import org.agilemore.agilegrid.DefaultLayoutAdvisor;
import org.agilemore.agilegrid.SWTX;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import schedule.classes.Section;
import schedule.timing.Time;
import schedule.timing.Timeslot;

public class ScheduleTable extends Composite {
	protected static final String[] titles = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	
	protected Collection<Section> sections;
	protected Set<Time> times;
	protected ArrayList<String> timeHeaders;
	protected String [][] stringTable;
	
	protected AgileGrid aTable;
	
	protected int [] rowHeights;
	protected int [] columnWidths;
	
	public ScheduleTable(Composite parent, int style)
	{
		super(parent, style); 
		sections = new ArrayList<Section>();
		times = new TreeSet<Time>();
		timeHeaders = new ArrayList<String>();
		
		setLayout(new FillLayout());
		
		aTable = new AgileGrid(this, SWT.MULTI | SWTX.AUTO_SCROLL | SWTX.COLUMN_SELECTION | SWT.NO_REDRAW_RESIZE);
		//aTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		aTable.setLayoutAdvisor(new ScheduleTableLayoutAdvisor(this));
		aTable.setContentProvider(new ScheduleTableContentAdvisor(this));
		
		aTable.pack();
	}
	
	public void setSections(Collection<Section> sections)
	{
		this.sections = sections;
		
		times.clear();
		for(Section i: sections)
		{
			for(Timeslot s: i.getTimes())
			{
				times.add(s.getFrom());
				times.add(s.getTo());
			}
		}
		
		if(times.isEmpty())
			return;
		
		timeHeaders.clear();
		Iterator<Time> iter = times.iterator();
		Time last = iter.next();
		while(iter.hasNext())
		{
			Time current = iter.next();
			timeHeaders.add(last.toString() + "-" + current.toString());
			last = current;
		}
		
		rowHeights = new int[timeHeaders.size()];
		Arrays.fill(rowHeights, 15);
		columnWidths = new int[titles.length];
		GC gc = new GC(aTable);
		
		ArrayList<Time> aTimes = new ArrayList<Time>(times);
		
		stringTable = new String[titles.length][aTimes.size()];
		
		for(Section i: sections)
		{
			for(Timeslot s: i.getTimes())
			{
				int start = aTimes.indexOf(s.getFrom());
				int end = aTimes.indexOf(s.getTo());
				for(int t = start; t < end; t++)
				{
					//TableItem current = table.getItem(t);
					for(int d = Timeslot.SUNDAY, q = 0; d >= Timeslot.SATURDAY; d /= 2, q++)
					{
						if(stringTable[q][t] == null)
							stringTable[q][t] = new String();
						
						if((s.getDays() & d) != 0)
						{
							stringTable[q][t] += i.getCourse() + " " + i.getSection() + "\n";
							Point dim = gc.textExtent(stringTable[q][t]);
							if(dim.x > columnWidths[q])
							{
								columnWidths[q] = dim.x;
							}
							if(dim.y > rowHeights[t])
							{
								rowHeights[t] = dim.y;
							}
						}
					}
				}
			}
		}
		
		gc.dispose();
	}
}

class ScheduleTableLayoutAdvisor extends DefaultLayoutAdvisor
{
	private ScheduleTable st;
	
	public ScheduleTableLayoutAdvisor(ScheduleTable st) {
		super(st.aTable);
		this.st = st;
		
	}
	
	@Override
	public String getTopHeaderLabel(int col) {
		return ScheduleTable.titles[col];
	}
	
	@Override
	public boolean isColumnResizable(int col) {
		return false;
	}
	
	@Override
	public boolean isRowResizable(int row) {
		return false;
	}
	
	@Override
	public int getColumnCount() {
		return ScheduleTable.titles.length;
	}
	
	@Override
	public int getRowCount() {
		return st.timeHeaders.size();
	}
	
	@Override
	public int getLeftHeaderWidth() {
		return 75;
	}
	
	@Override
	public int getRowHeight(int row) {
		return st.rowHeights[row];
	}
	
	@Override
	public int getColumnWidth(int col) {
		if(st.columnWidths[col] > 0)
			return st.columnWidths[col] + 10;
		return 0;
	}
	
	@Override
	public String getLeftHeaderLabel(int row) {
		return st.timeHeaders.get(row);
	}
}

class ScheduleTableContentAdvisor extends DefaultContentProvider
{
	private ScheduleTable st;
	
	public ScheduleTableContentAdvisor(ScheduleTable st)
	{
		this.st = st;
	}
	
	@Override
	public Object getContentAt(int row, int col) {
		return st.stringTable[col][row];
	}
}