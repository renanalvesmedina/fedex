package com.mercurio.lms.contasreceber.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.lms.contasreceber.model.dao.FaturaDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
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
	 * Vaolta a situação anterior a fatura quando ela está 'Em recibo'
	 * 
	 * @author Mickaël Jalbert
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
	 * Vaolta a situação anterior a fatura quando ela está 'Em redeco'
	 * 
	 * @author Mickaël Jalbert
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