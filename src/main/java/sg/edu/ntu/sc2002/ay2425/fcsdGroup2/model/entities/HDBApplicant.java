package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.ApplicationRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;

import java.util.ArrayList;

/**
 * Represents an HDB Applicant user.
 * Can apply for BTO projects, view application status, manage enquiries, and modify password.
 */
public class HDBApplicant extends User {

    int applicantId;
    private Application currentApplication;
    // Lazy-loaded to avoid circular initialization errors
    private BTORepository btoRepo;
    private UserRepository userRepo;
    private ApplicationRepository applicationRepo;

    /**
     * Constructs an HDBApplicant with basic details.
     */
    public HDBApplicant(String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
    }

    /**
     * Constructs an HDBApplicant with an applicant ID.
     */
    public HDBApplicant(int applicantId, String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.applicantId = 0;
    }

    /**
     * Retrieves the current application submitted by this applicant.
     *
     * @return the Application object or null if none
     */
    public Application getCurrentApplication() {
        if (btoRepo == null) btoRepo = BTORepository.getInstance();
        if (userRepo == null) userRepo = UserRepository.getInstance();
        if (applicationRepo == null) applicationRepo = new ApplicationRepository(btoRepo, userRepo);
        return applicationRepo.getApplicationByNric(this.nric);
    }

    /**
     * Sets the current application object manually.
     *
     * @param currentApplication the application to set
     */
    public void setCurrentApplication(Application currentApplication) {
        this.currentApplication = currentApplication;
    }

    /**
     * Displays the main menu for an applicant user.
     */
    @Override
    public void viewMenu() {
        System.out.println("\n=== Applicant Menu ===");
        System.out.println("1. View available BTO projects");
        System.out.println("2. Apply for a BTO project");
        System.out.println("3. View applied project status");
        System.out.println("4. Withdraw application");
        System.out.println("5. Submit enquiry");
        System.out.println("6. View/Edit/Delete enquiries");
        System.out.println("7. Change password");
        System.out.println("8. Logout");
    }
}
