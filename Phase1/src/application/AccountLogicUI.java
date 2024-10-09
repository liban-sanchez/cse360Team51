package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;

public class AccountLogicUI {
    //** User details labels and text fields for collecting user input such as name
    Label label_FirstName = new Label("First Name:");
    TextField text_FirstName = new TextField();
    Label label_MiddleName = new Label("Middle Name:");
    TextField text_MiddleName = new TextField();
    Label label_LastName = new Label("Last Name:");
    TextField text_LastName = new TextField();
    Label label_PreferredName = new Label("Preferred Name:");
    TextField text_PreferredName = new TextField();

    //** Role selection (Admin, Student, or both) using checkboxes
    Label label_Role = new Label("Roles:");
    CheckBox adminCheckBox = new CheckBox("Admin");
    CheckBox studentCheckBox = new CheckBox("Student");

    //** Topics and Proficiency Levels section with choice boxes to select proficiency levels
    Label label_Topic1 = new Label("Topic 1 Proficiency:");
    ChoiceBox<String> topic1ChoiceBox = new ChoiceBox<>();
    Label label_Topic2 = new Label("Topic 2 Proficiency:");
    ChoiceBox<String> topic2ChoiceBox = new ChoiceBox<>();

    //** Submit button for submitting the form and an error label for validation feedback
    Button submitButton = new Button("Submit");
    Label label_Error = new Label();

    //** Constructor to set up the UI elements and add them to the pane
    public AccountLogicUI(Pane accountPane, Main mainApp) {
        //** Create a GridPane for better layout and alignment of UI elements
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));  // Set padding around the grid
        grid.setVgap(8);  // Set vertical gap between rows
        grid.setHgap(10);  // Set horizontal gap between columns

        //** Set up the labels and text fields for user details
        GridPane.setConstraints(label_FirstName, 0, 0);
        GridPane.setConstraints(text_FirstName, 1, 0);
        GridPane.setConstraints(label_MiddleName, 0, 1);
        GridPane.setConstraints(text_MiddleName, 1, 1);
        GridPane.setConstraints(label_LastName, 0, 2);
        GridPane.setConstraints(text_LastName, 1, 2);
        GridPane.setConstraints(label_PreferredName, 0, 3);
        GridPane.setConstraints(text_PreferredName, 1, 3);

        //** Set up role checkboxes for role selection (Admin, Student, or both)
        GridPane.setConstraints(label_Role, 0, 4);
        GridPane.setConstraints(adminCheckBox, 1, 4);
        GridPane.setConstraints(studentCheckBox, 1, 5);

        //** Set up proficiency choice boxes for topics with default value "Intermediate"
        setupProficiencyChoiceBox(topic1ChoiceBox);
        setupProficiencyChoiceBox(topic2ChoiceBox);
        GridPane.setConstraints(label_Topic1, 0, 6);
        GridPane.setConstraints(topic1ChoiceBox, 1, 6);
        GridPane.setConstraints(label_Topic2, 0, 7);
        GridPane.setConstraints(topic2ChoiceBox, 1, 7);

        //** Set up the submit button and error label for form submission and validation
        GridPane.setConstraints(submitButton, 1, 8);
        GridPane.setConstraints(label_Error, 1, 9);
        label_Error.setTextFill(javafx.scene.paint.Color.RED);  // Set error label text color to red

        //** Add action event to the submit button to handle form submission
        submitButton.setOnAction(e -> handleSubmit(mainApp));

        //** Add all elements to the grid layout
        grid.getChildren().addAll(label_FirstName, text_FirstName, label_MiddleName, text_MiddleName,
                                  label_LastName, text_LastName, label_PreferredName, text_PreferredName,
                                  label_Role, adminCheckBox, studentCheckBox,
                                  label_Topic1, topic1ChoiceBox, label_Topic2, topic2ChoiceBox, submitButton, label_Error);

        //** Add the grid to the main pane
        accountPane.getChildren().add(grid);
    }

    //** Setup the proficiency choice box for each topic with options and default value
    private void setupProficiencyChoiceBox(ChoiceBox<String> choiceBox) {
        // Add proficiency levels as options in the choice box
        choiceBox.getItems().addAll("Beginner", "Intermediate", "Advanced", "Expert");
        choiceBox.setValue("Intermediate");  // Default to "Intermediate" proficiency level
    }

    //** Handle form submission and store user account information
    private void handleSubmit(Main mainApp) {
        //** Retrieve input values from the text fields and checkboxes
        String firstName = text_FirstName.getText();
        String middleName = text_MiddleName.getText();
        String lastName = text_LastName.getText();
        String preferredName = text_PreferredName.getText();

        boolean isAdmin = adminCheckBox.isSelected();  // Check if Admin role is selected
        boolean isStudent = studentCheckBox.isSelected();  // Check if Student role is selected

        //** Get the proficiency levels selected for each topic
        String topic1Proficiency = topic1ChoiceBox.getValue();
        String topic2Proficiency = topic2ChoiceBox.getValue();

        //** Validate that all required fields are filled and at least one role is selected
        if (firstName.isEmpty() || lastName.isEmpty() || (!isAdmin && !isStudent)) {
            label_Error.setText("Please fill in all required fields and select at least one role.");
            return;  // Stop form submission if validation fails
        }

        //** Store the user's account information (you can implement actual storage logic here)
        HashMap<String, String> topicProficiencies = new HashMap<>();
        topicProficiencies.put("Topic 1", topic1Proficiency);  // Store Topic 1 proficiency level
        topicProficiencies.put("Topic 2", topic2Proficiency);  // Store Topic 2 proficiency level

        //** Display collected information in the console (can be replaced with database logic)
        System.out.println("First Name: " + firstName);
        System.out.println("Middle Name: " + middleName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Preferred Name: " + preferredName);
        System.out.println("Roles: " + (isAdmin ? "Admin " : "") + (isStudent ? "Student " : ""));
        System.out.println("Topic 1 Proficiency: " + topic1Proficiency);
        System.out.println("Topic 2 Proficiency: " + topic2Proficiency);

        //** After submitting the form, redirect the user back to the login page
        mainApp.showLoginPage();
    }
}