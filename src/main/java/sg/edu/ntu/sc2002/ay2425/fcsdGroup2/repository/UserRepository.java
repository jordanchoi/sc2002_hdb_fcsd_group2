package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.UserRoles;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.FileIO;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

// For Read & Write
/**
 * UserRepository is responsible for managing the list of users in the system.
 * It provides methods to add users and retrieve lists of different types of users.
 * It should also perform the fetch and save operations to the Excel datasheet provided.
 * @author Jordan - 16/04/2025
 */

public class UserRepository {

    // Data Files Path
    private static final String MANAGER_FILE_PATH = "data/ManagerList.xlsx";
    private static final String OFFICER_FILE_PATH = "data/OfficerList.xlsx";
    private static final String APPLICANT_FILE_PATH = "data/ApplicantList.xlsx";

    private List<HDBManager> managers;
    private List<HDBOfficer> officers;
    private List<HDBApplicant> applicants;

    // Constructors
    public UserRepository() {
        this.managers = new ArrayList<>();
        this.officers = new ArrayList<>();
        this.applicants = new ArrayList<>();

        // Fetch from excel file provided
        loadUsersFromFile(MANAGER_FILE_PATH, "Manager");
        loadUsersFromFile(OFFICER_FILE_PATH, "Officer");
        loadUsersFromFile(APPLICANT_FILE_PATH, "Applicant");
    }

    // Constructors with managers, officers, applicants passed in
    public UserRepository(List<HDBManager> managers, List<HDBOfficer> officers, List<HDBApplicant> applicants) {
        this.managers = managers;
        this.officers = officers;
        this.applicants = applicants;

        loadUsersFromFile(MANAGER_FILE_PATH, "Manager");
        loadUsersFromFile(OFFICER_FILE_PATH, "Officer");
        loadUsersFromFile(APPLICANT_FILE_PATH, "Applicant");
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
    private void loadUsersFromFile(String filePath, String role) {
        // Implement logic to read from the Excel file and populate the lists
        // This method should be called in the constructor to initialize the lists
        // For example, using Apache POI or any other library to read Excel files

        // Commented out because this only read original data file
        //List<List<String>> data = FileIO.readExcelFile(filePath);

        // This reads both original and local data file
        List<List<String>> data = FileIO.readMergedExcelFile(filePath);

        int managerId = 0, officerId = 0, applicantId = 0;

        for (List<String> row : data) {
            String name = row.get(0);
            String nric = row.get(1);
            int age = (int) Double.parseDouble(row.get(2));
            MaritalStatus maritalStatus = MaritalStatus.valueOf(row.get(3).toUpperCase());
            String password = row.get(4);

            if (role.equals("Manager")) {
                HDBManager manager = new HDBManager(managerId + 1, name, nric, age, maritalStatus, password);
                managers.add(manager);
            } else if (role.equals("Officer")) {
                HDBOfficer officer = new HDBOfficer(officerId + 1, name, nric, age, maritalStatus, password);
                officers.add(officer);
            } else if (role.equals("Applicant")) {
                // we will identify applicant by their NRIC so no need for id.
                HDBApplicant applicant = new HDBApplicant(name, nric, age, maritalStatus, password);
                applicants.add(applicant);
            }
        }
    }

    // Save User to Data File
    private void saveUsersToFile(String filePath, List<HDBManager> managers, List<HDBOfficer> officers, List<HDBApplicant> applicants) {
        List<List<String>> data = new ArrayList<>();

        // Check file path and populate data accordingly
        if (filePath.equals(MANAGER_FILE_PATH)) {
            // Prepare manager data
            for (HDBManager manager : managers) {
                List<String> row = new ArrayList<>();
                row.add(manager.getFirstName());
                row.add(manager.getNric());
                row.add(String.valueOf(manager.getAge()));
                row.add(manager.getMaritalStatus().name());
                row.add(manager.getPwd());
                data.add(row);
            }
        } else if (filePath.equals(OFFICER_FILE_PATH)) {
            // Prepare officer data
            for (HDBOfficer officer : officers) {
                List<String> row = new ArrayList<>();
                row.add(officer.getFirstName());
                row.add(officer.getNric());
                row.add(String.valueOf(officer.getAge()));
                row.add(officer.getMaritalStatus().name());
                row.add(officer.getPwd());
                data.add(row);
            }
        } else if (filePath.equals(APPLICANT_FILE_PATH)) {
            // Prepare applicant data
            for (HDBApplicant applicant : applicants) {
                List<String> row = new ArrayList<>();
                row.add(applicant.getFirstName());
                row.add(applicant.getNric());
                row.add(String.valueOf(applicant.getAge()));
                row.add(applicant.getMaritalStatus().name());
                row.add(applicant.getPwd());
                data.add(row);
            }
        }

        // Write to Excel file using FileIO utility
        FileIO.writeExcelFile(filePath, data);
    }

    // Allow user to add users not within the data file to the List, call saveUsersToFile() to save.
    public void addUser(User user) {
        if (user instanceof HDBManager manager) {
            managers.add(manager);
        } else if (user instanceof HDBOfficer officer) {
            officers.add(officer);
        } else if (user instanceof HDBApplicant applicant) {
            applicants.add(applicant);
        }
        // Save the updated list to the file
        if (user instanceof HDBManager) {
            saveUsersToFile(MANAGER_FILE_PATH, managers, null, null);
        } else if (user instanceof HDBOfficer) {
            saveUsersToFile(OFFICER_FILE_PATH, null, officers, null);
        } else if (user instanceof HDBApplicant) {
            saveUsersToFile(APPLICANT_FILE_PATH, null, null, applicants);
        }
    }

    // Retrieve a user by NRIC, looping through the lists of managers, officers, and applicants.
    // We will search in applicants first, then officers, and finally managers, since it's more likely that applicants will be required.
    // This is a simple search, and can be improved with a more efficient data structure if needed. O(n)
    // Returns an Optional<User> to handle the case where no user is found with the given NRIC.
    // This method can also be used to check if a user exists in the system.
    // This method is useful for login and other operations where we need to find a user by their NRIC.

    public Optional<User> getUserByNric(String nric) {
        for (HDBManager manager : managers) {
            if (manager.getNric().equals(nric)) {
                return Optional.of(manager);
            }
        }
        for (HDBOfficer officer : officers) {
            if (officer.getNric().equals(nric)) {
                return Optional.of(officer);
            }
        }
        for (HDBApplicant applicant : applicants) {
            if (applicant.getNric().equals(nric)) {
                return Optional.of(applicant);
            }
        }
        return Optional.empty();
    }

    public Optional<User> getUserByName(String name, UserRoles role) {
        if (role == UserRoles.APPLICANT) {
            for (HDBApplicant applicant : applicants) {
                if (applicant.getFirstName().equals(name)) {
                    return Optional.of(applicant);
                }
            }
        } else if (role == UserRoles.OFFICER) {
            for (HDBOfficer officer : officers) {
                if (officer.getFirstName().equals(name)) {
                    return Optional.of(officer);
                }
            }
        } else if (role == UserRoles.MANAGER) {
            for (HDBManager manager : managers) {
                if (manager.getFirstName().equals(name)) {
                    return Optional.of(manager);
                }
            }
        }
        // nothing found
        return Optional.empty();
    }

    // Overloaded method to get user by NRIC and role
    // This method can be used to check if a user exists in the system with a specific role.
    // This is useful for login and other operations where we need to find a user by their NRIC and role.

    public Optional<User> getUserByNric(String nric, String role) {
        if (role.equals("Applicant")) {
            for (HDBApplicant applicant : applicants) {
                if (applicant.getNric().equals(nric)) {
                    return Optional.of(applicant);
                }
            }
        } else if (role.equals("Officer")) {
            for (HDBOfficer officer : officers) {
                if (officer.getNric().equals(nric)) {
                    return Optional.of(officer);
                }
            }
        } else if (role.equals("Manager")) {
            for (HDBManager manager : managers) {
                if (manager.getNric().equals(nric)) {
                    return Optional.of(manager);
                }
            }
        }
        // nothing found
        return Optional.empty();
    }

    // Combines the list of managers, officers, and applicants into a single list of users.
    // This method can be used to get all users in the system, regardless of their role.
    // This is for displaying all users in the system or performing operations on all users.

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(managers);
        allUsers.addAll(officers);
        allUsers.addAll(applicants);
        return allUsers;
    }

    public void updateUserPassword(User userToUpdate) {
        if (userToUpdate instanceof HDBManager) {
            saveUsersToFile(MANAGER_FILE_PATH, managers, null, null);
        } else if (userToUpdate instanceof HDBOfficer) {
            saveUsersToFile(OFFICER_FILE_PATH, null, officers, null);
        } else if (userToUpdate instanceof HDBApplicant) {
            saveUsersToFile(APPLICANT_FILE_PATH, null, null, applicants);
        }
    }
}
