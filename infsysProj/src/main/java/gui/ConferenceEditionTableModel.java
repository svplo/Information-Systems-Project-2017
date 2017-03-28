package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import infsysProj.infsysProj.ConferenceEdition;

public class ConferenceEditionTableModel extends AbstractTableModel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int COLUMN_TITLE = 0;
	private static final int COLUMN_YEAR = 1;
	private static final int COLUMN_EDITION = 2;

	private String[] columnNames = { "Name", "Year", "Proceeding" };

	private List<ConferenceEdition> listConferenceEdition;

	public ConferenceEditionTableModel(List<ConferenceEdition> allConferenceEdition) {		

		this.listConferenceEdition = allConferenceEdition;

	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return listConferenceEdition.size();
	}

	public String getValueAt(int rowIndex, int columnIndex) {
		ConferenceEdition conferenceEdition = listConferenceEdition.get(rowIndex);
		String returnValue = "";
		switch (columnIndex) {
		case COLUMN_TITLE:
			returnValue = DatabaseHelper.getConferenceEditionName(conferenceEdition);
			break;
		case COLUMN_YEAR:
			returnValue = String.valueOf(conferenceEdition.getYear());
			break;
		case COLUMN_EDITION:
			returnValue = DatabaseHelper.getConferenceEditionProceeding(conferenceEdition);
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}
	
	
	public void changeData(List<ConferenceEdition> newList){
		
		this.listConferenceEdition = newList;
		fireTableDataChanged();
	}

	// needed for sorting
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (listConferenceEdition.isEmpty()) {
			return Object.class;
		}
		return getValueAt(0, columnIndex).getClass();
	}
}
	

