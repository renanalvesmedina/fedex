package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.OrdemServicoDocumento;
import com.mercurio.lms.expedicao.model.dao.OrdemServicoDocumentoDAO;

public class OrdemServicoDocumentoService extends CrudService<OrdemServicoDocumento, Long> {
	@Override
	public void removeById(Long id) {			
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {		
		super.removeByIds(ids);		
	}
	
	public void removeByIdOrdemServicoByNotInIds(Long idOrdemServico, List<Long> ids) {		
		getOrdemServicoDocumentoDAO().removeByIdOrdemServicoByNotInIds(idOrdemServico, ids);		
	}
	
	public List<OrdemServicoDocumento> findByOrdemServico(Long idOrdemServico) {
		return getOrdemServicoDocumentoDAO().findByOrdemServico(idOrdemServico);
	}
	
	public List<Long> findIdsByOrdemServico(Long idOrdemServico) {
		List<OrdemServicoDocumento> doctos = findByOrdemServico(idOrdemServico);
		List<Long> ids = new ArrayList<Long>();
		
		if(doctos != null) {
			for(OrdemServicoDocumento docto : doctos) {
				ids.add(docto.getIdOrdemServicoDocumento());
			}
		}
		
		return ids;
	}
	
	public void setOrdemServicoDocumentoDAO(OrdemServicoDocumentoDAO ordemServicoDocumentoDAO) {
		setDao( ordemServicoDocumentoDAO );
	}

	private OrdemServicoDocumentoDAO getOrdemServicoDocumentoDAO() {
		return (OrdemServicoDocumentoDAO) getDao();
	}
}
