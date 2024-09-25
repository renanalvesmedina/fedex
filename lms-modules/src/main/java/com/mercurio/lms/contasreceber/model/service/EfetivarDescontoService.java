package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.contasreceber.model.Desconto;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.efetivarDescontoService"
 */
public class EfetivarDescontoService {
	
	private DescontoService descontoService;
	
	private DomainValueService domainValueService;

	/**
	 * Recupera uma instância de <code>Fatura</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public void executeWorkflow(List idsProcesso, List tpsSituacao) {
        if (idsProcesso == null || tpsSituacao == null || idsProcesso.size() != tpsSituacao.size()){
        	return;
        }

		Desconto desconto = this.getDescontoService().findByIdWithFaturaAndBoleto((Long)idsProcesso.get(0));        	

    	desconto.setTpSituacaoAprovacao(this.domainValueService.findDomainValueByValue("DM_STATUS_WORKFLOW",(String)tpsSituacao.get(0)));
    	
    	this.storeDesconto(desconto);
    }
    
    /**
     * Salva o desconto.
     * 
     * @param Desconto desconto
     * @return
     * */
    private void storeDesconto(Desconto desconto) {
    	this.getDescontoService().storePadrao(desconto);
    }

	public DescontoService getDescontoService() {
		return descontoService;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
   }