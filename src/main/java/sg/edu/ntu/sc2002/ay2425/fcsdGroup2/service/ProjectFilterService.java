package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.FlatType;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FilterOption;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for filtering and sorting BTO projects based on various criteria.
 */
public class ProjectFilterService {
    private LocalDate startDateFilter;
    private LocalDate endDateFilter;
    private FlatTypes selectedFlatType;
    private double minPrice;
    private double maxPrice = Double.MAX_VALUE;
    private String projectNameSearch;
    private String neighbourhoodSearch;

    /**
     * Applies a list of filters to the given list of BTO projects.
     *
     * @param projects the list of eligible projects to filter
     * @param filters the list of filters to apply
     * @return the filtered list of projects
     */
    public List<BTOProj> applyFilters(List<BTOProj> projects, List<FilterOption> filters) {
        List<BTOProj> filteredProjects = projects;

        // Default sort by project name alphabetically
        filteredProjects.sort(Comparator.comparing(BTOProj::getProjName));

        for (FilterOption filter : filters) {
            switch (filter) {
                case CLOSING_DATE -> filteredProjects = filterByDateRange(filteredProjects);
                case FLAT_TYPE -> filteredProjects = filterByFlatType(filteredProjects);
                case PRICE -> filteredProjects = filterByPrice(filteredProjects);
                case SEARCH_BY_NAME -> filteredProjects = filterByProjectName(filteredProjects);
                case SEARCH_BY_NEIGHBOURHOOD -> filteredProjects = filterByNeighbourhood(filteredProjects);
                default -> { /* Do nothing */ }
            }
        }

        return filteredProjects;
    }

    /**
     * Filters projects based on the application's closing date range.
     *
     * @param projects the list of projects to filter
     * @return the filtered list of projects
     */
    private List<BTOProj> filterByDateRange(List<BTOProj> projects) {
        if (startDateFilter != null && endDateFilter != null) {
            return projects.stream()
                    .filter(proj -> {
                        LocalDate closeDate = proj.getAppCloseDate().toLocalDate();
                        return (closeDate.isEqual(startDateFilter) || closeDate.isAfter(startDateFilter))
                                && (closeDate.isEqual(endDateFilter) || closeDate.isBefore(endDateFilter));
                    })
                    .sorted(Comparator.comparing(BTOProj::getAppCloseDate))
                    .collect(Collectors.toList());
        }
        return projects.stream()
                .sorted(Comparator.comparing(BTOProj::getAppCloseDate))
                .collect(Collectors.toList());
    }

    /**
     * Filters projects based on selected flat type availability.
     *
     * @param projects the list of projects to filter
     * @return the filtered list of projects
     */
    private List<BTOProj> filterByFlatType(List<BTOProj> projects) {
        if (selectedFlatType != null) {
            return projects.stream()
                    .filter(proj -> {
                        FlatType flat = proj.getFlatUnits().get(selectedFlatType);
                        return flat != null && flat.getUnitsAvail() > 0;
                    })
                    .collect(Collectors.toList());
        }
        return projects;
    }

    /**
     * Filters projects based on flat selling price range.
     *
     * @param projects the list of projects to filter
     * @return the filtered list of projects
     */
    private List<BTOProj> filterByPrice(List<BTOProj> projects) {
        return projects.stream()
                .filter(proj -> {
                    double lowestPrice = getLowestFlatPrice(proj);
                    return lowestPrice >= minPrice && lowestPrice <= maxPrice;
                })
                .sorted(Comparator.comparing(this::getLowestFlatPrice))
                .collect(Collectors.toList());
    }

    /**
     * Filters projects based on project name search keyword.
     *
     * @param projects the list of projects to filter
     * @return the filtered list of projects
     */
    private List<BTOProj> filterByProjectName(List<BTOProj> projects) {
        if (projectNameSearch != null && !projectNameSearch.trim().isEmpty()) {
            String searchLower = projectNameSearch.toLowerCase();
            return projects.stream()
                    .filter(proj -> proj.getProjName().toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }
        return projects;
    }

    /**
     * Filters projects based on neighbourhood search keyword.
     *
     * @param projects the list of projects to filter
     * @return the filtered list of projects
     */
    private List<BTOProj> filterByNeighbourhood(List<BTOProj> projects) {
        if (neighbourhoodSearch != null && !neighbourhoodSearch.trim().isEmpty()) {
            String searchLower = neighbourhoodSearch.toLowerCase();
            return projects.stream()
                    .filter(proj -> proj.getProjNbh().name().toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }
        return projects;
    }

    /**
     * Retrieves the lowest selling price among available flats in a project.
     *
     * @param project the BTO project
     * @return the lowest selling price or {@code Double.MAX_VALUE} if none available
     */
    private double getLowestFlatPrice(BTOProj project) {
        return project.getFlatUnits().values().stream()
                .filter(flat -> flat.getUnitsAvail() > 0)
                .mapToDouble(FlatType::getSellingPrice)
                .min()
                .orElse(Double.MAX_VALUE);
    }

    /**
     * Sets the date range filter for project closing dates.
     *
     * @param startDate the start date
     * @param endDate the end date
     */
    // Setters for filter parameters
    public void setDateRange(LocalDate startDate, LocalDate endDate) {
        this.startDateFilter = startDate;
        this.endDateFilter = endDate;
    }

    /**
     * Sets the project name search filter.
     *
     * @param search the project name search string
     */
    public void setProjectNameSearch(String search) {
        this.projectNameSearch = search;
    }

    /**
     * Sets the flat type filter.
     *
     * @param flatType the selected flat type
     */
    public void setSelectedFlatType(FlatTypes flatType) {
        this.selectedFlatType = flatType;
    }

    /**
     * Sets the price range filter for flat selling prices.
     *
     * @param minPrice the minimum price
     * @param maxPrice the maximum price
     */
    public void setPriceRange(double minPrice, double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    /**
     * Sets the neighbourhood search filter.
     *
     * @param neighbourhoodSearch the neighbourhood search string
     */
    public void setNeighbourhoodSearch(String neighbourhoodSearch) {
        this.neighbourhoodSearch = neighbourhoodSearch;
    }

    /**
     * Clears all applied filters and resets them to default values.
     */
    public void clearAllFilters() {
        startDateFilter = null;
        endDateFilter = null;
        selectedFlatType = null;
        minPrice = 0;
        maxPrice = Double.MAX_VALUE;
        projectNameSearch = null;
        neighbourhoodSearch = null;
    }
}