package main.java.gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import main.java.infsysProj.infsysProj.*;

public class ConferenceTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private static final int COLUMN_TITLE = 0;
	private static final int COLUMN_YEAR = 1;
	private static final int COLUMN_EDITION = 2;

	private String[] columnNames = { "Name", "Editions" };

	private List<Conference> listConference;

	public ConferenceTableModel(List<Conference> allConference) {

		this.listConference = allConference;

	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return listConference.size();
	}

	public String getValueAt(int rowIndex, int columnIndex) {
		Conference conference = listConference.get(rowIndex);
		String returnValue = "";
		switch (columnIndex) {
		case COLUMN_TITLE:
			returnValue = conference.getName();
			break;
		case COLUMN_YEAR:
			returnValue = String.valueOf(conference.getEditions().size());
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

	public void changeData(List<Conference> newList) {

		this.listConference = newList;
		fireTableDataChanged();
	}

	// needed for sorting
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (listConference.isEmpty()) {
			return Object.class;
		}
		return getValueAt(0, columnIndex).getClass();
	}

}
