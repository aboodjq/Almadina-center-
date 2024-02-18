package test;
import java.sql.*;

public class DatabaseConnectionManager {   
//
//    public static Connection getConnection() throws SQLException {
//        // JDBC URL, username, and password for database connection
//        String url = "jdbc:mysql://localhost:3306/madinacentre";
//        String username = "root";
//        String password = "";
//
//        // Declare a variable for the database connection
//        Connection con = null;
//
//        try {
//            // Load the MySQL JDBC driver
//            Class.forName("com.mysql.cj.jdbc.Driver");
//
//            // Establish the database connection
//            con = DriverManager.getConnection(url, username, password);
//        } catch (ClassNotFoundException e) {
//            // Handle ClassNotFoundException (e.g., driver not found)
//            e.printStackTrace();
//        }
//
//        return con;
//    }
	
	public static Connection getConnection() {
	    String url = "jdbc:mysql://localhost:3306/madinacentre";
	    String username = "root";
	    String password = "sniper";
	    Connection con = null;
	    
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        con = DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	        System.err.println("MySQL JDBC driver not found.");
	        e.printStackTrace();
	    } catch (SQLException e) {
	        System.err.println("Failed to connect to the database.");
	        e.printStackTrace();
	    }

	    return con;
	}

}
