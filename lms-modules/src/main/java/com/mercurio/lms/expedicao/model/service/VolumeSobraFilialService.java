package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.VolumeSobraFilial;
import com.mercurio.lms.expedicao.model.dao.VolumeSobraFilialDAO;

public class VolumeSobraFilialService extends CrudService<VolumeSobraFilial, Long> {
	
	private VolumeSobraFilialDAO volumeSobraFilialDAO;
	
	public Serializable store(VolumeSobraFilial bean) {
		return super.store(bean);
	}
	
	public void setVolumeSobraFilialDAO(VolumeSobraFilialDAO volumeSobraFilialDAO){
		this.volumeSobraFilialDAO = volumeSobraFilialDAO;
		setDao(volumeSobraFilialDAO);
	}
	
	public ResultSetPage findPaginatedConsultarSobrasDescarga(TypedFlatMap criteria){
		return  volumeSobraFilialDAO.findPaginatedConsultarSobrasDescarga(criteria, FindDefinition.createFindDefinition(criteria));		
	}
	
	public Integer getRowCountConsultarSobrasDescarga(TypedFlatMap criteria){
		return volumeSobraFilialDAO.getRowCountConsultarSobrasDescarga(criteria);
	}
	
	public ResultSetPage findPaginatedConsultarSobrasDescargaDetalhamento(TypedFlatMap criteria){
		return  volumeSobraFilialDAO.findPaginatedConsultarSobrasDescargaDetalhamento(criteria, FindDefinition.createFindDefinition(criteria));		
	}
	
	public Integer getRowCountConsultarSobrasDescargaDetalhamento(TypedFlatMap criteria){
		return volumeSobraFilialDAO.getRowCountConsultarSobrasDescargaDetalhamento(criteria);
	}
	
	
	
}
