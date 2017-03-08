package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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
		setBounds(100, 100, 450, 300);
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

		// modify label
		JLabel lblStartWindow = new JLabel("Welcome!");
		lblStartWindow.setForeground(Color.BLUE);
		lblStartWindow.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 24));
		lblStartWindow.setBounds(127, 82, 239, 39);
		contentPane.add(lblStartWindow);
	}
}