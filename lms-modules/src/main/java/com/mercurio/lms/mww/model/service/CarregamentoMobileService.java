package com.mercurio.lms.mww.model.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.lms.constantes.ConsErro;
import com.mercurio.lms.constantes.ConsGeral;
import com.mercurio.lms.constantes.entidades.ConsManifesto;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.LockMode;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.AdiantamentoTrecho;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.CarregamentoPreManifesto;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.DispCarregIdentificado;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.Equipe;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.LacreControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.ManifestoNacionalVolume;
import com.mercurio.lms.carregamento.model.PostoPassagemCc;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.carregamento.model.service.AdiantamentoTrechoService;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.carregamento.model.service.CarregamentoPreManifestoService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.carregamento.model.service.DispCarregIdentificadoService;
import com.mercurio.lms.carregamento.model.service.EquipeOperacaoService;
import com.mercurio.lms.carregamento.model.service.EquipeService;
import com.mercurio.lms.carregamento.model.service.FilialRotaCcService;
import com.mercurio.lms.carregamento.model.service.LacreControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoNacionalVolumeService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.PostoPassagemCcService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.carregamento.model.service.PreManifestoVolumeService;
import com.mercurio.lms.carregamento.model.service.TipoDispositivoUnitizacaoService;
import com.mercurio.lms.carregamento.model.service.UnitizacaoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contratacaoveiculos.model.FluxoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.SolicitacaoContratacaoService;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.DocumentoMir;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.service.AgendamentoDoctoServicoService;
import com.mercurio.lms.entrega.model.service.DocumentoMirService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConferirVolumeService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoAwbService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.OrdemFilialFluxo;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.municipios.model.service.FilialRotaService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.OrdemFilialFluxoService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.municipios.model.service.RotaIdaVoltaService;
import com.mercurio.lms.municipios.model.service.SubstAtendimentoFilialService;
import com.mercurio.lms.municipios.model.service.TrechoRotaIdaVoltaService;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.portaria.model.MacroZona;
import com.mercurio.lms.portaria.model.OrdemSaida;
import com.mercurio.lms.portaria.model.service.MacroZonaService;
import com.mercurio.lms.portaria.model.service.OrdemSaidaService;
import com.mercurio.lms.sgr.dto.PlanoGerenciamentoRiscoDTO;
import com.mercurio.lms.sgr.model.service.PlanoGerenciamentoRiscoService;
import com.mercurio.lms.sgr.model.util.PlanoGerenciamentoRiscoUtils;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.SolicitacaoRetirada;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.SolicitacaoRetiradaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class CarregamentoMobileService extends AbstractMobileService {
    

	private static final String STATUS_MANIFESTO_CARREG_CONCLUIDO = "CC";
    private static final String KEY = "key";
    private static final String MESSAGE = "message";
    private static final String NEED_CONFIRMATION = "needConfirmation";
    private static final String ALERT = "alert";
    private static final String ITEM_DESCRIPTION = "itemDescription";
    private static final String PA_SGR_VAL_EXIST_PGR = "SGR_VAL_EXIST_PGR";
    
    private AgendamentoDoctoServicoService agendamentoDoctoServicoService;
    private AdiantamentoTrechoService adiantamentoTrechoService;
    private CarregamentoDescargaService carregamentoDescargaService;
    private CarregamentoPreManifestoService carregamentoPreManifestoService;
    private ConferirVolumeService conferirVolumeService;
    private ConfiguracoesFacade configuracoesFacade;
    private ControleCargaService controleCargaService;
    private ControleTrechoService controleTrechoService;
    private ConversaoMoedaService conversaoMoedaService;
    private DispCarregIdentificadoService dispCarregIdentificadoService;
    private DoctoServicoService doctoServicoService;
    private DocumentoMirService documentoMirService;
    private DomainValueService domainValueService;
    private EquipeService equipeService;
    private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
    private EventoVolumeService eventoVolumeService;
    private FilialRotaCcService filialRotaCcService;
    private FilialRotaService filialRotaService;
    private IncluirEventosRastreabilidadeInternacionalService eventoRastreabilidadeService;
    private LacreControleCargaService lacreControleCargaService;
    private MacroZonaService macroZonaService;
    private ManifestoService manifestoService;
    private MeioTransporteService meioTransporteService;
    private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
    private OrdemFilialFluxoService ordemFilialFluxoService;
    private OrdemSaidaService ordemSaidaService;
    private ParametroGeralService parametroGeralService;
    private PostoPassagemCcService postoPassagemCcService;
    private PreManifestoDocumentoService preManifestoDocumentoService;
    private RotaColetaEntregaService rotaEntregaService;
    private RotaIdaVoltaService rotaIdaVoltaService;
    private SolicitacaoContratacaoService solicitacaoContratacaoService;
    private SolicitacaoRetiradaService solicitacaoRetiradaService;
    private SubstAtendimentoFilialService substAtendimentoFilialService;
    private TrechoRotaIdaVoltaService trechoRotaIdaVoltaService;
    private UnitizacaoService unitizacaoService;
    private EquipeOperacaoService equipeOperacaoService;
    private ConhecimentoService conhecimentoService;
    private MeioTranspProprietarioService meioTranspProprietarioService;
    private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
    private OcorrenciaPendenciaService ocorrenciaPendenciaService;
    private EventoService eventoService;
    private ManifestoNacionalVolumeService manifestoNacionalVolumeService;
    private PreManifestoVolumeService preManifestoVolumeService;
    private TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService;
    private CtoAwbService ctoAwbService;
    private AwbService awbService;
    private MunicipioService municipioService;
    private PlanoGerenciamentoRiscoUtils planoUtils;
    private PlanoGerenciamentoRiscoService planoGerenciamentoRiscoService;

    public boolean isCarga() {
        return true;
    }

    public MeioTransporte findMeioTransporteByBarCode(Long nrCodigoBarras) {
        MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByCodigoBarras(nrCodigoBarras);
//			LMS-06018- Meio de transporte não encontrado.
        if (meioTransporte == null){
            throw new BusinessException("LMS-06018");
        }
        return meioTransporte;
    }

    /*
     * i. Validar se o veiculo pode ser utilizado para a criação de um novo
     * Controle de Carga (utilizar rotina de validação: “Validar Semi-reboque
     * Controle de Carga” OU “Validar Veículo Controle de Carga”, dependendo do
     * tipo do meio de transporte, e a rotina “Verificar Estado Meio
     * Transporte”) ii. Caso alguma regra impedir de prosseguir, será
     * apresentada a mensagem correspondente e logo o sistema voltará ao
     * sub-menu viagem
     */
    public void validateMeioTransporte(MeioTransporte meioTransporte) {
        if (meioTransporteService.findMeioTransporteIsSemiReboque(meioTransporte.getIdMeioTransporte())) {
            /*
             * Executa validações para saber se meio transporte pode ser
			 * associado a um novo controle de cargas
			 */
            controleCargaService.validateSemiReboqueControleCarga(meioTransporte.getIdMeioTransporte());
            /* Verifica se o semi-reboque está ativo */
            if (!meioTransporteService.validateMeioTransporteAtivo(meioTransporte.getIdMeioTransporte())) {
                throw new BusinessException("LMS-00059");
            }
        } else {
            /*
			 * Executa validações para saber se meio transporte pode ser
			 * associado a um novo controle de cargas
			 */
            controleCargaService.validateVeiculoControleCarga(meioTransporte.getIdMeioTransporte(), true);
			/* Verifica se o veículo está ativo */
            if (!meioTransporteService.validateMeioTransporteAtivo(meioTransporte.getIdMeioTransporte())) {
                throw new BusinessException("LMS-00058");
            }
			
			/* Verifica se veículo possui ordem de saída em aberto */
            Map<String, Long> mapMeioTransporteTransportado = new HashMap<String, Long>();
            mapMeioTransporteTransportado.put("idMeioTransporte", meioTransporte.getIdMeioTransporte());
            Map<String, Map<String, Long>> mapOrdemSaida = new HashMap<String, Map<String, Long>>();
            mapOrdemSaida.put("meioTransporteRodoviarioByIdMeioTransporte", mapMeioTransporteTransportado);

            List<OrdemSaida> ordensSaida = ordemSaidaService.findByMeioTransporteInOrdemSaida(mapOrdemSaida);
            if (ordensSaida != null && !ordensSaida.isEmpty()) {
                OrdemSaida ordemSaida = ordensSaida.get(0);
                throw new BusinessException("LMS-05007", new Object[]{ordemSaida.getFilialByIdFilialOrigem().getSgFilial()});
            }
        }
		
		/*
		 * Verifica se meio de transporte possui seguro vigente e se está
		 * associado a um proprietário.
		 */
        meioTransporteRodoviarioService.validateEstadoMeioTransporte(meioTransporte.getIdMeioTransporte());
    }

    public ControleCarga findControleCargaAberto(Long idMeioTransporte, String tpControleCarga) {
        Long idFilialUsuarioLogado = SessionUtils.getFilialSessao().getIdFilial();

        List<String> listaStatus = new ArrayList<String>();
        listaStatus.add("GE"); // Gerado
        listaStatus.add("EC"); // Em Carregamento
        listaStatus.add("PO"); // Parada Operacional
        listaStatus.add("CP"); // Carregamento Parcial
        listaStatus.add("PM"); // Pre manifesto

        List<ControleCarga> listCc = controleCargaService.findByMeioTransporteAndStatus(idMeioTransporte, listaStatus);
        ControleCarga retorno = null;
        for (ControleCarga cc : listCc) {
			/*
			 * Verifica se o tipo de controle de carga encontrado é igual ao
			 * tipo desejado
			 */
            if (cc.getTpControleCarga().getValue().equalsIgnoreCase(tpControleCarga)) {
				/*
				 * Verifica se o controle de carga encontra-se na filial do
				 * usuário logado
				 */
                if (!cc.getFilialByIdFilialAtualizaStatus().getIdFilial().equals(idFilialUsuarioLogado)) {
					/* Este Controle de Carga não se encontra nesta filial. */
                    throw new BusinessException("LMS-05070");
                } else {
                    retorno = cc;
                    break;
                }
            }
        }
        return retorno;
    }

    public ControleCarga findControleCargaAberto(List<ControleCarga> listCc, String tpControleCarga) {
        ControleCarga retorno = null;
        for (ControleCarga cc : listCc) {
            /*
             * Verifica se o tipo de controle de carga encontrado é igual ao
             * tipo desejado
             */
            if (cc.getTpControleCarga().getValue().equalsIgnoreCase(tpControleCarga)) {
                /*
                 * Verifica se o controle de carga encontra-se na filial do
                 * usuário logado
                 */
                if (!cc.getFilialByIdFilialAtualizaStatus().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
                    /* Este Controle de Carga não se encontra nesta filial. */
                    throw new BusinessException("LMS-05070");
                } else {
                    retorno = cc;
                    break;
                }
            }
        }
        return retorno;
    }

    public List<PreManifestoDocumento> findDoctoServicoDivergente(Long idControleCarga) {
        return preManifestoDocumentoService.findByControleCarga(idControleCarga);
    }

    /*
     * Busca doctos divergentes por quantidade e por pertencerem ao mesmo AWB
     */
    public List<Map> findDoctoServicoDivergenteEntrega(Long idControleCarga) {
        return preManifestoDocumentoService.findDoctoServicoDivergenteEntrega(idControleCarga);
    }

    private List<Map<String, Object>> generateMessageList(List<BusinessException> exceptions,
                                                          List<BusinessException> confirmations, List<BusinessException> alerts, String itemDescription) {
        List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();

        for (BusinessException exception : exceptions) {
            Map<String, Object> message = new HashMap<String, Object>();
            message.put(KEY, exception.getMessageKey());
            message.put(MESSAGE, configuracoesFacade.getMensagem(exception.getMessageKey(), exception
                    .getMessageArguments()));
            message.put(NEED_CONFIRMATION, false);
            message.put(ALERT, false);
            message.put(ITEM_DESCRIPTION, itemDescription);
            messages.add(message);
        }

        if (exceptions.isEmpty()) {
            for (BusinessException confirm : confirmations) {
                Map<String, Object> message = new HashMap<String, Object>();
                message.put(KEY, confirm.getMessageKey());
                message.put(MESSAGE, configuracoesFacade.getMensagem(confirm.getMessageKey(), confirm
                        .getMessageArguments()));
                message.put(NEED_CONFIRMATION, true);
                message.put(ALERT, false);
                message.put(ITEM_DESCRIPTION, itemDescription);
                messages.add(message);
            }

            for (BusinessException alert : alerts) {
                Map<String, Object> message = new HashMap<String, Object>();
                message.put(KEY, alert.getMessageKey());
                message.put(MESSAGE, alert.getMessageKey() + " - " + configuracoesFacade.getMensagem(alert.getMessageKey(), alert
                        .getMessageArguments()));
                message.put(NEED_CONFIRMATION, false);
                message.put(ALERT, true);
                message.put(ITEM_DESCRIPTION, itemDescription);
                messages.add(message);
            }
        }

        return messages;
    }

    private List<Map<String, Object>> cleanMessageList(List<Map<String, Object>> messages) {
        List<Map<String, Object>> exceptions = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> confirmations = new ArrayList<Map<String, Object>>();

        for (Map<String, Object> message : messages) {
            if ((Boolean) message.get(NEED_CONFIRMATION)) {
                confirmations.add(message);
            } else {
                exceptions.add(message);
            }
        }

        if (!exceptions.isEmpty()) {
            return exceptions;
        } else {
            return confirmations;
        }
    }

    /**
     * Busca a filial de destino para o manifesto que conterá o documento de
     * serviço a partir das informações de origem, destino e fluxo do documento
     * de serviço
     *
     * @param idRota
     * @param idFluxoDoctoServico
     * @param idFilialOrigem
     * @param idFilialDestino
     * @return
     */
    private Long findIdFilialDestinoManifesto(Long idRota, Long idFluxoDoctoServico, Long idFilialOrigem,
                                              Long idFilialDestino) {
		/*
		 * Verifica se a filial destino do docto serviço faz parte da rota e se
		 * ela é posterior a filial do usuário logado
		 */
        List<FilialRota> filiaisRestantesRota = filialRotaService.findFiliaisRestantesByRota(idRota, idFilialOrigem);
		
		/*
		 * Percorre lista de filiais restantes da rota, verificando se alguma
		 * delas é a filial destino do documento
		 */
        for (FilialRota filialRota : filiaisRestantesRota) {
            Filial filial = filialRota.getFilial();
            if (filial.getIdFilial().equals(idFilialDestino)) {
				/*
				 * Se filial corrente for a filial destino da rota, então
				 * retorna o id desta filial
				 */
                return filial.getIdFilial();
            }
        }
			
		/* Verifica as filiais da rota que possuem fluxo de filial válido */
        Long idFilialComFluxoMaiorOrdem = null;
        Byte nrMaiorOrdem = 0;

        if (idFluxoDoctoServico != null) {
			/* Pega a lista de filiais do fluxo do documento */
            List<OrdemFilialFluxo> filiaisFluxo = ordemFilialFluxoService.findByIdFluxoFilial(idFluxoDoctoServico);
			
			/*
			 * Percorre lista de filiais restantes da rota, verificando se
			 * alguma delas pertence ao fluxo do documento
			 */
            for (FilialRota filialRota : filiaisRestantesRota) {
				/* Percorre as filiais que fazem parte do fluxo */
                for (OrdemFilialFluxo filialFluxo : filiaisFluxo) {
					/* Se a filial da rota fizer parte do fluxo do documento e a ordem desta filial for maior que a maior até
					 * então...
					 */
                    if (filialFluxo.getFilial().getIdFilial().equals(filialRota.getFilial().getIdFilial()) &&
                            filialRota.getNrOrdem() > nrMaiorOrdem) {
                        nrMaiorOrdem = filialRota.getNrOrdem();
                        idFilialComFluxoMaiorOrdem = filialRota.getFilial().getIdFilial();
                    }
                }
            }
        }

        return idFilialComFluxoMaiorOrdem;
    }

    /**
     * @param conhecimento
     * @param volume
     * @param controleCarga
     * @param tpManifesto
     * @param idFilialDesvio
     * @param blsorter
     * @param tpScan
     * @return
     */

    private List<Map<String, Object>> validateDoctoServicoConhecimento(Conhecimento conhecimento, VolumeNotaFiscal volume, ControleCarga controleCarga,
                                                                       String tpManifesto, Long idFilialDesvio, Boolean blsorter, String tpScan) {
        List<BusinessException> exceptions = new ArrayList<BusinessException>();
        List<BusinessException> confirmations = new ArrayList<BusinessException>();
        List<BusinessException> alerts = new ArrayList<BusinessException>();

        DoctoServico doctoServico = conhecimento;

        // LMS-7350
        if (null != conhecimento.getLocalizacaoMercadoria() &&
                !validaLocalizacaoMercadoriaCarregamento(volume, tpScan, conhecimento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria(), controleCarga.getTpControleCarga().getValue())) {
            // Gera evento de volume lido
            final String cdEventoVolumeLido = parametroGeralService.findSimpleConteudoByNomeParametro("CD_EVENTO_VOLUME_LIDO");
            eventoVolumeService.generateEventoVolume(volume, Short.valueOf(cdEventoVolumeLido), tpScan);

            exceptions.add(new BusinessException("LMS-45207", new Object[]{conhecimento.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria()}));
        }

        if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
            controleCarga.getMeioTransporteByIdSemiRebocado();
        } else {
            controleCarga.getMeioTransporteByIdTransportado();
        }

        Long idDoctoServico = doctoServico.getIdDoctoServico();
        Long idControleCarga = controleCarga.getIdControleCarga();
        Long idFilialDestinoDocto = doctoServico.getFilialByIdFilialDestino().getIdFilial();
        String tpDocumentoServico = doctoServico.getTpDocumentoServico().getValue();
        String tpAbrangencia = "N";
        Long idFluxoFilial = null;
        if (doctoServico.getFluxoFilial() != null) {
            idFluxoFilial = doctoServico.getFluxoFilial().getIdFluxoFilial();
        }

        Filial filial = doctoServico.getFilialLocalizacao();
        if (filial == null) {
            filial = doctoServico.getFilialByIdFilialOrigem();
        }
		
		/* Seta ids das filiais envolvidas */
        Long idFilialOrigem = SessionUtils.getFilialSessao().getIdFilial();
        Long idFilialDestinoManifesto = null;
        if ("E".equals(tpManifesto)) {
	/*
			 * Se for manifesto de entrega, filial destino recebe a filial do
			 * usuário logado
			 */
            idFilialDestinoManifesto = SessionUtils.getFilialSessao().getIdFilial();
        } else {
			/* senão recebe a filial de destino do docto de serviço */
            idFilialDestinoManifesto = this.findIdFilialDestinoManifesto(controleCarga.getRota().getIdRota(),
                    idFluxoFilial, idFilialOrigem, idFilialDestinoDocto);
        }
		
		
		/* Verifica em qual manifesto deve estar o controle de carga */
        if (idFilialDestinoManifesto == null) {
            if (controleCarga.getIdControleCarga() != null) {
                confirmations.add(new BusinessException("LMS-05113"));
            } else {
                exceptions.add(new BusinessException("LMS-45044"));
            }
        }

        Long idFilialValidacao = idFilialDestinoManifesto == null ? idFilialDesvio : idFilialDestinoManifesto;
        if (idFilialValidacao != null) {
            Boolean validaDocumento = preManifestoDocumentoService.validateDocumentoServicoManifesto(idFilialValidacao, tpManifesto,
                    conhecimento.getIdDoctoServico());
            if (!validaDocumento) {
                exceptions.add(new BusinessException(ConsErro.DOC_N_PODE_SER_ENTREGUE_UNI_PARM_P_NAO_ENT_SEM_DESTINO_P_MESMA));
            }
        }

        List<Manifesto> manifestos = null;
        if (idControleCarga != null && idFilialDestinoManifesto != null) {
            manifestos = manifestoService.findManifestosByTrecho(tpManifesto, "EC", idControleCarga, idFilialOrigem,
                    idFilialDestinoManifesto);
        }
		
		/* Se não existe manifesto, e a filial destino é diferente da filial
		   destino do documento de serviço (ou seja, desvio), não permite criar */
        validaPrimeiroVolumeIniciarPremanifestoExistirCTRCStatusFilial(exceptions, doctoServico, filial, manifestos);
			
		/* Verifica se o documento está bloqueado quando tipo <> RRE */
        validaDocumentoBloqueado(exceptions, doctoServico, idDoctoServico, tpDocumentoServico);
		
		
		/* Verifica status do conhecimento */
        String tpSituacaoConhecimento = conhecimento.getTpSituacaoConhecimento().getValue();
		
		/* Se status for diferente de emitido, ou documento não emitidos dispara erro  */
        if ((!"E".equals(tpSituacaoConhecimento)) && (!"P".equals(tpSituacaoConhecimento))) {
            exceptions.add(new BusinessException("LMS-45055"));
        } else if (("P".equals(tpSituacaoConhecimento)) && (!blsorter)) {
            throw new BusinessException("LMS-45092");
        }
		
		/* Verifica se o documento está associado a uma solicitação de retirada
		   para poder ser incluido em um manifesto de Viagem ou Entreg a, caso a
		   filial de retirada for igual da filial localização do documento, gera exceção. */
        List listSolicitacaoRetirada = solicitacaoRetiradaService.findSolicitacaoRetiradaByIdDoctoServico(doctoServico
                .getIdDoctoServico());
        if (!listSolicitacaoRetirada.isEmpty()) {
            for (Iterator iter = listSolicitacaoRetirada.iterator(); iter.hasNext(); ) {
                SolicitacaoRetirada solicitacaoRetirada = (SolicitacaoRetirada) iter.next();

                if (solicitacaoRetirada.getFilialRetirada().getIdFilial().equals(
                        doctoServico.getFilialLocalizacao().getIdFilial())
                        && "A".equals(solicitacaoRetirada.getTpSituacao().getValue())) {
                    exceptions.add(new BusinessException("LMS-05119"));
                }
            }
        }

        if ("V".equals(tpManifesto)) {
            if ("NFT".equals(tpDocumentoServico) || "NFE".equals(tpDocumentoServico)) {
                exceptions.add(new BusinessException(ConsErro.DOCTO_SERVICO_INVALIDO));
            }

            if ("RRE".equals(tpDocumentoServico)) {
                exceptions.add(new BusinessException(ConsErro.DOCTO_SERVICO_INVALIDO));
            }

            validaTpAbrangencia(exceptions, tpDocumentoServico, tpAbrangencia);
        } else {
			/*
			 * Verifica se a rota de entrega do documento é diferente da rota de
			 * entrega do Controle de Carga no Manifesto em questão
			 */
            this.validateRotaEntrega(controleCarga.getRotaColetaEntrega(), doctoServico, confirmations);
			
			/* Verifica se o Documento é do tipo 'RRE' e permite incluir no
			   manifesto somente se for vinculada a uma MIR do tipo 'Administrativo para entrega'. */
            if ("RRE".equals(doctoServico.getTpDocumentoServico().getValue())) {
                DocumentoMir documentoMir = documentoMirService.findDocumentoMirByIdReciboReembolso(idDoctoServico,
                        "AE");

                if (documentoMir == null) {
					/* Recibo necessita estar vinculado a uma MIR do tipo
					   administrativo para entrega. */
                    exceptions.add(new BusinessException("LMS-05046"));
                }
            } else {
				/* Verifica se o documento possui agendamento Ativo */
                AgendamentoDoctoServico agendamentoDoctoServico = null;

                List listAgendamentoDoctoServico = agendamentoDoctoServicoService
                        .findAgendamentoByIdDoctoServico(idDoctoServico);
                for (Iterator iter = listAgendamentoDoctoServico.iterator(); iter.hasNext(); ) {
                    AgendamentoDoctoServico agendamento = (AgendamentoDoctoServico) iter.next();

                    if ("A".equals(agendamento.getTpSituacao().getValue())
                            && "A".equals(agendamento.getAgendamentoEntrega().getTpSituacaoAgendamento().getValue())) {
                        agendamentoDoctoServico = agendamento;
                    }
                }

                if (agendamentoDoctoServico != null) {
                    AgendamentoEntrega agendamentoEntrega = agendamentoDoctoServico.getAgendamentoEntrega();
					/*
					 * Testa se data do Agendamento é maior ou menos que a data
					 * atual
					 */
                    if (agendamentoEntrega.getDtAgendamento().compareTo(JTDateTimeUtils.getDataAtual()) > 0) {
						/* Agendado para dia */
                        String dataAgendamento = JTFormatUtils.format(agendamentoEntrega.getDtAgendamento(), JTFormatUtils.MEDIUM);
                        alerts.add(new BusinessException("LMS-05324", new Object[]{dataAgendamento}));
                    } else if (agendamentoEntrega.getDtAgendamento().compareTo(JTDateTimeUtils.getDataAtual()) < 0) {
						/* Necessita reagendamento */
                        exceptions.add(new BusinessException("LMS-05045"));
                    }
                } else {
					/*
					 * Verifica se cliente Remetente do Documento em questão
					 * possui necessidade de Agendamento e se o Documento já foi
					 * agendado.
					 */
                    Boolean blAgendamentoPessoaFisicaRemetente = doctoServico.getClienteByIdClienteRemetente()
                            .getBlAgendamentoPessoaFisica();
                    Boolean blAgendamentoPessoaJuridicaRemetente = doctoServico.getClienteByIdClienteRemetente()
                            .getBlAgendamentoPessoaJuridica();
                    String tpPessoaDestinatario = doctoServico.getClienteByIdClienteDestinatario().getPessoa()
                            .getTpPessoa().getValue();

                    if ((blAgendamentoPessoaFisicaRemetente && "F".equals(tpPessoaDestinatario))
                            || (blAgendamentoPessoaJuridicaRemetente && "J".equals(tpPessoaDestinatario))) {
                        exceptions.add(new BusinessException("LMS-05043"));
                    }
                }
            }
        }

        String nrConhecimento = doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " "
                + StringUtils.leftPad(String.valueOf(volume.getNrConhecimento()), 7, "0");

        return this.generateMessageList(exceptions, confirmations, alerts, nrConhecimento);
    }

    private void validaTpAbrangencia(List<BusinessException> exceptions, String tpDocumentoServico, String tpAbrangencia) {
        if ("N".equals(tpAbrangencia)) {
            if (!"CTR".equals(tpDocumentoServico) && !"MDA".equals(tpDocumentoServico) && !"CTE".equals(tpDocumentoServico) && !"NTE".equals(tpDocumentoServico)) {
                exceptions.add(new BusinessException(ConsErro.DOCTO_SERVICO_INVALIDO));
            }
        } else if ("I".equals(tpAbrangencia) && !"CRT".equals(tpDocumentoServico) && !"CTE".equals(tpDocumentoServico)) {
            exceptions.add(new BusinessException(ConsErro.DOCTO_SERVICO_INVALIDO));
        }
    }

    private void validaDocumentoBloqueado(List<BusinessException> exceptions, DoctoServico doctoServico, Long idDoctoServico, String tpDocumentoServico) {
        if (!"RRE".equals(tpDocumentoServico) && doctoServico.getBlBloqueado()) {
            Boolean hasOcorrenciaBloqueioEntrega = ocorrenciaDoctoServicoService.findHasOcorrenciaSemLiberacaoBloqueioDoctoServicoByCdOcorrencia(doctoServico.getIdDoctoServico()
                    , new Short[]{ConstantesExpedicao.CD_OCORRENCIA_BLOQUEIO_AGENDAMENTO});
            if (hasOcorrenciaBloqueioEntrega) {
                OcorrenciaPendencia ocorrenciaPendencia = ocorrenciaPendenciaService.findByCodigoOcorrencia(ConstantesExpedicao.CD_OCORRENCIA_LIBERACAO_BLOQUEIO_AGENDAMENTO);
                ocorrenciaDoctoServicoService.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(idDoctoServico, ocorrenciaPendencia.getIdOcorrenciaPendencia(), null, null);
            } else {
                exceptions.add(new BusinessException(ConsErro.DOCUMENTO_BLOQUEADO));
            }
        }
    }

    private void validateRotaEntrega(RotaColetaEntrega rotaColetaEntrega, DoctoServico doctoServico, List<BusinessException> confirmations) {
		
		/* Se existir um awb para o docto na filial da sessão, não valida rota.*/
        Awb awb = awbService.findByIdDoctoServicoAndFilialOrigem(doctoServico.getIdDoctoServico(), SessionUtils.getFilialSessao().getIdFilial());

        if (awb != null) {
            return;
        }
		
		/* Busca a rota de coleta/entrega do docto de serviço */
        Long idRotaColetaEntrega = null;
        if (doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaReal() != null) {
            idRotaColetaEntrega = doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaReal()
                    .getIdRotaColetaEntrega();
        } else if (doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaSugerid() != null) {
            idRotaColetaEntrega = doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaSugerid()
                    .getIdRotaColetaEntrega();
        }

        if (rotaColetaEntrega != null && idRotaColetaEntrega != null
                && !idRotaColetaEntrega.equals(rotaColetaEntrega.getIdRotaColetaEntrega())) {
				/* Se for o primeiro volume do manifesto,
				   não pode necessitar de confirmação */
            confirmations.add(new BusinessException("LMS-05047"));
        }
    }

    /*
     * Executa todas as validações pertinentes ao documento de serviço, controle
     * carga e carregamento
     */
    private List<Map<String, Object>> validateDoctoServico(Conhecimento conhecimento, VolumeNotaFiscal volume, ControleCarga controleCarga, String tpManifesto, Long idFilialDesvio, String tpScan) {
        List<BusinessException> exceptions = new ArrayList<BusinessException>();
        List<BusinessException> confirmations = new ArrayList<BusinessException>();
        List<BusinessException> alerts = new ArrayList<BusinessException>();

        DoctoServico doctoServico = conhecimento;

        // LMS-7350
        if (null != conhecimento.getLocalizacaoMercadoria() &&
                !validaLocalizacaoMercadoriaCarregamento(volume, tpScan, conhecimento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria(), controleCarga.getTpControleCarga().getValue())) {
            // Gera evento de volume lido
            final String cdEventoVolumeLido = parametroGeralService.findSimpleConteudoByNomeParametro("CD_EVENTO_VOLUME_LIDO");
            eventoVolumeService.generateEventoVolume(volume, Short.valueOf(cdEventoVolumeLido), tpScan);

            exceptions.add(new BusinessException("LMS-45207", new Object[]{conhecimento.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria()}));
        }

        if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
            controleCarga.getMeioTransporteByIdSemiRebocado();
        } else {
            controleCarga.getMeioTransporteByIdTransportado();
        }

        Long idDoctoServico = doctoServico.getIdDoctoServico();
        Long idControleCarga = controleCarga.getIdControleCarga();
        Long idFilialDestinoDocto = doctoServico.getFilialByIdFilialDestino().getIdFilial();
        String tpDocumentoServico = doctoServico.getTpDocumentoServico().getValue();
        String tpAbrangencia = "N";
        Long idFluxoFilial = null;
        if (doctoServico.getFluxoFilial() != null) {
            idFluxoFilial = doctoServico.getFluxoFilial().getIdFluxoFilial();
        }
        Filial filial = doctoServico.getFilialLocalizacao();
        if (filial == null) {
            filial = doctoServico.getFilialByIdFilialOrigem();

        }
		
		/* Seta ids das filiais envolvidas */
        Long idFilialOrigem = SessionUtils.getFilialSessao().getIdFilial();
        Long idFilialDestinoManifesto = null;
        if ("E".equals(tpManifesto)) {
			/*
			 * Se for manifesto de entrega, filial destino recebe a filial do
			 * usuário logado
			 */
            idFilialDestinoManifesto = SessionUtils.getFilialSessao().getIdFilial();
            Boolean validaDocumento = false;
            validaDocumento = preManifestoDocumentoService.validateDocumentoServicoManifesto(idFilialDestinoManifesto, tpManifesto, conhecimento.getIdDoctoServico());
            if (!validaDocumento) {
                exceptions.add(new BusinessException(ConsErro.DOC_N_PODE_SER_ENTREGUE_UNI_PARM_P_NAO_ENT_SEM_DESTINO_P_MESMA));
            }
        } else {
            List<Manifesto> manifestos = manifestoService.findManifestoByIdControleCarga(controleCarga.getIdControleCarga(), filial.getIdFilial(),
                    null, tpManifesto);
            Boolean validaDocumento = false;
            if (!manifestos.isEmpty()) {
                validaManifestos:
                for (Manifesto manifesto : manifestos) {
                    validaDocumento = preManifestoDocumentoService.validateDocumentoServicoManifesto(
                            manifesto.getFilialByIdFilialDestino().getIdFilial(), tpManifesto, conhecimento.getIdDoctoServico());
                    if (validaDocumento) {
                        break validaManifestos;
                    }
                }
                if (!validaDocumento) {
                    exceptions.add(new BusinessException(ConsErro.DOC_N_PODE_SER_ENTREGUE_UNI_PARM_P_NAO_ENT_SEM_DESTINO_P_MESMA));
                }
            }
            idFilialDestinoManifesto = this.findIdFilialDestinoManifesto(controleCarga.getRota().getIdRota(), idFluxoFilial, idFilialOrigem, idFilialDestinoDocto);
        }
		
		/* Verifica em qual manifesto deve estar o controle de carga */
        if (idFilialDestinoManifesto == null) {
            if (controleCarga.getIdControleCarga() != null) {
                confirmations.add(new BusinessException("LMS-05113"));
            } else {
                exceptions.add(new BusinessException("LMS-45044"));
            }
        }


        List<Manifesto> manifestos = null;
        if (idControleCarga != null && idFilialDestinoManifesto != null) {
            manifestos = manifestoService.findManifestosByTrecho(tpManifesto, "EC", idControleCarga, idFilialOrigem, idFilialDestinoManifesto);
        }
		
		/*
		 * Se não existe manifesto, e a filial destino é diferente da filial
		 * destino do documento de serviço (ou seja, desvio), não permite criar
		 */
        validaPrimeiroVolumeIniciarPremanifestoExistirCTRCStatusFilial(exceptions, doctoServico, filial, manifestos);
			
		/* Verifica se o documento está bloqueado quando tipo <> RRE */
        validaDocumentoBloqueado(exceptions, doctoServico, idDoctoServico, tpDocumentoServico);
		
		/* Verifica status do conhecimento */
        String tpSituacaoConhecimento = conhecimento.getTpSituacaoConhecimento().getValue();
		/* Se status for diferente de emitido, ou documento não emitidos dispara erro  */
        if ((!"E".equals(tpSituacaoConhecimento)) && (!"P".equals(tpSituacaoConhecimento))) {
            exceptions.add(new BusinessException("LMS-45055"));
        } else if (("P".equals(tpSituacaoConhecimento)) && (!SessionUtils.getFilialSessao().getBlSorter())) {
            throw new BusinessException("LMS-45092");
        }
		/*
		 * Verifica se o documento está associado a uma solicitação de retirada
		 * para poder ser incluido em um manifesto de Viagem ou Entrega, caso a
		 * filial de retirada for igual da filial localização do documento, gera
		 * exceção.
		 */
        List listSolicitacaoRetirada = solicitacaoRetiradaService.findSolicitacaoRetiradaByIdDoctoServico(doctoServico.getIdDoctoServico());
        if (!listSolicitacaoRetirada.isEmpty()) {
            for (Iterator iter = listSolicitacaoRetirada.iterator(); iter.hasNext(); ) {
                SolicitacaoRetirada solicitacaoRetirada = (SolicitacaoRetirada) iter.next();

                if (solicitacaoRetirada.getFilialRetirada().getIdFilial().equals(doctoServico.getFilialLocalizacao().getIdFilial())
                        && "A".equals(solicitacaoRetirada.getTpSituacao().getValue())) {
                    exceptions.add(new BusinessException("LMS-05119"));
                }
            }
        }

        if ("V".equals(tpManifesto)) {
            if ("NFT".equals(tpDocumentoServico) || "RRE".equals(tpDocumentoServico)) {
                exceptions.add(new BusinessException(ConsErro.DOCTO_SERVICO_INVALIDO));
            }

            if ("NTE".equals(tpDocumentoServico)) {
                Municipio municipioFilialOrigem = municipioService.findMunicipioByFilial(idFilialOrigem);
                Municipio municipioFilialDestino = municipioService.findMunicipioByFilial(idFilialDestinoManifesto);

                if (!municipioFilialOrigem.getIdMunicipio().equals(municipioFilialDestino.getIdMunicipio())
                        || !doctoServico.getFilialByIdFilialOrigem().getIdFilial().equals(idFilialOrigem)) {
                    throw new BusinessException(ConsErro.DOCTO_SERVICO_INVALIDO);
                }
            }

            validaTpAbrangencia(exceptions, tpDocumentoServico, tpAbrangencia);

        } else {
			/*
			 * Verifica se a rota de entrega do documento é diferente da rota de
			 * entrega do Controle de Carga no Manifesto em questão e
			 * se existe um awb para o docto na filial da sessão (então não valida)
			 */
            this.validateRotaEntrega(controleCarga.getRotaColetaEntrega(), doctoServico, confirmations);

			/*
			 * Verifica se o Documento é do tipo 'RRE' e permite incluir no
			 * manifesto somente se for vinculada a uma MIR do tipo
			 * 'Administrativo para entrega'.
			 */
            if ("RRE".equals(doctoServico.getTpDocumentoServico().getValue())) {
                DocumentoMir documentoMir = documentoMirService.findDocumentoMirByIdReciboReembolso(idDoctoServico, "AE");

                if (documentoMir == null) {
					/*
					 * Recibo necessita estar vinculado a uma MIR do tipo
					 * administrativo para entrega.
					 */
                    exceptions.add(new BusinessException("LMS-05046"));
                }
            } else {
				/* Verifica se o documento possui agendamento Ativo */
                AgendamentoDoctoServico agendamentoDoctoServico = null;

                List listAgendamentoDoctoServico = agendamentoDoctoServicoService.findAgendamentoByIdDoctoServico(idDoctoServico);
                for (Iterator iter = listAgendamentoDoctoServico.iterator(); iter.hasNext(); ) {
                    AgendamentoDoctoServico agendamento = (AgendamentoDoctoServico) iter.next();

                    if ("A".equals(agendamento.getTpSituacao().getValue()) && "A".equals(agendamento.getAgendamentoEntrega().getTpSituacaoAgendamento().getValue())) {
                        agendamentoDoctoServico = agendamento;
                        break;
                    }
                }

                if (agendamentoDoctoServico != null) {
                    AgendamentoEntrega agendamentoEntrega = agendamentoDoctoServico.getAgendamentoEntrega();
					/*
					 * Testa se data do Agendamento é maior ou menos que a data
					 * atual
					 */
                    if (agendamentoEntrega.getDtAgendamento().compareTo(JTDateTimeUtils.getDataAtual()) > 0) {
						/* Agendado para dia */
                        String dataAgendamento = JTFormatUtils.format(agendamentoEntrega.getDtAgendamento(), JTFormatUtils.MEDIUM);
                        alerts.add(new BusinessException("LMS-05324", new Object[]{dataAgendamento}));
                    } else if (agendamentoEntrega.getDtAgendamento().compareTo(JTDateTimeUtils.getDataAtual()) < 0) {
						/* Necessita reagendamento */
                        exceptions.add(new BusinessException("LMS-05045"));
                    } else {
                        exceptions.remove(new BusinessException(ConsErro.DOCUMENTO_BLOQUEADO));
                        OcorrenciaDoctoServico ocorrenciaDoctoServico = getOcorrenciaDoctoServicoService().findOcorrenciaDoctoServicoEmAbertoByIdDoctoServico(idDoctoServico);
                        if (ocorrenciaDoctoServico != null && Short.valueOf((short) 203).equals(ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio().getCdOcorrencia())) {

                            OcorrenciaPendencia ocorrenciaPendenciaLiberacao = getOcorrenciaPendenciaService().findByCodigoOcorrencia((short) 204);
                            TypedFlatMap typedFlatMap = new TypedFlatMap();
                            typedFlatMap.put("doctoServico.idDoctoServico", doctoServico.getIdDoctoServico());
                            typedFlatMap.put("ocorrenciaPendencia.blApreensao", ocorrenciaPendenciaLiberacao.getBlApreensao());
                            typedFlatMap.put("ocorrenciaPendencia.idOcorrenciaPendencia", ocorrenciaPendenciaLiberacao.getIdOcorrenciaPendencia());
                            typedFlatMap.put("ocorrenciaPendencia.evento.idEvento", ocorrenciaPendenciaLiberacao.getEvento().getIdEvento());
                            typedFlatMap.put("ocorrenciaPendencia.tpOcorrencia", ocorrenciaPendenciaLiberacao.getTpOcorrencia().getValue());
                            getOcorrenciaDoctoServicoService().executeRegistrarOcorrenciaDoctoServico(typedFlatMap);
                        }
                    }


                } else {
					/*
					 * Verifica se cliente Remetente do Documento em questão
					 * possui necessidade de Agendamento e se o Documento já foi
					 * agendado.
					 */
                    Boolean blAgendamentoPessoaFisicaRemetente = doctoServico.getClienteByIdClienteRemetente().getBlAgendamentoPessoaFisica();
                    Boolean blAgendamentoPessoaJuridicaRemetente = doctoServico.getClienteByIdClienteRemetente().getBlAgendamentoPessoaJuridica();
                    String tpPessoaDestinatario = doctoServico.getClienteByIdClienteDestinatario().getPessoa().getTpPessoa().getValue();

                    if ((blAgendamentoPessoaFisicaRemetente && "F".equals(tpPessoaDestinatario))
                            || (blAgendamentoPessoaJuridicaRemetente && "J".equals(tpPessoaDestinatario))) {
                        exceptions.add(new BusinessException("LMS-05043"));
                    }
                }
            }
        }

        String nrConhecimento = doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(String.valueOf(volume.getNrConhecimento()), 7, "0");

        return this.generateMessageList(exceptions, confirmations, alerts, nrConhecimento);
    }

    private void validaPrimeiroVolumeIniciarPremanifestoExistirCTRCStatusFilial(List<BusinessException> exceptions, DoctoServico doctoServico, Filial filial, List<Manifesto> manifestos) {
        if (manifestos == null || manifestos.isEmpty()) {
			/* Se volume estiver sendo carregado sem o documento de serviço não permite criar o manifesto se ele não existir */

            short param = 6;
            Evento evento = eventoService.findByCdEvento(param);
            LocalizacaoMercadoria localizacaoMercadoria = doctoServico.getLocalizacaoMercadoria();
            if (localizacaoMercadoria == null || localizacaoMercadoria.getCdLocalizacaoMercadoria() == null) {
                evento.getLocalizacaoMercadoria();
            }

            if (!filial.getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
                exceptions.add(new BusinessException(ConsErro.PRIM_VOL_P_PREMANIF_OBRIG_EXIST_CTRC_STAT_FIL));
            }
        }
    }

    /*
     * Executa todas as validações pertinentes ao volume, controle carga e
     * carregamento
     */
    private List<Map<String, Object>> validateVolume(VolumeNotaFiscal volume, Conhecimento conhecimento, ControleCarga controleCarga,
                                                     DomainValue tpStatusCarregamento, String tpManifesto, String tpScan) {
        List<BusinessException> exceptions = new ArrayList<BusinessException>();
        List<BusinessException> confirmations = new ArrayList<BusinessException>();

        // LMS-5417
        if (controleCarga.getTpStatusControleCarga() != null &&
                !"GE".equals(controleCarga.getTpStatusControleCarga().getValue()) &&
                !"EC".equals(controleCarga.getTpStatusControleCarga().getValue()) &&
                !"PO".equals(controleCarga.getTpStatusControleCarga().getValue()) &&
                !"CP".equals(controleCarga.getTpStatusControleCarga().getValue())) {
            throw new BusinessException("LMS-05001", new Object[]{
                    controleCarga.getNrControleCarga().toString(), controleCarga.getFilialByIdFilialAtualizaStatus().getSgFilial()});
        }
		
		/* Busca volume no controle de cargas */
        PreManifestoVolume preManifestoVolume = preManifestoVolumeService.findByIdVolumeAndIdControleCarga(volume.getIdVolumeNotaFiscal(), controleCarga.getIdControleCarga());

        if (preManifestoVolume != null) {
			/* Este volume já está carregado. */
            exceptions.add(new BusinessException("LMS-45047"));
        }
		
		/* Valida se volume está unitizado, e não permite carregá-lo caso esteja */
        if (tpScan.equals(ConstantesSim.TP_SCAN_FISICO)) {
            if (volume.getDispositivoUnitizacao() != null) {
                DispositivoUnitizacao dispositivo = volume.getDispositivoUnitizacao();
                if (dispositivo.getDispositivoUnitizacaoPai() != null) {
					/*
					 * Este volume está unitizado em um dispositivo que também
					 * está unitizado
					 */
                    exceptions.add(new BusinessException("LMS-45054"));
                } else {
					/*
					 * Volume unitizado no dispositivo {0} código {1}. Você
					 * deseja forçar a desunitização deste volume?
					 */
                    confirmations.add(new BusinessException("LMS-45008", new Object[]{
                            dispositivo.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao(),
                            dispositivo.getNrIdentificacao()}));
                }
            }
			
			/*
			 * Se está alocado, solicita confirmação para desalocação, caso
			 * alocado
			 */
            if (volume.getMacroZona() != null) {
                MacroZona mz = macroZonaService.findById(volume.getMacroZona().getIdMacroZona());
                confirmations.add(new BusinessException(ConsErro.VOLUME_ALOCADO_MACR_ZONA_X_FORC_DESALOCACAO, new Object[]{mz.getDsMacroZona()}));
            }
        }
		
		/*
		 * Verifica se o carregamento está Em Conferência ou Concluído MWW, se
		 * sim, pergunta se quer reabrir carregamento
		 */
        if (!"I".equals(tpStatusCarregamento.getValue())) {
            exceptions.add(new BusinessException(ConsErro.OPR_INVALIDA_CARREGAMENTO_STATUS_X, new Object[]{tpStatusCarregamento
                    .getDescriptionAsString()}));
        }

        //Pad size 7 está correto?
        String nrConhecimento = conhecimento.getFilialByIdFilialOrigem().getSgFilial() + " "
                + StringUtils.leftPad(String.valueOf(volume.getNrConhecimento()), ConsGeral.CON_PAD_SIZE, "0") + " "
                + volume.getNrSequencia() + "/" + conhecimento.getQtVolumes();

        return this.generateMessageList(exceptions, confirmations, new ArrayList<BusinessException>(), nrConhecimento);
    }

    public List<Map<String, Object>> storeVolumeBlsorter(VolumeNotaFiscal volume, ControleCarga controleCarga, Long idBox,
                                                         String tpManifesto, String tpScan, Long idFilialDesvio, Boolean needValidation, Boolean blsorter) {
		/* Seta variável com data e hora */
        DateTime dataHora = JTDateTimeUtils.getDataHoraAtual();

        TrechoRotaIdaVolta triv = null;
        Integer nrTempoViagem = null;
        DateTime dhPrevisaoSaida = null;
        YearMonthDay dtSaidaRota = null;

        if (ConstantesExpedicao.TP_VOLUME_MESTRE.equals(volume.getTpVolume())) {
            throw new BusinessException("LMS-45081");
        }

        Conhecimento conhecimento = findConhecimentoAtual(volume);
        if (conhecimento == null) {
            conhecimento = volume.getNotaFiscalConhecimento().getConhecimento();
        }

        conhecimentoService.getConhecimentoDAO().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().lock(conhecimento, LockMode.NONE);

        DoctoServico doctoServico = doctoServicoService.findById(conhecimento.getIdDoctoServico());
		
        String bloqFluxoSubcontratacao = (String)configuracoesFacade.getValorParametro(
                SessionUtils.getFilialSessao().getIdFilial(), 
                "BL_BLOQ_DOC_SUB");
        if("S".equalsIgnoreCase(bloqFluxoSubcontratacao)) {
            if(doctoServico!= null && BooleanUtils.isTrue(doctoServico.getBlFluxoSubcontratacao())){
                throw new BusinessException("LMS-45215");
            }
        }
        
        if (ConstantesEntrega.TP_MANIFESTO_ENTREGA.equals(tpManifesto) && 
                conhecimentoService.validateAgendamentoObrigatorioEsemAgendamentoDoctoServicoAtivo(doctoServico.getIdDoctoServico())){
            throw new BusinessException("LMS-05424");       
        }
		
		/*
		 * Busca carregamento aberto para o controle de carga, retorna null se
		 * não existe
		 */
        CarregamentoDescarga carregamento = this.findCarregamentoByControleCarga(controleCarga.getIdControleCarga());

        Manifesto manifestoDocumento = findManifesto(controleCarga, conhecimento);

        boolean isLocalizacaoValida = validateLocalizacaoMercadoria(tpManifesto, conhecimento, true);
		
		/* Executa rotinas de validações */
        if (needValidation) {
            List<Map<String, Object>> messages = this.validateVolume(volume, conhecimento, controleCarga, this
                    .findTpStatusCarregamento(carregamento), tpManifesto, tpScan);
		
			/*
			 * Caso não exista volume carregado para o documento de serviço,
			 * executa validações do documento
			 */
            if (manifestoDocumento == null) {
                messages.addAll(this.validateDoctoServicoConhecimento(conhecimento, volume, controleCarga, tpManifesto, idFilialDesvio, blsorter, tpScan));
            }
			/*
			 * Caso retorne algo da rotina de validação, retorna lista de
			 * mensagens de erro ou confirmação
			 */
            if (messages != null && !messages.isEmpty()) {
                return this.cleanMessageList(messages);
            }
        }
		
		
		/*
		 * Seta a boleana identificando que é o primeiro volume do docto a ser carregado.
		 */
        boolean isFirstVolumeFromDocto = manifestoDocumento == null;
		
		
		/* Caso possível, reabre o carregamento. Caso contrário, lançará exceção */
        if (carregamento != null && !"I".equals(carregamento.getTpStatusOperacao().getValue())) {
            this.storeStatusCarregamento(carregamento, "I");
        }
		
		/* Caso tpScan = SCAN FISICO */
        scanFisicoDesutinizarEDesalocarVolume(volume, tpScan);
		
		/* Se volume estiver extraviado, retorna ele mesmo sem salvar */
        if (volume.getLocalizacaoMercadoria() != null
                && conferirVolumeService.isVolumeExtraviado(volume.getLocalizacaoMercadoria()
                .getCdLocalizacaoMercadoria())) {
            return null;
        }
						
		/* Se controle de carga não foi persistido, persiste */
        if (controleCarga != null && controleCarga.getIdControleCarga() == null) {
            Long idMeioTransporteTransportado = null;
            Long idMeioTransporteSemiRebocado = null;
            Long idRotaColetaEntregaCC = null;
            Long idRota = null;
            Long idSolicitacaoContratacao = null;
            Long idRotaIdaVolta = null;
            Long idProprietario = null;

            if (controleCarga.getMeioTransporteByIdTransportado() != null) {
                idMeioTransporteTransportado = controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte();
                if (idMeioTransporteTransportado != null) {
                    idProprietario = validaECarregaIdProprietario(idMeioTransporteTransportado);
                }
            }

            if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
                idMeioTransporteSemiRebocado = controleCarga.getMeioTransporteByIdSemiRebocado().getIdMeioTransporte();
                if (idMeioTransporteSemiRebocado != null) {
                    meioTransporteService.findById(idMeioTransporteSemiRebocado);
                }
            }
            if (controleCarga.getRotaColetaEntrega() != null) {
                idRotaColetaEntregaCC = controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega();
            }
            if (controleCarga.getRota() != null) {
                idRota = controleCarga.getRota().getIdRota();
            }
            if (controleCarga.getRotaIdaVolta() != null) {
                idRotaIdaVolta = controleCarga.getRotaIdaVolta().getIdRotaIdaVolta();

                triv = trechoRotaIdaVoltaService.findByTrechoRotaCompleta(idRotaIdaVolta);

                final int horasTempoLimite = ((BigDecimal) configuracoesFacade.getValorParametro("TEMPO_LIMITE_ROTA_DO_DIA")).intValue();
                dtSaidaRota = controleCargaService.validateRotaControleCarga(idRotaIdaVolta, horasTempoLimite);
                dhPrevisaoSaida = triv.getHrSaida() == null ? null : dtSaidaRota.toDateTime(triv.getHrSaida(), JTDateTimeUtils.getUserDtz());

                if (triv != null) {
                    nrTempoViagem = triv.getNrTempoViagem();
                }

            }
            if (controleCarga.getSolicitacaoContratacao() != null) {
                idSolicitacaoContratacao = controleCarga.getSolicitacaoContratacao().getIdSolicitacaoContratacao();
            }

            controleCargaService.generateControleCarga(controleCarga, idMeioTransporteTransportado,
                    idMeioTransporteSemiRebocado, null, idProprietario, null, null, idRotaColetaEntregaCC, idRotaIdaVolta,
                    idRota, idSolicitacaoContratacao, controleCarga.getTpControleCarga(), controleCarga
                            .getTpRotaViagem(), null, dhPrevisaoSaida, dataHora, nrTempoViagem, false, true, null);

            //Cria o Adiantamento Trecho para o Controle de Carga Jira LMS-762
            if (controleCarga.getSolicitacaoContratacao() != null) {

                criaAdiantamentoParaControleCarga(controleCarga);
            }

			/* Busca os postos de passagem do controle de carga */
            List<PostoPassagemCc> listaPostoPassagemCc = null;

            if ("V".equals(tpManifesto)) {
				/* Busca os trecho a partir da rota e grava-os  */
                List<ControleTrecho> controlesTrecho = controleTrechoService.findTrechosByTrechosRota(controleCarga
                        .getIdControleCarga(), idRotaIdaVolta, idRota, dhPrevisaoSaida, null, null, null);
                controleTrechoService.storeAll(controlesTrecho);
				
				/*
				 * Gera registros para a tabela a partir da Rota ou da Rota Ida
				 * Volta
				 */
                filialRotaCcService.generateFilialRotaCcByRotaOrRotaIdaVolta(controleCarga.getIdControleCarga(),
                        idRota, idRotaIdaVolta);

            }
			
			/* Se foi informado um meio de transporte que NÃO é um semi reboque */
            if (idMeioTransporteTransportado != null) {
                if ("V".equals(tpManifesto)) {
					/* Busca filiais que fazem parte da rota */
                    List<FilialRota> filiaisRota = filialRotaService.findFiliaisRota(idRotaIdaVolta, idRota);

					/* Busca os postos de passagem do controle de carga */
                    listaPostoPassagemCc = postoPassagemCcService.findPostoPassagemCcByViagem(controleCarga
                                    .getIdControleCarga(), idMeioTransporteTransportado, idMeioTransporteSemiRebocado,
                            filiaisRota, Boolean.TRUE, null);
                } else {
					/* Busca os postos de passagem do controle de carga */
                    listaPostoPassagemCc = postoPassagemCcService.findPostoPassagemCcByColetaEntrega(controleCarga
                                    .getIdControleCarga(), idMeioTransporteTransportado, idMeioTransporteSemiRebocado,
                            idRotaColetaEntregaCC, null);
                }

				/* Grava postos de passagem */
                postoPassagemCcService.storeAll(listaPostoPassagemCc);
            }
        }


        Long idFilialLocalizacaoConhecimento = conhecimento.getFilialLocalizacao() != null ? conhecimento.getFilialLocalizacao().getIdFilial() : conhecimento.getFilialOrigem().getIdFilial();
        if ("E".equals(tpManifesto)) {
            List<Short> localizacoesMercadoria = new ArrayList<Short>();
            localizacoesMercadoria.addAll(Arrays.asList((short) 24, (short) 33, (short) 34, (short) 35, (short) 43, (short) 1));

            if (!SessionUtils.getFilialSessao().getIdFilial().equals(idFilialLocalizacaoConhecimento)) {
                throw new BusinessException(ConsErro.VOLUME_N_CARREGADO_N_ENCONTRADO_FILIAL);
            }

            PreManifestoDocumento manifestoEntregaDocumento = null;
            if (manifestoDocumento != null) {
                manifestoEntregaDocumento = preManifestoDocumentoService.findPreManifestoDocumentoByIdManifestoDocto(manifestoDocumento.getIdManifesto(), conhecimento.getIdDoctoServico());
            }

            if (!localizacoesMercadoria.contains(conhecimento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria()) &&
                    manifestoEntregaDocumento == null) {
                throw new BusinessException(ConsErro.LOC_DOC_SERV_NAO_PERMITE_CARREGAMENTO);
            }

            if (!volume.getLocalizacaoFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
                volume.setLocalizacaoFilial(SessionUtils.getFilialSessao());
            }
        }

        Long idControleCarga = controleCarga.getIdControleCarga();
				
		/* Valida se controle de carga pode receber documentos novos */
        controleCargaService.validateControleCargaByInclusaoDocumentos(controleCarga);
		
		/* Gera o evento de início de carregamento caso não exista */
        Long idCarregamentoDescarga = null;
        if (carregamento == null) {
			/*
			 * Monta a equipeOperacao a ser utilizada, baseado na equipe buscada
			 * através do parâmetro geral ID_EQUIPE_MWW
			 */
            EquipeOperacao equipeOperacao = new EquipeOperacao();
            equipeOperacao.setDhInicioOperacao(dataHora);
            equipeOperacao.setControleCarga(controleCarga);
            equipeOperacao.setEquipe(getEquipeOperacaoService().storeEquipeCarregamento(controleCarga));
			
			/* Grava o início do carregamento e seus vínculos */
            idCarregamentoDescarga = (Long) carregamentoDescargaService.storeInicioCarregamento(controleCarga, idBox,
                    equipeOperacao, null, dataHora);
        } else {
            idCarregamentoDescarga = carregamento.getIdCarregamentoDescarga();
        }

        Manifesto manifesto = null;
		
		/*
		 * Se o documento de serviço já foi incluído para o manifesto, usa o
		 * mesmo manifesto
		 */
        if (!isFirstVolumeFromDocto) {
            manifesto = manifestoDocumento;

            //- Se a localização filial do Volume (volume_nota_fiscal.id_localizacao_filial) for diferente da FILIAL LOGADA e
            //filial localização do Documento de Serviço (docto_servico.id_filial_localizacao) for igual da FILIAL LOGADA.
            //Atualizar a filial do volume lido (volume_nota_fiscal.id_localizacao_filial) para FILIAL LOGADA.
            if ("E".equals(tpManifesto)
                    && !volume.getLocalizacaoFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial()) &&
                    conhecimento.getFilialLocalizacao().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
                volume.setLocalizacaoFilial(SessionUtils.getFilialSessao());
            }
        } else {
			/* Seta ids das filiais envolvidas */
            Long idFilialDestinoDocto = doctoServico.getFilialByIdFilialDestino().getIdFilial();
            Long idFilialOrigemManifesto = SessionUtils.getFilialSessao().getIdFilial();
            Long idFilialDestinoManifesto = null;
            Long idFluxoDoctoServico = null;

            Filial filialSubstitutaDoctoServico = substAtendimentoFilialService.findFilialDestinoDoctoServico(
                    conhecimento.getIdDoctoServico(), null, null, null);
            if (filialSubstitutaDoctoServico != null) {
                idFilialDestinoDocto = filialSubstitutaDoctoServico.getIdFilial();
            }

            if (doctoServico.getFluxoFilial() != null) {
                idFluxoDoctoServico = doctoServico.getFluxoFilial().getIdFluxoFilial();
            }

            if ("E".equals(tpManifesto)) {
				/*
				 * Se for manifesto de entrega, filial destino recebe a filial
				 * do usuário logado
				 */
                idFilialDestinoManifesto = SessionUtils.getFilialSessao().getIdFilial();
            } else {
                if (idFilialDesvio != null) {
					/*
					 * Se foi passada a filial de desvio, utiliza ela como
					 * destino do manifesto
					 */
                    idFilialDestinoManifesto = idFilialDesvio;
                } else {
					/* senão recebe a filial de destino do docto de serviço */
                    idFilialDestinoManifesto = this.findIdFilialDestinoManifesto(controleCarga.getRota().getIdRota(),
                            idFluxoDoctoServico, idFilialOrigemManifesto, idFilialDestinoDocto);
                }
            }

            List<Manifesto> manifestos = manifestoService.findManifestosByTrecho(tpManifesto, "EC",
                    idControleCarga, idFilialOrigemManifesto, idFilialDestinoManifesto);

			/*
			 * Se não existe manifesto em carregamento que pode ser aproveitado,
			 * cria
			 */
            if ((manifestos == null || manifestos.isEmpty()) && isFirstVolumeFromDocto) {
                manifesto = new Manifesto();
                manifesto.setBlBloqueado(false);
                manifesto.setPsTotalManifesto(BigDecimalUtils.ZERO);
                manifesto.setPsTotalAforadoManifesto(BigDecimalUtils.ZERO);
                manifesto.setVlTotalManifesto(BigDecimalUtils.ZERO);
                manifesto.setMoeda(SessionUtils.getMoedaSessao());
                manifesto.setFilialByIdFilialOrigem(SessionUtils.getFilialSessao());
                manifesto.setTpStatusManifesto(new DomainValue("EC"));
                manifesto.setTpManifesto(new DomainValue(tpManifesto));
                manifesto.setTpAbrangencia(new DomainValue("N"));
                manifesto.setNrPreManifesto(configuracoesFacade.incrementaParametroSequencial(SessionUtils
                        .getFilialSessao().getIdFilial(), ConsManifesto.VL_NR_PRE_MANIFESTO, true));

                manifesto.setDhGeracaoPreManifesto(dataHora);

                // LMS-5184
                List<Manifesto> listaManifestoByControleCarga = manifestoService.findManifestosByControleCarga(controleCarga);

                if (listaManifestoByControleCarga != null && !listaManifestoByControleCarga.isEmpty()) {
                    manifesto.setTpModal(listaManifestoByControleCarga.get(0).getTpModal());
                } else {
                    manifesto.setTpModal(new DomainValue("R"));
                }

                manifesto.setControleCarga(controleCarga);
			
				/*
				 * Se for manifesto de viagem, verifica se filial destino está
				 * sendo substituída
				 */
                adicionaDadosManifestoPorTpManifesto(tpManifesto, manifesto, idFilialDestinoManifesto);

                ControleTrecho controleTrecho = controleTrechoService.findControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(
                        idControleCarga, manifesto.getFilialByIdFilialOrigem().getIdFilial(), manifesto.getFilialByIdFilialDestino().getIdFilial());

                manifesto.setControleTrecho(controleTrecho);

                manifestoService.storeBasic(manifesto);
			
				/*
				 * Gera o registro de Carregamento do pre manifesto que foi
				 * iniciado
				 */
                carregamentoDescargaService.storeIniciarCarregamentoPreManifesto(manifesto.getIdManifesto(),
                        idCarregamentoDescarga, dataHora);
            } else {
                manifesto = manifestos.get(0);
            }
        }
		
		/* Verifica se documento de serviço já foi incluído no manifesto */
        PreManifestoDocumento preManifestoDocto = preManifestoDocumentoService
                .findPreManifestoDocumentoByIdManifestoDocto(manifesto.getIdManifesto(), doctoServico
                        .getIdDoctoServico());

        //Monta o PreManifestoVolume
        PreManifestoVolume preManifestoVolume = new PreManifestoVolume();
        preManifestoVolume.setDoctoServico(doctoServico);
        preManifestoVolume.setManifesto(manifesto);
        preManifestoVolume.setVolumeNotaFiscal(volume);
        preManifestoVolume.setTpScan(new DomainValue(tpScan));


        Filial filial = doctoServico.getFilialLocalizacao();
        if (filial == null) {
            filial = doctoServico.getFilialByIdFilialOrigem();
        }
		
		/*
		 * Verifica se o documento está na filial, isso significa que o volume irá viajar com seu docto de serviço
		 */
        if (filial.getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
			/*
			 * Verifica se é o primeiro volume do docto a ser incluso no manifesto.
			 * Então atualiza os totais do manifesto, somando valores do docto de
			 * serviço.
			 */
            if (isFirstVolumeFromDocto) {
				/*
				 * Soma os valores do docto de serviço ao manifesto.
				 */
                this.addValoresDoctoToManifesto(doctoServico, manifesto);
            }
					
			/*
			 * Se documento ainda não está naquele manifesto, inclui registro em
			 * PreManifestoDocumento
			 */
            if (isLocalizacaoValida && preManifestoDocto == null) {
                /* Se o volume carregado possuir a mesma localização do conhecimento, então o conhecimento
				 * será vinculado ao manifesto.
				 */
                short param = 6;
                Evento evento = eventoService.findByCdEvento(param);
                LocalizacaoMercadoria localizacaoMercadoria = doctoServico.getLocalizacaoMercadoria();
                if (localizacaoMercadoria == null || localizacaoMercadoria.getCdLocalizacaoMercadoria() == null) {
                    localizacaoMercadoria = evento.getLocalizacaoMercadoria();
                }

				/* Cria PreManifestoDocto */
                preManifestoDocto = new PreManifestoDocumento();
                preManifestoDocto.setDoctoServico(conhecimento);
                preManifestoDocto.setAwb(awbService.findPreAwbByIdDoctoServicoAndFilialOrigem(conhecimento.getIdDoctoServico(), SessionUtils.getFilialSessao().getIdFilial()));
                preManifestoDocto.setManifesto(manifesto);
                Integer ordem = preManifestoDocumentoService.findMaxNrOrdemByIdManifesto(manifesto.getIdManifesto());
                preManifestoDocto.setNrOrdem(++ordem);
                preManifestoDocumentoService.store(preManifestoDocto);

				/* Gera evento no documento de serviço de carregamento */
                String nrPreManifesto = preManifestoDocto.getManifesto().getFilialByIdFilialOrigem().getSgFilial()
                        + " "
                        + StringUtils.leftPad(preManifestoDocto.getManifesto().getNrPreManifesto().toString(), 8, '0');

                this.storeEventoCarregamentoDoctoServico(doctoServico.getIdDoctoServico(), tpManifesto, nrPreManifesto, dataHora);
            }
			/* Seta o PreManifestoDocumento do PreManifestoVolume */
            preManifestoVolume.setPreManifestoDocumento(preManifestoDocto);
        }
		
		/* Atualiza registros para contabilizar volumes carregados */
        if (carregamento == null) {
            carregamento = carregamentoDescargaService.findById(idCarregamentoDescarga);
        }
        this.storeCarregamentoDescargaVolume(carregamento, volume, volume.getDispositivoUnitizacao());
		
		/* Store do Volume no pré manifesto */
        preManifestoVolumeService.store(preManifestoVolume);
		
		/* Grava o volume */
        volumeNotaFiscalService.store(volume);

        if (manifesto != null && "V".equals(tpManifesto)) {
			/* Se o volume já estava vinculado a outro pré-manifesto de viagem */
            verificarVolumeVinculadoPreManifesto(manifesto, volume);
			
			/* Se o volume já estava vinculado a outro manifesto de viagem */
            verificarVolumeVinculadoManifesto(manifesto, volume);
        }
		
		/*
		 * Gera o evento de Em Carregamento para o volume. 24 - Entrega, 25
		 * Viagem
		 */
        this.storeEventoCarregamentoVolume(volume, tpManifesto, tpScan);

        return null;
    }

    private void adicionaDadosManifestoPorTpManifesto(String tpManifesto, Manifesto manifesto, Long idFilialDestinoManifesto) {
        if ("V".equals(tpManifesto)) {
            manifesto.setTpManifestoViagem(new DomainValue("VI"));
            Filial filialDestinoManifesto = new Filial();
            filialDestinoManifesto.setIdFilial(idFilialDestinoManifesto);
            manifesto.setFilialByIdFilialDestino(filialDestinoManifesto);
        } else if ("E".equals(tpManifesto)) {
            manifesto.setTpManifestoEntrega(new DomainValue("EN"));
            manifesto.setFilialByIdFilialDestino(SessionUtils.getFilialSessao());
        }
    }

    private Long validaECarregaIdProprietario(Long idMeioTransporteTransportado ) {
        Long idProprietario = null;
        if (meioTransporteService.findMeioTransporteIsCavaloTrator(idMeioTransporteTransportado)) {
            throw new BusinessException(ConsErro.NAO_POSSIVEL_INICIAR_CARREGAMENTO_COM_CAVALO_TRATOR); //Não pode iniciar Carregamento com um Cavalo-Trator
        }
        meioTransporteService.findById(idMeioTransporteTransportado);

        Map mapResultado = getMeioTranspProprietarioService().findProprietarioByMeioTransporte(idMeioTransporteTransportado);
        if (mapResultado != null) {
            Map mapProprietario = (Map) mapResultado.get("proprietario");
            if (mapProprietario != null) {
                idProprietario = Long.parseLong(mapProprietario.get("idProprietario").toString());
            }
        }
        if (idProprietario == null) {
            throw new BusinessException(ConsErro.MEIO_TRANSPORTE_NAO_PODE_SER_UTILIZADO_SEM_PROPRIETARIO);
        }
        return idProprietario;
    }

    private void scanFisicoDesutinizarEDesalocarVolume(VolumeNotaFiscal volume, String tpScan) {
        if (tpScan.equals(ConstantesSim.TP_SCAN_FISICO)) {
			/* Caso esteja unitizado, desunitiza */
            if (volume.getDispositivoUnitizacao() != null) {
                List<VolumeNotaFiscal> volumesDesunitizacao = new ArrayList<VolumeNotaFiscal>();
                volumesDesunitizacao.add(volume);
                unitizacaoService.storeDesunitizarParcial(volume.getDispositivoUnitizacao()
                        .getIdDispositivoUnitizacao(), volumesDesunitizacao, null);
            }
			/*
			 * Caso o volume esteja alocado, ele é desalocado.
			 */
            if (volume.getMacroZona() != null) {
                macroZonaService.storeDesalocarVolume(volume, tpScan);
            }
        }
    }

    private boolean validateLocalizacaoMercadoria(String tpManifesto, Conhecimento conhecimento, Boolean blSorter) {
        boolean isLocalizacaoValida = false;
        if (blSorter) {
            if ("P".equals(conhecimento.getTpSituacaoConhecimento().getValue()) && conhecimento.getFilialOrigem().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
                isLocalizacaoValida = true;
            } else {
                isLocalizacaoValida = validateLocalizacaoMercadoriaSeDiferenteDeNull(tpManifesto, conhecimento, isLocalizacaoValida);
            }
        } else {
            isLocalizacaoValida = validateLocalizacaoMercadoriaSeDiferenteDeNull(tpManifesto, conhecimento, isLocalizacaoValida);
        }
        return isLocalizacaoValida;
    }

    private boolean validateLocalizacaoMercadoriaSeDiferenteDeNull(String tpManifesto, Conhecimento conhecimento, boolean isLocalizacaoValida) {
        if (conhecimento.getLocalizacaoMercadoria() != null) {
            validaLocalizacaoMercadoriaAguardandoDescarga(tpManifesto, conhecimento.getLocalizacaoMercadoria(), conhecimento.getFilialLocalizacao());
            isLocalizacaoValida = this.validaLocalizacaoMercadoria(tpManifesto, conhecimento.getLocalizacaoMercadoria(), conhecimento.getFilialLocalizacao());
        }
        return isLocalizacaoValida;
    }


    private void atualizarFilialLocalizacaoVolume(VolumeNotaFiscal volumeNotaFiscalManifesto) {
        volumeNotaFiscalManifesto.setLocalizacaoFilial(SessionUtils.getFilialSessao());
        volumeNotaFiscalService.atualizarFilialLocalizacaoVolume(volumeNotaFiscalManifesto);
    }

    public List<Map<String, Object>> storeVolume(VolumeNotaFiscal volume, ControleCarga controleCarga, Long idBox,
                                                 String tpManifesto, String tpScan, Long idFilialDesvio, Boolean needValidation) {
		/* Seta variável com data e hora */

        //Carregar adsmNativeBatchOperations
        AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations = new AdsmNativeBatchSqlOperations(conhecimentoService.getConhecimentoDAO(), ConsGeral.LIMITE_OPERACAO);

        DateTime dataHora = JTDateTimeUtils.getDataHoraAtual();

        TrechoRotaIdaVolta triv = null;
        Integer nrTempoViagem = null;
        DateTime dhPrevisaoSaida = null;
        YearMonthDay dtSaidaRota = null;

        if (ConstantesExpedicao.TP_VOLUME_MESTRE.equals(volume.getTpVolume())) {
            throw new BusinessException("LMS-45081");
        }

        Conhecimento conhecimento = findConhecimentoAtual(volume);
        if (conhecimento == null) {
            conhecimento = conhecimentoService.findById(volume.getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico());
        }

        conhecimentoService.getConhecimentoDAO().getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().lock(conhecimento, LockMode.NONE);

		
        DoctoServico doctoServico = doctoServicoService.findById(conhecimento.getIdDoctoServico());

        String bloqFluxoSubcontratacao = (String)configuracoesFacade.getValorParametro(
                SessionUtils.getFilialSessao().getIdFilial(), 
                "BL_BLOQ_DOC_SUB");
        if("S".equalsIgnoreCase(bloqFluxoSubcontratacao)) {
            if(doctoServico!= null && BooleanUtils.isTrue(doctoServico.getBlFluxoSubcontratacao())){
                throw new BusinessException("LMS-45215");
            }
        }
        
		if (ConstantesEntrega.TP_MANIFESTO_ENTREGA.equals(tpManifesto) && 
				conhecimentoService.validateAgendamentoObrigatorioEsemAgendamentoDoctoServicoAtivo(doctoServico.getIdDoctoServico())){
			throw new BusinessException("LMS-05424");		
		}
		
		/*
		 * Busca carregamento aberto para o controle de carga, retorna null se
		 * não existe
		 */
        CarregamentoDescarga carregamento = this.findCarregamentoByControleCarga(controleCarga.getIdControleCarga());

        Manifesto manifestoDocumento = findManifesto(controleCarga, conhecimento);

        boolean isLocalizacaoValida = validateLocalizacaoMercadoria(tpManifesto, conhecimento, false);
		
		/* Executa rotinas de validações */
        if (needValidation) {
            List<Map<String, Object>> messages = this.validateVolume(volume, conhecimento, controleCarga, this.findTpStatusCarregamento(carregamento), tpManifesto, tpScan);
			/*
			 * Caso não exista volume carregado para o documento de serviço,
			 * executa validações do documento
			 */
            if (manifestoDocumento == null) {
                messages.addAll(this.validateDoctoServico(conhecimento, volume, controleCarga, tpManifesto, idFilialDesvio, tpScan));
            }
			/*
			 * Caso retorne algo da rotina de validação, retorna lista de
			 * mensagens de erro ou confirmação
			 */
            if (messages != null && !messages.isEmpty()) {
                return this.cleanMessageList(messages);
            }
        }
		
		/*
		 * Seta a boleana identificando que é o primeiro volume do docto a ser carregado.
		 */
        boolean isFirstVolumeFromDocto = manifestoDocumento == null;
		
		/* Caso possível, reabre o carregamento. Caso contrário, lançará exceção */
        if (carregamento != null && !"I".equals(carregamento.getTpStatusOperacao().getValue())) {
            this.storeStatusCarregamento(carregamento, "I");
        }
		
		/* Caso tpScan = SCAN FISICO */
        scanFisicoDesutinizarEDesalocarVolume(volume, tpScan);
		
		/* Se volume estiver extraviado, retorna ele mesmo sem salvar */
        if (volume.getLocalizacaoMercadoria() != null
                && conferirVolumeService.isVolumeExtraviado(volume.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())) {
            return null;
        }
						
		/* Se controle de carga não foi persistido, persiste */
        if (controleCarga != null && controleCarga.getIdControleCarga() == null) {
            Long idMeioTransporteTransportado = null;
            Long idMeioTransporteSemiRebocado = controleCarga.getMeioTransporteByIdSemiRebocado() == null ? null : controleCarga.getMeioTransporteByIdSemiRebocado().getIdMeioTransporte();
            Long idRotaColetaEntregaCC = null;
            Long idRota = null;
            Long idSolicitacaoContratacao = null;
            Long idRotaIdaVolta = null;
            Long idProprietario = null;

            if (controleCarga.getMeioTransporteByIdTransportado() != null) {
                idMeioTransporteTransportado = controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte();
                if (idMeioTransporteTransportado != null) {
                    if (meioTransporteService.findMeioTransporteIsCavaloTrator(idMeioTransporteTransportado)) {
                        throw new BusinessException(ConsErro.NAO_POSSIVEL_INICIAR_CARREGAMENTO_COM_CAVALO_TRATOR); //Não pode iniciar Carregamento com um Cavalo-Trator
                    }

                    Map mapResultado = getMeioTranspProprietarioService().findProprietarioByMeioTransporte(idMeioTransporteTransportado);
                    if (mapResultado != null) {
                        Map mapProprietario = (Map) mapResultado.get("proprietario");
                        if (mapProprietario != null) {
                            idProprietario = Long.parseLong(mapProprietario.get("idProprietario").toString());
                        }
                    }
                    if (idProprietario == null) {
                        throw new BusinessException(ConsErro.MEIO_TRANSPORTE_NAO_PODE_SER_UTILIZADO_SEM_PROPRIETARIO);
                    }
                }
            }

            if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
                idMeioTransporteSemiRebocado = controleCarga.getMeioTransporteByIdSemiRebocado().getIdMeioTransporte();
                if (idMeioTransporteSemiRebocado != null) {
                    meioTransporteService.findById(idMeioTransporteSemiRebocado);
                }
            }

            if (controleCarga.getRotaColetaEntrega() != null) {
                idRotaColetaEntregaCC = controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega();
            }

            if (controleCarga.getRota() != null) {
                idRota = controleCarga.getRota().getIdRota();
            }

            if (controleCarga.getRotaIdaVolta() != null) {
                idRotaIdaVolta = controleCarga.getRotaIdaVolta().getIdRotaIdaVolta();

                triv = trechoRotaIdaVoltaService.findByTrechoRotaCompleta(idRotaIdaVolta);

                final int horasTempoLimite = ((BigDecimal) configuracoesFacade.getValorParametro("TEMPO_LIMITE_ROTA_DO_DIA")).intValue();
                dtSaidaRota = controleCargaService.validateRotaControleCarga(idRotaIdaVolta, horasTempoLimite);
                dhPrevisaoSaida = triv.getHrSaida() == null ? null : dtSaidaRota.toDateTime(triv.getHrSaida(), JTDateTimeUtils.getUserDtz());

                if (triv != null) {
                    nrTempoViagem = triv.getNrTempoViagem();
                }
            }

            if (controleCarga.getSolicitacaoContratacao() != null) {
                idSolicitacaoContratacao = controleCarga.getSolicitacaoContratacao().getIdSolicitacaoContratacao();
            }

            controleCargaService.generateControleCarga(controleCarga, idMeioTransporteTransportado,
                    idMeioTransporteSemiRebocado, null, idProprietario, null, null, idRotaColetaEntregaCC, idRotaIdaVolta,
                    idRota, idSolicitacaoContratacao, controleCarga.getTpControleCarga(), controleCarga
                            .getTpRotaViagem(), null, dhPrevisaoSaida, dataHora, nrTempoViagem, false, true, null);

            //Cria o Adiantamento Trecho para o Controle de Carga Jira LMS-762
            if (controleCarga.getSolicitacaoContratacao() != null) {

                criaAdiantamentoParaControleCarga(controleCarga);
            }
			
			/* Busca os postos de passagem do controle de carga */
            List<PostoPassagemCc> listaPostoPassagemCc = null;

            if ("V".equals(tpManifesto)) {
				/* Busca os trecho a partir da rota e grava-os  */
                List<ControleTrecho> controlesTrecho = controleTrechoService.findTrechosByTrechosRota(controleCarga.getIdControleCarga(), idRotaIdaVolta, idRota, dhPrevisaoSaida, null, null, null);
                controleTrechoService.storeAll(controlesTrecho);
				
				/*
				 * Gera registros para a tabela a partir da Rota ou da Rota Ida
				 * Volta
				 */
                filialRotaCcService.generateFilialRotaCcByRotaOrRotaIdaVolta(controleCarga.getIdControleCarga(), idRota, idRotaIdaVolta);

            }
			
			/* Se foi informado um meio de transporte que NÃO é um semi reboque */
            if (idMeioTransporteTransportado != null) {
                if ("V".equals(tpManifesto)) {
					/* Busca filiais que fazem parte da rota */
                    List<FilialRota> filiaisRota = filialRotaService.findFiliaisRota(idRotaIdaVolta, idRota);
				
					/* Busca os postos de passagem do controle de carga */
                    listaPostoPassagemCc = postoPassagemCcService.findPostoPassagemCcByViagem(controleCarga.getIdControleCarga(), idMeioTransporteTransportado, idMeioTransporteSemiRebocado, filiaisRota, Boolean.TRUE, null);
                } else {
					/* Busca os postos de passagem do controle de carga */
                    listaPostoPassagemCc = postoPassagemCcService.findPostoPassagemCcByColetaEntrega(controleCarga.getIdControleCarga(), idMeioTransporteTransportado, idMeioTransporteSemiRebocado, idRotaColetaEntregaCC, null);
                }
				
				/* Grava postos de passagem */
                postoPassagemCcService.storeAll(listaPostoPassagemCc);
            }
        }

        Long idFilialLocalizacaoConhecimento = conhecimento.getFilialLocalizacao() != null ? conhecimento.getFilialLocalizacao().getIdFilial() : conhecimento.getFilialOrigem().getIdFilial();
        if ("E".equals(tpManifesto)) {

            List<Short> localizacoesMercadoria = new ArrayList<Short>();
            localizacoesMercadoria.addAll(Arrays.asList((short) 24, (short) 33, (short) 34, (short) 35, (short) 43, (short) 1));

            PreManifestoDocumento manifestoEntregaDocumento = null;
            if (manifestoDocumento != null) {
                manifestoEntregaDocumento = preManifestoDocumentoService.findPreManifestoDocumentoByIdManifestoDocto(manifestoDocumento.getIdManifesto(), conhecimento.getIdDoctoServico());
            }

            if (!SessionUtils.getFilialSessao().getIdFilial().equals(idFilialLocalizacaoConhecimento)) {
                throw new BusinessException(ConsErro.VOLUME_N_CARREGADO_N_ENCONTRADO_FILIAL);
            }
            if (!localizacoesMercadoria.contains(conhecimento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())
                    && manifestoEntregaDocumento == null) {
                throw new BusinessException(ConsErro.LOC_DOC_SERV_NAO_PERMITE_CARREGAMENTO);
            }

            if (!volume.getLocalizacaoFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
                volume.setLocalizacaoFilial(SessionUtils.getFilialSessao());
            }
        }

        Long idControleCarga = controleCarga.getIdControleCarga();
				
		/* Valida se controle de carga pode receber documentos novos */
        controleCargaService.validateControleCargaByInclusaoDocumentos(controleCarga);
		
		/* Gera o evento de início de carregamento caso não exista */
        Long idCarregamentoDescarga = null;
        if (carregamento == null) {
			/*
			 * Monta a equipeOperacao a ser utilizada, baseado na equipe buscada 
			 * através do parâmetro geral ID_EQUIPE_MWW
			 */
            EquipeOperacao equipeOperacao = new EquipeOperacao();
            equipeOperacao.setDhInicioOperacao(dataHora);
            equipeOperacao.setControleCarga(controleCarga);
            equipeOperacao.setEquipe(getEquipeOperacaoService().storeEquipeCarregamento(controleCarga));
			
			/* Grava o início do carregamento e seus vínculos */
            idCarregamentoDescarga = (Long) carregamentoDescargaService.storeInicioCarregamento(controleCarga, idBox, equipeOperacao, null, dataHora);
        } else {
            idCarregamentoDescarga = carregamento.getIdCarregamentoDescarga();
        }

        Manifesto manifesto = null;
		
		/*
		 * Se o documento de serviço já foi incluído para o manifesto, usa o
		 * mesmo manifesto
		 */
        if (!isFirstVolumeFromDocto) {
            manifesto = manifestoDocumento;
            if ("E".equals(tpManifesto)) {
                if (!idFilialLocalizacaoConhecimento.equals(SessionUtils.getFilialSessao().getIdFilial())) {
                    throw new BusinessException(ConsErro.VOLUME_N_CARREGADO_N_ENCONTRADO_FILIAL);
                }

                if (!volume.getLocalizacaoFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial()) &&
                        idFilialLocalizacaoConhecimento.equals(SessionUtils.getFilialSessao().getIdFilial())) {
                    volume.setLocalizacaoFilial(SessionUtils.getFilialSessao());
                }
            }
        } else {
			/* Seta ids das filiais envolvidas */
            Long idFilialDestinoDocto = conhecimento.getFilialByIdFilialDestino().getIdFilial();
            Long idFilialOrigemManifesto = SessionUtils.getFilialSessao().getIdFilial();
            Long idFilialDestinoManifesto = null;
            Long idFluxoDoctoServico = null;

            Filial filialSubstitutaDoctoServico = substAtendimentoFilialService.findFilialDestinoDoctoServico(conhecimento.getIdDoctoServico(), null, null, null);
            if (filialSubstitutaDoctoServico != null) {
                idFilialDestinoDocto = filialSubstitutaDoctoServico.getIdFilial();
            }

            if (conhecimento.getFluxoFilial() != null) {
                idFluxoDoctoServico = conhecimento.getFluxoFilial().getIdFluxoFilial();
            }

            if ("E".equals(tpManifesto)) {
				/*
				 * Se for manifesto de entrega, filial destino recebe a filial
				 * do usuário logado
				 */
                idFilialDestinoManifesto = SessionUtils.getFilialSessao().getIdFilial();

                //- Se a localização filial do Volume (volume_nota_fiscal.id_localizacao_filial) for diferente da FILIAL LOGADA e
                //filial localização do Documento de Serviço (docto_servico.id_filial_localizacao) for igual da FILIAL LOGADA.
                //Atualizar a filial do volume lido (volume_nota_fiscal.id_localizacao_filial) para FILIAL LOGADA.
                if (!volume.getLocalizacaoFilial().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial()) &&
                        idFilialLocalizacaoConhecimento.equals(SessionUtils.getFilialSessao().getIdFilial())) {
                    volume.setLocalizacaoFilial(SessionUtils.getFilialSessao());
                }
            } else {
                if (idFilialDesvio != null) {
					/*
					 * Se foi passada a filial de desvio, utiliza ela como
					 * destino do manifesto
					 */
                    idFilialDestinoManifesto = idFilialDesvio;
                } else {
					/* senão recebe a filial de destino do docto de serviço */
                    idFilialDestinoManifesto = this.findIdFilialDestinoManifesto(controleCarga.getRota().getIdRota(), idFluxoDoctoServico, idFilialOrigemManifesto, idFilialDestinoDocto);
                }
            }

            final List<Manifesto> manifestos = manifestoService.findManifestosByTrecho(tpManifesto, "EC", idControleCarga, idFilialOrigemManifesto, idFilialDestinoManifesto);

			/*
			 * Se não existe manifesto em carregamento que pode ser aproveitado,
			 * cria
			 */
            if ((manifestos == null || manifestos.isEmpty()) && isFirstVolumeFromDocto) {
                manifesto = new Manifesto();
                manifesto.setBlBloqueado(false);
                manifesto.setPsTotalManifesto(BigDecimalUtils.ZERO);
                manifesto.setPsTotalAforadoManifesto(BigDecimalUtils.ZERO);
                manifesto.setVlTotalManifesto(BigDecimalUtils.ZERO);
                manifesto.setMoeda(SessionUtils.getMoedaSessao());
                manifesto.setFilialByIdFilialOrigem(SessionUtils.getFilialSessao());
                manifesto.setTpStatusManifesto(new DomainValue("EC"));
                manifesto.setTpManifesto(new DomainValue(tpManifesto));
                manifesto.setTpAbrangencia(new DomainValue("N"));
                manifesto.setNrPreManifesto(configuracoesFacade.incrementaParametroSequencial(SessionUtils
                        .getFilialSessao().getIdFilial(), ConsManifesto.VL_NR_PRE_MANIFESTO, true));

                manifesto.setDhGeracaoPreManifesto(dataHora);

                // LMS-5184
                List<Manifesto> listaManifestoByControleCarga = manifestoService.findManifestosByControleCarga(controleCarga);

                if (listaManifestoByControleCarga != null && !listaManifestoByControleCarga.isEmpty()) {
                    manifesto.setTpModal(listaManifestoByControleCarga.get(0).getTpModal());
                } else {
                    manifesto.setTpModal(new DomainValue("R"));
                }

                manifesto.setControleCarga(controleCarga);
			
				/*
				 * Se for manifesto de viagem, verifica se filial destino está
				 * sendo substituída
				 */
                adicionaDadosManifestoPorTpManifesto(tpManifesto, manifesto, idFilialDestinoManifesto);

                ControleTrecho controleTrecho = controleTrechoService.findControleTrechoByIdControleCargaByIdFilialOrigemByIdFilialDestino(idControleCarga, manifesto.getFilialByIdFilialOrigem().getIdFilial(), manifesto.getFilialByIdFilialDestino().getIdFilial());
                manifesto.setControleTrecho(controleTrecho);

                manifesto.setNrPreManifesto(configuracoesFacade.incrementaParametroSequencial(SessionUtils.getFilialSessao().getIdFilial(), ConsManifesto.VL_NR_PRE_MANIFESTO, true));
                manifestoService.storeBasic(manifesto);
			
				/*
				 * Gera o registro de Carregamento do pre manifesto que foi
				 * iniciado
				 */
                carregamentoDescargaService.storeIniciarCarregamentoPreManifesto(manifesto.getIdManifesto(), idCarregamentoDescarga, dataHora);
            } else {
                manifesto = manifestos.get(0);
            }
        }

        //Monta o PreManifestoVolume
        final PreManifestoVolume preManifestoVolume = new PreManifestoVolume();
        preManifestoVolume.setDoctoServico(conhecimento);
        preManifestoVolume.setManifesto(manifesto);
        preManifestoVolume.setVolumeNotaFiscal(volume);
        preManifestoVolume.setTpScan(new DomainValue(tpScan));

        Filial filial = doctoServico.getFilialLocalizacao();
        if (filial == null) {
            filial = doctoServico.getFilialByIdFilialOrigem();
        }
		
		/*
		 * 
		 * Verifica se o documento está na filial, isso significa que o volume irá viajar com seu docto de serviço
		 */
        if (conhecimento.getFilialLocalizacao() == null || conhecimento.getFilialLocalizacao().getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())) {
			/*
			 * Verifica se é o primeiro volume do docto a ser incluso no manifesto.
			 * Então atualiza os totais do manifesto, somando valores do docto de
			 * serviço.
			 */
            if (isFirstVolumeFromDocto) {
				/*
				 * Soma os valores do docto de serviço ao manifesto.
				 */
                this.addValoresDoctoToManifesto(conhecimento, manifesto);
            }
					
			/* Verifica se documento de serviço já foi incluído no manifesto */
            PreManifestoDocumento preManifestoDocto = preManifestoDocumentoService.findPreManifestoDocumentoByIdManifestoDocto(manifesto.getIdManifesto(), conhecimento.getIdDoctoServico());

			/*
			 * Se documento ainda não está naquele manifesto, inclui registro em
			 * PreManifestoDocumento
			 */
            if (isLocalizacaoValida && preManifestoDocto == null) {
                    /* Se o volume carregado possuir a mesma localização do conhecimento, então o conhecimento
					 * será vinculado ao manifesto.
					 */

					/* Cria PreManifestoDocto */
                preManifestoDocto = new PreManifestoDocumento();
                preManifestoDocto.setDoctoServico(conhecimento);
                preManifestoDocto.setAwb(awbService.findPreAwbByIdDoctoServicoAndFilialOrigem(conhecimento.getIdDoctoServico(), SessionUtils.getFilialSessao().getIdFilial()));
                preManifestoDocto.setManifesto(manifesto);
                Integer ordem = preManifestoDocumentoService.findMaxNrOrdemByIdManifesto(manifesto.getIdManifesto());
                preManifestoDocto.setNrOrdem(++ordem);
                preManifestoDocumentoService.store(preManifestoDocto);

					/* Gera evento no documento de serviço de carregamento */
                String nrPreManifesto = preManifestoDocto.getManifesto().getFilialByIdFilialOrigem().getSgFilial()
                        + " "
                        + StringUtils.leftPad(preManifestoDocto.getManifesto().getNrPreManifesto().toString(), 8, '0');

                this.storeEventoCarregamentoDoctoServicoComBatch(conhecimento, tpManifesto, nrPreManifesto, dataHora, adsmNativeBatchSqlOperations);
            }
			
			/* Seta o PreManifestoDocumento do PreManifestoVolume */
            preManifestoVolume.setPreManifestoDocumento(preManifestoDocto);
        }

        //Atuliza registros para contabilizar volumes carregados
        if (carregamento == null) {
            carregamento = carregamentoDescargaService.findById(idCarregamentoDescarga);
        }
        this.storeCarregamentoDescargaVolume(carregamento, volume, volume.getDispositivoUnitizacao());
		
		/* Store do Volume no pré manifesto */
        preManifestoVolumeService.store(preManifestoVolume);
		
		/* Grava o volume */
        //LMS-4627 - via MWW só atualiza a localização da mercadoria e a filial
        if (("SF").equalsIgnoreCase(tpScan) || ("CS").equalsIgnoreCase(tpScan)) {
            volumeNotaFiscalService.updateLocalizacaoELocalizacaoFilial(volume.getIdVolumeNotaFiscal(), volume.getLocalizacaoMercadoria(), volume.getLocalizacaoFilial());
        } else {
            volumeNotaFiscalService.store(volume);
        }

        if (manifesto != null && "V".equals(tpManifesto)) {
            //Se o volume já estava vinculado a outro pré-manifesto de viagem
            verificarVolumeVinculadoPreManifesto(manifesto, volume);

            //Se o volume já estava vinculado a outro manifesto de viagem
            verificarVolumeVinculadoManifesto(manifesto, volume);
        }
		
		/*
		 * Gera o evento de Em Carregamento para o volume. 24 - Entrega, 25
		 * Viagem
		 */
        this.storeEventoCarregamentoVolume(volume, tpManifesto, tpScan);

        adsmNativeBatchSqlOperations.runAllCommands();

        return null;
    }

    private void criaAdiantamentoParaControleCarga(ControleCarga controleCarga) {
        List<FluxoContratacao> listFluxoContratacao = controleCarga.getSolicitacaoContratacao().getFluxosContratacao();

        BigDecimal vlFreteMaximoAutorizado = controleCarga.getSolicitacaoContratacao().getVlFreteMaximoAutorizado();

        if (listFluxoContratacao != null && !listFluxoContratacao.isEmpty()) {
            for (FluxoContratacao fc : listFluxoContratacao) {
                AdiantamentoTrecho adiantamentoTrecho = new AdiantamentoTrecho();

                adiantamentoTrecho.setControleCarga(controleCarga);
                adiantamentoTrecho.setTpStatusRecibo(new DomainValue("G"));
                adiantamentoTrecho.setPcFrete(BigDecimal.ZERO);
                adiantamentoTrecho.setVlFrete(BigDecimal.ZERO);
                adiantamentoTrecho.setFilialByIdFilialOrigem(fc.getFilialOrigem());
                adiantamentoTrecho.setFilialByIdFilialDestino(fc.getFilialDestino());

                criaAdiantamentoParaControleCargaValorFrete(vlFreteMaximoAutorizado, fc, adiantamentoTrecho);

                BigDecimal vlAdiantamento = aplicaPorcentagem(adiantamentoTrecho.getVlFrete(), adiantamentoTrecho.getPcFrete());

                if (vlAdiantamento != null) {
                    adiantamentoTrecho.setVlAdiantamento(vlAdiantamento);
                } else {
                    adiantamentoTrecho.setVlAdiantamento(BigDecimal.ZERO);
                }

                adiantamentoTrechoService.store(adiantamentoTrecho);
            }
        }
    }

    private void criaAdiantamentoParaControleCargaValorFrete(BigDecimal vlFreteMaximoAutorizado, FluxoContratacao fc, AdiantamentoTrecho adiantamentoTrecho) {
        if (fc.getPcValorFrete() != null) {
            BigDecimal vlFrete = aplicaPorcentagem(vlFreteMaximoAutorizado, fc.getPcValorFrete());

            if (vlFrete != null) {
                adiantamentoTrecho.setVlFrete(vlFrete);
            }
        }
    }

    private Conhecimento findConhecimentoAtual(VolumeNotaFiscal volume) {
        Conhecimento conhecimento = null;
        if (SessionUtils.getFilialSessao().getBlSorter()) {
            conhecimento = conhecimentoService.findConhecimentoAtualByIdSorter(volume.getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico());
        } else {
            conhecimento = conhecimentoService.findConhecimentoAtualByIdWithCriteria(volume.getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico());
        }
        return conhecimento;
    }

    private Manifesto findManifesto(ControleCarga controleCarga, Conhecimento conhecimento) {
        if (controleCarga.getIdControleCarga() != null) {
            return manifestoService.findByControleCargaAndDoctoServico(controleCarga.getIdControleCarga(), conhecimento.getIdDoctoServico());
        }
        return null;
    }

    /*
     * LMS-7350
     *
     * Valida se a localização da mercadoria permite que o volume possa ser embarcado
     */
    private boolean validaLocalizacaoMercadoriaCarregamento(VolumeNotaFiscal volume, String tpScan, Short cdLocalizacaoMercadoria, String tpControleCarga) {
        String[] localizacoesMercadoriaParametro = parametroGeralService.findSimpleConteudoByNomeParametro("LOCALIZACAO_MERCADORIA").split(";");

        for (String cdLocalizacaoMercadoriaParametro : localizacoesMercadoriaParametro) {
            if (String.valueOf(cdLocalizacaoMercadoria).equals(cdLocalizacaoMercadoriaParametro)) {
                return false;
            }
        }

        return true;
    }

    private void verificarVolumeVinculadoManifesto(Manifesto manifesto, VolumeNotaFiscal volume) {
        ManifestoNacionalVolume manifestoNacionalVolumeVinculado = manifestoNacionalVolumeService.findVolumeVinculadoManifestoViagem(manifesto.getIdManifesto(),
                volume.getIdVolumeNotaFiscal());

        if (manifestoNacionalVolumeVinculado != null && manifestoNacionalVolumeVinculado.getVolumeNotaFiscal() != null) {
            VolumeNotaFiscal volumeNotaFiscalManifestoVinculado = manifestoNacionalVolumeVinculado.getVolumeNotaFiscal();

            manifestoNacionalVolumeService.removeById(manifestoNacionalVolumeVinculado.getIdManifestoNacionalVolume());
            Short cdEvento = 150; //Volume retirado do manifesto
            Long numeroManifestoOrigem = manifestoNacionalVolumeVinculado.getManifestoViagemNacional().getNrManifestoOrigem().longValue();
            // Filial de origem para armazenar a sigla da filial na observação do evento gerado
            Filial filialOrigim = manifestoNacionalVolumeVinculado.getManifestoViagemNacional().getManifesto().getFilialByIdFilialOrigem();

            gerarEventoVolume(volumeNotaFiscalManifestoVinculado, numeroManifestoOrigem, filialOrigim, cdEvento);

            atualizarFilialLocalizacaoVolume(volumeNotaFiscalManifestoVinculado);
        }

    }

    private void verificarVolumeVinculadoPreManifesto(Manifesto manifesto, VolumeNotaFiscal volume) {
        PreManifestoVolume preManifestoVolumeVinculado = preManifestoVolumeService.findVolumeVinculadoPreManifestoViagem(manifesto.getIdManifesto(),
                volume.getIdVolumeNotaFiscal());

        if (preManifestoVolumeVinculado != null && preManifestoVolumeVinculado.getVolumeNotaFiscal() != null) {
            VolumeNotaFiscal volumeNotaFiscalPreManifestoVinculado = preManifestoVolumeVinculado.getVolumeNotaFiscal();
            Manifesto manifestoVinculado = preManifestoVolumeVinculado.getManifesto();

            preManifestoVolumeService.removeById(preManifestoVolumeVinculado.getIdPreManifestoVolume());

            Short cdEvento = 151; //Volume retirado do pré-manifesto
            Long numeroPreManifesto = manifestoVinculado.getNrPreManifesto();
            // Filial de origem para armazenar a sigla da filial na observação do evento gerado
            Filial filialOrigim = manifestoVinculado.getFilialByIdFilialOrigem();

            gerarEventoVolume(volumeNotaFiscalPreManifestoVinculado, numeroPreManifesto, filialOrigim, cdEvento);

            atualizarFilialLocalizacaoVolume(volumeNotaFiscalPreManifestoVinculado);
        }
    }

    /**
     * Método para gerar o evento do volume
     */
    private void gerarEventoVolume(VolumeNotaFiscal volumeNotaFiscal, Long numeroManifesto, Filial filialOrigin, Short cdEvento) {
        OcorrenciaEntrega ocorrenciaEntrega = null;
        Filial filial = SessionUtils.getFilialSessao();
        DateTime dhOcorrencia = JTDateTimeUtils.getDataHoraAtual();
        String numeroManifestoFormatado = new DecimalFormat("00000000").format(numeroManifesto);
        String obComplemento = "Manifesto " + filialOrigin.getSgFilial() + " " + numeroManifestoFormatado;

        eventoVolumeService.generateEventoVolume(volumeNotaFiscal, cdEvento, ConstantesSim.TP_SCAN_FISICO, obComplemento, ocorrenciaEntrega, filial, dhOcorrencia, SessionUtils.getUsuarioLogado());
    }

    private BigDecimal aplicaPorcentagem(BigDecimal base, BigDecimal porcentagem) {
        BigDecimal valor = base.multiply(porcentagem).divide(BigDecimalUtils.HUNDRED);
        valor.setScale(2, BigDecimal.ROUND_HALF_UP);
        return valor;
    }

    private Long findIdFilialDestinoDocumento(DoctoServico doctoServico, ControleCarga controleCarga, String tpManifesto, Long idFilialDesvio) {
		/* Seta ids das filiais envolvidas */
        Long idFilialDestinoDocto = doctoServico.getFilialByIdFilialDestino().getIdFilial();
        Long idFilialOrigemManifesto = SessionUtils.getFilialSessao().getIdFilial();
        Long idFilialDestinoManifesto = null;
        Long idFluxoDoctoServico = null;

        Filial filialSubstitutaDoctoServico = substAtendimentoFilialService.findFilialDestinoDoctoServico(doctoServico.getIdDoctoServico(), null, null, null);
        if (filialSubstitutaDoctoServico != null) {
            idFilialDestinoDocto = filialSubstitutaDoctoServico.getIdFilial();
        }

        if (doctoServico.getFluxoFilial() != null) {
            idFluxoDoctoServico = doctoServico.getFluxoFilial().getIdFluxoFilial();
        }

        if ("E".equals(tpManifesto)) {
			/*
			 * Se for manifesto de entrega, filial destino recebe a filial
			 * do usuário logado
			 */
            idFilialDestinoManifesto = SessionUtils.getFilialSessao().getIdFilial();
        } else {
            if (idFilialDesvio != null) {
				/*
				 * Se foi passada a filial de desvio, utiliza ela como
				 * destino do manifesto
				 */
                idFilialDestinoManifesto = idFilialDesvio;
            } else {
				/* senão recebe a filial de destino do docto de serviço */
                idFilialDestinoManifesto = this.findIdFilialDestinoManifesto(controleCarga.getRota().getIdRota(), idFluxoDoctoServico, idFilialOrigemManifesto, idFilialDestinoDocto);
                if (idFilialDestinoManifesto == null) {
                    idFilialDestinoManifesto = idFilialDestinoDocto;
                }
            }
        }

        return idFilialDestinoManifesto;
    }

    /* Executa todas as validações pertinentes ao controle carga e carregamento */
    private List<Map<String, Object>> validateRemoveVolume(VolumeNotaFiscal volume, ControleCarga controleCarga,
                                                           DomainValue tpStatusCarregamento, String tpScan) {
		/* Busca volume no controle de cargas */
        PreManifestoVolume preManifestoVolume = preManifestoVolumeService.findByIdVolumeAndIdControleCarga(volume.getIdVolumeNotaFiscal(), controleCarga.getIdControleCarga());
		
		/* Se volume não foi carregado */
        if (preManifestoVolume == null) {
			/*
			 * Não foi possível excluir este volume do Carregamento, pois não se
			 * encontra carregado.
			 */
            throw new BusinessException("LMS-45045");
        }

        List<BusinessException> exceptions = new ArrayList<BusinessException>();
        List<BusinessException> confirmations = new ArrayList<BusinessException>();
				
		/* Verifica se volume está viajando junto com o documento de serviço */
        if (preManifestoVolume.getPreManifestoDocumento() != null) {
			
			/*
			 * Pega todos os volumes daquele documento de serviço para o
			 * controle de carga em questão
			 */
            final Integer rowCountVolumes = preManifestoVolumeService.getRowCountPreManifestoVolume(controleCarga.getIdControleCarga(), preManifestoVolume.getDoctoServico().getIdDoctoServico());
			/* Se é o último volume do documento de serviço */
            if (IntegerUtils.ONE.equals(rowCountVolumes)) {
				/* Busca os documentos de serviço restantes do manifesto */
                final Integer rowCount = preManifestoDocumentoService.getRowCountPreManifestoDocumento(preManifestoVolume.getManifesto().getIdManifesto());
				
				/* Dá erro no caso do manifesto ficar vazio após a exclusão */
                if (IntegerUtils.ONE.equals(rowCount)) {
                    throw new BusinessException("LMS-05072");
                }
            }
        }
			
		/* Valida se volume está unitizado, e não permite carregá-lo caso esteja */
        if (volume.getDispositivoUnitizacao() != null && tpScan.equals(ConstantesSim.TP_SCAN_FISICO)) {
            DispositivoUnitizacao dispositivo = volume.getDispositivoUnitizacao();
            if (dispositivo.getDispositivoUnitizacaoPai() != null) {
				/*
				 * Este volume está unitizado em um dispositivo que também está
				 * unitizado
				 */
                exceptions.add(new BusinessException("LMS-45054"));
            } else {
				/*
				 * Volume unitizado no dispositivo {0} código {1}. Você deseja
				 * forçar a desunitização deste volume?
				 */
                confirmations.add(new BusinessException("LMS-45008", new Object[]{
                        dispositivo.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao(),
                        dispositivo.getNrIdentificacao()}));
            }
        }
		
		/*
		 * Verifica se o carregamento está Em Conferência, se sim, pergunta se
		 * quer reabrir carregamento
		 */
        if (!"I".equals(tpStatusCarregamento.getValue())) {
            exceptions.add(new BusinessException(ConsErro.OPR_INVALIDA_CARREGAMENTO_STATUS_X, new Object[]{tpStatusCarregamento
                    .getDescriptionAsString()}));
        }

        Conhecimento conhecimento = volume.getNotaFiscalConhecimento().getConhecimento();
        conhecimento = conhecimentoService.findConhecimentoAtualById(conhecimento.getIdDoctoServico());
        if (conhecimento == null) {
            conhecimento = volume.getNotaFiscalConhecimento().getConhecimento();
        }
        String nrConhecimento = conhecimento.getFilialByIdFilialOrigem().getSgFilial() + " "
                + StringUtils.leftPad(String.valueOf(conhecimento.getNrDoctoServico()), 7, "0") + " "
                + volume.getNrSequencia() + "/" + conhecimento.getQtVolumes();

        return this.generateMessageList(exceptions, confirmations, new ArrayList<BusinessException>(), nrConhecimento);
    }

    /**
     * Remove o volume do carregamento, após executadas as validações pelo
     * método executeRemoveValidations
     *
     * @param volume
     * @param controleCarga
     * @param tpScan
     * @param needValidation
     * @return
     */
    public List<Map<String, Object>> removeVolume(VolumeNotaFiscal volume, ControleCarga controleCarga, String tpScan, Boolean needValidation) {
        Long idManifesto = null;
        Manifesto manifesto = null;
        String tpManifesto = null;
		
		/* Lista com as mensagens de confirmação necessárias */
        List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();

        CarregamentoDescarga carregamento = this.findCarregamentoByControleCarga(controleCarga.getIdControleCarga());
        DomainValue tpStatusCarregamento = this.findTpStatusCarregamento(carregamento);
						
		/* Seta doctoServico, pegando do volume */
        Conhecimento conhecimento = volume.getNotaFiscalConhecimento().getConhecimento();
        conhecimento = conhecimentoService.findConhecimentoAtualById(conhecimento.getIdDoctoServico());
        if (conhecimento == null) {
            conhecimento = volume.getNotaFiscalConhecimento().getConhecimento();
        }

        DoctoServico doctoServico = conhecimento;
        if (needValidation) {
            messages = this.validateRemoveVolume(volume, controleCarga, tpStatusCarregamento, tpScan);
            if (!messages.isEmpty()) {
                return this.cleanMessageList(messages);
            }
        }
		
		/* Caso possível, reabre o carregamento. Caso contrário, lançará exceção */
        if (carregamento != null && !"I".equals(carregamento.getTpStatusOperacao().getValue())) {
            this.storeStatusCarregamento(carregamento, "I");
        }
		
		/*
		 * Caso tenha sido confirmada a desunitização e tipo de scan = físico,
		 * executa desunitização parcial do volume
		 */
        if (volume.getDispositivoUnitizacao() != null && ConstantesSim.TP_SCAN_FISICO.equals(tpScan)) {
            List<VolumeNotaFiscal> volumesDesunitizacao = new ArrayList<VolumeNotaFiscal>();
            volumesDesunitizacao.add(volume);
            unitizacaoService.storeDesunitizarParcial(volume.getDispositivoUnitizacao().getIdDispositivoUnitizacao(), volumesDesunitizacao, null);
        }
		
		/* Busca o Pre Manifesto Volume referente ao Volume / Controle Carga */
        PreManifestoVolume preManifestoVolume = preManifestoVolumeService.findByIdVolumeAndIdControleCarga(volume.getIdVolumeNotaFiscal(), controleCarga.getIdControleCarga());

        manifesto = preManifestoVolume.getManifesto();
        tpManifesto = manifesto.getTpManifesto().getValue();

        List<Short> eventos = this.getEventosCancelaCarregamento(tpManifesto);
        if (manifesto.getTpStatusManifesto() != null && manifesto.getTpStatusManifesto().getValue().equals(STATUS_MANIFESTO_CARREG_CONCLUIDO)) {
			/* CANCELA FIM DE CARREGAMENTO */
            if ("E".equals(tpManifesto)) {
                eventos.add(Short.valueOf("122"));
            } else {
                eventos.add(Short.valueOf("121"));
            }
        }
		
		/* Remove volume do pré manifesto */
        preManifestoVolumeService.removeById(preManifestoVolume.getIdPreManifestoVolume());
		
    	/* Se volume e o documento de serviço estão no mesmo manifesto */
        if (preManifestoVolume.getPreManifestoDocumento() != null) {
            Long idDoctoServico = preManifestoVolume.getDoctoServico().getIdDoctoServico();
            idManifesto = preManifestoVolume.getManifesto().getIdManifesto();
            List<PreManifestoVolume> volumesByManifesto = preManifestoVolumeService.findByDoctoServicoAndManifesto(idDoctoServico, idManifesto);

            boolean isLastVolumeFromDocto = volumesByManifesto == null || volumesByManifesto.isEmpty();
        	
			/*
			 * Se for último volume do docto de serviço, remove o docto de
			 * serviço do pré manifesto
			 */
            if (isLastVolumeFromDocto) {
        		/* Remove documento do Pre Manifesto */
                preManifestoDocumentoService.removeById(preManifestoVolume.getPreManifestoDocumento().getIdPreManifestoDocumento());
        		
				/*
				 * Subtrai os valores do docto de serviço do manifesto.
				 */
                this.subtractValoresDoctoFromManifesto(doctoServico, manifesto);
        		
        		/* Gera evento no documento de serviço de carregamento */
                String nrPreManifesto = manifesto.getFilialByIdFilialOrigem().getSgFilial() + " "
                        + StringUtils.leftPad(manifesto.getNrPreManifesto().toString(), 8, '0');
        		
        		/* Remove envento do documento de serviço */
                this.generateEventosDoctoServico(preManifestoVolume.getDoctoServico().getIdDoctoServico(),
                        nrPreManifesto, JTDateTimeUtils.getDataHoraAtual(), tpManifesto, eventos);
            }
        }
    	
		/*
		 * Gera o evento de Em Carregamento para o volume. 24 - Entrega, 25
		 * Viagem
		 */
        VolumeNotaFiscal novoVolume = preManifestoVolume.getVolumeNotaFiscal();
        this.generateEventosVolume(novoVolume, tpScan, eventos);

        //Remove volume da tabela que contabiliza os volumes lidos.
        carregamentoDescargaVolumeService.removeByCarregamentoDescargaByVolumeNotaFiscal(carregamento, volume);

        return null;
    }

    private void subtractValoresDoctoFromManifesto(DoctoServico doctoServico, Manifesto manifesto) {
        this.updateValoresManifesto(doctoServico, manifesto, -1);
    }

    private void addValoresDoctoToManifesto(DoctoServico doctoServico, Manifesto manifesto) {
        this.updateValoresManifesto(doctoServico, manifesto, 1);
    }

    public void updateValoresManifesto(DoctoServico doctoServico, Manifesto manifesto, int signalMultplier) {
		/* Seta variável com data e hora */
        DateTime dataHora = JTDateTimeUtils.getDataHoraAtual();

        if (signalMultplier < 0) {
            signalMultplier = -1;
        } else {
            signalMultplier = 1;
        }

        if (doctoServico.getVlMercadoria() != null) {
            BigDecimal vlMercadoriaDoctoConvertido = conversaoMoedaService.findConversaoMoeda(
                    doctoServico.getPaisOrigem().getIdPais(),
                    doctoServico.getMoeda().getIdMoeda(),
                    SessionUtils.getPaisSessao().getIdPais(),
                    SessionUtils.getMoedaSessao().getIdMoeda(),
                    dataHora.toYearMonthDay(),
                    doctoServico.getVlMercadoria());

            manifesto.setVlTotalManifesto(manifesto.getVlTotalManifesto().add(vlMercadoriaDoctoConvertido.multiply(new BigDecimal(signalMultplier))));
        }

        if (doctoServico.getPsReal() != null) {
            manifesto.setPsTotalManifesto(manifesto.getPsTotalManifesto().add(doctoServico.getPsReal().multiply(new BigDecimal(signalMultplier))));
        }

        if (doctoServico.getPsAforado() != null) {
            if (manifesto.getPsTotalAforadoManifesto() == null) {
                manifesto.setPsTotalAforadoManifesto(BigDecimalUtils.ZERO);
            }
            manifesto.setPsTotalAforadoManifesto(manifesto.getPsTotalAforadoManifesto().add(doctoServico.getPsAforado().multiply(new BigDecimal(signalMultplier))));
        }
		/* Atualiza manifesto */
        manifestoService.storeBasic(manifesto);
    }

    private List<Map<String, Object>> validateDispositivo(ControleCarga controleCarga,
                                                          DispositivoUnitizacao dispositivo, String tpManifesto, CarregamentoDescarga carregamento, String tpScan,
                                                          Long idFilialDesvio, Set<Long> idsFiliaisDestino) {
		/* Busca dispositivo no carregamento */
        DispCarregIdentificado dispCarregado = dispCarregIdentificadoService.findByDispositivoUnitizacaoAndControleCarga(controleCarga.getIdControleCarga(), dispositivo.getIdDispositivoUnitizacao());
		
				
	
		/* Lista com as mensagens de confirmação necessárias */
        List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
        List<BusinessException> exceptions = new ArrayList<BusinessException>();
        List<BusinessException> confirmations = new ArrayList<BusinessException>();
		
		/*
		 * Caso já esteja carregado, levanta exceção : Este dispositivo já está
		 * carregado.
		 */
        if (dispCarregado != null) {
            throw new BusinessException("LMS-45058");
        }
	
		/* Valida se a localização do dispositivo é válida */
        if (dispositivo.getLocalizacaoMercadoria() != null) {
            try {
                this.validaLocalizacaoMercadoria(tpManifesto, dispositivo.getLocalizacaoMercadoria());
            } catch (BusinessException be) {
                exceptions.add(be);
            }
        }
		
		/* Se for o volume lido por SCAN FISICO e estiver unitizado... */
        if (ConstantesSim.TP_SCAN_FISICO.equals(tpScan)) {
            if (dispositivo.getDispositivoUnitizacaoPai() != null) {
                DispositivoUnitizacao dispositivoPai = dispositivo.getDispositivoUnitizacaoPai();
				
				/*
				 * Se dispositivo pai também está unitizado, levanta mensagem de
				 * erro
				 */
                if (dispositivoPai.getDispositivoUnitizacaoPai() != null) {
					/*
					 * Este volume está unitizado em um dispositivo que também
					 * está unitizado
					 */
                    exceptions.add(new BusinessException("LMS-45057"));
					/*
					 * Senão, lança mensagem de confirmação perguntando se
					 * deseja fazer a desunitização parcial
					 */
                } else {
					/*
					 * Volume unitizado no dispositivo {0} código {1}. Você
					 * deseja forçar a desunitização deste volume?
					 */
                    confirmations.add(new BusinessException("LMS-45009", new Object[]{
                            tipoDispositivoUnitizacaoService.findById(dispositivoPai.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao())
                                    .getDsTipoDispositivoUnitizacao(),
                            dispositivoPai.getNrIdentificacao()}));
                }
            }
		
			/*
			 * Se está alocado, solicita confirmação para desalocação, caso
			 * alocado
			 */
            if (dispositivo.getMacroZona() != null) {
                MacroZona mz = macroZonaService.findById(dispositivo.getMacroZona().getIdMacroZona());
                confirmations.add(new BusinessException(ConsErro.VOLUME_ALOCADO_MACR_ZONA_X_FORC_DESALOCACAO, new Object[]{mz.getDsMacroZona()}));
            }
        }
		
		/* Busca volumes do Dispositivo de unitização */
        final List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findVolumeByIdDispositivoUnitizacao(dispositivo.getIdDispositivoUnitizacao());
        dispositivo.setVolumes(volumes);
		
		/* Lista com documentos de serviços já validados */
        Map<Long, Boolean> doctosServicoValidados = new HashMap<Long, Boolean>();

        Set<String> modais = new HashSet<String>();

        if (volumes == null || volumes.isEmpty()) {
            if (carregamento != null) {
                confirmations.add(new BusinessException("LMS-45096", new Object[]{}));
            } else {
                exceptions.add(new BusinessException("LMS-45098"));
            }

        }
		
		/*
		 * Percorre volumes chamando rotina de store e preenchendo mensagens de
		 * erro dos volumes problemáticos
		 */
        for (VolumeNotaFiscal volume : volumes) {
			/*
			 * Preenche lista com os ids das filiais destino dos volumes
			 * unitizados
			 */
            Conhecimento conhecimento = volume.getNotaFiscalConhecimento().getConhecimento();
            conhecimento = conhecimentoService.findConhecimentoAtualById(conhecimento.getIdDoctoServico());
            if (conhecimento == null) {
                conhecimento = volume.getNotaFiscalConhecimento().getConhecimento();
            }

            String bloqFluxoSubcontratacao = (String)configuracoesFacade.getValorParametro(
                    SessionUtils.getFilialSessao().getIdFilial(), 
                    "BL_BLOQ_DOC_SUB");
            if("S".equalsIgnoreCase(bloqFluxoSubcontratacao)) {
                if(BooleanUtils.isTrue(conhecimento.getBlFluxoSubcontratacao())){
                    throw new BusinessException("LMS-45216");
                }
            }
            
            modais.add(conhecimento.getServico().getTpModal().getValue());

            idsFiliaisDestino.add(this.findIdFilialDestinoDocumento(conhecimento, controleCarga, tpManifesto, idFilialDesvio));

			/* Valida documentos de serviços ainda não validados */
            if (!doctosServicoValidados.containsKey(conhecimento.getIdDoctoServico())) {
				/*
				 * Busca volumes carregados do documento de serviço para este
				 * controle de carga
				 */
                Integer rowCountVolumes = IntegerUtils.ZERO;
                if (controleCarga.getIdControleCarga() != null) {
                    rowCountVolumes = preManifestoVolumeService.getRowCountPreManifestoVolume(controleCarga.getIdControleCarga(), conhecimento.getIdDoctoServico());
                }
				
				/*
				 * Caso não exista volume carregado para o documento de serviço,
				 * executa validações do documento
				 */
                if (IntegerUtils.isZero(rowCountVolumes)) {
                    messages.addAll(this.validateDoctoServico(conhecimento, volume, controleCarga, tpManifesto, idFilialDesvio, tpScan));
                }

                doctosServicoValidados.put(conhecimento.getIdDoctoServico(), true);
            }
        }
		
		/* Busca dispositivos filhos */
        List<DispositivoUnitizacao> dispositivosFilhos = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivo.getIdDispositivoUnitizacao());
        dispositivo.setDispositivosUnitizacao(dispositivosFilhos);
		
		/* Percorre dispositivos filhos para gravação dos volumes */
        for (DispositivoUnitizacao dispositivoFilho : dispositivosFilhos) {
            messages.addAll(this.validateDispositivo(controleCarga, dispositivoFilho, tpManifesto, carregamento, ConstantesSim.TP_SCAN_CASCADE, idFilialDesvio, idsFiliaisDestino));
        }
		
		/*
		 * Se for o dispositivo principal (pai de todos) e tiver volumes com
		 * destinos diferentes
		 */
        if (ConstantesSim.TP_SCAN_FISICO.equals(tpScan)) {
            if (idsFiliaisDestino.size() > 1) {
                exceptions.add(new BusinessException("LMS-45056"));
            }
            if (modais.size() > 1) {
                exceptions.add(new BusinessException("LMS-45079"));
            }
        }
		
		/*
		 * Verifica se o carregamento está Em Conferência ou Concluído MWW, se
		 * sim, pergunta se quer reabrir carregamento
		 */
        DomainValue tpStatusCarregamento = this.findTpStatusCarregamento(carregamento);
        if (!"I".equals(tpStatusCarregamento.getValue())) {
            exceptions.add(new BusinessException(ConsErro.OPR_INVALIDA_CARREGAMENTO_STATUS_X, new Object[]{tpStatusCarregamento.getDescriptionAsString()}));
        }

        String description = dispositivo.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao().getValue().concat(" - ").concat(dispositivo.getNrIdentificacao());

        messages.addAll(this.generateMessageList(exceptions, confirmations, new ArrayList<BusinessException>(), description));

        return messages;
    }

    public List<Map<String, Object>> storeDispositivo(ControleCarga controleCarga, Long idBox,
                                                      DispositivoUnitizacao dispositivo, CarregamentoDescarga carregamento,
                                                      CarregamentoPreManifesto carregamentoPreManifesto, String tpManifesto, String tpScan, Long idFilialDesvio,
                                                      Boolean needValidation) {
		/* Testa dispositivo passado por parâmetro */
        if (dispositivo == null) {
            throw new BusinessException(ConsErro.DISP_UNITIZACAO_N_ENCONTRADO);
        }

		/* Lista com as mensagens de confirmação necessárias */
        List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();

        if (needValidation) {
            messages = this.validateDispositivo(controleCarga, dispositivo, tpManifesto, carregamento, tpScan, idFilialDesvio, new TreeSet<Long>());
        }

        if (!messages.isEmpty()) {
            return this.cleanMessageList(messages);
        } else {
            Long idFilialDestino = null;
            String tpModal = null;
			
			/* Caso tipo de scan = físico */
            if (tpScan.equals(ConstantesSim.TP_SCAN_FISICO)) {
				/*
				 * Caso esteja unitizado, executa desunitização parcial do
				 * volume
				 */
                if (dispositivo.getDispositivoUnitizacaoPai() != null) {
                    List<DispositivoUnitizacao> dispositivosDesunitizacao = new ArrayList<DispositivoUnitizacao>();
                    dispositivosDesunitizacao.add(dispositivo);
                    unitizacaoService.storeDesunitizarParcial(dispositivo.getDispositivoUnitizacaoPai().getIdDispositivoUnitizacao(), null, dispositivosDesunitizacao);
                }
				/* Caso esteja alocado, executa desalocação */
                if (dispositivo.getMacroZona() != null) {
                    macroZonaService.storeDesalocarDispositivo(dispositivo.getIdDispositivoUnitizacao(), tpScan);
                }
            }
			
			/* Se dispositivo tem volumes, percorre-os gravando */
            if (dispositivo.getVolumes() != null && !dispositivo.getVolumes().isEmpty()) {
				/* Busca volumes do Dispositivo de unitização */
                List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findVolumeByIdDispositivoUnitizacao(dispositivo.getIdDispositivoUnitizacao());
                dispositivo.setVolumes(volumes);

                if (volumes != null && !volumes.isEmpty()) {
                    Conhecimento conhecimento = conhecimentoService.findConhecimentoAtualById(volumes.get(0).getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico());
                    if (conhecimento == null) {
                        conhecimento = volumes.get(0).getNotaFiscalConhecimento().getConhecimento();
                    }
                    idFilialDestino = conhecimento.getFilialByIdFilialDestino().getIdFilial();
                    tpModal = conhecimento.getServico().getTpModal().getValue();
                }
				
				/*
				 * Percorre volumes chamando rotina de store e preenchendo
				 * mensagens de erro dos volumes problemáticos
				 */
                for (VolumeNotaFiscal volume : volumes) {
                    if (SessionUtils.getFilialSessao().getBlSorter()) {
                        this.storeVolumeBlsorter(volume, controleCarga, idBox, tpManifesto, ConstantesSim.TP_SCAN_CASCADE, idFilialDesvio, false, SessionUtils.getFilialSessao().getBlSorter());
                    } else {
                        this.storeVolume(volume, controleCarga, idBox, tpManifesto, ConstantesSim.TP_SCAN_CASCADE, idFilialDesvio, false);
                    }
                }
            }

            List<DispositivoUnitizacao> dispositivosFilhos = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivo.getIdDispositivoUnitizacao());
            dispositivo.setDispositivosUnitizacao(dispositivosFilhos);

            if (idFilialDesvio != null) {
                idFilialDestino = idFilialDesvio;
            }
			
			/* Percorre dispositivos filhos para gravação dos volumes */
            for (DispositivoUnitizacao dispositivoFilho : dispositivosFilhos) {
                this.storeDispositivo(controleCarga, idBox, dispositivoFilho, carregamento, carregamentoPreManifesto, tpManifesto, ConstantesSim.TP_SCAN_CASCADE, idFilialDesvio, false);
            }
			
			/* Se ainda não buscou PreManifestoCarregamento, busca */
            if (carregamentoPreManifesto == null) {
                if (carregamento == null) {
                    carregamento = this.findCarregamentoByControleCarga(controleCarga.getIdControleCarga());
                }

                if (carregamento != null) {
                    carregamentoPreManifesto = carregamentoPreManifestoService.findByCarregamentoAndDestino(carregamento.getIdCarregamentoDescarga(), idFilialDestino, tpModal);
                }
            }
			
			/* Gera o evento de carregamento */
            this.storeEventoCarregamentoDispositivo(dispositivo.getIdDispositivoUnitizacao(), tpManifesto, tpScan);
						
			/*
			 * Grava dispositivo na tabela de relacionamento
			 * disp_carreg_identificado
			 */
            DispCarregIdentificado dispIdentificado = new DispCarregIdentificado();
            dispIdentificado.setDispositivoUnitizacao(dispositivo);
            dispIdentificado.setCarregamentoDescarga(carregamento);
            dispIdentificado.setCarregamentoPreManifesto(carregamentoPreManifesto);
            dispCarregIdentificadoService.store(dispIdentificado);
        }

        return null;
    }

    private List<Map<String, Object>> validateRemoveDispositivo(ControleCarga controleCarga,
                                                                DispositivoUnitizacao dispositivo, String tpManifesto, CarregamentoDescarga carregamento, String tpScan) {
		/* Busca dispositivo no carregamento */
        DispCarregIdentificado dispCarregado = dispCarregIdentificadoService
                .findByDispositivoUnitizacaoAndControleCarga(controleCarga.getIdControleCarga(), dispositivo
                        .getIdDispositivoUnitizacao());
				
		/*
		 * Não foi possível excluir este dispositivo do Carregamento, pois não
		 * se encontra carregado.
		 */
        if (dispCarregado == null) {
            throw new BusinessException("LMS-45059");
        }
	
		/* Lista com as mensagens de confirmação necessárias */
        List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();
        List<BusinessException> exceptions = new ArrayList<BusinessException>();
        List<BusinessException> confirmations = new ArrayList<BusinessException>();
		
		/* Se for o volume lido por SCAN FISICO e estiver unitizado... */
        if (ConstantesSim.TP_SCAN_FISICO.equals(tpScan) && dispositivo.getDispositivoUnitizacaoPai() != null) {
            DispositivoUnitizacao dispositivoPai = dispositivo.getDispositivoUnitizacaoPai();
			
			/*
			 * Se dispositivo pai também está unitizado, levanta mensagem de
			 * erro
			 */
            if (dispositivoPai.getDispositivoUnitizacaoPai() != null) {
				/*
				 * Este volume está unitizado em um dispositivo que também está
				 * unitizado
				 */
                exceptions.add(new BusinessException("LMS-45057"));
				/*
				 * Senão, lança mensagem de confirmação perguntando se deseja
				 * fazer a desunitização parcial
				 */
            } else {
				/*
				 * Volume unitizado no dispositivo {0} código {1}. Você deseja
				 * forçar a desunitização deste volume?
				 */
                confirmations.add(new BusinessException("LMS-45009", new Object[]{
                        dispositivoPai.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao(),
                        dispositivoPai.getNrIdentificacao()}));
            }
        }
		
		/* Busca volumes do Dispositivo de unitização */
        List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findVolumeByIdDispositivoUnitizacao(dispositivo
                .getIdDispositivoUnitizacao());
        dispositivo.setVolumes(volumes);

		/*
		 * Percorre volumes chamando rotina de store e preenchendo mensagens de
		 * erro dos volumes problemáticos
		 */
        for (VolumeNotaFiscal volume : volumes) {
            volume = volumeNotaFiscalService.findVolumeByBarCodeUniqueResult(volume.getNrVolumeEmbarque());
			/* Busca status do carregamento para validação */
            DomainValue tpStatusCarregamento = this.findTpStatusCarregamento(carregamento);
            messages.addAll(this.validateRemoveVolume(volume, controleCarga, tpStatusCarregamento, ConstantesSim.TP_SCAN_CASCADE));
        }
		
		/* Busca dispositivos filhos */
        List<DispositivoUnitizacao> dispositivosFilhos = dispositivoUnitizacaoService
                .findDispositivosByIdPai(dispositivo.getIdDispositivoUnitizacao());
        dispositivo.setDispositivosUnitizacao(dispositivosFilhos);
		
		/* Percorre dispositivos filhos para gravação dos volumes */
        for (DispositivoUnitizacao dispositivoFilho : dispositivosFilhos) {
            messages.addAll(this.validateRemoveDispositivo(controleCarga, dispositivoFilho, tpManifesto, carregamento, ConstantesSim.TP_SCAN_CASCADE));
        }

        messages.addAll(this.generateMessageList(exceptions, confirmations, new ArrayList<BusinessException>(), null));
        return messages;
    }

    public List<Map<String, Object>> removeDispositivo(ControleCarga controleCarga, DispositivoUnitizacao dispositivo,
                                                       CarregamentoDescarga carregamento, String tpManifesto, String tpScan, Boolean needValidation) {
		
		/* Testa dispositivo passado por parâmetro */
        if (dispositivo == null) {
            throw new BusinessException(ConsErro.DISP_UNITIZACAO_N_ENCONTRADO);
        }

		/* Lista com as mensagens de confirmação necessárias */
        List<Map<String, Object>> messages = new ArrayList<Map<String, Object>>();

        if (needValidation) {
            messages = this.validateRemoveDispositivo(controleCarga, dispositivo, tpManifesto, carregamento, tpScan);
            if (!messages.isEmpty()) {
                return this.cleanMessageList(messages);
            }
        }
		
		/*
		 * Caso tenha sido confirmada a desunitização e tipo de scan = físico,
		 * executa desunitização parcial do volume
		 */
        if (dispositivo.getDispositivoUnitizacaoPai() != null && tpScan.equals(ConstantesSim.TP_SCAN_FISICO)) {
            List<DispositivoUnitizacao> dispositivosDesunitizacao = new ArrayList<DispositivoUnitizacao>();
            dispositivosDesunitizacao.add(dispositivo);
            unitizacaoService.storeDesunitizarParcial(dispositivo.getDispositivoUnitizacaoPai().getIdDispositivoUnitizacao(), null, dispositivosDesunitizacao);
        }
		
		/* Se dispositivo tem volumes, percorre-os removendo-os do carregamento */
        if (dispositivo.getVolumes() != null) {
			/* Busca volumes do Dispositivo de unitização */
            List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findVolumeByIdDispositivoUnitizacao(dispositivo.getIdDispositivoUnitizacao());
            dispositivo.setVolumes(volumes);

            if (volumes != null && !volumes.isEmpty()) {
				/*
				 * Percorre volumes chamando rotina de store e preenchendo
				 * mensagens de erro dos volumes problemáticos
				 */
                for (VolumeNotaFiscal volume : volumes) {
                    this.removeVolume(volume, controleCarga, ConstantesSim.TP_SCAN_CASCADE, false);
                }
            }
        }

        List<DispositivoUnitizacao> dispositivosFilhos = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivo.getIdDispositivoUnitizacao());
        dispositivo.setDispositivosUnitizacao(dispositivosFilhos);
					
		/* Percorre dispositivos filhos para gravação dos volumes */
        for (DispositivoUnitizacao dispositivoFilho : dispositivosFilhos) {
            this.removeDispositivo(controleCarga, dispositivoFilho, carregamento, tpManifesto, ConstantesSim.TP_SCAN_CASCADE, false);
        }
		
		/* Remove o evento de carregamento do dispositivo */
        this.removeEventoCarregamentoDispositivo(dispositivo.getIdDispositivoUnitizacao(), tpManifesto, tpScan);
					
		/* Remove o dispCarregIdentificado */
        DispCarregIdentificado dispIdentificado = dispCarregIdentificadoService.findByDispositivoUnitizacaoAndControleCarga(controleCarga.getIdControleCarga(), dispositivo.getIdDispositivoUnitizacao());
        dispCarregIdentificadoService.removeById(dispIdentificado.getIdDispCarregIdentificado());

        return null;
    }

    public void storeStatusCarregamento(CarregamentoDescarga carregamento, String tpStatus) {
		/*
		 * Caso Status: - Iniciado e tentando mudar para Concluído MWW
		 */
        if ("O".equals(tpStatus) && "I".equals(carregamento.getTpStatusOperacao().getValue())) {
			/* Muda status do carregamento para o novo */
            carregamento.setTpStatusOperacao(new DomainValue(tpStatus));
            carregamentoDescargaService.store(carregamento);
        } else if (!tpStatus.equals(carregamento.getTpStatusOperacao().getValue())
                || "O".equals(carregamento.getTpStatusOperacao().getValue())) {
            throw new BusinessException(ConsErro.OPR_INVALIDA_CARREGAMENTO_STATUS_X, new Object[]{carregamento.getTpStatusOperacao().getDescription()});
        }
    }

    public Map<String, Object> findOciosidadeAndLacres(long idControleCarga) {
        Map<String, Object> retorno = new HashMap<String, Object>();
        ControleCarga cc = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
        List<Map> lacres = lacreControleCargaService.findByControleCarga(idControleCarga);
        for (Map lacre : lacres) {
            lacre.remove("idLacreControleCarga");
        }
        retorno.put("lacres", lacres);
        retorno.put("ociosidadeVisual", cc.getPcOcupacaoInformado() != null ?
                100 - cc.getPcOcupacaoInformado().intValue() :
                null);
        return retorno;
    }

    public void storeStatusCarregamento(String sgFilial, String tpSituacaoConhecimento, Long idControleCarga, String tpStatus, BigDecimal pcOciosidadeVisual,
                                        String[] lacres) {

        if (lacres == null || lacres.length == 0) {
            throw new BusinessException("LMS-05149");
        }

        ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);

        BigDecimal percent = BigDecimalUtils.HUNDRED;
        percent = percent.subtract(pcOciosidadeVisual);
        controleCarga.setPcOcupacaoInformado(percent);
		
		/* Exclui os lacres  */
        List<Map> lacresOld = lacreControleCargaService.findByControleCarga(idControleCarga);
        for (Map lacre : lacresOld) {
            lacreControleCargaService.removeById(Long.parseLong(lacre.get("idLacreControleCarga").toString()));
        }
		/* ===================== */

        List<LacreControleCarga> listLacreControleCarga = new ArrayList<LacreControleCarga>();
        adicionarLacres(lacres, controleCarga, listLacreControleCarga);

        if (!listLacreControleCarga.isEmpty()) {
            lacreControleCargaService.storeLacresControleCarga(listLacreControleCarga);
        }

        CarregamentoDescarga carregamentoDescarga = this.findCarregamentoByControleCarga(idControleCarga);
        if (carregamentoDescarga.getBox() != null) {
            carregamentoDescarga.getBox().setTpSituacaoBox(new DomainValue("L"));
            if (carregamentoDescarga.getBox().getDoca() != null) {
                carregamentoDescarga.getBox().getDoca().setTpSituacaoDoca(new DomainValue("L"));
            }
        }

        this.storeStatusCarregamento(carregamentoDescarga, tpStatus);
		/*
		 * CQPRO00028189 - Atualiza informação do percentual de ocupação
		 * infromado.
		 */
        controleCargaService.store(controleCarga);
    }

    private void adicionarLacres(String[] lacres, ControleCarga controleCarga, List<LacreControleCarga> listLacreControleCarga) {
        for (String lacre : lacres) {
            if (lacre.length() > 0) {
                LacreControleCarga lacreControleCarga = new LacreControleCarga();
                lacreControleCarga.setNrLacres(lacre);
                lacreControleCarga.setTpStatusLacre(new DomainValue("FE"));
                lacreControleCarga.setDsLocalInclusao(null);
                lacreControleCarga.setControleCarga(controleCarga);
                if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
                    lacreControleCarga.setMeioTransporte(controleCarga.getMeioTransporteByIdSemiRebocado());
                } else if (controleCarga.getMeioTransporteByIdTransportado() != null) {
                    lacreControleCarga.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
                }
                lacreControleCarga.setFilialByIdFilialInclusao(SessionUtils.getFilialSessao());
                lacreControleCarga.setUsuarioByIdFuncInclusao(SessionUtils.getUsuarioLogado());
                lacreControleCarga.setObInclusaoLacre(null);
                lacreControleCarga.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
                listLacreControleCarga.add(lacreControleCarga);
            }
        }
    }

    public void storeStatusCarregamentoSorter(String sgFilial, String tpStatusConhecimento, Long idControleCarga, String tpStatus, BigDecimal pcOciosidadeVisual,
                                              String[] lacres) {

        if (lacres == null || lacres.length == 0) {
            throw new BusinessException("LMS-05149");
        }

        ControleCarga controleCarga = controleCargaService.findById(idControleCarga);

        BigDecimal percent = BigDecimalUtils.HUNDRED;
        percent = percent.subtract(pcOciosidadeVisual);
        controleCarga.setPcOcupacaoInformado(percent);
		
		/* Exclui os lacres  */
        List<Map> lacresOld = lacreControleCargaService.findByControleCarga(idControleCarga);
        for (Map lacre : lacresOld) {
            lacreControleCargaService.removeById(Long.parseLong(lacre.get("idLacreControleCarga").toString()));
        }
		/* ===================== */

        List<LacreControleCarga> listLacreControleCarga = new ArrayList<LacreControleCarga>();
        adicionarLacres(lacres, controleCarga, listLacreControleCarga);

        if (!listLacreControleCarga.isEmpty()) {
            lacreControleCargaService.storeLacresControleCarga(listLacreControleCarga);
        }

        CarregamentoDescarga carregamentoDescarga = this.findCarregamentoByControleCarga(idControleCarga);
        if (carregamentoDescarga.getBox() != null) {
            carregamentoDescarga.getBox().setTpSituacaoBox(new DomainValue("L"));
            if (carregamentoDescarga.getBox().getDoca() != null) {
                carregamentoDescarga.getBox().getDoca().setTpSituacaoDoca(new DomainValue("L"));
            }
        }
        this.storeStatusCarregamento(carregamentoDescarga, tpStatus);
		/*
		 * CQPRO00028189 - Atualiza informação do percentual de ocupação
		 * infromado.
		 */
        controleCargaService.store(controleCarga);
    }

    public void storeEventoCarregamentoDoctoServico(Long idDoctoServico, String tpManifesto, String nrPreManifesto,
                                                    DateTime dataHora) {
        List<Short> eventos = this.getEventosCarregamento(tpManifesto);
        this.generateEventosDoctoServico(idDoctoServico, nrPreManifesto, dataHora, tpManifesto, eventos);
    }

    //LMSA-4785
    public void storeEventoCarregamentoDoctoServicoComBatch(DoctoServico doctoServico, String tpManifesto, String nrPreManifesto,
                                                            DateTime dataHora, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        List<Short> eventos = this.getEventosCarregamento(tpManifesto);
        this.generateEventosDoctoServicoComBatch(doctoServico, nrPreManifesto, dataHora, tpManifesto, eventos,
                adsmNativeBatchSqlOperations);
    }

    private void storeEventoCarregamentoVolume(VolumeNotaFiscal volume, String tpManifesto, String tpScan) {
        List<Short> eventos = this.getEventosCarregamento(tpManifesto);
        this.generateEventosVolume(volume, tpScan, eventos);
    }

    private void storeEventoCarregamentoDispositivo(Long idDispositivo, String tpManifesto, String tpScan) {
        List<Short> eventos = this.getEventosCarregamento(tpManifesto);
        this.generateEventosDispositivo(idDispositivo, tpScan, eventos);
    }

    private void removeEventoCarregamentoDispositivo(Long idDispositivo, String tpManifesto, String tpScan) {
        List<Short> eventos = this.getEventosCancelaCarregamento(tpManifesto);
        this.generateEventosDispositivo(idDispositivo, tpScan, eventos);
    }

    private List<Short> getEventosCarregamento(String tpManifesto) {
        List<Short> eventos = new ArrayList<Short>();
        if ("E".equals(tpManifesto)) {
			/* Pré Manifesto */
            eventos.add(Short.valueOf("62"));
			/* Em Carregamento */
            eventos.add(Short.valueOf("24"));
        } else {
			/* Pré Manifesto */
            eventos.add(Short.valueOf("61"));
			/* Em Carregamento */
            eventos.add(Short.valueOf("25"));
        }
        return eventos;
    }

    private List<Short> getEventosCancelaCarregamento(String tpManifesto) {
        List<Short> eventos = new ArrayList<Short>();
        if ("E".equals(tpManifesto)) {
			/* Cancela Em Carregamento */
            eventos.add(Short.valueOf("123"));
			/* Cancela Pré Manifesto */
            eventos.add(Short.valueOf("86"));
        } else {
			/* Cancela Em Carregamento */
            eventos.add(Short.valueOf("124"));
			/* Cancela Pré Manifesto */
            eventos.add(Short.valueOf("63"));
        }
        return eventos;
    }

    private void generateEventosVolume(VolumeNotaFiscal volume, String tpScan, List<Short> cdEventos) {
        for (Short cdEvento : cdEventos) {
            eventoVolumeService.generateEventoVolume(volume, cdEvento, tpScan);
        }
    }

    private void generateEventosDispositivo(Long idDispositivo, String tpScan, List<Short> cdEventos) {
        for (Short cdEvento : cdEventos) {
            eventoDispositivoUnitizacaoService.generateEventoDispositivo(idDispositivo, cdEvento, tpScan, null);
        }
    }

    private void generateEventosDoctoServico(Long idDoctoServico, String nrPreManifesto, DateTime dataHora,
                                             String tpManifesto, List<Short> cdEventos) {
        for (Short cdEvento : cdEventos) {
            eventoRastreabilidadeService.generateEventoDocumento(cdEvento, idDoctoServico, SessionUtils
                    .getFilialSessao().getIdFilial(), nrPreManifesto, dataHora, null, null, "PM" + tpManifesto); // PME
            // -
            // Pré
            // Manifesto
            // de
            // Entrega
            // ; PMV - Pré Manifesto de
            // Viagem
        }
    }

    //LMSA-4785
    private void generateEventosDoctoServicoComBatch(
            DoctoServico doctoServico, String nrPreManifesto, DateTime dataHora,
            String tpManifesto, List<Short> cdEventos, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {
        for (Short cdEvento : cdEventos) {

            Map<String, Object> valoresMap = new HashMap<String, Object>();
            valoresMap.put("cdEvento", cdEvento);
            valoresMap.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
            valoresMap.put("nrDocumento", nrPreManifesto);
            valoresMap.put("dhEvento", dataHora);
            valoresMap.put("idPedidoCompra", null);
            valoresMap.put("dsObservacao", null);
            valoresMap.put("tpDocumento", "PM" + tpManifesto);
            valoresMap.put("idOcorrenciaEntrega", null);
            valoresMap.put("idOcorrenciaPendencia", null);
            valoresMap.put("armazenaDadosDocto", false);

            eventoRastreabilidadeService.generateEventoDocumentoComBatch(doctoServico,
                    valoresMap, adsmNativeBatchSqlOperations); // PME
        }
    }

    public List<VolumeNotaFiscal> findVolumesByDispositivoUnitizacao(DispositivoUnitizacao dispositivo) {
        List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findVolumeByIdDispositivoUnitizacao(dispositivo
                .getIdDispositivoUnitizacao());
        dispositivo.setVolumes(volumes);
        if (dispositivo.getVolumes() != null) {
            volumes.addAll(dispositivo.getVolumes());
        }

        List<DispositivoUnitizacao> dispositivos = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivo
                .getIdDispositivoUnitizacao());
        dispositivo.setDispositivosUnitizacao(dispositivos);
        for (DispositivoUnitizacao filho : dispositivos) {
            volumes.addAll(this.findVolumesByDispositivoUnitizacao(filho));
        }

        return volumes;
    }
    
    public TypedFlatMap executeValidacaoMensagensPGR(Long idControleCarga){
    	/*PROCESSO DE ENQUADRAMENTOS E EXIGÊNCIAS PGR */
    	PlanoGerenciamentoRiscoDTO planoPGR =  new PlanoGerenciamentoRiscoDTO();		
		if (planoUtils.findParametroFilial(PA_SGR_VAL_EXIST_PGR)) {
			planoPGR = planoGerenciamentoRiscoService.executeVerificarEnquadramentoRegra(idControleCarga);
			planoGerenciamentoRiscoService.generateExigenciasGerRisco(planoPGR);
			TypedFlatMap mapRetornoPGR = controleCargaService.validateEnquadramentosEWorkflows(planoPGR);

			if (!mapRetornoPGR.isEmpty()) {
				return mapRetornoPGR;
			}
		}
		return new TypedFlatMap();
    }

    /* Busca o carregamento a partir do controle de cargas */
    public CarregamentoDescarga findCarregamentoByControleCarga(Long idControleCarga) {
        CarregamentoDescarga carregamento = null;
        if (idControleCarga == null) {
            carregamento = null;
        } else {
            carregamento = carregamentoDescargaService.findCarregamentoDescarga(idControleCarga, SessionUtils.getFilialSessao().getIdFilial(), "C");
        }
        return carregamento;
    }

    public DomainValue findTpStatusCarregamento(CarregamentoDescarga carregamento) {
        if (carregamento == null) {
            return domainValueService.findDomainValueByValue("DM_STATUS_CARREG_DESCARGA", "I");
        } else {
            return carregamento.getTpStatusOperacao();
        }
    }

    /**
     * Consulta paginada que retorna documentos de serviço com DPE atrasado a a
     * partir de informações de carregamento
     *
     * @param idControleCarga
     * @param findDefinition
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedDoctoServicoWithDpeAtrasado(Long idControleCarga,
                                                                                       FindDefinition findDefinition) {
        ControleCarga controleCarga = controleCargaService.findById(idControleCarga);

        Long idRotaColetaEntrega = null;
        if (controleCarga.getRotaColetaEntrega() != null) {
            idRotaColetaEntrega = controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega();
        }

        List<Map<String, Long>> listaManifestos = new ArrayList<Map<String, Long>>();
        List<Manifesto> manifestos = controleCarga.getManifestos();
        adicionarFilialOrigemEDestinoMapManifesto(listaManifestos, manifestos);

        return doctoServicoService.findPaginatedDoctoServicoWithDpeAtrasado(controleCarga
                        .getFilialByIdFilialAtualizaStatus().getIdFilial(), controleCarga.getTpControleCarga().getValue(),
                listaManifestos, idRotaColetaEntrega, findDefinition);
    }

    private void adicionarFilialOrigemEDestinoMapManifesto(List<Map<String, Long>> listaManifestos, List<Manifesto> manifestos) {
        for (Manifesto manifesto : manifestos) {
            if (manifesto.getFilialByIdFilialOrigem().getIdFilial().compareTo(
                    SessionUtils.getFilialSessao().getIdFilial()) == 0) {
                Map<String, Long> mapManifeto = new HashMap<String, Long>();
                mapManifeto.put("idFilialOrigem", manifesto.getFilialByIdFilialOrigem().getIdFilial());
                mapManifeto.put("idFilialDestino", manifesto.getFilialByIdFilialDestino().getIdFilial());
                listaManifestos.add(mapManifeto);
            }
        }
    }

    /**
     * Faz uma busca paginada retornando os documentos de serviço de um
     * determinado controle de carga onde não foram carregados todos os volumes
     * do documento, isto é, onde a quantidades de volumes do documento é
     * diferente da quantidade de volumes carregados no controle de carga
     *
     * @param idControleCarga
     * @param findDef
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedDoctoServicoWithDivergenciaCarregamento(
            Long idControleCarga, FindDefinition findDef) {
        return doctoServicoService.findPaginatedDoctoServicoWithDivergenciaCarregamento(idControleCarga, findDef);
    }

    /**
     * Consulta paginada para Documentos de Servico que possuem priorização de
     * embarque, a partir de informações de carregamento
     *
     * @param idControleCarga
     * @param findDefinition
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedDoctoServicoWithPriorizacaoEmbarque(Long idControleCarga,
                                                                                               FindDefinition findDefinition) {
        ControleCarga controleCarga = controleCargaService.findById(idControleCarga);

        Long idRotaColetaEntrega = null;
        if (controleCarga.getRotaColetaEntrega() != null) {
            idRotaColetaEntrega = controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega();
        }

        List<Map<String, Long>> listaManifestos = new ArrayList<Map<String, Long>>();
        List<Manifesto> manifestos = controleCarga.getManifestos();
        adicionarFilialOrigemEDestinoMapManifesto(listaManifestos, manifestos);

        return doctoServicoService.findPaginatedDoctoServicoWithPriorizacaoEmbarque(controleCarga
                        .getFilialByIdFilialAtualizaStatus().getIdFilial(), controleCarga.getTpControleCarga().getValue(),
                listaManifestos, idRotaColetaEntrega, findDefinition);
    }

    /**
     * Consulta paginada que retorna documentos de serviço com serviço
     * prioritário a a partir de informações de carregamento
     *
     * @param idControleCarga
     * @param findDefinition
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedDoctoServicoWithServicoPrioritario(Long idControleCarga,
                                                                                              FindDefinition findDefinition) {
        ControleCarga controleCarga = controleCargaService.findById(idControleCarga);

        Long idRotaColetaEntrega = null;
        if (controleCarga.getRotaColetaEntrega() != null) {
            idRotaColetaEntrega = controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega();
        }

        List<Map<String, Long>> listaManifestos = new ArrayList<Map<String, Long>>();
        List<Manifesto> manifestos = controleCarga.getManifestos();
        adicionarFilialOrigemEDestinoMapManifesto(listaManifestos, manifestos);

        return doctoServicoService.findPaginatedDoctoServicoWithServicoPrioritario(controleCarga
                        .getFilialByIdFilialAtualizaStatus().getIdFilial(), controleCarga.getTpControleCarga().getValue(),
                listaManifestos, idRotaColetaEntrega, findDefinition);
    }

    /***********************************************************************************************************************************/

    public boolean validaLocalizacaoMercadoria(String tpManifesto, LocalizacaoMercadoria localizacaoMercadoria, Filial filialLocalizacao) {
        if (localizacaoMercadoria != null) {
            Short cdLocalizacaoMercadoria = localizacaoMercadoria.getCdLocalizacaoMercadoria();
            if (cdLocalizacaoMercadoria == null
                    || (!cdLocalizacaoMercadoria.equals(Short.valueOf("24")) &&
                    !cdLocalizacaoMercadoria.equals(Short.valueOf("28")) &&
                    !cdLocalizacaoMercadoria.equals(Short.valueOf("33")) &&
                    !cdLocalizacaoMercadoria.equals(Short.valueOf("34")) &&
                    !cdLocalizacaoMercadoria.equals(Short.valueOf("35")) &&
                    !cdLocalizacaoMercadoria.equals(Short.valueOf("43")))
                    || !filialLocalizacao.getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())
                    ) {
                return false;
            }
        }
        return true;
    }

    public void validaLocalizacaoMercadoriaAguardandoDescarga(String tpManifesto, LocalizacaoMercadoria localizacaoMercadoria, Filial filialLocalizacao) {
        if (localizacaoMercadoria != null) {
            Short cdLocalizacaoMercadoria = localizacaoMercadoria.getCdLocalizacaoMercadoria();
            if ((cdLocalizacaoMercadoria == null || cdLocalizacaoMercadoria.equals(Short.valueOf("37")) || cdLocalizacaoMercadoria.equals(Short.valueOf("38")))
                    && filialLocalizacao.getIdFilial().equals(SessionUtils.getFilialSessao().getIdFilial())
                    ) {
                throw new BusinessException("LMS-45099");
            }
        }
    }

    public void validaLocalizacaoMercadoria(String tpManifesto, LocalizacaoMercadoria localizacaoMercadoria) {
        if (localizacaoMercadoria != null) {
			/* Pega o código da localização para validação */
            Short cdLocalizacaoMercadoria = localizacaoMercadoria.getCdLocalizacaoMercadoria();

            // LMS-593: Caso o documento não esteja 'No Terminal' e 'Em Descarga'.
            if (cdLocalizacaoMercadoria == null
                    || (!cdLocalizacaoMercadoria.equals(Short.valueOf("24")) &&
                    !cdLocalizacaoMercadoria.equals(Short.valueOf("28")) &&
                    !cdLocalizacaoMercadoria.equals(Short.valueOf("33")) &&
                    !cdLocalizacaoMercadoria.equals(Short.valueOf("34")) &&
                    !cdLocalizacaoMercadoria.equals(Short.valueOf("35")) &&
                    !cdLocalizacaoMercadoria.equals(Short.valueOf("43"))
            )) {
                throw new BusinessException("LMS-45039");
            }
        }
    }

    public List<SolicitacaoContratacao> findSolicitacoesContratacaoAprovadas(MeioTransporte meioTransporte) {
		/* Pega o id da filial na qual o veículo é agregado */
        Long idFilialAgregado = null;
        if (meioTransporte.getFilialAgregadoCe() != null) {
            idFilialAgregado = meioTransporte.getFilialAgregadoCe().getIdFilial();
        }
		
		/* Busca solicitações de contratação Aprovadas */
        return solicitacaoContratacaoService.validadeMeioTransporteContratacaoAprovada(meioTransporte
                .getNrIdentificador(), meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte()
                .getIdTipoMeioTransporte(), null, idFilialAgregado);
    }

    //FIXME Verificar possibilidade de mandar método pra LUA
    public void validateSolicitacaoContratacao(MeioTransporte meioTransporte, String tpRotaViagem,
                                               Long idTipoMeioTransporteRota) {
        String tpVinculo = meioTransporte.getTpVinculo().getValue();
        Long idTipoMeioTransporte = meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte()
                .getIdTipoMeioTransporte();
		
		/* Busca solicitações de contratação Aprovadas */
        List<SolicitacaoContratacao> solicitacoes = this.findSolicitacoesContratacaoAprovadas(meioTransporte);
		
		/* Se não possui solicitacao de contratação */
        if (solicitacoes.isEmpty()) {
			/* E se tipo de vinculo for eventual ou agregado, levanta exceção */
            if ("E".equals(tpVinculo) || "A".equals(tpVinculo)) {
				/*
				 * Para efetuar o cadastro desse meio de transporte é necessário
				 * que o mesmo possua uma solicitação de contratação aprovada
				 * para este tipo de meio de transporte e para o tipo de vínculo
				 * escolhido.
				 */
                throw new BusinessException("LMS-26012");
            } else {
				/*
				 * Se for veículo próprio e o mesmo não é do tipo aceito para a
				 * rota, levanta exceção
				 */
                if ("P".equals(tpVinculo) && ("EX".equals(tpRotaViagem) || "EC".equals(tpRotaViagem))
                        && !idTipoMeioTransporte.equals(idTipoMeioTransporteRota)) {
					/*
					 * Este tipo de meio de transporte não esta vinculado a rota
					 * informada. O campo Solicitação de Contratação deverá ser
					 * informado.
					 */
                    throw new BusinessException("LMS-05132");
                }
            }
        } else {
			/* Pega a solicitação aprovada mais recente */
            SolicitacaoContratacao sc = solicitacoes.get(0);
            if ("A".equals(tpVinculo) && meioTransporte.getFilialAgregadoCe() != null
                    && !sc.getFilial().getIdFilial().equals(meioTransporte.getFilialAgregadoCe().getIdFilial())) {
				/*
				 * A filial onde o meio de transporte será agregado deve ser
				 * informada e deve ser igual a filial da última contratação.
				 */
                throw new BusinessException("LMS-26084");
            }
            solicitacaoContratacaoService.validateExistSolicitacaoContratacao(SessionUtils.getFilialSessao().getIdFilial(), sc.getIdSolicitacaoContratacao());
			
			/* Se a solicitação aprovada possuir rotaIdaVolta */
            if (sc.getRotaIdaVolta() != null) {
				/*
				 * Se o tipo da rota viagem for diferente do tipo da rota viagem
				 * aprovada
				 */
                if (!tpRotaViagem.equals(sc.getRotaIdaVolta().getRotaViagem().getTpRota().getValue())) {
                    String tipoRota = domainValueService.findDomainValueDescription("DM_TIPO_ROTA_VIAGEM_CC", sc.getRotaIdaVolta().getRotaViagem().getTpRota().getValue());
					/* Esta Solicitação de Contratação é para a Rota {0}. */
                    throw new BusinessException("LMS-05128", new Object[]{tipoRota});
                }
			/* Senão, se rotaIdaVolta == null e se solicitação possui rota */
            } else if (sc.getRota() != null && !"EV".equals(tpRotaViagem)) {
				/* Se tipo de viagem NÃO for Eventual */
                String tipoRota = domainValueService.findDomainValueDescription("DM_TIPO_ROTA_VIAGEM_CC", "EV");
					/* Esta Solicitação de Contratação é para a Rota {0}. */
                throw new BusinessException("LMS-05128", new Object[]{tipoRota});
            }
        }
    }

    public Equipe getEquipeFromParametroGeral(String key) {
        BigDecimal idEquipeBD = (BigDecimal) parametroGeralService.findConteudoByNomeParametro(key, false);
        return equipeService.findById(idEquipeBD.longValue());
    }

    public List<TrechoRotaIdaVolta> findRotasViagem(Long idFilial) {
        return rotaIdaVoltaService.findRotasViagemByIdFilialOrigem(idFilial);
    }

    public List<RotaColetaEntrega> findRotasEntrega(Long idFilial) {
        return rotaEntregaService.findRotaColetaEntrega(idFilial);
    }

    /**
     * Valida se o dispositivo de unitização tem algum conhecimento para poder
     * iniciar o carregamento.
     *
     * @param nrCodigoBarras
     */
    public void validateDispositivoUnitizacaoIsEmpty(String nrCodigoBarras) {
        DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findByBarcode(nrCodigoBarras);

        if (dispositivo == null) {
            // Dispositivo unitização não foi encontrado.
            throw new BusinessException(ConsErro.DISP_UNITIZACAO_N_ENCONTRADO);
        }

        if (validateDUIsEmpty(dispositivo.getIdDispositivoUnitizacao())) {
            // O primeiro volume lido para iniciar um pré-manifesto
            // obrigatoriamente deve existir CTRC com status na filial.
            throw new BusinessException(ConsErro.PRIM_VOL_P_PREMANIF_OBRIG_EXIST_CTRC_STAT_FIL);
        }
    }

    private Boolean validateDUIsEmpty(Long idDispositivoUnitizacao) {
        List<DispositivoUnitizacao> dispositivosFilhos = dispositivoUnitizacaoService
                .findDispositivosByIdPai(idDispositivoUnitizacao);
        List lstConhecimentos = dispositivoUnitizacaoService
                .findConhecimentoByDispositivoUnitizacao(idDispositivoUnitizacao);

        if (!dispositivosFilhos.isEmpty()) {
            for (DispositivoUnitizacao dispUnit : dispositivosFilhos) {
                if (validateDUIsEmpty(dispUnit.getIdDispositivoUnitizacao()).equals(Boolean.FALSE)) {
                    return false;
                }
            }
        }

        return lstConhecimentos.isEmpty();
    }

    /*************************************************************************************************************************************/
	/*
	 * SETS DAS SERVICES
	 */
    public void setAgendamentoDoctoServicoService(AgendamentoDoctoServicoService agendamentoDoctoServicoService) {
        this.agendamentoDoctoServicoService = agendamentoDoctoServicoService;
    }

    public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
        this.carregamentoDescargaService = carregamentoDescargaService;
    }

    public void setCarregamentoPreManifestoService(CarregamentoPreManifestoService carregamentoPreManifestoService) {
        this.carregamentoPreManifestoService = carregamentoPreManifestoService;
    }

    public void setConferirVolumeService(ConferirVolumeService conferirVolumeService) {
        this.conferirVolumeService = conferirVolumeService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    public void setControleTrechoService(ControleTrechoService controleTrechoService) {
        this.controleTrechoService = controleTrechoService;
    }

    public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
        this.conversaoMoedaService = conversaoMoedaService;
    }

    public void setDispCarregIdentificadoService(DispCarregIdentificadoService dispCarregIdentificadoService) {
        this.dispCarregIdentificadoService = dispCarregIdentificadoService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public void setDocumentoMirService(DocumentoMirService documentoMirService) {
        this.documentoMirService = documentoMirService;
    }

    public void setDomainValueService(DomainValueService domainValueService) {
        this.domainValueService = domainValueService;
    }

    public void setEquipeService(EquipeService equipeService) {
        this.equipeService = equipeService;
    }

    public void setEventoDispositivoUnitizacaoService(EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
        this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
    }

    public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
        this.eventoVolumeService = eventoVolumeService;
    }

    public void setFilialRotaCcService(FilialRotaCcService filialRotaCcService) {
        this.filialRotaCcService = filialRotaCcService;
    }

    public void setFilialRotaService(FilialRotaService filialRotaService) {
        this.filialRotaService = filialRotaService;
    }

    public void setEventoRastreabilidadeService(
            IncluirEventosRastreabilidadeInternacionalService eventoRastreabilidadeService) {
        this.eventoRastreabilidadeService = eventoRastreabilidadeService;
    }

    public void setLacreControleCargaService(LacreControleCargaService lacreControleCargaService) {
        this.lacreControleCargaService = lacreControleCargaService;
    }

    public void setMacroZonaService(MacroZonaService macroZonaService) {
        this.macroZonaService = macroZonaService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
        this.meioTransporteService = meioTransporteService;
    }

    public void setMeioTransporteRodoviarioService(MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
        this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
    }

    public void setOrdemFilialFluxoService(OrdemFilialFluxoService ordemFilialFluxoService) {
        this.ordemFilialFluxoService = ordemFilialFluxoService;
    }

    public void setOrdemSaidaService(OrdemSaidaService ordemSaidaService) {
        this.ordemSaidaService = ordemSaidaService;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setPostoPassagemCcService(PostoPassagemCcService postoPassagemCcService) {
        this.postoPassagemCcService = postoPassagemCcService;
    }

    public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
        this.preManifestoDocumentoService = preManifestoDocumentoService;
    }

    public void setRotaEntregaService(RotaColetaEntregaService rotaEntregaService) {
        this.rotaEntregaService = rotaEntregaService;
    }

    public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
        this.rotaIdaVoltaService = rotaIdaVoltaService;
    }

    public void setSolicitacaoContratacaoService(SolicitacaoContratacaoService solicitacaoContratacaoService) {
        this.solicitacaoContratacaoService = solicitacaoContratacaoService;
    }

    public void setSolicitacaoRetiradaService(SolicitacaoRetiradaService solicitacaoRetiradaService) {
        this.solicitacaoRetiradaService = solicitacaoRetiradaService;
    }

    public void setSubstAtendimentoFilialService(SubstAtendimentoFilialService substAtendimentoFilialService) {
        this.substAtendimentoFilialService = substAtendimentoFilialService;
    }

    public void setTrechoRotaIdaVoltaService(TrechoRotaIdaVoltaService trechoRotaIdaVoltaService) {
        this.trechoRotaIdaVoltaService = trechoRotaIdaVoltaService;
    }

    public void setUnitizacaoService(UnitizacaoService unitizacaoService) {
        this.unitizacaoService = unitizacaoService;
    }

    public EquipeOperacaoService getEquipeOperacaoService() {
        return equipeOperacaoService;
    }

    public void setEquipeOperacaoService(EquipeOperacaoService equipeOperacaoService) {
        this.equipeOperacaoService = equipeOperacaoService;
    }

    public void setConhecimentoService(ConhecimentoService conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }

    public ConhecimentoService getConhecimentoService() {
        return conhecimentoService;
    }

    public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
        this.meioTranspProprietarioService = meioTranspProprietarioService;
    }

    public MeioTranspProprietarioService getMeioTranspProprietarioService() {
        return meioTranspProprietarioService;
    }

    public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
        this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
    }

    public OcorrenciaDoctoServicoService getOcorrenciaDoctoServicoService() {
        return ocorrenciaDoctoServicoService;
    }

    public AdiantamentoTrechoService getAdiantamentoTrechoService() {
        return adiantamentoTrechoService;
    }

    public void setAdiantamentoTrechoService(
            AdiantamentoTrechoService adiantamentoTrechoService) {
        this.adiantamentoTrechoService = adiantamentoTrechoService;
    }

    public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
        this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
    }

    public OcorrenciaPendenciaService getOcorrenciaPendenciaService() {
        return ocorrenciaPendenciaService;
    }

    public EventoService getEventoService() {
        return eventoService;
    }

    public void setEventoService(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    public void setManifestoNacionalVolumeService(ManifestoNacionalVolumeService manisfestoNacionalVolumeService) {
        this.manifestoNacionalVolumeService = manisfestoNacionalVolumeService;
    }

    public void setPreManifestoVolumeService(
            PreManifestoVolumeService preManifestoVolumeService) {
        this.preManifestoVolumeService = preManifestoVolumeService;
    }

    public TipoDispositivoUnitizacaoService getTipoDispositivoUnitizacaoService() {
        return tipoDispositivoUnitizacaoService;
    }

    public void setTipoDispositivoUnitizacaoService(
            TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService) {
        this.tipoDispositivoUnitizacaoService = tipoDispositivoUnitizacaoService;
    }

    public CtoAwbService getCtoAwbService() {
        return ctoAwbService;
    }

    public void setCtoAwbService(CtoAwbService ctoAwbService) {
        this.ctoAwbService = ctoAwbService;
    }

    public AwbService getAwbService() {
        return awbService;
    }

    public void setAwbService(AwbService awbService) {
        this.awbService = awbService;
    }

    public void setMunicipioService(MunicipioService municipioService) {
        this.municipioService = municipioService;
    }
    
    public void setPlanoUtils(PlanoGerenciamentoRiscoUtils planoUtils) {
		this.planoUtils = planoUtils;
	}

	public void setPlanoGerenciamentoRiscoService(
			PlanoGerenciamentoRiscoService planoGerenciamentoRiscoService) {
		this.planoGerenciamentoRiscoService = planoGerenciamentoRiscoService;
	}

}
