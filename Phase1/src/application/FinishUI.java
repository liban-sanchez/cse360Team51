package application;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class FinishUI {
	// UI components for user information
    Label label_FirstName = new Label("First Name:");
    TextField text_FirstName = new TextField();
    Label label_MiddleName = new Label("Middle Name:");
    TextField text_MiddleName = new TextField();
    Label label_LastName = new Label("Last Name:");
    TextField text_LastName = new TextField();
    Label label_PreferredName = new Label("Preferred Name:");
    TextField text_PreferredName = new TextField();
    
    Label label_Email = new Label("Email:"); 
    TextField text_Email = new TextField();  
    
    Button submitButton = new Button("Submit");
    Label label_Error = new Label();

    /**
     * Constructor to initialize the FinishUI layout and components.
     *
     * @param accountPane The pane where the UI components will be added.
     * @param mainApp The main application instance for navigation.
     * @param username The username of the user whose details are being updated.
     */
    public FinishUI(Pane accountPane, Main mainApp, String username) {
    	// Creating a GridPane for organizing the layout
        GridPane grid = new GridPane();
        // Setting padding for the grid
        grid.setPadding(new Insets(10, 10, 10, 10));
        // Vertical gap between rows
        grid.setVgap(8);
        // Horizontal gap between columns
        grid.setHgap(10);
        
        // Setting positions of labels and text fields in the grid
        GridPane.setConstraints(label_FirstName, 0, 0);
        GridPane.setConstraints(text_FirstName, 1, 0);
        GridPane.setConstraints(label_MiddleName, 0, 1);
        GridPane.setConstraints(text_MiddleName, 1, 1);
        GridPane.setConstraints(label_LastName, 0, 2);
        GridPane.setConstraints(text_LastName, 1, 2);
        GridPane.setConstraints(label_PreferredName, 0, 3);
        GridPane.setConstraints(text_PreferredName, 1, 3);
   
        GridPane.setConstraints(label_Email, 0, 4);
        GridPane.setConstraints(text_Email, 1, 4);
        
        // Position for the submit button
        GridPane.setConstraints(submitButton, 1, 5);
        
        // Position and color for the error label
        GridPane.setConstraints(label_Error, 1, 6);
        label_Error.setTextFill(javafx.scene.paint.Color.RED);
        
        // Action event for the submit button
        submitButton.setOnAction(e -> handleSubmit(mainApp, username));
        
        // Adding all components to the grid
        grid.getChildren().addAll(label_FirstName, text_FirstName, label_MiddleName, text_MiddleName,
                                  label_LastName, text_LastName, label_PreferredName, text_PreferredName,
                                  label_Email, text_Email, 
                                  submitButton, label_Error);
        
        // Adding the grid to the provided pane
        accountPane.getChildren().add(grid);
    }
    
    /**
     * Handles the submit action when the user clicks the submit button.
     *
     * @param mainApp The main application instance for navigation.
     * @param username The username of the user whose details are being updated.
     */
    private void handleSubmit(Main mainApp, String username) {
    	// Retrieving input from text fields
        String firstName = text_FirstName.getText();
        String middleName = text_MiddleName.getText();
        String lastName = text_LastName.getText();
        String preferredName = text_PreferredName.getText();
        String email = text_Email.getText();
        
        // Validating required fields
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            label_Error.setText("Please fill in all required fields.");
            return;
        }

        try {
        	// Attempt to update user details in the database
            boolean updateSuccess = UserDatabase.updateUserDetails(username, firstName, middleName, lastName, preferredName, email);
            
            // Check if the update was successful
            if (updateSuccess) {
                System.out.println("User information updated successfully.");
                // Navigate back to the login page
                mainApp.showLoginPage();
            } else {
                label_Error.setText("Failed to update user information.");
            }
        } catch (Exception e) {
            label_Error.setText("An error occurred while updating user information.");
            e.printStackTrace();
        }
    }
}