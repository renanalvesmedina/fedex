package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.expedicao.model.OrdemServicoAnexo;
import com.mercurio.lms.expedicao.model.dao.OrdemServicoAnexoDAO;

public class OrdemServicoAnexoService extends CrudService<OrdemServicoAnexo, Long> {	
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {		
		super.removeByIds(ids);		
	}
	
	public List<OrdemServicoAnexo> findByOrdemServico(Long idOrdemServico) {
		if(idOrdemServico == null) {					
			return new ArrayList<OrdemServicoAnexo>();					
		}
		return getOrdemServicoAnexoDAO().findByOrdemServico(idOrdemServico);
	}
	
	public List<Long> findIdsByOrdemServico(Long idOrdemServico) {
		List<OrdemServicoAnexo> anexos = findByOrdemServico(idOrdemServico);
		List<Long> ids = new ArrayList<Long>();
		
		if(anexos != null) {
			for(OrdemServicoAnexo anexo : anexos) {
				ids.add(anexo.getIdOrdemServicoAnexo());
			}
		}
		
		return ids;
	}
	
	public OrdemServicoAnexo findById(Long id) {
		return (OrdemServicoAnexo) super.findById(id);
	}
	
	/**
	 * LMS-6538 - Busca página de <tt>OrdemServicoAnexo</tt> de determinada
	 * <tt>PreFaturaServicoItem</tt>, incluindo dados de <tt>ParcelaPreco</tt>
	 * para cada anexo.
	 * 
	 * @param findDefinition
	 *            definições de página da grid para busca dos dados
	 * @param idPreFaturaServicoItem
	 *            id da <tt>PreFaturaServicoItem</tt>
	 * @return página de <tt>OrdemServicoAnexo</tt> incluindo
	 *         <tt>ParcelaPreco</tt> relacionada
	 */
	public ResultSetPage<OrdemServicoAnexo> findByPreFaturaServico(FindDefinition findDefinition, Long idPreFaturaServicoItem) {
		return getOrdemServicoAnexoDAO().findByPreFaturaServico(findDefinition, idPreFaturaServicoItem);
	}

	/**
	 * LMS-6538 - Busca quantidade de <tt>OrdemServicoAnexo</tt> de determinada
	 * <tt>PreFaturaServicoItem</tt>.
	 * 
	 * @param idPreFaturaServicoItem
	 *            id da <tt>PreFaturaServicoItem</tt>
	 * @return quantidade de <tt>OrdemServicoAnexo</tt>
	 */
	public Long findCountByPreFaturaServico(Long idPreFaturaServicoItem) {
		return getOrdemServicoAnexoDAO().findCountByPreFaturaServico(idPreFaturaServicoItem);
	}

	public void setOrdemServicoAnexoDAO(OrdemServicoAnexoDAO ordemServicoAnexoDAO) {
		setDao( ordemServicoAnexoDAO );
	}

	private OrdemServicoAnexoDAO getOrdemServicoAnexoDAO() {
		return (OrdemServicoAnexoDAO) getDao();
	}
	
}
