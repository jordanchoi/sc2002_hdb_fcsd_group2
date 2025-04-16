

public class CantApply implements CanApplyFlat{
    @Override
    public boolean canApply(User user, BTOProj project) {
        return false;
    }
}
