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
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import infsysProj.infsysProj.InProceedings;
import infsysProj.infsysProj.Person;
import infsysProj.infsysProj.Publication;
import infsysProj.infsysProj.Proceedings;

public class PersonDetail extends JFrame {

	private JPanel contentPane;
	private Person person;
	JTextField txtTitle;
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

	public PersonDetail(Person person) {
		super("Detailed Person");
		this.person = person;

		contentPane = new JPanel(new GridBagLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setPreferredSize(new Dimension(500, 500));

		GridBagConstraints c = new GridBagConstraints();

		JRadioButton ActiveUpdate = new JRadioButton("Activate update");

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(ActiveUpdate, c);

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Person newPerson = new Person(txtTitle.getText());
				newPerson.setAuthoredPublications(person.getAuthoredPublications());
				newPerson.setEditedPublications(person.getEditedPublications());
				DatabaseHelper.updatePerson(person.getName(),newPerson);
				closeWindow();
			}
		});

		updateButton.setEnabled(false);
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(updateButton, c);

		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DatabaseHelper.deletePerson(person.getName());
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
		txtTitle.setEditable(false);
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
		

        //load names of InProceedings from Database
        List<String> inProcNames = DatabaseHelper.getAuthoredPublicationsForPerson(person.getName());

		JLabel lblInProceedings = new JLabel("Authored Publications (" + inProcNames.size() + ")");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 10;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblInProceedings, c);

		 //headers for the table
        String[] columns = new String[] {
            "Title"
        };
         
        String[][] data = new String[inProcNames.size()][1];
        for(int i = 0; i < inProcNames.size(); i++){
        	data[i][0] = inProcNames.get(i);
        }
        //create table with data
        JTable table = new JTable(data, columns);
         
        //add the table to the frame
        
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 5;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 10;
		c.insets = new Insets(5, 5, 5, 5);
		//contentPane.add(txtInProceedings, c);
        contentPane.add(new JScrollPane(table),c);

        //load names of InProceedings from Database
        List<String> editedPublications = DatabaseHelper.getEditedPublicationsForPerson(person.getName());

		JLabel editedPubs = new JLabel("Edited Publications (" + editedPublications.size() + ")");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 11;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(editedPubs, c);

		 //headers for the table
        columns = new String[] {
            "Title"
        };
         
        data = new String[editedPublications.size()][1];
        for(int i = 0; i < editedPublications.size(); i++){
        	data[i][0] = editedPublications.get(i);
        }
        //create table with data
        JTable table2 = new JTable(data, columns);
         
        //add the table to the frame
        
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 5;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 11;
		c.insets = new Insets(5, 5, 5, 5);
		//contentPane.add(txtInProceedings, c);
        contentPane.add(new JScrollPane(table2),c);

		
		ActiveUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ActiveUpdate.isSelected()){
					updateButton.setEnabled(true);
					txtTitle.setEditable(true);
				}
				else{
					updateButton.setEnabled(false);
					txtTitle.setEditable(false);
				}

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
	String format(Collection<?> c) {
		  String s = c.stream().map(Object::toString).collect(Collectors.joining("\n"));
		  return String.format("[%s]", s);
		}
	
	private void closeWindow(){
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));

	}

}
