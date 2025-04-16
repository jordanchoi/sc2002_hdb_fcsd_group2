package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.UserAuthController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.UserRoles;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;

import java.util.Scanner;

public class LoginView {
    private String username;
    private String password;
    UserAuthController controller = new UserAuthController();
    SessionStateManager session = SessionStateManager.getInstance();

    public LoginView() {
        this.username = "";
        this.password = "";
        int loginAttempts = 3;

        while (loginAttempts > 0 && session.isLoggedIn() == false) {
            displayLoginScreen();
            handleUserInput();
            if (controller.login(username, password) != null) {
                System.out.println("Login successful!");
                // Proceed to the next screen or functionality
                if (session.getLoggedInUserType() == UserRoles.APPLICANT) {
                    // Redirect to ApplicantView
                    System.out.println("Redirecting to Applicant View...");
                    System.out.println("You are logged in as an applicant.");
                    System.out.println("Welcome! Applicant " + session.getLoggedInUser().getFirstName());
                } else if (session.getLoggedInUserType() == UserRoles.OFFICER) {
                    // Redirect to AdminView
                    System.out.println("Redirecting to Officer View...");
                    System.out.println("Welcome! Officer " + session.getLoggedInUser().getFirstName());
                } else if (session.getLoggedInUserType() == UserRoles.MANAGER) {
                    // Redirect to BTO Officer View
                    System.out.println("Redirecting to BTO Manager View...");
                    System.out.println("Welcome! Manager " + session.getLoggedInUser().getFirstName());
                }
            } else {
                System.out.println("Invalid username or password.");
                if (loginAttempts > 1) {
                    System.out.println("Please try again.");
                }
                System.out.println("You have " + (loginAttempts - 1) + " attempts left.");
            }
            loginAttempts--;
        }
    }

    public void displayLoginScreen() {
        System.out.println("Please enter your username and password to login.");
    }

    public void handleUserInput() {
        // Logic to handle user input for login
        // This could involve reading from console or GUI input fields
        // For simplicity, we will just simulate user input here
        Scanner scanner = new Scanner(System.in);
        System.out.print("Username: ");
        this.username = scanner.nextLine();
        System.out.print("Password: ");
        this.password = scanner.nextLine();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
