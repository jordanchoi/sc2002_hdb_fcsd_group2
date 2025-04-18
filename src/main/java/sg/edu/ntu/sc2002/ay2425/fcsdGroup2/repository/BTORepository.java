package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.factory.FlatTypeFactory;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.Neighbourhoods;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.UserRoles;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.FileIO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BTORepository implements BTOStorageProvider {
    private static final UserRepository userRepo = new UserRepository();

    // Excel file path
    private static final String PROJECTS_FILE_PATH = "data/ProjectList.xlsx";
    private static final String EXERCISES_FILE_PATH = "data/ExerciseList.xlsx";

    // Container for the projects & exercises
    private List<BTOProj> projects;
    private List<BTOExercise> exercises;

    public BTORepository() {
        // Initialize new arraylists for projects and exercises
        projects = new ArrayList<>();
        exercises = new ArrayList<>();

        // Load projects and exercises from the Excel files
        loadProjectsFromFile(PROJECTS_FILE_PATH);
        loadExercisesFromFile(EXERCISES_FILE_PATH);
    }

    private void loadProjectsFromFile(String filePath) {
        // Load projects from the Excel file
        // This method should read the Excel file and populate the projects list
        List<List<String>> data = FileIO.readMergedExcelFile(filePath);

        // column structure as follows
        // Project Name/Neighborhood/Type 1/Number of units for Type 1/Selling price for Type 1/Type 2/Number of units for Type 2/Selling price for Type 2/Application opening date/Application closing date/Manager/Officer Slot/Officer

        Logger logger = LogManager.getLogger(BTORepository.class);
        // for code debugging
        if (data == null) {
            logger.debug("Data is null – file not read or path is wrong.");
            return;
        }

        if (data.isEmpty()) {
            logger.debug("Data is empty – file read but no content.");
            return;
        }

        logger.debug("Data is empty – file read but no content." + data.size());
        // ends here

        if (data == null || data.isEmpty()) {
            logger.debug("Data is empty – file read but no content." + data.size());
            return;
        }

        // Loop through the data and create BTOProj objects and add them to the projects list

        for (int i = 0; i < data.size(); i++) {
            // Project Name
            List<String> row = data.get(i);
            String projectName = row.get(0);

            // Parse Neighbourhoods
            Neighbourhoods neighborhood;
            try {
                neighborhood = Neighbourhoods.valueOf(row.get(1).toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown neighbourhood: " + row.get(1));
                continue;
            }

            // Create the FlatTypes object - reuse from HDBFlatsController
            List<FlatType> flatTypes = new ArrayList<>();

            boolean type1Valid = !row.get(2).isEmpty() && !row.get(3).isEmpty() && !row.get(4).isEmpty();
            if (type1Valid) {
                flatTypes.add(FlatTypeFactory.fromRow(row, 2));
            }


            boolean type2Valid = !row.get(5).isEmpty() && !row.get(6).isEmpty() && !row.get(7).isEmpty();
            if (type2Valid) {
                flatTypes.add(FlatTypeFactory.fromRow(row, 5));
            }


            // Parse the application opening and closing dates
            double excelDateOpen = Double.parseDouble(row.get(8));
            double excelDateClose = Double.parseDouble(row.get(9));

            LocalDateTime appOpenDate = convertExcelDateToLocalDateTime(excelDateOpen);
            LocalDateTime appCloseDate = convertExcelDateToLocalDateTime(excelDateClose).withHour(23).withMinute(59).withSecond(59);

            // Search for the Manager in UserRepository
            Optional<User> manager = userRepo.getUserByName(row.get(10), UserRoles.MANAGER);
            // If found, cast to HDBManager, null otherwise
            HDBManager assignedManager = manager.isPresent() ? (HDBManager) manager.get() : null;
            String officerSlotStr = row.get(11);
            int officerSlot = 0;

            try {
                officerSlot = (int) Double.parseDouble(officerSlotStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid officer slot value in row " + i + ": " + officerSlotStr);
                continue; // Skip invalid row or set default value
            }
            //int officerSlot = (int) Double.parseDouble(row.get(11));
            //String officers = row.get(11);

            List<HDBOfficer> officerList = new ArrayList<>();
            // Extract the officer string
            String officerString = row.get(12);
            if (!officerString.isEmpty()) {
                String[] officerExtractedList = officerString.split(",");
                for (String officerName : officerExtractedList) {
                    Optional<User> officerUser = userRepo.getUserByName(officerName.trim(), UserRoles.OFFICER);
                    officerUser.ifPresent(user -> officerList.add((HDBOfficer) user));
                }
            }

            // Create the BTOProj object
            BTOProj project = new BTOProj(i, projectName, neighborhood, flatTypes, appOpenDate, appCloseDate, assignedManager, officerSlot, officerList);
            projects.add(project);
        }
    }

    private void loadExercisesFromFile(String filePath) {
        List<List<String>> data = FileIO.readMergedExcelFile(filePath);

        // column structure as follows

        if (data == null || data.isEmpty()) {
            System.out.println("No BTO Exercises found in file.");
            return;
        }

        for (List<String> row : data) {
            try {
                int exerciseId = (int) Double.parseDouble(row.get(0));
                String exerciseName = row.get(1);
                int totalApplicants = (int) Double.parseDouble(row.get(2));
                ProjStatus status = ProjStatus.valueOf(row.get(3).toUpperCase());

                // For the projects
                String projectInExercise = row.get(4);
                List<BTOProj> projectsInExercise = new ArrayList<>();

                if (!projectInExercise.isEmpty()) {
                    String[] projectNames = projectInExercise.split(",");

                    // Loop through the project names and find the corresponding BTOProj objects
                    for (String projectName : projectNames) {
                        Optional<BTOProj> project = projects.stream()
                                .filter(p -> p.getProjectName().equalsIgnoreCase(projectName.trim()))
                                .findFirst();
                        project.ifPresent(projectsInExercise::add);
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
                System.out.println("Error parsing exercise data: " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("Loaded " + exercises.size() + " exercises from file.");
    }

    @Override
    public List<BTOProj> getAllProjects() {
        return projects;
    }

    @Override
    public List<BTOExercise> getAllExercises() {
        return exercises;
    }

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
    public void saveProject() {
        List<List<String>> rows = new ArrayList<>();

        for (BTOProj proj : projects) {
            List<String> row = new ArrayList<>();

            // Column indices based on loading
            row.add(proj.getProjName());
            row.add(proj.getProjNbh().name());

            // Type 1
            FlatType type1 = proj.getAvailableFlatTypes().size() > 0 ? proj.getAvailableFlatTypes().get(0) : null;
            row.add(type1 != null ? type1.getTypeName() : "");
            row.add(type1 != null ? String.valueOf(type1.getTotalUnits()) : "");
            row.add(type1 != null ? String.valueOf(type1.getSellingPrice()) : "");

            // Type 2
            FlatType type2 = proj.getAvailableFlatTypes().size() > 1 ? proj.getAvailableFlatTypes().get(1) : null;
            row.add(type2 != null ? type2.getTypeName() : "");
            row.add(type2 != null ? String.valueOf(type2.getTotalUnits()) : "");
            row.add(type2 != null ? String.valueOf(type2.getSellingPrice()) : "");

            row.add(String.valueOf(convertLocalDateToExcelDate(proj.getAppOpenDate().toLocalDate())));
            row.add(String.valueOf(convertLocalDateToExcelDate(proj.getAppCloseDate().toLocalDate())));

            row.add(proj.getManagerIC() != null ? proj.getManagerIC().getFirstName() : "");
            row.add(String.valueOf(proj.getOfficerSlots()));

            StringBuilder officers = new StringBuilder();
            for (HDBOfficer o : proj.getOfficersList()) {
                if (!officers.isEmpty()) officers.append(",");
                officers.append(o.getFirstName());
            }
            row.add(officers.toString());

            rows.add(row);
        }

        FileIO.writeExcelFile(PROJECTS_FILE_PATH, rows);
    }

    @Override
    public void saveExercise() {
        List<List<String>> rows = new ArrayList<>();

        for (BTOExercise ex : exercises) {
            List<String> row = new ArrayList<>();
            row.add(String.valueOf(ex.getExerciseId()));
            row.add(ex.getExerciseName());
            row.add(String.valueOf(ex.getTotalApplicants()));
            row.add(ex.getProjStatus().name());

            // Save project names as comma-separated
            StringBuilder projectNames = new StringBuilder();
            for (BTOProj proj : ex.getExerciseProjs()) {
                if (!projectNames.isEmpty()) projectNames.append(",");
                projectNames.append(proj.getProjName());
            }
            row.add(projectNames.toString());

            rows.add(row);
        }

        FileIO.writeExcelFile(EXERCISES_FILE_PATH, rows);
    }

    private LocalDateTime convertExcelDateToLocalDateTime(double excelDate) {
        return LocalDate.of(1899, 12, 30).plusDays((long) excelDate).atStartOfDay();
    }

    private double convertLocalDateToExcelDate(LocalDate date) {
        return (double) date.toEpochDay() - LocalDate.of(1899, 12, 30).toEpochDay();
    }


}
