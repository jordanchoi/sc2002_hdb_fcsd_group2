package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import java.util.ArrayList;
import java.util.List;

public class ProjNameFilter implements ReportFilter {
    private String projName;

    public ProjNameFilter(String projName) {
        this.projName = projName;
    }

    @Override
    public List<Application> filteredReport(List<Application> apps) {
        List<Application> result = new ArrayList<>();

        for (Application app : apps) {
            if (app.getProject().getProjName().equalsIgnoreCase(projName)) {
                result.add(app);
            }
        }

        return result;
    }
}
