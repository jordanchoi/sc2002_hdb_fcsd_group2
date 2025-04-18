package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.BTOProjsController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.HDBBTOExerciseController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.UserAuthController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;
import java.util.Scanner;

public class ManagerView implements UserView {
    private final SessionStateManager session = SessionStateManager.getInstance();
    private final UserAuthController controller = UserAuthController.getInstance();

    private final BTOProjsController projsController = new BTOProjsController();
    private final HDBBTOExerciseController exerciseController = new HDBBTOExerciseController();

    public ManagerView() {
        // Constructor logic if needed
    }

    public void start() {
        System.out.println("Welcome! Manager " + session.getLoggedInUser().getFirstName() + "!\nYou are in the Manager Main Menu.\n");
        int choice = 0;
        do {
            displayMenu();
            choice = handleUserInput();
        } while (choice != 10);
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
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nPlease select an option: ");
        int choice = scanner.nextInt();

        BTOExercisesView exercisesView = new BTOExercisesView(exerciseController);
        BTOProjectsView projectsView = new BTOProjectsView(projsController, exerciseController);

        switch (choice) {
            case 1 -> {
                System.out.println("Managing BTO Exercises...");
                exercisesView.start();
            }
            case 2 -> {
                System.out.println("Managing BTO Projects...");
                projectsView.start();
            }
            case 3 -> System.out.println("Managing BTO Applications...");
            case 4 -> System.out.println("Managing All Enquiries...");
            case 5 -> {
                System.out.println("Viewing All Projects...");
                projectsView.viewAllProjects(projsController); // optional shortcut
            }
            case 6 -> {
                System.out.println("Changing password...");
                changePassword();
            }
            case 7 -> {
                System.out.println("Logging out...");
                session.logout();
            }
            case 10 -> {
                System.out.println("Exiting...");
                session.logout();
            }
        }
        return choice;
    }

    public void changePassword() {
        Scanner scanner = new Scanner(System.in);

        // Change Password Attempt Count
        int attempts = 3;

        do {
            System.out.println("You have " + attempts + " attempts left to change your password.");
            System.out.print("Please enter your current password: ");
            String currentPassword = scanner.next();

            if (!controller.validatePassword(currentPassword)) {
                System.out.println("Current password is incorrect. Please try again.");
                attempts--;
            } else {
                System.out.print("Please enter your new password: ");
                String newPassword = scanner.next();
                if (controller.changePassword(currentPassword, newPassword)) {
                    System.out.println("Password changed successfully!");
                } else {
                    System.out.println("Failed to change password.");
                }
                break;
            }
        } while (attempts > 0);
    }
}
