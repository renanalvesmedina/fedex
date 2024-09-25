package com.mercurio.lms.sim.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sim.model.ContatoCCT;
import com.mercurio.lms.sim.model.EmailCCT;
import com.mercurio.lms.sim.model.dao.ContatosCCTDAO;

public class ContatosCCTService extends CrudService<ContatoCCT, Long>{
	
	public void setContatosCCTDAO(ContatosCCTDAO dao) {
        setDao(dao);
    }
	
	private ContatosCCTDAO getContatosCCTDAO() {
        return (ContatosCCTDAO) getDao();
    }
	
	@Override
    public java.io.Serializable store(ContatoCCT contatoCCT) {
		return super.store(contatoCCT);
    }
	
	public void removeById(java.lang.Long id) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		getContatosCCTDAO().removeByIdContatoCCT(ids);
        super.removeById(id);
    }
    
    public List<EmailCCT> findEmailsByIdContatoCCT(Long idContatoCCT){
    	return getContatosCCTDAO().findEmailsByIdContatoCCT(idContatoCCT);
    }
    
	public List<String> findEmailsByClientesTpParametrizacao(Long idClienteRemetente, Long idClienteDestinatario, String tpParametrizacao) {
		return getContatosCCTDAO().findEmailsByClientesTpParametrizacao(idClienteRemetente, idClienteDestinatario, tpParametrizacao);
	}
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public ResultSetPage<ContatoCCT> findPaginated(TypedFlatMap criteria, FindDefinition findDefinition) {
		return getContatosCCTDAO().findPaginated(criteria, findDefinition);
	}
    
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
		getContatosCCTDAO().removeByIdContatoCCT(ids);
        super.removeByIds(ids);
    }
	
	public Integer getRowCount(TypedFlatMap criteria) {		
		return getContatosCCTDAO().getRowCount(criteria);
	}
	
	@Override
	public Serializable findById(Long id) {
		return super.findById(id);
	}

	public void removeByIdContatoCCT(Long id) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		getContatosCCTDAO().removeByIdContatoCCT(ids);
	}

	public void storeEmail(EmailCCT emailCCT) {
		getContatosCCTDAO().storeEmails(emailCCT);
	}
    
}
