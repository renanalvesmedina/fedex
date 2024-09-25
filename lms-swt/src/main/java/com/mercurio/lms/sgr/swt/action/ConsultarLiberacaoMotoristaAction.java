package com.mercurio.lms.sgr.swt.action;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sgr.dto.FiltroLiberacaoMotoristaDto;
import com.mercurio.lms.sgr.model.HistLiberacaoMotorista;
import com.mercurio.lms.sgr.model.service.HistLiberacoMotoristaService;
import com.mercurio.lms.util.JTDateTimeUtils;

public class ConsultarLiberacaoMotoristaAction extends CrudAction {
	
	private HistLiberacoMotoristaService histLiberacoMotoristaService;
	
	public ResultSetPage<HistLiberacaoMotorista> findPaginated(TypedFlatMap criteria) {
		FiltroLiberacaoMotoristaDto filtro = getFiltro(criteria);
		return histLiberacoMotoristaService.findPaginated(filtro, FindDefinition.createFindDefinition(criteria));
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		FiltroLiberacaoMotoristaDto filtro = getFiltro(criteria);
		return histLiberacoMotoristaService.getRowCount(filtro);
	}

	private FiltroLiberacaoMotoristaDto getFiltro(TypedFlatMap criteria) {
		FiltroLiberacaoMotoristaDto filtro = new FiltroLiberacaoMotoristaDto();
		filtro.setDhPeriodoIni(criteria.getDateTime("dhPeriodoIni"));
		filtro.setDhPeriodoFim(criteria.getDateTime("dhPeriodoFim"));
		filtro.setCpfMotorista(criteria.getString("cpfMotorista"));
		filtro.setNmMotorista(criteria.getString("nmMotorista"));
		return filtro;
	}
	
	public Map getDadosDefault() {
		Map<String, DateTime> result = new HashMap<String, DateTime>();
		result.put("dhPeriodoIni", JTDateTimeUtils.getDataHoraAtual().minusDays(1));
		result.put("dhPeriodoFim", JTDateTimeUtils.getDataHoraAtual());
		return result;
	}
	
	public Map findConteudoArquivo(TypedFlatMap criteria) {
		Long idHistLiberacaoMotorista = criteria.getLong("idHistLiberacaoMotorista");
		String conteudo = histLiberacoMotoristaService.findConteudoArquivo(idHistLiberacaoMotorista);
		
		Map map = new TypedFlatMap();
		map.put("arquivo",conteudo);
		
		return map;
	}
	
	public void setHistLiberacoMotoristaService(HistLiberacoMotoristaService histLiberacoMotoristaService) {
		this.histLiberacoMotoristaService = histLiberacoMotoristaService;
	}

}
