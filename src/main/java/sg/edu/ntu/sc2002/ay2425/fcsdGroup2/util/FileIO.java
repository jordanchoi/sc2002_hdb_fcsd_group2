package sg.edu.ntu.sc2002.ay2425.fcsdGroup2.util;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(new File(filePath))) {

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}