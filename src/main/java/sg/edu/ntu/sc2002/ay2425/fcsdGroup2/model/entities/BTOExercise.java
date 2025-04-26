package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.ProjStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a BTO Exercise that groups multiple BTO Projects together.
 * Tracks exercise name, status, total applicants, and associated projects.
 */
public class BTOExercise {

    private int exerciseId;
    private String exerciseName;
    private int totalApplicants;
    private ProjStatus status;
    private List<BTOProj> projects;

    /**
     * Default constructor.
     * Initializes an empty project list.
     */
    public BTOExercise() {
        this.projects = new ArrayList<>();
    }

    /**
     * Overloaded constructor to initialize full BTOExercise details.
     *
     * @param id exercise ID
     * @param name exercise name
     * @param totalApplicants number of applicants
     * @param status project status
     * @param projects list of associated BTO projects
     */
    public BTOExercise(int id, String name, int totalApplicants, ProjStatus status, List<BTOProj> projects) {
        this.exerciseId = id;
        this.exerciseName = name;
        this.totalApplicants = totalApplicants;
        this.status = status;
        this.projects = projects;
    }

    // Getters and Setters with JavaDoc below...

    /**
     * Returns the exercise ID.
     *
     * @return exercise ID
     */
    public int getExerciseId() { return exerciseId; }

    /**
     * Returns the exercise name.
     *
     * @return exercise name
     */
    public String getExerciseName() { return exerciseName; }

    /**
     * Returns the total number of applicants.
     *
     * @return number of applicants
     */
    public int getTotalApplicants() { return totalApplicants; }

    /**
     * Returns the project status of the exercise.
     *
     * @return project status
     */
    public ProjStatus getProjStatus() { return status; }

    /**
     * Returns a copy of the list of associated projects.
     *
     * @return list of BTO projects
     */
    public List<BTOProj> getExerciseProjs() { return new ArrayList<>(projects); }

    /**
     * Sets the exercise ID.
     *
     * @param id exercise ID
     */
    public void setExerciseId(int id) { this.exerciseId = id; }

    /**
     * Sets the exercise name.
     *
     * @param name exercise name
     */
    public void setExerciseName(String name) { this.exerciseName = name; }

    /**
     * Sets the total number of applicants.
     *
     * @param totalApps total applicants
     */
    public void setTotalApplicants(int totalApps) { this.totalApplicants = totalApps; }

    /**
     * Sets the project status of the exercise.
     *
     * @param status project status
     */
    public void setProjStatus(ProjStatus status) { this.status = status; }

    /**
     * Sets the associated projects of the exercise.
     *
     * @param exerciseProjs list of projects
     */
    public void setExerciseProjs(List<BTOProj> exerciseProjs) { this.projects = exerciseProjs; }

    /**
     * Adds a project to the exercise.
     *
     * @param proj project to add
     */
    public void addProject(BTOProj proj) { projects.add(proj); }
}
