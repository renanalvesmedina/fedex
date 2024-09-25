package com.mercurio.lms.sgr.model.service;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sgr.model.FornecEscoltaRankingItem;
import com.mercurio.lms.sgr.model.FornecedorEscoltaRanking;
import com.mercurio.lms.sgr.model.dao.FornecedorEscoltaRankingDAO;

public class FornecedorEscoltaRankingService extends CrudService<FornecedorEscoltaRanking, Long> {

	public Integer getRowCount(TypedFlatMap criteria) {
		return getFornecedorEscoltaRankingDAO().getRowCountByFilter(criteria);
	}

	public List<FornecedorEscoltaRanking> find(TypedFlatMap criteria) {
		return getFornecedorEscoltaRankingDAO().findByFilter(criteria);
	}

	@Override
	public FornecedorEscoltaRanking findById(Long id) {
		return (FornecedorEscoltaRanking) super.findById(id);
	}

	public List<FornecEscoltaRankingItem> findRankItemByIdRanking(Long idfornecedorEscoltaRanking) {
		return getFornecedorEscoltaRankingDAO().findRankItemByIdRanking(idfornecedorEscoltaRanking);
	}
	
	
	protected void afterStore(FornecedorEscoltaRanking fornecedorEscoltaRanking) {
		getFornecedorEscoltaRankingDAO().storeItemRanking(fornecedorEscoltaRanking);
	}

	@Override
	public Long store(FornecedorEscoltaRanking bean) {
		Long serializable = (Long) super.store(bean);
		afterStore(bean);
		return serializable;
	}
	
	
	@Override
	protected FornecedorEscoltaRanking beforeStore(FornecedorEscoltaRanking bean) {
		validateVigenciaRanking(bean);
		return super.beforeStore(bean);
	}
	private void validateVigenciaRanking(FornecedorEscoltaRanking bean) {
		Boolean isVigenciaValida = getFornecedorEscoltaRankingDAO().validateVigenciaRanking(bean);
		if (BooleanUtils.isFalse(isVigenciaValida)) {
			throw new BusinessException("LMS-00047");
		}
	}

	@Override
	public void removeById(Long id) {
		getFornecedorEscoltaRankingDAO().removeItemsByIdFornecEscoltaRanking(id);
		super.removeById(id);
	}

	@Override
	public void removeByIds(List<Long> ids) {
		getFornecedorEscoltaRankingDAO().removeItemsByIdsFornecEscoltaRanking(ids);
		super.removeByIds(ids);
	}

	private FornecedorEscoltaRankingDAO getFornecedorEscoltaRankingDAO() {
		return (FornecedorEscoltaRankingDAO) getDao();
	}

	public void setFornecedorEscoltaRankingDAO(FornecedorEscoltaRankingDAO dao) {
		setDao(dao);
	}
} 
