package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;

/**
 * Represents an abstract base class for all users in the system.
 * Handles common attributes like NRIC, password, names, age, and marital status.
 */
public abstract class User {
    protected int userId;
    protected String nric;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String middleName;
    protected int age;
    protected MaritalStatus maritalStatus;

    /**
     * Constructor for basic user information (used based on project dataset).
     *
     * @param name the full name (first name used)
     * @param nric the NRIC number
     * @param age the age
     * @param maritalStatus the marital status
     * @param password the account password
     */
    public User(String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        this.firstName = name;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;

        // null because there it isn't in the data file
        this.lastName = null;
        this.middleName = null;
    }

    /**
     * Constructor for full real-world user information.
     *
     * @param userId the unique user ID
     * @param nric the NRIC
     * @param password the account password
     * @param firstName the first name
     * @param lastName the last name
     * @param middleName the middle name
     * @param age the age
     * @param maritalStatus the marital status
     */
    public User(int userId, String nric, String password, String firstName, String lastName, String middleName, int age, MaritalStatus maritalStatus) {
        this.userId = userId;
        this.nric = nric;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.age = age;
        this.maritalStatus = maritalStatus;
    }

    // === Getters and Setters ===

    /** @return the user ID. */
    public int getUserId() { return userId; }

    /** Sets the user ID. */
    public void setUserId(int id) { this.userId = id; }

    /** @return the NRIC. */
    public String getNric() { return nric; }

    /** Sets the NRIC. */
    public void setNric(String nric) { this.nric = nric; }

    /** @return the password. */
    public String getPwd() { return password; }

    /** Sets the password. */
    public void setPwd(String pwd) { this.password = pwd; }

    /**
     * Verifies if a given password matches this user's password.
     *
     * @param password the password to verify
     * @return true if match, false otherwise
     */
    public boolean verifyPwd(String password) {
        return this.password.equals(password);
    }

    /** @return the first name. */
    public String getFirstName() { return firstName; }

    /** Sets the first name. */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /** @return the middle name. */
    public String getMidName() { return middleName; }

    /** Sets the middle name. */
    public void setMidName(String midName) { this.middleName = midName; }

    /** @return the last name. */
    public String getLastName() { return lastName; }

    /** Sets the last name. */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /**
     * Combines first, middle, and last name into a full name string.
     *
     * @return full name
     */
    public String getFullName() {
        return firstName + " " + (middleName != null ? middleName + " " : "") + (lastName != null ? lastName : "");
    }

    /** @return the user's age. */
    public int getAge() { return age; }

    /** Sets the user's age. */
    public void setAge(int age) { this.age = age; }

    /** @return the user's marital status. */
    public MaritalStatus getMaritalStatus() { return maritalStatus; }

    /** Sets the user's marital status. */
    public void setMaritalStatus(MaritalStatus mStatus) { this.maritalStatus = mStatus; }

    /**
     * Validates if a given NRIC string follows correct Singapore NRIC format.
     *
     * @param nric the NRIC string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidNRIC(String nric) {
        if (nric.length() != 9) return false;
        char firstChar = nric.charAt(0);
        char lastChar = nric.charAt(8);

        if (firstChar != 'S' && firstChar != 'T') return false;

        for (int i = 1; i <= 7; i++) {
            if (!Character.isDigit(nric.charAt(i))) return false;
        }

        return Character.isLetter(lastChar);
    }

    /**
     * Abstract method to be implemented by child classes to display their own menu.
     */
    public void viewMenu() {}
}
