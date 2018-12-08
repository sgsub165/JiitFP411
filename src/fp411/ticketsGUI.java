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

/**
 * the purpose of this class to provide methods to create the GUI
 *
 */
public class ticketsGUI implements ActionListener {

	//define class level member objects

	Dao dao = new Dao(); // object creation for CRUD operations
	String chkIfAdmin = null;	//variable to test for Admin login
	private JFrame mainFrame;
	static Statement stmnt = null;

	JScrollPane sp = null;

	//main menu object items
	private JMenu menuFile = new JMenu("File");
	private JMenu menuAdmin = new JMenu("Admin");
	private JMenu menuTickets = new JMenu("Tickets");

	//sub menu item objects for all Main menu item objects
	JMenuItem menuObjectExit;
	JMenuItem menuObjectUpdate;
	JMenuItem menuObjectDelete;
	JMenuItem menuObjectOpenTicket;
	JMenuItem menuObjectViewTicket;

	// constructor
	public ticketsGUI(String verifyRole) {

		chkIfAdmin = verifyRole; 
		JOptionPane.showMessageDialog(null, "Welcome " + verifyRole + " to the Trouble Ticket System");
		if (chkIfAdmin.equals("Admin")) {
			try {
				dao.createTables();	//create depts, tickets, ticketstatus, users tables
				dao.addUsers();		//insert users into table
				dao.addDepts();		//insert depts into table
				dao.addTicketStatus();	//insert ticketstatus into table
				JOptionPane.showMessageDialog(null, "Database tables have been created\nPlease login to TTMS to continue");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			else {
				createMenu();
				System.out.printf("prior to prepGUI", verifyRole);
				prepareGUI(verifyRole);
				
			}
		}

	private void createMenu() {

		/* Initialize sub menu object items */

		// initialize sub menu object item for File main menu
		menuObjectExit = new JMenuItem("Exit");
		// add to File main menu object item
		menuFile.add(menuObjectExit);

		// initialize first sub menu object items for Admin main menu
		menuObjectDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu object item
		menuAdmin.add(menuObjectDelete);

		// initialize first sub menu object item for Tickets main menu
		menuObjectOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu object item
		menuTickets.add(menuObjectOpenTicket);

		// initialize second sub menu object item for Tickets main menu
		menuObjectViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu object item
		menuTickets.add(menuObjectViewTicket);
		
		// initialize third sub menu object items for Tickets main menu
		menuObjectUpdate = new JMenuItem("Update Ticket");
		// add to Ticket main menu object item
		menuTickets.add(menuObjectUpdate);

		/* Add action listeners for each desired menu object item */
		menuObjectExit.addActionListener(this);
		menuObjectDelete.addActionListener(this);
		menuObjectOpenTicket.addActionListener(this);
		menuObjectViewTicket.addActionListener(this);
		menuObjectUpdate.addActionListener(this);
	}

	private void prepareGUI(String verifyRole) {
		// initialize frame object
		mainFrame = new JFrame("Trouble Tickets  User: " + verifyRole);
		
		chkIfAdmin = verifyRole;
		System.out.printf("\nduring prepGUI", verifyRole);
		if (chkIfAdmin.equals("admin")) {

		// create admin jmenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(menuFile); // add main menu object items in order, to JMenuBar
		bar.add(menuAdmin);
		bar.add(menuTickets);
		// add menu bar components to frame
		mainFrame.setJMenuBar(bar);

		mainFrame.addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		mainFrame.setSize(1400, 400);
		mainFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
		refreshTable();
		
		} else {
			// create user jmenu bar
			JMenuBar bar = new JMenuBar();
			bar.add(menuFile); // add main menu object items in order, to JMenuBar
			bar.add(menuTickets);
			// add menu bar components to frame
			mainFrame.setJMenuBar(bar);

			mainFrame.addWindowListener(new WindowAdapter() {
				// define a window close operation
				public void windowClosing(WindowEvent wE) {
					System.exit(0);
				}
			});
			// set frame options
			mainFrame.setSize(1400, 400);
			mainFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
			mainFrame.setLocationRelativeTo(null);
			mainFrame.setVisible(true);
			refreshTable();
		}
	}

	/*
	 * action listener invokes items clicked on from sub menus with one action
	 * performed event handler!
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		// implement actions for sub menu object items
		if (e.getSource() == menuObjectExit) {	//event to exit the gui session
			System.exit(0);
			
		} else if (e.getSource() == menuObjectOpenTicket) {		//event to create new tickets
			dao.createTicket();			//call to createTicket method in dao class
			refreshTable();
			
		} else if (e.getSource() == menuObjectViewTicket) {		//event to view tickets
			refreshTable();

		} else if (e.getSource() == menuObjectUpdate)  {	//event to update tickets
			
			String typeUpdate = null;		//define string variable for decision about what to update

			//ask user if they want to set ticket state to resolved or closed
			typeUpdate = JOptionPane.showInputDialog(null, "Do you want to (R)esolve or (C)lose or (M)odify a Ticket");
			
				if (typeUpdate.equalsIgnoreCase("R")) {	//select R to call the method to enter resolution data
					dao.updateTicketResolved();
					refreshTable();
				} else if
					(typeUpdate.equalsIgnoreCase("C")) { //select C to call the method to enter closure data
					dao.updateTicketClosed();
					refreshTable();
				} else if
					(typeUpdate.equalsIgnoreCase("M")) {	//select M to call the method to modify ticket data
					dao.modifyTicket();
					refreshTable();
				} else
					JOptionPane.showMessageDialog(null, "Invalid Update Selection");	//handle invalid input
				
		} else if (e.getSource() == menuObjectDelete) {		//event to delete ticket record from DB
			dao.deleteTicket();								//with call to deleteTicket method in dao class
			refreshTable();
		}
	}
	
	public void refreshTable() {
		//retrieve ticket information for viewing in JTable
		try {

			stmnt = Dao.getConnection().createStatement();	//connect to DB
			
			//sql select statement to produce the ticket table data for viewing in gui
			ResultSet results = stmnt.executeQuery("SELECT sgt.ticket_id, sgt.emp_user_id, "
					+ "sgu.emp_full_name, sgt.dept_id, sgt.ticket_status_id, sgt.submit_date, "
					+ "sgt.issue_description, estimate_fix_date, sgt.issue_resolution, "
					+ "sgt.resolution_date, sgt.closed_description, sgt.closed_date "
					+ "FROM s_grif_tickets sgt "
					+ "INNER JOIN s_grif_users sgu ON sgt.emp_user_id = sgu.emp_user_id "
					+ "ORDER BY sgt.ticket_id");
					
			// Use JTable built in functionality to build a table model and
			// display the table model off your result set!!!
			JTable jt = new JTable(ticketsJTable.buildTableModel(results));

			jt.setBounds(50, 50, 300, 300);
			sp = new JScrollPane(jt);
			mainFrame.add(sp);
			mainFrame.setVisible(true); // refreshes or repaints frame on screen
			
			stmnt.close();  // close connections

		} catch (SQLException e1) {
				e1.printStackTrace();
		}
	}
}
