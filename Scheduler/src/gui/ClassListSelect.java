package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ClassListSelect extends Dialog {

	protected String[] selected;
	private List list;
	private Text text;
	
	public ClassListSelect(Shell parent, int style) {
		super(parent, style);
		selected = new String[0];
	}

	public String[] open()
	{	
		Shell parent = getParent();
		Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Subject List");
		shell.setLayout(new  GridLayout(1, false));
		
		Composite upper = new Composite(shell, SWT.NONE);
		upper.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		upper.setLayout(new GridLayout(4, false));
		
		Label label = new Label(upper, SWT.NONE);
		label.setText("Subject: ");
		
		text = new Text(upper, SWT.BORDER | SWT.SINGLE);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		Button add = new Button(upper, SWT.NONE);
		add.setText("Add Subject");
		add.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(e.button == 1)
				{
					if(list.indexOf(text.getText()) < 0)
					{
						list.add(text.getText());
						text.setText("");
						selected = list.getItems();
					}
				}
			}
		});
		
		Button remove = new Button(upper, SWT.NONE);
		remove.setText("Remove Selected");
		remove.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				if(e.button == 1 && list.getSelectionCount() > 0)
				{
					list.remove(list.getSelectionIndex());
					selected = list.getItems();
				}
			}
		});
		
		list = new List(shell, SWT.BORDER);
		GridData listData = new GridData(SWT.FILL, SWT.FILL, true, true);
		listData.widthHint = 300;
		listData.heightHint = 300;
		list.setLayoutData(listData);
		list.setItems(selected);
		
		shell.pack();
		
		shell.open();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}

		return selected;
	}
}
