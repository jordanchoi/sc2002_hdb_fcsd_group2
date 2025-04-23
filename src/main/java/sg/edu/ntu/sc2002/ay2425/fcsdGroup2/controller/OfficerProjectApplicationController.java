package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.OfficerProjectApplication;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.AssignStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.ProjectApplicationRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;

import java.util.List;

public class OfficerProjectApplicationController {
    private final UserRepository userRepo = UserRepository.getInstance();
    private final BTORepository btoRepo = BTORepository.getInstance();
    private final ProjectApplicationRepository projAppRepo = new ProjectApplicationRepository(userRepo, btoRepo);
    public OfficerProjectApplicationController() {}


    public boolean processOfficerDecision(OfficerProjectApplication application, boolean approve) {
        if (application == null) return false;

        AssignStatus newStatus = approve ? AssignStatus.ASSIGNED : AssignStatus.REJECTED;
        application.setAssignmentStatus(newStatus);
        projAppRepo.update(application);

        return true;
    }

    public List<OfficerProjectApplication> getApplicationsByProjectId(int projectId) {
        return projAppRepo.getByProjectId(projectId);
    }

    public List<OfficerProjectApplication> getApplicationsByOfficer(String nric) {
        return projAppRepo.getByOfficerNric(nric);
    }

    public void addOfficerProjectApplication(OfficerProjectApplication application) {
        projAppRepo.add(application);
    }

    public void deleteOfficerProjectApplication(int appId) {
        projAppRepo.delete(appId);
    }

    public List<OfficerProjectApplication> getAllOfficerProjectApplications() {
        return projAppRepo.getAll();
    }
}