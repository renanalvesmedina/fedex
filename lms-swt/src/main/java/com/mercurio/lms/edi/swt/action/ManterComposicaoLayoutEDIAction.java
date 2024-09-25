package com.mercurio.lms.edi.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.edi.model.CampoLayoutEDI;
import com.mercurio.lms.edi.model.ClienteEDIFilialEmbarcadora;
import com.mercurio.lms.edi.model.ClienteLayoutEDI;
import com.mercurio.lms.edi.model.ComposicaoLayoutEDI;
import com.mercurio.lms.edi.model.DeParaEDI;
import com.mercurio.lms.edi.model.LayoutEDI;
import com.mercurio.lms.edi.model.RegistroLayoutEDI;
import com.mercurio.lms.edi.model.service.ClienteEDIFilialEmbarcadoraService;
import com.mercurio.lms.edi.model.service.ComposicaoLayoutEdiService;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class ManterComposicaoLayoutEDIAction {
	
	private ComposicaoLayoutEdiService composicaoLayoutEdiService;
	private ClienteService clienteService;
	private ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService;
	
	/**
	 * Obtem o ComposicaoLayoutEDI através do ID
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> findById(Long id) {    	
    	
		ComposicaoLayoutEDI composicao = composicaoLayoutEdiService.findById(id);
		
		Map<String,Object> bean = composicao.getMapped();
    	
		bean.put("extTipoArquivoEDI", composicao.getLayout().getTipoArquivoEDI().getExtTipoArquivoEDI().toString());
		
		/*Cliente layout EDI*/
		if(composicao.getClienteLayoutEDI() != null && composicao.getClienteLayoutEDI().getIdClienteLayoutEDI() != null){
			
			Cliente cliente = clienteService.findById(composicao.getClienteLayoutEDI().getClienteEDI().getIdClienteEDI());
			
			/*Cliente EDI*/
			bean.put("idClienteLayoutEdi", composicao.getClienteLayoutEDI().getIdClienteLayoutEDI());
			bean.put("idClienteEdi", composicao.getClienteLayoutEDI().getClienteEDI().getIdClienteEDI());
			bean.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
			bean.put("nmPessoa", cliente.getPessoa().getNmPessoa());

			bean.put("idClienteEdi", composicao.getClienteLayoutEDI().getClienteEDI().getIdClienteEDI());
			
		}
		
		/*Filial Embarcadora*/
		if (composicao.getClienteLayoutEDI() != null && composicao.getClienteLayoutEDI().getEmbarcadora() != null 
				&& composicao.getClienteLayoutEDI().getEmbarcadora().getIdClienteEDIFilialEmbarcadora() != null){
			
			ClienteEDIFilialEmbarcadora clienteEDIFilialEmbarcadora = clienteEDIFilialEmbarcadoraService
					.findDadosEmbarcadora(composicao.getClienteLayoutEDI().getEmbarcadora().getIdClienteEDIFilialEmbarcadora());
			
			bean.put("idClienteEDIFilialEmbarcadora", clienteEDIFilialEmbarcadora.getIdClienteEDIFilialEmbarcadora());
			bean.put("identificacao", clienteEDIFilialEmbarcadora.getClienteEmbarcador().getPessoa().getNrIdentificacao());
			bean.put("nmPessoaEmbarcadora", clienteEDIFilialEmbarcadora.getClienteEmbarcador().getPessoa().getNmPessoa());
		}
    	return bean;
	}		
	
	/**
	 * Persiste o ComposicaoLayoutEDI
	 * @param bean
	 * @return
	 */
	public Map<String, Object> store(Map<String, Object> bean) {
		 
		MapUtilsPlus map = new MapUtilsPlus(bean);
		
		/*Id*/
		ComposicaoLayoutEDI pojo = new ComposicaoLayoutEDI();
		pojo.setIdComposicaoLayout(map.getLong("idComposicaoLayout"));
		
		/*Cliente Layout EDI*/
		Long idClienteLayoutEDI = map.getLong("idClienteLayoutEdi"); 
		if(idClienteLayoutEDI != null){
			ClienteLayoutEDI clienteLayoutEDI = new ClienteLayoutEDI();
			clienteLayoutEDI.setIdClienteLayoutEDI(idClienteLayoutEDI);
			pojo.setClienteLayoutEDI(clienteLayoutEDI);
		}
		
		/*LayoutEDI*/
		LayoutEDI layout = new LayoutEDI();
		layout.setIdLayoutEdi(map.getLong("idLayoutEdi"));
		pojo.setLayout(layout);
		
		/*RegistroLayoutEDI*/
		RegistroLayoutEDI registroLayout = new RegistroLayoutEDI();
		registroLayout.setIdRegistroLayoutEdi(map.getLong("idRegistro"));
		pojo.setRegistroLayout(registroLayout);
		
		/*CampoLayoutEDI*/
		CampoLayoutEDI campoLayout = new CampoLayoutEDI();
		campoLayout.setIdCampo(map.getLong("idCampo"));		
		pojo.setCampoLayout(campoLayout);
		
		/*Formato*/
		pojo.setFormato(map.getDomainValue("formato"));
		
		/*Compl. Formato*/
		pojo.setCompFormato(map.getString("compFormato"));
		
		/*Tamanho*/
		pojo.setTamanho(IntegerUtils.defaultInteger(map.getInteger("tamanho")));
		
		/*Quantidade decimal*/
		pojo.setQtDecimal(IntegerUtils.defaultInteger(map.getInteger("qtDecimal")));
		
		/*Posicao*/
		pojo.setPosicao(map.getInteger("posicao"));
		
		/*Valor Default*/
		pojo.setValorDefault(map.getString("valorDefault"));
		
		/* xpath */
		pojo.setXpath(map.getString("xpath"));
		
		/*Vigencia*/
		pojo.setDtVigenciaInicial(map.getYearMonthDay("dtVigenciaInicial"));		
		/*Caso o campo data final for nulo informa a data 01/01/4000*/
		if(map.getYearMonthDay("dtVigenciaFinal") == null){
			pojo.setDtVigenciaFinal(JTDateTimeUtils.MAX_YEARMONTHDAY);
		}else{
			pojo.setDtVigenciaFinal(map.getYearMonthDay("dtVigenciaFinal"));			
		}
		
		/*Observacao*/
		pojo.setObservacao(map.getString("observacao"));
				 
		/*De Para EDI*/
		Long idDeParaEDI = map.getLong("idDeParaEDI");
		if(idDeParaEDI != null){
			DeParaEDI deParaEDI = new DeParaEDI();
			deParaEDI.setIdDeParaEDI(idDeParaEDI);
			pojo.setDeParaEDI(deParaEDI);
		}
		
		/*Salva o Layout EDI*/
		composicaoLayoutEdiService.store(pojo);
			 
		Map<String,Object> retorno = new HashMap<String, Object>();
		retorno.put("idComposicaoLayout", pojo.getIdComposicaoLayout());
			 
		return retorno;
	}		
		
	/**
	 * Retorna a lista de registros para a grid
	 * @param criteria
	 * @return
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = composicaoLayoutEdiService.findPaginated(new PaginatedQuery(criteria));					
		
		List<ComposicaoLayoutEDI> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
				
		for(ComposicaoLayoutEDI layout : list){
			retorno.add(layout.getMappedToFind());
		}
						
		rsp.setList(retorno);
		return rsp;		
	}
	
	/**
	 * utilizado para recurerar os dados do ClienteLayoutEDI
	 * armazenado na sessao
	 *  
	 * @return
	 */
	public List obtemDadosClienteLayoutEDI(){
		
		Map data = (Map) SessionContext.get("clienteLayoutEDI");
		List result = new ArrayList();
		if(data != null) {
			result.add(data);
		}
		return result;
		
	}
	
	/**
	 * Remove uma lista de LayoutEDI
	 * @param ids
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		composicaoLayoutEdiService.removeByIds(ids);
	}
	
	/**
	 * Remove a entidade pelo ID
	 * @param id
	 */
	public void removeById(Long id) {
		composicaoLayoutEdiService.removeById(id);
	}	
	
	public ComposicaoLayoutEdiService getComposicaoLayoutEdiService() {
		return composicaoLayoutEdiService;
	}

	public void setComposicaoLayoutEdiService(
			ComposicaoLayoutEdiService composicaoLayoutEdiService) {
		this.composicaoLayoutEdiService = composicaoLayoutEdiService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setClienteEDIFilialEmbarcadoraService(
			ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService) {
		this.clienteEDIFilialEmbarcadoraService = clienteEDIFilialEmbarcadoraService;
	}
	
}
