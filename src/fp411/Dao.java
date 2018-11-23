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
	static Connection connect = null;
	Statement statement = null;

	// constructor
	public Dao() {
	createTables();
	}
	 
	public static Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/FP411?autoReconnect=true&useSSL=false"
							+ "&user=sgriffit&password=sgriffit1");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	public void createTables() {
		// variables for SQL Query table creations
		final String createTicketsTable = "CREATE TABLE s_grif_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "ticket_issuer VARCHAR(30), ticket_description VARCHAR(200))";
		final String createUsersTable = "CREATE TABLE s_grif_users(uid INT AUTO_INCREMENT PRIMARY KEY, "
				+ "uname VARCHAR(30), upass VARCHAR(30))";

		try {

			// execute queries to create tables

			statement = getConnection().createStatement();

			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
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

				sql = "insert into s_grif_users(uname,upass) " + "values('" + rowData.get(0) + "','" + rowData.get(1)
						+ "');";
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
