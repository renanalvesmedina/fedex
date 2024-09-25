package com.mercurio.lms.edi.model.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Predicate;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.LogEDIDetalhe;




/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class LogEDIDetalheDAO extends BaseCrudDao<LogEDIDetalhe, Long>{

	private static final long DAY_MILIS = 86400000l;
	
	@Override
	public Class getPersistentClass() {		
		return LogEDIDetalhe.class;
	}
		
	private StringBuilder getSqlFindPaginated(TypedFlatMap criteria, boolean find) {
		StringBuilder query = new StringBuilder();
		
		if(find){
			query.append(" SELECT logArqDet ");
		}
		query.append(" FROM "+LogEDIDetalhe.class.getName()+" as logArqDet where 1=1 ");
		
	
		if(MapUtils.getObject(criteria, "tiposNotas") != null && ((String)MapUtils.getObject(criteria, "tiposNotas")).equalsIgnoreCase("erro")) {
			query.append("  and logArqDet.status = 'Erro' ");
		}	
		if(MapUtils.getObject(criteria, "idLog") != null) {
			query.append("  and logArqDet.logEDI.idLogEdi =:idLog ");
		}	
		if(MapUtils.getObject(criteria, "numeroNotaFiscal") != null) {
			query.append("  and logArqDet.nrNotaFiscal =:numeroNotaFiscal ");
		}	
		if(find){
			query.append("  order by logArqDet.nrNotaFiscal asc ");
		}
		
		return query;
	} 
	@SuppressWarnings("unchecked")
	public ResultSetPage findPaginatedLogDetalhe(TypedFlatMap criteria, FindDefinition findDef) {
		StringBuilder query = this.getSqlFindPaginated(criteria,true);		
		String sql = query.toString();
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql, findDef.getCurrentPage(), findDef.getPageSize(),criteria); 
		return rsp;
	}
	public Integer getRowCountLogDetalhe(TypedFlatMap criteria) {
		StringBuilder query = this.getSqlFindPaginated(criteria,false);
		return getAdsmHibernateTemplate().getRowCountForQuery(query.toString(),criteria); 
	}
	public Long findSequence(){		
		return Long.valueOf(getSession().createSQLQuery("select LOG_ARQUIVO_EDI_DETALHE_SQ.nextval from dual").uniqueResult().toString());
	}
	
	
	
	public List<LogEDIDetalhe> findByNrCCEReprocessamento(Long nrCce, String cnpjReme){
		String hql = "select l from LogEDIDetalhe l, CCEItem item where "
				+ " l.chaveNfe = item.nrChave";
				
				hql = hql.concat(" and item.cce.idCCE = :nrCce ");
				hql = hql.concat(" and item.nrChave not in (select n.chaveNfe from NotaFiscalEdi n where n.chaveNfe is not null)");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nrCce", nrCce);
		return (List<LogEDIDetalhe>) getAdsmHibernateTemplate().findByNamedParam(hql, params);
	}

	/**
	 * Busca o ultimo registro de log de uma nota fiscal que ja foi processada
	 * 
	 * 
	 * @param cnpjReme
	 * @param nrNotaFiscal
	 * @return
	 */
	public LogEDIDetalhe findByNrNotaFiscalReprocessamento(String cnpjReme,
			Integer nrNotaFiscal) {
		String hql = "select l from LogEDIDetalhe l  "
				+ " where l.cnpjReme  = :cnpjReme "
				+ " and l.nrNotaFiscal = :nrNotaFiscal"
				+ " and not exists (select n from NotaFiscalEdi n where n.cnpjReme = :cnpjReme and n.nrNotaFiscal = :nrNotaFiscal)"
				+ " order by l.idLogEdiDetalhe desc";
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nrNotaFiscal", nrNotaFiscal);
		params.put("cnpjReme", Long.valueOf(cnpjReme));
		
		List<LogEDIDetalhe> result = getAdsmHibernateTemplate().findByNamedParam(hql, params);
		
		if (result != null && !result.isEmpty()){
			return result.get(0);
		}
		
		return null;
	}

	public List<LogEDIDetalhe> findByDoctoClienteReprocessamento(
			String cnpjReme, String dsCampoCliente ,String nrDoctoCliente, String tpProcessamento, Long nrDiasMaxLog) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		hql.append("select l from LogEDIComplemento lc join lc.logEDIDetalhe l")
			.append(" where " )
			.append(" lc.nomeComplemento = :dsCampoCliente ")
			.append(" and lc.valorComplemento = :nrDoctoCliente");
			
		
		if ("T".equals(tpProcessamento)){ 
			hql.append( " and l.cnpjTomador = :cnpjCliente ");
		}else{
			hql.append( " and l.cnpjReme = :cnpjCliente ");
		}
		params.put("cnpjCliente", Long.valueOf(cnpjReme));

		if (nrDiasMaxLog != null && nrDiasMaxLog > 0){
			long maxDiasMilis = nrDiasMaxLog * DAY_MILIS;
			params.put("dataLog", new Date(new Date().getTime() - maxDiasMilis));
			hql.append( " and l.dataLog >= :dataLog ");
		}
		
		hql.append(" and not exists (select n from NotaFiscalEdi n where n.cnpjReme = l.cnpjReme and n.nrNotaFiscal = l.nrNotaFiscal) "
				+ " order by l.id desc"); 
		 
		
		params.put("dsCampoCliente", dsCampoCliente);
		params.put("nrDoctoCliente", nrDoctoCliente);
		
		List<LogEDIDetalhe> rawList = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
		
		List<LogEDIDetalhe> filteredList = new ArrayList<LogEDIDetalhe>();
		
		for(LogEDIDetalhe logEDIDetalhe:rawList){
		
			final LogEDIDetalhe logCompare = logEDIDetalhe; 
			
			
			boolean exists = CollectionUtils.exists(filteredList, new Predicate() {
				@Override
				public boolean evaluate(Object arg0) {
					return logCompare.getNrNotaFiscal().equals(((LogEDIDetalhe)arg0).getNrNotaFiscal());
				}
			} );
			
			if (!exists){
				filteredList.add(logEDIDetalhe);
			}
		}
		
		return filteredList;
	}

	public LogEDIDetalhe findByCnpjRemeNrNota(Long cnpjReme,Integer nrNotaFiscal) {
		
		String hql = "select l from LogEDIDetalhe l where l.cnpjReme = :cnpjReme and l.nrNotaFiscal = :nrNotaFiscal order by l.idLogEdiDetalhe desc";
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("cnpjReme", cnpjReme);
		parameters.put("nrNotaFiscal", nrNotaFiscal);
		
		List<LogEDIDetalhe> result = getAdsmHibernateTemplate().findByNamedParam(hql, parameters);
		if (result != null && result.size() > 0){
			return result.get(0);
		}
		
		return null;
	}

	public LogEDIDetalhe findByIdNotaFiscalEDI(Long idNotaFiscalEdi) {
		String hql = "select l from LogEDIDetalhe l, NotaFiscalEdi nfe "
				+ " where l.cnpjReme = nfe.cnpjReme "
				+ " and l.nrNotaFiscal = nfe.nrNotaFiscal "
				+ " and nfe.id = :idNotaFiscalEdi"
				+ " order by l.idLogEdiDetalhe desc ";
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idNotaFiscalEdi", idNotaFiscalEdi);
		
		List<LogEDIDetalhe> result = getAdsmHibernateTemplate().findByNamedParam(hql, parameters);
		if (result != null && result.size() > 0){
			return result.get(0);
		}
		
		return null;
	}

	public List<LogEDIDetalhe> findByNrNotaFiscalReprocessamento
	(String cnpjReme, Integer nrNotaFiscalInicial, Integer nrNotaFiscalFinal) {

		StringBuilder hql = new StringBuilder();

		hql.append("select l from LogEDIDetalhe l  ");
		hql.append(" where l.cnpjReme  = :cnpjReme ");
		hql.append(" and l.nrNotaFiscal = :nrNotaFiscal");
		hql.append( " and not exists (select n from NotaFiscalEdi n where n.cnpjReme = :cnpjReme ");
		hql.append( " and n.nrNotaFiscal >= :nrNotaFiscalInicial and n.nrNotaFiscal <= :nrNotaFiscalFinal)");
		hql.append( " order by l.idLogEdiDetalhe desc");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cnpjReme", Long.valueOf(cnpjReme));
		params.put("nrNotaFiscalInicial", nrNotaFiscalInicial);
		params.put("nrNotaFiscalFinal", nrNotaFiscalFinal);

		List<LogEDIDetalhe> result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);

		return result;
	}

}

