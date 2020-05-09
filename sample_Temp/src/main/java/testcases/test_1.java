package testcases;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

public class test_1 {

	String WBPath;
	XSSFWorkbook wb;
	XSSFSheet ws;

	String testcasename = this.getClass().toString();

	@Test

	public void testcase() throws Exception {

		WBPath = "E:\\OfficeWork\\EclipseWorkspace2\\sample_Temp\\datatable\\datatable.xlsx";

		wb = new XSSFWorkbook(WBPath);

		ws = wb.getSheet("HomePage");

		int row = rowNumber("TC_3");
		int column = colNumber("Password");

		XSSFCell cell = ws.getRow(row).getCell(column);

		System.out.println(cell);

	}

	public int rowNumber(String tcName) {

		int rowCount = ws.getPhysicalNumberOfRows();
		String val;

		for (int iRow = 0; iRow < rowCount; iRow++) {
			val = ws.getRow(iRow).getCell(0).toString();
			if (val.equalsIgnoreCase(tcName)) {
				return iRow;
			}
		}
		return -1;

	}

	public int colNumber(String header) {

		int colCount = ws.getRow(0).getPhysicalNumberOfCells();
		String val;

		for (int iCol = 0; iCol < colCount; iCol++) {
			val = ws.getRow(0).getCell(iCol).toString();
			if (val.equalsIgnoreCase(header)) {
				return iCol;
			}
		}
		return -1;

	}

}
