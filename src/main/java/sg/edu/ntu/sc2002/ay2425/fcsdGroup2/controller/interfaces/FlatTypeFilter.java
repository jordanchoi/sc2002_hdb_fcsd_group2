package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller.interfaces;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.FlatType;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes; 

import java.util.ArrayList;
import java.util.List;

public class FlatTypeFilter implements ReportFilter {
    private FlatTypes flatType;

    public FlatTypeFilter(FlatTypes flatType) {
        this.flatType = flatType;
    }

    @Override
    public boolean matches(Application app) {
        return app.getFlatType() == flatType;
    }
}
