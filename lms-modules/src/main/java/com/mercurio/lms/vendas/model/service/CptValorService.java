package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.vendas.model.CptValor;
import com.mercurio.lms.vendas.model.dao.CptValorDAO;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.cptValorService"
 */
public class CptValorService extends CrudService<CptValor, Long> {
	
	@Override
	public Serializable findById(Long id) {		
		return super.findById(id);
	}
		
	/**
	 * Salva a entidade
	 */
	public Serializable store(CptValor bean) {		
		return super.store(bean);
	}
	
	/**
	 * Remove uma entidade
	 */
	@Override
	public void removeById(Long id) {		
		super.removeById(id);
	}
	
	/**
	 * Remove uma lista de entidade
	 */
	@Override
	public void removeByIds(List<Long> ids) {		
		super.removeByIds(ids);
	}
	
	@Override
	public Integer getRowCount(Map criteria) {		
		return getCptValorDAO().getRowCount(criteria);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getCptValorDAO().findPaginated(criteria,def);
	}
	
	/**
	 * Verifica se o cliente já posssue este valor cadastrado na tabela
	 * CPT_VALOR
	 * 
	 * @param idCliente
	 * @param valor
	 * @return Boolean
	 */
	public Boolean findValorCliente(Long idCliente, BigDecimal valor) {
		List list = getCptValorDAO().findValorCliente(idCliente,valor);		
		return !list.isEmpty();
	}	
	
    public void setCptValorDAO(CptValorDAO dao) {
        setDao( dao );
    }
	
	private CptValorDAO getCptValorDAO(){
		return (CptValorDAO) getDao();
	}

}