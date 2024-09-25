package com.mercurio.lms.configuracoes.model.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.CotacaoMoeda;
import com.mercurio.lms.configuracoes.model.MoedaPais;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CotacaoMoedaDAO extends BaseCrudDao<CotacaoMoeda, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return CotacaoMoeda.class;
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindByIdLazyProperties(java.util.Map)
	 */
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("moedaPais", FetchMode.SELECT);
		lazyFindById.put("moedaPais.pais", FetchMode.SELECT);
		lazyFindById.put("moedaPais.moeda", FetchMode.SELECT);
	}
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindPaginatedLazyProperties(java.util.Map)
	 */
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("moedaPais", FetchMode.SELECT);
		lazyFindPaginated.put("moedaPais.pais", FetchMode.SELECT);
		lazyFindPaginated.put("moedaPais.moeda", FetchMode.SELECT);
	}
	
	/**
	 * Retorna a cotação de uma data especifica para facilitar cálculos.
	 *
	 * @param Long idPais
	 * @param Long idMoedaOrigem
	 * @param Long idMoedaDestino
	 * @param YearMonthDay dtCotacao
	 * @return List
	 */
	public List findVlCotacaoMoedaByPaisMoeda(Long idPais, Long idMoedaOrigem, Long idMoedaDestino, YearMonthDay dtCotacao){	
		SqlTemplate sql = mountSqlFindCotacao(idPais, idMoedaDestino, dtCotacao);
		sql.addFrom(MoedaPais.class.getName()+ " mpo");
		sql.addJoin("mpo.pais","mpd.pais");
		sql.addCriteria("mpo.moeda.id","=",idMoedaOrigem);
		sql.addCustomCriteria("(mpo.blIndicadorPadrao = ? OR mpo.blIndicadorMaisUtilizada = ?)");
		sql.addCriteriaValue(Boolean.TRUE);
		sql.addCriteriaValue(Boolean.TRUE); 

		return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Retorna o valor da cotação da MoedaPais informada
	 * 
	 * @author Mickaël Jalbert
	 * @since 02/10/2006
	 * 
	 * @param Long idMoedaPais
	 * @param YearMonthDay dtCotacao
	 * 
	 * @return BigDecimal
	 */
	public List findVlCotacaoByMoedaPais(Long idMoedaPais, YearMonthDay dtCotacao){	
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("cm.vlCotacaoMoeda");
		sql.addInnerJoin(MoedaPais.class.getName(), "mpd");
		sql.addInnerJoin("mpd.cotacaoMoedas", "cm");
		sql.addCriteria("mpd.id", "=", idMoedaPais);
		sql.addCriteria("cm.dtCotacaoMoeda", "=", dtCotacao); 	

		return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Retorna a cotação de uma data especifica para facilitar cálculos.
	 *
	 * @param Long idPais
	 * @param Long idMoeda
	 * @param YearMonthDay dtCotacao
	 * @return List
	 */
	public List findVlCotacaoMoedaByPaisMoeda(Long idPais, Long idMoeda, YearMonthDay dtCotacao){	
		SqlTemplate sql = mountSqlFindCotacao(idPais, idMoeda, dtCotacao);
		return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	private SqlTemplate mountSqlFindCotacao(Long idPais, Long idMoeda, YearMonthDay dtCotacao){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("cm.vlCotacaoMoeda");
		sql.addFrom(MoedaPais.class.getName()+ " mpd join mpd.cotacaoMoedas as cm ");
		sql.addCriteria("mpd.pais.id","=",idPais);
		sql.addCriteria("mpd.moeda.id","=",idMoeda);
		sql.addCriteria("cm.dtCotacaoMoeda","=",dtCotacao); 
		return sql;
	}

	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		
		ResultSetPage page = super.findPaginated(criteria, findDef);
		
		// força a inicialização das associações assumindo que estarão em cache de segundo nivel
		List list = page.getList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object next = iter.next();
			CotacaoMoeda cm = (CotacaoMoeda) next;
			Hibernate.initialize(cm.getMoedaPais());
			Hibernate.initialize(cm.getMoedaPais().getMoeda());
			Hibernate.initialize(cm.getMoedaPais().getPais());
		}
		
		return page;
	}
	
	public CotacaoMoeda findById(Long id) {
		CotacaoMoeda cm = (CotacaoMoeda) super.findById(id);
		Hibernate.initialize(cm.getMoedaPais());
		Hibernate.initialize(cm.getMoedaPais().getMoeda());
		Hibernate.initialize(cm.getMoedaPais().getPais());
		return cm;
	}
}
