package com.mercurio.lms.configuracoes.model.dao;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.util.CollectionUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.util.AliasToNestedBeanResultTransformer;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean 
 */
public class TipoTributacaoIEDAO extends BaseCrudDao<TipoTributacaoIE, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoTributacaoIE.class;
    }
    
    /**
     * Método que inicializa os relacionamentos do pojo
     */
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tipoTributacaoIcms", FetchMode.JOIN);
		lazyFindById.put("inscricaoEstadual", FetchMode.JOIN);
		lazyFindById.put("inscricaoEstadual.pessoa", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("tipoTributacaoIcms", FetchMode.JOIN);
	} 
	
	/**
	 * Método responsável por buscar TipoTributacaoIE que estejam no mesmo
	 * intervalo de vigência
	 * 
	 * @author HectorJ
	 * @since 02/05/20006
	 * 
	 * @param vigenciaInicial
	 * @param vigenciaFinal
	 * @return List <TipoTributacaoIE>
	 */
	public List findTipoTributacaoIEByVigenciaEquals(
			YearMonthDay vigenciaInicial, YearMonthDay vigenciaFinal,
			Long idInscricaoEstadual, Long idTipoTributacaoIE) {
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("ttie");
		
		hql.addFrom(getPersistentClass().getName() + " ttie ");
		
		/** Criteria para buscar registros no mesmo intervalo de vigência */ 
		hql.addCustomCriteria("( (? between ttie.dtVigenciaInicial and ttie.dtVigenciaFinal) "
				+ " OR (? between ttie.dtVigenciaInicial and ttie.dtVigenciaFinal) "
				+ " OR (? < ttie.dtVigenciaInicial  AND ? > ttie.dtVigenciaFinal) )");
		
		hql.addCriteriaValue(vigenciaInicial);
		hql.addCriteriaValue(JTDateTimeUtils.maxYmd(vigenciaFinal));
		hql.addCriteriaValue(vigenciaInicial);
		hql.addCriteriaValue(JTDateTimeUtils.maxYmd(vigenciaFinal));
		
		hql.addCriteria("ttie.inscricaoEstadual.idInscricaoEstadual", "=",
				idInscricaoEstadual);
		hql.addCriteria("ttie.idTipoTributacaoIE", "<>", idTipoTributacaoIE);
		
		List lst = getAdsmHibernateTemplate().find(hql.getSql(),
				hql.getCriteria());
		
		return lst;
	}

	/**
	 * retorna os tipos de tributação de inscrição estadual que sem data final
	 * de vigência
	 * 
	 * @param idInscricaoEstadual
	 *            - o ni da inscrição estadual
	 * @author gustavo silva
	 * @since 23/01/2013
	 * @return
	 */
	public List findByDtVigenciaFinalNula(Long idInscricaoEstadual){
		
		SqlTemplate hql = new SqlTemplate();
				
		hql.addFrom(getPersistentClass().getName() + " ttie ");
		hql.addCriteria("ttie.inscricaoEstadual.idInscricaoEstadual", "=",
				idInscricaoEstadual);
		hql.addCriteria("ttie.dtVigenciaFinal", "=", new YearMonthDay(
				new GregorianCalendar(4000, Calendar.JANUARY, 1).getTime()
						.getTime()));
		
		List lst = getAdsmHibernateTemplate().find(hql.getSql(),
				hql.getCriteria());
		
		return lst;
	}

	/**
	 * @author José Rodrigo Moraes
	 * @since 02/06/2006
	 * 
	 * Busca os tipos Tributação IE vigentes na data passada
	 * 
	 * @param idInscricaoEstadual
	 *            Identificador da Inscrição Estadual associada ao tipo de
	 *            Tributação
	 * @param data
	 *            Data para teste de vigência
	 * @param vigenciaFutura
	 *            Booleano que indica se o teste deve ser feito para vigências
	 *            futuras <br>
	 * <br>
	 *                       <code>true</code>  Para vigência futura e<br> 
	 *                       <code>false</code>  Para vigência na data atual 
	 * @return Lista de Tipos Tributação IE
	 */
	public List findTiposTributacaoIEVigente(Long idInscricaoEstadual,
			YearMonthDay data, Boolean vigenciaFutura) {

		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("ttie");		
		hql.addInnerJoin(getPersistentClass().getName() + " ttie ");
		hql.addInnerJoin("ttie.inscricaoEstadual","ie");

		hql.addCriteria("ie.id","=",idInscricaoEstadual);

		if( vigenciaFutura.booleanValue() ){
			hql.addCustomCriteria("ttie.dtVigenciaInicial > ?");			
		} else {
			hql.addCustomCriteria("? between ttie.dtVigenciaInicial and ttie.dtVigenciaFinal");
		}

		hql.addCriteriaValue(data);
		hql.addOrderBy("ttie.dtVigenciaInicial");		

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

	}

	public List findVigentesByIdPessoa(Long idPessoa) {
		YearMonthDay dtVigencia = JTDateTimeUtils.getDataAtual();

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("ttie.tpSituacaoTributaria"),
				"tpSituacaoTributaria");
		projectionList.add(Projections.property("ie.idInscricaoEstadual"),
				"inscricaoEstadual.idInscricaoEstadual");
		projectionList.add(Projections.property("ie.nrInscricaoEstadual"),
				"inscricaoEstadual.nrInscricaoEstadual");
		projectionList.add(Projections.property("ie.blIndicadorPadrao"),
				"inscricaoEstadual.blIndicadorPadrao");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),
				"ttie");
		dc.setProjection(projectionList);
		dc.createAlias("ttie.inscricaoEstadual", "ie");

		dc.add(Restrictions.eq("ie.tpSituacao", "A"));
		dc.add(Restrictions.eq("ie.pessoa.id", idPessoa));
		dc.add(Restrictions.le("ttie.dtVigenciaInicial", dtVigencia));
		dc.add(Restrictions.ge("ttie.dtVigenciaFinal", dtVigencia));
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(
				getPersistentClass()));

		return findByDetachedCriteria(dc);
	}
	
	public TipoTributacaoIE findByIdPessoaAndDtVigenciaAndTpSituacaoTributaria(
			Long idPessoa, YearMonthDay dtVigencia,
			List<String> listTpSituacaoTributaria) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT tti ")
				.append("FROM ")
				.append(TipoTributacaoIE.class.getSimpleName())
				.append(" tti ")
		.append("JOIN tti.inscricaoEstadual ie ")
		.append("JOIN ie.pessoa p ")
		.append("WHERE ")
		.append("	 tti.tpSituacaoTributaria in(:listTpSituacaoTributaria) ")
		.append("AND tti.dtVigenciaInicial <=:dtVigencia ")
		.append("AND tti.dtVigenciaFinal >=:dtVigencia ")
		.append("AND ie.tpSituacao =:tpSituacao ")
				.append("AND p.idPessoa =:idPessoa ");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		parameters.put("listTpSituacaoTributaria", listTpSituacaoTributaria);
		parameters.put("dtVigencia", dtVigencia);
		parameters.put("tpSituacao", "A");
		parameters.put("idPessoa", idPessoa);
		
		List<TipoTributacaoIE> l = getAdsmHibernateTemplate().findByNamedParam(
				sql.toString(), parameters);
		
		if (!CollectionUtils.isEmpty(l)) {
			return l.get(0);
		}
		
		return null;
	}
	
	/**
	 * 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/09/2006
	 *
	 * @param idInscricaoEstadual
	 * @param idTipoTributacaoIE
	 * @param data
	 * @return
	 *
	 */
	public List findTiposTributacaoIEVigente(Long idInscricaoEstadual,
			Long idTipoTributacaoIE, YearMonthDay data) {

		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("ttie");		
		hql.addInnerJoin(getPersistentClass().getName() + " ttie ");
		hql.addInnerJoin("ttie.inscricaoEstadual","ie");

		hql.addCriteria("ie.id","=",idInscricaoEstadual);
		hql.addCriteria("ttie.idTipoTributacaoIE", "<>", idTipoTributacaoIE);
		hql.addCustomCriteria("? between ttie.dtVigenciaInicial and ttie.dtVigenciaFinal");

		hql.addCriteriaValue(data);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

	}

	/***
	 * @author Gabriel.Scossi
	 * @since 15/02/2016 Busca os tipos de situação tributária passados por
	 *        parâmetro com base em uma inscrição estadual e que esteja ativa.
	 * @param idIE
	 *            Inscrição Estadual
	 * @param dtVigencia
	 * @param listTpSituacaoTributaria
	 *            lista contendo os tipos de situações tributárias
	 * @return TipoTributacaoIE
	 */
	public TipoTributacaoIE findByIdIEAndDtVigenciaAndTpSituacaoTributaria(
			Long idIE, YearMonthDay dtVigencia,
			List<String> listTpSituacaoTributaria) {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT tti ")
				.append("FROM ")
				.append(TipoTributacaoIE.class.getSimpleName())
				.append(" tti ")
				.append("WHERE ")
				.append("	 tti.tpSituacaoTributaria in(:listTpSituacaoTributaria) ")
				.append("AND tti.dtVigenciaInicial <=:dtVigencia ")
				.append("AND tti.dtVigenciaFinal >=:dtVigencia ")
				.append("AND tti.inscricaoEstadual.idInscricaoEstadual =:ie ")

		;

		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("listTpSituacaoTributaria", listTpSituacaoTributaria);
		parameters.put("dtVigencia", dtVigencia);
		parameters.put("ie", idIE);

		List<TipoTributacaoIE> l = getAdsmHibernateTemplate().findByNamedParam(
				sql.toString(), parameters);

		if (!CollectionUtils.isEmpty(l)) {
			return l.get(0);
		}

		return null;
	}

}
