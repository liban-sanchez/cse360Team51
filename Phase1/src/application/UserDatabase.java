package application;

import java.sql.*;


public class UserDatabase {
	private static final String DB_URL = "jdbc:h2:~/userDatabase";
	
	private static final String JDBC_URL = "jdbc:h2:~/test"; // You can change 'test' to your database name
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    // Initialize the H2 database connection
    public static Connection connectDatabase() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    // Create the users table
    public static void createTable() throws SQLException {
    	String userInfo = "CREATE TABLE IF NOT EXISTS users (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "username VARCHAR(255) UNIQUE, " +
                     "password VARCHAR(255), " +
                     "email VARCHAR(255), " +
                     "first_name VARCHAR(255), " +
                     "last_name VARCHAR(255), " +
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
    
 // Retrieve user data
    public static ResultSet getUserByUsername(String username) throws SQLException {
        String userInfo = "SELECT * FROM users WHERE username = ?";
        Connection conn = connectDatabase();
        PreparedStatement pstmt = conn.prepareStatement(userInfo);
        pstmt.setString(1, username);
        return pstmt.executeQuery();
    }
    
    // Use to clear data base 
    public static void clearUsers() {
        String deleteQuery = "DELETE FROM users";
        try (Connection conn = connectDatabase(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteQuery);
            System.out.println("All users deleted from the database.");
        } catch (SQLException e) {
            System.err.println("Error clearing users: " + e.getMessage());
        }
    }
    
    public static boolean isDatabaseEmpty() {
        String countQuery = "SELECT COUNT(*) FROM users";
        try (Connection conn = connectDatabase(); 
             PreparedStatement pstmt = conn.prepareStatement(countQuery)) {
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // Returns true if no users exist
            }
        } catch (SQLException e) {
            System.err.println("Error checking database: " + e.getMessage());
        }
        return false; // In case of an error, assume the database is not empty
    }
    
}
