package com.mercurio.lms.vendas.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.report.EmitirRelatorioProdutividadeVendasService;

/**
 * @spring.bean id="lms.vendas.swt.emitirRelatorioProdutividadeVendasAction"
 */
public class EmitirRelatorioProdutividadeVendasAction {
	
	private ReportExecutionManager reportExecutionManager;
	private RegionalService regionalService;
	private FilialService filialService;
	private EmitirRelatorioProdutividadeVendasService emitirRelatorioProdutividadeVendasService;
	private UsuarioService usuarioService;
	private TabelaPrecoService tabelaPrecoService;
	
	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

	@SuppressWarnings("rawtypes")
	public String execute(Map parameters) throws Exception {
		return reportExecutionManager.generateReportLocator(emitirRelatorioProdutividadeVendasService, parameters);
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> findLookupFilialByRegional(Map criteria) {
		criteria.put("empresa.idEmpresa", SessionUtils.getEmpresaSessao().getIdEmpresa());

		if (criteria.get("idRegional") != null) {
			Map regionalFiliais = new HashMap();
			regionalFiliais.put("regional.idRegional", criteria.get("idRegional"));
			criteria.remove("idRegional");
			criteria.put("regionalFiliais", regionalFiliais);
		}

		List<Filial> filiais = filialService.findLookup(criteria);
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
	
	@SuppressWarnings("rawtypes")
	public List findLookupUsuarioFuncionario(Map criteria){
		TypedFlatMap tMap = new TypedFlatMap(criteria);
		return usuarioService.findLookupUsuarioFuncionario(
			null,
			FormatUtils.fillNumberWithZero((tMap.getString("nrMatricula")).toString(), 9),
			tMap.getLong("idFilial"),
			null,
			null,
			null,
			true
		);
	}
	
	@SuppressWarnings("rawtypes")
	public List findTabelaPreco(Map criteria) {
		return tabelaPrecoService.findLookup(criteria);
	}

	public void setRegionalService(RegionalService regionalService) {
		this.regionalService = regionalService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setEmitirRelatorioProdutividadeVendasService(
			EmitirRelatorioProdutividadeVendasService emitirRelatorioProdutividadeVendasService) {
		this.emitirRelatorioProdutividadeVendasService = emitirRelatorioProdutividadeVendasService;
	}

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
}