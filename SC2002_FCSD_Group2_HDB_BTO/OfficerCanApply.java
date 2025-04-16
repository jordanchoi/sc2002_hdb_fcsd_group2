

public class OfficerCanApply implements CanApplyFlat {
    @Override
    public boolean canApply(User user, BTOProj project) {
        if (!(user instanceof HDBOfficer)) {
            return false;
        }

        HDBOfficer officer = (HDBOfficer) user;
        
        if (!project.getVisibility()) {
            return false;
        }

        if (officer.getHandledProjects().contains(project)) {
            return false;
        }

        return new ApplicantCanApply().canApply(user, project);
    }
}
