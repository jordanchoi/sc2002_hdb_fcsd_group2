package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.*;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatBookingStatus;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.enums.FlatTypes;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.FileIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for managing Flat entities.
 * Handles loading, saving, and searching flats in Excel files.
 */
public class FlatsListRepository {
    private static FlatsListRepository instance;
    private static final String FLATS_FILE_PATH = "data/FlatsList.xlsx";

    private final List<Flat> flats;
    private final BlockListRepository blockRepo;

    /** Private constructor for Singleton pattern. */
    private FlatsListRepository(BlockListRepository blockRepo) {
        this.blockRepo = blockRepo;
        this.flats = new ArrayList<>();
        loadFlatsFromFile();
    }

    /**
     * Returns the singleton instance of FlatsListRepository.
     *
     * @param blockRepo block repository dependency
     * @return FlatsListRepository instance
     */
    public static FlatsListRepository getInstance(BlockListRepository blockRepo) {
        if (instance == null) {
            instance = new FlatsListRepository(blockRepo);
        }
        return instance;
    }

    /** Loads flats from the Excel file. */
    private void loadFlatsFromFile() {
        flats.clear();
        List<List<String>> data;
        File file = new File(FLATS_FILE_PATH);
        data = file.exists() ? FileIO.readExcelFileLocal(FLATS_FILE_PATH) : FileIO.readMergedExcelFile(FLATS_FILE_PATH);

        for (List<String> row : data) {
            if (row.size() < 6 || row.get(0).equalsIgnoreCase("Postal Code")) continue;

            try {
                int postalCode = (int) Double.parseDouble(row.get(0).trim());
                int floor = (int) Double.parseDouble(row.get(1).trim());
                int unit = (int) Double.parseDouble(row.get(2).trim());
                FlatBookingStatus status = FlatBookingStatus.valueOf(row.get(3).trim().toUpperCase());
                FlatTypes typeEnum = FlatTypes.fromDisplayName(row.get(4).trim());
                int projectId = (int) Double.parseDouble(row.get(5).trim());

                // Match by project ID
                Block block = null;
                for (Block b : blockRepo.getAll()) {
                    if (b.getProject() != null && b.getProject().getProjId() == projectId) {
                        block = b;
                        break;
                    }
                }
                if (block == null) {
                    System.out.printf("No block found for Project ID: %d%n", projectId);
                    continue;
                }

                FlatType flatType = block.getProject().getFlatMap().get(typeEnum);
                if (flatType == null) {
                    System.out.println("FlatType not found for: " + typeEnum + " in Project ID: " + projectId);
                    continue;
                }

                flats.add(new Flat(floor, unit, status, flatType, block));
            } catch (Exception e) {
                System.out.println("Error parsing flat row: " + row);
                e.printStackTrace();
            }
        }
    }
    /** Saves all flats to the Excel file. */
    public void saveFlatsToFile() {
        List<List<String>> data = new ArrayList<>();
        data.add(List.of("Postal Code", "Floor", "Unit", "Status", "Flat Type"));
        for (Flat f : flats) {
            data.add(List.of(
                    String.valueOf(f.getBlock().getBlockId()),
                    String.valueOf(f.getFloorNo()),
                    String.valueOf(f.getUnitNo()),
                    f.getStatus().name(),
                    f.getFlatType().getTypeName()
            ));
        }
        FileIO.writeExcelFile(FLATS_FILE_PATH, data);
    }

    /**
     * Adds a new flat and saves immediately.
     *
     * @param flat flat to add
     */
    public void addFlat(Flat flat) {
        flats.add(flat);
        saveFlatsToFile();
    }

    /**
     * Updates a flat (deletes old, adds new).
     *
     * @param updated the updated flat
     */
    public void updateFlat(Flat updated) {
        deleteFlat(updated.getFloorNo(), updated.getUnitNo(), updated.getBlock().getBlockId());
        addFlat(updated);
    }

    /**
     * Deletes a flat based on floor, unit, and block ID.
     */
    public void deleteFlat(int floor, int unit, int blockId) {
        flats.removeIf(f ->
                f.getFloorNo() == floor &&
                        f.getUnitNo() == unit &&
                        f.getBlock().getBlockId() == blockId);
        saveFlatsToFile();
    }

    /** @return all flats (copy). */
    public List<Flat> getAllFlats() {
        return new ArrayList<>(flats);
    }

    /**
     * Retrieves flats belonging to a specific block.
     */
    public List<Flat> getFlatsByBlockId(int blockId) {
        List<Flat> result = new ArrayList<>();
        for (Flat f : flats) {
            if (f.getBlock().getBlockId() == blockId) {
                result.add(f);
            }
        }
        return result;
    }

    /**
     * Retrieves a specific flat based on floor, unit, and block ID.
     */
    public Flat getFlat(int floor, int unit, int blockId) {
        for (Flat flat : flats) {
            if (flat.getFloorNo() == floor && flat.getUnitNo() == unit && flat.getBlock().getBlockId() == blockId) {
                return flat;
            }
        }
        return null;
    }
}
