package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.EtiquetaAfericao;
import com.mercurio.lms.expedicao.model.dao.EtiquetaAfericaoDAO;

public class EtiquetaAfericaoService extends CrudService<EtiquetaAfericao, Long>{
	
	private ParametroGeralService parametroGeralService;

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}
	
	public void setEtiquetaAfericaoDao (EtiquetaAfericaoDAO etiquetaAfericaoDAO) {
		setDao(etiquetaAfericaoDAO);
	}

	private EtiquetaAfericaoDAO getEtiquetaAfericaoDao() {
		return (EtiquetaAfericaoDAO) getDao();
	}

	public EtiquetaAfericao findById(Long id) {
		return (EtiquetaAfericao) super.findById(id);
	}

	public ResultSetPage findPaginatedEtiquetaAfericao(TypedFlatMap criteria){
		return getEtiquetaAfericaoDao().findPaginatedEtiquetaAfericao(criteria, FindDefinition.createFindDefinition(criteria));
	}

	public Integer getRowCountEtiquetaAfericao(TypedFlatMap criteria) {
		return getEtiquetaAfericaoDao().getRowCountEtiquetaAfericao(criteria);
	}
	
	@Override
	public Serializable store(EtiquetaAfericao etiqueta) {
		if (validateCodigoBarras(etiqueta.getNrCodigoBarras())) {
			throw new BusinessException("LMS-04404");
		}
		if(etiqueta.getIdEtiquetaAfericao() == null){
			parametroGeralService.generateValorParametroSequencial("SEQ_ETIQUETA_AFERICAO", false, 1);
		}
		return super.store(etiqueta);
	}

	public boolean validateCodigoBarras(String barcode) {
		return getEtiquetaAfericaoDao().validateCodigoBarras(barcode);
	}

	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	public EtiquetaAfericao findByNrCodBarra(String nrCodBarra) {
		return getEtiquetaAfericaoDao().findByNrCodBarra(nrCodBarra);
	}
	
}
