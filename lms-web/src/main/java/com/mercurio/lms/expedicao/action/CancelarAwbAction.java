package com.mercurio.lms.expedicao.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.service.AwbService;

/**
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.cancelarAwbAction"
 */
public class CancelarAwbAction extends CrudAction {
	private AwbService awbService;
	
	public void executeCancelarAwbs(TypedFlatMap data) {
		getAwbService().executeCancelarAwbs(data);
	}

	public AwbService getAwbService() {
		return awbService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}
}
