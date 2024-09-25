package com.mercurio.lms.sgr.model.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.lob.SerializableClob;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.sgr.dto.FiltroLiberacaoMotoristaDto;
import com.mercurio.lms.sgr.model.HistLiberacaoMotorista;

public class HistLiberacoMotoristaDao extends
		BaseCrudDao<HistLiberacaoMotorista, Long> {

	@Override
	protected Class<HistLiberacaoMotorista> getPersistentClass() {
		return HistLiberacaoMotorista.class;
	}

	public ResultSetPage<HistLiberacaoMotorista> findPaginated(FiltroLiberacaoMotoristaDto filtro, FindDefinition findDef) {

		SqlTemplate sql = getSql(filtro);

		String queryString = " select hlm " + sql.toString();
		Object[] values = sql.getCriteria();
		return getAdsmHibernateTemplate().findPaginated(queryString, findDef.getCurrentPage(), findDef.getPageSize(), values);

	}
	
	public Integer getRowCount(FiltroLiberacaoMotoristaDto filtro) {

		SqlTemplate sql = getSql(filtro);

		String queryString = " select count(*) " + sql.getSql();
		Object[] values = sql.getCriteria();

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(queryString, values);

		return result.intValue();
		
	}
	
	private SqlTemplate getSql(FiltroLiberacaoMotoristaDto filtro) {
		
		SqlTemplate sqlTemplate = new SqlTemplate();
		
		sqlTemplate.addFrom("HistLiberacaoMotorista","hlm");
		
		if (filtro.getDhPeriodoIni() != null) {
			sqlTemplate.addCriteria("hlm.dhProcessamento.value", ">=", filtro.getDhPeriodoIni());
		}
		
		if (filtro.getDhPeriodoFim() != null) {
			sqlTemplate.addCriteria("hlm.dhProcessamento.value", "<=", filtro.getDhPeriodoFim());
		}
		
		if (filtro.getCpfMotorista() != null) {
			sqlTemplate.addCriteria( "upper(hlm.cpfMotorista)", "like", "%"+filtro.getCpfMotorista()+"%");
		}
		
		if (filtro.getNmMotorista() != null) {
			sqlTemplate.addCriteria("upper(hlm.nmMotorista)", "like", "%"+filtro.getNmMotorista().toUpperCase()+"%");
		}
		
		sqlTemplate.addOrderBy("hlm.dhProcessamento");
		sqlTemplate.addOrderBy("hlm.nmMotorista");
		
		
		return sqlTemplate;
		
	}

	public String findConteudosArquivo(final Long idHistLiberacaoMotorista) {
    	
		final StringBuilder sql = new StringBuilder();
    	sql.append(" SELECT hlm.DC_ARQUIVO_LIBERACAO as arquivo ");
    	sql.append(" FROM HIST_LIBERACAO_MOTORISTA hlm ");
    	sql.append(" where hlm.id_HIST_LIBERACAO_MOTORISTA = :idHistLiberacaoMotorista ");
	
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				
				sqlQuery.addScalar("arquivo",Hibernate.CLOB);
				
			}
		};

    	final HibernateCallback hcb = new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				SQLQuery query = session.createSQLQuery(sql.toString());
				query.setParameter("idHistLiberacaoMotorista", idHistLiberacaoMotorista);
            	csq.configQuery(query);
            	
				return query.list();
				
			}
		};
		
		String xml = "";

		List list = getHibernateTemplate().executeFind(hcb);
		
		if (list != null && ! list.isEmpty()) {
			
			SerializableClob o = (SerializableClob) list.get(0);
			
			try {
				BufferedReader bf = new BufferedReader(o.getCharacterStream());
				String linha = null; 
				
				while ((linha = bf.readLine()) != null) { 
					xml += linha; 
				}
			} catch (SQLException e) {
				xml = "";
			} catch (IOException e) {
				xml = "";
			} 

			
		}
		
		return xml;

	}

}
