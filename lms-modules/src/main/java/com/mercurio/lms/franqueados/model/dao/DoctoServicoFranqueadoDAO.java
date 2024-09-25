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
import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.report.RelatorioAnaliticoBDMQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioAnaliticoDocumentosCompetenciaAnteriorQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioAnaliticoDocumentosQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioAnaliticoFreteLocalQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioAnaliticoServicoAdicionalQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioBaixaCessaoCreditoQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioConsolidadoGeralQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioContabilLancamentosAnaliticoQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioPendenciaPagamentoQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioSinteticoGeralTipoFreteQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioSinteticoParticipacaoDocumentosFiscaisQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioSinteticoParticipacaoLancamentosQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioSinteticoParticipacaoQuery;
import com.mercurio.lms.franqueados.model.report.RelatorioSinteticoParticipacaoServicoAdicionalQuery;
import com.mercurio.lms.util.JTDateTimeUtils;

public class DoctoServicoFranqueadoDAO extends BaseCrudDao<DoctoServicoFranqueado, Long> {

	@Override
	protected Class<DoctoServicoFranqueado> getPersistentClass() {
		return DoctoServicoFranqueado.class;
	}
	
	public void removeByCompetencia(final YearMonthDay dtCompetencia, final Long idFranquia){
		StringBuffer sql = new StringBuffer()
    	.append(" DELETE FROM DOCTO_SERVICO_FRQ ")
    	.append(" WHERE ")
		.append(" DT_COMPETENCIA = to_date(:dtCompetencia, 'dd/mm/yyyy') ")
		.append(" AND ID_FRANQUIA = :idFranquia ");
		
		Map<String,Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("dtCompetencia", JTDateTimeUtils.formatDateYearMonthDayToString(dtCompetencia));
		parametersValues.put("idFranquia", idFranquia);
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}
	
	public void removeCifFob(final YearMonthDay dtCompentencia, final Long idFranquia) {
		
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session = getSessionFactory().openSession();
					session.beginTransaction();
					
					Query query = null;
					
					StringBuffer sql = new StringBuffer()
				    	.append("delete from ")
				    	.append(DoctoServicoFranqueado.class.getName()).append(" as dsf ")
				    	.append("where dsf.tpFrete in (:cifExpedido, :cifRecebido, :fobExpedido, :fobRecebido) ")
						.append("	and dsf.dtCompetencia = to_date(:dtCompetencia, 'dd/mm/yyyy') ")
						.append("	and dsf.franquia.idFranquia = :idFranquia ");

					query = session.createQuery(sql.toString());
					query.setString("cifExpedido", ConstantesFranqueado.CIF_EXPEDIDO);
					query.setString("cifRecebido", ConstantesFranqueado.CIF_RECEBIDO);
					query.setString("fobExpedido", ConstantesFranqueado.FOB_EXPEDIDO);
					query.setString("fobRecebido", ConstantesFranqueado.FOB_RECEBIDO);
					query.setString("dtCompetencia", dtCompentencia.toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
					query.setLong("idFranquia", idFranquia);
					
					query.executeUpdate();

					session.getTransaction().commit();
				} finally {
					if(session != null) session.close();
				}
				return null;
			}
		});
		
	}

	public void removeLocalByCompetencia(final YearMonthDay dtCompentencia, final Long idFranquia) {
		
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session = getSessionFactory().openSession();
					session.beginTransaction();
					
					Query query = null;
					
					StringBuffer sql = new StringBuffer()
			    	.append("delete from ")
			    	.append(DoctoServicoFranqueado.class.getName()).append(" as dsf ")
			    	.append("where dsf.tpFrete = :tpFrete ")
					.append("	and dsf.dtCompetencia = to_date(:dtCompetencia, 'dd/mm/yyyy') ")
					.append("	and dsf.franquia.idFranquia = :idFranquia ");

					query = session.createQuery(sql.toString());
					query.setString("tpFrete", ConstantesFranqueado.FRETE_LOCAL);
					query.setString("dtCompetencia", dtCompentencia.toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
					query.setLong("idFranquia", idFranquia);
					
					query.executeUpdate();
					

					session.getTransaction().commit();
				} finally {
					if(session != null) session.close();
				}
				return null;
			}
		});
	}
	
	public void removeServicoAdicionalByCompetencia(final YearMonthDay dtCompentencia, final Long idFranquia) {
		
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session = getSessionFactory().openSession();
					session.beginTransaction();
					
					Query query = null;
					
					StringBuffer sql = new StringBuffer()
			    	.append("delete from ")
			    	.append(DoctoServicoFranqueado.class.getName()).append(" as dsf ")
			    	.append("where dsf.tpFrete = :tpFrete ")
					.append("	and dsf.dtCompetencia = to_date(:dtCompetencia, 'dd/mm/yyyy') ")
					.append("	and dsf.franquia.idFranquia = :idFranquia ");

					query = session.createQuery(sql.toString());
					query.setString("tpFrete", ConstantesFranqueado.SERVICO_ADICIONAL);
					query.setString("dtCompetencia", dtCompentencia.toString(DateTimeFormat.forPattern("dd/MM/yyyy")));
					query.setLong("idFranquia", idFranquia);
					
					query.executeUpdate();
					

					session.getTransaction().commit();
				} finally {
					if(session != null) session.close();
				}
				return null;
			}
		});
		
	}

	@SuppressWarnings("unchecked")
	public List<DoctoServicoFranqueado> findByIdDoctoServicoAndFranquia(Long idDoctoServico, Long idFranquia) {
		StringBuffer sql = new StringBuffer()
		.append("select dsf from ")
			.append(DoctoServicoFranqueado.class.getName()).append(" as dsf ")
		.append(" where ")
			.append(" dsf.conhecimento.idDoctoServico = :idDoctoServico ")
			.append(" and dsf.franquia.idFranquia = :idFranquia ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idDoctoServico", idDoctoServico);
		params.put("idFranquia", idFranquia);
		
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
	}
	
	@SuppressWarnings("unchecked")
	public List<DoctoServicoFranqueado> findServicosAdicionais(YearMonthDay dtCompetencia, Long idFranquia) {
		StringBuffer sql = new StringBuffer()
    	.append("select sum(dsf.vlParticipacao) as VL_TOTAL_SERVICO from ")
    	.append(DoctoServicoFranqueado.class.getName()).append(" as dsf ")
    	.append("where dsf.tpFrete = :tpFrete ")
		.append("	and dsf.dtCompetencia = :dtCompetencia ")
		.append("	and dsf.franquia.idFranquia = :idFranquia ")
		.append("	and dsf.doctoServicoFranqueadoOriginal is null ");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tpFrete", ConstantesFranqueado.SERVICO_ADICIONAL);
		params.put("dtCompetencia", dtCompetencia);
		params.put("idFranquia", idFranquia);
		
		List<DoctoServicoFranqueado> list = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
		return list;
	}
	
	public List<Map<String, Object>> findRelatorioContabilLancamentosAnalitico(Map<String,Object> parameters){
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioContabilLancamentosAnaliticoQuery.getQuery(parameters), 
																	parameters, 
																	RelatorioContabilLancamentosAnaliticoQuery.createConfigureSql());
	}
	
	public List<Map<String, Object>> findRelatorioAnaliticoDocumentos(boolean filtraFranquia,  boolean isCSV, Map<String,Object> parameters){
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioAnaliticoDocumentosQuery.getQuery(filtraFranquia,isCSV), 
																	parameters, 
																	RelatorioAnaliticoDocumentosQuery.createConfigureSql(isCSV));
	}


	public List<Map<String, Object>> findRelatorioAnaliticoFretesLocal(boolean filtraFranquia, boolean isCSV, Map<String, Object> parameters) {		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioAnaliticoFreteLocalQuery.getQuery(filtraFranquia,isCSV), 
																	parameters, 
																	RelatorioAnaliticoFreteLocalQuery.createConfigureSql(isCSV));
	}
	
	public List<Map<String, Object>> findRelatorioAnaliticoDocumentosCompetenciaAnterior(boolean filtraFranquia, boolean isCSV, Map<String, Object> parameters) {
		
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioAnaliticoDocumentosCompetenciaAnteriorQuery.getQuery(filtraFranquia,isCSV), 
																	parameters, 
																	RelatorioAnaliticoDocumentosCompetenciaAnteriorQuery.createConfigureSql(isCSV));
	}
	
	public List<Map<String, Object>> findRelatorioAnaliticoBDM(boolean filtraFranquia, boolean isCSV, Map<String, Object> parameters) {	
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioAnaliticoBDMQuery.getQuery(filtraFranquia,isCSV), 
																	parameters, 
																	RelatorioAnaliticoBDMQuery.createConfigureSql(isCSV));
	}
	
	public List<Map<String, Object>> findRelatorioAnaliticoServicosAdicionais(boolean filtraFranquia, boolean isCSV, Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioAnaliticoServicoAdicionalQuery.getQuery(filtraFranquia, isCSV), 
																	parameters, 
																	RelatorioAnaliticoServicoAdicionalQuery.createConfigureSql(isCSV));
	}

	
	public List<Map<String, Object>> findRelatorioBaixaCessaoCredito(boolean filtraFranquia,Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioBaixaCessaoCreditoQuery.getQuery(filtraFranquia), 
																	parameters, 
																	RelatorioBaixaCessaoCreditoQuery.createConfigureSql());
	}
	
	public List<Map<String, Object>> findRelatorioPendenciaPagamento(boolean filtraFranquia,Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioPendenciaPagamentoQuery.getQuery(filtraFranquia), 
																	parameters, 
																	RelatorioPendenciaPagamentoQuery.createConfigureSql());
	}

	
	public List<Map<String, Object>> findRelatorioSinteticoDefault(Map<String, Object> parameters, boolean sum) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioSinteticoParticipacaoQuery.getQueryDefault(sum), 
																	parameters, 
																	RelatorioSinteticoParticipacaoQuery.createConfigureSql(sum));
	}
	
	
	public List<Map<String, Object>> findRelatorioSinteticoColetaEntrega(Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioSinteticoParticipacaoQuery.getQueryOrderedByColetaEntrega(), 
																	parameters, 
																	RelatorioSinteticoParticipacaoQuery.createConfigureSql(false));
	}
	

	public List<Map<String, Object>> findRelatorioSinteticoServicoAdicional(Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioSinteticoParticipacaoServicoAdicionalQuery.getQuery(), 
																	parameters, 
																	RelatorioSinteticoParticipacaoServicoAdicionalQuery.createConfigureSql());
	}

	public List<Map<String, Object>> findRelatorioSinteticoLancamento(Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioSinteticoParticipacaoLancamentosQuery.getQuery(), 
																	parameters, 
																	RelatorioSinteticoParticipacaoLancamentosQuery.createConfigureSql());
	}
	

	public List<Map<String, Object>> findRelatorioSinteticoDocumentosFiscais(Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioSinteticoParticipacaoDocumentosFiscaisQuery.getQuery(), 
																	parameters, 
																	RelatorioSinteticoParticipacaoDocumentosFiscaisQuery.createConfigureSql());
	}
	
	public List<Map<String, Object>> findRelatorioConsolidadoGeral(Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioConsolidadoGeralQuery.getQuery(parameters), 
																	parameters, 
																	RelatorioConsolidadoGeralQuery.createConfigureSql());
	}
	
	public List<Map<String, Object>> findRelatorioSinteticoGeralTipoFrete(Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioSinteticoGeralTipoFreteQuery.getQuery(parameters), 
				parameters, 
				RelatorioSinteticoGeralTipoFreteQuery.createConfigureSql());
	}
	
	public void storeAllNewSession(final List<DoctoServicoFranqueado> doctoServicoFranqueadoList) {
		List<DoctoServicoFranqueado> doctoServicoFranqueadoStore = new ArrayList<DoctoServicoFranqueado>();
		
		for (int i = 0; i < doctoServicoFranqueadoList.size(); i++) {
			
			doctoServicoFranqueadoStore.add(doctoServicoFranqueadoList.get(i));
			
			if((i+1) % ConstantesFranqueado.LIMITE_COMMIT == 0 || i == doctoServicoFranqueadoList.size()-1 ){
				storeNewSession(doctoServicoFranqueadoStore);
				doctoServicoFranqueadoStore.clear();
			}
		}
	}

	private void storeNewSession(final List<DoctoServicoFranqueado> doctoServicoFranqueadoList) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session = getSessionFactory().openSession();
					session.beginTransaction();
					
					for (DoctoServicoFranqueado doctoServicoFranqueado : doctoServicoFranqueadoList) {
						session.saveOrUpdate(doctoServicoFranqueado);
					}
					
					session.getTransaction().commit();
				} finally {
					if(session != null) session.close();
				}
				return null;
			}
		});
	}


}
