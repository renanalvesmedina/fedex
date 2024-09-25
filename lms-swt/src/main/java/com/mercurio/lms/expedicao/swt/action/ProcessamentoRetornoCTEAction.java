
package com.mercurio.lms.expedicao.swt.action;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.lms.expedicao.model.service.ProcessamentoRetornoCTEService;

/**
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.swt.processamentoRetornoCTEAction"
 */
@Assynchronous(name = "processamentoRetornoCTEAction")
public class ProcessamentoRetornoCTEAction extends CrudAction {
	
	private ProcessamentoRetornoCTEService processamentoRetornoCTEService;
	
	@AssynchronousMethod(name = "processamentoRetornoCTEAction.executeProcessamentoRetornosCTE",
						 type = BatchType.BATCH_SERVICE,
						 feedback = BatchFeedbackType.ON_ERROR)
	public void executeProcessamentoRetornosCTE() {
		processamentoRetornoCTEService.executeProcessamentoRetornosCTE();
	}

	public void setProcessamentoRetornoCTEService(ProcessamentoRetornoCTEService processamentoRetornoCTEService) {
		this.processamentoRetornoCTEService = processamentoRetornoCTEService;
	}
	
}
