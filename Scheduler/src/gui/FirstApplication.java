package gui;
import java.io.IOException;

import org.agilemore.agilegrid.Cell;
import org.agilemore.agilegrid.CellRow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import schedule.database.ClassList;

/**
 * 
 * @author Ian Christian Fernandez
 *
 */
public class FirstApplication {
	
	protected Display display;
	
	protected Shell shellSched;
	protected Shell shellList;

	protected ScheduleTable scTable;
	protected ClassListTable clTable;
	protected List list;
	
	public FirstApplication()
	{
		display = new Display();
		createShellList();
		createShellSched();
	}
	
	private void createShellList()
	{
		if(display == null) return;
		
		if(display.isDisposed()) return;
		
		shellList = new Shell(display, SWT.SHELL_TRIM | (SWT.RESIZE));
		
		shellList.setLayout(new GridLayout(3, false));
		
		shellList.setText("Scheduler");
		
		clTable = new ClassListTable(shellList, SWT.NONE);
		
		GridData clTableData = new GridData(SWT.FILL, SWT.FILL, true, true);
		clTableData.minimumHeight = 300;
		clTableData.widthHint = 300;
		clTable.setLayoutData(clTableData);
		
		Composite control = new Composite(shellList, SWT.NONE);
		control.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		control.setLayout(new GridLayout(1, false));
		
		Button moveUp = new Button(control, SWT.NONE);
		moveUp.setText("ª");
		moveUp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		moveUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(e.button == 1 && list.getSelectionCount() > 0)
				{
					int select = list.getSelectionIndex();
					if(select > 0)
					{
						String upper = list.getItem(select - 1);
						String lower = list.getItem(select);
						list.setItem(select - 1, lower);
						list.setItem(select, upper);
						list.setSelection(select - 1);
					}
				}
			}
		});
		moveUp.setToolTipText("Move Item Up");
		
		Button add = new Button(control, SWT.NONE);
		add.setText(">");
		add.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		add.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(e.button == 1)
				{
					Cell selected = clTable.table.getCellSelection()[0];
					if(selected.row > -1 && selected.row < clTable.table.getLayoutAdvisor().getRowCount())
					{
						String toAdd = (String) clTable.table.getContentAt(selected.row, ClassListTable.Columns.Subject.getIndex())
								+ " " + (String) clTable.table.getContentAt(selected.row, ClassListTable.Columns.Section.getIndex());
						if (list.indexOf(toAdd) < 0)
							list.add(toAdd);
					}
				}
			}
		});
		add.setToolTipText("Add to List");
		
		Button remove = new Button(control, SWT.NONE);
		remove.setText("<");
		remove.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		remove.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(e.button == 1 && list.getSelectionCount() > 0)
				{
					list.remove(list.getSelectionIndex());
				}
			}
		});
		remove.setToolTipText("Remove from List");
		
		Button moveDown = new Button(control, SWT.NONE);
		moveDown.setText("«");
		moveDown.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		moveDown.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(e.button == 1 && list.getSelectionCount() > 0)
				{
					int select = list.getSelectionIndex();
					if(select < list.getItemCount() - 1)
					{
						String upper = list.getItem(select);
						String lower = list.getItem(select + 1);
						list.setItem(select, lower);
						list.setItem(select + 1, upper);
						list.setSelection(select + 1);
					}
				}
			}
		});
		moveDown.setToolTipText("Move Item Down");
		
		Button removeAll = new Button(control, SWT.NONE);
		removeAll.setText("<<");
		removeAll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		removeAll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(e.button == 1)
				{
					list.removeAll();
				}
			}
		});
		removeAll.setToolTipText("Remove All");

		control.pack();
		
		list = new List(shellList, SWT.BORDER);
		GridData listData = new GridData(SWT.FILL, SWT.FILL, true, true);
		listData.widthHint = 300;
		list.setLayoutData(listData);
		
		shellList.pack();
	}
	
	private void createShellSched()
	{
		shellSched = new Shell(display);
		
		shellSched.setLayout(new GridLayout(1, false));
		shellSched.setSize(800, 600);
		
		shellSched.setText("Scheduler");
		
		Composite upper = new Composite(shellSched, SWT.NONE);
		upper.setLayout(new GridLayout(2, false));
		upper.setLayoutData(new GridData(SWT.FILL, SWT.UP, true, false));
		
		Label label = new Label(upper, SWT.NONE);
		label.setText("This is a label:");
		label.setToolTipText("This is the tooltip of this label");

		Text text = new Text(upper, SWT.NONE);
		text.setText("This is the text in the text widget");
		text.setLayoutData(new GridData(SWT.FILL, SWT.UP, true, false));
				
		scTable = new ScheduleTable(shellSched, SWT.NONE);
		scTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}
	
	public void setClassList(ClassList list)
	{
		scTable.setSections(list.getList());
		clTable.setList(list);
	}
	
	public void start()
	{
		shellSched.open();
		shellList.open();
		shellList.setMaximized(true);
		shellSched.dispose();
		while((!shellSched.isDisposed() && shellSched.isVisible()) || (!shellList.isDisposed() && shellList.isVisible()))
		{
			if(!display.readAndDispatch())
				display.sleep();
		}
		
		if(!shellSched.isDisposed())
			shellSched.dispose();
		if(!shellList.isDisposed())
			shellList.dispose();
		display.dispose();
	}

	public static void main (String [] args)
	{
		FirstApplication fa = new FirstApplication();
		
		
		ClassList list;
		try {
			list = new ClassList(new String[]{"EEE 35", "EEE 23", "eee 52", "eee 51"});
			list.removeUseless();
			fa.setClassList(list);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		fa.start();
	}
}
