package com.mercurio.lms.carregamento.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.lms.vendas.model.service.ClienteService;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.edi.model.service.CCEItemService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.CCEItem;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoAwbService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.MacroZona;
import com.mercurio.lms.portaria.model.service.MacroZonaService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDispositivoUnitizacao;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.util.session.SessionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.unitizacaoService"
 */
public class UnitizacaoService {
	
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private ConhecimentoService conhecimentoService;
    private ParametroGeralService parametroGeralService;
    private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
    private EventoVolumeService eventoVolumeService;
    private MacroZonaService macroZonaService;
    private DomainValueService domainValueService;
    private TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService;
    private LocalizacaoMercadoriaService localizacaoMercadoriaService;
    private ConfiguracoesFacade configuracoesFacade;
    private CtoAwbService ctoAwbService;    
    private CCEItemService cceItemService;
    private ClienteService clienteService;
    
	private static final String CD_LOCALIZACAO_DISPOSITIVO_EXTRAVIADO = "CD_LOCALIZACAO_DISPOSITIVO_EXTRAVIADO";
	private static final String CD_EVENTO_DISPOSITIVO_EXTRAVIADO = "CD_EVENTO_DISPOSITIVO_EXTRAVIADO";
	private static final String CD_EVENTO_DISPOSITIVO_UNITIZADO = "CD_EVENTO_DISPOSITIVO_UNITIZADO";
	private static final String CD_EVENTO_DISPOSITIVO_DESUNITIZADO = "CD_EVENTO_DISPOSITIVO_DESUNITIZADO";
	
	private static final String CD_LOCALIZACAO_VOLUME_EXTRAVIADO = "CD_LOCALIZACAO_VOLUME_EXTRAVIADO";
	private static final String CD_EVENTO_VOLUME_EXTRAVIADO = "CD_EVENTO_VOLUME_EXTRAVIADO";
	private static final String CD_EVENTO_VOLUME_UNITIZADO = "CD_EVENTO_VOLUME_UNITIZADO";
	private static final String CD_EVENTO_VOLUME_DESUNITIZADO = "CD_EVENTO_VOLUME_DESUNITIZADO";
	
//	Valor	1	SF	Scan Físico
	private static final DomainValue SCAN_FISICO = new DomainValue("SF");
//	Valor	2	CS	Cascade Scan
	private static final DomainValue CASCADE_SCAN = new DomainValue("CS");

	private final Logger log = LogManager.getLogger(this.getClass());

	public Map executeFindDispositivoUnitizacaoByBarcode(String barcode){
		DispositivoUnitizacao dispositivoUnitizacao = findDispositivoUnitizacaoBarCode(barcode);
		if(dispositivoUnitizacao==null){
			dispositivoUnitizacao = storeDispositivoUnitizacao(barcode);
			if(dispositivoUnitizacao==null){
//			LMS-45018 - Dispositivo unitização não foi encontrado.
				throw new BusinessException("LMS-45018");
			}
		}else if(dispositivoUnitizacao.getLocalizacaoMercadoria()==null
				 || dispositivoUnitizacao.getLocalizacaoFilial()==null){
			//Se por algum motivo o dispositivo de unitização não possuir uma localização, após beepar este dispositivo de unitização
			//com intuito de ser o dispositivo unitizador pai, recebe a localização como "No Terminal", e a localização na Filial beepada.
			LocalizacaoMercadoria localizacaoMercadoria = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(Short.valueOf("24"));
			dispositivoUnitizacao.setLocalizacaoFilial(SessionUtils.getFilialSessao());
			dispositivoUnitizacao.setLocalizacaoMercadoria(localizacaoMercadoria);
			dispositivoUnitizacaoService.storeMWW(dispositivoUnitizacao);
		}
		return findMapDispositivoUnitizacao(dispositivoUnitizacao);
	}
	
	/**
	 * Recebe um codigo de barras de um dispositivo unitizacao para buscar os demais dados da tela. 
	 * @param barcode
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos Unitizados.
	 */
	public Map findMapDispositivoUnitizacaoByBarcode(String barcode){
		DispositivoUnitizacao dispositivoUnitizacao = findDispositivoUnitizacaoBarCode(barcode);
		
		if(dispositivoUnitizacao==null){
//			LMS-45018 - Dispositivo unitização não foi encontrado.
			throw new BusinessException("LMS-45018");
		}
		return findMapDispositivoUnitizacao(dispositivoUnitizacao); 		
	}
	
	public Map findMapDispositivoUnitizacao(DispositivoUnitizacao dispositivoUnitizacao){
		return findMapDispositivoUnitizacao(dispositivoUnitizacao, null, null);
	}
	
	public Map findMapDispositivoUnitizacao(DispositivoUnitizacao dispositivoUnitizacao, Long idControleCarga){
		return findMapDispositivoUnitizacao(dispositivoUnitizacao, idControleCarga, null);
	}
	
	public Map findMapDispositivoUnitizacao(DispositivoUnitizacao dispositivoUnitizacao, String tpOperacao){
		return findMapDispositivoUnitizacao(dispositivoUnitizacao, null, tpOperacao);
	}
	
	/**
	 * Recebe um codigo de barras de um dispositivo unitizacao para buscar os demais dados da tela. 
	 * @param barcode
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos Unitizados.
	 */
	public Map findMapDispositivoUnitizacao(DispositivoUnitizacao dispositivoUnitizacao, Long idControleCarga, String tpOperacao ){
					
		if ((dispositivoUnitizacao.getLocalizacaoMercadoria() != null) && ("29".equals(dispositivoUnitizacao.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().toString())
					|| "30".equals(dispositivoUnitizacao.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().toString()))){
				throw new BusinessException("LMS-45074");	
		}
					
		//Busca a lista de todos os Disp. Unit. que estão dentro do Disp. Unit. lido.
		List<DispositivoUnitizacao> listDisUni = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		
		//Transforma cada item da lista de Disp. Unit. na mesma formatação que o Pai, trazendo também seus conhecimento.
		List<Map> listaDispositivoUnitizacao = findListDispositivoUnitizacao(listDisUni, idControleCarga, tpOperacao );
		
		//Busca a lista de todos os Conhecimentos que estão dentro do Disp. Unit. lido.
		List listaConhecimentoDispositivoUnitizacao = dispositivoUnitizacaoService.
		              findConhecimentoByDispositivoUnitizacao(dispositivoUnitizacao.getIdDispositivoUnitizacao() , idControleCarga);
		
		List<Map> listaConhecimentoDispositivoUnitizacaoMap = new ArrayList<Map>();
		Map mapItem;
		Integer totalVolumesNivel = 0;
		for (Object item : listaConhecimentoDispositivoUnitizacao) {
			mapItem = (Map)item;
			
			Awb awb = ctoAwbService.findPreAwbByIdCto((Long) mapItem.get("idDoctoServico"));
			mapItem.put("idAwb", awb == null ? null : awb.getIdAwb());
			
			if (idControleCarga != null && StringUtils.isNotBlank(tpOperacao)) {
				Integer qtVolumes = volumeNotaFiscalService.findRowCountVolumesCarregamentoDescarga(idControleCarga, (Long) mapItem.get("idDoctoServico"), tpOperacao, dispositivoUnitizacao);
				if ("C".equals(tpOperacao)) {
					mapItem.put("qtVolumesCarregados", qtVolumes);
				} else {
					mapItem.put("qtVolumesDescarregados", qtVolumes);
				}
				
				VolumeNotaFiscal volume = findPrimeiroVolumeConhecimentoDispositivo(dispositivoUnitizacao, (Long) mapItem.get("idDoctoServico"));
				mapItem.put("isVolumePaletizado", volumeNotaFiscalService.validateIsVolumePaletizado(volume.getIdVolumeNotaFiscal()));
				totalVolumesNivel += qtVolumes;
			}
			listaConhecimentoDispositivoUnitizacaoMap.add(mapItem);
		}

		Map map = new HashMap();

		//Define qual é o AWB para este dispositivo
		if(listaConhecimentoDispositivoUnitizacaoMap != null 
				&& !listaConhecimentoDispositivoUnitizacaoMap.isEmpty()){
			map.put("idAwb", listaConhecimentoDispositivoUnitizacaoMap.get(0).get("idAwb"));
		}else if(listaDispositivoUnitizacao != null
				&& !listaDispositivoUnitizacao.isEmpty()){
			map.put("idAwb", listaDispositivoUnitizacao.get(0).get("idAwb"));
		}else{
			map.put("idAwb", -1);
		}

		//Listas de Conhecimentos e dispositivos.
		map.put("conhecimentos", listaConhecimentoDispositivoUnitizacaoMap);
		map.put("dispositivos", listaDispositivoUnitizacao);
		
		//Nome dispositivo
		
		DispositivoUnitizacao dispositivoUnitizacaoPai = dispositivoUnitizacao.getDispositivoUnitizacaoPai();
		if (dispositivoUnitizacaoPai != null) {
			map.put("idDispositivoUnitizacaoPai", dispositivoUnitizacaoPai.getIdDispositivoUnitizacao());
			DispositivoUnitizacao dispositivoUnitizacaoAvo = dispositivoUnitizacaoPai.getDispositivoUnitizacaoPai();
			if (dispositivoUnitizacaoAvo != null) {
				map.put("idDispositivoUnitizacaoAvo", dispositivoUnitizacaoAvo.getIdDispositivoUnitizacao());
			}
		}
		
		map.put("idDispositivoUnitizacao", dispositivoUnitizacao.getIdDispositivoUnitizacao());
		map.put("tpDispositivoUnit", dispositivoUnitizacao.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());
		
		//qtd para a soma de qtds de conhecimento
    	map.put("qtVolumes", totalVolumesNivel);
		map.put("qtDispositivoUnit", listaDispositivoUnitizacao.size());
		
		//Localizacao no terminal
		
		String locMercadoria = "";
		Short cdLocMercadoria = 0;
		if (dispositivoUnitizacao.getLocalizacaoMercadoria() != null) {
			locMercadoria = dispositivoUnitizacao.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria().getValue();
			cdLocMercadoria = dispositivoUnitizacao.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();
		}else{
			locMercadoria = "Sem Localização";
		}
		
		String locFilial = "";
		if (dispositivoUnitizacao.getLocalizacaoFilial() != null) {
			locFilial = dispositivoUnitizacao.getLocalizacaoFilial().getSgFilial();
		}
		map.put("locMercadoria", locMercadoria);
		map.put("cdLocMercadoria", cdLocMercadoria);
		map.put("locFilial", locFilial);
		map.put("idMacroZona", dispositivoUnitizacao.getMacroZona()!=null? dispositivoUnitizacao.getMacroZona().getIdMacroZona():null);
		
		//Retorna True se Item Extraviado
		map.put("extraviado", isDispositivoExtraviado(cdLocMercadoria));
		
		return map;
	}

	public VolumeNotaFiscal findPrimeiroVolumeConhecimentoDispositivo(DispositivoUnitizacao dispositivoUnitizacao, Long idDoctoServico) {
		List<VolumeNotaFiscal> listaVolumeNotaFiscal = volumeNotaFiscalService.findByIdConhecimento(idDoctoServico);
		for (VolumeNotaFiscal volumeNotaFiscal : listaVolumeNotaFiscal) {
			if (volumeNotaFiscal.getDispositivoUnitizacao() != null && dispositivoUnitizacao.getIdDispositivoUnitizacao().compareTo(volumeNotaFiscal.getDispositivoUnitizacao().getIdDispositivoUnitizacao()) == 0) {
				return volumeNotaFiscal;
			}
		}
		return null;
	}
	
	
	/**
	 * Grava o dispositivo de unitização se for bag ou pallet
	 * @param barcode
	 */
	public DispositivoUnitizacao storeDispositivoUnitizacao(String barcode){
		Long tpDisp = Long.parseLong(barcode.substring(0, 2));
		Long nroDispUnitBag = Long.parseLong(getParametroGeral("NR_ID_DISP_UNIT_BAG"));
		Long nroDispUnitPallet = Long.parseLong(getParametroGeral("NR_ID_DISP_UNIT_PALLET"));

		if(tpDisp.equals(nroDispUnitBag) || tpDisp.equals(nroDispUnitPallet)){
			
			Long idDispUnitBag = tipoDispositivoUnitizacaoService.getIdTipoDispositivoUnitizacaoBag();
			Long idDispUnitPallet = tipoDispositivoUnitizacaoService.getIdTipoDispositivoUnitizacaoPallet();
			
			Long idTpDispUni = null;
			
			if(tpDisp.equals(nroDispUnitBag)){
				idTpDispUni = idDispUnitBag;
			}else if(tpDisp.equals(nroDispUnitPallet)){
				idTpDispUni = idDispUnitPallet;
			}
			TipoDispositivoUnitizacao tipoDispositivoUnitizacao = tipoDispositivoUnitizacaoService.findById(idTpDispUni);
			//Chumbado segundo regra de negócio
			Filial localizacaoFilial = SessionUtils.getFilialSessao();
			LocalizacaoMercadoria localizacaoMercadoria = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(Short.valueOf("24"));
			
			DispositivoUnitizacao dispositivoUnitizacao = new DispositivoUnitizacao();
			dispositivoUnitizacao.setLocalizacaoFilial(localizacaoFilial);
			dispositivoUnitizacao.setLocalizacaoMercadoria(localizacaoMercadoria);
			dispositivoUnitizacao.setNrIdentificacao(barcode);
			dispositivoUnitizacao.setTipoDispositivoUnitizacao(tipoDispositivoUnitizacao);
			dispositivoUnitizacao.setTpSituacao(new DomainValue("A"));
			dispositivoUnitizacao.setEmpresa(SessionUtils.getEmpresaSessao());
			return dispositivoUnitizacaoService.storeMWW(dispositivoUnitizacao);
			
		}else{
			return null;
		}
	}
	
	/**
	 * Seta como encontrado um dispositivo apartir da tela
	 * @param idDispositivo
	 * @return
	 */
	public java.io.Serializable executeEventoDispositivoUnitizado(Long idDispositivo){
		return eventoDispositivoUnitizacaoService.generateEventoDispositivo(idDispositivo, CD_EVENTO_DISPOSITIVO_UNITIZADO, SCAN_FISICO.getValue());
	}
	
	public void executeEventoDispositivoUnitizado(DispositivoUnitizacao dispositivo) {
		eventoDispositivoUnitizacaoService.generateEventoDispositivoUnitizacao(dispositivo, CD_EVENTO_DISPOSITIVO_UNITIZADO, SCAN_FISICO.getValue(), null);
	}
	
	public java.io.Serializable executeEventoDispositivoDesunitizado(Long idDispositivo) {
		return eventoDispositivoUnitizacaoService.generateEventoDispositivo(idDispositivo, CD_EVENTO_DISPOSITIVO_DESUNITIZADO, SCAN_FISICO.getValue());
	}
	
	public java.io.Serializable executeEventoDispositivoUnitizadoTpScan(Long idDispositivo, String tpScan){
		return eventoDispositivoUnitizacaoService.generateEventoDispositivo(idDispositivo, CD_EVENTO_DISPOSITIVO_UNITIZADO, tpScan);
	}
	
	public java.io.Serializable executeEventoDispositivoDesunitizadoTpScan(Long idDispositivo, String tpScan) {
		return eventoDispositivoUnitizacaoService.generateEventoDispositivo(idDispositivo, CD_EVENTO_DISPOSITIVO_DESUNITIZADO, tpScan);
	}
	
	public java.io.Serializable executeEventoExtraviadoDispositivo(Long idDispositivo) {
		return eventoDispositivoUnitizacaoService.generateEventoDispositivo(idDispositivo, CD_EVENTO_DISPOSITIVO_EXTRAVIADO, CASCADE_SCAN.getValue());
	}
	
	public java.io.Serializable executeEventoExtraviadoVolume(Long idVolumeNotaFiscal){
		return eventoVolumeService.generateEventoVolume(idVolumeNotaFiscal, CD_EVENTO_VOLUME_EXTRAVIADO, CASCADE_SCAN.getValue());
	}
	
	public java.io.Serializable executeEventoVolumeUnitizado(Long idVolumeNotaFiscal){
		return eventoVolumeService.generateEventoVolume(idVolumeNotaFiscal, CD_EVENTO_VOLUME_UNITIZADO, SCAN_FISICO.getValue());
	}
	
	public java.io.Serializable executeEventoVolumeUnitizado(VolumeNotaFiscal volumeNotaFiscal){
		return eventoVolumeService.generateEventoVolumeUnitizado(volumeNotaFiscal, CD_EVENTO_VOLUME_UNITIZADO, SCAN_FISICO.getValue());
	}
	
	public void executeEventoVolumeUnitizacao(VolumeNotaFiscal volumeNotaFiscal){
		 eventoVolumeService.generateEventoVolumeUnitizacao(volumeNotaFiscal, CD_EVENTO_VOLUME_UNITIZADO, SCAN_FISICO.getValue());
	}
	
	public java.io.Serializable executeEventoVolumeDesunitizado(Long idVolumeNotaFiscal) {
		return eventoVolumeService.generateEventoVolume(idVolumeNotaFiscal, CD_EVENTO_VOLUME_DESUNITIZADO, SCAN_FISICO.getValue());
	}
		
	public java.io.Serializable executeEventoVolumeUnitizadoTpScan(Long idVolumeNotaFiscal, String tpScan){
		return eventoVolumeService.generateEventoVolume(idVolumeNotaFiscal, CD_EVENTO_VOLUME_UNITIZADO, tpScan);
	}
	
	public java.io.Serializable executeEventoVolumeDesunitizadoTpScan(Long idVolumeNotaFiscal, String tpScan) {
		return eventoVolumeService.generateEventoVolume(idVolumeNotaFiscal, CD_EVENTO_VOLUME_DESUNITIZADO, tpScan);
	}
	
	/**
	 * Busca uma lista de Dispositivos unitizados atravez de uma lista de DispositivosUnitizados, buscando os dispositivos e seus filhos.
	 * @param disUni
	 * @return Lista da Maps que retorna uma árvore de Dispositivos Unitizado.
	 */
	private List<Map> findListDispositivoUnitizacao(List<DispositivoUnitizacao> disUni, Long idControleCarga, String tpOperacao ){
        List<Map> ret = new ArrayList();
        Map mfilho;
		for (DispositivoUnitizacao dispositivoUnitizacao : disUni) {
			mfilho = new HashMap();
			List<DispositivoUnitizacao> listDisp = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivoUnitizacao.getIdDispositivoUnitizacao());
			List listaConhecimentoDispositivoUnitizacao = dispositivoUnitizacaoService.
									findConhecimentoByDispositivoUnitizacao(dispositivoUnitizacao.getIdDispositivoUnitizacao(), idControleCarga);
			
			List<Map> listaConhecimentoDispositivoUnitizacaoMap = new ArrayList<Map>();
			Map mapItem;
			Integer totalVolumesNivel = 0;
			for (Object item : listaConhecimentoDispositivoUnitizacao) {
				mapItem = (Map)item;
				
				Awb awb = ctoAwbService.findPreAwbByIdCto((Long) mapItem.get("idDoctoServico"));
				mapItem.put("idAwb", awb == null ? null : awb.getIdAwb());
				
				if (idControleCarga != null && StringUtils.isNotBlank(tpOperacao)) {
					Integer qtVolumes = volumeNotaFiscalService.findRowCountVolumesCarregamentoDescarga(idControleCarga, (Long) mapItem.get("idDoctoServico"), tpOperacao, dispositivoUnitizacao);
					if ("C".equals(tpOperacao)) {
						mapItem.put("qtVolumesCarregados", qtVolumes);
					} else {
						mapItem.put("qtVolumesDescarregados", qtVolumes);
					}
					VolumeNotaFiscal volume = findPrimeiroVolumeConhecimentoDispositivo(dispositivoUnitizacao, (Long) mapItem.get("idDoctoServico"));
					mapItem.put("isVolumePaletizado", volumeNotaFiscalService.validateIsVolumePaletizado(volume.getIdVolumeNotaFiscal()));
					totalVolumesNivel += qtVolumes;
				}

				listaConhecimentoDispositivoUnitizacaoMap.add(mapItem);
			}		

			List<Map> listDispUnitMap = findListDispositivoUnitizacao(listDisp, idControleCarga, tpOperacao);

			//Define qual é o AWB para este dispositivo
			mfilho.put("idAwb", this.getIdAwb(listaConhecimentoDispositivoUnitizacaoMap, listDispUnitMap));

			//Listas de Conhecimentos e dispositivos.
			mfilho.put("conhecimentos", listaConhecimentoDispositivoUnitizacaoMap);
			mfilho.put("dispositivos", listDispUnitMap);
			
			//Nome dispositivo
			mfilho.put("idDispositivoUnitizacao", dispositivoUnitizacao.getIdDispositivoUnitizacao());
			mfilho.put("tpDispositivoUnit", dispositivoUnitizacao.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());
			
			//qtd para a soma de qtds de conhecimento
			mfilho.put("qtVolumes", totalVolumesNivel);
			mfilho.put("qtDispositivoUnit", listDisp.size());
			
			ret.add(mfilho);
		}		
		return ret;
	}
	
	private Object getIdAwb(List<Map> listaConhecimentoDispositivoUnitizacaoMap, List<Map> listDispUnitMap) {
		if(listaConhecimentoDispositivoUnitizacaoMap != null 
				&& !listaConhecimentoDispositivoUnitizacaoMap.isEmpty()){
			return listaConhecimentoDispositivoUnitizacaoMap.get(0).get("idAwb");
		}else if(listDispUnitMap != null
				&& !listDispUnitMap.isEmpty()){
			return  listDispUnitMap.get(0).get("idAwb");
		}else{
			return null;
		}
	}

	/** Busca uma lista de Dispositivos unitizados atravez de uma lista de Ids.
	 * @param disUni
	 * @return Lista da Maps que retorna uma árvore de Dispositivos Unitizado.
	 */
	private List<DispositivoUnitizacao> findListDispositivoUnitizacaoByListLong(List<Long> disUni){
		List<DispositivoUnitizacao> listDisp = new ArrayList();
		for (Long idDispositivoUnitizacao : disUni) {
			listDisp.add(dispositivoUnitizacaoService.findById(idDispositivoUnitizacao));
		}		
		return listDisp;
	}
	
	private DispositivoUnitizacao findDispositivoUnitizacaoId(Long idDispositivoUnitizado) {
		return dispositivoUnitizacaoService.findById(idDispositivoUnitizado);
	}
	
	private VolumeNotaFiscal findVolumeNotaFiscalId(Long idVolumeNotaFiscal) {
		return volumeNotaFiscalService.findById(idVolumeNotaFiscal);
	}


	/**
	 * Busca os volumes que contenham os id recebidos da camada de apresentação
	 * @param listVolume
	 * @return Lista de Volumes
	 */
	private List<VolumeNotaFiscal> findListVolumeId(List<Long> listVolume) {
		List<VolumeNotaFiscal> listaVolumesNotaFiscal = new ArrayList<VolumeNotaFiscal>();
		for (Long dispUnit : listVolume) {
			listaVolumesNotaFiscal.add(findVolumeNotaFiscalId(dispUnit));
		}
		return listaVolumesNotaFiscal;
	}
	
	
	/**
	 * Verifica volumes a serem inseridos na lista de dispositivos
	 * @param barCode
	 * @return
	 */
	public Map<String, Object> findLeituraNovoVolume(String barCode){
		
			Map<String, Object> criteria = new HashMap();
			criteria.put("nrVolumeEmbarque", barCode);

			VolumeNotaFiscal volume = null;
			List listaVolume = volumeNotaFiscalService.find(criteria);
			if(listaVolume.size() == 1){
				volume = (VolumeNotaFiscal)listaVolume.get(0);
			}
			else{
				//LMS-45017 - Volume não foi encontrado.
				throw new BusinessException("LMS-45017");
			}
			
		Conhecimento conhecimento = conhecimentoService.findByIdInitLazyProperties(volume.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento(), false);
			LocalizacaoMercadoria localizacaoMercadoria = volume.getLocalizacaoMercadoria();
			
			Map<String, Object> map = new HashMap();
			
			map.put("qtTotalConhecimentos", conhecimento.getQtVolumes());

			map.put("locMercadoria", localizacaoMercadoria!=null?localizacaoMercadoria.getDsLocalizacaoMercadoria():"Sem Localização");
			map.put("cdLocMercadoria",  localizacaoMercadoria!=null?localizacaoMercadoria.getCdLocalizacaoMercadoria():0);
			map.put("locFilial", volume.getLocalizacaoFilial()!=null?volume.getLocalizacaoFilial().getSgFilial():"");
			map.put("extraviado", localizacaoMercadoria!=null?isVolumeExtraviado(localizacaoMercadoria.getCdLocalizacaoMercadoria()):false);
					
			return map;		
	}
	
	
	public void storePalletBag(Long idDispositivoUnitizacao){
		if(idDispositivoUnitizacao!=null){
			DispositivoUnitizacao dispositivoUnitizacao = dispositivoUnitizacaoService.findById(idDispositivoUnitizacao);
			List<DispositivoUnitizacao> lstDispUnit = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivoUnitizacao.getIdDispositivoUnitizacao());
			List<VolumeNotaFiscal> volumeList = volumeNotaFiscalService.findVolumeByIdDispositivoUnitizacao(dispositivoUnitizacao.getIdDispositivoUnitizacao());
			
			Long idPallet = Long.parseLong((parametroGeralService.findConteudoByNomeParametro("ID_TP_DISP_UNIT_PALLET", false)).toString());
	    	Long idBag = Long.parseLong((parametroGeralService.findConteudoByNomeParametro("ID_TP_DISP_UNIT_BAG", false)).toString());
			
	    	Long idTpDisp = dispositivoUnitizacao.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao();
			if((idTpDisp.equals(idPallet) || idTpDisp.equals(idBag)) 
				&& lstDispUnit.isEmpty() && volumeList.isEmpty() ){
				dispositivoUnitizacao.setTpSituacao(new DomainValue("I"));
				dispositivoUnitizacaoService.store(dispositivoUnitizacao);
			}
		}
	}
	
	private List<VolumeNotaFiscal> findListVolumeNotaFiscal(Long idDispositivoUnitizacao) {
			Map<String, Object> criteria = new HashMap();
			criteria.put("dispositivoUnitizacao.idDispositivoUnitizacao", idDispositivoUnitizacao);
		return volumeNotaFiscalService.find(criteria);
	}
	
	
	private Integer findSizeVolumeNotaFiscal(Long idDispositivoUnitizacaoFilho) {
		return findListVolumeNotaFiscal(idDispositivoUnitizacaoFilho).size();
	}
	
	/**
	 * Busca os Dispositivos pelos codigo de barras.
	 * @param barCode
	 * @return
	 */
	private DispositivoUnitizacao findDispositivoUnitizacaoBarCode(String barCode){
		return dispositivoUnitizacaoService.findByBarcode(barCode);		
		}
	
	//DESUNITIZAR 
	/**
	 * Busca Dispositivos Unitizacao de onde deve ser removido os Volumes.
	 */
	public Map findDadosDispositivoUnitizacao(String barCode){
		
		DispositivoUnitizacao dispositivoUnitizacao = findDispositivoUnitizacaoBarCode(barCode);
		if(dispositivoUnitizacao==null){
//			LMS-45018 - Dispositivo unitização não foi encontrado.
			throw new BusinessException("LMS-45018");
		}
		
		List<VolumeNotaFiscal> listaVolumeNotaFiscal = findListVolumeNotaFiscal(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		List<DispositivoUnitizacao> listDisUni = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		Long dispositivoUnitizacaoPai = Long.valueOf(0L);
		
		if(dispositivoUnitizacao.getDispositivoUnitizacaoPai()!=null){
			dispositivoUnitizacaoPai = dispositivoUnitizacao.getDispositivoUnitizacaoPai().getIdDispositivoUnitizacao();
		}
		
		String tpDispositivoUnit = dispositivoUnitizacao.getTipoDispositivoUnitizacao()!=null?
				dispositivoUnitizacao.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao().getValue():"";

		String dsLocalizacaoMercadoria = (dispositivoUnitizacao.getLocalizacaoMercadoria()!=null?
				dispositivoUnitizacao.getLocalizacaoMercadoria().getDsLocalizacaoMercadoria():"Sem Localização").toString();
		Short cdLocalizacaoMercadoria = dispositivoUnitizacao.getLocalizacaoMercadoria()!=null?
				dispositivoUnitizacao.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria():0;
		String sgFilial = dispositivoUnitizacao.getLocalizacaoFilial()!=null?dispositivoUnitizacao.getLocalizacaoFilial().getSgFilial():"";
		
		Map map = new HashMap();
		
		map.put("idDispositivoUnitizacao", dispositivoUnitizacao.getIdDispositivoUnitizacao());
		map.put("idDispositivoUnitizacaoPai", dispositivoUnitizacaoPai);
		map.put("tpDispositivoUnit", tpDispositivoUnit);
		map.put("qtVolumes", listaVolumeNotaFiscal.size());
		map.put("qtDispositivoUnit", listDisUni.size());
		map.put("locMercadoria", dsLocalizacaoMercadoria);
		map.put("cdLocMercadoria", cdLocalizacaoMercadoria);
		map.put("locFilial", sgFilial);
		map.put("extraviado", isDispositivoExtraviado(cdLocalizacaoMercadoria));
		
		return map;
	}
	
	
	//DESUNITIZAR PARCIAL
	/**
	 * Busca volumes a serem removidos, verificando se estão encaixadas nas regras. 
	 * Verificar se o volume pertence aquele dispositivo. 
	 * 
	 */
	public Map findDadosVolumeNotaFiscal(String barCode, Long idDispositivoUnitizacao){
		/** LMS-1039 */
		final Map<String, String> alias = new HashMap<String, String>();
		alias.put("localizacaoMercadoria", "loc");
		alias.put("notaFiscalConhecimento", "nfc");		
		alias.put("notaFiscalConhecimento.conhecimento", "con");
		alias.put("notaFiscalConhecimento.conhecimento.filialOrigem", "cfo");					
		alias.put("dispositivoUnitizacao", "du");
		
		/** Busca Volume Nota Fiscal */
		final VolumeNotaFiscal volumeNotaFiscal = volumeNotaFiscalService.findVolumeByBarCodeUniqueResult(barCode, alias);
		
		//Verificar se o volume pertence aquele dispositivo. 
		if (volumeNotaFiscal.getDispositivoUnitizacao().getIdDispositivoUnitizacao() != idDispositivoUnitizacao) {
			//LMS-45029 - O volume {0} não pertence a este dispositivo. 
			throw new BusinessException("LMS-45029", new Object[]{volumeNotaFiscal.getNrVolumeEmbarque()});
		}
		
		final Conhecimento conhecimento = volumeNotaFiscal.getNotaFiscalConhecimento().getConhecimento();
		final Filial filialOrigem = conhecimento.getFilialOrigem();
		
		final Map map = new HashMap();
		map.put("nrConhecimento", conhecimento.getNrConhecimento());
		map.put("qtVolumes", conhecimento.getQtVolumes());
		map.put("sgFilialOrigem", filialOrigem.getSgFilial());
		map.put("idVolumeNotaFiscal", volumeNotaFiscal.getIdVolumeNotaFiscal());
		map.put("extraviado", volumeNotaFiscal.getLocalizacaoMercadoria()!=null?isVolumeExtraviado(volumeNotaFiscal.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria()):false);
		return map;
	}
	
	//DESUNITIZAR PARCIAL
	public Map findDispositivoUnitizacaoByPai(String barCode, Long idDispositivoUnitizacao){
		
		ArrayList<Map> volumes = new ArrayList<Map>();
		Conhecimento conhecimento;
		Map mapVol = new HashMap();
		ArrayList<Map> dispositivos = new ArrayList<Map>();
		Map<String, Object> mapDU = new HashMap();		
		int contadorVol=0;
		int contadorDisp=0;
		
		
		DispositivoUnitizacao dispositivoUnitizacao = findDispositivoUnitizacaoBarCode(barCode);
		if(dispositivoUnitizacao==null){
//			LMS-45018 - Dispositivo unitização não foi encontrado.
			throw new BusinessException("LMS-45018");
		}
		
		//Verificar se o dispositivo pertence aquele dispositivo. 
		if (dispositivoUnitizacao.getDispositivoUnitizacaoPai().getIdDispositivoUnitizacao() != idDispositivoUnitizacao) {
//			LMS-45030 - O dispositivo {0} não pertence a este dispositivo.
			throw new BusinessException("LMS-45030", new Object[]{dispositivoUnitizacao.getNrIdentificacao()});
		}
		
		List<VolumeNotaFiscal> listaVolumeNotaFiscal = findListVolumeNotaFiscal(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		List<DispositivoUnitizacao> listDisUni = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		
		for (VolumeNotaFiscal volume : listaVolumeNotaFiscal) {
			conhecimento = volume.getNotaFiscalConhecimento().getConhecimento();
						
			mapVol.put("nrSequencia", volume.getNrSequencia());
			mapVol.put("qtVolumes",conhecimento.getQtVolumes());
			mapVol.put("nrConhecimento", conhecimento.getNrConhecimento());
			mapVol.put("sgFilialDocumento", conhecimento.getFilialOrigem().getSgFilial());
			volumes.add(mapVol);
			contadorVol++;
		}
		
		for (DispositivoUnitizacao dispUnitFilho : listDisUni) {
						
			mapDU.put("qtVolumes", findSizeVolumeNotaFiscal(dispUnitFilho.getIdDispositivoUnitizacao()));
			mapDU.put("qtDispositivoUnit", findSizeDispositivoUnitizacaoId(dispUnitFilho.getIdDispositivoUnitizacao()));
			mapDU.put("tpDispositivoUnit", dispUnitFilho.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());
			dispositivos.add(mapDU);
			contadorDisp++;
		}
		
		Map map = new HashMap();
		
		map.put("idDispositivoUnitizacao", dispositivoUnitizacao.getIdDispositivoUnitizacao());
		map.put("tpDispositivoUnit", dispositivoUnitizacao.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao().getValue());
		map.put("qtVolumes", contadorVol);
		map.put("qtDispositivoUnit", contadorDisp);
		map.put("volumes", volumes);
		map.put("dispositivos", dispositivos);
		map.put("extraviado", isDispositivoExtraviado(dispositivoUnitizacao.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria()));
		
		return map;
	}
	
	
	public Map storeDesunitizarTotalComDivergencia(Long idDispositivoUnitizado, List<VolumeNotaFiscal> idsVolumes, List<DispositivoUnitizacao> idsDispositivoUnitizacao){
	    return storeDesunitizarTotal(idDispositivoUnitizado, idsVolumes, idsDispositivoUnitizacao, true);
	}
	
	public Map storeDesunitizarTotal(Long idDispositivoUnitizado, List<VolumeNotaFiscal>idsVolumes, List<DispositivoUnitizacao> idsDispositivoUnitizacao){
	    return storeDesunitizarTotal(idDispositivoUnitizado, idsVolumes, idsDispositivoUnitizacao, false);
	}
	
	
	/**
	 * Compara idsVolumes com a listVolumes para ver se não faltou nenhum item, e retorna a quantidade de elementos que falta desunitizar.
	 * @param idDispositivoUnitizado
	 * @param idsVolumes
	 * @return qtdFaltaDesunitizar, listaDiferencas
	 */
	public Map storeDesunitizarTotal(Long idDispositivoUnitizado, List<VolumeNotaFiscal> idsVolumes, List<DispositivoUnitizacao> idsDispositivoUnitizacao, Boolean divergencia){

			Map map = new HashMap();
			Integer qtdFaltaDesunitizarVolume=0;
			Integer qtdFaltaDesunitizarDispUnit=0;
			
			List<VolumeNotaFiscal> listVolumes = findListVolumeNotaFiscal(idDispositivoUnitizado);
			//Retorno de Diferenca
			List<VolumeNotaFiscal> listVolumeDiferencas = new ArrayList<VolumeNotaFiscal>();
			//Vai ser gravado
			List<Long> listVolumesDesunitizar = new ArrayList<Long>();
			//Busca dentro da lista que vem do BD, se na lista que vem da Tela possui algum volumeNotaFiscal
			for (VolumeNotaFiscal volumeNotaFiscal : listVolumes) {
				if(!idsVolumes.contains(volumeNotaFiscal)){
					qtdFaltaDesunitizarVolume++;
					/**
					 * a.Se houver divergências entre o que estava unitizado e o que foi lido, o sistema apresentará as inconsistências 
					 * pertinentes em tela.
					 */
					listVolumeDiferencas.add(volumeNotaFiscal);
					
				}else if(qtdFaltaDesunitizarVolume<=0 || divergencia){
					listVolumesDesunitizar.add(volumeNotaFiscal.getIdVolumeNotaFiscal());
				}
			}
			
			/**INICIO DISPOSITIVO**/
			List<DispositivoUnitizacao> listDispositivo = dispositivoUnitizacaoService.findDispositivosByIdPai(idDispositivoUnitizado);
			//Retorno de Diferenca
			List<DispositivoUnitizacao> listDispositivoDiferencas = new ArrayList<DispositivoUnitizacao>();
			//Vai ser gravado
			List<Long> listDispositivoDesunitizar = new ArrayList<Long>();
			//Busca dentro da lista que vem do BD, se na lista que vem da Tela possui algum volumeNotaFiscal
			for (DispositivoUnitizacao dispositivoUnitizacao: listDispositivo) {
				if(!idsDispositivoUnitizacao.contains(dispositivoUnitizacao)){
					qtdFaltaDesunitizarDispUnit++;
					listDispositivoDiferencas.add(dispositivoUnitizacao);
				}else if(qtdFaltaDesunitizarDispUnit<=0){
					listDispositivoDesunitizar.add(dispositivoUnitizacao.getIdDispositivoUnitizacao());
				}
			}
			/**FINAL DISPOSITIVO**/

			if(qtdFaltaDesunitizarDispUnit<=0 && qtdFaltaDesunitizarVolume<=0){
				storeDesunitizacaoTotalComListas(null, listVolumesDesunitizar, listDispositivoDesunitizar);
				storePalletBag(idDispositivoUnitizado);
			}
			
			if(divergencia){
				storeExtraviado(listVolumeDiferencas, listDispositivoDiferencas);
				storeDesunitizacaoTotalComListas(null, listVolumesDesunitizar, listDispositivoDesunitizar);
				storePalletBag(idDispositivoUnitizado);
			}
			
			map.put("qtVolumes", qtdFaltaDesunitizarVolume);
			map.put("qtDispositivoUnit", qtdFaltaDesunitizarDispUnit);
			
			return map;
			
	}
	
	
	/**
	 * Envia somente idsVolume que realmente serão desunitizados
	 * @param idDispositivoUnitizado
	 * @param idsVolumes
	 */
	public void storeDesunitizarParcial(Long idDispositivoUnitizado, List<VolumeNotaFiscal> idsVolumes, List<DispositivoUnitizacao> idsDispositivoUnitizacao){
		if(idsVolumes != null) {
			DispositivoUnitizacao dispositivoUnitizacao = null;
			for (VolumeNotaFiscal volumeNotaFiscal : idsVolumes) {
				
				if(volumeNotaFiscal.getDispositivoUnitizacao()!=null 
						&& volumeNotaFiscal.getDispositivoUnitizacao().getIdDispositivoUnitizacao().equals(idDispositivoUnitizado)){
					
					dispositivoUnitizacao = dispositivoUnitizacaoService.findById(idDispositivoUnitizado);	
					
					String msg = dispositivoUnitizacao.getTipoDispositivoUnitizacao() != null ? 
							(" - " + dispositivoUnitizacao.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao().getValue()) : "";
							
					volumeNotaFiscal.setDispositivoUnitizacao(null);
					volumeNotaFiscalService.store(volumeNotaFiscal);
					eventoVolumeService.generateEventoVolume(
							volumeNotaFiscal.getIdVolumeNotaFiscal(), CD_EVENTO_VOLUME_DESUNITIZADO, 
							SCAN_FISICO.getValue(), 
							configuracoesFacade.getMensagem("desunitizacaoParcial") + msg);
				}
			}
		}
		
		if(idsDispositivoUnitizacao != null) {
			for (DispositivoUnitizacao dispositivoUnitizacao: idsDispositivoUnitizacao) {
				if(dispositivoUnitizacao.getDispositivoUnitizacaoPai()!=null 
						&& dispositivoUnitizacao.getDispositivoUnitizacaoPai().getIdDispositivoUnitizacao().equals(idDispositivoUnitizado)){
					dispositivoUnitizacao.setDispositivoUnitizacaoPai(null);
					dispositivoUnitizacaoService.store(dispositivoUnitizacao);
					eventoDispositivoUnitizacaoService.generateEventoDispositivo(
							dispositivoUnitizacao.getIdDispositivoUnitizacao(), CD_EVENTO_DISPOSITIVO_DESUNITIZADO, 
							SCAN_FISICO.getValue(), configuracoesFacade.getMensagem("desunitizacaoParcial"));
				}
			}
		}
		storePalletBag(idDispositivoUnitizado);
	}

	/**
	 * Usado para converter os idsVolume que vem do .Net de string para um objecto VolumeNotaFiscal
	 * @param idsVolumes
	 * @return
	 */
	public List<VolumeNotaFiscal> findVolumesByString(String idsVolumes){
		List<VolumeNotaFiscal> listaVolumesNotaFiscal = new ArrayList<VolumeNotaFiscal>();
		
		if (idsVolumes!=null && !idsVolumes.equals("")) {
			String idsvols[] = idsVolumes.split(";");
		
			long idVol;
			VolumeNotaFiscal volume;
			
			for (String sIdVol : idsvols) {
				try {
					idVol = Long.parseLong(sIdVol);					
				} catch (Exception e) {
					//LMS-45035 - Erro na conversão do tipo {0} para o tipo {1}.
					throw new BusinessException("LMS-45035", new Object[]{"String", "long"},e);
				}
				volume = volumeNotaFiscalService.findById(idVol);
				
				if(volume.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria() == 29
						|| volume.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria() == 30){
					throw new BusinessException("LMS-45071");
				}
				
				if (volume != null) {
					listaVolumesNotaFiscal.add(volume);				
				}
			}
		}
		return listaVolumesNotaFiscal;
	}
	
	/**
	 * Usado para converter os idsDispositivosUnitizacao que vem do .Net de string para um objecto DispositivoUnitizacao
	 * @param idsDispositivosUnitizacao
	 * @return
	 */
	public List<DispositivoUnitizacao> findDispositivoByString(String idsDispositivosUnitizacao){	
		List<DispositivoUnitizacao> listaDispositivoUnitizacao = new ArrayList<DispositivoUnitizacao>();
		
		if (idsDispositivosUnitizacao!=null && !idsDispositivosUnitizacao.equals("")) {
			String idsdisps[] = idsDispositivosUnitizacao.split(";");
			
			long idDisp;
			DispositivoUnitizacao dispositvo;
			
			for (String sIdDisp : idsdisps) {
				try {
					idDisp = Long.parseLong(sIdDisp);					
				} catch (Exception e) {
					//LMS-45035 - Erro na conversão do tipo {0} para o tipo {1}.
					throw new BusinessException("LMS-45035", new Object[]{"String", "long"},e);
				}
				dispositvo = dispositivoUnitizacaoService.findById(idDisp);
				
				if ((dispositvo.getLocalizacaoMercadoria() != null) && (Short.valueOf("29").equals(dispositvo.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())
							|| Short.valueOf("30").equals(dispositvo.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria()))) {
						throw new BusinessException("LMS-45072");
				}
				
				if (dispositvo != null){
					listaDispositivoUnitizacao.add(dispositvo);					
				}
			}
			
		}
		return listaDispositivoUnitizacao;
	}
	/**
	 * Unitiza conteiners e volumes atravez de uma String[], que possua os itens separados por ";"
	 * @param idDispositivoUnitizado
	 * @param idsVolumes
	 * @param idsDispositivosUnitizacao
	 * @return
	 */
	public void storeUnitizacao(String idDispositivoUnitizado, String idsVolumes, String idsDispositivosUnitizacao ){

		List<VolumeNotaFiscal> listaVolumesNotaFiscal = findVolumesByString(idsVolumes);
		
		List<DispositivoUnitizacao> listaDispositivoUnitizacao = findDispositivoByString(idsDispositivosUnitizacao);
			
		DispositivoUnitizacao dispositivoUnitizacaoPai = findDispositivoUnitizacaoId(Long.parseLong(idDispositivoUnitizado));		
	
		if((dispositivoUnitizacaoPai.getLocalizacaoMercadoria()!=null)
				&& (dispositivoUnitizacaoPai.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("29"))
				    || dispositivoUnitizacaoPai.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().equals(Short.valueOf("30")))){
				//LMS-45073 - O dispositivo unitização está em carregamento, não é possivel concluir a unitização.
				throw new BusinessException("LMS-45073");
		}
		//ATUALIZA VOLUME
		for (VolumeNotaFiscal volumeNotaFiscal : listaVolumesNotaFiscal) {
			//Aloca volumes unitizados no dispositivo de unitizacao na MacroZona do dispositivo Pai.
			if(dispositivoUnitizacaoPai.getMacroZona()!= null){
				if(volumeNotaFiscal.getMacroZona() != null){
					if(!dispositivoUnitizacaoPai.getMacroZona().getIdMacroZona().equals(volumeNotaFiscal.getMacroZona().getIdMacroZona())){
						macroZonaService.storeAlocarVolume(volumeNotaFiscal, dispositivoUnitizacaoPai.getMacroZona(), ConstantesSim.TP_SCAN_CASCADE);
					}
				}else{
					macroZonaService.storeAlocarVolume(volumeNotaFiscal, dispositivoUnitizacaoPai.getMacroZona(), ConstantesSim.TP_SCAN_CASCADE);
				}
			}else{
				if(volumeNotaFiscal.getMacroZona() != null){
					macroZonaService.storeDesalocarVolume(volumeNotaFiscal, volumeNotaFiscal.getMacroZona(), ConstantesSim.TP_SCAN_CASCADE);
				}
			}
			
			if(volumeNotaFiscal.getDispositivoUnitizacao()!=null 
					&& volumeNotaFiscal.getDispositivoUnitizacao().getIdDispositivoUnitizacao()!=null){
				executeEventoVolumeDesunitizado(volumeNotaFiscal.getIdVolumeNotaFiscal());
			}

			volumeNotaFiscal.setDispositivoUnitizacao(dispositivoUnitizacaoPai);
			volumeNotaFiscalService.store(volumeNotaFiscal);

			executeEventoVolumeUnitizado(volumeNotaFiscal);
		}
		
		//ATUALIZA DISPOSITIVO
		for (DispositivoUnitizacao dispositivoUnitizacao : listaDispositivoUnitizacao) {
			if(dispositivoUnitizacao.getDispositivoUnitizacaoPai()!=null 
					&& dispositivoUnitizacao.getDispositivoUnitizacaoPai().getIdDispositivoUnitizacao()!=null){
				executeEventoDispositivoDesunitizado(dispositivoUnitizacao.getIdDispositivoUnitizacao());
			}

			dispositivoUnitizacao.setDispositivoUnitizacaoPai(dispositivoUnitizacaoPai);
			
			//Aloca subitens do dispositivo de unitizacao de acordo com a MacroZona do dispositivo Pai.
			if(dispositivoUnitizacaoPai.getMacroZona()!=null){
				if(dispositivoUnitizacao.getMacroZona() != null){
					if(!dispositivoUnitizacaoPai.getMacroZona().getIdMacroZona().equals(dispositivoUnitizacao.getMacroZona().getIdMacroZona())){
						macroZonaService.storeAlocarDispositivo(dispositivoUnitizacao, dispositivoUnitizacaoPai.getMacroZona(), ConstantesSim.TP_SCAN_CASCADE);
			}
				}else{
					macroZonaService.storeAlocarDispositivo(dispositivoUnitizacao, dispositivoUnitizacaoPai.getMacroZona(), ConstantesSim.TP_SCAN_CASCADE);
				}
			}else{
				if(dispositivoUnitizacao.getMacroZona() != null){
					macroZonaService.storeDesalocarDispositivo(dispositivoUnitizacao, ConstantesSim.TP_SCAN_CASCADE);
				}
			}
			
			dispositivoUnitizacaoService.store(dispositivoUnitizacao);
			executeEventoDispositivoUnitizado(dispositivoUnitizacao.getIdDispositivoUnitizacao());
		}
		
	}

	public void storeUnitizarVolumesConhecimento(Conhecimento conhecimento) {

		List<NotaFiscalConhecimento> notasFiscais =  conhecimento.getNotaFiscalConhecimentos();
		List<CCEItem> cceItens = this.findCCEItens(notasFiscais);
		Map<String, DispositivoUnitizacao> dipositivos = new HashMap<String, DispositivoUnitizacao>();

		if(cceItens != null && !cceItens.isEmpty()){
			for (NotaFiscalConhecimento notaFiscal : notasFiscais) {

				Boolean isDanfeSimplificada = notaFiscal.getCliente().getBlNfeConjulgada() != null ? notaFiscal.getCliente().getBlNfeConjulgada() : false;
				log.info(String.format("Unitizando Volumes. CLiente é Danfe Simplificada ? %s", isDanfeSimplificada));

				CCEItem cceItem = this.obterCCEItemPorNota(cceItens, notaFiscal.getNrChave());
				if(this.isUnitizacao(cceItem) || isDanfeSimplificada) {
					DispositivoUnitizacao dispositivo = this.executeDispositivoUnitizacao(cceItem.getNrUnitizacao(), dipositivos, conhecimento.getFilialOrigem().getEmpresa().getIdEmpresa());

					for(int i = 0; i < notaFiscal.getVolumeNotaFiscais().size(); i++) {
						VolumeNotaFiscal volumeNotaFiscal = (VolumeNotaFiscal) notaFiscal.getVolumeNotaFiscais().get(i);
						volumeNotaFiscal.setDispositivoUnitizacao(dispositivo);
						volumeNotaFiscalService.store(volumeNotaFiscal);
						executeEventoVolumeUnitizacao(volumeNotaFiscal);
					}
				}
			}
		}

	}
	/**
	 * busca os CCEItens para o conjunto de Notas fiscais.
	 * @param notasFiscais Lista de Notas fiscais
	 * @return Lista contendo todos os CCEItens.
	 */
	private List<CCEItem> findCCEItens (List<NotaFiscalConhecimento> notasFiscais){
		List<CCEItem> cceItens = null;		
		if(existeNotaComChave(notasFiscais)) {
			cceItens = cceItemService.findChaveNfe(notasFiscais.get(0).getNrChave());
		}
		return cceItens;
	}
	
	private boolean existeNotaComChave(List<NotaFiscalConhecimento> notasFiscais) {
		return notasFiscais != null && !notasFiscais.isEmpty() 
				 && notasFiscais.get(0).getNrChave() != null && !notasFiscais.get(0).getNrChave().isEmpty();
	}
	
	private CCEItem obterCCEItemPorNota(List<CCEItem> cceItens, String nrChaveNota) {		
		for (CCEItem cceItem : cceItens) {
			if(nrChaveNota.equals(cceItem.getNrChave())) {
				return cceItem;
			}
		}		
		return null;
	}
	
	private boolean isUnitizacao(CCEItem cceItem) {
		return cceItem != null && cceItem.getNrUnitizacao() != null && !cceItem.getNrUnitizacao().isEmpty();
	}
	
	private DispositivoUnitizacao executeDispositivoUnitizacao(String nrIdentificacao, Map<String, DispositivoUnitizacao> dipositivos, Long idEmpresa) {
		
		if(dipositivos.containsKey(nrIdentificacao)) {
			return dipositivos.get(nrIdentificacao);
		}		
		DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findByBarcodeIdEmpresa(nrIdentificacao, idEmpresa); // A pesquisa tem que ser feita pela empresa tb.
		if(dispositivo == null) {
			dispositivo = this.storeDispositivo(nrIdentificacao, idEmpresa);
		}		
		
		List<EventoDispositivoUnitizacao> eventosDispositivos = eventoDispositivoUnitizacaoService.findEventosDispositivoUnitizacao(dispositivo.getIdDispositivoUnitizacao(), CD_EVENTO_DISPOSITIVO_UNITIZADO, SCAN_FISICO, false);
		
		if(eventosDispositivos == null || eventosDispositivos.isEmpty()) {
			executeEventoDispositivoUnitizado(dispositivo);
		}		
		
		dipositivos.put(nrIdentificacao, dispositivo);
		return dispositivo;
		
	}
	
	public DispositivoUnitizacao storeDispositivo(String nrIdentificacao, Long idEmpresa) {    	    	    	
    	DispositivoUnitizacao dispositivo = new DispositivoUnitizacao();    	
    	
    	dispositivo.setNrIdentificacao(nrIdentificacao);    	
    	dispositivo.setTpSituacao(new DomainValue("A")); // Verificar o valor correto.
    	
    	
    	TipoDispositivoUnitizacao tipoDispositivo = new TipoDispositivoUnitizacao();
    	tipoDispositivo.setIdTipoDispositivoUnitizacao(tipoDispositivoUnitizacaoService.getIdTipoDispositivoUnitizacaoPallet());
    	dispositivo.setTipoDispositivoUnitizacao(tipoDispositivo);
    	
    	/* Seta o id da empresa */
    	Empresa empresa = new Empresa();
    	empresa.setIdEmpresa(idEmpresa);
    	dispositivo.setEmpresa(empresa);    	    
    	
    	return dispositivoUnitizacaoService.storeMWW(dispositivo);
    }
	/**
	 * 
		i.	Para todos os itens que estão unitizados no dispositivo pai e que não foram lidos:
			1.	Desunitizar (CD_EVENTO_VOLUME_DESUNITIZADO ou CD_EVENTO_DISPOSITIVO_DESUNITIZADO)
			2.	Gerar evento (CD_EVENTO_VOLUME_EXTRAVIADO ou CD_EVENTO_DISPOSITIVO_EXTRAVIADO)
	 *
	 */
	public void storeExtraviado(List<VolumeNotaFiscal> listVolumeDiferencas, List<DispositivoUnitizacao> listDispositivoDiferencas){
		
			for (VolumeNotaFiscal volumeNotaFiscal : listVolumeDiferencas) {
				volumeNotaFiscal.setDispositivoUnitizacao(null);
				volumeNotaFiscalService.store(volumeNotaFiscal);
				executeEventoExtraviadoVolume(volumeNotaFiscal.getIdVolumeNotaFiscal());
			}
			for (DispositivoUnitizacao dispositivoUnitizacao : listDispositivoDiferencas) {
				dispositivoUnitizacao.setDispositivoUnitizacaoPai(null);
				dispositivoUnitizacaoService.store(dispositivoUnitizacao);
				executeEventoExtraviadoDispositivo(dispositivoUnitizacao.getIdDispositivoUnitizacao());
			}
	}
	
	
	/**
	 * Salva os itens somente após o usuário ter a certeza de diferenças. 
	 * @param idDispositivoUnitizado
	 * @param idsVolumes
	 * @param idsDispositivosFilhos
	 */
	
	private void storeDesunitizacaoTotalComListas(Long idDispositivoUnitizado, List<Long> idsVolumes, List<Long> idsDispositivosFilhos){

			List<VolumeNotaFiscal> lstVolNF = findListVolumeId(idsVolumes);
			List<DispositivoUnitizacao> lstDispositivoFilhos = findListDispositivoUnitizacaoByListLong(idsDispositivosFilhos);
			DispositivoUnitizacao unitizacao = null;
			if (idDispositivoUnitizado!=null) {
				unitizacao = findDispositivoUnitizacaoId(idDispositivoUnitizado);
			}

			for (VolumeNotaFiscal volumeNotaFiscal : lstVolNF) {
				
				String msg = volumeNotaFiscal.getDispositivoUnitizacao() != null ? volumeNotaFiscal.getDispositivoUnitizacao().getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao().getValue() : null;
				volumeNotaFiscal.setDispositivoUnitizacao(unitizacao);
				volumeNotaFiscalService.store(volumeNotaFiscal);
				
			eventoVolumeService.generateEventoVolume(
						volumeNotaFiscal.getIdVolumeNotaFiscal(), CD_EVENTO_VOLUME_DESUNITIZADO, 
						SCAN_FISICO.getValue(), (msg!=null?msg:""));
			}

			for (DispositivoUnitizacao dispositivoUnitizacao : lstDispositivoFilhos) {
				dispositivoUnitizacao.setDispositivoUnitizacaoPai(unitizacao);
				dispositivoUnitizacaoService.store(dispositivoUnitizacao);
				String msg = configuracoesFacade.getMensagem("desunitizacaoTotal");
			eventoDispositivoUnitizacaoService.generateEventoDispositivo(
						dispositivoUnitizacao.getIdDispositivoUnitizacao(), CD_EVENTO_DISPOSITIVO_DESUNITIZADO, 
						SCAN_FISICO.getValue(), (msg!=null?msg:""));
			}
			
	}
	
	/**
	 * Recebe um codigo de barras de um endereco para buscar o endereco onde deve ser ALOCADO um item. 
	 * @param barcode
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos Unitizados.
	 */
	public Map findEnderecoByBarcode(BigDecimal barcode){
		MacroZona macroZona = macroZonaService.findMacroZonaByBarcode(barcode);
		if(macroZona==null){
			return null;
		}
		
		String sgFilial = macroZona.getTerminal()!=null?
							(macroZona.getTerminal().getFilial()!=null?
									macroZona.getTerminal().getFilial().getSgFilial():""):"";
		
		DomainValue domainTpSituacao = domainValueService.findDomainValueByValue("DM_STATUS", macroZona.getTpSituacao().getValue());

		Map map = new HashMap();
		map.put("idMacroZona", macroZona.getIdMacroZona());
		map.put("tpSituacao", domainTpSituacao.getDescription());
		map.put("dsMacroZona", macroZona.getDsMacroZona());
		map.put("sgFilial", sgFilial);
		return map;
	}
	
	public void storeAlocar(MacroZona macroZona, String sVolumes, String sDispositivos) {
		List<VolumeNotaFiscal> volumes = getVolumesByStringIds(sVolumes);    
		List<DispositivoUnitizacao> dispositivos = getDispositivosByStringIds(sDispositivos);
		for (VolumeNotaFiscal volume : volumes)
			macroZonaService.storeAlocarVolume(volume, macroZona, ConstantesSim.TP_SCAN_FISICO);
	
		for (DispositivoUnitizacao dispositivoUnitizacao : dispositivos) 
			macroZonaService.storeAlocarDispositivo(dispositivoUnitizacao, macroZona, ConstantesSim.TP_SCAN_FISICO);
	}
			
	public void storeDesalocar(MacroZona macroZona, String sVolumes, String sDispositivos) {
		List<VolumeNotaFiscal> volumes = getVolumesByStringIds(sVolumes);    
		List<DispositivoUnitizacao> dispositivos = getDispositivosByStringIds(sDispositivos);
		for (VolumeNotaFiscal volume : volumes)
			macroZonaService.storeDesalocarVolume(volume, macroZona, ConstantesSim.TP_SCAN_FISICO);
			
		for (DispositivoUnitizacao dispositivoUnitizacao : dispositivos) 
			macroZonaService.storeDesalocarDispositivo(dispositivoUnitizacao, ConstantesSim.TP_SCAN_FISICO);
	}
	
	private List<VolumeNotaFiscal> getVolumesByStringIds(String idsVolumes) {
		List<VolumeNotaFiscal> listaVolumesNotaFiscal = new ArrayList<VolumeNotaFiscal>();
		if (idsVolumes!=null && !idsVolumes.equals("")) {
			String idsvols[] = idsVolumes.split(";");			
			for (String sIdVol : idsvols) {
				VolumeNotaFiscal volume = volumeNotaFiscalService.findById(Long.parseLong(sIdVol));
				listaVolumesNotaFiscal.add(volume);				
			}
		}
		return listaVolumesNotaFiscal;
	}
				
	private List<DispositivoUnitizacao> getDispositivosByStringIds(String idsDispositivos) {
		List<DispositivoUnitizacao> listaDispositivoUnitizacao = new ArrayList<DispositivoUnitizacao>();
		if (idsDispositivos!=null && !idsDispositivos.equals("")) {
			String idsdisp[] = idsDispositivos.split(";");			
			for (String sIdDisp : idsdisp) {
				DispositivoUnitizacao dispositivoUnitizacao = dispositivoUnitizacaoService.findById(Long.parseLong(sIdDisp));
				listaDispositivoUnitizacao.add(dispositivoUnitizacao);				
			}
		}
		return listaDispositivoUnitizacao;
	}

	private Integer findSizeDispositivoUnitizacaoId(Long idDispositivoUnitizacaoFilho) {
		return dispositivoUnitizacaoService.findDispositivosByIdPai(idDispositivoUnitizacaoFilho).size();
	}
	
	
	/**
	 * PESQUISA SE DISPOSITIVO ESTA EXTRAVIADO
	 * c.Ao ler um item, onde a sua localização seja ‘extraviado’, o sistema apresentará uma mensagem 
			  ao usuário perguntado se a localização do item pode ser ajustada
				i.Se sim, então a localização do item será ‘No Terminal’ na filial onde foi encontrado.
				ii.O volume não estará alocado, o usuário deverá tentar novamente.
	 */
	
	public Boolean isDispositivoExtraviado(Short cdLocMercadoria){
			
		int cdLocalizacaoMercadoriaExtraviada = 0;
		
		String param = getParametroGeral(CD_LOCALIZACAO_DISPOSITIVO_EXTRAVIADO);
		
		if (param != null && !param.isEmpty()){
			try {
				cdLocalizacaoMercadoriaExtraviada = Integer.parseInt(param);
			} catch (Exception e) {
				throw new BusinessException("LMS-45035", new Object[]{"String", "Integer"});
				//LMS-45035 - Erro na conversão do tipo {0} para o tipo {1}.
			}
		}	
		return cdLocMercadoria == cdLocalizacaoMercadoriaExtraviada;
		
	}
	
	/**
	 * PESQUISA SE VOLUME ESTA EXTRAVIADO
	 * c.Ao ler um item, onde a sua localização seja ‘extraviado’, o sistema apresentará uma mensagem 
			  ao usuário perguntado se a localização do item pode ser ajustada
				i.Se sim, então a localização do item será ‘No Terminal’ na filial onde foi encontrado.
				ii.O volume não estará alocado, o usuário deverá tentar novamente.
	 */
	public Boolean isVolumeExtraviado(Short cdLocMercadoria){
			
			int cdLocalizacaoMercadoriaExtraviada = 0;
			String sTemp = getParametroGeral(CD_LOCALIZACAO_VOLUME_EXTRAVIADO);
			if (sTemp != null && !sTemp.isEmpty()){
				try {
					cdLocalizacaoMercadoriaExtraviada = Integer.parseInt(sTemp);
				} catch (Exception e) {
					throw new BusinessException("LMS-45035", new Object[]{"String", "Integer"});
					//LMS-45035 - Erro na conversão do tipo {0} para o tipo {1}.
				}
			}
			return cdLocMercadoria == cdLocalizacaoMercadoriaExtraviada;
			
	}
	
	
	private String getParametroGeral(String nmParam){
		ParametroGeral param = parametroGeralService.findByNomeParametro(nmParam, Boolean.FALSE);
		if (param!=null) {
			return param.getDsConteudo();
		}else{
			return null;
		}
	}
	
	/*
	 * 
	 * GETTERS AND SETTERS
	 */
	public void setDispositivoUnitizacaoService(DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}

	public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}


	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}

	public void setMacroZonaService(MacroZonaService macroZonaService) {
		this.macroZonaService = macroZonaService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setTipoDispositivoUnitizacaoService(TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService) {
		this.tipoDispositivoUnitizacaoService = tipoDispositivoUnitizacaoService;
	}

	public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setEventoDispositivoUnitizacaoService(EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
		this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
	}

	public CtoAwbService getCteAwbService() {
		return ctoAwbService;
	}

	public void setCteAwbService(CtoAwbService ctoAwbService) {
		this.ctoAwbService = ctoAwbService;
	}

	public CCEItemService getCceItemService() {
		return cceItemService;
	}

	public void setCceItemService(CCEItemService cceItemService) {
		this.cceItemService = cceItemService;
	}	

}