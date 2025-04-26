package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.UserAuthController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.User;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.UserRoles;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;

import java.util.Scanner;

/**
 * Handles the login process for users.
 * Redirects users to their appropriate views based on their roles after successful authentication.
 */
public class LoginView {
    private String nric;
    private String password;
    private UserAuthController controller = UserAuthController.getInstance();
    private SessionStateManager session = SessionStateManager.getInstance();

    /**
     * Constructs a new LoginView with empty credentials.
     */
    public LoginView() {
        this.nric = "";
        this.password = "";
    }

    /**
     * Starts the login process.
     * Allows the user up to 3 attempts to log in before blocking.
     * Redirects to different views depending on user role.
     */
    public void start() {
        int loginAttempts = 3;

        while (loginAttempts > 0 && session.isLoggedIn() == false) {
            displayLoginScreen();
            handleUserInput();
            if (controller.login(nric, password) != null) {
                System.out.println("Login successful!\n");
                // Proceed to the next screen or functionality
                if (session.getLoggedInUserType() == UserRoles.APPLICANT) {
                    System.out.println("Redirecting to Applicant View..\n");
                    // Redirect to ApplicantView
                    ApplicantView applicantView = new ApplicantView();
                    applicantView.start();
                } else if (session.getLoggedInUserType() == UserRoles.OFFICER) {
                    // Redirect to Officer View
                    System.out.println("Redirecting to Officer View..\n");
                    OfficerView officerView = new OfficerView();
                    officerView.start();
                } else if (session.getLoggedInUserType() == UserRoles.MANAGER) {
                    // Redirect to BTO Officer View
                    System.out.println("Redirecting to Manager View..\n");
                    ManagerView managerView = new ManagerView();
                    managerView.start();
                }
            } else {
                System.out.println("Invalid NRIC or password.\n");
                if (loginAttempts > 1) {
                    System.out.println("Please try again.\n");
                }
                System.out.println("You have " + (loginAttempts - 1) + " attempts left.\n");
            }
            loginAttempts--;
        }
    }

    /**
     * Displays the login screen prompt asking for NRIC and password.
     */
    public void displayLoginScreen() {
        System.out.println("Please enter your NRIC and password to login.\n");
    }

    /**
     * Handles user input for login credentials (NRIC and password).
     */
    public void handleUserInput() {
        // Logic to handle user input for login
        Scanner scanner = new Scanner(System.in);
        System.out.print("NRIC: ");
        this.nric = scanner.nextLine();
        System.out.print("Password: ");
        this.password = scanner.nextLine();
    }

    /**
     * Returns the entered NRIC.
     *
     * @return the user's NRIC
     */
    public String getNric() {
        return nric;
    }

    /**
     * Returns the entered password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }
}
