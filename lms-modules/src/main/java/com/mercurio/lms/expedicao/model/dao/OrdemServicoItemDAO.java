package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.OrdemServicoItem;

public class OrdemServicoItemDAO extends BaseCrudDao<OrdemServicoItem, Long> {

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getPersistentClass() {
		return OrdemServicoItem.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<OrdemServicoItem> findByOrdemServico(Long idOrdemServico) {
		StringBuilder query = new StringBuilder()
		.append("from " + getPersistentClass().getName() + " as item ")
		.append("inner join fetch item.parcelaPreco pp ")
		.append("where ")
		.append("  item.ordemServico.idOrdemServico = ? ");
					
		return 	getAdsmHibernateTemplate().find(query.toString(), new Object[]{idOrdemServico});	
	}
	
	@SuppressWarnings("unchecked")
	public void storeFaturamentoItemByIds(List<Long> ids, Boolean blFaturado, Boolean blSemCobranca) {
		StringBuilder sql = new StringBuilder();
		TypedFlatMap criteria = new TypedFlatMap();  
				
		sql.append("UPDATE ORDEM_SERVICO_ITEM SET ");
		
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
		sql.append(" WHERE ID_ORDEM_SERVICO_ITEM IN (:idsItens) ");		
		
		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), criteria);
	}
}
