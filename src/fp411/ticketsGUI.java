/** 
* Name: Stephen Griffith
* Date: 11/21/2018
* Source File Name: ticketsGUI.java
* Lab: Final Project
*/

package fp411;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import fp411.ticketsJTable;

public class ticketsGUI implements ActionListener {

	// class level member objects

	Dao dao = new Dao(); // for CRUD operations
	String chkIfAdmin = null;
	private JFrame mainFrame;

	JScrollPane sp = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	/* add any more Main menu object items below */

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;

	/* add any more Sub object items below */

	// constructor
	public ticketsGUI(String verifyRole) {

		chkIfAdmin = verifyRole; 
		JOptionPane.showMessageDialog(null, "Welcome " + verifyRole + " to the Trouble Ticket System");
		if (chkIfAdmin.equals("Admin"))
			try {
				dao.createTables();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // fire up table creations (tickets / user
								// tables)
		/*
		 * else do something else if you like
		 *
		 */

		createMenu();
		prepareGUI();
	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);

		// add any more listeners for any additional sub menu items if desired

	}

	private void prepareGUI() {
		// initialize frame object
		mainFrame = new JFrame("Tickets");

		// create jmenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		bar.add(mnuAdmin);
		bar.add(mnuTickets);
		// add menu bar components to frame
		mainFrame.setJMenuBar(bar);

		mainFrame.addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		mainFrame.setSize(800, 400);
		mainFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	/*
	 * action listener fires up items clicked on from sub menus with one action
	 * performed event handler!
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} else if (e.getSource() == mnuItemOpenTicket) {

			try {

				// get ticket information
				String submitterName = JOptionPane.showInputDialog(null, "Enter your Employee User ID");
				String submitterDept = JOptionPane.showInputDialog(null, "Enter your Department ID");
				String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

				// insert ticket information to database

				Statement statement = Dao.getConnection().createStatement();
 
				int result = statement
					.executeUpdate("INSERT INTO s_grif_tickets(emp_user_id, dept_id, ticket_status_id, submit_date, issue_description) " 
								+ "VALUES(" + " '" + submitterName + "','" + submitterDept + "', 'OPEN', now(), '" + ticketDesc + "')", Statement.RETURN_GENERATED_KEYS);

//					.executeUpdate("INSERT INTO s_grif_tickets(ticket_status, emp_user_id, dept_id, "
//							+ "issue_description, submit_date)" 
//							+ "VALUES('OPEN', '" + submitterName + "','" + submitterDept + "','" 
//							+ ticketDesc + "', now())", Statement.RETURN_GENERATED_KEYS);
			
				// retrieve ticket id number newly auto generated upon record insertion
				ResultSet resultSet = null;
				resultSet = statement.getGeneratedKeys();
				int id = 0;
				if (resultSet.next()) {
					id = resultSet.getInt(1); // retrieve first field in table
				}
				// display results if successful or not to console / dialog box
				if (result != 0) {
					System.out.println("Ticket ID : " + id + " created successfully!!!");
					JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
				} else {
					System.out.println("Ticket cannot be created!!!");
				}

			} catch (SQLException ex) {
 				ex.printStackTrace();
			}
		} else if (e.getSource() == mnuItemViewTicket) {

			// retrieve ticket information for viewing in JTable

			try {

				Statement statement = Dao.getConnection().createStatement();

				ResultSet results = statement.executeQuery("SELECT sgt.ticket_id, sgt.emp_user_id, sgu.emp_full_name, sgt.dept_id, sgt.ticket_status_id, sgt.submit_date, sgt.issue_description, estimate_fix_date, sgt.issue_resolution, sgt.resolution_date FROM s_grif_tickets sgt INNER JOIN s_grif_users sgu ON sgt.emp_user_id = sgu.emp_user_id");
						
						
						
//						("SELECT sgt.ticket_id, "
//						+ "sgt.ticket_status_id, sgt.emp_user_id, sgu.emp_full_name, "
//						+ "sgt.dept_id, sgt.submit_date, sgt.issue_description, "
//						+ "sgt.issue_resolution_description, sgt.resolution_date, "
//						+ "FROM s_grif_tickets sgt"
//						+ "INNER JOIN s_grif_users sgu ON sgt.emp_user_id = sgu.emp_user_id");
				

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				JTable jt = new JTable(ticketsJTable.buildTableModel(results));

				jt.setBounds(50, 50, 300, 300);
				sp = new JScrollPane(jt);
				mainFrame.add(sp);
				mainFrame.setVisible(true); // refreshes or repaints frame on
											// screen
				statement.close();  // close connections!!!

			} catch (SQLException e1) {
					e1.printStackTrace();
			}
		}   /*
			 * continue implementing any other desired sub menu items (like for
			 * update and delete sub menus for example) with similar syntax &
			 * logic as shown above*
			 */
	}
}
