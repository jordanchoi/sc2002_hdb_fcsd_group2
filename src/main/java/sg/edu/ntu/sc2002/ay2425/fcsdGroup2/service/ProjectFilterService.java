package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.service;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.FlatType;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FilterOption;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectFilterService {
    private LocalDate startDateFilter;
    private LocalDate endDateFilter;
    private FlatTypes selectedFlatType;
    private double minPrice;
    private double maxPrice = Double.MAX_VALUE;
    private String projectNameSearch;
    private String neighbourhoodSearch;

    public List<BTOProj> applyFilters(List<BTOProj> projects, List<FilterOption> filters) {
        List<BTOProj> filteredProjects = projects;

        // Default sort by project name alphabetically
        filteredProjects.sort(Comparator.comparing(BTOProj::getProjName));

        for (FilterOption filter : filters) {
            switch (filter) {
                case CLOSING_SOONEST -> filteredProjects = filterByDateRange(filteredProjects);
                case FLAT_TYPE -> filteredProjects = filterByFlatType(filteredProjects);
                case PRICE -> filteredProjects = filterByPrice(filteredProjects);
                case SEARCH_BY_NAME -> filteredProjects = filterByProjectName(filteredProjects);
                case SEARCH_BY_NEIGHBOURHOOD -> filteredProjects = filterByNeighbourhood(filteredProjects);
                default -> { /* Do nothing */ }
            }
        }

        return filteredProjects;
    }

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

    private List<BTOProj> filterByPrice(List<BTOProj> projects) {
        return projects.stream()
                .filter(proj -> {
                    double lowestPrice = getLowestFlatPrice(proj);
                    return lowestPrice >= minPrice && lowestPrice <= maxPrice;
                })
                .sorted(Comparator.comparing(this::getLowestFlatPrice))
                .collect(Collectors.toList());
    }

    private List<BTOProj> filterByProjectName(List<BTOProj> projects) {
        if (projectNameSearch != null && !projectNameSearch.trim().isEmpty()) {
            String searchLower = projectNameSearch.toLowerCase();
            return projects.stream()
                    .filter(proj -> proj.getProjName().toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }
        return projects;
    }

    private List<BTOProj> filterByNeighbourhood(List<BTOProj> projects) {
        if (neighbourhoodSearch != null && !neighbourhoodSearch.trim().isEmpty()) {
            String searchLower = neighbourhoodSearch.toLowerCase();
            return projects.stream()
                    .filter(proj -> proj.getProjNbh().name().toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        }
        return projects;
    }

    private double getLowestFlatPrice(BTOProj project) {
        return project.getFlatUnits().values().stream()
                .filter(flat -> flat.getUnitsAvail() > 0)
                .mapToDouble(FlatType::getSellingPrice)
                .min()
                .orElse(Double.MAX_VALUE);
    }

    // Setters for filter parameters
    public void setDateRange(LocalDate startDate, LocalDate endDate) {
        this.startDateFilter = startDate;
        this.endDateFilter = endDate;
    }

    public void setProjectNameSearch(String search) {
        this.projectNameSearch = search;
    }

    public void setSelectedFlatType(FlatTypes flatType) {
        this.selectedFlatType = flatType;
    }

    public void setPriceRange(double minPrice, double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public void setNeighbourhoodSearch(String neighbourhoodSearch) {
        this.neighbourhoodSearch = neighbourhoodSearch;
    }

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