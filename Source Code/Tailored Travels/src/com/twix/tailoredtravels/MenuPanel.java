/**
 * Panel containing the main menu and most administrator and user functions. 
 * Only logged in users will be able to access this window. 
 * 
 * @author Christopher Pagan
 * @version 1.0
 */

package com.twix.tailoredtravels;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 3359477065217156534L;

	/**
	 * Components to add to the panel
	 */

	private JButton calcRoute, calcDist, addLocation, removeLocation, addUser,
			removeUser, logout, edit, help;
	private JLabel welcomeMsg, availMsg;
	private JList<String> list;
	private JScrollPane scroller;
	private JPanel welcome, addRmLoc, addRmUser, waypointsList, calculations,
			edits, exit, adminPanel, helpPanel, progress;
	final int REQUIRED_NUM = 3;
	final int REQUIRED_MAX = 12;
	private JProgressBar bar;
	private JDialog dialog;

	/**
	 * Filenames
	 */
	private static File helpFile;
	private static ImageIcon helpimg;

	/**
	 * The current user's username
	 */
	private String currentUser;

	/**
	 * Is the current user an admin
	 */
	private boolean isAdmin;

	/**
	 * Database instance
	 */
	private DatabaseManager dbm;

	/**
	 * Constructor for main menu
	 * 
	 * @param dbm
	 *            the database manager
	 * @param user
	 *            the user's username
	 * @param admin
	 *            whether or not the user is an administrator
	 */
	public MenuPanel(DatabaseManager dbm, String user, boolean admin) {
		// Assign variables
		this.dbm = dbm;
		isAdmin = admin;
		currentUser = user;
		Color bgColor = new Color(48, 235, 71); // Color for menuPanel
												// background
		TitledBorder adminBorder = BorderFactory.createTitledBorder(Messages
				.getString("MenuPanel.0")); //$NON-NLS-1$
		Border b = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

		// Create main menu panels
		helpPanel = new JPanel();
		welcome = new JPanel();
		addRmLoc = new JPanel();
		addRmUser = new JPanel();
		edits = new JPanel();
		adminPanel = new JPanel();
		bar = new JProgressBar();
		bar.setIndeterminate(true);
		// Create help button
		helpimg = new ImageIcon(Messages.getString("MenuPanel.1")); //$NON-NLS-1$
		help = new JButton(helpimg);
		help.setToolTipText(Messages.getString("MenuPanel.2")); //$NON-NLS-1$
		help.setBorder(BorderFactory.createEmptyBorder());

		// When user interacts with help button, load and open the help pdf file
		help.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
					try {
						helpFile = new File(Messages.getString("MenuPanel.3")); //$NON-NLS-1$
						Desktop.getDesktop().open(helpFile);
					} catch (IOException ex) {
						// no application registered for PDFs
						JOptionPane.showMessageDialog(
								null,
								Messages.getString("MenuPanel.4") //$NON-NLS-1$
										+ Messages.getString("MenuPanel.5"), Messages.getString("MenuPanel.6"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		helpPanel.add(help);
		helpPanel.setBackground(bgColor);
		help.setBackground(bgColor);

		// Create these components if the user is an administrator
		if (isAdmin) {
			// Admin section
			adminPanel.setLayout(new BoxLayout(adminPanel, BoxLayout.Y_AXIS));
			adminBorder.setBorder(b);
			adminPanel.setBorder(adminBorder);

			// Add/Remove/Edit User/Location buttons
			addLocation = new JButton(Messages.getString("MenuPanel.7")); //$NON-NLS-1$
			removeLocation = new JButton(Messages.getString("MenuPanel.8")); //$NON-NLS-1$
			addUser = new JButton(Messages.getString("MenuPanel.9")); //$NON-NLS-1$
			removeUser = new JButton(Messages.getString("MenuPanel.10")); //$NON-NLS-1$
			edit = new JButton(Messages.getString("MenuPanel.11")); //$NON-NLS-1$

			// Add all buttons to panel
			addRmLoc.add(addLocation);
			addRmLoc.add(removeLocation);
			addRmUser.add(addUser);
			addRmUser.add(removeUser);
			edits.add(edit);
			adminPanel.add(addRmLoc);
			adminPanel.add(addRmUser);
			adminPanel.add(edits);

			// Add ActionListeners to buttons
			addLocation.addActionListener(new AddLocListener());
			removeLocation.addActionListener(new RemLocListener());
			addUser.addActionListener(new AddUserListener());
			removeUser.addActionListener(new RemUserListener());
			edit.addActionListener(new EditListener());
		}

		// Instantiate GUI components
		welcomeMsg = new JLabel(
				Messages.getString("MenuPanel.12") + user + Messages.getString("MenuPanel.13")); //$NON-NLS-1$ //$NON-NLS-2$
		welcome.add(welcomeMsg);
		availMsg = new JLabel(Messages.getString("MenuPanel.14")); //$NON-NLS-1$
		list = new JList<String>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scroller = new JScrollPane(list);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		calcRoute = new JButton(Messages.getString("MenuPanel.15")); //$NON-NLS-1$
		calcDist = new JButton(Messages.getString("MenuPanel.16")); //$NON-NLS-1$
		logout = new JButton(Messages.getString("MenuPanel.17")); //$NON-NLS-1$

		// Add ActionListeners for normal user buttons
		list.addListSelectionListener(new ListSelListener());
		calcRoute.addActionListener(new RouteListener());
		calcDist.addActionListener(new DistListener());
		logout.addActionListener(new LogoutListener());

		// Add list of waypoints
		waypointsList = new JPanel();
		waypointsList.add(availMsg);
		waypointsList.add(scroller);
		waypointsList.setBorder(b);
		calculations = new JPanel();
		calculations.add(calcRoute);
		calculations.add(calcDist);
		exit = new JPanel();
		exit.add(logout);

		// Set uniform background color for all components
		setBackground(bgColor);
		adminPanel.setBackground(bgColor);
		welcome.setBackground(bgColor);
		addRmLoc.setBackground(bgColor);
		addRmUser.setBackground(bgColor);
		waypointsList.setBackground(bgColor);
		calculations.setBackground(bgColor);
		edits.setBackground(bgColor);
		exit.setBackground(bgColor);

		updateJList();
	}

	/**
	 * Add appropriate components to the main menu.
	 */
	public void addComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(welcome);

		if (isAdmin) {
			add(adminPanel);
		}

		add(waypointsList);
		add(calculations);
		add(exit);
		add(helpPanel);
	}

	/**
	 * Update list of waypoints for changes with edits, adding and removing
	 */
	public void updateJList() {
		try {
			// Update JList with the new waypoint names
			LinkedList<Waypoint> dbWaypoints = dbm.getWaypoints();
			Vector<String> listData = new Vector<String>();
			for (Waypoint wp : dbWaypoints) {
				listData.add(wp.getName());
			}
			list.setListData(listData);
		} catch (Exception e) {
			JOptionPane
					.showMessageDialog(
							null,
							Messages.getString("MenuPanel.18"), Messages.getString("MenuPanel.19"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Action listener for "Add Location" button
	 */
	private class AddLocListener implements ActionListener {

		/**
		 * Overwritten method for ActionListener class. Prompts for waypoint
		 * data and adds it to the system.
		 * 
		 * @param ae
		 *            An ActionEvent is created when the user clicks the
		 *            "Add Location" button
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			JTextField name = new JTextField();
			JTextField latitude = new JTextField();
			JTextField longitude = new JTextField();
			JTextField details = new JTextField();

			// Prompt to enter details
			Object[] message = { Messages.getString("MenuPanel.20"), name, //$NON-NLS-1$
					Messages.getString("MenuPanel.21"), latitude, //$NON-NLS-1$
					Messages.getString("MenuPanel.22"), longitude, //$NON-NLS-1$
					Messages.getString("MenuPanel.23"), details }; //$NON-NLS-1$

			// Assign input to variables
			int sel = JOptionPane
					.showConfirmDialog(
							null,
							message,
							Messages.getString("MenuPanel.24"), JOptionPane.OK_CANCEL_OPTION); //$NON-NLS-1$
			String wName = name.getText();
			String lat = latitude.getText();
			String lon = longitude.getText();
			String det = details.getText();

			// If cancel is selected, confirm cancellation and exit add new
			// location
			if (sel == JOptionPane.CANCEL_OPTION) {
				JOptionPane.showMessageDialog(null,
						Messages.getString("MenuPanel.25"), //$NON-NLS-1$
						Messages.getString("MenuPanel.26"), //$NON-NLS-1$
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// Check for blank entry
			if (wName.equals(Messages.getString("MenuPanel.27")) || lat.equals(Messages.getString("MenuPanel.28")) || lon.equals(Messages.getString("MenuPanel.29")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					|| det.equals(Messages.getString("MenuPanel.30"))) { //$NON-NLS-1$
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.31"), Messages.getString("MenuPanel.32"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.ERROR_MESSAGE);
				return;
			}

			// float variables for latitude and longitude
			float wLat, wLong;
			try {
				wLat = Float.parseFloat(lat);
				wLong = Float.parseFloat(lon);
			} catch (NumberFormatException e1) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.33"), //$NON-NLS-1$
								Messages.getString("MenuPanel.34"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				return;
			}

			// Latitude and Logitude validation
			if (wLat < -90 || wLat > 90 || wLong < -180 || wLong > 180) {
				String msg = Messages.getString("MenuPanel.35") //$NON-NLS-1$
						+ Messages.getString("MenuPanel.36"); //$NON-NLS-1$
				JOptionPane.showMessageDialog(null, msg,
						Messages.getString("MenuPanel.37"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Add new waypoint to database manager
			try {
				boolean added;
				added = dbm.addLocation(wLat, wLong, wName, det);

				if (!added) {
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.38") //$NON-NLS-1$
											+ wName
											+ Messages
													.getString("MenuPanel.39"), //$NON-NLS-1$
									Messages.getString("MenuPanel.40"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
				}

				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.41") + wName //$NON-NLS-1$
										+ Messages.getString("MenuPanel.42"), Messages.getString("MenuPanel.43"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.INFORMATION_MESSAGE);

				updateJList();
			} catch (SQLException e) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.44"), Messages.getString("MenuPanel.45"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	/**
	 * Action listener for "Remove Location" button
	 */
	private class RemLocListener implements ActionListener {

		/**
		 * Overwritten method for ActionListener class. Prompts for waypoint
		 * data and removes it from the system.
		 * 
		 * @param ae
		 *            An ActionEvent is created when the user clicks the
		 *            "Remove Location" button
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {
			LinkedList<Waypoint> waypoints = null;
			try {
				waypoints = dbm.getWaypoints();
			} catch (Exception e) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.46"), Messages.getString("MenuPanel.47"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}

			// Initialize array of removable waypoint names
			String[] waypointNames = new String[waypoints.size()];

			// Error message for empty list
			if (waypointNames.length == 0) {
				JOptionPane.showMessageDialog(null,
						Messages.getString("MenuPanel.48"), //$NON-NLS-1$
						Messages.getString("MenuPanel.49"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++) {
				waypointNames[i] = waypoints.get(i).getName();
			}

			String remLoc = (String) JOptionPane
					.showInputDialog(
							null,
							Messages.getString("MenuPanel.50"), Messages.getString("MenuPanel.51"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.QUESTION_MESSAGE, null, waypointNames,
							waypointNames[0]);

			// Notification if user clicks cancel button
			if (remLoc == null) {
				JOptionPane.showMessageDialog(null,
						Messages.getString("MenuPanel.52"), //$NON-NLS-1$
						Messages.getString("MenuPanel.53"), //$NON-NLS-1$
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// User confirmation of remove
			int sel = JOptionPane
					.showConfirmDialog(
							null,
							Messages.getString("MenuPanel.54") + remLoc + Messages.getString("MenuPanel.55"), //$NON-NLS-1$ //$NON-NLS-2$
							Messages.getString("MenuPanel.56"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$

			// Message when user clicks "no" button
			if (sel == JOptionPane.CANCEL_OPTION) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.57") + remLoc //$NON-NLS-1$
										+ Messages.getString("MenuPanel.58"), //$NON-NLS-1$
								Messages.getString("MenuPanel.59"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
				return;
			}

			try {
				// Remove waypoint
				dbm.removeLocation(remLoc);
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.60") + remLoc //$NON-NLS-1$
										+ Messages.getString("MenuPanel.61"), //$NON-NLS-1$
								Messages.getString("MenuPanel.62"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$

			} catch (Exception e) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.63"), Messages.getString("MenuPanel.64"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}

			updateJList();
		}
	}

	/**
	 * Action listener for "Add User" button
	 */
	private class AddUserListener implements ActionListener {

		/**
		 * Overwritten method for ActionListener class. Prompts for user data
		 * and adds it to the system.
		 * 
		 * @param ae
		 *            An ActionEvent is created when the user clicks the
		 *            "Add User" button
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {

			// Prompt for username and password
			JTextField nameField = new JTextField();
			JPasswordField passField = new JPasswordField();
			Object[] message = { Messages.getString("MenuPanel.65"), nameField, //$NON-NLS-1$
					Messages.getString("MenuPanel.66"), passField }; //$NON-NLS-1$
			int option = JOptionPane
					.showConfirmDialog(
							null,
							message,
							Messages.getString("MenuPanel.67"), JOptionPane.OK_CANCEL_OPTION); //$NON-NLS-1$
			String newName = nameField.getText();
			char[] passChars = passField.getPassword();

			// Check for valid data
			if ((option == JOptionPane.OK_OPTION)
					&& (!newName.equals(Messages.getString("MenuPanel.68"))) //$NON-NLS-1$
					&& (passChars.length > 0)) {
				boolean admin = false;

				// Prompt for new user privlege level
				Object[] opts = {
						Messages.getString("MenuPanel.69"), Messages.getString("MenuPanel.70") }; //$NON-NLS-1$ //$NON-NLS-2$
				String selection = (String) JOptionPane
						.showInputDialog(
								null,
								Messages.getString("MenuPanel.71"), Messages.getString("MenuPanel.72"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.QUESTION_MESSAGE, null, opts,
								Messages.getString("MenuPanel.73")); //$NON-NLS-1$

				if (selection == null) {
					// Inform user that the new user has not been added
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.74"), Messages.getString("MenuPanel.75"), //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.ERROR_MESSAGE);
					return;
				}

				String addMsg = Messages.getString("MenuPanel.76") + newName //$NON-NLS-1$
						+ Messages.getString("MenuPanel.77"); //$NON-NLS-1$

				if (selection.equals(Messages.getString("MenuPanel.78"))) { //$NON-NLS-1$
					admin = true;
					addMsg += Messages.getString("MenuPanel.79"); //$NON-NLS-1$
				} else {
					admin = false;
					addMsg += Messages.getString("MenuPanel.80"); //$NON-NLS-1$
				}

				// Convert password to string
				String password = Messages.getString("MenuPanel.81"); //$NON-NLS-1$
				for (int i = 0; i < passChars.length; i++) {
					password += passChars[i];
					passChars[i] = ' '; // clear array for security purposes
				}

				try {
					// Add user to database
					if (dbm.addUser(newName, password, admin))
						JOptionPane
								.showMessageDialog(
										null,
										addMsg,
										Messages.getString("MenuPanel.82"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
					else
						JOptionPane
								.showMessageDialog(
										null,
										Messages.getString("MenuPanel.83"), Messages.getString("MenuPanel.84"), //$NON-NLS-1$ //$NON-NLS-2$
										JOptionPane.ERROR_MESSAGE);
				} catch (SQLException e) {
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.85"), Messages.getString("MenuPanel.86"), //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
					System.exit(0);
				}

			} else if (option == JOptionPane.CANCEL_OPTION) {
				// User is not added if "cancel" is pressed
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.87"), Messages.getString("MenuPanel.88"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.INFORMATION_MESSAGE);
				return;
			} else {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.89"), //$NON-NLS-1$
								Messages.getString("MenuPanel.90"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				return;
			}

		}
	}

	/**
	 * Action listener for "Remove User" button
	 */
	private class RemUserListener implements ActionListener {

		/**
		 * Overwritten method for ActionListener class. Prompts for user data
		 * and removes it from the system.
		 * 
		 * @param ae
		 *            An ActionEvent is created when the user clicks the
		 *            "Remove User" button
		 */
		@Override
		public void actionPerformed(ActionEvent ae) {

			// Prompt for user name
			String name = JOptionPane
					.showInputDialog(
							null,
							Messages.getString("MenuPanel.91"), Messages.getString("MenuPanel.92"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.QUESTION_MESSAGE);
			if (name == null) {
				JOptionPane.showMessageDialog(null,
						Messages.getString("MenuPanel.93"), //$NON-NLS-1$
						Messages.getString("MenuPanel.94"), //$NON-NLS-1$
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if (name.equals(Messages.getString("MenuPanel.95"))) { //$NON-NLS-1$
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.96"), Messages.getString("MenuPanel.97"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Validation if removing the current user
			if (currentUser.equals(name)) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.98"), //$NON-NLS-1$
								Messages.getString("MenuPanel.99"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				return;
			}

			int sel = JOptionPane
					.showConfirmDialog(
							null,
							Messages.getString("MenuPanel.100") + name + Messages.getString("MenuPanel.101"), //$NON-NLS-1$ //$NON-NLS-2$
							Messages.getString("MenuPanel.102"), JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$

			// Cancel operation if No option is clicked
			if (sel == JOptionPane.NO_OPTION) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.103"), Messages.getString("MenuPanel.104"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			try {
				boolean removed = dbm.removeUser(name);
				if (removed) {
					JOptionPane.showMessageDialog(null,
							Messages.getString("MenuPanel.105") + name //$NON-NLS-1$
									+ Messages.getString("MenuPanel.106"), //$NON-NLS-1$
							Messages.getString("MenuPanel.107"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					String errmsg = Messages.getString("MenuPanel.108") //$NON-NLS-1$
							+ name + Messages.getString("MenuPanel.109") //$NON-NLS-1$
							+ Messages.getString("MenuPanel.110"); //$NON-NLS-1$
					JOptionPane.showMessageDialog(null, errmsg,
							Messages.getString("MenuPanel.111"), //$NON-NLS-1$
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (SQLException e) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.112"), Messages.getString("MenuPanel.113"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	/**
	 * Action listener for "Edit Location" button
	 */
	private class EditListener implements ActionListener {
		/**
		 * Overwritten method for ActionListener class. Prompts for which type
		 * of data is edited for the waypoint
		 * 
		 * @param ae
		 *            An ActionEvent is created when the user clicks the
		 *            "Edit Location" button
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			LinkedList<Waypoint> waypoints = null;
			try {
				waypoints = dbm.getWaypoints();
			} catch (Exception e1) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.114"), Messages.getString("MenuPanel.115"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
				System.exit(0);
			}

			// Initialize array of editable waypoint names
			String[] waypointNames = new String[waypoints.size()];

			// Error message for empty list
			if (waypointNames.length == 0) {
				JOptionPane.showMessageDialog(null,
						Messages.getString("MenuPanel.116"), //$NON-NLS-1$
						Messages.getString("MenuPanel.117"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++) {
				waypointNames[i] = waypoints.get(i).getName();
			}

			String editLoc = (String) JOptionPane
					.showInputDialog(
							null,
							Messages.getString("MenuPanel.118"), Messages.getString("MenuPanel.119"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.QUESTION_MESSAGE, null, waypointNames,
							waypointNames[0]);

			// Notification if user clicks cancel button
			if (editLoc == null) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.120"), //$NON-NLS-1$
								Messages.getString("MenuPanel.121"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
				return;
			}

			// Prompt for edit type
			Object[] options = {
					Messages.getString("MenuPanel.122"), Messages.getString("MenuPanel.123"), Messages.getString("MenuPanel.124"), Messages.getString("MenuPanel.125") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			int sel = JOptionPane
					.showOptionDialog(
							null,
							Messages.getString("MenuPanel.126") + editLoc + Messages.getString("MenuPanel.127"), //$NON-NLS-1$ //$NON-NLS-2$
							Messages.getString("MenuPanel.128"), JOptionPane.YES_NO_CANCEL_OPTION, //$NON-NLS-1$
							JOptionPane.QUESTION_MESSAGE, null, options,
							options[3]);

			// Edit Name
			if (sel == 0) {
				String newName = JOptionPane
						.showInputDialog(
								null,
								Messages.getString("MenuPanel.129") + editLoc + Messages.getString("MenuPanel.130"), //$NON-NLS-1$ //$NON-NLS-2$
								Messages.getString("MenuPanel.131"), JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$

				// Exit operation if cancelled
				if (newName == null) {
					JOptionPane.showMessageDialog(null,
							Messages.getString("MenuPanel.132"), //$NON-NLS-1$
							Messages.getString("MenuPanel.133"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				// Loop for empty field
				while (newName == null
						|| newName.equals(Messages.getString("MenuPanel.134"))) { //$NON-NLS-1$
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.135"), Messages.getString("MenuPanel.136"), //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.WARNING_MESSAGE);

					newName = JOptionPane
							.showInputDialog(
									null,
									Messages.getString("MenuPanel.137") + editLoc + Messages.getString("MenuPanel.138"), //$NON-NLS-1$ //$NON-NLS-2$
									Messages.getString("MenuPanel.139"), JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$

					// Exit operation if cancelled
					if (newName == null) {
						JOptionPane.showMessageDialog(null,
								Messages.getString("MenuPanel.140"), //$NON-NLS-1$
								Messages.getString("MenuPanel.141"), //$NON-NLS-1$
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}

				try {
					dbm.setWaypointName(editLoc, newName);
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.142") + editLoc //$NON-NLS-1$
											+ Messages
													.getString("MenuPanel.143") + newName //$NON-NLS-1$
											+ Messages
													.getString("MenuPanel.144"), Messages.getString("MenuPanel.145"), //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e1) {
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.146") + editLoc //$NON-NLS-1$
											+ Messages
													.getString("MenuPanel.147"), Messages.getString("MenuPanel.148"), //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.ERROR_MESSAGE);
				}

				updateJList();
			}
			// Edit coordinates
			else if (sel == 1) {
				// Prompt for latitude and longitude
				JTextField latField = new JTextField();
				JTextField longField = new JTextField();
				Object[] message = {
						Messages.getString("MenuPanel.149"), latField, //$NON-NLS-1$
						Messages.getString("MenuPanel.150"), longField }; //$NON-NLS-1$
				int option = JOptionPane
						.showConfirmDialog(
								null,
								message,
								Messages.getString("MenuPanel.151"), JOptionPane.OK_CANCEL_OPTION); //$NON-NLS-1$

				if (option == JOptionPane.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(null,
							Messages.getString("MenuPanel.152"), //$NON-NLS-1$
							Messages.getString("MenuPanel.153"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				float newLat;
				float newLong;

				try {
					newLat = Float.parseFloat(latField.getText());
					newLong = Float.parseFloat(longField.getText());
				} catch (NumberFormatException ne) {
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.154"), //$NON-NLS-1$
									Messages.getString("MenuPanel.155"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
					return;
				}

				// Validation for latitude and longitude values
				while (newLat < -90 || newLat > 90 || newLong < -180
						|| newLong > 180) {

					String msg = Messages.getString("MenuPanel.156") //$NON-NLS-1$
							+ Messages.getString("MenuPanel.157"); //$NON-NLS-1$
					JOptionPane.showMessageDialog(null, msg,
							Messages.getString("MenuPanel.158"), //$NON-NLS-1$
							JOptionPane.ERROR_MESSAGE);

					Object[] message1 = {
							Messages.getString("MenuPanel.159"), latField, //$NON-NLS-1$
							Messages.getString("MenuPanel.160"), longField }; //$NON-NLS-1$
					int option1 = JOptionPane.showConfirmDialog(null, message1,
							Messages.getString("MenuPanel.161"), //$NON-NLS-1$
							JOptionPane.OK_CANCEL_OPTION);

					if (option1 == JOptionPane.CANCEL_OPTION) {
						JOptionPane.showMessageDialog(null,
								Messages.getString("MenuPanel.162"), //$NON-NLS-1$
								Messages.getString("MenuPanel.163"), //$NON-NLS-1$
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}

					try {
						newLat = Float.parseFloat(latField.getText());
						newLong = Float.parseFloat(longField.getText());
					} catch (NumberFormatException ne) {
						JOptionPane.showMessageDialog(null,
								Messages.getString("MenuPanel.164"), //$NON-NLS-1$
								Messages.getString("MenuPanel.165"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}

				}

				try {
					dbm.setWaypointLatLong(editLoc, newLat, newLong);
					JOptionPane.showMessageDialog(
							null,
							Messages.getString("MenuPanel.166") //$NON-NLS-1$
									+ editLoc
									+ Messages.getString("MenuPanel.167"), //$NON-NLS-1$
							Messages.getString("MenuPanel.168"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e1) {
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.169") + editLoc //$NON-NLS-1$
											+ Messages
													.getString("MenuPanel.170"), Messages.getString("MenuPanel.171"), //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.ERROR_MESSAGE);
				}

				updateJList();
			}
			// Edit Description
			else if (sel == 2) {
				String newDesc = JOptionPane
						.showInputDialog(
								null,
								Messages.getString("MenuPanel.172") + editLoc + Messages.getString("MenuPanel.173"), //$NON-NLS-1$ //$NON-NLS-2$
								Messages.getString("MenuPanel.174"), JOptionPane.PLAIN_MESSAGE); //$NON-NLS-1$

				if (newDesc == null) {
					JOptionPane.showMessageDialog(null,
							Messages.getString("MenuPanel.175"), //$NON-NLS-1$
							Messages.getString("MenuPanel.176"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				while (newDesc == null
						|| newDesc.equals(Messages.getString("MenuPanel.177"))) { //$NON-NLS-1$

					if (newDesc == null) {
						JOptionPane.showMessageDialog(null,
								Messages.getString("MenuPanel.178"), //$NON-NLS-1$
								Messages.getString("MenuPanel.179"), //$NON-NLS-1$
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}

					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.180"), Messages.getString("MenuPanel.181"), //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.ERROR_MESSAGE);

					newDesc = JOptionPane
							.showInputDialog(
									null,
									Messages.getString("MenuPanel.182") + editLoc //$NON-NLS-1$
											+ Messages
													.getString("MenuPanel.183"), Messages.getString("MenuPanel.184"), //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.PLAIN_MESSAGE);

				}

				try {
					dbm.setWaypointDescription(editLoc, newDesc);
					JOptionPane.showMessageDialog(null,
							Messages.getString("MenuPanel.185") + editLoc //$NON-NLS-1$
									+ Messages.getString("MenuPanel.186"), //$NON-NLS-1$
							Messages.getString("MenuPanel.187"), //$NON-NLS-1$
							JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e1) {
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.188") + editLoc //$NON-NLS-1$
											+ Messages
													.getString("MenuPanel.189"), Messages.getString("MenuPanel.190"), //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.ERROR_MESSAGE);
				}
			}
			// Cancel
			else {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.191") + editLoc //$NON-NLS-1$
										+ Messages.getString("MenuPanel.192"), Messages.getString("MenuPanel.193"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
	}

	/**
	 * List selection listener for when an entry in the list of locations is
	 * selected
	 */
	private class ListSelListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			// Check if selection is changing due to mouse click
			boolean valAdjusting = e.getValueIsAdjusting();

			if (!valAdjusting) {
				// Get the selection
				String selection = list.getSelectedValue();
				Waypoint selectedWaypoint = null;
				LinkedList<Waypoint> points = null;
				try {
					points = dbm.getWaypoints();

					if (points == null)
						throw new SQLException(
								Messages.getString("MenuPanel.194")); //$NON-NLS-1$
				} catch (SQLException e1) {
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.195"), Messages.getString("MenuPanel.196"), //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					System.exit(0);
				}

				// ASSUMES no duplicate waypoint names
				for (Waypoint wp : points) {
					if (wp.getName().equals(selection))
						selectedWaypoint = wp;
				}

				// Error if waypoint cannot be found
				if (selectedWaypoint == null) {
					// Exit operation if list has no items selected
					if (list.isSelectionEmpty())
						return;

					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.197"), Messages.getString("MenuPanel.198"), //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.ERROR_MESSAGE);
					return;
				}

				// Split waypoint details into separate sentences for formatting
				String[] wpDetails = selectedWaypoint.getDescription().split(
						Messages.getString("MenuPanel.199")); //$NON-NLS-1$
				String wpDet = Messages.getString("MenuPanel.200"); //$NON-NLS-1$
				for (int i = 0; i < wpDetails.length; i++) {
					// Fix for java.lang.String.format percent escape
					if (wpDetails[i].contains(Messages
							.getString("MenuPanel.201"))) //$NON-NLS-1$
						wpDet += wpDetails[i]
								.replace(
										Messages.getString("MenuPanel.202"), Messages.getString("MenuPanel.203")) + Messages.getString("MenuPanel.204"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					else
						wpDet += wpDetails[i]
								+ Messages.getString("MenuPanel.205"); //$NON-NLS-1$
				}

				String waypointDetails = String
						.format(Messages.getString("MenuPanel.206") //$NON-NLS-1$
								+ Messages.getString("MenuPanel.207") + selectedWaypoint.getName() //$NON-NLS-1$
								+ Messages.getString("MenuPanel.208") + Messages.getString("MenuPanel.209") //$NON-NLS-1$ //$NON-NLS-2$
								+ Messages.getString("MenuPanel.210") //$NON-NLS-1$
								+ Messages.getString("MenuPanel.211") + wpDet, //$NON-NLS-1$
								selectedWaypoint.getLatitude(),
								selectedWaypoint.getLongitude());

				JTextArea details = new JTextArea(waypointDetails);
				details.setEditable(false);

				JOptionPane
						.showMessageDialog(
								null,
								details,
								Messages.getString("MenuPanel.212"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$

				list.clearSelection();
				return;
			}
		}

	}

	/**
	 * Action listener for "Calculate Route" button
	 */
	private class RouteListener implements ActionListener {

		/**
		 * Overwritten method for ActionListener class. Calculates the route
		 * given the waypoints the user has selected.
		 * 
		 * @param ae
		 *            An ActionEvent is created when the user clicks the
		 *            "Calculate Route" button
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {

			// Get list of points from database
			LinkedList<Waypoint> waypoints = new LinkedList<Waypoint>();
			try {
				waypoints = dbm.getWaypoints();
			} catch (Exception e) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.213"), Messages.getString("MenuPanel.214"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}

			// Validation for fewer than two available locations
			if (waypoints.size() < 2) {
				JOptionPane.showMessageDialog(null,
						Messages.getString("MenuPanel.215"), //$NON-NLS-1$
						Messages.getString("MenuPanel.216"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Prompt for starting point
			String[] firstPts = new String[waypoints.size()];

			// Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++) {
				firstPts[i] = waypoints.get(i).getName();
			}
			String startPoint = (String) JOptionPane
					.showInputDialog(
							null,
							Messages.getString("MenuPanel.217"), Messages.getString("MenuPanel.218"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.PLAIN_MESSAGE, null, firstPts,
							firstPts[0]);

			// If cancelled
			if (startPoint == null) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.219"), //$NON-NLS-1$
								Messages.getString("MenuPanel.220"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
				return;
			}

			// Prompt for ending point
			ArrayList<String> secondList = new ArrayList<String>();
			String[] secondPts = new String[waypoints.size() - 1];

			// Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++) {
				if (!waypoints.get(i).getName().equals(startPoint))
					secondList.add(waypoints.get(i).getName());
			}

			for (int i = 0; i < secondList.size(); i++) {
				secondPts[i] = secondList.get(i);
			}

			String endPoint = (String) JOptionPane
					.showInputDialog(
							null,
							Messages.getString("MenuPanel.221"), Messages.getString("MenuPanel.222"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.PLAIN_MESSAGE, null, secondPts,
							secondPts[0]);

			// If cancelled when selecting second point
			if (endPoint == null) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.223"), //$NON-NLS-1$
								Messages.getString("MenuPanel.224"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
				return;
			}

			// Use waypoint names to calculate route
			Waypoint ptA = null;
			Waypoint ptB = null;

			ArrayList<Waypoint> points = new ArrayList<Waypoint>();
			LinkedList<Waypoint> pts = new LinkedList<Waypoint>();
			try {
				// Find corresponding Waypoints with names
				pts = dbm.getWaypoints();
				for (int i = 0; i < pts.size(); i++) {
					points.add(pts.get(i));
					if (pts.get(i).getName().equals(startPoint))
						ptA = pts.get(i);
					if (pts.get(i).getName().equals(endPoint))
						ptB = pts.get(i);
				}

				if (ptA == null || ptB == null) {
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.225"), Messages.getString("MenuPanel.226"), //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.ERROR_MESSAGE);
					return;
				}

				ArrayList<Waypoint> selectedPoints = new ArrayList<Waypoint>();

				// Number of required points in route is greater than 2
				if (REQUIRED_NUM >= 2) {
					// Remove the starting and end points for other popup
					ArrayList<Waypoint> remainingPoints = points;
					Vector<String> remainingNames = new Vector<String>();
					JList<String> names;
					int indexA = remainingPoints.indexOf(ptA);
					remainingPoints.remove(indexA);
					int indexB = remainingPoints.indexOf(ptB);
					remainingPoints.remove(indexB);
					if (remainingPoints.size() >= (REQUIRED_NUM - 2)) {
						for (Waypoint wp : remainingPoints)
							remainingNames.add(wp.getName());
						names = new JList<String>();
						JScrollPane sp = new JScrollPane(names);
						names.setListData(remainingNames);
						names.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

						// Set so multiple items are selected without the need
						// to hold other keys.
						// Taken from https://forums.oracle.com/thread/1360864
						names.setSelectionModel(new DefaultListSelectionModel() {
							private static final long serialVersionUID = 1L;

							@Override
							public void setSelectionInterval(int index0,
									int index1) {
								if (isSelectedIndex(index0))
									super.removeSelectionInterval(index0,
											index1);
								else
									super.addSelectionInterval(index0, index1);
							}
						});

						Object[] msg = { Messages.getString("MenuPanel.227"), //$NON-NLS-1$
								sp };
						int op = JOptionPane.showConfirmDialog(null, msg,
								Messages.getString("MenuPanel.228"), //$NON-NLS-1$
								JOptionPane.OK_CANCEL_OPTION);

						// If cancelled
						if (op == JOptionPane.CANCEL_OPTION) {
							JOptionPane.showMessageDialog(null,
									Messages.getString("MenuPanel.229"), //$NON-NLS-1$
									Messages.getString("MenuPanel.230"), //$NON-NLS-1$
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}

						int selectedNum = names.getSelectedIndices().length;

						if (selectedNum < (REQUIRED_NUM - 2)
								|| (selectedNum + 2) > REQUIRED_MAX) {
							JOptionPane
									.showMessageDialog(
											null,
											Messages.getString("MenuPanel.231") //$NON-NLS-1$
													+ (REQUIRED_NUM - 2)
													+ Messages
															.getString("MenuPanel.232") //$NON-NLS-1$
													+ (REQUIRED_MAX - 2)
													+ Messages
															.getString("MenuPanel.233"), //$NON-NLS-1$
											Messages.getString("MenuPanel.234"), //$NON-NLS-1$
											JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					// Error for points < 13
					else {
						JOptionPane.showMessageDialog(
								null,
								Messages.getString("MenuPanel.235") //$NON-NLS-1$
										+ REQUIRED_NUM
										+ Messages.getString("MenuPanel.236"), //$NON-NLS-1$
								Messages.getString("MenuPanel.237"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					selectedPoints = new ArrayList<Waypoint>();
					selectedPoints.add(ptA);
					for (String wpName : names.getSelectedValuesList()) {
						for (Waypoint wp : points) {
							if (wp.getName().equals(wpName)) {
								selectedPoints.add(wp);
							}
						}
					}
					selectedPoints.add(ptB);
				}

				// Progress bar
				dialog = new JDialog();
				progress = new JPanel();
				progress.add(bar);
				dialog.setContentPane(progress);
				dialog.pack();
				dialog.setVisible(true);
				dialog.setTitle(Messages.getString("MenuPanel.238")); //$NON-NLS-1$
				// perform background calcualations
				DoRoute doRoute = new DoRoute(selectedPoints);
				doRoute.execute();
			} catch (SQLException e) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.239"), Messages.getString("MenuPanel.240"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(0);
			}

		}

	}

	/**
	 * Action listener for "Calculate Distance" button
	 */
	private class DistListener implements ActionListener {

		/**
		 * Overwritten method for ActionListener class. Prompts for starting and
		 * ending waypoint data and finds the distance between them.
		 * 
		 * @param ae
		 *            An ActionEvent is created when the user clicks the
		 *            "Calculate Distance" button
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			// Get list of points from database
			LinkedList<Waypoint> waypoints = new LinkedList<Waypoint>();
			try {
				waypoints = dbm.getWaypoints();
			} catch (Exception e1) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.241"), Messages.getString("MenuPanel.242"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
				System.exit(0);
			}

			// Validation for fewer than two available locations
			if (waypoints.size() < 2) {
				JOptionPane.showMessageDialog(null,
						Messages.getString("MenuPanel.243"), //$NON-NLS-1$
						Messages.getString("MenuPanel.244"), //$NON-NLS-1$
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Prompt for starting point
			String[] firstPts = new String[waypoints.size()];

			// Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++) {
				firstPts[i] = waypoints.get(i).getName();
			}
			String startPoint = (String) JOptionPane
					.showInputDialog(
							null,
							Messages.getString("MenuPanel.245"), Messages.getString("MenuPanel.246"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.PLAIN_MESSAGE, null, firstPts,
							firstPts[0]);

			// If cancelled
			if (startPoint == null) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.247"), //$NON-NLS-1$
								Messages.getString("MenuPanel.248"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
				return;
			}

			// Prompt for ending point
			ArrayList<String> secondList = new ArrayList<String>();
			String[] secondPts = new String[waypoints.size() - 1];

			// Create array for use in JOptionPane
			for (int i = 0; i < waypoints.size(); i++) {
				if (!waypoints.get(i).getName().equals(startPoint))
					secondList.add(waypoints.get(i).getName());
			}

			for (int i = 0; i < secondList.size(); i++) {
				secondPts[i] = secondList.get(i);
			}

			String endPoint = (String) JOptionPane
					.showInputDialog(
							null,
							Messages.getString("MenuPanel.249"), Messages.getString("MenuPanel.250"), //$NON-NLS-1$ //$NON-NLS-2$
							JOptionPane.PLAIN_MESSAGE, null, secondPts,
							secondPts[0]);

			// If cancelled when selecting second point
			if (endPoint == null) {
				JOptionPane.showMessageDialog(null,
						Messages.getString("MenuPanel.251"), //$NON-NLS-1$
						Messages.getString("MenuPanel.252"), //$NON-NLS-1$
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			// Use waypoint names to calculate route
			Waypoint ptA = null;
			Waypoint ptB = null;

			ArrayList<Waypoint> points = new ArrayList<Waypoint>();
			LinkedList<Waypoint> pts = new LinkedList<Waypoint>();
			ArrayList<Waypoint> selectedPoints = new ArrayList<Waypoint>();
			try {
				// Find corresponding Waypoints with names
				pts = dbm.getWaypoints();
				for (int i = 0; i < pts.size(); i++) {
					points.add(pts.get(i));
					if (pts.get(i).getName().equals(startPoint))
						ptA = pts.get(i);
					if (pts.get(i).getName().equals(endPoint))
						ptB = pts.get(i);
				}

				if (ptA == null || ptB == null) {
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString("MenuPanel.253"), Messages.getString("MenuPanel.254"), //$NON-NLS-1$ //$NON-NLS-2$
									JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (REQUIRED_NUM >= 2) {
					// Remove the starting and end points for other popup
					ArrayList<Waypoint> remainingPoints = points;
					Vector<String> remainingNames = new Vector<String>();
					JList<String> names;
					int indexA = remainingPoints.indexOf(ptA);
					remainingPoints.remove(indexA);
					int indexB = remainingPoints.indexOf(ptB);
					remainingPoints.remove(indexB);
					if (remainingPoints.size() >= (REQUIRED_NUM - 2)) {
						for (Waypoint wp : remainingPoints)
							remainingNames.add(wp.getName());
						names = new JList<String>();
						JScrollPane sp = new JScrollPane(names);
						names.setListData(remainingNames);
						names.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

						// Set so multiple items are selected without the need
						// to hold other keys.
						// Taken from https://forums.oracle.com/thread/1360864
						names.setSelectionModel(new DefaultListSelectionModel() {
							private static final long serialVersionUID = 1L;

							@Override
							public void setSelectionInterval(int index0,
									int index1) {
								if (isSelectedIndex(index0))
									super.removeSelectionInterval(index0,
											index1);
								else
									super.addSelectionInterval(index0, index1);
							}
						});

						Object[] msg = { Messages.getString("MenuPanel.255"), //$NON-NLS-1$
								sp };
						int op = JOptionPane.showConfirmDialog(null, msg,
								Messages.getString("MenuPanel.256"), //$NON-NLS-1$
								JOptionPane.OK_CANCEL_OPTION);

						// If cancelled
						if (op == JOptionPane.CANCEL_OPTION) {
							JOptionPane.showMessageDialog(null,
									Messages.getString("MenuPanel.257"), //$NON-NLS-1$
									Messages.getString("MenuPanel.258"), //$NON-NLS-1$
									JOptionPane.INFORMATION_MESSAGE);
							return;
						}

						int selectedNum = names.getSelectedIndices().length;

						if (selectedNum < (REQUIRED_NUM - 2)
								|| (selectedNum + 2) > REQUIRED_MAX) {
							JOptionPane
									.showMessageDialog(
											null,
											Messages.getString("MenuPanel.259") //$NON-NLS-1$
													+ (REQUIRED_NUM - 2)
													+ Messages
															.getString("MenuPanel.260") //$NON-NLS-1$
													+ (REQUIRED_MAX - 2)
													+ Messages
															.getString("MenuPanel.261"), //$NON-NLS-1$
											Messages.getString("MenuPanel.262"), //$NON-NLS-1$
											JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					// Error for points < 13
					else {
						JOptionPane.showMessageDialog(
								null,
								Messages.getString("MenuPanel.263") //$NON-NLS-1$
										+ REQUIRED_NUM
										+ Messages.getString("MenuPanel.264"), //$NON-NLS-1$
								Messages.getString("MenuPanel.265"), //$NON-NLS-1$
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					selectedPoints.add(ptA);
					for (String wpName : names.getSelectedValuesList()) {
						for (Waypoint wp : points) {
							if (wp.getName().equals(wpName)) {
								selectedPoints.add(wp);
							}
						}
					}
					selectedPoints.add(ptB);

				}

			}

			catch (SQLException e1) {
				JOptionPane
						.showMessageDialog(
								null,
								Messages.getString("MenuPanel.266"), Messages.getString("MenuPanel.267"), //$NON-NLS-1$ //$NON-NLS-2$
								JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
				System.exit(0);
			}

			// Progress bar
			dialog = new JDialog();
			progress = new JPanel();
			progress.add(bar);
			dialog.setContentPane(progress);
			dialog.pack();
			dialog.setVisible(true);
			dialog.setTitle(Messages.getString("MenuPanel.268")); //$NON-NLS-1$
			// perform background calcualations
			DoCalc doCalc = new DoCalc(selectedPoints);
			doCalc.execute();

		}

	}

	/**
	 * Action listener for "Log Out" button
	 */
	private class LogoutListener implements ActionListener {
		/**
		 * Logs the user out of the system
		 * 
		 * @param e
		 *            the ActionEvent created when the user selects the
		 *            "Log Out" button.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane
					.showMessageDialog(
							null,
							Messages.getString("MenuPanel.269"), //$NON-NLS-1$
							Messages.getString("MenuPanel.270"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
			dbm.logout();
			System.exit(0);
		}
	}

	/**
	 * Swingworker for calculating the route
	 */
	private class DoCalc extends SwingWorker<Double, Void> {

		private ArrayList<Waypoint> selectedPoints;
		private double dist;

		public DoCalc(ArrayList<Waypoint> wp) {
			selectedPoints = wp;
		}

		/**
		 * Perform calculation for the distance in the background
		 */
		@Override
		protected Double doInBackground() throws Exception {

			ArrayList<Waypoint> rt = DistCalcDriver
					.shortDistAlgorithm(selectedPoints);
			dist = DistCalcDriver.totalDistance(rt);
			return dist;
		}

		/**
		 * Displays message after completed calculations
		 */
		@Override
		public void done() {
			dialog.dispose();
			String startPoint = selectedPoints.get(0).getName();
			String endPoint = selectedPoints.get(selectedPoints.size() - 1)
					.getName();

			String distMsg = String.format(Messages.getString("MenuPanel.271") //$NON-NLS-1$
					+ startPoint
					+ Messages.getString("MenuPanel.272") + endPoint //$NON-NLS-1$
					+ Messages.getString("MenuPanel.273"), dist); //$NON-NLS-1$

			JOptionPane.showMessageDialog(null, distMsg,
					Messages.getString("MenuPanel.274"), //$NON-NLS-1$
					JOptionPane.INFORMATION_MESSAGE);

		}
	}

	/**
	 * Swingworker for calculating the route
	 */
	private class DoRoute extends SwingWorker<String, Void> {

		private ArrayList<Waypoint> selectedPoints;
		private String result;

		public DoRoute(ArrayList<Waypoint> wp) {
			selectedPoints = wp;
		}

		/**
		 * Perform calculation for the distance in the background
		 */
		@Override
		protected String doInBackground() throws Exception {

			ArrayList<Waypoint> routeWaypoints = DistCalcDriver
					.shortDistAlgorithm(selectedPoints);
			GoogleEarthPath path = new GoogleEarthPath(routeWaypoints);
			GoogleEarthManager gem = new GoogleEarthManager();
			result = gem.Path2KML(path);
			return result;
		}

		/**
		 * Displays message after completed calculations
		 */
		@Override
		public void done() {
			dialog.dispose();
			JOptionPane.showMessageDialog(null, result,
					Messages.getString("MenuPanel.275"), //$NON-NLS-1$
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
