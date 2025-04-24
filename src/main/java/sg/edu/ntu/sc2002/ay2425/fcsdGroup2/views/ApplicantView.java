package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.ApplicantController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.UserAuthController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;

import java.util.Scanner;

public class ApplicantView implements UserView {
    private final SessionStateManager session = SessionStateManager.getInstance();
    private final ApplicantController controller = new ApplicantController((HDBApplicant) session.getLoggedInUser());
    private final Scanner scanner;
    private final UserAuthController authController = UserAuthController.getInstance();

    public ApplicantView() {
        // Constructor logic if needed
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void start() {
        System.out.println("Welcome! Applicant " + session.getLoggedInUser().getFirstName() + "!\nYou are in the Applicant Main Menu.\n");
        int choice = 0;
        do {
            displayMenu();
            handleUserInput();
        } while (choice != 12);
    }


    @Override
    public void displayMenu() {
        System.out.println("\n=== Applicant Menu ===");
        System.out.println("1. View available BTO projects");
        System.out.println("2. Apply for a BTO project");
        System.out.println("3. View applied project status");
        System.out.println("4. Withdraw application");
        System.out.println("5. Submit enquiry");
        System.out.println("6. Add message to existing enquiry");
        System.out.println("7. View enquiries");
        System.out.println("8. Edit enquiries");
        System.out.println("9. Delete enquiry");
        System.out.println("10. Delete enquiry in existing enquiry");
        System.out.println("11. Change password");
        System.out.println("12. Logout");
    }

    @Override
    public int handleUserInput() {
        // placeholder return statement
        int choice = 0;
        System.out.print("Please select an option: ");
        choice = scanner.nextInt();
        switch (choice) {
            case 1 -> controller.viewEligibleProjects();
            case 2 -> controller.applyForProject();
            case 3 -> controller.showApplicantApplicationDetails();
            case 4 -> controller.withdrawApplication();
//            case 5 -> controller.submitEnquiry();
//            case 6 -> controller.submitExisting();
//            case 7 -> controller.showAllEnquiries();
//            case 8 -> controller.editEnquiryMessage();
//            case 9 -> controller.deleteEnquiry();
//            case 10 -> controller.deleteMessageInEnquiry();
            case 11 -> changePassword();
            case 12 -> System.out.println("Logging out...");
            default -> System.out.println("Invalid choice. Please try again.");
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

}
