package application;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Represents the system interface for administrative actions within the application.
 * Provides functionalities for user management, including inviting users,
 * resetting passwords, deleting users, listing accounts, and managing user roles.
 */
public class SystemUI {

    // Common UI components
    Button logoutButton = new Button("Log Out");

    // Admin-specific UI components
    Button inviteButton = new Button("Generate Invite Code");
    Button resetPassword = new Button("Generate Reset Code");
    Button deleteUserButton = new Button("Delete User");
    Button listUsersButton = new Button("List User Accounts");
    Button addRoleButton = new Button("Add Role");
    Button removeRoleButton = new Button("Remove Role");
    Label label_CommandStatus = new Label();

    /**
     * Constructs the SystemUI and initializes the layout and functionalities
     * based on the user's role.
     *
     * @param systemPane  The pane where the UI elements will be displayed.
     * @param mainApp    The main application to navigate between pages.
     * @param ot_code    The instance of OneTimeCode used for generating codes.
     * @param role       The role of the user (Admin, Instructor, etc.).
     */
    public SystemUI(Pane systemPane, Main mainApp, OneTimeCode ot_code, String role) {

    	// Layout setup using a GridPane for structured arrangement
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        
        // Add logout button to the grid layout
        GridPane.setConstraints(logoutButton, 0, 0);

        // Different buttons based on user role
        if (role.equals("Admin")) {
            // Admin-only buttons
            GridPane.setConstraints(inviteButton, 0, 1);
            GridPane.setConstraints(resetPassword, 0, 2);
            GridPane.setConstraints(deleteUserButton, 0, 3);
            GridPane.setConstraints(listUsersButton, 0, 4);
            GridPane.setConstraints(addRoleButton, 0, 5);
            GridPane.setConstraints(removeRoleButton, 0, 6);
            GridPane.setConstraints(label_CommandStatus, 0, 7);

            grid.getChildren().addAll(
                inviteButton, resetPassword, deleteUserButton, 
                listUsersButton, addRoleButton, removeRoleButton, label_CommandStatus
            );

            // Assign actions to Admin-specific buttons
            inviteButton.setOnAction(e -> handleInviteUser(ot_code));
            resetPassword.setOnAction(e -> handleResetPassword(ot_code));
            deleteUserButton.setOnAction(e -> handleDeleteUser());
            listUsersButton.setOnAction(e -> handleListUsers());
            addRoleButton.setOnAction(e -> handleAddRole());
            removeRoleButton.setOnAction(e -> handleRemoveRole());
        }

        // Logout button is common for all roles
        logoutButton.setOnAction(e -> mainApp.showLoginPage());

        // Add all components to the system pane
        grid.getChildren().add(logoutButton);
        systemPane.getChildren().add(grid);
    }

    /**
     * Generates a one-time invite code for a new user based on selected roles
     * (Student/Instructor). Prompts the admin to select a role before generating.
     *
     * @param ot_code The instance of OneTimeCode used for generating invite codes.
     */
    private void handleInviteUser(OneTimeCode ot_code) {
    	// Dialog for admin input to select user role
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Invite User");
        dialog.setHeaderText("Select a role");

        // Create checkboxes for role selection
        CheckBox studentCheckBox = new CheckBox("Student");
        CheckBox instructorCheckBox = new CheckBox("Instructor");
        
        // Layout for checkboxes
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Select role:"), 0, 0);
        grid.add(studentCheckBox, 0, 1);
        grid.add(instructorCheckBox, 1, 1);
        
        // Set the content of the dialog
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Show the dialog and wait for confirmation
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String newRole = "";
                if (studentCheckBox.isSelected()) {
                    newRole = "Student";
                } else if (instructorCheckBox.isSelected()) {
                    newRole = "Instructor";
                }
                
                if (!newRole.isEmpty()) {
                    // Generate invite code using InviteCodeGenerator (adjust based on your logic)
                	String inviteCode = ot_code.generateInviteCode(newRole);
                    
                    // Display the generated invite code
                    Alert codeAlert = new Alert(Alert.AlertType.INFORMATION);
                    codeAlert.setTitle("Invite Code Generated");
                    codeAlert.setHeaderText("Invite code for the selected role:");
                    codeAlert.setContentText("Role: " + newRole + "\nInvite Code: " + inviteCode);
                    codeAlert.showAndWait();
                } else {
                	// Warning alert for no role selected
                    Alert noRoleAlert = new Alert(Alert.AlertType.WARNING);
                    noRoleAlert.setTitle("No Role Selected");
                    noRoleAlert.setHeaderText(null);
                    noRoleAlert.setContentText("Please select at least one role before generating an invite code.");
                    noRoleAlert.showAndWait();
                }
            }
        });
    }

    /**
     * Resets the password for a specified user. Prompts the admin for the username,
     * generates a reset code, and displays it.
     *
     * @param ot_code The instance of OneTimeCode used for generating reset codes.
     */
    private void handleResetPassword(OneTimeCode ot_code) {
    	// Dialog for admin input to reset user password
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Reset User Password");
        dialog.setHeaderText("Enter the username for password reset");

        // Text field for username input
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        // Layout for dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        
        // Set the content of the dialog
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        // Show the dialog and wait for confirmation
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String username = usernameField.getText().trim();
                if (!username.isEmpty()) {
                    // Generate reset code using OneTimeCode or your logic
                    String resetCode = ot_code.generateResetCode(); 
                    
                    // Display the reset code information
                    Alert codeAlert = new Alert(Alert.AlertType.INFORMATION);
                    codeAlert.setTitle("Reset Code Generated");
                    codeAlert.setHeaderText("Reset code for user:");
                    codeAlert.setContentText("Username: " + username + "\nReset Code: " + resetCode);
                    codeAlert.showAndWait();
                } else {
                	// Warning alert for no username entered
                    Alert noUsernameAlert = new Alert(Alert.AlertType.WARNING);
                    noUsernameAlert.setTitle("No Username Entered");
                    noUsernameAlert.setHeaderText(null);
                    noUsernameAlert.setContentText("Please enter a username before generating a reset code.");
                    noUsernameAlert.showAndWait();
                }
            }
        });
    }

    /**
     * Deletes a user account from the database. Prompts the admin for the username
     * and email, verifies input, and executes the deletion.
     */
    private void handleDeleteUser() {
    	// Dialog for admin input to delete user
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete User");
        dialog.setHeaderText("Enter the username and email of the user to delete");

        // Text fields for username and email input
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        // Layout for dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);

        // Set the content of the dialog
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog and wait for confirmation
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();

                if (!username.isEmpty() && !email.isEmpty()) {
                    try {
                        // Call the deleteUser method from UserDatabase
                        boolean isDeleted = UserDatabase.deleteUser(username, email);
                        if (isDeleted) {
                            label_CommandStatus.setText("User deleted successfully.");
                        } else {
                            label_CommandStatus.setText("User not found. Please check the username and email.");
                        }
                    } catch (SQLException e) {
                        label_CommandStatus.setText("Error deleting user: " + e.getMessage());
                    }
                } else {
                    // No username or email entered
                    Alert noInputAlert = new Alert(Alert.AlertType.WARNING);
                    noInputAlert.setTitle("Input Required");
                    noInputAlert.setHeaderText(null);
                    noInputAlert.setContentText("Please enter both username and email before deleting a user.");
                    noInputAlert.showAndWait();
                }
            }
        });
    }

    /**
     * Lists all user accounts in the system, displaying their usernames and roles.
     * Retrieves the user data from the database and formats it for display.
     * Shows an alert dialog with the list of user accounts or an error message if retrieval fails.
     */
    private void handleListUsers() {
        try {
        	// Fetch all users
            ResultSet resultSet = UserDatabase.getAllUsers(); // Fetch all users
            StringBuilder userList = new StringBuilder("User Accounts:\n");
            
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String role = resultSet.getString("role");
                userList.append("Username: ").append(username).append(", Role: ").append(role).append("\n");
            }
            
            // Display the user accounts in an alert
            Alert listAlert = new Alert(Alert.AlertType.INFORMATION);
            listAlert.setTitle("User Accounts");
            listAlert.setHeaderText(null);
            listAlert.setContentText(userList.toString());
            listAlert.showAndWait();
        } catch (SQLException e) {
            label_CommandStatus.setText("Error retrieving users: " + e.getMessage());
        }
    }

    /**
     * Allows the admin to add a role to an existing user. 
     * Prompts the admin for the username and the new role, 
     * verifies input, and updates the user's roles in the database.
     */
    private void handleAddRole() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Role");
        dialog.setHeaderText("Enter the username and role to add");
        
        // Input fields for the username and role
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        TextField roleField = new TextField();
        roleField.setPromptText("Role (e.g., Student, Instructor)");

        // Create a grid layout to arrange the input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Role:"), 0, 1);
        grid.add(roleField, 1, 1);

        // Set the content of the dialog to the grid
        dialog.getDialogPane().setContent(grid);
        // Add buttons for OK and Cancel
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog and handle the response
        dialog.showAndWait().ifPresent(response -> {
        	// Check if the OK button was pressed
            if (response == ButtonType.OK) {
            	// Get trimmed username input
                String username = usernameField.getText().trim();
                // Get trimmed role input
                String role = roleField.getText().trim();

                // Validate input fields are not empty
                if (!username.isEmpty() && !role.isEmpty()) {
                    try {
                    	// Add role to the user in the database
                        UserDatabase.addRoleToUser(username, role);
                        label_CommandStatus.setText("Role added successfully.");
                    } catch (SQLException e) {
                        label_CommandStatus.setText("Error adding role: " + e.getMessage());
                    }
                } else {
                	// Alert user if inputs are empty
                    Alert noInputAlert = new Alert(Alert.AlertType.WARNING);
                    noInputAlert.setTitle("Input Required");
                    noInputAlert.setHeaderText(null);
                    noInputAlert.setContentText("Please enter both username and role.");
                    noInputAlert.showAndWait();
                }
            }
        });
    }

    /**
     * Allows the admin to remove a role from a user. 
     * Prompts the admin for the username and the role to be removed, 
     * verifies input, and updates the user's roles in the database.
     */
    private void handleRemoveRole() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Remove Role");
        dialog.setHeaderText("Enter the username and role to remove");

        // Input fields for the username and role
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        TextField roleField = new TextField();
        roleField.setPromptText("Role");

        // Create a grid layout to arrange the input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Role:"), 0, 1);
        grid.add(roleField, 1, 1);

        // Set the content of the dialog to the grid
        dialog.getDialogPane().setContent(grid);
        // Add buttons for OK and Cancel
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show the dialog and handle the response
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String username = usernameField.getText().trim();
                String role = roleField.getText().trim();

                if (!username.isEmpty() && !role.isEmpty()) {
                    try {
                        UserDatabase.removeRoleFromUser(username, role);
                        label_CommandStatus.setText("Role removed successfully.");
                    } catch (SQLException e) {
                        label_CommandStatus.setText("Error removing role: " + e.getMessage());
                    }
                } else {
                    Alert noInputAlert = new Alert(Alert.AlertType.WARNING);
                    noInputAlert.setTitle("Input Required");
                    noInputAlert.setHeaderText(null);
                    noInputAlert.setContentText("Please enter both username and role.");
                    noInputAlert.showAndWait();
                }
            }
        });
    }
}