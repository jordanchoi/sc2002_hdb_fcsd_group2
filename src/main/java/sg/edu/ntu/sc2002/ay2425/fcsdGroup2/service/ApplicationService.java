package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ApplicationStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.ApplicationRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;

import java.util.Comparator;
import java.util.List;

/**
 * Service class to handle application-related operations for HDB applicants.
 * Supports applying for projects, withdrawing, and viewing application details.
 */
public class ApplicationService {
    private final BTORepository btoRepo = BTORepository.getInstance();
    private final UserRepository userRepo = UserRepository.getInstance();
    private final ApplicationRepository applicationRepo = new ApplicationRepository(btoRepo, userRepo);

    /**
     * Submits a new application for an applicant to a given project.
     *
     * @param applicant the applicant
     * @param project the BTO project
     */
    public void applyForProject(HDBApplicant applicant, BTOProj project) {
        Application currentApp = applicant.getCurrentApplication();

        if (currentApp != null) {
            ApplicationStatus status = currentApp.getStatusEnum();

            if (status == ApplicationStatus.UNSUCCESSFUL || status == ApplicationStatus.WITHDRAWN) {
                int existingId = currentApp.getAppId();
                Application newApp = new Application(existingId, applicant, project);

                applicant.setCurrentApplication(newApp);
                applicationRepo.update(newApp);

                System.out.println("Application submitted successfully for project: " + project.getProjName());
                return;
            }

            System.out.println("You already have an ongoing application.");
            return;
        }

        // New application
        int newId = generateNextAppId();
        Application newApp = new Application(newId, applicant, project);
        applicant.setCurrentApplication(newApp);
        applicationRepo.add(newApp);
        System.out.println("Application submitted successfully for project: " + project.getProjName());
    }

    /**
     * Requests withdrawal of an existing application for an applicant.
     *
     * @param applicant the applicant
     */
    public void withdrawApplication(HDBApplicant applicant) {
        Application current = applicant.getCurrentApplication();
        if (current == null) {
            System.out.println("No application to withdraw.");
            return;
        }

        if (current.getStatusEnum() == ApplicationStatus.WITHDRAWN ||
                current.getStatusEnum() == ApplicationStatus.WITHDRAW_REQ) {
            System.out.println("Withdrawal already processed/requested.");
            return;
        }

        if (current.getStatusEnum() == ApplicationStatus.UNSUCCESSFUL) {
            System.out.println("Cannot withdraw an unsuccessful application.");
            return;
        }

        current.requestWithdrawal();
        applicationRepo.update(current);
        System.out.println("Withdrawal request submitted.");
    }

    /**
     * Views the details of an applicant's current application.
     *
     * @param applicant the applicant
     * @return string representation of the application details
     */
    public String viewApplicationDetails(HDBApplicant applicant) {
        Application app = applicant.getCurrentApplication();
        if (app == null) {
            return "No application found.";
        }

        BTOProj project = app.getAppliedProj();

        StringBuilder sb = new StringBuilder();
        sb.append("=== Application Details ===\n");
        sb.append("Application ID     : ").append(app.getAppId()).append("\n");
        sb.append("Status             : ").append(app.getStatus()).append("\n");

        sb.append("\n--- Project Info ---\n");
        sb.append("Project ID         : ").append(project.getProjId()).append("\n");
        sb.append("Project Name       : ").append(project.getProjName()).append("\n");
        sb.append("Neighbourhood      : ").append(project.getProjNbh()).append("\n");

        sb.append("\n--- Selected Flat ---\n");
        if (app.getFlat() != null) {
            sb.append("Flat: Block ").append(app.getFlat().getBlock().getBlkNo()).append(", ");
            sb.append(app.getFlat().getBlock().getStreetAddr()).append(", ");
            sb.append("Postal Code: ").append(app.getFlat().getBlock().getPostalCode()).append(", ");
            sb.append("Unit: ").append(app.getFlat().getFloorUnit()).append("\n");
        } else {
            sb.append("Chosen Flat        : N/A\n");
        }

        return sb.toString();
    }

    /**
     * Generates the next application ID based on existing applications.
     *
     * @return new application ID
     */
    private int generateNextAppId() {
        List<Application> applications = applicationRepo.getApplications();
        return applications.stream()
                .map(Application::getAppId)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;
    }
}
