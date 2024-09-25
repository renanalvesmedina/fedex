package com.mercurio.lms.carregamento.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.CodigoBarrasService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.EventoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;

/**
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.conferirDispositivoUnitizacaoService"
 */
public class ConferirDispositivoUnitizacaoService extends CrudService {
	
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private ConhecimentoService conhecimentoService;
	private FilialService filialService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private ParametroGeralService parametroGeralService;
	private EventoService eventoService;
	private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
	private EventoVolumeService eventoVolumeService;
	
	private static final String CD_LOCALIZACAO_VOLUME_EXTRAVIADO = "CD_LOCALIZACAO_VOLUME_EXTRAVIADO";
	
	private static final String SCAN_FISICO = "SF";
	private static final String CASCADE_SCAN = "CS";
		
	private static final String CD_EVENTO_DISPOSITIVO_ENCONTRADO = "CD_EVENTO_DISPOSITIVO_ENCONTRADO";
	private static final String CD_EVENTO_DISPOSITIVO_LIDO = "CD_EVENTO_DISPOSITIVO_LIDO";
	
	
	public Map<String, Object> findDispositivoUnitizacaoById(Long idDispositivoUnitizacao){	
		DispositivoUnitizacao dispositivoUnitizacao = dispositivoUnitizacaoService.findById(idDispositivoUnitizacao);
		
		return findDispositivoUnitizacaoByBarCode(dispositivoUnitizacao.getNrIdentificacao());
	}
	
	public Map<String, Object> findDispositivoUnitizacaoByBarCode(String barCode){			
		
		DispositivoUnitizacao dispositivoUnitizacao = null;
		
		List listaDispositivoUnitizacao = findListDispositivoUnitizacaoBarCode(barCode);
		
		if(listaDispositivoUnitizacao.size() == 1){
			dispositivoUnitizacao = (DispositivoUnitizacao)listaDispositivoUnitizacao.get(0);
		}
		else if(listaDispositivoUnitizacao.size() == 0){
			throw new BusinessException("LMS-45018");
		}
		//LMS-45033 - Erro ao buscar o dispositivo de unitização.
		else throw new BusinessException("LMS-45033");		
		
		
		List<VolumeNotaFiscal> listVolumeNotaFiscal = findVolumeNotaFiscalByDispositivoUnitizacao(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		
		ArrayList<Map> volumes = new ArrayList<Map>();
		Integer contadorVol=0;
		Integer contadorDisp=0;
		
		Map mapVol;
		Conhecimento conhecimento;
		
		Map<String, Object> map = new HashMap();		
		
		for (VolumeNotaFiscal volume : listVolumeNotaFiscal) {
			conhecimento = volume.getNotaFiscalConhecimento().getConhecimento();
			mapVol = new HashMap();		
			mapVol.put("nrSequencia", volume.getNrSequencia());
			mapVol.put("qtVolumes",conhecimento.getQtVolumes());
			mapVol.put("nrConhecimento", conhecimento.getNrConhecimento());
			mapVol.put("sgFilialDocumento", conhecimento.getFilialOrigem().getSgFilial());
			mapVol.put("idVolumeNotaFiscal", volume.getIdVolumeNotaFiscal());
			mapVol.put("tpDocServico", conhecimento.getTpDoctoServico().getDescriptionAsString());
			if(conhecimento.getNrCae()!=null){
				mapVol.put("nrCae", conhecimento.getNrCae());
			}else{
				mapVol.put("nrCae", "");
			}
			volumes.add(mapVol);
			contadorVol++;
		}
		
		/**
		 * Lista todos os filhos do DispositivoUnitizado, passando seu ID, caso possuimos uma BAG, esta devera conter o count
		 * de volumes + dispositivos
		 */
		List<DispositivoUnitizacao> listFilhosDispositivoUnitizacao = findListDispositivoUnitizacaoId(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		
		ArrayList<Map> dispositivos = new ArrayList<Map>();
		Map<String, Object> mapDU;		
		for (DispositivoUnitizacao dispUnitFilho : listFilhosDispositivoUnitizacao) {
			mapDU = new HashMap();		
			mapDU.put("qtVolumes", findSizeVolumeNotaFiscal(dispUnitFilho.getIdDispositivoUnitizacao()));
			mapDU.put("qtDispositivoUnit", findSizeDispositivoUnitizacaoId(dispUnitFilho.getIdDispositivoUnitizacao()));
			mapDU.put("tpDispositivoUnit", dispUnitFilho.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());
			mapDU.put("idDispositivoUnitizacao", dispUnitFilho.getIdDispositivoUnitizacao());
			dispositivos.add(mapDU);
			contadorDisp++;
		}
		/*
		 * OBTEM OS OBEJTOS
		 */
		LocalizacaoMercadoria localizacaoMercadoria = dispositivoUnitizacao.getLocalizacaoMercadoria();
		//*****************
		
		
		map.put("qtVolumes", contadorVol);
		map.put("qtDispositivoUnit", contadorDisp);
		map.put("tpDispositivoUnit", dispositivoUnitizacao.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());		
		map.put("volumes", volumes);
		map.put("dispositivos", dispositivos);
		
		String locMercadoria = "";
		Short cdLocMercadoria = 0;
		if (dispositivoUnitizacao.getLocalizacaoMercadoria() != null) {
			locMercadoria = dispositivoUnitizacao.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria().getValue();
			cdLocMercadoria = dispositivoUnitizacao.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();
		}
		String locFilial = "";
		Long idFilialLocalizacao = null;
		if (dispositivoUnitizacao.getLocalizacaoFilial() != null) {
			locFilial = dispositivoUnitizacao.getLocalizacaoFilial().getSgFilial();
			idFilialLocalizacao = dispositivoUnitizacao.getLocalizacaoFilial().getIdFilial();
		}
		
		map.put("locMercadoria", locMercadoria);
		map.put("cdLocMercadoria", cdLocMercadoria);
		map.put("locFilial", locFilial);
		map.put("idFilialLocalizacao", idFilialLocalizacao);
		
		map.put("idDispositivoUnitizacao", dispositivoUnitizacao.getIdDispositivoUnitizacao());
		map.put("macroZona", (dispositivoUnitizacao.getMacroZona()!=null?dispositivoUnitizacao.getMacroZona().getMapped():null));
		
		boolean extraviado = false;
		extraviado = RN_VerificaExtravio(cdLocMercadoria);
		map.put("extraviado", extraviado);
		
		return map;		
	}
	
	private boolean isBagPallet(String barCode){
		CodigoBarrasService codigoBarrasService = new CodigoBarrasService();
		
		Map map = codigoBarrasService.findIdCodigoBarras();
		
		short itemLido;
		
		short bag;
		short pallet;
		
		try{
			itemLido = Short.parseShort(barCode.substring(0, 2));
			
			bag = Short.parseShort(map.get("bag").toString());		
			pallet = Short.parseShort(map.get("pallet").toString());
		}
		catch (Exception e) {
//			LMS-45035 - Erro na conversão do tipo {0} para o tipo {1}.
			throw new BusinessException("LMS-45035", new Object[]{"String","Short"});
		}
		if (itemLido == bag  || itemLido == pallet) {
			return true;
		}else return false;		
		
	}
	
	private boolean RN_VerificaExtravio(Short cdLocMercadoria){
		int cdLocalizacaoMercadoriaExtraviada = 0;
		
		String param = getParametroGeral(CD_LOCALIZACAO_VOLUME_EXTRAVIADO);
		
		if (param != null && param != ""){
			try {
				cdLocalizacaoMercadoriaExtraviada = Integer.parseInt(param);
			} catch (Exception e) {
//				LMS-45035 - Erro na conversão do tipo {0} para o tipo {1}.
				throw new BusinessException("LMS-45035", new Object[]{"String","Integer"},e);
			}
		}	
		if(cdLocMercadoria == cdLocalizacaoMercadoriaExtraviada){
			return true;
		}
		else{
			return false;
		}
	}
	
	private String getParametroGeral(String nmParam){
		ParametroGeral param = getParametroGeralService().findByNomeParametro(nmParam, Boolean.FALSE);
		if (param!=null) {
			return param.getDsConteudo();
		}else{
			return null;
		}
	}
	
	public java.io.Serializable executeGeraEventoDispositivoEncontrado(Long idDispositivo){
		return getEventoDispositivoUnitizacaoService().generateEventoDispositivo(idDispositivo, CD_EVENTO_DISPOSITIVO_ENCONTRADO, SCAN_FISICO);
	}
	
	//Alterado para gerar eventos para os dispositivos filhos e os volumes de dado dispositivo pai
	public java.io.Serializable executeEventoDispositivoLido(Long idDispositivo) {
		List<VolumeNotaFiscal> lstVolumeNotaFiscal = volumeNotaFiscalService.findVolumeByIdDispositivoUnitizacao(idDispositivo);
		
		for (VolumeNotaFiscal volumeNotaFiscal : lstVolumeNotaFiscal) {
			getEventoVolumeService().generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), "CD_EVENTO_VOLUME_LIDO", CASCADE_SCAN);
		}
		
		List<DispositivoUnitizacao> lstDispositivoUnitizacao = dispositivoUnitizacaoService.findDispositivosByIdPai(idDispositivo);
		List<VolumeNotaFiscal> lstVolumeNotaFiscalSubDispositivo = null;
		
		for (DispositivoUnitizacao dispositivoUnitizacao : lstDispositivoUnitizacao) {
			getEventoDispositivoUnitizacaoService().generateEventoDispositivo(dispositivoUnitizacao.getIdDispositivoUnitizacao(), CD_EVENTO_DISPOSITIVO_LIDO, CASCADE_SCAN);
			
			lstVolumeNotaFiscalSubDispositivo = volumeNotaFiscalService.findVolumeByIdDispositivoUnitizacao(dispositivoUnitizacao.getIdDispositivoUnitizacao());
			
			for (VolumeNotaFiscal volumeNotaFiscal : lstVolumeNotaFiscalSubDispositivo) {
				getEventoVolumeService().generateEventoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal(), "CD_EVENTO_VOLUME_LIDO", CASCADE_SCAN);
			}
		}
		return getEventoDispositivoUnitizacaoService().generateEventoDispositivo(idDispositivo, CD_EVENTO_DISPOSITIVO_LIDO, SCAN_FISICO);
	}
	
	
	public java.io.Serializable executeGeraEventoDispositivoEncontradoTpScan(Long idDispositivo, String TpScan){
		return getEventoDispositivoUnitizacaoService().generateEventoDispositivo(idDispositivo, CD_EVENTO_DISPOSITIVO_ENCONTRADO, TpScan);
	}

	public java.io.Serializable executeEventoDispositivoLidoTpScan(Long idDispositivo, String TpScan) {
		return getEventoDispositivoUnitizacaoService().generateEventoDispositivo(idDispositivo, CD_EVENTO_DISPOSITIVO_LIDO, TpScan);
	}
	


	private List findListDispositivoUnitizacaoBarCode(String barCode) {
		Map<String, Object> criteria = new HashMap();
		criteria.put("nrIdentificacao", barCode);
		List listaDispositivoUnitizacao = getDispositivoUnitizacaoService().find(criteria);
		return listaDispositivoUnitizacao;
	}
		
	private List<DispositivoUnitizacao> findListDispositivoUnitizacaoId(Long idDispositivoUnitizacaoPai) {
		Map<String, Object> criteria = new HashMap();
		criteria.put("dispositivoUnitizacaoPai.idDispositivoUnitizacao", idDispositivoUnitizacaoPai);
		List<DispositivoUnitizacao> listaDispositivoUnitizacao = getDispositivoUnitizacaoService().find(criteria);
		return listaDispositivoUnitizacao;
	}
		
	private List<VolumeNotaFiscal> findVolumeNotaFiscalByDispositivoUnitizacao(long idDispositivoUnitizacao) {
		Map<String, Object> criteria = new HashMap();
		criteria.put("dispositivoUnitizacao.idDispositivoUnitizacao", idDispositivoUnitizacao);
		List<VolumeNotaFiscal> listaVolumeNotaFiscal = getVolumeNotaFiscalService().find(criteria);
		return listaVolumeNotaFiscal;
	}
		
	private Integer findSizeDispositivoUnitizacaoId(Long idDispositivoUnitizacaoFilho) {
		return findListDispositivoUnitizacaoId(idDispositivoUnitizacaoFilho).size();
	}
		
	private Integer findSizeVolumeNotaFiscal(Long idDispositivoUnitizacaoFilho) {
		return findVolumeNotaFiscalByDispositivoUnitizacao(idDispositivoUnitizacaoFilho).size();
	}
	
	public void setDispositivoUnitizacaoService(
			DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}

	public DispositivoUnitizacaoService getDispositivoUnitizacaoService() {
		return dispositivoUnitizacaoService;
	}

	public VolumeNotaFiscalService getVolumeNotaFiscalService() {
		return volumeNotaFiscalService;
	}

	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setLocalizacaoMercadoriaService(
			LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}

	public LocalizacaoMercadoriaService getLocalizacaoMercadoriaService() {
		return localizacaoMercadoriaService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	/**
	 * @param eventoService the eventoService to set
	 */
	public void setEventoService(EventoService eventoService) {
		this.eventoService = eventoService;
	}

	/**
	 * @return the eventoService
	 */
	public EventoService getEventoService() {
		return eventoService;
	}

	/**
	 * @param eventoDispositivoUnitizacaoService the eventoDispositivoUnitizacaoService to set
	 */
	public void setEventoDispositivoUnitizacaoService(
			EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
		this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
	}

	/**
	 * @return the eventoDispositivoUnitizacaoService
	 */
	public EventoDispositivoUnitizacaoService getEventoDispositivoUnitizacaoService() {
		return eventoDispositivoUnitizacaoService;
	}

	public EventoVolumeService getEventoVolumeService() {
		return eventoVolumeService;
	}

	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}	
	
}
