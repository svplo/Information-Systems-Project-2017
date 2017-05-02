package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;
import infsysProj.infsysProj.Publisher;
import infsysProj.infsysProj.Publisher;

public class PublisherTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	private static final int COLUMN_TITLE = 0;
	private static final int COLUMN_YEAR = 1;
	private static final int COLUMN_EDITION = 2;

	private String[] columnNames = { "Name", "Publications" };

	private List<Publisher> listPublisher;

	public PublisherTableModel(List<Publisher> allPublisher) {

		this.listPublisher = allPublisher;

		
	}
	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return listPublisher.size();
	}
	
	public String getValueAt(int rowIndex, int columnIndex) {
		Publisher publisher = listPublisher.get(rowIndex);
		String returnValue = "";
		switch (columnIndex) {
		case COLUMN_TITLE:
			returnValue = publisher.getName();
			break;
		case COLUMN_YEAR:
			returnValue = DatabaseHelper.get().getNumberOfPublicationsForPublisher(publisher.getName());
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}

	public void changeData(List<Publisher> newList) {

		this.listPublisher = newList;
		fireTableDataChanged();
	}

	// needed for sorting
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (listPublisher.isEmpty()) {
			return Object.class;
		}
		return getValueAt(0, columnIndex).getClass();
	}


}
