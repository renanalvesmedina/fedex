package com.mercurio.lms.expedicao.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.configuracoes.model.service.ImpressoraFormularioService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.dao.EmitirCTRDAO;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDescargaService;

/**
 * @author Claiton Grings
 * @spring.bean id="lms.expedicao.emitirCTRCanceladosService"
 */
public class EmitirCTRCanceladosService extends EmitirCTOService {
	private EmitirCTRDAO emitirCTRDAO;
	private GerarCTRService gerarCTRService;
	private ImpressoraFormularioService impressoraFormularioService;
	private ConhecimentoService conhecimentoService;
	private MonitoramentoDescargaService monitoramentoDescargaService;
	private ParametroGeralService parametroGeralService;
	
	/**
	 * @return the conhecimentoService
	 */
	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}
	/**
	 * @param conhecimentoService the conhecimentoService to set
	 */
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public EmitirCTRDAO getEmitirCTRDAO() {
		return emitirCTRDAO;
	}
	public void setEmitirCTRDAO(EmitirCTRDAO emitirCTRDAO) {
		this.emitirCTRDAO = emitirCTRDAO;
	}

	public GerarCTRService getGerarCTRService() {
		return gerarCTRService;
	}
	public void setGerarCTRService(GerarCTRService gerarCTRService) {
		this.gerarCTRService = gerarCTRService;
	}

	public ImpressoraFormularioService getImpressoraFormularioService() {
		return impressoraFormularioService;
	}
	public void setImpressoraFormularioService(ImpressoraFormularioService impressoraFormularioService) {
		this.impressoraFormularioService = impressoraFormularioService;
	}

	public List<Map> findCTRCsCanceladosAEmitir(
		String tpOperacaoEmissao,
		String tpOpcaoEmissao,
		Long idFilial,
		Long nrProximoFormulario,
		Long nrConhecimento,
		String dsMacAddress, 
		Long idMonitoramentoDescarga
	) {
		List<Map> listaConhecimentos = new LinkedList<Map>();
		return listaConhecimentos;
	}

	public Map executeEmitirCTRCsCancelados(
		String tpOperacaoEmissao,
		String tpOpcaoEmissao,
		Long idFilial,
		Long nrProximoFormulario,
		Long nrConhecimento,
		String dsMacAddress, 
		Long idMonitoramentoDescarga,
		List<Map> ctrcsDuplicados
	) {
		List forms = new ArrayList();
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<String> ctrcs = new ArrayList<String>();
		
		result.put("ctrcs", ctrcs);
		result.put("forms", forms);
		return result;
	}

	public MonitoramentoDescargaService getMonitoramentoDescargaService() {
		return monitoramentoDescargaService;
	}
	public void setMonitoramentoDescargaService(
			MonitoramentoDescargaService monitoramentoDescargaService) {
		this.monitoramentoDescargaService = monitoramentoDescargaService;
	}
	
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
}