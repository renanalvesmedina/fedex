package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.CotacaoMoeda;
import com.mercurio.lms.configuracoes.model.service.CotacaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.PaisService;


/**
 * Cálculo de Juro diário.<BR>
 * @author Robson Edemar Gehl
 *@spring.bean id="lms.contasreceber.calcularValorAtualFaturaService"
 */
public class CalcularValorAtualFaturaService {
	
	private DescontoService descontoService;
	
	private BoletoService boletoService;
	
	private FaturaService faturaService;
	
	private PaisService paisService;
	
	private MoedaService moedaService;
	
	private CotacaoMoedaService cotacaoMoedaService;	
	
	/**
	 * Calcula o valor total da fatura, a quantidade de documentos, o valor total de descontos
	 * da fatura informada e seta os valores no pojo informada. NÂO SALVA O POJO.
	 * 
	 * @author Mickaël Jalbert
	 * @since 15/08/2006
	 * 
	 * @param Fatura fatura
	 * @return Fatura
	 */
	public Fatura executeCalcularValorAtualFatura(Fatura fatura){
		// Buscar a soma dos valores dos documento e setar na fatura
		TypedFlatMap somaValores = descontoService.findSomaByFatura(fatura.getIdFatura());
		if (somaValores != null) {
			if (somaValores.getLong("qtDocumento") != null) {
				fatura.setQtDocumentos(somaValores.getLong("qtDocumento").intValue());
			}
			if (somaValores.getBigDecimal("vlTotal") != null) {
				fatura.setVlTotal(somaValores.getBigDecimal("vlTotal"));
			}
			if (somaValores.getBigDecimal("vlDesconto") != null) {
				fatura.setVlDesconto(somaValores.getBigDecimal("vlDesconto"));
			}
		}
		
		//vlTotal = vlTotal (valor total dos documentos) - vlDesconto - vlIva + vlJurosRecebidos
		BigDecimal vlTotal = fatura.getVlTotal().subtract(fatura.getVlDesconto()).subtract(fatura.getVlIva()).add(fatura.getVlJuroRecebido());
		
		// Caso a situacao da fatura seja LI, seta o vlTotalRecebido.
		if (fatura.getTpSituacaoFatura() != null &&
				"LI".equals(fatura.getTpSituacaoFatura().getValue())) {
			fatura.setVlTotalRecebido(vlTotal);
		} else {
			fatura.setVlTotalRecebido(BigDecimal.ZERO);
		}
		
		
		fatura = calculateCotacao(fatura);
		return fatura;
	}
	
	/**
	 * Calcula o valor total da fatura e do boleto se existe, a quantidade de documentos, o valor total de descontos
	 * da fatura informada e seta os valores no pojo informada. SALVA O POJO.
	 * 
	 * @author Mickaël Jalbert
	 * @since 15/08/2006
	 * 
	 * @param Fatura fatura
	 */	
	public void executeAtualizarValorAtualFatura(Fatura fatura){
		fatura = executeCalcularValorAtualFatura(fatura);
		
		faturaService.storeBasic(fatura);
		
		updateBoleto(fatura);
	}

	/**
	 * Atualiza o valor total do boleto a partir do valor total da fatura informada.
	 * 
	 * @author Mickaël Jalbert
	 * @since 15/08/2006
	 * 
	 * @param Fatura fatura
	 */
	private void updateBoleto(Fatura fatura) {
		Boleto boleto = boletoService.findByFatura(fatura.getIdFatura());
		
		if (boleto != null){
			boleto.setVlTotal(fatura.getVlTotal());
			boletoService.storeBasic(boleto);
		}
	}	

	/**
	 * Valida e calcula, se for o caso, o valor da fatura com a cotação
	 * 
	 * Regra 6.2
	 */
	protected Fatura calculateCotacao(Fatura fatura) {
		//Se a abrangência for internacional 
		if (fatura.getTpAbrangencia().getValue().equals("I")){
			Pais pais = paisService.findByIdPessoa(fatura.getFilialByIdFilial().getIdFilial());
						
			//Se o pais da fatura for Brasil
			if (pais.getSgResumida().equals("BR")){
				CotacaoMoeda cotacaoMoeda = cotacaoMoedaService.findById(fatura.getCotacaoMoeda().getIdCotacaoMoeda());
				
				//Se a cotação for informada pelo usuário (a cotação não existe)
				if (fatura.getVlCotacaoMoeda() != null){
					if (fatura.getVlDesconto() != null){
						fatura.setVlDesconto(fatura.getVlDesconto().multiply(fatura.getVlCotacaoMoeda()));
					}
					
					fatura.setVlTotal(fatura.getVlTotal().multiply(fatura.getVlCotacaoMoeda()));
					fatura.setVlTotalRecebido(fatura.getVlTotalRecebido().multiply(fatura.getVlCotacaoMoeda()));
				} else {
					if (fatura.getVlDesconto() != null){
						fatura.setVlDesconto(fatura.getVlDesconto().multiply(cotacaoMoeda.getVlCotacaoMoeda()));
					}
					
					fatura.setVlTotal(fatura.getVlTotal().multiply(cotacaoMoeda.getVlCotacaoMoeda()));
					fatura.setVlTotalRecebido(fatura.getVlTotalRecebido().multiply(cotacaoMoeda.getVlCotacaoMoeda()));
				}
				
				//Trocar a moeda da fatura por a moeda da cotação
				fatura.setMoeda(moedaService.findMoedaPadraoBySiglaPais("BRA"));
				fatura.setCotacaoMoeda(cotacaoMoeda);
			}
		}
		
		return fatura;
	}		
	
	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setCotacaoMoedaService(CotacaoMoedaService cotacaoMoedaService) {
		this.cotacaoMoedaService = cotacaoMoedaService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
}
