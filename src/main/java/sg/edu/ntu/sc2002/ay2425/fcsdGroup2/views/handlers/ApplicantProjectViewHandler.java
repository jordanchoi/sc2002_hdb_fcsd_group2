package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.handlers;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.ApplicantController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.FlatType;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FilterOption;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service.ProjectFilterService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ApplicantProjectViewHandler {
    private final Set<FilterOption> activeFilters = new HashSet<>();
    private final ProjectFilterService filterService = new ProjectFilterService();
    private final Scanner scanner = new Scanner(System.in);
    private final ApplicantController appController;

    public ApplicantProjectViewHandler(ApplicantController applicantController) {
        appController = applicantController;
    }

    private void handleFlatTypeFilter() {
        System.out.println("\nSelect a flat type to filter:");
        int index = 1;
        for (FlatTypes type : FlatTypes.values()) {
            System.out.println(index++ + ". " + type.name());
        }

        int choice = getIntInput();
        if (choice >= 1 && choice <= FlatTypes.values().length) {
            FlatTypes selectedType = FlatTypes.values()[choice - 1];
            filterService.setSelectedFlatType(selectedType);
            activeFilters.add(FilterOption.FLAT_TYPE);
        } else {
            System.out.println("Invalid choice. Keeping current filter.");
        }
    }

    private void handlePriceFilter() {
        try {
            System.out.print("Enter minimum price: ");
            String minInput = scanner.nextLine();
            double minPrice = Double.parseDouble(minInput);

            System.out.print("Enter maximum price: ");
            String maxInput = scanner.nextLine();
            double maxPrice = Double.parseDouble(maxInput);

            if (maxPrice < minPrice) {
                System.out.println("Maximum price cannot be less than minimum price. Cancelling filter change.");
                return;
            }

            filterService.setPriceRange(minPrice, maxPrice);
            activeFilters.add(FilterOption.PRICE);

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter numeric values for prices. Cancelling filter change.");
        }
    }

    private void handleDateRangeFilter() {
        try {
            System.out.print("Enter start date (yyyy-MM-dd): ");
            String start = scanner.nextLine();
            LocalDate startDate = LocalDate.parse(start);

            System.out.print("Enter end date (yyyy-MM-dd): ");
            String end = scanner.nextLine();
            LocalDate endDate = LocalDate.parse(end);

            if (endDate.isBefore(startDate)) {
                System.out.println("End date cannot be before start date. Cancelling filter change.");
                return;
            }

            filterService.setDateRange(startDate, endDate);
            activeFilters.add(FilterOption.CLOSING_DATE);

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd. Cancelling filter change.");
        }
    }

    private void handleNeighbourhoodSearch() {
        System.out.print("Enter neighbourhood or part of it to search: ");
        String nbhInput = scanner.nextLine();
        filterService.setNeighbourhoodSearch(nbhInput.trim());
        activeFilters.add(FilterOption.SEARCH_BY_NEIGHBOURHOOD);
    }

    private void clearAllFilters() {
        filterService.clearAllFilters();
        activeFilters.clear();
        System.out.println("All filters cleared. Showing default view (alphabetical order).");
    }

    private void handleProjectNameSearch() {
        System.out.print("Enter project name or part of it to search: ");
        String nameInput = scanner.nextLine();
        filterService.setProjectNameSearch(nameInput.trim());
        activeFilters.add(FilterOption.SEARCH_BY_NAME);
    }

    private void displayFilterOptions() {
        boolean continueFiltering = true;
        while (continueFiltering) {
            displayActiveFilters();
            System.out.println("\nChoose filter options (multiple choices allowed, enter numbers separated by commas):");
            System.out.println("1. Filter by Application Closing Date Range");
            System.out.println("2. Filter by Flat Type");
            System.out.println("3. Filter by Price");
            System.out.println("4. Filter by Project Name");
            System.out.println("5. Filter by Neighbourhood");
            System.out.println("6. Clear All Filters");
            System.out.println("7. Exit Filter Menu");

            String input = scanner.nextLine();
            String[] choices = input.split(",");

            for (String choice : choices) {
                try {
                    int filterChoice = Integer.parseInt(choice.trim());
                    switch (filterChoice) {
                        case 1 -> handleDateRangeFilter();
                        case 2 -> handleFlatTypeFilter();
                        case 3 -> handlePriceFilter();
                        case 4 -> handleProjectNameSearch();
                        case 5 -> handleNeighbourhoodSearch();
                        case 6 -> clearAllFilters();
                        case 7 -> {
                            continueFiltering = false;
                            System.out.println("Returning to main menu...");
                        }
                        default -> System.out.println("Invalid option. Skipping.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input, skipping this option.");
                }
            }

            if (continueFiltering) {
                viewEligibleProjects();
            }
        }
    }

    public void displayActiveFilters() {
        if (activeFilters.isEmpty()) {
            System.out.print("(No filters applied)");
        } else {
            System.out.print("(Filters: ");
            List<String> filterNames = new ArrayList<>();
            for (FilterOption filter : activeFilters) {
                switch (filter) {
                    case CLOSING_DATE -> filterNames.add("Closing Date Range");
                    case FLAT_TYPE -> filterNames.add("Flat Type");
                    case PRICE -> filterNames.add("Price Range");
                    case SEARCH_BY_NAME -> filterNames.add("Project Name Search");
                    case SEARCH_BY_NEIGHBOURHOOD -> filterNames.add("Neighbourhood Search");
                }
            }
            System.out.print(String.join(", ", filterNames) + ")");
        }
    }

    private int getIntInput() {
        int input;
        while (true) {
            System.out.print("Enter your choice: ");
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                scanner.nextLine();
                return input;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();
            }
        }
    }

    public void viewEligibleProjects() {
        List<BTOProj> eligibleProjects = appController.getEligibleProjs();
        List<BTOProj> filteredProjects = filterService.applyFilters(eligibleProjects, new ArrayList<>(activeFilters));

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

    public void askToChangeFilters() {
        System.out.println("Would you like to change the filters? (y/n)");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("y")) {
            displayFilterOptions();
        }
    }
}
