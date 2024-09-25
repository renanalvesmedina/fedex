package com.mercurio.lms.entrega.reports;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;


public abstract class ExcelRemoteReportRunnerCCT {
	
	private String fileName;
	private byte[] byteArrayTemplate;
	private File reportOutputDir;
	protected Map dadosGerais;
	protected List<Map<String, Object>> dadosItens;
	protected static final String DATE_FORMAT = "dd/MM/yyyy";
	protected NumberFormat formatDuasCasasDecimais = new DecimalFormat("###,##0.00", new DecimalFormatSymbols(LocaleContextHolder.getLocale()));
	protected NumberFormat formatTresCasasDecimais = new DecimalFormat("###,##0.000", new DecimalFormatSymbols(LocaleContextHolder.getLocale()));

	public ExcelRemoteReportRunnerCCT(byte[] byteArrayTemplate, String fileName, File reportOutputDir){
		this.byteArrayTemplate = byteArrayTemplate;
		this.fileName = fileName;
		this.reportOutputDir = reportOutputDir;
	}
	
	protected abstract void fillReportDadosGerais(HSSFSheet sheet);
	protected abstract void fillReportDadosTotalizadores(HSSFSheet sheet);

	public File generateReportFile() throws Exception{
		HSSFWorkbook workbook = getWorkbook();
		HSSFSheet sheet = workbook.getSheetAt(0);
		fillReportDadosGerais(sheet);
		fillReportDadosItens(sheet);
		fillReportDadosTotalizadores(sheet);
		ExcelReportUtilsCCT.clearReport(sheet);
		return getFileFromWorkbook(workbook, fileName);
	}
	
	private File getFileFromWorkbook(HSSFWorkbook workbook, String nomeArquivo) throws Exception{
		File templateFile = File.createTempFile(nomeArquivo.substring(0, nomeArquivo.lastIndexOf(".")) + "-", nomeArquivo.substring(nomeArquivo.lastIndexOf(".")), reportOutputDir);
		FileOutputStream fileOutputStream = new FileOutputStream(templateFile);
		workbook.write(fileOutputStream);
		fileOutputStream.close();
		return templateFile;
	}
	
	private HSSFWorkbook getWorkbook() throws Exception{
		HSSFWorkbook workbook = new HSSFWorkbook(new ByteArrayInputStream(byteArrayTemplate));
		return workbook;
	}
	
	protected void fillReportDadosItens(HSSFSheet sheet) {
		if (dadosItens != null && !dadosItens.isEmpty()) {
			HSSFRow listIdentifierRow = ExcelReportUtilsCCT.createRowsList(sheet, dadosItens.size());
			if (listIdentifierRow != null) {
				iteratorRows(sheet, listIdentifierRow);
			}
		}
	}
	
	/**
	 * 
	 * @param sheet
	 * @param listIdentifierRow
	 */
	private void iteratorRows(HSSFSheet sheet, HSSFRow listIdentifierRow){
		int rowNum;
		HSSFRow row;
		int rowCount = 0;
		List<String> keysStringToBigDecimal2 = getItensKeysStringToBigDecimalTwo();
		List<String> keysStringToBigDecimal3 = getItensKeysStringToBigDecimalThree();
		
		for (rowNum = listIdentifierRow.getRowNum(); rowNum <= sheet.getLastRowNum(); rowNum++) {
			row = sheet.getRow(rowNum);
			if (row != null && rowCount < dadosItens.size()) {
				iteratorColumns(sheet, rowCount, rowNum, row, keysStringToBigDecimal2, keysStringToBigDecimal3);
				rowCount++;
			}
		}
	}
	
	/**
	 * Itera as colunas da planilha.
	 * 
	 * @param sheet
	 * @param rowCount
	 * @param rowNum
	 * @param row
	 * @param keysStringToBigDecimal2
	 * @param keysStringToBigDecimal3
	 */
	private void iteratorColumns(HSSFSheet sheet, int rowCount, int rowNum, HSSFRow row, List<String> keysStringToBigDecimal2, List<String> keysStringToBigDecimal3){
		short columnNum;
		HSSFCell cell;
		Map<String, Object> dadosItem = dadosItens.get(rowCount);

		for (columnNum = sheet.getRow(rowNum).getFirstCellNum(); columnNum <= sheet.getRow(rowNum).getLastCellNum(); columnNum++) {
			cell = row.getCell(columnNum);
			if (cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
				String cellValue = cell.getStringCellValue();
				
				if (cellValue != null && cellValue.startsWith(ExcelReportUtilsCCT.START_VAR)) {
					fillItem(cellValue, cell, dadosItem, keysStringToBigDecimal2, keysStringToBigDecimal3);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param cellValue
	 * @param cell
	 * @param dadosItem
	 * @param keysStringToBigDecimal2
	 * @param keysStringToBigDecimal3
	 */
	private void fillItem(String cellValue, HSSFCell cell, Map<String, Object> dadosItem, List<String> keysStringToBigDecimal2, List<String> keysStringToBigDecimal3){
		String key = cellValue.substring(ExcelReportUtilsCCT.START_VAR.length(), cellValue.length() - 1);
		Object value = dadosItem.get(key);

		if (value != null) {
			fillItemValue(cell, key, value, keysStringToBigDecimal2, keysStringToBigDecimal3);

		} else {
			cell.setCellType(HSSFCell.CELL_TYPE_BLANK);
		}
	}
	
	/**
	 * Realiza a troca da chave contida na célula pelo seu respectivo valor.
	 * 
	 * @param cell
	 * @param key
	 * @param value
	 * @param keysStringToBigDecimal2
	 * @param keysStringToBigDecimal3
	 */
	protected void fillItemValue(HSSFCell cell, String key, Object value, List<String> keysStringToBigDecimal2, List<String> keysStringToBigDecimal3) {
		if (value instanceof String) {
			replaceStringValue(cell, key, value, keysStringToBigDecimal2, keysStringToBigDecimal3);
		} else if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else if (value instanceof BigDecimal) {
			if ("vl_frete".equals(key)) {
				cell.setCellValue(formatDuasCasasDecimais.format((BigDecimal) value));
			} else {
				cell.setCellValue(formatTresCasasDecimais.format((BigDecimal) value));
			}
		} else if (value instanceof YearMonthDay) {
			cell.setCellValue(((YearMonthDay) value).toString(DATE_FORMAT));
		} else if (value instanceof DateTime) {
			cell.setCellValue(((DateTime) value).toString(DATE_FORMAT));
		}
	}
	
	private void replaceStringValue(HSSFCell cell, String key, Object value, List<String> keysStringToBigDecimal2, List<String> keysStringToBigDecimal3){
		if (keysStringToBigDecimal2.contains(key)) {
			cell.setCellValue(formatDuasCasasDecimais.format(new BigDecimal((String) value)));
		} else if (keysStringToBigDecimal3.contains(key)) {
			cell.setCellValue(formatTresCasasDecimais.format(new BigDecimal((String) value)));
		} else if ("dt_emissao_nf".equals(key)) {
			cell.setCellValue(new YearMonthDay(value).toString(DATE_FORMAT));
		} else if ("nr_cnpj_dest".equals(key) || "nr_cnpj_remet".equals(key)) {
			ExcelReportUtilsCCT.setCpfCnpfVar(null, key, value, cell);
		} else {
			cell.setCellValue((String) value);
		}
	}
	
	protected List<String> getItensKeysStringToBigDecimalTwo(){
		List<String> keysStringToBigDecimal2 = new ArrayList<String>();
		keysStringToBigDecimal2.add("vl_unit_item");
		keysStringToBigDecimal2.add("vl_total_item");
		keysStringToBigDecimal2.add("vl_icms");
		keysStringToBigDecimal2.add("tx_icms");
		keysStringToBigDecimal2.add("vl_ipi");
		keysStringToBigDecimal2.add("tx_ipi");
		keysStringToBigDecimal2.add("vl_total_nf");
		return keysStringToBigDecimal2;
	}
	
	protected List<String> getItensKeysStringToBigDecimalThree(){
		List<String> keysStringToBigDecimal3 = new ArrayList<String>();
		keysStringToBigDecimal3.add("vl_peso_bruto");
		keysStringToBigDecimal3.add("vl_qtde_item");
		return keysStringToBigDecimal3;
	}
	
	public List<Map<String, Object>> getDadosItens() {
		return dadosItens;
	}
	public void setDadosItens(List<Map<String, Object>> dadosItens) {
		this.dadosItens = dadosItens;
	}
	public Map getDadosGerais() {
		return dadosGerais;
	}
	public void setDadosGerais(Map dadosGerais) {
		this.dadosGerais = dadosGerais;
	}
}