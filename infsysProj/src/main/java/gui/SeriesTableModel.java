package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;
import infsysProj.infsysProj.*;
public class SeriesTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private static final int COLUMN_TITLE = 0;
	private static final int COLUMN_YEAR = 1;
	private static final int COLUMN_EDITION = 2;

	private String[] columnNames = { "Name", "Publications" };

	private List<Series> listSeries;

	public SeriesTableModel(List<Series> allSeries) {

		this.listSeries = allSeries;

	}
	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return listSeries.size();
	}
	
	public String getValueAt(int rowIndex, int columnIndex) {
		Series series = listSeries.get(rowIndex);
		String returnValue = "";
		switch (columnIndex) {
		case COLUMN_TITLE:
			returnValue = series.getName();
			break;
		case COLUMN_YEAR:
			returnValue = String.valueOf(series.getPublications().size());
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

	public void changeData(List<Series> newList) {

		this.listSeries = newList;
		fireTableDataChanged();
	}

	// needed for sorting
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (listSeries.isEmpty()) {
			return Object.class;
		}
		return getValueAt(0, columnIndex).getClass();
	}

}
