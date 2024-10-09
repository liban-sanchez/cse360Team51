package application;
	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {
	// Keep these attributes for access across methods
	public final static double WINDOW_WIDTH = 500;
	public final static double WINDOW_HEIGHT = 430;
	public CreateAccUI cGUI;
	public LoginUI lGUI;
	private Stage mainApp;   // Reference to the main Stage

	@Override
	public void start(Stage theStage) throws Exception {
		this.mainApp = theStage;  // Save a reference to the stage
		boolean init = false;
		
		if (init) {
			showCreateAccountPage(false);
		}
		else {
			showLoginPage();
		}
		
		theStage.setTitle("Help System");
		theStage.show();
	}

	// Method to display the Create Account page
	public void showCreateAccountPage(boolean admin) {
		Pane createAccPane = new Pane();
		cGUI = new CreateAccUI(createAccPane, this, admin);   // Pass reference to Main
		Scene createAccScene = new Scene(createAccPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		mainApp.setScene(createAccScene); 
	}

	// Method to display the Login page
	public void showLoginPage() {
		Pane loginPane = new Pane();
		lGUI = new LoginUI(loginPane, this);   // Implement LoginUI as needed
		Scene loginScene = new Scene(loginPane, WINDOW_WIDTH, WINDOW_HEIGHT);
		mainApp.setScene(loginScene); 
	}

	public static void main(String[] args) {
		launch(args);
	}
}