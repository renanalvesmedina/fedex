package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.Banco;

/**
 * @author JoseMR
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class BancoDAO extends BaseCrudDao<Banco, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Banco.class;
	}	

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("pais",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("pais",FetchMode.JOIN);
	}
	
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("pais",FetchMode.JOIN);
	}	
	public boolean findNrBanco(Short nrBanco){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("nrBanco", nrBanco));
		return findByDetachedCriteria(dc).size()>0;
	}

	/**
	 * Busca um Banco pelo seu número
	 *
	 * @author José Rodrigo Moraes
	 * @since 06/12/2006
	 *
	 * @param nrBanco Número do Banco
	 * @return Banco
	 */
	public Banco findByNrBanco(Short nrBanco) {
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("b");
		
		hql.addFrom(getPersistentClass().getName(),"b");
		
		hql.addCriteria("b.nrBanco","=",nrBanco);
		
		List <Banco> bancos = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		
		Banco banco = null;
		
		if( bancos != null && !bancos.isEmpty() ){
			banco = bancos.get(0); 
		}
		
		return banco;
	}

	/**
	 * Carrega a entidade Banco de acordo com o número e o idPais.
	 * Método criado para a integração.
	 * 
	 * Hector Julian Esnaola Junior
	 * 13/02/2008
	 *
	 * @param nrBanco
	 * @param idPais
	 *
	 * @return Banco
	 *
	 */
	public Banco findBancoByNrBancoAndIdPais (Short nrBanco, Long idPais) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("b");
		hql.addFrom(getPersistentClass().getName(), "b");
		hql.addCriteria("b.nrBanco", "=", nrBanco);
		hql.addCriteria("b.pais.id", "=", idPais);
		
		return (Banco) getAdsmHibernateTemplate().findUniqueResult(
				hql.getSql(), 
				hql.getCriteria());
	}
	
	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT b.id_banco AS idBanco, b.nr_banco AS nrBanco, b.nm_banco AS nmBanco");
		sql.append(" FROM banco b");
		sql.append(" WHERE b.tp_situacao = 'A'");
		
		if(filter.get("nrBanco") != null) {
			sql.append(" AND b.nr_banco = :nrBanco");
		}
		
		if(filter.get("nmBanco") != null) {
			sql.append(" AND LOWER(b.nm_banco) LIKE LOWER(:nmBanco)");
			filter.put("nmBanco", "%" + filter.get("nmBanco") + "%");
		}
		
		return new ResponseSuggest(sql.toString(), filter);
	}
}
