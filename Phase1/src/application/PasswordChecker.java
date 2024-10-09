package application;

/**
 * Default Constructor
 */
public class PasswordChecker {

	/**
	 * Initialize variables used 
	 */
	
	/**
	 * Initializes includesUppercase which is used to determine whether or not the input includes an upper case character
	 */
	public static boolean includesUppercase = false;
	
	/**
	 * Initializes includesLowercase which is used to determine whether or not the input includes an lower case character
	 */
	public static boolean includesLowercase = false;	
	
	/**
	 * Initializes includesNum which is used to determine whether or not the input includes a numerical value
	 */
	public static boolean includesNum = false;			
	
	/**
	 * Initializes includesSpecialchar which is used to determine whether or not the input includes a special character
	 */
	public static boolean includesSpecialchar = false;
	
	/**
	 * Initializes sufficientLength which is used to determine whether or not the input is of sufficient length
	 */
	public static boolean sufficientLength = false;
	
	/**
	 * Initializes currentChar which is used to iterate through the input
	 */
	private static char currentChar;
	
	/**
	 * Initializes running which is used to determine when the while loop stops
	 */
	private static boolean running;
	
	/**
	 * Initializes index which is used to iterate through the input
	 */
	private static int index;
	
	/**
	 * Input goes through this method and it determines whether or not the password is valid or not
	 * @param input is the password that is entered
	 * @return returns a string based on the input given
	 */
	public static String checkPassword(String input) {
		index = 0;

		if(input.length() <= 0) {
			return "The password is empty! Please enter password.";
		}
		
		currentChar = input.charAt(0);			// currentChar is the first char in the input

		includesUppercase = false;
		includesLowercase = false;	
		includesNum = false;
		includesSpecialchar = false;
		sufficientLength = false;
		running = true;

		/**
		 * iterates through the password and determines the characteristics of the password
		 */
		while (running) {
			if (currentChar >= 'A' && currentChar <= 'Z') {
				includesUppercase = true;
			} 
			else if (currentChar >= 'a' && currentChar <= 'z') {
				includesLowercase = true;
			} 
			else if (currentChar >= '0' && currentChar <= '9') {
				includesNum = true;
			} 
			else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(currentChar) >= 0) {
				includesSpecialchar = true;
			} 
			
			if (index >= 7) {
				System.out.println("At least 8 characters found");
				sufficientLength= true;
			}
			
			index++;
			if (index >= input.length()) {
				running = false;
			}
			else {
				currentChar = input.charAt(index);
			}
		}
		if (includesUppercase && includesLowercase && includesNum && includesSpecialchar) {
			return "Password Accepted!";
		}
		return "";
	}
}
