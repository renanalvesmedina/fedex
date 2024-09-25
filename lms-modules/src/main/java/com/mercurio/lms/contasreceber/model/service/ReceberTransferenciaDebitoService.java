package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.log.BatchLogger;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.ItemTransferencia;
import com.mercurio.lms.contasreceber.model.Transferencia;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.util.DiaUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * 
 * Classe para realizar os recebimentos de transferência de débito 
 * @spring.bean id="lms.contasreceber.receberTransferenciaDebitoService"
 */
public class ReceberTransferenciaDebitoService {
	
	private TransferenciaService transferenciaService;
	private ItemTransferenciaService itemTransferenciaService;	
	private ConfiguracoesFacade configuracoesFacade;
	private PpeService ppeService;
	private DomainValueService domainValueService;
	private DevedorDocServFatService devedorDocServFatService;
	private DiaUtils diaUtils;
	private BatchLogger batchLogger;

	/**
	 * Rotina que aprova as transferências de débito não recebidas na filial destino no prazo de rota + "N" dias úteis
	 *
	 */
	public void storeReceberAutomaticamente(){
		
		//Regra 1
		List transferencias = itemTransferenciaService.findTransferenciasPendentesWithItemTransferencia(null);
		batchLogger.info("Aprovação das transferências de débito não recebidas na filial destino no prazo de rota + N dias úteis, total de transaferências: "+transferencias.size());
		//Regra 2
		Long idServico = Long.valueOf(((BigDecimal) configuracoesFacade.getValorParametro("ID_SERVICO_RODOVIARIO_NACIONAL")).longValue());
		final YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		//Regra 3
		final BigDecimal nrDiasMax = (BigDecimal) configuracoesFacade.getValorParametro("NR_DIAS_MAX_REC_TRANSF");
		final DomainValue DM_STATUS_TRANSFERENCIA_RA = domainValueService.findDomainValueByValue("DM_STATUS_TRANSFERENCIA","RA");
		
		for (Iterator iter = transferencias.iterator(); iter.hasNext();) {
			
			Transferencia transf = (Transferencia) iter.next();
			
			final Filial filialByIdFilialDestino = transf.getFilialByIdFilialDestino();
 																	
			YearMonthDay dtMaxima = diaUtils.somaNDiasUteis(transf.getDtEmissao(), 
															nrDiasMax.intValue(),
															filialByIdFilialDestino.getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio()); 
			
			if ( dataAtual.compareTo(dtMaxima) > 0 ){
				transf.setTpSituacaoTransferencia( DM_STATUS_TRANSFERENCIA_RA );
				transf.setDtRecebimento( dataAtual );
				
				transferenciaService.store( transf );
			
				List itensTransf = transf.getItemTransferencias();
				
				for (Iterator iterator = itensTransf.iterator(); iterator.hasNext();) {
				
					ItemTransferencia itemTransf = (ItemTransferencia) iterator.next();
					
					DevedorDocServFat devedorDocServFat = itemTransf.getDevedorDocServFat();
					
					devedorDocServFat.setDivisaoCliente(itemTransf.getDivisaoClienteNovo());
					devedorDocServFat.setFilial(filialByIdFilialDestino);
					devedorDocServFat.setCliente(itemTransf.getClienteByIdNovoResponsavel());
					
					devedorDocServFatService.store(devedorDocServFat);
					
				}
			}
		}
		batchLogger.info("Aprovação das transferências de débito não recebidas na filial destino no prazo de rota + N dias úteis concluída com sucesso!");
	}

	public void setTransferenciaService(TransferenciaService transferenciaService) {
		this.transferenciaService = transferenciaService;
	}

	public void setItemTransferenciaService(
			ItemTransferenciaService itemTransferenciaService) {
		this.itemTransferenciaService = itemTransferenciaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setDiaUtils(DiaUtils diaUtils) {
		this.diaUtils = diaUtils;
	}

	public void setBatchLogger(BatchLogger batchLogger) {
		this.batchLogger = batchLogger;
		this.batchLogger.logClass(ReceberTransferenciaDebitoService.class);
	}
	
}