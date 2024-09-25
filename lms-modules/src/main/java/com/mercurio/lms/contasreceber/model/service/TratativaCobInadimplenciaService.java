package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.contasreceber.model.TratativaCobInadimplencia;
import com.mercurio.lms.contasreceber.model.dao.TratativaCobInadimplenciaDAO;

public class TratativaCobInadimplenciaService extends CrudService<TratativaCobInadimplencia, Long> {

	private TratativaCobInadimplenciaDAO tratativaCobInadimplenciaDAO;
	
	public Object[] findTratativaByIdFatura(Long idFatura) {
		return tratativaCobInadimplenciaDAO.findTratativaByIdFatura(idFatura);
	}
	
	public List findTratativasByFatura(Long idFatura){
		return tratativaCobInadimplenciaDAO.findTratativasByFatura(idFatura);
	}
	public TratativaCobInadimplencia findTratativaById(Long idCobrancaInadimplencia) {
		return tratativaCobInadimplenciaDAO.findTratativaById(idCobrancaInadimplencia);
	}
	
	
	public void setTratativaCobInadimplenciaDAO(TratativaCobInadimplenciaDAO tratativaCobInadimplenciaDAO) {
		this.tratativaCobInadimplenciaDAO = tratativaCobInadimplenciaDAO;
		setDao( tratativaCobInadimplenciaDAO );
	}
	
	public Serializable store(TratativaCobInadimplencia entity) {
		return super.store(entity);
	}
	
}