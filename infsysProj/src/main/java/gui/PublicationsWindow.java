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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Publication;

/**
 * This program demonstrates how to sort rows in a table.
 * 
 * @author www.codejava.net resource: http://www.codejava.net/java-se/swing/6-techniques-for-sorting-jtable-you-should-know
 */
public class PublicationsWindow extends JFrame {
	private JTable table;
	private JPanel contentPane;

	public PublicationsWindow() {
		super("Publications Window");

				
		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setPreferredSize(new Dimension(1000,600));
		JTextField searchTextField = new JTextField();
		searchTextField.setBorder(new EmptyBorder(5, 5, 5, 5));


	    GridBagConstraints c = new GridBagConstraints();
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.ipadx = 10;
	    c.weightx = 1;
	    c.gridwidth = 6;
	    c.gridx = 0;
	    c.gridy = 1;
	    c.insets = new Insets(5,5,5,5);
	    contentPane.add( searchTextField, c );
	    
		//List<Publication> listPublications = createListPublications();
		PublicationTableModel tableModel = new PublicationTableModel();
		table = new JTable(tableModel);

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
				List<Publication> filteredList = DatabaseHelper.searchFor(searchTextField.getText());
				tableModel.changeData(filteredList);
			}
		});

	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.weightx = 1;
	    c.weighty = 0;
	    c.gridwidth = 3;
	    c.gridx = 6;
	    c.gridy = 1;
	    c.insets = new Insets(5,5,5,5);
	    contentPane.add(searchButton, c);


	    
	    JLabel label1 = new JLabel("44000-44100 of 66801 items");
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.weightx = 1;
	    c.weighty = 0;
	    c.gridwidth = 1;
	    c.gridx = 1;
	    c.gridy = 3;
	    c.insets = new Insets(5,5,5,5);
	    contentPane.add(label1, c);

	    String[] petStrings = { "100 items per page", "200 items per page", "300 items per page", "400 items per page", "500 items per page" };

	    JComboBox petList = new JComboBox(petStrings);
	    petList.setSelectedIndex(4);
	    //petList.addActionListener(this);
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
				System.out.println(searchTextField.getText());
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


		JTextField pageTextField = new JTextField();

	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.ipadx = 10;
	    c.weightx = 1;
	    c.gridwidth = 1;
	    c.gridx = 5;
	    c.gridy = 3;
	    c.insets = new Insets(5,5,5,5);
	    contentPane.add( pageTextField, c );

	    
	    
	    JLabel label3 = new JLabel("of 840");
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.weightx = 1;
	    c.weighty = 0;
	    c.gridwidth = 1;
	    c.gridx = 6;
	    c.gridy = 3;
	    c.insets = new Insets(5,5,5,5);
	    contentPane.add(label3, c);

	    
	    BasicArrowButton nextPageButton = new BasicArrowButton(BasicArrowButton.EAST);
	    prevPageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(searchTextField.getText());
			}
		});
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.weightx = 1;
	    c.gridwidth = 1;
	    c.gridx = 7;
	    c.gridy = 3;
	    c.insets = new Insets(5,5,5,5);
	    contentPane.add(nextPageButton, c);


		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		table.setAutoCreateRowSorter(true);


		/*
		JButton btnPublicationUpdateFrame = new JButton("Search");
		btnPublicationUpdateFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// call the object of PublicationsWindow and set visible true
				PublicationsUpdateWindow frame = new PublicationsUpdateWindow();
				frame.setVisible(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		// modify button
		btnPublicationUpdateFrame.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
		btnPublicationUpdateFrame.setBounds(100, 200, 200, 25);
		contentPane.add(btnPublicationUpdateFrame);
		
		*/
	    
	    
		setContentPane(contentPane);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

/*
		// insert code for sorting here...

		//add(new JScrollPane(table), BorderLayout.CENTER);
		JPanel listPanel = new JPanel();
		listPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		listPanel.setPreferredSize(new Dimension(5000,5000));
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(4000,4000));
		listPanel.add(scrollPane,BorderLayout.PAGE_END);
		contentPane.add(listPanel);
		//contentPane.add(new JScrollPane(table) );
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		table.setAutoCreateRowSorter(true);
		*/
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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new PublicationsWindow().setVisible(true);
			}
		});

	}
}