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
import com.mercurio.lms.franqueados.model.SimulacaoReembarqueDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.report.RelatorioAnaliticoReembarqueQuery;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;

public class SimulacaoReembarqueDoctoServicoFranqueadoDAO extends BaseCrudDao< SimulacaoReembarqueDoctoServicoFranqueado, Long> {
	
	@Override
	protected Class<SimulacaoReembarqueDoctoServicoFranqueado> getPersistentClass() {
		return SimulacaoReembarqueDoctoServicoFranqueado.class;
	}

	public List<Map<String, Object>> findRelatorioAnaliticoReembarques(boolean filtraFranquia,  boolean isCSV, Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioAnaliticoReembarqueQuery.getQuerySimulacao(filtraFranquia,isCSV), 
																  parameters,
																  RelatorioAnaliticoReembarqueQuery.createConfigureSql(isCSV));
	}
	
	@SuppressWarnings("rawtypes")
	public List findValorTotalReembarque(YearMonthDay dtCompetencia, Long idFilial) {
		List<Object> param = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(reembarque.vlCte + reembarque.vlTonelada) as vlTotalReembarque ");
		sql.append(" from ").append(getPersistentClass().getName()).append(" reembarque ");
		sql.append(" join reembarque.filial f ");
		
		if (dtCompetencia != null || idFilial != null) {
			sql.append(" where ");
			
			if(dtCompetencia != null){
				YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMes(dtCompetencia);
				YearMonthDay dtCompetenciaFinal = FranqueadoUtils.buscarUltimoDiaMes(dtCompetencia);
				
				sql.append(" reembarque.dtCompetencia between ? and ? ");
				param.add(dtCompetenciaInicial);
				param.add(dtCompetenciaFinal);
				
				if(idFilial != null){
					sql.append(" and ");
				}
			}
			
			if(idFilial != null){
				sql.append(" f.idFilial = ? ");
				param.add(idFilial);
			}
		}
		
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<SimulacaoReembarqueDoctoServicoFranqueado> findServicosReembarque(YearMonthDay dtCompetencia, Long idFilial) {
		StringBuffer sql = new StringBuffer()
    	.append("select sum(rdsf.vlCte + rdsf.vlTonelada) AS VL_TOTAL_REEMBARQUE from ")
    	.append(getPersistentClass().getName()).append(" as rdsf ")
    	.append("where ")
		.append("	rdsf.dtCompetencia = :dtCompetencia ")
		.append("	and rdsf.filial.idFilial = :idFilial ");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dtCompetencia", dtCompetencia);
		params.put("idFilial", idFilial);
		
		List<SimulacaoReembarqueDoctoServicoFranqueado> list = (List<SimulacaoReembarqueDoctoServicoFranqueado>)getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
		return list;
	}
	
	public void storeAllNewSession(final List<SimulacaoReembarqueDoctoServicoFranqueado> simulacaoReembarqueDoctoServicoFranqueadoList) {
	 	List<SimulacaoReembarqueDoctoServicoFranqueado> simulacaoreembarqueDoctoServicoFranqueadoStore = new ArrayList<SimulacaoReembarqueDoctoServicoFranqueado>();
	 		
	 	for (int i = 0; i < simulacaoReembarqueDoctoServicoFranqueadoList.size(); i++  ) {
	 			
	 		simulacaoreembarqueDoctoServicoFranqueadoStore.add(simulacaoReembarqueDoctoServicoFranqueadoList.get(i));
	 			
	 		if((i*1) % ConstantesFranqueado.LIMITE_COMMIT == 0 || i == simulacaoReembarqueDoctoServicoFranqueadoList.size()-1 ){
	 			storeNewSession(simulacaoreembarqueDoctoServicoFranqueadoStore);
	 			simulacaoreembarqueDoctoServicoFranqueadoStore.clear();
	 		}
	 	}
	 }
	 
	private void storeNewSession(final List<SimulacaoReembarqueDoctoServicoFranqueado> simulacaoReembarqueDoctoServicoFranqueadoList) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
		 		try {
		 			session = getSessionFactory().openSession();
		 			session.beginTransaction();
		 					
		 			for (SimulacaoReembarqueDoctoServicoFranqueado simulacaoReembarqueDoctoServicoFranqueado : simulacaoReembarqueDoctoServicoFranqueadoList) {
		 				session.saveOrUpdate(simulacaoReembarqueDoctoServicoFranqueado);
		 			}
		 					
		 			session.getTransaction().commit();
		 		} finally {
		 			if(session != null) session.close();
		 		}
		 		return null;
 			}
		});
	}

	public void removeAll(final YearMonthDay dtCompentencia, final Long idFilial) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session = getSessionFactory().openSession();
					session.beginTransaction();
					
					Query query = null;
					
					StringBuffer sql = new StringBuffer()
			    	.append("delete from ")
			    	.append(SimulacaoReembarqueDoctoServicoFranqueado.class.getName()).append(" as srdsf ")
			    	.append("where srdsf.dtCompetencia = to_date(:dtCompetencia, 'dd/mm/yyyy') ")
					.append("	and srdsf.filial.idFilial = :idFilial ");

					query = session.createQuery(sql.toString());
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
	
}	
