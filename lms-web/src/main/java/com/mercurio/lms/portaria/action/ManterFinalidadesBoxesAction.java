package com.mercurio.lms.portaria.action;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.portaria.model.BoxFinalidade;
import com.mercurio.lms.portaria.model.service.BoxFinalidadeService;
import com.mercurio.lms.portaria.model.service.FinalidadeService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.portaria.manterFinalidadesBoxesAction"
 */

public class ManterFinalidadesBoxesAction extends CrudAction {
	
	
	private FinalidadeService finalidadeService;
	private ServicoService servicoService;
	
	
	public void setService(BoxFinalidadeService serviceService) {
		this.defaultService = serviceService;
	}

	public Serializable store(BoxFinalidade bean) {
		return ((BoxFinalidadeService) this.defaultService).store(bean); 
	}
	
	
	public List findFinalidade(Map criteria){
		return getFinalidadeService().find(criteria);
	}
	
	public List findServico(Map criteria){
		return getServicoService().find(criteria);
	}
	
	
	public Map storeMap(TypedFlatMap map) {
		return ((BoxFinalidadeService) this.defaultService).storeMap(map); 
	}
	
	public Map findById(Long id) {
		return ((BoxFinalidadeService) this.defaultService).findByIdDetalhamento(id); 
	}

	
	public void removeById(Long id) {
		((BoxFinalidadeService) this.defaultService).removeById(id); 
	}
	
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = this.defaultService.findPaginated(criteria);
		
		FilterResultSetPage frsp = new FilterResultSetPage(rsp) {

			public Map filterItem(Object item) {
				BoxFinalidade boxFinalidade = (BoxFinalidade) item;
				TypedFlatMap row = new TypedFlatMap();
				
				row.put("idBoxFinalidade", boxFinalidade.getIdBoxFinalidade());
				row.put("finalidade.dsFinalidade", boxFinalidade.getFinalidade().getDsFinalidade());
				if (boxFinalidade.getServico()!=null)
					row.put("servico.dsServico", boxFinalidade.getServico().getDsServico());
				else row.put("servico.dsServico", "");
				row.put("hrInicial", boxFinalidade.getHrInicial());
				row.put("hrFinal", boxFinalidade.getHrFinal());
				row.put("dtVigenciaInicial", boxFinalidade.getDtVigenciaInicial());
				row.put("dtVigenciaFinal", boxFinalidade.getDtVigenciaFinal());
								
				return row;
			} 
			
		}; 
		
		return (ResultSetPage)frsp.doFilter();
		
	}
	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		((BoxFinalidadeService) this.defaultService).removeByIds(ids); 
	}

	/**
	 * @return Returns the finalidadeService.
	 */
	public FinalidadeService getFinalidadeService() {
		return finalidadeService;
	}

	/**
	 * @param finalidadeService The finalidadeService to set.
	 */
	public void setFinalidadeService(FinalidadeService finalidadeService) {
		this.finalidadeService = finalidadeService;
	}

	/**
	 * @return Returns the servicoService.
	 */
	public ServicoService getServicoService() {
		return servicoService;
	}

	/**
	 * @param servicoService The servicoService to set.
	 */
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}	
	
	
}
