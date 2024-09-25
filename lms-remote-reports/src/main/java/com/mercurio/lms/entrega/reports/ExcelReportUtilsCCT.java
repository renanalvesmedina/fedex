package com.mercurio.lms.entrega.reports;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.lms.fretecarreteirocoletaentrega.reports.ExcelReportUtils;

public class ExcelReportUtilsCCT extends ExcelReportUtils {
	private static final String LIST_IDENTIFIER = "#lista-";

	public static HSSFRow findRow(HSSFSheet sheet, String firstCellContent) {
		int rowNum;
		short columnNum;
		HSSFRow row;
		HSSFCell cell;
		
		for (rowNum = sheet.getFirstRowNum(); rowNum <= sheet.getLastRowNum(); rowNum++) {
			row = sheet.getRow(rowNum);
			if (row != null) {
				for (columnNum = sheet.getRow(rowNum).getFirstCellNum(); columnNum <= sheet.getRow(rowNum).getLastCellNum(); columnNum++) {
					cell = row.getCell(columnNum);
					if (cell != null && cell.getStringCellValue() != null  && cell.getStringCellValue().startsWith(firstCellContent)) {
						return row;
					} else {
						break;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Cria tantas linhas quanto existirem dados na lista.
	 * 
	 * @param sheet
	 * @param size
	 */
	public static HSSFRow createRowsList(HSSFSheet sheet, int size){
		HSSFRow firstRow = findRow(sheet, LIST_IDENTIFIER);
		if(firstRow != null){
			HSSFCell firstCell = findFirstCellInRow(firstRow, HSSFCell.CELL_TYPE_STRING);
			
			String valor = firstCell.getStringCellValue();
			firstCell.setCellValue(valor.replace(LIST_IDENTIFIER, ""));
			
			int firstRowNum = firstRow.getRowNum();
			
			for (int i = 1; i < size; i++) {
				duplicateRow(sheet, firstRowNum,  firstRowNum + i);
			}
		}
		return firstRow;
	}

	public static void setDateStringVar(HSSFSheet sheet, String var, Object value){
		if (value != null){
			setFormatedDateVar(sheet, var, new YearMonthDay(value));
		} else {
			setFormatedDateVar(sheet, var, null);
		}
	}
	
	/**
	 * Seta valor para data no formato dd/mm/yyyy.
	 * @param sheet
	 * @param var
	 * @param value
	 */
	public static void setFormatedDateVar(HSSFSheet sheet, String var, Object value) {
		HSSFCell cell = findCell(sheet, START_VAR + var + END_VAR, HSSFCell.CELL_TYPE_STRING);
		if (cell != null) {
			if (value != null) {
				if (value instanceof YearMonthDay) {
					cell.setCellValue(((YearMonthDay) value).toString("dd/MM/yyyy"));
				} else if (value instanceof DateTime) {
					cell.setCellValue(((DateTime) value).toString("dd/MM/yyyy"));
				}
			} else {
				cell.setCellValue("");
			}
		}
	}
	
	/**
	 * Seta valor para hora no formato hh:mm.
	 * @param sheet
	 * @param var
	 * @param value
	 */
	public static void setTimeVar(HSSFSheet sheet, String var, Object value) {
		HSSFCell cell = findCell(sheet, START_VAR + var + END_VAR, HSSFCell.CELL_TYPE_STRING);
		if (cell != null) {
			if (value != null) {
				cell.setCellValue(((TimeOfDay) value).toString("HH:mm"));
			} else {
				cell.setCellValue("");
			}
		}
	}
	
	/**
	 * Seta valor para númerico com 2 ou 3 casas decimais.
	 * 
	 * @param sheet
	 * @param var
	 * @param value
	 */
	public static void setNumericVar(HSSFSheet sheet, String var, Object value, Integer qtdDecimal) {
		HSSFCell cell = findCell(sheet, START_VAR + var + END_VAR, HSSFCell.CELL_TYPE_STRING);
		if (cell != null) {
			if (value != null) {
				NumberFormat format = null;
				if (qtdDecimal.intValue() == 2) {
					format = new DecimalFormat("###,##0.00", new DecimalFormatSymbols(LocaleContextHolder.getLocale()));
				} else if (qtdDecimal.intValue() == 3) {
					format = new DecimalFormat("###,##0.000", new DecimalFormatSymbols(LocaleContextHolder.getLocale()));
				}

				if (value instanceof String) {
					cell.setCellValue(format.format(new BigDecimal((String) value)));
				} else if (value instanceof BigDecimal) {
					cell.setCellValue(format.format((BigDecimal) value));
				}
			} else {
				cell.setCellValue("");
			}
		}
	}
	
	/**
	 * 
	 * @param sheet
	 * @param var
	 * @param value
	 */
	public static void setStringVar(HSSFSheet sheet, String var, Object value){
		HSSFCell cell = findCell(sheet, START_VAR + var + END_VAR, HSSFCell.CELL_TYPE_STRING);	
		if (cell != null){
			cell.setCellValue((String) value);
		}
	}
	
	public static void setLongVar(HSSFSheet sheet, String var, Object value){
		HSSFCell cell = findCell(sheet, START_VAR + var + END_VAR, HSSFCell.CELL_TYPE_STRING);	
		if (cell != null){
			if(value != null){
				cell.setCellValue((Long) value);
			} else {
				cell.setCellValue("");
			}
		}
	}
	
	/**
	 * Seta o valor da célula com CPF ou CNPJ formatado. Replicada a lógica de
	 * formatação pois este módulo não tem visibilidade da classe
	 * com.mercurio.lms.util.FormatUtils, e no momento não é possível alterar
	 * isto, pois gera dependência cíclica entre dos módulos.
	 * 
	 * @param sheet
	 * @param var
	 * @param value
	 */
	public static void setCpfCnpfVar(HSSFSheet sheet, String var, Object value, HSSFCell cell) {
		if(cell == null && sheet != null){
			cell = findCell(sheet, START_VAR + var + END_VAR, HSSFCell.CELL_TYPE_STRING);
		}
		
		if (cell != null) {
			if (value != null) {
				String nrIdentificacao = (String) value;
				if (nrIdentificacao.length() < 12 && nrIdentificacao.length() > 8) {
					StringBuffer format = new StringBuffer().append(nrIdentificacao.substring(0, 3)).append(".")
							.append(nrIdentificacao.substring(3, 6)).append(".").append(nrIdentificacao.substring(6, 9)).append("-")
							.append(nrIdentificacao.substring(9));
					nrIdentificacao = format.toString();

				} else if (nrIdentificacao.length() >= 12) {
					StringBuffer format = new StringBuffer().append(nrIdentificacao.substring(0, 2)).append(".")
							.append(nrIdentificacao.substring(2, 5)).append(".").append(nrIdentificacao.substring(5, 8)).append("/")
							.append(nrIdentificacao.substring(8, 12)).append("-").append(nrIdentificacao.substring(12));
					nrIdentificacao = format.toString();
				}
				cell.setCellValue((String) nrIdentificacao);
			} else {
				cell.setCellValue("");
			}
		}
	}
}
