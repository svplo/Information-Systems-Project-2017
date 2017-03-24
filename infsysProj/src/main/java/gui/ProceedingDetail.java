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
import infsysProj.infsysProj.Publication;
import infsysProj.infsysProj.Proceedings;

public class ProceedingDetail extends JFrame {

	private JPanel contentPane;
	private Proceedings proceeding;
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

	public ProceedingDetail(Proceedings proceeding) {
		super("Detailed Preceeding");
		this.proceeding = proceeding;

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

		JButton Update = new JButton("Update");
		Update.setEnabled(false);
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(Update, c);

		JButton Delete = new JButton("Delete");
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(Delete, c);
		
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
		txtTitle.setText(proceeding.getTitle());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtTitle, c);
		
		JLabel lblYear = new JLabel("Year");
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblYear, c);

		JTextField txtYear = new JTextField();
		txtYear.setEditable(false);
		txtYear.setText(proceeding.getYear().toString());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtYear, c);
		
		JLabel lblElect = new JLabel("Electronic edition");
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblElect, c);

		JTextField txtElect = new JTextField();
		txtElect.setEditable(false);
		txtElect.setText(proceeding.getElectronicEdition());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtElect, c);

		JLabel lblNote = new JLabel("Note");
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblNote, c);

		JTextField txtNote = new JTextField();
		txtNote.setEditable(false);
		txtNote.setText(proceeding.getNote());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtNote, c);

		JLabel lblNumber = new JLabel("Number");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblNumber, c);

		JTextField txtNumber = new JTextField();
		txtNumber.setEditable(false);
		txtNumber.setText(proceeding.getNumber()+"");
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 5;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtNumber, c);

/*		JLabel lblPublisher = new JLabel("Publisher");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 6;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblPublisher, c);

		JTextField txtPublisher = new JTextField();
		txtPublisher.setEditable(false);
		txtPublisher.setText(proceeding.getPublisher().getName());
		c.fill = GridBagConstraints.BOTH;
		c.ipadx = 10;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 6;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtPublisher, c);*/

		JLabel lblVolume = new JLabel("Volume");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 7;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblVolume, c);

		JTextField txtVolume = new JTextField();
		txtVolume.setEditable(false);
		txtVolume.setText(proceeding.getVolume());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 7;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtVolume, c);

		JLabel lblISBN = new JLabel("ISBN");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 8;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblISBN, c);

		JTextField txtISBN = new JTextField();
		txtISBN.setEditable(false);
		txtISBN.setText(proceeding.getIsbn());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 8;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtISBN, c);

		/*JLabel lblSeries = new JLabel("Series");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 9;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblSeries, c);

		JTextField txtSeries = new JTextField();
		txtSeries.setEditable(false);
		txtSeries.setText(proceeding.getSeries().getName());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 9;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtSeries, c);*/

/*		JLabel lblConfEdition = new JLabel("Conference edition");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 10;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblConfEdition, c);

		JTextField txtConfEdition = new JTextField();
		txtConfEdition.setEditable(false);
		txtConfEdition.setText(proceeding.getConferenceEdition().getConference().getName());
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 10;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtConfEdition, c);*/

/*		JLabel lblInProceedings = new JLabel("InProceedings");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 11;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(lblInProceedings, c);

		JTextField txtInProceedings = new JTextField();
		txtInProceedings.setEditable(false);
		txtInProceedings.setText(format(proceeding.getInProceedings()));
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 5;
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 11;
		c.insets = new Insets(5, 5, 5, 5);
		contentPane.add(txtInProceedings, c);*/
		
		ActiveUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ActiveUpdate.isSelected()){
/*					Update.setEnabled(true);
					txtNote.setEditable(true);
					txtNumber.setEditable(true);
					txtPublisher.setEditable(true);
					txtVolume.setEditable(true);
					txtISBN.setEditable(true);*/
/*					txtSeries.setEditable(true);
					txtConfEdition.setEditable(true);
//					txtInProceedings.setEditable(true);
*/				}
				else{
/*					Update.setEnabled(false);
					txtNote.setEditable(false);
					txtNumber.setEditable(false);
					txtPublisher.setEditable(false);
					txtVolume.setEditable(false);
					txtISBN.setEditable(false);*/
/*					txtSeries.setEditable(false);
					txtConfEdition.setEditable(false);
//					txtInProceedings.setEditable(false);
*/				}

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
		  String s = c.stream().map(Object::toString).collect(Collectors.joining(","));
		  return String.format("[%s]", s);
		}

}
