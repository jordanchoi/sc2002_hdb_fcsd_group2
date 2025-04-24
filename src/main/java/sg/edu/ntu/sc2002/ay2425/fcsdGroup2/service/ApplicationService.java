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

public class ApplicationService {
    private final BTORepository btoRepo = BTORepository.getInstance();
    private final UserRepository userRepo = UserRepository.getInstance();
    private final ApplicationRepository applicationRepo = new ApplicationRepository(btoRepo, userRepo);

    public void applyForProject(HDBApplicant applicant, BTOProj project) {
        Application currentApp = applicant.getCurrentApplication();

        if (currentApp != null) {
            ApplicationStatus status = currentApp.getStatusEnum();

            // Only allow override if previous application is unsuccessful or withdrawn
            if (status == ApplicationStatus.UNSUCCESSFUL || status == ApplicationStatus.WITHDRAWN) {
                // Use same app ID to override
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

        current.requestWithdrawal();
        applicationRepo.update(current);
        System.out.println("Withdrawal request submitted.");
    }

    public String viewApplicationDetails(HDBApplicant applicant) {
        Application app = applicant.getCurrentApplication();
        if (app == null) {
            return "No application found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Application ID: ").append(app.getAppId()).append("\n");
        sb.append("Project: ").append(app.getAppliedProj().getProjName()).append("\n");
        sb.append("Status: ").append(app.getStatus()).append("\n");

        if (app.getFlat() != null) {
            sb.append("Flat: Block ").append(app.getFlat().getBlock().getBlkNo()).append(", ");
            sb.append(app.getFlat().getBlock().getStreetAddr()).append(", ");
            sb.append("Postal Code: ").append(app.getFlat().getBlock().getPostalCode()).append(", ");
            sb.append("Unit: ").append(app.getFlat().getFloorUnit()).append("\n");
        } else {
            sb.append("Chosen Flat: N/A\n");
        }
        return sb.toString();
    }

    private int generateNextAppId() {
        List<Application> applications = applicationRepo.getApplications();
        return applications.stream()
                .map(Application::getAppId)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;
    }
}
