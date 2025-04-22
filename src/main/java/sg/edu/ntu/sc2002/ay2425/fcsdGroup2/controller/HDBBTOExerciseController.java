package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOExercise;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HDBBTOExerciseController {
    private List<BTOExercise> exercises = new ArrayList<>();
    private BTORepository btoRepo = BTORepository.getInstance();

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
                btoRepo.saveExercise();
                return true;
            }
        }
        return false;
    }

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

    public List<BTOExercise> viewAllExercises() {
        return new ArrayList<>(exercises);
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
