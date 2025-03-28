package authentication;

import database.DBconnection;
import java.sql.*;


public class UserAuthentication {
    public static boolean authenticateUser(String username,String password,String role){
        String query="SELECT * FROM users WHERE name=? AND password=? AND role=?";
        try(Connection conn=DBconnection.getConnection();
        PreparedStatement stmt=conn.prepareStatement(query)){
            stmt.setString(1, username);
            stmt.setString(2,password);
            stmt.setString(3,role);

            ResultSet rs=stmt.executeQuery();
            return rs.next();
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registerUser(String username,String password){
        String query="INSERT INTO users (name,password,role) VALUES (?,?,'reader')";
        try(Connection conn=DBconnection.getConnection();
        PreparedStatement stmt=conn.prepareStatement(query)){
            stmt.setString(1,username);
            stmt.setString(2, password);
            int rowsInserted=stmt.executeUpdate();

            return rowsInserted>0;
        }
        catch(SQLException e){
            if(e.getErrorCode()==1062){
                System.out.println("Error: Username already exists.");
            }
            else{
                e.printStackTrace();
            }
        }
        return false;
    }
    public static int getUserId(String name) {
        String sql = "SELECT id FROM users WHERE name = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
