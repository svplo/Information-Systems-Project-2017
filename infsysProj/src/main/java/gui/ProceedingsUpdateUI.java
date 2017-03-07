package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import infsysProj.infsysProj.Proceedings;

/* Java GUI using swing
* checkout resource: http://www.developer.com/java/creating-a-jdbc-gui-application.html
* this class should be used to delete, update and create proceedings
*/

public class ProceedingsUpdateUI extends JPanel {
	
	 //Create Fields
	 private JTextField mdateField = new JTextField(10);
	 private JTextField editorField = new JTextField(30);
	 private JTextField titleField = new JTextField(30);
	 private JTextField booktitleField = new JTextField(30);
	 private JTextField publisherField = new JTextField(30);
	 private JTextField seriesField = new JTextField(30);
	 private JTextField yearField = new JTextField(30);
	 private JTextField isbnField = new JTextField(30);
	 private JTextField urlField = new JTextField(30);
	 
	 //Create Buttons
     private JButton createButton = new JButton("Create");
	 private JButton updateButton = new JButton("Update");
	 private JButton deleteButton = new JButton("Delete");
	 private Proceedings proc = new Proceedings();

	 public ProceedingsUpdateUI() {
	      setBorder(new TitledBorder (new EtchedBorder(),"Person Details"));
	      setLayout(new BorderLayout(5, 5));
	      add(initFields(), BorderLayout.NORTH);
	      add(initButtons(), BorderLayout.CENTER);
	      //setFieldData(pers.moveFirst());
	   }

	   private JPanel initButtons() {
	      JPanel panel = new JPanel();
	      panel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
	      
	      panel.add(createButton);
	      createButton.addActionListener(new ButtonHandler());

	      panel.add(updateButton);
	      updateButton.addActionListener(new ButtonHandler());
	      
	      panel.add(deleteButton);
	      deleteButton.addActionListener(new ButtonHandler());
	      
	      return panel;
	   }

	   //which layout?
	   private JPanel initFields() {
	      JPanel panel = new JPanel();
	      panel.setLayout(new BorderLayout());
	      panel.add(new JLabel("Date"), "align label");
	      panel.add(mdateField, "wrap");
	      mdateField.setEnabled(false);
	      panel.add(new JLabel("Editors"), "align label");
	      panel.add(editorField, "wrap");
	      panel.add(new JLabel("Title"), "align label");
	      panel.add(titleField, "wrap");
	      panel.add(new JLabel("Book title"), "align label");
	      panel.add(booktitleField, "wrap");
	      panel.add(new JLabel("Publisher"), "align label");
	      panel.add(publisherField, "wrap");
	      panel.add(new JLabel("Series"), "align label");
	      panel.add(seriesField, "wrap");
	      panel.add(new JLabel("Year"), "align label");
	      panel.add(yearField, "wrap");
	      panel.add(new JLabel("ISBN"), "align label");
	      panel.add(isbnField, "wrap");
	      panel.add(new JLabel("URL"), "align label");
	      panel.add(urlField, "wrap");
	      return panel;
	   }

/*	   private Publication getFieldData() {
*		   Publication p = new Publication();
*	      p.setTitle(titleField.getText());
*	      p.setAuthors(authorsField.getText());
*	      p.setConferences(conferenceField.getText());
*	      p.setProceedings(proceedingsField.getText());
*	      p.setYear(Integer.parseInt(yearField.getText()));
*	      p.setPages(Integer.parseInt(pagesField.getText()));
*	      return p;
*	   }
*
*	   private void setFieldData(Publication p) {
*	      titleField.setText(String.valueOf(p.getTitle()));
*	      authorsField.setCaret(p.getAuthors());
*	      conferenceField.setText(p.getConferences());
*	      proceedingsField.setText(p.getProceedings());
*	      yearField.setText(p.getYear());
*	      pagesField.setText(p.getPages());
*	   }
*
*	   private boolean isEmptyFieldData() {
*	      return (authorsField.getText().trim().isEmpty()
*	         && authorsField.getText().trim().isEmpty()
*	         && conferenceField.getText().trim().isEmpty()
*	         && proceedingsField.getText().trim().isEmpty()
*	         && yearField.getText().trim().isEmpty())
*	         && pagesField.getText().trim().isEmpty());
*	   }
*
*	   private class ButtonHandler implements ActionListener {
*	      @Override
*	      public void actionPerformed(ActionEvent e) {
*	         Publication p = getFieldData();
*	         switch (e.getActionCommand()) {
*	         case "Create":
*	            if (isEmptyFieldData()) {
*	               JOptionPane.showMessageDialog(null,
*	               "Cannot create an empty record");
*	               return;
*	            }
*	            if (pub.create(p) != null)
*	               JOptionPane.showMessageDialog(null,
*	               "New person created successfully.");
*	               createButton.setText("New...");
*	               break;
*	         case "Update":
*	            p.setTitle("");
*	            p.authorsField("");
*	            p.conferenceField("");
*	            p.proceedingsField("");
*	            p.setYear(0);
*	            p.setPages(0);
*	            setFieldData(p);
*	            createButton.setText("Save");
*	            break;
*	         case "Delete":
*	            if (isEmptyFieldData()) {
*	               JOptionPane.showMessageDialog(null,
*	               "Cannot delete an empty record");
*	               return;
*	            }
*	            p = pub.getCurrent();
*	            pub.delete();
*	            JOptionPane.showMessageDialog(
*	               null,"Person with ID:"
*	               + String.valueOf(p.getPersonId()
*	               + " is deleted successfully"));
*	               break;
*	         default:
*	            JOptionPane.showMessageDialog(null, "invalid command");
*	         }
*	      }
*	     }
*/
}
