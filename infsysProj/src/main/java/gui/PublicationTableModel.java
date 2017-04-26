package gui;

import java.util.List;


import javax.swing.table.AbstractTableModel;

import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Publication;

//resource: https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#simple
public class PublicationTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private static final int COLUMN_TITLE = 0;
	private static final int COLUMN_YEAR = 1;
	private static final int COLUMN_EDITION = 2;
	private static final int COLUMN_DETAILS = 3;

	private String[] columnNames = { "Publication Title", "Year", "Electronic Edition", "Details" };

	private List<Publication> listPublication;

	public PublicationTableModel(List<Publication> allPublications) {		

		this.listPublication = allPublications;

	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return (int) listPublication.size();
	}

	public String getValueAt(int rowIndex, int columnIndex) {
		Publication publications = listPublication.get(rowIndex);
		String returnValue = "";
		switch (columnIndex) {
		case COLUMN_TITLE:
			returnValue = (String) publications.getTitle();
			break;
		case COLUMN_YEAR:
			returnValue = String.valueOf(publications.getYear());
			break;
		case COLUMN_EDITION:
			returnValue = (String) publications.getElectronicEdition();
			break;
		case COLUMN_DETAILS:
			if(publications instanceof InProceedings){
				returnValue = ((InProceedings) publications).getNote();
			}
			break;
		default:
			throw new IllegalArgumentException("Invalid column index");
		}

		return returnValue;
	}
	
	
	public void changeData(List<Publication> newList){
		
		this.listPublication = newList;
		fireTableDataChanged();
	}

}
