package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.dao.FaturaDAO;



/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.contasreceber.gerarFaturaFaturaService"
 */
public class GerarFaturaFaturaService extends GerarFaturaService {
	
	private FaturaDAO faturaDAO;
	
	protected void setValorDefaultSpecific(Fatura fatura){
		fatura.setTpOrigem(new DomainValue("M"));
	}

	/**
	 * Ao retransmitir uma fatura é atualizado as colunas DH_TRANSMISSAO e DH_TRANSMISSAO_TZR
	 * através do atributo dhTransmissao no pojo Fatura
	 * @param idFatura
	 */
	public void executeRetransmitirFatura(Long idFatura){
    	// Foi feito um evict do objeto para que o Hibernate dispare um update mesmo
		// que o campo DhTransmissao nao tenha alteração de valores.
		// Isso é necessário para que seja disparada uma trigger no banco de dados.
		Fatura fatura = faturaService.findById(idFatura);
		faturaDAO.getAdsmHibernateTemplate().evict(fatura);
		fatura.setDhTransmissao(null);
		faturaService.store(fatura);
}
	
	public void setFaturaDAO(FaturaDAO faturaDAO) {
		this.faturaDAO = faturaDAO;
}
	
}
