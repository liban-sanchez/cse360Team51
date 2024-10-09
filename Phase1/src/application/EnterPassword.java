package application;

/**
 * Default Constructor
 */
public class Testing {
	
	/**
	 * Main
	 */
	public static void main(String[] args) {
		System.out.println("Requirements for Password: \n8 or more characters, \nat least one upper case letter, \nat least one lower case letter, \nat least numerical value, \nand at least one special character");
		System.out.println("____________________________________________________________________________");

		performPaswwordChecker("Aa!15678");

		performPaswwordChecker("A!");
		
		performPaswwordChecker("Aa!15678");
		
		performPaswwordChecker("A!");
		
		performPaswwordChecker("");
		
		performPaswwordChecker("kevin");

		
		System.out.println("____________________________________________________________________________");
		System.out.println();
	}

	/**
	 * This method is used to call another method and print to the interface
	 * @param input is the password that is entered
	 */
	private static void performPaswwordChecker(String input) {
		System.out.println("\nInput: \"" + input + "\"");
		
		
		Check.checkPassword(input);
		
		System.out.println();
		
		if (Check.includesUppercase && Check.includesLowercase && Check.includesNum && Check.includesSpecialchar) {
			System.out.println("Password Accepted!");
		}
		
		if (input.length() <= 0) {
			System.out.println("Password has not been entered. Please enter password.");
		}
		
		if (input.length() < 8) {
			System.out.println("Password not long enough. Needs to be 8 characters or more");
		}
		print();
	}
	
	/**
	 * Prints to the interface
	 */
	private static void print() {
		if (Check.includesUppercase) {	
		}
		else if (!Check.includesUppercase) {
			System.out.println("Missing an upper case character");
		}
		if (Check.includesLowercase) {
		}
		else if (!Check.includesLowercase){
			System.out.println("Missing a lower case character");
		}
		if (Check.includesNum) {
		}
		else if (!Check.includesNum){
			System.out.println("Missing a numerical value");
		}
		if (Check.includesSpecialchar) {
		}
		else if (!Check.includesSpecialchar){
			System.out.println("Missing a special character");
		}
	}
}
