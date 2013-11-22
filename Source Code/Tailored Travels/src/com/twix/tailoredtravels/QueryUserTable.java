package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryUserTable {
	public static final String queryStatement = "select * from UserPassword";
	public static void main(String[] args) throws SQLException
	{
		Connection connect = DriverManager.getConnection(CreateUserTable.url);
		Statement statement = connect.createStatement();
		ResultSet resultSet = statement.executeQuery(queryStatement);
		ResultSetMetaData metaData = resultSet.getMetaData();
		int count = metaData.getColumnCount();
		System.out.format("%5s |", metaData.getColumnName(1));
		for(int i = 2; i <= count; i++) System.out.format("%25s |", metaData.getColumnName(i));
		while(resultSet.next())
		{
			//print all the user table in a readable format
			System.out.println();
			System.out.format("%5s |", resultSet.getString(1));			
			for(int i = 2; i <= count;i++) System.out.format("%25s |", resultSet.getString(i));
		}
		if(statement != null)
			statement.close();
		if(connect != null)
			statement.close();
	}
}
