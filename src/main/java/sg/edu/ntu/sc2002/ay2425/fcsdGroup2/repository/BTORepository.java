package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.factory.FlatTypeFactory;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
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
        List<List<String>> data = FileIO.readMergedExcelFile(filePath);

        if (data == null || data.isEmpty()) {
            System.out.println("There are no projects in the file.");
            return;
        }

        // Counter for projectId
        int projectIdCounter = 1;

        for (List<String> row : data) {
            // === Validate Project Name ===
            String projectName = row.get(0);
            if (projectName == null || projectName.trim().isEmpty()) {
                System.out.println("⚠ Skipping row with invalid project name: " + row);
                continue;
            }
            projectName = projectName.trim();

            // === Parse Neighbourhood ===
            String projectNeighborhood = row.get(1).trim();
            Neighbourhoods neighborhood;
            try {
                neighborhood = Neighbourhoods.valueOf(projectNeighborhood.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("⚠ Invalid neighbourhood: \"" + projectNeighborhood + "\" in row: " + row);
                continue;
            }

            // === Flat Types ===
            FlatType type1 = null;
            FlatType type2 = null;

            if (!row.get(2).isEmpty()) {
                try {
                    type1 = FlatTypeFactory.fromRow(row, 2); // 2-Room
                } catch (Exception e) {
                    System.out.println("⚠ Error parsing 2-Room: " + e.getMessage());
                }
            }

            if (!row.get(5).isEmpty()) {
                try {
                    type2 = FlatTypeFactory.fromRow(row, 5); // 3-Room
                } catch (Exception e) {
                    System.out.println("⚠ Error parsing 3-Room: " + e.getMessage());
                }
            }

            List<FlatType> flatTypes = new ArrayList<>();
            if (type1 != null) flatTypes.add(type1);
            if (type2 != null) flatTypes.add(type2);

            // === Dates ===
            LocalDateTime appOpenDate = convertExcelDateToLocalDateTime(Double.parseDouble(row.get(8)));
            LocalDateTime appCloseDate = convertExcelDateToLocalDateTime(Double.parseDouble(row.get(9)))
                    .withHour(23).withMinute(59).withSecond(59);

            // === Manager ===
            Optional<User> manager = userRepo.getUserByName(row.get(10), UserRoles.MANAGER);
            HDBManager assignedManager = manager.isPresent() ? (HDBManager) manager.get() : null;

            // === Officer Slot ===
            int officerSlot = 0;
            try {
                officerSlot = (int) Double.parseDouble(row.get(11));
            } catch (NumberFormatException e) {
                System.out.println("Invalid officer slot value: " + row.get(11));
            }

            // === Officer List ===
            List<HDBOfficer> officerList = new ArrayList<>();
            if (row.size() > 12 && !row.get(12).isEmpty()) {
                String[] officerNames = row.get(12).split(",");
                for (String name : officerNames) {
                    Optional<User> user = userRepo.getUserByName(name.trim(), UserRoles.OFFICER);
                    user.ifPresent(u -> officerList.add((HDBOfficer) u));
                }
            }

            // === Create and Add Project ===
            BTOProj project = new BTOProj(projectIdCounter++, projectName, neighborhood, flatTypes, appOpenDate, appCloseDate, assignedManager, officerSlot, officerList);


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
        List<List<String>> data = FileIO.readMergedExcelFile(filePath);

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
                        String trimmedName = projectName.trim();

                        Optional<BTOProj> project = projects.stream()
                                .filter(p -> p.getProjName() != null && p.getProjName().equalsIgnoreCase(trimmedName))
                                .findFirst();

                        if (project.isPresent()) {
                            projectsInExercise.add(project.get());
                        } else {
                            System.out.println("⚠ Project name not found: " + trimmedName);
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
        System.out.println("Saving project...");
        List<List<String>> rows = new ArrayList<>();

        for (BTOProj proj : projects) {
            List<String> row = new ArrayList<>();

            // A - Project Name
            row.add(proj.getProjName());

            // B - Neighbourhood
            row.add(proj.getProjNbh() != null ? proj.getProjNbh().name() : "");

            // C-E - Type 1: Name, Units, Price
            FlatType type1 = proj.getFlatUnits().get(FlatTypes.TWO_ROOM);
            row.add(type1 != null ? type1.getTypeName() : "");
            row.add(type1 != null ? String.valueOf(type1.getTotalUnits()) : "");
            row.add(type1 != null ? String.valueOf(type1.getSellingPrice()) : "");

            // F-H - Type 2: Name, Units, Price
            FlatType type2 = proj.getFlatUnits().get(FlatTypes.THREE_ROOM);
            row.add(type2 != null ? type2.getTypeName() : "");
            row.add(type2 != null ? String.valueOf(type2.getTotalUnits()) : "");
            row.add(type2 != null ? String.valueOf(type2.getSellingPrice()) : "");

            // I - Application Open Date (Excel numeric format)
            row.add(String.valueOf(convertLocalDateToExcelDate(proj.getAppOpenDate().toLocalDate())));

            // J - Application Close Date (Excel numeric format)
            row.add(String.valueOf(convertLocalDateToExcelDate(proj.getAppCloseDate().toLocalDate())));

            // K - Manager
            row.add(proj.getManagerIC() != null ? proj.getManagerIC().getFirstName() : "");

            // L - Officer Slot Limit
            row.add(String.valueOf(proj.getOfficerSlots()));

            // M - Officers (comma separated)
            StringBuilder officers = new StringBuilder();
            HDBOfficer[] officerArray = proj.getOfficersList();
            if (officerArray != null) {
                for (HDBOfficer o : officerArray) {
                    if (!officers.isEmpty()) officers.append(",");
                    officers.append(o.getFirstName());
                }
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
