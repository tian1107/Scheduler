package gui;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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

	public static void main (String [] args)
	{
		Display display = new Display();
		
		// Shell shell = new Shell(display, SWT.SHELL_TRIM & (~SWT.RESIZE));
		Shell shell = new Shell(display);
		
		shell.setLayout(new GridLayout(1, false));
		shell.setSize(800, 600);
		
		shell.setText("Scheduler");
		
		Shell shell2 = new Shell(display);
		
		shell2.setLayout(new GridLayout(1, false));
		shell2.setSize(800, 600);
		
		shell2.setText("Scheduler");
		
		// Shell can be used as container
		Composite upper = new Composite(shell, SWT.NONE);
		upper.setLayout(new GridLayout(2, false));
		upper.setLayoutData(new GridData(SWT.FILL, SWT.UP, true, false));
		
		Label label = new Label(upper, SWT.NONE);
		label.setText("This is a label:");
		label.setToolTipText("This is the tooltip of this label");

		Text text = new Text(upper, SWT.NONE);
		text.setText("This is the text in the text widget");
		text.setLayoutData(new GridData(SWT.FILL, SWT.UP, true, false));
		
//		Browser browser = new Browser(shell, SWT.NONE);
//		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		ScheduleTable table = new ScheduleTable(shell, SWT.NONE);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		ClassListTable clTable = new ClassListTable(shell2, SWT.NONE);
		clTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		ClassList list;
		
		try {
			list = new ClassList(new String[]{"EEE 35", "EEE 23", "eee 52", "eee 51"});
			list.removeUseless();
			table.setSections(list.getList());
			clTable.setList(list);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		clTable.pack();
		upper.pack();
		table.pack();
		
//		try {
//			//Document x = Jsoup.connect("file:///Z:/scheduler/test/eee.html").get();
//			Document x = Jsoup.parse(new File("Z:/scheduler/test/eee.html"), "utf-8", "");			
//			browser.setText("<table>" + x.select("#tbl_schedule tbody tr").toString() + "</table>");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		shell.open();
		shell2.open();
		//shell.setVisible(false);
		while((!shell.isDisposed() && shell.isVisible()) || (!shell2.isDisposed() && shell2.isVisible()))
		{
			if(!display.readAndDispatch())
				display.sleep();
		}
		
		if(!shell.isDisposed())
			shell.dispose();
		if(!shell2.isDisposed())
			shell2.dispose();
		display.dispose();
	}
}
