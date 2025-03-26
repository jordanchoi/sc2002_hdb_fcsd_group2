import java.util.ArrayList;
import java.util.List;

class BTOProject {
    private String name;  // Project Name
    private String neighborhood;  // Location (e.g., Yishun, Boon Lay)
    private boolean visible;  // Visibility toggle
    private int twoRoomUnits;  // Number of 2-Room flats
    private int threeRoomUnits;  // Number of 3-Room flats
    private String applicationOpenDate;  // Application opening date
    private String applicationCloseDate;  // Application closing date
    private HDBManager manager;  // HDB Manager in charge
    private int availableOfficerSlots;  // Available HDB Officer slots (Max: 10)
    
    private List<HDBOfficer> assignedOfficers;  // List of HDB Officers handling this project

    private static List<BTOProject> allProjects = new ArrayList<>();  // List of all projects

    // Constructor
    public BTOProject(String name, String neighborhood, int twoRoomUnits, int threeRoomUnits, 
                      String applicationOpenDate, String applicationCloseDate, HDBManager manager) {
        this.name = name;
        this.neighborhood = neighborhood;
        this.visible = true; // Default: visible
        this.twoRoomUnits = twoRoomUnits;
        this.threeRoomUnits = threeRoomUnits;
        this.applicationOpenDate = applicationOpenDate;
        this.applicationCloseDate = applicationCloseDate;
        this.manager = manager;
        this.availableOfficerSlots = 10; // Max 10 officers
        this.assignedOfficers = new ArrayList<>();
        
        allProjects.add(this);
    }

    // Getters
    public String getProjectName() { return name; }
    public String getNeighborhood() { return neighborhood; }
    public boolean isVisible() { return visible; }
    public int getTwoRoomUnits() { return twoRoomUnits; }
    public int getThreeRoomUnits() { return threeRoomUnits; }
    public String getApplicationOpenDate() { return applicationOpenDate; }
    public String getApplicationCloseDate() { return applicationCloseDate; }
    public HDBManager getManager() { return manager; }
    public int getAvailableOfficerSlots() { return availableOfficerSlots; }
    
    // Setters
    public void setVisible(boolean visibility) { this.visible = visibility; }
    public void updateFlatUnits(int twoRoomUnits, int threeRoomUnits) {
        this.twoRoomUnits = twoRoomUnits;
        this.threeRoomUnits = threeRoomUnits;
    }
    
    // HDB Officer Management
    public boolean assignHDBOfficer(HDBOfficer officer) {
        if (assignedOfficers.size() < availableOfficerSlots) {
            assignedOfficers.add(officer);
            return true;
        }
        return false; // No slots available
    }

    public void removeHDBOfficer(HDBOfficer officer) {
        assignedOfficers.remove(officer);
    }

    // Static method to get all projects
    public static List<BTOProject> getAllProjects() { return allProjects; }
}
