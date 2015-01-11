package gui;

import java.io.IOException;

import org.agilemore.agilegrid.AgileGrid;
import org.agilemore.agilegrid.ColumnSortComparator;
import org.agilemore.agilegrid.ColumnSortOnClick;
import org.agilemore.agilegrid.DefaultCompositorStrategy;
import org.agilemore.agilegrid.DefaultContentProvider;
import org.agilemore.agilegrid.DefaultLayoutAdvisor;
import org.agilemore.agilegrid.ICompositorStrategy;
import org.agilemore.agilegrid.ILayoutAdvisor;
import org.agilemore.agilegrid.SWTX;
import org.agilemore.agilegrid.samples.SortableCellRendererProvider;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import schedule.classes.Section;
import schedule.database.ClassList;

public class ClassListTable extends Composite{
	
	public static final String [] titles = {"Subject", "Section", "Time", "Remarks", "Probability"};
	
	protected ClassList list;
	
	protected AgileGrid table;
	
	public ClassListTable(Composite parent, int style) {
		super(parent, style);
		
		try {
			list = new ClassList(new String[]{});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setLayout(new FillLayout());
		table = new AgileGrid(this, SWTX.AUTO_SCROLL | SWTX.ROW_SELECTION);
		table.setContentProvider(new ClassListTableContentProvider(this));
		
		ILayoutAdvisor layoutAdvisor = new ClassListTableLayoutAdvisor(this);
		final ICompositorStrategy compositorStrategy = new DefaultCompositorStrategy(
				layoutAdvisor);
		layoutAdvisor.setCompositorStrategy(compositorStrategy);
		
		table.setLayoutAdvisor(layoutAdvisor);
		table.setCellRendererProvider(new SortableCellRendererProvider(table));
		
		table.addMouseListener(new ColumnSortOnClick(table, new ColumnSortComparator(table, Integer.MIN_VALUE, ColumnSortComparator.SORT_NONE) {
			
			@Override
			public int doCompare(Object o1, Object o2, int row1, int row2) {
				int col = getColumnToSortOn();
				
				if(col == 0 || col == 1)
					return ((String) o1).compareTo((String) o2);
				else if(col == 4)
					return ((int) (Float.parseFloat((String) o1) - Float.parseFloat((String) o2)));
				return 0;
			}
		}));
	}
	
	public void setList(ClassList list) {
		this.list = list;
	}

}

class ClassListTableLayoutAdvisor extends DefaultLayoutAdvisor
{
	protected ClassListTable clt;
	
	public ClassListTableLayoutAdvisor(ClassListTable clt) {
		super(clt.table);
		this.clt = clt;
	}
	
	@Override
	public int getColumnCount() {
		return ClassListTable.titles.length;
	}
	
	@Override
	public int getRowCount() {
		return clt.list.getList().size();
	}
	
	@Override
	public String getTopHeaderLabel(int col) {
		return ClassListTable.titles[col];
	}
	
	@Override
	public boolean isLeftHeaderVisible() {
		return false;
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
	public int getColumnWidth(int col) {
		int properWidth =  (clt.table.getClientArea().width);
		if(clt.table.getVerticalBar().isVisible())
			properWidth -= clt.table.getVerticalBar().getSize().x;
		
		properWidth /= ClassListTable.titles.length;
		return properWidth;
	}
	
	@Override
	public String getTooltip(int row, int col) {
		return col == 0 ? "Conflicts with ..." : null;
	}
}

class ClassListTableContentProvider extends DefaultContentProvider
{
	protected ClassListTable clt;
	
	public ClassListTableContentProvider(ClassListTable clt) {
		this.clt = clt;
	}
	
	@Override
	public Object doGetContentAt(int row, int col) {
		Section select = clt.list.getList().get(row);
		switch(col)
		{
		case 0:
			return select.getCourse();
		case 1:
			return select.getSection();
		case 2:
			return select.getTimeString();
		case 4:
			return String.format("%.3f", select.getProbability() * 100);
		}
		
		return "";
	}
}
