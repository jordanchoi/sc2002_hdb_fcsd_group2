package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.Neighbourhoods;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.UserRoles;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;

public class BTOProjsController {
    private List<BTOProj> projects = new ArrayList<>();
    private List<HDBManager> managers;
    private List<Enquiry> enquiries;
    public BTOProjsController() {}
    private BTORepository btoRepo = new BTORepository();
    // Create a new exercise

    public BTOProjsController(List<BTOProj> p, List<HDBManager> m, List<Enquiry> e) {
        this.projects = p;
        this.managers = m;
        this.enquiries = e;
    }

    public BTOProj CreateProj(
            int id,
            String name,
            Neighbourhoods nbh,
            Map<FlatTypes, FlatType> flatUnits,
            LocalDateTime appOpenDate,
            LocalDateTime appCloseDate,
            boolean isVisible,
            HDBManager manager,
            int officerSlots,
            HDBOfficer[] officers
    ) {
        // Create base project
        BTOProj proj = new BTOProj(id, name, appOpenDate, appCloseDate, isVisible);

        // Set core attributes
        proj.setProjNbh(nbh);
        proj.setManagerIC(manager);
        proj.setOfficerSlots(officerSlots);
        proj.setOfficersList(officers);

        // Add flat types to internal map
        for (Map.Entry<FlatTypes, FlatType> entry : flatUnits.entrySet()) {
            FlatTypes type = entry.getKey();
            FlatType ft = entry.getValue();
            proj.addFlatTypeWithPrice(type, ft.getTotalUnits(), ft.getSellingPrice());
        }

        // Save into memory and persist
        btoRepo.addProject(proj);  // this calls saveProject()
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

    public boolean toggleVisibilityById(int id) {
        for (BTOProj proj : viewAllProjs()) {
            if (proj.getProjId() == id) {
                proj.setVisibility(!proj.getVisibility());
                return true; // toggled successfully
            }
        }
        return false; // project not found
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

    public boolean isManagerAvailable(HDBManager manager, LocalDateTime newOpen, LocalDateTime newClose, List<BTOExercise> allExercises) {
        for (BTOExercise ex : allExercises) {
            for (BTOProj proj : ex.getExerciseProjs()) {
                if (proj.getManagerIC() == manager) {
                    LocalDateTime existingOpen = proj.getAppOpenDate();
                    LocalDateTime existingClose = proj.getAppCloseDate();
                    boolean overlaps = !newClose.isBefore(existingOpen) && !newOpen.isAfter(existingClose);
                    if (overlaps) return false;
                }
            }
        }
        return true;
    }

    public boolean isProjectIdUnique(int id) {
        for (BTOProj proj : viewAllProjs()) {
            if (proj.getProjId() == id) {
                return false;
            }
        }
        return true;
    }

    public void insertProjectsFromRepo() {
        projects.clear();
        List<BTOProj> repoProjects = btoRepo.getAllProjects();
        for (BTOProj proj : repoProjects) {
            addProject(proj);
        }
    }

    public void addProject(BTOProj project) {
        projects.add(project);
    }

    public List<BTOProj> getProjectsByManagerName(String name) {
        return viewAllProjs().stream()
                .filter(p -> p.getManagerIC() != null && p.getManagerIC().getFirstName() == name)
                .collect(Collectors.toList());
    }
}

