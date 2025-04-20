package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOExercise;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;

import java.util.ArrayList;
import java.util.List;

public class HDBBTOExerciseController {
    private List<BTOExercise> exercises = new ArrayList<>();
    private BTORepository btoRepo = new BTORepository();

    // Create a new exercise
    public HDBBTOExerciseController() {}

    public BTOExercise createExercise(int id, String name, int totalApplicants, ProjStatus status, List<BTOProj> projList) {
        BTOExercise newExercise = new BTOExercise(id, name, totalApplicants, status, projList);
        btoRepo.addExercise(newExercise);
        return newExercise;
    }

    public boolean editExercise(int id, String newName, int newApplicants, ProjStatus newStatus) {
        for (BTOExercise ex : exercises) {
            if (ex.getExerciseId() == id) {
                ex.setExerciseName(newName);
                ex.setTotalApplicants(newApplicants);
                ex.setProjStatus(newStatus);
                btoRepo.saveExercise(); // persist update
                return true;
            }
        }
        return false;
    }

    // Delete exercise by ID
    public boolean deleteExercise(String exerciseId) {
        return exercises.removeIf(ex -> String.valueOf(ex.getExerciseId()).equals(exerciseId));
    }

    public List<BTOExercise> viewAllExercises() {
        // This method returns all BTO projects stored in the controller.
        // It provides access to the full list of project records.
        return new ArrayList<>(exercises); // Return a copy to avoid direct modification
    }

    public boolean isExerciseIdUnique(int id) {
        for (BTOExercise exercise : viewAllExercises()) {
            if (exercise.getExerciseId() == id) {
                return false;
            }
        }
        return true;
    }

    public void insertExercisesFromRepo() {
        exercises.clear();
        List<BTOExercise> repoExercises = btoRepo.getAllExercises();
        for (BTOExercise exercise : repoExercises) {
            addExercise(exercise);
        }
    }

    public void addExercise(BTOExercise exercise) {
        exercises.add(exercise);
    }

}
