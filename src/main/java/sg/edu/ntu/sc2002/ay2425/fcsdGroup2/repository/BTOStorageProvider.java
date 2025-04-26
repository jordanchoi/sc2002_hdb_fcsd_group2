package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import java.util.List;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOExercise;

/**
 * Interface defining storage operations for BTO-related entities.
 * Includes retrieval, addition, and saving operations for projects, exercises, and applications.
 */
public interface BTOStorageProvider {

    /**
     * Retrieves all BTO projects.
     *
     * @return list of BTOProj
     */
    List<BTOProj> getAllProjects();

    /**
     * Retrieves all BTO exercises.
     *
     * @return list of BTOExercise
     */
    List<BTOExercise> getAllExercises();

    /**
     * Retrieves all applications.
     *
     * @return list of Application
     */
    List<Application> getAllApplications();

    /**
     * Adds a new BTO project.
     *
     * @param project BTO project to add
     */
    void addProject(BTOProj project);

    /**
     * Adds a new BTO exercise.
     *
     * @param exercise BTO exercise to add
     */
    void addExercise(BTOExercise exercise);

    /**
     * Adds a new application.
     *
     * @param application Application to add
     */
    void addApplication(Application application);

    /**
     * Saves all BTO projects to storage.
     */
    void saveProject();

    /**
     * Saves all BTO exercises to storage.
     */
    void saveExercise();

    /**
     * Saves all applications to storage.
     */
    void saveApplication();
}
