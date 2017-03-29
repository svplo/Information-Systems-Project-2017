package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import infsysProj.infsysProj.Person;

//resource: https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#simple
public class PersonTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int COLUMN_TITLE = 0;
	private static final int COLUMN_YEAR = 1;
	private static final int COLUMN_EDITION = 2;

	private String[] columnNames = { "Name", "Authored Publications", "Edited Publications" };

	private List<Person> listPerson;

	public PersonTableModel(List<Person> allPeople) {

		this.listPerson = allPeople;

	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return listPerson.size();
	}

	public String getValueAt(int rowIndex, int columnIndex) {
		Person person = listPerson.get(rowIndex);
		String returnValue = "";
		switch (columnIndex) {
		case COLUMN_TITLE:
			returnValue = person.getName();
			break;
		case COLUMN_YEAR:
			returnValue = String.valueOf(person.getAuthoredPublications().size());
			break;
		case COLUMN_EDITION:
			returnValue = String.valueOf(person.getEditedPublications().size());
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

	public void changeData(List<Person> newList) {

		this.listPerson = newList;
		fireTableDataChanged();
	}

	// needed for sorting
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (listPerson.isEmpty()) {
			return Object.class;
		}
		return getValueAt(0, columnIndex).getClass();
	}
}
