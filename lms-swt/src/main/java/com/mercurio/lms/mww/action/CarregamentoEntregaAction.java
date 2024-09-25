package com.mercurio.lms.mww.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.carregamento.model.service.UnitizacaoService;
import com.mercurio.lms.carregamento.util.MeioTranspProprietarioBuilder;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.CodigoBarrasService;
import com.mercurio.lms.expedicao.model.service.ConferirVolumeService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.mww.model.service.CarregamentoMobileService;
import com.mercurio.lms.rnc.model.service.NaoConformidadeService;
import com.mercurio.lms.util.session.SessionUtils;

public class CarregamentoEntregaAction {

	private CarregamentoMobileService carregamentoService;
	private CodigoBarrasService codigoBarrasService;
	private ConferirVolumeService conferirVolumeService;
	private ControleCargaService controleCargaService;	
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private MeioTransporteService meioTransporteService;
	private UnitizacaoService unitizacaoService;
	private VolumeNotaFiscalService volumeService;
	private FilialService filialService;
	private ConhecimentoService conhecimentoService;
	// LMS-5719 - Indicador de RNC
	private NaoConformidadeService naoConformidadeService;	

	/* Manifesto de entrega */
	private static final String MANIFESTO_ENTREGA = "E";
	/* Controle de Carga de Coleta/Entrega */
	private static final String CONTROLE_CARGA_COLETA_ENTREGA = "C";
	/* TpScan = Scan Físico */
	private static final String SCAN_FISICO = "SF";
	
	private static final String CARREGAMENTO_CONCLUIDO_MWW = "O";
	
	private MeioTranspProprietarioService meioTranspProprietarioService;
	
	/* 2. Scanear o código de identificação do meio de transporte */
	public Map findMeioTransporte(Map param) {
		Long nrCodigoBarras = Long.parseLong((String) param.get("nrCodigoBarras"));
		MeioTransporte meioTransporte = carregamentoService.findMeioTransporteByBarCode(nrCodigoBarras);
		Map<String, Object> retorno = this.getMeioTransporteMapped(meioTransporte);

		meioTranspProprietarioService.validateBloqueioMeioTransporte(MeioTranspProprietarioBuilder.buildFromMeioTransporte(meioTransporte));
		meioTranspProprietarioService.verificaSituacaoMeioTransporte(MeioTranspProprietarioBuilder.buildFromMeioTransporte(meioTransporte));
		/*
		 * a. Se o veiculo estiver associado a um Controle de Carga do tipo
		 * Coleta/Entrega e com situação “GE” = “Gerado”
		 */
		ControleCarga carga = carregamentoService.findControleCargaAberto(meioTransporte.getIdMeioTransporte(), this.CONTROLE_CARGA_COLETA_ENTREGA);
		if (carga != null) {
			/* então o usuário será redirecionado ao carregamento deste veiculo */
			retorno.put("controleCarga", this.getControleCargaMapped(carga));
		} else {
			/*
			 * c. Se não, verificar se o veiculo pode iniciar um novo Controle
			 * de Carga.
			 */
			carregamentoService.validateMeioTransporte(meioTransporte);
		}
		return retorno;
	}

	public Map findRotasEntrega() {
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		List<RotaColetaEntrega> rotas = carregamentoService.findRotasEntrega(idFilial);

		Map<String, Object> retorno = new HashMap<String, Object>();
		List<Map<String, Object>> listRotas = new ArrayList<Map<String, Object>>();

		for (RotaColetaEntrega rota : rotas) {
			Map<String, Object> rotaMapped = new HashMap<String, Object>();
			rotaMapped.put("idRota", rota.getIdRotaColetaEntrega());
			rotaMapped.put("dsRota", rota.getDsRota());
			rotaMapped.put("nrRota", rota.getNrRota());
			listRotas.add(rotaMapped);
		}

		retorno.put("rotas", listRotas);

		return retorno;
	}

	public Map findOciosidadeAndLacres(Map param){
		Long idControleCarga = this.getLongProperty("idControleCarga",param);
		return carregamentoService.findOciosidadeAndLacres(idControleCarga);
	}

	public Map storeControleCarga(Map param) {
		String nrCodigoBarras = (String)param.get("nrCodigoBarras");											
		Boolean blConfirmado = Boolean.parseBoolean((String) param.get("blConfirma"));
		
		/* Pega parâmetros passados pelo .NET */
		Long idRota = Long.parseLong((String)param.get("idRota"));			
		Long idMeioTransporte = Long.parseLong((String)param.get("idMeioTransporte"));		
		
		/* Cria um novo controle de Carga com os valores vindos por parâmetro */
		ControleCarga newCC = this.generateControleCarga(idMeioTransporte, idRota);
			
		/* Monta o retorno */
		Boolean isVolume = codigoBarrasService.isVolume(nrCodigoBarras);
		Map<String, Object> controleCargaMapped = null;
				
		Long idBox = null;
		/* Pega idBox se foi informado */
		if (param.containsKey("idBox")) {
			idBox = Long.parseLong((String) param.get("idBox"));
		}
		if(isVolume || codigoBarrasService.validatePadraoCodigoBarra(nrCodigoBarras) ) {
			Map<String, Object> volumeMapped = null;
			if(SessionUtils.getFilialSessao().getBlSorter()){
				volumeMapped = this.storeVolumeBlsorter(nrCodigoBarras, newCC, idBox, blConfirmado, SessionUtils.getFilialSessao().getBlSorter());
			}else{
				volumeMapped = this.storeVolume(nrCodigoBarras, newCC, idBox, blConfirmado);
			}
			List<Map<String,Object>> conhecimentos = new ArrayList<Map<String,Object>>();
			conhecimentos.add(volumeMapped);
			controleCargaMapped = this.getControleCargaMapped(newCC);					
			controleCargaMapped.put("conhecimentos", conhecimentos);
		} else {
			Map<String, Object> dispositivoMapped = this.storeDispositivo(nrCodigoBarras, newCC, idBox, blConfirmado);
			List<Map<String,Object>> dispositivos = new ArrayList<Map<String,Object>>();
			dispositivos.add(dispositivoMapped);
			controleCargaMapped = this.getControleCargaMapped(newCC);					
			controleCargaMapped.put("dispositivos", dispositivos);
		}
		
		return controleCargaMapped;
	}
	
	/* Cria um novo controle de carga e o retorna sem salvar */
	private ControleCarga generateControleCarga(Long idMeioTransporte, Long idRota) {
		ControleCarga newCC = new ControleCarga();

		/* Verifica tipo do meio de transporte */
		if(meioTransporteService.findMeioTransporteIsSemiReboque(idMeioTransporte)) {
			newCC.setMeioTransporteByIdSemiRebocado(meioTransporteService.findByIdInitLazyProperties(idMeioTransporte, false));
		} else{
			newCC.setMeioTransporteByIdTransportado(meioTransporteService.findByIdInitLazyProperties(idMeioTransporte, false));
		}

		RotaColetaEntrega rotaColetaEntrega = new RotaColetaEntrega();
		rotaColetaEntrega.setIdRotaColetaEntrega(idRota);
		newCC.setRotaColetaEntrega(rotaColetaEntrega);
		newCC.setTpControleCarga(new DomainValue(this.CONTROLE_CARGA_COLETA_ENTREGA));		
		return newCC;
	}
	
	public Map storeVolume(Map param) {
		/* Busca o volume a partir do código de barras vindo por parâmetro */
		String nrCodigoBarras = (String) param.get("nrCodigoBarras");

		/* Busca o controle de cargas a partir do id vindo no parâmetro */
		Long idControleCarga = Long.parseLong((String) param.get("idControleCarga"));
		ControleCarga cc = controleCargaService.findById(idControleCarga);

		Boolean blConfirmado = Boolean.parseBoolean((String) param.get("blConfirma"));

		Long idBox = null;
		/* Pega idBox se foi informado */
		if (param.containsKey("idBox")) {
			idBox = Long.parseLong((String) param.get("idBox"));	
		}
			
		return this.storeVolume(nrCodigoBarras, cc, idBox, blConfirmado);
	}
	public Map storeVolumeBlsorter(Map param) {
		/* Busca o volume a partir do código de barras vindo por parâmetro */
		String nrCodigoBarras = (String) param.get("nrCodigoBarras");

		/* Busca o controle de cargas a partir do id vindo no parâmetro */
		Long idControleCarga = Long.parseLong((String) param.get("idControleCarga"));
		ControleCarga cc = controleCargaService.findById(idControleCarga);
	
		Boolean blConfirmado = Boolean.parseBoolean((String) param.get("blConfirma"));
		Boolean blSorter = Boolean.parseBoolean((String) param.get("blSorter"));

		Long idBox = null;
		/* Pega idBox se foi informado */
		if (param.containsKey("idBox")) { 
			idBox = Long.parseLong((String) param.get("idBox"));	
		}
		
		return this.storeVolumeBlsorter(nrCodigoBarras, cc, idBox, blConfirmado, blSorter);
	}
	
	/**
	 * Novo metodo  storeVolumeConhecimento  para carregar documento quando a filial  tem blsorter = true e tp_statusConhecimento = p
	 * 
	 * @param nrCodigoBarras
	 * @param controleCarga
	 * @param idBox
	 * @param blConfirmado
	 * @param filialBlsorter
	 * @return
	 */
	private Map<String, Object> storeVolumeBlsorter(String nrCodigoBarras, ControleCarga controleCarga, Long idBox, Boolean blConfirmado,Boolean filialBlsorter) {
		VolumeNotaFiscal volume = volumeService.findVolumeByBarCodeUniqueResult(nrCodigoBarras);
		
		/* Tenta executar store do Volume, caso não consiga retorna lista de Exceções */
		List<Map<String, Object>> exceptions = 
			carregamentoService.storeVolumeBlsorter(volume, controleCarga, idBox, this.MANIFESTO_ENTREGA, this.SCAN_FISICO, null, !blConfirmado,filialBlsorter);
		
		Map<String, Object> volumeMapped = this.getVolumeMapped(volume, controleCarga.getIdControleCarga());
		if (exceptions != null && !exceptions.isEmpty()) {
			volumeMapped.put("messages", exceptions);
		}
		
		return volumeMapped;
	}

	
	private Map<String, Object> storeVolume(String nrCodigoBarras, ControleCarga controleCarga, Long idBox, Boolean blConfirmado) {
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

		/* Tenta executar store do Volume, caso não consiga retorna lista de Exceções */
		List<Map<String, Object>> exceptions = carregamentoService.storeVolume(volumeNotaFiscal, controleCarga, idBox, this.MANIFESTO_ENTREGA, this.SCAN_FISICO, null, !blConfirmado);
		
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
		Long idControleCarga = Long.parseLong((String)param.get("idControleCarga"));
		ControleCarga controleCarga = controleCargaService.findById(idControleCarga);			
		
		/* Pega varíavel que indica se já foram feitas as confirmações necessárias */
		Boolean blConfirmado = Boolean.parseBoolean((String)param.get("blConfirma"));	
		
		Long idBox = null;
		/* Pega idBox se foi informado */
		if (param.containsKey("idBox")) {
			idBox = Long.parseLong((String) param.get("idBox"));
		}
		
		return this.storeDispositivo(nrCodigoBarras, controleCarga, idBox, blConfirmado);
	}
	
	private Map<String, Object> storeDispositivo(String nrCodigoBarras, ControleCarga controleCarga, Long idBox , Boolean blConfirmado) {	
		/* Busca o dispositivo de unitização pelo código de barras */
		DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findByBarcode(nrCodigoBarras);
		
		/* Busca carregamento aberto para o controle de carga, retorna null se não existe */
		CarregamentoDescarga carregamento = carregamentoService.findCarregamentoByControleCarga(controleCarga.getIdControleCarga());			
		
		/* Chama método que grava o dispositivo de unitização */		
		List<Map<String, Object>> exceptions = 
			carregamentoService.storeDispositivo(controleCarga,idBox, dispositivo, carregamento, null, this.MANIFESTO_ENTREGA, this.SCAN_FISICO, null, !blConfirmado);
		
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
		exceptions = carregamentoService.removeVolume(volume, controleCarga, SCAN_FISICO, !blConfirmado);		

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
		Long idControleCarga = Long.parseLong((String)param.get("idControleCarga"));
		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);			
		
		/* Pega varíavel que indica se já foram feitas as confirmações necessárias */
		Boolean blConfirmado = Boolean.parseBoolean((String)param.get("blConfirma"));	
		
		/* Busca carregamento aberto para o controle de carga, retorna null se não existe */
		CarregamentoDescarga carregamento = carregamentoService.findCarregamentoByControleCarga(idControleCarga);		
		
		List<Map<String, Object>> exceptions = 
			carregamentoService.removeDispositivo(controleCarga, dispositivo, carregamento, MANIFESTO_ENTREGA, SCAN_FISICO, !blConfirmado);		
		
		/* Retorna dispositivo Mapeado */
		Map<String, Object> dispositivoMapped = new HashMap<String, Object>();
		
		if (exceptions != null && !exceptions.isEmpty()) {
			dispositivoMapped.put("messages", exceptions);
		} else {
			dispositivoMapped = unitizacaoService.findMapDispositivoUnitizacao(dispositivo);
		}
		
		return dispositivoMapped;
	}	
	
	@SuppressWarnings("rawtypes")
	public Map executeIniciarConferencia(Map param) {
		Long idControleCarga = Long.parseLong((String) param.get("idControleCarga"));
		
		List<Map> docsByCC = carregamentoService.findDoctoServicoDivergenteEntrega(idControleCarga);
		List<Map<String, Object>> conhecimentos = new ArrayList<Map<String, Object>>();

		for (Map conhecimento : docsByCC) {
			if (((Integer) conhecimento.get("qtVolumes")).compareTo(Integer.parseInt(conhecimento.get("qtVolumesCarregados").toString())) != 0) {
				conhecimento.put("dsRnc", naoConformidadeService.findByIdDoctoServicoAndStatusNaoConformidade(Long.parseLong(conhecimento.get("idDoctoServico").toString()), "CAN") != null ? "SIM" : "NÃO");
				conhecimentos.add(conhecimento);
			}
		}

		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("conhecimentos", conhecimentos);

		carregamentoService.storeStatusCarregamento(
				carregamentoService.findCarregamentoByControleCarga(idControleCarga), "I");

		return retorno;
	}

	public void executeReiniciarCarregamento(Map param) {
		Long idControleCarga = Long.parseLong((String) param.get("idControleCarga"));
		carregamentoService.storeStatusCarregamento(
				carregamentoService.findCarregamentoByControleCarga(idControleCarga), "I");
	}

	public void executeConcluirCarregamento(Map param) {
		Long idControleCarga = Long.parseLong((String) param.get("idControleCarga"));
		BigDecimal ociosidadeVisual = new BigDecimal((String)param.get("ociosidadeVisual"));
		Filial filial = filialService.findById(Long.parseLong((String)param.get("idFilial")));
		String lacresStr = (String)param.get("lacres");
		String[] lacres = lacresStr.split("\\|");
		carregamentoService.storeStatusCarregamentoSorter(filial.getSgFilial(),"P",idControleCarga,CARREGAMENTO_CONCLUIDO_MWW,ociosidadeVisual,lacres);
	}	
	
	public Map<String, Object> getControleCargaMapped(ControleCarga cc) {
		Map<String, Object> retorno = new HashMap<String, Object>();
		if (cc != null) {
			retorno.put("conhecimentos", carregamentoService.findConhecimentosControleCargaMapped(cc.getIdControleCarga(), cc.getTpControleCarga().getValue()));
			retorno.put("dispositivos", carregamentoService.findDispositivosControleCarga(cc.getIdControleCarga(), cc.getTpControleCarga().getValue()));
			retorno.put("idControleCarga", cc.getIdControleCarga());
			retorno.put("nrControleCarga", cc.getNrControleCarga());

			if (cc.getRotaColetaEntrega() != null) {
				Map<String, Object> rota = new HashMap<String, Object>();
				rota.put("idRota", cc.getRotaColetaEntrega().getIdRotaColetaEntrega());
				rota.put("dsRota", cc.getRotaColetaEntrega().getDsRota());
				rota.put("nrRota", cc.getRotaColetaEntrega().getNrRota());
				retorno.put("rota", rota);
			}

			if (cc.getFilialByIdFilialOrigem() != null) {
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

	public Map<String, Object> getMeioTransporteMapped(MeioTransporte meioTransporte) {
		Map<String, Object> retorno = new HashMap<String, Object>();
		retorno.put("nrFrota", meioTransporte.getNrFrota());
		retorno.put("placa", meioTransporte.getNrIdentificador());
		retorno.put("idMeioTransporte", meioTransporte.getIdMeioTransporte());
		return retorno;
	}

	private Map<String,Object> getVolumeMapped(VolumeNotaFiscal volume, Long idControleCarga) {
		Conhecimento docto = conhecimentoService.findConhecimentoAtualById(volume.getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico());
		if (docto == null){
			docto = volume.getNotaFiscalConhecimento().getConhecimento();
		}
		Map<String, Object> conhecimento = new HashMap<String, Object>();
		Integer qtCarregadosNivel = 0;
		conhecimento.put("sgFilialDocumento", docto.getFilialOrigem().getSgFilial());
		conhecimento.put("nrConhecimento", volume.getNrConhecimento());		
		conhecimento.put("qtVolumes", docto.getQtVolumes());
		conhecimento.put("nrCae", docto.getNrCae());
		conhecimento.put("tpDoctoServico", docto.getTpDoctoServico().getDescriptionAsString());
		
		if(idControleCarga != null) {
			/* Faz contagem dos volumes carregados do docto serviço NÃO unitizados */
			qtCarregadosNivel = volumeService.findRowCountVolumesCarregamentoDescarga(idControleCarga, docto.getIdDoctoServico(), "C", volume.getDispositivoUnitizacao());
			Integer totaolVolumesDoctoInCC = volumeService.findRowCountVolumesCarregamentoDescargaConhecimento(idControleCarga, docto.getIdDoctoServico(), "C");						

			conhecimento.put("qtVolumesCarregados", qtCarregadosNivel);
			conhecimento.put("completo", totaolVolumesDoctoInCC.intValue() == docto.getQtVolumes().intValue());
			conhecimento.put("isVolumePaletizado", volumeService.validateIsVolumePaletizado(volume.getIdVolumeNotaFiscal()));
		}
		
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
		conhecimento.put("tpSituacaoConhecimento",docto.getTpSituacaoConhecimento());		
				
		return conhecimento;
	}

	public Map<String, Object> getDispositivoMapped(DispositivoUnitizacao dispositivo, Long idControleCarga) {
		Map<String,Object> retorno = new HashMap<String, Object>();		
		
		List<VolumeNotaFiscal> volumes = dispositivo.getVolumes();
		
		List<Map<String,Object>> volumesMapped = new ArrayList<Map<String,Object>>();
		for(VolumeNotaFiscal volume : volumes) {
			Map<String, Object> volumeMapped = this.getVolumeMapped(volume, idControleCarga);
			volumesMapped.add(volumeMapped);
		}						
		
		List<DispositivoUnitizacao> dispositivos = dispositivo.getDispositivosUnitizacao();
		
		List<Map<String,Object>> dispositivosMapped = new ArrayList<Map<String,Object>>();
		
		for(DispositivoUnitizacao dispositivoFilho : dispositivos) {
			Map<String, Object> dispositivoMapped = this.getDispositivoMapped(dispositivoFilho, idControleCarga);
			dispositivosMapped.add(dispositivoMapped);
		}
		
		retorno.put("volumes", volumesMapped);
		retorno.put("dispositivos", dispositivosMapped);
		retorno.put("nrIdentificacao", dispositivo.getNrIdentificacao());
		return retorno;
	}
	
	public Map<String, Object> getConhecimentoMapped(Long idControleCarga, DoctoServico docto,Long idManifesto) {
		Map<String, Object> conhecimento = new HashMap<String, Object>();
		Integer qtCarregados = 0;
		
		VolumeNotaFiscal vnf = volumeService.findVolumeByIdConhecimento(docto.getIdDoctoServico());
		
		conhecimento.put("sgFilialDocumento", docto.getFilialByIdFilialOrigem().getSgFilial());
		conhecimento.put("nrConhecimento", vnf.getNrConhecimento());
		conhecimento.put("qtVolumes", docto.getQtVolumes());
		
		if (idManifesto != null) {
			qtCarregados = volumeService.findRowCountVolumesCarregamentoDescargaConhecimento(idControleCarga, docto.getIdDoctoServico(), "C");
			conhecimento.put("qtVolumesCarregados", qtCarregados);
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

	public void setDispositivoUnitizacaoService(DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public void setUnitizacaoService(UnitizacaoService unitizacaoService) {
		this.unitizacaoService = unitizacaoService;
	}

	public void setVolumeService(VolumeNotaFiscalService volumeService) {
		this.volumeService = volumeService;
	}
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
    	this.conhecimentoService = conhecimentoService;
    }
	
	public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}
	
	// LMS-5719 - Indicador de RNC
	public void setNaoConformidadeService(NaoConformidadeService naoConformidadeService) {
		this.naoConformidadeService = naoConformidadeService;
	}
}