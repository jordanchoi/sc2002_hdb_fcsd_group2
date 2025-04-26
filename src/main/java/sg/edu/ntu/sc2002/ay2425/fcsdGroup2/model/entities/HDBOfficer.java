package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
//import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces.canApplyFlat;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
/**
 * Represents an HDB Officer who handles assigned BTO projects and officer applications.
 */
public class HDBOfficer extends HDBApplicant {
    private int officerId;
    private List<BTOProj> projectsHandled;
    private List<OfficerProjectApplication> registrationApps;

    /**
     * Constructs an HDB Officer with basic applicant information.
     */
    public HDBOfficer(String firstName, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(firstName, nric, age, maritalStatus, password);
        this.projectsHandled = new ArrayList<>();
        this.registrationApps = new ArrayList<>();
    }

    /**
     * Constructs an HDB Officer with an officer ID and basic details.
     */
    public HDBOfficer(int officerId, String name, String nric, int age, MaritalStatus maritalStatus, String password) {
        super(name, nric, age, maritalStatus, password);
        this.projectsHandled = new ArrayList<>();
        this.registrationApps = new ArrayList<>();
        this.officerId = 0;
    }

    /*@Override
    public boolean checkEligibility(String proj) {
        boolean appliedAsApplicant = proj.getAllApps().stream().anyMatch(app -> app.getApplicant().getUserId() == this.getUserId());
        if (appliedAsApplicant) {return false;}

        LocalDateTime startNew = proj.getAppOpenDate();
        LocalDateTime endNew   = proj.getAppCloseDate();
        for (BTOProj handled : projectsHandled) {
            LocalDateTime startOld = handled.getAppOpenDate();
            LocalDateTime endOld   = handled.getAppCloseDate();

            // overlap iff NOT (new ends before old starts OR new starts after old ends)
            boolean overlap = !( endNew.isBefore(startOld) || startNew.isAfter(endOld) );

            if (overlap) {return false;}
        }
        // passed both checks!
        return true;
    }

    @Override
    public boolean submitApplication(String projName) {
        if (!checkEligibility(projName)) return false;

        BTOProj newproj = null;
        BTORepository repo = new BTORepository();
        for (BTOProj project : repo.getAllProjects()) {
            if (project.getProjName().equalsIgnoreCase(projName)) {
                newproj = project;
            }
        }

        for (OfficerProjectApplication a : registrationApps) {
            if (projName == a.getProj().getProjName()) {return false;}
        }
       
        OfficerProjectApplication app = new OfficerProjectApplication(this, proj, AssignStatus.Pending);
        registrationApps.add(0, app);
        return true;
    }*/

    /** Returns the officer ID. */
    public int getOfficerId() { return officerId; }

    /** Sets the officer ID. */
    public void setOfficerId(int id) { this.officerId = id; }

    /** Returns all projects handled by this officer. */
    public List<BTOProj> getAllProj() { return projectsHandled; }

    /** Sets the list of projects handled by this officer. */
    public void setProj(List<BTOProj> projects) { this.projectsHandled = projects; }

    /** Adds a project to the officer's handled list. */
    public void addProj(BTOProj proj) { this.projectsHandled.add(0, proj); }

    /**
     * Removes a project by project ID.
     *
     * @throws NoSuchElementException if project not found
     */
    public void removeProj(int projId) {
        boolean removed = projectsHandled.removeIf(p -> p.getProjId() == projId);
        if (!removed) {
            throw new NoSuchElementException("No project with ID " + projId);
        }
    }

    /**
     * Retrieves a project by ID.
     *
     * @throws NoSuchElementException if project not found
     */
    public BTOProj getProj(int projId) {
        for (BTOProj p : projectsHandled) {
            if (p.getProjId() == projId) {return p;}
        }
        throw new NoSuchElementException("No project with ID " + projId);
    }

    /** Returns the list of officer project registration applications. */
    public List<OfficerProjectApplication> getRegisteredApps() { return registrationApps; }

    /** Sets the officer's registration applications. */
    public void setRegisteredApps(List<OfficerProjectApplication> apps) { this.registrationApps = apps; }

    /** Adds a registration application for the officer. */
    public void addApps(OfficerProjectApplication app) { this.registrationApps.add(0, app); }

    /**
     * Removes a registration application by application ID.
     *
     * @throws NoSuchElementException if application not found
     */
    public void removeApps(int appId) {
        boolean removed = registrationApps.removeIf(a -> a.getOfficerAppId() == appId);
        if (!removed) {
            throw new NoSuchElementException("No application with ID " + appId);
        }
    }

    /**
     * Retrieves a registration application by ID.
     *
     * @throws NoSuchElementException if application not found
     */
    public OfficerProjectApplication getApps(int appId) {
        for (OfficerProjectApplication a : registrationApps) {
            if (a.getOfficerAppId() == appId) {return a;}
        }
        throw new NoSuchElementException("No application with ID " + appId);
    }
}



