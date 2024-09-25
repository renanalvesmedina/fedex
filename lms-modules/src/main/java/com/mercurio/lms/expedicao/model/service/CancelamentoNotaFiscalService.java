package com.mercurio.lms.expedicao.model.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.Impressora;
import com.mercurio.lms.expedicao.model.MotivoCancelamento;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.MonitoramentoNotasFiscaisCCTService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.cancelamentoNotaFiscalService"
 */
public class CancelamentoNotaFiscalService extends CrudService<Impressora, Long> {

	private DoctoServicoService doctoServicoService;
	private ConhecimentoService conhecimentoService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;	
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private NotaFiscalServicoService notaFiscalServicoService;
	private DevedorDocServFatService devedorDocServFatService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService;
	
	public void executeCancelNF(Long idDoctoServico,Long idMotivoCancelamento) {
		DomainValue domainNFT = new DomainValue("NFT");
		DomainValue domainNFS = new DomainValue("NFS");
		
		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
		
		//*** [1.6]-Verifica se NFS ja nao esta Cancelada
		if( domainNFS.equals(doctoServico.getTpDocumentoServico())){
			NotaFiscalServico notaFiscalServico = (NotaFiscalServico)doctoServico;
			if(!ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO.equals(notaFiscalServico.getTpSituacaoNf().getValue())) {
				throw new BusinessException("LMS-04103");
			}
		}
		//*** [1.7]-Verifica se CRT ja nao esta faturado
		List<Map<String, Object>> documentosFat = devedorDocServFatService.findDevedorDocServFatByDoctoServico(idDoctoServico);
		for (Map<String, Object> mp : documentosFat) {
			DomainValue tpSituacaoCobranca = (DomainValue)mp.get("tpSituacaoCobranca");
			if (!Arrays.asList(new String[]{"P","C"}).contains(tpSituacaoCobranca.getValue()))
				throw new BusinessException("LMS-04115");
		}
		//*** [1.8]-Verificar se a filial do usuário logado é a mesma filial de emissão da nota fiscal 
		if(!SessionUtils.getFilialSessao().getIdFilial().equals(doctoServico.getFilialByIdFilialOrigem().getIdFilial())){
			throw new BusinessException("LMS-04104");
		}
		
		/* LMS-1325 - Não permite o cancelamento de Documentos Emitimos em meses fechados */
		ConhecimentoCancelarService.validateDataEmissao(doctoServico.getDhEmissao());
		
		//***[1.9]-Verificar se a data de emissão da Nota Fiscal é menor que o mês atual e o dia atual é posterior ao quinto dia do mês
		if(doctoServico.getDhEmissao().getMonthOfYear() != JTDateTimeUtils.getDataHoraAtual().getMonthOfYear()
				&& JTDateTimeUtils.getDataHoraAtual().getDayOfMonth() > 5) {
				throw new BusinessException("LMS-04212");
			}
		
		
		if( domainNFT.equals(doctoServico.getTpDocumentoServico()) ){
			//*** Busca Ultimo Evento do Docto p/ validação
			EventoDocumentoServico eventoDocumentoServico = eventoDocumentoServicoService.findUltimoEventoDoctoServico(idDoctoServico,ConstantesSim.TP_EVENTO_REALIZADO,false);
			if(eventoDocumentoServico == null) {
				throw new BusinessException("LMS-04092");
			}
			//*** [1.5]-Docs possiveis de serem cancelados(DIGITADOS ou EMITIDOS)
			Short cdEvento = eventoDocumentoServico.getEvento().getCdEvento(); 
			if (!ConstantesSim.EVENTO_DOCUMENTO_DIGITADO.equals(cdEvento)
					&& !ConstantesSim.EVENTO_DOCUMENTO_EMITIDO.equals(cdEvento)) {
				throw new BusinessException("LMS-04092");
			}
			
			Conhecimento conhecimento = conhecimentoService.findById(doctoServico.getIdDoctoServico());
			executeCancelNFT(conhecimento,idMotivoCancelamento);
		}else if( domainNFS.equals(doctoServico.getTpDocumentoServico()) ){
			executeCancelNFS(doctoServico.getIdDoctoServico());
		}
	}

	public void executeCancelNFT(Conhecimento conhecimento,Long idMotivoCancelamento) {

		// cancela Conhecimento
		MotivoCancelamento motivoCancelamento = new MotivoCancelamento();
		motivoCancelamento.setIdMotivoCancelamento(idMotivoCancelamento);

		conhecimento.setMotivoCancelamento(motivoCancelamento);
		conhecimento.setTpSituacaoConhecimento(new DomainValue(ConstantesExpedicao.DOCUMENTO_SERVICO_CANCELADO));
		conhecimento.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		conhecimento.setUsuarioByIdUsuarioAlteracao(SessionUtils.getUsuarioLogado());

		// Gerar evento de cancelamento
		String nrCtrc = ConhecimentoUtils.formatConhecimento(
				conhecimento.getFilialByIdFilialOrigem().getSgFilial(), conhecimento.getNrConhecimento()
		);
		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
				ConstantesSim.EVENTO_DOCUMENTO_CANCELADO,
				conhecimento.getIdDoctoServico(),
				conhecimento.getFilialByIdFilialOrigem().getIdFilial(),
				nrCtrc,
				JTDateTimeUtils.getDataHoraAtual(),
				null,
				null,
				conhecimento.getTpDocumentoServico().getValue()
			);
		
		for(DevedorDocServFat d : conhecimento.getDevedorDocServFats()) {
			d.setTpSituacaoCobranca(new DomainValue("L"));
			d.setDtLiquidacao(YearMonthDay.fromDateFields(conhecimento.getDhEmissao().toDate()));
		}
		
		monitoramentoNotasFiscaisCCTService.executeVincularDocumentoComMonitoramento(conhecimento, "CA");
		
		// Apaga as notas fiscais relacionadas ao conhecimento
		notaFiscalConhecimentoService.removeByIdConhecimento(conhecimento.getIdDoctoServico());

		// Salva registro original cancelado
		conhecimentoService.store(conhecimento);
	}
	
	public void executeCancelNFS(Long idNotaFiscalServico) {
		notaFiscalServicoService.removeCancelaNF(idNotaFiscalServico);
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public void setNotaFiscalServicoService(
			NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public MonitoramentoNotasFiscaisCCTService getMonitoramentoNotasFiscaisCCTService() {
		return monitoramentoNotasFiscaisCCTService;
	}

	public void setMonitoramentoNotasFiscaisCCTService(
			MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
		this.monitoramentoNotasFiscaisCCTService = monitoramentoNotasFiscaisCCTService;
	}
}