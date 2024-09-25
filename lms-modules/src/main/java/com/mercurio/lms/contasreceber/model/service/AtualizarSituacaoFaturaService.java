package com.mercurio.lms.contasreceber.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.lms.contasreceber.model.dao.FaturaDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.atualizarSituacaoFaturaService"
 */
public class AtualizarSituacaoFaturaService {
	
	private FaturaDAO faturaDAO;

	/**
	 * @see executeVoltarRecibo(Long idFatura)
	 */
	public void executeVoltarRecibo(List lstIdFatura){
		for (Iterator iter = lstIdFatura.iterator(); iter.hasNext();) {
			Long idFatura = (Long) iter.next();
			executeVoltarRecibo(idFatura);
		}
	}
	
	/**
	 * Vaolta a situa��o anterior a fatura quando ela est� 'Em recibo'
	 * 
	 * @author Micka�l Jalbert
	 * @since 22/08/2006
	 * 
	 * @param Long idFatura
	 */
	public void executeVoltarRecibo(Long idFatura){
		faturaDAO.updateSituacaoFatura(idFatura, 2);
		faturaDAO.updateSituacaoFaturaBoleto(idFatura);
	}

	/**
	 * @see executeVoltarRedeco(Long idFatura)
	 */	
	public void executeVoltarRedeco(List lstIdFatura){
		for (Iterator iter = lstIdFatura.iterator(); iter.hasNext();) {
			Long idFatura = (Long) iter.next();
			executeVoltarRedeco(idFatura);
		}
	}
	
	/**
	 * Vaolta a situa��o anterior a fatura quando ela est� 'Em redeco'
	 * 
	 * @author Micka�l Jalbert
	 * @since 22/08/2006
	 * 
	 * @param Long idFatura
	 */	
	public void executeVoltarRedeco(Long idFatura){
		faturaDAO.updateSituacaoFatura(idFatura, 1);
		faturaDAO.updateSituacaoFaturaBoleto(idFatura);
		faturaDAO.updateSituacaoFaturaRecibo(idFatura);
	}
	
	public void setFaturaDAO(FaturaDAO faturaDAO) {
		this.faturaDAO = faturaDAO;
	}
}