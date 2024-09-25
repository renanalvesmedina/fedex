package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.vendas.model.CptTipoValor;
import com.mercurio.lms.vendas.model.dao.CptTipoValorDAO;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.cptTipoValorService"
 */
public class CptTipoValorService extends CrudService<CptTipoValor, Long> {
	
	@Override
	public Serializable findById(Long id) {		
		return super.findById(id);
	}
	
	/**
	 * Metodo utilizado pelo combo na tela de complexidade 
	 * @return
	 */
	public List findListTipoValor(){
		return getCptTipoValorDAO().findListTipoValor();
	}
	
	/**
	 * Salva a entidade
	 */
	public Serializable store(CptTipoValor bean) {		
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
		return super.getRowCount(criteria);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {		
		return super.findPaginated(criteria);
	}
	
	
    public void setCptTipoValorDAO(CptTipoValorDAO dao) {
        setDao( dao );
    }
	
	private CptTipoValorDAO getCptTipoValorDAO(){
		return (CptTipoValorDAO) getDao();
	}
	
}