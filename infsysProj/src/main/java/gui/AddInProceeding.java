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
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import infsysProj.infsysProj.DomainObject;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Publication;
import infsysProj.infsysProj.Proceedings;

public class AddInProceeding extends MyJFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private InProceedings inProceeding;
	List<String> authors = new ArrayList<String>();
	JLabel lblAuthors;
	JTable authorsTable;
	String proceedingsName;
	JTextField txtProceeding;
	JTextField txtTitle;
	JTextField txtYear;
	JTextField txtElect;
	JTextField txtNote;
	JTextField txtPages;

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

	public AddInProceeding(InProceedingsWindow caller) {
		super("Add InProceeding");
		this.inProceeding = new InProceedings();

		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setPreferredSize(new Dimension(500, 500));

		GridBagConstraints c = new GridBagConstraints();


		JButton updateButton = new JButton("Save");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newName = txtTitle.getText();
				InProceedings newInProc = new InProceedings();
				newInProc.setTitle(txtTitle.getText());
				newInProc.setElectronicEdition(txtElect.getText());
				newInProc.setNote(txtNote.getText());
				newInProc.setPages(txtPages.getText());
				try{
					newInProc.setYear(Integer.parseInt(txtYear.getText()));
				}
				catch(NumberFormatException e){
					newInProc.setYear(0);

				}

				DatabaseHelper.addInProceeding(newInProc,txtProceeding.getText(),authors);
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

		JButton deleteButton = new JButton("Cancel");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				closeWindow();
			}
		});

		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(deleteButton, c);

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
		txtTitle.setText(inProceeding.getTitle());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtTitle, c);

		JLabel lblYear = new JLabel("Year");
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblYear, c);

		txtYear = new JTextField();
		txtYear.setEditable(true);
		txtYear.setText(inProceeding.getYear().toString());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtYear, c);

		JLabel lblElect = new JLabel("Electronic edition");
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblElect, c);

		txtElect = new JTextField();
		txtElect.setEditable(true);
		txtElect.setText(inProceeding.getElectronicEdition());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtElect, c);

		JLabel lblNote = new JLabel("Note");
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblNote, c);

		txtNote = new JTextField();
		txtNote.setEditable(true);
		txtNote.setText(inProceeding.getNote());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtNote, c);

		JLabel lblPages = new JLabel("Pages");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblPages, c);

		txtPages = new JTextField();
		txtPages.setEditable(true);
		txtPages.setText(inProceeding.getPages());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 5;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtPages, c);

		JLabel lblProceeding = new JLabel("Proceeding");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 6;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblProceeding, c);

		txtProceeding = new JTextField();
		txtProceeding.setEditable(false);
		txtProceeding.setText(proceedingsName);
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 6;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtProceeding, c);

		JButton editProceeding = new JButton("Edit");
		editProceeding.setEnabled(true);
		editProceeding.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SelectPublication frame = new SelectPublication(SelectPublication.ObjectMode.PROCEEDINGS, AddInProceeding.this, 1);
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
		c.gridy = 7;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(editProceeding, c);

		lblAuthors = new JLabel("Authors (" + authors.size() + ")");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 8;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblAuthors, c);

		JButton addAuthor = new JButton("Add");
		addAuthor.setEnabled(true);
		addAuthor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SelectPublication frame = new SelectPublication(SelectPublication.ObjectMode.PERSON, AddInProceeding.this, 2);
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
		c.gridy = 9;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(addAuthor, c);

		// headers for the table
		String[] columns = new String[] { "Title" };

		String[][] data = new String[authors.size()][1];
		for (int i = 0; i < authors.size(); i++) {
			data[i][0] = authors.get(i);
		}
		// create table with data
		authorsTable = new JTable();

		authorsTable.setModel(new DefaultTableModel(data, columns) {

			@Override
			public boolean isCellEditable(int row, int column) {
				// all cells false
				return false;
			}
		});
		final JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("Delete");
		deleteItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				authors.remove(authorsTable.getSelectedRow());
				((DefaultTableModel) authorsTable.getModel()).removeRow(authorsTable.getSelectedRow());
				authorsTable.clearSelection();
				lblAuthors.setText("Authors (" + authors.size() + ")");

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
						int selectedRowAuthoredPublications = authorsTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), authorsTable));
						if (selectedRowAuthoredPublications > -1) {
							authorsTable.setRowSelectionInterval(selectedRowAuthoredPublications, selectedRowAuthoredPublications);
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

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 5;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 8;
		c.insets = new Insets(5, 5, 5, 5);
		// contentPane.add(txtInProceedings, c);
		contentPane.add(new JScrollPane(authorsTable), c);


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
	
	private void closeWindow(){
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

	}
	
	@Override
	public void selectedObject(DomainObject object, int id) {
		if(id == 1){
				proceedingsName = ((Proceedings)object).getTitle();
				txtProceeding.setText(proceedingsName);
		}
		else if(id == 2){
			
            authors.add(((Person)object).getName());
            String[] newProc = new String[1];
            newProc[0] = ((Person)object).getName();
            ((DefaultTableModel)authorsTable.getModel()).addRow(newProc);
            authorsTable.clearSelection();
            lblAuthors.setText("Authors (" + authors.size() + ")");

		}
		
	}


}
