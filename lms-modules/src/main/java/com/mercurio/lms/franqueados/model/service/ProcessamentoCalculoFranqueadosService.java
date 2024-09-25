package com.mercurio.lms.franqueados.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.franqueados.TipoCalculoFranqueado;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.service.BaixaDevMercService;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.ContaContabilFranqueado;
import com.mercurio.lms.franqueados.model.DescontoFranqueado;
import com.mercurio.lms.franqueados.model.DistanciaColetaEntregaFranqueado;
import com.mercurio.lms.franqueados.model.DistanciaTransferenciaFranqueado;
import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.FixoFranqueado;
import com.mercurio.lms.franqueados.model.Franquia;
import com.mercurio.lms.franqueados.model.FreteLocalFranqueado;
import com.mercurio.lms.franqueados.model.LancamentoFranqueado;
import com.mercurio.lms.franqueados.model.LimiteParticipacaoFranqueado;
import com.mercurio.lms.franqueados.model.MunicipioNaoAtendidoFranqueado;
import com.mercurio.lms.franqueados.model.ReembarqueDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.ReembarqueFranqueado;
import com.mercurio.lms.franqueados.model.RepasseFranqueado;
import com.mercurio.lms.franqueados.model.ServicoAdicionalFranqueado;
import com.mercurio.lms.franqueados.model.SimulacaoDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.SimulacaoReembarqueDoctoServicoFranqueado;
import com.mercurio.lms.franqueados.model.service.calculo.CalculoBDMFranqueadoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.CalculoFranqueadoParametros;
import com.mercurio.lms.franqueados.model.service.calculo.CalculoFranqueados;
import com.mercurio.lms.franqueados.model.service.calculo.CalculoReembarqueFranqueadoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.CalculoServicoAdicionalFranqueadoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.ConhecimentoFranqueadoDTO;
import com.mercurio.lms.franqueados.model.service.calculo.RecalculoServicoAdicionalFranqueadoDTO;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class ProcessamentoCalculoFranqueadosService {

	public static final String LOG_NAME_CALCULO_FRANQUEADOS = "CALCULO_FRANQUEADO";
	private static final Logger LOG = LogManager.getLogger(LOG_NAME_CALCULO_FRANQUEADOS);
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	
	private FranquiaService franquiaService;
	private ConhecimentoFranqueadoService conhecimentoFranqueadoService;
	private ParametroGeralService parametroGeralService;
	private RepasseFranqueadoService repasseFranqueadoService;
	private DescontoFranqueadoService descontoFranqueadoService;
	private GeneralidadeFranqueadoService generalidadeFranqueadoService;
	private FreteLocalFranqueadoService freteLocalFranqueadoService;
	private FixoFranqueadoService fixoFranqueadoService;
	private DistanciaColetaEntregaFranqueadoService distanciaColetaEntregaFranqueadoService;
	private DistanciaTransferenciaFranqueadoService distanciaTransferenciaFranqueadoService;
	private MunicipioFilialService municipioFilialService;
	private MunicipioNaoAtendidoFranqueadoService municipioNaoAtendidoFranqueadoService;
	private ConfiguracoesFacade configuracoesFacade;
	private ServicoAdicionalFranqueadoService servicoAdicionalFranqueadoService;
	private LimiteParticipacaoFranqueadoService limiteParticipacaoFranqueadoService;
	private ReembarqueFranqueadoService reembarqueFranqueadoService;
	private BaixaDevMercService baixaDevMercService;
	private ContaContabilFranqueadoService contaContabilFranqueadoService;
	private DoctoServicoFranqueadoService doctoServicoFranqueadoService;
	private LancamentoFranqueadoService lancamentoFranqueadoService;
	private ReembarqueDoctoServicoFranqueadoService reembarqueDoctoServicoFranqueadoService;
	private SimulacaoDoctoServicoFranqueadoService simulacaoDoctoServicoFranqueadoService;
	private SimulacaoReembarqueDoctoServicoFranqueadoService simulacaoReembarqueDoctoServicoFranqueadoService;
	private ManifestoService manifestoService;
	private IntegracaoJmsService integracaoJmsService; 
	
	private static final String LINE_SEPARATOR = VMProperties.LINE_SEPARATOR.getValue();

	public void executarProcessamentoCalculoFranqueadosSimulacao(TypedFlatMap parameters) {

		Long idFilial = parameters.getLong("idFilial");
		Long idFranquiaComparativa = parameters.getLong("idFranquiaBase");
		YearMonthDay dataCompetencia = parameters.getYearMonthDay("competencia");
		List<TypedFlatMap> listaMunicipios = (ArrayList<TypedFlatMap>) parameters.get("listaMunicipios");

		List<Long> idsMunicipio = new ArrayList<Long>();

		if (listaMunicipios != null) {
			for (TypedFlatMap tfm : listaMunicipios) {
				idsMunicipio.add(tfm.getLong("idMunicipioOrigem"));
			}
		}
		try {
			Queue<Serializable> documentoSimulacao = executeProcessamentoCalculoFranqueadosSimulacao(dataCompetencia, idFilial, idFranquiaComparativa, idsMunicipio);
			storeSimulacaoFranquia(documentoSimulacao);
			sendMailSucessoSimulacao();
		} catch (Exception e) {
			sendMailExcecaoSimulacao(e);
		}

	}

	private void sendMailSucessoSimulacao() {
		String textoEmail = configuracoesFacade.getMensagem("textoFinalizacaoSimulacao");
		sendMailSimulacao(textoEmail);
	}

	private void sendMailExcecaoSimulacao(Exception e) {
		StringBuilder textoErroEmail = new StringBuilder(configuracoesFacade.getMensagem("textoErroSimulacao"));
		textoErroEmail.append(LINE_SEPARATOR);
		textoErroEmail.append(e.getMessage());
		textoErroEmail.append(LINE_SEPARATOR);

		for (StackTraceElement stackTraceElement : e.getStackTrace()) {
			textoErroEmail.append(stackTraceElement);
			textoErroEmail.append(LINE_SEPARATOR);
		}

		sendMailSimulacao(textoErroEmail.toString());
	}

	private void sendMailSimulacao(String texto) {

		final String dsRemetente = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		String dsEmailDestinatario = SessionUtils.getUsuarioLogado().getDsEmail();
		final String dsAssunto = configuracoesFacade.getMensagem("assuntoFinalizacaoSimulacao");

		Mail mail = createMail(dsEmailDestinatario, dsRemetente, dsAssunto, texto);
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
 	}

	private Mail createMail(String strTo, String strFrom, String strSubject, String body) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		return mail;
	}

	public List<Franquia> findFranquiasParaCalculo(YearMonthDay dtBase) {
		return franquiaService.findFranquiasAtivasByCompetencia(dtBase, null);
	}

	public Queue<Serializable> executeProcessamentoCalculoFranqueadosSimulacao(YearMonthDay dataBase, Long idFilial,
			Long idFranquia, List<Long> idsMunicipio) {

		Integer month = JTDateTimeUtils.getDataAtual().minusMonths(1).getMonthOfYear();
		Integer year = JTDateTimeUtils.getDataAtual().getYear();

		Integer monthBase = dataBase.getMonthOfYear();
		Integer yearBase = dataBase.getYear();

		if (yearBase.compareTo(year) > 0 || (yearBase.compareTo(year) == 0 && monthBase.compareTo(month) >= 0)) {
			throw new IllegalArgumentException("Competencia deve ser inferior ao Mes corrente.");
		}

		CalculoFranqueadoParametros parametros = createParametrosCalculosMensal(dataBase);
		parametros.setTipoCalculo(TipoCalculoFranqueado.SIMULACAO);
		parametros.setIdFranquia(idFilial);
		parametros.setIdFranquiaBaseSimulacao(idFranquia);
		parametros.setIdsMunicipioSimulacao(idsMunicipio);
		return executeCalculoFranqueados(parametros);
	}

	public Queue<Serializable> executeProcessamentoCalculoFranqueadosMensal(YearMonthDay dataBase, Long idFranquia) {
		Integer month = JTDateTimeUtils.getDataAtual().getMonthOfYear();
		Integer year = JTDateTimeUtils.getDataAtual().getYear();

		Integer monthBase = dataBase.getMonthOfYear();
		Integer yearBase = dataBase.getYear();

		if (yearBase.compareTo(year) > 0 || (yearBase.compareTo(year) == 0 && monthBase.compareTo(month) >= 0)) {
			throw new IllegalArgumentException("Competencia deve ser inferior ao Mes corrente.");
		}

		CalculoFranqueadoParametros parametros = createParametrosCalculosMensal(dataBase);
		parametros.setTipoCalculo(TipoCalculoFranqueado.MENSAL);
		parametros.setIdFranquia(idFranquia);

		return executeCalculoFranqueados(parametros);
	}

	private CalculoFranqueadoParametros createParametrosCalculosMensal(YearMonthDay dtCompetencia) {
		CalculoFranqueadoParametros parametros = new CalculoFranqueadoParametros();
		parametros.setCompetencia(dtCompetencia);
		return parametros;
	}

	public Queue<Serializable> executeProcessamentoCalculoFranqueadosDiario(YearMonthDay dataBase, Long idFranquia) {
		if (dataBase.isAfter(JTDateTimeUtils.getDataAtual().minusDays(1))) {
			throw new IllegalArgumentException("Dia deve ser inferior ao Dia corrente.");
		}
		CalculoFranqueadoParametros parametros = createParametrosCalculosDiario(dataBase, dataBase);
		parametros.setTipoCalculo(TipoCalculoFranqueado.DIARIO);
		parametros.setIdFranquia(idFranquia);
		return executeCalculoFranqueados(parametros);
	}

	private CalculoFranqueadoParametros createParametrosCalculosDiario(YearMonthDay dtInicio, YearMonthDay dtFim) {
		CalculoFranqueadoParametros parametros = new CalculoFranqueadoParametros();
		parametros.setCompetencia(dtInicio, dtFim);
		return parametros;
	}

	private Queue<Serializable> executeCalculoFranqueados(CalculoFranqueadoParametros parametros) {
		CalculoFranqueadoParametros parametrosCalculo = buildParametrosGerais(parametros);
		CalculoFranqueados calculoParticipacao = new CalculoFranqueados(parametrosCalculo);
		return processarFranquia(calculoParticipacao);
	}

	private CalculoFranqueadoParametros buildParametrosGerais(CalculoFranqueadoParametros parametros) {
		Long psMinFreteCarreteiro = Long.valueOf(parametroGeralService
				.findSimpleConteudoByNomeParametro(ConstantesFranqueado.PARAMETRO_MIN_FRETE_CARRETEIRO));

		BigDecimal nrKmMinimoColetaEntrega = (BigDecimal) configuracoesFacade
				.getValorParametro("NR_KM_MINIMO_COLETA_ENTREGA");
		RepasseFranqueado repasseFranqueado = repasseFranqueadoService.findRepasseFranqueadoJdbc(
				parametros.getCompetencia().getInicio(), parametros.getCompetencia().getFim());
		List<DescontoFranqueado> descontoFranqueados = descontoFranqueadoService.findDescontoFranqueadoByCompetencia(
				parametros.getCompetencia().getInicio(), parametros.getCompetencia().getFim());
		FreteLocalFranqueado freteLocalFranqueado = freteLocalFranqueadoService
				.findByCompetencia(parametros.getCompetencia().getInicio(), parametros.getCompetencia().getFim());
		List<DistanciaTransferenciaFranqueado> lstDistanciaTransferencia = distanciaTransferenciaFranqueadoService
				.findDistanciaTransferenciaFranqueado(parametros.getCompetencia().getInicio());
		List<LimiteParticipacaoFranqueado> lstLimiteParticipacaoFranqueado = limiteParticipacaoFranqueadoService
				.findLimiteParticipacao(parametros.getCompetencia().getInicio());
		List<DistanciaColetaEntregaFranqueado> lstDistanciaColetaEntrega = distanciaColetaEntregaFranqueadoService
				.findDistanciaColetaEntregaFranqueado(parametros.getCompetencia().getInicio());
		ReembarqueFranqueado reembarqueFranqueado = reembarqueFranqueadoService
				.findReembarqueFranqueadoByDtVigenciaInicioFim(parametros.getCompetencia().getInicio(),
						parametros.getCompetencia().getFim());
		List<ServicoAdicionalFranqueado> servicoAdicionalFranqueado = servicoAdicionalFranqueadoService
				.findByIdServicoAdicional(parametros.getCompetencia().getInicio());
		List<TypedFlatMap> vlParcelaParticipacaoList = generalidadeFranqueadoService
				.findParcelaParticipacao(parametros.getCompetencia().getInicio(), parametros.getCompetencia().getFim());
		List<TypedFlatMap> vlParcelaPcParticipacaoList = generalidadeFranqueadoService
				.findParcelaParticipacao(parametros.getCompetencia().getInicio(), parametros.getCompetencia().getFim());

		ContaContabilFranqueado contaContabilFranqueado = contaContabilFranqueadoService.findContaByTipo(
				parametros.getCompetencia().getInicio(), ConstantesFranqueado.CONTA_CONTABIL_BDM,
				ConstantesFranqueado.TIPO_LANCAMENTO_DEBITO);

		parametros.setPsMinFreteCarreteiro(psMinFreteCarreteiro);
		parametros.setNrKmMinimoColetaEntrega(nrKmMinimoColetaEntrega);
		parametros.setRepasseFranqueado(repasseFranqueado);
		parametros.setDescontosFranqueados(descontoFranqueados);
		parametros.setFreteLocalFranqueado(freteLocalFranqueado);
		parametros.setLstDistanciaColetaEntrega(lstDistanciaColetaEntrega);
		parametros.setLstDistanciaTransferencia(lstDistanciaTransferencia);
		parametros.setLstLimiteParticipacao(lstLimiteParticipacaoFranqueado);
		parametros.setReembarqueFranqueado(reembarqueFranqueado);
		parametros.setServicoAdicionalFranqueado(servicoAdicionalFranqueado);
		parametros.setVlParcelaParticipacaoList(vlParcelaParticipacaoList);
		parametros.setVlParcelaPcParticipacaoList(vlParcelaPcParticipacaoList);
		parametros.setContaContabilFranqueado(contaContabilFranqueado);

		return parametros;
	}

	private Queue<Serializable> processarFranquia(CalculoFranqueados calculoParticipacao) {
		CalculoFranqueadoParametros parametros = calculoParticipacao.getCalculoFranqueadoParametros();
		setParametrosFranquia(parametros);
		if (parametros.isMensal()) {
			processamentoCalculoMensal(calculoParticipacao);
		} else if (parametros.isSimulacao()) {
			processamentoCalculoSimulacao(calculoParticipacao);
		} else {
			processamentoCalculoDiario(calculoParticipacao);
		}
		return calculoParticipacao.getDocumentos();
	}

	private void setParametrosFranquia(CalculoFranqueadoParametros parametrosCalculo) {
		parametrosCalculo.clearParametros();
		Long idFranquiaFixo = parametrosCalculo.getIdFranquia();
		if (parametrosCalculo.isSimulacao()) {
			idFranquiaFixo = parametrosCalculo.getIdFranquiaBaseSimulacao();
		}
		List<FixoFranqueado> parametrosFranquia = fixoFranqueadoService.findParametrosFranquiaOrdenado(idFranquiaFixo);
		List<MunicipioFilial> municipioFilial = municipioFilialService.findMunicipioByJdbcForFranqueado(
				parametrosCalculo.getIdFranquia(), parametrosCalculo.getCompetencia().getInicio());
		List<MunicipioNaoAtendidoFranqueado> municipioNaoAtendidoFranqueado = municipioNaoAtendidoFranqueadoService
				.findMunicipioByJdbc(parametrosCalculo.getIdFranquia(), parametrosCalculo.getCompetencia().getInicio());

		parametrosCalculo.setParametrosFranquia(parametrosFranquia);
		parametrosCalculo.setMunicipiosFiliais(municipioFilial);
		parametrosCalculo.setMunicipiosNaoAtendidoFranqueado(municipioNaoAtendidoFranqueado);
	}
	
	private void processamentoCalculoDiario(CalculoFranqueados calculoParticipacao) {
		executeCalculoDocumentos(calculoParticipacao);
		executeCalculoServicoAdicional(calculoParticipacao);
	}

	private void processamentoCalculoMensal(CalculoFranqueados calculoParticipacao) {
		executeCalculoDocumentos(calculoParticipacao);
		executeCalculoServicoAdicional(calculoParticipacao);
		executeCalculoReembarque(calculoParticipacao);
		executeRecalculoDocumentos(calculoParticipacao);
		executeRecalculoServicoAdicional(calculoParticipacao);
		executeCalculoBDM(calculoParticipacao);
	}

	private void processamentoCalculoSimulacao(CalculoFranqueados calculoParticipacao) {
		executeCalculoDocumentos(calculoParticipacao);
		executeCalculoServicoAdicional(calculoParticipacao);
		executeCalculoReembarque(calculoParticipacao);
	}

	private void executeCalculoServicoAdicional(CalculoFranqueados calculoParticipacao) {
		logInfo("executeCalculoServicoAdicional");
		Long psMinFreteCarreteiro = calculoParticipacao.getCalculoFranqueadoParametros().getPsMinFreteCarreteiro();
		List<CalculoServicoAdicionalFranqueadoDTO> servicoAdicionalFranqueadoList = conhecimentoFranqueadoService
				.findServicoAdicionalFranqueado(calculoParticipacao.getIdFranquia(),
						calculoParticipacao.getCalculoFranqueadoParametros().getCompetencia().getInicio(),
						calculoParticipacao.getCalculoFranqueadoParametros().getCompetencia().getFim(),
						psMinFreteCarreteiro);
		calculoParticipacao.addListConhecimentoFranqueado(servicoAdicionalFranqueadoList);
		calculoParticipacao.executarCalculo();
	}

	private void executeCalculoDocumentos(CalculoFranqueados cf) {
		logInfo("executeCalculoDocumentos");
		Long psMinFreteCarreteiro = cf.getCalculoFranqueadoParametros().getPsMinFreteCarreteiro();
		CalculoFranqueadoParametros paramentros = cf.getCalculoFranqueadoParametros();

		List<ConhecimentoFranqueadoDTO> conhecimentoFranqueadoList = this.conhecimentoFranqueadoService
				.findConhecimentoFranqueado(cf.getIdFranquia(),
						cf.getCalculoFranqueadoParametros().getCompetencia().getInicio(),
						cf.getCalculoFranqueadoParametros().getCompetencia().getFim(),
						psMinFreteCarreteiro, paramentros.getIdsMunicipioSimulacao(), paramentros.isSimulacao());
		cf.addListConhecimentoFranqueado(conhecimentoFranqueadoList);
		cf.executarCalculo();
	}

	private void executeCalculoBDM(CalculoFranqueados calculoParticipacao) {
		logInfo("executeCalculoBDM");
		List<CalculoBDMFranqueadoDTO> calculoBDMFranqueadoList = baixaDevMercService.findBMDFranqueado(
				calculoParticipacao.getIdFranquia(),
				calculoParticipacao.getCalculoFranqueadoParametros().getCompetencia().getInicio(),
				calculoParticipacao.getCalculoFranqueadoParametros().getCompetencia().getFim());
		calculoParticipacao.addListConhecimentoFranqueado(calculoBDMFranqueadoList);
		calculoParticipacao.executarCalculo();
	}

	private void executeRecalculoServicoAdicional(CalculoFranqueados calculoParticipacao) {
		logInfo("executeRecalculoServicoAdicional");
		List<RecalculoServicoAdicionalFranqueadoDTO> recalculoServicoAdicionalFranqueadoList = conhecimentoFranqueadoService
				.findRecalculoServicoAdicionalFranqueado(calculoParticipacao.getIdFranquia(),
						calculoParticipacao.getCalculoFranqueadoParametros().getCompetencia().getInicio(),
						calculoParticipacao.getCalculoFranqueadoParametros().getCompetencia().getFim());
		calculoParticipacao.addListConhecimentoFranqueado(recalculoServicoAdicionalFranqueadoList);
		calculoParticipacao.executarCalculo();
	}

	private void executeRecalculoDocumentos(CalculoFranqueados calculoParticipacao) {
		logInfo("executeRecalculoDocumentos");
		Long psMinFreteCarreteiro = calculoParticipacao.getCalculoFranqueadoParametros().getPsMinFreteCarreteiro();
		List<ConhecimentoFranqueadoDTO> recalculoFranqueadoList = conhecimentoFranqueadoService
				.findRecalculoConhecimentoFranqueado(calculoParticipacao.getIdFranquia(),
						calculoParticipacao.getCalculoFranqueadoParametros().getCompetencia().getInicio(),
						calculoParticipacao.getCalculoFranqueadoParametros().getCompetencia().getFim(),
						psMinFreteCarreteiro);
		calculoParticipacao.addListConhecimentoFranqueado(recalculoFranqueadoList);
		calculoParticipacao.executarCalculo();
	}

	private void executeCalculoReembarque(CalculoFranqueados calculoParticipacao) {
		logInfo("executeCalculoReembarque");
		CalculoFranqueadoParametros parametros = calculoParticipacao.getCalculoFranqueadoParametros();
		List<CalculoReembarqueFranqueadoDTO> reembarqueFranqueadoList = conhecimentoFranqueadoService
				.findReembarcadoFranqueado(calculoParticipacao.getIdFranquia(),
						calculoParticipacao.getCalculoFranqueadoParametros().getCompetencia().getInicio(),
						calculoParticipacao.getCalculoFranqueadoParametros().getCompetencia().getFim(),
						parametros.isSimulacao());
		calculoParticipacao.addListConhecimentoFranqueado(reembarqueFranqueadoList);
		calculoParticipacao.executarCalculo();
	}

	public void storeDocumentosFranquiaMensal(Long idFranquia, YearMonthDay dtCompetencia, Queue<Serializable> documentos) {
		this.doctoServicoFranqueadoService.removeByCompetencia(dtCompetencia, idFranquia);
		storeDocumentosFranquia(documentos);
	}

	public void storeDocumentosFranquiaDiario(Queue<Serializable> documentos) {
		storeDocumentosFranquia(documentos, true); 
	}
	
	public void storeDocumentosFranquia(Queue<Serializable> documentos) {
		storeDocumentosFranquia(documentos, false); 
	}

	public void storeDocumentosFranquia(Queue<Serializable> documentos, boolean alterarCompetencia) {
		List<DoctoServicoFranqueado> listDoctoServicoFranqueados = new ArrayList<>();
		List<LancamentoFranqueado> listLancamentoFranqueado = new ArrayList<>();
		List<ReembarqueDoctoServicoFranqueado> listReembarqueDoctoServicoFranqueado = new ArrayList<ReembarqueDoctoServicoFranqueado>();
		Serializable documento;
		do {
			documento = documentos.poll();
			if (documento instanceof DoctoServicoFranqueado) {
				DoctoServicoFranqueado doctoServicoFranqueado = (DoctoServicoFranqueado) documento;
				if (alterarCompetencia) {
					doctoServicoFranqueado.setDtCompetencia(JTDateTimeUtils.setDay(doctoServicoFranqueado.getDtCompetencia(), 1));
				}					
				listDoctoServicoFranqueados.add(doctoServicoFranqueado);
			} else if (documento instanceof LancamentoFranqueado) {
				listLancamentoFranqueado.add((LancamentoFranqueado) documento);
			} else if (documento instanceof ReembarqueDoctoServicoFranqueado) {
				listReembarqueDoctoServicoFranqueado.add((ReembarqueDoctoServicoFranqueado) documento);
			}
		} while (documento != null);
		if (!listDoctoServicoFranqueados.isEmpty()) {
			doctoServicoFranqueadoService.storeAll(listDoctoServicoFranqueados);
		}
		if (!listLancamentoFranqueado.isEmpty()) {
			lancamentoFranqueadoService.storeAll(listLancamentoFranqueado);
		}
		if (!listReembarqueDoctoServicoFranqueado.isEmpty()) {
			for (ReembarqueDoctoServicoFranqueado r : listReembarqueDoctoServicoFranqueado) {
				r.setManifesto(manifestoService.findById(r.getManifesto().getIdManifesto()));
			}
			reembarqueDoctoServicoFranqueadoService.storeAll(listReembarqueDoctoServicoFranqueado);
		}
	}

	public void storeSimulacaoFranquia(Queue<Serializable> documentos) {
		List<SimulacaoDoctoServicoFranqueado> listSimulacaoDoctoServicoFranqueados = new ArrayList<SimulacaoDoctoServicoFranqueado>();
		List<SimulacaoReembarqueDoctoServicoFranqueado> listSimulacaoReembarque = new ArrayList<SimulacaoReembarqueDoctoServicoFranqueado>();

		Serializable documento;
		do {
			documento = documentos.poll();
			if (documento instanceof SimulacaoDoctoServicoFranqueado) {
				listSimulacaoDoctoServicoFranqueados.add((SimulacaoDoctoServicoFranqueado) documento);
			} else if (documento instanceof SimulacaoReembarqueDoctoServicoFranqueado) {
				listSimulacaoReembarque.add((SimulacaoReembarqueDoctoServicoFranqueado) documento);
			}
		} while (documento != null);
		if (!listSimulacaoDoctoServicoFranqueados.isEmpty()) {
			simulacaoDoctoServicoFranqueadoService.storeAll(listSimulacaoDoctoServicoFranqueados);
		}
		if (!listSimulacaoReembarque.isEmpty()) {
			simulacaoReembarqueDoctoServicoFranqueadoService.storeAll(listSimulacaoReembarque);
		}
	}

	public void setFranquiaService(FranquiaService franquiaService) {
		this.franquiaService = franquiaService;
	}

	public void setConhecimentoFranqueadoService(ConhecimentoFranqueadoService conhecimentoFranqueadoService) {
		this.conhecimentoFranqueadoService = conhecimentoFranqueadoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setRepasseFranqueadoService(RepasseFranqueadoService repasseFranqueadoService) {
		this.repasseFranqueadoService = repasseFranqueadoService;
	}

	public void setDescontoFranqueadoService(DescontoFranqueadoService descontoFranqueadoService) {
		this.descontoFranqueadoService = descontoFranqueadoService;
	}

	public void setGeneralidadeFranqueadoService(GeneralidadeFranqueadoService generalidadeFranqueadoService) {
		this.generalidadeFranqueadoService = generalidadeFranqueadoService;
	}

	public void setFreteLocalFranqueadoService(FreteLocalFranqueadoService freteLocalFranqueadoService) {
		this.freteLocalFranqueadoService = freteLocalFranqueadoService;
	}

	public void setFixoFranqueadoService(FixoFranqueadoService fixoFranqueadoService) {
		this.fixoFranqueadoService = fixoFranqueadoService;
	}

	public void setDistanciaColetaEntregaFranqueadoService(
			DistanciaColetaEntregaFranqueadoService distanciaColetaEntregaFranqueadoService) {
		this.distanciaColetaEntregaFranqueadoService = distanciaColetaEntregaFranqueadoService;
	}

	public void setDistanciaTransferenciaFranqueadoService(
			DistanciaTransferenciaFranqueadoService distanciaTransferenciaFranqueadoService) {
		this.distanciaTransferenciaFranqueadoService = distanciaTransferenciaFranqueadoService;
	}

	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	public void setMunicipioNaoAtendidoFranqueadoService(
			MunicipioNaoAtendidoFranqueadoService municipioNaoAtendidoFranqueadoService) {
		this.municipioNaoAtendidoFranqueadoService = municipioNaoAtendidoFranqueadoService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setServicoAdicionalFranqueadoService(
			ServicoAdicionalFranqueadoService servicoAdicionalFranqueadoService) {
		this.servicoAdicionalFranqueadoService = servicoAdicionalFranqueadoService;
	}

	public void setLimiteParticipacaoFranqueadoService(
			LimiteParticipacaoFranqueadoService limiteParticipacaoFranqueadoService) {
		this.limiteParticipacaoFranqueadoService = limiteParticipacaoFranqueadoService;
	}

	public void setDoctoServicoFranqueadoService(DoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}

	public void setLancamentoFranqueadoService(LancamentoFranqueadoService lancamentoFranqueadoService) {
		this.lancamentoFranqueadoService = lancamentoFranqueadoService;
	}

	public void setReembarqueDoctoServicoFranqueadoService(
			ReembarqueDoctoServicoFranqueadoService reembarqueDoctoServicoFranqueadoService) {
		this.reembarqueDoctoServicoFranqueadoService = reembarqueDoctoServicoFranqueadoService;
	}

	public void setReembarqueFranqueadoService(ReembarqueFranqueadoService reembarqueFranqueadoService) {
		this.reembarqueFranqueadoService = reembarqueFranqueadoService;
	}

	public void setBaixaDevMercService(BaixaDevMercService baixaDevMercService) {
		this.baixaDevMercService = baixaDevMercService;
	}

	public void setContaContabilFranqueadoService(ContaContabilFranqueadoService contaContabilFranqueadoService) {
		this.contaContabilFranqueadoService = contaContabilFranqueadoService;
	}

	public void setSimulacaoDoctoServicoFranqueadoService(
			SimulacaoDoctoServicoFranqueadoService simulacaoDoctoServicoFranqueadoService) {
		this.simulacaoDoctoServicoFranqueadoService = simulacaoDoctoServicoFranqueadoService;
	}

	public void setSimulacaoReembarqueDoctoServicoFranqueadoService(
			SimulacaoReembarqueDoctoServicoFranqueadoService simulacaoReembarqueDoctoServicoFranqueadoService) {
		this.simulacaoReembarqueDoctoServicoFranqueadoService = simulacaoReembarqueDoctoServicoFranqueadoService;
	}

	private void logInfo(String mensagem, Object... parametros) {
		if (LOG.isInfoEnabled()) {
			LOG.info(String.format(mensagem, parametros));
		}
	}

	public ManifestoService getManifestoService() {
		return manifestoService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}	
}
