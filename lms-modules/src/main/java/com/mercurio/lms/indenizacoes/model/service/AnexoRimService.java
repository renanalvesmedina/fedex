package com.mercurio.lms.indenizacoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.indenizacoes.model.AnexoRim;
import com.mercurio.lms.indenizacoes.model.dao.AnexoRimDAO;
import com.mercurio.lms.pendencia.model.dao.MdaDAO;

public class AnexoRimService extends CrudService<AnexoRim, Long>{

	public AnexoRim findById(java.lang.Long id) {
		return (AnexoRim)super.findById(id);
	}
	
	public void removeById(java.lang.Long id) {
        super.removeById(id);
    }
	
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }
	
	public java.io.Serializable store(AnexoRim bean) {
        return super.store(bean);
    }
	
	private AnexoRimDAO getAnexoRimDAO() {
        return (AnexoRimDAO) getDao();
    }
	
	public void setAnexoRimDAO(AnexoRimDAO dao) {
        setDao(dao);
    }
	
	public List findItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
		return getAnexoRimDAO().findItensByIdReciboIndenizacao(idReciboIndenizacao);
    }

	public Integer getRowCountItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
		return getAnexoRimDAO().getRowCountItensByIdReciboIndenizacao(idReciboIndenizacao);
	}  
	
}