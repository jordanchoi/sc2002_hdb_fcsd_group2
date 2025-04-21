package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;

public class AndFilter implements ReportFilter{
    private ReportFilter filter1;
    private ReportFilter filter2;

    public AndFilter(ReportFilter filter1, ReportFilter filter2) {
        this.filter1 = filter1;
        this.filter2 = filter2;
    }

    @Override
    public boolean matches(Application app) {
        return filter1.matches(app) && filter2.matches(app);
    }
}
