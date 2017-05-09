package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import infsysProj.infsysProj.*;

public class PublicationsUpdateWindow extends JFrame {
	private JPanel contentPane;

	public static void main(String[] args) {
		/*
		 * It posts an event (Runnable)at the end of Swings event list and is processed after all other GUI events are processed.
		 */
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PublicationsUpdateWindow frame = new PublicationsUpdateWindow();
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
	public PublicationsUpdateWindow() {
		setTitle("Publications Update Frame");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnUpdatePublication = new JButton("Update");
		btnUpdatePublication.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO: update
			}
		});
		// modify button
		btnUpdatePublication.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
		btnUpdatePublication.setBounds(100, 200, 200, 25);
		contentPane.add(btnUpdatePublication);
	}
}
