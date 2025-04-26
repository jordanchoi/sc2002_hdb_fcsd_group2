package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.AssignStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.FileIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing Officer Project Applications.
 * Handles loading, saving, adding, updating, and deleting officer project applications.
 */
public class ProjectApplicationRepository {
    private static final String FILE_PATH = "data/ProjectApplicationList.xlsx";
    private final List<OfficerProjectApplication> officerApplications;

    private final UserRepository userRepo;
    private final BTORepository btoRepo;

    public ProjectApplicationRepository(UserRepository userRepo, BTORepository btoRepo) {
        this.userRepo = userRepo;
        this.btoRepo = btoRepo;
        this.officerApplications = new ArrayList<>();
        loadFromFile();
    }
    /**
     * Loads officer project applications from the Excel file.
     */
    private void loadFromFile() {
        officerApplications.clear();
        List<List<String>> data;

        File localFile = new File(FILE_PATH);
        if (localFile.exists()) {
            data = FileIO.readExcelFileLocal(FILE_PATH);
        } else {
            data = FileIO.readMergedExcelFile(FILE_PATH);
        }

        for (List<String> row : data) {
            if (row.size() < 4 || row.get(0).equalsIgnoreCase("Officer NRIC")) continue;
            try {
                String officerNric = row.get(0).trim();
                Optional<User> user = userRepo.getUserByNric(officerNric);
                if (user.isEmpty() || !(user.get() instanceof HDBOfficer)) continue;
                HDBOfficer officer = (HDBOfficer) user.get();

                int officerAppId = (int) Double.parseDouble(row.get(1).trim());

                int projId = (int) Double.parseDouble(row.get(2).trim());

                AssignStatus status = AssignStatus.valueOf(row.get(3).trim().toUpperCase());

                BTOProj project = btoRepo.getProjById(projId);
                if (project == null) {
                    continue;
                }

                officerApplications.add(new OfficerProjectApplication(officerAppId, officer, project, status));
            } catch (Exception e) {
                System.out.println("Error parsing row: " + row);
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves officer project applications to the Excel file.
     */
    public void saveToFile() {
        List<List<String>> data = new ArrayList<>();
        data.add(List.of("Officer NRIC", "Application ID", "Project ID", "Status"));

        for (OfficerProjectApplication app : officerApplications) {
            data.add(List.of(
                    app.getOfficer().getNric(),
                    String.valueOf(app.getOfficerAppId()),
                    String.valueOf(app.getProj().getProjId()),
                    app.getAssignmentStatus().name()
            ));
        }

        FileIO.writeExcelFile(FILE_PATH, data);
    }

    /**
     * Adds a new officer project application and saves.
     *
     * @param app the application to add
     */
    public void add(OfficerProjectApplication app) {
        officerApplications.add(app);
        saveToFile();
    }

    /**
     * Updates an existing officer project application by replacing it.
     *
     * @param updated the updated application
     */
    public void update(OfficerProjectApplication updated) {
        delete(updated.getOfficerAppId());
        add(updated);
    }

    /**
     * Deletes an officer project application by application ID.
     *
     * @param officerAppId ID of the application to delete
     */
    public void delete(int officerAppId) {
        officerApplications.removeIf(app -> app.getOfficerAppId() == officerAppId);
        saveToFile();
    }

    /**
     * Retrieves all officer project applications.
     *
     * @return list of applications
     */
    public List<OfficerProjectApplication> getAll() {
        return new ArrayList<>(officerApplications);
    }

    /**
     * Retrieves applications related to a specific project.
     *
     * @param projId project ID
     * @return list of applications
     */
    public List<OfficerProjectApplication> getByProjectId(int projId) {
        List<OfficerProjectApplication> result = new ArrayList<>();
        for (OfficerProjectApplication app : officerApplications) {
            if (app.getProj().getProjId() == projId) {
                result.add(app);
            }
        }
        return result;
    }

    /**
     * Retrieves applications submitted by a specific officer.
     *
     * @param nric officer NRIC
     * @return list of applications
     */
    public List<OfficerProjectApplication> getByOfficerNric(String nric) {
        List<OfficerProjectApplication> result = new ArrayList<>();
        for (OfficerProjectApplication app : officerApplications) {
            if (app.getOfficer().getNric().equals(nric)) {
                result.add(app);
            }
        }
        return result;
    }
}