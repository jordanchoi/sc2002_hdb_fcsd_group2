package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.Neighbourhoods;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.FileIO;

import java.util.ArrayList;
import java.util.List;

public class BTORepository implements BTOStorageProvider {

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

        if (data == null || data.isEmpty()) {
            System.out.println("There are no projects in the file.");
            return;
        }

        // Counter for projectId
        int projectIdCounter = 1;

        // Loop through the data and create BTOProj objects and add them to the projects list
        for (List<String> row : data) {
            // Assuming the first column is the project name and the second column is the project ID

            // Project Name
            String projectName = row.get(0);

            // Parse Neighbourhoods
            String projectNeighborhood = row.get(1);
            Neighbourhoods neighborhood = Neighbourhoods.valueOf(projectNeighborhood.toUpperCase());

            // Count the number of FlatType
            int flatTypeCount = 0;

            if (row.get(2) != null || !row.get(2).isEmpty()) {
                flatTypeCount++;
            }

            if (row.get(5) != null || !row.get(5).isEmpty()) {
                flatTypeCount++;
            }

            // Create the FlatTypes object - reuse from HDBFlatsController
            List<FlatType> flatType = new ArrayList<>();

            for (int i = 0; i < flatTypeCount; i++) {

            }


            String projectId = row.get(1);
            BTOProj project = new BTOProj();
            projects.add(project);
        }

    }

    private void loadExercisesFromFile(String filePath) {

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

    }

    @Override
    public void saveExercise() {

    }


}
