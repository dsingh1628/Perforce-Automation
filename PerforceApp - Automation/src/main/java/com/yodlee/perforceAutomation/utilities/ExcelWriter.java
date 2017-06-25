package com.yodlee.perforceAutomation.utilities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yodlee.perforeAutomation.PerforceApp.Result;

public class ExcelWriter {

	public static void writeExcelFile(ArrayList<Result> resultList, String fileName) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Agent Changes");
		sheet.setDefaultColumnWidth(50);

		XSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(BorderStyle.THICK);
		style.setBorderTop(BorderStyle.THICK);
		style.setBorderRight(BorderStyle.THICK);
		style.setBorderLeft(BorderStyle.THICK);
		sheet.setAutoFilter(CellRangeAddress.valueOf("A1:D1"));

		CellStyle blueStyle = workbook.createCellStyle();
		blueStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		blueStyle.setBorderBottom(BorderStyle.THICK);
		blueStyle.setBorderLeft(BorderStyle.THICK);
		blueStyle.setBorderRight(BorderStyle.THICK);
		blueStyle.setBorderTop(BorderStyle.THICK);

		int rowNum = 0;
		System.out.println("Creating excel");
		int headerCol = 0;
		Row header = sheet.createRow(rowNum++);
		header.createCell(headerCol++).setCellValue("Agent Name");
		header.createCell(headerCol++).setCellValue("User Name");
		header.createCell(headerCol++).setCellValue("Comment");
		header.createCell(headerCol++).setCellValue("Date");

		for (Result result : resultList) {
			Row row = sheet.createRow(rowNum++);
			int colNum = 0;
			row.createCell(colNum++).setCellValue((String) result.getAgentname());
			row.createCell(colNum++).setCellValue(result.getUserName());
			row.createCell(colNum++).setCellValue(result.getComment());
			row.createCell(colNum++).setCellValue(result.getDateString());

		}

		try {
			FileOutputStream outputStream = new FileOutputStream(fileName);
			workbook.write(outputStream);
			workbook.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Done");
	}
}
