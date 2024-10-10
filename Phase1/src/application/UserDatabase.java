package application;

import java.sql.*;

public class UserDatabase {
	// URL to connect to the H2 database
    private static final String DB_URL = "jdbc:h2:~/userDatabase";
    // Username for database access
    private static final String USER = "sa";
    // Password for database access
    private static final String PASSWORD = "";

    /**
     * Initializes the H2 database connection.
     *
     * @return A Connection object to the database.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection connectDatabase() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
    
    /**
     * Creates the users table in the database if it does not exist.
     *
     * The table includes columns for user attributes such as username, password, email,
     * first name, last name, middle name, preferred name, and role.
     *
     * @throws SQLException If a database access error occurs.
     */
    public static void createTable() throws SQLException {
    	dropTable();
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
        	// Execute the create table
            stmt.execute(userInfo);
        }
    }
    
    /**
     * Inserts a new user into the database.
     *
     * @param username The username of the new user.
     * @param password The password for the new user.
     * @param email The email address of the new user.
     * @param firstName The first name of the new user.
     * @param lastName The last name of the new user.
     * @param role The role of the new user.
     * @throws SQLException If a database access error occurs.
     */
    public static void insertUser(String username, String password, String email, 
                                  String firstName, String lastName, String role) throws SQLException {
    	// query to insert a new user
        String userInfo = "INSERT INTO users (username, password, email, first_name, last_name, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connectDatabase(); PreparedStatement pstmt = conn.prepareStatement(userInfo)) {
        	// Set parameters for the prepared statement
            pstmt.setString(1, username);
            pstmt.setString(2, password); 
            pstmt.setString(3, email);
            pstmt.setString(4, firstName);
            pstmt.setString(5, lastName);
            pstmt.setString(6, role);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Drops the users table from the database if it exists. Isn't explicitly used 
     *
     * @throws SQLException If a database access error occurs.
     */
    public static void dropTable() throws SQLException {
        String dropQuery = "DROP TABLE IF EXISTS users";
        try (Connection conn = connectDatabase(); Statement stmt = conn.createStatement()) {
            stmt.execute(dropQuery);
        }
    }
    
    /**
     * Clears all users from the database. Isn't explicitly used
     *
     * This method deletes all records from the users table.
     */
    public static void clearUsers() {
        String deleteQuery = "DELETE FROM users";
        try (Connection conn = connectDatabase(); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteQuery);
        } catch (SQLException e) {
            System.err.println("Error clearing users: " + e.getMessage());
        }
    }
    
    /**
     * Deletes a user from the database based on username and email.
     *
     * @param username The username of the user to be deleted.
     * @param email The email of the user to be deleted.
     * @return True if the user was deleted; false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public static boolean deleteUser(String username, String email) throws SQLException {
    	// SQL query to delete a specific user
        String deleteQuery = "DELETE FROM users WHERE username = ? AND email = ?";
        try (Connection conn = connectDatabase(); PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
        	// Set username parameter
        	pstmt.setString(1, username);
        	// Set email parameter
        	pstmt.setString(2, email);
        	//Execute the delete statement
            int rowsDeleted = pstmt.executeUpdate();
            // Return true if a user was deleted
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Retrieves user data based on the provided username.
     *
     * @param username The username of the user to retrieve.
     * @return A ResultSet containing the user's data.
     * @throws SQLException If a database access error occurs.
     */
    public static ResultSet getUserByUsername(String username) throws SQLException {
    	// query to get a user by username
        String userInfo = "SELECT * FROM users WHERE username = ?";
        // Establish database connection	
        Connection conn = connectDatabase();
        // Prepare the statement
        PreparedStatement pstmt = conn.prepareStatement(userInfo);
        // Set username parameter
        pstmt.setString(1, username);
        // Execute query and return ResultSet
        return pstmt.executeQuery();
    }
    
    /**
     * Checks if the users table in the database is empty.
     *
     * @return True if the database is empty; false otherwise.
     */
    public static boolean isDatabaseEmpty() {
    	// query to count users
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
    
    /**
     * Retrieves all users from the database.
     *
     * @return A ResultSet containing the usernames and roles of all users.
     * @throws SQLException If a database access error occurs.
     */
    public static ResultSet getAllUsers() throws SQLException {
        String query = "SELECT username, role FROM users";
        Connection conn = connectDatabase();
        PreparedStatement pstmt = conn.prepareStatement(query);
        return pstmt.executeQuery();
    }

    /**
     * Adds a role to a user based on their username.
     *
     * @param username The username of the user to update.
     * @param role The new role to assign to the user.
     * @throws SQLException If a database access error occurs.
     */
    public static void addRoleToUser(String username, String role) throws SQLException {
    	// SQL query to update user role
        String updateQuery = "UPDATE users SET role = ? WHERE username = ?";
        try (Connection conn = connectDatabase(); PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, role);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        }
    }

    /**
     * Removes a role from a user based on their username.
     *
     * @param username The username of the user to update.
     * @param role The role to remove from the user.
     * @throws SQLException If a database access error occurs.
     */
    public static void removeRoleFromUser(String username, String role) throws SQLException {
    	// SQL query to remove user role
        String updateQuery = "UPDATE users SET role = NULL WHERE username = ? AND role = ?";
        try (Connection conn = connectDatabase(); PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, username);
            pstmt.setString(2, role);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Updates user details such as first name, middle name, last name, preferred name, and email.
     *
     * @param username The username of the user
     * @param firstName The new first name of the user.
     * @param middleName The new middle name of the user.
     * @param lastName The new last name of the user.
     * @param preferredName The new preferred name of the user.
     * @param email The new email address of the user.
     * @return True if the user details were updated; false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public static boolean updateUserDetails(String username, String firstName, String middleName, String lastName, String preferredName, String email) throws SQLException {
    	// SQL query to update user details
    	String updateQuery = "UPDATE users SET first_name = ?, middle_name = ?, last_name = ?, preferred_name = ?, email = ? WHERE username = ?";
        
        try (Connection conn = connectDatabase(); PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
        	// Set parameters for the prepared statement
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