package com.mercurio.lms.mobilescanapp.model.service;

import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.carregamento.model.service.UnitizacaoService;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.IntegracaoJwtService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConferirVolumeService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.mww.model.service.CarregamentoMobileService;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarregamentoMobileScanEntregaService  extends AbstractMobileScanService{

	private CarregamentoMobileService carregamentoMobileService;
	private ConferirVolumeService conferirVolumeService;
	private ControleCargaService controleCargaService;
	private UnitizacaoService unitizacaoService;
	private VolumeNotaFiscalService volumeService;
	private FilialService filialService;
	private ConhecimentoService conhecimentoService;
	private IntegracaoJwtService integracaoJwtService;
	private MoedaService moedaService;
	private PaisService paisService;
	private HistoricoFilialService historicoFilialService;

	/* Manifesto de entrega */
	private static final String MANIFESTO_ENTREGA = "E";
	/* TpScan = Scan Físico */
	private static final String SCAN_FISICO = "SF";
	private static final String TP_OPERACAO = "C";
	private static final String LIST_CONTROLE_CARGA = "listControleCarga";
	private static final String TP_CONTROLE_CARGA = "tpControleCarga";
	private static final String CONTROLE_CARGA = "controleCarga";

	private static final String MEIO_TRANPORTE = "meioTransporte";
	private static final String NEED_CONFIRMATION = "needConfirmation";
	private static final String ALERT = "alert";

	/* 2. Scanear o código de identificação do meio de transporte */
	public Map findControleCargaAberto(Map transporte) {
			ControleCarga controleCarga = (ControleCarga) transporte.get(CONTROLE_CARGA);
			MeioTransporte meioTransporte = (MeioTransporte) transporte.get(MEIO_TRANPORTE);
			transporte.remove(CONTROLE_CARGA);
			transporte.remove(MEIO_TRANPORTE);

			if (controleCarga != null) {
				transporte.put("controleCarga", this.getControleCargaMapped(controleCarga));
			} else {
				carregamentoMobileService.validateMeioTransporte(meioTransporte);
			}
			return transporte;
	}

	public Map storeVolume(Map param, String token) {
		criaSessao(token);
		try {
			/* Busca o volume a partir do código de barras vindo por parâmetro */
			String nrCodigoBarras = param.get("nrCodigoBarras").toString();

			/* Busca o controle de cargas a partir do id vindo no parâmetro */
			Long idControleCarga = Long.parseLong(param.get("idControleCarga").toString());
			ControleCarga cc = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);

			Boolean blConfirmado = Boolean.parseBoolean(param.get("blConfirma").toString());
			Boolean blSorter = Boolean.parseBoolean(param.get("blSorter").toString());

			if(blSorter){
				return this.storeVolumeBlsorter(nrCodigoBarras, cc, null, blConfirmado, blSorter);
			}
			return this.storeVolume(nrCodigoBarras, cc, null, blConfirmado);

		}finally {
			destroiSessao();
		}
	}

	/**
	 * Novo metodo  storeVolumeConhecimento  para carregar documento quando a filial  tem blsorter = true e tp_statusConhecimento = p
	 */
	private Map<String, Object> storeVolumeBlsorter(String nrCodigoBarras, ControleCarga controleCarga, Long idBox, Boolean blConfirmado, Boolean filialBlsorter) {
		VolumeNotaFiscal volume = volumeService.findVolumeByBarCodeUniqueResult(nrCodigoBarras);
		
		/* Tenta executar store do Volume, caso não consiga retorna lista de Exceções */
		List<Map<String, Object>> exceptions = 
			carregamentoMobileService.storeVolumeBlsorter(volume, controleCarga, idBox, this.MANIFESTO_ENTREGA, this.SCAN_FISICO, null, !blConfirmado, filialBlsorter);

		if(verificarExceptionApenasTipoAlerta(exceptions) && !blConfirmado){
			Map<String, Object> volumeMap = storeVolumeBlsorter(nrCodigoBarras, controleCarga, idBox, true, filialBlsorter);
			if(volumeMap.get("messages") == null) {
				volumeMap.put("messages", exceptions);
			} else {
				((List<Map<String, Object>>) volumeMap.get("messages")).addAll(exceptions);
			}
			return volumeMap;
		}

		Map<String, Object> volumeMapped = this.getVolumeMapped(volume, controleCarga.getIdControleCarga());
		if (exceptions != null && !exceptions.isEmpty()) {
			volumeMapped.put("messages", exceptions);
		}
		
		return volumeMapped;
	}

	private boolean verificarExceptionApenasTipoAlerta(List<Map<String, Object>> exceptions){
		long qtdAlertas = exceptions == null ? 0 : exceptions.stream().filter(e -> (Boolean)e.get(ALERT) && !(Boolean)e.get(NEED_CONFIRMATION)).count();
		return exceptions == null ? false : qtdAlertas == exceptions.size();
	}

	
	private Map<String, Object> storeVolume(String nrCodigoBarras, ControleCarga controleCarga, Long idBox, Boolean blConfirmado) {
		/** LMS-1039 */
		final Map<String, String> alias = new HashMap<>();
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
		List<Map<String, Object>> exceptions = carregamentoMobileService.storeVolume(volumeNotaFiscal, controleCarga, idBox, this.MANIFESTO_ENTREGA, this.SCAN_FISICO, null, !blConfirmado);

		if(verificarExceptionApenasTipoAlerta(exceptions) && !blConfirmado){
			Map<String, Object> volume = storeVolume(nrCodigoBarras, controleCarga, idBox, true);
			if(volume.get("messages") == null) {
				volume.put("messages", exceptions);
			} else {
				((List<Map<String, Object>>) volume.get("messages")).addAll(exceptions);
			}
			return volume;
		}

		Map<String, Object> volumeMapped = this.getVolumeMapped(volumeNotaFiscal, controleCarga.getIdControleCarga());
		if (exceptions != null && !exceptions.isEmpty()) {
			volumeMapped.put("messages", exceptions);
		}
		return volumeMapped;
	}
	
	public Map storeDispositivo(Map param, String token) {
		criaSessao(token);
		try {
			/* Busca o volume a partir do código de barras vindo por parâmetro */
			String nrCodigoBarras = param.get("nrCodigoBarras").toString();
			/* Busca o controle de cargas a partir do id vindo no parâmetro */
			Long idControleCarga = Long.parseLong(param.get("idControleCarga").toString());
			ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);

			/* Pega varíavel que indica se já foram feitas as confirmações necessárias */
			Boolean blConfirmado = Boolean.parseBoolean(param.get("blConfirma").toString());

			return this.storeDispositivo(nrCodigoBarras, controleCarga, null, blConfirmado);
		}finally {
			destroiSessao();
		}
	}
	
	private Map<String, Object> storeDispositivo(String nrCodigoBarras, ControleCarga controleCarga, Long idBox , Boolean blConfirmado) {
		/* Busca o dispositivo de unitização pelo código de barras */
		DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findByBarcode(nrCodigoBarras);
		
		/* Busca carregamento aberto para o controle de carga, retorna null se não existe */
		CarregamentoDescarga carregamento = carregamentoMobileService.findCarregamentoByControleCarga(controleCarga.getIdControleCarga());
		
		/* Chama método que grava o dispositivo de unitização */
		LocalizacaoMercadoria localizacaoMercadoria = dispositivo.getLocalizacaoMercadoria();
		List<Map<String, Object>> exceptions = 
			carregamentoMobileService.storeDispositivo(controleCarga,idBox, dispositivo, carregamento, null, this.MANIFESTO_ENTREGA, this.SCAN_FISICO, null, !blConfirmado);
		
		/* Retorna dispositivo Mapeado */
		Map<String, Object> dispositivoMapped = new HashMap<>();
		
		if (exceptions != null && !exceptions.isEmpty()) {
			dispositivoMapped.put("messages", exceptions);
		} else {
			dispositivo.setLocalizacaoMercadoria(localizacaoMercadoria);
			List<Map<String, Object>> listDispMap = new ArrayList<>();
			listDispMap.add(unitizacaoService.findMapDispositivoUnitizacao(dispositivo, controleCarga.getIdControleCarga(), TP_OPERACAO));
			verificaConhecimentoCompleto(controleCarga.getIdControleCarga(), (List<Map>)dispositivoMapped.get("conhecimentos"));
			verificaDispositivoCompleto(controleCarga.getIdControleCarga(), (List<Map>)dispositivoMapped.get("dispositivos"), dispositivo.getIdDispositivoUnitizacao());
			listDispMap.addAll((List<Map<String, Object>>) listDispMap.get(0).get("dispositivos"));
			dispositivoMapped.put("dispositivos", listDispMap);
		}
		
		return dispositivoMapped;
	}
	
	private void verificaDispositivoCompleto(Long idControleCarga,	List<Map> list, Long idPai) {
		if(list != null){
			for (Map<String, Object> disp : list) {
				verificaConhecimentoCompleto(idControleCarga, (List<Map>)disp.get("conhecimentos"));
				verificaDispositivoCompleto(idControleCarga, (List<Map>)disp.get("dispositivos"), idPai);
				Long id = Long.parseLong(disp.get("idDispositivoUnitizacao").toString());
				if(idPai.longValue() != id.longValue()){
					disp.put("idDispositivoUnitizacaoPai", idPai);
				}
			}
		}
	}
	
	private void verificaConhecimentoCompleto(Long idControleCarga,	List<Map> list) {
		if(list != null){
			for (Map<String, Object> con : list) {
				final Integer rowCountVolumes = volumeService.findRowCountVolumesCarregamentoDescargaConhecimento(idControleCarga,
						Long.parseLong(con.get("idDoctoServico").toString()), "C");
				con.put("completo", (rowCountVolumes == Integer.parseInt(con.get("qtVolumes").toString())));
			}
		}
	}
	
	public Map<String, Object> deleteVolume(Map param, String token) {
		criaSessao(token);
		try {
			Long idControleCarga = Long.parseLong(param.get("idControleCarga").toString());
			String nrCodigoBarras = param.get("nrCodigoBarras").toString();
			Boolean blConfirmado = Boolean.parseBoolean(param.get("blConfirma").toString());

			VolumeNotaFiscal volume = volumeService.findVolumeByBarCodeUniqueResult(nrCodigoBarras);
			ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);

			List<Map<String, Object>> exceptions = carregamentoMobileService.removeVolume(volume, controleCarga, SCAN_FISICO, !blConfirmado);

			Map<String, Object> conhecimento = this.getVolumeMapped(volume, idControleCarga);
			if (exceptions != null && !exceptions.isEmpty()) {
				conhecimento.put("messages", exceptions);
			}
			return conhecimento;
		}finally {
			destroiSessao();
		}
	}
	
	public Map<String, Object> deleteDispositivo(Map param, String token) {
		criaSessao(token);
		try {
			/* Busca o volume a partir do código de barras vindo por parâmetro */
			String nrCodigoBarras = param.get("nrCodigoBarras").toString();
			DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findByBarcode(nrCodigoBarras);

			/* Busca o controle de cargas a partir do id vindo no parâmetro */
			Long idControleCarga = Long.parseLong(param.get("idControleCarga").toString());
			ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);

			/* Pega varíavel que indica se já foram feitas as confirmações necessárias */
			Boolean blConfirmado = Boolean.parseBoolean(param.get("blConfirma").toString());

			/* Busca carregamento aberto para o controle de carga, retorna null se não existe */
			CarregamentoDescarga carregamento = carregamentoMobileService.findCarregamentoByControleCarga(idControleCarga);

			List<Map<String, Object>> exceptions =
					carregamentoMobileService.removeDispositivo(controleCarga, dispositivo, carregamento, MANIFESTO_ENTREGA, SCAN_FISICO, !blConfirmado);

			/* Retorna dispositivo Mapeado */
			Map<String, Object> dispositivoMapped = new HashMap<>();

			if (exceptions != null && !exceptions.isEmpty()) {
				dispositivoMapped.put("messages", exceptions);
			} else {
				dispositivoMapped = unitizacaoService.findMapDispositivoUnitizacao(dispositivo);
			}

			return dispositivoMapped;
		}finally {
			destroiSessao();
		}
	}

	public Map<String, Object> getControleCargaMapped(ControleCarga cc) {
		Map<String, Object> retorno = new HashMap<>();
		if (cc != null) {
			retorno.put("conhecimentos", this.findConhecimentosControleCargaMapped(cc.getIdControleCarga(), null, cc.getTpControleCarga().getValue()));
			retorno.put("dispositivos", this.findDispositivosControleCarga(cc.getIdControleCarga(), cc.getTpControleCarga().getValue(), SessionUtils.getFilialSessao().getIdFilial()));
			retorno.put("idControleCarga", cc.getIdControleCarga());
			retorno.put("nrControleCarga", cc.getNrControleCarga());

			if (cc.getRotaColetaEntrega() != null) {
				Map<String, Object> rota = new HashMap<>();
				rota.put("idRota", cc.getRotaColetaEntrega().getIdRotaColetaEntrega());
				rota.put("dsRota", cc.getRotaColetaEntrega().getDsRota());
				rota.put("nrRota", cc.getRotaColetaEntrega().getNrRota());
				retorno.put("rota", rota);
			}

			if (cc.getFilialByIdFilialOrigem() != null) {
				Map<String, Object> filialOrigem = new HashMap<>();
				filialOrigem.put("sgFilial", cc.getFilialByIdFilialOrigem().getSgFilial());
				retorno.put("filialOrigem", filialOrigem);
			}
		} else {
			retorno.put("idControleCarga", null);
			retorno.put("nrControleCarga", null);
		}

		return retorno;
	}

	private Map<String,Object> getVolumeMapped(VolumeNotaFiscal volume, Long idControleCarga) {
		Conhecimento docto = conhecimentoService.findConhecimentoAtualById(volume.getNotaFiscalConhecimento().getConhecimento().getIdDoctoServico());
		if (docto == null){
			docto = volume.getNotaFiscalConhecimento().getConhecimento();
		}
		Map<String, Object> conhecimento = new HashMap<>();
		Integer qtCarregadosNivel = 0;
		conhecimento.put("sgFilialDocumento", docto.getFilialOrigem().getSgFilial());
		conhecimento.put("nrConhecimento", volume.getNrConhecimento());		
		conhecimento.put("qtVolumes", docto.getQtVolumes());
		conhecimento.put("nrCae", docto.getNrCae());
		conhecimento.put("tpDoctoServico", docto.getTpDoctoServico().getDescriptionAsString());
		
		if(idControleCarga != null) {
			/* Faz contagem dos volumes carregados do docto serviço NÃO unitizados */
			qtCarregadosNivel = volumeService.findRowCountVolumesCarregamentoDescarga(idControleCarga, docto.getIdDoctoServico(), "C",
					volume.getDispositivoUnitizacao());
			Integer totaolVolumesDoctoInCC = volumeService.findRowCountVolumesCarregamentoDescargaConhecimento(idControleCarga,
					docto.getIdDoctoServico(), "C");

			conhecimento.put("qtVolumesCarregados", qtCarregadosNivel);
			conhecimento.put("completo", totaolVolumesDoctoInCC.intValue() == docto.getQtVolumes().intValue());
			conhecimento.put("isVolumePaletizado", volumeService.validateIsVolumePaletizado(volume.getIdVolumeNotaFiscal()));
		}
		
		if(volume.getLocalizacaoMercadoria()!=null){
			Boolean isExtraviado = conferirVolumeService.isVolumeExtraviado(volume.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria());
			
			if(isExtraviado) {
				Map<String, Object> volumeMapped = new HashMap<>();
				volumeMapped.put("extraviado",isExtraviado);
				volumeMapped.put("nrSequencia", volume.getNrSequencia());
				volumeMapped.put("idVolumeNotaFiscal", volume.getIdVolumeNotaFiscal());		
			
				List<Map<String,Object>> volumes = new ArrayList<>();
				volumes.add(volumeMapped);
			
				conhecimento.put("volumes", volumes);
			}
		}
		conhecimento.put("tpSituacaoConhecimento",docto.getTpSituacaoConhecimento());		
				
		return conhecimento;
	}

	public Map<String, Object> getDispositivoMapped(DispositivoUnitizacao dispositivo, Long idControleCarga) {
		Map<String,Object> retorno = new HashMap<>();
		
		List<VolumeNotaFiscal> volumes = dispositivo.getVolumes();
		
		List<Map<String,Object>> volumesMapped = new ArrayList<>();
		for(VolumeNotaFiscal volume : volumes) {
			Map<String, Object> volumeMapped = this.getVolumeMapped(volume, idControleCarga);
			volumesMapped.add(volumeMapped);
		}						
		
		List<DispositivoUnitizacao> dispositivos = dispositivo.getDispositivosUnitizacao();
		
		List<Map<String,Object>> dispositivosMapped = new ArrayList<>();
		
		for(DispositivoUnitizacao dispositivoFilho : dispositivos) {
			Map<String, Object> dispositivoMapped = this.getDispositivoMapped(dispositivoFilho, idControleCarga);
			dispositivosMapped.add(dispositivoMapped);
		}
		
		retorno.put("volumes", volumesMapped);
		retorno.put("dispositivos", dispositivosMapped);
		retorno.put("nrIdentificacao", dispositivo.getNrIdentificacao());
		return retorno;
	}
	
	private void criaSessao(String token){
		Filial filial =  integracaoJwtService.getFilialSessao(integracaoJwtService.getIdFilialByToken(token));
		Usuario usuario = integracaoJwtService.getUsuarioSessaoByToken(token);
		Empresa empresa = integracaoJwtService.getEmpresaSessao(integracaoJwtService.getIdEmpresaByToken(token));
		SessionContext.setUser(usuario);
		SessionContext.set(SessionKey.EMPRESA_KEY, empresa);
		SessionContext.set(SessionKey.FILIAL_KEY, filial);
		SessionContext.set(SessionKey.FILIAL_DTZ, filial.getDateTimeZone());
		SessionContext.set(SessionKey.MOEDA_KEY, moedaService.findMoedaByUsuarioEmpresa(usuario, empresa));
		SessionContext.set(SessionKey.PAIS_KEY, paisService.findPaisByUsuarioEmpresa(usuario, empresa));
		SessionContext.set(SessionKey.ULT_HIST_FILIAL_KEY, historicoFilialService.findUltimoHistoricoFilial(filial.getIdFilial()));
		SessionContext.set(SessionKey.FILIAL_MATRIZ_KEY, historicoFilialService.validateFilialUsuarioMatriz(filial.getIdFilial()));
	}

	private void destroiSessao(){
		SessionContext.remove(SessionKey.EMPRESA_KEY);
		SessionContext.remove(SessionKey.FILIAL_KEY);
		SessionContext.remove(SessionKey.FILIAL_DTZ);
		SessionContext.remove(SessionKey.MOEDA_KEY);
		SessionContext.remove(SessionKey.PAIS_KEY);
		SessionContext.remove("adsm.session.authenticatedUser");
		SessionContext.remove(SessionKey.ULT_HIST_FILIAL_KEY);
		SessionContext.remove(SessionKey.FILIAL_MATRIZ_KEY);
	}

	public void setCarregamentoMobileService(CarregamentoMobileService carregamentoMobileService) {
		this.carregamentoMobileService = carregamentoMobileService;
	}

	public void setConferirVolumeService(ConferirVolumeService conferirVolumeService) {
		this.conferirVolumeService = conferirVolumeService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	@Override
	public boolean isCarga() {
		return true;
	}

	public void setDispositivoUnitizacaoService(DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
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
	
	public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
		this.integracaoJwtService = integracaoJwtService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}
}
