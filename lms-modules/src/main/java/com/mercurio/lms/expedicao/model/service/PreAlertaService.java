package com.mercurio.lms.expedicao.model.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.MultiReportCommand;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.ManifestoColetaService;
import com.mercurio.lms.coleta.model.service.OcorrenciaColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.PreAlerta;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.dao.PreAlertaDAO;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.portaria.model.service.InformarSaidaService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.preAlertaService"
 */
public class PreAlertaService extends CrudService<PreAlerta, Long> {	
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	private static final String ID_MOTIVO_CANCELAMENTO_CC_AEREO = "ID_MOTIVO_CANCELAMENTO_CC_AEREO";
	private static final String MSG_CANCELADO_EMBARQUE_CIA_AEREA = "LMS-04528";
	private ConfiguracoesFacade configuracoesFacade;
	private CtoAwbService ctoAwbService;
	private IntegracaoJmsService integracaoJmsService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private EventoVolumeService eventoVolumeService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private AwbService awbService;
	private PedidoColetaService pedidoColetaService;
	private ConhecimentoService conhecimentoService;
	private ServicoService servicoService;
	private ContatoService contatoService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private ManifestoColetaService manifestoColetaService;
	private ControleCargaService controleCargaService;
	private ClienteService clienteService;
	private ReportExecutionManager reportExecutionManager;
	private OcorrenciaColetaService ocorrenciaColetaService;
	private ParametroGeralService parametroGeralService;
	private FilialService filialService;
	private DoctoServicoService doctoServicoService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private InformarSaidaService informarSaidaService;
	private AwbOcorrenciaService awbOcorrenciaService;
	private TrackingAwbService trackingAwbService;
	private EnderecoPessoaService enderecoPessoaService;

	private static final String LINE_SEPARATOR = VMProperties.LINE_SEPARATOR.getValue();
	
	/** Eventos */
	static final short CD_EMBARQUE_AEREO = 180;
	static final short CD_CANCELAMENTO_EMBARQUE_AEREO = 181;
	static final short CD_ENTREGA_REALIZADA_AEREO = 85;
	
	static final Short CD_PEDIDO_COLETA_EMITIDO = 400;
	
	/** Localização Mercadoria */
	static final short CD_AGUARDANDO_EMBARQUE_CIA_AEREA = 55;
	static final short CD_EM_VIAGEM_AEREA = 2;
	static final short CD_EM_ROTA_COLETA_AEROPORTO = 100;
	
	/** CÓDIGO EXCEÇÃO */
	static final String LMS_DOCTOS_NAO_ENTREGUE_AERO = "LMS-04503";
	static final String LMS_DOCTOS_NAO_EM_VIAGEM_AEREA = "LMS-04504";
	static final String LMS_DOCTOS_JA_COLETADOS = "LMS-04532";
	
	/** TIPO LOCALIZACAO AWB */
	static final String TP_LOCALIZACAO_AWB_EMBARCADO = "EV";
	static final String TP_LOCALIZACAO_AWB_AGUARDANDO_EMBARQUE = "AE";
	
	static final String TP_SCAN_LMS_MANUAL = "LM";

	private static final String TIPO_FRETE_CIF = "C";
	private static final String SERVICO_AEREO_NACIONAL_CONVENCIONAL = "ANC";
	private static final String TELEFONE_COMERCIAL = TIPO_FRETE_CIF;
	private static final String COLETA_TIPO_AEROPORTO = "AE";
	private static final String COLETA_MODO_TELEFONE = "TE";
	private static final String COLETA_EM_ABERTO = "AB";
	private static final String OCORRENCIA_COLETA_CANCELADA = "CA";

	/**
	 * @param ctoAwbService
	 */
	public void setCtoAwbService(CtoAwbService ctoAwbService) {
		this.ctoAwbService = ctoAwbService;
	}
	/**
	 * @param configuracoesFacade
	 */
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	protected PreAlerta beforeInsert(PreAlerta bean) {
		PreAlerta preAlerta = (PreAlerta)bean;
		preAlerta.setNrPreAlerta(Integer.valueOf(configuracoesFacade.incrementaParametroSequencial(
				 "NR_PRE_ALERTA"
				,true).toString()));
		return super.beforeInsert(bean);
	}

	/**
	 * @author Andre Valadas
	 * 
	 * @param preAlerta
	 * @param oldPreAlerta
	 * @param validaVooConfirmado Valida se confirmacao do voo foi alterada pra "S"
	 * @return 
	 */
	private boolean validateUpdate(PreAlerta preAlerta, PreAlerta oldPreAlerta, boolean validaVooConfirmado) {
		return !preAlerta.getDsVoo().equals(oldPreAlerta.getDsVoo()) ||
			   !preAlerta.getDhSaida().equals(oldPreAlerta.getDhSaida()) ||
			   !preAlerta.getDhChegada().equals(oldPreAlerta.getDhChegada()) ||
			   (validaVooConfirmado &&
					(Boolean.TRUE.equals(preAlerta.getBlVooConfirmado()) && 
			   		!preAlerta.getBlVooConfirmado().equals(oldPreAlerta.getBlVooConfirmado())));
	}

	/**
	 * Aplica o store e valida se deve eviar e-mail 
	 * @author Andre Valadas
	 * @param preAlerta
	 * @return TypedFlatMap
	 */
	public TypedFlatMap executeValidateInStore(PreAlerta preAlerta, Long idMeioTransporte, Long idMotorista, Long idRotaColetaEntrega) {

		Boolean blVooConfirmadoNew = preAlerta.getBlVooConfirmado();
		Boolean blVooConfirmadoOld = !preAlerta.getBlVooConfirmado();
		Boolean blIsFilialParceira = awbService.findResponsavelAwbEmpresaParceira(preAlerta.getAwb().getIdAwb()) != null;
		Boolean blIsNewPreAlerta = preAlerta.getIdPreAlerta() == null;
		
		Long idFilial =  SessionUtils.getFilialSessao().getIdFilial();
		if(blIsFilialParceira){
			Awb awb = awbService.findById(preAlerta.getAwb().getIdAwb());
			idFilial = awb.getFilialByIdFilialDestino().getIdFilial();
		}
		
		/** Valida Filiais */
		validaFiliais(preAlerta.getAwb().getIdAwb());
		/** Valida Datas */
		if (preAlerta.getDhSaida().compareTo(preAlerta.getDhChegada()) > 0) {
			throw new BusinessException("LMS-04193");
		}

		/** Nova entidade da sessao */
		PreAlerta sessionPreAlerta = preAlerta;
		TypedFlatMap toReturn = new TypedFlatMap();
		boolean blSendMail = false;
		if (!blIsNewPreAlerta) {
			/** Busca a referencia antiga do POJO(nova sessao) */
			sessionPreAlerta = findById(preAlerta.getIdPreAlerta());			
			blVooConfirmadoOld = sessionPreAlerta.getBlVooConfirmado();
			/** Se necessário gera eventos para documentos e volumes*/
			if (!(blIsFilialParceira && blVooConfirmadoOld && !blVooConfirmadoNew)){
				this.executeValidaEventosVooConfirmado(preAlerta, sessionPreAlerta, blVooConfirmadoOld, blVooConfirmadoNew, blIsFilialParceira, idFilial);
			}
			
			/**
			 * Envia e-mail caso:
			 * Os campos forem alterados;
			 * Se confirmacao do voo foi alterada pra "S"
			 */
			blSendMail = validateUpdate(preAlerta, sessionPreAlerta, true);
			/** Valida se campos foram alterados */
			if (validateUpdate(preAlerta, sessionPreAlerta, false)) {
				preAlerta.setUsuario(null);
				preAlerta.setDhRecebimentoMens(null);
				toReturn.put("cleanUser", Boolean.TRUE);
			}
			/** Copia as propriedades para a entidade da sessao */
			try {
				BeanUtils.copyProperties(sessionPreAlerta, preAlerta);
			} catch (Exception e) {
				throw new InfrastructureException(e);
			}
		} else blSendMail = true;

		/** Executa Store */
		this.store(sessionPreAlerta);

		if(blIsFilialParceira && !blIsNewPreAlerta){
			if (!blVooConfirmadoOld.equals(blVooConfirmadoNew)) {
				if(blVooConfirmadoNew){
					this.executePedidoColetaAeroportoParceira(preAlerta.getAwb().getIdAwb(), idMeioTransporte, idMotorista, idRotaColetaEntrega, toReturn);
				}else{
					List documentosServico = this.executeCancelarPedidoColetaAeroportoParceira(preAlerta.getAwb().getIdAwb(), idFilial);
					this.executeValidaEventosVooConfirmado(preAlerta, sessionPreAlerta, blVooConfirmadoOld, blVooConfirmadoNew, blIsFilialParceira, idFilial);
				}
			}
		}
		
		toReturn.put("blSendMail", blSendMail);
		
		return toReturn;
	}
	
	public void executeSendEmail(PreAlerta preAlerta, TypedFlatMap toReturn){
		executeSendEmail(preAlerta, toReturn, false);
	}
	
	public void executeSendEmail(PreAlerta preAlerta, TypedFlatMap toReturn, Boolean blIsFilialParceira) {
		
		List<File> attachments = new ArrayList<File>();
		
		if(toReturn.containsKey("idControleCarga")){
			attachments.add(this.generateControleCargaReport(toReturn.getLong("idControleCarga"), toReturn.getLong("idFilial")));
			attachments.add(this.generateManifestoColetaReport(toReturn.getLong("idRotaColetaEntrega"), toReturn.getLong("idManifestoColeta")));
		}
		
		/** Retorna se deve eviar e-mail + dados para Coleta */
		if (toReturn.getBoolean("blSendMail")) {
			toReturn.putAll(findAwbByPreAlerta(preAlerta.getIdPreAlerta()));

			/** 
			 * Envio de E-mails
			 * OBS: Essa rotida deve ser chamada somento após o store, 
			 *		pois o mesmo deve ser concluido caso o 
			 *		sendMailPreAlerta levantar alguma excecao.
			 */
			try {
				executeSendMailPreAlerta(preAlerta, attachments, blIsFilialParceira);
			} catch (Exception e) {
				if (e instanceof BusinessException) {
					toReturn.put("exception", configuracoesFacade.getMensagem(((BusinessException)e).getMessageKey()));
				} else {
					toReturn.put("exception", e.getMessage());
				}
			}
		}
	}

	private List executeCancelarPedidoColetaAeroportoParceira(Long idAwb, Long idFilial) {
		PedidoColeta pedidoColeta = pedidoColetaService.findPedidoColetaByAwb(idAwb);
		if(pedidoColeta != null){
			OcorrenciaColeta ocorrenciaColeta = this.findOcorrenciaColetaCancelamento();
			Long idControleCarga = pedidoColeta.getManifestoColeta().getControleCarga().getIdControleCarga();
			
			String dsCancelamento = configuracoesFacade.getMensagem(MSG_CANCELADO_EMBARQUE_CIA_AEREA);  
			
			Long idMotivoCancelamentoCC = Long.parseLong(parametroGeralService.findConteudoByNomeParametro(ID_MOTIVO_CANCELAMENTO_CC_AEREO, false).toString());
			
			List listaDoctosServico = doctoServicoService.findDoctosServicoByIdControleCargaColetaParceira(idControleCarga);
			
			pedidoColetaService.generateEventosColetaAeroByPedidoColeta(pedidoColeta, ConstantesSim.EVENTO_CANCELA_ROTA_COLETA_AERO, null, idFilial);
			
			controleCargaService.generateCancelamentoControleCarga(idControleCarga, idMotivoCancelamentoCC, dsCancelamento, true, idFilial);		
			pedidoColetaService.executeCancelarColeta(pedidoColeta.getIdPedidoColeta(), ocorrenciaColeta.getIdOcorrenciaColeta(), dsCancelamento, true);
			return listaDoctosServico;
			
		}
		return new ArrayList();
	}

	private void executeUpdateLocalizacaoMercadoria(List documentosServico) {
		for (Iterator iter = documentosServico.iterator(); iter.hasNext();) {
    		DoctoServico doctoServico = (DoctoServico)iter.next();
    		LocalizacaoMercadoria localizacao = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(Short.valueOf(CD_AGUARDANDO_EMBARQUE_CIA_AEREA));
    		doctoServico.setLocalizacaoMercadoria(localizacao);
    		doctoServicoService.store(doctoServico);
		}
	}
	private OcorrenciaColeta findOcorrenciaColetaCancelamento() {
		List<OcorrenciaColeta> list = ocorrenciaColetaService.findOcorrenciaColetaByTpEventoColeta(OCORRENCIA_COLETA_CANCELADA);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	private void executePedidoColetaAeroportoParceira(Long idAwb, Long idMeioTransporte, Long idMotorista, Long idRotaColetaEntrega, TypedFlatMap toReturn) {
		Awb awb = awbService.findById(idAwb);			
		
		ControleCarga controleCarga = controleCargaService.generateValidacaoControleCargaParceiraAutomatico(idMeioTransporte, idRotaColetaEntrega, idMotorista, awb.getFilialByIdFilialDestino().getIdFilial());
		
		ManifestoColeta manifestoColeta = new ManifestoColeta();
		manifestoColeta.setControleCarga(controleCarga);
		manifestoColeta.setRotaColetaEntrega(rotaColetaEntregaService.findById(idRotaColetaEntrega));
		manifestoColeta.setFilial(awb.getFilialByIdFilialDestino());
		manifestoColetaService.store(manifestoColeta);
		
		PedidoColeta pedidoColeta = this.generatePedidoColeta(awb, manifestoColeta);
		pedidoColetaService.storeAll(pedidoColeta, null, null, null, null);

		manifestoColetaService.executeRegistrarEmissaoManifestoColeta(manifestoColeta.getIdManifestoColeta());
		
		controleCargaService.generatePosEmissaoControleCarga(controleCarga.getIdControleCarga(), 
				controleCarga.getMeioTransporteByIdTransportado() != null ? controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte() : null,
				controleCarga.getMeioTransporteByIdSemiRebocado() != null ? controleCarga.getMeioTransporteByIdSemiRebocado().getIdMeioTransporte() : null,
				null, true, awb.getFilialByIdFilialDestino().getIdFilial());
		
		Filial filialCC = filialService.findById(controleCarga.getFilialByIdFilialOrigem().getIdFilial());
		
		informarSaidaService.executeEventsAndStoreConfirmaSaida(true, true, null, null, filialCC.getIdFilial(), 
				idMeioTransporte, null, controleCarga.getIdControleCarga(), controleCarga.getNrControleCarga(), 
				null, filialCC.getSgFilial(), filialCC, 
				JTDateTimeUtils.getDataHoraAtual(), true);
		
		toReturn.put("idControleCarga", controleCarga.getIdControleCarga());
		toReturn.put("idManifestoColeta", manifestoColeta.getIdManifestoColeta());
		toReturn.put("idRotaColetaEntrega", idRotaColetaEntrega);
		toReturn.put("idFilial", controleCarga.getFilialByIdFilialOrigem().getIdFilial());
	}
	
	public File generateControleCargaReport(Long idControleCarga, Long idFilialEmissao) {
		TypedFlatMap reportParams = new TypedFlatMap();
		
		reportParams.put("controleCarga.idControleCarga",idControleCarga);
		reportParams.put("filialUsuarioEmissor",idFilialEmissao);
		reportParams.put("blEmissao", true);
		reportParams.put("tpFormatoRelatorio", "pdf");
		
		MultiReportCommand mrc = new MultiReportCommand("relatorioControleCarga");
		
		mrc.addCommand("lms.coleta.emitirControleColetaEntregaService", reportParams);
		
		try {
			return this.reportExecutionManager.executeMultiReport(mrc);
		} catch (Exception e) {
			throw new InfrastructureException(e);
		}
	}
	public File generateManifestoColetaReport(Long idRotaColetaEntrega, Long idManifestoColeta) {
		TypedFlatMap reportParams = new TypedFlatMap();
		
		reportParams.put("rotaColetaEntrega.idRotaColetaEntrega",idRotaColetaEntrega);
		reportParams.put("manifestoColeta.idManifestoColeta", idManifestoColeta);
		reportParams.put("nrFolhasComplementares", 0);
		reportParams.put("tpFormatoRelatorio", "pdf");
		
		MultiReportCommand mrc = new MultiReportCommand("relatorioManifestoColeta");
		
		mrc.addCommand("lms.coleta.emitirManifestosService", reportParams);
		mrc.addCommand("lms.coleta.emitirManifestosAwbService", reportParams);
		
		try {
			return this.reportExecutionManager.executeMultiReport(mrc);
		} catch (Exception e) {
			throw new InfrastructureException(e);
		}
	}
	@SuppressWarnings("unchecked")
	private PedidoColeta generatePedidoColeta(Awb awb, ManifestoColeta manifestoColeta) {
		PedidoColeta pedidoColeta = new PedidoColeta();
		
		DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
		
		Cliente cliente = clienteService.findById(awb.getAeroportoByIdAeroportoDestino().getPessoa().getIdPessoa());
		EnderecoPessoa enderecoPessoa = awb.getAeroportoByIdAeroportoDestino().getPessoa().getEnderecoPessoa();
		Municipio municipio = awb.getAeroportoByIdAeroportoDestino().getPessoa().getEnderecoPessoa().getMunicipio();
		Moeda moeda = awb.getMoeda();
		
		TelefoneEndereco telefoneEndereco = null;
		if(enderecoPessoa.getTelefoneEnderecos() != null && ! enderecoPessoa.getTelefoneEnderecos().isEmpty()){
			telefoneEndereco = (TelefoneEndereco)CollectionUtils.find(enderecoPessoa.getTelefoneEnderecos(), new Predicate() {				
				@Override
				public boolean evaluate(Object arg0) {
					TelefoneEndereco te = (TelefoneEndereco)arg0;
					return te.getTpTelefone().equals(new DomainValue(TELEFONE_COMERCIAL));
				}
			});
		}
		
		pedidoColeta.setManifestoColeta(manifestoColeta);
		pedidoColeta.setCliente(cliente);
		pedidoColeta.setDhPedidoColeta(dataHoraAtual);
		pedidoColeta.setTpStatusColeta(new DomainValue(COLETA_EM_ABERTO));
		pedidoColeta.setTpModoPedidoColeta(new DomainValue(COLETA_MODO_TELEFONE));
		pedidoColeta.setTpPedidoColeta(new DomainValue(COLETA_TIPO_AEROPORTO));
		pedidoColeta.setDtPrevisaoColeta(JTDateTimeUtils.getDataAtual());
		pedidoColeta.setFilialByIdFilialSolicitante(awb.getFilialByIdFilialDestino());
		pedidoColeta.setFilialByIdFilialResponsavel(awb.getFilialByIdFilialDestino());
		pedidoColeta.setMoeda(moeda);
		pedidoColeta.setDhColetaDisponivel(dataHoraAtual);
		pedidoColeta.setUsuario(SessionUtils.getUsuarioLogado());
		
		pedidoColeta.setEnderecoPessoa(enderecoPessoa);
		pedidoColeta.setMunicipio(municipio);
		pedidoColeta.setEdColeta(this.enderecoPessoaService.getEnderecoCompleto(enderecoPessoa.getIdEnderecoPessoa()));
		pedidoColeta.setDsComplementoEndereco(enderecoPessoa.getDsComplemento());
		pedidoColeta.setDsBairro(enderecoPessoa.getDsBairro());
		pedidoColeta.setNrCep(enderecoPessoa.getNrCep());
		pedidoColeta.setNrEndereco(enderecoPessoa.getNrEndereco());
		
		if(telefoneEndereco != null){
			pedidoColeta.setNrDddCliente(telefoneEndereco.getNrDdd());
			pedidoColeta.setNrTelefoneCliente(telefoneEndereco.getNrTelefone());
		}
		
		pedidoColeta.setBlClienteLiberadoManual(Boolean.FALSE);
		pedidoColeta.setBlAlteradoPosProgramacao(Boolean.FALSE);
		pedidoColeta.setBlProdutoDiferenciado(Boolean.FALSE);
		pedidoColeta.setHrLimiteColeta(dataHoraAtual.toTimeOfDay());
		
		PedidoColeta ultimoPedidoColeta = pedidoColetaService.findUltimoPedidoColetaByIdCliente(cliente.getIdCliente());
		if (ultimoPedidoColeta!=null){
			pedidoColeta.setNmContatoCliente(ultimoPedidoColeta.getNmContatoCliente());
		} else {
			List contatos = contatoService.findContatosByIdPessoaTpContato(cliente.getIdCliente(), "CN");
			if (!contatos.isEmpty()){
				pedidoColeta.setNmContatoCliente(((Contato)contatos.get(0)).getNmContato());
			}
		}
		pedidoColeta.setNmSolicitante(pedidoColeta.getNmContatoCliente());
		pedidoColeta.setRotaColetaEntrega(manifestoColeta.getRotaColetaEntrega());
		
		List<Long> idsDoctos = pedidoColetaService.findIdsConhecimentoByAwb(awb.getIdAwb(), null);		
		pedidoColeta.setDetalheColetas(this.generateDetalheColetas(pedidoColeta, idsDoctos, municipio, awb.getFilialByIdFilialDestino(), moeda));
		
		Integer qtVolumes = 0;
		BigDecimal psTotal = new BigDecimal(0);
		BigDecimal psAforado = new BigDecimal(0);
		BigDecimal vlInformado = new BigDecimal(0);
		for (DetalheColeta dc : (List<DetalheColeta>)pedidoColeta.getDetalheColetas()) {
			qtVolumes += dc.getQtVolumes();
			psTotal = psTotal.add(dc.getPsMercadoria());
			psAforado = psAforado.add(dc.getPsAforado());
			vlInformado = vlInformado.add(dc.getVlMercadoria());
		}
		
		pedidoColeta.setQtTotalVolumesInformado(qtVolumes);
		pedidoColeta.setQtTotalVolumesVerificado(qtVolumes);
		pedidoColeta.setPsTotalInformado(psTotal);
		pedidoColeta.setPsTotalVerificado(psTotal);
		pedidoColeta.setPsTotalAforadoInformado(psAforado);
		pedidoColeta.setPsTotalAforadoVerificado(psAforado);
		pedidoColeta.setVlTotalInformado(vlInformado);
		pedidoColeta.setVlTotalVerificado(vlInformado);
		
		return pedidoColeta;
	}
	private List<DetalheColeta> generateDetalheColetas(PedidoColeta pedidoColeta,List<Long> idsDoctos, Municipio municipio, Filial filial, Moeda moeda) {
		Servico servico = servicoService.findServicoBySigla(SERVICO_AEREO_NACIONAL_CONVENCIONAL);
		
		List<DetalheColeta> list = new ArrayList<DetalheColeta>();
		for (Long idDocto : idsDoctos) {
			DoctoServico doc = new DoctoServico();
			doc.setIdDoctoServico(idDocto);
			DetalheColeta detalheColeta = new DetalheColeta();
			detalheColeta.setDoctoServico(doc);
			
			Conhecimento con = conhecimentoService.findById(idDocto);
			
			detalheColeta.setPedidoColeta(pedidoColeta);
			detalheColeta.setNaturezaProduto(con.getNaturezaProduto());
			detalheColeta.setVlMercadoria(con.getVlMercadoria());
			detalheColeta.setQtVolumes(con.getQtVolumes());
			detalheColeta.setPsAforado(con.getPsAforado());
			detalheColeta.setPsMercadoria(con.getPsReal());
			
			detalheColeta.setMunicipio(municipio);
			detalheColeta.setFilial(filial);			
			detalheColeta.setMoeda(moeda);
			detalheColeta.setServico(servico);
			detalheColeta.setTpFrete(new DomainValue(TIPO_FRETE_CIF));

			detalheColeta.setBlEntregaDireta(true);
			
			list.add(detalheColeta);
		}
		return list;
	}
	private void validaFiliais(Long id) {
		Awb awb = awbService.findById(id);
		if(awb.getFilialByIdFilialOrigem() != null){
			Long idFilialOrigem = awb.getFilialByIdFilialOrigem().getIdFilial();
			if (!SessionUtils.getFilialSessao().getIdFilial().equals(idFilialOrigem)) {
				throw new BusinessException("LMS-04181");
			}
		}
	}
	private void executeValidaEventosVooConfirmado(PreAlerta newPreAlerta, PreAlerta oldPreAlerta, Boolean blVooConfirmadoOld, Boolean blVooConfirmado, Boolean blIsFilialParceira, Long idFilial) {
		if (blVooConfirmadoOld.equals(blVooConfirmado)) {
			return;
		}
		
		short cdEvento;
		DateTime dhEvento;	
		List<Short> cdLocalizacaoMercadoria = new ArrayList<Short>();
		String	keyLMSException;
		String tpLocalizacaoAwb;
		String tpLocalizacaoAwbOcorrencia = null;
		
		Awb awb = awbService.findById(newPreAlerta.getAwb().getIdAwb());		
		
		if(newPreAlerta.getBlVooConfirmado()){
			cdEvento = CD_EMBARQUE_AEREO;
			dhEvento = newPreAlerta.getDhSaida();
			cdLocalizacaoMercadoria.add(CD_AGUARDANDO_EMBARQUE_CIA_AEREA);
			keyLMSException = LMS_DOCTOS_NAO_ENTREGUE_AERO;
			tpLocalizacaoAwb = TP_LOCALIZACAO_AWB_EMBARCADO;
			tpLocalizacaoAwbOcorrencia = tpLocalizacaoAwb;
		}else{
			cdEvento = CD_CANCELAMENTO_EMBARQUE_AEREO;
			dhEvento = JTDateTimeUtils.getDataHoraAtual();
			cdLocalizacaoMercadoria.add(CD_AGUARDANDO_EMBARQUE_CIA_AEREA);
			cdLocalizacaoMercadoria.add(CD_EM_VIAGEM_AEREA);
			keyLMSException = blIsFilialParceira ? LMS_DOCTOS_JA_COLETADOS : LMS_DOCTOS_NAO_EM_VIAGEM_AEREA;
			tpLocalizacaoAwb = TP_LOCALIZACAO_AWB_AGUARDANDO_EMBARQUE;
			
			if (TP_LOCALIZACAO_AWB_EMBARCADO.equals(awb.getTpLocalizacao().getValue())) {
				tpLocalizacaoAwbOcorrencia = tpLocalizacaoAwb;
			}
		}
		
		List<CtoAwb> ctoAwbs = ctoAwbService.findByIdAwb(newPreAlerta.getAwb().getIdAwb());
		
		/** Percorre conhecimentos do AWB */
		if (CollectionUtils.isNotEmpty(ctoAwbs)){		
			for (CtoAwb ctoAwb : ctoAwbs) {
				Conhecimento cto = ctoAwb.getConhecimento();
				
				/** Valida Localização esperada */
				this.validateLocalizacaoCto(cto, cdLocalizacaoMercadoria, keyLMSException);
				
				/** Valida Data de Saida do Voo com Data do evento de entrega no aeroporto */
				this.validateDataEntregaCtoAeroporto(newPreAlerta, cto);				
				
				/** Gera eventos para documentos e volumes*/
				this.generateEventosCtoAndVolumes(cto, cdEvento, dhEvento, idFilial, awb);
			}
		}
		
		/** Altera localização do AWB */
		awb.setTpLocalizacao(new DomainValue(tpLocalizacaoAwb));
		getAwbService().store(awb);
		
		//LMS-3381: Registrar ocorrencia na tabela de ocorrencias com o mesmo tpLocalizacao do Awb
		if (StringUtils.isNotBlank(tpLocalizacaoAwbOcorrencia)) {
			awbOcorrenciaService.store(awb, new DomainValue(tpLocalizacaoAwbOcorrencia), dhEvento);
			trackingAwbService.storeTrackingAwb(awb, awb.getCiaFilialMercurio().getEmpresa().getIdEmpresa(), tpLocalizacaoAwbOcorrencia);
		}
		
	}
	
	/**
	 * Gera eventos para documentos e volumes
	 * @param cto
	 * @param cdEvento
	 * @param dhEvento
	 * @param awb 
	 */
	private void generateEventosCtoAndVolumes(Conhecimento cto, short cdEvento,	DateTime dhEvento, Long idFilial, Awb awb) {
		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(cdEvento, cto.getIdDoctoServico(), idFilial, 
										AwbUtils.getSgEmpresaAndNrAwbFormated(awb), dhEvento, ConstantesExpedicao.AIRWAY_BILL);
		
		List<VolumeNotaFiscal> volumeNotaFiscais = volumeNotaFiscalService.findByIdConhecimento(cto.getIdDoctoServico());
		
		eventoVolumeService.storeEventoVolume(volumeNotaFiscais, cdEvento, TP_SCAN_LMS_MANUAL, idFilial, SessionUtils.getUsuarioLogado());
	}
	
	/**
	 * Valida Data de Saida do Voo com Data do evento de entrega no aeroporto
	 * @param newPreAlerta
	 * @param cto
	 */
	private void validateDataEntregaCtoAeroporto(PreAlerta newPreAlerta, Conhecimento cto) {
		EventoDocumentoServico evento = eventoDocumentoServicoService.findLastEventoNaoCanceladoByCd(cto.getIdDoctoServico(), CD_ENTREGA_REALIZADA_AEREO);
		if (evento != null && newPreAlerta.getDhSaida().isBefore(evento.getDhEvento())) {
			throw new BusinessException("LMS-04502", new Object[]{ JTDateTimeUtils.formatDateTimeToString(evento.getDhEvento()) });
		}
	}
	
	/**
	 * Valida Localização esperada
	 * @param cto
	 * @param cdLocalizacaoMercadoria
	 * @param keyLMSException
	 */
	private void validateLocalizacaoCto(Conhecimento cto, List<Short> cdLocalizacaoMercadoria, String keyLMSException) {
		LocalizacaoMercadoria lm = cto.getLocalizacaoMercadoria();
		if (!cdLocalizacaoMercadoria.contains(lm.getCdLocalizacaoMercadoria())) {
			throw new BusinessException(keyLMSException);
		}		
	}
	
	public java.io.Serializable store(PreAlerta bean) {
		return super.store(bean);
	}

	/**
	 * Envio de PreAlerta via e-mail para os contados da filial 
	 * de destino do AWB vinculado ao PreAlerta.
	 * @author Andre Valadas
	 * 
	 * @param preAlerta
	 */
	public void executeSendMailPreAlerta(PreAlerta preAlerta, List<File> attachments, Boolean blIsFilialParceira) {

		List contatos = new ArrayList<String>();
		if(blIsFilialParceira){
			Awb awb = awbService.findById(preAlerta.getAwb().getIdAwb());
			contatos = getPreAlertaDAO().findEmailsContadosByParceira(awb.getClienteByIdClienteDestinatario().getIdCliente());
		}else{
			contatos = getPreAlertaDAO().findEmailsContadosByFiliaisDestinoAWB(preAlerta.getIdPreAlerta());
		}
		
		if (contatos.size() < 1) {
			throw new BusinessException("LMS-04177");
		}
		
		String[] dsEmails = (String[]) contatos.toArray(new String[]{});
		/** Dados do E-mail */
		String strSubject = configuracoesFacade.getMensagem("preAlerta");
		String strFrom = (String)configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		StringBuffer strText = new StringBuffer();
		strText.append("LMS - Logistics Management System").append(LINE_SEPARATOR);
		strText.append(configuracoesFacade.getMensagem("preAlertaCargaAerea").toUpperCase()).append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		strText.append(configuracoesFacade.getMensagem("data")).append(": ").append(JTFormatUtils.format(JTDateTimeUtils.getDataAtual())).append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		strText.append(configuracoesFacade.getMensagem("mailPreAlertaText1", new Object[]{preAlerta.getNrPreAlerta()})).append(LINE_SEPARATOR).append(LINE_SEPARATOR);
		strText.append(configuracoesFacade.getMensagem("mailPreAlertaText2", new Object[]{configuracoesFacade.getValorParametro("ENDERECO_LMS")}));

		/** Envia E-mail */
		sendEmail(
			 dsEmails
			,strSubject
			,strFrom
			,strText.toString()
			,attachments);
	}

	/**
	 * @author Andre Valadas
	 * 
	 * @param strEmails
	 * @param strSubject
	 * @param strFrom
	 * @param strText
	 */
	private void sendEmail(final String[] strEmails,final  String strSubject,final  String strFrom,final  String strText, final List<File> attachments){
		List<MailAttachment> mailAttachments = new ArrayList<MailAttachment>();
		
		if (attachments != null) {
			for (File file : attachments) {
				MailAttachment mailAttachment = new MailAttachment();
				mailAttachment.setName(file.getName());
				mailAttachment.setData(FileUtils.readFile(file));
				mailAttachments.add(mailAttachment);
			}
		}
				
		Mail mail = createMail(StringUtils.join(strEmails, ";"), strFrom, strSubject, strText, mailAttachments);
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}

	private Mail createMail(String strTo, String strFrom, String strSubject, String body, List<MailAttachment> mailAttachmentList) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		mail.setAttachements(mailAttachmentList.toArray(new MailAttachment[mailAttachmentList.size()]));
		return mail;
	}
	
	/**
	 * @param nrAwb
	 * @return
	 */
	public List findByNrAwb(Long nrAwb) {
		return getPreAlertaDAO().findByNrAwb(nrAwb);
	}

	/**
	 * Recupera uma instância de <code>PreAlerta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public PreAlerta findById(java.lang.Long id) {
		return (PreAlerta) super.findById(id);
	}

	public TypedFlatMap findMapById(Long id) {
		return getPreAlertaDAO().findMapById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setPreAlertaDAO(PreAlertaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private PreAlertaDAO getPreAlertaDAO() {
		return (PreAlertaDAO) getDao();
	}

	/**
	 * Apaga uma entidade através do Id do Pedido de Coleta.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeByIdPedidoColeta(Long idPedidoColeta) {
		this.getPreAlertaDAO().removeByIdPedidoColeta(idPedidoColeta);
	}

	/**
	 * getRowCount Visualizar Prealerta Implementado
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCount(Map criteria) {
		return getPreAlertaDAO().getRowCount(criteria);
	}

	/**
	 * findPaginated Visualizar Prealerta Implementado
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @param findDef
	 * @return TypedFlatMap
	 */
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage<Map<String, Object>> retornoPaginado = getPreAlertaDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		List<Map<String, Object>> mapasPaginados = retornoPaginado.getList();
		formataAwb(mapasPaginados);
		return retornoPaginado;
	}

	/**
	 * formataAwb Formata o Awb para devolver as telas de Manter e Visualizar Pré-Alertas
	 * 
	 * @param mapasPaginados
	 */
	private void formataAwb(List<Map<String, Object>> mapasPaginados) {
		for (Map mapa :  mapasPaginados){
			String awbFormatado = "";
			String awb = "";
			if (mapa.get("nrAwb") != null && mapa.get("dvAwb") != null && mapa.get("dsSerie") != null && mapa.get("sgEmpresa") != null){
				awbFormatado = mapa.get("sgEmpresa") + " " + AwbUtils.getNrAwbFormated(mapa.get("dsSerie").toString(), Long.valueOf(mapa.get("nrAwb").toString()), Integer.valueOf(mapa.get("dvAwb").toString()));
				awb = AwbUtils.getNrAwb(mapa.get("dsSerie").toString(), Long.valueOf(mapa.get("nrAwb").toString()), Integer.valueOf(mapa.get("dvAwb").toString()));
			}
			mapa.put("awbFormatado", awbFormatado);
			mapa.put("awb", awb);
		}
	}
	/**
	 * getRowCountPrealerta Implementado
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountPreAlerta(TypedFlatMap criteria) {
		return getPreAlertaDAO().getRowCountPreAlerta(criteria);
	}

	/**
	 * findPaginatedPrealerta Implementado
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @param findDef
	 * @return TypedFlatMap
	 */
	public ResultSetPage findPaginatedPreAlerta(TypedFlatMap criteria) {
		ResultSetPage rsp = getPreAlertaDAO().findPaginatedPreAlerta(criteria, FindDefinition.createFindDefinition(criteria));
		List<Map<String, Object>> listaPaginada = rsp.getList();
		formataAwb(listaPaginada);
		return rsp;
	}

	/**
	 * findAwbByPreAlerta
	 * @author Andre Valadas
	 * 
	 * @param criteria
	 * @return TypedFlatMap
	 */
	public TypedFlatMap findAwbByPreAlerta(Long idPreAlerta) {
		List awbs = getPreAlertaDAO().findAwbByPreAlerta(idPreAlerta);
		TypedFlatMap typedFlatMap = new TypedFlatMap();
		for (Iterator iter = awbs.iterator(); iter.hasNext();) {
			typedFlatMap.putAll((TypedFlatMap) iter.next());
			/** Valor das Mercadorias */
			double sumMercadorias = 0;
			List<CtoAwb> ctoAwbs = ctoAwbService.findByIdAwb(typedFlatMap.getLong("awb.idAwb"));
			if(ctoAwbs != null && !ctoAwbs.isEmpty()) {
				for (CtoAwb ctoAwb : ctoAwbs) {
					sumMercadorias += ctoAwb.getConhecimento().getVlMercadoria().doubleValue();
				}
			}
			typedFlatMap.put("awb.ctoAwbs.vlMercadoria", new Double(sumMercadorias));
			/** Serviços Lookup */
			typedFlatMap.put("idServicoAereoNacConv", Long.valueOf(configuracoesFacade.getValorParametro("ID_SERVICO_AEREO_NAC_CONV").toString()));
		}
		return typedFlatMap;
	}

	public TypedFlatMap findVlMercadoriaAndIdServicoAereoNacConv(Long idAwb) {
		TypedFlatMap typedFlatMap = new TypedFlatMap();
		double sumMercadorias = 0;
		
		List<CtoAwb> ctoAwbs = ctoAwbService.findByIdAwb(idAwb);
		if(ctoAwbs != null && !ctoAwbs.isEmpty()) {
			for (CtoAwb ctoAwb : ctoAwbs) {
				sumMercadorias += ctoAwb.getConhecimento().getVlMercadoria().doubleValue();
			}
		}
		typedFlatMap.put("awb.ctoAwbs.vlMercadoria", new Double(sumMercadorias));
		/** Serviços Lookup */
		typedFlatMap.put("idServicoAereoNacConv", Long.valueOf(configuracoesFacade.getValorParametro("ID_SERVICO_AEREO_NAC_CONV").toString()));
		
		return typedFlatMap;
	}

	/**
	 * Recebimento de PreAlertas
	 * @author Andre Valadas
	 * 
	 * @param idsList<Long>
	 */
	public void executeRecebimentoPreAlerta(List idsList) {
		//Verifica se Existem registros
		if (idsList.size() < 1)
			throw new BusinessException("LMS-04178");

		//Busca Lista de PreAlertas
		List preAlertas = getPreAlertaDAO().findByIds(idsList);
		for (Iterator iter = preAlertas.iterator(); iter.hasNext();) {
			PreAlerta preAlerta = (PreAlerta) iter.next();
			///Caso ja tenha sido confirmada por outro Usuario
			if (preAlerta.getDhRecebimentoMens() != null) {
				throw new BusinessException("LMS-04179");
			}

			// Se Usuario pertence a um das Filiais Origem/Destino
			if (!SessionUtils.isFilialAllowedByUsuario(preAlerta.getAwb().getFilialByIdFilialOrigem()) &&
					!SessionUtils.isFilialAllowedByUsuario(preAlerta.getAwb().getFilialByIdFilialDestino())) {
				throw new BusinessException("LMS-04180");
			}

			//Altera Dados do PreAlerta
			preAlerta.setUsuario(SessionUtils.getUsuarioLogado());
			preAlerta.setDhRecebimentoMens(JTDateTimeUtils.getDataHoraAtual());
			store(preAlerta);
		}
	}
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
	public IncluirEventosRastreabilidadeInternacionalService getIncluirEventosRastreabilidadeInternacionalService() {
		return incluirEventosRastreabilidadeInternacionalService;
	}
	public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}
	public VolumeNotaFiscalService getVolumeNotaFiscalService() {
		return volumeNotaFiscalService;
	}
	public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}
	public EventoVolumeService getEventoVolumeService() {
		return eventoVolumeService;
	}
	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}
	public EventoDocumentoServicoService getEventoDocumentoServicoService() {
		return eventoDocumentoServicoService;
	}
	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}
	public AwbService getAwbService() {
		return awbService;
	}
	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
	public void setManifestoColetaService(ManifestoColetaService manifestoColetaService) {
		this.manifestoColetaService = manifestoColetaService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
	public void setOcorrenciaColetaService(OcorrenciaColetaService ocorrenciaColetaService) {
		this.ocorrenciaColetaService = ocorrenciaColetaService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public LocalizacaoMercadoriaService getLocalizacaoMercadoriaService() {
		return localizacaoMercadoriaService;
	}
	public void setLocalizacaoMercadoriaService(
			LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}
	public void setInformarSaidaService(InformarSaidaService informarSaidaService) {
		this.informarSaidaService = informarSaidaService;
	}
	public AwbOcorrenciaService getAwbOcorrenciaService() {
		return awbOcorrenciaService;
	}
	public void setAwbOcorrenciaService(AwbOcorrenciaService awbOcorrenciaService) {
		this.awbOcorrenciaService = awbOcorrenciaService;
	}
	public TrackingAwbService getTrackingAwbService() {
		return trackingAwbService;
	}
	public void setTrackingAwbService(TrackingAwbService trackingAwbService) {
		this.trackingAwbService = trackingAwbService;
	}

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
}
