package com.mercurio.lms.expedicao.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MotivoCancelamento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.dao.CtoInternacionalDAO;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD: 
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.conhecimentoCancelarService"
 */
public class ConhecimentoCancelarService {
	
	private static final String PARAMETRO_HORARIO_INICIO_FECHA_MENSAL = "HORARIO_INICIO_FECHA_MENSAL";

	private DevedorDocServFatService devedorDocServFatService;
	private ConhecimentoService conhecimentoService;
	private CtoInternacionalService ctoInternacionalService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private ReciboReembolsoService reciboReembolsoService;
	private CtoInternacionalDAO ctoInternacionalDAO;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private ConfiguracoesFacade configuracoesFacade;
	private LiberacaoNotaNaturaService liberacaoNotaNaturaService;
	private static ParametroGeralService parametroGeralService;

	/**
	 * [LMS-414] - Gera Evento de Cancelamento para Conhecimentos com Limites de Calculo do Frete Excedentes
	 * Responsáveis: Fabiano Fração e Iardlei
	 * 
	 * @author André Valadas
	 * @since 02/05/2011
	 */
	public void storeEventoCancelamentoLimiteCalculoFrete(final Long idConhecimento, final String dsObservacao) {
		final Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(idConhecimento, false);

		// Gerar evento de cancelamento
		final String nrCtrc = ConhecimentoUtils.formatConhecimento(
				conhecimento.getFilialByIdFilialOrigem().getSgFilial(), conhecimento.getNrConhecimento()
		);
		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
				ConstantesSim.EVENTO_DOCUMENTO_CANCELADO,
				conhecimento.getIdDoctoServico(),
				conhecimento.getFilialByIdFilialOrigem().getIdFilial(),
				nrCtrc,
				JTDateTimeUtils.getDataHoraAtual(),
				null,
				dsObservacao,
				conhecimento.getTpDocumentoServico().getValue()
		);
	}
	/**
	 * [LMS-414] - Cancela Conhecimento com Limites de Calculo do Frete Excedentes
	 * Responsáveis: Fabiano Fração e Iardlei
	 * 
	 * @author André Valadas
	 * @since 02/05/2011
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void storeCancelamentoObservacaoLimiteCalculoFrete(final Long idConhecimento, final String dsObservacao) {
		final Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(idConhecimento, false);

		/** Adiciona OBS de cancelamento */
		conhecimento.addObservacaoDoctoServico(ConhecimentoUtils.createObservacaoDocumentoServico(conhecimento, dsObservacao, Boolean.FALSE));
		conhecimento.setBlPesoAferido(false);

		// Salva registro original cancelado
		conhecimentoService.store(conhecimento);
	}
	
	public void storeCancelamentoLimiteCalculoFrete(final Long idConhecimento, final Long idMotivoCancelamento, final Long idLocalizacaoMercadoria) {
		final Conhecimento conhecimento = conhecimentoService.findById(idConhecimento);

		final LocalizacaoMercadoria localizacaoMercadoria = new LocalizacaoMercadoria();
		localizacaoMercadoria.setIdLocalizacaoMercadoria(idLocalizacaoMercadoria);
		conhecimento.setLocalizacaoMercadoria(localizacaoMercadoria);

		prepareConhecimentoCancelado(conhecimento, idMotivoCancelamento, SessionUtils.getFilialSessao());

		for(DevedorDocServFat d : conhecimento.getDevedorDocServFats()) {
			d.setTpSituacaoCobranca(new DomainValue("N"));
			d.setDtLiquidacao(JTDateTimeUtils.getDataAtual());
	}
	
		// Apaga as notas fiscais relacionadas ao conhecimento
		notaFiscalConhecimentoService.removeByIdConhecimento(idConhecimento);
		
		// Salva registro original cancelado
		conhecimentoService.store(conhecimento);
	}
	
	public void storeCancelaCTRC(Boolean blAproveitarDados, Long idConhecimentoOriginal, Long idMotivoCancelamento, Filial filialUsuarioLogado) {

		Conhecimento conhecimentoOriginal = conhecimentoService.findById(idConhecimentoOriginal);

		validateCancelarCTRC(conhecimentoOriginal, filialUsuarioLogado);

		// Verifica se existe recibo de reembolso ativo associado ao conhecimento e cancela os mesmos
		reciboReembolsoService.executeCancelaReciboByDoctoServico(idConhecimentoOriginal);

		//jira LMS-448
		liberacaoNotaNaturaService.atualizaTerraNaturaCancelado(idConhecimentoOriginal);

		if(Boolean.TRUE.equals(blAproveitarDados)) {
			storeConhecimentoCanceladoAproveitandoDados(conhecimentoOriginal, idMotivoCancelamento, filialUsuarioLogado);
		} else {
			// Grava o documento de servico orginal como cancelado
			storeConhecimentoCanceladoNormal(conhecimentoOriginal, idMotivoCancelamento, filialUsuarioLogado, null);
		}
	}

	private void validateCancelarCTRC(Conhecimento conhecimentoOriginal, Filial filialUsuarioLogado) {
		/* LMS-1325 - Não permite o cancelamento de Documentos Emitimos em meses fechados */
		ConhecimentoCancelarService.validateDataEmissao(conhecimentoOriginal.getDhEmissao());

		//O conhecimento so pode ser cancelado se o seu ultimo evento for "Documento emitido" 
		EventoDocumentoServico eventoDocumentoServico = eventoDocumentoServicoService.findUltimoEventoDoctoServico(conhecimentoOriginal.getIdDoctoServico(), ConstantesSim.TP_EVENTO_REALIZADO, Boolean.FALSE);
		if( (eventoDocumentoServico == null) || (!ConstantesSim.EVENTO_DOCUMENTO_EMITIDO.equals(eventoDocumentoServico.getEvento().getCdEvento())) ) {
			throw new BusinessException("LMS-04092");
		}

		//Verifica se o Conhecimento ja foi faturado
		devedorDocServFatService.validateCTRCFaturado(conhecimentoOriginal.getIdDoctoServico());

		//Verifica se a filial do usuario logado eh a mesma filial de origem do documento de servico 
		Long idFilialOrigem = conhecimentoOriginal.getFilialByIdFilialOrigem().getIdFilial();
		if(!idFilialOrigem.equals(filialUsuarioLogado.getIdFilial())) {
			throw new BusinessException("LMS-04084");
		}
	}

	/**
	 * Gera evento de Cancelamento de Documento para as 3 regras de bloqueio de valores da demanda LMS-3281, que são: valor da mercadoria, valor do
	 * frete ou percentual do segundo em relação ao primeiro.
	 * 
	 * @param idConhecimento
	 * @param dsObservacao
	 */
	public void storeEventoCancelamentoBloqueioValores(DoctoServico conhecimento, final String dsObservacao) {

		String nrDocumento = conhecimento.getFilialByIdFilialOrigem().getSgFilial() + " "
				+ FormatUtils.formatLongWithZeros(conhecimento.getNrDoctoServico(), "00000000");
		
		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
				ConstantesSim.EVENTO_DOCUMENTO_CANCELADO,
				conhecimento.getIdDoctoServico(),
				conhecimento.getFilialByIdFilialOrigem().getIdFilial(),
				nrDocumento,
				JTDateTimeUtils.getDataHoraAtual(),
				null,
				dsObservacao,
				conhecimento.getTpDocumentoServico().getValue()
		);
	}
	
	/**
	 * LMS-1325 - Não permite o cancelamento de Documentos Emitimos em meses fechados
	 * 
	 * @author André Valadas
	 * @param dhEmissao
	 */
	public static void validateDataEmissao(final DateTime dhEmissao) {
		final DateTime dataHoraAtual = new DateTime();//JTDateTimeUtils.getDataHoraAtual();
		int yearDiff = dataHoraAtual.getYear() - dhEmissao.getYear();
		int monthDiff = dataHoraAtual.getMonthOfYear() - dhEmissao.getMonthOfYear();
		// Regra que valida o ano
		if(yearDiff > 0) {
			monthDiff = monthDiff + (12 * yearDiff);  
	}

		/*
		 * 1. Se mês de emissão do CTRC é igual ao mês corrente o CTRC poderá ser cancelado
		 */
		if(monthDiff == 0) {
			return;
		}

		/* 
		 * 2. Se o mês de emissão é menor que o mês anterior ao mês corrente, 
		 * 	  visualizar a mensagem “LMS-04370”, não deixando cancelar o documento
		 */
		if(monthDiff > 1) {
			throw new BusinessException("LMS-04370");
		}

		/* 
		 * 3. Se o mês de emissão é igual ao mês anterior ao mês corrente e o dia corrente é o primeiro dia do mês 
		 * 	  e a hora corrente é maior que conteúdo do parâmetro geral “HORARIO_INICIO_FECHA_MENSAL, 
		 * 	  visualizar a mensagem “LMS-04370”, não deixando cancelar o documento.
		 */
		if(monthDiff == 1) {
			if(dataHoraAtual.getDayOfMonth() == 1 && !isHoraMinutoSegundoExcedido(dataHoraAtual)) {
				return;
			}else{
				throw new BusinessException("LMS-04370");
			}
		}
	}
	
	private static boolean isHoraMinutoSegundoExcedido(DateTime dataHoraAtual) {
        DateTime horarioInicioFechaMensal = findHorarioInicioFechaMensal();

        if (horarioInicioFechaMensal != null) {
        	if(dataHoraAtual.getHourOfDay() > horarioInicioFechaMensal.getHourOfDay()){
        		return true;
        	}
        	if(dataHoraAtual.getMinuteOfDay() > horarioInicioFechaMensal.getMinuteOfDay()){
        		return true;
        	}
        	if(dataHoraAtual.getSecondOfDay() > horarioInicioFechaMensal.getSecondOfDay()){
        		return true;
        	}
        }

        return false;
    }

	public static DateTime findHorarioInicioFechaMensal() {
        String data = (String) parametroGeralService.findConteudoByNomeParametro(PARAMETRO_HORARIO_INICIO_FECHA_MENSAL, false);
        if (data != null) {
            return JTDateTimeUtils.convertDataStringToDateTime(data);
        }
        return null;
    }

	private void storeConhecimentoCanceladoAproveitandoDados(Conhecimento conhecimentoOriginal, Long idMotivoCancelamento, Filial filialUsuarioLogado) {

		//Prepara dados do conhecimento novo
		Conhecimento conhecimentoNovo = ConhecimentoUtils.createCopy(conhecimentoOriginal);
		prepareConhecimentoCancelado(conhecimentoNovo, idMotivoCancelamento, filialUsuarioLogado);
		ConhecimentoUtils.copyEventoDocumentoServico(conhecimentoOriginal, conhecimentoNovo);

		conhecimentoNovo.setNrConhecimento(conhecimentoOriginal.getNrConhecimento());
		conhecimentoNovo.setDvConhecimento(conhecimentoOriginal.getDvConhecimento());
		conhecimentoNovo.setNrDoctoServico(conhecimentoOriginal.getNrDoctoServico());
		conhecimentoNovo.setNrFormulario(conhecimentoOriginal.getNrFormulario());
		conhecimentoNovo.setNrSeloFiscal(conhecimentoOriginal.getNrSeloFiscal());
		conhecimentoNovo.setDsSerie(conhecimentoOriginal.getDsSerie());
		conhecimentoNovo.setDsSerieSeloFiscal(conhecimentoOriginal.getDsSerieSeloFiscal());
		conhecimentoNovo.setFilialOrigem(filialUsuarioLogado);
		conhecimentoNovo.setUsuarioByIdUsuarioInclusao(SessionUtils.getUsuarioLogado());
		conhecimentoNovo.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());

		String nrCtrcOriginal = ConhecimentoUtils.formatConhecimento(conhecimentoOriginal.getFilialByIdFilialOrigem().getSgFilial(), conhecimentoOriginal.getNrConhecimento());
		String dsObservacao = configuracoesFacade.getMensagem("LMS-04010", new Object[] { nrCtrcOriginal });
		ObservacaoDoctoServico observacao = ConhecimentoUtils.createObservacaoDocumentoServico(conhecimentoOriginal, dsObservacao, Boolean.TRUE);
		
		//Prepara dados do conhecimento original
		conhecimentoOriginal.setTpSituacaoConhecimento(new DomainValue("P"));
		conhecimentoOriginal.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		conhecimentoOriginal.setDhEmissao(null);
		conhecimentoOriginal.setNrFormulario(null);
		conhecimentoOriginal.setNrSeloFiscal(null);
		conhecimentoOriginal.setDsSerie(null);
		conhecimentoOriginal.setDsSerieSeloFiscal(null);
		conhecimentoOriginal.addObservacaoDoctoServico(observacao);

		//Salva conhecimento original cancelado
		conhecimentoService.store(conhecimentoOriginal);

		//Excluir registros de eventos do conhecimento original
		eventoDocumentoServicoService.removeByIdDoctoServico(conhecimentoOriginal.getIdDoctoServico());

		//Descarrega dados
		conhecimentoService.flush();
		
		//Salva conhecimento novo cancelado
		conhecimentoService.store(conhecimentoNovo);

		String nrCtrc = ConhecimentoUtils.formatConhecimento(conhecimentoNovo
				.getFilialByIdFilialOrigem().getSgFilial(), conhecimentoNovo.getNrConhecimento());
		
		//Gera evento de cancelamento para o conhecimento novo
		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
			Short.valueOf(String.valueOf(ConstantesSim.EVENTO_DOCUMENTO_CANCELADO)),
			conhecimentoNovo.getIdDoctoServico(),
			conhecimentoNovo.getFilialByIdFilialOrigem().getIdFilial(),
			nrCtrc,
			JTDateTimeUtils.getDataHoraAtual(),
			null,
			null,
			conhecimentoNovo.getTpDocumentoServico().getValue()
		);

	}

	private void prepareConhecimentoCancelado(Conhecimento conhecimento, Long idMotivoCancelamento, Filial filialUsuarioLogado) {

		MotivoCancelamento motivoCancelamento = new MotivoCancelamento();
		motivoCancelamento.setIdMotivoCancelamento(idMotivoCancelamento);
		conhecimento.setMotivoCancelamento(motivoCancelamento);

		conhecimento.setTpSituacaoConhecimento(new DomainValue(ConstantesExpedicao.DOCUMENTO_SERVICO_CANCELADO));
		conhecimento.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());

		conhecimento.setUsuarioByIdUsuarioAlteracao(SessionUtils.getUsuarioLogado());
	}

	
	
	private void storeConhecimentoCanceladoNormal(Conhecimento conhecimentoOriginal, Long idMotivoCancelamento, Filial filialUsuarioLogado, String dsObservacao) {
		prepareConhecimentoCancelado(conhecimentoOriginal, idMotivoCancelamento, filialUsuarioLogado);

		for(DevedorDocServFat d : conhecimentoOriginal.getDevedorDocServFats()) {
			if(d.getTpSituacaoCobranca().getValue().equals("P") || d.getTpSituacaoCobranca().getValue().equals("C")) {
				d.setTpSituacaoCobranca(new DomainValue("L"));
				d.setDtLiquidacao(JTDateTimeUtils.getDataAtual());
			} else {
				throw new BusinessException("LMS-04115");
		}
		}

		// Apaga as notas fiscais relacionadas ao conhecimento
		notaFiscalConhecimentoService.removeByIdConhecimento(conhecimentoOriginal.getIdDoctoServico());

		// Salva registro original cancelado
		conhecimentoService.store(conhecimentoOriginal);

		// Gerar evento de cancelamento
		String nrCtrc = ConhecimentoUtils.formatConhecimento(
				conhecimentoOriginal.getFilialByIdFilialOrigem().getSgFilial(), conhecimentoOriginal.getNrConhecimento()
		);

		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
			ConstantesSim.EVENTO_DOCUMENTO_CANCELADO,
			conhecimentoOriginal.getIdDoctoServico(),
			conhecimentoOriginal.getFilialByIdFilialOrigem().getIdFilial(),
			nrCtrc,
			JTDateTimeUtils.getDataHoraAtual(),
			null,
			dsObservacao,
			conhecimentoOriginal.getTpDocumentoServico().getValue()
		);
	}

	/**
	 * Metodos cancelamento CRT
	 * @author Andre Valadas
	 * 
	 * @param idDoctoServico
	 * @param nrCrt
	 * @param idMotivoCancelamento
	 * @param idFilialUsuario
	 */
	public void cancelCRT(Long idDoctoServico, Long idMotivoCancelamento, Long idFilialUsuario) {

		/* LMS-1325 - Não permite o cancelamento de Documentos Emitimos em meses fechados */
		final Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(idDoctoServico, false);
		ConhecimentoCancelarService.validateDataEmissao(conhecimento.getDhEmissao());

		//*** Busca Ultimo Evento do Docto p/ validação
		EventoDocumentoServico eventoDocumentoServico = eventoDocumentoServicoService.findUltimoEventoDoctoServico(idDoctoServico);
		//*** VALIDADE ***\\
		/** Se não retornar evento **/
		if(eventoDocumentoServico == null) {
			throw new BusinessException("LMS-04092");
		}
		//*** [1]-Docs possiveis de serem cancelados(DIGITADOS ou EMITIDOS)
		Short cdEvento = eventoDocumentoServico.getEvento().getCdEvento(); 
		if (!ConstantesSim.EVENTO_DOCUMENTO_DIGITADO.equals(cdEvento)
				&& !ConstantesSim.EVENTO_DOCUMENTO_EMITIDO.equals(cdEvento)) {
			throw new BusinessException("LMS-04092");
		}

		//*** [2]-Se filial Origem(Usuario) for Diferente de filial Origem do Documento
		Long idFilialOrigemDoc = eventoDocumentoServico.getFilial().getIdFilial();
		if (!idFilialOrigemDoc.equals(idFilialUsuario))
			throw new BusinessException("LMS-04084");

		//*** [3]-Verifica se CRT ja nao esta faturado
		List<Map<String, Object>> documentosFat = devedorDocServFatService.findDevedorDocServFatByDoctoServico(idDoctoServico);
		DomainValue tpFaturado = new DomainValue("P");
		for (Map<String, Object> map : documentosFat) {
			if (!tpFaturado.equals(map.get("tpSituacaoCobranca")))
				throw new BusinessException("LMS-04115");
		}
		storeCancelarCRT(idDoctoServico, idMotivoCancelamento);
	}

	private void storeCancelarCRT(Long idDoctoServico, Long idMotivoCancelamento) {
		//*** Busca ctoInternacional para Cancelamento
		CtoInternacional crt = ctoInternacionalService.findById(idDoctoServico);
		crt.setTpSituacaoCrt(new DomainValue("C"));
		MotivoCancelamento motivoCancelamento = new MotivoCancelamento();
		motivoCancelamento.setIdMotivoCancelamento(idMotivoCancelamento);
		crt.setMotivoCancelamento(motivoCancelamento);
		crt.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
		getCtoInternacionalDAO().store(crt);
		


		//*** Inclui Evento: "Conhecimento Cancelado"
		String nrCrt = ConhecimentoUtils.formatConhecimentoInternacional(
				crt.getSgPais(), crt.getNrPermisso(), crt.getNrCrt());
		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
			ConstantesSim.EVENTO_DOCUMENTO_CANCELADO,
			idDoctoServico,
			crt.getFilial().getIdFilial(),
			nrCrt,
			JTDateTimeUtils.getDataHoraAtual(),
			null,
			null,
			crt.getTpDocumentoServico().getValue()
		);
	}

	public CtoInternacionalDAO getCtoInternacionalDAO() {
		return ctoInternacionalDAO;
	}
	public void setCtoInternacionalDAO(CtoInternacionalDAO ctoInternacionalDAO) {
		this.ctoInternacionalDAO = ctoInternacionalDAO;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setCtoInternacionalService(CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}
	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}
	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}
	public void setReciboReembolsoService(ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}
	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}
	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setLiberacaoNotaNaturaService(LiberacaoNotaNaturaService liberacaoNotaNaturaService) {
		this.liberacaoNotaNaturaService = liberacaoNotaNaturaService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
}