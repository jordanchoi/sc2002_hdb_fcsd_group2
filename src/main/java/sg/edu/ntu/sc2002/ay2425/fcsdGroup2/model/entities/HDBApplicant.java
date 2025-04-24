package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.ApplicationRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;

import java.util.List;

public class HDBApplicant extends User {
    private Application currentApplication;
    private final BTORepository btoRepo = new BTORepository();
    private final UserRepository userRepo = new UserRepository();
    private final ApplicationRepository applicationRepo = new ApplicationRepository(btoRepo, userRepo);

    public HDBApplicant(String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
    }

    public Application getCurrentApplication() {
        return applicationRepo.getApplicationByNric(this.nric);
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

    // Optional toString (commented out or edit for debugging)
    /*
    @Override
    public String toString() {
        return "HDBApplicant{" +
                "currentApplication=" + currentApplication +
                ", name='" + getName() + '\'' +
                ", nric='" + getNric() + '\'' +
                ", age=" + getAge() +
                ", maritalStatus=" + getMaritalStatus() +
                '}';
    }
    */
}
