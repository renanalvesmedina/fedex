package com.mercurio.lms.configuracoes.model.service;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.dao.ConversaoMoedaDAO;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Mickaël Jalbert
 * @version 28/10/2005
 * 
 * Classe que serve para converter valoe de uma moeda para outra.
 * 
 * Especificado por: Hugo Wannmacher
 * 
 * @spring.bean id="lms.configuracoes.conversaoService" 
 * */

public class ConversaoMoedaService {
	private ConversaoMoedaDAO conversaoMoedaDAO;
	private MoedaService moedaService;
	
	public void setConversaoMoedaDAO(ConversaoMoedaDAO conversaoMoedaDAO) {
		this.conversaoMoedaDAO = conversaoMoedaDAO;
	}
	
	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	/**
	 * Converte o valor informado do pais e da moeda informado para o pais e a moeda informado 
	 * passando pela moeda a mais utilizada de cada pais.
	 * 
	 * @param Long idPaisOrigem
	 * @param Long idMoedaOrigem
	 * @param Long idPaisDestino
	 * @param Long idMoedaDestino
	 * @param YearMonthDay dtCotacao
	 * @param BigDecimal vlMoeda
	 * @return BigDecimal retorno
	 * */		
	public BigDecimal findConversaoMoeda(Long idPaisOrigem, Long idMoedaOrigem, Long idPaisDestino, Long idMoedaDestino, YearMonthDay dtCotacao, BigDecimal vlMoeda){
		//Se existe um parametro nullo, lançar uma exception
		if (idPaisOrigem == null || idMoedaOrigem == null || idPaisDestino == null || idMoedaDestino == null || dtCotacao == null){
			throw new BusinessException("LMS-00057");
		}
		if (vlMoeda == null){
			return null;
		}
		//Se a origem e o destino são iguais
		if (CompareUtils.eq(idPaisOrigem, idPaisDestino) && CompareUtils.eq(idMoedaOrigem, idMoedaDestino)) {
			return vlMoeda;
		} else {
			return conversaoMoedaDAO.executeConversaoMoeda(idPaisOrigem, idMoedaOrigem, idPaisDestino, idMoedaDestino, dtCotacao, vlMoeda);
		}
	}	

    /**
     * Converte o valor especificado na data de cotação especificada, da moeda origem para a moeda destino. 
     * @param idMoedaOrigem
     * @param idMoedaDestino
     * @param dtCotacao
     * @param valor
     * @return
     */
    public BigDecimal converteValor(Long idMoedaOrigem, Long idMoedaDestino, BigDecimal valor, YearMonthDay dtCotacao) {
    	
    	Long idPais = SessionUtils.getMoedaSessao().getIdMoeda();
 
    	try {
    		BigDecimal resultado = findConversaoMoeda(idPais, idMoedaOrigem, idPais, idMoedaDestino, dtCotacao, valor);
    		return resultado;
    		
    	} catch (BusinessException be) {
			Moeda moedaOrigem = moedaService.findById(idMoedaOrigem); 
			Moeda moedaDestino = moedaService.findById(idMoedaDestino);
			String data = JTFormatUtils.format(dtCotacao);
			String sgMoedaOrigem = moedaOrigem.getSiglaSimbolo();
			String sgMoedaDestino = moedaDestino.getSiglaSimbolo();
			String nmPais = SessionUtils.getPaisSessao().getNmPais().getValue();
			throw new BusinessException("LMS-22016", new Object[]{sgMoedaOrigem, sgMoedaDestino, data, nmPais});
		}
    }
}
