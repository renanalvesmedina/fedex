package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.ReemissaoEtiquetaVolume;
import com.mercurio.lms.expedicao.model.ReemissaoEtiquetaVolumeDTO;
import com.mercurio.lms.expedicao.model.dao.ReemissaoEtiquetaVolumeDAO;

public class ReemissaoEtiquetaVolumeService extends
		CrudService<ReemissaoEtiquetaVolume, Long> {

	public void setReemissaoEtiquetaVolumeDAO(ReemissaoEtiquetaVolumeDAO reemissaoEtiquetaVolumeDAO) {
		setDao( reemissaoEtiquetaVolumeDAO );
	}

	public SqlTemplate getSqlTemplate(TypedFlatMap criteria){
		return getReemissaoEtiquetaVolumeDAO().getSqlTemplate(criteria);
	}
	
	public ReemissaoEtiquetaVolume findByFilial(Long idFilial) {
		return getReemissaoEtiquetaVolumeDAO().findByFilial(idFilial);
	}

	@Override
	public ReemissaoEtiquetaVolume findById(java.lang.Long id) {
		return (ReemissaoEtiquetaVolume) super.findById(id);
	}

	public List<ReemissaoEtiquetaVolumeDTO> find(TypedFlatMap criteria) {
		return getReemissaoEtiquetaVolumeDAO().find(criteria);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return getReemissaoEtiquetaVolumeDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
	}

	private ReemissaoEtiquetaVolumeDAO getReemissaoEtiquetaVolumeDAO() {
		return (ReemissaoEtiquetaVolumeDAO) getDao();
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getReemissaoEtiquetaVolumeDAO().getRowCount(criteria);
	}

	@Override
	public java.io.Serializable store(ReemissaoEtiquetaVolume reemissaoEtiquetaVolume) {
		return super.store(reemissaoEtiquetaVolume);
	}
}
