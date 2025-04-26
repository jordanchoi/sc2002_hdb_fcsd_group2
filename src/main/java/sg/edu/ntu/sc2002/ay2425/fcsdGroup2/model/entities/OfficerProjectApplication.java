package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import java.util.List;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.OfficerProjectApplicationController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.AssignStatus;

/**
 * Represents an officer's application to be assigned to a BTO project.
 */
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

    /**
     * Default constructor. Auto-generates an ID.
     */
    public OfficerProjectApplication() {
        this.officerAppId = getId();
    }

    /**
     * Constructs an application with all fields specified.
     *
     * @param officerAppId application ID
     * @param officer officer who applied
     * @param proj project applied for
     * @param assignmentStatus assignment status
     */
    public OfficerProjectApplication(int officerAppId, HDBOfficer officer, BTOProj proj, AssignStatus assignmentStatus) {
        this.officerAppId = officerAppId;
        this.officer = officer;
        this.proj = proj;
        this.assignmentStatus = assignmentStatus;
    }

    /**
     * Constructs an application with auto-generated ID.
     *
     * @param officer officer who applied
     * @param proj project applied for
     * @param assignmentStatus assignment status
     */
    public OfficerProjectApplication(HDBOfficer officer, BTOProj proj, AssignStatus assignmentStatus) {
        this.officerAppId = getId(); // Auto-increment
        this.officer = officer;
        this.proj = proj;
        this.assignmentStatus = assignmentStatus;
    }

    /**
     * Generates the next application ID based on the number of existing applications.
     *
     * @return the generated application ID
     */
    public int getId() {
        int counter = 1;
        OfficerProjectApplicationController appController = new OfficerProjectApplicationController();
        List<OfficerProjectApplication> officerAppList = appController.getAllOfficerProjectApplications();
        for (OfficerProjectApplication officerApp : officerAppList) {
            counter++;
        }
        return counter;
    }

    /** @return the officer application ID. */
    public int getOfficerAppId() { return officerAppId; }

    /** Sets the officer application ID. */
    public void setOfficerAppId(int id) { this.officerAppId = id; }

    /** @return the officer who submitted the application. */
    public HDBOfficer getOfficer() { return officer; }

    /** Sets the officer who submitted the application. */
    public void setOfficer(HDBOfficer officer) { this.officer = officer; }

    /** @return the BTO project related to this application. */
    public BTOProj getProj() { return proj; }

    /** Sets the BTO project for the application. */
    public void setProj(BTOProj proj) { this.proj = proj; }

    /** @return the current assignment status. */
    public AssignStatus getAssignmentStatus() { return assignmentStatus; }

    /** Sets the assignment status of the application. */
    public void setAssignmentStatus(AssignStatus status) { this.assignmentStatus = status; }
}
