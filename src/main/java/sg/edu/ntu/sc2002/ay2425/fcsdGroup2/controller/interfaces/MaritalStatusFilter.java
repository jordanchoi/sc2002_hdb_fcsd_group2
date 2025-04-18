package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.MaritalStatus;

import java.util.ArrayList;
import java.util.List;

public class MaritalStatusFilter implements ReportFilter {
    private MaritalStatus maritalStatus;

    public MaritalStatusFilter(MaritalStatus MaritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    @Override
    public boolean matches(Application app) {
        return app.getApplicant().getMaritalStatus() == maritalStatus;
    }
}