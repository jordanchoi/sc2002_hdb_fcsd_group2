package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.factory;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.FlatType;

import java.util.List;

public class FlatTypeFactory {
    /**
     * Parses a FlatType from the Excel row using a starting index.
     * @param row the project row
     * @param typeStartIndex the index of the flat type name (e.g., 2 or 5)
     * @return FlatType object
     */
    public static FlatType fromRow(List<String> row, int typeStartIndex) {
        String flatTypeName = row.get(typeStartIndex);
        int units = (int) Double.parseDouble(row.get(typeStartIndex + 1));;
        double price = Double.parseDouble(row.get(typeStartIndex + 2));

        return new FlatType(flatTypeName, units, (float) price);
    }
}
