package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ApplicationStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.Neighbourhoods;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.UserRoles;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
/**
 * Controller class for managing BTO project creation, editing, deletion, and visibility toggling.
 * Also provides project filtering and officer application handling functionalities.
 */
public class BTOProjsController {
    private List<BTOProj> projects = new ArrayList<>();
    private List<HDBManager> managers;
    private List<Enquiry> enquiries;
    private final BTORepository btoRepo = BTORepository.getInstance();

    /** Default constructor. */
    public BTOProjsController() {
        this.managers = new ArrayList<>();
    }

    /**
     * Full constructor for project controller.
     *
     * @param p list of BTO projects
     * @param m list of HDB managers
     * @param e list of enquiries
     */
    public BTOProjsController(List<BTOProj> p, List<HDBManager> m, List<Enquiry> e) {
        this.projects = p;
        this.managers = m;
        this.enquiries = e;
    }

    /**
     * Creates a new BTO project and adds it to the repository.
     */
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
            HDBOfficer[] officers,
            String postalCode) {

        BTOProj proj = new BTOProj(id, name, appOpenDate, appCloseDate, isVisible);

        proj.setProjNbh(nbh);
        proj.setManagerIC(manager);
        proj.setOfficerSlots(officerSlots);
        proj.setOfficersList(officers);
        proj.setPostalCode(postalCode);

        for (Map.Entry<FlatTypes, FlatType> entry : flatUnits.entrySet()) {
            FlatTypes type = entry.getKey();
            FlatType ft = entry.getValue();
            proj.addFlatTypeWithPrice(type, ft.getTotalUnits(), ft.getSellingPrice());
        }

        if (!managers.contains(manager)) {
            managers.add(manager);
        }
        manager.getCurrentProj().add(proj);
        btoRepo.addProject(proj);
        return proj;
    }

    /**
     * Edits an existing BTO project.
     */
    public boolean editProj(BTOProj proj,
                                 String newName,
                                 Neighbourhoods newNbh,
                                 LocalDateTime newOpen,
                                 LocalDateTime newClose,
                                 int newSlots,
                                 Map<String, FlatType> updatedFlatTypes) {
        if (proj == null) return false;

        proj.setProjName(newName);
        proj.setProjNbh(newNbh);
        proj.setApplicationOpenDate(newOpen);
        proj.setAppCloseDate(newClose);
        proj.setOfficerSlots(newSlots);

        // Apply flat type edits
        if (updatedFlatTypes != null) {
            List<FlatType> flatList = proj.getAvailableFlatTypes();
            for (FlatType ft : flatList) {
                FlatType updated = updatedFlatTypes.get(ft.getTypeName());
                if (updated != null) {
                    ft.setTotalUnits(updated.getTotalUnits());
                    ft.setSellingPrice(updated.getSellingPrice());
                }
            }
        }
        btoRepo.saveProject();
        return true;
    }

    /**
     * Deletes a BTO project based on ID.
     */
    public boolean deleteProjId(int id) {
        List<BTOProj> repoProj = btoRepo.getAllProjects();
        Iterator<BTOProj> iterator = repoProj.iterator();
        boolean found = false;

        while (iterator.hasNext()) {
            BTOProj proj = iterator.next();
            if (proj.getProjId() == id) {
                iterator.remove();
                found = true;
                break;
            }
        }
        if (found) {
            btoRepo.saveProject();
        }
        return found;
    }

    /**
     * Toggles visibility status of a BTO project by ID.
     */
    public boolean toggleVisibilityById(int id) {
        for (BTOProj proj : viewAllProjs()) {
            if (proj.getProjId() == id) {
                proj.setVisibility(!proj.getVisibility());
                return true;
            }
        }
        return false;
    }

    /**
     * Toggles visibility for a given project and saves.
     */
    public void toggleProjVisibility(BTOProj proj) {
        proj.setVisibility(!proj.getVisibility());
        btoRepo.saveProject(); // make sure this updates the file
    }

    /**
     * Retrieves all projects in memory.
     */
    public List<BTOProj> viewAllProjs() {
         return new ArrayList<>(projects); // Return a copy to avoid direct modification
    }

    /**
     * Retrieves all projects created by all managers.
     */
    public List<BTOProj> viewProjsByAllManagers() {
        List<BTOProj> result = new ArrayList<>();
        for (HDBManager manager : managers) {
            for (BTOProj proj : manager.getCurrentProj()) {
                result.add(proj);
            }
        }
        return result;
    }

    /**
     * Retrieves only projects created by the logged-in manager.
     */
    public List<BTOProj> viewOwnProjs() {
        SessionStateManager session = SessionStateManager.getInstance();

        if (session.getLoggedInUserType() != UserRoles.MANAGER) {
            return Collections.emptyList();
        }

        HDBManager currentManager = (HDBManager) session.getLoggedInUser();
        String loggedManagerName = currentManager.getFirstName();

        List<BTOProj> myProjects = new ArrayList<>();
        for (BTOProj project : projects) {
            if (project.getManagerIC() != null &&
                    project.getManagerIC().getFirstName().equalsIgnoreCase(loggedManagerName)) {
                myProjects.add(project);
            }
        }

        return myProjects;
    }

    /**
     * Retrieves projects handled by the logged-in officer.
     */
    public List<BTOProj> viewOwnProjsOfficer() {
        SessionStateManager session = SessionStateManager.getInstance();

        // Ensure the logged-in user is an officer
        if (session.getLoggedInUserType() != UserRoles.OFFICER) {
            return Collections.emptyList();
        }

        HDBOfficer currentOfficer = (HDBOfficer) session.getLoggedInUser();
        String officerNric = currentOfficer.getNric();

        List<BTOProj> officerProjects = new ArrayList<>();
        for (BTOProj project : projects) {
            if (project.getOfficersList() != null) {
                for (HDBOfficer officer : project.getOfficersList()) {
                    if (officer.getNric().equalsIgnoreCase(officerNric)) {
                        officerProjects.add(project);
                        break;
                    }
                }
            }
        }

        return officerProjects;
    }

    public void approveOfficerApplication(OfficerProjectApplication app, BTOProj proj) {

    }

    public void rejectOfficerApplication(OfficerProjectApplication app) {

    }

    public void processApplicantDecision(Application app, BTOProj proj, boolean approve) {

    }

    /**
     * Retrieves all enquiries.
     */
    public List<Enquiry> getAllEnq() {
        return enquiries;
    }

    /* Commented out by Jordan because this is not implemented yet
    public void replyEnq(int enquiryId, String string){
        for(Enquiry enquiry : enquiries){
            if(enquiry.getEnquiryId() == enquiryId){
                enquiry.setReply(string);
            }
        }
    }
     */

    // Not completed
    public void generateReport() {

    }

    /**
     * Checks if a manager is available to handle a new project based on time overlap.
     */
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

    /**
     * Checks if a project ID is unique across projects.
     */
    public boolean isProjectIdUnique(int id) {
        for (BTOProj proj : viewAllProjs()) {
            if (proj.getProjId() == id) {
                return false;
            }
        }
        return true;
    }

    /**
     * Inserts projects from the BTO repository into memory.
     */
    public void insertProjectsFromRepo() {
        projects.clear();
        List<BTOProj> repoProjects = btoRepo.getAllProjects();
        for (BTOProj proj : repoProjects) {
            addProject(proj);
        }
    }

    /**
     * Adds a project to memory.
     */
    public void addProject(BTOProj project) {
        projects.add(project);
    }
}

