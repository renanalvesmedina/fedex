package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.AwbOcorrenciaLocalizacao;

public class AwbOcorrenciaDAO extends BaseCrudDao<AwbOcorrenciaLocalizacao, Long>{

	@Override
	protected Class getPersistentClass() {
		return AwbOcorrenciaLocalizacao.class;
	}
	
	public List<AwbOcorrenciaLocalizacao> findAwbOcorrenciaLocalizacaoByIdAwbAndTpLocalizacao(Long idAwb, String tpLocalizacao){
		StringBuilder sql = new StringBuilder()
    	.append("select ao ")
    	.append(" from ")
    	.append(AwbOcorrenciaLocalizacao.class.getName()).append(" as ao ")
    	.append(" inner join ao.awb awb ")
    	.append(" where ")
    	.append(" awb.idAwb = ? ")
    	.append(" and ao.tpLocalizacao = ? ");
    	
    	return (List<AwbOcorrenciaLocalizacao>) getAdsmHibernateTemplate().find(sql.toString(), new Object[] {idAwb, tpLocalizacao});
	}
	
	public List<AwbOcorrenciaLocalizacao> findAwbOcorrenciaLocalizacaoByIdAwb(Long idAwb){
		StringBuilder sql = new StringBuilder()
		.append("select ao ")
		.append(" from ")
		.append(AwbOcorrenciaLocalizacao.class.getName()).append(" as ao ")
		.append(" inner join ao.awb a ")
		.append(" where ")
		.append(" a.idAwb = ? ")
		;
		
		return (List<AwbOcorrenciaLocalizacao>) getAdsmHibernateTemplate().find(sql.toString(), new Object[] {idAwb});
	}

}
