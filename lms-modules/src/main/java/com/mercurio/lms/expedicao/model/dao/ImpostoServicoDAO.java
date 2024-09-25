package com.mercurio.lms.expedicao.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.ImpostoServico;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ImpostoServicoDAO extends BaseCrudDao<ImpostoServico, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<ImpostoServico> getPersistentClass() {
		return ImpostoServico.class;
	}

	public ResultSetPage<ImpostoServico> findPaginated(PaginatedQuery paginatedQuery) {
		StringBuilder query = new StringBuilder()
			.append("from ImpostoServico as i ")
			.append("	inner join fetch i.notaFiscalServico as nfs ")
			.append("	left join fetch i.municipioByIdMunicipioIncidencia as mi ")
			.append("where 1=1 ");

		Map<String, Object> criteria = paginatedQuery.getCriteria();
		if(MapUtils.getObject(criteria, "idNotaFiscalServico") != null) {
			query.append("  and nfs.id = :idNotaFiscalServico ");
		}
		query.append("order by i.tpImposto desc ");
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, query.toString());
		
	}

	public List<Map<String, Object>> findNFServicoAdicionalValores(Long idNotaFiscalServico){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new map(" +
				"imps.tpImposto as tpImposto, " +
				"imps.vlImposto as vlImposto " +
				") ");

		sql.addFrom(ImpostoServico.class.getName() + " as imps " +
				"left join imps.notaFiscalServico nfs ");

		sql.addCriteria("nfs.id", "=", idNotaFiscalServico, Long.class);

		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Boolean findIssRetidoByIdConhecimento(Long idConhecimento) {
		StringBuilder query = new StringBuilder()
		.append(" select impSer from ImpostoServico as impSer ")
		.append("	left join impSer.conhecimento as conhecimento")
		.append(" where  conhecimento.id = ?")
		.append("    and impSer.tpImposto = 'IS'")
		.append("    and impSer.blRetencaoTomadorServico = 'S'");
		
		List param = new ArrayList();
		param.add(idConhecimento);
		
		List retornoQuery = getAdsmHibernateTemplate().find(query.toString(), param.toArray());
		
		if(retornoQuery != null && !retornoQuery.isEmpty()){
			return true;
		}
		
		return false;
	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<ImpostoServico> findoByIdNotaFiscalServico(Long idNotaFiscalServico) {
		StringBuilder query = new StringBuilder()
		.append(" select impSer from ImpostoServico as impSer ")
		.append("	left join impSer.notaFiscalServico as notaFiscalServico")
		.append(" where  notaFiscalServico.id = ?");
		
		List param = new ArrayList();
		param.add(idNotaFiscalServico);
		
		return  getAdsmHibernateTemplate().find(query.toString(), param.toArray());
	}
	
	public String retornarIdDoctoServicosParaUtilizarNaRemocaoDoImpostoServico(final Long idMonitoramentoDocEletronic){
		StringBuilder sql = new StringBuilder();

		sql.append("select mde.id_docto_servico as idDoctoServico ").
			append("from monitoramento_doc_eletronico mde ").
			append("where mde.id_monitoramento_doc_eletronic = ").
			append(idMonitoramentoDocEletronic);

        List idDoctoServicos = getAdsmHibernateTemplate().findBySql(sql.toString(), null, new ConfigureSqlQuery() {

            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("idDoctoServico", Hibernate.LONG);
            }

        });

        if (idDoctoServicos == null || idDoctoServicos.isEmpty()) {
            return "";
        }

        return StringUtils.join(idDoctoServicos, ", ");
	}
	
	public void removeImpostoServicoByMonitoramentoDocEletronico(final String idDoctoServicos) {
		if (idDoctoServicos == null || idDoctoServicos.isEmpty()){
			return;
		}
		HibernateCallback hcb = new HibernateCallback() {
			
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder sql = new StringBuilder();
				sql.append(" delete imposto_servico ");
				sql.append(" where id_conhecimento in (").append(idDoctoServicos).append(")");
				sql.append("   or id_nota_fiscal_servico in (").append(idDoctoServicos).append(")");
				
				SQLQuery query = session.createSQLQuery(sql.toString());
				
				return query.executeUpdate();
			}
			
		};
		
		getAdsmHibernateTemplate().execute(hcb);
		
	}

}