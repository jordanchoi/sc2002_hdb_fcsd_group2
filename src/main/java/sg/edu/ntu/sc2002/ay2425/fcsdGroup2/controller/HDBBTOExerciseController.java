package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOExercise;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Controller class to manage BTO Exercises.
 * Handles creation, editing, deletion, and retrieval of BTO exercises.
 */
public class HDBBTOExerciseController {
    private List<BTOExercise> exercises = new ArrayList<>();
    private BTORepository btoRepo = BTORepository.getInstance();

    /** Default constructor. */
    public HDBBTOExerciseController() {}

    /**
     * Creates a new BTO exercise and adds it to repository.
     *
     * @param id exercise ID
     * @param name exercise name
     * @param totalApplicants number of applicants
     * @param status project status
     * @param projList list of projects linked to this exercise
     * @return created BTOExercise object
     */
    public BTOExercise createExercise(int id, String name, int totalApplicants, ProjStatus status, List<BTOProj> projList) {
        BTOExercise newExercise = new BTOExercise(id, name, totalApplicants, status, projList);
        btoRepo.addExercise(newExercise);
        return newExercise;
    }

    /**
     * Edits details of an existing exercise.
     *
     * @param id exercise ID
     * @param newName new name
     * @param newApplicants updated number of applicants
     * @param newStatus updated status
     * @return true if successful
     */
    public boolean editExercise(int id, String newName, int newApplicants, ProjStatus newStatus) {
        for (BTOExercise ex : exercises) {
            if (ex.getExerciseId() == id) {
                ex.setExerciseName(newName);
                ex.setTotalApplicants(newApplicants);
                ex.setProjStatus(newStatus);
                btoRepo.saveExercise();
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes an exercise by ID.
     *
     * @param id exercise ID
     * @return true if deleted successfully
     */
    public boolean deleteExerciseId(int id) {
        List<BTOExercise> repoExercises = btoRepo.getAllExercises();
        Iterator<BTOExercise> iterator = repoExercises.iterator();
        boolean deleted = false;

        while (iterator.hasNext()) {
            BTOExercise ex = iterator.next();
            if (ex.getExerciseId() == id) {
                iterator.remove();
                deleted = true;
                break;
            }
        }
        if (deleted) {
            btoRepo.saveExercise();
        }
        return deleted;
    }

    /**
     * Retrieves all BTO exercises currently in memory.
     *
     * @return list of BTOExercise
     */
    public List<BTOExercise> viewAllExercises() {
        return new ArrayList<>(exercises);
    }

    /**
     * Checks whether an exercise ID is unique.
     *
     * @param id the exercise ID to check
     * @return true if unique
     */
    public boolean isExerciseIdUnique(int id) {
        for (BTOExercise exercise : viewAllExercises()) {
            if (exercise.getExerciseId() == id) {
                return false;
            }
        }
        return true;
    }

    /**
     * Inserts exercises from repository storage into local memory.
     */
    public void insertExercisesFromRepo() {
        exercises.clear();
        List<BTOExercise> repoExercises = btoRepo.getAllExercises();
        for (BTOExercise exercise : repoExercises) {
            addExercise(exercise);
        }
    }

    /**
     * Adds an exercise manually into local list.
     *
     * @param exercise the BTOExercise to add
     */
    public void addExercise(BTOExercise exercise) {
        exercises.add(exercise);
    }
}
