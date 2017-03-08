package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import infsysProj.infsysProj.Publication;

//resource: https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#simple
public class PublicationListUI extends JList<Publication>{

		   private JFrame mainFrame;
		   private JLabel headerLabel;
		   private JLabel statusLabel;
		   private JPanel controlPanel;
		   
		   public PublicationListUI(){
		      prepareGUI();
		   }
			 
		   public static void main(String[] args){
			   PublicationListUI  publicationListUi = new PublicationListUI();      
		      publicationListUi.showList();
		   }
		   
		   private void prepareGUI(){
		      mainFrame = new JFrame("Publications");
		      mainFrame.setSize(750,750);
		      mainFrame.setLayout(new GridLayout(3, 1));
		      
		      mainFrame.addWindowListener(new WindowAdapter() {
		         public void windowClosing(WindowEvent windowEvent){
		            System.exit(0);
		         }        
		      });    
		      headerLabel = new JLabel("", JLabel.CENTER);        
		      statusLabel = new JLabel("",JLabel.CENTER);    
		      statusLabel.setSize(350,100);

		      controlPanel = new JPanel();
		      controlPanel.setLayout(new FlowLayout());

		      mainFrame.add(headerLabel);
		      mainFrame.add(controlPanel);
		      mainFrame.add(statusLabel);
		      mainFrame.setVisible(true);  
		   }
		   private void showList(){                                       
		      headerLabel.setText("Publication Search"); 
		      
		      String[] columnNames = {
		    		  "Publication Title",
                      "Year",
                      "Electronic Edition",
                      "Details"};
		      
		      //Initialization of the data in the table
		      Object[][] data = {
		    		    {"Kathy", "Smith",
		    		     "Snowboarding", new Integer(5), new Boolean(false)},
		    		    {"John", "Doe",
		    		     "Rowing", new Integer(3), new Boolean(true)},
		    		    {"Sue", "Black",
		    		     "Knitting", new Integer(2), new Boolean(false)},
		    		    {"Jane", "White",
		    		     "Speed reading", new Integer(20), new Boolean(true)},
		    		    {"Joe", "Brown",
		    		     "Pool", new Integer(10), new Boolean(false)}
		    		};

		      JTable table = new JTable(data, columnNames);
		      JScrollPane scrollPane = new JScrollPane(table);
		      table.setFillsViewportHeight(true);

		      //TODO add header to table
//		      container.setLayout(new BorderLayout());
//		      container.add(table.getTableHeader(), BorderLayout.PAGE_START);
//		      container.add(table, BorderLayout.CENTER);
          
		      JButton showButton = new JButton("Show");

		      showButton.addActionListener(new ButtonClickListener());
  
		      controlPanel.add(showButton);    
		      controlPanel.add(table);
			  
		      mainFrame.setVisible(true);             
		   }
		   
	private class ButtonClickListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();  
		       
			if( command.equals( "OK" ))  {
		        statusLabel.setText("Ok Button clicked.");
			} else if( command.equals( "Submit" ) )  {
		           statusLabel.setText("Submit Button clicked."); 
			} else if ( command.equals( "Cancel" ) ){
 		     	 statusLabel.setText("Cancel Button clicked.");
			}
		}		
	}
}
