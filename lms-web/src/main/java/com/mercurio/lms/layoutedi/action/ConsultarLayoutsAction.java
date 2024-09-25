package com.mercurio.lms.layoutedi.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.lms.edi.model.CampoLayoutEDI;
import com.mercurio.lms.edi.model.ComposicaoLayoutEDI;
import com.mercurio.lms.edi.model.RegistroLayoutEDI;
import com.mercurio.lms.edi.model.service.CampoLayoutEDIService;
import com.mercurio.lms.edi.model.service.ComposicaoLayoutEdiService;
import com.mercurio.lms.edi.model.service.RegistroLayoutEDIService;
import com.mercurio.lms.edi.model.service.LayoutEDIService;
import com.mercurio.lms.layoutedi.model.CampoLayoutEdiWSRetorno;
import com.mercurio.lms.layoutedi.model.GeraArquivosTransformacaoWSRetorno;
import com.mercurio.lms.layoutedi.model.LayoutEdiWSRetorno;

/**
 * 
 * @author ThiagoFA
 * @spring.bean id="lms.layoutedi.ConsultarLayoutsAction"
 */
@ServiceSecurity
public class ConsultarLayoutsAction {
	private CampoLayoutEDIService campoLayoutEdiService;
	private ComposicaoLayoutEdiService composicaoLayoutEdiService;
	private RegistroLayoutEDIService registroLayoutEDIService;
	private LayoutEDIService layoutEDIService;
	private Long principal;
	private String cabecalho;
	private Long idClienteLayoutEdi;
	
	public Long getPrincipal() {
		return principal;
	}
	public void setPrincipal(Long principal) {
		this.principal = principal;
	}
	public CampoLayoutEDIService getCampoLayoutEdiService() {
		return campoLayoutEdiService;
	}
	public void setCampoLayoutEdiService(CampoLayoutEDIService campoLayoutEdiService) {
		this.campoLayoutEdiService = campoLayoutEdiService;
	}
	public ComposicaoLayoutEdiService getComposicaoLayoutEdiService() {
		return composicaoLayoutEdiService;
	}
	public void setComposicaoLayoutEdiService(
			ComposicaoLayoutEdiService composicaoLayoutEdiService) {
		this.composicaoLayoutEdiService = composicaoLayoutEdiService;
	}
	public RegistroLayoutEDIService getRegistroLayoutEDIService() {
		return registroLayoutEDIService;
	}
	public void setRegistroLayoutEDIService(
			RegistroLayoutEDIService registroLayoutEDIService) {
		this.registroLayoutEDIService = registroLayoutEDIService;
	}
	public LayoutEDIService getLayoutEDIService(){
		return layoutEDIService;
	}
	public void setLayoutEDIService(LayoutEDIService layoutEDIService){
		this.layoutEDIService = layoutEDIService;
	}

	@MethodSecurity(processGroup = "layoutedi.geraArquivosTransformacaoWebService", processName = "buscaIdClienteLayoutPorTipo", authenticationRequired=false)											  
	public Long buscaIdClienteLayoutPorTipo(Map params){
		Long idClienteLayoutEdi = (Long)params.get("idClienteLayoutEdi");
		String tipoLayout = (String)params.get("tpLayout");
		Long idLayoutEdi = registroLayoutEDIService.findClienteLayoutPorTipo(idClienteLayoutEdi, tipoLayout);
		return idLayoutEdi;
	}
	
	
	
	@MethodSecurity(processGroup = "layoutedi.geraArquivosTransformacaoWebService", processName = "buscaIdLayoutPorTipo", authenticationRequired=false)											  
	public Long buscaIdLayoutPorTipo(Map params){
		Long idClienteLayoutEdi = (Long)params.get("idClienteLayoutEdi");
		String tipoLayout = (String)params.get("tpLayout");
		Long idLayoutEdi = registroLayoutEDIService.findLayoutPorTipo(idClienteLayoutEdi, tipoLayout);
		return idLayoutEdi;
	}	
	
	@MethodSecurity(processGroup = "layoutedi.buscaTipoArquivoLayoutEdi", processName = "buscaTipoArquivoLayoutEdi", authenticationRequired=false)											  
	public String buscaTipoArquivoLayoutEdi(Map params){
		Long idLayoutEdi = (Long)params.get("idLayoutEdi"); 
		String tipoArquivo = layoutEDIService.findTipoArquivo(idLayoutEdi);
		return tipoArquivo;
	}	
	
	
	private List<LayoutEdiWSRetorno> buscaFilhosPorTipo(Long idLayout, LayoutEdiWSRetorno lewsr, Long idClienteLayoutEdi, String tpArquivo ){
		List<RegistroLayoutEDI> listaRegistros = null;
		if (lewsr == null && tpArquivo == null){
			listaRegistros = registroLayoutEDIService.findFilhosPorTipo(idLayout, null,this.cabecalho);
		} else if (lewsr != null) {
			listaRegistros = registroLayoutEDIService.findFilhosPorTipo(idLayout, lewsr.getIdRegistroLayout(),this.cabecalho);
			if ((listaRegistros == null || listaRegistros.size() == 0)){
				List<RegistroLayoutEDI> cabecalhos = registroLayoutEDIService.findFilhosPorTipo(idLayout, lewsr.getIdRegistroLayout(),this.cabecalho);
				if(cabecalhos != null && cabecalhos.size() > 0){
					listaRegistros = registroLayoutEDIService.findFilhosPorTipo(idLayout, cabecalhos.get(0).getIdRegistroLayoutEdi(),this.cabecalho);
				}
			}
		} else if (lewsr == null && tpArquivo != null && tpArquivo.equals("XML")){ 
			listaRegistros = registroLayoutEDIService.findFilhosPorTipo(idLayout, null,this.cabecalho, tpArquivo);
		}
		
		if (listaRegistros == null || listaRegistros.size() == 0)
			return null;
		
		List<LayoutEdiWSRetorno> filhos = new ArrayList<LayoutEdiWSRetorno>();
		
		Long clienteLayoutEDI = null;
		//Percorre os registros, buscando seus filhos
		LayoutEdiWSRetorno ler;
		for (RegistroLayoutEDI registroLayoutEDI : listaRegistros) {
			ler = populaLayoutEdiWS(registroLayoutEDI);
			ler.setPrincipal(ler.getIdRegistroLayout().compareTo(this.getPrincipal()) == 0);
			if(registroLayoutEDI.getIdRegistroPai() != null){
				ler.setPai(this.populaLayoutEdiWS(this.registroLayoutEDIService.findById(registroLayoutEDI.getIdRegistroPai())));
			}else{
				ler.setPai(lewsr);
			}
			
			ler.setFilhos(buscaFilhosPorTipo(idLayout, ler, idClienteLayoutEdi, null));
			ler.setCampos(buscaCamposPorTipo(idLayout, ler, idClienteLayoutEdi));
			filhos.add(ler);
		}
		
		return filhos;
	}	
	
	private List<CampoLayoutEdiWSRetorno> buscaCamposPorTipo(Long idLayout, LayoutEdiWSRetorno ler, Long idClienteLayoutEdi){
		Map<String, Object> crit = new HashMap<String, Object>();
		crit.put("idLayoutEdi", idLayout);	
		crit.put("idRegistroLayoutEdi", ler.getIdRegistroLayout());			
		crit.put("idClienteLayoutEdi",idClienteLayoutEdi);
		
		List<ComposicaoLayoutEDI> listaComposicao = composicaoLayoutEdiService.findComposicaoClienteTipo(crit);
		List <ComposicaoLayoutEDI> listaComposicaoRetorno = new ArrayList <ComposicaoLayoutEDI>();
		List <CampoLayoutEdiWSRetorno> camposRetorno = new ArrayList <CampoLayoutEdiWSRetorno>();
		
		for (ComposicaoLayoutEDI composicaoLayoutEDI : listaComposicao) {		
			int contador = 0;
			boolean adicionar = true;
			for (ComposicaoLayoutEDI composicaoRetorno : listaComposicaoRetorno) {
				if(composicaoRetorno.getRegistroLayout().getIdRegistroLayoutEdi().longValue() ==  composicaoLayoutEDI.getRegistroLayout().getIdRegistroLayoutEdi()
						&& composicaoRetorno.getCampoLayout().getIdCampo().longValue() == composicaoLayoutEDI.getCampoLayout().getIdCampo().longValue()){
					adicionar = false;
					if(composicaoLayoutEDI.getClienteLayoutEDI() != null && composicaoLayoutEDI.getClienteLayoutEDI().getIdClienteLayoutEDI() == this.idClienteLayoutEdi){
						listaComposicaoRetorno.set(contador,composicaoLayoutEDI);					
						break;
					}
				}
				contador++;
			}
			if(adicionar){
				Date dtVigenciaIni = new Date(composicaoLayoutEDI.getDtVigenciaInicial().toDateTimeAtCurrentTime().getMillis());
				Date dtVigenciaFim = new Date(composicaoLayoutEDI.getDtVigenciaFinal().toDateTimeAtCurrentTime().getMillis());
				if(dtVigenciaIni.compareTo(new Date()) <= 0 && dtVigenciaFim.compareTo(new Date()) >= 0){
					listaComposicaoRetorno.add(composicaoLayoutEDI);
				}
			}
		}
		for (ComposicaoLayoutEDI composicaoLayoutEDIRetorno : listaComposicaoRetorno) {	
			camposRetorno.add(populaCampoLayoutEdiWS(composicaoLayoutEDIRetorno));
		}				
		return camposRetorno;
	}
	
	@MethodSecurity(processGroup = "layoutedi.geraArquivosTransformacaoWebService", processName = "buscaLayoutTipo", authenticationRequired=false)
	public GeraArquivosTransformacaoWSRetorno buscaLayoutTipo(Map params){
		Long idClienteLayoutEdi = (Long)params.get("idClienteLayoutEdi");
		String tipoLayout = (String)params.get("tpLayout");
		Long idLayoutEdi = registroLayoutEDIService.findLayoutPorTipo(idClienteLayoutEdi, tipoLayout);
		Long idRegistroPrincipalEdi = null;
		if(params.get("tpArquivo") == null){
			idRegistroPrincipalEdi = registroLayoutEDIService.findIdRegistroPrincipalPorTipo(idClienteLayoutEdi, tipoLayout);
		} else if(params.get("tpArquivo") != null && ((String)params.get("tpArquivo")).equals("XML")){
			idRegistroPrincipalEdi =  registroLayoutEDIService.findIdRegistroPrincipalXml(idClienteLayoutEdi, tipoLayout);
		}
	
		GeraArquivosTransformacaoWSRetorno retorno = null;
		if(idLayoutEdi != null ){
			this.setPrincipal(idRegistroPrincipalEdi);		
			retorno = new GeraArquivosTransformacaoWSRetorno();
			if(params.get("tpArquivo") == null){
				retorno.setComposicaoLayout(buscaFilhosPorTipo(idLayoutEdi, null, idClienteLayoutEdi, null ));
			} else if(params.get("tpArquivo") != null && ((String)params.get("tpArquivo")).equals("XML")){
				retorno.setComposicaoLayout(buscaFilhosPorTipo(idLayoutEdi, null, idClienteLayoutEdi, "XML" ));
		} 
			
		} 
		this.principal = null;
		this.cabecalho = null;
		this.idClienteLayoutEdi = null;
		return retorno;
	}
	
	

	@MethodSecurity(processGroup = "layoutedi.geraArquivosTransformacaoWebService", processName = "buscaLayout", authenticationRequired=false)
	public GeraArquivosTransformacaoWSRetorno buscaLayout(Map params){
		Long idLayout = (Long)params.get("idLayoutEDI");
		idClienteLayoutEdi = (Long)params.get("idClienteLayoutEDI");
		
		this.cabecalho = null;
		this.setPrincipal(registroLayoutEDIService.findIdRegistroPrincipal(idLayout));
		
		GeraArquivosTransformacaoWSRetorno retorno = new GeraArquivosTransformacaoWSRetorno();
		retorno.setComposicaoLayout(buscaFilhos(idLayout, null));
		
		return retorno; 
	}
	
	@MethodSecurity(processGroup = "layoutedi.geraArquivosTransformacaoWebService", processName = "buscaCabecalhos", authenticationRequired=false)
	public GeraArquivosTransformacaoWSRetorno buscaCabecalhos(Map params){
		Long idLayout = (Long)params.get("idLayoutEDI");
		idClienteLayoutEdi = (Long)params.get("idClienteLayoutEDI");
		String cabecalho = (String)params.get("cabecalho");
		
		this.cabecalho = cabecalho;
		
		this.setPrincipal(registroLayoutEDIService.findIdRegistroCabecalho(idLayout,cabecalho));
		
		if(this.principal != null){
			GeraArquivosTransformacaoWSRetorno retorno = new GeraArquivosTransformacaoWSRetorno();
			retorno.setComposicaoLayout(buscaFilhos(idLayout, null));
			
			return retorno; 
		}else{
			return null;
		}
	}
	
	/**
	 * Busca toda árvore sob um registro, recursivamente
	 * @param lewsr
	 * @return
	 */
	private List<LayoutEdiWSRetorno> buscaFilhos(Long idLayout, LayoutEdiWSRetorno lewsr){
		List<RegistroLayoutEDI> listaRegistros;
		if (lewsr == null)
			listaRegistros = registroLayoutEDIService.findFilhos(idLayout, null,this.cabecalho);
		else{
			listaRegistros = registroLayoutEDIService.findFilhos(idLayout, lewsr.getIdRegistroLayout(),this.cabecalho);
			if ((listaRegistros == null || listaRegistros.size() == 0) && lewsr.getNomeIdentificador().equals("REMETENTE")){
				List<RegistroLayoutEDI> cabecalhos = registroLayoutEDIService.findFilhos(idLayout, lewsr.getIdRegistroLayout(),"CABDOCUMEN");
				if(cabecalhos != null && cabecalhos.size() > 0){
					listaRegistros = registroLayoutEDIService.findFilhos(idLayout, cabecalhos.get(0).getIdRegistroLayoutEdi(),this.cabecalho);
				}
			}
		}
		
		if (listaRegistros == null || listaRegistros.size() == 0)
			return null;
		
		List<LayoutEdiWSRetorno> filhos = new ArrayList<LayoutEdiWSRetorno>();
		
		Long clienteLayoutEDI = null;
		//Percorre os registros, buscando seus filhos
		LayoutEdiWSRetorno ler;
		for (RegistroLayoutEDI registroLayoutEDI : listaRegistros) {
			
			
			ler = populaLayoutEdiWS(registroLayoutEDI);
			ler.setPrincipal(ler.getIdRegistroLayout().compareTo(this.getPrincipal()) == 0);
			if(registroLayoutEDI.getIdRegistroPai() != null && this.cabecalho != null){
				ler.setPai(this.populaLayoutEdiWS(this.registroLayoutEDIService.findById(registroLayoutEDI.getIdRegistroPai())));
			}else{
				ler.setPai(lewsr);
			}
			
			ler.setFilhos(buscaFilhos(idLayout, ler));
			ler.setCampos(buscaCampos(idLayout, ler));
			filhos.add(ler);
		}
		
		return filhos;
	}

	/**
	 * Busca as informações dos campos (composicao_layout_edi e campo_layout_edi) que compoe o registro.
	 * @param ler
	 * @return
	 */
	private List<CampoLayoutEdiWSRetorno> buscaCampos(Long idLayout, LayoutEdiWSRetorno ler){
		Map<String, Object> crit = new HashMap<String, Object>();
		crit.put("registroLayout.idRegistroLayoutEdi", ler.getIdRegistroLayout());
		crit.put("layout.idLayoutEdi", idLayout);
		
		List<ComposicaoLayoutEDI> listaComposicao = composicaoLayoutEdiService.find(crit);
		List <ComposicaoLayoutEDI> listaComposicaoRetorno = new ArrayList <ComposicaoLayoutEDI>();
		List <CampoLayoutEdiWSRetorno> camposRetorno = new ArrayList <CampoLayoutEdiWSRetorno>();
		
		for (ComposicaoLayoutEDI composicaoLayoutEDI : listaComposicao) {		
			int contador = 0;
			boolean adicionar = true;
			for (ComposicaoLayoutEDI composicaoRetorno : listaComposicaoRetorno) {
				if(composicaoRetorno.getRegistroLayout().getIdRegistroLayoutEdi().longValue() ==  composicaoLayoutEDI.getRegistroLayout().getIdRegistroLayoutEdi()
						&& composicaoRetorno.getCampoLayout().getIdCampo().longValue() == composicaoLayoutEDI.getCampoLayout().getIdCampo().longValue()){
					adicionar = false;
					if(composicaoLayoutEDI.getClienteLayoutEDI() != null 
							&& composicaoLayoutEDI.getClienteLayoutEDI().getIdClienteLayoutEDI().longValue() == this.idClienteLayoutEdi){
						listaComposicaoRetorno.set(contador,composicaoLayoutEDI);					
						break;
					}
				}
				contador++;
			}
			if(adicionar){
				if(composicaoLayoutEDI.getClienteLayoutEDI() == null || composicaoLayoutEDI.getClienteLayoutEDI().getIdClienteLayoutEDI().longValue() == this.idClienteLayoutEdi){
					Date dtVigenciaIni = new Date(composicaoLayoutEDI.getDtVigenciaInicial().toDateTimeAtCurrentTime().getMillis());
					Date dtVigenciaFim = new Date(composicaoLayoutEDI.getDtVigenciaFinal().toDateTimeAtCurrentTime().getMillis());
					if(dtVigenciaIni.compareTo(new Date()) <= 0 && dtVigenciaFim.compareTo(new Date()) >= 0){
						listaComposicaoRetorno.add(composicaoLayoutEDI);
					}
				}
			}
		}
		for (ComposicaoLayoutEDI composicaoLayoutEDIRetorno : listaComposicaoRetorno) {	
			camposRetorno.add(populaCampoLayoutEdiWS(composicaoLayoutEDIRetorno));
		}
		
		
		return camposRetorno;
	}
	
	/**
	 * Popula um objeto <code>LayoutEdiWSRetorno</code> com as informações de um <code>RegistroLayoutEDI</code>
	 * @param rle
	 * @return
	 */
	private LayoutEdiWSRetorno populaLayoutEdiWS(RegistroLayoutEDI rle){
		LayoutEdiWSRetorno lews = new LayoutEdiWSRetorno();
		lews.setIdRegistroLayout(rle.getIdRegistroLayoutEdi());
		lews.setIdentificador(rle.getIdentificador());
		lews.setNomeIdentificador(rle.getNomeIdentificador());
		lews.setOrdem(rle.getOrdem());
		lews.setDescricao(rle.getDescricao());
		lews.setTamanhoRegistro(rle.getTamanhoRegistro());
		lews.setIdRegistroLayoutPai(rle.getIdRegistroPai());
		lews.setOcorrencias(rle.getOcorrencias());
		lews.setPreenchimento(rle.getPreenchimento());
		return lews;
	}

	/**
	 * Popula um objeto <code>CampoLayoutEdiWSRetorno</code> com as informações de um <code>ComposicaoLayoutEDI</code> 
	 * @param cle
	 * @return
	 */
	private CampoLayoutEdiWSRetorno populaCampoLayoutEdiWS(ComposicaoLayoutEDI cle){
		CampoLayoutEdiWSRetorno clews = new CampoLayoutEdiWSRetorno();
		
		clews.setValorDefault(cle.getValorDefault());
		clews.setIdComposicaoLayout(cle.getIdComposicaoLayout());
		clews.setIdCampoLayout(cle.getCampoLayout().getIdCampo());
		
		if (cle.getFormato() == null)
			clews.setFormato("");
		else
			clews.setFormato(cle.getFormato().getValue());
		
		if (cle.getFormato() == null)
			clews.setComplementoFormato("");
		else
			clews.setComplementoFormato(cle.getCompFormato());
		
		clews.setTamanho(cle.getTamanho());
		clews.setQtdDecimais(cle.getQtDecimal());
		clews.setPosicao(cle.getPosicao());
		if(cle.getDeParaEDI() != null){
			clews.setTemDePara("S");
		}else{
			clews.setTemDePara("N");
		}
		      
		
		CampoLayoutEDI campo = campoLayoutEdiService.findById(cle.getCampoLayout().getIdCampo());
		
		clews.setNomeCampo(campo.getNomeCampo());
		clews.setCampoTabelaTemp(campo.getCampoTabela());
		
		if (campo.getDmTipoDePara() != null)
			clews.setTipoDePara(campo.getDmTipoDePara().getValue());
		else 
			clews.setTipoDePara("");
		
		if (campo.getNmComplemento() == null)
			clews.setNomeComplemento("");
		else
			clews.setNomeComplemento(campo.getNmComplemento());
		
		
		if (campo.getDmObrigatorio() != null)
			clews.setObrigatorio(campo.getDmObrigatorio().getValue());
		else
			clews.setObrigatorio("N");
		
		if(cle.getXpath()!= null && !cle.getXpath().equals("")){
			clews.setXpath(cle.getXpath());
		}
		
		return clews;
	}
}