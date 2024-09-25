package com.mercurio.lms.portaria.model.service;


import java.io.Serializable;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.portaria.model.AcaoIntegracao;
import com.mercurio.lms.portaria.model.AcaoIntegracaoEvento;
import com.mercurio.lms.portaria.model.dao.AcaoIntegracaoEventosDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.acaoIntegracaoEventosService"
 */
public class AcaoIntegracaoEventosService extends CrudService<AcaoIntegracaoEvento, Long> {
	
	private AcaoIntegracaoService acaoIntegracaoService;
	
	@Override
	public Serializable store(AcaoIntegracaoEvento bean) {
		bean.setFilial(SessionUtils.getFilialSessao());
		return super.store(bean);
	}
	
	/**
	 * Salva informações de AcaoIntegracaoEvento através do
	 * processo passado por parâmetro
	 *  
	 * @param dsProcesso
	 * @param idManifesto
	 */
	public void storeAcaoIntegracaoEvento(String dsProcesso , Long idManifesto){

		AcaoIntegracao acaoIntegracao = acaoIntegracaoService.findByProcesso(dsProcesso);
		if(acaoIntegracao == null){
			throw new BusinessException("LMS-21082");
		}
		AcaoIntegracaoEvento aie = new AcaoIntegracaoEvento();
		aie.setAcaoIntegracao(acaoIntegracao);
		aie.setNrDocumento(idManifesto);
		aie.setTpDocumento(acaoIntegracao.getTpDocumento());
		aie.setDsInformacao(acaoIntegracao.getDsAcaoIntegracao());
		aie.setDhGeracaoTzr(JTDateTimeUtils.getDataHoraAtual().toString());
		
		Long nrAgrupador = findLastAgrupador();
		aie.setNrAgrupador(nrAgrupador+1);
		
		store(aie);

	}		
	
	/**
	 * Obtem AcaoIntegracaoEvento através do DS_PROCESSO_INTEGRACAO
	 * 
	 * @param dsProcesso
	 * @return
	 */
	public AcaoIntegracaoEvento findByProcesso(String dsProcesso){
		return getAcaoIntegracaoEventosDAO().findByProcesso(dsProcesso);
	}
		
	public void setAcaoIntegracaoEventosDAO(AcaoIntegracaoEventosDAO acaoIntegracaoEventosDAO) {
		setDao(acaoIntegracaoEventosDAO);
	}

	public AcaoIntegracaoEventosDAO getAcaoIntegracaoEventosDAO() {
		return (AcaoIntegracaoEventosDAO)  getDao();
	}
	
	public Long findLastAgrupador(){
		return getAcaoIntegracaoEventosDAO().findLastAgrupador();
	}

	public AcaoIntegracaoService getAcaoIntegracaoService() {
		return acaoIntegracaoService;
}

	public void setAcaoIntegracaoService(AcaoIntegracaoService acaoIntegracaoService) {
		this.acaoIntegracaoService = acaoIntegracaoService;
	}

}
