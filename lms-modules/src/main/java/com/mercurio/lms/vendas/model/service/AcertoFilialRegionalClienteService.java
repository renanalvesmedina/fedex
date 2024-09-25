package com.mercurio.lms.vendas.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.ParametroCliente;


/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.acertoFilialRegionalClienteService"
 */
public class AcertoFilialRegionalClienteService {

	private ClienteService clienteService;
	private ParametroClienteService parametroClienteService;
	
	/**
	 * Rotina atualiza as regionais no cadastro de clientes quando uma filial estiver
	 * sido alterada
	 * 
	 * @return
	 */
	public Integer acertaRegionalFilialCliente(Long idFilial, Long idRegionalAnterior, Long idRegionalAtual){
		
		Long idRgComercial   = null;
		Long idRgFinanceiro  = null;
		Long idRgOperacional = null;
		
		Regional regional = new  Regional();
		regional.setIdRegional(idRegionalAtual);		
				
		int count = 0;
		Cliente  cliente = null;
		/*Obtem todos os clientes através da filial informada na tela*/
		List<Map> list = clienteService.findListClientesByFilial(idFilial);
		for(Map mp : list){
			
			/*A consulta retorna uma map , isso foi necessário por causa de performance 
			na consulta*/
			idRgComercial   = LongUtils.getLong(mp.get("idRegionalComercial"));
			idRgFinanceiro  = LongUtils.getLong(mp.get("idRegionalFinanceiro"));
			idRgOperacional = LongUtils.getLong(mp.get("idRegionalOperacional"));
			
			/*Verifica se os dados retornados atendem pelo menos uma das condições . Isso
			foi feitoo somente para não executar sem a necessidade o findById*/
			if(idRegionalAnterior.equals(idRgComercial) 
					|| idRegionalAnterior.equals(idRgFinanceiro) 
						|| idRegionalAnterior.equals(idRgOperacional)){
				
				cliente = clienteService.findByIdInitLazyProperties(LongUtils.getLong(mp.get("idCliente")), false);

				/*Atualiza as regionais do cliente com a regional atual da tela*/ 
				if(idRegionalAnterior.equals(idRgComercial)){				
					cliente.setRegionalComercial(regional);					
				}
				if(idRegionalAnterior.equals(idRgFinanceiro)){				
					cliente.setRegionalFinanceiro(regional);				
				}
				if(idRegionalAnterior.equals(idRgOperacional)){				
					cliente.setRegionalOperacional(regional);
				}
				clienteService.store(cliente);	
				count++;
			}			
		}				
		return count;			
	}


	/**
	 * Atualiza as filiais no cadastro de cliente
	 * @param map
	 * @return
	 */
	public Integer acertaFilialMunicipioCliente(Long idMunicipio, Long idFilialAnterior, Long idFilialAtual){

		int count = 0;
		
		Long idFlComercial   = null;
		Long idFlFinanceiro  = null;
		Long idFlOperacional = null;

		/*Filial atual da tela Acertar Filial Regional Cliente*/
		Filial filial = new  Filial();
		filial.setIdFilial(idFilialAtual);
		
		Cliente  cliente = null;		
		/*Obtem a lista de clientes através do municipio*/
		List<Map> list = clienteService.findListClientesByMunicipio(idMunicipio);
		for(Map mp : list){

			/*Obtem os dados do hql , foi necessario limitar a consulta por causa da performance*/
			idFlComercial   = LongUtils.getLong(mp.get("idRegionalComercial"));
			idFlFinanceiro  = LongUtils.getLong(mp.get("idRegionalFinanceiro"));
			idFlOperacional = LongUtils.getLong(mp.get("idRegionalOperacional"));
			
			/*Validações para que o findById não seja executado a todo momento*/ 
			if(idFilialAnterior.equals(idFlComercial) 
					|| idFilialAnterior.equals(idFlFinanceiro) 
						|| idFilialAnterior.equals(idFlOperacional)){
				
				cliente = clienteService.findByIdInitLazyProperties(LongUtils.getLong(mp.get("idCliente")), false);
				
				/*Atualiza a entidade cliente com a filial atual informada na tela*/
				if(idFilialAnterior.equals(idFlComercial)){				
					cliente.setFilialByIdFilialAtendeComercial(filial);					
				}
				if(idFilialAnterior.equals(idFlFinanceiro)){				
					cliente.setFilialByIdFilialCobranca(filial);				
				}
				if(idFilialAnterior.equals(idFlOperacional)){				
					cliente.setFilialByIdFilialAtendeOperacional(filial);
				}
				/*Salva a entidade*/
				clienteService.store(cliente);
				/*Utilizada para mostrar na tela p numero de registros atualizados*/
				count++;
			}			
		}		
		
		/*Atualiza o parametros cliente*/
		acertaFilialMunicipioParametros(idMunicipio,idFilialAnterior,idFilialAtual);
		
		return count;			
	
	}
	
	
	/**
	 * Atualiza os parametros do cliente 
	 * 
	 * @param idMunicipio
	 * @param idFilialAnterior
	 * @param idFilialAtual
	 */
	public void acertaFilialMunicipioParametros(Long idMunicipio, Long idFilialAnterior, Long idFilialAtual){
		
		Long idCliente			= null;
		Long idFilialOrigem  	= null;
		Long idFilialDestino 	= null;
		Long idParametroCliente = null;
		
		Long idParamDestino = null;
		Long idParamOrigem  = null;
		
		/*Filial atual da tela Acertar Filial Regional Cliente*/
		final Filial filial = new Filial();
		filial.setIdFilial(idFilialAtual);
		
		List<ParametroCliente> existParam = null;
		
		ParametroCliente parametro = null;
		/*Obtem a lista de parametros cliente através da filial anterior informada na tela*/
		List<Map> list = parametroClienteService.findListParametrosByRotaFilial(idFilialAnterior);		
		for(Map mp : list){
			
			/*Retorna os parametros do gerados pelo hql no hibernate, foi necessario
			restringir o numero de colunas para aumentara performance da consulta*/ 
			idCliente	 		= LongUtils.getLong(mp.get("idCliente"));
			idFilialOrigem  	= LongUtils.getLong(mp.get("idFilialOrigem"));
			idFilialDestino 	= LongUtils.getLong(mp.get("idFilialDestino"));
			idParametroCliente 	= LongUtils.getLong(mp.get("idParametroCliente")); 
			
			/*As verificações abaixo são somente para não fazer um find desnecessário na entidade 
			  ParametroCliente*/
			if(idFilialAnterior.equals(idFilialOrigem) || idFilialAnterior.equals(idFilialDestino)){
				
				/*Busca o parametro cliente pelo id*/ 
				parametro = parametroClienteService.findById(idParametroCliente);
				
				/*Na et existia uma regra maluca , que foi necessário criar a rotina abaixo*/
				if(idFilialAnterior.equals(idFilialOrigem)){					
					idParamOrigem = filial.getIdFilial();
				}else{
					if(parametro.getFilialByIdFilialOrigem() != null){
						idParamOrigem = parametro.getFilialByIdFilialOrigem().getIdFilial();
					}
				}
								
				if(idFilialAnterior.equals(idFilialDestino)){					
					idParamDestino = filial.getIdFilial();
				}else{
					if(parametro.getFilialByIdFilialDestino() != null){
						idParamDestino = parametro.getFilialByIdFilialDestino().getIdFilial();
					}
				}		
				
				/*Verifica se através da alteração da entidade ParametroCliente existe algum registro igual no banco*/
				existParam = parametroClienteService.findParametroClienteVigenteByRota(idCliente, idParamOrigem, idParamDestino);
				
				/*Se existir fecha a vigencia do parametro, se não existir atualiza a rota e salva o parametro*/
				if(existParam != null && !existParam.isEmpty()){
					parametro.setDtVigenciaFinal(JTDateTimeUtils.getDataAtual());
					parametroClienteService.store(parametro);					
				}else{
					/*Salva o parametro com a origem e destino atualizada*/
					if(idParamOrigem != null){
						parametro.setFilialByIdFilialOrigem(filial);
					}
					if(idParamDestino != null){
						parametro.setFilialByIdFilialDestino(filial);
					}
					parametroClienteService.store(parametro);
				}																						
			}				
		}		
	}	
	
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setParametroClienteService(
			ParametroClienteService parametroClienteService) {
		this.parametroClienteService = parametroClienteService;
	}
	
}