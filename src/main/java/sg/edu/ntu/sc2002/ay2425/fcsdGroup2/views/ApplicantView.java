package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.ApplicantController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.UserAuthController;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FilterOption;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.SessionStateManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.handlers.ApplicantViewHandler;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.views.interfaces.UserView;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ApplicantView implements UserView {
    private final SessionStateManager session = SessionStateManager.getInstance();
    private final ApplicantController controller = new ApplicantController((HDBApplicant) session.getLoggedInUser());
    private final Scanner scanner;
    private final UserAuthController authController = UserAuthController.getInstance();
    private final Set<FilterOption> activeFilters = new HashSet<>();

    public ApplicantView() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void start() {
        System.out.println("Welcome! Applicant " + session.getLoggedInUser().getFirstName() + "!\nYou are in the Applicant Main Menu.\n");
        int choice;
        do {
            displayMenu();
            choice = handleUserInput();
        } while (choice != 5);
    }

    @Override
    public void displayMenu() {
        System.out.println("\n=== Applicant Menu ===");
        System.out.print("1. View available BTO projects ");
        displayActiveFilters();
        System.out.println();
        System.out.println("2. BTO Application Services");
        System.out.println("3. Enquiry Services");
        System.out.println("4. Change password");
        System.out.println("5. Logout");
    }

    @Override
    public int handleUserInput() {
        int choice = getIntInput("Please select an option: ");

        switch (choice) {
            case 1 -> {
                controller.viewEligibleProjects(new ArrayList<>(activeFilters));
                askToChangeFilters();
            }
            case 2 -> handleBTOApplicationMenu();
            case 3 -> new ApplicantViewHandler((HDBApplicant) session.getLoggedInUser(), controller).displayEnquiryOptions();
            case 4 -> changePassword();
            case 5 -> System.out.println("Logging out...");
            default -> System.out.println("Invalid choice. Please try again.");
        }
        return choice;
    }

    private void askToChangeFilters() {
        System.out.println("Would you like to change the filters? (y/n)");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("y")) {
            displayFilterOptions();
        }
    }

    private void displayFilterOptions() {
        boolean continueFiltering = true;
        while (continueFiltering) {
            displayActiveFilters();
            System.out.println("\nChoose filter options (multiple choices allowed, enter numbers separated by commas):");
            System.out.println("1. Filter by Application Closing Date Range");
            System.out.println("2. Filter by Flat Type");
            System.out.println("3. Filter by Price");
            System.out.println("4. Search by Project Name");
            System.out.println("5. Search by Neighbourhood");
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
                            continueFiltering = false;  // Exit the loop
                            System.out.println("Returning to main menu...");
                        }
                        default -> System.out.println("Invalid option. Skipping.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input, skipping this option.");
                }
            }

            // Show filtered results after applying changes (unless exiting)
            if (continueFiltering) {
                controller.viewEligibleProjects(new ArrayList<>(activeFilters));
            }
        }
    }

    private void displayActiveFilters() {
        if (activeFilters.isEmpty()) {
            System.out.print("(No filters applied)");  // Changed from println to print
        } else {
            System.out.print("(Filters: ");  // Changed from println to print
            List<String> filterNames = new ArrayList<>();
            for (FilterOption filter : activeFilters) {
                switch (filter) {
                    case CLOSING_SOONEST -> filterNames.add("Closing Date Range");
                    case FLAT_TYPE -> filterNames.add("Flat Type");
                    case PRICE -> filterNames.add("Price Range");
                    case SEARCH_BY_NAME -> filterNames.add("Project Name Search");
                    case SEARCH_BY_NEIGHBOURHOOD -> filterNames.add("Neighbourhood Search");
                }
            }
            System.out.print(String.join(", ", filterNames) + ")");  // Changed from println to print
        }
    }

    private void handleNeighbourhoodSearch() {
        System.out.print("Enter neighbourhood or part of it to search: ");
        String nbhInput = scanner.nextLine();
        controller.setNeighbourhoodSearch(nbhInput.trim());
        activeFilters.add(FilterOption.SEARCH_BY_NEIGHBOURHOOD);
    }

    private void clearAllFilters() {
        controller.clearAllFilters();
        activeFilters.clear();
        System.out.println("All filters cleared. Showing default view (alphabetical order).");
    }


    private void handleProjectNameSearch() {
        System.out.print("Enter project name or part of it to search: ");
        String nameInput = scanner.nextLine();
        controller.setProjectNameSearch(nameInput.trim());
        activeFilters.add(FilterOption.SEARCH_BY_NAME);
    }

    private void handleBTOApplicationMenu() {
        int choice;
        do {
            System.out.println("\n=== BTO Application Services ===");
            System.out.println("1. Apply for a BTO project");
            System.out.println("2. View applied project status");
            System.out.println("3. Withdraw application");
            System.out.println("4. Back to main menu");

            choice = getIntInput("Please select a BTO application option: ");

            switch (choice) {
                case 1 -> controller.applyForProject();
                case 2 -> controller.showApplicantApplicationDetails();
                case 3 -> controller.withdrawApplication();
                case 4 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 4);
    }

    public void changePassword() {
        int attempts = 3;
        do {
            System.out.println("You have " + attempts + " attempts left to change your password.");
            System.out.print("Please enter your current password: ");
            String currentPassword = scanner.next();

            if (!authController.validatePassword(currentPassword)) {
                System.out.println("Current password is incorrect. Please try again.");
                attempts--;
            } else {
                System.out.print("Please enter your new password: ");
                String newPassword = scanner.next();
                if (authController.changePassword(currentPassword, newPassword)) {
                    System.out.println("Password changed successfully!");
                } else {
                    System.out.println("Failed to change password.");
                }
                break;
            }
        } while (attempts > 0);
    }

    private int getIntInput(String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
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

    private void handleFlatTypeFilter() {
        System.out.println("\nSelect a flat type to filter:");
        int index = 1;
        for (FlatTypes type : FlatTypes.values()) {
            System.out.println(index++ + ". " + type.name());
        }

        int choice = getIntInput("Enter your choice: ");
        if (choice >= 1 && choice <= FlatTypes.values().length) {
            FlatTypes selectedType = FlatTypes.values()[choice - 1];
            controller.setSelectedFlatType(selectedType);
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

            controller.setPriceRange(minPrice, maxPrice);
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

            controller.setDateRange(startDate, endDate);
            activeFilters.add(FilterOption.CLOSING_SOONEST);

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd. Cancelling filter change.");
        }
    }
}
