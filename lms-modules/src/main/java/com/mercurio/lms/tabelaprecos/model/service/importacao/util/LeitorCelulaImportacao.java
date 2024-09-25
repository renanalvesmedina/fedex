package com.mercurio.lms.tabelaprecos.model.service.importacao.util;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class LeitorCelulaImportacao {

	private LeitorCelulaImportacao() {

	}

	public static String valorCelula(HSSFCell cell) {
		String retorno = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BLANK:
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			retorno = String.valueOf(cell.getBooleanCellValue()).trim();
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			retorno = cell.getStringCellValue().trim();
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			retorno = cell.getCellFormula().trim();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			retorno = String.valueOf(cell.getNumericCellValue()).trim();
			break;
		default:
			retorno = cell.getStringCellValue().trim();
		}
		return retorno;
	}
}
