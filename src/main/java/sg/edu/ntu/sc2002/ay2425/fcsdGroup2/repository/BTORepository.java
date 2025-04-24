package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.factory.FlatTypeFactory;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.FileIO;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BTORepository implements BTOStorageProvider {
    private static UserRepository userRepo = UserRepository.getInstance();
    private static BTORepository instance;

    // Excel file path
    private static final String PROJECTS_FILE_PATH = "data/ProjectList.xlsx";
    private static final String EXERCISES_FILE_PATH = "data/ExerciseList.xlsx";
    private static final String APPLICATIONS_FILE_PATH = "data/ApplicationLists.xlsx";

    // Container for the projects & exercises
    private List<BTOProj> projects;
    private List<BTOExercise> exercises;
    private List<Application> applications;

    private BTORepository() {
        // Initialize new arraylists for projects and exercises
        projects = new ArrayList<>();
        exercises = new ArrayList<>();
        applications = new ArrayList<>();

        // Load projects and exercises from the Excel files
        loadProjectsFromFile(PROJECTS_FILE_PATH);
        loadExercisesFromFile(EXERCISES_FILE_PATH);
        loadApplicationsFromFile(APPLICATIONS_FILE_PATH);

        for (BTOProj proj : projects) {
            HDBManager manager = proj.getManagerIC(); // assuming this returns the assigned manager

        }
    }

    public static BTORepository getInstance() {
        if (instance == null) {
            instance = new BTORepository();
        }
        return instance;
    }

    private void loadProjectsFromFile(String filePath) {
        projects.clear();
        List<List<String>> data;

        File localFile = new File(filePath);
        if (localFile.exists()) {
            data = FileIO.readExcelFileLocal(filePath);
        } else {
            data = FileIO.readMergedExcelFile(filePath);
        }


        if (data == null || data.isEmpty()) {
            System.out.println("There are no projects in the file.");
            return;
        }

        for (List<String> row : data) {
            // A - Project Name
            String projectName = row.get(0);
            if (projectName == null || projectName.trim().isEmpty()) {
                System.out.println("Skipping row with invalid project name: " + row);
                continue;
            }
            projectName = projectName.trim();

            int projectId;
            try {
                projectId = (int) Double.parseDouble(row.get(1).trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid or missing project ID in row: " + row);
                continue;
            }

            // C - Neighbourhood
            String projectNeighborhood = row.get(2).trim();
            Neighbourhoods neighborhood;
            try {
                neighborhood = Neighbourhoods.valueOf(projectNeighborhood.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid neighbourhood: \"" + projectNeighborhood + "\" in row: " + row);
                continue;
            }

            // D-I - FlatType
            FlatType type1 = null;
            FlatType type2 = null;

            if (!row.get(3).isEmpty()) {
                try {
                    type1 = FlatTypeFactory.fromRow(row, 3); // 2-Room
                } catch (Exception e) {
                    System.out.println("Error parsing 2-Room: " + e.getMessage());
                }
            }

            if (!row.get(6).isEmpty()) {
                try {
                    type2 = FlatTypeFactory.fromRow(row, 6); // 3-Room
                } catch (Exception e) {
                    System.out.println("Error parsing 3-Room: " + e.getMessage());
                }
            }

            List<FlatType> flatTypes = new ArrayList<>();
            if (type1 != null) flatTypes.add(type1);
            if (type2 != null) flatTypes.add(type2);

            // J-K - Open Date, Close Date
            LocalDateTime appOpenDate = convertExcelDateToLocalDateTime(Double.parseDouble(row.get(9)));
            LocalDateTime appCloseDate = convertExcelDateToLocalDateTime(Double.parseDouble(row.get(10)))
                    .withHour(23).withMinute(59).withSecond(59);

            // L - Manager
            Optional<User> manager = userRepo.getUserByName(row.get(11), UserRoles.MANAGER);
            HDBManager assignedManager = manager.isPresent() ? (HDBManager) manager.get() : null;

            // M - Officer Slot
            int officerSlot = 0;
            try {
                officerSlot = (int) Double.parseDouble(row.get(12));
            } catch (NumberFormatException e) {
                System.out.println("Invalid officer slot value: " + row.get(12));
            }

            // N - Officer List
            List<HDBOfficer> officerList = new ArrayList<>();
            if (row.size() > 13 && !row.get(13).isEmpty()) {
                String[] officerNames = row.get(13).split(",");
                for (String name : officerNames) {
                    Optional<User> user = userRepo.getUserByName(name.trim(), UserRoles.OFFICER);
                    user.ifPresent(u -> officerList.add((HDBOfficer) u));
                }
            }

            // O - Project Visibility
            Boolean visibilityOverride = null;
            if (row.size() >= 15) {
                String colN = row.get(14);
                if (colN != null && !colN.trim().isEmpty()) {
                    String visibilityStr = colN.trim().toLowerCase();
                    if (visibilityStr.equals("true") || visibilityStr.equals("1")) {
                        visibilityOverride = true;
                    } else if (visibilityStr.equals("false") || visibilityStr.equals("0")) {
                        visibilityOverride = false;
                    } else {
                        System.out.println("Unrecognized visibility value in row: " + visibilityStr);
                    }
                } else {
                    System.out.println("Visibility column is empty or null.");
                }
            } else {
                System.out.println("Row has insufficient columns: " + row.size());
            }

            // === Create and Add Project ===
            BTOProj project = new BTOProj(
                    projectId,
                    projectName,
                    neighborhood,
                    flatTypes,
                    appOpenDate,
                    appCloseDate,
                    assignedManager,
                    officerSlot,
                    officerList,
                    visibilityOverride
            );

            try {
                assignedManager.assignProj(project);
            } catch (NullPointerException e) {
                System.out.println("Manager not found for project: " + projectName + ": " + e.getMessage());
            }

            for (FlatType ft : new ArrayList<>(flatTypes)) {
                try {
                    FlatTypes type = FlatTypes.fromDisplayName(ft.getTypeName());
                    project.addFlatTypeWithPrice(type, ft.getTotalUnits(), ft.getSellingPrice());
                } catch (IllegalArgumentException e) {
                    System.out.println("Could not map flat type: " + ft.getTypeName());
                }
            }
            projects.add(project);
        }

    }


    private void loadExercisesFromFile(String filePath) {
        exercises.clear();

        List<List<String>> data;

        File localFile = new File(filePath);
        if (localFile.exists()) {
            data = FileIO.readExcelFileLocal(filePath);
        } else {
            data = FileIO.readMergedExcelFile(filePath);
        }

        for (List<String> row : data) {
            try {
                String exerciseName = row.get(0);
                int exerciseId = (int) Double.parseDouble(row.get(1));
                int totalApplicants = (int) Double.parseDouble(row.get(2));
                ProjStatus status = ProjStatus.valueOf(row.get(3).toUpperCase());

                // For the projects
                String projectInExercise = row.get(4);
                List<BTOProj> projectsInExercise = new ArrayList<>();

                if (!projectInExercise.isEmpty()) {
                    String[] projectNames = projectInExercise.split(",");

                    for (String name : projectNames) {
                        String trimmed = name.trim();

                        Optional<BTOProj> match = projects.stream()
                                .filter(p -> p.getProjName() != null && p.getProjName().equalsIgnoreCase(trimmed))
                                .findFirst();

                        if (match.isPresent()) {
                            projectsInExercise.add(match.get());
                        } else {
                            System.out.println("Project name not found: " + trimmed);
                        }
                    }
                }

                // Create the BTOExercise object
                BTOExercise exercise = new BTOExercise(exerciseId, exerciseName, totalApplicants, status, projectsInExercise);
                exercises.add(exercise);

                // Assign the exercise to the projects
                for (BTOProj project : projectsInExercise) {
                    project.setExercise(exercise);
                }
            } catch (Exception e) {
                System.out.println("Error parsing exercise row: " + row);
                e.printStackTrace();
            }
        }
    }

    private void loadApplicationsFromFile(String filePath) {
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
                Optional<User> user = userRepo.getUserByName(applicantNric, UserRoles.APPLICANT);
                if (user.isEmpty()) {
                    continue;
                }
                HDBApplicant applicant = (HDBApplicant) user.get();

                // B - Application ID
                int appId = (int) Double.parseDouble(row.get(1).trim());

                // C - Project ID
                int projId = (int) Double.parseDouble(row.get(2).trim());
                Optional<BTOProj> matchedProject = projects.stream()
                        .filter(p -> p.getProjId() == projId)
                        .findFirst();
                if (matchedProject.isEmpty()) {
                    continue;
                }
                BTOProj project = matchedProject.get();

                // D - Application Status
                ApplicationStatus status = ApplicationStatus.valueOf(row.get(3).trim().toUpperCase());

                // E - Flat Type
                FlatTypes flatTypeEnum = FlatTypes.fromDisplayName(row.get(4).trim());
                FlatType flatType = project.getFlatUnits().get(flatTypeEnum);

                // F - Flat Booked (optional)
                String flatStr = row.get(5).trim();
                Flat bookedFlat = null;
                if (!flatStr.isEmpty()) {
                    bookedFlat = parseFlatFromString(flatStr, flatType, project);
                }

                // G - Previous Status (optional)
                ApplicationStatus previousStatus = null;
                if (row.size() > 6 && !row.get(6).trim().isEmpty()) {
                    previousStatus = ApplicationStatus.valueOf(row.get(6).trim().toUpperCase());
                }

                // Create and populate the Application object
                Application app = new Application(appId, applicant, project);

                // Use reflection to patch internal fields
                if (flatType != null) {
                    java.lang.reflect.Field ftField = Application.class.getDeclaredField("flatType");
                    ftField.setAccessible(true);
                    ftField.set(app, flatType);
                }

                java.lang.reflect.Field statusField = Application.class.getDeclaredField("status");
                statusField.setAccessible(true);
                statusField.set(app, status);

                if (bookedFlat != null) {
                    java.lang.reflect.Field bookedField = Application.class.getDeclaredField("flatBooked");
                    bookedField.setAccessible(true);
                    bookedField.set(app, bookedFlat);
                }

                if (previousStatus != null) {
                    java.lang.reflect.Field prevStatusField = Application.class.getDeclaredField("previousStatus");
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

    private Flat parseFlatFromString(String flatStr, FlatType type, BTOProj project) {
        try {
            String cleaned = flatStr.replace("Blk ", "").trim(); // "10 03-105"
            String[] parts = cleaned.split(" ");
            int blockNo = Integer.parseInt(parts[0]);
            String[] floorUnit = parts[1].split("-");

            int floor = Integer.parseInt(floorUnit[0]);
            int unit = Integer.parseInt(floorUnit[1]);

            for (Block block : project.getBlocks()) {
                if (block.getBlkNo() == blockNo) {
                    return new Flat(floor, unit, FlatBookingStatus.BOOKED, type, block);
                }
            }

            System.out.println("Block not found for booked flat: " + flatStr);
            return null;

        } catch (Exception e) {
            System.out.println("Invalid flat string: " + flatStr);
            return null;
        }
    }


    @Override
    public List<BTOProj> getAllProjects() {
        return projects;
    }

    @Override
    public List<BTOExercise> getAllExercises() {
        return exercises;
    }

    @Override
    public List<Application> getAllApplications() { return applications;}

    public void addProject(BTOProj project) {
        projects.add(project);
        this.saveProject();
    }

    @Override
    public void addExercise(BTOExercise exercise) {
        exercises.add(exercise);
        this.saveExercise();
    }

    @Override
    public void addApplication(Application application) {
        applications.add(application);
        this.saveApplication();
    }

    @Override
    public void saveProject() {
        System.out.println("Saving project...");
        List<List<String>> rows = new ArrayList<>();

        for (BTOProj proj : projects) {
            List<String> row = new ArrayList<>();

            // A - Project Name
            row.add(proj.getProjName());

            // B - Project ID
            row.add(String.valueOf(proj.getProjId()));

            // C - Neighbourhood
            row.add(proj.getProjNbh() != null ? proj.getProjNbh().name() : "");

            // D-F - Type 1: Name, Units, Price
            FlatType type1 = proj.getFlatUnits().get(FlatTypes.TWO_ROOM);
            row.add(type1 != null ? type1.getTypeName() : "");
            row.add(type1 != null ? String.valueOf(type1.getTotalUnits()) : "");
            row.add(type1 != null ? String.valueOf(type1.getSellingPrice()) : "");

            // G-I - Type 2: Name, Units, Price
            FlatType type2 = proj.getFlatUnits().get(FlatTypes.THREE_ROOM);
            row.add(type2 != null ? type2.getTypeName() : "");
            row.add(type2 != null ? String.valueOf(type2.getTotalUnits()) : "");
            row.add(type2 != null ? String.valueOf(type2.getSellingPrice()) : "");

            // J - Application Open Date
            row.add(String.valueOf(convertLocalDateToExcelDate(proj.getAppOpenDate().toLocalDate())));

            // K - Application Close Date
            row.add(String.valueOf(convertLocalDateToExcelDate(proj.getAppCloseDate().toLocalDate())));

            // L - Manager
            row.add(proj.getManagerIC() != null ? proj.getManagerIC().getFirstName() : "");

            // M - Officer Slot Limit
            row.add(String.valueOf(proj.getOfficerSlots()));

            // N - Officers (comma separated)
            StringBuilder officers = new StringBuilder();
            HDBOfficer[] officerArray = proj.getOfficersList();
            if (officerArray != null) {
                for (HDBOfficer o : officerArray) {
                    if (!officers.isEmpty()) officers.append(",");
                    officers.append(o.getFirstName());
                }
            }
            row.add(officers.toString());

            // O - Visibility
            row.add(String.valueOf(proj.getVisibility()));

            rows.add(row);
        }

        FileIO.writeExcelFile(PROJECTS_FILE_PATH, rows);
    }


    @Override
    public void saveExercise() {
        List<List<String>> rows = new ArrayList<>();

        for (BTOExercise ex : exercises) {
            List<String> row = new ArrayList<>();

            // A - Exercise Name
            row.add(ex.getExerciseName());

            // B - Exercise ID
            row.add(String.valueOf(ex.getExerciseId()));

            // C - Total Applicants
            row.add(String.valueOf(ex.getTotalApplicants()));

            // D - Status
            row.add(ex.getProjStatus().name());

            // E - Project IDs (CSV)
            StringBuilder projectNames = new StringBuilder();
            for (BTOProj proj : ex.getExerciseProjs()) {
                if (!projectNames.isEmpty()) projectNames.append(", ");
                projectNames.append(proj.getProjName());
            }
            row.add(projectNames.toString());

            rows.add(row);
        }

        FileIO.writeExcelFile(EXERCISES_FILE_PATH, rows);
    }

    @Override
    public void saveApplication() {

    }


    private LocalDateTime convertExcelDateToLocalDateTime(double excelDate) {
        return LocalDate.of(1899, 12, 30).plusDays((long) excelDate).atStartOfDay();
    }

    private double convertLocalDateToExcelDate(LocalDate date) {
        return (double) date.toEpochDay() - LocalDate.of(1899, 12, 30).toEpochDay();
    }

    public BTOProj getProjById(int projId) {
        for (BTOProj proj : projects) {
            if (proj.getProjId() == projId) {
                return proj;
            }
        }

        // return null if not found - handle with care.
        return null;
    }

    public BTOProj getProjByName(String name) {
        for (BTOProj proj : projects) {
            if (proj.getProjName().equalsIgnoreCase(name)) {
                return proj;
            }
        }

        // return null if not found - handle with care.
        return null;
    }
}
