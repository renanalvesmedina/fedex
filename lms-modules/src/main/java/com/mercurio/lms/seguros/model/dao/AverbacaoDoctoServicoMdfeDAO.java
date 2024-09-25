package com.mercurio.lms.seguros.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.seguros.model.AverbacaoDoctoServicoMdfe;

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
public class AverbacaoDoctoServicoMdfeDAO extends BaseCrudDao<AverbacaoDoctoServicoMdfe, Long> {

    /**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<AverbacaoDoctoServicoMdfe> getPersistentClass() {
		return AverbacaoDoctoServicoMdfe.class;
	}
        
	public void updateDataEnvioMdfe(Long idAverbacao, String tpWebservice) {
		StringBuilder sql = new StringBuilder();

			if ("E".equals(tpWebservice)) {
				sql.append("UPDATE AVERB_DOCTO_SERVICO_MDFE SET DH_ENVIO_AUTORIZADO = sysdate, DH_ENVIO_AUTORIZADO_TZR = 'America/Sao_Paulo' WHERE ID_AVERB_DOCTO_SERVICO_MDFE = :id");
			} else if ("C".equals(tpWebservice)) {
				sql.append("UPDATE AVERB_DOCTO_SERVICO_MDFE SET DH_ENVIO_CANCELADO = sysdate, DH_ENVIO_CANCELADO_TZR = 'America/Sao_Paulo' WHERE ID_AVERB_DOCTO_SERVICO_MDFE = :id");
			} else if ("G".equals(tpWebservice)) {
				sql.append("UPDATE AVERB_DOCTO_SERVICO_MDFE SET DH_ENVIO_ENCERRADO = sysdate, DH_ENVIO_ENCERRADO_TZR = 'America/Sao_Paulo' WHERE ID_AVERB_DOCTO_SERVICO_MDFE = :id");
			} else {
				throw new IllegalArgumentException("Situação de tp_webservice de averbação (" + tpWebservice + ") não mapeada.");
			}
		
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("id", idAverbacao);
                
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}

	public List<AverbacaoDoctoServicoMdfe> findByIds(Set<Long> ids) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.in("idAverbacaoDoctoServicoMdfe", ids));
		return criteria.list();
	}

    public List<AverbacaoDoctoServicoMdfe> findMonitoramentoAverbacoes(Map<String, Object> criteria) {
        String hql = mountQuery(criteria);
        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), criteria);
    }
    
    public List<Map<String,Object>> findMonitoramentoAverbacoesBySql(Map<String,Object> criteria){
    	return getAdsmHibernateTemplate().findBySqlToMappedResult(mountSql(criteria), criteria, getFindConfiguration());
    }

	private ConfigureSqlQuery getFindConfiguration() {
		
		return new ConfigureSqlQuery() {
			
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				
				sqlQuery.addScalar("idAverbacaoDoctoServico", Hibernate.LONG);
				sqlQuery.addScalar("tpSituacaoDocumento",Hibernate.STRING);
				sqlQuery.addScalar("nrProtocolo",Hibernate.STRING);
				sqlQuery.addScalar("blAutorizado",Hibernate.STRING);
				sqlQuery.addScalar("blEncerrado",Hibernate.STRING);
				sqlQuery.addScalar("tpWebservice",Hibernate.STRING);
				sqlQuery.addScalar("dhEnvioAutorizado",Hibernate.DATE);
				sqlQuery.addScalar("dhEnvioCancelado",Hibernate.DATE);
				sqlQuery.addScalar("dhEnvioEncerrado",Hibernate.DATE);
				sqlQuery.addScalar("tpModalManifesto",Hibernate.STRING);
				sqlQuery.addScalar("dsObservacaoSefaz",Hibernate.STRING);
				sqlQuery.addScalar("blOperacaoSpitFire", Hibernate.STRING);
				
			}
		};
	}

    private String mountQuery(Map<String, Object> criteria) {
        StringBuilder hql = new StringBuilder();
        hql.append(" SELECT ADSM FROM AverbacaoDoctoServicoMdfe ADSM ")
            .append(" INNER JOIN FETCH ADSM.manifestoEletronico ME ")
            .append(" INNER JOIN FETCH ME.controleCarga CC ")
            .append(" LEFT OUTER JOIN fetch CC.manifestos M ")
            .append(" LEFT OUTER JOIN CC.filialByIdFilialOrigem F ");
        if (criteria.get("idCliente") != null) {
            hql.append(" LEFT OUTER JOIN M.cliente C ");
        }
        hql.append(" WHERE F.idFilial = :idFilial ");

        if (criteria.get("idCliente") != null) {
            hql.append(" AND C.idCliente = :idCliente ");
        }
        if (criteria.get("averbado") != null) {
            hql.append(" AND ((ADSM.blAutorizado = :averbado ")
                .append(" OR ADSM.blEncerrado = :averbado) ")
                .append(" OR (ADSM.blAutorizado = :averbado ")
                .append(" OR ADSM.blCancelado = :averbado) ")
                .append(") ");
        }

        hql.append(" AND ( ")
            .append(" (TRUNC(ADSM.dhEnvioAutorizado.value) >= :dtAverbacaoInicial ")
            .append(" AND TRUNC(ADSM.dhEnvioAutorizado.value) <= :dtAverbacaoFinal) ")
            .append(" OR (TRUNC(ADSM.dhEnvioEncerrado.value) >= :dtAverbacaoInicial ")
            .append(" AND TRUNC(ADSM.dhEnvioEncerrado.value) <= :dtAverbacaoFinal) ")
            .append(" OR (TRUNC(ADSM.dhEnvioCancelado.value) >= :dtAverbacaoInicial ")
            .append(" AND TRUNC(ADSM.dhEnvioCancelado.value) <= :dtAverbacaoFinal) ")
            .append(" ) ");

        hql.append(" AND ADSM.tpDestino = :tpDestino");

        hql.append(" ORDER BY ADSM.idAverbacaoDoctoServicoMdfe DESC ");
        return hql.toString();
    }

    public String mountSql(Map<String,Object> criteria){
    	StringBuilder sql = new StringBuilder();
    	
    	String sqlTpModal = "select m.tp_modal from manifesto m where m.id_controle_carga = cc.id_controle_carga and rownum = 1";
    	sql.append("SELECT ");
		sql.append(" ABSM.ID_AVERB_DOCTO_SERVICO_MDFE as idAverbacaoDoctoServico, ");
		sql.append("    (SELECT vi18n(ds_valor_dominio_i)");
        sql.append("          FROM valor_dominio");
        sql.append("         WHERE id_dominio IN");
        sql.append("               (SELECT id_dominio");
        sql.append("                  FROM dominio");
        sql.append("                 WHERE nm_dominio = 'DM_SITUACAO_MANIFESTO_ELETRONICO')");
        sql.append("           AND vl_valor_dominio = me.tp_situacao) as tpSituacaoDocumento, ");
    	sql.append(" ME.NR_PROTOCOLO as nrProtocolo,");
    	sql.append(" ABSM.BL_AUTORIZADO as blAutorizado,");
    	sql.append(" ABSM.BL_ENCERRADO as blEncerrado,");
    	sql.append(" ABSM.TP_WEBSERVICE as tpWebservice,");
    	sql.append(" ABSM.DH_ENVIO_AUTORIZADO as dhEnvioAutorizado,");
    	sql.append(" ABSM.DH_ENVIO_CANCELADO as dhEnvioCancelado,");
    	sql.append(" ABSM.DH_ENVIO_ENCERRADO as dhEnvioEncerrado,");
    	sql.append(" ("+sqlTpModal+") as tpModalManifesto,");
    	sql.append(" ME.DS_OBSERVACAO_SEFAZ as dsObservacaoSefaz,");
		sql.append(" C.BL_SPITFIRE as blOperacaoSpitFire");
    	
    	sql.append(" FROM ");
    	sql.append(" averb_docto_servico_mdfe   ABSM, ");
    	sql.append(" manifesto_eletronico       ME, ");
    	sql.append(" controle_carga             CC, ");
    	sql.append(" filial                     F, ");
    	sql.append(" manifesto                  M, ");
		sql.append(" manifesto_entrega_documento MED, ");
		sql.append(" conhecimento                C, ");
		sql.append(" manifesto_nacional_cto      MNC ");

    	sql.append(" WHERE ");
    	sql.append(" ABSM.id_manifesto_eletronico = ME.id_manifesto_eletronico ");
    	sql.append(" AND ME.id_controle_carga = CC.id_controle_carga ");
    	sql.append(" AND CC.id_filial_origem = F.id_filial (+) ");
		sql.append(" AND CC.id_controle_carga = M.id_controle_carga (+) ");
    	
    	if (criteria.get("idCliente") != null) {
    		sql.append(" AND M.id_cliente_consig = :idCliente ");
    	}
		sql.append(" AND M.id_manifesto = MED.id_manifesto_entrega (+) ");
		sql.append(" AND C.ID_CONHECIMENTO = MED.id_docto_servico (+) ");
		sql.append(" AND M.ID_MANIFESTO = MNC.id_manifesto_viagem_nacional (+) ");
		sql.append(" AND C.ID_CONHECIMENTO = MNC.id_conhecimento ");
    	
    	sql.append(" AND F.id_filial = :idFilial ");
    	sql.append(" AND ( trunc(cast(ABSM.dh_envio_autorizado as date)) >= :dtAverbacaoInicial ");
    	sql.append(" AND trunc(cast(ABSM.dh_envio_autorizado as date)) <= :dtAverbacaoFinal ");
    	sql.append(" OR trunc(cast(ABSM.dh_envio_encerrado as date)) >= :dtAverbacaoInicial ");
    	sql.append(" AND trunc(cast(ABSM.dh_envio_encerrado as date)) <= :dtAverbacaoFinal ");
    	sql.append(" OR trunc(cast(ABSM.dh_envio_cancelado as date)) >= :dtAverbacaoInicial ");
    	sql.append(" AND trunc(cast(ABSM.dh_envio_cancelado as date)) <= :dtAverbacaoFinal ) ");
    	sql.append(" AND ABSM.tp_destino = :tpDestino ");
    	
    	if (criteria.get("averbado") != null) {
            sql.append(" AND ((ABSM.bl_autorizado = :averbado ")
                .append(" OR ABSM.bl_encerrado = :averbado) ")
                .append(" OR (ABSM.bl_autorizado = :averbado ")
                .append(" OR ABSM.bl_cancelado = :averbado) ")
                .append(") ");
        }
    	
    	return sql.toString();
    }

	@SuppressWarnings("unchecked")
	public List<AverbacaoDoctoServicoMdfe> findDeclaracaoMdfePendenteEnvio(String tpDestino) {

		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT a ");
		hql.append(" FROM AverbacaoDoctoServicoMdfe a ");
		hql.append(" inner join fetch a.manifestoEletronico me ");
		hql.append(" WHERE 1 = 1 ");
		hql.append(" AND trunc(cast(a.dhInclusao.value as date)) >= trunc(sysdate-30) ");
		hql.append(" AND ( ");
		hql.append("  (a.tpWebservice = 'E' and trunc(cast(a.dhEnvioAutorizado.value as date)) is null and me.dsDadosAutorizacao is not null) or ");
		hql.append("  (a.tpWebservice = 'C' and trunc(cast(a.dhEnvioCancelado.value as date)) is null and me.dsDadosCancelamento is not null) or ");
		hql.append("  (a.tpWebservice = 'G' and trunc(cast(a.dhEnvioEncerrado.value as date)) is null and me.dsDadosEncerramento is not null) ");
		hql.append(")");

		if (tpDestino != null) {
			hql.append(" and a.tpDestino = '").append(tpDestino).append("' ");
		}

		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{});
	}
}