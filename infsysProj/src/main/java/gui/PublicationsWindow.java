package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
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

		List<Publication> listPublications = createListPublications();
		TableModel tableModel = new PublicationTableModel(listPublications);
		table = new JTable(tableModel);
		table.setBounds(0, 0, 2000, 2000);
		contentPane = new JPanel();

		JButton btnPublicationUpdateFrame = new JButton("Publication Update");
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

		// insert code for sorting here...

		add(new JScrollPane(table), BorderLayout.CENTER);

		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		table.setAutoCreateRowSorter(true);
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