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
 * @spring.bean id="lms.contasreceber.gerarFaturaIntegracaoService"
 */
public class GerarFaturaIntegracaoService extends GerarFaturaService{

	@Override
	protected void setValorDefaultSpecific(Fatura fatura) {
		fatura.setTpOrigem(new DomainValue("I"));
		fatura.setBlGerarBoleto(Boolean.FALSE);
	} 

	@Override
	protected Fatura beforeInsert(Fatura fatura) {
		fatura = setValorDefault(fatura);

		fatura = setFilial(fatura);

		fatura = setFilialCobranca(fatura);

		return fatura;
	}

}
