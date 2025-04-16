package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.User;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;

import java.util.List;
import java.util.Optional;

class UserAuthController {

    // This class handles user authentication and login functionality.
    // It provides methods for user login and validation.
    // It also includes a method to check if the user is eligible to apply for a BTO flat.
    // This class is used to manage user authentication and login functionality.
    // It is used to manage user authentication and login functionality.

    // This is a repository class that handles user data storage and retrieval.
    // It is used to manage user data and perform CRUD operations on user data.

    // This is a static instance of the UserRepository class.
    private static final UserRepository userRepository = new UserRepository();
    private static final SessionStateManager session = SessionStateManager.getInstance();

    // This method handles user login and authentication.
    public static User Login(String nric, String password) {
        // This method handles user login and authentication.
        // It takes a list of users as input and checks if the provided NRIC and password match any user in the list.
        // If a match is found, it returns the corresponding User object.
        // If no match is found, it prompts the user to try again.
        // It also validates the NRIC format before proceeding with the login process.
        // This method is used to handle user login and authentication.
        // It is used to manage user authentication and login functionality.

        Optional<User> user = userRepository.getUserByNric(nric);
        // Check if NRIC is valid
        if (user.isPresent()) {
            if (user.get().getPwd().equals(password)) {
                session.login(user.get());
                return user.get();
            } else {
                System.out.println("Incorrect password. Try again.");
            }
        } else {
            System.out.println("NRIC not found. Try again.");
        }

        return null; // Placeholder return statement
    }

    public static User login(List<User> users) {
    /* REMOVED BY JORDAN - START FROM AFRESH
        boolean status = false;
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
            if (user.getNric().equals(nric) && user.getPwd().equals(password)) {
                System.out.println("Login successful! Welcome, " + user.getFullName());
                return user;
            }
            else if(user.getNric().equals(nric) && user.getPwd() != (password)){
                status = true;
                System.out.println("Incorrect password. Try again.");
            }
        }
        if (status != true){
            System.out.println("NRIC not found. Try again.");
            status = false;
        }
        return login(users);
     */
        return null; // Placeholder return statement
    }
}
