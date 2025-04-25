package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.handlers.ManagerViewHandler;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;
import java.util.Scanner;

/**
 * Represents the view for HDB Manager users.
 * Provides functionality to manage BTO exercises, BTO projects, enquiries,
 * generate reports, and manage account settings like password changes.
 */
public class ManagerView implements UserView {

    private final SessionStateManager session = SessionStateManager.getInstance();
    private final UserAuthController controller = UserAuthController.getInstance();
    private final BTOProjsController projsController = new BTOProjsController();
    private final HDBBTOExerciseController exerciseController = new HDBBTOExerciseController();
    private final ApplicationController applicationController = new ApplicationController();
    private final OfficerProjectApplicationController officerProjectApplicationController = new OfficerProjectApplicationController();

    /**
     * Constructs a new ManagerView.
     */
    public ManagerView() {
        // Constructor logic if needed
    }

    /**
     * Starts the manager view session, displaying the main menu
     * and handling user input.
     */
    public void start() {
        System.out.println("Welcome! Manager " + session.getLoggedInUser().getFirstName() + "!\nYou are in the Manager Main Menu.\n");
        int choice = 0;
        do {
            displayMenu();
            choice = handleUserInput();
        } while (choice != 10 || choice != 6);
    }

    /**
     * Displays the main menu options for the manager.
     */
    @Override
    public void displayMenu() {
        System.out.println("What would you like to do?\n");
        System.out.println("1. Manage BTO Exercises");
        System.out.println("2. Manage BTO Projects");
        System.out.println("3. Manage All Enquiries");
        System.out.println("4. Generate Report");
        System.out.println("5. Change Password");
        System.out.println("6. Logout");
        System.out.println("10. Exit");
    }

    /**
     * Handles user input from the menu and executes the corresponding actions.
     *
     * @return the selected menu choice
     */
    @Override
    public int handleUserInput() {
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        BTOExercisesView exercisesView = new BTOExercisesView(exerciseController);
        BTOProjectsView projectsView = new BTOProjectsView(projsController, exerciseController, applicationController, officerProjectApplicationController);

        while (true) {
            System.out.print("\nPlease select an option: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
                continue;
            }

            choice = scanner.nextInt();
            scanner.nextLine();

            if (choice < 1 || (choice > 6 && choice != 10)) {
                System.out.println("Invalid choice. Please enter a valid option (1–7 or 10).");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.println("Managing BTO Exercises...");
                    exercisesView.start();
                }
                case 2 -> {
                    System.out.println("Managing BTO Projects...");
                    projectsView.start();
                }
                case 3 ->  {
                    System.out.println("Managing All Enquiries...");
                    new EnquiryView(new ManagerViewHandler((HDBManager) session.getLoggedInUser())).display();
                }
                case 4 -> {
                    System.out.println("Generating Report...");
                    generateReport();
                }
                case 5 -> {
                    System.out.println("Changing password...");
                    changePassword();
                }
                case 6 -> {
                    System.out.println("Logging out...");
                    session.logout();
                    System.exit(1);
                }
                case 10 -> {
                    System.out.println("Exiting...");
                    session.logout();
                    System.exit(1);
                }
            }
            return choice;
        }
    }

    /**
     * Allows the manager to change their password.
     * Prompts the user to validate the current password and enter a new one.
     */
    public void changePassword() {
        Scanner scanner = new Scanner(System.in);
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

    /**
     * Initiates report generation.
     */
    public void generateReport() {
        ReportView reportView = new ReportView();
        reportView.generateReport();
    }
}
