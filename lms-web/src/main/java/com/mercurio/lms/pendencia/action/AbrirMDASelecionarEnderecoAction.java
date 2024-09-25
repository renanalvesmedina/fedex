package com.mercurio.lms.pendencia.action;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.pendencia.abrirMDASelecionarEnderecoAction"
 */

public class AbrirMDASelecionarEnderecoAction extends CrudAction {

	public void setService(EnderecoPessoaService enderecoPessoaService) {
		this.defaultService = enderecoPessoaService;
	}
	
	public YearMonthDay getDataAtual() {
		return JTDateTimeUtils.getDataAtual();
	}
		
}
