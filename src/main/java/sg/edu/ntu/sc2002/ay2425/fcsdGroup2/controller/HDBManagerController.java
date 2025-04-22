package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBOfficer;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.OfficerProjectApplication;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.AssignStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;

import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;

public class HDBManagerController {
    private List<OfficerProjectApplication> applications;
    private List<Application> allApplications;

    public HDBManagerController(List<OfficerProjectApplication> a) {
        this.applications = a;
    }

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

    public boolean approveApplication(Application app){
        // This method approves an application by setting its status to 'Successful'.
        // Returns true if the application is valid; false otherwise.
        if (app != null) {
            app.approve(); 
            return true;
        }
        return false;
    }

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
