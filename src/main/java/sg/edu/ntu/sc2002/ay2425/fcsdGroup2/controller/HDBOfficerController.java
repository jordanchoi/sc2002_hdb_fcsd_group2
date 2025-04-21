package controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.entities.HDBOfficer;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import java.time.LocalDateTime;

public class OfficerController {
    private Officer officer;
    private BTORepository btoRepository;

    public OfficerController (Officer officer, BTORepository btoRepository) {
        this.officer = officer;
        this.btoRepository = btoRepository;
    }

    public boolean projEligibility(BTOProj project, HDBApplicant appliedProj) {
        if (project.btoProjId == appliedProj.btoProjId) {
            return false;
        }

        LocalDateTime newOpen = project.getAppOpenDate();
        LocalDateTime newClose = project.getAppCloseDate();

        if (newOpen == null || newClose == null) {
            // If dates are not set properly, avoid applying.
            return false;
        }

        for (BTOProj handledProj : officer.getProjectsHandled()) {
            LocalDateTime handledOpen = handledProj.getAppOpenDate();
            LocalDateTime handledClose = handledProj.getAppCloseDate();
            if (handledOpen == null || handledClose == null) {
                continue;
            }
            if (newOpen.isBefore(handledClose) || newClose.isAfter(handledOpen)) {
                return false;
            }
        }
        return true;
    }

    public boolean applyProjOfficer(String projectName) {
        BTOProj newProject = null;

        // Search for the project by name.
        for (BTOProj proj : btoRepository.getAllProjs()) {
            if (proj.getProjName().equalsIgnoreCase(projectName)) {
                newProject = proj;
                break;
            }
        }
        if (newProject == null) {
            System.out.println("Project not found.");
            return false;
        }
        if (projEligibility(officer.projectsHandled[0],newProject)) {
            newProject.assignOfficer(officer);
            officer.addProject(newProject);
            System.out.println("Successfully applied to be the officer for project: " + newProject.getProjName());
            return true;
        }else{
            System.out.println("You are already handling a project in the same time frame.");
            return false;
        }
    }

    public String projRegStatus(String projectName) {
        for (BTOProj proj : btoRepository.getAllProjs()) {
            if (proj.getProjName().equalsIgnoreCase(projectName)) {
                if (proj.getOfficersList().contains(officer)) {
                    return "Registered";
                } else {
                    return "Not Registered";
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
