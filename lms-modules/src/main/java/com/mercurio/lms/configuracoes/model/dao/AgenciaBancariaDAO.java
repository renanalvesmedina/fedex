package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.AgenciaBancaria;

/**
 * @author JoseMR
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AgenciaBancariaDAO extends BaseCrudDao<AgenciaBancaria, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {		
		return AgenciaBancaria.class;
	}
	
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("banco",FetchMode.JOIN);
		lazyFindPaginated.put("banco.pais",FetchMode.JOIN);
	}
		
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("banco",FetchMode.JOIN);
		fetchModes.put("banco.pais",FetchMode.JOIN);
	}	
	
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("banco",FetchMode.JOIN);
		lazyFindLookup.put("banco.pais",FetchMode.JOIN);
	}	
	
	public boolean findNrAgenciaBancaria(Short nrBanco, Short nrAgencia){
		DetachedCriteria dc = createDetachedCriteria();
		if(nrBanco != null)
			dc.add(Restrictions.eq("banco.nrBanco", nrBanco));
		dc.add(Restrictions.eq("nrAgenciaBancaria", nrAgencia));
		return findByDetachedCriteria(dc).size()>0;
	}

	/**
	 * Busca uma agência Bancária de acordo com os parâmetros
	 *
	 * @author José Rodrigo Moraes
	 * @since 06/12/2006
	 *
	 * @param idBanco Identificador do Banco
	 * @param nrDigito Número do dígito da Agência Bancária
	 * @param nrAgenciaBancaria Número da Agência Bancária
	 * @return AgenciaBancaria
	 */
	public AgenciaBancaria findAgenciaBancaria(Long idBanco, String nrDigito, Short nrAgenciaBancaria) {
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("ab");
		
		hql.addInnerJoin(getPersistentClass().getName(),"ab");
		hql.addInnerJoin("ab.banco","b");
		
		hql.addCriteria("b.id","=",idBanco);
		hql.addCriteria("ab.nrDigito","=",nrDigito);
		hql.addCriteria("ab.nrAgenciaBancaria","=",nrAgenciaBancaria);
		
		List <AgenciaBancaria> listaAgencias = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		
		AgenciaBancaria retorno = null;
		
		if( listaAgencias != null && !listaAgencias.isEmpty() ){
			retorno = listaAgencias.get(0);
		}
		
		return retorno;
	}
	
	
	public AgenciaBancaria findAgencia(Long idBanco, Short nrAgenciaBancaria) {
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("ab");
		
		hql.addInnerJoin(getPersistentClass().getName(),"ab");
		hql.addInnerJoin("ab.banco","b");
		
		hql.addCriteria("b.id","=",idBanco);
		hql.addCriteria("ab.nrAgenciaBancaria","=",nrAgenciaBancaria);
		
		List <AgenciaBancaria> listaAgencias = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		
		AgenciaBancaria retorno = null;
		
		if( listaAgencias != null && !listaAgencias.isEmpty() ){
			retorno = listaAgencias.get(0);
		}
		
		return retorno;
	}
	
	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT a.id_agencia_bancaria AS idAgenciaBancaria, a.nr_agencia_bancaria AS nrAgenciaBancaria, a.nm_agencia_bancaria AS nmAgenciaBancaria");
		sql.append(" FROM agencia_bancaria a");
		sql.append("  INNER JOIN banco b");
		sql.append("  ON b.id_banco = a.id_banco");
		sql.append(" WHERE b.tp_situacao = 'A'");
		
		if(filter.get("idBanco") != null) {
			sql.append(" AND b.id_banco = :idBanco");
		}
		
		if(filter.get("nrAgenciaBancaria") != null) {
			sql.append(" AND a.nr_agencia_bancaria = :nrAgenciaBancaria");
		}
		
		if(filter.get("nmAgenciaBancaria") != null) {
			sql.append(" AND LOWER(a.nm_agencia_bancaria) LIKE LOWER(:nmAgenciaBancaria)");
			filter.put("nmAgenciaBancaria", "%" + filter.get("nmAgenciaBancaria") + "%");
		}
		
		return new ResponseSuggest(sql.toString(), filter);
	}
}
