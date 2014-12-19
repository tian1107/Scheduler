import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class FirstApplication {

	public static void main (String [] args)
	{
		Display display = new Display();
		
		Shell shell = new Shell(display);
		
		shell.setLayout(new GridLayout(1, false));
		shell.setSize(800, 600);
		
		shell.setText("Scheduler");
		
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
		text.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		text.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
		
		Browser browser = new Browser(shell, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		// set widgets size to their preferred size
		text.pack();
		label.pack(); 
		
		upper.pack();
		
		try {
			//Document x = Jsoup.connect("file:///Z:/scheduler/test/eee.html").get();
			Document x = Jsoup.parse(new File("Z:/scheduler/test/eee.html"), "utf-8", "");			
			browser.setText("<table>" + x.select("#tbl_schedule tbody tr").toString() + "</table>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		shell.open();
		while(!shell.isDisposed())
		{
			if(!display.readAndDispatch())
				display.sleep();
		}
		
		display.dispose();
	}
}
