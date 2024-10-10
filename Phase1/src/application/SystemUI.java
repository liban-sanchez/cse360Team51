package application;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

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

    public SystemUI(Pane systemPane, Main mainApp, OneTimeCode ot_code, String role) {

        // Layout setup
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        GridPane.setConstraints(logoutButton, 0, 0);

        // Add different buttons based on user role
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

            // Set button actions
            inviteButton.setOnAction(e -> handleInviteUser(ot_code));
            resetPassword.setOnAction(e -> handleResetPassword(ot_code));
            deleteUserButton.setOnAction(e -> handleDeleteUser());
            listUsersButton.setOnAction(e -> handleListUsers());
            addRoleButton.setOnAction(e -> handleAddRole());
            removeRoleButton.setOnAction(e -> handleRemoveRole());
        }

        // Logout button is common for all roles
        logoutButton.setOnAction(e -> mainApp.showLoginPage());

        grid.getChildren().add(logoutButton);
        systemPane.getChildren().add(grid);
    }

 // Invite User: Generates a one-time invite code based on selected roles (Student/Instructor)
    private void handleInviteUser(OneTimeCode ot_code) {
        // Create a dialog for admin input
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
                    // No role selected
                    Alert noRoleAlert = new Alert(Alert.AlertType.WARNING);
                    noRoleAlert.setTitle("No Role Selected");
                    noRoleAlert.setHeaderText(null);
                    noRoleAlert.setContentText("Please select at least one role before generating an invite code.");
                    noRoleAlert.showAndWait();
                }
            }
        });
    }

    // Reset User Password.
    private void handleResetPassword(OneTimeCode ot_code) {
        // Create a dialog for admin input
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Reset User Password");
        dialog.setHeaderText("Enter the username for password reset");

        // Create a text field for username input
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
                    String resetCode = ot_code.generateResetCode(); // Assuming this method exists

                    Alert codeAlert = new Alert(Alert.AlertType.INFORMATION);
                    codeAlert.setTitle("Reset Code Generated");
                    codeAlert.setHeaderText("Reset code for user:");
                    codeAlert.setContentText("Username: " + username + "\nReset Code: " + resetCode);
                    codeAlert.showAndWait();
                } else {
                    // No username entered
                    Alert noUsernameAlert = new Alert(Alert.AlertType.WARNING);
                    noUsernameAlert.setTitle("No Username Entered");
                    noUsernameAlert.setHeaderText(null);
                    noUsernameAlert.setContentText("Please enter a username before generating a reset code.");
                    noUsernameAlert.showAndWait();
                }
            }
        });
    }

    // Delete User: Prompts admin with confirmation before deleting a user.
    private void handleDeleteUser() {
    	// Create a dialog for admin input
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Delete User");
        dialog.setHeaderText("Enter the username and email of the user to delete");

        // Create text fields for username and email input
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

 // List Users: Displays all user accounts with usernames and roles.
    private void handleListUsers() {
        try {
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

 // Add Role: Allows admin to add a role to a user.
    private void handleAddRole() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Role");
        dialog.setHeaderText("Enter the username and role to add");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        TextField roleField = new TextField();
        roleField.setPromptText("Role (e.g., Student, Instructor)");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Role:"), 0, 1);
        grid.add(roleField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String username = usernameField.getText().trim();
                String role = roleField.getText().trim();

                if (!username.isEmpty() && !role.isEmpty()) {
                    try {
                        UserDatabase.addRoleToUser(username, role);
                        label_CommandStatus.setText("Role added successfully.");
                    } catch (SQLException e) {
                        label_CommandStatus.setText("Error adding role: " + e.getMessage());
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

 // Remove Role: Allows admin to remove a role from a user.
    private void handleRemoveRole() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Remove Role");
        dialog.setHeaderText("Enter the username and role to remove");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        TextField roleField = new TextField();
        roleField.setPromptText("Role");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Role:"), 0, 1);
        grid.add(roleField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

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