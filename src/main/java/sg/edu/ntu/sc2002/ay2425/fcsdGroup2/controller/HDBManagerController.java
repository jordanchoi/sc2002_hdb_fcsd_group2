package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBOfficer;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.OfficerProjectApplication;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.AssignStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
/**
 * Controller class for managing HDB Manager operations.
 * Handles officer registration approvals and application approvals.
 */

public class HDBManagerController {
    private final List<OfficerProjectApplication> applications;
    private List<Application> allApplications;

    /**
     * Constructs the HDBManagerController.
     *
     * @param a list of officer project applications
     */
    public HDBManagerController(List<OfficerProjectApplication> a) {
        this.applications = a;
    }

    /**
     * Approves an officer's application for a project.
     *
     * @param officer the officer applying
     * @param project the project applied for
     * @return true if approval successful
     */
    public boolean approveOfficerRegistration(HDBOfficer officer, BTOProj project){
        // This method approves an officer's application for a specific project
        // by setting their application status to Assign.
        for (OfficerProjectApplication app : applications) {
            if (app.getProj().equals(project) && app.getOfficer().equals(officer)) {
                app.setAssignmentStatus(AssignStatus.ASSIGNED);
                return true;
            }  
        }
        return false;
    }

    /**
     * Rejects an officer's application for a project.
     *
     * @param officer the officer applying
     * @param project the project applied for
     * @return true if rejection successful
     */
    public boolean rejectOfficerRegistration(HDBOfficer officer, BTOProj project){
        // This method rejects an officer's application for a specific project
        // by setting their application status to Rejected.
        for (OfficerProjectApplication app : applications) {
            if (app.getProj().equals(project) && app.getOfficer().equals(officer)) {
                app.setAssignmentStatus(AssignStatus.REJECTED);
                return true;
            }
        }
        return false;
    }

    /**
     * Approves an applicant's BTO application.
     *
     * @param app the application to approve
     * @return true if approval successful
     */
    public boolean approveApplication(Application app){
        // This method approves an application by setting its status to 'Successful'.
        // Returns true if the application is valid; false otherwise.
        if (app != null) {
            app.approve(); 
            return true;
        }
        return false;
    }

    /**
     * Rejects an applicant's BTO application.
     *
     * @param app the application to reject
     * @return true if rejection successful
     */
    public boolean rejectApplication(Application app){
        // This method rejects an application by setting its status to 'Unsuccessful'.
        // Returns true if the application is valid; false otherwise.
        if (app != null) {
            app.reject();
            return true;
        }
        return false;
    }
}
