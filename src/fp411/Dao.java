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

	public void createTables() {
		// variables for SQL Query table creations
//		final String createTicketsTable = "CREATE TABLE s_grif_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, "
//				+ "submitter_id VARCHAR(20) NOT NULL, submitter_dept_id VARCHAR(5) NOT NULL)";
		

		final String createTicketsTable = "CREATE TABLE s_grif_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "submitter_id VARCHAR(20) NOT NULL, submitter_dept_id VARCHAR(10) NOT NULL, "
				+ "submit_date DATE NOT NULL, issue_description VARCHAR(512) NOT NULL, "
				+ "assigned_to_fix VARCHAR(20) NULL, issue_resolution_description VARCHAR(256) NULL, "
				+ "resolution_date DATE NULL, target_release VARCHAR(10) NULL, "
				+ "verification_description VARCHAR(256) NULL, verfication_date DATE NULL)";
		
		final String createUsersTable = "CREATE TABLE s_grif_users(employee_id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "employee_user_id VARCHAR(20) NOT NULL, employee_passwrd VARCHAR(30) NOT NULL, "
				+ "employee_full_name VARCHAR(45), employee_department_id VARCHAR(10))";

		try {

			// execute queries to create tables

			stmnt = getConnection().createStatement();

			stmnt.executeUpdate(createTicketsTable);
			stmnt.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			stmnt.close();
			cnct.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
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

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into s_grif_users(employee_user_id, employee_passwrd, employee_full_name, employee_department_id) " 
				+ "values('" + rowData.get(0) + "','" + rowData.get(1) + "','" + rowData.get(2) + "','" + rowData.get(3) + "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close statement object
			statement.close();
 
		} catch (Exception e) {
			System.out.println( e.getMessage());
		}
	}
	// add other desired CRUD methods needed like for updates, deletes, etc.
	//DELETE FROM `fp411`.`s_grif_tickets` WHERE (`ticket_id` = '1');
}
