package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;

import java.util.ArrayList;
import java.util.List;

public class BTOExercise {

    // Attributes
    private int exerciseId;
    private String exerciseName;
    private int totalApplicants;
    private ProjStatus status;
    private List<BTOProj> projects;

    // Default constructor
    public BTOExercise() {
        this.projects = new ArrayList<>();
    }

    // Overloaded constructor
    public BTOExercise(int id, String name) {
        this.exerciseId = id;
        this.exerciseName = name;
        this.projects = new ArrayList<>();
    }

    // Getters
    public int getExerciseId() {
        return exerciseId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public int getTotalApplicants() {
        return totalApplicants;
    }

    public ProjStatus getProjStatus() {
        return status;
    }

    public List<BTOProj> getExerciseProjs() {
        return new ArrayList<>(projects); // return copy to protect internal list
    }

    // Setters
    public void setExerciseId(int id) {
        this.exerciseId = id;
    }

    public void setExerciseName(String name) {
        this.exerciseName = name;
    }

    public void setTotalApplicants(int totalApps) {
        this.totalApplicants = totalApps;
    }

    public void setProjStatus(ProjStatus status) {
        this.status = status;
    }

    public void setExerciseProjs(List<BTOProj> exerciseProjs) {
        this.projects = exerciseProjs;
    }
}
