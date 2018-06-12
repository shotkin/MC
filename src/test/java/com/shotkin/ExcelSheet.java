/**
 * 
 */
package com.shotkin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author David Shotkin
 */
public class ExcelSheet implements AutoCloseable {

	private final XSSFWorkbook workbook;
	private final XSSFSheet excelSheet;

	/**
	 * 
	 * @param workbookPath
	 *            from src/test/resources to workbook file (including the filename and extension)
	 * @param sheetName
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws URISyntaxException
	 */
	ExcelSheet(String workbookPath, String sheetName) throws IOException, URISyntaxException, InvalidFormatException {
		File file = new File(getClass().getClassLoader().getResource(workbookPath).getFile());
		workbook = new XSSFWorkbook(file);
		excelSheet = workbook.getSheet(sheetName);
		if (excelSheet == null)
			throw new IOException("Workbook " + workbookPath + " does not contain sheet " + sheetName + ".");
	}

	public List<XSSFRow> getRows() {
		List<XSSFRow> rows = new ArrayList<>();
		for (int rowNum = 1; rowNum <= excelSheet.getLastRowNum(); rowNum++)
			rows.add(excelSheet.getRow(rowNum));
		return rows;
	}

	public int getLastRowNum() {
		return excelSheet.getLastRowNum();
	}

	/* (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close() */
	@Override
	public void close() throws IOException {
		workbook.close();
	}
}
