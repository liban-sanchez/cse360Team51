package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {
	// Keep these attributes for access across methods
	public final static double WINDOW_WIDTH = 500;
	public final static double WINDOW_HEIGHT = 430;
	
	// References
	private Stage mainApp;  
	public CreateAccUI cGUI;
	public LoginUI lGUI;
	public FinishUI fGUI;
	public SystemUI sGUI;
	private OneTimeCode ot_code;
	
	/**
     * The entry point for the Help System application.
     * Initializes the main application stage and decides whether 
     * to show the create account or login page initially
     * @param theStage The primary stage for this application.
     * @throws Exception If an error occurs during startup.
     */
	@Override
	public void start(Stage theStage) throws Exception {
		 // Save a reference to the stage
		this.mainApp = theStage;
		// Initialize the one-time code handler
		this.ot_code = new OneTimeCode();
		
		 // Check if the user database is empty
		if (UserDatabase.isDatabaseEmpty()) {
			// Show create account page for admin
			showCreateAccountPage("Admin");
		}
		else {
			// Show login page if users within data base exist
			showLoginPage();
		}
		
		// Display the application window
		theStage.setTitle("Help System");
		theStage.show();
	}

	 /**
     * Displays the Create Account page.
     *
     * @param role is the role of the user being created (Admin, student, or instructor).
     */
	public void showCreateAccountPage(String role) {
		// Create a new Pane for the create account UI
		Pane createAccPane = new Pane();
		// Instantiate CreateAccUI and pass reference to Main
		cGUI = new CreateAccUI(createAccPane, this, role);
		// Create a new scene
		Scene createAccScene = new Scene(createAccPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		// Set the scene of the main application stage
		mainApp.setScene(createAccScene); 
	}

	/**
     * Displays the Login page.
     */
	public void showLoginPage() {
		// Create a new Pane for the login UI
		Pane loginPane = new Pane();
		// Instantiate LoginUI and pass reference to Main
		lGUI = new LoginUI(loginPane, this, ot_code);
		// Create a new scene
		Scene loginScene = new Scene(loginPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		// Set the scene to the main application stage
		mainApp.setScene(loginScene); 
	}
	
	/**
     * Displays the Finish Account page.
     *
     * @param username is the username of the user whose account is being finished.
     */
	public void showFinishPage(String username){
		// Create a new Pane for the finish account UI
		Pane finishPane = new Pane();
		// Instantiate FinishUI and pass reference to Main
		fGUI = new FinishUI(finishPane, this, username);
		// Create a new scene
		Scene finishScene = new Scene(finishPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		// Set the scene to the main application stage
		mainApp.setScene(finishScene);
	}
	
	/**
     * Displays the System UI for the user.
     *
     * @param username the username of the logged-in user.
     * @param role the role of the logged-in user.
     */
	public void showSystemUI(String username, String role) {
		// Create a new Pane for the system UI
        Pane systemPane = new Pane();
        // Instantiate SystemUI and pass reference to Main
        SystemUI sysUI = new SystemUI(systemPane, this, ot_code, role);
        // Create a new scene
        Scene systemScene = new Scene(systemPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        // Set the scene to the main application stage
        mainApp.setScene(systemScene);
    }

	/**
     * The main method to launch the application.
     *
     * @param args Command line arguments.
     */
	public static void main(String[] args) {
		try {
			// Initialize the table
            UserDatabase.createTable();
            // Use to reset if needed
            //UserDatabase.clearUsers();	
        } catch (SQLException e) {
            e.printStackTrace();
        }
		launch(args);
	}
	
}