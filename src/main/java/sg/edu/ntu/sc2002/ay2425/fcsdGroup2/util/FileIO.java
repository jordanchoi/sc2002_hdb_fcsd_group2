package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FileIO {
    // Generic method to read data from an Excel file
    public static List<List<String>> readExcelFile(String filePath) {
        List<List<String>> data = new ArrayList<>();

        URL resourceUrl = FileIO.class.getClassLoader().getResource(filePath);


        try (InputStream fis = FileIO.class.getClassLoader().getResourceAsStream(filePath)) {
            if (fis == null) {
                return data;
            }

            try (Workbook workbook = new XSSFWorkbook(fis)) {
                Sheet sheet = workbook.getSheetAt(0);
                int rowIndex = 0;
                for (Row row : sheet) {
                    if (rowIndex++ == 0) continue; // skip header row
                    List<String> rowData = new ArrayList<>();
                    for (Cell cell : row) {
                        switch (cell.getCellType()) {
                            case STRING -> rowData.add(cell.getStringCellValue());
                            case NUMERIC -> rowData.add(String.valueOf(cell.getNumericCellValue()));
                            case BOOLEAN -> rowData.add(String.valueOf(cell.getBooleanCellValue()));
                            default -> rowData.add("");
                        }
                    }
                    data.add(rowData);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    // Generic method to write data to an Excel file
    public static void writeExcelFile(String filePath, List<List<String>> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            File file = new File(filePath);

            // Ensure parent directory exists
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs(); // create directories if needed
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                Sheet sheet = workbook.createSheet("Sheet1");
                for (int i = 0; i < data.size(); i++) {
                    Row row = sheet.createRow(i);
                    List<String> rowData = data.get(i);
                    for (int j = 0; j < rowData.size(); j++) {
                        Cell cell = row.createCell(j);
                        cell.setCellValue(rowData.get(j));
                    }
                }
                workbook.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<List<String>> readMergedExcelFile(String filePath) {
        Map<String, List<String>> uniqueDataMap = new LinkedHashMap<>();

        // 1. Load resource/original data first
        try (InputStream fis = FileIO.class.getClassLoader().getResourceAsStream(filePath)) {
            if (fis != null) {
                List<List<String>> originalData = readFromWorkbookStream(fis);
                for (List<String> row : originalData) {
                    String nric = row.get(1).toUpperCase(); // NRIC column
                    uniqueDataMap.putIfAbsent(nric, row); // only add if not present
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. Load local file next (overwrites duplicates)
        File localFile = new File("./" + filePath);
        if (localFile.exists()) {
            try (InputStream fis = new FileInputStream(localFile)) {
                List<List<String>> localData = readFromWorkbookStream(fis);
                for (List<String> row : localData) {
                    String nric = row.get(1).toUpperCase(); // overwrite any existing
                    uniqueDataMap.put(nric, row);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 3. Return merged & deduplicated
        return new ArrayList<>(uniqueDataMap.values());
    }


    private static List<List<String>> readFromWorkbookStream(InputStream fis) throws IOException {
        List<List<String>> data = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex++ == 0) continue;
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING -> rowData.add(cell.getStringCellValue());
                        case NUMERIC -> rowData.add(String.valueOf(cell.getNumericCellValue()));
                        case BOOLEAN -> rowData.add(String.valueOf(cell.getBooleanCellValue()));
                        default -> rowData.add("");
                    }
                }
                data.add(rowData);
            }
        }
        return data;
    }

}