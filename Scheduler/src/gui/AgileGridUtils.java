package gui;

import org.agilemore.agilegrid.AgileGrid;
import org.eclipse.swt.graphics.GC;

public class AgileGridUtils
{

	public static void resizeAllColumnsOptimal(AgileGrid table)
	{
		for (int i = 0; i < table.getLayoutAdvisor().getColumnCount(); i++)
			AgileGridUtils.resizeColumnOptimal(table, i);
	}

	public static int resizeColumnOptimal(AgileGrid table, int column)
	{

		int optWidth = 5;
		int width = 0;
		if (column >= 0 && column < table.getLayoutAdvisor().getColumnCount())
		{
			GC gc = new GC(table);

			if (table.getLayoutAdvisor().isTopHeaderVisible())
			{
				width = table.getCellRendererProvider()
						.getTopHeadRenderer(column)
						.getOptimalWidth(gc, -1, column);
				if (width > optWidth)
				{
					optWidth = width;
				}
			}

			for (int i = 0; i < table.getLayoutAdvisor().getRowCount(); i++)
			{
				width = table.getCellRendererProvider()
						.getCellRenderer(i, column)
						.getOptimalWidth(gc, i, column);
				if (width > optWidth)
				{
					optWidth = width;
				}
			}

			gc.dispose();
			table.getLayoutAdvisor().setColumnWidth(column, optWidth);
			return optWidth;
		}
		return Integer.MIN_VALUE;
	}
}
