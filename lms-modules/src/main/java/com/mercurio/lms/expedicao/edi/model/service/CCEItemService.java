package com.mercurio.lms.expedicao.edi.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.edi.model.dao.CCEItemDAO;
import com.mercurio.lms.expedicao.model.CCEItem;

public class CCEItemService extends CrudService<CCEItem, Long> {
	public Serializable store(CCEItem CCEItem){
		return super.store(CCEItem);
	}
	
	public void setCCEItemDAO(CCEItemDAO dao) {
		setDao(dao);
	}
	
	public List<CCEItem> findByCCE(String nrCce) {
		return ((CCEItemDAO) getDao()).findByCCE(nrCce);
	}

	public List<CCEItem> findByChavesNfe(List<String> chavesNotasFiscaisEncontradas) {
		return ((CCEItemDAO) getDao()).findByChavesNfe(chavesNotasFiscaisEncontradas);
	}
	
	public CCEItem findByChaveNfe(String nrChave) {
		return ((CCEItemDAO) getDao()).findByChaveNfe(nrChave);
	}
	
	public void storeCEEItem(CCEItem cCEItem) {
		((CCEItemDAO) getDao()).store(cCEItem, true);
	}
	
	public List<CCEItem> findChaveNfe(String nrChave){
		CCEItem item = ((CCEItemDAO) getDao()).findByChaveNfe(nrChave);
		if(item == null ) {
			return null;
		}
		return ((CCEItemDAO) getDao()).findByCCE(item.getCce().getIdCCE().toString());
	}
	
	public boolean findExistsNotasPaletizadas(List<String> chavesNotasFiscais) {
		return ((CCEItemDAO) getDao()).findExistsNotasPaletizadas(chavesNotasFiscais);
	}
}
