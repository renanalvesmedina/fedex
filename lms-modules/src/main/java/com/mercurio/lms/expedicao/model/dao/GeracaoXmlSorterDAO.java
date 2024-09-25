package com.mercurio.lms.expedicao.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.expedicao.model.GeracaoXmlSorter;

public class GeracaoXmlSorterDAO extends BaseCrudDao<GeracaoXmlSorter, Long> {

	@Override
	protected Class<GeracaoXmlSorter> getPersistentClass() {
		return GeracaoXmlSorter.class;
	}

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
	}
	
    public List<Map<String, Object>> findDadosGeracaoXmlSorter(final GeracaoXmlSorter geracaoXmlSorter){
    	final StringBuilder sql = new StringBuilder();
    	
    	sql.append("SELECT ")
		.append("		VNF.NR_VOLUME_COLETA AS NRVOLUMECOLETA, ")
		.append("		FI.CD_FILIAL AS CDFILIAL, ")
		.append("		RCE.NR_ROTA AS NRROTA, ")
		.append("		MA.ID_FILIAL_DESTINO AS IDFILIALDESTINO, ")
		.append("       FM.SG_FILIAL AS SGFILIAL ")
		.append("	FROM DOCTO_SERVICO DS, ")
		.append("		CONHECIMENTO CO, NOTA_FISCAL_CONHECIMENTO NFC, ")
		.append("		VOLUME_NOTA_FISCAL VNF, FILIAL FI, FILIAL FM,")
		.append("		ROTA_COLETA_ENTREGA RCE, ")
		.append("		MANIFESTO_NACIONAL_CTO MNC, ")
		.append("		MANIFESTO MA ")
		.append("	WHERE ")
		.append("	  	DS.ID_FILIAL_ORIGEM = :idFilialOrigem AND ")
		.append("		DS.ID_CLIENTE_REMETENTE = :idClienteRemetente AND ")
		.append("		DS.DH_EMISSAO >= SYSDATE - NUMTODSINTERVAL(:nrHorasInicio,'HOUR') AND ")
		.append("		DS.DH_EMISSAO < SYSDATE - NUMTODSINTERVAL(:nrHorasFim,'HOUR') AND ")
		.append("		CO.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO AND ")
		.append("		CO.TP_SITUACAO_CONHECIMENTO = 'E' AND ")
		.append("		NFC.ID_CONHECIMENTO = CO.ID_CONHECIMENTO AND ")
		.append("		VNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO AND ")
		.append("		FI.ID_FILIAL = DS.ID_FILIAL_DESTINO AND ")
		.append("		RCE.ID_ROTA_COLETA_ENTREGA = DS.ID_ROTA_COLETA_ENTREGA_SUGERID AND ")
		.append("		MNC.ID_CONHECIMENTO = CO.ID_CONHECIMENTO AND ")
		.append("		MA.ID_MANIFESTO = MNC.ID_MANIFESTO_VIAGEM_NACIONAL AND ")
		.append("		MA.ID_FILIAL_DESTINO = FM.ID_FILIAL AND  ")
		.append("		MA.ID_FILIAL_DESTINO = :idFilialDestino ");
    	
    	final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("NRVOLUMECOLETA",Hibernate.STRING);
				sqlQuery.addScalar("CDFILIAL",Hibernate.STRING);
				sqlQuery.addScalar("NRROTA",Hibernate.STRING);
				sqlQuery.addScalar("IDFILIALDESTINO",Hibernate.LONG);
				sqlQuery.addScalar("SGFILIAL",Hibernate.STRING);
			}
		};
    	
		final HibernateCallback hcb = new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
            	csq.configQuery(query);
            	query.setParameter("idFilialOrigem", geracaoXmlSorter.getFilialOrigem().getIdFilial());
            	query.setParameter("idClienteRemetente", geracaoXmlSorter.getClienteRemetente().getIdCliente());
            	query.setParameter("nrHorasInicio", geracaoXmlSorter.getNrHorasInicio());
            	query.setParameter("nrHorasFim", geracaoXmlSorter.getNrHorasFim());
            	query.setParameter("idFilialDestino", geracaoXmlSorter.getFilialDestino().getIdFilial());
				return query.list();
			}
		};
    	
		List<Object[]> result = getHibernateTemplate().executeFind(hcb);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Object[] obj : result){
			Map<String, Object> line = new HashMap<String, Object>();
			line.put("NRVOLUMECOLETA", obj[0]);			
			line.put("CDFILIAL",StringUtils.leftPad((String)obj[1], 3, '0'));
			line.put("NRROTA",StringUtils.leftPad((String)obj[2], 3, '0') );
			line.put("IDFILIALDESTINO",obj[3]);
			line.put("SGFILIAL",obj[4]);
			list.add(line);
		}
		return list;
    }
    
    
    public List<GeracaoXmlSorter> findAllGeracaoXmlSorter() {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT gxs ");
        sql.append("   FROM ").append(GeracaoXmlSorter.class.getName()).append(" gxs ");
        sql.append("  ORDER BY  gxs.filialDestino.id ");
      
        return getAdsmHibernateTemplate().find(sql.toString());
    }

	public List<Map<String, Object>> findDadosGeracaoXmlSorterPortaria(final GeracaoXmlSorter geracaoXmlSorter, final Long idControleCarga) {		   
    	final StringBuilder sql = new StringBuilder();
    	
    	sql.append("SELECT ")				
		.append(" 		VNF.NR_VOLUME_COLETA AS NRVOLUMECOLETA,")
		.append(" 		FI.CD_FILIAL AS CDFILIAL,")
		.append(" 		RCE.NR_ROTA AS NRROTA,")
		.append(" 		MA.ID_FILIAL_DESTINO AS IDFILIALDESTINO,")
		.append("        FM.SG_FILIAL AS SGFILIAL")
		.append(" 	FROM DOCTO_SERVICO DS,")
		.append(" 		CONHECIMENTO CO, ")
		.append(" 		NOTA_FISCAL_CONHECIMENTO NFC, ")
		.append(" 		VOLUME_NOTA_FISCAL VNF, ")
		.append(" 		FILIAL FI,  ")
		.append(" 		FILIAL FM, ")
		.append(" 		ROTA_COLETA_ENTREGA RCE, ")
		.append(" 		MANIFESTO_NACIONAL_CTO MNC, ")
		.append(" 		MANIFESTO MA ")
		.append(" 	WHERE ")
		.append(" 	  	DS.ID_FILIAL_ORIGEM = :idFilialOrigem AND ")
		.append(" 		DS.ID_CLIENTE_REMETENTE = :idClienteRemetente AND ")
		.append(" 		CO.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO AND ")
		.append(" 		CO.TP_SITUACAO_CONHECIMENTO = 'E' AND ")
		.append(" 		NFC.ID_CONHECIMENTO = CO.ID_CONHECIMENTO AND ")
		.append(" 		VNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO AND ")
		.append(" 		FI.ID_FILIAL = DS.ID_FILIAL_DESTINO AND ")
		.append(" 		RCE.ID_ROTA_COLETA_ENTREGA = DS.ID_ROTA_COLETA_ENTREGA_SUGERID AND ")
		.append(" 		MNC.ID_CONHECIMENTO = CO.ID_CONHECIMENTO AND ")
		.append(" 		MA.ID_MANIFESTO = MNC.ID_MANIFESTO_VIAGEM_NACIONAL AND ")
		.append(" 		MA.ID_FILIAL_DESTINO = FM.ID_FILIAL AND  ")
		.append(" 		MA.ID_FILIAL_DESTINO = :idFilialDestino AND  ")
		.append(" 		MA.ID_CONTROLE_CARGA = :idControleCarga ");
    	
    	final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("NRVOLUMECOLETA",Hibernate.STRING);
				sqlQuery.addScalar("CDFILIAL",Hibernate.STRING);
				sqlQuery.addScalar("NRROTA",Hibernate.STRING);
				sqlQuery.addScalar("IDFILIALDESTINO",Hibernate.LONG);
				sqlQuery.addScalar("SGFILIAL",Hibernate.STRING);
			}
		};
    	
		final HibernateCallback hcb = new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
            	csq.configQuery(query);
            	query.setParameter("idFilialOrigem", geracaoXmlSorter.getFilialOrigem().getIdFilial());
            	query.setParameter("idClienteRemetente", geracaoXmlSorter.getClienteRemetente().getIdCliente());            	
            	query.setParameter("idFilialDestino", geracaoXmlSorter.getFilialDestino().getIdFilial());
            	query.setParameter("idControleCarga", idControleCarga);
				return query.list();
			}
		};
    	
		List<Object[]> result = getHibernateTemplate().executeFind(hcb);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Object[] obj : result){
			Map<String, Object> line = new HashMap<String, Object>();
			line.put("NRVOLUMECOLETA", obj[0]);			
			line.put("CDFILIAL",StringUtils.leftPad((String)obj[1], 3, '0'));
			line.put("NRROTA",StringUtils.leftPad((String)obj[2], 3, '0') );
			line.put("IDFILIALDESTINO",obj[3]);
			line.put("SGFILIAL",obj[4]);
			list.add(line);
		}
		return list;
    }
	
}
