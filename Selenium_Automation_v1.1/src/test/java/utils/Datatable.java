package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Datatable {
	public XSSFWorkbook workbook;
	public XSSFSheet sheet;
	public String testcasename, sheetName, datatablePath;

	public Datatable(HashMap testcase) {
		this.workbook = (XSSFWorkbook) testcase.get("workbook");
		this.testcasename = (String) testcase.get("testcasename");
		this.sheetName = (String) testcase.get("sheetName");
		sheet = this.workbook.getSheet(sheetName);
	}

	public int getRowCount() {
		try {
			int rowCount = sheet.getPhysicalNumberOfRows();
			return rowCount;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
			return -1;
		}
	}

	public int getColCount() {
		try {
			int colCount = sheet.getRow(0).getPhysicalNumberOfCells();
			return colCount;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
			return -1;
		}
	}

	public String getCellData(int rowNum, int colNum) {
		try {
			DataFormatter formatter = new DataFormatter();
			String cellData = formatter.formatCellValue(sheet.getRow(rowNum).getCell(colNum));

			return cellData;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
			return "";
		}
	}

	public String getCellData(String colName) {
		try {
			return getCellData(colName, 1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
			return null;
		}
	}

	public String getCellData(String colName, int iteration) {

		try {
			sheet = workbook.getSheet(sheetName);
			int rowNum = -1, colNum = -1;

			// Find the Row and Column count
			int rowCount = getRowCount();
			int colCount = getColCount();

			DataFormatter formatter = new DataFormatter();
			String val;
			int rowIteration;

			// Find the Column Number
			for (int iCol = 1; iCol < colCount; iCol++) {
				val = formatter.formatCellValue(sheet.getRow(0).getCell(iCol));
				if (val.trim().equalsIgnoreCase(colName)) {
					colNum = iCol;
					break;
				}
			}

			// Find the Row Number
			for (int iRow = 1; iRow < rowCount; iRow++) {

				val = formatter.formatCellValue(sheet.getRow(iRow).getCell(0));
				rowIteration = Integer.valueOf(formatter.formatCellValue(sheet.getRow(iRow).getCell(1)));

				if (val.trim().equalsIgnoreCase(testcasename) && rowIteration == iteration) {
					rowNum = iRow;
					break;
				}
			}

			String cellData = formatter.formatCellValue(sheet.getRow(rowNum).getCell(colNum));
			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

			Cell cell = sheet.getRow(rowNum).getCell(colNum);
			evaluator.evaluateFormulaCell(cell);

			if (cell.getCellType().name().equals("FORMULA"))
				cellData = cell.getStringCellValue();

			return cellData;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
			return null;
		}
	}

	public void putCellData(String exportData, String colName) {
		try {
			putCellData(exportData, colName, 1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}

	public void putCellData(String exportData, String colName, int iteration) {

		try {
			FileInputStream fileIn = new FileInputStream(new File(datatablePath));
			XSSFWorkbook workbook = new XSSFWorkbook(fileIn);
			sheet = workbook.getSheet(sheetName);
			int rowNum = -1, colNum = -1;

			// Find the Row and Column count
			int rowCount = getRowCount();
			int colCount = getColCount();

			DataFormatter formatter = new DataFormatter();
			String val;
			int rowIteration;

			// Find the Column Number
			for (int iCol = 1; iCol < colCount; iCol++) {
				val = formatter.formatCellValue(sheet.getRow(0).getCell(iCol));
				if (val.trim().equalsIgnoreCase(colName)) {
					colNum = iCol;
					break;
				}
			}

			// Find the Row Number
			for (int iRow = 1; iRow < rowCount; iRow++) {

				val = formatter.formatCellValue(sheet.getRow(iRow).getCell(0));
				rowIteration = Integer.valueOf(formatter.formatCellValue(sheet.getRow(iRow).getCell(1)));

				if (val.trim().equalsIgnoreCase(testcasename) && rowIteration == iteration) {
					rowNum = iRow;
					break;
				}
			}

			sheet.getRow(rowNum).createCell(colNum).setCellValue(exportData);

			// Write the output to the file
			FileOutputStream fileOut = new FileOutputStream(datatablePath);
			workbook.write(fileOut);
			fileOut.close();

			// Closing the workbook
			workbook.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			e.printStackTrace();
		}
	}

}
