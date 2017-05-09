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
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;

import infsysProj.infsysProj.*;

public class AddPerson extends MyJFrame {

	private JPanel contentPane;
	private Person person = new Person();
	JTextField txtTitle;
	List<String> authoredPublications = new ArrayList<String>();
	List<String> editedPublications = new ArrayList<String>();
	JTable authoredPublicationsTable;
	JLabel lblauthoredPublications;
	JTable editedPublicationsTable;
	JLabel lblEditedPublications;
	
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

	public AddPerson(PersonWindow caller) {
		super("Add Person");

		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setPreferredSize(new Dimension(500, 500));

		GridBagConstraints c = new GridBagConstraints();

		JButton updateButton = new JButton("Save");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newName = txtTitle.getText();
				DatabaseHelper.get().addPerson(newName, authoredPublications, editedPublications);
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
		
		JLabel lblTitle = new JLabel("Name");
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
		txtTitle.setText(person.getName());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtTitle, c);
		

		lblauthoredPublications = new JLabel("Authored Publications (" + authoredPublications.size() + ")");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblauthoredPublications, c);

		JButton addAuthoredPublicationButton = new JButton("Add");
		addAuthoredPublicationButton.setEnabled(true);
		addAuthoredPublicationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SelectPublication frame = new SelectPublication(SelectPublication.ObjectMode.INPROCEEDINGS,AddPerson.this,1);
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
		c.gridy = 4;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(addAuthoredPublicationButton, c);

		 //headers for the table
        String[] columns = new String[] {
            "Title"
        };
         
        String[][] data = new String[authoredPublications.size()][1];
        for(int i = 0; i < authoredPublications.size(); i++){
        	data[i][0] = authoredPublications.get(i);
        }
        //create table with data
        authoredPublicationsTable = new JTable();

        authoredPublicationsTable.setModel(new DefaultTableModel(data,columns) {

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
	            	
                    authoredPublications.remove(authoredPublicationsTable.getSelectedRow());
                    ((DefaultTableModel)authoredPublicationsTable.getModel()).removeRow(authoredPublicationsTable.getSelectedRow());
                    authoredPublicationsTable.clearSelection();
                    lblauthoredPublications.setText("Authored Publications (" + authoredPublications.size() + ")");

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
	                        int selectedRowAuthoredPublications = authoredPublicationsTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), authoredPublicationsTable));
	                        if (selectedRowAuthoredPublications > -1) {
	                            authoredPublicationsTable.setRowSelectionInterval(selectedRowAuthoredPublications, selectedRowAuthoredPublications);
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

	        authoredPublicationsTable.setComponentPopupMenu(popupMenu);

        
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 5;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		//contentPane.add(txtInProceedings, c);
        contentPane.add(new JScrollPane(authoredPublicationsTable),c);

		JButton addEditedPublicationButton = new JButton("Add");
		addEditedPublicationButton.setEnabled(true);
		addEditedPublicationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SelectPublication frame = new SelectPublication(SelectPublication.ObjectMode.PROCEEDINGS,AddPerson.this,2);
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
		c.gridy = 12;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(addEditedPublicationButton, c);

        //load names of InProceedings from Database

		lblEditedPublications = new JLabel("Edited Publications (" + editedPublications.size() + ")");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 11;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblEditedPublications, c);

		 //headers for the table
        columns = new String[] {
            "Title"
        };
         
        data = new String[editedPublications.size()][1];
        for(int i = 0; i < editedPublications.size(); i++){
        	data[i][0] = editedPublications.get(i);
        }
        //create table with data
        editedPublicationsTable = new JTable(data, columns);
        editedPublicationsTable.setModel(new DefaultTableModel(data,columns) {

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
                    editedPublications.remove(editedPublicationsTable.getSelectedRow());
                    ((DefaultTableModel)editedPublicationsTable.getModel()).removeRow(editedPublicationsTable.getSelectedRow());
                    lblEditedPublications.setText("Edited Publications (" + editedPublications.size() + ")");
                    editedPublicationsTable.clearSelection();

                    }
	        });
			deleteItem2.setEnabled(false);

	        popupMenu2.add(deleteItem2);

	        popupMenu2.addPopupMenuListener(new PopupMenuListener() {

	            @Override
	            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
	                SwingUtilities.invokeLater(new Runnable() {
	                    @Override
	                    public void run() {
	                        int selectedRowAuthoredPublications = editedPublicationsTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu2, new Point(0, 0), editedPublicationsTable));
	                        if (selectedRowAuthoredPublications > -1) {
	                            editedPublicationsTable.setRowSelectionInterval(selectedRowAuthoredPublications, selectedRowAuthoredPublications);
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

	        editedPublicationsTable.setComponentPopupMenu(popupMenu2);

        

        //add the table to the frame
        
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 5;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 11;
		c.insets = new Insets(5, 5, 5, 5);
		//contentPane.add(txtInProceedings, c);
        contentPane.add(new JScrollPane(editedPublicationsTable),c);

		
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
	String format(Collection<?> c) {
		  String s = c.stream().map(Object::toString).collect(Collectors.joining("\n"));
		  return String.format("[%s]", s);
		}
	
	private void closeWindow(){
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

	}

	@Override
	public void selectedObject(DomainObject object, int id) {
		if(id == 1){
			if(authoredPublications.contains(((InProceedings)object).getTitle())){
				System.out.println("This InProceedings is already in the list");
			}
			else{
	            authoredPublications.add(((InProceedings)object).getTitle());
	            String[] newInProc = new String[1];
	            newInProc[0] = ((InProceedings)object).getTitle();
	            ((DefaultTableModel)authoredPublicationsTable.getModel()).addRow(newInProc);
	            authoredPublicationsTable.clearSelection();
	            lblauthoredPublications.setText("Authored Publications (" + authoredPublications.size() + ")");
			}

		}
		else if(id == 2){
            editedPublications.add(((Proceedings)object).getTitle());
            String[] newProc = new String[1];
            newProc[0] = ((Proceedings)object).getTitle();
            ((DefaultTableModel)editedPublicationsTable.getModel()).addRow(newProc);
            editedPublicationsTable.clearSelection();
            lblEditedPublications.setText("Edited Publications (" + editedPublications.size() + ")");

		}
		
	}

}
