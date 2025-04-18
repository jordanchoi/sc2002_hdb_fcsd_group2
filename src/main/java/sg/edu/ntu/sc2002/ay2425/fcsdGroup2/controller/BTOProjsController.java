package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Enquiry;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.OfficerProjectApplication;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.UserRoles;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;

public class BTOProjsController {
    private List<BTOProj> projects;
    private List<HDBManager> managers;
    private List<Enquiry> enquiries;

    public BTOProjsController() {
        this.projects = new ArrayList<>(); // ✅ this prevents the crash
    }

    public BTOProjsController(List<BTOProj> p, List<HDBManager> m, List<Enquiry> e) {
        this.projects = p;
        this.managers = m;
        this.enquiries = e;
    }

    public BTOProj CreateProj(int id, String name, LocalDateTime appOpenDate, LocalDateTime appCloseDate, boolean isVisible) {
        // This method handles full creation of a BTO project.
        // It initializes core project details and sets up empty lists for related entities.
        BTOProj proj = new BTOProj(id, name, appOpenDate, appCloseDate, isVisible);
        projects.add(proj);
        return proj;
    }

    public BTOProj editProj(int btoProjId) {
        // This method handles editing of an existing BTO project by its ID.
        // It locates the project and updates its visibility status as an example.
        // This supports managing project visibility in the BTO system.
        for (BTOProj proj : projects) {
            if (proj.getProjId() == btoProjId) {
                proj.setVisibility(!proj.getVisibility());
                return proj;
            }
        }
        return null; 
    }

    public boolean deleteProj(int btoProjId) {
        // This method handles deletion of a BTO project by its ID.
        // It searches the project list and removes the matching project directly.
        // This supports project removal from the system for cleanup or archiving.
        for (int i = 0; i < projects.size(); i++) {
            if (projects.get(i).getProjId() == btoProjId) {
                projects.remove(i);
                return true;
            }
        }
        return false; // Project not found
    }

    public void toggleProjVisibility(int btoProjId, boolean visible) {
        // This method updates the visibility status of a BTO project based on its ID.
        // It searches the list and sets the visibility if the project is found.
        for (BTOProj proj : projects) {
            if (proj.getProjId() == btoProjId) {
                proj.setVisibility(visible);
                break;
            }
        }
    }

    public void toggleProjVisibility(BTOProj proj) {
        // This method toggles the visibility of a BTO project object.
        // It inverts the current visibility status of the given project.
        proj.setVisibility(!proj.getVisibility());
    }

    public List<BTOProj> viewAllProjs() {
        // This method returns all BTO projects stored in the controller.
        // It provides access to the full list of project records.
        return new ArrayList<>(projects); // Return a copy to avoid direct modification
    }

    public List<BTOProj> viewProjsByAllManagers() {
        // This method returns a list of all BTO projects managed by every manager in the system.
        // It loops through each manager and each of their managed projects, adding them one by one.
        List<BTOProj> result = new ArrayList<>();
        for (HDBManager manager : managers) {
            for (BTOProj proj : manager.getCurrentProj()) {
                result.add(proj);
            }
        }
        return result;
    }

    public List<BTOProj> viewOwnProjs() {
        SessionStateManager session = SessionStateManager.getInstance();
        if (session.getLoggedInUserType() == UserRoles.MANAGER) {
            HDBManager manager = (HDBManager) session.getLoggedInUser();
            int managerId = manager.getManagerId();
            for (HDBManager m : managers) {
                if (m.getManagerId() == managerId){
                    return m.getCurrentProj();
                }
            }
        }
        return null;
    }

    // Officer - Not completed
    public List<OfficerProjectApplication> getApplicationsFromProjs(){
        return null;
    }

    // Officer - Not completed
    public boolean addOfficersToProj() {
        return true;
    }

    // Officer - Not completed
    public boolean updateBTOApplicationStatus(boolean outcome, BTOProj proj) {
        return true;
    }

    public List<Enquiry> getAllEnq() {
        // This method returns the full list of all enquiries.
        // It allows external access to all recorded enquiry objects.
        return enquiries;
    }

    public void replyEnq(int enquiryId, String string){
        // This method replies to an enquiry based on an input string format.
        for(Enquiry enquiry : enquiries){
            if(enquiry.getEnquiryId() == enquiryId){
                enquiry.setReply(string);
            }
        }
    }

    // Not completed
    public void generateReport() {

    }
}

