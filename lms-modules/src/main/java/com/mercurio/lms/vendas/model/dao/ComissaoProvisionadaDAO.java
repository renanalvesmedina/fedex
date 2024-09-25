package com.mercurio.lms.vendas.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vendas.model.ComissaoProvisionada;

public class ComissaoProvisionadaDAO extends BaseCrudDao<ComissaoProvisionada, Long> {

	@Override
	protected Class getPersistentClass() {
		return ComissaoProvisionada.class;
	}

	public List<ComissaoProvisionada> findDadosForDemonstrativo(Long idExecutivo, Integer mesVigencia, Integer anoVigencia, DomainValue tpModal, Long idFilial) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("cp");
		
		sql.addInnerJoin(getPersistentClass().getName(),"cp");
		
		sql.addLeftOuterJoin("fetch cp.usuario","usuario"); // relativo ao executivo
		sql.addInnerJoin("fetch cp.territorio","territorio");
		sql.addInnerJoin("fetch territorio.filial","filial");
		
		sql.addCriteria("usuario.idUsuario", "=", idExecutivo);
		sql.addCriteria("filial.idFilial", "=", idFilial);
		sql.addCriteria("nrMesCalculo", "=", mesVigencia);
		sql.addCriteria("nrAnoCalculo", "=", anoVigencia);

		List <ComissaoProvisionada> retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());

		return retorno;
	}

}
