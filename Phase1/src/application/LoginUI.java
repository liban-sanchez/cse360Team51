package application;

// JavaFX imports needed to support the Graphical User Interface
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUI {

    /**********************************************************************************************

    Attributes

    **********************************************************************************************/

    // These are the application values required by the Graphical User Interface
    Label label_ApplicationTitle = new Label("Help System Login");
    Label label_Username = new Label("User Name:");
    TextField text_Username = new TextField();
    Label label_Password = new Label("Password:");
    TextField text_Password = new TextField();
    Button loginButton = new Button("Login");
    Label label_InviteCode = new Label("Invite Code:");
    TextField text_InviteCode = new TextField();
    Button createAccButton = new Button("Create Account");
    Label label_Error = new Label();
    Label label_InviteError = new Label();

    /**********************************************************************************************

    Constructors

    **********************************************************************************************/

    /**********
     * This method initializes all of the elements of the graphical user interface. These assignments
     * determine the UI and event handlers for each GUI object.
     *
     * @param loginPane The Pane to which UI elements are added.
     * @param mainApp The main application instance to navigate between scenes.
     * @param ot_code The OneTimeCode instance for managing invite codes.
     */
    public LoginUI(Pane loginPane, Main mainApp, OneTimeCode ot_code) {

    	// Label with the "Login" centered at the top of the pane
        setupLabelUI(label_ApplicationTitle, "Arial", 24, Main.WINDOW_WIDTH - 5, Pos.CENTER, 0, 50);

        // Label the User name input field with a title just above it, left aligned
        setupLabelUI(label_Username, "Arial", 14, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 125, 100);
        setupTextUI(text_Username, "Arial", 18, Main.WINDOW_WIDTH - 250, Pos.BASELINE_LEFT, 125, 120, true);
        text_Username.textProperty().addListener((observable, oldValue, newValue) 
				-> {label_Error.setText(""); label_InviteError.setText("");});

        // Label the Password input field with a title just above it, left aligned
        setupLabelUI(label_Password, "Arial", 14, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 125, 160);
        setupTextUI(text_Password, "Arial", 18, Main.WINDOW_WIDTH - 250, Pos.BASELINE_LEFT, 125, 180, true);
        text_Password.textProperty().addListener((observable, oldValue, newValue) 
				-> {label_Error.setText(""); label_InviteError.setText("");});

        // Login button
        setupButtonUI(loginButton, "Arial", 14, 75, Pos.CENTER, (Main.WINDOW_WIDTH - 75) / 2, 220);
        loginButton.setOnAction(e -> handleLogin(mainApp, ot_code));
        
        // Invalid User/pass
        setupLabelUI(label_Error, "Arial", 14, Main.WINDOW_WIDTH - 10, Pos.CENTER, 0, 250);
        
        // Invite Code label and text field
        setupLabelUI(label_InviteCode, "Arial", 14, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 125, 280);
        setupTextUI(text_InviteCode, "Arial", 18, Main.WINDOW_WIDTH - 250, Pos.BASELINE_LEFT, 125, 300, true);
        text_InviteCode.textProperty().addListener((observable, oldValue, newValue) 
				-> {label_Error.setText(""); label_InviteError.setText("");});

        // Create Account button
        setupButtonUI(createAccButton, "Arial", 14, 125, Pos.CENTER, (Main.WINDOW_WIDTH - 125) / 2, 340);
        createAccButton.setOnAction(e -> handleCreateAcc(mainApp, ot_code));
        label_Error.setTextFill(Color.RED);

        // Invalid invite code
        setupLabelUI(label_InviteError, "Arial", 14, Main.WINDOW_WIDTH - 10, Pos.CENTER, 0, 370);
        label_InviteError.setTextFill(Color.RED);

        // Add all UI elements to the pane
        loginPane.getChildren().addAll(label_ApplicationTitle, label_Username, text_Username,
                label_Password, text_Password, loginButton, label_InviteCode, text_InviteCode, createAccButton,
                label_Error, label_InviteError);
    
    }

    /**********
     * Private local method to initialize the standard fields for a label
     */
    private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }
    
    /**********
     * Private local method to initialize the standard fields for a text field
     */
    private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e) {
        t.setFont(Font.font(ff, f));
        t.setMinWidth(w);
        t.setMaxWidth(w);
        t.setAlignment(p);
        t.setLayoutX(x);
        t.setLayoutY(y);
        t.setEditable(e);
    }

    /**********
     * Private local method to initialize the standard fields for a button
     */
    private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) {
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }

    /**********
     * Method to handle login logic when the login button is clicked.
     *
     * @param mainApp The main application instance for navigation.
     * @param ot_code The OneTimeCode instance for invite code validation.
     */
    private void handleLogin(Main mainApp, OneTimeCode ot_code) {
    	// Get username, password, and resetCode from text fields
    	String username = text_Username.getText();
        String password = text_Password.getText();
        String resetCode = text_InviteCode.getText();
        
        try {
            // Retrieve user data by username
            ResultSet rs = UserDatabase.getUserByUsername(username);

            // Check if the username exists
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String email = rs.getString("email");
                
                // Check the password is correct
                if (storedPassword.equals(password)) {
                	// Check if user has not set their email, meaning they must finish user setUp
                	if (email == null || email.isEmpty()) {
                         mainApp.showFinishPage(username);
                    }
                	// User has already set their email, proceed with login to system
                	else {
                		// The Role determines the system display and functionality
                        mainApp.showSystemUI(username, rs.getString("role"));
                    }
                } 
                // Check if password was a password reset
                else if (ot_code.validateResetCode(resetCode)) {
                    // If the invite code is valid, go to update password page
                	ot_code.useResetCode(resetCode);	// Remove reset code
                    //mainApp.showUpdatePasswordUI();
                }
                else {
                    // Password does not match
                    label_Error.setText("*Invalid Username/Password");
                }
            } 
            else {
                // Username not found
                label_Error.setText("*Invalid Username/Password");
            }
        } catch (SQLException e) {
            label_Error.setText("*Database error: " + e.getMessage());
        }
        
    }
    
    /**********
     * Method to handle account creation logic when the create account button is clicked.
     *
     * @param mainApp The main application instance for navigation.
     * @param ot_code The OneTimeCode instance for invite code management.
     */
    private void handleCreateAcc(Main mainApp, OneTimeCode ot_code) {
    	String inviteCode = text_InviteCode.getText();
    	
    	// Check if the invite code is valid
        if (ot_code.validateInviteCode(inviteCode)) {
        	// Assign role from invite code
        	String inviteRole = ot_code.getInviteCodeRole(inviteCode);	
        	ot_code.useInviteCode(inviteCode);
        	 // Proceed to create account page
            mainApp.showCreateAccountPage(inviteRole);
        } else {
        	// Invalid Invite Code
            label_InviteError.setText("*Invalid Invite Code.");
        }
    }
}