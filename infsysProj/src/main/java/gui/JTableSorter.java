package gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import infsysProj.infsysProj.Publication;

/**
* This program demonstrates how to sort rows in a table.
* @author www.codejava.net
*
*/
public class JTableSorter extends JFrame {
   private JTable table;

   public JTableSorter() {
       super("JTable Sorting Example");


       List<Publication> listPublications = createListPublications();
       TableModel tableModel = new PublicationTableModel(listPublications);
       table = new JTable(tableModel);

       // insert code for sorting here...

       add(new JScrollPane(table), BorderLayout.CENTER);

       pack();
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       setLocationRelativeTo(null);
       table.setAutoCreateRowSorter(true);
   }

   public List<Publication> createListPublications() {
       List<Publication> listPublications = new ArrayList<>();

       // code to add dummy data 
		Publication p1 = new Publication(1);
		Publication p2 = new Publication(2);
		Publication p3 = new Publication(3);
		   
	  listPublications.add(p1);
	  listPublications.add(p2);
	  listPublications.add(p3);
	  
       return listPublications;
   }

   public static void main(String[] args) {
       SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {
               new JTableSorter().setVisible(true);
           }
       });
       
   }
}