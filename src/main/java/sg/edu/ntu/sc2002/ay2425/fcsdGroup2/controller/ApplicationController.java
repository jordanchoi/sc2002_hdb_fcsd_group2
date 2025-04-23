package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.FlatType;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.ApplicationRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class ApplicationController {
    private final List<Application> applications = new ArrayList<>();
    private final BTORepository btoRepo = new BTORepository();
    private final UserRepository userRepo = new UserRepository();
    private final ApplicationRepository applicationRepo = new ApplicationRepository(btoRepo, userRepo);
    public ApplicationController() {}

    public boolean processApplicationDecision(Application app, boolean approve) {
        FlatType flatType = app.getFlatType();
        if (approve) {
            if (flatType.getUnitsAvail() > 0) {
                flatType.setUnitsBooked(flatType.getUnitsBooked() + 1);
                app.approve();
            } else {
                app.reject(); // fallback reject if out of supply
                return false;
            }
        } else {
            app.reject();
        }

        applicationRepo.update(app);
        return true;
    }


}
