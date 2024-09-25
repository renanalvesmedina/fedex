package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.CodigoMunicipioFilial;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;

public class CodigoMunicipioFilialDAO extends BaseCrudDao<CodigoMunicipioFilial, Long> {

	protected final Class<CodigoMunicipioFilial> getPersistentClass() {
		return CodigoMunicipioFilial.class;
	}

    public CodigoMunicipioFilial findByFilialMunicipio(Filial filial, Municipio municipio) {
        
        StringBuilder sql = new StringBuilder();
        sql.append(" select cmf ");
        sql.append(" from CodigoMunicipioFilial cmf ");
        sql.append(" where cmf.filial.idFilial = ? ");
        sql.append("   and cmf.municipio.idMunicipio = ? ");
        
        List<CodigoMunicipioFilial> list = getAdsmHibernateTemplate().find(sql.toString(), new Object[] {filial.getIdFilial(), municipio.getIdMunicipio()});
        
        if (list != null && ! list.isEmpty() && list.size() == 1) {
        	return list.get(0);
        }
        
        return null;
        
    }
    
}