abstract class User {
    protected String name;
    protected String nric;
    protected String password;
    protected int age;
    protected String maritalStatus;

    public User(String name, String nric, String password, int age, String maritalStatus) {
        if (!isValidNRIC(nric)) {
            throw new IllegalArgumentException("Invalid NRIC format! Must start with 'S' or 'T', followed by 7 digits, and end with a letter.");
        }
        this.name = name;
        this.nric = nric;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
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

    public abstract void viewMenu();

    public abstract void viewProjects();

    public void changePass(String password){
        this.password = password;
        System.out.println("Password successfully changed!");
    }

    public String getName(){
        return name;
    }
}
