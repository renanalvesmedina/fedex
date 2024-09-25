package com.mercurio.lms.franqueados.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.franqueados.model.Franqueado;
import com.mercurio.lms.franqueados.model.FranqueadoFranquia;
import com.mercurio.lms.franqueados.model.dao.FranqueadoFranquiaDAO;

public class FranqueadoFranquiaService extends CrudService<FranqueadoFranquia, Long> {
	
	private FranqueadoFranquiaDAO getFranqueadoFranquiaDAO() {
		return (FranqueadoFranquiaDAO) getDao();
	}
	
	public void setFranqueadoFranquiaDAO(FranqueadoFranquiaDAO franqueadoFranquiaDAO) {
        setDao(franqueadoFranquiaDAO);
    }
	
	@Override
	public Serializable findById(Long id) {
		return getFranqueadoFranquiaDAO().findById(id);
	}
	
	public List<FranqueadoFranquia> findFranqueadoFranquiasVigentes(YearMonthDay date) {
		return getFranqueadoFranquiaDAO().findFranqueadoFranquiasVigentes(date);
	}
	
	public List<FranqueadoFranquia> findFranqueadoFranquiasVigentesByFranqueado(Long franqueadoId, YearMonthDay date){
		return getFranqueadoFranquiaDAO().findFranqueadoFranquiasVigentesByFranqueado(franqueadoId, date);
	}
	
	public List<FranqueadoFranquia> findFranqueadoFranquiasByFranqueado(Long franqueadoId, YearMonthDay date){
		return getFranqueadoFranquiaDAO().findFranqueadoFranquiasByFranqueado(franqueadoId, date);
	}
	
	public void removeByFranqueado(Long id) {
		getFranqueadoFranquiaDAO().removeByFranqueado(id);
	}
	
	@SuppressWarnings("rawtypes")
	public void removeByList(List list) {
		
		List<Long> ids = new ArrayList<Long>(); 
		for (Object object : list) {
			FranqueadoFranquia frq = (FranqueadoFranquia) object;
			ids.add(frq.getIdFranqueadoFranquia());
		}

		super.removeByIds(ids);
	}

	@SuppressWarnings("rawtypes")
	public List findByFranqueado(Long id) {
		return getFranqueadoFranquiaDAO().findByFranqueado(id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void storeAll(Franqueado franqueado, List newers) {
		List toInsert = new ArrayList();
		for (Object object : newers) {
			FranqueadoFranquia franquia = (FranqueadoFranquia) object;
			franquia.setFranqueado(franqueado);
			
			toInsert.add(franquia);
		}
		
		super.storeAll(toInsert);
	}

}
