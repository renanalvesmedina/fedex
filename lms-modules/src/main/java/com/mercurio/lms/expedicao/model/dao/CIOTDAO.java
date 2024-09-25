package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CIOTDAO extends BaseCrudDao<CIOT, Long> {
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return CIOT.class;
	}

	public void updateTpSituacao(Long idCIOT, String tpSituacao){
		StringBuilder hql = new StringBuilder();
		hql.append("UPDATE ");
		hql.append(getPersistentClass().getName());
		hql.append(" c ");
		hql.append("SET c.tpSituacao = ? ");
		hql.append("WHERE c.idCIOT = ? ");

		List<Object> param = new ArrayList<Object>();
		param.add(tpSituacao);
		param.add(idCIOT);

		executeHql(hql.toString(), param);
	}
	
	public List<Map<String, Object>> findRelatorioCIOT(TypedFlatMap criteria) {
		Map<String, Object> parametersValues = new HashMap<String, Object>(); 
		StringBuilder sql = getSqlRelatorioCIOT(criteria, parametersValues);
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), parametersValues, getConfigureSqlQueryRelatorioCIOT());
	}
	
	private StringBuilder getSqlRelatorioCIOT(TypedFlatMap criteria, Map<String, Object> parametersValues){
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append("f.sg_filial as filialOrigem ");
		sql.append(",cc.nr_controle_carga as controleCarga ");
		sql.append(",(SELECT VI18N(vc.ds_valor_dominio_i) FROM dominio dc, valor_dominio vc WHERE dc.id_dominio = vc.id_dominio AND dc.nm_dominio = 'DM_STATUS_CONTROLE_CARGA' AND cc.tp_status_controle_carga = vc.vl_valor_dominio ) as statusControleCarga ");
		sql.append(",mt.nr_identificador  as meioTranporte "); 
		sql.append(",cc.bl_exige_ciot  as exigeCIOT ");
		sql.append(",CASE WHEN c.nr_ciot IS NOT NULL THEN c.nr_ciot || '/' || c.nr_cod_verificador ELSE NULL END as numeroCIOT ");
		sql.append(",(SELECT VI18N(vc.ds_valor_dominio_i) FROM dominio dc, valor_dominio vc WHERE dc.id_dominio = vc.id_dominio AND dc.nm_dominio = 'DM_SITUACAO_CIOT' AND c.tp_situacao = vc.vl_valor_dominio ) as situacao ");
		sql.append(",TO_CHAR(c.vl_frete, 'FM999G999G990D90')  as valorFrete ");
		sql.append(",(SELECT MAX(TO_CHAR(ecc.dh_evento,'DD/MM/YYYY')) FROM evento_controle_carga ecc WHERE cc.id_controle_carga = ecc.id_controle_carga AND cc.id_filial_origem = ecc.id_filial AND ecc.tp_evento_controle_carga = 'EM') as  dataEmissaoControleCarga ");
		sql.append(",TO_CHAR(c.dh_geracao,'DD/MM/YYYY') as dtGeracaoCIOT ");
		sql.append(",TO_CHAR(c.dh_alteracao,'DD/MM/YYYY') as dtAlteracao ");
		sql.append(",TO_CHAR(c.dh_encerramento,'DD/MM/YYYY') as dataEncerramento ");
		sql.append(",c.nr_protocolo_encer as protocoloEncerramento ");
		sql.append(",TO_CHAR(c.dh_cancelamento,'DD/MM/YYYY') as dtCancelamento ");
		sql.append(",c.nr_protocolo_cancel as protocoloCancelamento ");
		sql.append(",replace(replace(c.ds_observacao,CHR(13),''),CHR(10),' ') as observacao ");
		
		sql.append("FROM ");
		sql.append("controle_carga cc  ");
		sql.append(",ciot c ");
		sql.append(",ciot_controle_carga ccc  ");
		sql.append(",filial f  ");
		sql.append(",meio_transporte mt  ");
		sql.append(",proprietario p  ");
		
		sql.append("WHERE 1=1 ");
		sql.append("AND cc.id_filial_origem = f.id_filial ");
		sql.append("AND cc.id_proprietario = p.id_proprietario ");
		sql.append("AND cc.bl_exige_ciot IS NOT NULL ");
		sql.append("AND p.tp_proprietario <> 'P' ");
		sql.append("AND cc.id_controle_carga = ccc.id_controle_carga(+) ");
		sql.append("AND c.id_ciot(+)  = ccc.id_ciot ");
		sql.append("AND c.id_meio_transporte = mt.id_meio_transporte(+) ");

		sql.append("AND cc.id_filial_origem = :idFilial ");
		parametersValues.put("idFilial", criteria.getLong("idFilial"));

		sql.append("AND SYS_EXTRACT_UTC(cc.dh_geracao) >= :periodoInicial ");
		parametersValues.put("periodoInicial", JTDateTimeUtils.formatDateTimeToString(criteria.getDateTime("periodoInicial"), JTDateTimeUtils.DATETIME_WITH_SECONDS_PATTERN));
		
		sql.append("AND SYS_EXTRACT_UTC(cc.dh_geracao) <= :periodoFinal ");
		parametersValues.put("periodoFinal", JTDateTimeUtils.formatDateTimeToString(criteria.getDateTime("periodoFinal"), JTDateTimeUtils.DATETIME_WITH_SECONDS_PATTERN));
		
		if(criteria.getString("tpSituacao") != null){
			sql.append("AND c.tp_situacao = :tpSituacao ");
			parametersValues.put("tpSituacao", criteria.getString("tpSituacao"));
		}
		
		if(criteria.getString("tpControleCarga") != null){
			sql.append("AND cc.tp_controle_carga = :tpControleCarga ");
			parametersValues.put("tpControleCarga", criteria.getString("tpControleCarga"));
		}
		
		sql.append("ORDER BY cc.nr_controle_carga, mt.nr_identificador");
		return sql;
	}
	
	private ConfigureSqlQuery getConfigureSqlQueryRelatorioCIOT(){
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("filialOrigem", Hibernate.STRING);
				sqlQuery.addScalar("controleCarga", Hibernate.LONG);
				sqlQuery.addScalar("statusControleCarga", Hibernate.STRING);
				sqlQuery.addScalar("meioTranporte", Hibernate.STRING);
				sqlQuery.addScalar("exigeCIOT", Hibernate.STRING);
				sqlQuery.addScalar("numeroCIOT", Hibernate.STRING);
				sqlQuery.addScalar("situacao", Hibernate.STRING);
				sqlQuery.addScalar("valorFrete", Hibernate.STRING);
				sqlQuery.addScalar("dataEmissaoControleCarga", Hibernate.STRING);
				sqlQuery.addScalar("dtGeracaoCIOT", Hibernate.STRING);
				sqlQuery.addScalar("dtAlteracao", Hibernate.STRING);
				sqlQuery.addScalar("dataEncerramento", Hibernate.STRING);
				sqlQuery.addScalar("protocoloEncerramento", Hibernate.STRING);
				sqlQuery.addScalar("dtCancelamento", Hibernate.STRING);
				sqlQuery.addScalar("protocoloCancelamento", Hibernate.STRING);
				sqlQuery.addScalar("observacao", Hibernate.STRING);
			}
		};
		return csq;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("controleCarga", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	public CIOT findGeradoAPartirDe(Long idMeioTransporte, YearMonthDay data) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT c FROM ");
		hql.append(getPersistentClass().getName()).append(" c ");
		hql.append(" INNER JOIN FETCH c.controleCarga cc ");
		hql.append(" INNER JOIN FETCH c.meioTransporte m ");
		hql.append(" WHERE m.idMeioTransporte = ? ");
		hql.append(" AND TRUNC(c.dhGeracao.value) > ? ");
		hql.append(" AND c.tpSituacao = 'G' ");
		hql.append(" AND cc.tpControleCarga = 'C' ");
		
		return (CIOT) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idMeioTransporte, data});
	}
	
}