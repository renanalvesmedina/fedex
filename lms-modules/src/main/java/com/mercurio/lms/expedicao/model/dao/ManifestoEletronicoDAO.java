package com.mercurio.lms.expedicao.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.FilialPercursoUF;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;

public class ManifestoEletronicoDAO extends BaseCrudDao<ManifestoEletronico, Long> {

	protected final Class<ManifestoEletronico> getPersistentClass() {
		return ManifestoEletronico.class;
	}

    @SuppressWarnings("unchecked")
    public List<ManifestoEletronico> findManifestoEletronicoByControleCargaAndTpSituacao(Long idControleCarga,Long idFilialLogada, String... tpSituacao) {
    	
    	Map<String, Object> parametros = new HashMap<String, Object>();
    	
    	StringBuffer hql = new StringBuffer();
        hql.append(" select me from ManifestoEletronico me ");
        hql.append("    join me.controleCarga cc  ");
        hql.append("    join me.filialOrigem fo  ");
        hql.append(" where 1 = 1");
        if (idControleCarga != null) {
        	hql.append(" and cc.idControleCarga = :idControleCarga");
        	parametros.put("idControleCarga", idControleCarga);
        }
        
        if (idFilialLogada != null) {
            hql.append("    and fo.idFilial = :idFilial");
            parametros.put("idFilial", idFilialLogada);
        }
        
        if(tpSituacao != null && tpSituacao.length > 0){
            hql.append("    and me.tpSituacao in (:tpSituacao)");
            parametros.put("tpSituacao", tpSituacao);
        }
        hql.append(" order by me.id ");

        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametros);
    }
    
	public List<ManifestoEletronico> findManifestoEletronicoByTpSituacao(Long idFilial, String... tpSituacao) {
		return findManifestoEletronicoByControleCargaAndTpSituacao(null, idFilial, tpSituacao);
	}
    
    @SuppressWarnings("unchecked")
	public ResultSetPage<ManifestoEletronico> findPaginatedManifestoEletronicoByControleCarga(TypedFlatMap criteria) {
		
		String idControleCarga = criteria.getString("idControleCargaTemp");
		
		if(StringUtils.isNotBlank(idControleCarga)){
			
			StringBuffer hql = new StringBuffer();
			hql.append(" select new Map( me.idManifestoEletronico as idManifestoEletronico ," +
					"					fo.idFilial as idFilial," +
					"					fo.sgFilial as sgFilialOrigem," +
					"					me.nrManifestoEletronico as nrManifestoEletronico," +
					"					fd.sgFilial as sgFilialDestino," +
					"					me.tpSituacao as tpSituacao," +
					"					me.dsObservacaoSefaz as dsObservacao" +
					") from ManifestoEletronico me ");
			hql.append("    join me.controleCarga cc  ");
			hql.append("    join me.filialOrigem fo  ");
			hql.append("    join me.filialDestino fd  ");
			hql.append(" where cc.idControleCarga = " + idControleCarga);
			
			List<ManifestoEletronico> manifestosEletronicos = getAdsmHibernateTemplate().find(hql.toString());
			ResultSetPage<ManifestoEletronico> rsp = new ResultSetPage<ManifestoEletronico>(1, manifestosEletronicos);
			return rsp;
		}else return null;
	}

    public ManifestoEletronico findByNrChave(String chave) {
        
        StringBuilder sql = new StringBuilder();
        sql.append(" select me ");
        sql.append(" from ManifestoEletronico me ");
        sql.append(" where me.nrChave = ? ");
        
        return (ManifestoEletronico) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[] {chave});
        
    }

	public List<ManifestoEletronico> findByIds(List<Long> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select me ");
        sql.append(" from ManifestoEletronico me ");
        sql.append(" where me.idManifestoEletronico in ("+StringUtils.join(ids.toArray(new Object[]{}),",")+") ");
        
        return getAdsmHibernateTemplate().find(sql.toString(), new Object[] {});
	}
	
	@SuppressWarnings("unchecked")
	public List<ManifestoEletronico> findManifestosParaEncerramentoAutomatico(YearMonthDay dtLimiteDiasMDFe, YearMonthDay dtLimiteDiasMDFeDuasUFs){
		StringBuilder sql = new StringBuilder();
        sql.append("SELECT  me ");
        sql.append("FROM ");
        sql.append(getPersistentClass().getName());
        sql.append(" me ");
        sql.append("WHERE ");
        sql.append("(me.tpSituacao in ('A', 'D') AND trunc(cast(me.dhEmissao.value as date)) < ? )");
        sql.append(" OR ");
        sql.append("(me.tpSituacao in ('H') AND me.cdStatusEncerramento = 999 )");
        sql.append(" OR ");
        sql.append("(");
        sql.append("	me.tpSituacao in ('A', 'D') AND trunc(cast(me.dhEmissao.value as date)) < ? ");
        sql.append(" 	AND  ");
        sql.append(" 	 	(");
        sql.append(" 	 		SELECT max(fp.nrOrdem) FROM ");
        sql.append(				FilialPercursoUF.class.getName());
        sql.append("			fp ");
        sql.append("			WHERE ");
        sql.append("			fp.filialOrigem.idFilial = me.filialOrigem.idFilial ");
        sql.append("			AND fp.filialDestino.idFilial = me.filialDestino.idFilial ");
        sql.append(" 	 	) <= 2");
        sql.append(")");
        return getAdsmHibernateTemplate().find(sql.toString(), new Object[] {dtLimiteDiasMDFe, dtLimiteDiasMDFeDuasUFs});
	}
	

}