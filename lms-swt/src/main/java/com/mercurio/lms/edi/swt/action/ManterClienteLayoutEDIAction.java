package com.mercurio.lms.edi.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.edi.model.ClienteEDI;
import com.mercurio.lms.edi.model.ClienteEDIFilialEmbarcadora;
import com.mercurio.lms.edi.model.ClienteLayoutEDI;
import com.mercurio.lms.edi.model.LayoutEDI;
import com.mercurio.lms.edi.model.TipoTransmissaoEDI;
import com.mercurio.lms.edi.model.service.ClienteEDIFilialEmbarcadoraService;
import com.mercurio.lms.edi.model.service.ClienteLayoutEDIService;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
    
public class ManterClienteLayoutEDIAction{

	private ClienteLayoutEDIService clienteLayoutEDIService;
	private ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService;
	private ClienteService clienteService;
	
	/**
	 * Obtem o LayoutEDI através do ID
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> findById(Long id) {  
		Map<String, Object> map = findDados(clienteLayoutEDIService.findById(id)); 
    	return map;
	}
		
	/**
	 * Remove uma lista de ClienteLayoutEDI
	 * @param ids
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		clienteLayoutEDIService.removeByIds(ids);
	}
	
	/**
	 * Remove a entidade pelo ID
	 * @param id
	 */
	public void removeById(Long id) {
		clienteLayoutEDIService.removeById(id);
	}		
		
	/**
	 * Retorna a lista de registros para a grid
	 * @param criteria
	 * @return
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = clienteLayoutEDIService.findPaginated(new PaginatedQuery(criteria));					
		
		List<ClienteLayoutEDI> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(ClienteLayoutEDI cle : list){						
			retorno.add(findDados(cle));
		}
		
		rsp.setList(retorno);
		return rsp;		
	}
	
	/**
	 * Salva ClienteLayoutEDI
	 * @param bean
	 * @return Map dados da tela
	 */
	public Map<String, Object> store(Map<String, Object> bean) {
		
		MapUtilsPlus map = new MapUtilsPlus(bean);
		
		ClienteLayoutEDI pojo = new ClienteLayoutEDI();
		
		/*Id*/
		pojo.setIdClienteLayoutEDI(map.getLong("idClienteLayoutEDI"));
		
		/*Cliente EDI*/
		ClienteEDI clienteEDI = new ClienteEDI();
		clienteEDI.setIdClienteEDI(map.getLong("idClienteEdi"));
		pojo.setClienteEDI(clienteEDI);

		/*Nome Arquivo*/
		String nmArquivo = map.getString("nomeArquivo");
		if(nmArquivo != null){			
			pojo.setNomeArquivo(nmArquivo.toUpperCase());
		}
		
		/*Tipo Transmissão*/
		TipoTransmissaoEDI tte = new TipoTransmissaoEDI();
		tte.setIdTipoTransmissaoEDI(map.getLong("idTipoTransmissaoEDI"));
		pojo.setTransmissaoEDI(tte);
		
		/*Layout*/
		LayoutEDI layoutEDI = new LayoutEDI();
		layoutEDI.setIdLayoutEdi(map.getLong("idLayoutEdi"));
		pojo.setLayoutEDI(layoutEDI);

		/*Embarcadora*/
		ClienteEDIFilialEmbarcadora embarcadora = new ClienteEDIFilialEmbarcadora();
		embarcadora.setIdClienteEDIFilialEmbarcadora(map.getLong("idClienteEDIFilialEmbarcadora"));
		pojo.setEmbarcadora(embarcadora);
		
		/*E-mail*/
		pojo.setEmail(map.getString("email"));
		
		/*FTP Caminho*/
		pojo.setFtpCaminho(map.getString("ftpCaminho"));
		
		/*FTP Servidor*/
		pojo.setFtpServidor(map.getString("ftpServidor"));
		
		/*FTP User*/
		pojo.setFtpUser(map.getString("ftpUser"));
		
		/*FTP Senha*/
		pojo.setFtpSenha(map.getString("ftpSenha"));
		
		/*Pasta*/
		pojo.setNmPasta(map.getString("nmPasta"));
		
		/*Informações complementares*/
		pojo.setInfComplementares(map.getString("infComplementares"));
		
		/*blMigrado*/
		pojo.setBlMigrado(map.getBoolean(bean, "blMigrado"));
		
		
		/*Salvar Cliente Layout EDI*/
		clienteLayoutEDIService.store(pojo);
		
		Map<String,Object> retorno = new HashMap<String, Object>();
		retorno.put("idClienteLayoutEDI", pojo.getIdClienteLayoutEDI());		
		
		return retorno;
	}	
	
	/**
	 * Obtem o mapa com os dados necessários para a grid e tela de cadastro
	 * 
	 * @param cle
	 * @return Map
	 */
	private Map<String, Object> findDados(ClienteLayoutEDI cle){
		
		Map<String, Object> param = new HashMap<String, Object>();			
		/*Id Cliente Layout*/
		param.put("idClienteLayoutEDI", cle.getIdClienteLayoutEDI());		
		/*Nome do arquivo*/
		param.put("nomeArquivo", cle.getNomeArquivo());
		/*blMigrado*/
		param.put("blMigrado", cle.getBlMigrado());
		/*Transmissão EDI*/
		param.put("idTipoTransmissaoEDI", cle.getTransmissaoEDI().getIdTipoTransmissaoEDI());					
		/*Cliente EDI*/			
		Cliente cliente = clienteService.findById(cle.getClienteEDI().getIdClienteEDI());		
		param.put("idClienteEdi", cliente.getIdCliente());
		param.put("nmPessoa", cliente.getPessoa().getNmPessoa() );
		param.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao() );
		/*Layout*/
		param.put("idLayoutEdi", cle.getLayoutEDI().getIdLayoutEdi());
		param.put("nmLayoutEdi", cle.getLayoutEDI().getNmLayoutEdi());
		param.put("tpLayoutEdi", cle.getLayoutEDI().getTpLayoutEdi().getValue());		
		/*Embarcadora*/		
		if(cle.getEmbarcadora() != null){
			ClienteEDIFilialEmbarcadora embarcadora = clienteEDIFilialEmbarcadoraService.findDadosEmbarcadora(cle.getEmbarcadora().getIdClienteEDIFilialEmbarcadora());
			param.put("idClienteEDIFilialEmbarcadora", embarcadora.getIdClienteEDIFilialEmbarcadora());
			param.put("identificacao", embarcadora.getClienteEmbarcador().getPessoa().getNrIdentificacao());
			param.put("cliente", embarcadora.getClienteEmbarcador().getPessoa().getNmPessoa());

			/*Detalhe na grid de listagem*/
			param.put("clienteFilial", embarcadora.getClienteEmbarcador().getPessoa().getNrIdentificacao().concat(" - ").concat(embarcadora.getClienteEmbarcador().getPessoa().getNmPessoa()));						
		}
		/*E-mail*/
		param.put("email", cle.getEmail());
		/*FTP Servidor*/
		param.put("ftpServidor", cle.getFtpServidor());
		/*FTP Caminho*/
		param.put("ftpCaminho", cle.getFtpCaminho());
		/*FTP User*/
		param.put("ftpUser", cle.getFtpUser());
		/*FTP Senha*/
		param.put("ftpSenha", cle.getFtpSenha());
		/*Pasta*/
		param.put("nmPasta", cle.getNmPasta());
		/*Infromações complementares*/
		param.put("infComplementares", cle.getInfComplementares());
		
		return param;
	}

	/**
	 * Retorna uma lista com todas entidades TipoTransmissaoEDI
	 * @return
	 */
	public List<TipoTransmissaoEDI> findListTpTransmissao(){
		return clienteLayoutEDIService.findListTpTransmissao();
	}
	
	public void setClienteLayoutEDIService(
			ClienteLayoutEDIService clienteLayoutEDIService) {
		this.clienteLayoutEDIService = clienteLayoutEDIService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setClienteEDIFilialEmbarcadoraService(
			ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService) {
		this.clienteEDIFilialEmbarcadoraService = clienteEDIFilialEmbarcadoraService;
	}
}
