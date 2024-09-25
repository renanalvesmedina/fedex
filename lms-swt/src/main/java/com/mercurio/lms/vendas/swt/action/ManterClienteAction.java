package com.mercurio.lms.vendas.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.ManterClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.swt.manterClienteAction"
 */

public class ManterClienteAction extends CrudAction {
	
	private ManterClienteService manterClienteService;
	private ClienteService clienteService;

	public ResultSetPage findPaginated(Map criteria) {
		TypedFlatMap tfmCriteria = createFindCriteria(criteria);
		
		ResultSetPage resultSetPage = manterClienteService.findPaginated(tfmCriteria);
		List listReturn = new ArrayList();
		
		for (Iterator iter = resultSetPage.getList().iterator(); iter.hasNext();) {
			TypedFlatMap map = (TypedFlatMap) iter.next();
			
			Map mapReturn = new HashMap();
			mapReturn.put("tpIdentificacao", map.get("pessoa.tpIdentificacao.description"));
			mapReturn.put("nrIdentificacaoFormatado", map.get("pessoa.nrIdentificacaoFormatado"));
			mapReturn.put("nmPessoa", map.get("pessoa.nmPessoa"));
			mapReturn.put("nmFantasia", map.get("pessoa.nmFantasia"));
			mapReturn.put("enderecoFormatado", formatEndereco(map));
			
			mapReturn.put("nrConta", map.get("nrConta"));
			mapReturn.put("tpCliente", map.get("tpCliente.description"));
			mapReturn.put("tpSituacao", map.get("tpSituacao.description"));
			mapReturn.put("tpClienteValor", map.get("tpCliente.value"));
			mapReturn.put("idCliente", map.get("idCliente"));
			
			listReturn.add(mapReturn);
		}
		resultSetPage.setList(listReturn);
		return resultSetPage;
	}

	public Integer getRowCount(Map criteria) {
		TypedFlatMap tfmCriteria = createFindCriteria(criteria);
		return manterClienteService.getRowCount(tfmCriteria);		
	}
	
	/*
	 * Metodos privados
	 */
	
	private TypedFlatMap createFindCriteria(Map criteria) {		
		TypedFlatMap tfmCriteria = new TypedFlatMap();
		tfmCriteria.put("pessoa.tpPessoa", criteria.get("tpPessoa"));
		tfmCriteria.put("pessoa.tpIdentificacao", criteria.get("tpIdentificacao"));
		tfmCriteria.put("pessoa.nrIdentificacao", criteria.get("nrIdentificacao"));
		tfmCriteria.put("pessoa.nmPessoa", criteria.get("nmPessoa"));
		tfmCriteria.put("nmFantasia", criteria.get("nmFantasia"));		
		tfmCriteria.put("tpCliente", criteria.get("tpCliente"));
		tfmCriteria.put("tpSituacao", criteria.get("tpSituacao"));
		tfmCriteria.put("nrConta", criteria.get("nrConta"));
    	tfmCriteria.put("_currentPage", criteria.get("_currentPage"));
    	tfmCriteria.put("_pageSize", criteria.get("_pageSize"));
    	tfmCriteria.put("_order", criteria.get("_order"));
    	return tfmCriteria;
	}
	
	private String formatEndereco(TypedFlatMap pessoa) {
		String dsEndereco = pessoa.getString("enderecoPessoa.dsEndereco");
		String nrEndereco = pessoa.getString("enderecoPessoa.nrEndereco");
		String dsComplemento = pessoa.getString("enderecoPessoa.dsComplemento");
		VarcharI18n dsTipoLogradouroI18n = pessoa.getVarcharI18n("enderecoPessoa.tipoLogradouro.dsTipoLogradouro");
		String dsTipoLogradouro = null;
		if (dsTipoLogradouroI18n != null) {
			dsTipoLogradouro = dsTipoLogradouroI18n.getValue();
		}
		String nmMunicipio = pessoa.getString("enderecoPessoa.municipio.nmMunicipio");
		String sgUnidadeFederativa = pessoa.getString("enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa");
		
		if (StringUtils.isNotBlank(dsTipoLogradouro) &&
				StringUtils.isNotBlank(dsEndereco) &&
				StringUtils.isNotBlank(nrEndereco)) {
			return FormatUtils.formatEnderecoPessoa(
					dsTipoLogradouro, dsEndereco, nrEndereco,
					dsComplemento, null, nmMunicipio, sgUnidadeFederativa);
		}
		return "";
	}
	
	 public List<Map<String, Object>> findLookupCliente(Map criteria) {
			List<Map<String, Object>> listResult =  clienteService.findLookupCliente(MapUtils.getString(criteria, "nrIdentificacao"));
			for (Map<String, Object> cliente : listResult) {
				Map<String, Object> pessoa = (Map)cliente.remove("pessoa");
				cliente.put("nrIdentificacao", MapUtils.getString(pessoa, "nrIdentificacaoFormatado"));				
				cliente.put("nmPessoa", MapUtils.getString(pessoa, "nmPessoa"));	
			}
			return listResult;
		}
	
	
	/*
	 * GETTERS E SETTERS
	 */
	public void setClienteService(ClienteService clienteService) {
		super.defaultService = clienteService;
		this.clienteService = clienteService;
	}

	public void setManterClienteService(ManterClienteService manterClienteService) {
		this.manterClienteService = manterClienteService;
	}
}
