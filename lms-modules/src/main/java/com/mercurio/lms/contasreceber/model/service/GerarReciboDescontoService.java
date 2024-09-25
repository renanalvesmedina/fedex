package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.ReciboDesconto;
import com.mercurio.lms.contasreceber.model.Redeco;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;




/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.contasreceber.gerarReciboDescontoService"
 */
public class GerarReciboDescontoService {
	
	private RedecoService redecoService;
	
	private ReciboDescontoService reciboDescontoService;
	
	private DevedorDocServFatService devedorDocServFatService;
	
	private ConfiguracoesFacade configuracoesFacade;
	
	private DescontoService descontoService;
	
	/**
	 * Rotina que apaga os reciboDesconto anterior e cria novos agrupado por cliente e filial
	 * 
	 * @author Mickaël Jalbert
	 * @since 13/07/2006
	 * 
	 * @param Long idRedeco
	 */
	public void executeGerarReciboDesconto(Long idRedeco, Boolean blCancelarReciboDesconto){
		Redeco redeco = redecoService.findById(idRedeco);
		
		//Se o tipo de finalidade do redeco for 'Cobrança em carteira'
		if (redeco.getTpFinalidade().getValue().equals("CC") && blCancelarReciboDesconto){
			reciboDescontoService.removeRecibosDescontoRedeco(redeco.getIdRedeco());
			
			List lstDevedorDocServFat = devedorDocServFatService.findByRedeco(redeco.getIdRedeco(),true);
			Long idClienteAnterior = Long.valueOf(-1);
			Long idFilialAnterior = Long.valueOf(-1);
			List lstDesconto = null;
			ReciboDesconto reciboDesconto = null;
						
			//Por cada item de redeco que TEM desconto
			for (Iterator iter = lstDevedorDocServFat.iterator(); iter.hasNext();) {
				DevedorDocServFat devedorDocServFat = (DevedorDocServFat) iter.next();
				
				//Se não é o primeiro registro e se o cliente é diferente do cliente anterior ou 
				//a filial é diferente da filial anterior ou que tem 8 descontos no mesmo reciboDesconto
				if ((!idClienteAnterior.equals(devedorDocServFat.getCliente().getIdCliente()) ||
						!idFilialAnterior.equals(devedorDocServFat.getCliente().getFilialByIdFilialCobranca().getIdFilial()) ||
						(lstDesconto != null && lstDesconto.size() == 8))){
					if (idClienteAnterior.longValue() > -1 && idFilialAnterior.longValue() > -1){
						executeInsertReciboDesconto(reciboDesconto, lstDesconto);
					}
					
					lstDesconto = new ArrayList();
					reciboDesconto = mountReciboDesconto(redeco, devedorDocServFat);
				}
				
				//Se tem desconto e que ele está aprovado
				if (devedorDocServFat.getDesconto() != null && devedorDocServFat.getDesconto().getTpSituacaoAprovacao().getValue().equals("A")){
					lstDesconto.add(devedorDocServFat.getDesconto());
					reciboDesconto.setVlReciboDesconto(reciboDesconto.getVlReciboDesconto().add(devedorDocServFat.getDesconto().getVlDesconto()));
				}
				idClienteAnterior = devedorDocServFat.getCliente().getIdCliente();
				idFilialAnterior = devedorDocServFat.getCliente().getFilialByIdFilialCobranca().getIdFilial();
			}
			
			if (lstDesconto != null && lstDesconto.size() > 0){
				executeInsertReciboDesconto(reciboDesconto, lstDesconto);
			}
		} else {
			List lstRecibo = reciboDescontoService.findByRedeco(idRedeco);
			
			if (lstRecibo != null && lstRecibo.size() > 0) {
				ReciboDesconto rd = (ReciboDesconto)lstRecibo.get(0);
				rd.setObReciboDesconto(redeco.getObRedeco());
				reciboDescontoService.store(rd);			
			}
		}
	}
	
	/**
	 * Insere o registro ReciboDesconto e atualiza os descontos anteriores
	 * 
	 * @author Mickaël Jalbert
	 * @since 13/07/2006
	 * 
	 * @param ReciboDesconto reciboDesconto
	 * @param List lstDesconto
	 */
	private void executeInsertReciboDesconto(ReciboDesconto reciboDesconto, List lstDesconto){
		reciboDescontoService.store(reciboDesconto);
		
		for (Iterator iter = lstDesconto.iterator(); iter.hasNext();) {
			Desconto desconto = (Desconto) iter.next();
			desconto.setReciboDesconto(reciboDesconto);
			descontoService.storePadrao(desconto);
		}
	}
	
	/**
	 * Monta um objeto recibo desconto a partir dos valor da rotina;
	 * 
	 * @author Mickaël Jalbert
	 * @since 14/07/2006
	 * 
	 * @param Redeco redeco
	 * @param DevedorDocServFat devedorDocServFat
	 * 
	 * @return ReciboDesconto
	 */
	private ReciboDesconto mountReciboDesconto(Redeco redeco, DevedorDocServFat devedorDocServFat){
		ReciboDesconto reciboDesconto = new ReciboDesconto();
		
		Filial filial = devedorDocServFat.getFilial();
		
		reciboDesconto.setVlReciboDesconto(new BigDecimal(0));
		reciboDesconto.setFilial(filial);
		reciboDesconto.setCliente(devedorDocServFat.getCliente());
		reciboDesconto.setRedeco(redeco);
		reciboDesconto.setDtEmissao(JTDateTimeUtils.getDataAtual());
		reciboDesconto.setObReciboDesconto(redeco.getObRedeco());
		reciboDesconto.setTpSituacaoReciboDesconto(new DomainValue("E"));
		reciboDesconto.setDvReciboDesconto(Short.valueOf("1"));
		reciboDesconto.setNrReciboDesconto((configuracoesFacade.incrementaParametroSequencial(filial.getIdFilial(), "NR_RECIBO_DESCONTO", true)));

		Long digito = calculatDigitoVerifcador(reciboDesconto, filial);
		
		reciboDesconto.setDvReciboDesconto(Short.valueOf(digito.toString()));
		
		return reciboDesconto;
	}

	/**
	 * @author Mickaël Jalbert
	 * @since 14/07/2006
	 * @param reciboDesconto
	 * @param filial
	 * @return
	 */
	private Long calculatDigitoVerifcador(ReciboDesconto reciboDesconto, Filial filial) {
		Short nrCentroCusto = filial.getNrCentroCusto();
		
		if (nrCentroCusto == null){
			nrCentroCusto = Short.valueOf("000");
		} else {
			nrCentroCusto = Short.valueOf(FormatUtils.fillNumberWithZero(nrCentroCusto.toString(), 3).substring(0,3));
		}
		
		BigDecimal baseCalculo = new BigDecimal(nrCentroCusto.toString() + reciboDesconto.getNrReciboDesconto()); 

		Long digito = baseCalculo.longValue() - (baseCalculo.divide(new BigDecimal(11), BigDecimal.ROUND_DOWN).longValue() * 11);
		
		if (digito > 9) {
			digito = Long.valueOf(0);
		}
		
		return digito;
	}

	public void setReciboDescontoService(ReciboDescontoService reciboDescontoService) {
		this.reciboDescontoService = reciboDescontoService;
	}

	public void setRedecoService(RedecoService redecoService) {
		this.redecoService = redecoService;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

}
