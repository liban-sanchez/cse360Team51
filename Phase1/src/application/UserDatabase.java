package application;

import java.sql.*;

public class UserDatabase {
    private static final String DB_URL = "jdbc:h2:~/userDatabase";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Initialize the H2 database connection
    public static Connection connectDatabase() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
    
    // Create the users table
    public static void createTable() throws SQLException {
    	//dropTable();
        String userInfo = "CREATE TABLE IF NOT EXISTS users (" +
                          "id INT AUTO_INCREMENT PRIMARY KEY, " +
                          "username VARCHAR(255) UNIQUE, " +
                          "password VARCHAR(255), " +
                          "email VARCHAR(255), " +
                          "first_name VARCHAR(255), " +
                          "middle_name VARCHAR(255), " + 
                          "last_name VARCHAR(255), " +
                          "preferred_name VARCHAR(255), " + 
                          "role VARCHAR(50))";
        try (Connection conn = connectDatabase(); Statement stmt = conn.createStatement()) {
            stmt.execute(userInfo);
        }
    }
    
    // Insert a new user into the database
    public static void insertUser(String username, String password, String email, 
                                  String firstName, String lastName, String role) throws SQLException {
        String userInfo = "INSERT INTO users (username, password, email, first_name, last_name, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connectDatabase(); PreparedStatement pstmt = conn.prepareStatement(userInfo)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); 
            pstmt.setString(3, email);
            pstmt.setString(4, firstName);
            pstmt.setString(5, lastName);
            pstmt.setString(6, role);
            pstmt.executeUpdate();
        }
    }
    
    public static void dropTable() throws SQLException {
        String dropQuery = "DROP TABLE IF EXISTS users";
        try (Connection conn = connectDatabase(); Statement stmt = conn.createStatement()) {
            stmt.execute(dropQuery);
        }
    }
    
    // Clear the database if needed
    public static void clearUsers() {
        String deleteQuery = "DELETE FROM users";
        try (Connection conn = connectDatabase(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteQuery);
            System.out.println("All users deleted from the database.");
        } catch (SQLException e) {
            System.err.println("Error clearing users: " + e.getMessage());
        }
    }
    
    // Delete a user from the database
    public static boolean deleteUser(String username, String email) throws SQLException {
        String deleteQuery = "DELETE FROM users WHERE username = ? AND email = ?";
        try (Connection conn = connectDatabase(); PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0; // Return true if a user was deleted
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    // Retrieve user data
    public static ResultSet getUserByUsername(String username) throws SQLException {
        String userInfo = "SELECT * FROM users WHERE username = ?";
        Connection conn = connectDatabase();
        PreparedStatement pstmt = conn.prepareStatement(userInfo);
        pstmt.setString(1, username);
        return pstmt.executeQuery();
    }
    
    // Check if the database is empty
    public static boolean isDatabaseEmpty() {
        String countQuery = "SELECT COUNT(*) FROM users";
        try (Connection conn = connectDatabase(); 
             PreparedStatement pstmt = conn.prepareStatement(countQuery)) {
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; 
            }
        } catch (SQLException e) {
            System.err.println("Error checking database: " + e.getMessage());
        }
        return false; 
    }
    
 // Retrieve all users from the database
    public static ResultSet getAllUsers() throws SQLException {
        String query = "SELECT username, role FROM users";
        Connection conn = connectDatabase();
        PreparedStatement pstmt = conn.prepareStatement(query);
        return pstmt.executeQuery();
    }

    // Add role to a user
    public static void addRoleToUser(String username, String role) throws SQLException {
        String updateQuery = "UPDATE users SET role = ? WHERE username = ?";
        try (Connection conn = connectDatabase(); PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, role);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    // Remove role from a user
    public static void removeRoleFromUser(String username, String role) throws SQLException {
        String updateQuery = "UPDATE users SET role = NULL WHERE username = ? AND role = ?";
        try (Connection conn = connectDatabase(); PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, username);
            pstmt.setString(2, role);
            pstmt.executeUpdate();
        }
    }
    
 // Update user details (first name, middle name, last name, preferred name, email)
    public static boolean updateUserDetails(String username, String firstName, String middleName, String lastName, String preferredName, String email) throws SQLException {
        String updateQuery = "UPDATE users SET first_name = ?, middle_name = ?, last_name = ?, preferred_name = ?, email = ? WHERE username = ?";
        
        try (Connection conn = connectDatabase(); PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, middleName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, preferredName);
            pstmt.setString(5, email); 
            pstmt.setString(6, username);
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating user details: " + e.getMessage());
            return false;
        }
    }
}