package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;

import infsysProj.infsysProj.DomainObject;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Proceedings;

public class AddProceedings extends JFrame{

	private JPanel contentPane;
	private Proceedings proceeding = new Proceedings();
	List<String> authors = new ArrayList<String>();
	List<String> inProcNames = new ArrayList<String>();
	
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
	
	public AddProceedings(ProceedingsWindow caller){
		super("Add Proceedings");
		
		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setPreferredSize(new Dimension(500, 500));

		GridBagConstraints c = new GridBagConstraints();

		JButton updateButton = new JButton("Save");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO 
				/*String newName = txtTitle.getText();
				DatabaseHelper.addPerson(newName, authoredPublications, editedPublications);
				closeWindow();*/
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

		JTextField txtTitle = new JTextField();
		txtTitle.setEditable(false);
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


		JLabel lblAuthors = new JLabel("Authors(" + authors.size() + ")");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblAuthors, c);
		
		JButton addAuthorsButton = new JButton("Add");
		addAuthorsButton.setEnabled(false);
		addAuthorsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO SelectPublication frame = new SelectPublication(SelectPublication.ObjectMode.INPROCEEDINGS,ProceedingDetail.this,1);
				//frame.setVisible(true);
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
		JTable tableA = new JTable();
		tableA.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableA.setModel(new DefaultTableModel(dataA,columnsA){
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
            	
                authors.remove(tableA.getSelectedRow());
                ((DefaultTableModel)tableA.getModel()).removeRow(tableA.getSelectedRow());
                tableA.clearSelection();
                lblAuthors.setText("Authors" + authors.size() + ")");

                }
        });
        deleteItem.setEnabled(false);
        popupMenu.add(deleteItem);
        
        popupMenu.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int selectedRowAuthors = tableA.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), tableA));
                        if (selectedRowAuthors > -1) {
                            tableA.setRowSelectionInterval(selectedRowAuthors, selectedRowAuthors);
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
        
        tableA.setComponentPopupMenu(popupMenu);
        //table
        c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 5;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
        contentPane.add(new JScrollPane(tableA),c);

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

		JTextField txtYear = new JTextField();
		txtYear.setEditable(false);
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

		JTextField txtElect = new JTextField();
		txtElect.setEditable(false);
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

		JTextField txtNote = new JTextField();
		txtNote.setEditable(false);
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

		JTextField txtNumber = new JTextField();
		txtNumber.setEditable(false);
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

		JTextField txtPublisher = new JTextField();
		txtPublisher.setEditable(false);
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

		JTextField txtVolume = new JTextField();
		txtVolume.setEditable(false);
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

		JTextField txtISBN = new JTextField();
		txtISBN.setEditable(false);
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

		JTextField txtSeries = new JTextField();
		txtSeries.setEditable(false);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 11;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtSeries, c);

		JLabel lblConfEdition = new JLabel("Conference edition");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 12;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblConfEdition, c);

		JTextField txtConfEdition = new JTextField();
		txtConfEdition.setEditable(false);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 12;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtConfEdition, c);

		//load names of InProceedings from Database


		JLabel lblInProceedings = new JLabel("InProceedings(" + inProcNames.size() + ")");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 13;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblInProceedings, c);
		
		JButton addInProceedingsButton = new JButton("Add");
		addInProceedingsButton.setEnabled(false);
		addInProceedingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO SelectPublication frame = new SelectPublication(SelectPublication.ObjectMode.INPROCEEDINGS,PersonDetail.this,1);
				//frame.setVisible(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			}
		});
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 14;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(addInProceedingsButton, c);

		// headers for the table
		String[] columns = new String[] { "Title" };

		String[][] data = new String[inProcNames.size()][1];
		for (int i = 0; i < inProcNames.size(); i++) {
			data[i][0] = inProcNames.get(i);
		}
		// create table with data
		JTable table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setModel(new DefaultTableModel(data,columns) {

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

		table.setComponentPopupMenu(popupMenu);
		// add the table to the frame

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 5;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 13;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(new JScrollPane(table), c);
		
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
}
