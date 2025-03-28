package database;
import java.sql.*;

public class DBconnection {

    private static final String URL="jdbc:mysql://localhost:3306/librarydb";
    private static final String USER="root";
    private static final String PASSWORD="Javajdbccon@27";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
    
}


