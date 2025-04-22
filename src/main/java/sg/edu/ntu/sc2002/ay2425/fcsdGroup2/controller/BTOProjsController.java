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

public class BTOProjsController {
    private List<BTOProj> projects = new ArrayList<>();
    private List<Application> applications = new ArrayList<>();
    private List<HDBManager> managers;
    private List<Enquiry> enquiries;
    private final BTORepository btoRepo = BTORepository.getInstance();

    public BTOProjsController() {
        this.managers = new ArrayList<>();
    }

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
            HDBOfficer[] officers) {

        BTOProj proj = new BTOProj(id, name, appOpenDate, appCloseDate, isVisible);

        proj.setProjNbh(nbh);
        proj.setManagerIC(manager);
        proj.setOfficerSlots(officerSlots);
        proj.setOfficersList(officers);

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

    public boolean toggleVisibilityById(int id) {
        for (BTOProj proj : viewAllProjs()) {
            if (proj.getProjId() == id) {
                proj.setVisibility(!proj.getVisibility());
                return true;
            }
        }
        return false;
    }

    public void toggleProjVisibility(BTOProj proj) {
        proj.setVisibility(!proj.getVisibility());
        btoRepo.saveProject(); // make sure this updates the file
    }

    public List<BTOProj> viewAllProjs() {
         return new ArrayList<>(projects); // Return a copy to avoid direct modification
    }

    public List<BTOProj> viewProjsByAllManagers() {
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

    public void approveOfficerApplication(OfficerProjectApplication app, BTOProj proj) {

    }

    public void rejectOfficerApplication(OfficerProjectApplication app) {

    }

    public void processApplicantDecision(Application app, BTOProj proj, boolean approve) {

    }

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
}

