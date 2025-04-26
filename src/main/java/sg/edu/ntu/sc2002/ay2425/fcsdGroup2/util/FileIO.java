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

/**
 * Utility class for reading from and writing to Excel files.
 * Supports both local and resource-based file operations.
 */
public class FileIO {

    /**
     * Reads an Excel file from the local filesystem.
     *
     * @param filePath the path to the local Excel file
     * @return list of rows, each row as a list of strings
     */
    public static List<List<String>> readExcelFileLocal(String filePath) {
        List<List<String>> data = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Local file not found: " + filePath);
            return data;
        }

        try (InputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
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

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }


    /**
     * Reads an Excel file from the project resources.
     *
     * @param filePath the path to the resource Excel file
     * @return list of rows, each row as a list of strings
     */
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

    /**
     * Writes data to a new Excel file.
     *
     * @param filePath the path to save the Excel file
     * @param data the data to write
     */
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

    /**
     * Reads and merges Excel data from both resource and local files,
     * prioritizing local file data in case of duplicates.
     *
     * @param filePath the path to the resource/local Excel file
     * @return merged list of rows
     */
    public static List<List<String>> readMergedExcelFile(String filePath) {
        Map<String, List<String>> uniqueDataMap = new LinkedHashMap<>();

        // Step 1: Read from resource file (original)
        try (InputStream fis = FileIO.class.getClassLoader().getResourceAsStream(filePath)) {
            if (fis != null) {
                List<List<String>> originalData = readFromWorkbookStream(fis);
                for (List<String> row : originalData) {
                    if (row.size() > 1) {
                        String key = extractKey(row.get(1));
                        uniqueDataMap.putIfAbsent(key, row);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Step 2: Read from local file (overwrite duplicates)
        File localFile = new File("./" + filePath);
        if (localFile.exists()) {
            try (InputStream fis = new FileInputStream(localFile)) {
                List<List<String>> localData = readFromWorkbookStream(fis);
                for (List<String> row : localData) {
                    if (row.size() > 1) {
                        String key = extractKey(row.get(1));
                        uniqueDataMap.put(key, row); // overwrite
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return new ArrayList<>(uniqueDataMap.values());
    }

    /**
     * Extracts a normalized key from a raw string or numeric value.
     *
     * @param raw the raw string
     * @return a normalized key
     */
    // Helper to normalize numeric or string keys
    private static String extractKey(String raw) {
        try {
            double num = Double.parseDouble(raw.trim());
            return String.valueOf((int) num);  // "1.0" → "1"
        } catch (NumberFormatException e) {
            return raw.trim();
        }
    }

    /**
     * Reads an Excel file from an input stream and converts it to a list of rows.
     *
     * @param fis the input stream
     * @return list of rows, each row as a list of strings
     * @throws IOException if an error occurs during reading
     */
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