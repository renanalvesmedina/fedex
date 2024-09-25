package com.mercurio.lms.edi.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.edi.model.ClienteEDIFilialEmbarcadora;
import com.mercurio.lms.edi.model.dao.ClienteEDIFilialEmbarcadoraDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.PessoaUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.clienteEDIFilialEmbarcadoraService"
 */

public class ClienteEDIFilialEmbarcadoraService extends CrudService<ClienteEDIFilialEmbarcadora, Long> {

	@Override
	public ClienteEDIFilialEmbarcadora findById(Long id) {		
		return (ClienteEDIFilialEmbarcadora)super.findById(id);
	}
	
	public List findByNrIdentificacaoByIdFilial(String nrIdentificacao, Long idFilial) {
		return getClienteEDIFilialEmbarcadoraDAO().findByNrIdentificacaoByIdFilial(nrIdentificacao, idFilial);
	}
	
	@Override
	public Serializable store(ClienteEDIFilialEmbarcadora bean) {
		return super.store(bean);
	}
	
	@Override
	public void removeById(Long id) {		
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}
	
	public List findClienteByNrIdentificacao(String nrIndentificacao) {
		nrIndentificacao = PessoaUtils.validateIdentificacao(nrIndentificacao);
		if(StringUtils.isBlank(nrIndentificacao)) {
			throw new BusinessException("LMS-00049");
		}
		List<Map> clientes = getClienteEDIFilialEmbarcadoraDAO().findLookupClienteEmbarc(nrIndentificacao);
		for(Map cliente : clientes) {
			Map pessoa = (Map) cliente.get("pessoa");
			String nrIdentificacao = MapUtils.getString(pessoa, "nrIdentificacao");
			String tpIdentificacao = MapUtilsPlus.getStringOnMap(pessoa, "tpIdentificacao", "value", null);
			if(StringUtils.isNotBlank(nrIdentificacao) && StringUtils.isNotBlank(tpIdentificacao)){
				pessoa.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao));
			}
		}
		return clientes;
	}
    /**
     * Obtem todas as filiais embarcadoras relacionados a Cliente EDI
     * 
     * @param  id
     * @return List
     */
    public List<ClienteEDIFilialEmbarcadora> findFiliaisEmbarcadoraById(Long id){
    	return getClienteEDIFilialEmbarcadoraDAO().findFiliaisEmbarcadoraById(id);
    }    	
    
    public List<ClienteEDIFilialEmbarcadora> findLookupEmbarcadora(Long idCliente , String identificacao){
    	return getClienteEDIFilialEmbarcadoraDAO().findLookupEmbarcadora(idCliente,identificacao);
    }
    
    /**
     * Paginação da grid
     * 
     * @param paginatedQuery
     * @return
     */
	public ResultSetPage<ClienteEDIFilialEmbarcadora> findPaginated(PaginatedQuery paginatedQuery) {
		return getClienteEDIFilialEmbarcadoraDAO().findPaginated(paginatedQuery);
	}   
	
	public ResultSetPage<TypedFlatMap> findPaginatedLookup(TypedFlatMap criteria) {	
		String nrIdentificacao = criteria.getString("pessoa.nrIdentificacao");
		if(StringUtils.isNotBlank(nrIdentificacao)) {
			criteria.put("pessoa.nrIdentificacao", PessoaUtils.validateIdentificacao(nrIdentificacao));
		}

		ResultSetPage<TypedFlatMap> rsp = getClienteEDIFilialEmbarcadoraDAO().findPaginatedLookup(criteria, FindDefinition.createFindDefinition(criteria));
		List<TypedFlatMap> list = rsp.getList();

		String nrIdentificacaoFormatado = "";
		for(TypedFlatMap cliente : list) {
			nrIdentificacao = cliente.getString("pessoa.nrIdentificacao");
			cliente.put("pessoa.tpIdentificacao", cliente.getVarcharI18n("pessoa.tpIdentificacao.description"));
			if(!StringUtils.isBlank(nrIdentificacao)) {
				nrIdentificacaoFormatado = FormatUtils.formatIdentificacao(cliente.getString("pessoa.tpIdentificacao.value"), nrIdentificacao);
			} else {
				nrIdentificacaoFormatado = "";
			}
			cliente.put("pessoa.nrIdentificacaoFormatado", nrIdentificacaoFormatado);
		}
		return rsp;
	}
	public Integer getRowCountLookup(TypedFlatMap criteria) {
		return getClienteEDIFilialEmbarcadoraDAO().getRowCountLookup(criteria, FindDefinition.createFindDefinition(criteria));
		
	}
	
	/**
	 * Obtem dados da Embarcadora
	 * @param idClienteEDIFilialEmbarcadora
	 * @return
	 */
	public ClienteEDIFilialEmbarcadora findDadosEmbarcadora(Long idClienteEDIFilialEmbarcadora) {
		return getClienteEDIFilialEmbarcadoraDAO().findDadosEmbarcadora(idClienteEDIFilialEmbarcadora);
	}
	
    public ClienteEDIFilialEmbarcadoraDAO getClienteEDIFilialEmbarcadoraDAO() {
        return (ClienteEDIFilialEmbarcadoraDAO) getDao();
    }
    
    public void setClienteEDIFilialEmbarcadoraDAO(ClienteEDIFilialEmbarcadoraDAO dao) {
        setDao(dao);
    }
}
