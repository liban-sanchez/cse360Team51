package application;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OneTimeCode {
	private Random random;
    // Maps to store one-time codes and their details
    private Map<String, CodeInfo> inviteCodes; // For invite codes
    private Map<String, CodeInfo> resetCodes;  // For password reset codes

    /**
     * Constructor to initialize the OneTimeCode instance.
    */
    public OneTimeCode() {
    	// Initializing the random number generator
    	this.random = new Random();
    	// Initializing the invite codes map
        inviteCodes = new HashMap<>();
        // Initializing the reset codes map
        resetCodes = new HashMap<>();
    }

    /**
     * Method to generate an invite code with an associated role and expiration time.
     *
     * @param role The role associated with the invite code.
     * @return A unique invite code as a String.
     */
    public String generateInviteCode(String role) {
    	int num = (10000 + random.nextInt(90000));
        String code = String.valueOf(num); // Generate a unique code
        long expirationTime = System.currentTimeMillis() + 300000; // 5 minutes expiration
        inviteCodes.put(code, new CodeInfo(role, expirationTime, false));
        return code;
    }

    /**
     * Method to validate an invite code.
     *
     * @param code The invite code to validate.
     * @return True if the code is valid; false otherwise.
     */
    public boolean validateInviteCode(String code) {
        CodeInfo details = inviteCodes.get(code);
        if (details == null || details.isUsed()) {
            return false; 
        }
        if (System.currentTimeMillis() > details.getExpirationTime()) {
            return false; // Code has expired
        }
        return true; // Code is valid and not used
    }

    /**
     * Mark the invite code as used and remove it from storage.
     *
     * @param code The invite code to mark as used.
     */
    public void useInviteCode(String code) {
        CodeInfo details = inviteCodes.get(code);
        if (details != null) {
            details.setUsed(true);
            inviteCodes.remove(code); // Remove the code from the map after use
        }
    }

    /**
     * Method to generate a one-time password (reset code) with expiration time.
     *
     * @return A unique reset code as a String.
     */
    public String generateResetCode() {
    	int num = (10000 + random.nextInt(90000));
        String code = String.valueOf(num);
        long expirationTime = System.currentTimeMillis() + 300000; // 5 minutes expiration
        resetCodes.put(code, new CodeInfo(null, expirationTime, false)); // No role associated
        return code;
    }

    /**
     * Method to validate the password reset code.
     *
     * @param code The reset code to validate.
     * @return True if the code is valid; false otherwise.
     */
    public boolean validateResetCode(String code) {
        CodeInfo details = resetCodes.get(code);
        if (details == null || details.isUsed()) {
            return false;
        }
        if (System.currentTimeMillis() > details.getExpirationTime()) {
            return false;
        }
        return true;
    }

    /**
     * Mark the reset code as used and remove it from storage.
     *
     * @param code The reset code to mark as used.
     */
    public void useResetCode(String code) {
        CodeInfo details = resetCodes.get(code);
        if (details != null) {
            details.setUsed(true);
            resetCodes.remove(code); // Remove the code from the map after use
        }
    }
    
    /**
     * Method to get the role associated with an invite code.
     *
     * @param code The invite code to check.
     * @return The associated role, or null if not found.
     */
    public String getInviteCodeRole(String code) {
        CodeInfo details = inviteCodes.get(code);
        return (details != null) ? details.getRole() : null; // Return the role or null if not found
    }

    // Class to represent the code details
    public class CodeInfo {
    	// The role associated with the code
        private String role; 
        // Expiration time of the code
        private long expirationTime; 
        // Flag to indicate if the code has been used
        private boolean used;
        
        /**
        * Constructor to initialize CodeInfo.
        *
        * @param role The role associated with the code.
        * @param expirationTime The expiration time of the code.
        * @param used Whether the code has been used.
        */
        public CodeInfo(String role, long expirationTime, boolean used) {
            this.role = role;
            this.expirationTime = expirationTime;
            this.used = used;
        }
        
        // Getter for expiration time
        public long getExpirationTime() {
            return expirationTime;
        }

        // Getter for used status
        public boolean isUsed() {
            return used;
        }
        
        // Setter for used status
        public void setUsed(boolean used) {
            this.used = used;
        }
        
        // Getter for role
        public String getRole() {
            return role;
        }
    }
}