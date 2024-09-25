package com.mercurio.lms.edi.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.edi.model.ConhecimentoVolumeFedex;

/**
 * @author ernani.brandao
 * LMSA-6520: LMSA-6534
 */
public class ConhecimentoVolumeFedexDAO extends BaseCrudDao<ConhecimentoVolumeFedex, Long> {

    @SuppressWarnings("rawtypes")
    @Override
    protected Class getPersistentClass() {
        return ConhecimentoVolumeFedex.class;
    }
    
	public Long findSequence(){
		return Long.valueOf(getSession().createSQLQuery("select VOLUME_CONHECIMENTO_FEDEX_SQ.nextval from dual").uniqueResult().toString());
	}	

}
