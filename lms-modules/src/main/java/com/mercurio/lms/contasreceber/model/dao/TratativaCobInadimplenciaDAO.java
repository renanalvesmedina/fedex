package com.mercurio.lms.contasreceber.model.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.TratativaCobInadimplencia;

public class TratativaCobInadimplenciaDAO extends BaseCrudDao<TratativaCobInadimplencia, Long> {

    protected final Class getPersistentClass() {
        return TratativaCobInadimplencia.class;
    }
    
   
	
	public List findTratativasByFatura(Long idFatura){
		StringBuffer sql = new StringBuffer();
		sql.append("select to_char(t.dh_tratativa,'dd/MM/yyyy HH24:mi') as dh_tratativa, u.nm_usuario, m.ds_motivo_inadimplencia, to_char(t.dt_prev_solucao,'dd/MM/yyyy') as dt_prev_solucao, t.ds_plano_acao, t.ds_parecer_matriz ");
		sql.append(" from fatura f, ITEM_COBRANCA i, TRATATIVA_COB_INADIMPLENCIA t, usuario u, MOTIVO_INADIMPLENCIA m ");
		sql.append(" where f.id_fatura = i.id_fatura and t.id_cobranca_inadimplencia = i.id_cobranca_inadimplencia and u.id_usuario = t.id_usuario ");
		sql.append(" and t.id_motivo_inadimplencia = m.id_motivo_inadimplencia ");
		sql.append(" and f.id_fatura = " + idFatura);
		sql.append(" order by t.dh_tratativa");
		
		List<Object[]> lista = getAdsmHibernateTemplate().findBySql(sql.toString(), null, getConfigureSqlQueryToString());
		List eventos = new ArrayList();
		for(int i=0; i < lista.size();i++){
			Object[] tratativa = lista.get(i);
			TypedFlatMap retorno = new TypedFlatMap();
			retorno.put("dtTratativa",tratativa[0]);
			retorno.put("usuarioTratativa", tratativa[1]);
			retorno.put("motivoInadimplencia", tratativa[2]);
			retorno.put("dtPrevistaSolucao", tratativa[3]);
			retorno.put("planoAcao", tratativa[4]);
			retorno.put("parecerMatriz", tratativa[5]);
			eventos.add(retorno);
		}
		return eventos;
	}
	
	public Object[] findTratativaByIdFatura(Long idFatura){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.dh_tratativa, u.nm_usuario, m.ds_motivo_inadimplencia, t.dt_prev_solucao, t.ds_plano_acao, t.ds_parecer_matriz ");
		sql.append(" from fatura f, ITEM_COBRANCA i, TRATATIVA_COB_INADIMPLENCIA t, usuario u, MOTIVO_INADIMPLENCIA m ");
		sql.append(" where f.id_fatura = i.id_fatura and t.id_cobranca_inadimplencia = i.id_cobranca_inadimplencia and u.id_usuario = t.id_usuario ");
		sql.append(" and t.id_motivo_inadimplencia = m.id_motivo_inadimplencia ");
		sql.append(" and f.id_fatura = " + idFatura);
		sql.append(" order by t.dh_tratativa");
		
		List<Object[]> lista = getAdsmHibernateTemplate().findBySql(sql.toString(), null, getConfigureSqlQuery());
		return lista.get(0);
	}
	
	public TratativaCobInadimplencia findTratativaById(Long idTratativa){
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(TratativaCobInadimplencia.class.getName(), "t");
		hql.addCriteria("t.idTratativaCobInadimplencia", "=", idTratativa);

		List dados = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		if (dados != null && dados.size() == 1){
			return (TratativaCobInadimplencia) dados.get(0);
		} else {
			return null;
		}	
	}
	private ConfigureSqlQuery getConfigureSqlQueryToString() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("dh_tratativa", Hibernate.STRING);
				sqlQuery.addScalar("nm_usuario", Hibernate.STRING);
				sqlQuery.addScalar("ds_motivo_inadimplencia", Hibernate.STRING);
				sqlQuery.addScalar("dt_prev_solucao", Hibernate.STRING);
				sqlQuery.addScalar("ds_plano_acao", Hibernate.STRING);
				sqlQuery.addScalar("ds_parecer_matriz", Hibernate.STRING);
			}
		};
		return csq;
	}
	
	private ConfigureSqlQuery getConfigureSqlQuery() {
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("dh_tratativa", Hibernate.DATE);
				sqlQuery.addScalar("nm_usuario", Hibernate.STRING);
				sqlQuery.addScalar("ds_motivo_inadimplencia", Hibernate.STRING);
				sqlQuery.addScalar("dt_prev_solucao", Hibernate.DATE);
				sqlQuery.addScalar("ds_plano_acao", Hibernate.STRING);
				sqlQuery.addScalar("ds_parecer_matriz", Hibernate.STRING);
			}
		};
		return csq;
	}
    
}