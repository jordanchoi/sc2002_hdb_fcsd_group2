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

    private SessionStateManager session = SessionStateManager.getInstance();
    private UserAuthController controller = UserAuthController.getInstance();

    public ManagerView() {
        // Constructor logic if needed
    }

    public void start() {
        System.out.println("Welcome! Manager " + session.getLoggedInUser().getFirstName() + "!\nYou are in the Manager Main Menu.\n");
        int choice = 0;
        do {
            displayMenu();
            choice = handleUserInput();
        } while (choice != 10); // Assuming 10 is the exit option
    }


    @Override
    public void displayMenu() {
        System.out.println("What would you like to do?\n");
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
        System.out.print("\nPlease select an option: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1-> {
                // Manage BTO Exercises
                System.out.println("Managing BTO Exercises...");
                BTOExercisesView exercisesView = new BTOExercisesView();
                exercisesView.start();
            } case 2-> {
                // Manage BTO Projects
                System.out.println("Managing BTO Projects...");
            } case 3-> {
                // Manage BTO Applications
                System.out.println("Managing BTO Applications...");
            } case 4-> {
                // Manage All Enquiries
                System.out.println("Managing All Enquiries...");
            } case 5-> {
                // View All Projects
                System.out.println("Viewing All Projects...");
            } case 6-> {
                // Change Password
                System.out.println("Changing Password...");
                //changePassword();
            } case 7-> {
                // Logout
                System.out.println("Logging out...");
                session.logout();
            } case 10-> {
                // Exit
                session.logout();
                System.out.println("Exiting...");
            }
        }
        return choice;
    }
}
