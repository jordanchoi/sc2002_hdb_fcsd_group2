package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import java.util.ArrayList;
import java.util.List;

public class MaritalStatusFilter implements ReportFilter {
    private String status;

    public MaritalStatusFilter(String status) {
        this.status = status;
    }

    @Override
    public List<Application> filteredReport(List<Application> apps) {
        List<Application> result = new ArrayList<>();

        for (Application app : apps) {
            if (app.getApplicant().getMaritalStatus().equals(status)) {
                result.add(app);
            }
        }
        return result;
    }
}