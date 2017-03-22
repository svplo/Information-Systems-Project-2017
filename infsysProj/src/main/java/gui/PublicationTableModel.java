package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import infsysProj.infsysProj.Publication;

//resource: https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#simple
public class PublicationTableModel extends AbstractTableModel {

	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JPanel controlPanel;

	private static final int COLUMN_TITLE = 0;
	private static final int COLUMN_YEAR = 1;
	private static final int COLUMN_EDITION = 2;
	private static final int COLUMN_DETAILS = 3;

	private String[] columnNames = { "Publication Title", "Year", "Electronic Edition", "Details" };

	private List<Publication> listPublication;

	public PublicationTableModel() {
		Collection<Publication> allPublications = DatabaseHelper.getAllPublications();
		

		this.listPublication = new ArrayList<Publication>(allPublications);

	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return listPublication.size();
	}

	public String getValueAt(int rowIndex, int columnIndex) {
		Publication publications = listPublication.get(rowIndex);
		String returnValue = "";
		System.out.print("e");
		switch (columnIndex) {
		case COLUMN_TITLE:
			returnValue = publications.getTitle();
			break;
		case COLUMN_YEAR:
			returnValue = publications.getYear().toString();
			break;
		case COLUMN_EDITION:
			returnValue = publications.getElectronicEdition();
			break;
		case COLUMN_DETAILS:
			// go to detailed view
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

	// needed for sorting
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (listPublication.isEmpty()) {
			return Object.class;
		}
		return getValueAt(0, columnIndex).getClass();
	}
}
