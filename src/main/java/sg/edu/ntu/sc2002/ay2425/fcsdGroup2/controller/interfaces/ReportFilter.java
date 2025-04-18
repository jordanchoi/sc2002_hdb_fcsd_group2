package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import java.util.List;

public interface ReportFilter {
    List<Application> filteredReport(List<Application> apps);
}
