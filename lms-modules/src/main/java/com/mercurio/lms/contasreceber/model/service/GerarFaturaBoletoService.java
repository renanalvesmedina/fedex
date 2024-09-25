package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.Fatura;



/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.contasreceber.gerarFaturaBoletoService"
 */
public class GerarFaturaBoletoService extends GerarFaturaService {
	protected void setValorDefaultSpecific(Fatura fatura){
		fatura.setTpOrigem(new DomainValue("M"));
		fatura.setTpFatura(new DomainValue("R"));
		fatura.setTpSituacaoFatura(new DomainValue("DI"));
    	fatura.setQtDocumentos(Integer.valueOf(1));
    	fatura.setBlGerarEdi(Boolean.TRUE);
    	fatura.setBlGerarBoleto(Boolean.FALSE);		
	}
}
