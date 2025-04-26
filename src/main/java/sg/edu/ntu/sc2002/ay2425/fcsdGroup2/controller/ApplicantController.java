package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces.canApplyFlat;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FilterOption;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.ApplicationService;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.ProjectFilterService;

import java.time.LocalDate;
import java.util.*;

public class ApplicantController implements canApplyFlat {
    private final HDBApplicant model;
    private final ApplicationService appService = new ApplicationService();
    private final BTORepository projRepo = BTORepository.getInstance();
    private final ProjectFilterService filterService;
    private final Scanner scanner = new Scanner(System.in);

    public ApplicantController(HDBApplicant model) {
        this.model = model;
        this.filterService = new ProjectFilterService();
    }

    // Setters now set filters in the service directly
    public void setDateRange(LocalDate startDate, LocalDate endDate) {
        filterService.setDateRange(startDate, endDate);
    }

    public void setProjectNameSearch(String search) {
        filterService.setProjectNameSearch(search);
    }

    public void setSelectedFlatType(FlatTypes flatType) {
        filterService.setSelectedFlatType(flatType);
    }

    public void setPriceRange(double minPrice, double maxPrice) {
        filterService.setPriceRange(minPrice, maxPrice);
    }

    public void setNeighbourhoodSearch(String neighbourhoodSearch) {
        filterService.setNeighbourhoodSearch(neighbourhoodSearch);
    }

    public void clearAllFilters() {
        filterService.clearAllFilters();
    }

    public void withdrawApplication() {
        appService.withdrawApplication(model);
    }

    public void viewEligibleProjects(List<FilterOption> filters) {
        List<BTOProj> eligibleProjects = getEligibleProjs();

        // Apply filters through the service
        List<BTOProj> filteredProjects = filterService.applyFilters(eligibleProjects, filters);

        System.out.println("\n=== Eligible BTO Projects ===");

        if (filteredProjects.isEmpty()) {
            System.out.println("No eligible projects found.");
            return;
        }

        for (BTOProj project : filteredProjects) {
            System.out.println("---------------------------------------");
            System.out.println("Project ID          : " + project.getProjId());
            System.out.println("Project Name        : " + project.getProjName());
            System.out.println("Neighbourhood       : " + project.getProjNbh());
            System.out.println("Application Opens   : " + project.getAppOpenDate());
            System.out.println("Application Closes  : " + project.getAppCloseDate());

            System.out.println("Available Flat Types:");
            Map<FlatTypes, FlatType> flatUnits = project.getFlatUnits();

            if (flatUnits.isEmpty()) {
                System.out.println("  - No flat types available.");
            } else {
                for (FlatTypes type : FlatTypes.values()) {
                    FlatType flat = flatUnits.get(type);
                    if (flat != null && flat.getUnitsAvail() > 0) {
                        System.out.printf("  - %s: %d units available ($%.2f)\n",
                                flat.getTypeName(), flat.getUnitsAvail(), flat.getSellingPrice());
                    }
                }
            }
            System.out.println("---------------------------------------\n");
        }
    }

    private double getLowestFlatPrice(BTOProj project) {
        return project.getFlatUnits().values().stream()
                .filter(flat -> flat.getUnitsAvail() > 0)
                .mapToDouble(FlatType::getSellingPrice)
                .min()
                .orElse(Double.MAX_VALUE);
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
