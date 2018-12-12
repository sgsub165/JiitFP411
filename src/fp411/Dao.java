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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;


public class Dao {
	// class instance fields
	static Connection  cnct = null;
	static Statement stmnt = null;
	static String sql = null;
	static PreparedStatement prepedStmnt = null;

	// class constructor method
	public Dao() {
		System.out.println("Welcome to the Trouble Ticket System\n");	//welcome banner to console
	}
	 
	public static Connection getConnection() {  //sql database connection method
		// Setup the connection with the DB
		try {		//try/catch block for connection to database
			
			cnct = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
			
//			cnct = DriverManager
//					.getConnection("jdbc:mysql://localhost:3306/FP411?autoReconnect=true&useSSL=false"
//							+ "&user=sgriffit&password=sgriffit1");
							
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cnct;
	}

	public void createTables() throws SQLException {		//method to create the database tables
		
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
				+ "estimate_fix_date DATE NOT NULL, "
				+ "issue_resolution VARCHAR(256) NULL, "
				+ "resolution_date DATETIME NULL, "
				+ "closed_description VARCHAR(256) NULL, "
				+ "closed_date DATETIME NULL, "
				+ "PRIMARY KEY(ticket_id), "
				+ "FOREIGN KEY(ticket_status_id) REFERENCES s_grif_ticketstatus(ticket_status_id), "
				+ "FOREIGN KEY(dept_id) REFERENCES s_grif_depts(dept_id), "
				+ "FOREIGN KEY(emp_user_id) REFERENCES s_grif_users(emp_user_id))";
				
		try {

			// execute sql statements to create tables
			stmnt = getConnection().createStatement();

			stmnt.executeUpdate(createTicketStatusTable);
			System.out.println("\tSuccessfully created Ticket Status table in Trouble Ticket database.");
			JOptionPane.showMessageDialog(null, "Ticket Status Table Created");
			stmnt.executeUpdate(createDeptTable);
			System.out.println("\tSuccessfully created Department table in Trouble Ticket database.");
			JOptionPane.showMessageDialog(null, "Departments Table Created");
			stmnt.executeUpdate(createUsersTable);
			System.out.println("\tSuccessfully created Users table in Trouble Ticket database.");
			JOptionPane.showMessageDialog(null, "Users Table Created");
			stmnt.executeUpdate(createTicketsTable);
			System.out.println("\tSuccessfully created Tickets table in Trouble Ticket database.");
			JOptionPane.showMessageDialog(null, "Tickets Table Created");
			System.out.println("Tables have been created and ready for data insertion.");

			// end create table
			// close connection/statement object
			stmnt.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//method to add the user data from the .csv file into the DB user table
	public void addUsers() {
		// add the list of users from userlist.csv file to users table

		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // array list to hold
												  	  // spreadsheet rows &
													  // columns
		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String row;
			while ((row = br.readLine()) != null) {
				array.add(Arrays.asList(row.split(",")));
			}
		} catch (Exception e) {
			System.out.println("File Load Error!!!\nPlease check file existence and integrity.");
			JOptionPane.showMessageDialog(null, "File Load Error!\nPlease check file existence and integrity");
			JOptionPane.showMessageDialog(null, "Program will now exit");
			System.exit(0);
		}

		try {

			// Setup the connection to the database
			stmnt = getConnection().createStatement();
			System.out.println("\nInserting Users into the User table...");

			// loop to access each array index containing a list of values
			// and insert that data into the User table
			for (List<String> userRowData : array) {

				sql = "INSERT INTO s_grif_users(emp_user_id, emp_passwrd, emp_full_name, dept_id) " 
						+ "VALUES('" + userRowData.get(0) 
						+ "','" + userRowData.get(1) 
						+ "','" + userRowData.get(2) 
						+ "','" + userRowData.get(3) 
						+ "');";
				
				//execute the sql statement to insert users
				stmnt.executeUpdate(sql);
			}
			System.out.println("\tUser inserts completed in the Trouble Ticket database.");
			JOptionPane.showMessageDialog(null, "Users have been inserted into Users table");

			// close statement object
			stmnt.close();
 
		} catch (Exception e) {
			System.out.println( e.getMessage());
		}
	}
	
	//method to add the department elements into the departments table
	public void addDepts() {
		
		try {
			stmnt = getConnection().createStatement();		//set up the DB connection
			
			System.out.println("\nInserting Department values into the Department table...");
			
			//sql statements to insert elements and execute the sql statements
			sql = "INSERT INTO s_grif_depts(dept_id)" + "VALUES('IT')";
			stmnt.executeUpdate(sql);
			sql = "INSERT INTO s_grif_depts(dept_id)" + "VALUES('DEV')";
			stmnt.executeUpdate(sql);
			sql = "INSERT INTO s_grif_depts(dept_id)" + "VALUES('TACS')";
			stmnt.executeUpdate(sql);
			sql = "INSERT INTO s_grif_depts(dept_id)" + "VALUES('TEST')";
			stmnt.executeUpdate(sql);
			sql = "INSERT INTO s_grif_depts(dept_id)" + "VALUES('HR')";
			stmnt.executeUpdate(sql);
			
			System.out.println("\tDepartment inserts completed in the Trouble Ticket database.");
			JOptionPane.showMessageDialog(null, "Departments have been inserted into Dept table");
			
			stmnt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	//method to add the ticket status elements into the ticket status table
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
			JOptionPane.showMessageDialog(null, "Ticket Status types have been inserted into Status table");
			stmnt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	//method to create and insert the new ticket records into the tickets table
	public void createTicket() {
		
		try {

			// prompt user to request ticket information for insertion
			String submitterName = JOptionPane.showInputDialog(null, "Enter your Employee User ID");
			String submitterDept = JOptionPane.showInputDialog(null, "Enter your Department ID");
			String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
			String estimate_fix_date = JOptionPane.showInputDialog(null, "Enter a date the ticket must be resolved (ex. 2018-2-4)");

			stmnt = getConnection().createStatement();

			// insert ticket information into database
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
				JOptionPane.showMessageDialog(null, "Ticket id: " + id + " is created");
			} else {
				System.out.println("ERROR: Ticket cannot be created!!!");
			}
			stmnt.close();

		} catch (SQLException ex) {
				ex.printStackTrace();
		}
	}
	
	//method to update the tickets table for information about the resolution of the original issue
	public void updateTicketResolved() {

		try {
		//prompt user for resolution information to update the tickets table
		String resolvedTicketID = JOptionPane.showInputDialog(null, "Enter Ticket ID for Resolution");
		String resolutionDescription = JOptionPane.showInputDialog(null, "Enter a description of the resolution");

		//sql statement to update the tickets table after original issue has been resolved
		String updateTableSQL = "UPDATE s_grif_tickets SET ticket_status_id = 'RESOLVED', issue_resolution = ?, resolution_date = now() WHERE ticket_id = ?";

		//establish connection to DB and update the variables for prepared statements
		stmnt = getConnection().createStatement();
		prepedStmnt = cnct.prepareStatement(updateTableSQL);
		prepedStmnt.setString(1, resolutionDescription);
		prepedStmnt.setInt(2, Integer.parseInt(resolvedTicketID));

		// execute the update SQL statement
		prepedStmnt.executeUpdate();

			System.out.println("Trouble Ticket has been updated to Resolved");
			JOptionPane.showMessageDialog(null, "Ticket id: " + resolvedTicketID + " updated to Resolved");
			
			stmnt.close();

		} catch (SQLException e) {

			System.out.println(e.getMessage());
		} 		
	}
	
	//method to update the tickets table for information about the closure of the original issue
	public void updateTicketClosed() {
		
		try {

		//prompt user for closure information to update the tickets table	
		String closedTicketID = JOptionPane.showInputDialog(null, "Enter Ticket ID to close");
		String closedDescription = JOptionPane.showInputDialog(null, "Enter a verfication description for close");

		String updateTableSQL = "UPDATE s_grif_tickets SET ticket_status_id = 'CLOSED', "
				+ "closed_description = ?, closed_date = now() WHERE ticket_id = ?";
		
		//establish connection to DB and update the variables for prepared statements
		stmnt = getConnection().createStatement();
		prepedStmnt = cnct.prepareStatement(updateTableSQL);
		prepedStmnt.setString(1, closedDescription);
		prepedStmnt.setInt(2, Integer.parseInt(closedTicketID));

		// execute the update SQL statement
		prepedStmnt.executeUpdate();

			System.out.println("Trouble Ticket has been updated to Closed");
			JOptionPane.showMessageDialog(null, "Ticket id: " + closedTicketID + " updated to Closed");
			
			stmnt.close();

		} catch (SQLException e) {

			System.out.println(e.getMessage());
		} 		
	}
	
	public void modifyTicket() {
		
		try {

		//prompt user for closure information to update the tickets table	
		String modifyTicketID = JOptionPane.showInputDialog(null, "Enter Ticket ID to modify description");
		String modifyDescription = JOptionPane.showInputDialog(null, "Enter the new issue description for update");

		String updateTableSQL = "UPDATE s_grif_tickets SET issue_description = ? WHERE ticket_id = ?";
		
		//establish connection to DB and update the variables for prepared statements
		stmnt = getConnection().createStatement();
		prepedStmnt = cnct.prepareStatement(updateTableSQL);
		prepedStmnt.setString(1, modifyDescription);
		prepedStmnt.setInt(2, Integer.parseInt(modifyTicketID));

		// execute the update SQL statement
		prepedStmnt.executeUpdate();

			System.out.println("Trouble Ticket has been updated");
			JOptionPane.showMessageDialog(null, "Ticket id: " + modifyTicketID + " updated");
			
			stmnt.close();

		} catch (SQLException e) {

			System.out.println(e.getMessage());
		} 		
	}
	
	
	//method to delete tickets from the database
	public void deleteTicket() {	
		
		try {
		//prompt user for ticket to delete	
		String deletedTicketID = JOptionPane.showInputDialog(null, "Enter Ticket ID to delete");

		//sql statement to delete ticket records
		String updateTableSQL = "DELETE FROM s_grif_tickets WHERE ticket_id = ?";

		//establish connection to DB and update the variables for prepared statements
		stmnt = getConnection().createStatement();
		prepedStmnt = cnct.prepareStatement(updateTableSQL);
		prepedStmnt.setInt(1, Integer.parseInt(deletedTicketID));
		
		int response = JOptionPane.showConfirmDialog(null, 
			"Are you sure you want to delete ticket #" + deletedTicketID + "?",
			"Confirm Deletion", JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE);
		if(response == JOptionPane.NO_OPTION) {
			System.out.println("No record deleted");
		} else if(response == JOptionPane.YES_OPTION) {

		// execute the update SQL statement
		prepedStmnt.executeUpdate();

			System.out.println("Trouble Ticket has been Deleted");
			JOptionPane.showMessageDialog(null, "Ticket id: " + deletedTicketID + " is Deleted");
		}
			stmnt.close();

		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}
	}
}
