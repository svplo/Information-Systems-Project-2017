package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import infsysProj.infsysProj.*;

public class AddProceedings extends MyJFrame {

	private JPanel contentPane;
	private Proceedings proceeding;
	List<String> authors = new ArrayList<String>();
	List<String> inProcNames = new ArrayList<String>();
	JTable authorsTable;
	JLabel lblAuthors;
	JTable inProceedingsTable;
	JLabel lblInProceedings;
	String proceedingsName;
	JTextField txtTitle;
	JTextField txtYear;
	JTextField txtElect;
	JTextField txtNote;
	JTextField txtNumber;
	JTextField txtPublisher;
	JTextField txtVolume;
	JTextField txtISBN;
	JTextField txtSeries;
	JTextField txtConf;
	JTextField txtConfEdition;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public AddProceedings( ProceedingsWindow caller) {
		super("Add Preceeding");
		proceeding = new Proceedings();
		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setPreferredSize(new Dimension(500, 600));

		GridBagConstraints c = new GridBagConstraints();


		JButton updateButton = new JButton("Save");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newName = txtTitle.getText();
				Proceedings newProc = new Proceedings();
				newProc.setTitle(txtTitle.getText());
				newProc.setElectronicEdition(txtElect.getText());
				newProc.setNote(txtNote.getText());
				try{
					newProc.setNumber(Integer.parseInt(txtNumber.getText()));
				}
				catch(NumberFormatException e){
					newProc.setNumber(0);

				}
				try{
					newProc.setYear(Integer.parseInt(txtYear.getText()));
				}
				catch(NumberFormatException e){
					newProc.setYear(0);

				}
				newProc.setIsbn(txtISBN.getText());
				newProc.setVolume(txtVolume.getText());
				try{
					DatabaseHelper.get().addProceeding(newProc,authors,inProcNames, txtPublisher.getText(), txtSeries.getText(), txtConf.getText(),Integer.parseInt(txtConfEdition.getText()));
				}
				catch(NumberFormatException e){
					DatabaseHelper.get().addProceeding(newProc,authors,inProcNames, txtPublisher.getText(), txtSeries.getText(), txtConf.getText(),0);

				}

				caller.reloadDataFromDatabase();
				closeWindow();
			}
		});

		updateButton.setEnabled(true);

		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(updateButton, c);

		JButton Delete = new JButton("Cancel");
		
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(Delete, c);

		JLabel lblTitle = new JLabel("Title");
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblTitle, c);

		txtTitle = new JTextField();
		txtTitle.setEditable(true);
		txtTitle.setText(proceeding.getTitle());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtTitle, c);

		//Load the Authors from DB

		lblAuthors = new JLabel("Authors(" + authors.size() + ")");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblAuthors, c);
		
		JButton addAuthorsButton = new JButton("Add");
		addAuthorsButton.setEnabled(true);
		addAuthorsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SelectPublication frame = new SelectPublication(SelectPublication.ObjectMode.PERSON,AddProceedings.this,1);
				frame.setVisible(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(addAuthorsButton, c);

		// headers for the table
		String[] columnsA = new String[] { "Name" };

		String[][] dataA = new String[authors.size()][1];
		for (int i = 0; i < authors.size(); i++) {
			dataA[i][0] = authors.get(i);
		}

		// create table with data
		authorsTable = new JTable();
		authorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		authorsTable.setModel(new DefaultTableModel(dataA,columnsA){
			 @Override
	            public boolean isCellEditable(int row, int column) {
	               //all cells false
	               return false;
	            }
	        }
	);
		
		final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");

        deleteItem.addActionListener(new ActionListener() {
	        
            @Override
            public void actionPerformed(ActionEvent e) {
            	
                authors.remove(authorsTable.getSelectedRow());
                ((DefaultTableModel)authorsTable.getModel()).removeRow(authorsTable.getSelectedRow());
                authorsTable.clearSelection();
                lblAuthors.setText("Authors(" + authors.size() + ")");

                }
        });
        deleteItem.setEnabled(true);
        popupMenu.add(deleteItem);
        
        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int selectedRowAuthors = authorsTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), authorsTable));
                        if (selectedRowAuthors > -1) {
                            authorsTable.setRowSelectionInterval(selectedRowAuthors, selectedRowAuthors);
                        }
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // TODO Auto-generated method stub

            }
        });
        
        authorsTable.setComponentPopupMenu(popupMenu);
        //table
        c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 5;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
        contentPane.add(new JScrollPane(authorsTable),c);

		JLabel lblYear = new JLabel("Year");
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblYear, c);

		txtYear = new JTextField();
		txtYear.setEditable(true);
		txtYear.setText(proceeding.getYear().toString());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtYear, c);

		JLabel lblElect = new JLabel("Electronic edition");
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblElect, c);

		txtElect = new JTextField();
		txtElect.setText(proceeding.getElectronicEdition());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 5;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtElect, c);

		JLabel lblNote = new JLabel("Note");
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 6;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblNote, c);

		txtNote = new JTextField();
		txtNote.setText(proceeding.getNote());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 6;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtNote, c);

		JLabel lblNumber = new JLabel("Number");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 7;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblNumber, c);

		txtNumber = new JTextField();
		txtNumber.setText(proceeding.getNumber() + "");
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 7;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtNumber, c);

		JLabel lblPublisher = new JLabel("Publisher");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 8;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblPublisher, c);

		txtPublisher = new JTextField();
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 8;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtPublisher, c);

		JLabel lblVolume = new JLabel("Volume");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 9;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblVolume, c);

		txtVolume = new JTextField();
		txtVolume.setText(proceeding.getVolume());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 9;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtVolume, c);

		JLabel lblISBN = new JLabel("ISBN");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 10;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblISBN, c);

		txtISBN = new JTextField();
		txtISBN.setText(proceeding.getIsbn());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 10;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtISBN, c);

		JLabel lblSeries = new JLabel("Series");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 11;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblSeries, c);

		txtSeries = new JTextField();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 11;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtSeries, c);

		JLabel lblConf = new JLabel("Conference");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 12;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblConf, c);

		txtConf = new JTextField();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 12;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtConf, c);

		JLabel lblConfEdition = new JLabel("Conference edition");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 13;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblConfEdition, c);

		txtConfEdition = new JTextField();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 13;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtConfEdition, c);

		lblInProceedings = new JLabel("InProceedings(" + inProcNames.size() + ")");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 14;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblInProceedings, c);
		
		JButton addInProceedingsButton = new JButton("Add");
		addInProceedingsButton.setEnabled(true);
		addInProceedingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SelectPublication frame = new SelectPublication(SelectPublication.ObjectMode.INPROCEEDINGS,AddProceedings.this,2);
				frame.setVisible(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 15;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(addInProceedingsButton, c);

		// headers for the table
		String[] columns = new String[] { "Title" };

		String[][] data = new String[inProcNames.size()][1];
		for (int i = 0; i < inProcNames.size(); i++) {
			data[i][0] = inProcNames.get(i);
		}
		// create table with data
		inProceedingsTable = new JTable();
		inProceedingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inProceedingsTable.setModel(new DefaultTableModel(data,columns) {

            @Override
            public boolean isCellEditable(int row, int column) {
               //all cells false
               return false;
            }
        }
);
		final JPopupMenu popupMenu2 = new JPopupMenu();
		JMenuItem deleteItem2 = new JMenuItem("Delete");
		deleteItem2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Right-click performed on table and choose DELETE");
				/*
				 * JTable target = (JTable) e.getSource(); int row = target.getSelectedRow(); inProcNames.remove(row);
				 */
			}
		});
		popupMenu2.add(deleteItem2);

		inProceedingsTable.setComponentPopupMenu(popupMenu);
		// add the table to the frame

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 5;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 14;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(new JScrollPane(inProceedingsTable), c);


		Delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});

		setContentPane(contentPane);
		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				dispose();
			}
		});

	}
	
	@Override
	public void selectedObject(DomainObject object, int id) {
		if(id == 1){
			if(!authors.contains(((Person)object).getName())){
            authors.add(((Person)object).getName());
            String[] newProc = new String[1];
            newProc[0] = ((Person)object).getName();
            ((DefaultTableModel)authorsTable.getModel()).addRow(newProc);
            authorsTable.clearSelection();
            lblAuthors.setText("Authors (" + authors.size() + ")");
			}
			else{
				System.out.println("There is already an author with name: " + ((Person)object).getName());
			}

		}
		else if(id == 2){
			if(!inProcNames.contains(((InProceedings)object).getTitle())){
				inProcNames.add(((InProceedings)object).getTitle());
	            String[] newProc = new String[1];
	            newProc[0] = ((InProceedings)object).getTitle();
	            ((DefaultTableModel)inProceedingsTable.getModel()).addRow(newProc);
	            inProceedingsTable.clearSelection();
	            lblInProceedings.setText("InProceedings (" + inProcNames.size() + ")");
				}
				else{
					System.out.println("There is already an InProceedings with name: " + ((InProceedings)object).getTitle());
				}


		}
		
	}
	
	private void closeWindow(){
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

	}


	/*
	 * String format(Collection<?> c) { String s = c.stream().map(Object::toString).collect(Collectors.joining("\n")); return String.format("[%s]", s); }
	 */

}
