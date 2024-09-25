package com.mercurio.lms.configuracoes.model.dao;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.LikeVarcharI18n;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.contasreceber.model.ItemDepositoCcorrente;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MoedaDAO extends BaseCrudDao<Moeda, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Moeda.class;
	}

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("moedaPais.pais", FetchMode.JOIN);
	} 
 
	/**
	 * Busca a Moeda padrão do Pais informado.
	 * @param idPais
	 * @return Moeda padrão do país
	 */
	public Moeda findMoedaPadraoByPais(Long idPais){
		DetachedCriteria dcMoeda = createDetachedCriteria();
		DetachedCriteria dcMoedaPais = dcMoeda.createCriteria("moedaPais");

		dcMoedaPais.add(Expression.and(
				Expression.eq("blIndicadorPadrao", Boolean.TRUE),
				Expression.eq("pais.id",idPais))
		);

		List<Moeda> list = findByDetachedCriteria(dcMoedaPais);
		if (!list.isEmpty()){
			Moeda moedaPadrao = list.get(0);
			getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().evict(moedaPadrao);
			return moedaPadrao;
		}
		return null;
	}

	/**
	 * Busca a Moeda mais utilizada do Pais informado.
	 * @param idPais
	 * @return Moeda mais utilizada do país
	 */
	public Moeda findMoedaMaisUtilizadaByPais(Long idPais){
		DetachedCriteria dcMoeda = createDetachedCriteria();
		DetachedCriteria dcMoedaPais = dcMoeda.createCriteria("moedaPais");

		dcMoedaPais.add(Expression.and(
				Expression.eq("blIndicadorMaisUtilizada", Boolean.TRUE),
				Expression.eq("pais.id",idPais))
		);
		List<Moeda> list = findByDetachedCriteria(dcMoedaPais);

		if (!list.isEmpty()) {
			Moeda moedaPadrao = list.get(0);
			getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().evict(moedaPadrao);
			return moedaPadrao;
		}
		return null;
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	public List<Moeda> findMoedasByPais(Map<String, Object> map) {
		DetachedCriteria dc = createDetachedCriteria();
		if (StringUtils.isNotBlank((String)map.get("dsMoeda"))){
			dc.add(LikeVarcharI18n.ilike("dsMoeda", (String)map.get("dsMoeda"), SessionContext.getUserLocale()));
		}
		dc.addOrder(Order.asc("dsSimbolo"));

		String idPais = (String) ReflectionUtils.getNestedBeanPropertyValue(map, "idPais",null);
		if ( idPais != null && !"".equals(idPais)){
			DetachedCriteria dcMoedaPais = dc.createCriteria("moedaPais", "mp");
			DetachedCriteria dcPais = dcMoedaPais.createCriteria("pais", "p");
			dcPais.add(Restrictions.eq("idPais", Long.valueOf((String)idPais) ));
			dc = dcPais;
		}
		return findByDetachedCriteria(dc);
	}

	/**
	 * Traz apenas as moedas conforme o País do usuário logado e que não estejam inativas, 
	 * tanto na tabela MOEDA quanto na MOEDA_PAIS.
	 * @param criterios Traz o id do pais e o id da moeda padrão do pais relacionados ao usuário logado
	 * @return List Lista contendo as moedas ativas do pais do usuário logado e o id da moeda padrão deste pais
	 */
	public List<Map<String, Object>> findMoedasAtivasByPaisUsuario(Map<String, Object> map) {
		Map<String, Object> hash = new Hashtable<String, Object>();
		Long idPais = (Long) map.get("idPais");
		Long idMoeda = (Long) map.get("idMoeda");

		DetachedCriteria dc = createDetachedCriteria();

		dc.setFetchMode("moedaPais", FetchMode.JOIN);

		dc.createAlias("moedaPais", "mp");

		dc.add(Restrictions.and(
				Restrictions.eq("mp.pais.idPais", idPais),
				Restrictions.eq("mp.tpSituacao","A")));
		dc.add(Restrictions.eq("tpSituacao","A"));

		List listaMoedas = super.findByDetachedCriteria(dc);

		hash.put("listaMoedas", listaMoedas);
		hash.put("moedaPadrao", idMoeda);

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(1);
		list.add(hash);

		return list;
	}

	/**
	 * Método que retorna as moedas do pais informado, ativo.
	 * 
	 * @param Long idPais
	 * @param String strAtivo
	 * @return List
	 * */
	public List<Moeda> findMoedaByPais(Long idPais, String strAtivo){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("m");
		hql.addFrom(MoedaPais.class.getName(), "mp " +
				"join mp.moeda as m " +
				"join mp.pais as p");
		hql.addCriteria("p.id","=",idPais);
		hql.addCriteria("m.tpSituacao","=",strAtivo);
		hql.addCriteria("mp.tpSituacao","=",strAtivo);

		hql.addOrderBy("m.sgMoeda");
		hql.addOrderBy("m.dsSimbolo");

		return this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	public List findListByCriteria(Map criterions, List order) {
		order = new ArrayList<String>(1);
		order.add("dsSimbolo");
		return super.findListByCriteria(criterions, order);
	}
	 
	private SqlTemplate getSQLTemplateFindMoedaOrderBySgSimbolo(boolean somenteAtivos, Long idPais) {
		SqlTemplate sqlTemplate = new SqlTemplate();
		sqlTemplate.addFrom(Moeda.class.getName(), "mo");
		sqlTemplate.addOrderBy("mo.sgMoeda");
		sqlTemplate.addOrderBy("mo.dsSimbolo");
		if (somenteAtivos) {
			sqlTemplate.addCriteria("mo.tpSituacao", "=", "A");
		}
		if (idPais!=null) {
			sqlTemplate.addCriteria("mo.moedaPais.pais.id", "=", idPais);
		}
		return sqlTemplate;
	}

	/**
	 * Método para a obtencao de moedas, ativas ou não, ordenadas por (sigla + simbolo).<br>
	 * Recomendado o uso em combos que apresentem (sigla + simbolo).
	 * @param somenteAtivos
	 * @return
	 * @author luisfco
	 */
	public List<Moeda> findMoedaOrderBySgSimbolo(boolean somenteAtivos) {
		SqlTemplate sqlTemplate = getSQLTemplateFindMoedaOrderBySgSimbolo(somenteAtivos, null);
		return getAdsmHibernateTemplate().find(sqlTemplate.getSql(), sqlTemplate.getCriteria());
	}

	/**
	 * 
	 * Informa o id do DepositoCcorrente e retorna a moeda do primeiro itemDepositoCcorrente
	 * 
	 * @author Mickaël Jalbert
	 * @since 18/05/2006
	 * 
	 * @param Long idDepositcoCcorrente
	 * @return Moeda
	 * */
	public Moeda findMoedoByDepositoCcorrente(Long idDepositcoCcorrente) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("NVL(moe1, moe2)");
		
		hql.addInnerJoin(ItemDepositoCcorrente.class.getName(), "itc");		
		hql.addLeftOuterJoin("itc.fatura", "fat");
		hql.addLeftOuterJoin("fat.moeda", "moe1");
		
		hql.addLeftOuterJoin("itc.devedorDocServFat", "dev");
		hql.addLeftOuterJoin("dev.doctoServico", "doc");
		hql.addLeftOuterJoin("doc.moeda", "moe2");
		
		hql.addCriteria("itc.depositoCcorrente.id", "=", idDepositcoCcorrente);
		
		List<Moeda> lstMoeda = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (!lstMoeda.isEmpty()) {
			 Moeda moeda = lstMoeda.get(0);
			 Hibernate.initialize(moeda);
			return moeda;
		} else {
			return null;
		}
	}

	/**
	 * Retorna a moeda padrão do pais da sigla informada
	 * 
	 * @author Mickaël Jalbert
	 * @since 18/05/2006
	 * 
	 * @param String sgPais
	 * @return Moeda
	 */
	public Moeda findMoedaPadraoBySiglaPais(String sgPais) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" moe");

		hql.addInnerJoin(MoedaPais.class.getName(), "mp");
		hql.addInnerJoin("mp.pais", "pa");
		hql.addInnerJoin("mp.moeda", "moe");

		hql.addCriteria("pa.sgPais","=",sgPais);
		hql.addCriteria("mp.blIndicadorPadrao","=",Boolean.TRUE);

		List<Moeda> lstMoeda = this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (!lstMoeda.isEmpty()){
			return lstMoeda.get(0);
		} else {
			return null;
		}
	}

	public Moeda findMoedaByNrIsoCode(Short nrIsoCode) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("nrIsoCode", nrIsoCode));

		return (Moeda) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

}