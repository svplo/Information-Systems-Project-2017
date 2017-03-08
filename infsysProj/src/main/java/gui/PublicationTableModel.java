package gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import infsysProj.infsysProj.Publication;

//resource: https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#simple
public class PublicationTableModel extends AbstractTableModel{

		   private JFrame mainFrame;
		   private JLabel headerLabel;
		   private JLabel statusLabel;
		   private JPanel controlPanel;
		   
		   private static final int COLUMN_TITLE       = 0;
		   private static final int COLUMN_YEAR   	   = 1;
		   private static final int COLUMN_EDITION     = 2;
		   private static final int COLUMN_DETAILS     = 3;
		    
		   private String[] columnNames = {
		 		  "Publication Title",
		           "Year",
		           "Electronic Edition",
		           "Details"};
		   
		   private List<Publication> listPublication;
		    
		   public PublicationTableModel(List<Publication> listPublication) {
		       this.listPublication = listPublication;
		        
		       Integer indexCount = 1;
		       for (Publication p : listPublication) {
		           p.setId((indexCount++).toString());
		       }
		   }
		     
		    @Override
		    public String getColumnName(int columnIndex) {
		        return columnNames[columnIndex];
		    }
		     
		   public PublicationTableModel(){
		      prepareGUI();
		   }
			 
//		   public static void main(String[] args){
//			   PublicationListUI  publicationListUi = new PublicationListUI();      
//		      publicationListUi.showList();
//		   }
		   
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
		      
		      //TODO Initialization of the data in the table
				Publication p1 = new Publication(1);
				Publication p2 = new Publication(2);
				Publication p3 = new Publication(3);
				   
			  Publication[] publicationList = {p1, p2, p3};
		      JTable table = new JTable(publicationList.length, columnNames.length);
		      JScrollPane scrollPane = new JScrollPane(table);
		      table.setFillsViewportHeight(true);
          
		      JButton showButton = new JButton("Show");

		      showButton.addActionListener(new ButtonClickListener());
  
		      controlPanel.add(showButton);    
		      controlPanel.add(table);
		      //makes header of table visible
		      controlPanel.add(new JScrollPane(table));
			  
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

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
 
    @Override
    public int getRowCount() {
        return listPublication.size();
    }
     
    @Override
    public String getValueAt(int rowIndex, int columnIndex) {
    	Publication publications = listPublication.get(rowIndex);
    	String returnValue = "";
         
        switch (columnIndex) {
        case COLUMN_TITLE:
            returnValue = publications.getTitle();
            break;
        case COLUMN_YEAR:
            returnValue = publications.getYear().toString();
            break;
        case COLUMN_EDITION:
            returnValue = publications.getElectronicEdition();
            break;
        case COLUMN_DETAILS:
            //go to detailed view
            break;
        default:
            throw new IllegalArgumentException("Invalid column index");
        }
         
        return returnValue;
    }
    //needed for sorting
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (listPublication.isEmpty()) {
            return Object.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }
}
