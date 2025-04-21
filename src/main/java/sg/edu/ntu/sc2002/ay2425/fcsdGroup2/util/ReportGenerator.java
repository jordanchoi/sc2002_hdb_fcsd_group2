package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces.ReportFilter;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    public List<Application> generateReport(List<Application> applications, ReportFilter filter) {
        List<Application> result = new ArrayList<>();
        for (Application app : applications) {
            if (filter.matches(app)) {
                result.add(app);
            }
        }
        return result;
    }
}