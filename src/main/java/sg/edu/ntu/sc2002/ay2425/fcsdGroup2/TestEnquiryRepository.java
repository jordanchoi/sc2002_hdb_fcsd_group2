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

        userRepo.getAllUsers().forEach(u -> System.out.println(u.getNric() + " - " + u.getFirstName()));
        btoRepo.getAllProjects().forEach(p -> System.out.println(p.getProjName()));

        // Get test user and project
        HDBApplicant applicant = (HDBApplicant) userRepo.getUserByNric("S1234567A").orElse(null);
        BTOProj project = btoRepo.getProjByName("Bukit Merah Ridges");

        if (applicant == null || project == null) {
            System.out.println("Missing test data. Ensure user and project exist.");
            return;
        }

        /*
        // Create and add a new enquiry
        String msg = "Test Test Is there a playground?";
        Enquiry newEnquiry = enquiryRepo.add(msg, applicant, project);
        if (!enquiryRepo.enquiryExists(applicant, project, msg)) {
            System.out.println("New enquiry added.");
        } else {
            System.out.println("Duplicate enquiry detected — not added.");
        }
        //enquiryRepo.add(newEnquiry);
*/

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
