package com.gmail.icbfernandez2012.scheduler.gui;

import org.agilemore.agilegrid.AgileGrid;
import org.agilemore.agilegrid.ColumnSortComparator;
import org.agilemore.agilegrid.ColumnSortOnClick;
import org.agilemore.agilegrid.DefaultCompositorStrategy;
import org.agilemore.agilegrid.DefaultLayoutAdvisor;
import org.agilemore.agilegrid.ICompositorStrategy;
import org.agilemore.agilegrid.IContentProvider;
import org.agilemore.agilegrid.ILayoutAdvisor;
import org.agilemore.agilegrid.SWTX;
import org.agilemore.agilegrid.samples.ScalableColumnContentProvider;
import org.agilemore.agilegrid.samples.SortableCellRendererProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.gmail.icbfernandez2012.scheduler.schedule.classes.Section;
import com.gmail.icbfernandez2012.scheduler.schedule.database.ClassList;

public class ClassListTable extends Composite
{

	public enum Columns
	{
		Code(0, "Code"), Subject(1, "Subject"), Section(2, "Section"), Time(3,
				"Time"), Remarks(4, "Remarks"), Probability(5, "Probability"), End(
				6, "");

		private int		index;
		private String	title;

		private Columns(int index, String title)
		{
			this.index = index;
			this.title = title;
		}

		public int getIndex()
		{
			return index;
		}

		public String getTitle()
		{
			return title;
		}

		public static Columns getColumn(int index)
		{
			for (Columns c : Columns.values())
				if (c.index == index) return c;
			return null;
		}
	};

	// public static final String [] titles = {"Code", "Subject", "Section",
	// "Time", "Remarks", "Probability"};

	protected ClassList	list;

	protected AgileGrid	table;

	public ClassListTable(Composite parent, int style)
	{
		super(parent, style);

		list = new ClassList(new String[] {});

		setLayout(new FillLayout());
		table = new AgileGrid(this, SWT.V_SCROLL | SWT.H_SCROLL
				| SWTX.ROW_SELECTION | SWT.NO_REDRAW_RESIZE);
		table.setContentProvider(new ScalableColumnContentProvider());

		ILayoutAdvisor layoutAdvisor = new ClassListTableLayoutAdvisor(this);
		final ICompositorStrategy compositorStrategy = new DefaultCompositorStrategy(
				layoutAdvisor);
		layoutAdvisor.setCompositorStrategy(compositorStrategy);

		table.setLayoutAdvisor(layoutAdvisor);
		table.setCellRendererProvider(new SortableCellRendererProvider(table));

		table.addMouseListener(new ColumnSortOnClick(table,
				new ColumnSortComparator(table, Integer.MIN_VALUE,
						ColumnSortComparator.SORT_NONE) {

					@Override
					public int doCompare(Object o1, Object o2, int row1,
							int row2)
					{
						int col = getColumnToSortOn();

						if (col == Columns.Code.getIndex())
							return Integer.parseInt((String) o1)
									- Integer.parseInt((String) o2);
						else if (col == Columns.Subject.getIndex()
								|| col == Columns.Section.getIndex())
							return ((String) o1).compareTo((String) o2);
						else if (col == Columns.Probability.getIndex())
							return ((int) (Float.parseFloat((String) o1) - Float
									.parseFloat((String) o2)));
						return 0;
					}
				}));
	}

	public void setList(ClassList list)
	{
		this.list = list;

		table.setRedraw(false);
		IContentProvider provider = table.getContentProvider();

		int i = 0;
		for (Section section : list.getList())
		{
			provider.setContentAt(i, Columns.Code.getIndex(),
					((Integer) section.getId()).toString());
			provider.setContentAt(i, Columns.Subject.getIndex(),
					section.getCourse());
			provider.setContentAt(i, Columns.Section.getIndex(),
					section.getSection());
			provider.setContentAt(i, Columns.Time.getIndex(),
					section.getTimeString());
			provider.setContentAt(i, Columns.Probability.getIndex(),
					String.format("%.3f", section.getProbability() * 100));
			i++;
		}
		AgileGridUtils.resizeAllColumnsOptimal(table);
		table.setRedraw(true);
	}
}

class ClassListTableLayoutAdvisor extends DefaultLayoutAdvisor
{
	protected ClassListTable	clt;

	public ClassListTableLayoutAdvisor(ClassListTable clt)
	{
		super(clt.table);
		this.clt = clt;
	}

	@Override
	public int getColumnCount()
	{
		return ClassListTable.Columns.End.getIndex();
	}

	@Override
	public int getRowCount()
	{
		return clt.list.getList().size();
	}

	@Override
	public String getTopHeaderLabel(int col)
	{
		return ClassListTable.Columns.getColumn(col).getTitle();
	}

	@Override
	public boolean isLeftHeaderVisible()
	{
		return false;
	}

	@Override
	public boolean isColumnResizable(int col)
	{
		return false;
	}

	@Override
	public boolean isRowResizable(int row)
	{
		return false;
	}
}
