package gui;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
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
		
		shellList = new Shell(display);
		
		shellList.setLayout(new GridLayout(3, false));
		shellList.setSize(800, 600);
		
		shellList.setText("Scheduler");
		
		clTable = new ClassListTable(shellList, SWT.NONE);
		clTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite control = new Composite(shellList, SWT.NONE);
		control.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		control.setLayout(new GridLayout(1, false));
		
		Button addAll = new Button(control, SWT.NONE);
		addAll.setText(">>");
		addAll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Button add = new Button(control, SWT.NONE);
		add.setText(">");
		add.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Button remove = new Button(control, SWT.NONE);
		remove.setText("<");
		remove.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Button removeAll = new Button(control, SWT.NONE);
		removeAll.setText("<<");
		removeAll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		control.pack();
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
		//shellSched.setVisible(false);
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
