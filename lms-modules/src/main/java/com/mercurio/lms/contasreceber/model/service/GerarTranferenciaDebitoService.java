package com.mercurio.lms.contasreceber.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.AgendaTransferencia;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.ItemTransferencia;
import com.mercurio.lms.contasreceber.model.Transferencia;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.gerarTranferenciaDebitoService"
 */
public class GerarTranferenciaDebitoService {
	
	/** Inversão de Controle - AgendaTransferenciaService */
	private AgendaTransferenciaService agendaTransferenciaService;
	public void setAgendaTransferenciaService(AgendaTransferenciaService agendaTransferenciaService){
		this.agendaTransferenciaService = agendaTransferenciaService;
	}
	
	/** Inversão de Controle - Transferencia */
	private TransferenciaService transferenciaService;
	public void setTransferencia(TransferenciaService transferenciaService){
		this.transferenciaService = transferenciaService;
	}
	
	/** Inversão de Controle - Transferencia */
	private ItemTransferenciaService itemTransferenciaService;
	public void setItemTransferencia(ItemTransferenciaService itemTransferenciaService){
		this.itemTransferenciaService = itemTransferenciaService;
	}
	
	public void teste(TypedFlatMap tfm){
		gerarTranferenciaDebito(tfm.getLong("filial.idFilial"), tfm.getDomainValue("tpOrigem").getValue());
	}
	
	/**
	 * Método responsável pelo processo GerarTranferenciaDebito
	 * 
	 * @author Hector Julian Esnaola Junior
	 * 20/03/2006
	 * 
	 * @param idFilialOrigem
	 * @param tpOrigem
	 */
	public void gerarTranferenciaDebito(Long idFilialOrigem, String tpOrigem){
		
		/** Busca uma lista de AgendaTransferencia de acordo com os filtros */
		List agendasTranferencia = agendaTransferenciaService.findAgendaTranferencia(idFilialOrigem, tpOrigem);
		
		/**  Variáveis que armzenarão os valores da iteração anterior */
		Long idFilialDestinoAnterior = Long.valueOf(-1);
		String tpDoctoServicoAnterior = "";
		
		/** Variáveis que armazenarão os valores da iteração atual */
		Long idFilialDestinoAtual;
		String tpDoctoServicoAtual;

		Transferencia transferencia = null;
		ItemTransferencia itemTransferencia = null;
		DevedorDocServFat devedorDocServFat = null;
		
		/** Processo para cada agendaTransferencia */
		for (Iterator iter = agendasTranferencia.iterator(); iter.hasNext();) {
			
			/** Faz um cast(AgendaTransferencia) da iteração atual do Iterator */
			AgendaTransferencia agendaTransferencia = (AgendaTransferencia) iter.next();
			
			/** Atribui o id da filial de destino da iteração atual para a comparação com o id da filial de destino da iteração anterior */
			idFilialDestinoAtual = agendaTransferencia.getFilialByIdFilialDestino().getIdFilial();
			
			/** Atribui o tipo de docto de serviço da iteração atual para a comparação com o tipo de docto de serviço  da iteração anterior*/
			tpDoctoServicoAtual = agendaTransferencia.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue();
			
			/** 
			 * A cada quebra de filial de destino ou quebra de tipo de documento de serviço, 
			 * será inserido um novo registro na tabela Transferência 
			 */
			if(!idFilialDestinoAtual.equals(idFilialDestinoAnterior) || !tpDoctoServicoAtual.equals(tpDoctoServicoAnterior)){
				
				transferencia = new Transferencia();
				
				/** Insere um Trnaferencia */
				transferencia = transferenciaService.storeTransferenciaByAgendaTransferencia(agendaTransferencia);
				
			}
			
			itemTransferencia = new ItemTransferencia();

			/** Insere um ItemTransferencia */
			itemTransferencia = itemTransferenciaService.storeItemTransferenciaByAgendaTransferencia(agendaTransferencia, transferencia);
				
			/** Caso tpSituacaTransferencia = RE, atualiza devedorDocServFat da itemTransferencia */
			if(transferencia.getTpSituacaoTransferencia().getValue().equals("RE")){
				devedorDocServFat = itemTransferencia.getDevedorDocServFat();
				
				devedorDocServFat.setFilial(agendaTransferencia.getFilialByIdFilialDestino());
				devedorDocServFat.setCliente(agendaTransferencia.getCliente());
				devedorDocServFat.setDivisaoCliente(agendaTransferencia.getDivisaoCliente());
				
				itemTransferenciaService.store(itemTransferencia);
			}
				
			/** Exclui a AgendaTransferencia em questão */
			agendaTransferenciaService.removeById(agendaTransferencia.getIdAgendaTransferencia());
			
			/** guarda o id da filial de destino da iteração atual, para ser compararado com o id da filial de destino da proxima iteração  */
			idFilialDestinoAnterior = idFilialDestinoAtual;
			
			/** guarda o tipo de docto de serviço da iteração atual, para ser compararado com o tipo de docto de serviço da proxima iteração  */
			tpDoctoServicoAnterior = tpDoctoServicoAtual;
			
		}
		
		
		
	}

}
