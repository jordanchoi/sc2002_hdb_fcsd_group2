
import Enumeration.MaritalStatus;

public class ApplicantCanApply implements CanApplyFlat {
    @Override
    public boolean canApply(User user, BTOProj project) {
        // Check project visibility
        if (!project.getVisibility()) {
            return false;
        }

        // Check user type (ensure it's an applicant)
        if (!(user instanceof HDBApplicant)) {
            return false;
        }

        HDBApplicant applicant = (HDBApplicant) user;
        int age = applicant.getAge();
        MaritalStatus maritalStatus = applicant.getMaritalStatus();

        // Eligibility rules
        if (maritalStatus == MaritalStatus.Married && age >= 21) {
            return true;
        } else if (maritalStatus == MaritalStatus.Single && age >= 35) {
            return true;
        }
        return false;
    }
}
