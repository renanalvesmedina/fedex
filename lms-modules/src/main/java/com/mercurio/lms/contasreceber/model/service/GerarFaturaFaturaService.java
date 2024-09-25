package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.dao.FaturaDAO;



/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.contasreceber.gerarFaturaFaturaService"
 */
public class GerarFaturaFaturaService extends GerarFaturaService {
	
	private FaturaDAO faturaDAO;
	
	protected void setValorDefaultSpecific(Fatura fatura){
		fatura.setTpOrigem(new DomainValue("M"));
	}

	/**
	 * Ao retransmitir uma fatura � atualizado as colunas DH_TRANSMISSAO e DH_TRANSMISSAO_TZR
	 * atrav�s do atributo dhTransmissao no pojo Fatura
	 * @param idFatura
	 */
	public void executeRetransmitirFatura(Long idFatura){
    	// Foi feito um evict do objeto para que o Hibernate dispare um update mesmo
		// que o campo DhTransmissao nao tenha altera��o de valores.
		// Isso � necess�rio para que seja disparada uma trigger no banco de dados.
		Fatura fatura = faturaService.findById(idFatura);
		faturaDAO.getAdsmHibernateTemplate().evict(fatura);
		fatura.setDhTransmissao(null);
		faturaService.store(fatura);
}
	
	public void setFaturaDAO(FaturaDAO faturaDAO) {
		this.faturaDAO = faturaDAO;
}
	
}
