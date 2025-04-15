package model.entities;

import model.enums.MaritalStatus;

public abstract class User {
    protected int userId;
    protected String nric;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String middleName;
    protected int age;
    protected MaritalStatus maritalStatus;
    
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


    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;   
    }

    public String getPwd() {
        return password;
    }

    public void setPwd(String pwd) {
        this.password = pwd;
    }

    public boolean verifyPwd(String password) {
        return this.password.equals(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMidName() {
        return middleName;
    }

    public void setMidName(String midName) {
        this.middleName = midName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + (middleName != null ? middleName + " " : "") + lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus mStatus) {
        this.maritalStatus = mStatus;
    }

    // Simple NRIC format validation using string operations
    public static boolean isValidNRIC(String nric) {
        if (nric.length() != 9) {
            return false;
        }
        
        char firstChar = nric.charAt(0);
        char lastChar = nric.charAt(8);

        if (firstChar != 'S' && firstChar != 'T') {
            return false;
        }

        for (int i = 1; i <= 7; i++) {
            if (!Character.isDigit(nric.charAt(i))) {
                return false;
            }
        }

        if (!Character.isLetter(lastChar)) {
            return false;
        }

        return true; // NRIC is valid
    }

    public void viewMenu() {}
}
