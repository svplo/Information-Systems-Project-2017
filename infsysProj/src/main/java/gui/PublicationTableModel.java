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

import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import infsysProj.infsysProj.InProceedings;
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

	private DBCollection listPublication;

	public PublicationTableModel(DBCollection allPublications) {		

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
		return (int) listPublication.count();
	}

	public String getValueAt(int rowIndex, int columnIndex) {
		DBObject publications = listPublication.findOne();
		String returnValue = "";
		switch (columnIndex) {
		case COLUMN_TITLE:
			returnValue = (String) publications.get("title");
			break;
		case COLUMN_YEAR:
			returnValue = (String) publications.get("year");
			break;
		case COLUMN_EDITION:
			returnValue = (String) publications.get("electronicEdition");
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
	
	
	public void changeData(DBCollection newList){
		
		this.listPublication = newList;
		fireTableDataChanged();
	}

}
