package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryLocationTable {
	public static final String queryStatement = "select * from Location";
	public static void main(String[] args) throws SQLException
	{
		Connection connect = DriverManager.getConnection(CreateLocationTable.url);
		Statement statement = connect.createStatement();
		ResultSet resultSet = statement.executeQuery(queryStatement);
		ResultSetMetaData metaData = resultSet.getMetaData();
		int count = metaData.getColumnCount();
		for(int i = 1; i <= count; i++) System.out.format("%20s |", metaData.getColumnName(i));
		while(resultSet.next())
		{
			System.out.println();
			for(int i = 1; i < count;i++) System.out.format("%20s |", resultSet.getString(i));
			System.out.format("%-300s", resultSet.getString(count));
		}
		if(statement != null)
			statement.close();
		if(connect != null)
			statement.close();
	}
}
