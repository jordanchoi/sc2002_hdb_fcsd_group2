package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.ApplicationRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;

public class HDBApplicant extends User {

    private Application currentApplication;

    // Lazy-loaded to avoid circular initialization errors
    private BTORepository btoRepo;
    private UserRepository userRepo;
    private ApplicationRepository applicationRepo;

    public HDBApplicant(String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
    }

    public Application getCurrentApplication() {
        if (currentApplication != null) return currentApplication;

        // Lazy load repositories
        if (btoRepo == null) btoRepo = BTORepository.getInstance();
        if (userRepo == null) userRepo = UserRepository.getInstance();
        if (applicationRepo == null) applicationRepo = new ApplicationRepository(btoRepo, userRepo);

        // Attempt to fetch and cache the current application
        currentApplication = applicationRepo.getApplicationByNric(this.nric);

        return currentApplication;
    }

    public void setCurrentApplication(Application currentApplication) {
        this.currentApplication = currentApplication;
    }

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
