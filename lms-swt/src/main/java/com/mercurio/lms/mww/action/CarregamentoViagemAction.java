package com.mercurio.lms.mww.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.carregamento.model.service.EquipeOperacaoService;
import com.mercurio.lms.carregamento.model.service.LacreControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.UnitizacaoService;
import com.mercurio.lms.carregamento.util.MeioTranspProprietarioBuilder;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.SolicitacaoContratacaoService;
import com.mercurio.lms.contratacaoveiculos.model.service.TipoMeioTransporteService;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.CodigoBarrasService;
import com.mercurio.lms.expedicao.model.service.ConferirVolumeService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialRota;
import com.mercurio.lms.municipios.model.Rota;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.municipios.model.service.FilialRotaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaIdaVoltaService;
import com.mercurio.lms.municipios.model.service.RotaService;
import com.mercurio.lms.municipios.model.service.TrechoRotaIdaVoltaService;
import com.mercurio.lms.mww.model.service.CarregamentoMobileService;
import com.mercurio.lms.portaria.model.Box;
import com.mercurio.lms.portaria.model.Doca;
import com.mercurio.lms.portaria.model.Terminal;
import com.mercurio.lms.portaria.model.service.BoxService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.mww.carregamentoViagemAction"
 */
public class CarregamentoViagemAction {		
	
	private static final String LMS_26044 = "LMS-26044";
	private CarregamentoMobileService carregamentoService;
	private CodigoBarrasService codigoBarrasService; 
	private ConferirVolumeService conferirVolumeService;	
	private ControleCargaService controleCargaService;	
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;	
	private FilialRotaService filialRotaService;
	private ManifestoService manifestoService;
	private MeioTransporteService meioTransporteService;
	private RotaIdaVoltaService rotaIdaVoltaService;
	private RotaService rotaService;
	private SolicitacaoContratacaoService solicitacaoContratacaoService;
	private TipoMeioTransporteService tipoMeioTransporteService;
	private UnitizacaoService unitizacaoService;
	private VolumeNotaFiscalService volumeService;	
	private EquipeOperacaoService equipeOperacaoService;
	private BoxService boxService;
	private LacreControleCargaService lacreControleCargaService;
	private TrechoRotaIdaVoltaService trechoRotaIdaVoltaService;
	private ConfiguracoesFacade configuracoesFacade;
	private FilialService filialService;
	private MeioTranspProprietarioService meioTranspProprietarioService;
	
	/* Manifesto de entrega */
	private static final String MANIFESTO_VIAGEM = "V";
	/* Controle de Carga de Coleta/Entrega */
	private static final String CONTROLE_CARGA_VIAGEM = "V";
	/* tpRota = Expressa */
	private static final String ROTA_EXPRESSA= "EX";
	
	private static final String CARREGAMENTO_CONCLUIDO_MWW = "O";
	
	/* 2. Scanear o código de identificação do meio de transporte */
	public Map findMeioTransporte(Map param) {			
		Long nrCodigoBarras = this.getLongProperty("nrCodigoBarras", param);
		MeioTransporte meioTransporte = carregamentoService.findMeioTransporteByBarCode(nrCodigoBarras);
		Map<String,Object> retorno = this.getMeioTransporteMapped(meioTransporte);			
		
		meioTranspProprietarioService.validateBloqueioMeioTransporte(MeioTranspProprietarioBuilder.buildFromMeioTransporte(meioTransporte));
		meioTranspProprietarioService.verificaSituacaoMeioTransporte(MeioTranspProprietarioBuilder.buildFromMeioTransporte(meioTransporte));
		
		/* a. Se o veiculo estiver associado a um Controle de Carga do tipo Viagem e com situação “GE” = “Gerado” */		
		ControleCarga carga = carregamentoService.findControleCargaAberto(meioTransporte.getIdMeioTransporte(), this.CONTROLE_CARGA_VIAGEM);
		if(carga != null) {				
			/* então o usuário será redirecionado ao carregamento deste veiculo */
			retorno.put("controleCarga", this.getControleCargaMapped(carga));		
			CarregamentoDescarga carregamentoDescarga = carregamentoService.findCarregamentoByControleCarga(carga.getIdControleCarga());
			if(carregamentoDescarga!=null){
				EquipeOperacao equipeOperacao = equipeOperacaoService.findEquipeOperacaoByIdControleCargaAndCarregamentoDescarga(carga.getIdControleCarga(), carregamentoDescarga.getIdCarregamentoDescarga());
				if(equipeOperacao!=null){
					getEquipeOperacaoService().storeIntegranteEquipeOperacao(equipeOperacao);
				}
			}
		} else {
			/* c. Se não, verificar se o veiculo pode iniciar um novo Controle de Carga. */ 			
			carregamentoService.validateMeioTransporte(meioTransporte);
		}
		
		return  retorno;
	}
	
	/* 3. a. ii. 1.	Aparecerá uma lista com todas as rotas onde a filial do usuário logado é origem de rota. */
	public Map findRotasViagem(Map param) {
		/* Pega o id do meio de transporte */
		Long idMeioTransporte = this.getLongProperty("idMeioTransporte", param);
		MeioTransporte meioTransporte = meioTransporteService.findById(idMeioTransporte);
				
		/* Busca solicitações de contratação Aprovadas */
		List<SolicitacaoContratacao> solicitacoes = carregamentoService.findSolicitacoesContratacaoAprovadas(meioTransporte);
			
		/* Pega o tipo da rota */
		String tpRotaViagem = (String)param.get("tpRotaViagem");
		if(tpRotaViagem.equals(ROTA_EXPRESSA)) {
			return this.findRotasViagemExpressa(meioTransporte, solicitacoes);
		} else {
			return this.findRotasViagemEventual(solicitacoes);
		}			
	}
	
	public Map findBoxByNrBox(Map param) {
		/* Pega o nr do box */
		Short nrBox = Short.parseShort(param.get("nrBox").toString());
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		
		/* Pega o(s) box(es) 
		 * */
		List<Box> boxes = boxService.findByNrBoxAndIdFilial(nrBox, idFilial);
		List<Map<String, Object>> boxesMapped = new ArrayList<Map<String,Object>>();
		
		for (Box box : boxes) {
			Map<String,Object> boxMap = new HashMap<String, Object>();
			boxMap.put("idBox", box.getIdBox());
			boxMap.put("nrBox", box.getNrBox());
			boxMap.put("dsBox", box.getDsBox());
			boxMap.put("tpSituacaoBox", box.getTpSituacaoBox().getValue());
			boxMap.put("obBox", box.getObBox());
			boxMap.put("doca", getDocaMapped(box.getDoca()));
			boxesMapped.add(boxMap);
		}
		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("boxes", boxesMapped);
			
		return retorno;	
	}
	
	private Map getDocaMapped(Doca doca) {
		Map<String, Object> docaMapped = new HashMap<String, Object>();
		docaMapped.put("idDoca", doca.getIdDoca());
		docaMapped.put("dsDoca", doca.getDsDoca());
		docaMapped.put("nrDoca", doca.getNrDoca());
		docaMapped.put("terminal", getTerminalMapped(doca.getTerminal()));
		return docaMapped;
	}

	private Map getTerminalMapped(Terminal terminal) {
		Map<String, Object> terminalMapped = new HashMap<String, Object>();
		terminalMapped.put("idTerminal", terminal.getIdTerminal());
		terminalMapped.put("obTerminal", terminal.getObTerminal());
		return terminalMapped;
	}
	
	public Map executeValidacaoMensagensPGR(Map param){
		Long idControleCarga = this.getLongProperty("idControleCarga", param);
		Map messages = carregamentoService.executeValidacaoMensagensPGR(idControleCarga);
		
		if(messages.containsKey("mensagensEnquadramento")
				|| messages.containsKey("mensagensWorkflowRegras")
				|| messages.containsKey("mensagensWorkflowExigencias")){
			throw new BusinessException("LMS-45214");
		}
		return new TypedFlatMap();
	}

	public Map findBoxesLiberadosVigentes(){
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		List<Box> boxes = boxService.findLiberadoVigenteByFilial(idFilial);
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		List<Map<String,Object>> boxesMapped = new ArrayList<Map<String,Object>>();
		for (Box box : boxes) {
			Map<String,Object> boxMapped = new HashMap<String, Object>();
			boxMapped.put("idBox", box.getIdBox());
			boxMapped.put("nrBox", box.getNrBox());
			boxMapped.put("dsBox", box.getNrBox().toString() + (box.getDsBox()!= null ? " - " + box.getDsBox() : ""));
			boxesMapped.add(boxMapped);			
		}
		map.put("boxes", boxesMapped);
		return map;
	}
	
	private Map findRotasViagemExpressa(MeioTransporte meioTransporte, List<SolicitacaoContratacao> solicitacoes) {						
		/* Prepara mapa com os tipos de meio de transporte (tipo do meio informado mais o tipo que podem carregá-lo) */
		Long idTipoMeioTransporte = meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte().getIdTipoMeioTransporte();		
		Boolean isSemiReboque = meioTransporteService.findMeioTransporteIsSemiReboque( meioTransporte.getIdMeioTransporte() );	
		List<TipoMeioTransporte> listaMeiosTransportesCompostos = tipoMeioTransporteService.findComposicoesByTipo(idTipoMeioTransporte);		
		HashMap<Long, Boolean> idsTipos = new HashMap<Long, Boolean>();
		idsTipos.put(idTipoMeioTransporte, Boolean.TRUE);
		for(TipoMeioTransporte tipo : listaMeiosTransportesCompostos) {
			idsTipos.put(tipo.getIdTipoMeioTransporte(), Boolean.TRUE);
		}
				
		String tpVinculo = meioTransporte.getTpVinculo().getValue();		
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();		
				
		Map<String, Object> retorno = new HashMap<String, Object>();
		List<Map<String, Object>> listRotas = new ArrayList<Map<String,Object>>(); 			
		
		Boolean showRota = false;
		Long idSolicitacaoContratacao = null;
		final int horasTempoLimite = ((BigDecimal)configuracoesFacade.getValorParametro("TEMPO_LIMITE_ROTA_DO_DIA")).intValue();
		List<TrechoRotaIdaVolta> rotas = carregamentoService.findRotasViagem(idFilial); 

		/* Percorre lista de rotas encontradas */
		for(TrechoRotaIdaVolta trechoRotaIdaVolta : rotas) {
			showRota = false;
			idSolicitacaoContratacao = null;
			RotaIdaVolta rotaIdaVolta = trechoRotaIdaVolta.getRotaIdaVolta();
			
			try {
				controleCargaService.validateRotaControleCarga(rotaIdaVolta.getIdRotaIdaVolta(), horasTempoLimite);

				/* Se vinculo for "P" ou "A" e tipo aceito na rota então deve exibir a rota de viagem */
				if( ( "P".equalsIgnoreCase(tpVinculo) || "A".equalsIgnoreCase(tpVinculo)  )
						&& idsTipos.containsKey(rotaIdaVolta.getRotaViagem().getTipoMeioTransporte().getIdTipoMeioTransporte())
				  ) {				
				showRota = true;				
				}else if( isSemiReboque ){
					showRota = true;
			}
			
				if(!showRota && !isSemiReboque) {
				/* Verifica se a rota em questão possui solicitação de contratação aprovada */
				for(SolicitacaoContratacao sc : solicitacoes) {
						if(sc.getRotaIdaVolta() != null && sc.getRotaIdaVolta().getIdRotaIdaVolta().equals(rotaIdaVolta.getIdRotaIdaVolta())) {
							idSolicitacaoContratacao = sc.getIdSolicitacaoContratacao();
							showRota = true;
							break;
						} 
				}
			}
			
				if(showRota && !existsIdRotaIdaVolta(listRotas, rotaIdaVolta.getIdRotaIdaVolta())) {
				Map<String, Object> rotaMapped = new HashMap<String, Object>();
				rotaMapped.put("idRota", rotaIdaVolta.getRota().getIdRota());
				rotaMapped.put("idRotaIdaVolta", rotaIdaVolta.getIdRotaIdaVolta());
				rotaMapped.put("dsRota", rotaIdaVolta.getRota().getDsRota());
				rotaMapped.put("nrRota", rotaIdaVolta.getNrRota());
				rotaMapped.put("idSolicitacaoContratacao", idSolicitacaoContratacao);
					rotaMapped.put("hrSaida", trechoRotaIdaVolta.getHrSaida().toString(DateTimeFormat.forPattern("HH:mm")));
					rotaMapped.put("blDom", trechoRotaIdaVolta.getBlDomingo());
					rotaMapped.put("blSeg", trechoRotaIdaVolta.getBlSegunda());
					rotaMapped.put("blTer", trechoRotaIdaVolta.getBlTerca());
					rotaMapped.put("blQua", trechoRotaIdaVolta.getBlQuarta());
					rotaMapped.put("blQui", trechoRotaIdaVolta.getBlQuinta());
					rotaMapped.put("blSex", trechoRotaIdaVolta.getBlSexta());
					rotaMapped.put("blSab", trechoRotaIdaVolta.getBlSabado());
					rotaMapped.put("obRotaIdaVolta", rotaIdaVolta.getObRotaIdaVolta());
				listRotas.add(rotaMapped);
			}
			} catch (Exception e) {
				//Ignora a exceção levantada
		}					
		}					
		
		retorno.put("rotas", listRotas);		
		return retorno;
	}
		
	private boolean existsIdRotaIdaVolta(List<Map<String, Object>> listRotas , Long idRotaIdaVolta){
		for (Map<String, Object> map : listRotas) {
			if (idRotaIdaVolta.equals((Long)map.get("idRotaIdaVolta"))) return true;
		}
		return false;
	}
		
	private Map findRotasViagemEventual(List<SolicitacaoContratacao> solicitacoes) {
		/* Pega o id do meio de transporte */
		Map<String, Object> retorno = new HashMap<String, Object>();
		List<Map<String, Object>> listRotas = new ArrayList<Map<String,Object>>(); 
		
		/* Verifica se a rota em questão possui solicitação de contratação aprovada */
		for(SolicitacaoContratacao sc : solicitacoes) {
			/* Adicionado pois no caso da solicitação de contratação não poder ser usada simplesmente não deve
			 * retornar a rota para o coletor */
			try {
				solicitacaoContratacaoService.validateExistSolicitacaoContratacao(
						SessionUtils.getFilialSessao().getIdFilial(), sc.getIdSolicitacaoContratacao());				
				
				boolean isCargaCompartilhada = sc.getTpCargaCompartilhada() != null;
				
				if(sc.getRota() != null) {
					Map<String, Object> rotaMapped = new HashMap<String, Object>();
					rotaMapped.put("idRota", sc.getRota().getIdRota());
					rotaMapped.put("dsRota", (isCargaCompartilhada ? "FDX* " : "") + sc.getRota().getDsRota());
					rotaMapped.put("idSolicitacaoContratacao", sc.getIdSolicitacaoContratacao());
					listRotas.add(rotaMapped);
				} 
			} catch(BusinessException be) {
				
		}	
		}	
		retorno.put("rotas", listRotas);		
		return retorno;
	}
	
	public Map storeControleCargaBlSorter(Map param) {
		String nrCodigoBarras = (String)param.get("nrCodigoBarras");											
		Boolean blConfirmado = Boolean.parseBoolean((String) param.get("blConfirma"));
		
		/* Pega parâmetros passados pelo .NET */
		Long idRota = this.getLongProperty("idRota", param);
		Long idRotaIdaVolta = this.getLongProperty("idRotaIdaVolta", param);
		Long idSolicitacaoContratacao = this.getLongProperty("idSolicitacaoContratacao", param);
		Long idMeioTransporte = this.getLongProperty("idMeioTransporte", param);
		String tpRotaViagem = (String)param.get("tpRotaViagem");
		String sorter= (String)param.get("blSorter");
		Boolean blSorter = "True".equals(sorter);
		
		Long idBox = null;
		/* Pega idBox se foi informado */
		if (param.containsKey("idBox")) 
			idBox = Long.parseLong((String) param.get("idBox"));	
		
		/* Cria um novo controle de Carga com os valores vindos por parâmetro */
		ControleCarga newCC = this.generateControleCarga(idMeioTransporte, idRota, idRotaIdaVolta, idSolicitacaoContratacao, tpRotaViagem);
			
		/* Monta o retorno */
		Boolean isVolume = codigoBarrasService.isVolume(nrCodigoBarras, true);
		Map<String, Object> controleCargaMapped = null;
				
		if(isVolume || codigoBarrasService.validatePadraoCodigoBarra(nrCodigoBarras)) {
			Map<String, Object> volumeMapped = null;
				volumeMapped = blSorter ? this.storeVolumeSorter(nrCodigoBarras, newCC, idBox, null, blConfirmado,blSorter)
										: this.storeVolume(nrCodigoBarras, newCC, idBox, null, blConfirmado);			
			List<Map<String,Object>> conhecimentos = new ArrayList<Map<String,Object>>();
			conhecimentos.add(volumeMapped);
			controleCargaMapped = this.getControleCargaMapped(newCC);					
			controleCargaMapped.put("conhecimentos", conhecimentos);
		} else {
			Map<String, Object> dispositivoMapped = this.storeDispositivo(nrCodigoBarras, newCC, idBox, null, blConfirmado);
			List<Map<String,Object>> dispositivos = new ArrayList<Map<String,Object>>();
			dispositivos.add(dispositivoMapped);
			controleCargaMapped = this.getControleCargaMapped(newCC);					
			controleCargaMapped.put("dispositivos", dispositivos);
		}
		
		return controleCargaMapped;
	}
	public Map storeControleCarga(Map param) {
		return storeControleCargaBlSorter(param);
	}	
	
	/* Cria um novo controle de carga e o retorna sem salvar */
	private ControleCarga generateControleCarga(Long idMeioTransporte, Long idRota, Long idRotaIdaVolta, Long idSolicitacaoContratacao, String tpRotaViagem) {
		ControleCarga newCC = new ControleCarga();
		Boolean isSemiReboque = meioTransporteService.findMeioTransporteIsSemiReboque(idMeioTransporte); 
		
		/* Verifica tipo do meio de transporte */
		if(isSemiReboque) {
			newCC.setMeioTransporteByIdSemiRebocado(meioTransporteService.findByIdInitLazyProperties(idMeioTransporte, false));			
		} else{
			newCC.setMeioTransporteByIdTransportado(meioTransporteService.findByIdInitLazyProperties(idMeioTransporte, false));
		}

		Rota rota = rotaService.findById(idRota);
		newCC.setRota(rota);
		
		if(idRotaIdaVolta != null) {
			RotaIdaVolta rotaIdaVolta = rotaIdaVoltaService.findById(idRotaIdaVolta);
			newCC.setRotaIdaVolta(rotaIdaVolta);
		}
		
		if(idSolicitacaoContratacao != null) {		
			SolicitacaoContratacao sc = solicitacaoContratacaoService.findById(idSolicitacaoContratacao);
			newCC.setSolicitacaoContratacao(sc);
			
			/* LMSA-6263 */
			if(sc.getTpCargaCompartilhada() != null && "C2".equals(sc.getTpCargaCompartilhada().getValue())){
            	throw new BusinessException("LMS-45213");
            }
		}
						
		newCC.setTpControleCarga(new DomainValue("V"));		
		newCC.setTpRotaViagem(new DomainValue(tpRotaViagem));
		
		return newCC;
	}
	
	public Map storeVolume(Map param) {
		/* Busca o volume a partir do código de barras vindo por parâmetro */
		String nrCodigoBarras = (String) param.get("nrCodigoBarras");

		/* Busca o controle de cargas a partir do id vindo no parâmetro */
		Long idControleCarga = Long.parseLong((String) param.get("idControleCarga"));
		ControleCarga cc = controleCargaService.findById(idControleCarga);
		
		/* Pega a filial de desvio no parametro de entrada */
		Long idFilialDesvio = null;
		if(param.get("idFilialDesvio") != null)
			idFilialDesvio = Long.parseLong((String) param.get("idFilialDesvio"));		
		
		Long idBox = null;
		/* Pega idBox se foi informado */
		if (param.containsKey("idBox")) 
			idBox = Long.parseLong((String) param.get("idBox"));	
		
		Boolean blConfirmado = Boolean.parseBoolean((String) param.get("blConfirma"));

		return this.storeVolume(nrCodigoBarras, cc, idBox, idFilialDesvio, blConfirmado);
	}		
	
	public Map storeVolumeBlsorter(Map param) {
		/* Busca o volume a partir do código de barras vindo por parâmetro */
		String nrCodigoBarras = (String) param.get("nrCodigoBarras");
		String sorter = (String)param.get("blSorter");
		Boolean blSorter = false;
		
		if("True".equals(sorter)){
			blSorter = true;
		}
		
		/* Busca o controle de cargas a partir do id vindo no parâmetro */
		Long idControleCarga = Long.parseLong((String) param.get("idControleCarga"));
		ControleCarga cc = controleCargaService.findById(idControleCarga);
		
		/* Pega a filial de desvio no parametro de entrada */
		Long idFilialDesvio = null;
		if(param.get("idFilialDesvio") != null)
			idFilialDesvio = Long.parseLong((String) param.get("idFilialDesvio"));		
		
		Long idBox = null;
		/* Pega idBox se foi informado */
		if (param.containsKey("idBox")) 
			idBox = Long.parseLong((String) param.get("idBox"));	
		
		Boolean blConfirmado = Boolean.parseBoolean((String) param.get("blConfirma"));

		return this.storeVolumeSorter(nrCodigoBarras, cc, idBox, idFilialDesvio, blConfirmado,blSorter);
	}		
	
	private Map<String, Object> storeVolumeSorter(String nrCodigoBarras, ControleCarga controleCarga, Long idBox, Long idFilialDesvio, Boolean blConfirmado,Boolean blSorter) {
		VolumeNotaFiscal volume = volumeService.findVolumeByBarCodeUniqueResult(nrCodigoBarras);
		List<Map<String, Object>> exceptions = new ArrayList<Map<String, Object>>(); 
		
		try {
		/* Tenta executar store do Volume, caso não consiga retorna lista de Exceções */
			exceptions = carregamentoService.storeVolumeBlsorter(volume, controleCarga, idBox, this.MANIFESTO_VIAGEM, ConstantesSim.TP_SCAN_FISICO, idFilialDesvio, !blConfirmado,blSorter);
		} catch (BusinessException e) {
			// Quando ocorre o bloqueio do meio de transporte é necessário armazenar a informação do bloqueio.
			if (LMS_26044.equals(e.getMessageKey()) && e.getMessageArguments() != null) {
				Object[] args = e.getMessageArguments();
				controleCargaService.storeBloqueioViagemEventual(Long.parseLong(args[0].toString()), Long.parseLong(args[1].toString()), Boolean.parseBoolean(args[2].toString()));
			}
			// Caso contrário passa quem sabe tratar o erro.
			throw e;
		}
		
		Map<String, Object> volumeMapped = this.getVolumeMapped(volume, controleCarga.getIdControleCarga());
		if (exceptions != null && !exceptions.isEmpty()) {
			volumeMapped.put("messages", exceptions);
		}

		return volumeMapped;
	}
		
	
	private Map<String, Object> storeVolume(String nrCodigoBarras, ControleCarga controleCarga, Long idBox, Long idFilialDesvio, Boolean blConfirmado) {
		/** LMS-1039 */
		final Map<String, String> alias = new HashMap<String, String>();
		alias.put("localizacaoMercadoria", "loc");
		alias.put("localizacaoFilial", "locFil");
		alias.put("notaFiscalConhecimento", "nfc");		
		alias.put("notaFiscalConhecimento.conhecimento", "con");
		alias.put("notaFiscalConhecimento.conhecimento.filialOrigem", "cfo");					
		alias.put("dispositivoUnitizacao", "du");
		alias.put("dispositivoUnitizacao.tipoDispositivoUnitizacao", "tdu");
		alias.put("dispositivoUnitizacao.dispositivoUnitizacaoPai", "dup");
		
		final VolumeNotaFiscal volumeNotaFiscal = volumeService.findVolumeByBarCodeUniqueResult(nrCodigoBarras, alias);

		List<Map<String, Object>> exceptions = new ArrayList<Map<String, Object>>(); 
		
		try {
		/* Tenta executar store do Volume, caso não consiga retorna lista de Exceções */
			exceptions = carregamentoService.storeVolume(volumeNotaFiscal, controleCarga, idBox, this.MANIFESTO_VIAGEM, ConstantesSim.TP_SCAN_FISICO, idFilialDesvio, !blConfirmado);
		} catch (BusinessException e) {
			// Quando ocorre o bloqueio do meio de transporte é necessário armazenar a informação do bloqueio.
			if (LMS_26044.equals(e.getMessageKey()) && e.getMessageArguments() != null) {
				Object[] args = e.getMessageArguments();
				controleCargaService.storeBloqueioViagemEventual(Long.parseLong(args[0].toString()), Long.parseLong(args[1].toString()), Boolean.parseBoolean(args[2].toString()));
			}
			// Caso contrário passa quem sabe tratar o erro.
			throw e;
		}
		
		Map<String, Object> volumeMapped = this.getVolumeMapped(volumeNotaFiscal, controleCarga.getIdControleCarga());
		if (exceptions != null && !exceptions.isEmpty()) {
			volumeMapped.put("messages", exceptions);
		}
		return volumeMapped;
	}
	
	public Map storeDispositivo(Map param) {
		/* Busca o volume a partir do código de barras vindo por parâmetro */
		String nrCodigoBarras = (String)param.get("nrCodigoBarras");		
		
		/* Busca o controle de cargas a partir do id vindo no parâmetro */
		Long idControleCarga = this.getLongProperty("idControleCarga",param);
		ControleCarga controleCarga = controleCargaService.findById(idControleCarga);			
		
		/* Pega varíavel que indica se já foram feitas as confirmações necessárias */
		Boolean blConfirmado = Boolean.parseBoolean((String)param.get("blConfirma"));	
		
		/* Pega a filial de desvio no parametro de entrada */
		Long idFilialDesvio = null;
		if(param.get("idFilialDesvio") != null) {
			idFilialDesvio = Long.parseLong((String) param.get("idFilialDesvio"));	
		}
		
		Long idBox = null;
		/* Pega idBox se foi informado */
		if (param.containsKey("idBox")) { 
			idBox = Long.parseLong((String) param.get("idBox"));	
		}
		
		return this.storeDispositivo(nrCodigoBarras, controleCarga, idBox, idFilialDesvio, blConfirmado);
	}
	
	private Map<String, Object> storeDispositivo(String nrCodigoBarras, ControleCarga controleCarga, Long idBox,  Long idFilialDesvio, Boolean blConfirmado) {	
		/* Busca o dispositivo de unitização pelo código de barras */
		DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findByBarcode(nrCodigoBarras);
		
		/* Busca carregamento aberto para o controle de carga, retorna null se não existe */
		CarregamentoDescarga carregamento = carregamentoService.findCarregamentoByControleCarga(controleCarga.getIdControleCarga());			
		List<Map<String, Object>> exceptions = new ArrayList<Map<String, Object>>();
		
		try {
		/* Chama método que grava o dispositivo de unitização */		
			exceptions = carregamentoService.storeDispositivo(controleCarga, idBox, dispositivo, carregamento, null, this.MANIFESTO_VIAGEM, ConstantesSim.TP_SCAN_FISICO, idFilialDesvio, !blConfirmado);
		} catch (BusinessException e) {
			// Quando ocorre o bloqueio do meio de transporte.
			if (LMS_26044.equals(e.getMessageKey()) && e.getMessageArguments() != null) {
				Object[] args = e.getMessageArguments();
				controleCargaService.storeBloqueioViagemEventual(Long.parseLong(args[0].toString()), Long.parseLong(args[1].toString()), Boolean.parseBoolean(args[2].toString()));
			}
			throw e;
		}
		
		/* Retorna dispositivo Mapeado */
		Map<String, Object> dispositivoMapped = new HashMap<String, Object>();
		
		if (exceptions != null && !exceptions.isEmpty()) {
			dispositivoMapped.put("messages", exceptions);
		} else {
			dispositivoMapped = unitizacaoService.findMapDispositivoUnitizacao(dispositivo, controleCarga.getIdControleCarga(), "C");
			verificaConhecimentoCompleto(controleCarga.getIdControleCarga(), (List<Map>)dispositivoMapped.get("conhecimentos"));
			verificaDispositivoCompleto(controleCarga.getIdControleCarga(), (List<Map>)dispositivoMapped.get("dispositivos"));
		}
		
		return dispositivoMapped;
	}
	
	private void verificaDispositivoCompleto(Long idControleCarga,	List<Map> list) {
		if(list != null){
			for (Map<String, Object> disp : list) {
				verificaConhecimentoCompleto(idControleCarga, (List<Map>)disp.get("conhecimentos"));
				verificaDispositivoCompleto(idControleCarga, (List<Map>)disp.get("dispositivos"));
			}
		}
	}
	private void verificaConhecimentoCompleto(Long idControleCarga,	List<Map> list) {
		if(list != null){
			for (Map<String, Object> con : list) {
				final Integer rowCountVolumes = volumeService.findRowCountVolumesCarregamentoDescargaConhecimento(idControleCarga, Long.parseLong(con.get("idDoctoServico").toString()), "C");
				con.put("completo", (rowCountVolumes == Integer.parseInt(con.get("qtVolumes").toString())));
			}
		}
	}

	public Map<String, Object> deleteVolume(Map param) {
		Long idControleCarga = Long.parseLong((String) param.get("idControleCarga"));
		String nrCodigoBarras = (String) param.get("nrCodigoBarras");
		Boolean blConfirmado = Boolean.parseBoolean((String) param.get("blConfirma"));			

		VolumeNotaFiscal volume = volumeService.findVolumeByBarCodeUniqueResult(nrCodigoBarras);
		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);

		List<Map<String, Object>> exceptions = new ArrayList<Map<String, Object>>();
		exceptions = carregamentoService.removeVolume(volume, controleCarga, ConstantesSim.TP_SCAN_FISICO, !blConfirmado);		

		Map<String, Object> conhecimento = new HashMap<String, Object>();
		conhecimento = this.getVolumeMapped(volume, idControleCarga);
		if (exceptions != null && !exceptions.isEmpty()) {
			conhecimento.put("messages", exceptions);	
		}		
		return conhecimento;
	}	
		
	public Map<String, Object> deleteDispositivo(Map param) {
		/* Busca o volume a partir do código de barras vindo por parâmetro */
		String nrCodigoBarras = (String)param.get("nrCodigoBarras");
		DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findByBarcode(nrCodigoBarras);
		
		/* Busca o controle de cargas a partir do id vindo no parâmetro */
		Long idControleCarga = this.getLongProperty("idControleCarga", param);
		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);			
		
		/* Pega varíavel que indica se já foram feitas as confirmações necessárias */
		Boolean blConfirmado = Boolean.parseBoolean((String)param.get("blConfirma"));	
		
		/* Busca carregamento aberto para o controle de carga, retorna null se não existe */
		CarregamentoDescarga carregamento = carregamentoService.findCarregamentoByControleCarga(idControleCarga);		
		
		List<Map<String, Object>> exceptions = 
			carregamentoService.removeDispositivo(controleCarga, dispositivo, carregamento, MANIFESTO_VIAGEM, ConstantesSim.TP_SCAN_FISICO, !blConfirmado);		
		
		/* Retorna dispositivo Mapeado */
		Map<String, Object> dispositivoMapped = new HashMap<String, Object>();
		
		if (exceptions != null && !exceptions.isEmpty()) {
			dispositivoMapped.put("messages", exceptions);
		} else {
			dispositivoMapped = unitizacaoService.findMapDispositivoUnitizacao(dispositivo);
		}
		
		return dispositivoMapped;
	}	
	
	public Map<String,Object> executeConferir(Map param) {
		Long idControleCarga = this.getLongProperty("idControleCarga",param);
		carregamentoService.storeStatusCarregamento(carregamentoService.findCarregamentoByControleCarga(idControleCarga), "I");
		return carregamentoService.findOciosidadeAndLacres(idControleCarga);
	}
	
	public void executeReiniciarCarregamento(Map param) {
		Long idControleCarga = Long.parseLong((String) param.get("idControleCarga"));
		carregamentoService.storeStatusCarregamento(carregamentoService.findCarregamentoByControleCarga(idControleCarga), "I");
	}
	
	public void executeConcluirCarregamento(Map param) {
		Long idControleCarga = this.getLongProperty("idControleCarga", param);
		BigDecimal ociosidadeVisual = new BigDecimal((String)param.get("ociosidadeVisual"));
		String lacresStr = (String)param.get("lacres");
		String[] lacres = lacresStr.split("\\|");
		String idFilial = (String)param.get("idFilial");
		Filial filial = filialService.findById(Long.valueOf(idFilial));
		
		carregamentoService.storeStatusCarregamento(filial.getSgFilial(),"P",idControleCarga,CARREGAMENTO_CONCLUIDO_MWW,ociosidadeVisual,lacres);
	}
	
	
	public Map<String,Object> findFaltaCarregar(Map param) {
		Long idControleCarga = this.getLongProperty("idControleCarga", param);
		Integer pageNumber = Integer.parseInt((String)param.get("pageNumber"));
		Integer pageSize = Integer.parseInt((String)param.get("pageSize"));				
		ResultSetPage<Map<String,Object>> rs = carregamentoService.findPaginatedDoctoServicoWithDivergenciaCarregamento(idControleCarga, this.getFindDefinition(pageNumber, pageSize)); 			
		return this.getTotalizadoresMapped(rs, pageSize);
	}
	
	public Map<String,Object> findDPEAtrasado(Map param) {
		Long idControleCarga = this.getLongProperty("idControleCarga", param);
		Integer pageNumber = Integer.parseInt((String)param.get("pageNumber"));
		Integer pageSize = Integer.parseInt((String)param.get("pageSize"));				
		ResultSetPage<Map<String,Object>> rs = carregamentoService.findPaginatedDoctoServicoWithDpeAtrasado(idControleCarga, this.getFindDefinition(pageNumber, pageSize)); 			
		return this.getTotalizadoresMapped(rs, pageSize);
	}
	
	public Map<String,Object> findServicoPrioritario(Map param) {
		Long idControleCarga = this.getLongProperty("idControleCarga", param);
		Integer pageNumber = Integer.parseInt((String)param.get("pageNumber"));
		Integer pageSize = Integer.parseInt((String)param.get("pageSize"));				
		ResultSetPage<Map<String,Object>> rs = carregamentoService.findPaginatedDoctoServicoWithServicoPrioritario(idControleCarga, this.getFindDefinition(pageNumber, pageSize)); 			
		return this.getTotalizadoresMapped(rs, pageSize);
	}
	
	public Map<String,Object> findPriorizacaoEmbarque(Map param) {
		Long idControleCarga = this.getLongProperty("idControleCarga", param);
		Integer pageNumber = Integer.parseInt((String)param.get("pageNumber"));
		Integer pageSize = Integer.parseInt((String)param.get("pageSize"));				
		ResultSetPage<Map<String,Object>> rs = carregamentoService.findPaginatedDoctoServicoWithPriorizacaoEmbarque(idControleCarga, this.getFindDefinition(pageNumber, pageSize)); 			
		return this.getTotalizadoresMapped(rs, pageSize);	
	}
	
	public Map<String,Object> findFiliaisDesvio(Map param) {
		Long idControleCarga = this.getLongProperty("idControleCarga", param);
		
		Map<String, Object> retorno = new HashMap<String, Object>();
		ControleCarga cc = controleCargaService.findById(idControleCarga);
		
		List<Map<String,Object>> filiais = new ArrayList<Map<String,Object>>();
		List<FilialRota> filiaisRota = filialRotaService.findFiliaisRestantesByRota(cc.getRota().getIdRota(), SessionUtils.getFilialSessao().getIdFilial());
		for(FilialRota filialRota : filiaisRota) {						
			List<Manifesto> manifestos = manifestoService.findManifestosByTrecho(MANIFESTO_VIAGEM, "EC", idControleCarga, SessionUtils.getFilialSessao().getIdFilial(), filialRota.getFilial().getIdFilial());
			
			if(manifestos != null && manifestos.size() > 0) {			
				Map<String,Object> filialMapped = new HashMap<String, Object>();
				filialMapped.put("idFilial", filialRota.getFilial().getIdFilial());
				filialMapped.put("sgFilial", filialRota.getFilial().getSgFilial());
				filiais.add(filialMapped);
			}
		}
		
		/* Caso não exista pre manifesto aberto para poder desviar, levanta exceção */
		if(filiais.size() == 0) {
			throw new BusinessException("LMS-45044");
		}
		
		retorno.put("filiais", filiais);
		return retorno;		
	}
	
	private FindDefinition getFindDefinition(Integer pageNumber, Integer pageSize) {
		return new FindDefinition(pageNumber, pageSize, null);
	}
	
	public Map<String,Object> getMeioTransporteMapped(MeioTransporte meioTransporte) {
		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("nrFrota", meioTransporte.getNrFrota());
		retorno.put("placa", meioTransporte.getNrIdentificador());
		retorno.put("idMeioTransporte", meioTransporte.getIdMeioTransporte());								
		return retorno;
	}
	
	private Map<String,Object> getTotalizadoresMapped(ResultSetPage<Map<String, Object>> rs, Integer pageSize) {
		Map<String,Object> retorno = new HashMap<String, Object>();		
		List<Map<String,Object>> itens = rs.getList();			
						
		BigDecimal bdRowCount = new BigDecimal(rs.getRowCount());
		BigDecimal bdPageSize = new BigDecimal(pageSize);
		
		Integer pageCount = bdRowCount.divide(bdPageSize,RoundingMode.DOWN).add(BigDecimal.valueOf(1)).intValue();		
		
		retorno.put("itens", itens);
		retorno.put("rowCount", rs.getRowCount());
		retorno.put("pageCount", pageCount);
		retorno.put("currentPage", rs.getCurrentPage());
		retorno.put("hasNext", rs.getHasNextPage());
		retorno.put("hasPrior", rs.getHasPriorPage());
		return retorno;
	}
	
	private Map<String,Object> getControleCargaMapped(ControleCarga cc) {
		Map<String, Object> retorno = new HashMap<String, Object>();
		if(cc != null) {
			retorno.put("conhecimentos", getConhecimentosControleCargaMapped(cc.getIdControleCarga(), cc.getTpControleCarga().getValue()));
			retorno.put("dispositivos", carregamentoService.findDispositivosControleCarga(cc.getIdControleCarga(), cc.getTpControleCarga().getValue()));
			retorno.put("idControleCarga", cc.getIdControleCarga());
			retorno.put("nrControleCarga", cc.getNrControleCarga());
			
			Map<String, Object> rota = new HashMap<String, Object>();
			
			/* LMSA-6370 */
			boolean isCargaCompartilhada = cc.getSolicitacaoContratacao() != null 
					&& cc.getSolicitacaoContratacao().getTpCargaCompartilhada()!=null;
			
			rota.put("dsRota", (isCargaCompartilhada ? "FDX* " : "") + cc.getRota().getDsRota());
			rota.put("idRota", cc.getRota().getIdRota());
						
			if(cc.getRotaIdaVolta() != null) {				
				TrechoRotaIdaVolta trechoRotaIdaVolta = trechoRotaIdaVoltaService.findTrechoByIdRotaIdaVoltaAndFilialUsuarioLogado(cc.getRotaIdaVolta().getIdRotaIdaVolta());
				rota.put("idRotaIdaVolta", cc.getRotaIdaVolta().getIdRotaIdaVolta());
				rota.put("nrRota", cc.getRotaIdaVolta().getNrRota());														
				rota.put("hrSaida", trechoRotaIdaVolta != null ? trechoRotaIdaVolta.getHrSaida().toString(DateTimeFormat.forPattern("HH:mm")) : null);
			}								
			retorno.put("rota", rota);
			
			if(cc.getFilialByIdFilialOrigem() != null) {
				Map<String, Object> filialOrigem = new HashMap<String, Object>();
				filialOrigem.put("sgFilial", cc.getFilialByIdFilialOrigem().getSgFilial());
				retorno.put("filialOrigem", filialOrigem);
			}					
		} else {
			retorno.put("idControleCarga", null);
			retorno.put("nrControleCarga", null);
		}			
	
		return retorno;
	}	
	
	private List<Map<String, Object>> getConhecimentosControleCargaMapped(Long idControleCarga, String tpControleCarga) {
		return carregamentoService.findConhecimentosControleCargaMapped(idControleCarga, tpControleCarga);
				}
		
	private Map<String,Object> getVolumeMapped(VolumeNotaFiscal volume, Long idControleCarga) {
		Map<String, Object> conhecimento = carregamentoService.findConhecimentoMappedByVolumeAndIdControleCarga(volume, idControleCarga, volume.getNrConhecimento());

		if(volume.getLocalizacaoMercadoria()!=null){
			Boolean isExtraviado = conferirVolumeService.isVolumeExtraviado(volume.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria());
			
			if(isExtraviado) {
				Map<String, Object> volumeMapped = new HashMap<String, Object>();
				volumeMapped.put("extraviado",isExtraviado);
				volumeMapped.put("nrSequencia", volume.getNrSequencia());
				volumeMapped.put("idVolumeNotaFiscal", volume.getIdVolumeNotaFiscal());		
			
				List<Map<String,Object>> volumes = new ArrayList<Map<String,Object>>();
				volumes.add(volumeMapped);
			
				conhecimento.put("volumes", volumes);
			}
		}
				
		return conhecimento;
	}

	private Long getLongProperty(String mapKey, Map mapa) {
		if(mapa.get(mapKey) != null) {
			return Long.parseLong((String) mapa.get(mapKey));
		} else {
			return null;
		}		
	}

	public void setCarregamentoService(CarregamentoMobileService carregamentoService) {
		this.carregamentoService = carregamentoService;
	}

	public void setCodigoBarrasService(CodigoBarrasService codigoBarrasService) {
		this.codigoBarrasService = codigoBarrasService;
	}

	public void setConferirVolumeService(ConferirVolumeService conferirVolumeService) {
		this.conferirVolumeService = conferirVolumeService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setDispositivoUnitizacaoService(
			DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}

	public void setFilialRotaService(FilialRotaService filialRotaService) {
		this.filialRotaService = filialRotaService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
		this.rotaIdaVoltaService = rotaIdaVoltaService;
	}

	public void setRotaService(RotaService rotaService) {
		this.rotaService = rotaService;
	}

	public void setSolicitacaoContratacaoService(
			SolicitacaoContratacaoService solicitacaoContratacaoService) {
		this.solicitacaoContratacaoService = solicitacaoContratacaoService;
	}
	
	public void setTipoMeioTransporteService(
			TipoMeioTransporteService tipoMeioTransporteService) {
		this.tipoMeioTransporteService = tipoMeioTransporteService;
	}

	public void setUnitizacaoService(UnitizacaoService unitizacaoService) {
		this.unitizacaoService = unitizacaoService;
	}

	public void setVolumeService(VolumeNotaFiscalService volumeService) {
		this.volumeService = volumeService;
	}		

	public EquipeOperacaoService getEquipeOperacaoService() {
		return equipeOperacaoService;
}

	public void setEquipeOperacaoService(EquipeOperacaoService equipeOperacaoService) {
		this.equipeOperacaoService = equipeOperacaoService;
	}

	public void setBoxService(BoxService boxService) {
		this.boxService = boxService;
	}

	public BoxService getBoxService() {
		return boxService;
	}

	public void setLacreControleCargaService(LacreControleCargaService lacreControleCargaService) {
		this.lacreControleCargaService = lacreControleCargaService;
	}

	public LacreControleCargaService getLacreControleCargaService() {
		return lacreControleCargaService;
	}		

	public void setTrechoRotaIdaVoltaService(TrechoRotaIdaVoltaService trechoRotaIdaVoltaService) {
		this.trechoRotaIdaVoltaService = trechoRotaIdaVoltaService;
}

	public TrechoRotaIdaVoltaService getTrechoRotaIdaVoltaService() {
		return trechoRotaIdaVoltaService;
	}		

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
}

	public FilialService getFilialService() {
		return filialService;
}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}
}
