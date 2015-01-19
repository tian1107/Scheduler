package com.gmail.icbfernandez2012.scheduler.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.agilemore.agilegrid.Cell;
import org.agilemore.agilegrid.ISelectionChangedListener;
import org.agilemore.agilegrid.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.jsoup.helper.StringUtil;

import com.gmail.icbfernandez2012.scheduler.configuration.Configuration;
import com.gmail.icbfernandez2012.scheduler.schedule.classes.Section;
import com.gmail.icbfernandez2012.scheduler.schedule.database.ClassList;

/**
 * 
 * @author Ian Christian Fernandez
 *
 */
public class FirstApplication
{

	protected Display			display;

	protected Shell				shellSched;
	protected Shell				shellList;

	protected ScheduleTable		scTable;
	protected ClassListTable	clTable;
	protected List				list;
	protected ClassListSelect	listSelect;

	private Text				conflictLabel;
	private Text				conflictLabel2;
	private Text				indivLabel;
	private Text				fullLabel;

	private ClassList			cList;

	public FirstApplication()
	{
		display = new Display();

		createShellList();
		createShellSched();

		cList = new ClassList(new String[] {});
		load();
	}

	private void load()
	{
		String[] lastList = Configuration.INSTANCE.getProperties()
				.getProperty("list", "").split(";");
		String[] secList = Configuration.INSTANCE.getProperties()
				.getProperty("selList", "").split(";");
		setClassList(lastList);
		list.setItems(secList);
		while (list.indexOf("") > -1)
			list.remove("");
		listSelect.selected = lastList;
		updateSubjectProbabilities();
	}

	private void createShellList()
	{
		if (display == null) return;

		if (display.isDisposed()) return;

		shellList = new Shell(display, SWT.SHELL_TRIM | (SWT.RESIZE));

		shellList.setLayout(new GridLayout(1, false));

		shellList.setText("Scheduler");

		shellList.setMinimumSize(800, 600);

		shellList.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e)
			{
				save();
			}
		});

		Composite selection = new Composite(shellList, SWT.NONE);
		selection.setLayout(new GridLayout(3, false));
		selection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		clTable = new ClassListTable(selection, SWT.NONE);

		GridData clTableData = new GridData(SWT.FILL, SWT.FILL, true, true);
		clTableData.widthHint = 300;
		clTableData.heightHint = 240;
		clTable.setLayoutData(clTableData);
		clTable.table
				.addSelectionChangedListener(new ISelectionChangedListener() {

					@Override
					public void selectionChanged(SelectionChangedEvent event)
					{
						updateConflictsLeft();
					}
				});

		Composite control = new Composite(selection, SWT.NONE);
		control.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));
		control.setLayout(new GridLayout(1, false));

		Button moveUp = new Button(control, SWT.NONE);
		moveUp.setText("ª");
		moveUp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		moveUp.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (list.getSelectionCount() > 0)
				{
					int select = list.getSelectionIndex();
					if (select > 0)
					{
						String upper = list.getItem(select - 1);
						String lower = list.getItem(select);
						list.setItem(select - 1, lower);
						list.setItem(select, upper);
						list.setSelection(select - 1);
						updateSubjectProbabilities();
					}
				}
			}
		});
		moveUp.setToolTipText("Move Item Up");

		Button add = new Button(control, SWT.NONE);
		add.setText(">");
		add.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (clTable.table.getCellSelection().length < 1) return;

				Cell selected = clTable.table.getCellSelection()[0];
				if (selected.row > -1
						&& selected.row < clTable.table.getLayoutAdvisor()
								.getRowCount())
				{
					String toAdd = (String) clTable.table.getContentAt(
							selected.row,
							ClassListTable.Columns.Subject.getIndex())
							+ " "
							+ (String) clTable.table.getContentAt(selected.row,
									ClassListTable.Columns.Section.getIndex());
					if (list.indexOf(toAdd) < 0)
					{
						list.add(toAdd);
						updateSubjectProbabilities();
					}
				}
			}
		});
		add.setToolTipText("Add to List");

		Button remove = new Button(control, SWT.NONE);
		remove.setText("<");
		remove.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		remove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (list.getSelectionCount() > 0)
				{
					list.remove(list.getSelectionIndex());
					updateSubjectProbabilities();
				}
			}
		});
		remove.setToolTipText("Remove from List");

		Button moveDown = new Button(control, SWT.NONE);
		moveDown.setText("«");
		moveDown.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		moveDown.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (list.getSelectionCount() > 0)
				{
					int select = list.getSelectionIndex();
					if (select < list.getItemCount() - 1)
					{
						String upper = list.getItem(select);
						String lower = list.getItem(select + 1);
						list.setItem(select, lower);
						list.setItem(select + 1, upper);
						list.setSelection(select + 1);
						updateSubjectProbabilities();
					}
				}
			}
		});
		moveDown.setToolTipText("Move Item Down");

		Button removeAll = new Button(control, SWT.NONE);
		removeAll.setText("<<");
		removeAll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		removeAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				list.removeAll();
				updateSubjectProbabilities();
			}
		});
		removeAll.setToolTipText("Remove All");

		control.pack();

		list = new List(selection, SWT.BORDER);
		GridData listData = new GridData(SWT.FILL, SWT.FILL, true, true);
		listData.widthHint = 300;
		listData.heightHint = 240;
		list.setLayoutData(listData);
		list.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e)
			{
				if (list.getSelectionCount() > 0)
				{
					updateConflictsRight();
				}
			}
		});

		Composite calc = new Composite(shellList, SWT.NONE);
		calc.setLayout(new GridLayout(2, true));
		calc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Group conflict = new Group(calc, SWT.NONE);
		conflict.setText("Conflicts");
		GridData conflictData = new GridData(SWT.FILL, SWT.FILL, true, true);
		conflictData.heightHint = 60;
		conflict.setLayoutData(conflictData);
		conflict.setLayout(new FillLayout());

		conflictLabel = new Text(conflict, SWT.MULTI | SWT.READ_ONLY
				| SWT.V_SCROLL);
		conflictLabel.setText("\n\n\n\n\n");

		Group conflict2 = new Group(calc, SWT.NONE);
		conflict2.setText("Conflicts");
		conflict2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		conflict2.setLayout(new FillLayout());

		conflictLabel2 = new Text(conflict2, SWT.MULTI | SWT.READ_ONLY
				| SWT.V_SCROLL);
		conflictLabel2.setText("\n\n\n\n\n");

		Group indiv = new Group(calc, SWT.NONE);
		indiv.setText("Section Probabilities");
		GridData indivData = new GridData(SWT.FILL, SWT.FILL, true, true);
		indivData.heightHint = 60;
		indiv.setLayoutData(indivData);
		indiv.setLayout(new FillLayout());

		indivLabel = new Text(indiv, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL);
		indivLabel.setText("\n\n\n\n\n");

		Group full = new Group(calc, SWT.NONE);
		full.setText("Subject Probabilities");
		full.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		full.setLayout(new FillLayout());

		fullLabel = new Text(full, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL);
		fullLabel.setText("\n\n\n\n\n");

		listSelect = new ClassListSelect(shellList, SWT.NONE);

		Button subjectSelect = new Button(calc, SWT.NONE);
		subjectSelect.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				false));
		subjectSelect.setText("Edit Subject List");
		subjectSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				setClassList(listSelect.open());
			}
		});

		shellList.setDefaultButton(subjectSelect);
		shellList.pack();

		updateSubjectProbabilities();
	}

	private void save()
	{
		Configuration.INSTANCE.getProperties().setProperty("list",
				StringUtil.join(Arrays.asList(listSelect.selected), ";"));
		Configuration.INSTANCE.getProperties().setProperty("selList",
				StringUtil.join(Arrays.asList(list.getItems()), ";"));
		Configuration.INSTANCE.save();
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

	private void updateSubjectProbabilities()
	{
		String secText = "";

		HashMap<String, Float> subProb = new HashMap<String, Float>();
		HashSet<String> subjects = new HashSet<String>();
		for (int index = 0; index < list.getItemCount(); index++)
		{
			String title = list.getItem(index);
			Section s = cList.getSection(title);
			subjects.add(s.getCourse());
			float posteriori = 1.0f;
			for (int c = 0; c < index; c++)
			{
				String cTitle = list.getItem(c);
				Section con = cList.getSection(cTitle);

				if (con.doesConflictWith(s))
				{
					posteriori *= 1 - con.getProbability();
				}
			}
			posteriori *= s.getProbability();
			secText += String.format("%s: %.3f%%\n", title, posteriori * 100);
			if (subProb.containsKey(s.getCourse()))
			{
				subProb.put(s.getCourse(), subProb.get(s.getCourse())
						+ posteriori);
			}
			else
			{
				subProb.put(s.getCourse(), posteriori);
			}
		}

		float totalProb = 1.0f;
		String text = "";
		for (String title : subjects)
		{
			text += String.format("%s: %.3f%%\n", title,
					subProb.get(title) * 100);
			totalProb *= subProb.get(title);
		}

		text = String.format("All: %.3f%%\n", totalProb * 100) + text;

		// Make it always to have at least five lines
		text = fillNewLines(text, 5);
		secText = fillNewLines(secText, 5);

		indivLabel.setText(secText);
		fullLabel.setText(text);
	}

	private void updateConflictsLeft()
	{
		if (clTable.table.getCellSelection().length < 1) return;

		Cell selected = clTable.table.getCellSelection()[0];
		String subject = (String) clTable.table.getContentAt(selected.row,
				ClassListTable.Columns.Subject.getIndex());
		String section = (String) clTable.table.getContentAt(selected.row,
				ClassListTable.Columns.Section.getIndex());

		String text = subject + " " + section + " conflicts with: \n";

		Section s = cList.getSection(subject, section);
		for (Section t : cList.getList())
		{
			if (t.getCourse().compareToIgnoreCase(s.getCourse()) != 0
					&& t.doesConflictWith(s))
				text += t.getCourse() + " " + t.getSection() + "\n";
		}

		fillNewLines(text, 5);

		conflictLabel.setText(text);
	}

	private void updateConflictsRight()
	{
		if (list.getSelectionCount() < 1) return;

		Section s = cList.getSection(list.getItem(list.getSelectionIndex()));
		String text = s.getCourse() + " " + s.getSection()
				+ " conflicts with:\n";
		for (String title : list.getItems())
		{
			Section t = cList.getSection(title);
			if (t.getCourse().compareToIgnoreCase(s.getCourse()) != 0
					&& t.doesConflictWith(s))
				text += t.getCourse() + " " + t.getSection() + "\n";
		}

		fillNewLines(text, 5);

		conflictLabel2.setText(text);
	}

	private String fillNewLines(String value, int numLines)
	{
		int lines = value.length() - value.replace("\n", "").length();
		while (lines < 4)
		{
			value += "\n";
			lines++;
		}

		return value;
	}

	public void setClassList(String[] list)
	{
		this.cList.setList(list);
		scTable.setSections(cList.getList());
		clTable.setList(cList);
	}

	public void start()
	{
		shellSched.open();
		shellList.open();
		shellList.setMaximized(true);
		shellSched.setVisible(false);
		while ((!shellSched.isDisposed() && shellSched.isVisible())
				|| (!shellList.isDisposed() && shellList.isVisible()))
		{
			if (!display.readAndDispatch()) display.sleep();
		}

		if (!shellSched.isDisposed()) shellSched.dispose();
		if (!shellList.isDisposed()) shellList.dispose();
		display.dispose();
	}

	public static void main(String[] args)
	{
		FirstApplication fa = new FirstApplication();
		fa.start();
	}
}
