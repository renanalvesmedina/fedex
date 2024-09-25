package com.mercurio.lms.configuracoes.model.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.jmx.MBeanFactory;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.jmx.ParametroGeralCache;
import com.mercurio.lms.configuracoes.jmx.ParametroGeralCacheMBean;
import com.mercurio.lms.configuracoes.model.ParametroGeral;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParametroGeralDAO extends BaseCrudDao<ParametroGeral, Long> {

	private static final String FIND_BY_NOME_PARAMETRO = "from "+ParametroGeral.class.getName()+
														 " pg where pg.nmParametroGeral = ?";

	private static ParametroGeralCacheMBean cacheMBean;
	private static List<ParametroGeralCacheMBean> cacheMBeans;

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ParametroGeral.class;
    }

	/**
	 * Busca o conteúdo do parâmetro geral de acordo com o nome
	 * @deprecated @see ParametroGeralDAO#findByNomeParametro(String)
	 */
    public String findParametroGeral(String nmParametroGeral) {
        String param = null;

        DetachedCriteria dc = DetachedCriteria.forClass(ParametroGeral.class, "pg");

        dc.setProjection(Projections.property("pg.dsConteudo"));

        dc.add(Restrictions.eq("pg.nmParametroGeral", nmParametroGeral));

        List result = findByDetachedCriteria(dc);

        if(result.size() == 1) {
        	param = (String) result.get(0);
        }
        return param;
    }

    /**
     * Retorna um ParametroGeral de acordo com o nome do parametro informado.
     * 
     * @param nomeParametro nome do parametro
     * @param realizaLock realiza lock pessimista no parametro informado no banco de dados
     * 
     * @return <code>null</code> caso nenhum parametro seja encontrado, o ParametroGeral caso contrário.
     */
    public ParametroGeral findByNomeParametro(String nomeParametro, boolean realizaLock) {
    	if (StringUtils.isBlank(nomeParametro)) {
    		throw new IllegalArgumentException("nomeParametro não pode ser null ou vazio");
    	}
		ParametroGeral param = null;

    	if (realizaLock) {
    		param = (ParametroGeral) getAdsmHibernateTemplate().findUniqueResultForUpdate(FIND_BY_NOME_PARAMETRO, new Object[]{nomeParametro}, "pg");
    	} else {
			initializeCache();
			List<ParametroGeral> parametroGeralCache = cacheMBean.getParametrosGerais();
			for (ParametroGeral parametroGeral : parametroGeralCache) {
				if (parametroGeral.getNmParametroGeral().equals(nomeParametro)) {
					param = parametroGeral;
					break;
    	}
			}
    	}

    	return param;
    }

	private void initializeCache() {
		initializeMBean();
		boolean needRefresh = false;
		for (ParametroGeralCacheMBean bean : cacheMBeans) {
			if (bean.needRefresh()) {
				needRefresh = true;
				break;
			}
				}
		if (needRefresh) {
			StringBuilder hql = new StringBuilder();
			hql.append(" select pg from ");
			hql.append(ParametroGeral.class.getName());
			hql.append(" pg ");
			List<ParametroGeral> cacheData = (List<ParametroGeral>) getAdsmHibernateTemplate().find(hql.toString());
			for (ParametroGeralCacheMBean bean : cacheMBeans) {
				bean.setParametrosGerais(cacheData);
		}
	}
	}

	private void initializeMBean() {
		if (cacheMBeans == null || cacheMBeans.isEmpty()) {
			cacheMBeans = MBeanFactory.getJMXProxy("*:name=LMS-ParametroGeralCache", ParametroGeralCacheMBean.class, ParametroGeralCache.class);
			if (cacheMBeans != null && !cacheMBeans.isEmpty()) {
				cacheMBean = cacheMBeans.get(0);
			}
		}
	}

	/**
	 * Altera o valor da flag de atualização de dados para que na proxima
	 * consulta a cache ela seja atualizada.
	 */
	public void refreshCache() {
		initializeMBean();
		for (ParametroGeralCacheMBean bean : cacheMBeans) {
			bean.refresh();
	}
	}

    public List findServicosAdicionais(List listaParametros) {
		return getAdsmHibernateTemplate()
				.findByNamedQueryAndNamedParam(ParametroGeral.FIND_SERVICOS_ADICIONAIS, 
						"listaParametros",
						listaParametros);
	}

    @SuppressWarnings("deprecation")
	public Long generateValorParametroSequencial(String nomeParametro, long incremento) {
    	if (StringUtils.isBlank(nomeParametro)) {
    		throw new IllegalArgumentException("nomeParametro não pode ser null ou vazio");
    	}
    	Long result = null;

    	String updateParametroGeralQuery = " UPDATE parametro_geral "
    		+ " SET DS_CONTEUDO = (case when regexp_like(DS_CONTEUDO, '^-?[[:digit:],.]*$') "
    				+ "then (NVL(DS_CONTEUDO, 0) + ("+incremento+")) "
    				+ "else "+incremento+" end) "
    		+ " WHERE nm_parametro_geral = '"+nomeParametro+"' "
    		+ " RETURNING DS_CONTEUDO INTO ? ";
    	
		try {
			Connection connection = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().connection();
			CallableStatement statement = connection.prepareCall("{call "+updateParametroGeralQuery+" }");
			statement.registerOutParameter(1, Types.NUMERIC);

			statement.executeUpdate();
			if(statement.getObject(1) == null){
				throw new InfrastructureException("LMS-27051", new Object[]{nomeParametro});
			}
			result = statement.getLong(1);
		} catch (InfrastructureException ie) {
			throw ie;
		} catch (Exception e) {
			throw new InfrastructureException(e);
		}
    	
    	return result;
    }
    
	public void storeValorParametroNewSession(final String nomeParametro, final Object valorParametro) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session = getSessionFactory().openSession();
					session.beginTransaction();
					
					Query query = null;
					String hql = "UPDATE " + getPersistentClass().getName() + " pg \n" +
								 	" SET pg.dsConteudo=:conteudo \n" +
									" WHERE pg.nmParametroGeral=:nomeParametro "; 
					query = session.createQuery(hql);
					query.setString("conteudo", valorParametro.toString());
					query.setString("nomeParametro", nomeParametro);
					query.executeUpdate();
					session.getTransaction().commit();
				} finally {
					if(session != null) session.close();
				}
				return null;
			}
		});
	}
    
	// TODO - Verficar o merge com a producao
	public String findSimpleConteudoByNomeParametro(String nomeParametro) {
		StringBuilder hql = new StringBuilder();
		hql.append("select pg.dsConteudo ")
		.append(FIND_BY_NOME_PARAMETRO);
		
		return (String) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{nomeParametro});
	}
}