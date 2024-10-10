package application;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OneTimeCode {
    // Maps to store one-time codes and their details
    private Map<String, CodeDetails> inviteCodes; // For invite codes
    private Map<String, CodeDetails> resetCodes;  // For password reset codes

    public OneTimeCode() {
        inviteCodes = new HashMap<>();
        resetCodes = new HashMap<>();
    }

    // Method to generate an invite code with expiration time and role
    public String generateInviteCode(String role) {
        String code = UUID.randomUUID().toString(); // Generate a unique code
        long expirationTime = System.currentTimeMillis() + 300000; // 5 minutes expiration
        inviteCodes.put(code, new CodeDetails(role, expirationTime, false));
        return code;
    }

    // Method to validate an invite code
    public boolean validateInviteCode(String code) {
        CodeDetails details = inviteCodes.get(code);
        if (details == null || details.isUsed()) {
            return false; // Code does not exist or has been used
        }
        if (System.currentTimeMillis() > details.getExpirationTime()) {
            return false; // Code has expired
        }
        return true; // Code is valid and not used
    }

    // Mark the invite code as used and remove it
    public void useInviteCode(String code) {
        CodeDetails details = inviteCodes.get(code);
        if (details != null) {
            details.setUsed(true);
            inviteCodes.remove(code); // Remove the code from the map after use
        }
    }

    // Method to generate a one-time password (for password reset)
    public String generateResetCode() {
        String code = UUID.randomUUID().toString(); // Generate a unique reset code
        long expirationTime = System.currentTimeMillis() + 300000; // 5 minutes expiration
        resetCodes.put(code, new CodeDetails(null, expirationTime, false)); // No role associated
        return code;
    }

    // Method to validate the password reset code
    public boolean validateResetCode(String code) {
        CodeDetails details = resetCodes.get(code);
        if (details == null || details.isUsed()) {
            return false; // Code does not exist or has been used
        }
        if (System.currentTimeMillis() > details.getExpirationTime()) {
            return false; // Code has expired
        }
        return true; // Code is valid and not used
    }

    // Mark the reset code as used and remove it
    public void useResetCode(String code) {
        CodeDetails details = resetCodes.get(code);
        if (details != null) {
            details.setUsed(true);
            resetCodes.remove(code); // Remove the code from the map after use
        }
    }

    // Class to represent the code details
    public class CodeDetails {
        private String role; // Role for invite code
        private long expirationTime; // Expiration time of the code
        private boolean used; // Indicates if the code has been used

        public CodeDetails(String role, long expirationTime, boolean used) {
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