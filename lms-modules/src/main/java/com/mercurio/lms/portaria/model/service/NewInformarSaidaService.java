package com.mercurio.lms.portaria.model.service;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.jms.VirtualTopics;
import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.*;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.LacreControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.EventoMeioTransporteService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsComBatchService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.AcaoIntegracaoEvento;
import com.mercurio.lms.portaria.model.dao.NewSaidaChegadaDAO;
import com.mercurio.lms.portaria.model.processo.SaidaPortaria;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;
import com.mercurio.lms.portaria.model.service.utils.EmailPreAlertaManifestoBuilder;
import com.mercurio.lms.portaria.model.service.utils.EventoDoctoServicoHelper;
import com.mercurio.lms.portaria.model.service.utils.InformarChegadaSaidaUtils;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDispositivoUnitizacao;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LMSA-1002 - Classe de serviços para processo "Informar Saída na Portaria"
 * para operações tipo Viagem.
 *
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 * @spring.bean id="lms.portaria.newInformarSaidaService"
 */
public class NewInformarSaidaService {

	private static final DomainValue TP_SITUACAO_EM_VIAGEM = new DomainValue(ConstantesPortaria.TP_SITUACAO_EM_VIAGEM);
	public static final String TP_CONTROLE_CARGA = "tpControleCarga";
	public static final String NR_IDENTIFICACAO = "nrIdentificacao";


	private IntegracaoJmsComBatchService integracaoJmsComBatchService;
	private NewSaidaChegadaDAO newSaidaChegadaDAO;
	private ControleCargaService controleCargaService;
	private EventoMeioTransporteService eventoMeioTransporteService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private ConfiguracoesFacade configuracoesFacade;
	private FronteiraRapidaService fronteiraRapidaService;
	private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
	private IntegracaoJmsService integracaoJmsService;
	private InformarSaidaService informarSaidaService;
	private FilialService filialService;
	private LacreControleCargaService lacreControleCargaService;
	private ControleEntSaidaTerceiroService controleEntSaidaTerceiroService;
	private OrdemSaidaService ordemSaidaService;
	private ConfiguracaoAuditoriaService configuracaoAuditoriaService;

	public EventoDocumentoServico gerarEventoDoctoServico(
			Filial filial,
			Usuario usuario,
			DoctoServico doctoServico,
			String nrDocumento,
			String dsObservacao,
			String tpDocumento,
			Evento evento,
			DateTime dhEvento) {
		incluirEventosRastreabilidadeInternacionalService.atualizaLocalizacaoMercadoria(
				evento,
				doctoServico,
				dhEvento,
				dsObservacao,
				null,
				null);

		if(doctoServico != null && doctoServico.getFilialByIdFilialOrigem() != null){
			if(doctoServico.getFilialByIdFilialOrigem() != null) {
				doctoServico.getFilialByIdFilialOrigem().getSgFilial(); //Init sgFilial origem
			}
			if(doctoServico.getFilialByIdFilialDestino() != null) {
				doctoServico.getFilialByIdFilialDestino().getSgFilial(); //Init sgFilial destino
			}
		}

		return incluirEventosRastreabilidadeInternacionalService.gerarEventoDocumento(
				evento,
				doctoServico,
				filial,
				nrDocumento,
				dhEvento,
				null,
				dsObservacao,
				tpDocumento,
				null,
				null,
				usuario,
				JTDateTimeUtils.getDataHoraAtual());
	}

	public void storeEventoDoctoServicoRastreabilidade(EventoDocumentoServico eventoDocumentoServico) {
		EventoDocumentoServicoDMN dto = EventoDoctoServicoHelper.convertEventoDoctoServico(eventoDocumentoServico);
		JmsMessageSender message = integracaoJmsService.createMessage(VirtualTopics.EVENTO_DOCUMENTO_SERVICO, dto);
		integracaoJmsService.storeMessage(message);
	}

	public void storeEventosDoctoServicoRastreabilidade(List<Serializable> eventoDocumentoServicos) {
		JmsMessageSender message = integracaoJmsService.createMessage(VirtualTopics.EVENTO_DOCUMENTO_SERVICO);
		message.addAllMsg(eventoDocumentoServicos);
		integracaoJmsService.storeMessage(message);
	}

	public EventoControleCarga gerarEventoControleCarga(
			Filial filial, ControleCarga controleCarga, String tpEvento, DateTime dhEvento) {
		return controleCargaService.generateEventoChangeStatusControleCarga(
				controleCarga,
				filial,
				tpEvento,
				dhEvento,
				controleCarga.getMeioTransporteByIdTransportado(),
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				false);
	}

	public List<EventoMeioTransporte> gerarEventoMeioTransporte(
			Filial filial, ControleTrecho controleTrecho, MeioTransporte meioTransporte, DateTime dhEvento) {
		EventoMeioTransporte eventoMeioTransporte = new EventoMeioTransporte();
		eventoMeioTransporte.setFilial(filial);
		eventoMeioTransporte.setControleCarga(controleTrecho.getControleCarga());
		eventoMeioTransporte.setControleTrecho(controleTrecho);
		eventoMeioTransporte.setMeioTransporte(meioTransporte);
		eventoMeioTransporte.setTpSituacaoMeioTransporte(TP_SITUACAO_EM_VIAGEM);
		return eventoMeioTransporteService.generateEvent(eventoMeioTransporte, dhEvento, false);
	}

	public void gerarEventosPrevisaoChegada(Filial filial, ControleCarga controleCarga, DateTime dhEvento) {
		controleCargaService.generateEventoPrevisaoChegadaParaDoctoServico(
				controleCarga.getIdControleCarga(), filial.getIdFilial(), dhEvento);
	}

	public long getEventoAcaoIntegracaoNrAgrupador() {
		return fronteiraRapidaService.getNrAgrupador();
	}

	public List<AcaoIntegracaoEvento> gerarEventosAcaoIntegracao(Manifesto manifesto) {
		return fronteiraRapidaService.findAcoesIntegracao(manifesto, null);
	}

	public EventoDispositivoUnitizacao gerarEventoDispositivoUnitizacao(
			Filial filial,
			Usuario usuario,
			DispositivoUnitizacao dispositivoUnitizacao,
			Evento evento,
			DateTime dhEvento) {
		return eventoDispositivoUnitizacaoService.generateEventoDispositivo(
				filial, usuario, dispositivoUnitizacao, evento, ConstantesPortaria.TP_SCAN_LMS, dhEvento);
	}

	public void gerarPreAlertaManifesto(Manifesto manifesto, List<DoctoServico> doctoServicos) {
		EmailPreAlertaManifestoBuilder.newEmailPreAlertaManifestoBuilder()
				.setConfiguracoesFacade(configuracoesFacade)
				.setIntegracaoJmsService(integracaoJmsService)
				.sendPreAlertaManifesto(manifesto, doctoServicos);
	}

	public void processarMensagens(Queues queue, List<Serializable> mensagens) {
		JmsMessageSender sender = integracaoJmsService.createMessage(queue);
		for (Serializable mensagem : mensagens) {
			sender.addMsg(mensagem);
		}
		integracaoJmsService.storeMessage(sender);
	}

	public Map<String, Object> findDados(TypedFlatMap parametros) {
		String idControleTemp = parametros.getString("idControleTemp");
		Long idControle = InformarChegadaSaidaUtils.getIdControle(idControleTemp);
		String tpSaida = InformarChegadaSaidaUtils.getTipo(idControleTemp);

		Map<String, Object> retorno = new HashMap<String, Object>();

		if (ConstantesPortaria.TP_VIAGEM.equals(tpSaida) || ConstantesPortaria.TP_COLETA_ENTREGA.equals(tpSaida)) {
			retorno = preencheDadosControleCarga(idControle, tpSaida);
		} else if (ConstantesPortaria.TP_TERCEIRO.equals(tpSaida)) {
			retorno = preencheDadosEntSaidaTerceiro(idControle);
		} else if (ConstantesPortaria.TP_ORDEM_SAIDA.equals(tpSaida)) {
			retorno = preencheDadosOrdemSaida(idControle);
		}

		String dsRota = (String) retorno.get("dsRota");
		if (dsRota == null) {
			dsRota = (String) retorno.get("dsRotaControleCarga");
		}
		Integer nrRota = retorno.get("nrRota") != null ? ((Short) retorno.get("nrRota")).intValue() : null;
		if (nrRota == null) {
			nrRota = (Integer) retorno.get("nrRotaIdaVolta");
		}
		retorno.put("dsRota", InformarChegadaSaidaUtils.formatarRota(nrRota, dsRota));

		retorno.put("dhSaida", JTDateTimeUtils.getDataHoraAtual());
		retorno.put("tpSaida", tpSaida);
		retorno.put("blInformaKmPortaria", getFilialService().findBlInformaKmPortaria(parametros.getLong("idFilial")));

		return retorno;
	}

	private Map<String, Object> preencheDadosOrdemSaida(Long idControle) {
		Map<String, Object> retorno = new HashMap<String, Object>();
		List dadosOrdemSaida = getOrdemSaidaService().findDadosChegadaSaida(idControle);
		if (!dadosOrdemSaida.isEmpty()) {
			retorno = (Map) dadosOrdemSaida.get(0);
			retorno.put("idOrdemServico", idControle);
			retorno.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao((DomainValue) retorno.get("tpIdentificacao"), (String) retorno.get(NR_IDENTIFICACAO)));
		}
		return retorno;
	}

	private Map<String, Object> preencheDadosEntSaidaTerceiro(Long idControle) {
		Map<String, Object> retorno = new HashMap<String, Object>();
		List dadosEntSaidaTerceiro = getControleEntSaidaTerceiroService().findDadosSaida(idControle);
		if (!dadosEntSaidaTerceiro.isEmpty()) {
			retorno = (Map) dadosEntSaidaTerceiro.get(0);
			retorno.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao("CPF", ((Long) retorno.get("nrCpf")).toString()));
			retorno.put("idControleEntSaidaTerceiro", idControle);
		}
		return retorno;
	}

	private Map<String, Object> preencheDadosControleCarga(Long idControle, String tpSaida) {
		Map<String, Object> retorno = new HashMap<String, Object>();
		List dadosControleCarga = getControleCargaService().findDadosChegadaSaida(idControle, tpSaida);
		if (!dadosControleCarga.isEmpty()) {
			retorno = (Map) dadosControleCarga.get(0);
			retorno.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao((DomainValue) retorno.get("tpIdentificacao"), (String) retorno.get(NR_IDENTIFICACAO)));
			retorno.put(TP_CONTROLE_CARGA, ((DomainValue) retorno.get(TP_CONTROLE_CARGA)).getValue());
			retorno.put("idControleCarga", idControle);
			retorno.put("lacres", getLacreControleCargaService().findByControleCarga(idControle));
		}
		return retorno;
	}

	public void validateManifestoEmTransito(TypedFlatMap parametros) {
		if (validaManifestoEmTransito(parametros.getString("subTipo"))) {
			getInformarSaidaService().validateManifestoETransito(parametros);
		}
	}

	/**
	 * Verifica, a partir do subtipo, se é necessário validar manifestos em trânsito
	 * Ordem de saída não requer validação de manifesto em trânsito pois
	 * uma ordem de saída não possui um controle de carga.
	 *
	 * @param subtipo
	 * @return boolean indicando se é necessário validar manifestos em trânsito
	 */
	private boolean validaManifestoEmTransito(String subtipo) {
		return !ConstantesPortaria.SUBTIPO_ORDEM_SAIDA.equals(subtipo);
	}

	public void processarEventosRastreabilidade(EventoDocumentoServico entidade, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
		IntegracaoJmsComBatchService.JmsMessageSender messageSender =
				integracaoJmsComBatchService.createMessage(VirtualTopics.EVENTO_DOCUMENTO_SERVICO,
						EventoDoctoServicoHelper.convertEventoDoctoServico((EventoDocumentoServico) entidade));
		integracaoJmsComBatchService.storeMessage(messageSender, adsmNativeBatchSqlOperations);
	}

	public Map<String, Object> executeFindDados(TypedFlatMap parametros) {
		return findDados(parametros);
	}

	public void executeSalvar(TypedFlatMap parametros) {
		String tpControleCarga = parametros.getString(TP_CONTROLE_CARGA);
		if (ConstantesPortaria.TP_VIAGEM.equals(tpControleCarga)) {
			SaidaPortaria.createSaidaPortaria(this, newSaidaChegadaDAO, parametros);
		} else {
			informarSaidaService.executeConfirmaSaidaColetaEntrega(parametros);
		}
	}

	public NewSaidaChegadaDAO getNewSaidaChegadaDAO() {
		return newSaidaChegadaDAO;
	}

	public void setNewSaidaChegadaDAO(NewSaidaChegadaDAO newSaidaChegadaDAO) {
		this.newSaidaChegadaDAO = newSaidaChegadaDAO;
	}

	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public EventoMeioTransporteService getEventoMeioTransporteService() {
		return eventoMeioTransporteService;
	}

	public void setEventoMeioTransporteService(
			EventoMeioTransporteService eventoMeioTransporteService) {
		this.eventoMeioTransporteService = eventoMeioTransporteService;
	}

	public IncluirEventosRastreabilidadeInternacionalService getIncluirEventosRastreabilidadeInternacionalService() {
		return incluirEventosRastreabilidadeInternacionalService;
	}

	public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public FronteiraRapidaService getFronteiraRapidaService() {
		return fronteiraRapidaService;
	}

	public void setFronteiraRapidaService(
			FronteiraRapidaService fronteiraRapidaService) {
		this.fronteiraRapidaService = fronteiraRapidaService;
	}

	public EventoDispositivoUnitizacaoService getEventoDispositivoUnitizacaoService() {
		return eventoDispositivoUnitizacaoService;
	}

	public void setEventoDispositivoUnitizacaoService(
			EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
		this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
	}

	public IntegracaoJmsService getIntegracaoJmsService() {
		return integracaoJmsService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public InformarSaidaService getInformarSaidaService() {
		return informarSaidaService;
	}

	public void setInformarSaidaService(InformarSaidaService informarSaidaService) {
		this.informarSaidaService = informarSaidaService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public LacreControleCargaService getLacreControleCargaService() {
		return lacreControleCargaService;
	}

	public void setLacreControleCargaService(
			LacreControleCargaService lacreControleCargaService) {
		this.lacreControleCargaService = lacreControleCargaService;
	}

	public ControleEntSaidaTerceiroService getControleEntSaidaTerceiroService() {
		return controleEntSaidaTerceiroService;
	}

	public void setControleEntSaidaTerceiroService(
			ControleEntSaidaTerceiroService controleEntSaidaTerceiroService) {
		this.controleEntSaidaTerceiroService = controleEntSaidaTerceiroService;
	}

	public OrdemSaidaService getOrdemSaidaService() {
		return ordemSaidaService;
	}

	public void setOrdemSaidaService(OrdemSaidaService ordemSaidaService) {
		this.ordemSaidaService = ordemSaidaService;
	}

	public ConfiguracaoAuditoriaService getConfiguracaoAuditoriaService() {
		return configuracaoAuditoriaService;
	}

	public void setConfiguracaoAuditoriaService(
			ConfiguracaoAuditoriaService configuracaoAuditoriaService) {
		this.configuracaoAuditoriaService = configuracaoAuditoriaService;
	}

	public void setIntegracaoJmsComBatchService(IntegracaoJmsComBatchService integracaoJmsComBatchService) {
		this.integracaoJmsComBatchService = integracaoJmsComBatchService;
	}

}
