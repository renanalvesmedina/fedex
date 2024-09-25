package com.mercurio.lms.franqueados.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.SimulacaoDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.report.RelatorioAnaliticoDocumentosQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioAnaliticoFreteLocalQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioAnaliticoServicoAdicionalQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioSinteticoParticipacaoDocumentosFiscaisQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioSinteticoParticipacaoQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioSinteticoParticipacaoServicoAdicionalQuery;

public class SimulacaoDoctoServicoFranqueadoDAO extends BaseCrudDao<SimulacaoDoctoServicoFranqueado, Long> {

	@Override
	protected Class<SimulacaoDoctoServicoFranqueado> getPersistentClass() {
		return SimulacaoDoctoServicoFranqueado.class;
	}
	
	public void removeCifFob(final YearMonthDay dtCompentencia, final Long idFilial) {
		
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session = getSessionFactory().openSession();
					session.beginTransaction();
					
					Query query = null;
					
					StringBuffer sql = new StringBuffer()
				    	.append("delete from ")
				    	.append(SimulacaoDoctoServicoFranqueado.class.getName()).append(" as sdsf ")
				    	.append("where sdsf.tpFrete in (:cifExpedido, :cifRecebido, :fobExpedido, :fobRecebido) ")
						.append("	and sdsf.dtCompetencia = to_date(:dtCompetencia, 'dd/mm/yyyy') ")
						.append("	and sdsf.filial.idFilial = :idFilial ");

					query = session.createQuery(sql.toString());
					query.setString("cifExpedido", ConstantesFranqueado.CIF_EXPEDIDO);
					query.setString("cifRecebido", ConstantesFranqueado.CIF_RECEBIDO);
					query.setString("fobExpedido", ConstantesFranqueado.FOB_EXPEDIDO);
					query.setString("fobRecebido", ConstantesFranqueado.FOB_RECEBIDO);
					query.setString("dtCompetencia", dtCompentencia.toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
					query.setLong("idFilial", idFilial);
					
					query.executeUpdate();

					session.getTransaction().commit();
				} finally {
					if(session != null) session.close();
				}
				return null;
			}
		});
		
	}

	public void removeLocal(final YearMonthDay dtCompentencia, final Long idFilial) {
		
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session = getSessionFactory().openSession();
					session.beginTransaction();
					
					Query query = null;
					
					StringBuffer sql = new StringBuffer()
			    	.append("delete from ")
			    	.append(SimulacaoDoctoServicoFranqueado.class.getName()).append(" as sdsf ")
			    	.append("where sdsf.tpFrete = :tpFrete ")
					.append("	and sdsf.dtCompetencia = to_date(:dtCompetencia, 'dd/mm/yyyy') ")
					.append("	and sdsf.filial.idFilial = :idFilial ");

					query = session.createQuery(sql.toString());
					query.setString("tpFrete", ConstantesFranqueado.FRETE_LOCAL);
					query.setString("dtCompetencia", dtCompentencia.toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
					query.setLong("idFilial", idFilial);
					
					query.executeUpdate();
					

					session.getTransaction().commit();
				} finally {
					if(session != null) session.close();
				}
				return null;
			}
		});
	}
	
	public void removeServicoAdicional(final YearMonthDay dtCompentencia, final Long idFilial) {
		
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session = getSessionFactory().openSession();
					session.beginTransaction();
					
					Query query = null;
					
					StringBuffer sql = new StringBuffer()
			    	.append("delete from ")
			    	.append(SimulacaoDoctoServicoFranqueado.class.getName()).append(" as sdsf ")
			    	.append("where sdsf.tpFrete = :tpFrete ")
					.append("	and sdsf.dtCompetencia = to_date(:dtCompetencia, 'dd/mm/yyyy') ")
					.append("	and sdsf.filial.idFilial = :idFilial ");

					query = session.createQuery(sql.toString());
					query.setString("tpFrete", ConstantesFranqueado.SERVICO_ADICIONAL);
					query.setString("dtCompetencia", dtCompentencia.toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
					query.setLong("idFilial", idFilial);
					
					query.executeUpdate();
					

					session.getTransaction().commit();
				} finally {
					if(session != null) session.close();
				}
				return null;
			}
		});
		
	}

	public void storeAllNewSession(final List<SimulacaoDoctoServicoFranqueado> SimulacaoDoctoServicoFranqueadoList) {
		List<SimulacaoDoctoServicoFranqueado> SimulacaoDoctoServicoFranqueadoStore = new ArrayList<SimulacaoDoctoServicoFranqueado>();
		
		for (int i = 0; i < SimulacaoDoctoServicoFranqueadoList.size(); i++) {
			
			SimulacaoDoctoServicoFranqueadoStore.add(SimulacaoDoctoServicoFranqueadoList.get(i));
			
			if((i+1) % ConstantesFranqueado.LIMITE_COMMIT == 0 || i == SimulacaoDoctoServicoFranqueadoList.size()-1 ){
				storeNewSession(SimulacaoDoctoServicoFranqueadoStore);
				SimulacaoDoctoServicoFranqueadoStore.clear();
			}
		}
	}

	private void storeNewSession(final List<SimulacaoDoctoServicoFranqueado> simulacaoDoctoServicoFranqueadoList) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session = getSessionFactory().openSession();
					session.beginTransaction();
					
					for (SimulacaoDoctoServicoFranqueado simulacaoDoctoServicoFranqueado : simulacaoDoctoServicoFranqueadoList) {
						session.saveOrUpdate(simulacaoDoctoServicoFranqueado);
					}
					
					session.getTransaction().commit();
				} finally {
					if(session != null) session.close();
				}
				return null;
			}
		});
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<SimulacaoDoctoServicoFranqueado> findServicosAdicionais(YearMonthDay dtCompetencia, Long idFilial) {
		StringBuffer sql = new StringBuffer()
    	.append("select sum(dsf.vlParticipacao) as VL_TOTAL_SERVICO from ")
    	.append(getPersistentClass().getName()).append(" as dsf ")
    	.append("where dsf.tpFrete = :tpFrete ")
		.append("	and dsf.dtCompetencia = :dtCompetencia ")
		.append("	and dsf.filial.idFilial = :idFilial ")
		.append("	and dsf.doctoServicoFranqueadoOriginal IS NULL ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tpFrete", ConstantesFranqueado.SERVICO_ADICIONAL);
		params.put("dtCompetencia", dtCompetencia);
		params.put("idFilial", idFilial);
		
		List<SimulacaoDoctoServicoFranqueado> list = (List<SimulacaoDoctoServicoFranqueado>)getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
		return list;
	}
	
	
	public List<Map<String, Object>> findRelatorioSinteticoDefault(Map<String, Object> parameters, boolean sum) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioSinteticoParticipacaoQuery.getQueryDefaultSimulacao(sum), 
																	parameters, 
																	RelatorioSinteticoParticipacaoQuery.createConfigureSql(sum));
	}
	
	
	public List<Map<String, Object>> findRelatorioSinteticoColetaEntrega(Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioSinteticoParticipacaoQuery.getQueryOrderedByColetaEntregaSimulacao(), 
																	parameters, 
																	RelatorioSinteticoParticipacaoQuery.createConfigureSql(false));
	}


	public List<Map<String, Object>> findRelatorioSinteticoDocumentosFiscais(Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioSinteticoParticipacaoDocumentosFiscaisQuery.getQuerySimulacao(), 
																	parameters, 
																	RelatorioSinteticoParticipacaoDocumentosFiscaisQuery.createConfigureSql());
	}
	
	public List<Map<String, Object>> findRelatorioSinteticoServicoAdicional(Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioSinteticoParticipacaoServicoAdicionalQuery.getQuerySimulacao(), 
																	parameters, 
																	RelatorioSinteticoParticipacaoServicoAdicionalQuery.createConfigureSql());
	}
	
	public List<Map<String, Object>> findRelatorioAnaliticoDocumentos(boolean filtraFranquia,  boolean isCSV, Map<String,Object> parameters){
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioAnaliticoDocumentosQuery.getQuerySimulacao(filtraFranquia,isCSV), 
																	parameters, 
																	RelatorioAnaliticoDocumentosQuery.createConfigureSql(isCSV));
	}


	public List<Map<String, Object>> findRelatorioAnaliticoFretesLocal(boolean filtraFranquia, boolean isCSV, Map<String, Object> parameters) {		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioAnaliticoFreteLocalQuery.getQuerySimulacao(filtraFranquia,isCSV), 
																	parameters, 
																	RelatorioAnaliticoFreteLocalQuery.createConfigureSql(isCSV));
	}
	
	public List<Map<String, Object>> findRelatorioAnaliticoServicosAdicionais(boolean filtraFranquia, boolean isCSV, Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioAnaliticoServicoAdicionalQuery.getQuerySimulacao(filtraFranquia, isCSV), 
																	parameters, 
																	RelatorioAnaliticoServicoAdicionalQuery.createConfigureSql(isCSV));
	}
}
