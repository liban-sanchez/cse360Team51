package application;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.SQLException;

public class CreateAccUI {

  /**********************************************************************************************

  Attributes

  **********************************************************************************************/

  // These are the application values required by the Graphical User Interface
  Label label_ApplicationTitle = new Label("Create Account");
  Label label_Username = new Label("User Name:");
  TextField text_Username = new TextField();
  Label label_Password = new Label("Password:");
  TextField text_Password = new TextField();
  Label label_Confirm = new Label("Confirm Password:");
  TextField text_Confirm = new TextField();
  Button createAccButton = new Button("Create Account");
  Button returnButton = new Button("Return to Login");
  Label label_Requirements = new Label("A valid password must satisfy the following requirements:");
  Label label_UpperCase = new Label("At least one upper case letter - Not yet satisfied");
  Label label_LowerCase = new Label("At least one lower case letter - Not yet satisfied");
  Label label_NumericDigit = new Label("At least one numeric digit - Not yet satisfied");
  Label label_SpecialChar = new Label("At least one special character - Not yet satisfied");
  Label label_LongEnough = new Label("At least eight characters - Not yet satisfied");
  Label label_Match = new Label("Passwords match - Not yet satisfied");
  Label label_noAdmin = new Label("");
  Label label_Error = new Label("");
  
  
  /**********************************************************************************************

  Constructors

  **********************************************************************************************/

  /**********
   * This method initializes all of the elements of the graphical user interface. These assignments
   * determine the location, size, font, color, and change and event handlers for each GUI object.
   */
  public CreateAccUI(Pane createAccPane, Main mainApp, String role) {

      // Label with the "Create Account" centered at the top of the pane
      setupLabelUI(label_ApplicationTitle, "Arial", 24, Main.WINDOW_WIDTH - 5, Pos.CENTER, 0, 50);

      // Label the User name input field with a title just above it, left aligned
      setupLabelUI(label_Username, "Arial", 14, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 125, 100);
      setupTextUI(text_Username, "Arial", 18, Main.WINDOW_WIDTH - 250, Pos.BASELINE_LEFT, 125, 120, true);
      text_Username.textProperty().addListener((observable, oldValue, newValue) 
				-> {label_noAdmin.setText(""); label_Error.setText("");});

      // Label the Password input field with a title just above it, left aligned
      setupLabelUI(label_Password, "Arial", 14, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 125, 160);
      setupTextUI(text_Password, "Arial", 18, Main.WINDOW_WIDTH - 250, Pos.BASELINE_LEFT, 125, 180, true);
      text_Password.textProperty().addListener((observable, oldValue, newValue) 
				-> {setPassword(); label_noAdmin.setText(""); label_Error.setText("");});
      
      // Label the Confirm Password input field with a title just above it, left aligned
      setupLabelUI(label_Confirm, "Arial", 14, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 125, 220);
      setupTextUI(text_Confirm, "Arial", 18, Main.WINDOW_WIDTH - 250, Pos.BASELINE_LEFT, 125, 240, true);
      text_Confirm.textProperty().addListener((observable, oldValue, newValue) 
				-> {checkMatch(); label_noAdmin.setText(""); label_Error.setText("");});
      
      // Position the requirements assessment display for each required aspect
      setupLabelUI(label_Requirements, "Arial", 8, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 140, 320);
      
      // Upper case
      setupLabelUI(label_UpperCase, "Arial", 8, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 140, 330);
      label_UpperCase.setTextFill(Color.RED);
      
      // Lower case
      setupLabelUI(label_LowerCase, "Arial", 8, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 140, 340);
      label_LowerCase.setTextFill(Color.RED);
      
      // Numeric
      setupLabelUI(label_NumericDigit, "Arial", 8, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 140, 350);
      label_NumericDigit.setTextFill(Color.RED);
      
      // Special
      setupLabelUI(label_SpecialChar, "Arial", 8, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 140, 360);
      label_SpecialChar.setTextFill(Color.RED);
      
      // Length
      setupLabelUI(label_LongEnough, "Arial", 8, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 140, 370);
      label_LongEnough.setTextFill(Color.RED);
      
      // Match
      setupLabelUI(label_Match, "Arial", 8, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 140, 380);
      label_Match.setTextFill(Color.RED);
      
      // Missing Admin Warning
      setupLabelUI(label_noAdmin, "Arial", 8, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 90, 402);
      label_noAdmin.setTextFill(Color.RED);
      
      // Error label
      setupLabelUI(label_Error, "Arial", 8, Main.WINDOW_WIDTH - 10, Pos.BASELINE_LEFT, 90, 420); 
      label_Error.setTextFill(Color.RED);
      
      // Create Account button
      setupButtonUI(createAccButton, "Arial", 14, 100, Pos.CENTER, 190, 280);
      createAccButton.setOnAction(e -> handleCreateAcc(mainApp, role)); // Switch to login page
      
      // Return to Login button
      setupButtonUI(returnButton, "Arial", 8, 30, Pos.CENTER, 20, 400);
      returnButton.setOnAction(e -> handleReturnToLogin(mainApp)); // Switch to login page

      // Add all UI elements to the pane
      createAccPane.getChildren().addAll(label_ApplicationTitle, label_Username, text_Username,
              label_Password, text_Password, label_Confirm, text_Confirm, returnButton, createAccButton,
              label_Requirements, label_UpperCase, label_LowerCase, label_NumericDigit,
              label_SpecialChar, label_LongEnough, label_Match, label_noAdmin);
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
	 * Reset all the relevant flags and error messages whenever the user changes the input
	 */
	private void setPassword() {
		displayAssessments();				// Reset the flags for all of the assessment criteria
		checkMatch();
		performEvaluation();			// Perform the evaluation to set all the assessment flags
	}
	
  /**********
	 * Check PassWord Matching
	 */
  	private void checkMatch() {
  		if (text_Password.getText().equals(text_Confirm.getText()) && text_Password.getText().length() != 0) {
    	    label_Match.setText("Passwords match");
    	    label_Match.setTextFill(Color.GREEN);
  		}
  		else {
  			label_Match.setText("Passwords match - Not yet satisfied");
  			label_Match.setTextFill(Color.RED);
  		}
  	}

  /**********
   * Evaluate the input whenever the user changes it and update the GUI and the console so the
   * user knows what is going on
   */
  private void performEvaluation() {
      // Get the user input string from the GUI
      String inputText = text_Password.getText();

      // If the input is empty, set that flag and stop
      if (!(inputText.isEmpty())) {
          // There is input to process.  Call the evaluatePassword method to assess each of the
          // remaining criteria 
          String errMessage = PasswordChecker.checkPassword(inputText);
          updateFlags();				// Check for each criteria and set the GUI for that element
			// to green with the criteria satisfied
          if (errMessage != "") {
              // If the returned string from evaluatePassword is not empty, there are errors!
              System.out.println(errMessage); // Display the error message on the console
          }
          // If no error message was found, check to see if all the criteria has been met
          else if (PasswordChecker.includesUppercase && PasswordChecker.includesLowercase &&
        		  PasswordChecker.includesNum && PasswordChecker.includesSpecialchar && PasswordChecker.sufficientLength) {
              // All the criteria have been met. display the success message to the console
              System.out.println("Success! The password satisfies the requirements.");
          }
      }
  }

  /**********
   * Reset each of the criteria to red and not yet satisfied after the user makes any change to
   * the input. The evaluation code updates the text and turns it green when a criterion is 
   * satisfied.
   */
  protected void displayAssessments() {

      // Upper case character
      label_UpperCase.setText("At least one upper case letter - Not yet satisfied");
      label_UpperCase.setTextFill(Color.RED);

      // Lower case character
      label_LowerCase.setText("At least one lower case letter - Not yet satisfied");
      label_LowerCase.setTextFill(Color.RED);

      // Numeric character
      label_NumericDigit.setText("At least one numeric digit - Not yet satisfied");
      label_NumericDigit.setTextFill(Color.RED);

      // Special character
      label_SpecialChar.setText("At least one special character - Not yet satisfied");
      label_SpecialChar.setTextFill(Color.RED);

      // Not long enough
      label_LongEnough.setText("At least eight characters - Not yet satisfied");
      label_LongEnough.setTextFill(Color.RED);
  }

  /**********
   * Check each criterion. If satisfied, update the text and turn it green
   */
  private void updateFlags() {
      // Upper case character
      if (PasswordChecker.includesUppercase) {
          label_UpperCase.setText("At least one upper case letter - Satisfied");
          label_UpperCase.setTextFill(Color.GREEN);
      }

      // Lower case character
      if (PasswordChecker.includesLowercase) {
          label_LowerCase.setText("At least one lower case letter - Satisfied");
          label_LowerCase.setTextFill(Color.GREEN);
      }

      // Numeric character
      if (PasswordChecker.includesNum) {
          label_NumericDigit.setText("At least one numeric digit - Satisfied");
          label_NumericDigit.setTextFill(Color.GREEN);
      }

      // Special character
      if (PasswordChecker.includesSpecialchar) {
          label_SpecialChar.setText("At least one special character - Satisfied");
          label_SpecialChar.setTextFill(Color.GREEN);
      }

      // Not long enough
      if (PasswordChecker.sufficientLength) {
          label_LongEnough.setText("At least eight characters - Satisfied");
          label_LongEnough.setTextFill(Color.GREEN);
      }
      
  }
  
  /**********
   * Method to handle login logic when the login button is clicked
   */
  private void handleCreateAcc(Main mainApp, String role) {
      String username = text_Username.getText();
      String password = text_Password.getText();

      // Check if Username is valid and password meets the criteria
      if (username.length() > 4 && PasswordChecker.includesUppercase && PasswordChecker.includesLowercase &&
    		  PasswordChecker.includesNum && PasswordChecker.includesSpecialchar && PasswordChecker.sufficientLength
    		  && text_Password.getText().equals(text_Confirm.getText())) {
    	  
    	  try {
              // Add Username and Password to the database and return to login page
              UserDatabase.insertUser(username, password, null, null, null, role);
              mainApp.showLoginPage();
          } catch (SQLException e) {
              // Handle SQL exceptions
              label_Error.setText("*Error saving user: " + e.getMessage());
          }
      }
  }
  
  /**********
   * Method to handle Create Account logic when the login button is clicked
   */
  private void handleReturnToLogin(Main mainApp) {
      if (UserDatabase.isDatabaseEmpty()) {
    	  label_noAdmin.setText("*Must Create Admin Account");
    	  return;
      }
      mainApp.showLoginPage();
      
  }
  
}


