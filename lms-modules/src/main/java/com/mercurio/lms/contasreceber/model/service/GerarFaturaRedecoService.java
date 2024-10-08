package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.Fatura;



/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.contasreceber.gerarFaturaRedecoService"
 */
public class GerarFaturaRedecoService extends GerarFaturaService {
	protected void setValorDefaultSpecific(Fatura fatura){
		fatura.setTpOrigem(new DomainValue("M"));
		fatura.setBlGerarBoleto(Boolean.FALSE);
	}

}
