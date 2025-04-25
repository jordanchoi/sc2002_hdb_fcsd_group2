package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces.canApplyFlat;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.ApplicationService;

import java.util.*;

public class ApplicantController implements canApplyFlat {
    private final HDBApplicant model;
    private final ApplicationService appService = new ApplicationService();
    private final BTORepository projRepo = BTORepository.getInstance();
    private final Scanner scanner = new Scanner(System.in);

    public ApplicantController(HDBApplicant model) {
        this.model = model;
    }

    public void withdrawApplication() {
        appService.withdrawApplication(model);
    }

    public void viewEligibleProjects() {
        List<BTOProj> eligibleProjects = getEligibleProjs();
        System.out.println("\n=== Eligible BTO Projects ===");

        if (eligibleProjects.isEmpty()) {
            System.out.println("No eligible projects found.");
            return;
        }

        for (BTOProj project : eligibleProjects) {
            System.out.println("---------------------------------------");
            System.out.println("Project ID          : " + project.getProjId());
            System.out.println("Project Name        : " + project.getProjName());
            System.out.println("Neighbourhood       : " + project.getProjNbh());
            System.out.println("Application Opens   : " + project.getAppOpenDate());
            System.out.println("Application Closes  : " + project.getAppCloseDate());
            System.out.println("Available Flat Types:");
            for (Map.Entry<FlatTypes, FlatType> entry : project.getFlatUnits().entrySet()) {
                FlatType ft = entry.getValue();
                System.out.printf("  - %s: %d units available\n", ft.getTypeName(), ft.getUnitsAvail());

            }

            System.out.println("---------------------------------------\n");
        }
    }




    public BTOProj selectProject(List<BTOProj> availableProjects) {
        System.out.println("\n=== Available BTO Projects ===");

        if (availableProjects.isEmpty()) {
            System.out.println("No available projects found.");
            return null;
        }

        for (BTOProj project : availableProjects) {
            System.out.println(project.getProjId() + ". " + project.getProjName() + " (" + project.getProjNbh() + ")");
        }

        while (true) {
            System.out.println("\nEnter the Project ID of the project you want to apply for, or 0 to return:");
            String input = scanner.nextLine();

            try {
                int chosenProjId = Integer.parseInt(input);
                if (chosenProjId == 0) return null;

                for (BTOProj project : availableProjects) {
                    if (project.getProjId() == chosenProjId) {
                        return project;
                    }
                }
                System.out.println("Invalid Project ID. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    public List<BTOProj> getEligibleProjs() {
        List<BTOProj> allProjs = projRepo.getAllProjects();
        List<BTOProj> eligibleProjects = new ArrayList<>();

        for (BTOProj project : allProjs) {
            if (checkEligibility(project)) {
                eligibleProjects.add(project);
            }
        }
        return eligibleProjects;
    }

    @Override
    public boolean checkEligibility(BTOProj project) {
        if (project.getProjStatus() == ProjStatus.CLOSED) {
            return false;
        }

        int age = model.getAge();
        MaritalStatus maritalStatus = model.getMaritalStatus();

        if (maritalStatus == MaritalStatus.MARRIED && age >= 21) {
            return true;
        } else if (maritalStatus == MaritalStatus.SINGLE && age >= 35) {
            FlatType twoRoom = project.getFlatUnits().get(FlatTypes.TWO_ROOM);
            return twoRoom != null && twoRoom.getUnitsAvail() > 0;
        }
        return false;
    }


    public void applyForProject() {
        List<BTOProj> eligibleProjects = getEligibleProjs();
        BTOProj selectedProject = selectProject(eligibleProjects);
        if (selectedProject == null) {
            System.out.println("Returning to main menu.");
            return;
        }
        submitApplication(selectedProject);
    }

    @Override
    public boolean submitApplication(BTOProj selectedProject) {
        if (selectedProject != null) {
            appService.applyForProject(model, selectedProject);
            return true;
        }
        return false;
    }

    public void showApplicantApplicationDetails() {
        String applicationDetails = appService.viewApplicationDetails(model);
        System.out.println(applicationDetails);
    }

}
