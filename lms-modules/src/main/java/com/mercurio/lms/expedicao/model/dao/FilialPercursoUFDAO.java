package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.FilialPercursoUF;

public class FilialPercursoUFDAO extends BaseCrudDao<FilialPercursoUF, Long> {

    protected final Class<FilialPercursoUF> getPersistentClass() {
        return FilialPercursoUF.class;
    }
    
    @SuppressWarnings("unchecked")
	public List<FilialPercursoUF> findFiliaisPercursoByIdFilialOrigemAndIdFilialDestino(Long idFilialOrigem, Long idFilialDestino) {
    	
   		StringBuilder sql = new StringBuilder()
		.append("select filialPercursoUF from ")
		.append(FilialPercursoUF.class.getName()).append(" as filialPercursoUF ")
		.append("where ")
		.append("filialPercursoUF.filialOrigem.idFilial = ? ")
		.append("and filialPercursoUF.filialDestino.idFilial = ? ")
   		.append("order by filialPercursoUF.nrOrdem asc ");

   		List<Long> param = new ArrayList<Long>();
		param.add(idFilialOrigem);
		param.add(idFilialDestino);
		
   		return (List<FilialPercursoUF>)getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}

}