package sg.edu.ntu.sc2002.ay2425.fcsdGroup2;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.*;

import java.util.List;

public class TestEnquiryRepository {
    public static void main(String[] args) {
        // Set up dependencies
        UserRepository userRepo = new UserRepository();       // make sure this loads test data
        BTORepository btoRepo = new BTORepository();           // make sure this loads test projects
        EnquiryRepository enquiryRepo = new EnquiryRepository(userRepo, btoRepo);

        // Get test user and project
        HDBApplicant applicant = (HDBApplicant) userRepo.getUserByNric("S1234567A").orElse(null);
        BTOProj project = btoRepo.getProjByName("MyProjectName");

        if (applicant == null || project == null) {
            System.out.println("Missing test data. Ensure user and project exist.");
            return;
        }

        // Create and add a new enquiry
        Enquiry newEnquiry = new Enquiry("Is there a playground?", applicant, project);
        newEnquiry.addMessage("When is the expected completion?", applicant);

        enquiryRepo.add(newEnquiry); // Should trigger saveToFile()

        // Reload repository and list all enquiries
        enquiryRepo = new EnquiryRepository(userRepo, btoRepo);
        List<Enquiry> all = enquiryRepo.getAll();

        System.out.println("Loaded Enquiries:");
        for (Enquiry e : all) {
            System.out.println("Enquiry #" + e.getEnquiryId() + " for Project: " + e.getForProj().getProjName());
            for (ProjectMessage m : e.getEnquiries()) {
                System.out.println(" - " + m);
            }
        }
    }
}
