package com.mercurio.lms.configuracoes.model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.ParametroFilial;
import com.mercurio.lms.municipios.model.Filial;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ConteudoParametroFilialDAO extends BaseCrudDao<ConteudoParametroFilial, Long>
{
	private static Log log = LogFactory.getLog(ConteudoParametroFilialDAO.class);
	
	private static final String FIND_BY_NOME_PARAMETRO = "from "+ConteudoParametroFilial.class.getName()+" cpf"+
														 "  join fetch cpf.parametroFilial pf "+
														 " where pf.nmParametroFilial = ? "+
														 " and cpf.filial.id = ?";


	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ConteudoParametroFilial.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("filial",FetchMode.JOIN);
		lazyFindById.put("filial.pessoa",FetchMode.JOIN);		
	}

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
        lazyFindLookup.put("filial",FetchMode.JOIN);
        lazyFindLookup.put("filial.pessoa",FetchMode.JOIN);
    }
	
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
        lazyFindPaginated.put("filial",FetchMode.JOIN);        
        lazyFindPaginated.put("filial.pessoa",FetchMode.JOIN);
    }
    
    /**
     * Retorna o valor do conteudo a partir do id da filial e da chave do parametro.
     * 
     * @param Long idFilial
     * @param String nmParametro
     * @return String
     * */
    public String findValorConteudo(Long idFilial, String nmParametro){
        ConteudoParametroFilial conteudoParametroFilial = findConteudoParametroFilialByNmParametro(idFilial, nmParametro);
    	if (conteudoParametroFilial != null) {
    		return conteudoParametroFilial.getVlConteudoParametroFilial();
    	} else {
    		return null;
    	}
    }
    
    /**
     * Retorna o objeto <code>ConteudoParametroFilial</code> a partir do id da filial e da chave do parametro.
     * 
     * @param Long idFilial
     * @param String nmParametro
     * @return ConteudoParametroFilial
     * @deprecated @see ConteudoParametroFilialDAO#findByNomeParametro(Long, String)
     * */
    public ConteudoParametroFilial findConteudoParametroFilialByNmParametro(Long idFilial, String nmParametro){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("cpf");
    	sql.addFrom(ConteudoParametroFilial.class.getName(),"cpf join cpf.parametroFilial pf");
    	sql.addCriteria("pf.nmParametroFilial","=",nmParametro);
    	sql.addCriteria("cpf.filial.id","=",idFilial);    	
    	List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    	
    	if (!list.isEmpty()) {
    		return (ConteudoParametroFilial)list.get(0);
    	} else {
    		return null;
    	}
    }
    
    public ConteudoParametroFilial findConteudoParametroFilial(Long idFilial, String nomeParametro, String vlConteudo) {
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(ConteudoParametroFilial.class,"cpf")
    	.createAlias("cpf.parametroFilial", "pf")
    	.add(Restrictions.eq("pf.nmParametroFilial", nomeParametro));
    	
    	dc.add(Restrictions.eq("cpf.filial.id", idFilial));
    	
    	if(vlConteudo != null){
    		dc.add(Restrictions.eq("cpf.vlConteudoParametroFilial", vlConteudo));
    	}
    	
    	return (ConteudoParametroFilial)getAdsmHibernateTemplate().findUniqueResult(dc);
    }
        
    /**
     * Consulta uma <code>ConteudoParametroFilial</code> através do nome e da filial.
     * 
     * @param idFilial
     * @param nomeParametro
     * @param realizaLock realiza um lock pessimista no registro retornado
     * @return o parametro ou <code>null</code> caso não seja localizado.
     */
    public ConteudoParametroFilial findByNomeParametro(Long idFilial, 
    													String nomeParametro, 
    													boolean realizaLock) {

    	if (idFilial == null) {
    		throw new IllegalArgumentException("idFilial não pode ser null");
    	}
    	if (StringUtils.isBlank(nomeParametro)) {
    		throw new IllegalArgumentException("nomeParametro não pode ser null ou vazio");
    	}
    	
    	ConteudoParametroFilial paramFilial; 
    	
    	if (realizaLock) {
    		paramFilial = (ConteudoParametroFilial) findUniqueResultForUpdate(FIND_BY_NOME_PARAMETRO, 
					new Object[]{nomeParametro, idFilial}, "cpf");
    	} else {
    		paramFilial = (ConteudoParametroFilial) getAdsmHibernateTemplate().findUniqueResult(FIND_BY_NOME_PARAMETRO, 
					new Object[]{nomeParametro, idFilial});
    	}
    	
    	return paramFilial;
    }
    
    /*
     * FIXME: Remover e adicionar na AdsmHibernateTemplate do ADSM
     */
	public Object findUniqueResultForUpdate(final String queryString,
											final Object[] parameterValues, 
											final String lockAlias) {

		final HibernateCallback hcb = new HibernateCallback() {
		
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createQuery(queryString);
					query.setLockMode(lockAlias, LockMode.UPGRADE);

					if (parameterValues != null) {
						for (int i = 0; i < parameterValues.length; i++) {
							query.setParameter(i, parameterValues[i]);
						}
					}
					
					return query.uniqueResult();
				}
			};
			
		return getHibernateTemplate().execute(hcb);
	}


    public Integer getRowCountCounteudoParametroFilial(Long idFilial, String nomeParametro){
    	StringBuffer sql = new StringBuffer()
    	.append("from ")
		.append(ConteudoParametroFilial.class.getName())
		.append(" as cpf ")
		.append("inner join cpf.parametroFilial pf ")
		.append("where cpf.filial.id = ? ")
		.append("and pf.nmParametroFilial = ? ");
		
		Object[] parameters = {idFilial, nomeParametro};
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), parameters);
    }
	
	
	public ConteudoParametroFilial createConteudoParametroFilial(Long idFilial, ParametroFilial parametroFilial, String vlConteudo) {
		Filial filial = (Filial)getAdsmHibernateTemplate().load(Filial.class, idFilial);

		ConteudoParametroFilial conteudoParametroFilial = new ConteudoParametroFilial();
		conteudoParametroFilial.setFilial(filial);
		conteudoParametroFilial.setParametroFilial(parametroFilial);
		conteudoParametroFilial.setVlConteudoParametroFilial(vlConteudo);

		store(conteudoParametroFilial);
		return conteudoParametroFilial;
	}
	
	/**
	 * Gera o parametro sequencial negativo para números de conhecimentos temporários.
	 * Correção de performance para a issue LMS-968.
	 * @return proximo numero da sequencia.
	 */
	public Long generateNrConhecimentoTmp() {
		return Long.valueOf(getSession().createSQLQuery("select NR_CONHECIMENTO_TMP_SQ.nextval from dual").uniqueResult().toString());
	}
	
    @SuppressWarnings("deprecation")
	public Long generateValorParametroSequencial(Long idFilial, String nomeParametro, long incremento) {
    	if (StringUtils.isBlank(nomeParametro)) {
    		throw new IllegalArgumentException("nomeParametro não pode ser null ou vazio");
}
    	Long result = null;

    	String updateParametroGeralQuery = " UPDATE conteudo_parametro_filial "
    		+ " SET VL_CONTEUDO_PARAMETRO_FILIAL = (case when regexp_like(VL_CONTEUDO_PARAMETRO_FILIAL, '^-?[[:digit:],.]*$') "
    				+ "then (NVL(VL_CONTEUDO_PARAMETRO_FILIAL, 0) + ("+incremento+")) "
    				+ "else "+incremento+" end) "
    		+ " WHERE id_filial = "+idFilial
    		    + " AND id_parametro_filial = (select id_parametro_filial from parametro_filial pf where pf.nm_parametro_filial='"+nomeParametro+"')"
    		+ " RETURNING VL_CONTEUDO_PARAMETRO_FILIAL INTO ? ";

		try {
			Connection connection = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().connection();
			CallableStatement statement = connection.prepareCall("{call "+updateParametroGeralQuery+" }");
			statement.registerOutParameter(1, Types.NUMERIC);

			statement.executeUpdate();
			if(statement.getObject(1) == null){
				Filial filial = (Filial) getAdsmHibernateTemplate().findUniqueResult("FROM "+Filial.class.getName()+" WHERE ID_FILIAL = ? ", new Object[]{idFilial});
				throw new InfrastructureException("LMS-00075", new Object[]{idFilial, filial.getSgFilial(), nomeParametro});
			}
			result = statement.getLong(1);
		} catch (InfrastructureException ie) {
			throw ie;
		} catch (Exception e) {
			throw new InfrastructureException(e);
		}
    	
    	return result;
    }
    
    public Filial findFilialByConteudoParametro(String nomeParametro, String valorConteudo){
        String hql = "select cpf.filial "
                + "from ConteudoParametroFilial cpf "
                + "where cpf.parametroFilial.nmParametroFilial = ? "
                + "and cpf.vlConteudoParametroFilial = ?";
        
        return (Filial)getAdsmHibernateTemplate().findUniqueResult(hql, new Object[]{nomeParametro, valorConteudo});
    }
    
    @SuppressWarnings("unchecked")
	public List<Filial> findFilialByNmParametro(String nomeParametro){
    	
    	String hql = "select cpf.filial "
                + "from ConteudoParametroFilial cpf "
                + "where cpf.parametroFilial.nmParametroFilial = ? ";
        
        return getAdsmHibernateTemplate().find(hql, new Object[]{nomeParametro});
    }
    
}
