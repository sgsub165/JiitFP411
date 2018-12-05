/** 
* Name: Stephen Griffith
* Date: 11/21/2018
* Source File Name: ticketsJTable.java
* Lab: Final Project 411
*/

package fp411;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 * The purpose of this class is to provide for creating JTable
 */

public class ticketsJTable {

	@SuppressWarnings("unused")
	private final DefaultTableModel tableModel = new DefaultTableModel();	//table model object creation

	//method to create the table model
	public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

		ResultSetMetaData metaData = rs.getMetaData();

		// define the column names
		Vector<String> columnNames = new Vector<String>();
		int columnCount = metaData.getColumnCount();
		for (int column = 1; column <= columnCount; column++) {
			columnNames.add(metaData.getColumnName(column));
		}

		// define the table data
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();
		while (rs.next()) {
			Vector<Object> vector = new Vector<Object>();
			for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
				vector.add(rs.getObject(columnIndex));
			}
			data.add(vector);
		}
		// return data/col.names for JTable
		return new DefaultTableModel(data, columnNames); 

	}

}
