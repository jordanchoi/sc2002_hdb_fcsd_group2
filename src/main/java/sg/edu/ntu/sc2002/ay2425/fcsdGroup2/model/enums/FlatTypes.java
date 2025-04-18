package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums;
public enum FlatTypes {
    TWO_ROOM,
    THREE_ROOM;

    public static FlatTypes fromDisplayName(String input) {
        switch (input.trim().toUpperCase()) {
            case "2-ROOM":
                return TWO_ROOM;
            case "3-ROOM":
                return THREE_ROOM;
            default:
                throw new IllegalArgumentException("Unknown flat type: " + input);
        }
    }
}
