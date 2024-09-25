package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.municipios.model.Rota;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RotaDAO extends BaseCrudDao<Rota, Long> {

	public Integer getRowCountRotasViagem(TypedFlatMap criteria) {
		Object[] obj = montaQuery(criteria);
		Integer contador = getAdsmHibernateTemplate().getRowCountForQuery((String)obj[0],(Object[])obj[1]);
		return contador;
	}
	
	public Integer getRowCountRotaViagemEventual(TypedFlatMap criteria) {
		Object[] obj = montaQueryRotaViagemEventual(criteria);
		Integer contador = getAdsmHibernateTemplate().getRowCountForQuery((String)obj[0],(Object[])obj[1]);
		return contador;
	}

	public ResultSetPage findPaginatedRotasViagem(TypedFlatMap criteria, FindDefinition findDef) {
		Object[] obj = montaQuery(criteria);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated((String)obj[0],findDef.getCurrentPage(),findDef.getPageSize(),(Object[])obj[1]);
		return rsp;
	}
	
	public ResultSetPage findPaginatedRotaViagemEventual(TypedFlatMap criteria, FindDefinition findDef) {
		Object[] obj = montaQueryRotaViagemEventual(criteria);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated((String)obj[0],findDef.getCurrentPage(),findDef.getPageSize(),(Object[])obj[1]);
		return rsp;
	}

	private Object[] montaQuery(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(Rota.class.getName() + " as rt ");

		String dsRota = criteria.getString("dsRota").toUpperCase();
		if(StringUtils.isNotBlank(dsRota)) {
			criteria.put("dsRota", dsRota);
		}
		hql.addCriteria("upper(rt.dsRota)", "like", criteria.getString("dsRotaConcatenada"));	
		hql.addCriteria("upper(rt.dsRota)", "like", criteria.getString("dsRota"));
		hql.addCriteria("upper(rt.dsRota)", "like", criteria.getString("filialOrigem"));
		hql.addCriteria("upper(rt.dsRota)", "like", criteria.getString("filialDestino"));
		hql.addOrderBy("rt.dsRota");

		return new Object[]{hql.getSql(), hql.getCriteria()};
	}
	
	private Object[] montaQueryRotaViagemEventual(TypedFlatMap criteria) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(Rota.class.getName() + " as rt");

		String dsRota = criteria.getString("dsRota").toUpperCase();
		if(StringUtils.isNotBlank(dsRota)) {
			criteria.put("dsRota", dsRota);
		}
		hql.addCriteria("upper(rt.dsRota)", "like", criteria.getString("dsRotaConcatenada"));	
		hql.addCriteria("upper(rt.dsRota)", "like", criteria.getString("dsRota"));
		hql.addCriteria("upper(rt.dsRota)", "like", criteria.getString("filialOrigem"));
		hql.addCriteria("upper(rt.dsRota)", "like", criteria.getString("filialDestino"));
		StringBuilder sb = new StringBuilder("exists (")
			.append(" from ").append(SolicitacaoContratacao.class.getName()).append(" sc")
			.append(" where sc.rota.id = rt.id ")
			.append(" and sc.tpRotaSolicitacao = 'EV' ")
			.append(" and sc.tpSituacaoContratacao in ('AN', 'AP')")
			.append(")");
		hql.addCustomCriteria(sb.toString());
		hql.addOrderBy("rt.dsRota");

		return new Object[]{hql.getSql(), hql.getCriteria()};
	}

	public Rota findByIdVigenteEm(Long idRota, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("id",idRota));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigenciaFinal)));
		return (Rota)dc.getExecutableCriteria(getSession()).uniqueResult();
	}
	
	protected void initFindByIdLazyProperties(Map map) {
		map.put("empresa", FetchMode.JOIN);
		map.put("empresa.pessoa", FetchMode.JOIN);
		map.put("filialRotas", FetchMode.SELECT);
	}

	protected void initFindLookupLazyProperties(Map map) {
		map.put("empresa", FetchMode.JOIN);
		map.put("empresa.pessoa", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("empresa", FetchMode.JOIN);
		map.put("empresa.pessoa", FetchMode.JOIN);
		map.put("filialRotas", FetchMode.JOIN);
		
	}
	
	/**
	 * Retorna id da Rota com a descrição informada.
	 * 
	 * @author Felipe Ferreira
	 * @param dsRota descrição da rota com filiais separadas por hífen.
	 * @return id se encontrar, nulo, caso negativo.
	 */
	public Long findIdRotaByDescricao(String dsRota) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.setProjection(Projections.property("idRota"));
		dc.add(Restrictions.like("dsRota",dsRota,MatchMode.EXACT));

		List result = (List)getAdsmHibernateTemplate().findByDetachedCriteria(dc);

		return (result.size() > 0) ? (Long)result.get(0) : null;
	}
	
	/**
	 * Retorna uma entidade com os parâmetros informados.
	 * 
	 * @author Felipe Ferreira
	 * @param dsRota descrição da rota com filiais separadas por hífen.
	 * @param blEnvolveParceira true se a rota deve envolver parceira.
	 * @return Rota se encontrar, nulo, caso negativo.
	 */
	public List<Rota> find(String dsRota, Boolean blEnvolveParceira) {
		if (dsRota == null || blEnvolveParceira == null) {
			throw new IllegalArgumentException("Os argumentos são obrigatórios.");
		}
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
		dc.add(Restrictions.eq("dsRota",dsRota));
		dc.add(Restrictions.eq("blEnvolveParceira",blEnvolveParceira));
		
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public Rota findByIdRota(Long idRota){
		DetachedCriteria dc= createDetachedCriteria();
		dc.add(Restrictions.eq("idRota",idRota));
		return (Rota)findByDetachedCriteria(dc).get(0);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Rota.class;
	}

	//busca o maxLenght de rota
	public List findMaxIdRota(){
		DetachedCriteria dc = createDetachedCriteria();
		dc.setProjection(Projections.max("idRota"));
		return findByDetachedCriteria(dc);
	}

	/**
	 * @param idRota (required)
	 * @return 
	 */
	public Rota findToTrechosById(Long idRota) {
		StringBuffer hql = new StringBuffer();
		hql.append(" select rota from " + Rota.class.getName() + " as rota ")
			.append("left join fetch rota.filialRotas filialRota ")
			.append("left join fetch filialRota.filial filial ")
			.append("where rota.id = ? ")
			.append("order by filialRota.nrOrdem ");

		List parameters = new ArrayList();
		parameters.add(idRota);
		List result = getAdsmHibernateTemplate().find(hql.toString(),parameters.toArray());
		return (Rota)result.get(0);
	}
}