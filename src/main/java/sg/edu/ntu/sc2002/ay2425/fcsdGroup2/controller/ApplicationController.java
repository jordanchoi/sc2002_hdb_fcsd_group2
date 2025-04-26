package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.FlatType;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.ApplicationRepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
/**
 * Controller class for managing processing of applications.
 * Handles approval or rejection of applications based on flat availability.
 */
public class ApplicationController {
    private final List<Application> applications = new ArrayList<>();
    private BTORepository btoRepo = BTORepository.getInstance();
    private final UserRepository userRepo = UserRepository.getInstance();
    private final ApplicationRepository applicationRepo = new ApplicationRepository(btoRepo, userRepo);

    /** Default constructor. */
    public ApplicationController() {}

    /**
     * Processes an application decision for approval or rejection.
     *
     * @param app the application to process
     * @param approve true to approve, false to reject
     * @return true if decision processed successfully
     */
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
