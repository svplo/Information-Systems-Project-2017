package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import infsysProj.infsysProj.Series;

//resource: https://www.eduonix.com/blog/java-programming-2/learn-to-create-multiple-frames-java/
public class StartWindow extends JFrame {

	private JPanel contentPane;

	public static void main(String[] args) {
		/*
		 * It posts an event (Runnable)at the end of Swings event list and is processed after all other GUI events are processed.
		 */
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartWindow frame = new StartWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StartWindow() {
		setTitle("Start Frame");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 680);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnPublicationTableFrame = new JButton("Publication Table");
		btnPublicationTableFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// call the object of PublicationsWindow and set visible true
				PublicationsWindow frame = new PublicationsWindow();
				frame.setVisible(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		// modify button
		btnPublicationTableFrame.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
		btnPublicationTableFrame.setBounds(100, 200, 200, 25);
		contentPane.add(btnPublicationTableFrame);
		
		JButton btnProceedingsTableFrame = new JButton("Proceedings Table");
		btnProceedingsTableFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// call the object of PublicationsWindow and set visible true
				ProceedingsWindow frame = new ProceedingsWindow();
				frame.setVisible(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		// modify button
		btnProceedingsTableFrame.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
		btnProceedingsTableFrame.setBounds(100, 250, 200, 25);
		contentPane.add(btnProceedingsTableFrame);
		
		JButton btnInProceedingsTableFrame = new JButton("InProceedings Table");
		btnInProceedingsTableFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// call the object of PublicationsWindow and set visible true
				InProceedingsWindow frame = new InProceedingsWindow();
				frame.setVisible(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		// modify button
		btnInProceedingsTableFrame.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
		btnInProceedingsTableFrame.setBounds(100, 300, 200, 25);
		contentPane.add(btnInProceedingsTableFrame);

		
		JButton btnPersonTableFrame = new JButton("Person Table");
		btnPersonTableFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// call the object of PublicationsWindow and set visible true
				PersonWindow frame = new PersonWindow();
				frame.setVisible(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		// modify button
		btnPersonTableFrame.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
		btnPersonTableFrame.setBounds(100, 350, 200, 25);
		contentPane.add(btnPersonTableFrame);
		
		JButton btnConferenceTableFrame = new JButton("Conference Table");
		btnConferenceTableFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// call the object of PublicationsWindow and set visible true
				ConferenceWindow frame = new ConferenceWindow();
				frame.setVisible(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		// modify button
		btnConferenceTableFrame.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
		btnConferenceTableFrame.setBounds(100, 400, 200, 25);
		contentPane.add(btnConferenceTableFrame);

		JButton btnSeriesTableFrame = new JButton("Series Table");
		btnSeriesTableFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// call the object of PublicationsWindow and set visible true
				SeriesWindow frame = new SeriesWindow();
				frame.setVisible(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		// modify button
		btnSeriesTableFrame.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
		btnSeriesTableFrame.setBounds(100, 450, 200, 25);
		contentPane.add(btnSeriesTableFrame);
		
		JButton btnPublisherTableFrame = new JButton("Publisher Table");
		btnPublisherTableFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// call the object of PublicationsWindow and set visible true
				PublisherWindow frame = new PublisherWindow();
				frame.setVisible(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		// modify button
		btnPublisherTableFrame.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
		btnPublisherTableFrame.setBounds(100, 500, 200, 25);
		contentPane.add(btnPublisherTableFrame);
		
		JButton btnConferenceEditionTableFrame = new JButton("Conference Edition Table");
		btnConferenceEditionTableFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// call the object of PublicationsWindow and set visible true
				ConferenceEditionWindow frame = new ConferenceEditionWindow();
				frame.setVisible(true);
				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			}
		});
		// modify button
		btnConferenceEditionTableFrame.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
		btnConferenceEditionTableFrame.setBounds(100, 550, 200, 25);
		contentPane.add(btnConferenceEditionTableFrame);


		// modify label
		JLabel lblStartWindow = new JLabel("Welcome!");
		lblStartWindow.setForeground(Color.BLUE);
		lblStartWindow.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 24));
		lblStartWindow.setBounds(127, 82, 239, 39);
		contentPane.add(lblStartWindow);

		final JButton reloadXMLButton = new JButton("Reload XML");
		reloadXMLButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//XMLParser xmlparser = new XMLParser();
				//xmlparser.parse();
				DatabaseHelper.createDB();
			}
		});
		// modify button
		reloadXMLButton.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
		reloadXMLButton.setBounds(250, 15, 150, 25);
		contentPane.add(reloadXMLButton);
		
		//Queries
		JButton btnQueriesTableFrame = new JButton("Run Queries");
		btnQueriesTableFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO: check if answers are correct
				//Run the queries required for task 1.3
				//DatabaseHelper.query1("conf/acm/ColeMM87");
				
				//diff. to zoodb, since no ordering involved
				//DatabaseHelper.query2("expert system", 0, 3);
				
				//equal to zoodb output
				//DatabaseHelper.query3("expert system", 0, 3);
				
				//equal to zoodb output
				//DatabaseHelper.query4("William D. Young");
				
				//equal to zoodb output
				//DatabaseHelper.query5("J. Thomas Haigh", "William R. Bevier");
				//William D. Young --> William R. Bevier --> J. Thomas Haigh = 2
				//DatabaseHelper.query5( "Guy Dupenloup","Hiroshi Fukuda");

				//zoodbAverage authors 1.8636056420805172
				//mongodb Average authors 1.8821040258595356.
				//xml 1.8686696306941513.
				//DatabaseHelper.query6();
				
				//equal to zoodb output, only counting separately
				//DatabaseHelper.query7(1980,1990);
				
				//equal to zoodb output
				DatabaseHelper.query8("conf/microcomputing");
				
				//equal to zoodb output
				//DatabaseHelper.query9("Microcomputing");

				//equal to zoodb output
				//DatabaseHelper.query10("Microcomputing");
				
				//equal to zoodb output
				//DatabaseHelper.query11("Microcomputing");

				//equal to zoodb output
				//DatabaseHelper.query12();

				//equal to zoodb output
				//DatabaseHelper.query13("Adi Shamir");
				
				//DatabaseHelper.query14(1980, 1990);

			}
		});
		// modify button
		btnQueriesTableFrame.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
		btnQueriesTableFrame.setBounds(100, 600, 200, 25);
		contentPane.add(btnQueriesTableFrame);
	}
	
}