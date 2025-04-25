package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.repository;

import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.Block;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.model.entities.BTOProj;
import sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util.FileIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BlockListRepository {
    private static final String FILE_PATH = "data/BlocksList.xlsx";
    private final List<Block> blocks;
    private final BTORepository btoRepo;

    public BlockListRepository(BTORepository btoRepo) {
        this.btoRepo = btoRepo;
        this.blocks = new ArrayList<>();
        loadFromFile();
    }

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

    public void add(Block block) {
        blocks.add(block);
        saveToFile();
    }

    public void update(Block updated) {
        delete(updated.getBlockId());
        add(updated);
    }

    public void delete(int blockId) {
        blocks.removeIf(b -> b.getBlockId() == blockId);
        saveToFile();
    }

    public List<Block> getAll() {
        return new ArrayList<>(blocks);
    }

    public List<Block> getByProjectId(int projId) {
        List<Block> result = new ArrayList<>();
        for (Block b : blocks) {
            if (b.getProject().getProjId() == projId) {
                result.add(b);
            }
        }
        return result;
    }

    public Block getById(int blockId) {
        for (Block b : blocks) {
            if (b.getBlockId() == blockId) return b;
        }
        return null;
    }
}
