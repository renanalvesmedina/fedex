package com.mercurio.lms.vendas.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.report.EmitirRelatorioClientesComExcecaoService;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.vendas.swt.emitirRelatorioClientesComExcecaoAction"
 */
public class EmitirRelatorioClientesComExcecaoAction {

	private ReportExecutionManager reportExecutionManager;
	private EmitirRelatorioClientesComExcecaoService emitirRelatorioClientesComExcecaoService;
	private RegionalService regionalService;
	private FilialService filialService;

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setEmitirRelatorioClientesComExcecaoServiceService(EmitirRelatorioClientesComExcecaoService emitirRelatorioClientesComExcecaoService) {
		this.emitirRelatorioClientesComExcecaoService = emitirRelatorioClientesComExcecaoService;
	}

	public void setRegionalService(RegionalService regionalService) {
		this.regionalService = regionalService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public String execute(Map parameters) throws Exception {
		return reportExecutionManager.generateReportLocator(emitirRelatorioClientesComExcecaoService, parameters);
	}

	public List<Map<String, String>> findComboRegional() {
		List<Map<String, Object>> regionais = regionalService.findRegionaisVigentes();

		List<Map<String, String>> retorno = new ArrayList<Map<String, String>>();

		for (Map<String, Object> map : regionais) {
			Map<String, String> regionalMap = new HashMap<String, String>();
			regionalMap.put("idRegional", map.get("idRegional").toString());
			regionalMap.put("dsRegional", (String) map.get("siglaDescricao"));
			retorno.add(regionalMap);
		}
		return retorno;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map findRegionalByFilial(Map criteria) {
		List listaFilial = new ArrayList();
		Filial filial = new Filial();
		filial.setIdFilial((Long) criteria.get("idFilial"));
		listaFilial.add(filial);

		List<Regional> regionais = regionalService.findRegionaisByFiliais(listaFilial);
		if (!regionais.isEmpty()) {
			Map retorno = new HashMap();
			retorno.put("value", regionais.get(0).getIdRegional().toString());
			return retorno;
		} else {
			return null;
		}
	}

	public List<Map<String, Object>> findLookupFilialByRegional(Map criteria) {
		criteria.put("empresa.idEmpresa", SessionUtils.getEmpresaSessao().getIdEmpresa());

		if (criteria.get("idRegional") != null) {
			Map regionalFiliais = new HashMap();
			regionalFiliais.put("regional.idRegional", criteria.get("idRegional"));
			criteria.remove("idRegional");
			criteria.put("regionalFiliais", regionalFiliais);
		}

		List<Filial> filiais = filialService.find(criteria);
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		if (filiais != null) {
			for (Filial filial : filiais) {
				Filial filialCompleta = filialService.findById(filial.getIdFilial());

				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filialCompleta.getSgFilial());
				mapFilial.put("idFilial", filialCompleta.getIdFilial());
				mapFilial.put("nmFantasia", filialCompleta.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		
		return result;
	}

}