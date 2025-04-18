package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOExercise;
import java.util.ArrayList;
import java.util.List;

public class HDBBTOExerciseController {
    private List<BTOExercise> exercises = new ArrayList<>();

    // Create a new exercise
    public BTOExercise createExercise() {
        BTOExercise newExercise = new BTOExercise();
        exercises.add(newExercise);
        return newExercise;
    }

    // Edit exercise by ID
    public BTOExercise editExercise(String exerciseId) {
        for (BTOExercise ex : exercises) {
            if (String.valueOf(ex.getExerciseId()).equals(exerciseId)) {
                return ex;
            }
        }
        return null;
    }

    // Delete exercise by ID
    public boolean deleteExercise(String exerciseId) {
        return exercises.removeIf(ex -> String.valueOf(ex.getExerciseId()).equals(exerciseId));
    }
}
