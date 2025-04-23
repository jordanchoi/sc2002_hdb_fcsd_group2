package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;
import java.util.List;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOExercise;

public interface BTOStorageProvider {
    List<BTOProj> getAllProjects();
    List<BTOExercise> getAllExercises();

    List<Application> getAllApplications();

    void addProject(BTOProj project);
    void addExercise(BTOExercise exercise);

    void addApplication(Application application);

    void saveProject();
    void saveExercise();

    void saveApplication();
}
