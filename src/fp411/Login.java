/** 
* Name: Stephen Griffith
* Date: 11/21/2018
* Source File Name: Login.java
* Lab: Final Project
*/

package fp411;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

//import com.mysql.jdbc.PreparedStatement;
import java.sql.PreparedStatement;

import fp411.ticketsGUI;
/**
 * The purpose of this class is to provide the tools to create
 * the login frame and allow the user to login with 
 * credential authentication.
 */
public class Login {

	//create instance fields for class
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JLabel uidLabel;
	private JLabel pwdLabel;
	private JTextField userText;
	private JPasswordField pwdText;
	private JButton loginButton;
	private JPanel controlPanel;

	//class constructor method to call methods to initiate GUI
	public Login() {
		prepareGUI();
		showTextFields();
	}
	//method to create the GUI elements
	private void prepareGUI() {

		//instantiate jframe and jpanel objects
		mainFrame = new JFrame("User Login"); // title of window form
		headerLabel = new JLabel("", JLabel.CENTER);
		statusLabel = new JLabel("", JLabel.CENTER);
		controlPanel = new JPanel();

		//window frame settings
		mainFrame.setSize(400, 400);
		mainFrame.setLayout(new GridLayout(3, 1));
		mainFrame.getContentPane().setBackground(Color.yellow);
		mainFrame.setLocationRelativeTo(null);

		//frame object settings
		headerLabel.setText("Trouble Ticket DB Access");
		statusLabel.setSize(350, 100);

		//add frame objects to mainframe
		mainFrame.add(headerLabel);
		mainFrame.add(controlPanel);
		mainFrame.add(statusLabel);

		mainFrame.addWindowListener(new WindowAdapter() {
			
			//define method to close the login frame window
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});		//end of add window listener
	}

	//method to present the GUI elements
	private void showTextFields() {

		//instantiate controls
		uidLabel = new JLabel("User ID: ", JLabel.RIGHT);
		pwdLabel = new JLabel("Password: ", JLabel.CENTER);
		userText = new JTextField(6);
		pwdText = new JPasswordField(6);

		loginButton = new JButton("              Login                ");
		loginButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				String adminTasks = null;

				/*
				 * Check credentials for various users
				 * 
				 * Administrator is a super user to the trouble ticket system 
				 * Code below uses a default (temporary) hard coded admin user
				 * name/password for verification.
				 */
				// Create user friendly variable name to store user name from text box
				String userId = userText.getText();
				// convert characters from password field to string for input validation
				String password = new String(pwdText.getPassword());
				boolean adminFlag = false;		//initialize admin flag

				if (userId.equals("admin") && password.equals("admin1")) {
					adminFlag = true;
					//ask the admin user if they want to create tables to init the DB or simply create tickets in the established DB
					adminTasks = JOptionPane.showInputDialog(null, "Do you want to (C)reate tables or (T)ickets?");

					try {
						
						// establish role as admin via constructor call
						if (adminTasks.equalsIgnoreCase("C")) {	//decision branch to create tables
							mainFrame.dispose();		//close frame
							new ticketsGUI("Admin"); 	// open up ticketsGUI file upon successful login
						
						} else if	
						(adminTasks.equalsIgnoreCase("T")) { 	//decision branch to create tickets in DB
						adminFlag = false;						//set admin flag false to go create tickets

						} else if
							(!adminTasks.equalsIgnoreCase("C")) {	//check for invalid input tell user if true
							JOptionPane.showMessageDialog(null, "Invalid Input Response\nPlease login and select character C or T");
						}
						
					} catch (NullPointerException e1) {
						e1.printStackTrace();
					}
						
				} else 
				// close Login window
				mainFrame.dispose();

				/*
				 * match credentials from text fields with users table for a
				 * match for users
				 */
					if (!adminFlag) {
				
					Connection connect = Dao.getConnection();		//connect to DB
					//sql select statement to check for user/pswd match
				    String selectStatement = "SELECT emp_user_id, emp_passwrd FROM s_grif_users where emp_user_id=? and emp_passwrd=?";
					PreparedStatement prepstmnt;
					ResultSet results = null;
					
					try {
						//set up prepared statements to execute query string
						prepstmnt = (PreparedStatement) connect.prepareStatement(selectStatement);
						prepstmnt.setString(1, userId);
						prepstmnt.setString(2, password);
						results = prepstmnt.executeQuery();
						
						if (results.next()) {   //verify if a record match exists
							JOptionPane.showMessageDialog(null, "User Credentials have been verified accurate");
							//close of Login window
							mainFrame.dispose();
							//open up ticketsGUI file upon successful login
							new ticketsGUI(userId);   //establish role as user
														//constructor call
						} else {
							JOptionPane.showMessageDialog(null, "Invalid Credentials\nPlease check Username and Password ");
						}
						
					} catch (SQLException e1) {
						e1.printStackTrace();
						
					} finally {
						try {
							results.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
						try {
							connect.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		});
		// add layout type /background color to control panel
		controlPanel.setLayout(new FlowLayout());
		controlPanel.setBackground(Color.yellow);
		// add controls to control panel
		controlPanel.add(uidLabel);
		controlPanel.add(userText);
		controlPanel.add(pwdLabel);
		controlPanel.add(pwdText);
		controlPanel.add(loginButton);
		// lastly, set visibility of Window as all controls are instantiated for frame
		mainFrame.setVisible(true);
	}

	//main method of class
	public static void main(String[] args) {
		new Login();
	}
}