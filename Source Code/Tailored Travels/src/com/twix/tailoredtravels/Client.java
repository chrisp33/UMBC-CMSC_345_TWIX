/**
 * 
 * @author Christopher Pagan
 */

package com.twix.tailoredtravels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Client{

	private static JPanel panel, p, p1, p2, p3, p4;
	private static JFrame frame;
	private static JButton login;
	private static JTextField nameField;
	private static JPasswordField passField;
	private static Authenticator auth;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		frame = new JFrame("Tailored Travels");
		panel = new JPanel();
		p = new JPanel();
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel();
		login = new JButton("Login");
		
		p.setBackground(new Color(0,91,255));
		p1.setBackground(new Color(0,91,255));
		p2.setBackground(new Color(0,91,255));
		p3.setBackground(new Color(0,91,255));
		p4.setBackground(new Color(0,91,255));

		p.add(new JLabel("Tailored Travels"));
		p1.add(new JLabel ("Enter your username and password"));
		p2.add(new JLabel("Username"));
		nameField = new JTextField(25);
		p2.add(nameField);
		
		p3.add(new JLabel("Password"));
		passField = new JPasswordField(25);
		//if (passField.echoCharIsSet() == false)
		{
			String bullet = "\u2022";
			passField.setEchoChar(bullet.charAt(0));
		}
		p3.add(passField);
		
		p4.add(login);
		
		panel.add(p);
		panel.add(p1);
		panel.add(p2);
		panel.add(p3);
		panel.add(p4);
		
		panel.setPreferredSize(new Dimension(600,300));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setContentPane(panel);
		frame.pack();
		// TODO Auto-generated method stub

		
// check login credentials here ------------------------------------------------------
		login.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				String userName = nameField.getText();
				char[] password = passField.getPassword();
				
				nameField.setText(null);
				passField.setText(null);
				
				auth = new Authenticator(userName, password);
				
				// Java documentation recommends clearing password array after use
				for (int i = 0; i < password.length; i++)
					password[i] = ' ';
				
				// do other stuff
			}
		});
	}

}
