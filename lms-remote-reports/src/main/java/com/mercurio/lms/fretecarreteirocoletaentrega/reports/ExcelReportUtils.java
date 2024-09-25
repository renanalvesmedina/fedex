package com.mercurio.lms.fretecarreteirocoletaentrega.reports;

import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class ExcelReportUtils {
	
	public static final String START_VAR = "${";
	protected static final String END_VAR = "}";
	private static final Pattern varPattern = Pattern.compile("\\$\\{.*?\\}");
	
	/**
	 * Procura por uma célula do tipo cellType
	 * no sheet que contenha a String cellContent
	 * 
	 * Utilizada para buscar variáveis '$()' numa sheet.
	 * Caso a string não seja única dentro do sheet, 
	 * a primeira ocorrência será retornada.  
	 */
	public static HSSFCell findCell(HSSFSheet sheet, String cellContent, int cellType) {
		int rowNum;
		short columnNum;	
		HSSFRow row;
		HSSFCell cell;			
		for (rowNum = sheet.getFirstRowNum(); rowNum <= sheet.getLastRowNum(); rowNum++){
			row = sheet.getRow(rowNum);
			if (row != null){
				for (columnNum = sheet.getRow(rowNum).getFirstCellNum(); columnNum <= sheet.getRow(rowNum).getLastCellNum(); columnNum++){
					cell = row.getCell(columnNum);
					if (cell != null && cell.getCellType() == cellType) {
						if (cell.getStringCellValue().contains(cellContent))
							return cell;
					}
				}
			}
		}			
		return null;
	}
	
	/**
	 * Procura por uma linha no sheet, onde a primeira célula válida (isto é, não nula)
	 * tenha conteúdo igual a cellContent e tipo cellType
	 * 
	 * Utilizada para buscar linhas, como por exemplo de uma tabela, numa sheet.
	 * Caso exista mais de uma célula com mesmo conteúdo e tipo no sheet, 
	 * a primeira ocorrência da linha será retornada. 
	 */
	public static HSSFRow findRow(HSSFSheet sheet, String firstCellContent, int firstCellType){
		int rowNum;
		short columnNum;
		HSSFRow row;
		HSSFCell cell;			
		for (rowNum = sheet.getFirstRowNum(); rowNum < sheet.getLastRowNum(); rowNum++){
			row = sheet.getRow(rowNum);
			if (row != null){
				for (columnNum = sheet.getRow(rowNum).getFirstCellNum(); columnNum <= sheet.getRow(rowNum).getLastCellNum(); columnNum++){
					cell = row.getCell(columnNum);
					if (cell != null && cell.getCellType() == firstCellType) {
						if (cell.getStringCellValue().equals(firstCellContent))
							return row;
						else
							break;
					}
				}
			}
		}			
		return null;
	}	
	
	/**
	 * Procura pela primeira célula válida (não nula) do tipo cellType em uma dada linha.
	 */
	public static HSSFCell findFirstCellInRow(HSSFRow row, int cellType){
		short columnNum;
		HSSFCell cell;			
		for (columnNum = row.getFirstCellNum(); columnNum <= row.getLastCellNum(); columnNum++){
			cell = row.getCell(columnNum);
			if (cell != null && cell.getCellType() == cellType) {
				return cell;
			}
		}
		return null;			
	}
	
	/**
	 * Duplica (copia) uma linha da sheet para outra linha, deslocando todo conteudo da sheet para o fim
	 */
	public static void duplicateRow(HSSFSheet sheet, int srcRow, int dstRow){
		HSSFRow row = sheet.getRow(srcRow);
		HSSFRow newRow = sheet.getRow(dstRow);
		if(newRow == null){
			newRow = sheet.createRow(dstRow);
		}
		
		sheet.shiftRows(newRow.getRowNum(), sheet.getLastRowNum(), 1);
		short columnNum;
		for (columnNum = row.getFirstCellNum(); columnNum <= row.getLastCellNum(); columnNum++){
			HSSFCell cell = newRow.createCell(columnNum);
			if (row.getCell(columnNum) != null){
				cell.setCellType(row.getCell(columnNum).getCellType());
				switch (row.getCell(columnNum).getCellType()){
					case HSSFCell.CELL_TYPE_BLANK:
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN:
						cell.setCellValue(row.getCell(columnNum).getBooleanCellValue());
						break;
					case HSSFCell.CELL_TYPE_ERROR:
						cell.setCellErrorValue(row.getCell(columnNum).getErrorCellValue());
						break;
					case HSSFCell.CELL_TYPE_FORMULA:
						cell.setCellFormula(row.getCell(columnNum).getCellFormula());
						break;
					case HSSFCell.CELL_TYPE_NUMERIC:
						cell.setCellValue(row.getCell(columnNum).getNumericCellValue());
						break;
					case HSSFCell.CELL_TYPE_STRING:
						cell.setCellValue(row.getCell(columnNum).getStringCellValue());
						break;
				}
				cell.setCellStyle(row.getCell(columnNum).getCellStyle());
			}
		}
	}
	
	/**
	 * Duplica múltiplas linhas consecutivas na sheet, para uma linha destino.
	 */
	public static void duplicateMultipleRows(HSSFSheet sheet, int firstRow, int lastRow, int dstRow){
		int row;
		for (row = firstRow; row <= lastRow; row++){
			duplicateRow(sheet, row, dstRow + (row - firstRow));
		}
	}
	
	/**
	 * Copia as linhas de uma tabela na sheet, tantas vezes quanto numberOfCopies,
	 * considerando todas linhas entre as margens + uma linha após a tabela. 
	 */
	public static void copyTable(HSSFSheet sheet, String tableName, int numberOfCopies){
		HSSFRow firstRow = findRow(sheet, tableName, HSSFCell.CELL_TYPE_STRING);
		HSSFCell firstCell = findFirstCellInRow(firstRow, HSSFCell.CELL_TYPE_STRING);
		HSSFRow lastRow;
		HSSFCell cell;
		int rowNum;
		short columnNum;
		for (rowNum = firstRow.getRowNum(); rowNum <= sheet.getLastRowNum(); rowNum++){
			lastRow = sheet.getRow(rowNum);
			cell = lastRow.getCell(firstCell.getCellNum());
			if (cell != null && cell.getCellStyle().getBorderBottom() == firstCell.getCellStyle().getBorderTop()){
				int firstRowNum = firstRow.getRowNum();
				int lastRowNum = lastRow.getRowNum();
				for (int i = 0; i < numberOfCopies; i++){						
					duplicateMultipleRows(sheet, firstRowNum, lastRowNum + 1, lastRowNum + 2);
					firstRowNum = lastRowNum + 2;
					lastRowNum = firstRowNum + (lastRow.getRowNum() - firstRow.getRowNum());
				}
				break;
			}				
		}
	}
	
	/**
	 * Apaga o conteúdo das linhas de uma tabela na sheet. Este método não remove as linhas.
	 * A tabela é procurada através de seu 'nome' na sheet, normalmente na primeira linha.
	 * As bordas de célula são utilizadas para delimitar início e fim da tabela.
	 * ATENÇÃO: para remover tabelas que estejam no meio do excel, deve-se alterar esta função pra fazer shift negativo (tal como em removeRows) 
	 * @param sheet
	 * @param tableName
	 */
	public static void removeTable(HSSFSheet sheet, String tableName){
		HSSFRow firstRow = findRow(sheet, tableName, HSSFCell.CELL_TYPE_STRING);
		HSSFCell firstCell = findFirstCellInRow(firstRow, HSSFCell.CELL_TYPE_STRING);
		HSSFRow lastRow;
		HSSFCell cell;
		int rowNum;
		short columnNum;
		int tableRowNum;
		for (rowNum = firstRow.getRowNum(); rowNum <= sheet.getLastRowNum(); rowNum++){
			lastRow = sheet.getRow(rowNum);
			cell = lastRow.getCell(firstCell.getCellNum());
			if (cell != null && cell.getCellStyle().getBorderBottom() == firstCell.getCellStyle().getBorderTop()){
				for (tableRowNum = firstRow.getRowNum(); tableRowNum <= lastRow.getRowNum(); tableRowNum++){
					sheet.removeRow(sheet.getRow(tableRowNum));
				}
				break;
			}
		}
	}
	
	/**
	 * Remove linhas de uma sheet através de um shift.
	 * @param sheet
	 * @param startRow
	 * @param endRow
	 */
	public static void removeRows(HSSFSheet sheet, int startRow, int endRow){
		sheet.shiftRows(endRow + 1, sheet.getLastRowNum(), - (endRow - startRow + 1));
	}
	
	/**
	 * Define valor para 'value' de uma célula que contenha o valor var.
	 * @param sheet
	 * @param var
	 * @param value
	 */
	public static void setStringVar(HSSFSheet sheet, String var, String value){
		HSSFCell cell = findCell(sheet, START_VAR + var + END_VAR, HSSFCell.CELL_TYPE_STRING);	
		if (cell != null){
			cell.setCellValue(value);
		}
	}
	
	/**
	 * Define valor para 'value' de uma célula que contenha o valor var.
	 * @param sheet
	 * @param var
	 * @param value
	 */
	public static void setNumericVar(HSSFSheet sheet, String var, Double value){
		HSSFCell cell = findCell(sheet, START_VAR + var + END_VAR, HSSFCell.CELL_TYPE_STRING);	
		if (cell != null){
			cell.setCellValue(value);			
		}
	}
	
	/**
	 * Define valor para 'value' de uma célula que contenha o valor var.
	 * Data deve vir no formato YYYY-mm-dd, tal como o sql retorna de uma query.
     * No caso de outras datas, deve ser sobreescrever este metodo, ou usar texto puro.
	 * @param sheet
	 * @param var
	 * @param value
	 */	
	public static void setDateVar(HSSFSheet sheet, String var, String value){
		HSSFCell cell = findCell(sheet, START_VAR + var + END_VAR, HSSFCell.CELL_TYPE_STRING);	
		if (cell != null)
			cell.setCellValue(Date.valueOf(value));
	}
	
	/**
	 * Substitui o valor para 'value' em uma célula que contenha o valor var.
	 * @param sheet
	 * @param var
	 * @param value
	 */
	public static void replaceStringVar(HSSFSheet sheet, String var, String value){
		HSSFCell cell = findCell(sheet, START_VAR + var + END_VAR, HSSFCell.CELL_TYPE_STRING);	
		if (cell != null)
			cell.setCellValue(cell.getStringCellValue().replace(START_VAR + var + END_VAR, value));
	}
	
	/**
	 * 'Limpa' o sheet, isto é, remove qualquer variável que possa não ter sido preenchida por qualquer motivo
	 * evitando que o texto de controle de variáveis (brackets) fiquem no report.
	 * @param sheet
	 */
	public static void clearReport(HSSFSheet sheet){		
		HSSFCell cell;
		HSSFRow row;
		int rowNum;
		short columnNum;
		Matcher matcher;	
		StringBuffer result = new StringBuffer();
		for (rowNum = sheet.getLastRowNum(); rowNum >= sheet.getFirstRowNum(); rowNum--){
			row = sheet.getRow(rowNum);
			if (row != null){
				for (columnNum = row.getLastCellNum(); columnNum >= row.getFirstCellNum(); columnNum--){
					cell = row.getCell(columnNum);
					if (cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_STRING && cell.getStringCellValue().contains(START_VAR)){						
						matcher = varPattern.matcher(cell.getStringCellValue());
						if (matcher.find()){
							cell.setCellValue(matcher.replaceAll(""));
						}
					}
				}
			}
		}
	}
}
