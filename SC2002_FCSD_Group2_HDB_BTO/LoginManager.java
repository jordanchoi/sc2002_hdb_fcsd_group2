import java.util.Scanner;
import java.util.List;

class LoginManager {
    public static User login(List<User> users) {
        Boolean status = false;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter NRIC: ");
        String nric = scanner.nextLine().toUpperCase();

        // Validate NRIC using simple string operations
        if (!User.isValidNRIC(nric)) {
            System.out.println("Error: Invalid NRIC format! Example of a correct format: S1234567A.");
            return login(users); // Retry login
        }

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        for (User user : users) {
            if (user.nric.equals(nric) && user.password.equals(password)) {
                System.out.println("Login successful! Welcome, " + user.getName());
                return user;
            }
            else if(user.nric.equals(nric) && user.password != (password)){
                status = true;
                System.out.println("Incorrect password. Try again.");
            }
        }
        if (status != true){
            System.out.println("NRIC not found. Try again.");
            status = false;
        }
        return login(users);
    }
}
