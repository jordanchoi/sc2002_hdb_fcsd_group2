package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Block;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.FileIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class to manage Block entities.
 * Handles loading, saving, and searching blocks by various attributes.
 */
public class BlockListRepository {
    private static final String FILE_PATH = "data/BlocksList.xlsx";
    private final List<Block> blocks;
    private final BTORepository btoRepo;

    /**
     * Constructs a BlockListRepository and loads data.
     *
     * @param btoRepo BTO repository for linking projects
     */
    public BlockListRepository(BTORepository btoRepo) {
        this.btoRepo = btoRepo;
        this.blocks = new ArrayList<>();
        loadFromFile();
    }

    /** Loads all blocks from file into memory. */
    private void loadFromFile() {
        blocks.clear();
        List<List<String>> data;

        File localFile = new File(FILE_PATH);
        if (localFile.exists()) {
            data = FileIO.readExcelFileLocal(FILE_PATH);
        } else {
            data = FileIO.readMergedExcelFile(FILE_PATH);
        }

        for (List<String> row : data) {
            // Skip header or incomplete rows
            if (row.size() < 4 || row.get(0).trim().equalsIgnoreCase("Block Number")) continue;

            try {
                String blkNoStr = row.get(0).trim();
                String postalCodeStr = row.get(1).trim();
                String streetAddr = row.get(2).trim();
                String projIdStr = row.get(3).trim();

                if (blkNoStr.isEmpty() || postalCodeStr.isEmpty() || projIdStr.isEmpty()) {
                    System.out.println("Skipping invalid row: " + row);
                    continue;
                }

                int blkNo = (int) Double.parseDouble(blkNoStr);
                int postalAsId = (int) Double.parseDouble(postalCodeStr); // Used as blockId
                int projId = (int) Double.parseDouble(projIdStr);

                BTOProj project = btoRepo.getProjById(projId);
                if (project == null) {
                    System.out.println("Skipping block, project not found for ID: " + projId);
                    continue;
                }

                Block block = new Block(postalAsId, streetAddr, blkNo, postalCodeStr, project);
                blocks.add(block);
                project.addBlock(block);

            } catch (Exception e) {
                System.out.println("Error reading row: " + row);
                e.printStackTrace();
            }
        }
    }

    /** Saves all blocks to file. */
    public void saveToFile() {
        List<List<String>> data = new ArrayList<>();
        data.add(List.of("Block ID", "Blk No", "Street Address", "Postal Code", "Project ID"));

        for (Block block : blocks) {
            data.add(List.of(
                    String.valueOf(block.getBlockId()),
                    String.valueOf(block.getBlkNo()),
                    block.getStreetAddr(),
                    block.getPostalCode(),
                    String.valueOf(block.getProject().getProjId())
            ));
        }

        FileIO.writeExcelFile(FILE_PATH, data);
    }

    /**
     * Finds a block by its postal code.
     *
     * @param postalCode postal code
     * @return matching block or null
     */
    public Block getByPostalCode(int postalCode) {
        for (Block b : blocks) {
            try {
                int blockPostal = Integer.parseInt(b.getPostalCode());
                if (blockPostal == postalCode) {
                    return b;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid postal code in block: " + b.getPostalCode());
            }
        }
        return null;
    }

    /**
     * Adds a new block and saves.
     *
     * @param block the block to add
     */
    public void add(Block block) {
        blocks.add(block);
        saveToFile();
    }

    /**
     * Updates an existing block by replacing and saving.
     *
     * @param updated the updated block
     */
    public void update(Block updated) {
        delete(updated.getBlockId());
        add(updated);
    }

    /**
     * Deletes a block by block ID.
     *
     * @param blockId the block ID
     */
    public void delete(int blockId) {
        blocks.removeIf(b -> b.getBlockId() == blockId);
        saveToFile();
    }

    /** @return a copy of all blocks. */
    public List<Block> getAll() {
        return new ArrayList<>(blocks);
    }

    /**
     * Retrieves all blocks belonging to a project by project ID.
     *
     * @param projId project ID
     * @return list of blocks
     */
    public List<Block> getByProjectId(int projId) {
        List<Block> result = new ArrayList<>();
        for (Block b : blocks) {
            if (b.getProject().getProjId() == projId) {
                result.add(b);
            }
        }
        return result;
    }

    /**
     * Retrieves a block by its ID.
     *
     * @param blockId block ID
     * @return matching block or null
     */
    public Block getById(int blockId) {
        for (Block b : blocks) {
            if (b.getBlockId() == blockId) return b;
        }
        return null;
    }
}
