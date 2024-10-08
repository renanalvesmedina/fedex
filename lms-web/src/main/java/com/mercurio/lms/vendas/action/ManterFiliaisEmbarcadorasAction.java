package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.vendas.model.FilialEmbarcadora;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.FilialEmbarcadoraService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterFiliaisEmbarcadorasAction"
 */
public class ManterFiliaisEmbarcadorasAction extends CrudAction {
	private FilialService filialService;
	private ClienteService clienteService;

    /**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	getFilialEmbarcadoraService().removeByIds(ids);
    }
    public void removeById(Long id) {
    	getFilialEmbarcadoraService().removeById(id);
    }
    public Serializable store(FilialEmbarcadora bean) {
    	return getFilialEmbarcadoraService().store(bean);
    }

    public Serializable findById(java.lang.Long id) {
    	return getFilialEmbarcadoraService().findById(id);
    }

    public Integer getRowCountCustom(TypedFlatMap criteria) {
    	return getFilialEmbarcadoraService().getRowCountCustom(criteria);
    }
    public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
    	ResultSetPage rsp = getFilialEmbarcadoraService().findPaginatedCustom(criteria);
    	List<FilialEmbarcadora> result = rsp.getList();
    	/** Formata nmFilial */
    	for (FilialEmbarcadora filialEmbarcadora : result) {
    		StringBuilder nmFilial = new StringBuilder()
    			.append(filialEmbarcadora.getFilial().getSgFilial())
    			.append(" - ")
    			.append(filialEmbarcadora.getFilial().getPessoa().getNmFantasia());

			filialEmbarcadora.getFilial().getPessoa().setNmFantasia(nmFilial.toString());
		}
    	return rsp;
    }

    public List findLookupCliente(TypedFlatMap criteria){
    	
    	List clienteEspecial = clienteService.findLookupCliente(criteria.getString("pessoa.nrIdentificacao"),"S");
    	if(clienteEspecial.isEmpty()){
    		clienteEspecial = clienteService.findLookupCliente(criteria.getString("pessoa.nrIdentificacao"),"F");
    }
    	return clienteEspecial;
    }

    public List findLookupFilial(TypedFlatMap criteria){
    	return filialService.findLookupBySgFilial(
    		 criteria.getString("sgFilial")
    		,criteria.getString("tpAcesso"));
    }


    public void setFilialEmbarcadoraService(FilialEmbarcadoraService filialEmbarcadoraService) {
		this.defaultService = filialEmbarcadoraService;
	}
    public FilialEmbarcadoraService getFilialEmbarcadoraService() {
		return (FilialEmbarcadoraService)this.defaultService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}
