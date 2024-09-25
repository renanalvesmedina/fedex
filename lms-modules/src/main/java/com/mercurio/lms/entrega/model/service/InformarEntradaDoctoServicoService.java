package com.mercurio.lms.entrega.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.JTDateTimeUtils;


/**
 * @author Andrêsa Vargas
 * 
 * Classe de serviço para  InformarEntradaDoctoServico:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.informarEntradaDoctoServicoService"
 */
public class InformarEntradaDoctoServicoService{
	
	private DoctoServicoService doctoServicoService;	
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	
	public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	/**
	 * Andresa Vargas
	 * 
	 * Metodo que confirma a entrada do documento, atualizando a data de entrada no setor de entrega
	 * @param idDoctoServico
	 * @return
	 */
	public Serializable storeConfirmarEntradaDocumento(Long idDoctoServico){
		return null;
	}
	
	public void storeEntradaDoc(Long idDoctoServico, Long idFilial, Long idFilialOperacional, Long idFilialDestino){
		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
		List listRotaColetaEntrega = doctoServicoService.findRotaColetaEntregaById(idDoctoServico);
		String nrRota = "";
		if(listRotaColetaEntrega!=null){
			Map mapRotaColetaEntrega = (Map)listRotaColetaEntrega.get(0);
			Short nrRota1 = (Short)mapRotaColetaEntrega.get("nrRota");
			if(nrRota1!= null)
				nrRota = nrRota1.toString();
			
		}
			
		if(doctoServico.getDhEntradaSetorEntrega()!=null)
			throw new BusinessException("LMS-09001");
		
		if(idFilialOperacional != null)
			if(idFilialOperacional.compareTo(idFilial)!=0)
							throw new BusinessException("LMS-09041");
		
		if(doctoServicoService.findEventoByDoctoServico(idDoctoServico, ConstantesEntrega.MANIFESTO_ENTREGA) ||
				doctoServicoService.findEventoByDoctoServico(idDoctoServico,ConstantesEntrega.PRE_MANIFESTO_ENTREGA))
			throw new BusinessException("LMS-09002");
		
		if(doctoServicoService.findLocalizacaoMercCANByDoctoServico(idDoctoServico,ConstantesSim.CD_MERCADORIA_CANCELADA))
			throw new BusinessException("LMS-09003");
		
		doctoServico.setDhEntradaSetorEntrega(JTDateTimeUtils.getDataHoraAtual());
		
		doctoServicoService.store(doctoServico);
		
		//ConstantesSim.EVENTO_ENTRADA_CHECK_IN
		//Código do evento referente a Localização de Mercadoria(No terminal - Programado Rota Entrega)
		
		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(ConstantesSim.EVENTO_ENTRADA_CHECK_IN,doctoServico.getIdDoctoServico(),idFilial,null,JTDateTimeUtils.getDataHoraAtual(),null,nrRota,null);
		
	}

	
} 

