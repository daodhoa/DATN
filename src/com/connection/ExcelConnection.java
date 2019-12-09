package com.connection;

import com.enums.DataType;
import com.model.Column;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelConnection {
    private String filePath;

    public ExcelConnection(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private Workbook readExcel() throws IOException {
        //File file = new File(getFilePath());
        InputStream file = new FileInputStream(getFilePath());
        Workbook workbook = WorkbookFactory.create(file);
        file.close();
        return workbook;
    }

    public ArrayList<String> getListSheet() throws IOException {
        ArrayList<String> listSheet = new ArrayList<>();
        Workbook workbook = this.readExcel();
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            listSheet.add(sheet.getSheetName());
        }
        workbook.close();
        return listSheet;
    }

    public Sheet getSheetAtIndex(int index) throws IOException {
        Workbook workbook = this.readExcel();
        return workbook.getSheetAt(index);
    }

    public List<Column> getColumns(int index, boolean isFirstRow) throws IOException {
        List<Column> listColumn = new ArrayList<>();
        Sheet sheet = this.getSheetAtIndex(index);
        Row firstRow = sheet.getRow(0);
        try {
            int checkEmpty = firstRow.getFirstCellNum();
        } catch (Exception e) {
            return null;
        }

        if (isFirstRow == false) {
            int numberOfColumn = firstRow.getLastCellNum();
            for (int i = 0; i < numberOfColumn; i++) {
                Column column = new Column("Column " + i, DataType.STRING, 100);
                listColumn.add(column);
            }
            return listColumn;
        }
        DataFormatter dataFormatter = new DataFormatter();
        Iterator<Cell> cellIterator = firstRow.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            Column column = new Column(dataFormatter.formatCellValue(cell), DataType.STRING, 100);
            listColumn.add(column);
        }

        return listColumn;
    }

    public List<List<String>> getDataFromSheet(int index) throws IOException {
        Sheet sheet = this.getSheetAtIndex(index);
        List<List<String>> listData = new ArrayList<>();
        DataFormatter dataFormatter = new DataFormatter();
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            List<String> listCellData = new ArrayList<>();
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = dataFormatter.formatCellValue(cell);
                listCellData.add(cellValue);
            }
            listData.add(listCellData);
        }
        return listData;
    }

    public void insertSheet(String sheetName) throws IOException {
        Workbook workbook = readExcel();
        workbook.createSheet(sheetName);
        OutputStream out = new FileOutputStream(getFilePath());
        workbook.write(out);
        workbook.close();
    }

    public boolean insertDataToSheet(int sheetIndex, List<Column> listInputColumns,
                                  List<Map<String, String>> listInputDataToDestination) {
        Workbook workbook = null;
        try {
            workbook = this.readExcel();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Sheet sheet = workbook.getSheetAt(sheetIndex);

        int currentRow = sheet.getLastRowNum();
        int rowNum = currentRow == 0 ? currentRow : (currentRow + 1);

        Row rowName = sheet.createRow(rowNum);
        for (int j = 0; j < listInputColumns.size(); j ++) {
            Cell cell = rowName.createCell(j);
            cell.setCellValue(listInputColumns.get(j).getName());
        }
        rowNum ++;

        for (int i = 0; i < listInputDataToDestination.size(); i++) {
            Row row = sheet.createRow(rowNum++);
            Map<String, String> mapOneRow = listInputDataToDestination.get(i);
            for (int j = 0; j < listInputColumns.size(); j ++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(mapOneRow.get(listInputColumns.get(j).getId()));
            }
        }

        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(getFilePath());
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("Write successfully");
        return true;
    }
}
