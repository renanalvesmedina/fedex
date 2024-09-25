package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contasreceber.model.param.DataVencimentoParam;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.vendas.model.PrazoVencimento;
import com.mercurio.lms.vendas.util.ConstantesVendas;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PrazoVencimentoDAO extends BaseCrudDao<PrazoVencimento, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PrazoVencimento.class;
    }

    protected void initFindByIdLazyProperties(Map map) {
    	map.put("servico", FetchMode.JOIN);
    	map.put("divisaoCliente", FetchMode.JOIN);
    }

    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put("servico", FetchMode.JOIN);
    	map.put("divisaoCliente", FetchMode.JOIN);
    }

    protected void initFindListLazyProperties(Map map) {
    	map.put("servico", FetchMode.JOIN);
    	map.put("divisaoCliente", FetchMode.JOIN);
    }

    public void storeBasic(PrazoVencimento bean) {
		super.store(bean);
	}

    public ResultSetPage findPaginated(Map criteria,FindDefinition def) {
    	StringBuilder from = new StringBuilder();
    	from.append(PrazoVencimento.class.getName()).append(" as dv")
    		.append(" left join fetch dv.servico").append(" as s")
    		.append(" left join fetch dv.divisaoCliente").append(" as dc");
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(from.toString()) ;

    	Long idDivisaoCliente = MapUtilsPlus.getLongOnMap(criteria, "divisaoCliente", "idDivisaoCliente");
    	if (idDivisaoCliente != null) {
    		sql.addCriteria("dc.idDivisaoCliente","=", idDivisaoCliente);
    	}
    	Long idServico = MapUtilsPlus.getLongOnMap(criteria, "servico", "idServico");
    	if (idServico != null) {
    		sql.addCriteria("s.idServico","=", idServico);
    	}
    	String tpModal = MapUtils.getString(criteria, "tpModal");
    	if (StringUtils.isNotBlank(tpModal)) {
    		sql.addCriteria("dv.tpModal","=", tpModal);
    	}
    	String tpAbrangencia = MapUtils.getString(criteria, "tpAbrangencia");
    	if (StringUtils.isNotBlank(tpAbrangencia)) {
    		sql.addCriteria("dv.tpAbrangencia","=", tpAbrangencia);
    	}
    	String tpFrete = MapUtils.getString(criteria, "tpFrete");
    	if (StringUtils.isNotBlank(tpFrete)) {
    		sql.addCriteria("dv.tpFrete","=", tpFrete);
    	}
        sql.addOrderBy("dv.tpModal");
    	sql.addOrderBy("dv.tpAbrangencia");
    	sql.addOrderBy(OrderVarcharI18n.hqlOrder("s.dsServico", LocaleContextHolder.getLocale()));
    	sql.addOrderBy("dv.tpFrete");
    	sql.addOrderBy("dv.nrPrazoPagamento");

        return getAdsmHibernateTemplate().findPaginated(sql.getSql(),def.getCurrentPage(),def.getPageSize(),sql.getCriteria());  
    }

    /**
     * Solicitação CQPRO00005946/CQPRO00007479 da Integração.
     * @param tpFrete
     * @param tpModal
     * @param nrPrazoPagamento
     * @param idDivisaoCliente
     * @return
     */
    public PrazoVencimento findPrazoVencimento(Long idDivisaoCliente, String tpFrete, String tpModal) {
    	DetachedCriteria dc = DetachedCriteria.forClass(PrazoVencimento.class, "pv");
    	dc.add(Restrictions.eq("pv.divisaoCliente.id", idDivisaoCliente));
    	dc.add(Restrictions.eq("pv.tpModal", tpModal));
    	if(StringUtils.isNotBlank(tpFrete)) {
    		dc.add(Restrictions.eq("pv.tpFrete", tpFrete));
    	}

    	ResultSetPage rsp = findPaginatedByDetachedCriteria(dc, IntegerUtils.ONE, IntegerUtils.ONE);
    	if(rsp.getList().isEmpty()) {
    		return null;
    	}
    	return (PrazoVencimento) rsp.getList().get(0);
    }

    /**
     * Retorna os Prazos de Vencimento do Cliente
     * @author Andre Valadas
     * @param idCliente
     * @return
     */
    public List<PrazoVencimento> findPrazoVencimento(Long idCliente) {
    	DetachedCriteria dc = DetachedCriteria.forClass(PrazoVencimento.class, "pv");
    	dc.createAlias("pv.divisaoCliente", "dc");
    	dc.createAlias("dc.cliente", "c");

    	dc.add(Restrictions.eq("c.id", idCliente));
    	dc.add(Restrictions.eq("dc.tpSituacao", ConstantesVendas.SITUACAO_ATIVO));

    	return findByDetachedCriteria(dc);
    }

    /**
     * Retorna a lista de PrazoVencimento que possuem nrPrazoPagamentoSolicitado diferente de nulo
     * @param idCliente
     * @return
     */
	 public  List<TypedFlatMap> findPrazoVencimentoSolicitado(Long idCliente) {
	    	
    	ProjectionList pl = Projections.projectionList()
		.add(Projections.property("pv.nrPrazoPagamentoSolicitado"), "nrPrazoPagamentoSolicitado");
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(PrazoVencimento.class, "pv");
    	dc.setProjection(pl);
    	dc.createAlias("pv.divisaoCliente", "dc");
    	dc.createAlias("dc.cliente", "c");    	
    	dc.add(Restrictions.eq("c.id", idCliente));
    	dc.add(Restrictions.eq("dc.tpSituacao", ConstantesVendas.SITUACAO_ATIVO));
    	dc.add(Restrictions.isNotNull("pv.nrPrazoPagamentoSolicitado"));
    	
    	dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	 }
    
    /**
     * Retorna uma lista mapeada com os Prazos de Vencimento do Cliente
     * @author Andre Valadas
     * @param idCliente
     * @return
     */
    public List<TypedFlatMap> findPrazoVencimentoMapped(Long idCliente) {
    	ProjectionList pl = Projections.projectionList()
		.add(Projections.property("pv.id"), "idPrazoVencimento")
		.add(Projections.property("pv.tpFrete"), "tpFrete")
		.add(Projections.property("pv.tpModal"), "tpModal")
		.add(Projections.property("pv.tpAbrangencia"), "tpAbrangencia")
		.add(Projections.property("pv.nrPrazoPagamento"), "nrPrazoPagamento")
		.add(Projections.property("pv.nrPrazoPagamentoSolicitado"), "nrPrazoPagamentoSolicitado")
		.add(Projections.property("pv.nrPrazoPagamentoAprovado"), "nrPrazoPagamentoAprovado")
		/** Divisao */
		.add(Projections.property("dc.id"), "divisaoCliente_idDivisaoCliente")
		.add(Projections.property("dc.dsDivisaoCliente"), "divisaoCliente_dsDivisaoCliente")
		/** Servico */
		.add(Projections.property("s.id"), "servico_idServico")
		.add(Projections.property("s.dsServico"), "servico_dsServico");

    	DetachedCriteria dc = DetachedCriteria.forClass(PrazoVencimento.class, "pv");
    	dc.setProjection(pl);
    	dc.createAlias("pv.divisaoCliente", "dc");
    	dc.createAlias("dc.cliente", "c");
    	dc.createAlias("pv.servico", "s", CriteriaSpecification.LEFT_JOIN);

    	dc.add(Restrictions.eq("c.id", idCliente));
    	dc.add(Restrictions.eq("dc.tpSituacao", ConstantesVendas.SITUACAO_ATIVO));

    	dc.addOrder(Order.asc("dc.dsDivisaoCliente"));
    	dc.addOrder(Order.asc("pv.tpModal"));
    	dc.addOrder(Order.asc("pv.tpAbrangencia"));
    	dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
    }

	/**
	 * Retorna a lista de filial centralazadora da filial informada por tpModal e tpAbrangencia.
	 * @return List
	 * */
    public Object[] findPrazoVencimentoByCriteria(DataVencimentoParam dataVencimentoParam){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("PV.ID_PRAZO_VENCIMENTO", "ID_PRAZO_VENCIMENTO");
		sql.addProjection("PV.NR_PRAZO_PAGAMENTO", "NR_PRAZO_PAGAMENTO");
		sql.addProjection("PV.TP_DIA_SEMANA_VENCIMENTO", "TP_DIA_SEMANA_VENCIMENTO");

		sql.addFrom("PRAZO_VENCIMENTO","PV");

		sql.addCriteria("PV.ID_DIVISAO_CLIENTE","=",dataVencimentoParam.getIdDivisao());
		sql.addCriteria("PV.TP_MODAL","=",dataVencimentoParam.getTpModal());
		sql.addCustomCriteria("(PV.TP_ABRANGENCIA = ? OR PV.TP_ABRANGENCIA IS NULL)", dataVencimentoParam.getTpAbrangencia());
		sql.addCustomCriteria("(PV.TP_FRETE = ? OR PV.TP_FRETE IS NULL)", dataVencimentoParam.getTpFrete());
		sql.addCustomCriteria("(PV.ID_SERVICO = ? OR PV.ID_SERVICO IS NULL)", dataVencimentoParam.getIdServico());

		sql.addOrderBy("PV.ID_SERVICO", "ASC");
		sql.addOrderBy("PV.TP_FRETE", "ASC");
		sql.addOrderBy("PV.TP_ABRANGENCIA", "ASC");
		sql.addOrderBy("PV.TP_MODAL", "ASC");

		ConfigureSqlQuery csq = new ConfigureSqlQuery(){
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_PRAZO_VENCIMENTO", Hibernate.LONG);
				sqlQuery.addScalar("NR_PRAZO_PAGAMENTO", Hibernate.LONG);
				sqlQuery.addScalar("TP_DIA_SEMANA_VENCIMENTO", Hibernate.LONG);
			}
		};
		return (Object[])this.getAdsmHibernateTemplate().findByIdBySql("SELECT * FROM (" + sql.getSql() + ") WHERE ROWNUM < 2", sql.getCriteria(), csq);    	    	
    }	   

	/**
	 * Retorna o PrazoVencimento da divisão informada onde os outros campos são nulos.
	 * 
	 * @param Long idDivisaoCliente
	 * @return List
	 * */    
    public List findPrazoVencimentoPadrao(Long idDivisao){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("di");
		hql.addFrom(PrazoVencimento.class.getName(),"di");

		hql.addCustomCriteria("di.servico.id IS NULL");
		hql.addCustomCriteria("di.tpModal IS NULL");
		hql.addCustomCriteria("di.tpAbrangencia IS NULL");
		hql.addCustomCriteria("di.tpFrete IS NULL");

		hql.addCriteria("di.divisaoCliente.id","=",idDivisao);

		return this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());    	    	
    }	    

    public PrazoVencimento findById(java.lang.Long id) {
    	StringBuilder hql = new StringBuilder();
    	hql.append("from	").append(PrazoVencimento.class.getName()).append(" as pv\n");
    	hql.append("join 	fetch pv.divisaoCliente as pvdc\n");
    	hql.append("left	join fetch pv.servico as pvs\n");
    	hql.append("left	join fetch pv.diasVencimento as pvdv\n");
    	hql.append("where	pv.id = :id");
    	Map criteria = new HashMap(1);
    	criteria.put("id", id);

   		return (PrazoVencimento)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), criteria);
    }
}