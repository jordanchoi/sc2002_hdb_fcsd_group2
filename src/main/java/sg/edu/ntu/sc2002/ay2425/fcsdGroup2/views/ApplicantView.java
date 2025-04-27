package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.ApplicantController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.UserAuthController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.handlers.ApplicantViewHandler;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.handlers.ApplicantProjectViewHandler;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;

import java.util.*;


/**
 * Represents the view for an HDB applicant.
 * Allows the applicant to interact with various services such as viewing BTO projects, managing applications, submitting enquiries, and changing passwords.
 */
public class ApplicantView implements UserView {
    private final SessionStateManager session = SessionStateManager.getInstance();
    private final ApplicantController controller = new ApplicantController((HDBApplicant) session.getLoggedInUser());
    private final Scanner scanner;
    private final UserAuthController authController = UserAuthController.getInstance();
    private final ApplicantProjectViewHandler projectViewHandler = new ApplicantProjectViewHandler(controller);

    /**
     * Constructs a new ApplicantView and initializes the scanner.
     */
    public ApplicantView() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the Applicant View interface.
     * Displays the main menu and handles user interaction.
     */
    @Override
    public void start() {
        System.out.println("Welcome! Applicant " + session.getLoggedInUser().getFirstName() + "!\nYou are in the Applicant Main Menu.\n");
        int choice;
        do {
            displayMenu();
            choice = handleUserInput();
        } while (choice != 5);
    }

    /**
     * Displays the main applicant menu options.
     */
    @Override
    public void displayMenu() {
        System.out.println("\n=== Applicant Menu ===");
        System.out.print("1. View available BTO projects ");
        projectViewHandler.displayActiveFilters();
        System.out.println();
        System.out.println("2. BTO Application Services");
        System.out.println("3. Enquiry Services");
        System.out.println("4. Change password");
        System.out.println("5. Logout");
    }

    /**
     * Handles the user's input for the main menu options.
     *
     * @return the choice selected by the user
     */
    @Override
    public int handleUserInput() {
        int choice = getIntInput("Please select an option: ");

        switch (choice) {
            case 1 -> {
                projectViewHandler.viewEligibleProjects();
                projectViewHandler.askToChangeFilters();
            }
            case 2 -> handleBTOApplicationMenu();
            case 3 -> new ApplicantViewHandler((HDBApplicant) session.getLoggedInUser(), controller).displayEnquiryOptions();
            case 4 -> changePassword();
            case 5 -> System.out.println("Logging out...");
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return choice;
    }


    /**
     * Displays and handles the BTO Application Services menu.
     */
    private void handleBTOApplicationMenu() {
        int choice;
        do {
            System.out.println("\n=== BTO Application Services ===");
            System.out.println("1. Apply for a BTO project");
            System.out.println("2. View applied project status");
            System.out.println("3. Withdraw application");
            System.out.println("4. Back to main menu");

            choice = getIntInput("Please select a BTO application option: ");

            switch (choice) {
                case 1 -> controller.applyForProject();
                case 2 -> controller.showApplicantApplicationDetails();
                case 3 -> controller.withdrawApplication();
                case 4 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 4);
    }

    /**
     * Allows the user to change their password.
     * User has up to 3 attempts to enter the correct current password.
     */
    public void changePassword() {
        int attempts = 3;
        do {
            System.out.println("You have " + attempts + " attempts left to change your password.");
            System.out.print("Please enter your current password: ");
            String currentPassword = scanner.next();

            if (!authController.validatePassword(currentPassword)) {
                System.out.println("Current password is incorrect. Please try again.");
                attempts--;
            } else {
                System.out.print("Please enter your new password: ");
                String newPassword = scanner.next();
                if (authController.changePassword(currentPassword, newPassword)) {
                    System.out.println("Password changed successfully!");
                } else {
                    System.out.println("Failed to change password.");
                }
                break;
            }
        } while (attempts > 0);
    }

    /**
     * Prompts the user for an integer input and validates the input.
     *
     * @param prompt the prompt message to display
     * @return the valid integer input from the user
     */
    private int getIntInput(String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }
}
