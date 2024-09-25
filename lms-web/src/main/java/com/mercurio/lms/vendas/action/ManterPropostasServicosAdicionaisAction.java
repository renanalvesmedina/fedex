package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoParcelaService;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.vendas.manterPropostasServicosAdicionaisAction"
 */
public class ManterPropostasServicosAdicionaisAction extends CrudAction {
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		return getServicoAdicionalClienteService().findPaginatedByProposta(criteria);
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getServicoAdicionalClienteService().getRowCountByProposta(criteria);
	}

	public void removeById(java.lang.Long id) {
		getServicoAdicionalClienteService().removeByIdProposta(id);
	}
	
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getServicoAdicionalClienteService().removeByIdsPropostas(ids);
	}

	public ServicoAdicionalCliente findById(java.lang.Long id) {
		return getServicoAdicionalClienteService().findById(id);
	}

	public List findTabelaPrecoParcelaCombo(Map criteria) {
		return getTabelaPrecoParcelaService().findMappedList(criteria, "S".equals(MapUtils.getString(criteria, "tpSituacao")));
	}

    public Serializable store(ServicoAdicionalCliente bean) {
    	return getServicoAdicionalClienteService().store(bean);
    }
    
    @SuppressWarnings("rawtypes")
	public boolean isTaxaPermanenciaCargaOrTaxaFielDepositario(Map criteria){
		return getTabelaPrecoParcelaService().findTaxaPermanenciaCargaOrTaxaFielDepositario(criteria);
	}

    public void setService(ServicoAdicionalClienteService service) {
		this.defaultService = service;
	}
    public ServicoAdicionalClienteService getServicoAdicionalClienteService() {
    	return (ServicoAdicionalClienteService)defaultService;
    }
	public void setTabelaPrecoParcelaService(TabelaPrecoParcelaService tabelaPrecoParcelaService) {
		this.tabelaPrecoParcelaService = tabelaPrecoParcelaService;
	}
	public TabelaPrecoParcelaService getTabelaPrecoParcelaService(){
		return tabelaPrecoParcelaService;
	}
	
}