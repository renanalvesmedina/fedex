package com.mercurio.lms.franqueados.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.LancamentoFranqueado;
import com.mercurio.lms.franqueados.model.report.RelatorioAnaliticoIREQuery;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;

public class LancamentoFranqueadoDAO extends BaseCrudDao<LancamentoFranqueado, Long> {

	@Override
	protected Class<LancamentoFranqueado> getPersistentClass() {
		return LancamentoFranqueado.class;
	}
		
	@Override
	public LancamentoFranqueado findById(Long id) {
		StringBuilder query = new StringBuilder();
		query.append("from " + getPersistentClass().getName() + " as lf ");
		query.append("inner join fetch lf.contaContabilFranqueado cc ");
		query.append("inner join fetch lf.franquia fr ");
		query.append("inner join fetch fr.filial fil ");
		query.append("inner join fetch fil.pessoa p ");
		query.append("left outer join fetch lf.pendencia pe ");
		query.append("left outer join fetch pe.ocorrencia o ");
		query.append("where ");
		query.append(" lf.id = :id ");
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("id", id);
		
		return (LancamentoFranqueado) getAdsmHibernateTemplate().findUniqueResult(query.toString(), criteria);
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		StringBuffer joins = new StringBuffer();
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("new Map(lancamentoFranqueado.idLancamentoFrq as idLancamentoFrq, "  
				            + "f.sgFilial as sgFilial,"
							+ "lancamentoFranqueado.dtCompetencia as dtCompetencia,"
							+ "cc.dsContaContabil as dsContaContabil,"
							+ " lancamentoFranqueado.dsLancamento as dsLancamento,"
							+ " lancamentoFranqueado.vlLancamento as vlLancamento,"
							+ " lancamentoFranqueado.tpSituacaoPendencia as tpSituacaoPendencia "
							+ ")");
		
		joins.append(" join lancamentoFranqueado.franquia fr ");
		joins.append(" join fr.filial f ");
		joins.append(" join lancamentoFranqueado.contaContabilFranqueado cc ");
		
		sql.addFrom( getPersistentClass().getName() + " lancamentoFranqueado " + joins.toString() );
		
		sql.addCriteria("lancamentoFranqueado.franquia.idFranquia","=",criteria.get("idFilial"));
		sql.addCriteria("lancamentoFranqueado.contaContabilFranqueado.idContaContabilFrq","=",criteria.get("idContaContabilFrq"));
		sql.addCriteria("lancamentoFranqueado.dtCompetencia",">=",criteria.get("competenciaIni"));
		sql.addCriteria("lancamentoFranqueado.dtCompetencia","<=",criteria.get("competenciaFim"));
		sql.addOrderBy("lancamentoFranqueado.dtCompetencia desc,f.sgFilial desc,cc.dsContaContabil desc");
		
		ResultSetPage result = getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), 
																		findDef.getPageSize(), sql.getCriteria());
		
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Integer getRowCount(Map criteria) {
		
		StringBuffer joins = new StringBuffer();
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("new Map(agrupado.rotaColetaEntrega as filialRotaColeta, agrupado.id as idAgrupadoSorter)");
		
		joins.append(" join lancamentoFranqueado.franquia fr ");
		joins.append(" join lancamentoFranqueado.contaContabilFranqueado cc ");
		
		sql.addFrom( getPersistentClass().getName() + " lancamentoFranqueado " + joins.toString() );
		
		sql.addCriteria("lancamentoFranqueado.franquia.idFranquia","=",criteria.get("idFilial"));
		sql.addCriteria("lancamentoFranqueado.contaContabilFranqueado.idContaContabilFrq","=",criteria.get("idContaContabilFrq"));
		sql.addCriteria("lancamentoFranqueado.dtCompetencia",">=",criteria.get("competenciaIni"));
		sql.addCriteria("lancamentoFranqueado.dtCompetencia","<=",criteria.get("competenciaFim"));
		
		Integer result = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(true), sql.getCriteria());
		return result;
	}

	/**
	 * 2.3 ...Verificar se a informação que está sendo importada já existe na tabela LANCAMENTO_FRQ
	 *  
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List findByIdFranquiaSiglaCodigoNroDocto(Long idFranquia, String sgDocto, Short cdDocto, Integer nrDocto, Long idLancamento) {
		List<Object> param = new ArrayList<Object>();
		param.add(idFranquia);
		param.add(sgDocto);
		param.add(cdDocto);
		param.add(nrDocto);
		
		StringBuilder sql = new StringBuilder();
		sql.append("select LF ");
		sql.append(" from " + LancamentoFranqueado.class.getName() + " LF ");
		sql.append(" join LF.franquia FF ");
		sql.append(" where FF.id = ? and");
		sql.append("	LF.sgDoctoInternacional = ? and");
		sql.append("	LF.cdDoctoInternacional = ? and");
		sql.append("	LF.nrDoctoInternacional = ? and");
		
		if(idLancamento != null){
			sql.append("	LF.id != ? and");
			param.add(idLancamento);
		}

		sql.append("	rownum = 1");



		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	/**
	 * Consulta TRI/IRE
	 *  
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getConsultaTRIIRE(YearMonthDay dtCompetencia, Long idFranquia) {
		List<Object> param = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(LF.vlLancamento) as vlTotalIre ");
		sql.append(" from " + LancamentoFranqueado.class.getName() + " LF ");
		sql.append(" join LF.contaContabilFranqueado CCF ");
		sql.append(" where CCF.tpContaContabil = 'IR' ");
		sql.append("	and LF.tpSituacaoPendencia = 'A' ");
		
		if(idFranquia != null){
			sql.append("	and LF.franquia.idFranquia = ? ");
			param.add(idFranquia);
		}
		
		if(dtCompetencia != null){
			YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMes(dtCompetencia);
			YearMonthDay dtCompetenciaFinal = FranqueadoUtils.buscarUltimoDiaMes(dtCompetencia);
			
			sql.append("	and LF.dtCompetencia between ? and ? ");
			param.add(dtCompetenciaInicial);
			param.add(dtCompetenciaFinal);
		}
		
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	
	
	@SuppressWarnings("rawtypes")
	public List getConsultaOver60(YearMonthDay dtCompetencia, Long idFranquia) {
		List<Object> param = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(LF.vlLancamento) as vlTotalOver60 ");
		sql.append(" from " + LancamentoFranqueado.class.getName() + " as LF ");
		sql.append(" join LF.contaContabilFranqueado as CCF ");
		
		sql.append(" where CCF.tpContaContabil = 'OV' ");
		sql.append(" and LF.tpSituacaoPendencia = 'A' ");
		
		if(dtCompetencia != null){
			YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMes(dtCompetencia);
			YearMonthDay dtCompetenciaFinal = FranqueadoUtils.buscarUltimoDiaMes(dtCompetencia);
			
			sql.append(" and LF.dtCompetencia between ? and ? ");
			param.add(dtCompetenciaInicial);
			param.add(dtCompetenciaFinal);
		}
		
		if(idFranquia != null){
			sql.append(" and LF.franquia.idFranquia = ? ");
			param.add(idFranquia);
		}
		
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaIndenizacoes(YearMonthDay dtCompetencia, Long idFranquia) {
		List<Object> param = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(LF.vlLancamento) as vlTotalIndenizacao ");
		sql.append(" from " + LancamentoFranqueado.class.getName() + " as LF ");
		sql.append(" join LF.contaContabilFranqueado as CCF ");
		
		sql.append(" where CCF.tpContaContabil in ('IA', 'IF', 'IO') ");
		sql.append(" and LF.tpSituacaoPendencia = 'A' ");
		
		if(dtCompetencia != null){
			YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMes(dtCompetencia);
			YearMonthDay dtCompetenciaFinal = FranqueadoUtils.buscarUltimoDiaMes(dtCompetencia);
			
			sql.append(" and LF.dtCompetencia between ? and ? ");
			param.add(dtCompetenciaInicial);
			param.add(dtCompetenciaFinal);
		}
		
		if(idFranquia != null){
			sql.append(" and LF.franquia.idFranquia = ? ");
			param.add(idFranquia);
		}
		
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaRecalculos(YearMonthDay dtCompetencia, Long idFranquia) {
		List<Object> param = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(DSF.vlDiferencaParticipacao) as vlTotalRecalculo ");
		sql.append(" from " + DoctoServicoFranqueado.class.getName() + " as DSF ");
		sql.append(" where ");
		sql.append(" DSF.doctoServicoFranqueadoOriginal is not null ");
		
		if (dtCompetencia != null || idFranquia != null) {
			sql.append(" and ");
			
			if(dtCompetencia != null){
				YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMes(dtCompetencia);
				YearMonthDay dtCompetenciaFinal = FranqueadoUtils.buscarUltimoDiaMes(dtCompetencia);
				
				sql.append(" DSF.dtCompetencia between ? and ? ");
				param.add(dtCompetenciaInicial);
				param.add(dtCompetenciaFinal);
				
				if (idFranquia != null) {
					sql.append(" and ");
				}
			}
			
			if(idFranquia != null){
				sql.append(" DSF.franquia.idFranquia = ? ");
				param.add(idFranquia);
			}
		}
		
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaRecalculosFrete(YearMonthDay dtCompetencia, Long idFranquia) {
		List<Object> param = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(DSF.vlDiferencaParticipacao) as vlTotalRecalculo ");
		sql.append(" from " + DoctoServicoFranqueado.class.getName() + " as DSF ");
		sql.append(" where DSF.tpFrete <> 'SE' ");
		sql.append(" and DSF.doctoServicoFranqueadoOriginal is not null ");
		
		if(dtCompetencia != null){
			sql.append(" and ");
			
			YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMes(dtCompetencia);
			YearMonthDay dtCompetenciaFinal = FranqueadoUtils.buscarUltimoDiaMes(dtCompetencia);
			
			sql.append(" DSF.dtCompetencia between ? and ? ");
			param.add(dtCompetenciaInicial);
			param.add(dtCompetenciaFinal);
			
		}
		
		if(idFranquia != null){
			sql.append(" and ");
			sql.append(" DSF.franquia.idFranquia = ? ");
			param.add(idFranquia);
		}
		
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaRecalculosServicos(YearMonthDay dtCompetencia, Long idFranquia) {
		List<Object> param = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(DSF.vlDiferencaParticipacao) as vlTotalRecalculo ");
		sql.append(" from " + DoctoServicoFranqueado.class.getName() + " as DSF ");
		sql.append(" where DSF.tpFrete = 'SE' ");
		sql.append("and DSF.doctoServicoFranqueadoOriginal is not NULL");
		
		if(dtCompetencia != null){
			sql.append(" and ");
			
			YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMes(dtCompetencia);
			YearMonthDay dtCompetenciaFinal = FranqueadoUtils.buscarUltimoDiaMes(dtCompetencia);
			
			sql.append(" DSF.dtCompetencia between ? and ? ");
			param.add(dtCompetenciaInicial);
			param.add(dtCompetenciaFinal);
			
		}
		
		if(idFranquia != null){
			sql.append(" and ");
			sql.append(" DSF.franquia.idFranquia = ? ");
			param.add(idFranquia);
		}
		
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaBDMs(YearMonthDay dtCompetencia, Long idFranquia) {
		List<Object> param = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(LF.vlLancamento) as vlTotalBDM ");
		sql.append(" from " + LancamentoFranqueado.class.getName() + " as LF ");
		sql.append(" join LF.contaContabilFranqueado as CCF ");
		
		sql.append(" where CCF.tpContaContabil = 'BD' ");
		sql.append(" and LF.tpSituacaoPendencia = 'A' ");
		
		if(dtCompetencia != null){
			YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMes(dtCompetencia);
			YearMonthDay dtCompetenciaFinal = FranqueadoUtils.buscarUltimoDiaMes(dtCompetencia);
			
			sql.append(" and LF.dtCompetencia between ? and ? ");
			param.add(dtCompetenciaInicial);
			param.add(dtCompetenciaFinal);
		}
		
		if(idFranquia != null){
			sql.append(" and LF.franquia.idFranquia = ? ");
			param.add(idFranquia);
		}
		
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaCreditosDiversos(YearMonthDay dtCompetencia, Long idFranquia) {
		List<Object> param = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(LF.vlLancamento) as vlTotalCredito ");
		sql.append(" from " + LancamentoFranqueado.class.getName() + " as LF ");
		sql.append(" join LF.contaContabilFranqueado as CCF ");
		
		sql.append(" where CCF.tpContaContabil in ('CD', 'CA') ");
		sql.append(" and LF.tpSituacaoPendencia = 'A' ");
		
		if(dtCompetencia != null){
			YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMes(dtCompetencia);
			YearMonthDay dtCompetenciaFinal = FranqueadoUtils.buscarUltimoDiaMes(dtCompetencia);
			
			sql.append(" and LF.dtCompetencia between ? and ? ");
			param.add(dtCompetenciaInicial);
			param.add(dtCompetenciaFinal);
		}
		
		if(idFranquia != null){
			sql.append(" and LF.franquia.idFranquia = ? ");
			param.add(idFranquia);
		}
		
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	@SuppressWarnings("rawtypes")
	public List getConsultaDebitosDiversos(YearMonthDay dtCompetencia, Long idFranquia) {
		List<Object> param = new ArrayList<Object>();
		
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(LF.vlLancamento) as vlTotalCredito ");
		sql.append(" from " + LancamentoFranqueado.class.getName() + " as LF ");
		sql.append(" join LF.contaContabilFranqueado as CCF ");
		
		sql.append(" where CCF.tpContaContabil in ('DD', 'DA') ");
		sql.append(" and LF.tpSituacaoPendencia = 'A' ");
		
		if(dtCompetencia != null){
			YearMonthDay dtCompetenciaInicial = FranqueadoUtils.buscarPrimeiroDiaMes(dtCompetencia);
			YearMonthDay dtCompetenciaFinal = FranqueadoUtils.buscarUltimoDiaMes(dtCompetencia);
			
			sql.append(" and LF.dtCompetencia between ? and ? ");
			param.add(dtCompetenciaInicial);
			param.add(dtCompetenciaFinal);
		}
		
		if(idFranquia != null){
			sql.append(" and LF.franquia.idFranquia = ? ");
			param.add(idFranquia);
		}
		
		return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
	
	public List<Map<String, Object>> findRelatorioAnaliticoIRE(boolean filtraFranquia, boolean isCSV, Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioAnaliticoIREQuery.getQuery(filtraFranquia, isCSV), 
																	parameters, 
																	RelatorioAnaliticoIREQuery.createConfigureSql(isCSV));
	}
	
	public void storeAllNewSession(final List<LancamentoFranqueado> lancamentoFranqueadoList) {
		List<LancamentoFranqueado> lancamentoFranqueadoStore = new ArrayList<LancamentoFranqueado>();
		
		for (int i = 0; i < lancamentoFranqueadoList.size(); i++) {
			
			lancamentoFranqueadoStore.add(lancamentoFranqueadoList.get(i));
			
			if((i+1) % ConstantesFranqueado.LIMITE_COMMIT == 0 || i == lancamentoFranqueadoList.size()-1 ){
				storeNewSession(lancamentoFranqueadoStore);
				lancamentoFranqueadoStore.clear();
			}
		}
	}

	private void storeNewSession(final List<LancamentoFranqueado> lancamentoFranqueadoList) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session = getSessionFactory().openSession();
					session.beginTransaction();
					
					for (LancamentoFranqueado lancamentoFranqueado : lancamentoFranqueadoList) {
						session.saveOrUpdate(lancamentoFranqueado);
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
