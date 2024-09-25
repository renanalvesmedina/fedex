package com.mercurio.lms.edi.model.dao;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.expedicao.model.CCE;

public class CceDAO extends BaseCrudDao<CCE, Long>{

	@SuppressWarnings("rawtypes")
	protected Class getPersistentClass() {
		return CCE.class;
	}

	public Long storeProximoCCE(Usuario usuarioSolicitante) {
		CCE proximoCCE = new CCE();
		
		proximoCCE.setBlDivergencia("S");
		proximoCCE.setDhEmissao(new DateTime(System.currentTimeMillis()));
		proximoCCE.setIdUsuarioInclusao(usuarioSolicitante.getIdUsuario());
		
		store(proximoCCE); 
		
		return proximoCCE.getIdCCE();
	}


}
