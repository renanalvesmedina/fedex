package com.mercurio.lms.expedicao.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.ServicoGeracaoAutomatica;

public class ServicoGeracaoAutomaticaDAO extends BaseCrudDao<ServicoGeracaoAutomatica, Long> {
	@Override
	@SuppressWarnings("rawtypes") 
	protected Class getPersistentClass() {
		return ServicoGeracaoAutomatica.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<ServicoGeracaoAutomatica> findByDoctoServico(Long idDoctoServico) {
		StringBuilder sb = new StringBuilder();
		sb.append("from  " + getPersistentClass().getName() + " sga ");
		sb.append("where sga.doctoServico.idDoctoServico = ? ");
		
		return getAdsmHibernateTemplate().find(sb.toString(), new Object[]{idDoctoServico});
	}
	
	public List<ServicoGeracaoAutomatica> findByDoctoServicoParcelaPreco(Long idDoctoServico, String[] cdParcelaPreco, Boolean blFinalizado) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idDoctoServico", idDoctoServico);
		params.put("cdParcelaPreco", cdParcelaPreco);
		
		final StringBuilder hql = new StringBuilder()
		.append(" select sga from ").append(getPersistentClass().getName()).append(" sga ")
		.append(" where sga.doctoServico.idDoctoServico = :idDoctoServico ")
		.append(" and sga.parcelaPreco.cdParcelaPreco in (:cdParcelaPreco) ");
		
		if(blFinalizado!= null){
			params.put("blFinalizado", blFinalizado);
			hql.append(" and sga.blFinalizado = :blFinalizado ");
		}
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
	}
	
	public List<ServicoGeracaoAutomatica> findByDoctoServicoTpExecucao(Long idDoctoServico, DomainValue[] tpExecucao) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idDoctoServico", idDoctoServico);
		params.put("tpExecucao", tpExecucao);
		
		final StringBuilder hql = new StringBuilder()
		.append(" select sga from ").append(getPersistentClass().getName()).append(" sga ")
		.append(" where sga.doctoServico.idDoctoServico = :idDoctoServico ")
		.append(" and sga.tpExecucao in (:tpExecucao) ");
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
	}
	
	@SuppressWarnings("unchecked")
	public void storeFaturamentoItemByIds(List<Long> ids, Boolean blFaturado, Boolean blSemCobranca) {
		StringBuilder sql = new StringBuilder();
		TypedFlatMap criteria = new TypedFlatMap();  
				
		sql.append("UPDATE SERVICO_GERACAO_AUTOMATICA SET ");
		
		if(blFaturado != null) {
			criteria.put("blFaturado", blFaturado);
			sql.append("BL_FATURADO = :blFaturado ");
		}
		if(blSemCobranca != null) {
			if(blFaturado != null) {
				sql.append(",");
			}
			criteria.put("blSemCobranca", blSemCobranca);
			sql.append("BL_SEM_COBRANCA = :blSemCobranca ");
		}
		
		criteria.put("idsItens", ids);
		sql.append(" WHERE ID_SERVICO_GERACAO_AUTOMATICA IN (:idsItens) ");		
		
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), criteria);
	}
	
	   @SuppressWarnings("unchecked")
	    public void storeFaturamentoItemByIds(List<Long> ids) {
	        StringBuilder sql = new StringBuilder();
	        TypedFlatMap criteria = new TypedFlatMap();  
	                
	        sql.append("UPDATE SERVICO_GERACAO_AUTOMATICA SET ");
	        sql.append("BL_FATURADO = BL_SEM_COBRANCA ");
	        
	        criteria.put("idsItens", ids);
	        sql.append(" WHERE ID_SERVICO_GERACAO_AUTOMATICA IN (:idsItens) ");     
	        
	        getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), criteria);
	    }
}
