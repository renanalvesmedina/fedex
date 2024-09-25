package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.contasreceber.model.LoteCheque;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LoteChequeDAO extends BaseCrudDao<LoteCheque, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return LoteCheque.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("redeco", FetchMode.JOIN);
		lazyFindById.put("redeco.filial", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	public void removeById(Long id) {
		super.removeById(id, true);
	}
	
	public List findLookupLoteCheque(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" lc ");

		sql.addFrom(this.getPersistentClass().getName(), " lc " );

		sql.addCriteria("lc.nrLoteCheque", "=", criteria.get("nrLoteCheque"));

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	/**
	 * Monta combo de moeda da tela
	 * @return List combo de moedas conforme pais do usuario logado
	 */
	public List findComboMoeda(){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(mp.id as idMoedaPais, m.sgMoeda || ' ' || m.dsSimbolo as dsSimbolo)");
		hql.addFrom(MoedaPais.class.getName(), "mp " +
				"join mp.moeda as m ");

		hql.addCriteria("mp.pais.id", "=", SessionUtils.getPaisSessao().getIdPais());
		hql.addCriteria("mp.tpSituacao", "=", "A");
		hql.addCriteria("m.tpSituacao", "=", "A");

		hql.addOrderBy("m.dsSimbolo");

		return this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

}