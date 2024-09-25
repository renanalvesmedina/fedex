package com.mercurio.lms.entrega.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.entrega.model.EntregaNotaFiscal;
import com.mercurio.lms.util.SQLUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EntregaNotaFiscalDAO extends BaseCrudDao<EntregaNotaFiscal, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EntregaNotaFiscal.class;
    }
    
	public List<Long> findNotasComOcorrenciaEntrega(final Long idManifesto, final List<Long> idsNotasFiscaisConhecimento) {
		final StringBuilder sql = new StringBuilder()
			.append(" SELECT id_nota_fiscal_conhecimento ")
			.append("   FROM entrega_nota_fiscal ENF ")
			.append("  WHERE id_manifesto = :idManifesto ")
			.append("    AND id_nota_fiscal_conhecimento IN( :idsNotasFiscaisConhecimento ) ")
			.append("    AND id_ocorrencia_entrega IS NOT NULL ");

		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.setLong("idManifesto", idManifesto);
				sqlQuery.setParameterList("idsNotasFiscaisConhecimento", idsNotasFiscaisConhecimento);
				sqlQuery.addScalar("id_nota_fiscal_conhecimento", Hibernate.LONG);
			}
		};

		final HibernateCallback hcb = new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				csq.configQuery(query);
				return query.list();
			}
		};

		return getHibernateTemplate().executeFind(hcb);
	}
	
	public Long findIdByIdNotaFiscalConhecimento(Long idNotaFiscalConhecimento) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT enf.idEntregaNotaFiscal");
		hql.append(" FROM EntregaNotaFiscal enf ");
		hql.append(" WHERE enf.notaFiscalConhecimento.id = ? ");
		
		List param = new ArrayList();
		param.add(idNotaFiscalConhecimento);
		
		List result = getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
		return (Long) (result.isEmpty() ? null : result.get(0));
	}
	public void executeAlteracaoNota(Long idNotaFiscalConhecimento,
			Long idOcorrenciaEntrega, String observacao) {
		StringBuilder sql = new StringBuilder()
			.append("update entrega_nota_fiscal enf ")
			.append("set enf.OB_ALTERACAO = :obAlteracao ||  enf.OB_ALTERACAO, ") 
			.append("enf.ID_OCORRENCIA_ENTREGA = :idOcorenciaEntrega ") 
			.append("where enf.ID_NOTA_FISCAL_CONHECIMENTO = :idNotaFiscalConhecimento ");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idNotaFiscalConhecimento",idNotaFiscalConhecimento);
		parameters.put("idOcorenciaEntrega",idOcorrenciaEntrega);
		parameters.put("obAlteracao",observacao);
		
		
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parameters);
		
	}

	public List<EntregaNotaFiscal> findByIdManifestoEntregaDocumento(Long idManifestoEntregaDocumento) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
        parametersValues.put("idManifestoEntregaDocumento", idManifestoEntregaDocumento);

        StringBuilder query = new StringBuilder();
        query.append(" select enf");
        query.append(" from EntregaNotaFiscal as enf ");
        query.append(" where enf.manifestoEntregaDocumento.id = :idManifestoEntregaDocumento ");

        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametersValues);
	}
	
	public boolean existsNotaFiscalSemEntregaByIdsDoctoServico(List<Long> idsDoctoServico, Long idControleCarga) {
	   	StringBuilder sql = new StringBuilder()
	   		.append(" SELECT 1 ")
	   		.append("   FROM NotaFiscalConhecimento NFC")
	   		.append("  WHERE NFC.conhecimento.id IN " + SQLUtils.mountNumberForInExpression(idsDoctoServico))
	   		.append("  AND NOT EXISTS (SELECT 1 ")
	   		.append("            FROM EntregaNotaFiscal ENF, Manifesto MAN ")
	   		.append("            WHERE ENF.notaFiscalConhecimento.id = NFC.id")
	   		.append("   		 AND ENF.manifesto.id = MAN.id ")
	   		.append("            AND MAN.controleCarga.id = ? ) ")
	   		.append(" AND NOT EXISTS (SELECT 1 ")
            .append("            FROM NotaFiscalOperada NFO ")
            .append("            WHERE NFO.notaFiscalConhecimentoOriginal.id = NFC.id")
            .append("            AND NFO.tpSituacao in ('EN', 'DV', 'RF'))");
	   	
	   	List param = new ArrayList();
        param.add(idControleCarga);
	   	
	   	return !getAdsmHibernateTemplate().find(sql.toString(), param.toArray()).isEmpty();
	}
}