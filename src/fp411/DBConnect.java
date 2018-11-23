package fp411;
/** 
* Name: Stephen Griffith
* Date: 11/14/2018
* Source File Name: DBConnect.java
* Lab: Lab4
*/


/**
 * The purpose of this class is to provide a method to connect
 * to the database.
 */

//import the java DB connection interface, JDBC management class and SQL exception
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
  
public class DBConnect {		//class header
 
	// define the URL for the DB to be connected
	static final String DB_URL = "jdbc:mysql://www.papademas.net:3307/411labs?autoReconnect=true&useSSL=false";
	//static final String DB_URL = "jdbc:mysql://localhost:3306/lab4?autoReconnect=true&useSSL=false";
	
	// define the DB user credentials
	static final String USERID = "db411", PASSWD = "411";	
	//static final String USERID = "root", PASSWD = "eOncw7)obb";

	//DB connect method with exception handling
	public Connection connect() throws SQLException {

		return DriverManager.getConnection(DB_URL, USERID, PASSWD);	//return elements for connection

	}
}

