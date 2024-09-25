package com.mercurio.lms.edi.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.edi.model.ComposicaoCodigoVolume;
import com.mercurio.lms.edi.model.ComposicaoLayoutEDI;
import com.mercurio.lms.edi.model.service.ComposicaoCodigoVolumeService;

public class ManterComposicaoCodigoVolumeAction {
	
	private ComposicaoCodigoVolumeService composicaoCodigoVolumeService;
	
	/**
	 * Obtem ComposicaoCodigoVolume através do Id
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> findById(Long id) {
		
		return composicaoCodigoVolumeService.findById(id).getMapped();
	}
	
	/**
	 * Salva a entidade ComposicaoCodigoVolume
	 * 
	 * @param bean
	 * @return
	 */
	public Map<String, Object> store(Map<String, Object> bean) {
		
		MapUtilsPlus map = new MapUtilsPlus(bean);
		
		ComposicaoCodigoVolume pojo = new ComposicaoCodigoVolume();
		
		/*Id*/
		pojo.setIdComposicaoCodigoVolume(map.getLong("idComposicaoCodigoVolume"));
		
		/*Ordem*/
		pojo.setOrdem(map.getInteger("ordem"));
		
		/*Tamanho*/
		pojo.setTamanho(map.getInteger("tamanho"));
		
		/*Formato*/
		pojo.setFormato(map.getString("formato"));
		
		/*Complemento Preenchimento*/
		pojo.setComplPreenchimento(map.getString("complPreenchimento"));
		
		/*Alinhamento*/
		pojo.setAlinhamento(map.getDomainValue("alinhamento"));
		
		/*Indicador*/ 
		pojo.setIndicadorCalculo(map.getDomainValue("indicadorCalculo"));
		
		/*Composição Layout EDI*/
		if(map.getLong("idComposicao") != null){
			ComposicaoLayoutEDI layout = new ComposicaoLayoutEDI();
			layout.setIdComposicaoLayout(map.getLong("idComposicao"));
			pojo.setComposicaoLayoutEDI(layout);
		}
		
		/*Composição Campo EDI*/
		if(map.getLong("idComposicaoLayout") != null){
			ComposicaoLayoutEDI campo = new ComposicaoLayoutEDI();
			campo.setIdComposicaoLayout(map.getLong("idComposicaoLayout"));
			pojo.setComposicaoCampoEDI(campo);
		}
		
		composicaoCodigoVolumeService.store(pojo);
		
		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("idComposicaoCodigoVolume", pojo.getIdComposicaoCodigoVolume());
		
		return retorno;
	}
	
	/**
	 * Paginação ComposicaoCodigoVolume
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		
		ResultSetPage rsp = composicaoCodigoVolumeService.findPaginated(new PaginatedQuery(criteria));			
		
		List<ComposicaoCodigoVolume> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
				
		for(ComposicaoCodigoVolume composicao : list){
			retorno.add(composicao.getMapped());
		}
						
		rsp.setList(retorno);
		return rsp;		

	}
	
	/**
	 * Remove uma lista de entidades
	 * @param ids
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		composicaoCodigoVolumeService.removeByIds(ids);
	}
	
	/**
	 * Remove a entidade pelo ID
	 * @param id
	 */
	public void removeById(Long id) {
		composicaoCodigoVolumeService.removeById(id);
	}

	public void setComposicaoCodigoVolumeService(ComposicaoCodigoVolumeService composicaoCodigoVolumeService) {
		this.composicaoCodigoVolumeService = composicaoCodigoVolumeService;
	}	
}