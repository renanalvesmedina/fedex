package com.mercurio.lms.ppd.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.PpdStatusRecibo;
import com.mercurio.lms.ppd.model.enums.PpdTipoDataRecibo;

public class PpdReciboDAO extends BaseCrudDao<PpdRecibo, Long>  {

	@Override
	protected Class getPersistentClass() {		
		return PpdRecibo.class;
	}
		
	@SuppressWarnings("unchecked")
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("naturezaProduto", FetchMode.JOIN);		
		lazyFindById.put("pessoa", FetchMode.JOIN);
		lazyFindById.put("formaPgto", FetchMode.JOIN);			
		lazyFindById.put("filial", FetchMode.JOIN);			
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);
	}
	
	public void store(PpdRecibo reciboIndenizacaoPpd) {
		super.store(reciboIndenizacaoPpd);
	}
	
	
	public PpdRecibo findById(Long id) {
		return (PpdRecibo)super.findById(id);
	}
	
	public PpdRecibo findByRecibo(Long idFilialRecibo, Long nrRecibo, YearMonthDay dtRecibo) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("recibo");
		
		hql.addLeftOuterJoin(getPersistentClass().getName(),"recibo");													   
		hql.addCriteria("recibo.nrRecibo","=",nrRecibo);
		hql.addCriteria("recibo.dtRecibo","=",dtRecibo);
		hql.addCriteria("recibo.filial.idFilial","=",idFilialRecibo);			
		
		return (PpdRecibo)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}
	
	public PpdRecibo findByRecibo(Long idFilialRecibo, Long nrRecibo, Long nrCTRC) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("recibo");
		
		hql.addLeftOuterJoin(getPersistentClass().getName(),"recibo");													   
		hql.addCriteria("recibo.nrRecibo","=",nrRecibo);
		hql.addCriteria("recibo.nrCtrc","=", nrCTRC);
		hql.addCriteria("recibo.filial.idFilial","=",idFilialRecibo);			
		
		return (PpdRecibo)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}

	public List<PpdRecibo> findByConhecimento(String sgFilialOrigem, Long nrCtrc, YearMonthDay dtCtrc) {
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("recibo");
		
		hql.addLeftOuterJoin(getPersistentClass().getName(),"recibo");													   
		hql.addCriteria("recibo.nrCtrc","=",nrCtrc);
		hql.addCriteria("recibo.dtEmissaoCtrc","=",dtCtrc);
		hql.addCriteria("recibo.sgFilialOrigem","=",sgFilialOrigem);	
		
		hql.addOrderBy("recibo.dtRecibo","desc");
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	public List<PpdRecibo> findExcel(Map<String,Object> criteria) {			
		StringBuilder query = getSqlFindPaginated(criteria);
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(),criteria);
	}
		
	public ResultSetPage<PpdRecibo> findPaginated(PaginatedQuery paginatedQuery) {	
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		StringBuilder query = getSqlFindPaginated(criteria);
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery,query.toString());
	}
	
	public Integer getRowCount(Map criteria) {
		StringBuilder query = getSqlFindPaginated(criteria);
		
		return getAdsmHibernateTemplate().getRowCountForQuery(query.toString(), criteria);
	}
	
	private StringBuilder getSqlFindPaginated(Map<String, Object> criteria) {
		StringBuilder query = new StringBuilder();
		
		query
		.append("from " + getPersistentClass().getName() + " as recibo ")
		.append("	left join fetch recibo.naturezaProduto as natureza ")		
		.append("	left join fetch recibo.pessoa as pessoa ")
		.append("	left join fetch recibo.formaPgto as formaPgto ")
		.append("	left join fetch recibo.filial as filial ") 
		.append("	left join fetch recibo.filial.pessoa as pessoa ")	
		.append("where 1=1 ");	
		
		if(MapUtils.getObject(criteria, "idRecibo") != null) {
			query.append(" and recibo.idRecibo = :idRecibo");
		}
		
		StringBuilder sbRecibos = null;
		if (MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.LOTE.toString()) ||
				 MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.ENVIO.toString()) ||					 
				 MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECEBIMENTO.toString())) {
			sbRecibos = new StringBuilder();
			sbRecibos.append(" and exists (");
			sbRecibos.append("select status.recibo from " + PpdStatusRecibo.class.getName() + " as status ");				
			sbRecibos.append(" where recibo.id = status.recibo.id ");
			//Adição ao lote
			if (MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.LOTE.toString()))
				sbRecibos.append("and status.tpStatusRecibo = 'L' ");
			//Envio JDE
			if (MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.ENVIO.toString()))
				sbRecibos.append("and status.tpStatusRecibo = 'E' ");				
			//Recebimento documentação
			if (MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECEBIMENTO.toString()))
				sbRecibos.append("and status.tpStatusRecibo = 'R' ");

		}		
		
		if(MapUtils.getObject(criteria, "dtReciboInicial") != null) {
			//Data de emissão do CTRC
			if(MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.CTRC.toString()))
				query.append(" and recibo.dtEmissaoCtrc >= :dtReciboInicial");
			//Data de emissão do Recibo
			else if(MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECIBO.toString()))
				query.append(" and recibo.dtRecibo >= :dtReciboInicial");
			//Data de emissão do RNC
			else if(MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RNC.toString()))
				query.append(" and recibo.dtEmissaoRnc >= :dtReciboInicial");
			//Data de pagamento
			else if(MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.PAGAMENTO.toString()))
				query.append("and recibo.dtPagto >= :dtReciboInicial ");
			//Filtro por data de status
			else if (MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.LOTE.toString()) ||
					 MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.ENVIO.toString()) ||					 
					 MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECEBIMENTO.toString())) {
				sbRecibos.append("and trunc(status.dhStatusRecibo.value) >= :dtReciboInicial ");
			}			
		}
		if(MapUtils.getObject(criteria, "dtReciboFinal") != null) {
			//Data de emissão do CTRC
			if(MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.CTRC.toString()))
				query.append(" and recibo.dtEmissaoCtrc <= :dtReciboFinal");
			//Data de emissão do Recibo
			else if(MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECIBO.toString()))
				query.append(" and recibo.dtRecibo <= :dtReciboFinal");
			//Data de emissão do RNC
			else if(MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RNC.toString()))
				query.append(" and recibo.dtEmissaoRnc <= :dtReciboFinal");
			//Data de pagamento
			else if (MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.PAGAMENTO.toString()))
				query.append("and recibo.dtPagto <= :dtReciboFinal");
			//Data de adição do Recibo no lote 
			else if (MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.LOTE.toString()) ||
					 MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.ENVIO.toString()) ||					 
					 MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECEBIMENTO.toString())) {
				sbRecibos.append("and trunc(status.dhStatusRecibo.value) <= :dtReciboFinal ");
			}					
		}

		if (MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.LOTE.toString()) ||
				 MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.ENVIO.toString()) ||					 
				 MapUtils.getString(criteria, "sgTipoDataRecibo").equals(PpdTipoDataRecibo.RECEBIMENTO.toString())) {
			sbRecibos.append(")");
			query.append(sbRecibos);
		}	
		
		if(MapUtils.getObject(criteria, "vlIndenizacaoInicial") != null) { 
			query.append(" and recibo.vlIndenizacao >= :vlIndenizacaoInicial");
		}
		if(MapUtils.getObject(criteria, "vlIndenizacaoFinal") != null) {
			query.append(" and recibo.vlIndenizacao <= :vlIndenizacaoFinal");		
		}
		if(MapUtils.getObject(criteria, "idFilial") != null) {
			query.append(" and recibo.filial.idFilial = :idFilial");		
		}
		if(MapUtils.getObject(criteria, "sgFilialOrigem") != null) {			
			query.append(" and recibo.sgFilialOrigem like :sgFilialOrigem");
		}
		if(MapUtils.getObject(criteria, "nrCtrc") != null) {
			query.append(" and recibo.nrCtrc = :nrCtrc");
		}
		if(MapUtils.getObject(criteria, "sgFilialRnc") != null) {			
			query.append(" and recibo.sgFilialRnc like :sgFilialRnc");
		}
		if(MapUtils.getObject(criteria, "nrRnc") != null) {
			query.append(" and recibo.nrRnc = :nrRnc");
		}
		if(MapUtils.getObject(criteria, "tpStatus") != null) {
			query.append(" and recibo.tpStatus = :tpStatus");
		}		
		if(MapUtils.getObject(criteria, "idPessoa") != null) {
			query.append(" and recibo.pessoa.idPessoa = :idPessoa");
		}
		if(MapUtils.getObject(criteria, "tpIndenizacao") != null) {
			query.append(" and recibo.tpIndenizacao = :tpIndenizacao");
		}
		if(MapUtils.getObject(criteria, "nrRecibo") != null) {
			query.append(" and recibo.nrRecibo = :nrRecibo");
		}
		if(MapUtils.getObject(criteria, "idNaturezaProduto") != null) {
			query.append(" and recibo.naturezaProduto.idNaturezaProduto = :idNaturezaProduto");
		}
		if(MapUtils.getObject(criteria, "tpLocalidade") != null) {
			query.append(" and recibo.tpLocalidade = :tpLocalidade");
		}
		if(MapUtils.getObject(criteria, "sgFilialLocal1") != null) {
			query.append(" and recibo.sgFilialLocal1 like :sgFilialLocal1");
		}
		if(MapUtils.getObject(criteria, "sgFilialLocal2") != null) {
			query.append(" and recibo.sgFilialLocal2 like :sgFilialLocal2");
		}		
		if(MapUtils.getObject(criteria, "idsFiliais") != null) {			
			List<Long> filiais = (List<Long>)criteria.get("idsFiliais"); 			
			query.append(" and recibo.filial.idFilial in (");
			for(int i=0; i<filiais.size(); i++) {			
				query.append(filiais.get(i));				
				if(i != filiais.size() - 1)
					query.append(",");
				else
					query.append(")");
			}
		}		
		
		query.append(" order by recibo.dtRecibo desc, filial.sgFilial asc, recibo.nrRecibo desc ");
		
		return query;
	}
	
	/**
	 * Solicitação 21.06.01.02 Aviso de pagamento para Clientes
	 * 
	 * @param tpStatusIndenizacao
	 * @param dtPagamentoEfetuado
	 * @param blEmailPagto
	 * @return lista de Recibo Indenização de acordo com tpStatusIndenizacao, dtPagamentoEfetuado e blEmailPagto
	 */
	public List<PpdRecibo> findReciboIndenizacaoByStatusIndenizacaoDtPagtoEfetuadoBlEmailPagto(String tpStatusIndenizacao,
					YearMonthDay dtPagamentoEfetuado, Boolean blEmailPagto){
		
		 StringBuffer hql = new StringBuffer();
		 hql.append( " select distinct reciboIndenizacao " )
		 	.append( " from " + PpdRecibo.class.getName() + " as reciboIndenizacao " )
			.append( " inner join reciboIndenizacao.pessoa as favorecido " );
		
		 SqlTemplate sql = new SqlTemplate();
		 sql.addCriteria("reciboIndenizacao.tpStatus", "=", tpStatusIndenizacao);
		 sql.addCriteria("reciboIndenizacao.dtPagto", "<=", dtPagamentoEfetuado);
		 sql.addCriteria("reciboIndenizacao.blEmailPgto", "=", blEmailPagto);
		 
		 hql.append(sql.getSql());
		 List<PpdRecibo> reciboIndenizacaoList = getAdsmHibernateTemplate().find(hql.toString(), sql.getCriteria());
		 return reciboIndenizacaoList;						
}	
	
	
}	
