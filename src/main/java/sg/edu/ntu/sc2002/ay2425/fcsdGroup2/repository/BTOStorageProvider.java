package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;
import java.util.List;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOExercise;

public interface BTOStorageProvider {
    List<BTOProj> getAllProjects();
    List<BTOExercise> getAllExercises();

    void addProject(BTOProj project);
    void addExercise(BTOExercise exercise);

    void saveProject();
    void saveExercise();
}
