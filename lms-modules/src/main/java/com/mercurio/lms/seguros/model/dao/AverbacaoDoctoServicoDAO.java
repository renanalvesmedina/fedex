package com.mercurio.lms.seguros.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.seguros.model.AverbacaoDoctoServico;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 *
 * @spring.bean
 */
@SuppressWarnings("deprecation")
public class AverbacaoDoctoServicoDAO extends
		BaseCrudDao<AverbacaoDoctoServico, Long> {

    /**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<AverbacaoDoctoServico> getPersistentClass() {
		return AverbacaoDoctoServico.class;
	}

	@SuppressWarnings("unchecked")
	public List<AverbacaoDoctoServico> findAberbacaoPendenteEnvio(String tpDestino, Integer rownumEnvio) {

		StringBuilder hql = new StringBuilder();
		hql.append(" select a from AverbacaoDoctoServico a ");
		hql.append(" inner join a.doctoServico ds ");
		hql.append("where a.blEnviado = 'N' and (a.tpWebservice = 'C' or a.tpWebservice = 'E') ");

		if (tpDestino != null) {
			hql.append(" and a.tpDestino = '").append(tpDestino).append("' ");
		}
        hql.append(" and rownum <= " + rownumEnvio );
        
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{});
	}
        
        @SuppressWarnings("unchecked")
	public List<AverbacaoDoctoServico> findDeclaracaoMdfePendenteEnvio(String tpDestino) {

		StringBuilder hql = new StringBuilder();
		hql.append(" select a from AverbacaoDoctoServicoMdfe a ");
		hql.append(" 	inner join fetch a.manifestoEletronico me ");
		hql.append(" where a.doctoServico is null and ( ");
        hql.append("    (a.tpWebservice = 'E' and a.dhMdfeEnvioAutorizado is null and me.dsDadosAutorizacao is not null) or ");
        hql.append("    (a.tpWebservice = 'C' and a.dhMdfeEnvioCancelado is null and me.dsDadosCancelamento is not null) or ");
        hql.append("    (a.tpWebservice = 'G' and a.dhMdfeEnvioEncerrado is null and me.dsDadosEncerramento is not null) ");
                hql.append(")");

		if (tpDestino != null) {
			hql.append(" and a.tpDestino = '").append(tpDestino).append("' ");
		}

		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{});
	}


	@SuppressWarnings("unchecked")
	public List<AverbacaoDoctoServico> findCanceladoPendenteByIdDoctoServico(Long idDoctoServico) {

		StringBuilder hql = new StringBuilder();
		hql.append(" select a from AverbacaoDoctoServico a ");
		hql.append(" 	join a.doctoServico ds ");
		hql.append(" where a.dhEnvio is null and a.tpWebservice = 'B'");
		hql.append(" and ds.idDoctoServico = '").append(idDoctoServico).append("' ");

		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{});
	}

	// LMSA-7369
	@SuppressWarnings("unchecked")
	public List<AverbacaoDoctoServico> findByIdDoctoServicoAverbado(Long idDoctoServico) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select a from AverbacaoDoctoServico a ");
		hql.append(" 	join a.doctoServico ds ");
		hql.append(" where a.blAverbado = 'S'");
		hql.append(" and ds.idDoctoServico = '").append(idDoctoServico).append("' ");
		hql.append(" order by a.idAverbacaoDoctoServico DESC ");
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{});
	}

	public void updateDataEnvio(Long idAverbacao, Integer nrEnvio){
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE AVERBACAO_DOCTO_SERVICO SET DH_ENVIO = sysdate, DH_ENVIO_TZR = 'America/Sao_Paulo', NR_ENVIO = :nrEnvio WHERE ID_AVERBACAO_DOCTO_SERVICO = :id");
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("id", idAverbacao);
		parametersValues.put("nrEnvio", nrEnvio);
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}
        
	public void updateDataEnvio(Long idAverbacao){
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE AVERBACAO_DOCTO_SERVICO SET DH_ENVIO = sysdate, DH_ENVIO_TZR = 'America/Sao_Paulo',");
		sql.append("NR_ENVIO = 1, BL_ENVIADO = 'S' WHERE ID_AVERBACAO_DOCTO_SERVICO = :id");
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("id", idAverbacao);
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}
	
	
    private SqlTemplate mountQuery(Map<String, Object> criteria) {
        SqlTemplate sqlTemplate = new SqlTemplate();
        sqlTemplate.addProjection("ads");
        sqlTemplate.addInnerJoin("AverbacaoDoctoServico", "ads");
        
        sqlTemplate.addLeftOuterJoin("fetch ads.doctoServico", "ds");
        sqlTemplate.addInnerJoin("fetch ds.filialByIdFilialOrigem", "f");
        sqlTemplate.addInnerJoin("fetch ds.clienteByIdClienteRemetente", "c");
        sqlTemplate.addInnerJoin("fetch ds.servico", "ser");
        sqlTemplate.addInnerJoin("fetch c.pessoa", "p");
        sqlTemplate.addLeftOuterJoin("fetch ads.manifestoEletronico", "me");

        sqlTemplate.addCriteria("f.idFilial", "=", criteria.get("idFilial"));
        
        if (criteria.get("nrConhecimento") != null) {        	
            sqlTemplate.addCriteria("ds.nrDoctoServico", "=", criteria.get("nrConhecimento"));
        }
        
        if(criteria.get("idCliente") != null){
            sqlTemplate.addCriteria("c.idCliente", "=", criteria.get("idCliente"));
        }
        
        if(criteria.get("averbado") != null){
            sqlTemplate.addCriteria("ads.blAverbado", "=", criteria.get("averbado"));
        }
        sqlTemplate.addCriteria("trunc(cast(ads.dhEnvio.value as date))", ">=", criteria.get("dtAverbacaoInicial"));
        sqlTemplate.addCriteria("trunc(cast(ads.dhEnvio.value as date))", "<=", criteria.get("dtAverbacaoFinal"));
        sqlTemplate.addCriteria("ads.tpDestino", "=", criteria.get("tpDestino"));
                
        sqlTemplate.addOrderBy("ds.nrDoctoServico", "asc");
        sqlTemplate.addOrderBy("ads.dhEnvio", "asc");
        
        return sqlTemplate;
    }


    public Integer getRowCountMonitoramentoAverbacoes(Map<String, Object> criteria) {
        Integer total = 0;
        
        total += getAdsmHibernateTemplate().getRowCountBySql(mountSqlQuery(criteria), criteria);
        
        return total;
    }

    @SuppressWarnings("unchecked")
	public List<AverbacaoDoctoServico> findMonitoramentoAverbacoes(Map<String, Object> criteria) {
        SqlTemplate sqlTemplate = mountQuery(criteria);
        return getAdsmHibernateTemplate().find(sqlTemplate.getSql(), sqlTemplate.getCriteria());
    }

    public AverbacaoDoctoServico findByIdDoctoServicoAndTpWebservice(Long idDoctoServico, String tpWebserviceE) {

        StringBuilder hql = new StringBuilder();
        hql.append(" select a from AverbacaoDoctoServico a ");
        hql.append(" join a.doctoServico ds ");
        hql.append(" where ds.idDoctoServico = " + idDoctoServico);
        hql.append(" and a.tpWebservice = '" + tpWebserviceE + "'");

        return (AverbacaoDoctoServico) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{});
    }

    public AverbacaoDoctoServico findByIdComMdfe(Long id) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select a from AverbacaoDoctoServico a ");
        hql.append(" join a.averbacaoDoctoServicoMdfe adsm ");
        hql.append(" join a.doctoServico ds ");

        return (AverbacaoDoctoServico) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{});
    
    }
    
    public List<AverbacaoDoctoServico> findByIds(Set<Long> ids) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.in("idAverbacaoDoctoServico", ids));
        return criteria.list();
    }

     @SuppressWarnings("unchecked")
    public List<AverbacaoDoctoServico> findAverbacoesParaReenvio(Integer numMaxReenvio, Integer rownumReenvio) {

        StringBuilder hql = new StringBuilder();
        hql.append(" select a from AverbacaoDoctoServico a ");
        hql.append(" 	join a.doctoServico ds         ");
        hql.append(" where a.dhEnvio is not null       ");
        hql.append(" and (a.dsRetorno is null or a.dsRetorno = 'Erro de sistema') ");
        hql.append(" and a.tpWebservice in ('C','E')   ");
        hql.append(" and a.nrEnvio <= ").append(numMaxReenvio);
        hql.append(" and ds.tpDocumentoServico = 'CTE' ");
        hql.append(" and rownum <= ").append(rownumReenvio);

		 return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{});
    }
    
    @SuppressWarnings("unchecked")
    public List<AverbacaoDoctoServico> findAverbacoesSemRetornoParaEnvioEmail(Integer numMaxReenvio, Integer rownumEnvio) {

        StringBuilder hql = new StringBuilder();
        hql.append(" select a from AverbacaoDoctoServico a ");
        hql.append(" 	join a.doctoServico ds         ");
        hql.append(" where a.dhEnvio is not null       ");
        hql.append(" and a.tpWebservice in ('C','E')   ");
        hql.append(" and a.nrEnvio = ").append(numMaxReenvio);
        hql.append(" and ds.tpDocumentoServico = 'CTE' ");		
        hql.append(" and rownum <= ").append(rownumEnvio);
        
        return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{});
    }

	public List<Map<String, Object>> findMonitoramentoAverbacoesBySql(
			Map<String, Object> criteria) {
    	return getAdsmHibernateTemplate().findBySqlToMappedResult(mountSqlQuery(criteria), criteria, getFindConfiguration());
	}
	
	public String mountSqlQuery(Map<String,Object> criteria){
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("     ads.id_averbacao_docto_servico  as idAverbacaoDoctoServico, ");
		sql.append("     ds.tp_documento_servico         as tpDocumentoServico, ");
		sql.append("     f.sg_filial                     as sgFilial, ");
		sql.append("     ds.nr_docto_servico             as nrDoctoServico, ");
		sql.append("     p.nm_pessoa                     as nmCliente, ");
		sql.append("    (SELECT vi18n(ds_valor_dominio_i)");
        sql.append("          FROM valor_dominio");
        sql.append("         WHERE id_dominio IN");
        sql.append("               (SELECT id_dominio");
        sql.append("                  FROM dominio");
        sql.append("                 WHERE nm_dominio = 'DM_STATUS_CONHECIMENTO')");
        sql.append("           AND vl_valor_dominio = co.tp_situacao_conhecimento) as tpSituacaoDocumento, ");
		sql.append("     ads.nr_averbacao                as nrAverbacao, ");
		sql.append("     ads.ds_retorno                  as dsRetorno, ");
		sql.append("     ads.dh_envio                    as dhEnvio, ");
		sql.append("     ads.nr_protocolo                as nrProtocolo, ");
		sql.append("     ads.bl_averbado                 as blAverbado, ");
		sql.append("     s.tp_modal                      as tpModal, ");
		sql.append("     mde.ds_observacao               as dsObservacaoSefaz, ");
		sql.append("     co.bl_spitfire                  as blOperacaoSpitFire ");
		sql.append(" FROM ");
		sql.append("     averbacao_docto_servico   ADS, ");
		sql.append("     docto_servico             DS, ");
		sql.append("     conhecimento              CO, ");
		sql.append("     filial                    F, ");
		sql.append("     cliente                   C, ");
		sql.append("     pessoa                    P, ");
		sql.append("     servico                   S, ");
		sql.append("     monitoramento_doc_eletronico MDE ");
		sql.append(" WHERE ");
		sql.append("     ADS.id_docto_servico = DS.id_docto_servico (+) ");
		sql.append("    AND DS.id_docto_servico = CO.id_conhecimento (+) ");
		sql.append(" AND DS.id_filial_origem = F.id_filial ");
		sql.append(" AND DS.id_cliente_remetente = C.id_cliente ");
		sql.append(" AND C.id_cliente = P.id_pessoa ");
		sql.append("  AND DS.id_servico = S.id_servico ");
		sql.append("  AND DS.id_docto_servico = MDE.id_docto_servico ");
		sql.append("  AND F.id_filial = :idFilial ");
		sql.append("  AND trunc(CAST(ADS.dh_envio AS DATE)) >= :dtAverbacaoInicial ");
		sql.append("  AND trunc(CAST(ADS.dh_envio AS DATE)) <= :dtAverbacaoFinal ");
		sql.append("  AND ADS.tp_destino = :tpDestino ");
		
		if (criteria.get("nrConhecimento") != null) {        	
			sql.append(" and ds.nr_docto_servico = :nrConhecimento");
        }
		
		if(criteria.get("idCliente") != null){
			sql.append(" and c.id_Cliente = :idCliente");
        }
        
        if(criteria.get("averbado") != null){
        	sql.append(" and ads.bl_Averbado = :averbado");
        }
		
		sql.append(" ORDER BY ");
		sql.append("  DS.nr_docto_servico ASC, ");
		sql.append("   ADS.dh_envio, ");
		sql.append("  ADS.dh_envio_tzr ASC ");
		
		return sql.toString();
		
	}
	
	private ConfigureSqlQuery getFindConfiguration() {
		
		return new ConfigureSqlQuery() {
			
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				
				sqlQuery.addScalar("idAverbacaoDoctoServico", Hibernate.LONG);
				sqlQuery.addScalar("tpDocumentoServico", Hibernate.STRING);
				sqlQuery.addScalar("sgFilial", Hibernate.STRING);
				sqlQuery.addScalar("nrDoctoServico", Hibernate.STRING);
				sqlQuery.addScalar("nmCliente", Hibernate.STRING);
				sqlQuery.addScalar("tpSituacaoDocumento", Hibernate.STRING);
				sqlQuery.addScalar("nrAverbacao", Hibernate.STRING);
				sqlQuery.addScalar("dsRetorno", Hibernate.STRING);
				sqlQuery.addScalar("dhEnvio", Hibernate.DATE);
				sqlQuery.addScalar("nrProtocolo", Hibernate.STRING);
				sqlQuery.addScalar("blAverbado", Hibernate.STRING);
				sqlQuery.addScalar("tpModal", Hibernate.STRING);
				sqlQuery.addScalar("dsObservacaoSefaz", Hibernate.STRING);
				sqlQuery.addScalar("blOperacaoSpitFire", Hibernate.STRING);
								
			}
		};
	}
}