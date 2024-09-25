package com.mercurio.lms.entrega.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.sim.ConstantesSim;


/**
 * @author Andrêsa Vargas
 * 
 * Classe de serviço para  :  AtualizarRotaEntregaDoctoServicoService 
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.atualizarRotaEntregaDoctoServicoService"
 */
public class AtualizarRotaEntregaDoctoServicoService {
	private DoctoServicoService doctoServicoService;
	
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	
	public void storeAlteracaoRota(Long idDoctoServico, Long idFilial, Long idRotaColetaNova, Long idFilialOperacional, Long idFilialDestino){
		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
		if(idFilialOperacional!= null)
			if(idFilialOperacional.compareTo(idFilial)!=0)
						throw new BusinessException("LMS-09041");
		
		if(doctoServico.getDhEntradaSetorEntrega()== null)
		 	throw new BusinessException("LMS-09044");
		
		String manifestoEntrega = ConstantesEntrega.MANIFESTO_ENTREGA;
		String preManifesto = ConstantesEntrega.PRE_MANIFESTO_ENTREGA;
		
		if(doctoServicoService.findEventoByDoctoServico(idDoctoServico, manifestoEntrega) ||
				doctoServicoService.findEventoByDoctoServico(idDoctoServico,preManifesto))
			throw new BusinessException("LMS-09002");
		
				
		if(doctoServicoService.findLocalizacaoMercCANByDoctoServico(idDoctoServico,ConstantesSim.CD_MERCADORIA_CANCELADA))
			throw new BusinessException("LMS-09003");
		
		RotaColetaEntrega rotaColetaEntrega = new RotaColetaEntrega();
		rotaColetaEntrega.setIdRotaColetaEntrega(idRotaColetaNova);
		
		doctoServico.setRotaColetaEntregaByIdRotaColetaEntregaReal(rotaColetaEntrega);
		
		doctoServicoService.store(doctoServico);
		
	}
	
	

}
