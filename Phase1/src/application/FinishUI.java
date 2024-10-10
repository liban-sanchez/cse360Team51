package application;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class FinishUI {
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

    public FinishUI(Pane accountPane, Main mainApp, String username) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

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
        
        GridPane.setConstraints(submitButton, 1, 5);
        GridPane.setConstraints(label_Error, 1, 6);
        label_Error.setTextFill(javafx.scene.paint.Color.RED);

        submitButton.setOnAction(e -> handleSubmit(mainApp, username));

        grid.getChildren().addAll(label_FirstName, text_FirstName, label_MiddleName, text_MiddleName,
                                  label_LastName, text_LastName, label_PreferredName, text_PreferredName,
                                  label_Email, text_Email, 
                                  submitButton, label_Error);

        accountPane.getChildren().add(grid);
    }

    private void handleSubmit(Main mainApp, String username) {
        String firstName = text_FirstName.getText();
        String middleName = text_MiddleName.getText();
        String lastName = text_LastName.getText();
        String preferredName = text_PreferredName.getText();
        String email = text_Email.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            label_Error.setText("Please fill in all required fields.");
            return;
        }

        try {
            boolean updateSuccess = UserDatabase.updateUserDetails(username, firstName, middleName, lastName, preferredName, email);

            if (updateSuccess) {
                System.out.println("User information updated successfully.");
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