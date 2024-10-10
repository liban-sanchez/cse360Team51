package application;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OneTimeCode {
	private Random random;
    // Maps to store one-time codes and their details
    private Map<String, CodeInfo> inviteCodes; // For invite codes
    private Map<String, CodeInfo> resetCodes;  // For password reset codes

    public OneTimeCode() {
    	this.random = new Random();
        inviteCodes = new HashMap<>();
        resetCodes = new HashMap<>();
    }

    // Method to generate an invite code with expiration time and role
    public String generateInviteCode(String role) {
    	int num = (10000 + random.nextInt(90000));
        String code = String.valueOf(num); // Generate a unique code
        long expirationTime = System.currentTimeMillis() + 300000; // 5 minutes expiration
        inviteCodes.put(code, new CodeInfo(role, expirationTime, false));
        return code;
    }

    // Method to validate an invite code
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

    // Mark the invite code as used and remove it
    public void useInviteCode(String code) {
        CodeInfo details = inviteCodes.get(code);
        if (details != null) {
            details.setUsed(true);
            inviteCodes.remove(code); // Remove the code from the map after use
        }
    }

    // Method to generate a one-time password (for password reset code)
    public String generateResetCode() {
    	int num = (10000 + random.nextInt(90000));
        String code = String.valueOf(num);
        long expirationTime = System.currentTimeMillis() + 300000; // 5 minutes expiration
        resetCodes.put(code, new CodeInfo(null, expirationTime, false)); // No role associated
        return code;
    }

    // Method to validate the password reset code
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

    // Mark the reset code as used and remove it
    public void useResetCode(String code) {
        CodeInfo details = resetCodes.get(code);
        if (details != null) {
            details.setUsed(true);
            resetCodes.remove(code); // Remove the code from the map after use
        }
    }
    
    // Method to get the role associated with an invite code
    public String getInviteCodeRole(String code) {
        CodeInfo details = inviteCodes.get(code);
        return (details != null) ? details.getRole() : null; // Return the role or null if not found
    }

    // Class to represent the code details
    public class CodeInfo {
        private String role; 
        private long expirationTime; 
        private boolean used;

        public CodeInfo(String role, long expirationTime, boolean used) {
            this.role = role;
            this.expirationTime = expirationTime;
            this.used = used;
        }

        public long getExpirationTime() {
            return expirationTime;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getRole() {
            return role;
        }
    }
}