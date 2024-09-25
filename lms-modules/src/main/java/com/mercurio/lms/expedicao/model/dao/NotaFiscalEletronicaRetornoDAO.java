package com.mercurio.lms.expedicao.model.dao;

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
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.TBIntegration;

public class NotaFiscalEletronicaRetornoDAO extends BaseCrudDao<TBIntegration, Long>{

	@Override
	protected Class getPersistentClass() {
		return TBIntegration.class;
	}
	
	
	
	//TODO verificar isso com o Eri,pois poderemos ter dois RPS (doctoServico )com o mesmo numero em filiais diferente 
	//e que ainda não tenham sido processados...

	public List<TBIntegration> findNotaFiscalEletronicaRetornoNaoProcessadoByRpsNumber(Long rpsNumber){
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom( getPersistentClass().getName() + " notaFiscalEletronicaRetorno ");
		sql.addCriteria("notaFiscalEletronicaRetorno.rpsNumber", "=", rpsNumber);
		sql.addCriteria("notaFiscalEletronicaRetorno.docStatus", "=", 0);//não processado
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	
	public List<TBIntegration> findNotaFiscalEletronicaRetornoByDocStatus(Integer docStatus){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select i from TBIntegration i where i.docStatus = ").append(docStatus);
		sql.append(" order by i.id ");
		
		List find = getAdsmHibernateTemplate().find(sql.toString());
		return find;
	}
	
	public String findDocData(Long id) {
		
    	final StringBuilder sql = new StringBuilder();
    	sql.append(" SELECT i.docdata as docdata ");
    	sql.append(" FROM tbintegration i ");
    	sql.append(" where i.id = " + id);
	
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("docdata",Hibernate.CLOB);
			}
		};

    	final HibernateCallback hcb = new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
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
