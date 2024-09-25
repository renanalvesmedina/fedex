package com.mercurio.lms.fretecarreteiroviagem.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.fretecarreteiroviagem.model.AnexoReciboFc;

public class AnexoReciboFcDAO extends BaseCrudDao<AnexoReciboFc, Long>{

	@Override
	@SuppressWarnings("rawtypes")
	protected Class getPersistentClass() {
		return AnexoReciboFc.class;
	}
			
	@SuppressWarnings("rawtypes")
	public List findItensByIdReciboFreteCarreteiro(Long idReciboFreteCarreteiro) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new Map(");
		hql.append(" anexo.idAnexoReciboFc AS idAnexoReciboFc,");
		hql.append(" anexo.nmArquivo AS nmArquivo,");
		hql.append(" anexo.descAnexo AS descAnexo,");
		hql.append(" anexo.dhCriacao AS dhCriacao,");
		hql.append(" usuario.usuarioADSM.nmUsuario as nmUsuario)");
		hql.append(" FROM AnexoReciboFc AS anexo");
		hql.append("  INNER JOIN anexo.reciboFreteCarreteiro recibo");
		hql.append("  INNER JOIN anexo.usuarioLMS usuario");
		hql.append(" WHERE recibo.idReciboFreteCarreteiro = ?");
		hql.append(" ORDER BY anexo.dhCriacao.value DESC ");
				
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idReciboFreteCarreteiro});
	}

	public Integer getRowCountItensByIdReciboFreteCarreteiro(Long idReciboFreteCarreteiro) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 1 FROM anexo_recibo_fc WHERE id_recibo_frete_carreteiro = ?");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{idReciboFreteCarreteiro});
	}
}