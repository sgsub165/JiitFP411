/** 
* Name: Stephen Griffith
* Date: 11/21/2018
* Source File Name: Dao.java
* Lab: Final Project
*/

package fp411;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Date;
import java.util.List;

//import javax.swing.JFrame;
import javax.swing.JOptionPane;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.JTable;

public class Dao {
	// instance fields
	static Connection  cnct = null;
	static Statement stmnt = null;
	static String sql = null;
	static PreparedStatement prepedStmnt = null;

	// constructor
	public Dao() {
		System.out.println("Welcome to the Trouble Ticket System\n");
	//createTables();
	}
	 
	public static Connection getConnection() {
		// Setup the connection with the DB
		try {
			cnct = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/FP411?autoReconnect=true&useSSL=false"
							+ "&user=sgriffit&password=sgriffit1");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cnct;
	}

	public void createTables() throws SQLException {
		// variables for SQL Query table creations
		
		System.out.println("Creating tables in Trouble Ticket database...");
		
		final String createTicketStatusTable = "CREATE TABLE IF NOT EXISTS s_grif_ticketstatus "
				+ "(ticket_status_id VARCHAR(10) NOT NULL, "
				+ "PRIMARY KEY(ticket_status_id))";
		
		final String createDeptTable = "CREATE TABLE IF NOT EXISTS s_grif_depts "
				+ "(dept_id VARCHAR(10) NOT NULL, "
				+ "PRIMARY KEY(dept_id))";
		
		final String createUsersTable = "CREATE TABLE IF NOT EXISTS s_grif_users "
				+ "(emp_user_id VARCHAR(20) NOT NULL, " 
				+ "emp_passwrd VARCHAR(30) NOT NULL, " 
				+ "emp_full_name VARCHAR(45) NOT NULL, " 
				+ "dept_id VARCHAR(10) NOT NULL, "
				+ "PRIMARY KEY(emp_user_id))";
		
		final String createTicketsTable = "CREATE TABLE IF NOT EXISTS s_grif_tickets "
				+ "(ticket_id INT AUTO_INCREMENT, "
				+ "emp_user_id VARCHAR(20) NOT NULL, "
				+ "dept_id VARCHAR(10) NOT NULL, "
				+ "ticket_status_id VARCHAR(10) NOT NULL, "
				+ "submit_date DATETIME NOT NULL, "
				+ "issue_description VARCHAR(512) NOT NULL, "
				+ "estimate_fix_date DATE NULL, "
				+ "issue_resolution VARCHAR(256) NULL, "
				+ "resolution_date DATETIME NULL, "
				+ "closed_description VARCHAR(256) NULL, "
				+ "closed_date DATETIME NULL, "
				+ "PRIMARY KEY(ticket_id), "
				+ "FOREIGN KEY(ticket_status_id) REFERENCES s_grif_ticketstatus(ticket_status_id), "
				+ "FOREIGN KEY(dept_id) REFERENCES s_grif_depts(dept_id), "
				+ "FOREIGN KEY(emp_user_id) REFERENCES s_grif_users(emp_user_id))";
				
		try {

			// execute queries to create tables

			stmnt = getConnection().createStatement();

			stmnt.executeUpdate(createTicketStatusTable);
			System.out.println("\tSuccessfully created Ticket Status table in Trouble Ticket database.");
			stmnt.executeUpdate(createDeptTable);
			System.out.println("\tSuccessfully created Department table in Trouble Ticket database.");
			stmnt.executeUpdate(createUsersTable);
			System.out.println("\tSuccessfully created Users table in Trouble Ticket database.");
			stmnt.executeUpdate(createTicketsTable);
			System.out.println("\tSuccessfully created Tickets table in Trouble Ticket database.");
			System.out.println("Tables have been created and ready for data insertion.");

			// end create table
			// close connection/statement object
			stmnt.close();
//			cnct.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
		addDepts();
		addTicketStatus();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // array list to hold
												  	  // spreadsheet rows &
													  // columns

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			stmnt = getConnection().createStatement();
			
			System.out.println("\nInserting Users into the User table...");

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "INSERT INTO s_grif_users(emp_user_id, emp_passwrd, emp_full_name, dept_id) " 
						+ "VALUES('" + rowData.get(0) 
						+ "','" + rowData.get(1) 
						+ "','" + rowData.get(2) 
						+ "','" + rowData.get(3) 
						+ "');";
				stmnt.executeUpdate(sql);
			}
			System.out.println("\tUser inserts completed in the Trouble Ticket database.");

			// close statement object
			stmnt.close();
 
		} catch (Exception e) {
			System.out.println( e.getMessage());
		}
	}
	
	public void addDepts() {
		
		//set up the DB connection
		
		try {
			stmnt = getConnection().createStatement();
			
			System.out.println("\nInserting Department values into the Department table...");
			
			sql = "INSERT INTO s_grif_depts(dept_id)" + "VALUES('IT')";
			stmnt.executeUpdate(sql);
			sql = "INSERT INTO s_grif_depts(dept_id)" + "VALUES('DEV')";
			stmnt.executeUpdate(sql);
			sql = "INSERT INTO s_grif_depts(dept_id)" + "VALUES('TACS')";
			stmnt.executeUpdate(sql);
			sql = "INSERT INTO s_grif_depts(dept_id)" + "VALUES('TEST')";
			stmnt.executeUpdate(sql);
			
			System.out.println("\tDepartment inserts completed in the Trouble Ticket database.");
			
			stmnt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void addTicketStatus() {

		//set up the DB connection
		
		try {
			stmnt = getConnection().createStatement();
			
			System.out.println("\nInserting Ticket Status values into the Ticket Status table...");
			
			sql = "INSERT INTO s_grif_ticketstatus(ticket_status_id)" + "VALUES('OPEN')";
			stmnt.executeUpdate(sql);
			sql = "INSERT INTO s_grif_ticketstatus(ticket_status_id)" + "VALUES('RESOLVED')";
			stmnt.executeUpdate(sql);
			sql = "INSERT INTO s_grif_ticketstatus(ticket_status_id)" + "VALUES('CLOSED')";
			stmnt.executeUpdate(sql);
			
			System.out.println("\tTicket Status inserts completed in the Trouble Ticket database.");
			
			stmnt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void createTicket() {
		
		try {

			// get ticket information
			String submitterName = JOptionPane.showInputDialog(null, "Enter your Employee User ID");
			String submitterDept = JOptionPane.showInputDialog(null, "Enter your Department ID");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
			String estimate_fix_date = JOptionPane.showInputDialog(null, "Enter a date the ticket must be resolved");

			// insert ticket information to database

			stmnt = getConnection().createStatement();

			int result = stmnt
				.executeUpdate("INSERT INTO s_grif_tickets"
						+ "(emp_user_id, dept_id, ticket_status_id, "
						+ "submit_date, issue_description, estimate_fix_date) " 
						+ "VALUES(" + " '" + submitterName + "','" + submitterDept + "', 'OPEN', now(), "
						+ "'" + ticketDesc + "','" + estimate_fix_date + "')", Statement.RETURN_GENERATED_KEYS);
		
			// retrieve ticket id number newly auto generated upon record insertion
			ResultSet resultSet = null;
			resultSet = stmnt.getGeneratedKeys();
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
			stmnt.close();

		} catch (SQLException ex) {
				ex.printStackTrace();
		}

	}
	
	public void readTicket() {
		
	}
	public void updateTicketResolved() {

		try {
		
		String resolvedTicketID = JOptionPane.showInputDialog(null, "Enter Ticket ID for Resolution");
		String resolutionDescription = JOptionPane.showInputDialog(null, "Enter a description of the resolution");

		String updateTableSQL = "UPDATE s_grif_tickets SET ticket_status_id = 'RESOLVED', issue_resolution = ?, resolution_date = now() WHERE ticket_id = ?";

		stmnt = getConnection().createStatement();
		prepedStmnt = cnct.prepareStatement(updateTableSQL);
		prepedStmnt.setString(1, resolutionDescription);
		prepedStmnt.setInt(2, Integer.parseInt(resolvedTicketID));

			// execute update SQL statement
		prepedStmnt.executeUpdate();

			System.out.println("Trouble Ticket has been updated to Resolved");
			
			stmnt.close();

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} 
		
	}

	
	public void updateTicketClosed() {
		
		try {
			
		String closedTicketID = JOptionPane.showInputDialog(null, "Enter Ticket ID to close");
		String closedDescription = JOptionPane.showInputDialog(null, "Enter a description of the resolution");

		String updateTableSQL = "UPDATE s_grif_tickets SET ticket_status_id = 'CLOSED', closed_description = ?, closed_date = now() WHERE ticket_id = ?";

		stmnt = getConnection().createStatement();
		prepedStmnt = cnct.prepareStatement(updateTableSQL);
		prepedStmnt.setString(1, closedDescription);
		prepedStmnt.setInt(2, Integer.parseInt(closedTicketID));

			// execute update SQL statement
		prepedStmnt.executeUpdate();

			System.out.println("Trouble Ticket has been updated to Closed");
			
			stmnt.close();

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} 
		
	}

	public void deleteTicket() {
		
	}
	
	// add other desired CRUD methods needed like for updates, deletes, etc.
	//DELETE FROM `fp411`.`s_grif_tickets` WHERE (`ticket_id` = '1');
}
