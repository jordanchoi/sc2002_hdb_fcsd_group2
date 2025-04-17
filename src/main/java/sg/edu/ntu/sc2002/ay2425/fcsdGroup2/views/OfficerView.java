package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.UserAuthController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;
import java.util.Scanner;

public class OfficerView implements UserView {
    SessionStateManager session = SessionStateManager.getInstance();
    UserAuthController controller = UserAuthController.getInstance();
    public OfficerView() {}

    public void main() {
        System.out.println("You are logged in as an officer.");
        System.out.println("Welcome! Officer " + session.getLoggedInUser().getFirstName());
        int choice == 0;
        while (choice != 9) {
            displayMenu();
            choice = handleUserInput();
        }
    }

    public int displayMenu() {
        System.out.println("Officer Menu:");
        System.out.println("1. Open Applicant Menu");
        System.out.println("2. Apply Project as Officer");
        System.out.println("3. Check Project Registration Status");
        System.out.println("4. View Project Details");
        System.out.println("5. Open Enquiry/Booking Menu");
        System.out.println("9. Exit");
    }

    public int handleUserInput() {
        System.out.print("Enter your choice: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
                case 1:
                    ApplicantView applicantView = new ApplicantView();
                    break;
                case 2:
                    System.out.print("Enter the project name to apply for as officer: ");
                    Scanner scanner = new Scanner(System.in);
                    String projName = scanner.nextLine();
                    HDBOfficerController.applyProjOfficer(projName);
                    break;
                case 3:
                    System.out.print("Enter the project name to check registration status: ");
                    String regProjName = scanner.nextLine();
                    String status = HDBofficerController.projRegStatus(regProjName);
                    System.out.println("Registration status for project \"" + regProjName + "\": " + status);
                    break;
                case 4:
                    System.out.print("Enter the project name to view details: ");
                    String detailProjName = scanner.nextLine();
                    BTOProj foundProj = null;
                    for (BTOProj proj : btoRepository.getAllProjs()) {
                        if (proj.getProjName().equalsIgnoreCase(detailProjName)) {
                            foundProj = proj;
                            break;
                        }
                    }
                    if (bookProj == null) {
                        System.out.println("Project not found.");
                    }
                    break;
                case 5:
                    BookingView bookingView = new BookingView();
                    break;
                case 9:
                    break;
            }
        }
    }
}
