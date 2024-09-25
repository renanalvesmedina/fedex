package com.mercurio.lms.portaria.model.service;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.EntityMode;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.DispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.service.DispositivoUnitizacaoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.MacroZona;
import com.mercurio.lms.portaria.model.dao.MacroZonaDAO;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DeParaXmlCte;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.macroZonaService"
 */
public class MacroZonaService extends CrudService<MacroZona, Long> {
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private EventoDispositivoUnitizacaoService eventoDispositivoService;
	private EventoVolumeService eventoVolumeService;
	private ConfiguracoesFacade configuracoesFacade;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private ConhecimentoService conhecimentoService;
	private FilialService filialService;

    private final String CD_EVENTO_ALOCADO = "CD_EVENTO_ALOCADO";
    private final String CD_EVENTO_DESALOCADO = "CD_EVENTO_DESALOCADO";
	
	
    private MacroZonaDAO getMacroZonaDAO() {
        return (MacroZonaDAO) getDao();
    }
    
    public void setMacroZonaDAO(MacroZonaDAO dao) {
        setDao(dao);
    }


	/**
	 * Recupera uma instância de <code>MacroZona</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public MacroZona findById(java.lang.Long id) {
        return getMacroZonaDAO().findById(id);
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(MacroZona bean) {
    	if (bean.getNrCodigoBarras() != null) {
    		String strCodigoBarras = bean.getNrCodigoBarras().toString();
    		if (!strCodigoBarras.substring(0,2).equals("41")){
    			throw new BusinessException("LMS-06031");
    		}
    	}
    	return getMacroZonaDAO().store(bean);
    }   
    
    public ResultSetPage<MacroZona> findPaginated(PaginatedQuery paginatedQuery) {
		return getMacroZonaDAO().findPaginated(paginatedQuery);
	}

    public MacroZona findMacroZonaByBarcode(BigDecimal barcode) {
        return getMacroZonaDAO().findMacroZonaByBarcode(barcode);
    }
    
    public void storeDesalocarDispositivo(DispositivoUnitizacao dispositivo, String tpScan) {    	
    	this.storeAlocarDispositivo(dispositivo, null, tpScan);
    }
    
    public void storeDesalocarDispositivo(Long idDispositivoUnitizacao, String tpScan) {    	
    	this.storeAlocarDispositivo(idDispositivoUnitizacao, null, tpScan);
    }
    
    public void storeAlocarDispositivo(Long idDispositivoUnitizacao, MacroZona macroZona, String tpScan) {
    	DispositivoUnitizacao dispositivo = dispositivoUnitizacaoService.findById(idDispositivoUnitizacao);
    	this.storeAlocarDispositivo(dispositivo, macroZona, tpScan);
    }
    
    public void storeAlocarDispositivo(DispositivoUnitizacao dispositivo, MacroZona macroZona, String tpScan) {
		Short cdLocalizacaoMercadoria = dispositivo.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();
		Long idFilialLocalizacao = dispositivo.getLocalizacaoFilial().getIdFilial();
		
		String dsDispositivo = configuracoesFacade.getMensagem("dispUnitizacao") + ": " + dispositivo.getNrIdentificacao();
		
		this.validateLocalizacaoMercadoria(cdLocalizacaoMercadoria, idFilialLocalizacao, dsDispositivo);
		
		if(ConstantesSim.TP_SCAN_FISICO.equals(tpScan))
			dispositivo.setDispositivoUnitizacaoPai(null);
    	
    	List<VolumeNotaFiscal> volumes = volumeNotaFiscalService.findVolumeByIdDispositivoUnitizacao(dispositivo.getIdDispositivoUnitizacao());  
    	if(!volumes.isEmpty()) {    		
    		for(VolumeNotaFiscal volume : volumes) {
    			this.storeAlocarVolume(volume, macroZona, ConstantesSim.TP_SCAN_CASCADE);
    		}
    	}
    	    	
    	List<DispositivoUnitizacao> dispositivos = dispositivoUnitizacaoService.findDispositivosByIdPai(dispositivo.getIdDispositivoUnitizacao());
    	if(!dispositivos.isEmpty()) {
    		for(DispositivoUnitizacao dispositivoFilho : dispositivos) {
    			this.storeAlocarDispositivo(dispositivoFilho, macroZona, ConstantesSim.TP_SCAN_CASCADE);
    		}
    	}
    	
    	dispositivo.setMacroZona(macroZona);
    	dispositivoUnitizacaoService.store(dispositivo);
    	
    	if(macroZona != null){
			eventoDispositivoService.generateEventoDispositivo(
					dispositivo.getIdDispositivoUnitizacao(), CD_EVENTO_ALOCADO, 
					tpScan, configuracoesFacade.getMensagem("alocadoMacroZona", new Object[]{ macroZona.getDsMacroZona() }));
		}
		else
		{
			eventoDispositivoService.generateEventoDispositivo(
					dispositivo.getIdDispositivoUnitizacao(), CD_EVENTO_DESALOCADO, 
					tpScan);
		}
    	
    }
    
    public void storeDesalocarVolume(Long idVolumeNotaFiscal, String tpScan) {
    	VolumeNotaFiscal volume = volumeNotaFiscalService.findVolumeById(idVolumeNotaFiscal);
    	this.storeAlocarVolume(volume, null, tpScan);    	
    }
    
    public void storeDesalocarVolume(VolumeNotaFiscal volumeNotaFiscal, String tpScan) {
    	this.storeAlocarVolume(volumeNotaFiscal, null, tpScan);    	
    }
    
    // LMS 428
    public void storeDesalocarVolume(VolumeNotaFiscal volume, MacroZona macroZona, String tpScan) {
		
		storeAlocarDesalocarVolume(volume, null, tpScan);
		
		String dsMacroZona = macroZona != null ? macroZona.getDsMacroZona() : null;
		
		if(!dsMacroZona.isEmpty()) {
			eventoVolumeService.generateEventoVolume(
					volume.getIdVolumeNotaFiscal(), CD_EVENTO_DESALOCADO, 
					tpScan, configuracoesFacade.getMensagem("desalocadoMacroZona", new Object[]{ macroZona.getDsMacroZona() }));
		} else {
			eventoVolumeService.generateEventoVolume(
					volume.getIdVolumeNotaFiscal(), CD_EVENTO_DESALOCADO, 
					tpScan);
		}
    }
    
    public void storeAlocarVolume(Long idVolumeNotaFiscal, MacroZona macroZona, String tpScan) {
    	VolumeNotaFiscal volume = volumeNotaFiscalService.findVolumeById(idVolumeNotaFiscal);
    	this.storeAlocarVolume(volume, macroZona, tpScan);    	
    }

	public void storeAlocarVolume(VolumeNotaFiscal volume, MacroZona macroZona, String tpScan) {
		
		storeAlocarDesalocarVolume(volume, macroZona, tpScan);
		
		if(macroZona != null){
			eventoVolumeService.generateEventoVolume(
					volume.getIdVolumeNotaFiscal(), CD_EVENTO_ALOCADO, 
					tpScan, configuracoesFacade.getMensagem("alocadoMacroZona", new Object[]{ macroZona.getDsMacroZona() }));
		}
		else
		{
			eventoVolumeService.generateEventoVolume(
					volume.getIdVolumeNotaFiscal(), CD_EVENTO_DESALOCADO, 
					tpScan);
		}
	}

	// LMS 428
	private void storeAlocarDesalocarVolume(VolumeNotaFiscal volume,
			MacroZona macroZona, String tpScan) {
		Short cdLocalizacaoMercadoria = volume.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria();
		Long idFilialLocalizacao = volume.getLocalizacaoFilial().getIdFilial();
		
		NotaFiscalConhecimento nfc = notaFiscalConhecimentoService.findById(volume.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento());
		Conhecimento con = conhecimentoService.findByIdInitLazyProperties(nfc.getConhecimento().getIdDoctoServico(), false);
		Filial filialOrigem = filialService.findById(con.getFilialByIdFilialOrigem().getIdFilial());
		
		String dsVolume = filialOrigem.getSgFilial() + " "
				+ org.apache.commons.lang.StringUtils.leftPad(volume.getNrConhecimento().toString(), 8, "0") + " "
				+ volume.getNrSequencia().toString() + "/" + con.getQtVolumes().toString();
			
		//Verifica localização dos volumes .
		
		this.validateLocalizacaoMercadoria(cdLocalizacaoMercadoria, idFilialLocalizacao, dsVolume);
		
		if(ConstantesSim.TP_SCAN_FISICO.equals(tpScan))
			volume.setDispositivoUnitizacao(null);
		
		volume.setMacroZona(macroZona);
		volumeNotaFiscalService.store(volume);
	}	

	private void validateLocalizacaoMercadoria(Short cdLocalizacaoMercadoria, Long idFilialLocalizacao, String dsItem) {
		if (!SessionUtils.getFilialSessao().getIdFilial().equals(idFilialLocalizacao) || (!isCdLocalizacaoParametrizado(cdLocalizacaoMercadoria))) {
			throw new BusinessException("LMS-45080", new Object[]{ dsItem, SessionUtils.getFilialSessao().getSgFilial() });
		}    	
	}
	
	private Boolean isCdLocalizacaoParametrizado(Short cdLocalizacaoMercadoria){
		Boolean cdLocalizacaoParametrizado = Boolean.FALSE;
		
		if(cdLocalizacaoMercadoria != null){
			String[] cdLocalizacoes = ((String) configuracoesFacade.getValorParametro("MWW_LOCALIZACAO_ALOCACAO")).split(";");
			
			for (String cdLocalizacao : cdLocalizacoes){
				if(cdLocalizacaoMercadoria.equals(Short.parseShort(cdLocalizacao))){
					cdLocalizacaoParametrizado = Boolean.TRUE;
					break;
				}
			}
		}
		
		return cdLocalizacaoParametrizado;
	}

	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public void setDispositivoUnitizacaoService(
			DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}

	public void setEventoDispositivoService(
			EventoDispositivoUnitizacaoService eventoDispositivoService) {
		this.eventoDispositivoService = eventoDispositivoService;
	}

	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}