package com.mercurio.lms.franqueados.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.ReembarqueDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.report.RelatorioAnaliticoReembarqueQuery;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;

public class ReembarqueDoctoServicoFranqueadoDAO extends BaseCrudDao< ReembarqueDoctoServicoFranqueado, Long> {
	
	@Override
	protected Class<ReembarqueDoctoServicoFranqueado> getPersistentClass() {
		return ReembarqueDoctoServicoFranqueado.class;
	}
	

	 public void storeAllNewSession(final List<ReembarqueDoctoServicoFranqueado> reembarqueDoctoServicoFranqueadoList) {
		 	List<ReembarqueDoctoServicoFranqueado> reembarqueDoctoServicoFranqueadoStore = new ArrayList<ReembarqueDoctoServicoFranqueado>();
		 		
		 	for (int i = 0; i < reembarqueDoctoServicoFranqueadoList.size(); i++  ) {
		 			
		 		reembarqueDoctoServicoFranqueadoStore.add(reembarqueDoctoServicoFranqueadoList.get(i));
		 			
		 		if((i*1) % ConstantesFranqueado.LIMITE_COMMIT == 0 || i == reembarqueDoctoServicoFranqueadoList.size()-1 ){
		 			storeNewSession(reembarqueDoctoServicoFranqueadoStore);
		 			reembarqueDoctoServicoFranqueadoStore.clear();
		 		}
		 	}
		 }
		 
	 private void storeNewSession(final List<ReembarqueDoctoServicoFranqueado> reembarqueDoctoServicoFranqueadoList) {
		 getAdsmHibernateTemplate().execute(new HibernateCallback() {
			 public Object doInHibernate(Session session) throws HibernateException, SQLException {
		 		try {
		 			session = getSessionFactory().openSession();
		 			session.beginTransaction();
		 					
		 			for (ReembarqueDoctoServicoFranqueado reembarqueDoctoServicoFranqueado : reembarqueDoctoServicoFranqueadoList) {
		 				session.saveOrUpdate(reembarqueDoctoServicoFranqueado);
		 			}
		 					
		 			session.getTransaction().commit();
		 		} finally {
		 			if(session != null) session.close();
		 		}
		 		return null;
		 			}
		 });
	 }
		 	
	public List<Map<String, Object>> findRelatorioAnaliticoReembarques(boolean filtraFranquia,  boolean isCSV, Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioAnaliticoReembarqueQuery.getQuery(filtraFranquia,isCSV), 
																  parameters,
																  RelatorioAnaliticoReembarqueQuery.createConfigureSql(isCSV));
	}
	
	@SuppressWarnings("rawtypes")
	public List findValorTotalReembarque(YearMonthDay dtCompetencia, Long idFranquia) {
		List<Object> param = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(vlCte + vlTonelada) as vlTotalReembarque ");
		sql.append(" from " + ReembarqueDoctoServicoFranqueado.class.getName());
		
		if (dtCompetencia != null || idFranquia != null) {
			sql.append(" where ");
			
			if(dtCompetencia != null){
				YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMes(dtCompetencia);
				YearMonthDay dtCompetenciaFinal = FranqueadoUtils.buscarUltimoDiaMes(dtCompetencia);
				
				sql.append(" dtCompetencia between ? and ? ");
				param.add(dtCompetenciaInicial);
				param.add(dtCompetenciaFinal);
				
				if(idFranquia != null){
					sql.append(" and ");
				}
			}
			
			if(idFranquia != null){
				sql.append(" franquia.idFranquia = ? ");
				param.add(idFranquia);
			}
		}
		
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<ReembarqueDoctoServicoFranqueado> findServicosReembarque(YearMonthDay dtCompetencia, Long idFranquia) {
		StringBuffer sql = new StringBuffer()
    	.append("select sum(vlCte + vlTonelada) AS VL_TOTAL_REEMBARQUE from ")
    	.append(ReembarqueDoctoServicoFranqueado.class.getName()).append(" as rdsf ")
    	.append("where ")
		.append("	rdsf.dtCompetencia = :dtCompetencia ")
		.append("	and rdsf.franquia.idFranquia = :idFranquia ");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dtCompetencia", dtCompetencia);
		params.put("idFranquia", idFranquia);
		
		List<ReembarqueDoctoServicoFranqueado> list = (List<ReembarqueDoctoServicoFranqueado>)getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
		return list;
	}
	
			
		 	
}	
