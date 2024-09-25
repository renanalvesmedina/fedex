package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.OrdemServicoItem;
import com.mercurio.lms.expedicao.model.dao.OrdemServicoItemDAO;

public class OrdemServicoItemService extends CrudService<OrdemServicoItem, Long> {	
	@Override
	public void removeById(Long id) {			
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {		
		super.removeByIds(ids);		
	}	
	
	@Override
	public Serializable findById(Long idOrdemServicoItem){
		return super.findById(idOrdemServicoItem);
	}
	
	public List<OrdemServicoItem> findByOrdemServico(Long idOrdemServico) {
		if(idOrdemServico == null) {					
			return new ArrayList<OrdemServicoItem>();					
		}
		return getOrdemServicoItemDAO().findByOrdemServico(idOrdemServico);
	}
	
	public List<Long> findIdsByOrdemServico(Long idOrdemServico) {
		List<OrdemServicoItem> itens = findByOrdemServico(idOrdemServico);
		List<Long> ids = new ArrayList<Long>();
		
		if(itens != null) {
			for(OrdemServicoItem item : itens) {
				ids.add(item.getIdOrdemServicoItem());
			}
		}
		
		return ids;
	}
	
	public void storeFaturamentoItemByIds(List<Long> ids, Boolean blFaturado, Boolean blSemCobranca) {
		if(ids != null && ids.size() > 0) {
			getOrdemServicoItemDAO().storeFaturamentoItemByIds(ids, blFaturado, blSemCobranca);
		}
	}
	
	public Serializable store(OrdemServicoItem bean) {
		return super.store(bean);
	}
	
	public void setOrdemServicoItemDAO(OrdemServicoItemDAO ordemServicoItemDAO) {
		setDao( ordemServicoItemDAO );
	}

	private OrdemServicoItemDAO getOrdemServicoItemDAO() {
		return (OrdemServicoItemDAO) getDao();
	}
}
