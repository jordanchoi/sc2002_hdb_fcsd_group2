package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.controller;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Application;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository.BTORepository;

import java.util.ArrayList;
import java.util.List;

public class ApplicationController {
    private final List<Application> applications = new ArrayList<>();
    private BTORepository btoRepo = BTORepository.getInstance();

    public ApplicationController() {}

    public ApplicationController(BTORepository btoRepo) {
        this.btoRepo = btoRepo;
    }

    public List<Application> viewAllApplications() {
        return new ArrayList<>(applications);
    }

    public void insertApplicationsFromRepo() {
        applications.clear();
        List<Application> repoApplications = btoRepo.getAllApplications();
        for (Application app : repoApplications) {
            addApplication(app);
        }
    }
    private void addApplication(Application app) {applications.add(app);}
}
