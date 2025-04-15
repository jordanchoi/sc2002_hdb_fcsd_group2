package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBApplicant;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBManager;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.HDBOfficer;

import java.util.ArrayList;
import java.util.List;

/**
 * UserRepository is responsible for managing the list of users in the system.
 * It provides methods to add users and retrieve lists of different types of users.
 * It should also perform the fetch and save operations to the Excel datasheet provided.
 * @author Jordan - 16/04/2025
 */

public class UserRepository {

    // Data Files Path
    private static final String MANAGER_FILE_PATH = "src/main/resources/managers.xlsx";
    private static final String OFFICER_FILE_PATH = "src/main/resources/officers.xlsx";
    private static final String APPLICANT_FILE_PATH = "src/main/resources/applicants.xlsx";

    private List<HDBManager> managers;
    private List<HDBOfficer> officers;
    private List<HDBApplicant> applicants;

    // Constructors
    public UserRepository() {
        this.managers = new ArrayList<>();
        this.officers = new ArrayList<>();
        this.applicants = new ArrayList<>();

        // Fetch from excel file provided


        // Placeholder to call the load method
    }

    // Constructors with managers, officers, applicants passed in
    public UserRepository(List<HDBManager> managers, List<HDBOfficer> officers, List<HDBApplicant> applicants) {
        this.managers = managers;
        this.officers = officers;
        this.applicants = applicants;
    }

    // Getters & Setters
    public List<HDBManager> getManagers() {
        return managers;
    }
    public void setManagers(List<HDBManager> managers) {
        this.managers = managers;
    }

    public List<HDBOfficer> getOfficers() {
        return officers;
    }
    public void setOfficers(List<HDBOfficer> officers) {
        this.officers = officers;
    }

    public List<HDBApplicant> getApplicants() {
        return applicants;
    }
    public void setApplicants(List<HDBApplicant> applicants) {
        this.applicants = applicants;
    }

    // Load Users from Excel
    public void loadUsersFromFile(String filePath) {
        // Implement logic to read from the Excel file and populate the lists

        // This method should be called in the constructor to initialize the lists
        // For example, using Apache POI or any other library to read Excel files
    }

}
