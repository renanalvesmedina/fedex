package com.mercurio.lms.sgr.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.joda.time.DateTime;
import org.springframework.util.CollectionUtils;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.pgr.retornosmp.EventoSmpDto;
import br.com.tntbrasil.integracao.domains.pgr.retornosmp.MensagemDto;
import br.com.tntbrasil.integracao.domains.pgr.retornosmp.RetornoEventosSmpDto;
import br.com.tntbrasil.integracao.domains.pgr.retornosmp.RetornoSmpDto;
import br.com.tntbrasil.integracao.domains.pgr.smpIn.MeioTransporteDto;
import br.com.tntbrasil.integracao.domains.pgr.smpIn.MotoristaDto;
import br.com.tntbrasil.integracao.domains.pgr.smpIn.PontoCargaDto;
import br.com.tntbrasil.integracao.domains.pgr.smpIn.RastreadorDto;
import br.com.tntbrasil.integracao.domains.pgr.smpIn.SmpDto;
import br.com.tntbrasil.integracao.domains.pgr.smpIn.SmpPontoDto;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.service.FluxoFilialService;
import com.mercurio.lms.sgr.dto.EnquadramentoRegraDTO;
import com.mercurio.lms.sgr.dto.ExigenciaGerRiscoDTO;
import com.mercurio.lms.sgr.dto.PlanoGerenciamentoRiscoDTO;
import com.mercurio.lms.sgr.model.EventoSMP;
import com.mercurio.lms.sgr.model.ExigenciaFaixaValor;
import com.mercurio.lms.sgr.model.ExigenciaSmp;
import com.mercurio.lms.sgr.model.FaixaDeValorSmp;
import com.mercurio.lms.sgr.model.GerenciadoraRisco;
import com.mercurio.lms.sgr.model.SmpManifesto;
import com.mercurio.lms.sgr.model.SolicMonitPreventivo;
import com.mercurio.lms.sgr.model.VirusCarga;
import com.mercurio.lms.sgr.util.ConstantesGerRisco;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.gerarEnviarSMPService"
 */
public class GerarEnviarSMPService {
    
    public static final BigDecimal DEFAULT_VL_CARGA = new BigDecimal(0.01).setScale(2,BigDecimal.ROUND_HALF_EVEN);

	private ConfiguracoesFacade configuracoesFacade;
	private MoedaPaisService moedaPaisService;
	private ManifestoService manifestoService;
	private ExigenciaSmpService exigenciaSmpService;
	private SmpManifestoService smpManifestoService;
	private ControleCargaService controleCargaService;
	private ParametroGeralService parametroGeralService;
	private ControleTrechoService controleTrechoService;
	private GerenciadoraRiscoService gerenciadoraRiscoService;	
	private SolicMonitPreventivoService solicMonitPreventivoService;
	private FaixaDeValorSmpService faixaDeValorSmpService;
	private DomainValueService domainValueService;
	private FluxoFilialService fluxoFilialService;
	private EventoSMPService eventoSMPService;
	private IntegracaoJmsService integracaoJmsService;
	private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
	private VirusCargaService virusCargaService;
	
	public void setEventoSMPService(EventoSMPService eventoSMPService) {
		this.eventoSMPService = eventoSMPService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}
	public void setSolicMonitPreventivoService(SolicMonitPreventivoService solicMonitPreventivoService) {
		this.solicMonitPreventivoService = solicMonitPreventivoService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}
	public void setExigenciaSmpService(ExigenciaSmpService exigenciaSmpService) {
		this.exigenciaSmpService = exigenciaSmpService;
	}
	public void setGerenciadoraRiscoService(GerenciadoraRiscoService gerenciadoraRiscoService) {
		this.gerenciadoraRiscoService = gerenciadoraRiscoService;
	}
	
	public void setSmpManifestoService(SmpManifestoService smpManifestoService) {
		this.smpManifestoService = smpManifestoService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	
	
	/**
	 * Método referente ao processo 11.01.01.04 <br>
	 * Gerar SMP
	 * @param idControleCarga
	 * @return idSolicMonitPreventivo gerado
	 * @author luisfco
	 */
	public Long generateGerarSMP(Long idControleCarga, PlanoGerenciamentoRiscoDTO planoPGR) {
		
		ControleCarga controleCarga = this.controleCargaService.findById(idControleCarga);
		verifyControleCarga(controleCarga);
		Filial filialUsuario = SessionUtils.getFilialSessao();
			
		// REGRA 2 
		
		//6853
		if (controleCargaService.isMonitoradoIdTransportado(idControleCarga)
				|| controleCargaService.isMonitoradoSemiRebocado(idControleCarga)) {

			// REGRAS 1 e 4
			SolicMonitPreventivo solicMonitPreventivo = storeSolicMonitPreventivo(controleCarga, filialUsuario);
			
			// 6853
			storeFaixaDeValorSmp(planoPGR, solicMonitPreventivo);

			// REGRA 5
			// 6853
			storeExigenciasSMP(planoPGR, solicMonitPreventivo);

			// REGRA 6
			storeManifestosSMP(solicMonitPreventivo, idControleCarga);

			return solicMonitPreventivo.getIdSolicMonitPreventivo();
		}
		return null;
	}
	
	/**
	 * 
	 * Quando envia para fila grava erro padrao e status da operacao que gerou erro
	 * para ser reenviada
	 * 
	 * @param retornoSmpDto
	 * @param domainErro
	 */
	public void storeSMPRetornoErro(RetornoSmpDto retornoSmpDto, DomainValue domainErro){
		SolicMonitPreventivo smp =  solicMonitPreventivoService.findById(retornoSmpDto.getIdSmp());
		if (retornoSmpDto.getListMensagem() != null) {
			smp.setDsRetornoGR(criarMensagemRetorno(retornoSmpDto.getListMensagem()));
		}
		if (domainErro != null) {
			smp.setTpStatusSmpGR(domainErro);
		}
		solicMonitPreventivoService.store(smp);
	}
	
	/**
	 * É chamado por @RetornoSmpRest
	 * é o retorno da apisul, se foi incluida chama eventos
	 * senao coloca mensagem de erro e atualiza status para ser reenviada
	 * 
	 * @param retornoSmpDto
	 */
	public void storeSMPRetornoInsere(RetornoSmpDto retornoSmpDto){ SolicMonitPreventivo smp =  solicMonitPreventivoService.findById(retornoSmpDto.getIdSmp());
		smp.setNrSmpGR(retornoSmpDto.getNrSmp());
		if(retornoSmpDto.getAnoSmp()!=null){
			smp.setNrSmpAnoGR(retornoSmpDto.getAnoSmp().intValue());
		}

		if (!CollectionUtils.isEmpty(retornoSmpDto.getListMensagem())) {
			smp.setDsRetornoGR(criarMensagemRetorno(retornoSmpDto.getListMensagem()));
		}
		
		if (retornoSmpDto.isTransacaoOk()) {
			smp.setTpStatusSmpGR(new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_GR_INCLUIDA));
			solicMonitPreventivoService.store(smp);
		}else{
			smp.setTpStatusSmpGR(new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_GR_ERRO_INSERIR));
			solicMonitPreventivoService.store(smp);
		}
	}
	
	
	/**
	 * É chamado por @RetornoSmpRest
	 * Quando retorna da apisul verifica se é um evento final, senão continua enviando eventos
	 * @param retornoSmpDto
	 */
	public void storeSMPRetornoEventos(RetornoEventosSmpDto retornoEventosSmpDto) {
		List<EventoSmpDto> eventos = retornoEventosSmpDto.getListEventos();
		
		Boolean pararMonitEventos = Boolean.FALSE;
		SolicMonitPreventivo smp = solicMonitPreventivoService.findById(retornoEventosSmpDto.getIdSmp());
		
		if (!CollectionUtils.isEmpty(eventos)) {
			
			for (EventoSmpDto eventoSmpDto : eventos) {
				if (Integer.valueOf(ConstantesGerRisco.EVENTO_SMP_APROVADA).equals(eventoSmpDto.getTipoEvento())) {
					smp.setTpStatusSmpGR(new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_GR_SMP_EM_MONITORAMENTO));
					smp.setDsRetornoGR(new VarcharI18n(eventoSmpDto.getDsDescricaoEvento()));
					pararMonitEventos = Boolean.TRUE;
				} else if (Integer.valueOf(ConstantesGerRisco.EVENTO_SMP_REJEITADA).equals(eventoSmpDto.getTipoEvento())) {
					smp.setTpStatusSmpGR(new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_GR_SMP_REJEITADA));
					smp.setDsRetornoGR(new VarcharI18n(eventoSmpDto.getDsDescricaoEvento()));
					pararMonitEventos = Boolean.TRUE;
				}
				if (!CollectionUtils.isEmpty(retornoEventosSmpDto.getListMensagem())) {
					smp.setDsRetornoGR(criarMensagemRetorno(retornoEventosSmpDto.getListMensagem()));
				}
				solicMonitPreventivoService.store(smp);
				insereEventoSmp(smp, eventoSmpDto);
			}
		}

		if (ConstantesGerRisco.TP_STATUS_SMP_GR_SMP_CANCELADA.equals(smp.getTpStatusSmp().getValue())
				|| ConstantesGerRisco.TP_STATUS_SMP_GR_SMP_EM_MONITORAMENTO.equals(smp.getTpStatusSmp().getValue())) {
			pararMonitEventos = Boolean.TRUE;
		}
		
		if (BooleanUtils.isFalse(pararMonitEventos)) {
			generateEnviarEventoSMP(smp.getIdSolicMonitPreventivo(), smp.getNrSmpAnoGR(), smp.getNrSmpGR(), retornoEventosSmpDto.getTimestamp());
		}
	}	

	/**
	 * gravar evento associado a smp
	 * @param smp
	 * @param eventoSmpDto
	 */
	private void insereEventoSmp(SolicMonitPreventivo smp, EventoSmpDto eventoSmpDto) {
		EventoSMP eventoSmp = new EventoSMP();
		eventoSmp.setCdEvento(eventoSmpDto.getTipoEvento().toString());
		eventoSmp.setDhEvento(JTDateTimeUtils.getDataHoraAtual());
		eventoSmp.setSolicMonitPreventivo(smp);
		eventoSmp.setDsEvento(eventoSmpDto.getDsDescricaoEvento());
		eventoSMPService.store(eventoSmp);
	}
	
	
	/**
	 * É chamado por @RetornoSmpRest
	 * Quando retorna da apisul grava status de cancelado ou mostrar os erros
	 * @param retornoSmpDto
	 */
	public void storeSMPRetornoCancela(RetornoSmpDto retornoSmpDto){
		SolicMonitPreventivo smp =  solicMonitPreventivoService.findById(retornoSmpDto.getIdSmp());
		if (!CollectionUtils.isEmpty(retornoSmpDto.getListMensagem())) {
			smp.setDsRetornoGR(criarMensagemRetorno(retornoSmpDto.getListMensagem()));
		}
		
		if (retornoSmpDto.isTransacaoOk()) {
			if (retornoSmpDto.isSMPCancelada()) {
				smp.setTpStatusSmpGR(new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_GR_SMP_CANCELADA));
			}
		}
		solicMonitPreventivoService.store(smp);
	}

	/**
	 * É chamado por @RetornoSmpRest
	 * Quando retorna da apisul grava status de finalizado ou mostrar os erros
	 * @param retornoSmpDto
	 */
	public void storeSMPRetornoFinaliza(RetornoSmpDto retornoSmpDto){
		SolicMonitPreventivo smp =  solicMonitPreventivoService.findById(retornoSmpDto.getIdSmp());
		if (!CollectionUtils.isEmpty(retornoSmpDto.getListMensagem())) {
			smp.setDsRetornoGR(criarMensagemRetorno(retornoSmpDto.getListMensagem()));
		}
		if (retornoSmpDto.isTransacaoOk()) {
			if (retornoSmpDto.isSMPFinalizada()) {
				smp.setTpStatusSmpGR(new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_GR_FINALIZADA));
			}
		}
		solicMonitPreventivoService.store(smp);
	}
	
	/**
	 * trata a mensagem de retorno para gravar na smp
	 * @param listMensagem
	 * @return
	 */
	private VarcharI18n criarMensagemRetorno(List<MensagemDto> listMensagem) {
		StringBuilder mensagem = new StringBuilder();
		for (MensagemDto mensagemDto : listMensagem) {
			if(mensagemDto.getCodigo()!=null){
				mensagem.append(mensagemDto.getCodigo().toString()+" - ");
			}
			mensagem.append(mensagemDto.getMensagem()+" \n");
		}
		return new VarcharI18n(mensagem.toString());
	}
	
	/**
	 * enviar para apisul solicitação para gerar eventos
	 * @param idControleCarga
	 * @param idSmp
	 */
	public void generateEnviarEventoSMP(Long idSmp, Integer nrSmpAnoGR, Integer nrSmpGR, Long timestamp) {		
		Serializable message = populaBuscaEventoSmp(idSmp, nrSmpAnoGR, nrSmpGR, timestamp);
		try {
			JmsMessageSender msg = integracaoJmsService.createMessage(Queues.PGR_SERVICE_SEND_EVENTO_SMP, message);
			integracaoJmsService.storeMessage(msg);
		} catch (Exception e) {
			RetornoSmpDto retornoSmpDto = buildRetornoException(configuracoesFacade.getMensagem(ConstantesGerRisco.ERRO_INSERIR_SMP_FILA)+" - "+e.getMessage());
			retornoSmpDto.setIdSmp(idSmp);
			storeSMPRetornoErro(retornoSmpDto, null); 
		}
	}
	
	private Serializable populaBuscaEventoSmp(Long idSmp, Integer nrSmpAnoGR, Integer nrSmpGR, Long timestamp) {
		EventoSmpDto eventoSmpDto = new EventoSmpDto();
		eventoSmpDto.setIdSmp(idSmp);
		eventoSmpDto.setNrSmpAnoGR(nrSmpAnoGR);
		eventoSmpDto.setNrSmpGR(nrSmpGR);
		eventoSmpDto.setTimestamp(timestamp);
		return eventoSmpDto;
	}
	/**
	 * enviar para apisul solicitação para finalizar a smp
	 * @param idControleCarga
	 * @param idSmp
	 */
	public void generateFinalizarSMP(Long idControleCarga, Long idSmp) {
		try {
		storeStatusSmp(idSmp, new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_FINALIZADA));				
		Serializable message = populaAnoNrSMP(idControleCarga, idSmp);
		storeStatusSmpGr(idSmp, new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_GR_FINALIZACAO_ENVIADA));
			JmsMessageSender msg = integracaoJmsService.createMessage(Queues.PGR_SERVICE_SEND_SMP_FINALIZACAO, message);
			integracaoJmsService.storeMessage(msg);
		} catch (Exception e) {
			RetornoSmpDto retornoSmpDto = buildRetornoException(configuracoesFacade.getMensagem(ConstantesGerRisco.ERRO_INSERIR_SMP_FILA)+" - "+e.getMessage());
			retornoSmpDto.setIdSmp(idSmp);
			storeSMPRetornoErro(retornoSmpDto, new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_GR_ERRO_FINALIZAR));
		}
	}
	
	/**
	 * Popula dados de Finalização e Cancelamento da SMP
	 * 
	 * @param idControleCarga
	 * @param idSmp
	 * @return
	 * @throws Exception 
	 */
	private Serializable populaAnoNrSMP(Long idControleCarga, Long idSmp) throws Exception {
		SolicMonitPreventivo smp =  solicMonitPreventivoService.findById(idSmp);
		validarSMPSolicMonitPreventivo(smp);
		SmpDto smpDto = new SmpDto();
		smpDto.setIdSmp(idSmp);
		smpDto.setNrSmpAnoGR(smp.getNrSmpAnoGR());
		smpDto.setNrSmpGR(smp.getNrSmpGR());
		return smpDto;
	}
	
	/**
	 * Validar o cancelarSMP e FinalizarSMP
	 * @param smp
	 * @throws Exception
	 */
	private void validarSMPSolicMonitPreventivo(SolicMonitPreventivo smp) throws Exception {
		if(null == smp.getNrSmpGR() || null == smp.getNrSmpAnoGR()) {
			throw new Exception("NrSmpAnoGR e NrSmpGr não pode ser nulo");
		}
	}
	
	/**
	 * enviar para apisul solicitação para cancelar a smp
	 * @param idControleCarga
	 * @param idSmp
	 */
	public void generateCancelarSMP(Long idControleCarga, Long idSmp) {
		try {
		storeStatusSmp(idSmp, new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_CANCELADA));
		Serializable message = populaAnoNrSMP(idControleCarga, idSmp);
			JmsMessageSender msg = integracaoJmsService.createMessage(Queues.PGR_SERVICE_SEND_SMP_CANCELAMENTO, message);
			integracaoJmsService.storeMessage(msg);
			storeStatusSmpGr(idSmp,new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_GR_CANCELAMENTO_ENVIADO));
		} catch (Exception e) {
			RetornoSmpDto retornoSmpDto = buildRetornoException(configuracoesFacade.getMensagem(ConstantesGerRisco.ERRO_INSERIR_SMP_FILA)+" - "+e.getMessage());
			retornoSmpDto.setIdSmp(idSmp);
			storeSMPRetornoErro(retornoSmpDto,  new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_GR_SMP_ERRO_CANCELAR_SMP));
		}
	}
	
	/**
	 * usada por @ManterSMPAction para reenviar smp
	 * reenvia somente quando os status foram erros de inserir finalizar ou cancelar
	 * @param idControleCarga
	 * @param idSolicMonitPreventivo
	 */
	public void generateReenviarSmp(Long idControleCarga, Long idSmp){
		SolicMonitPreventivo smp = solicMonitPreventivoService.findById(idSmp);
		if (ConstantesGerRisco.TP_STATUS_SMP_GR_ERRO_INSERIR.equals(smp.getTpStatusSmpGR().getValue())) {
			generateEnviarSMP(idControleCarga, idSmp);
		} else if (ConstantesGerRisco.TP_STATUS_SMP_GR_ERRO_FINALIZAR.equals(smp.getTpStatusSmpGR().getValue())) {
			generateFinalizarSMP(idControleCarga, idSmp);
		} else if (ConstantesGerRisco.TP_STATUS_SMP_GR_SMP_ERRO_CANCELAR_SMP.equals(smp.getTpStatusSmpGR().getValue())) {
			generateCancelarSMP(idControleCarga, idSmp);
		}
	}
	
	/**
	 * enviar para apisul solicitação para inserir a smp
	 * @param idControleCarga
	 * @param idSmp
	 */
	public void generateEnviarSMP(Long idControleCarga, Long idSmp){		
		try {
			Serializable smp = popularDadosEnvioSMP(idControleCarga, idSmp);
			if(smp!= null) {
			JmsMessageSender msg = integracaoJmsService.createMessage(Queues.PGR_SERVICE_SEND_SMP, smp);
			integracaoJmsService.storeMessage(msg);
			storeStatusSmpGr(idSmp, new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_GR_ENVIADA));
			}
		} catch (Exception e) {
			gravarMensagemErro(idSmp, configuracoesFacade.getMensagem(ConstantesGerRisco.ERRO_INSERIR_SMP)+" - "+e.getCause());
		} 
	}

	private void gravarMensagemErro(Long idSmp, String menssagemErro) {
		RetornoSmpDto retornoSmpDto = buildRetornoException(menssagemErro);
		retornoSmpDto.setAnoSmp(Short.valueOf("0"));
		retornoSmpDto.setNrSmp(0);
		retornoSmpDto.setIdSmp(idSmp);
		storeSMPRetornoErro(retornoSmpDto,new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_GR_ERRO_INSERIR));
	}
	
	/**
	 * grava status da integração  da smp 
	 * @param idSmp
	 * @param tpStatusSmpGR
	 */
	private void storeStatusSmpGr(Long idSmp, DomainValue tpStatusSmpGR) {
		SolicMonitPreventivo smp =  solicMonitPreventivoService.findById(idSmp);
		smp.setTpStatusSmpGR(tpStatusSmpGR);
		smp.setDsRetornoGR(null);
		solicMonitPreventivoService.store(smp);
	}
	
	/**
	 * grava status da smp 
	 * @param idSmp
	 * @param tpStatusSmp
	 */
	public void storeStatusSmp(Long idSmp, DomainValue tpStatusSmp) {
		SolicMonitPreventivo smp =  solicMonitPreventivoService.findById(idSmp);
		smp.setTpStatusSmp(tpStatusSmp);
		smp.setDsRetornoGR(null);
		solicMonitPreventivoService.store(smp);
	}
	
	
	/**
	 * cria mensagem padrão de erro quando ocorre problema 
	 * antes de colocar na fila de mensagens
	 * @param e
	 * @return
	 */
	private RetornoSmpDto buildRetornoException(String excepMessa) {
		RetornoSmpDto  retornoSmpDto = new RetornoSmpDto();
		retornoSmpDto.setTransacaoOk(Boolean.FALSE);
		List<MensagemDto> mensagem = new ArrayList<MensagemDto>();
		MensagemDto mensa = new MensagemDto();
		mensa.setCodigo(Integer.valueOf(ConstantesGerRisco.CODIGO_ERRO_LMS));
		mensa.setMensagem(excepMessa);
		mensagem.add(mensa);
		retornoSmpDto.setListMensagem(mensagem);
		return retornoSmpDto;
	}
	
	/**
	 * faz buscas e popula a smpDto para fazer a integração
	 * com a Apisul
	 * @param idControleCarga
	 * @param idSmp
	 * @return
	 */
	public Serializable popularDadosEnvioSMP(Long idControleCarga, Long idSmp) throws Exception{
		ControleCarga controleCarga = this.controleCargaService.findById(idControleCarga);
		List<MeioTransporteDto> meioTransportList = popularListaMeioTransporte(controleCarga);

		if (null != meioTransportList && !meioTransportList.isEmpty()) {
		SmpDto smp = new SmpDto();
		ParametroGeral cnpj = parametroGeralService.findByNomeParametro(ConstantesGerRisco.SGR_CNPJ_MATRIZ);
		if(cnpj!=null){
			smp.setCnpjMatriz(cnpj.getDsConteudo());
		}
		if(controleCarga.getTpControleCarga().getValue().equals(ConstantesGerRisco.VIAGEM) && isRotaExpressa(controleCarga)){
			smp.setTpControleCarga(ConstantesGerRisco.VIAGEM_EXPRESSA);
		}else{
			smp.setTpControleCarga(controleCarga.getTpControleCarga().getValue());
		}
		
		smp.setNrControleCarga(controleCarga.getFilialByIdFilialOrigem().getSgFilial()+"-"+controleCarga.getNrControleCarga());
		//popula meio transporte

		smp.setListMeioTransporte(meioTransportList);
		//popula motorista
		MotoristaDto motoristaDto = popularMotoristaEnvioSMP(controleCarga.getMotorista());
		smp.setMotorista(motoristaDto);
		//popula pontos de acordo com o tipo de viagem coleta/entrega ou viagem
		List<SmpPontoDto> listSmpPonto = popularListaPontoEnvioSMP(controleCarga);
		smp.setListSmpPonto(listSmpPonto);
		smp.setIdSmp(idSmp);
		smp.setDhInicioViagem(buscaDhInicio(controleCarga));
		
		smp.setListRastreador(popularListaRastreadoresVirusCarga(controleCarga));
		
		return smp;
		}else{
			return null;
	}
	}

	private boolean isRotaExpressa(ControleCarga controleCarga) {
		if( controleCarga.getRotaIdaVolta()!=null && controleCarga.getRotaIdaVolta().getRotaViagem()!=null && controleCarga.getRotaIdaVolta().getRotaViagem().getTpRota()!=null){
			return ConstantesGerRisco.EXPRESSA.equals(controleCarga.getRotaIdaVolta().getRotaViagem().getTpRota().getValue().toString());
		}else{
			return false;
		}
		
	}
	
	/**
	 * 
	 * busca a data de inicio de acordo com o tipo de carga 
	 * @param controleCarga
	 * @return
	 */
	private DateTime buscaDhInicio(ControleCarga controleCarga) {
		DateTime dataSaida = new DateTime();
		if (controleCarga.getTpControleCarga().getValue().equals(ConstantesGerRisco.VIAGEM)) {
			return buscaDhInicioViagem(controleCarga);
		} else if (controleCarga.getTpControleCarga().getValue().equals(ConstantesGerRisco.COLETA)
				|| controleCarga.getTpControleCarga().getValue().equals(ConstantesGerRisco.ENTREGA)) {
			return buscaDhInicioColetaEntrega(controleCarga);
		}
		return dataSaida;
	}
	
	  
	/**
	 * 
	 * utiliza parametro do sistema para calcular a data de inicio da Coleta Entrega
	 * @param controleCarga
	 * @return
	 */
	private DateTime buscaDhInicioColetaEntrega(ControleCarga controleCarga) {
		DateTime dataAtual = new DateTime();
		DateTime dataSaida = new DateTime();
		ParametroGeral acrecentaMinutos = parametroGeralService
				.findByNomeParametro(ConstantesGerRisco.SGR_MIN_ACRESC_DATA_S_SMP_4);
		Integer acrescMinutos = 0;
		if (acrecentaMinutos != null) {
			acrescMinutos = Integer.valueOf(acrecentaMinutos.getDsConteudo());
			dataSaida = dataAtual.plusMinutes(acrescMinutos);
		}
		return dataSaida;
	}
	  
	
	
	/**
	 * utiliza parametro do sistema para calcular a data de inicio da Viagem
	 * @param controleCarga
	 * @return
	 */
     private DateTime buscaDhInicioViagem(ControleCarga controleCarga) {
    	 DateTime dataAtual = new DateTime();
    	 DateTime dataSaida = new DateTime();
		if (isRotaExpressa(controleCarga)) {
			if (JTDateTimeUtils.comparaData(controleCarga.getDhPrevisaoSaida(), dataAtual) > 0) {
				dataSaida = controleCarga.getDhPrevisaoSaida();
			} else {
				int difMinutos = JTDateTimeUtils.getIntervalInMinutes(dataAtual,
						controleCarga.getDhPrevisaoSaida());

				ParametroGeral diferenca = parametroGeralService.findByNomeParametro(ConstantesGerRisco.SGR_DIFERENCA_PHR_SMP);
				Integer diferencaParametro = 0;
				if (diferenca != null) {
					diferencaParametro = Integer.valueOf(diferenca.getDsConteudo());
				}
				if (difMinutos <= diferencaParametro) {
					ParametroGeral acrecentaMinutos = parametroGeralService
							.findByNomeParametro(ConstantesGerRisco.SGR_MIN_ACRESC_DATA_S_SMP);
					Integer acrescMinutos = 0;
					if (acrecentaMinutos != null) {
						acrescMinutos = Integer.valueOf(acrecentaMinutos.getDsConteudo());
					}
					dataSaida = dataAtual.plusMinutes(acrescMinutos);
				} else {
					ParametroGeral acrecentaMinutos = parametroGeralService
							.findByNomeParametro(ConstantesGerRisco.SGR_MIN_ACRESC_DATA_S_SMP_2);
					Integer acrescMinutos = 0;
					if (acrecentaMinutos != null) {
						acrescMinutos = Integer.valueOf(acrecentaMinutos.getDsConteudo());
					}
					dataSaida = dataAtual.plusMinutes(acrescMinutos);
				}
			}
		} else {
			ParametroGeral acrecentaMinutos = parametroGeralService
					.findByNomeParametro(ConstantesGerRisco.SGR_MIN_ACRESC_DATA_S_SMP_3);
			Integer acrescMinutos = 0;
			if (acrecentaMinutos != null) {
				acrescMinutos = Integer.valueOf(acrecentaMinutos.getDsConteudo());
			}
			dataSaida = dataAtual.plusMinutes(acrescMinutos);
		}
    	 return dataSaida;
	}
	
	/**
	 * é ultilizado por popula smpDto para pegar os pontos 
	 * @param controleCarga
	 * @return
	 */
	private List<SmpPontoDto> popularListaPontoEnvioSMP(ControleCarga controleCarga) {
		List<SmpPontoDto> smpPontos = new ArrayList<SmpPontoDto>();
		if(controleCarga.getTpControleCarga().getValue().equals(ConstantesGerRisco.VIAGEM)){
			smpPontos.addAll(popularViagemListaPontoEnvioSMP(controleCarga));
		}else if(controleCarga.getTpControleCarga().getValue().equals(ConstantesGerRisco.COLETA) || controleCarga.getTpControleCarga().getValue().equals(ConstantesGerRisco.ENTREGA)) {
			smpPontos.addAll(popularColetaEntregaListaPontoEnvioSMP(controleCarga));
		}
		return smpPontos;
	}
	
	/**
	 * é ultilizado por popula smpDto para pegar coleta e entrega 
	 * @param controleCarga
	 * @return
	 */
	private List<SmpPontoDto> popularColetaEntregaListaPontoEnvioSMP(ControleCarga controleCarga) {
		List<SmpPontoDto> smpPontos = new ArrayList<SmpPontoDto>();
		
		smpPontos.addAll(populaPontoPorFilial(controleCarga.getFilialByIdFilialOrigem().getIdFilial(), ConstantesGerRisco.COLETA));
        smpPontos.addAll(populaPontoPorFilialColetaEntrega(controleCarga));
        smpPontos.addAll(populaPontoPorFilial(controleCarga.getFilialByIdFilialDestino().getIdFilial(), ConstantesGerRisco.ENTREGA));
		
		return smpPontos;
	}
	
	/**
	 * é ultilizado por popula smpDto para pegar os pontos de Coleta e entrega 
	 * @param controleCarga
	 * @return
	 */
	private List<SmpPontoDto> populaPontoPorFilialColetaEntrega(ControleCarga controleCarga) {
		List<SmpPontoDto> smpPontos = new ArrayList<SmpPontoDto>();
		List<Object[]> trechos = controleCargaService.findTrechosDestinoColetaEntregaCargas(controleCarga.getIdControleCarga());
		for (Object[] trecho : trechos) {
			SmpPontoDto ponto = populaPonto(trecho);
			ponto.setTpOperacao((String)trecho[9]);
			ponto.setPrevisaoChegada(buscarDataPrevisaoChegadaColetaEntrega());
			ponto.setTempoPermanencia(obterTempoPermanencia(ConstantesGerRisco.SGR_SMP_TEMPO_PERMANENCIA_COL_ENT));
			PontoCargaDto pontoCarga = populaCarga(trecho, 10);
			List<PontoCargaDto> pontoCargaDto = new ArrayList<PontoCargaDto>();
			pontoCargaDto.add(pontoCarga);
			ponto.setListPontoCarga(pontoCargaDto);
			smpPontos.add(ponto);
		}
		return smpPontos;
		
	}
	
	/**
	 * é ultilizado por popula smpDto para pegar os pontos filial de Coleta e entrega 
	 * @param controleCarga
	 * @return
	 */
	private List<SmpPontoDto> populaPontoPorFilial(Long idFilial, String tpOperacao){
		List<SmpPontoDto> smpPontos = new ArrayList<SmpPontoDto>();
		List<Object[]> trechos = controleCargaService.findTrechosDestinoColetaEntrega(idFilial);
		for (Object[] trecho : trechos) {
			SmpPontoDto filial = populaPonto(trecho);
			filial.setTpOperacao(tpOperacao);
			filial.setTempoPermanencia(obterTempoPermanencia(ConstantesGerRisco.SGR_SMP_TEMPO_PERMANENCIA_COL_ENT));
			filial.setPrevisaoChegada(buscarDataPrevisaoChegadaColetaEntrega());
			String NmFantasia = obterNmFantasia(trecho, filial);
			filial.setNmPessoa(NmFantasia);
			smpPontos.add(filial);
		}
		return smpPontos;
	}

	/**
	 * adiciona o sg da filial ao nome para diferenciar a filial
	 * @param trecho
	 * @param filial
	 * @return
	 */
	private String obterNmFantasia(Object[] trecho, SmpPontoDto filial) {
		int ult = trecho.length-1;
		String sgFilial = (String)trecho[ult];
		String NmFantasia = sgFilial+" - "+ filial.getNmPessoa();
		return NmFantasia;
	}
	
	
	/**
	 * obtem o tempo de permanencia pelo nome do parametro 
	 * é usado por coleta entrega e também por viagem só muda a passagem de parametro
	 */
	private Integer obterTempoPermanencia(String parametro){
		ParametroGeral permanencia = parametroGeralService
				.findByNomeParametro(parametro);
		Integer minutosPermanencia = 0;
		if (permanencia != null) {
			minutosPermanencia = Integer.valueOf(permanencia.getDsConteudo());
		}
		return minutosPermanencia;
	}
	
	
	/**
	 * data de previsao de chegada para coleta e entrega
	 * @return
	 */
	private DateTime buscarDataPrevisaoChegadaColetaEntrega() {
		DateTime atual = JTDateTimeUtils.getDataHoraAtual();
		DateTime doisDias = atual.plusDays(2);
		return JTDateTimeUtils.getFimDoDia(doisDias);
	}
	
	/**
	 * utilizado por popula smpDto para pegar os pontos de Viagem
	 * @param controleCarga
	 * @return
	 */
	private List<SmpPontoDto> popularViagemListaPontoEnvioSMP(ControleCarga controleCarga) {
		Filial filialUsuario = SessionUtils.getFilialSessao();
		List<SmpPontoDto> smpPontos = new ArrayList<SmpPontoDto>();
		List<Object[]> trechos = controleCargaService.findTrechosOrigemDestino(controleCarga.getIdControleCarga(), filialUsuario.getIdFilial());
		int x = 0;
		for (Object[] trecho : trechos) {
			SmpPontoDto ponto = populaPonto(trecho);
			ponto.setTpOperacao(ConstantesGerRisco.COLETA);
			ponto.setTempoPermanencia(obterTempoPermanencia(ConstantesGerRisco.SGR_SMP_TEMPO_PERMANENCIA_VIAGEM));
			Long idFilial = (Long)trecho[9];
			String sgFilial = (String)trecho[10];
			ponto.setNmPessoa(sgFilial + " - " + ponto.getNmPessoa());
			List<Object[]> cargas = controleCargaService.findCargaTrechosDestino(controleCarga.getIdControleCarga(), Long.valueOf(idFilial));
			List<PontoCargaDto> pontoCargas = new ArrayList<PontoCargaDto>();
			
			for (Object[] carga : cargas) {
				PontoCargaDto pontoCarga = populaCarga(carga, 0);
				pontoCargas.add(pontoCarga);
				ponto.setTpOperacao(ConstantesGerRisco.ENTREGA);
				
			}
			
			if (x != 0) {
				ponto.setPrevisaoChegada(buscarDataPrevisaoChegada(controleCarga, idFilial));
			}
			ponto.setListPontoCarga(pontoCargas);
			smpPontos.add(ponto);
			x++;
		}
		return smpPontos;
	}
	
	/**
	 * se for viagem expressa usa o previsão total da viagem
	 * senao calcula por trecho usando parametros do sistema
	 * @param controleCarga
	 * @return
	 */
	private DateTime buscarDataPrevisaoChegada(ControleCarga controleCarga, Long idFilial) {
		DateTime dataViagem = new DateTime();
		if (isRotaExpressa(controleCarga)) {
			List<ControleTrecho> controleTrechos = controleCarga.getControleTrechos();
			for (ControleTrecho controleTrecho : controleTrechos) {
				if (controleTrecho.getFilialByIdFilialDestino().getIdFilial()
						.equals(controleCarga.getFilialByIdFilialDestino().getIdFilial())
						&& controleTrecho.getFilialByIdFilialOrigem().getIdFilial()
								.equals(controleCarga.getFilialByIdFilialOrigem().getIdFilial())) {
					return controleTrecho.getDhPrevisaoChegada();
				}
			}
		} else {
			FluxoFilial ultimo = fluxoFilialService.findUltimoFluxoFilial(SessionUtils.getFilialSessao().getIdFilial(), idFilial, null);
			Integer distancia = ultimo.getNrDistancia();
			String categoria = controleCarga.getMeioTransporteByIdTransportado().getModeloMeioTransporte()
					.getTipoMeioTransporte().getTpCategoria().getValue();
			if (categoria == null) {
				categoria = controleCarga.getMeioTransporteByIdSemiRebocado().getModeloMeioTransporte()
						.getTipoMeioTransporte().getTpCategoria().getValue();
			}
			Integer velocidadeMedia = BuscaVelocidadeMediaPorParametro(categoria);
			if (distancia != null && velocidadeMedia > 0) {
				dataViagem = calculaPrevisaoChegada(distancia, velocidadeMedia, buscaDhInicioViagem(controleCarga));
			}
		}
		return dataViagem;
	}
	
	/**
	 * utilizado por buscarDataPrevisaoChegada para calcular a data usando velocidade média X distancia
	 * @param distancia
	 * @param velocidadeMedia
	 * @return
	 */
	private DateTime calculaPrevisaoChegada(Integer distancia, Integer velocidadeMedia, DateTime dateTime ) {
		Double tempo = (distancia.doubleValue() / velocidadeMedia.doubleValue());
		int minutos = (int) Math.round(tempo * 60);
		dateTime = dateTime.plusMinutes(minutos);
		return dateTime;
	}
	
	/**
	 * busca velocidade por parametro
	 * @param categoria
	 * @return
	 */
	private Integer BuscaVelocidadeMediaPorParametro(String categoria) {
		Integer velocidadeMedia = 0;
		String parametroCategoria = ConstantesGerRisco.VEL_MT_MEDIO;
		if(categoria.equals(ConstantesGerRisco.VEL_MEDIA)){
			parametroCategoria = ConstantesGerRisco.VEL_MT_MEDIO;
		}else if(categoria.equals(ConstantesGerRisco.VEL_PESADO)){
			parametroCategoria = ConstantesGerRisco.VEL_MT_PESADO;
		}else if(categoria.equals(ConstantesGerRisco.VEL_LEVE)){
			parametroCategoria = ConstantesGerRisco.VEL_MT_LEVE;
		}else if(categoria.equals(ConstantesGerRisco.VEL_LEVE_LEVE)){
			parametroCategoria = ConstantesGerRisco.VEL_MT_LEVE_LEVE;
		}else if(categoria.equals(ConstantesGerRisco.VEL_LEVE_MEDIO)){
			parametroCategoria = ConstantesGerRisco.VEL_MT_LEVE_MEDIO;
		}
		
		ParametroGeral parametro = parametroGeralService.findByNomeParametro(parametroCategoria);
		if(parametro!=null){
			velocidadeMedia = Integer.valueOf(parametro.getDsConteudo());
		}
		return velocidadeMedia;
	}
	
	/**
	 * utilizado por popula smpDto para pegar a carga do ponto
	 * @param carga
	 * @param i
	 * @return
	 */
	private PontoCargaDto populaCarga(Object[] carga, int i) {
		PontoCargaDto pontoCarga = new PontoCargaDto();
		pontoCarga.setCnpjRemetente((String)carga[i++]);
		
		BigDecimal vlCarga = (BigDecimal)carga[i++];
		if (vlCarga == null || BigDecimal.ZERO.equals(vlCarga)){
		    vlCarga = DEFAULT_VL_CARGA;
		}
		pontoCarga.setVlCarga(vlCarga);
		return pontoCarga;
	}
	
	/**
	 * utilizado por popula smpDto pegar o ponto
	 * @param objects
	 * @return
	 */
	private SmpPontoDto populaPonto(Object[] objects) {
		int i = 0;
		SmpPontoDto smpPonto = new SmpPontoDto();
		smpPonto.setNmPessoa((String)objects[i++]);
		smpPonto.setDsEndereco((String)objects[i++]);
		smpPonto.setNmMunicipio((String)objects[i++]);
		smpPonto.setNrCep((String)objects[i++]);
		smpPonto.setNrIbge((Integer)objects[i++]);
		smpPonto.setIdEnderecoPessoa((Long)objects[i++]);
		smpPonto.setSgPais((String)objects[i++]);
		smpPonto.setSgUnidadeFederativa((String)objects[i++]);
		smpPonto.setNrIdentificacao((String)objects[i++]);
		return smpPonto;
	}
	
	/**
	 * utilizado por popula smpDto para pegar meio de transporte
	 * @param controleCarga
	 * @return
	 */
	private List<MeioTransporteDto> popularListaMeioTransporte(ControleCarga controleCarga) {
		List<MeioTransporte> meioTRansportes = montarListaMeioTransporte(controleCarga);
		List<MeioTransporteDto> meioTransporteDtos = new ArrayList<MeioTransporteDto>();
		for (MeioTransporte meioTransporte : meioTRansportes) {
			MeioTransporteDto meioTransporteDto = new MeioTransporteDto();
			if (meioTransporte.getModeloMeioTransporte() != null
					&& meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte() != null) {
				meioTransporteDto.setDsTipoMeioTransporte(meioTransporte.getModeloMeioTransporte()
						.getTipoMeioTransporte().getDsTipoMeioTransporte());
			}
			meioTransporteDto.setNrIdentificador(meioTransporte.getNrIdentificador());
			if (meioTransporte.getTpVinculo() != null) {
				meioTransporteDto.setTpVinculo(meioTransporte.getTpVinculo().getValue());
			}
			
			List<RastreadorDto> listRastreador = populaRastreador(meioTransporte);
			meioTransporteDto.setListRastreador(listRastreador);
			meioTransporteDtos.add(meioTransporteDto);
		}
		return meioTransporteDtos;
	}
	
	
	private List<RastreadorDto> popularListaRastreadoresVirusCarga(ControleCarga controleCarga) {
        List<RastreadorDto> rastreadoresVirusCarga = new ArrayList<RastreadorDto>(); 
        TypedFlatMap criteriaVirusCarga = new TypedFlatMap();
        criteriaVirusCarga.put("idControleCarga", controleCarga.getIdControleCarga());
        ParametroGeral parametroCNPJ = parametroGeralService.findByNomeParametro(ConstantesGerRisco.SGR_CNPJ_MATRIZ);
        ParametroGeral parametroFabricanteIscas = parametroGeralService.findByNomeParametro(ConstantesGerRisco.SGR_CD_FABRICANTE_ISCAS);
        List<VirusCarga> virusCargas = virusCargaService.find(criteriaVirusCarga);
        
        for (VirusCarga vc : virusCargas){
            RastreadorDto dto = new RastreadorDto();
            dto.setNrRastreador(vc.getNrIscaCarga());
            
            if(parametroCNPJ!=null){
                dto.setNrIdentificacaoEmitente(parametroCNPJ.getDsConteudo());
            }
            
            if (parametroFabricanteIscas != null){
                dto.setCdFabricante(Integer.parseInt(parametroFabricanteIscas.getDsConteudo()));
            }
            
            dto.setTipoComunicacao(ConstantesGerRisco.SGR_TP_COMUNICACAO_CELULAR);

            rastreadoresVirusCarga.add(dto);
        }
        
        return rastreadoresVirusCarga;
    }
	
	/**
	 * utilizado por popula smpDto para pegar o rastreador
	 * @param meioTransporte
	 * @return
	 */
	private List<RastreadorDto> populaRastreador(MeioTransporte meioTransporte) {
		List<RastreadorDto> listRastreador = new ArrayList<RastreadorDto>();
		if(meioTransporte.getMeioTransporteRodoviario()!=null && BooleanUtils.isTrue(meioTransporte.getMeioTransporteRodoviario().getBlMonitorado())){
		RastreadorDto rastreador = new RastreadorDto();
			rastreador.setNrRastreador(String.valueOf(meioTransporte.getMeioTransporteRodoviario().getNrRastreador()));
			rastreador.setCdFabricante(meioTransporte.getMeioTransporteRodoviario().getOperadoraMct().getCdFabricante());
			ParametroGeral parametroCNPJ = parametroGeralService.findByNomeParametro(ConstantesGerRisco.SGR_CNPJ_MATRIZ);
			if(parametroCNPJ!=null){
				rastreador.setNrIdentificacaoEmitente(parametroCNPJ.getDsConteudo());
			}
			rastreador.setTipoComunicacao(ConstantesGerRisco.SGR_TP_COMUNICACAO_SATELITAL);
			listRastreador.add(rastreador);
		}
		return listRastreador;
	}

	/**
	 * utilizado por popula smpDto para pegar o caminhao
	 * @param controleCarga
	 * @return
	 */
	private List<MeioTransporte> montarListaMeioTransporte(ControleCarga controleCarga) {
		List<MeioTransporte> listMeioTransporte = new ArrayList<MeioTransporte>();
		Boolean isVeiculoPrincipalRastreado = false; 
		if (controleCarga.getMeioTransporteByIdTransportado() != null) {
			isVeiculoPrincipalRastreado = validateMeioTransporteNrRastreador(controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte());
			if(isVeiculoPrincipalRastreado) {
			listMeioTransporte.add(controleCarga.getMeioTransporteByIdTransportado());
				if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
			listMeioTransporte.add(controleCarga.getMeioTransporteByIdSemiRebocado());
		}
			}
		}
		return listMeioTransporte;
	}
	
	private Boolean validateMeioTransporteNrRastreador(Long idMeioTransporte){
	    MeioTransporteRodoviario mt = meioTransporteRodoviarioService.findById(idMeioTransporte);
	    if (mt != null && mt.getNrRastreador() != null && BooleanUtils.isTrue(mt.getBlMonitorado())){
	        return true;
	    }
	    return false;
	}
	
	/**
	 * 
	 * @param idControleCarga
	 * @return
	 */
	private boolean isMonitoradoIdTransportado(Long idControleCarga) {
		return controleCargaService.isMonitoradoIdTransportado(idControleCarga);
	}
	
	/**
	 * 
	 * @param idControleCarga
	 * @return
	 */
	private boolean isMonitoradoSemiRebocado(Long idControleCarga) {
		return controleCargaService.isMonitoradoSemiRebocado(idControleCarga);
	}
	
	/**
	 * utilizado por popula smpDto para pegar motoristas
	 * @param motorista
	 * @return
	 */
	private MotoristaDto popularMotoristaEnvioSMP(Motorista motorista) {
		MotoristaDto motoristaDto = new MotoristaDto();
		if (motorista.getPessoa() != null) {
			motoristaDto.setNome(motorista.getPessoa().getNmPessoa());
			motoristaDto.setNrIdentificacao(motorista.getPessoa().getNrIdentificacao());
			motoristaDto.setTpDocumento(motorista.getPessoa().getTpIdentificacao().getValue());
		}
		
		if (motorista.getTpVinculo() != null) {
			motoristaDto.setTpVinculo(motorista.getTpVinculo().getValue());
		}
		return motoristaDto;
	}
	
	
	/**
	 * Cria faixa de valores na smp de acordo com as regras do plano de sgr
	 *  
	 * @param planoPGR
	 * @param solicMonitPreventivo
	 */
	private void storeFaixaDeValorSmp(PlanoGerenciamentoRiscoDTO planoPGR,
			SolicMonitPreventivo solicMonitPreventivo) {
		
		if (planoPGR != null && planoPGR.getEnquadramentos() != null) {
			List<FaixaDeValorSmp> faixaDeValorSmps = new ArrayList<FaixaDeValorSmp>();
			List<EnquadramentoRegraDTO> enquadramentos = new ArrayList<EnquadramentoRegraDTO>(
					planoPGR.getEnquadramentos());
			for (EnquadramentoRegraDTO enquadramentoRegraDTO : enquadramentos) {
				List<ExigenciaFaixaValor> efv = enquadramentoRegraDTO.getFaixaDeValor().getExigenciaFaixaValors();
				for (ExigenciaFaixaValor exigenciaFaixaValor : efv) {
					FaixaDeValorSmp faixaDeValorSmp = new FaixaDeValorSmp();
					faixaDeValorSmp.setExigenciaGerRisco(exigenciaFaixaValor.getExigenciaGerRisco());
					faixaDeValorSmp.setFaixaDeValor(exigenciaFaixaValor.getFaixaDeValor());
					faixaDeValorSmp.setFilialInicio(exigenciaFaixaValor.getFilialInicio());
					faixaDeValorSmp.setQtExigida(exigenciaFaixaValor.getQtExigida());
					faixaDeValorSmp.setVlKmFranquia(exigenciaFaixaValor.getVlKmFranquia());
					faixaDeValorSmp.setSolicMonitPreventivo(solicMonitPreventivo);
					faixaDeValorSmps.add(faixaDeValorSmp);
				}
			}
			if (!faixaDeValorSmps.isEmpty()) {
				faixaDeValorSmpService.storeAll(faixaDeValorSmps);
			}
		}
		
	}
	
	/**
	 * Método referente ao processo 11.01.01.04 <br>
	 * Alterar SMP
	 * @return idSolicMonitPreventivo gerado
	 * @author luisfco
	 */
	public void generateAlterarSMP(Long idSolicMonitPreventivo) {
		SolicMonitPreventivo solicMonitPreventivo = solicMonitPreventivoService.findById(idSolicMonitPreventivo);
		ControleCarga controleCarga = controleCargaService.findById(solicMonitPreventivo.getControleCarga().getIdControleCarga());
		solicMonitPreventivo.setMeioTransporteByIdMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
		solicMonitPreventivo.setMeioTransporteByIdMeioSemiReboque(controleCarga.getMeioTransporteByIdSemiRebocado());
		solicMonitPreventivo.setMotorista(controleCarga.getMotorista());
		solicMonitPreventivoService.store(solicMonitPreventivo);
	}
	
	private void verifyControleCarga(ControleCarga controleCarga) {
		if (controleCarga.getMotorista()==null)
			throw new BusinessException("LMS-11010", new Object[]{controleCarga.getFilialByIdFilialOrigem().getSgFilial()+"-"+new DecimalFormat("00000000").format(controleCarga.getNrControleCarga())});
	}
	
	/**
	 * Persiste as exigencias de SMP, tantas quantos forem os registros retornados em ExigenciasGerRisco
	 * @param exigenciasGerRisco
	 * @param solicMonitPreventivo
	 */
	private void storeExigenciasSMP(PlanoGerenciamentoRiscoDTO planoPGR, SolicMonitPreventivo solicMonitPreventivo) {
		if (planoPGR != null && planoPGR.getExigencias() != null) {
			List<ExigenciaGerRiscoDTO> exigencias = (List<ExigenciaGerRiscoDTO>) planoPGR.getExigencias();
			List<ExigenciaSmp> exigenciasSMP = new ArrayList<ExigenciaSmp>();
			for (ExigenciaGerRiscoDTO exigencia : exigencias) {
				ExigenciaSmp exigenciaSMP = new ExigenciaSmp();
				exigenciaSMP.setExigenciaGerRisco(exigencia.getExigenciaGerRisco());
				exigenciaSMP.setSolicMonitPreventivo(solicMonitPreventivo);
				exigenciaSMP.setQtExigida(exigencia.getQtExigida());
				exigenciaSMP.setQtExigidaOriginal(exigencia.getQtExigida());
				exigenciaSMP.setFilialInicio(exigencia.getFilialInicio());
				exigenciaSMP.setFilialInicioOriginal(exigencia.getFilialInicio());
				exigenciaSMP.setVlKmFranquia(exigencia.getVlKmFranquia());
				exigenciaSMP.setVlKmFranquiaOriginal(exigencia.getVlKmFranquia());
				DomainValue tpManutRegistroSmp = domainValueService.findDomainValueByValue("DM_TIPO_MANUTENCAO_REGISTRO","P");
				exigenciaSMP.setTpManutRegistro(tpManutRegistroSmp);
				exigenciasSMP.add(exigenciaSMP);
			}
			if (!exigenciasSMP.isEmpty()) {
				exigenciaSmpService.storeAll(exigenciasSMP);
			}
		}
	}
	
	private void storeManifestosSMP(SolicMonitPreventivo solicMonitPreventivo, Long idControleCarga) {
		List list = new ArrayList();
		List manifestos = manifestoService.findManifestosCCorEFByIdControleCarga(idControleCarga);
		for (Iterator it = manifestos.iterator(); it.hasNext(); ) {
			Manifesto manifesto = (Manifesto)it.next();
			SmpManifesto smpManifesto = new SmpManifesto();
			smpManifesto.setManifesto(manifesto);
			smpManifesto.setSolicMonitPreventivo(solicMonitPreventivo);
			list.add(smpManifesto);
		}
		smpManifestoService.storeAll(list);
	}
	
	/**
	 * Persiste a SMP
	 * @param controleCarga
	 * @param filialUsuario
	 * @return
	 */
	private SolicMonitPreventivo storeSolicMonitPreventivo(ControleCarga controleCarga, Filial filialUsuario) {
		SolicMonitPreventivo solicMonitPreventivo = solicMonitPreventivoService.findByIdControleCargaAndFilial(controleCarga.getIdControleCarga(), filialUsuario.getIdFilial());

		// se já existe a SMP, atualiza a mesma, removendo os filhos ExigenciaSMP e SMPManifesto 
		if (solicMonitPreventivo!=null) {
			Long idSolicMonitPreventivo = solicMonitPreventivo.getIdSolicMonitPreventivo();
			exigenciaSmpService.removeByIdSolicMonitPreventivo(idSolicMonitPreventivo);
			smpManifestoService.removeByIdSolicMonitPreventivo(idSolicMonitPreventivo);
			faixaDeValorSmpService.removeByIdSolicMonitPreventivo(idSolicMonitPreventivo);

		// senão, cria a SMP
		} else {
	        Long nrSmp = configuracoesFacade.incrementaParametroSequencial(filialUsuario.getIdFilial(), "NR_SMP", true);
			solicMonitPreventivo = new SolicMonitPreventivo();
			solicMonitPreventivo.setFilial(filialUsuario);
			solicMonitPreventivo.setControleCarga(controleCarga);
			solicMonitPreventivo.setNrSmp( Integer.valueOf(nrSmp.intValue()) );
			solicMonitPreventivo.setTpStatusSmpGR(new DomainValue(ConstantesGerRisco.TP_STATUS_SMP_GR_NAO_ENVIADA));
		}

		// obtém o id da gerenciadora de risco na tabela de parâmetros gerais
		Long idGerenciadoraRisco = Long.valueOf(((BigDecimal)parametroGeralService.findConteudoByNomeParametro("ID_GERENCIADORA_RISCO_MERCURIO", false)).longValue());
		GerenciadoraRisco gerenciadoraRisco = this.gerenciadoraRiscoService.findById(idGerenciadoraRisco);
			
		ControleTrecho controleTrecho = null;
		
		if (ConstantesGerRisco.VIAGEM.equals(controleCarga.getTpControleCarga().getValue())) {
			// obtém o controle de trecho com a menor previsão de chegada,
			// disparando exceção se não encontrar
			controleTrecho = controleTrechoService.findControleTrechoByBlTrechoDireto(controleCarga.getIdControleCarga(),
					filialUsuario.getIdFilial(), Boolean.TRUE);

			if (controleTrecho == null) {
				String[] descricaoControleCarga = new String[] { controleCarga.getFilialByIdFilialOrigem().getSgFilial()
						+ "-" + new DecimalFormat("00000000").format(controleCarga.getNrControleCarga().longValue()) };
				throw new BusinessException("LMS-11005", descricaoControleCarga);
			}
		}

		solicMonitPreventivo.setControleTrecho(controleTrecho);
		solicMonitPreventivo.setMotorista(controleCarga.getMotorista());		
		solicMonitPreventivo.setDhGeracao(new DateTime(filialUsuario.getDateTimeZone()));
		solicMonitPreventivo.setMeioTransporteByIdMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
		solicMonitPreventivo.setMeioTransporteByIdMeioSemiReboque(controleCarga.getMeioTransporteByIdSemiRebocado());
		solicMonitPreventivo.setTpStatusSmp(new DomainValue("GE"));
		solicMonitPreventivo.setGerenciadoraRisco(gerenciadoraRisco);
		solicMonitPreventivo.setMoedaPais(moedaPaisService.findMoedaPaisUsuarioLogado());
		solicMonitPreventivo.setVlSmp(controleCargaService.generateCalculaValorTotalMercadoriaControleCarga(
				controleCarga.getIdControleCarga()));
	
		solicMonitPreventivoService.store(solicMonitPreventivo);		
		return solicMonitPreventivo;
	}
	public FaixaDeValorSmpService getFaixaDeValorSmpService() {
		return faixaDeValorSmpService;
	}
	public void setFaixaDeValorSmpService(FaixaDeValorSmpService faixaDeValorSmpService) {
		this.faixaDeValorSmpService = faixaDeValorSmpService;
	}
	public DomainValueService getDomainValueService() {
		return domainValueService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	
	public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
		this.fluxoFilialService = fluxoFilialService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

    public void setMeioTransporteRodoviarioService(MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
        this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
    }

    public void setVirusCargaService(VirusCargaService virusCargaService) {
        this.virusCargaService = virusCargaService;
    }
}
