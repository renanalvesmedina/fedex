package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ProprietarioRPA;

public class ProprietarioRpaDAO extends BaseCrudDao<ProprietarioRPA, Long>{

	@Override
	@SuppressWarnings("rawtypes")
	protected Class getPersistentClass() {
		return ProprietarioRPA.class;
	}

	public Long findProximoNumero(Long idProprietario) {
		SqlTemplate hql = new SqlTemplate();

		StringBuilder hqlFrom = new StringBuilder().append(ProprietarioRPA.class.getName()).append(" rpa ");
		
		hql.addProjection("MAX(rpa.nrRPA)");
		hql.addFrom(hqlFrom.toString());		
		hql.addCriteria("rpa.idProprietario","=", idProprietario);
		
		return (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	public boolean isGerarRPA(Long idReciboFreteCarreteiro) {
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT 1 FROM ");
		sql.append("  RECIBO_FRETE_CARRETEIRO RFC,  ");
		sql.append("  PESSOA PE , ");
		sql.append("  PARAMETRO_GERAL PG ");
		sql.append("   WHERE RFC.ID_RECIBO_FRETE_CARRETEIRO = ?   ");
		sql.append("   AND RFC.ID_PROPRIETARIO = PE.ID_PESSOA         ");
		sql.append("   AND PE.TP_PESSOA = 'F'   ");
		sql.append("   AND PG.NM_PARAMETRO_GERAL ='ATIVA_EMISSAO_RPA'   ");
		sql.append("   AND UPPER(PG.DS_CONTEUDO) ='S' ");
		
		return  getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{idReciboFreteCarreteiro}) > 0;
	}
}
