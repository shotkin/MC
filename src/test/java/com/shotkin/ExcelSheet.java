package com.shotkin;

import java.io.File;
import java.io.IOException;
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

	/**
	 * The workbook found at {@link #workbookPath}
	 */
	private final XSSFWorkbook workbook;
	/**
	 * The workbookPath which was passed into the constructor
	 */
	private final String workbookPath;

	/**
	 * @param workbookPath
	 *            from src/test/resources to workbook file (including the filename and extension)
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	ExcelSheet(String workbookPath) throws InvalidFormatException, IOException {
		this.workbookPath = workbookPath;
		File file = new File(getClass().getClassLoader().getResource(workbookPath).getFile());
		workbook = new XSSFWorkbook(file);
	}

	/**
	 * @param sheetName
	 *            the name of a sheet in {@link #workbook}
	 * @return All the rows in sheet sheetName except for the first one (which is presumed to be a header row)
	 * @throws IOException
	 *             if {@link #workbook} does not contain a sheet named sheetName
	 */
	public List<XSSFRow> getRows(String sheetName) throws IOException {
		XSSFSheet excelSheet = getSheet(sheetName);
		List<XSSFRow> rows = new ArrayList<>();
		for (int rowNum = 1; rowNum <= excelSheet.getLastRowNum(); rowNum++)
			rows.add(excelSheet.getRow(rowNum));
		return rows;
	}

	/**
	 * @param sheetName
	 * @return sheet sheetName in {@link #workbook}
	 * @throws IOException
	 *             if {@link #workbook} does not contain a sheet named sheetName
	 */
	private XSSFSheet getSheet(String sheetName) throws IOException {
		XSSFSheet excelSheet = workbook.getSheet(sheetName);
		if (excelSheet == null)
			throw new IOException("Workbook " + workbookPath + " does not contain sheet " + sheetName + ".");
		return excelSheet;
	}

	/* (non-Javadoc)
	 * 
	 * @see java.lang.AutoCloseable#close() */
	@Override
	public void close() throws IOException {
		workbook.close();
	}
}
