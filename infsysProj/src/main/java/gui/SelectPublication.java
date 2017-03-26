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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import infsysProj.infsysProj.DomainObject;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Proceedings;
import infsysProj.infsysProj.Publication;

/**
 * This program demonstrates how to sort rows in a table.
 * 
 * @author www.codejava.net resource: http://www.codejava.net/java-se/swing/6-techniques-for-sorting-jtable-you-should-know
 */
public class SelectPublication extends JFrame {
	private JTable table;
	private JPanel contentPane;
	private ItemsPerPage itemsPerPageIndex = ItemsPerPage.FIFTY;
	List<? extends DomainObject> allObjects;
	List<? extends DomainObject> currentObjects;
	List<DomainObject> allPersons;
	TableModel tableModel;
	private int pageNumber = 0;
	JTextField pageTextField;
	JLabel numberOfPagesLabel;
	JLabel numberOfItemsLabel;
	int nTitleClicked = 0;
	int nYearClicked = 0;
	int nEEClicked = 0;
	ObjectMode mode;

	public static enum ObjectMode {
		PUBLICATION(""), PROCEEDINGS(""), INPROCEEDINGS(""), PERSON("");

		private final String string;

		private ObjectMode(String string) {
			this.string = string;
		}

		public String getString() {
			return string;
		}

	}

	public static enum ItemsPerPage {
		TWENTY("20 items per page", 20, 0), FIFTY("50 items per page", 50, 1), HUNDRED("100 items per page", 100, 2), TWOHUNDRED("200 items per page", 200, 3), FIVEHUNDRED("500 items per page", 500, 4), THOUSAND("1000 items per page", 1000, 5), ALL("All items", Integer.MAX_VALUE, 6);

		private final String string;
		private final int number;
		private final int index;

		private ItemsPerPage(String string, int number, int index) {
			this.string = string;
			this.number = number;
			this.index = index;
		}

		public String getString() {
			return string;
		}

		public int getNumber() {
			return number;
		}

		public int getIndex() {
			return index;
		}

		public static ItemsPerPage getEnumForInt(int legIndex) {
			for (ItemsPerPage l : ItemsPerPage.values()) {
				if (l.index == legIndex)
					return l;
			}
			throw new IllegalArgumentException("Leg not found. Amputated?");
		}

	}

	public SelectPublication(ObjectMode mode, MyJFrame caller, int id) {
		super("Select a Publication");
		this.mode = mode;
		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setPreferredSize(new Dimension(1000, 600));
		JTextField searchTextField = new JTextField();
		searchTextField.setBorder(new EmptyBorder(5, 5, 5, 5));
		searchTextField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				switch (mode) {
				case PUBLICATION:
					allObjects = DatabaseHelper.searchForPublication(searchTextField.getText());
					break;
				case INPROCEEDINGS:
					allObjects = DatabaseHelper.searchForInProceedings(searchTextField.getText());

					break;
				case PROCEEDINGS:
					allObjects = DatabaseHelper.searchForProceedings(searchTextField.getText());

					break;
				case PERSON:
					allObjects = DatabaseHelper.searchForPeople(searchTextField.getText());
					break;

				default:
					allObjects = DatabaseHelper.searchForPublication(searchTextField.getText());

					break;
				}
				pageNumber = 0;
				reloadTable();

			}
		});

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipadx = 10;
		c.weightx = 1;
		c.gridwidth = 6;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(searchTextField, c);

		switch (mode) {
		case PUBLICATION:
			allObjects = new ArrayList<Publication>(DatabaseHelper.getAllPublications());
			break;
		case INPROCEEDINGS:
			allObjects = new ArrayList<Publication>(DatabaseHelper.getAllInProceedings());

			break;
		case PROCEEDINGS:
			allObjects = new ArrayList<Publication>(DatabaseHelper.getAllProceedings());

			break;
		case PERSON:
			allObjects = new ArrayList<Person>(DatabaseHelper.getAllPeople());

			break;
		default:
			allObjects = new ArrayList<Publication>(DatabaseHelper.getAllPublications());

			break;
		}

		currentObjects = allObjects.subList(0, itemsPerPageIndex.getNumber());
		switch (mode) {
		case PUBLICATION:
		case INPROCEEDINGS:
		case PROCEEDINGS:
			tableModel = new PublicationTableModel((List<Publication>) currentObjects);
			break;
		case PERSON:
			tableModel = new PersonTableModel((List<Person>) currentObjects);
			break;
		default:
			tableModel = new PublicationTableModel((List<Publication>) currentObjects);

			break;
		}

		table = new JTable(tableModel) {
			public boolean editCellAt(int row, int column, java.util.EventObject e) {
				return false;
			}
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					DomainObject publications = currentObjects.get(row);
					caller.selectedObject(publications, id);
					SelectPublication.this.dispatchEvent(new WindowEvent(SelectPublication.this, WindowEvent.WINDOW_CLOSING));

				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 8;
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(scrollPane, c);

		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				switch (mode) {
				case PUBLICATION:
					allObjects = DatabaseHelper.searchForPublication(searchTextField.getText());
					break;
				case INPROCEEDINGS:
					allObjects = DatabaseHelper.searchForInProceedings(searchTextField.getText());

					break;
				case PROCEEDINGS:
					allObjects = DatabaseHelper.searchForProceedings(searchTextField.getText());

					break;
				case PERSON:
					allObjects = DatabaseHelper.searchForPeople(searchTextField.getText());

					break;
				default:
					allObjects = DatabaseHelper.searchForPublication(searchTextField.getText());

					break;
				}

				pageNumber = 0;
				reloadTable();
			}
		});

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 3;
		c.gridx = 6;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(searchButton, c);

		numberOfItemsLabel = new JLabel("1-50 of " + String.valueOf(allObjects.size()) + " items");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(numberOfItemsLabel, c);

		JComboBox petList = new JComboBox(getItemsPerPageArray());
		petList.setSelectedIndex(itemsPerPageIndex.getIndex());
		petList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox combo = (JComboBox) e.getSource();
				pageNumber = 0;
				itemsPerPageIndex = ItemsPerPage.getEnumForInt(combo.getSelectedIndex());
				reloadTable();
			}
		});

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(petList, c);

		BasicArrowButton prevPageButton = new BasicArrowButton(BasicArrowButton.WEST);
		prevPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (pageNumber > 0) {

					pageNumber--;
					reloadTable();
				}
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridwidth = 1;
		c.gridx = 3;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(prevPageButton, c);

		JLabel label2 = new JLabel("Page");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 4;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(label2, c);

		pageTextField = new JTextField();
		pageTextField.setText("1");
		pageTextField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				int newPage = Integer.parseInt(pageTextField.getText());
				if (newPage > 0 && numberOfPages() >= newPage) {
					pageNumber = newPage - 1;
					reloadTable();
				} else {
					pageTextField.setText(String.valueOf(pageNumber + 1));
				}

			}
		});

		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipadx = 10;
		c.weightx = 1;
		c.gridwidth = 1;
		c.gridx = 5;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(pageTextField, c);

		numberOfPagesLabel = new JLabel("of " + String.valueOf(numberOfPages()));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 6;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(numberOfPagesLabel, c);

		BasicArrowButton nextPageButton = new BasicArrowButton(BasicArrowButton.EAST);
		nextPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (numberOfPages() > pageNumber + 1) {

					pageNumber++;
					reloadTable();
				}
			}
		});
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.gridwidth = 1;
		c.gridx = 7;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(nextPageButton, c);

		setContentPane(contentPane);
		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		// table.setAutoCreateRowSorter(true);
		table.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				int col = table.columnAtPoint(e.getPoint());
				String name = table.getColumnName(col);

				switch (mode) {
				case PERSON:
		        	switch(col){
	        		case 0:
						nTitleClicked++;
	        			if(nTitleClicked%2 == 0){
					        Collections.sort((List<Person>)allObjects, new Comparator<Person>() {
					            @Override
					            public int compare(Person o1, Person o2) {
					                return o1.getName().compareTo(o2.getName());
					            }
					        });
	        			}
	        			else{
					        Collections.sort((List<Person>)allObjects, new Comparator<Person>() {
					            @Override
					            public int compare(Person o1, Person o2) {
					                return o2.getName().compareTo(o1.getName());
					            }
					        });

	        			}
				        break;
	        		case 1 :
	        			nYearClicked++;
	        		
	        			if(nYearClicked%2 == 0){
					        Collections.sort((List<Person>)allObjects, new Comparator<Person>() {
					            @Override
					            public int compare(Person o1, Person o2) {
					                return ((Integer)o1.getAuthoredPublications().size()).compareTo((Integer)o2.getAuthoredPublications().size());
					            }
					        });
	        			}
	        			else{
					        Collections.sort((List<Person>)allObjects, new Comparator<Person>() {
					            @Override
					            public int compare(Person o1, Person o2) {
					                return ((Integer)o2.getAuthoredPublications().size()).compareTo((Integer)o1.getAuthoredPublications().size());
					            }
					        });

	        			}

	        			break;
	        		case 2 :
	        			nEEClicked++;
		        		
	        			if(nEEClicked%2 == 0){
					        Collections.sort((List<Person>)allObjects, new Comparator<Person>() {
					            @Override
					            public int compare(Person o1, Person o2) {
					                return ((Integer)o1.getEditedPublications().size()).compareTo((Integer)o2.getEditedPublications().size());
					            }
					        });
	        			}
	        			else{
					        Collections.sort((List<Person>)allObjects, new Comparator<Person>() {
					            @Override
					            public int compare(Person o1, Person o2) {
					                return ((Integer)o2.getEditedPublications().size()).compareTo((Integer)o1.getEditedPublications().size());
					            }
					        });

	        			}


	        			break;

	        		default :
	        			
	        			break;
	        	}

					break;
				case PUBLICATION:
				case INPROCEEDINGS:
				case PROCEEDINGS:
				default:
					switch (col) {
					case 0:
						nTitleClicked++;
						if (nTitleClicked % 2 == 0) {
							Collections.sort((List<Publication>) allObjects, new Comparator<Publication>() {
								@Override
								public int compare(Publication o1, Publication o2) {
									return o1.getTitle().compareTo(o2.getTitle());
								}
							});
						} else {
							Collections.sort((List<Publication>) allObjects, new Comparator<Publication>() {
								@Override
								public int compare(Publication o1, Publication o2) {
									return o2.getTitle().compareTo(o1.getTitle());
								}
							});

						}
						break;
					case 1:
						nYearClicked++;
						if (nYearClicked % 2 == 0) {
							Collections.sort((List<Publication>) allObjects, new Comparator<Publication>() {
								@Override
								public int compare(Publication o1, Publication o2) {
									return o1.getYear().compareTo(o2.getYear());
								}
							});
						} else {
							Collections.sort((List<Publication>) allObjects, new Comparator<Publication>() {
								@Override
								public int compare(Publication o1, Publication o2) {
									return o2.getYear().compareTo(o1.getYear());
								}
							});

						}
						break;
					case 2:
						nEEClicked++;
						if (nEEClicked % 2 == 0) {
							Collections.sort((List<Publication>) allObjects, new Comparator<Publication>() {
								@Override
								public int compare(Publication o1, Publication o2) {
									return o1.getElectronicEdition().compareTo(o2.getElectronicEdition());
								}
							});
						} else {
							Collections.sort((List<Publication>) allObjects, new Comparator<Publication>() {
								@Override
								public int compare(Publication o1, Publication o2) {
									return o2.getElectronicEdition().compareTo(o1.getElectronicEdition());
								}
							});

						}
						break;

					default:

						break;
					}

					break;

				}
				System.out.println("reload");
				reloadTable();

				System.out.println("Column index selected " + col + " " + name);
			}
		});

		/*
		 * JButton btnPublicationUpdateFrame = new JButton("Search"); btnPublicationUpdateFrame.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent arg0) { // call the object of PublicationsWindow and set visible true PublicationsUpdateWindow frame = new PublicationsUpdateWindow(); frame.setVisible(true); setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); } }); // modify button btnPublicationUpdateFrame.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12)); btnPublicationUpdateFrame.setBounds(100, 200, 200, 25); contentPane.add(btnPublicationUpdateFrame);
		 * 
		 */

		/*
		 * // insert code for sorting here...
		 * 
		 * //add(new JScrollPane(table), BorderLayout.CENTER); JPanel listPanel = new JPanel(); listPanel.setBorder(new EmptyBorder(5, 5, 5, 5)); listPanel.setPreferredSize(new Dimension(5000,5000)); JScrollPane scrollPane = new JScrollPane(table); scrollPane.setPreferredSize(new Dimension(4000,4000)); listPanel.add(scrollPane,BorderLayout.PAGE_END); contentPane.add(listPanel); //contentPane.add(new JScrollPane(table) ); pack(); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); setLocationRelativeTo(null); table.setAutoCreateRowSorter(true);
		 */
	}

	public static String[] getItemsPerPageArray() {
		ItemsPerPage[] states = ItemsPerPage.values();
		String[] names = new String[states.length];

		for (int i = 0; i < states.length; i++) {
			names[i] = states[i].getString();
		}

		return names;
	}

	public List<Publication> createListPublications() {

		Collection<Publication> allPublications = DatabaseHelper.getAllPublications();
		return new ArrayList<Publication>(allPublications);

		/*
		 * List<Publication> listPublications = new ArrayList<Publication>();
		 * 
		 * System.out.println(allPublications); // TODO add real data Publication p1 = new Publication(1); Publication p2 = new Publication(2); Publication p3 = new Publication(3);
		 * 
		 * listPublications.add(p1); listPublications.add(p2); listPublications.add(p3);
		 */
	}

	public void reloadTable() {
		int size = allObjects.size();
		if (itemsPerPageIndex == ItemsPerPage.ALL) {
			numberOfPagesLabel.setText("of 1");
			numberOfItemsLabel.setText("1 - " + size + " of " + size + " items");
			pageTextField.setText("1");

		} else {
			numberOfPagesLabel.setText("of " + String.valueOf(numberOfPages()));
			numberOfItemsLabel.setText(String.valueOf(pageNumber * itemsPerPageIndex.getNumber() + 1) + " - " + String.valueOf((int) Math.min(size, (pageNumber + 1) * itemsPerPageIndex.getNumber())) + " of " + String.valueOf(allObjects.size()) + " items");
			pageTextField.setText(String.valueOf(pageNumber + 1));
		}
		currentObjects = allObjects.subList(itemsPerPageIndex.getNumber() * pageNumber, Math.min(itemsPerPageIndex.getNumber() * (pageNumber + 1), size));
		
		switch (mode) {
		case PERSON:
			((PersonTableModel)tableModel).changeData((List<Person>)currentObjects);
			break;
		case PUBLICATION:
		case INPROCEEDINGS:
		case PROCEEDINGS:
		default:
			((PublicationTableModel)tableModel).changeData((List<Publication>)currentObjects);

			break;
		}


	}

	private int numberOfPages() {
		if (allObjects.size() < itemsPerPageIndex.getNumber()) {
			return 1;
		} else {
			return (int) Math.floor(allObjects.size() / itemsPerPageIndex.getNumber()) + 1;
		}
	}

	public class PublicationComparatorTitleAscend implements Comparator<Publication> {
		@Override
		public int compare(Publication o1, Publication o2) {
			return o1.getTitle().compareTo(o2.getTitle());
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new PublicationsWindow().setVisible(true);
			}
		});

	}
}