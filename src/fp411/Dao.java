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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dao {
	// instance fields
	static Connection  cnct = null;
	Statement stmnt = null;

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
		
		final String createTicketStatusTable = "CREATE TABLE s_grif_ticketstatus "
				+ "(ticket_status_id VARCHAR(10) NOT NULL, "
				+ "PRIMARY KEY(ticket_status_id))";
		
		final String createDeptTable = "CREATE TABLE s_grif_depts "
				+ "(dept_id VARCHAR(10) NOT NULL, "
				+ "PRIMARY KEY(dept_id))";
		
		final String createUsersTable = "CREATE TABLE s_grif_users "
				+ "(emp_user_id VARCHAR(20) NOT NULL, " 
				+ "emp_passwrd VARCHAR(30) NOT NULL, " 
				+ "emp_full_name VARCHAR(45) NOT NULL, " 
				+ "dept_id VARCHAR(10) NOT NULL, "
				+ "PRIMARY KEY(emp_user_id))";
//				+ "FOREIGN KEY(dept_id) REFERENCES s_grif_depts(dept_id))";
		
		final String createTicketsTable = "CREATE TABLE s_grif_tickets "
				+ "(ticket_id INT AUTO_INCREMENT, "
				+ "emp_user_id VARCHAR(20) NOT NULL, "
				+ "dept_id VARCHAR(10) NOT NULL, "
				+ "ticket_status_id VARCHAR(10) NOT NULL, "
				+ "submit_date DATE NOT NULL, "
				+ "issue_description VARCHAR(512) NOT NULL, "
				+ "estimate_fix_date DATE NULL, "
				+ "issue_resolution VARCHAR(256) NULL, "
				+ "resolution_date DATE NULL, "
				+ "PRIMARY KEY(ticket_id), "
				+ "FOREIGN KEY(ticket_status_id) REFERENCES s_grif_ticketstatus(ticket_status_id))";
				
		try {

			// execute queries to create tables

			stmnt = getConnection().createStatement();

			stmnt.executeUpdate(createTicketStatusTable);
			stmnt.executeUpdate(createDeptTable);
			stmnt.executeUpdate(createUsersTable);
			stmnt.executeUpdate(createTicketsTable);
			System.out.println("Created tables in Trouble Ticket database.");

			// end create table
			// close connection/statement object
			stmnt.close();
			cnct.close();
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

		// variables for SQL Query inserts
		String sql;

		Statement statement;
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

			statement = getConnection().createStatement();
			
			System.out.println("\tInserting Users into the User table...");

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "INSERT INTO s_grif_users(emp_user_id, emp_passwrd, emp_full_name, dept_id) " 
						+ "VALUES('" + rowData.get(0) 
						+ "','" + rowData.get(1) 
						+ "','" + rowData.get(2) 
						+ "','" + rowData.get(3) 
						+ "');";
				statement.executeUpdate(sql);
			}
			System.out.println("User inserts completed in the Trouble Ticket database.");

			// close statement object
			statement.close();
 
		} catch (Exception e) {
			System.out.println( e.getMessage());
		}
	}
	
	public void addDepts() {
		String sql;
		Statement statement;
		//set up the DB connection
		
		try {
			statement = getConnection().createStatement();
			System.out.println("Successful connection to the Trouble Ticket database.");
			
			System.out.println("\tInserting Departments into the Department table...");
			
			sql = "INSERT INTO s_grif_depts(dept_id)" + "VALUES('IT')";
			statement.executeUpdate(sql);
			sql = "INSERT INTO s_grif_depts(dept_id)" + "VALUES('DEV')";
			statement.executeUpdate(sql);
			sql = "INSERT INTO s_grif_depts(dept_id)" + "VALUES('TACS')";
			statement.executeUpdate(sql);
			sql = "INSERT INTO s_grif_depts(dept_id)" + "VALUES('TEST')";
			statement.executeUpdate(sql);
			
			System.out.println("Department inserts completed in the Trouble Ticket database.");
			
			statement.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addTicketStatus() {
		String sql;
		Statement statement;
		//set up the DB connection
		
		try {
			statement = getConnection().createStatement();
			System.out.println("Successful connection to the Trouble Ticket database.");
			
			System.out.println("Inserting Ticket Status into the Ticket Status table...");
			
			sql = "INSERT INTO s_grif_ticketstatus(ticket_status_id)" + "VALUES('OPEN')";
			statement.executeUpdate(sql);
			sql = "INSERT INTO s_grif_ticketstatus(ticket_status_id)" + "VALUES('RESOLVED')";
			statement.executeUpdate(sql);
			sql = "INSERT INTO s_grif_ticketstatus(ticket_status_id)" + "VALUES('CLOSED')";
			statement.executeUpdate(sql);
			
			System.out.println("Ticket Status inserts completed in the Trouble Ticket database.");
			
			statement.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	// add other desired CRUD methods needed like for updates, deletes, etc.
	//DELETE FROM `fp411`.`s_grif_tickets` WHERE (`ticket_id` = '1');
}
