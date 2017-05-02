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

import gui.PersonWindow.ItemsPerPage;
import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Proceedings;
import infsysProj.infsysProj.Publication;
import infsysProj.infsysProj.Conference;
import infsysProj.infsysProj.Publisher;

public class PublisherWindow extends JFrame{

	private JTable table;
	private JPanel contentPane;
	private ItemsPerPage itemsPerPageIndex = ItemsPerPage.FIFTY;
	List<Publisher> allPublisher;
	List<Publisher> currentPublisher;
	PublisherTableModel tableModel;
	private int pageNumber = 0;
	JTextField pageTextField;
	JLabel numberOfPagesLabel;
	JLabel numberOfItemsLabel;
	int nNameClicked = 0;
	int nPublicationsClicked = 0;
	
	public static enum ItemsPerPage {
	    TWENTY("20 items per page",20,0), 
	    FIFTY("50 items per page",50,1),
	    HUNDRED("100 items per page",100,2),
	    TWOHUNDRED("200 items per page",200,3),
	    FIVEHUNDRED("500 items per page",500,4),
	    THOUSAND("1000 items per page",1000,5),
	    ALL("All items",Integer.MAX_VALUE,6);

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
	            if (l.index == legIndex) return l;
	        }
	        throw new IllegalArgumentException("Leg not found. Amputated?");
	     }
	}
	
	public PublisherWindow(){
		super("Publisher window");
		
		
		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setPreferredSize(new Dimension(1000,600));
		JTextField searchTextField = new JTextField();
		searchTextField.setBorder(new EmptyBorder(5, 5, 5, 5));
		searchTextField.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){
				System.out.println(searchTextField.getText());
				allPublisher = DatabaseHelper.get().searchForPublisher(searchTextField.getText());
				pageNumber = 0;
				reloadTable();
            	
            }});
		GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.ipadx = 10;
	    c.weightx = 1;
	    c.gridwidth = 5;
	    c.gridx = 0;
	    c.gridy = 1;
	    c.insets = new Insets(5,5,5,5);
	    contentPane.add( searchTextField, c );
	    
	    allPublisher = new ArrayList<Publisher>(DatabaseHelper.get().getAllPublisher());
		currentPublisher = allPublisher.subList(0, itemsPerPageIndex.getNumber());
		tableModel = new PublisherTableModel(currentPublisher);
		table = new JTable(tableModel)/*{
	         public boolean editCellAt(int row, int column, java.util.EventObject e) {
	             return false;
	          }
	       }*/;
	      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    /*  table.addMouseListener(new MouseAdapter() {
	          public void mouseClicked(MouseEvent e) {
	             if (e.getClickCount() == 2) {
	            	 JTable target = (JTable) e.getSource();
	                 int row = target.getSelectedRow();
	            	 Person person = currentPeople.get(row);
	            	 PersonDetail textFrame = new PersonDetail(person, getMe());
	                textFrame.setVisible(true);
	             }
	          }
	       });*/
	      
	      JScrollPane scrollPane = new JScrollPane(table);		
		    c.fill = GridBagConstraints.BOTH;
		    c.weightx = 1;
		    c.weighty = 1;
		    c.gridwidth = 8;
		    c.gridx = 1;
		    c.gridy = 2;
		    c.insets = new Insets(5,5,5,5);
		    contentPane.add(scrollPane, c);
		    

		    JButton searchButton = new JButton("Search");
			searchButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					System.out.println(searchTextField.getText());
					allPublisher = DatabaseHelper.get().searchForPublisher(searchTextField.getText());
					pageNumber = 0;
					reloadTable();
				}
			});
			
			c.fill = GridBagConstraints.HORIZONTAL;
		    c.weightx = 1;
		    c.weighty = 0;
		    c.gridwidth = 3;
		    c.gridx = 5;
		    c.gridy = 1;
		    c.insets = new Insets(5,5,5,5);
		    contentPane.add(searchButton, c);
		    
		    /*JButton addButton = new JButton("Add");
			addButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
	        	 AddPerson textFrame = new AddPerson(getMe());
	        	 textFrame.setVisible(true);

				}
			});

		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.weightx = 1;
		    c.weighty = 0;
		    c.gridwidth = 1;
		    c.gridx = 7;
		    c.gridy = 1;
		    c.insets = new Insets(5,5,5,5);
		    contentPane.add(addButton, c);
*/

		    
		    numberOfItemsLabel = new JLabel("1-50 of " + String.valueOf(allPublisher.size())+ " items");
		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.weightx = 1;
		    c.weighty = 0;
		    c.gridwidth = 1;
		    c.gridx = 1;
		    c.gridy = 3;
		    c.insets = new Insets(5,5,5,5);
		    contentPane.add(numberOfItemsLabel, c);
		    
		    JComboBox petList = new JComboBox(getItemsPerPageArray());
		    petList.setSelectedIndex(itemsPerPageIndex.getIndex());
		    petList.addActionListener( new ActionListener(){
	            public void actionPerformed(ActionEvent e){
	                JComboBox combo = (JComboBox)e.getSource();
	                pageNumber = 0;
	                itemsPerPageIndex = ItemsPerPage.getEnumForInt(combo.getSelectedIndex());
	                reloadTable();
	                }
	        }            
	);
		    
		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.weightx = 1;
		    c.weighty = 0;
		    c.gridwidth = 1;
		    c.gridx = 2;
		    c.gridy = 3;
		    c.insets = new Insets(5,5,5,5);
		    contentPane.add(petList, c);

		    BasicArrowButton prevPageButton = new BasicArrowButton(BasicArrowButton.WEST);
		    prevPageButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(pageNumber > 0){
						
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
		    c.insets = new Insets(5,5,5,5);
		    contentPane.add(prevPageButton, c);
		    
		    JLabel label2 = new JLabel("Page");
		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.weightx = 1;
		    c.weighty = 0;
		    c.gridwidth = 1;
		    c.gridx = 4;
		    c.gridy = 3;
		    c.insets = new Insets(5,5,5,5);
		    contentPane.add(label2, c);

		    pageTextField = new JTextField();
			pageTextField.setText("1");
			pageTextField.addActionListener(new ActionListener(){

	            public void actionPerformed(ActionEvent e){

	            		int newPage = Integer.parseInt(pageTextField.getText());
	                    if(newPage>0 && numberOfPages()>=newPage){
	                    	pageNumber = newPage-1;
	                    	reloadTable();
	                    }
	                    else{
	                    	pageTextField.setText(String.valueOf(pageNumber+1));
	                    }

	            }});

		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.ipadx = 10;
		    c.weightx = 1;
		    c.gridwidth = 1;
		    c.gridx = 5;
		    c.gridy = 3;
		    c.insets = new Insets(5,5,5,5);
		    contentPane.add( pageTextField, c );
		    
		    numberOfPagesLabel = new JLabel("of " + String.valueOf(numberOfPages()));
		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.weightx = 1;
		    c.weighty = 0;
		    c.gridwidth = 1;
		    c.gridx = 6;
		    c.gridy = 3;
		    c.insets = new Insets(5,5,5,5);
		    contentPane.add(numberOfPagesLabel, c);
		    
		    BasicArrowButton nextPageButton = new BasicArrowButton(BasicArrowButton.EAST);
		    nextPageButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(numberOfPages()> pageNumber+1){

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
		    c.insets = new Insets(5,5,5,5);
		    contentPane.add(nextPageButton, c);
		    
		    setContentPane(contentPane);
			pack();
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLocationRelativeTo(null);
			//table.setAutoCreateRowSorter(true);
			table.getTableHeader().addMouseListener(new MouseAdapter() {
			    @Override
			    public void mouseClicked(MouseEvent e) {
			    	
			        int col = table.columnAtPoint(e.getPoint());
			        String name = table.getColumnName(col);
			        	switch(col){
			        		case 0:
			        			nNameClicked++;
			        			if(nNameClicked%2 == 0){
							        Collections.sort(allPublisher, new Comparator<Publisher>() {
							            @Override
							            public int compare(Publisher o1, Publisher o2) {
							                return o1.getName().compareTo(o2.getName());
							            }
							        });
			        			}
			        			else{
							        Collections.sort(allPublisher, new Comparator<Publisher>() {
							            @Override
							            public int compare(Publisher o1, Publisher o2) {
							                return o2.getName().compareTo(o1.getName());
							            }
							        });

			        			}
						        break;
			        		case 1 :
			        			nPublicationsClicked++;
			        		
			        			if(nPublicationsClicked%2 == 0){
							        Collections.sort(allPublisher, new Comparator<Publisher>() {
							            @Override
							            public int compare(Publisher o1, Publisher o2) {
							                return ((Integer)o1.getPublications().size()).compareTo((Integer)o2.getPublications().size());
							            }
							        });
			        			}
			        			else{
							        Collections.sort(allPublisher, new Comparator<Publisher>() {
							            @Override
							            public int compare(Publisher o1, Publisher o2) {
							                return ((Integer)o2.getPublications().size()).compareTo((Integer)o1.getPublications().size());
							            }
							        });

			        			}

			        			break;

			        		default :
			        			
			        			break;
			        	}
			        	System.out.println("reload");
				        reloadTable();

			        

			        System.out.println("Column index selected " + col + " " + name);
			    }
			});

	}
	public static String[] getItemsPerPageArray() {
	    ItemsPerPage[] states = ItemsPerPage.values();
	    String[] names = new String[states.length];

	    for (int i = 0; i < states.length; i++) {
	        names[i] = states[i].getString();
	    }

	    return names;
	}
	
	public void reloadDataFromDatabase(){
		allPublisher = new ArrayList<Publisher>(DatabaseHelper.get().getAllPublisher());
		reloadTable();
	}
	
	public void reloadTable(){
		int size = allPublisher.size();
		if(itemsPerPageIndex == ItemsPerPage.ALL){
	        numberOfPagesLabel.setText("of 1");
	        numberOfItemsLabel.setText("1 - " + size + " of " + size + " items");
	        pageTextField.setText("1");

		}
		else{
        numberOfPagesLabel.setText("of " + String.valueOf(numberOfPages()));
        numberOfItemsLabel.setText(String.valueOf(pageNumber*itemsPerPageIndex.getNumber() + 1) + " - " + String.valueOf((int)Math.min(size,(pageNumber +1)*itemsPerPageIndex.getNumber())) + " of " + String.valueOf(allPublisher.size()) + " items");
		pageTextField.setText(String.valueOf(pageNumber+1));
		}
        currentPublisher = allPublisher.subList(itemsPerPageIndex.getNumber()*pageNumber, Math.min(itemsPerPageIndex.getNumber()*(pageNumber+1),size));
		tableModel.changeData(currentPublisher);

	}
	
	private int numberOfPages(){
		if(allPublisher.size()< itemsPerPageIndex.getNumber()){
			return 1;
		}
		else{
			return (int) Math.floor(allPublisher.size()/itemsPerPageIndex.getNumber())+1;
		}
	}
	public PublisherWindow getMe(){
		return this;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new PublicationsWindow().setVisible(true);
			}
		});

	}
}
