package com.mercurio.lms.vendas.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.vendas.model.DiaFaturamento;
import com.mercurio.lms.vendas.util.ConstantesVendas;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DiaFaturamentoDAO extends BaseCrudDao<DiaFaturamento, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DiaFaturamento.class;
    }

    protected void initFindByIdLazyProperties(Map map) {
    	map.put("servico", FetchMode.JOIN);
    }
    
    /**
     * Retorna a lista de dia de faturamento da divisao informada no tipo de periodicidade informado
     * 
     * @author Mickaël Jalbert
     * @since 06/10/2006
     * 
     * @param Long idDivisao
     * @param String tpPeriodicidade
     * @return DiaFaturamento
     */
    public List findByDivisaoTpPeriodicidade(Long idDivisao, String tpPeriodicidade){
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("df");
    	hql.addInnerJoin(DiaFaturamento.class.getName(), "df");
    	hql.addCriteria("df.divisaoCliente.id", "=", idDivisao);
    	hql.addCriteria("df.tpPeriodicidade", "=", tpPeriodicidade);
    	return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }    

    /**
     * Solicitação CQPRO00005945/CQPRO00007478 da Integração.
     * @param tpModal
     * @param tpPeriodicidade
     * @param idDivisaoCliente
     * @return
     */
    public DiaFaturamento findDiaFaturamento(Long idDivisaoCliente, String tpModal) {
    	DetachedCriteria dc = DetachedCriteria.forClass(DiaFaturamento.class, "df");
    	dc.add(Restrictions.eq("df.divisaoCliente.id", idDivisaoCliente));
    	dc.add(Restrictions.eq("df.tpModal", tpModal));

    	ResultSetPage rsp = findPaginatedByDetachedCriteria(dc, IntegerUtils.ONE, IntegerUtils.ONE);
    	if(rsp.getList().isEmpty()) {
    		return null;
    	}
    	return (DiaFaturamento) rsp.getList().get(0);
    }
    
    /**
     * Retorna os Dias de Faturamento do Cliente
     * @author Andre Valadas
     * @param idCliente
     * @return
     */
    public List<DiaFaturamento> findDiaFaturamento(Long idCliente) {
    	DetachedCriteria dc = DetachedCriteria.forClass(DiaFaturamento.class, "df");
    	dc.createAlias("df.divisaoCliente", "dc");
    	dc.createAlias("dc.cliente", "c");

    	dc.add(Restrictions.eq("c.id", idCliente));
    	dc.add(Restrictions.eq("dc.tpSituacao", ConstantesVendas.SITUACAO_ATIVO));

    	return findByDetachedCriteria(dc);
    }

    
    /**
     * Retorna os Dias de Faturamento do Cliente
     * @author Andre Valadas
     * @param idCliente
     * @return
     */
    public List<DiaFaturamento> findDiaFaturamentoByDivisao(Long idDivisaoCliente) {
    	DetachedCriteria dc = DetachedCriteria.forClass(DiaFaturamento.class, "df");
    	dc.setFetchMode("servico", FetchMode.JOIN);
    	dc.createAlias("df.divisaoCliente", "dc");
    	dc.add(Restrictions.eq("dc.id", idDivisaoCliente));
    	dc.add(Restrictions.eq("dc.tpSituacao", ConstantesVendas.SITUACAO_ATIVO));
    	return findByDetachedCriteria(dc);
    }
    
    /**
     * Retorna os dias de  faturamento que possuem tpPeriodicidadeSolicitado
     * não nulo
     * 
     * @param idCliente
     * @return
     */
    public List<TypedFlatMap> findDiaFaturamentoSolicitado(Long idCliente) {
    	
    	ProjectionList pl = Projections.projectionList()
		.add(Projections.property("df.tpPeriodicidadeSolicitado"), "tpPeriodicidadeSolicitado");
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(DiaFaturamento.class, "df");
    	dc.setProjection(pl);
    	dc.createAlias("df.divisaoCliente", "dc");
    	dc.createAlias("dc.cliente", "c");    	
    	dc.add(Restrictions.eq("c.id", idCliente));
    	dc.add(Restrictions.eq("dc.tpSituacao", ConstantesVendas.SITUACAO_ATIVO));
    	dc.add(Restrictions.isNotNull("df.tpPeriodicidadeSolicitado"));
    	
    	dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
    }

    /**
     * Retorna uma lista mapeada com os Dias de Faturamento do Cliente
     * @author Andre Valadas
     * @param idCliente
     * @return
     */
    public List<TypedFlatMap> findDiaFaturamentoMapped(Long idCliente) {
    	ProjectionList pl = Projections.projectionList()
		.add(Projections.property("df.id"), "idDiaFaturamento")
		.add(Projections.property("df.tpFrete"), "tpFrete")
		.add(Projections.property("df.tpModal"), "tpModal")
		.add(Projections.property("df.tpAbrangencia"), "tpAbrangencia")
		.add(Projections.property("df.tpPeriodicidade"), "tpPeriodicidade")
		.add(Projections.property("df.tpPeriodicidadeSolicitado"), "tpPeriodicidadeSolicitado")
		.add(Projections.property("df.tpPeriodicidadeAprovado"), "tpPeriodicidadeAprovado")
		.add(Projections.property("df.tpDiaSemana"), "tpDiaSemana")
		.add(Projections.property("df.nrDiaFaturamento"), "nrDiaFaturamento")
		/** Divisao */
		.add(Projections.property("dc.id"), "divisaoCliente_idDivisaoCliente")
		.add(Projections.property("dc.dsDivisaoCliente"), "divisaoCliente_dsDivisaoCliente")
		/** Servico */
		.add(Projections.property("s.id"), "servico_idServico")
		.add(Projections.property("s.dsServico"), "servico_dsServico");

    	DetachedCriteria dc = DetachedCriteria.forClass(DiaFaturamento.class, "df");
    	dc.setProjection(pl);
    	dc.createAlias("df.divisaoCliente", "dc");
    	dc.createAlias("dc.cliente", "c");
    	dc.createAlias("df.servico", "s", CriteriaSpecification.LEFT_JOIN);

    	dc.add(Restrictions.eq("c.id", idCliente));
    	dc.add(Restrictions.eq("dc.tpSituacao", ConstantesVendas.SITUACAO_ATIVO));

    	dc.addOrder(Order.asc("dc.dsDivisaoCliente"));
    	dc.addOrder(Order.asc("df.tpModal"));
    	dc.addOrder(Order.asc("df.tpAbrangencia"));
    	dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
    }
    
    
    
    public TypedFlatMap findTabelaByIdServico(Long idServico) {
    	final StringBuilder sql = new StringBuilder();
    	sql.append("select P.ID_TABELA_PRECO,T.TP_TIPO_TABELA_PRECO,T.NR_VERSAO,S.TP_SUBTIPO_TABELA_PRECO,P.DS_DESCRICAO")
			.append("	from TABELA_PRECO P, TIPO_TABELA_PRECO T, SUBTIPO_TABELA_PRECO S")
			.append("	where P.ID_TIPO_TABELA_PRECO = T.ID_TIPO_TABELA_PRECO")
			.append("	and (T.TP_TIPO_TABELA_PRECO = 'M' or T.TP_TIPO_TABELA_PRECO = 'T' or T.TP_TIPO_TABELA_PRECO = 'A')")
			.append("	and P.ID_SUBTIPO_TABELA_PRECO = S.ID_SUBTIPO_TABELA_PRECO")
			.append("	and S.TP_SUBTIPO_TABELA_PRECO = 'X'")
			.append("	and T.ID_SERVICO = "+idServico)
			.append("	and P.BL_EFETIVADA = 'S'")
			.append("	and current_date between P.DT_VIGENCIA_INICIAL and P.DT_VIGENCIA_FINAL");
	
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_TABELA_PRECO",Hibernate.LONG);
				sqlQuery.addScalar("TP_TIPO_TABELA_PRECO",Hibernate.STRING);
				sqlQuery.addScalar("NR_VERSAO",Hibernate.INTEGER);
				sqlQuery.addScalar("TP_SUBTIPO_TABELA_PRECO",Hibernate.STRING);
				sqlQuery.addScalar("DS_DESCRICAO",Hibernate.STRING);
			}
		};

    	final HibernateCallback hcb = new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
            	csq.configQuery(query);
				return query.list();
			}
		};
		TypedFlatMap map = new TypedFlatMap();
		List<TypedFlatMap[]> list = getHibernateTemplate().executeFind(hcb);
		if(list!=null && list.size()>0){
			Object[] obj = list.get(0);
			map.put("ID_TABELA_PRECO", obj[0]);
			map.put("TP_TIPO_TABELA_PRECO", obj[1]);
			map.put("NR_VERSAO", obj[2]);
			map.put("TP_SUBTIPO_TABELA_PRECO", obj[3]);
			map.put("DS_DESCRICAO", obj[4]);
		}
		
		return map;
	}
    
    
    
}