package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import java.util.ArrayList;
import java.util.List;

public class FlatTypeFilter implements ReportFilter {
    private String flatType;

    public FlatTypeFilter(String flatType) {
        this.flatType = flatType;
    }

    @Override
    public List<Application> filteredReport(List<Application> apps) {
        List<Application> result = new ArrayList<>();

        for (Application app : apps) {
            if (app.getFlatType().getTypeName().equalsIgnoreCase(flatType)) {
                result.add(app);
            }
        }

        return result;
    }
}
