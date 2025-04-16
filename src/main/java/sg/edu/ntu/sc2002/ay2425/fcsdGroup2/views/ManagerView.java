package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.UserAuthController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;
import java.util.Scanner;

public class ManagerView implements UserView {
    /*
     * This class is responsible for displaying the manager menu and handling user input.
     * It implements the UserView interface.
     * The manager menu includes options to manage BTO exercises, projects, applications,
     * enquiries, and view all projects.
     * The class uses the UserAuthController to manage user authentication and authorization.
     * It also provides a method to change the password.
     * The class uses the SessionStateManager to manage the session state and
     * user information.
     */

    SessionStateManager session = SessionStateManager.getInstance();
    UserAuthController controller = UserAuthController.getInstance();

    public ManagerView() {
        // Constructor logic if needed
    }

    public void run() {
        System.out.println("Welcome! Manager " + session.getLoggedInUser().getFirstName());
        int choice = 0;
        do {
            displayMenu();
            choice = handleUserInput();
        } while (choice != 10); // Assuming 10 is the exit option

    }


    @Override
    public void displayMenu() {
        System.out.println("1. Manage BTO Exercises");
        System.out.println("2. Manage BTO Projects");
        System.out.println("3. Manage BTO Applications");
        System.out.println("4. Manage All Enquiries");
        System.out.println("5. View All Projects");
        System.out.println("6. Change Password");
        System.out.println("7. Logout");
        System.out.println("10. Exit");
    }

    @Override
    public int handleUserInput() {
        System.out.print("Please select an option: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // Manage BTO Exercises
                System.out.println("Managing BTO Exercises...");
                break;
            case 2:
                // Manage BTO Projects
                System.out.println("Managing BTO Projects...");
                break;
            case 3:
                // Manage BTO Applications
                System.out.println("Managing BTO Applications...");
                break;
            case 4:
                // Manage All Enquiries
                System.out.println("Managing All Enquiries...");
            case 5:
                // View All Projects
                System.out.println("Viewing All Projects...");
                break;
            case 6:
                // Change Password
                System.out.println("Changing Password...");
                //changePassword();
                break;
            case 7:
                // Logout
                System.out.println("Logging out...");
                session.logout();
                break; // Exit the menu
            case 10:
                // Exit
                System.out.println("Exiting...");
                break; // Exit the menu
        }
        return choice;
    }
}
