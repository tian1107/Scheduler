package gui;
import java.io.IOException;

import org.agilemore.agilegrid.Cell;
import org.agilemore.agilegrid.ISelectionChangedListener;
import org.agilemore.agilegrid.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import schedule.classes.Section;
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

	private Text conflictLabel;
	private Text conflictLabel2;
	private Text indivLabel;
	private Text fullLabel;

	private ClassList cList;
	
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
		
		shellList.setLayout(new GridLayout(1, false));
		
		shellList.setText("Scheduler");
		
		Composite selection = new Composite(shellList, SWT.NONE);
		selection.setLayout(new GridLayout(3, false));
		selection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		clTable = new ClassListTable(selection, SWT.NONE);
		
		GridData clTableData = new GridData(SWT.FILL, SWT.FILL, true, true);
		clTableData.minimumHeight = 300;
		clTableData.widthHint = 300;
		clTable.setLayoutData(clTableData);
		clTable.table.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				for(Cell cell : event.getNewSelections())
				{
					String subject = (String) clTable.table.getContentAt(cell.row, ClassListTable.Columns.Subject.getIndex());
					String section = (String) clTable.table.getContentAt(cell.row, ClassListTable.Columns.Section.getIndex());
					
					Section selected = cList.getSection(subject, section);
					conflictLabel.setText(selected.toString());
				}
			}
		});
		
		Composite control = new Composite(selection, SWT.NONE);
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
		
		list = new List(selection, SWT.BORDER);
		GridData listData = new GridData(SWT.FILL, SWT.FILL, true, true);
		listData.widthHint = 300;
		list.setLayoutData(listData);
		list.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(list.getSelectionCount() > 0)
				{
					Section section = cList.getSection(list.getSelection()[0]);
					conflictLabel2.setText(section.toString() + "\nConflicts with");
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("Here!");
				
			}
		});
		
		Composite calc = new Composite(shellList, SWT.NONE);
		calc.setLayout(new GridLayout(2, true));
		calc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Group conflict = new Group(calc, SWT.NONE);
		conflict.setText("Conflicts");
		conflict.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		conflict.setLayout(new FillLayout());
		
		conflictLabel = new Text(conflict, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL);
		conflictLabel.setText("\n\n\n\n");
		
		Group conflict2 = new Group(calc, SWT.NONE);
		conflict2.setText("Conflicts");
		conflict2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		conflict2.setLayout(new FillLayout());
		
		conflictLabel2 = new Text(conflict2, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL);
		conflictLabel2.setText("\n\n\n\n");
		
		Group indiv = new Group(calc, SWT.NONE);
		indiv.setText("Section Probabilities");
		indiv.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		indiv.setLayout(new FillLayout());
		
		indivLabel = new Text(indiv, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL);
		indivLabel.setText("\n\n\n\n");
		
		Group full = new Group(calc, SWT.NONE);
		full.setText("Subject Probabilities");
		full.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		full.setLayout(new FillLayout());
		
		fullLabel = new Text(full, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL);
		fullLabel.setText("\n\n\n\n");
		
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
		this.cList = list;
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
			list = new ClassList(new String[]{"EEE 23", "eee 52", "eee 35"});
			list.removeUseless();
			fa.setClassList(list);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		fa.start();
	}
}
