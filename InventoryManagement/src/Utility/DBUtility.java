package Utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtility {

		public static Connection getconnection()
		{
			Connection con=null;
			try
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				String url="jdbc:mysql://localhost:3306/inventory";
				String user="your_mysql_username";
				String pass="your_mysql_password";    // Replace with your local password and username before running
				con=DriverManager.getConnection(url, user, pass);
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			return con;
		}
	}


