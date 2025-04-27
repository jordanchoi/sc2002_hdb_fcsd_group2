package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces.canApplyFlat;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.ApplicationService;

import java.util.*;

/**
 * Controller class to handle BTO project applications for applicants.
 * <p>
 * Provides functionality for applicants to apply for projects, view their application status,
 * withdraw applications
 * </p>
 */
public class ApplicantController implements canApplyFlat {
    private final HDBApplicant model;
    private final ApplicationService appService = new ApplicationService();
    private final BTORepository projRepo = BTORepository.getInstance();
    private final Scanner scanner = new Scanner(System.in);
    /**
     * Constructs an ApplicantController for the given applicant.
     *
     * @param model the logged-in HDB applicant
     */
    public ApplicantController(HDBApplicant model) {
        this.model = model;
    }

    /**
     * Requests to withdraw the applicant's current BTO application.
     */
    public void withdrawApplication() {
        appService.withdrawApplication(model);
    }

    /**
     * Displays a list of available projects and allows the applicant to select a project.
     *
     * @param availableProjects the list of eligible BTO projects
     * @return the selected project, or {@code null} if the applicant cancels the selection
     */
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
            System.out.print("\nEnter the Project ID you want to apply for (or 0 to cancel): ");
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
    /**
     * Retrieves a list of BTO projects for which the applicant is eligible.
     *
     * @return a list of eligible BTO projects
     */
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

    /**
     * Checks if the given project is eligible for the applicant based on their age and marital status.
     *
     * @param project the project to check
     * @return {@code true} if the applicant is eligible for the project, {@code false} otherwise
     */
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

    /**
     * Initiates the process for the applicant to apply for a BTO project.
     * Prompts the applicant to select from eligible projects.
     */
    public void applyForProject() {
        List<BTOProj> eligibleProjects = getEligibleProjs();
        BTOProj selectedProject = selectProject(eligibleProjects);
        if (selectedProject == null) {
            System.out.println("Returning to main menu.");
            return;
        }
        submitApplication(selectedProject);
    }

    /**
     * Submits an application for the selected BTO project on behalf of the applicant.
     *
     * @param selectedProject the project the applicant wishes to apply for
     * @return {@code true} if the application was successfully submitted, {@code false} otherwise
     */
    @Override
    public boolean submitApplication(BTOProj selectedProject) {
        if (selectedProject != null) {
            appService.applyForProject(model, selectedProject);
            return true;
        }
        return false;
    }

    /**
     * Displays the details of the applicant's current BTO application.
     */
    public void showApplicantApplicationDetails() {
        String applicationDetails = appService.viewApplicationDetails(model);
        System.out.println(applicationDetails);
    }
}
