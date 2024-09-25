package com.mercurio.lms.vendas.swt.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DeParaXmlCte;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DeParaXmlCteService;

public class ManterDeParaXmlCteAction {
	private ClienteService clienteService;
	private DeParaXmlCteService deParaXmlCteService;
	private DomainValueService domainValueService;

	public Map<String, Object> findById(java.lang.Long id) {
		Map<String, Object> retorno = new HashMap<String, Object>();
		DeParaXmlCte deParaXmlCte = deParaXmlCteService.findById(id);
		retorno.put("idDeParaXmlCte", deParaXmlCte.getIdDeParaXmlCte());
		retorno.put("tpCampo", deParaXmlCte.getTpCampo().getValue());
		retorno.put("idCliente", deParaXmlCte.getCliente().getIdCliente());
		retorno.put("blMatriz", deParaXmlCte.getBlMatriz());
		retorno.put("nmCliente", deParaXmlCte.getNmCliente());
		retorno.put("nmTnt", deParaXmlCte.getNmTnt());
		retorno.put("nmPessoa", deParaXmlCte.getCliente().getPessoa().getNmPessoa());
		retorno.put("nrIdentificacao", FormatUtils.formatIdentificacao(deParaXmlCte.getCliente().getPessoa().getTpIdentificacao(), deParaXmlCte.getCliente().getPessoa().getNrIdentificacao()));
		return retorno;
	}

	public List<Map<String, Object>> findLookupCliente(Map<String, Object> criteria) {
		List<Map<String, Object>> clientes = clienteService.findClienteByNrIdentificacao((String) criteria.get("nrIdentificacao"));
		if (clientes != null) {
			for (Map<String, Object> cliente : clientes) {
				cliente.remove("tpCliente");
				Map<String, Object> pessoa = (Map<String, Object>) cliente.remove("pessoa");
				if (pessoa != null) {
					cliente.put("nmPessoa", pessoa.get("nmPessoa"));
					cliente.put("nrIdentificacao", pessoa.remove("nrIdentificacaoFormatado"));
				}
			}
		}
		return clientes;
	}

	public ResultSetPage<Map<String, Object>> findPaginated(TypedFlatMap criteria) {
		ResultSetPage<Map<String, Object>> rsp = deParaXmlCteService.findPaginated(criteria);
		List<Map<String, Object>> lista = rsp.getList();
		for (Map<String, Object> item : lista) {
			item.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao((DomainValue) item.get("tpIdentificacao"), (String) item.get("nrIdentificacao")));
		}
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(lista));
		return rsp;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return deParaXmlCteService.getRowCount(criteria);
	}

	public void removeById(Long id) {
		deParaXmlCteService.removeById(id);
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		deParaXmlCteService.removeByIds(ids);
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setDeParaXmlCteService(DeParaXmlCteService deParaXmlCteService) {
		this.deParaXmlCteService = deParaXmlCteService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public Serializable store(TypedFlatMap bean) {
		DeParaXmlCte deParaXmlCte = null;
		boolean repetido=false;
		if (bean.getLong("idDeParaXmlCte") != null) {
			deParaXmlCte = deParaXmlCteService.findById(bean.getLong("idDeParaXmlCte"));
		}
		else {
			repetido=deParaXmlCteService.verificaSeJaExiste(bean.getString("nmTnt"),bean.getLong("idCliente"),bean.getBoolean("blMatriz"));
			deParaXmlCte = new DeParaXmlCte();
		}
		
		if(!repetido){
			Cliente cliente = new Cliente();
			DomainValue domainValue = getDomainValueService().findDomainValueByValue("DM_TIPO_CAMPO_XML_CTE", bean.getString("tpCampo"));
			cliente.setIdCliente(bean.getLong("idCliente"));
			deParaXmlCte.setBlMatriz(bean.getBoolean("blMatriz"));
			deParaXmlCte.setCliente(cliente);
			deParaXmlCte.setNmCliente(bean.getString("nmCliente"));
			deParaXmlCte.setNmTnt(bean.getString("nmTnt"));
			deParaXmlCte.setTpCampo(domainValue);
			return deParaXmlCteService.store(deParaXmlCte);
		}else{
			throw new BusinessException("LMS-00002");
		}
	}
}