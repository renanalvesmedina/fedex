package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.dao.EmitirCRTDAO;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.CtoInternacionalUtils;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.emitirCRTService"
 */
public class EmitirCRTService {

	private IncluirEventosRastreabilidadeInternacionalService incluirEventos;
	private CtoInternacionalService ctoInternacionalService;
	private WorkflowPendenciaService workflowPendenciaService;
	private EmitirCRTDAO emitirCRTDAO;

	public void validateCRTCancelado(Long idDoctoServico) {
		List lista = this.emitirCRTDAO.validateCRTCancelado(idDoctoServico);
		if(lista != null && lista.size() > 0) {
			throw new BusinessException("LMS-04143");
		}
	}

	public CtoInternacional executeValidacaoPendenciaWorkflow(Long idDoctoServico){
		CtoInternacional ctoInternacional = ctoInternacionalService.findById(idDoctoServico);

		if( (ctoInternacional.getPendenciaReemissao() != null)
			&& (ctoInternacional.getTpSituacaoPendenciaReemissao() != null)
			&& (!"C".equals(ctoInternacional.getTpSituacaoPendenciaReemissao().getValue()))
		) {
			if("A".equals(ctoInternacional.getTpSituacaoPendenciaReemissao().getValue())) {
				//Emitir conforme regra 1.9
				storeEvento(ctoInternacional.getIdDoctoServico());
			}else if("R".equals(ctoInternacional.getTpSituacaoPendenciaReemissao().getValue())) {
				throw new BusinessException("LMS-04147");
			} else {//"E".equals(ctoInternacional.getTpSituacaoPendenciaReemissao().getValue())
				throw new BusinessException("LMS-04148");
			}
		}
		return ctoInternacional;
	}

	public void executeValidacaoDescontoAprovadoWorkflow(){
		Long idDoctoServico = CtoInternacionalUtils.getCtoInternacionalInSession().getIdDoctoServico();
		List lista = this.emitirCRTDAO.executeValidacaoDescontoAprovadoWorkflow(idDoctoServico);
		if(lista == null || lista.size() < 1){
			throw new BusinessException("LMS-04151");
		}
	}

	public void storeEvento(Long idDoctoServico) {
		CtoInternacional ctoInternacional = ctoInternacionalService.findById(idDoctoServico);
		DateTime dtHrAtual = JTDateTimeUtils.getDataHoraAtual();
		String nrCrt = ConhecimentoUtils.formatConhecimentoInternacional(ctoInternacional.getSgPais(),
				ctoInternacional.getNrPermisso(),				
				ctoInternacional.getNrCrt());

		//Regra 1.9 - CRT não foi emitido
		incluirEventos.generateEventoDocumento(
			ConstantesSim.EVENTO_DOCUMENTO_EMITIDO,
			ctoInternacional.getIdDoctoServico(),	
			SessionUtils.getFilialSessao().getIdFilial(), 
			nrCrt,
			dtHrAtual,
			null,
			null,
			ctoInternacional.getTpDocumentoServico().getValue()
		);

		ctoInternacional.setDhEmissao(dtHrAtual);
		ctoInternacional.setTpSituacaoCrt(new DomainValue("E"));

		//Atualiza a situação do documento de serviço
		ctoInternacionalService.store(ctoInternacional);
	}

	public void storePendenciaWorkflow(Long idDoctoServico) {
		CtoInternacional ctoInternacional = ctoInternacionalService.findById(idDoctoServico);
		DateTime dtHrAtual = JTDateTimeUtils.getDataHoraAtual();

		//TODO Futuramente o grupo 2 criará uma constante que contemple o número abaixo.
		Short nrTpEvento = Short.valueOf("0401");
		StringBuilder dsProcesso = new StringBuilder();
		dsProcesso.append("Liberação de reemissão do CRT ");
		dsProcesso.append(ctoInternacional.getSgPais()).append(".");
		dsProcesso.append(ctoInternacional.getNrPermisso()).append(".");
		dsProcesso.append(ctoInternacional.getNrCrt());

		Pendencia pendencia = workflowPendenciaService.generatePendencia(
			SessionUtils.getFilialSessao().getIdFilial(),
			nrTpEvento,
			ctoInternacional.getIdDoctoServico(), 
			dsProcesso.toString(),
			dtHrAtual
		);

		//pendencia.setIdPendencia();
		pendencia.setTpSituacaoPendencia(new DomainValue("R"));
		ctoInternacional.setPendenciaReemissao(pendencia);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setEmitirCRTDAO(EmitirCRTDAO dao) {	
		this.emitirCRTDAO = dao;
	}

	public void setCtoInternacionalService(CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}

	public void setIncluirEventos(IncluirEventosRastreabilidadeInternacionalService incluirEventos) {
		this.incluirEventos = incluirEventos;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

}
