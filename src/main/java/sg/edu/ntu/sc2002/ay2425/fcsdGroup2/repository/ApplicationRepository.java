package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.FileIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class to manage Application entities.
 * Handles loading, saving, and manipulating applications from Excel files.
 */
public class ApplicationRepository {
    private static final String APPLICATION_FILE_PATH = "data/ApplicationLists.xlsx";
    private final List<Application> applications;

    private final BTORepository btoRepo;
    private final UserRepository userRepo;

    /**
     * Constructs a new ApplicationRepository.
     *
     * @param btoRepo BTO repository for project lookup
     * @param userRepo User repository for applicant lookup
     */
    public ApplicationRepository(BTORepository btoRepo, UserRepository userRepo) {
        this.applications = new ArrayList<>();
        this.btoRepo = btoRepo;
        this.userRepo = userRepo;
        loadApplicationsFromFile(APPLICATION_FILE_PATH, userRepo, btoRepo);
    }

    /** @return all applications. */
    public List<Application> getApplications() {
        return applications;
    }

    /**
     * Loads application data from the file.
     */
    private void loadApplicationsFromFile(String filePath, UserRepository userRepo, BTORepository btoRepo) {
        applications.clear();

        List<List<String>> data;

        File localFile = new File(filePath);
        if (localFile.exists()) {
            data = FileIO.readExcelFileLocal(filePath);
        } else {
            data = FileIO.readMergedExcelFile(filePath);
        }

        for (List<String> row : data) {
            try {
                // A - Applicant NRIC
                String applicantNric = row.get(0).trim();
                Optional<User> user = userRepo.getUserByNric(applicantNric);
                if (user.isEmpty() || !(user.get() instanceof HDBApplicant)) {
                    continue;
                }
                HDBApplicant applicant = (HDBApplicant) user.get();

                // B - Application ID
                int appId = (int) Double.parseDouble(row.get(1).trim());

                // C - Project ID
                int projId = (int) Double.parseDouble(row.get(2).trim());
                BTOProj project = btoRepo.getProjById(projId);
                if (project == null) {
                    continue;
                }

                // D - Application Status
                ApplicationStatus status = ApplicationStatus.valueOf(row.get(3).trim().toUpperCase());

                // E - Flat Type
                FlatTypes flatTypeEnum = FlatTypes.fromDisplayName(row.get(4).trim());
                FlatType flatType = project.getFlatUnits().get(flatTypeEnum);

                // F - Booked Flat
                String flatStr = row.get(5).trim();
                Flat bookedFlat = parseFlatFromString(flatStr, flatType, project);

                // G - Previous Status (optional)
                ApplicationStatus previousStatus = null;
                if (row.size() > 6 && !row.get(6).trim().isEmpty()) {
                    previousStatus = ApplicationStatus.valueOf(row.get(6).trim().toUpperCase());
                }

                // === Create and Patch Application ===
                Application app = new Application(appId, applicant, project);

                if (flatType != null) {
                    var ftField = Application.class.getDeclaredField("flatType");
                    ftField.setAccessible(true);
                    ftField.set(app, flatType);
                }

                var statusField = Application.class.getDeclaredField("status");
                statusField.setAccessible(true);
                statusField.set(app, status);

                if (bookedFlat != null) {
                    var bookedField = Application.class.getDeclaredField("flatBooked");
                    bookedField.setAccessible(true);
                    bookedField.set(app, bookedFlat);
                }

                if (previousStatus != null) {
                    var prevStatusField = Application.class.getDeclaredField("previousStatus");
                    prevStatusField.setAccessible(true);
                    prevStatusField.set(app, previousStatus);
                }

                applications.add(app);
            } catch (Exception e) {
                System.out.println("Error parsing application row: " + row);
                e.printStackTrace();
            }
        }
    }

    /**
     * Parses a flat object from a formatted flat string.
     *
     * @param flatStr formatted flat string (e.g., \"Blk 10 03-105\")
     * @param type flat type
     * @param project related project
     * @return parsed Flat object or null
     */
    private Flat parseFlatFromString(String flatStr, FlatType type, BTOProj project) {
        try {
            // Skip if placeholder or invalid
            if (flatStr == null || flatStr.trim().equals("-") || flatStr.trim().isEmpty()) {
                return null;
            }

            String cleaned = flatStr.replace("Blk ", "").trim(); // e.g. "10 03-105"
            String[] parts = cleaned.split(" ");
            if (parts.length < 2) return null;

            int blockNo = Integer.parseInt(parts[0]);

            String[] floorUnit = parts[1].split("-");
            if (floorUnit.length < 2) return null;

            int floor = Integer.parseInt(floorUnit[0]);
            int unit = Integer.parseInt(floorUnit[1]);

            // Always create and add the flat to the appropriate block
            Block matchingBlock = null;

            for (Block block : project.getBlocks()) {
                if (block.getBlkNo() == blockNo) {
                    matchingBlock = block;
                    break;
                }
            }

            // If block not found, create a dummy one
            if (matchingBlock == null) {
                matchingBlock = new Block("Unknown", blockNo, "000000", new ArrayList<>(), project);
                project.getBlocks().add(matchingBlock);
            }

            Flat newFlat = new Flat(floor, unit, FlatBookingStatus.BOOKED, type, matchingBlock);
            matchingBlock.getFlatsListing().add(newFlat);

            return newFlat;

        } catch (Exception e) {
            System.out.println("Error parsing flat from string: " + flatStr);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Saves all applications back to the Excel file.
     */
    public void saveToFile() {
        List<List<String>> data = new ArrayList<>();
        data.add(List.of("Applicant NRIC", "Application ID", "Project ID", "Status", "Flat Type", "Booked Flat", "Previous Status"));

        for (Application app : applications) {
            String bookedFlat = "-";
            if (app.getFlat() != null) {
                Flat flat = app.getFlat();
                bookedFlat = "Blk " + flat.getBlock().getBlkNo() + " " + flat.getFloorNo() + "-" + flat.getUnitNo();
            }

            String previousStatus = (app.getPreviousStatus() != null) ? app.getPreviousStatus().name() : "";
            String flatTypeName = (app.getFlatType() != null) ? app.getFlatType().getTypeName() : "NIL";

            data.add(List.of(
                    app.getApplicant().getNric(),
                    String.valueOf(app.getAppId()),
                    String.valueOf(app.getProject().getProjId()),
                    app.getStatus(),
                    flatTypeName,
                    bookedFlat,
                    previousStatus
            ));
        }
        FileIO.writeExcelFile(APPLICATION_FILE_PATH, data);
    }

    /**
     * Adds a new application to the repository and saves.
     *
     * @param application the application to add
     */
    public void add(Application application) {
        applications.add(application);
        saveToFile();
    }

    /**
     * Updates an existing application.
     *
     * @param updated the updated application
     */
    public void update(Application updated) {
        delete(updated.getAppId());
        add(updated);
    }

    /**
     * Deletes an application by ID.
     *
     * @param id application ID
     */
    public void delete(int id) {
        applications.removeIf(app -> app.getAppId() == id);
        saveToFile();
    }

    /** @return a copy of all applications. */
    public List<Application> getAll() {
        return new ArrayList<>(applications);
    }

    /**
     * Retrieves applications by project ID.
     *
     * @param projId the project ID
     * @return list of applications
     */
    public List<Application> getApplicationsByProjectId(int projId) {
        List<Application> result = new ArrayList<>();
        for (Application app : applications) {
            if (app.getProject().getProjId() == projId) {
                result.add(app);
            }
        }
        return result;
    }

    /**
     * Retrieves an application by applicant's NRIC.
     *
     * @param nric the NRIC
     * @return matching application or null
     */
    public Application getApplicationByNric(String nric) {
        for (Application app : applications) {
            if (app.getApplicant().getNric().equalsIgnoreCase(nric)) {
                return app;
            }
        }
        return null;
    }
}
