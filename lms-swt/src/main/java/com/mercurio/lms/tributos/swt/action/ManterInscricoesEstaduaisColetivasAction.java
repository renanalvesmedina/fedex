package com.mercurio.lms.tributos.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.InscricaoEstadualColetiva;
import com.mercurio.lms.tributos.model.service.InscricaoEstadualColetivaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class ManterInscricoesEstaduaisColetivasAction {

	private InscricaoEstadualColetivaService inscricaoEstadualColetivaService;
	private ClienteService clienteService;
	private UnidadeFederativaService unidadeFederativaService;
	
	
	public void setInscricaoEstadualColetivaService(InscricaoEstadualColetivaService inscricaoEstadualColetivaService) {
		this.inscricaoEstadualColetivaService = inscricaoEstadualColetivaService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	
	public List<Map<String, Object>> findLookupCliente(TypedFlatMap criteria) {
		Map<String, Object> pessoa = new HashMap<String, Object>();
		pessoa.put("nrIdentificacao", criteria.remove("nrIdentificacao"));
		pessoa.put("nmFantasia", criteria.remove("nmFantasia"));
		criteria.put("pessoa", pessoa);
	    @SuppressWarnings("unchecked")
		List<Cliente> clientes = clienteService.findLookup(criteria);
		if (clientes != null && !clientes.isEmpty()) {
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			for(Cliente cliente : clientes) {
				Map<String, Object> mapCliente = new HashMap<String, Object>();
				mapCliente.put("idCliente",cliente.getIdCliente());
				mapCliente.put("nmPessoa", cliente.getPessoa().getNmPessoa());
				// já vem formatado do clienteService.
				mapCliente.put("nrIdentificacao",FormatUtils.formatIdentificacao(cliente.getPessoa()));
				mapCliente.put("nmFantasia", cliente.getPessoa().getNmFantasia());
				mapCliente.put("nrConta", cliente.getNrConta());
				result.add(mapCliente);
			}
			return result;
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findUnidadeFederativa(TypedFlatMap criteria) {
		criteria.put("idPais", SessionUtils.getPaisSessao().getIdPais());
		List result = unidadeFederativaService.findComboAtivo(criteria);
		return result;
	}

	public void store(TypedFlatMap data) {
		InscricaoEstadualColetiva bean = new InscricaoEstadualColetiva().fillByMap(data);
		inscricaoEstadualColetivaService.store(bean);
	}
	
	public Map<String, Object> findById(Long id) {  
		Map<String, Object> result = new HashMap<String, Object>();
		InscricaoEstadualColetiva bean = inscricaoEstadualColetivaService.findById(id);
		
		result.put("idInscricaoEstadualColetiva", bean.getIdInscricaoEstadualColetiva());
		result.put("idCliente", bean.getCliente().getIdCliente());
		result.put("nrIdentificacao", FormatUtils.formatIdentificacao(bean.getCliente().getPessoa()));
		result.put("nmPessoa", bean.getCliente().getPessoa().getNmPessoa());
		result.put("idUnidadeFederativa", bean.getUnidadeFederativa().getIdUnidadeFederativa());
		result.put("nrInscricaoEstadual", bean.getNrInscricaoEstadualColetiva());
		result.put("periodoInicial", bean.getDtVigenciaInicial());
		result.put("periodoFinal", bean.getDtVigenciaFinal());
		
		return result; 
	}
	
	public void removeById(Long id) {
		inscricaoEstadualColetivaService.removeById(id);
	}
	
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		inscricaoEstadualColetivaService.removeByIds(ids);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		validaDatasInscricaoEstadualColetiva(criteria);
		ResultSetPage rsp = inscricaoEstadualColetivaService.findPaginated(new PaginatedQuery(criteria));	
		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			Map map = (Map)iter.next();
			map.put("nrIdentificacao", ((DomainValue)map.get("tpIdentificacao")).getValue()+"  "+ FormatUtils.formatCNPJ((String)map.get("nrIdentificacao")));
		}
		
		
		return rsp;		
	}	
	
	public int findRowCount(TypedFlatMap criteria){
		return inscricaoEstadualColetivaService.getRowCount(criteria) ;//inscricaoEstadualColetivaService.find
	}
	
	private void validaDatasInscricaoEstadualColetiva(TypedFlatMap criteria) {
		YearMonthDay dtVigenciaInicial = criteria.getYearMonthDay("periodoInicial");
		YearMonthDay dtVigenciaFinal = criteria.getYearMonthDay("periodoFinal");
		
		if (dtVigenciaInicial != null && dtVigenciaFinal != null 
				&& dtVigenciaFinal.isBefore(dtVigenciaInicial)) {
			throw new BusinessException("LMS-00008");
		}
	}
	
}
