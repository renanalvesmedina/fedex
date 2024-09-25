package com.mercurio.lms.rest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.adsm.rest.ResponseDTO;
import com.mercurio.lms.rest.utils.ExportUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class LmsBaseCrudReportRest<S extends BaseDTO, L extends BaseDTO, F extends BaseFilterDTO> extends BaseCrudRest<S, L, F> {

	private static final Logger LOGGER = LogManager.getLogger(LmsBaseCrudReportRest.class);

	/**
	 * Retorna o Diretorio do relatorio.
	 * 
	 * @return
	 */
	private File getReportOutputDir() {
		ReportExecutionManager reportExecutionManager = BeanUtils.getBean(ReportExecutionManager.class);
		return reportExecutionManager.getReportOutputDir();
	}
	
	/**
	 * Retorna um Mapa que deve definir o label e o dado de todas as colunas.
	 * 
	 * @return
	 */
	protected abstract List<Map<String, String>> getColumns();
	
	/**
	 * Metodo que busca os dados para o relatório.
	 * 
	 * @param filter
	 * @return
	 */
	protected abstract List<Map<String, Object>> findDataForReport(F filter);
	
	/**
	 * Cria e retona o relatorio.
	 * 
	 * @param filter
	 * @return
	 */
	@POST
	@Path("reportCsv")
	public Response reportCsvRest(F filter) {
		if (Boolean.FALSE.equals(isValidLimit(filter, ROW_LIMIT, count(filter)))) {
			return getException();
		}
		List<Map<String, Object>> list = findDataForReport(filter);
		return createFile(list);
	}

	protected File createFileColumnDTNatura(List<Map<String, Object>> list, Boolean exibeDtNatura) {
		List<Map<String, String>> colunas = getColumns();
		
		if ( Boolean.FALSE.equals(exibeDtNatura)){
			int indiceDtNatura  = -1;
			for(int i = 0 ; i <= colunas.size()-1; i++){
				if ( "DtNatura".equals(colunas.get(i).get("column")) ){
					indiceDtNatura = i;
				}
			}
			if (indiceDtNatura > -1){
				colunas.remove(indiceDtNatura);
			}
		}
		try {
			return ExportUtils.exportFileCsv(getReportOutputDir(), "CSV", list, colunas);

		} catch (IOException e) {

		}
		return null;
	}
	
	protected Response createFile(List<Map<String, Object>> list) {
		
		ResponseDTO responseDTO = new ResponseDTO();
		if (list.isEmpty()) {
			responseDTO.setInfo(getLabel("grid.paginacao.nenhum-registro").replace("<BR>", ""));
			return Response.ok(responseDTO).build();
		}
		
		try {
			responseDTO.setFileName(ExportUtils.exportCsv(getReportOutputDir(), "CSV", list, getColumns()));
		} catch (IOException e) {
			responseDTO.setError(getLabel("fileReportError") + e.getMessage());
			LOGGER.info(e);
		}
		
		return Response.ok(responseDTO).build();
	}
	/**
	 *Monta o mapa que contem o label e a coluna do relatorio. 
	 * 
	 * @param label
	 * @param column
	 * @return
	 */
	protected Map<String,String> getColumn(String label, String column) {
		Map<String,String> retorno = new HashMap<>();
		retorno.put("title", getLabel(label));
		retorno.put("column", column);
		return retorno;
	}

}
