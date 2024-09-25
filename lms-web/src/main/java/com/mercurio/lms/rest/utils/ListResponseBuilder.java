package com.mercurio.lms.rest.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.cache.RecursoMensagemCache;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.dto.FiltroPaginacaoDto;
import com.mercurio.lms.rest.BeanUtils;

public class ListResponseBuilder {

	private static final Log log = LogFactory.getLog(ListResponseBuilder.class);

	private final FiltroPaginacaoDto filtro;
	private final Integer limiteRegistros;
	private final File reportOutputDir;
	private final String name;
	private final List<Map<String, String>> columns;
	private Closure<List<Map<String, Object>>> find;
	private Closure<Integer> rowCount;
	private boolean suppressWarning = false;
	
	public static Integer getLimiteRegistros(Boolean isReport) {
		ParametroGeralService parametroGeralService = BeanUtils.getBean(ParametroGeralService.class);
		return Boolean.TRUE.equals(isReport) ? Integer.parseInt(parametroGeralService.findByNomeParametro("VL_LIMITE_REGISTROS_CSV", false).getDsConteudo()) : Integer.parseInt(parametroGeralService.findByNomeParametro("VL_LIMITE_REGISTROS_GRID", false).getDsConteudo());
	}

	private File getReportOutputDir() {
		ReportExecutionManager reportExecutionManager = BeanUtils.getBean(ReportExecutionManager.class);
		return reportExecutionManager.getReportOutputDir();
	}

	public ListResponseBuilder(FiltroPaginacaoDto filtro, String name,
			List<Map<String, String>> columns) {
		super();
		this.filtro = filtro;
		if (filtro != null) {
			this.limiteRegistros = getLimiteRegistros(filtro.getReport());
		} else {
			this.limiteRegistros = -1;
		}
		if (filtro != null && Boolean.TRUE.equals(filtro.getReport())) {
			this.reportOutputDir = getReportOutputDir();
		} else {
			this.reportOutputDir = null;
		}
		this.name = name;
		this.columns = columns;
	}

	public ListResponseBuilder(FiltroPaginacaoDto filtro,
			Integer limiteRegistros, File reportOutputDir, String name,
			List<Map<String, String>> columns) {
		super();
		this.filtro = filtro;
		this.limiteRegistros = limiteRegistros;
		this.reportOutputDir = reportOutputDir;
		this.name = name;
		this.columns = columns;
	}

	public ListResponseBuilder() {
		super();
		this.filtro = null;
		this.limiteRegistros = -1;
		this.reportOutputDir = null;
		this.name = null;
		this.columns = null;
	}
	
	public ListResponseBuilder findClosure(Closure<List<Map<String, Object>>> find) {
		this.find = find;
		return this;
	}
	
	public ListResponseBuilder rowCountClosure(Closure<Integer> rowCount) {
		this.rowCount = rowCount;
		return this;
	}
	
	public ListResponseBuilder suppressWarning() {
		this.suppressWarning = true;
		return this;
	}
	
	public Map<String, Object> toMap() {
		Integer qtRegistros = rowCount.execute();

		if (suppressWarning || limiteRegistros <= 0 || qtRegistros <= limiteRegistros){
			if (filtro != null && Boolean.TRUE.equals(filtro.getReport())) {
				long inicio = System.currentTimeMillis();
				log.debug("[Geracao CSV] Iniciando busca dos dados");
				List<Map<String, Object>> list = find.execute();
				log.debug("[Geracao CSV] Tempo busca dos dados: " + (System.currentTimeMillis() - inicio) + " ms");
				if (list.size() == 0) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("info", RecursoMensagemCache.getMessage("grid.paginacao.nenhum-registro", LocaleContextHolder.getLocale().toString().toLowerCase()).replace("<BR>", ""));
					return map;
				}
				String fileName = "";
				try {
					fileName = ExportUtils.exportCsv(reportOutputDir, name, list, columns);
				} catch (IOException e) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("error", "Erro ao gerar arquivo temporário do relatório: " + e.getMessage());
					return map;
				}
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("fileName", fileName);
				return map;
			} else {
				
				List<Map<String, Object>> list = find.execute();
					
				Map<String, Object> toReturn = new HashMap<String, Object>();
				toReturn.put("list", list);
				toReturn.put("qtRegistros", qtRegistros);
				
				return toReturn;
				
			}
		}else{
			Map<String, Object> map = new HashMap<String, Object>();
			StringBuilder msg = new StringBuilder();
			msg.append("Sua pesquisa ultrapassou " + limiteRegistros + " resultados, refine os filtros e tente novamente");
			if (!Boolean.TRUE.equals(filtro.getReport())) {
				msg.append(" ou selecione a opção Exportar excel");
			}
			msg.append(".");
			map.put("warning", msg.toString());
			return map;
		}
	}
	
	public Response build() {
		
		return Response.ok(toMap()).build();

	}
	
}
