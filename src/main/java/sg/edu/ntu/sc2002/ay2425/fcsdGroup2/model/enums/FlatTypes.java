package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums;
public enum FlatTypes {
    TWO_ROOM("2-Room"),
    THREE_ROOM("3-Room"),
    NIL("NIL");

    private final String displayName;

    FlatTypes(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static FlatTypes fromDisplayName(String input) {
        if (input == null || input.trim().isEmpty()) {
            return NIL;
        }

        String normalized = input.trim().replaceAll("\\s+", "").replace("-", "").toUpperCase();

        switch (normalized) {
            case "2ROOM":
                return TWO_ROOM;
            case "3ROOM":
                return THREE_ROOM;
            case "NIL":
                return NIL;
            default:
                throw new IllegalArgumentException("Unknown flat type: " + input);
        }
    }
}
