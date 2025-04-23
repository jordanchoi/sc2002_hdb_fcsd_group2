package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBOfficer;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.User;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.UserRoles;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;

import java.util.List;
import java.util.Optional;

public class UserAuthController {

    // This class handles user authentication and login functionality.
    // It provides methods for user login and validation.
    // It also includes a method to check if the user is eligible to apply for a BTO flat.
    // This class is used to manage user authentication and login functionality.
    // It is used to manage user authentication and login functionality.

    // This is a repository class that handles user data storage and retrieval.
    // It is used to manage user data and perform CRUD operations on user data.

    // Convert to Singleton
    private static UserAuthController instance;

    // This is a static instance of the UserRepository class.
    private final UserRepository userRepository = UserRepository.getInstance();
    private final SessionStateManager session = SessionStateManager.getInstance();

    // Private constructor to prevent instantiation
    private UserAuthController() {}

    // Public accessor
    public static UserAuthController getInstance() {
        if (instance == null) {
            instance = new UserAuthController();
        }
        return instance;
    }

    // This method handles user login and authentication.
    public User login(String nric, String password) {
        // This method handles user login and authentication.
        // It takes a list of users as input and checks if the provided NRIC and password match any user in the list.
        // If a match is found, it returns the corresponding User object.
        // If no match is found, it prompts the user to try again.
        // It also validates the NRIC format before proceeding with the login process.
        // This method is used to handle user login and authentication.
        // It is used to manage user authentication and login functionality.

        // get the said user from the repository by nric
        Optional<User> user = userRepository.getUserByNric(nric);

        // Check if NRIC is valid
        if (user.isPresent()) {
            User foundUser = user.get();
            // check the password if NRIC is valid and user is retrieved
            if (foundUser.getPwd().equals(password)) {
                UserRoles userRoles = null;
                if (foundUser instanceof HDBOfficer) {
                    userRoles = UserRoles.OFFICER;
                } else if (foundUser instanceof HDBApplicant) {
                    userRoles = UserRoles.APPLICANT;
                } else if (foundUser instanceof HDBManager) {
                    userRoles = UserRoles.MANAGER;
                }
                session.login(foundUser, userRoles);
                return foundUser;
            } else {
                // Password is not valid. Comment out the line below to remove the message before submission.
                System.out.println("Incorrect password.");
            }
        } else {
            // NRIC is not valid. Comment out the line below to remove the message before submission.
            System.out.println("NRIC not found.");
        }
        return null; // Placeholder return statement
    }

    public boolean logout() {
        // This method handles user logout functionality.
        // It clears the session state and logs out the user.
        // This method is used to manage user logout functionality.
        // It is used to manage user authentication and login functionality.
        session.logout();
        return true;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        // This method handles password change functionality.
        // It checks if the old password matches the current password and updates it to the new password.
        // This method is used to manage user password change functionality.
        // It is used to manage user authentication and login functionality.

        // Check if user is logged in first.
        if (session.isLoggedIn()) {
            User user = session.getLoggedInUser();
            if (user.getPwd().equals(oldPassword)) {
                user.setPwd(newPassword);
                userRepository.updateUserPassword(user);
               // System.out.println("Password changed successfullyTry.");
                // Need to call to update the data file.
                return true;
            } else {
                // System.out.println("Old password is incorrect. Try again.");
                return false;
            }
        } else {
            // This shouldn't run.
            System.out.println("User is not logged in. Please log in first.");
            return false;
        }


    }

    public boolean validatePassword(String password) {
        // This method validates the provided password against the stored password for the given NRIC.
        // It returns true if the password is valid, false otherwise.
        // This method is used to manage user authentication and login functionality.
        // It is used to manage user authentication and login functionality.

        User user = SessionStateManager.getInstance().getLoggedInUser();
        return user.getPwd().equals(password);
    }
}
