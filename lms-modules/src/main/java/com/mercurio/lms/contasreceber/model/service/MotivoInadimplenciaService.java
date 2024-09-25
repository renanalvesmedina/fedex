package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.MotivoInadimplencia;
import com.mercurio.lms.contasreceber.model.dao.MotivoInadimplenciaDAO;

public class MotivoInadimplenciaService extends CrudService<MotivoInadimplencia, Long> {

	private MotivoInadimplenciaDAO motivoInadimplenciaDAO;
	
	public Serializable store(MotivoInadimplencia entity) {
		return super.store(entity);
	}
	
	public List<MotivoInadimplencia> findAll(TypedFlatMap filtro) {
		return motivoInadimplenciaDAO.findAll(filtro);
	}
	
	public MotivoInadimplencia findMotivoInadimplenciaById(Long id) {
		return motivoInadimplenciaDAO.findMotivoInadimplenciaById(id);
	}
	
	public List<MotivoInadimplencia> findMotivoInadimplenciaByTratativaId(Long idTratativa) {
		return motivoInadimplenciaDAO.findMotivoInadimplenciaByTratativaId(idTratativa);
	}
	
	public void setMotivoInadimplenciaDAO(MotivoInadimplenciaDAO motivoInadimplenciaDAO) {
		this.motivoInadimplenciaDAO = motivoInadimplenciaDAO;
		setDao( motivoInadimplenciaDAO );
	}

}