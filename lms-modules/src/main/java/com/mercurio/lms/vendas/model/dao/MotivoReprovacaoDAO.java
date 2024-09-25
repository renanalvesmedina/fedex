package com.mercurio.lms.vendas.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.MotivoReprovacao;

public class MotivoReprovacaoDAO extends BaseCrudDao<MotivoReprovacao, Long> {

    @SuppressWarnings("rawtypes")
	protected final Class getPersistentClass() {
        return MotivoReprovacao.class;
    }

	public List<MotivoReprovacao> findMotivosReprovacao() {
		return findAllEntities();
	}
	
	@Override
	public MotivoReprovacao findById(Long id) {
		return super.findById(id);
	}


}