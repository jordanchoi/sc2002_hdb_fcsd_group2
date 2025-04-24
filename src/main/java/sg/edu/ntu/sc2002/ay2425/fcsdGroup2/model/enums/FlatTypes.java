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
        switch (input.trim().toUpperCase()) {
            case "2-ROOM":
                return TWO_ROOM;
            case "3-ROOM":
                return THREE_ROOM;
            case "NIL":
                return NIL;
            default:
                throw new IllegalArgumentException("Unknown flat type: " + input);
        }
    }
}
