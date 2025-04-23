package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces.*;
import java.time.LocalDateTime;

public class HDBOfficerController implements canApplyFlat {
    private HDBOfficer officer;
    private BTORepository btoRepository;

    public HDBOfficerController (HDBOfficer officer, BTORepository btoRepository) {
        this.officer = officer;
        this.btoRepository = btoRepository;
    }
    
    @Override
    public boolean checkEligibility(String projName) {
        BTOProj proj = null;
        for (BTOProj p : btoRepository.getAllProjects()) {
            if (p.getProjName() == projName)  {proj = p;}
        }
        //questionable need to wait for Applicant to know the different applications applied before
        //boolean appliedAsApplicant = proj.getAllApp().stream().anyMatch(app -> app.getApplicant().getUserId() == this.getUserId());
        Application currentApp = officer.getCurrentApplication();
        if (currentApp != null && proj == currentApp.getAppliedProj()) {return false;}

        LocalDateTime startNew = proj.getAppOpenDate();
        LocalDateTime endNew   = proj.getAppCloseDate();
        for (BTOProj handled : officer.getAllProj()) {
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

        for (OfficerProjectApplication a : officer.getRegisteredApps()) {
            if (projName == a.getProj().getProjName()) {return false;}
        }
       
        OfficerProjectApplication app = new OfficerProjectApplication(officer, newproj, AssignStatus.PENDING);
        officer.addApps(app);
        return true;
    }

    public String projRegStatus(String projectName) {
        for (BTOProj proj : btoRepository.getAllProjects()) {
            if (proj.getProjName().equalsIgnoreCase(projectName)) {
                for (HDBOfficer o : proj.getOfficersList()) {
                    if (o.equals(officer)) {return "Registered";} else {return "Not Registered";}
                }
            }
        }
        return "Project not found";
    }

    public java.util.List<String> viewProjDetails(BTOProj project) {
        java.util.List<String> details = new java.util.ArrayList<>();
        if (project == null) {return details;}

        details.add("Project ID: " + project.getProjId());
        details.add("Project Name: " + project.getProjName());
        details.add("Status: " + project.getProjStatus());
        // Additional details can be appended as required.
        return details;
    }
}
