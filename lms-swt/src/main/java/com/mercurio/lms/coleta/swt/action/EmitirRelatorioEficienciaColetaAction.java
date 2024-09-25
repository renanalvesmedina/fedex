package com.mercurio.lms.coleta.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.lms.coleta.report.EmitirRelatorioEficienciaColetaDetalhadoService;
import com.mercurio.lms.coleta.report.EmitirRelatorioEficienciaColetaResumidoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RegionalService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * @spring.bean id="lms.coleta.swt.emitirRelatorioEficienciaColetaAction"
 */
public class EmitirRelatorioEficienciaColetaAction {

	private ReportExecutionManager reportExecutionManager;
	private EmitirRelatorioEficienciaColetaDetalhadoService emitirRelatorioEficienciaColetaDetalhadoService;
	private EmitirRelatorioEficienciaColetaResumidoService emitirRelatorioEficienciaColetaResumidoService;
	private RegionalService regionalService;
	private FilialService filialService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private ClienteService clienteService;

	@SuppressWarnings("rawtypes") 
	public String executeReportDetalhado(Map parameters) throws Exception {
		return reportExecutionManager.generateReportLocator(emitirRelatorioEficienciaColetaDetalhadoService, parameters);
	}

	@SuppressWarnings("rawtypes") 
	public String executeReportResumido(Map parameters) throws Exception {
		return reportExecutionManager.generateReportLocator(emitirRelatorioEficienciaColetaResumidoService, parameters);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    public List findLookupRotaColetaEntrega(Map criteria){
    	List rotas = rotaColetaEntregaService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = rotas.iterator(); iter.hasNext();) {
			RotaColetaEntrega rotaColetaEntrega = (RotaColetaEntrega) iter.next();
			Map map = new HashMap();
			map.put("idRotaColetaEntrega", rotaColetaEntrega.getIdRotaColetaEntrega());
			map.put("nrRota", rotaColetaEntrega.getNrRota());
			map.put("dsRota", rotaColetaEntrega.getDsRota());
			retorno.add(map);
		}
    	return retorno;
    }

	public List<Map<String, String>> findComboRegional() {
		List<Map<String, Object>> regionais = regionalService.findRegionaisVigentes(); 
		
		List<Map<String, String>> retorno = new ArrayList<Map<String, String>>();

		for (Map<String, Object> map : filterRegionaisBrasileiras(regionais)) {
			Map<String, String> regionalMap = new HashMap<String, String>();
			regionalMap.put("idRegional", map.get("idRegional").toString());
			regionalMap.put("dsRegional", (String) map.get("siglaDescricao"));
			retorno.add(regionalMap);
		}
		return retorno;
	}

	private List<Map<String, Object>> filterRegionaisBrasileiras(List<Map<String, Object>> regionais) {
		List<String> regionaisBrasileiras = new ArrayList<String>();
		regionaisBrasileiras.add("SU1");
		regionaisBrasileiras.add("SUL");
		regionaisBrasileiras.add("NTE");
		regionaisBrasileiras.add("NDE");
		regionaisBrasileiras.add("COE");
		regionaisBrasileiras.add("SD1");
		regionaisBrasileiras.add("SD2");
		
		List<Map<String, Object>> regionaisFiltered = new ArrayList<Map<String,Object>>();
		
		for (Map<String,Object> regional : regionais) {
			String key = (String) regional.get("sgRegional");
			if (regionaisBrasileiras.contains(key)) {
				regionaisFiltered.add(regional);
			}
		}
		return regionaisFiltered;
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
    public List findLookupCliente(Map criteria){
		Map mapPessoa = new HashMap();
		mapPessoa.put("nrIdentificacao", criteria.get("nrIdentificacao"));
		criteria.put("pessoa", mapPessoa);
		criteria.remove("nrIdentificacao");

		List clientes = clienteService.findLookup(criteria);
		List retorno = new ArrayList();
		if (clientes != null) {
			for (Iterator iter = clientes.iterator(); iter.hasNext();) {
				Map map = new HashMap();
				Cliente cliente = (Cliente) iter.next();
				map.put("idCliente", cliente.getIdCliente());
				map.put("tpSituacao", cliente.getTpSituacao());
				map.put("nmPessoa", cliente.getPessoa().getNmPessoa());
				map.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
				map.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(cliente.getPessoa()));
				map.put("tpIdentificacao", cliente.getPessoa().getTpIdentificacao());
				map.put("tpCliente", cliente.getTpCliente());
				retorno.add(map);
			}
		}
		return retorno;
    }

	public void setEmitirRelatorioEficienciaColetaDetalhadoService(EmitirRelatorioEficienciaColetaDetalhadoService emitirRelatorioEficienciaColetaDetalhadoService) {
		this.emitirRelatorioEficienciaColetaDetalhadoService = emitirRelatorioEficienciaColetaDetalhadoService;
	}

	public void setEmitirRelatorioEficienciaColetaResumidoService(EmitirRelatorioEficienciaColetaResumidoService emitirRelatorioEficienciaColetaResumidoService) {
		this.emitirRelatorioEficienciaColetaResumidoService = emitirRelatorioEficienciaColetaResumidoService;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setRegionalService(RegionalService regionalService) {
		this.regionalService = regionalService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

}