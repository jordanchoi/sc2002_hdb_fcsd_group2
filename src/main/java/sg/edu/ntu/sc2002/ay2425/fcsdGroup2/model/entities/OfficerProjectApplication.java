package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.util.List;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.OfficerProjectApplicationController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.AssignStatus;

public class OfficerProjectApplication {
    /*
    - officerAppId : int
    - officer : HDBOfficer
    - proj : BTOProj
    - assignmentStatus : AssignStatus

    + OfficerProjectApplication(officerAppId : int, officer : HDBOfficer, proj : BTOProj, status : AssignStatus)
    + setOfficerAppId(id : int) : void
    + getOfficerAppId() : int
    + setOfficer(officer : HDBOfficer) : void
    + getOfficer() : HDBOfficer
    + setProj(proj : BTOProj) : void
    + getProj() : BTOProj
    + setAssignmentStatus(status : AssignStatus) : void
    + getAssignmentStatus() : AssignStatus
     */
    private static int idCounter = 1;
    private int officerAppId;
    private HDBOfficer officer;
    private BTOProj proj;
    private AssignStatus assignmentStatus;

    // Constructor - Default
    public OfficerProjectApplication() {
        this.officerAppId = getId();
    }

    // Constructor - With parameters
    public OfficerProjectApplication(int officerAppId, HDBOfficer officer, BTOProj proj, AssignStatus assignmentStatus) {
        this.officerAppId = officerAppId;
        this.officer = officer;
        this.proj = proj;
        this.assignmentStatus = assignmentStatus;
    }

    // Constructor for auto-generating ID
    public OfficerProjectApplication(HDBOfficer officer, BTOProj proj, AssignStatus assignmentStatus) {
        this.officerAppId = getId(); // Auto-increment
        this.officer = officer;
        this.proj = proj;
        this.assignmentStatus = assignmentStatus;
    }

    public int getId() {
        int counter = 1;
        OfficerProjectApplicationController appController = new OfficerProjectApplicationController();
        List<OfficerProjectApplication> officerAppList = appController.getAllOfficerProjectApplications();
        for (OfficerProjectApplication officerApp : officerAppList) {
            counter++;
        }
        return counter;
    }

    public int getOfficerAppId() {
        return officerAppId;
    }

    public void setOfficerAppId(int id) {
        this.officerAppId = id;
    }

    public HDBOfficer getOfficer() {
        return officer;
    }

    public void setOfficer(HDBOfficer officer) {
        this.officer = officer;
    }

    public BTOProj getProj() {
        return proj;
    }

    public void setProj(BTOProj proj) {
        this.proj = proj;
    }

    public AssignStatus getAssignmentStatus() {
        return assignmentStatus;
    }

    public void setAssignmentStatus(AssignStatus status) {
        this.assignmentStatus = status;
    }
}
