package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
//import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces.canApplyFlat;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class HDBOfficer extends HDBApplicant{
    private int officerId;
    private List<BTOProj> projectsHandled;
    private List<OfficerProjectApplication> registrationApps;


    public HDBOfficer(int userId, String nric, String password, String firstName, String lastName, String middleName,int age, MaritalStatus maritalStatus) {
        super(userId, nric, password, firstName, lastName, middleName, age, maritalStatus);
        this.officerId = userId;
        this.projectsHandled = new ArrayList<>();
        this.registrationApps = new ArrayList<>();
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

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int id) {
        this.officerId = id;
    }

    public List<BTOProj> getAllProj() {
        return projectsHandled;
    }

    public void setProj(List<BTOProj> projects) {
        this.projectsHandled = projects;
    }

    public void addProj(BTOProj proj) {
        this.projectsHandled.add(0,proj);
    }

    public void removeProj(int projId) {
        boolean removed = projectsHandled.removeIf(p -> p.getProjId() == projId);
        if (!removed) {
        throw new NoSuchElementException("No project with ID " + projId);
        }
    }

    public BTOProj getProj(int projId) {
        for (BTOProj p : projectsHandled) {
            if (p.getProjId() == projId) {return p;}
        }
        throw new NoSuchElementException("No project with ID " + projId);
    }

    public List<OfficerProjectApplication> getRegisteredApps() {
        return registrationApps;
    }

    public void setRegisteredApps(List<OfficerProjectApplication> apps) {
        this.registrationApps = apps;
    }

    public void addApps(OfficerProjectApplication app) {
        this.registrationApps.add(0,app);
    }

    public void removeApps(int appId) {
        boolean removed = registrationApps.removeIf(a -> a.getOfficerAppId() == appId);
        if (!removed) {
            throw new NoSuchElementException("No application with ID " + appId);
        }
    }

    public OfficerProjectApplication getApps(int appId) {
        for (OfficerProjectApplication a : registrationApps) {
            if (a.getOfficerAppId() == appId) {return a;}
        }
        throw new NoSuchElementException("No application with ID " + appId);
    }
}



