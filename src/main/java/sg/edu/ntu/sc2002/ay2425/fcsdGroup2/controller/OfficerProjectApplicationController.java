package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.OfficerProjectApplication;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.AssignStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.ProjectApplicationRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;

import java.util.List;

/**
 * Controller class for managing officer project applications.
 * Handles adding, updating, deleting, and retrieving officer project applications.
 */
public class OfficerProjectApplicationController {
    private final UserRepository userRepo = UserRepository.getInstance();
    private final BTORepository btoRepo = BTORepository.getInstance();
    private final ProjectApplicationRepository projAppRepo = new ProjectApplicationRepository(userRepo, btoRepo);

    /** Default constructor. */
    public OfficerProjectApplicationController() {}

    /**
     * Processes an officer project application decision (approve or reject).
     *
     * @param application the application to process
     * @param approve true to approve, false to reject
     * @return true if processed successfully
     */
    public boolean processOfficerDecision(OfficerProjectApplication application, boolean approve) {
        if (application == null) return false;

        AssignStatus newStatus = approve ? AssignStatus.ASSIGNED : AssignStatus.REJECTED;
        application.setAssignmentStatus(newStatus);
        projAppRepo.update(application);

        return true;
    }

    /**
     * Retrieves all officer project applications for a specific project.
     *
     * @param projectId the project ID
     * @return list of officer applications
     */
    public List<OfficerProjectApplication> getApplicationsByProjectId(int projectId) {
        return projAppRepo.getByProjectId(projectId);
    }

    /**
     * Retrieves all officer project applications for a specific officer by NRIC.
     *
     * @param nric officer's NRIC
     * @return list of applications
     */
    public List<OfficerProjectApplication> getApplicationsByOfficer(String nric) {
        return projAppRepo.getByOfficerNric(nric);
    }

    /**
     * Adds a new officer project application to repository.
     *
     * @param application the officer project application
     */
    public void addOfficerProjectApplication(OfficerProjectApplication application) {
        projAppRepo.add(application);
    }

    /**
     * Deletes an officer project application by ID.
     *
     * @param appId the application ID
     */
    public void deleteOfficerProjectApplication(int appId) {
        projAppRepo.delete(appId);
    }

    /**
     * Retrieves all officer project applications.
     *
     * @return list of all applications
     */
    public List<OfficerProjectApplication> getAllOfficerProjectApplications() {
        return projAppRepo.getAll();
    }
}
