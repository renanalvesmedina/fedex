package com.mercurio.lms.expedicao.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.service.PreFaturaServicoService;
import com.mercurio.lms.expedicao.report.GerarPlanilhaServicosAdicionaisGeradosService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class GerarPlanilhaServicosAdicionaisGeradosAction extends CrudAction {
	private ClienteService clienteService;
	private FilialService filialService;
	private GerarPlanilhaServicosAdicionaisGeradosService gerarPlanilhaServicosAdicionaisGeradosService;
	private ParcelaPrecoService parcelaPrecoService;
	private RegionalService regionalService;
	private ReportExecutionManager reportExecutionManager;
	private UsuarioService usuarioService;
	
	public String executeReport(TypedFlatMap criteria) throws Exception {
		return reportExecutionManager.generateReportLocator(gerarPlanilhaServicosAdicionaisGeradosService, criteria);				
	}
	
	public List<Map<String,Object>> findComboRegional(){
		Usuario usuarioSessao = (Usuario) SessionContext.getUser();			
		List<Regional> regionais = regionalService.findRegionaisVigentesByIdUsuario(usuarioSessao.getIdUsuario());		
		List<Map<String,Object>> retorno = new ArrayList<Map<String,Object>>();
		
		for (Regional regional:regionais){
			Map<String,Object> regionalMap = new HashMap<String,Object>();
			regionalMap.put("idRegional", regional.getIdRegional());
			regionalMap.put("dsRegional", regional.getSiglaDescricao());
			retorno.add(regionalMap);
		}
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLookupFilial(Map<String, Object> criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);				
			}
		}
		return result;
	}	
	
	public List<Map<String, Object>> findComboServicosAdicionais(TypedFlatMap criteria) {
		return parcelaPrecoService.findServicosAdicionaisParcela();
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLookupCliente(Map<String,Object> criteria) {
		List<Map<String, Object>> listResult = clienteService.findLookupCliente(MapUtils.getString(criteria, "nrIdentificacao"));
		for (Map<String, Object> cliente : listResult) {
			Map<String, Object> pessoa = (Map<String, Object>)cliente.remove("pessoa");
			cliente.put("nrIdentificacao", MapUtils.getString(pessoa, "nrIdentificacaoFormatado"));
			cliente.put("nmPessoa", MapUtils.getString(pessoa, "nmPessoa"));
		}
		return listResult;
	}
	
	public List<Map<String, Object>> findLookupUsuario(TypedFlatMap criteria) {
		List<Map<String, Object>> usuarios = usuarioService.findLookupUsuarioPromotor(
				null, criteria.getString("login"), null, criteria.getLong("idFilial"), null, null, true);
		return usuarios;			
	}	
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public void setGerarPlanilhaServicosAdicionaisGeradosService(
			GerarPlanilhaServicosAdicionaisGeradosService gerarPlanilhaServicosAdicionaisGeradosService) {
		this.gerarPlanilhaServicosAdicionaisGeradosService = gerarPlanilhaServicosAdicionaisGeradosService;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}
	
	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setRegionalService(RegionalService regionalService) {
		this.regionalService = regionalService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
}
