package com.mercurio.lms.seguros.action;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.service.RecursoMensagemService;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Foto;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.param.ConsultarUsuarioLMSParam;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.FotoService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contasreceber.model.service.NotaDebitoNacionalService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ManifestoInternacionalService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalServicoService;
import com.mercurio.lms.indenizacoes.model.service.DoctoServicoIndenizacaoService;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Rodovia;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.RodoviaService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.seguros.model.CustoAdicionalSinistro;
import com.mercurio.lms.seguros.model.FotoProcessoSinistro;
import com.mercurio.lms.seguros.model.ProcessoSinistro;
import com.mercurio.lms.seguros.model.SinistroDoctoServico;
import com.mercurio.lms.seguros.model.SituacaoReembolso;
import com.mercurio.lms.seguros.model.TipoSeguro;
import com.mercurio.lms.seguros.model.TipoSinistro;
import com.mercurio.lms.seguros.model.service.ApoliceSeguroService;
import com.mercurio.lms.seguros.model.service.CustoAdicionalSinistroService;
import com.mercurio.lms.seguros.model.service.FotoProcessoSinistroService;
import com.mercurio.lms.seguros.model.service.ProcessoSinistroService;
import com.mercurio.lms.seguros.model.service.ReguladoraSeguroService;
import com.mercurio.lms.seguros.model.service.SeguroTipoSinistroService;
import com.mercurio.lms.seguros.model.service.SinistroDoctoServicoService;
import com.mercurio.lms.seguros.model.service.SituacaoReembolsoService;
import com.mercurio.lms.seguros.model.service.TipoSeguroService;
import com.mercurio.lms.seguros.model.service.TipoSinistroService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.DiaUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.manterProcessosSinistroAction"
 */

public class ManterProcessosSinistroAction extends MasterDetailAction {

	private static final String DOCUMENTOS_CONFIG_ALIAS = "SISNISTRO_DOCTO_SERVICO";

	private Logger log = LogManager.getLogger(this.getClass());
	private UnidadeFederativaService unidadeFederativaService;
	private ControleCargaService controleCargaService;
	private MeioTransporteService meioTransporteService;
	private ManifestoService manifestoService;
	private ManifestoEntregaService manifestoEntregaService;
	private ManifestoViagemNacionalService manifestoViagemNacionalService;
	private ManifestoInternacionalService manifestoInternacionalService;
	private RodoviaService rodoviaService;
	private AeroportoService aeroportoService;
	private MunicipioService municipioService;
	private TipoSinistroService tipoSinistroService;
	private DomainValueService domainValueService;
	private FilialService filialService;
	private TipoSeguroService tipoSeguroService;
	private ClienteService clienteService;
	private ReguladoraSeguroService reguladoraSeguroService;
	private ConversaoMoedaService conversaoMoedaService;
	private DoctoServicoService doctoServicoService;
	private CustoAdicionalSinistroService custoAdicionalSinistroService;
	private FotoProcessoSinistroService fotoProcessoSinistroService;
	private FotoService fotoService;
	private SinistroDoctoServicoService sinistroDoctoServicoService;
	private ConfiguracoesFacade configuracoesFacade;
	private MoedaService moedaService;
	private RecursoMensagemService recursoMensagemService;
	private DoctoServicoIndenizacaoService doctoServicoIndenizacaoService;
	private DiaUtils diaUtils;
	private SituacaoReembolsoService situacaoReembolsoService;
	private MotoristaService motoristaService;
	private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
	private SeguroTipoSinistroService seguroTipoSinistroService;
	private ConhecimentoService conhecimentoService;
	private CtoInternacionalService ctoInternacionalService; 
	private MdaService mdaService;
	private ReciboReembolsoService reciboReembolsoService;
	private UsuarioService usuarioService;
	private ApoliceSeguroService apoliceSeguroService;
	private UsuarioLMSService usuarioLMSService;
	private NotaDebitoNacionalService notaDebitoNacionalService;
	private NotaFiscalServicoService notaFiscalServicoService;
		
	public ApoliceSeguroService getApoliceSeguroService() {
		return apoliceSeguroService;
	}
	public void setApoliceSeguroService(ApoliceSeguroService apoliceSeguroService) {
		this.apoliceSeguroService = apoliceSeguroService;
	}
	public SituacaoReembolsoService getSituacaoReembolsoService() {
		return situacaoReembolsoService;
	}
	public void setSituacaoReembolsoService(
			SituacaoReembolsoService situacaoReembolsoService) {
		this.situacaoReembolsoService = situacaoReembolsoService;
	}
	public AeroportoService getAeroportoService() {
		return aeroportoService;
	}
	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}
	public ClienteService getClienteService() {
		return clienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	public CustoAdicionalSinistroService getCustoAdicionalSinistroService() {
		return custoAdicionalSinistroService;
	}
	public void setCustoAdicionalSinistroService(CustoAdicionalSinistroService custoAdicionalSinistroService) {
		this.custoAdicionalSinistroService = custoAdicionalSinistroService;
	}
	public DiaUtils getDiaUtils() {
		return diaUtils;
	}
	public void setDiaUtils(DiaUtils diaUtils) {
		this.diaUtils = diaUtils;
	}
	public DoctoServicoIndenizacaoService getDoctoServicoIndenizacaoService() {
		return doctoServicoIndenizacaoService;
	}
	public void setDoctoServicoIndenizacaoService(DoctoServicoIndenizacaoService doctoServicoIndenizacaoService) {
		this.doctoServicoIndenizacaoService = doctoServicoIndenizacaoService;
	}
	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public DomainValueService getDomainValueService() {
		return domainValueService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public FotoProcessoSinistroService getFotoProcessoSinistroService() {
		return fotoProcessoSinistroService;
	}
	public void setFotoProcessoSinistroService(FotoProcessoSinistroService fotoProcessoSinistroService) {
		this.fotoProcessoSinistroService = fotoProcessoSinistroService;
	}
	public FotoService getFotoService() {
		return fotoService;
	}
	public void setFotoService(FotoService fotoService) {
		this.fotoService = fotoService;
	}
	public ManifestoEntregaService getManifestoEntregaService() {
		return manifestoEntregaService;
	}
	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}
	public ManifestoInternacionalService getManifestoInternacionalService() {
		return manifestoInternacionalService;
	}
	public void setManifestoInternacionalService(ManifestoInternacionalService manifestoInternacionalService) {
		this.manifestoInternacionalService = manifestoInternacionalService;
	}
	public ManifestoService getManifestoService() {
		return manifestoService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	public ManifestoViagemNacionalService getManifestoViagemNacionalService() {
		return manifestoViagemNacionalService;
	}
	public void setManifestoViagemNacionalService(ManifestoViagemNacionalService manifestoViagemNacionalService) {
		this.manifestoViagemNacionalService = manifestoViagemNacionalService;
	}
	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public MoedaService getMoedaService() {
		return moedaService;
	}
	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	public MunicipioService getMunicipioService() {
		return municipioService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public RecursoMensagemService getRecursoMensagemService() {
		return recursoMensagemService;
	}
	public void setRecursoMensagemService(RecursoMensagemService recursoMensagemService) {
		this.recursoMensagemService = recursoMensagemService;
	}
	public ReguladoraSeguroService getReguladoraSeguroService() {
		return reguladoraSeguroService;
	}
	public void setReguladoraSeguroService(ReguladoraSeguroService reguladoraSeguroService) {
		this.reguladoraSeguroService = reguladoraSeguroService;
	}
	public RodoviaService getRodoviaService() {
		return rodoviaService;
	}
	public void setRodoviaService(RodoviaService rodoviaService) {
		this.rodoviaService = rodoviaService;
	}
	public SinistroDoctoServicoService getSinistroDoctoServicoService() {
		return sinistroDoctoServicoService;
	}
	public void setSinistroDoctoServicoService(SinistroDoctoServicoService sinistroDoctoServicoService) {
		this.sinistroDoctoServicoService = sinistroDoctoServicoService;
	}
	public TipoSeguroService getTipoSeguroService() {
		return tipoSeguroService;
	}
	public void setTipoSeguroService(TipoSeguroService tipoSeguroService) {
		this.tipoSeguroService = tipoSeguroService;
	}
	public TipoSinistroService getTipoSinistroService() {
		return tipoSinistroService;
	}
	public void setTipoSinistroService(TipoSinistroService tipoSinistroService) {
		this.tipoSinistroService = tipoSinistroService;
	}
	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	public SeguroTipoSinistroService getSeguroTipoSinistroService() {
		return seguroTipoSinistroService;
	}
	public void setSeguroTipoSinistroService(
			SeguroTipoSinistroService seguroTipoSinistroService) {
		this.seguroTipoSinistroService = seguroTipoSinistroService;
	}
	public void setProcessoSinistroService(ProcessoSinistroService processoSinistroService) {
		super.setMasterService(processoSinistroService);
	}
	private ProcessoSinistroService getProcessoSinistroService() {
		return (ProcessoSinistroService) super.getMasterService();
	}
	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public CtoInternacionalService getCtoInternacionalService() {
		return ctoInternacionalService;
	}
	public void setCtoInternacionalService(
			CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}
	public MdaService getMdaService() {
		return mdaService;
	}
	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}
	public ReciboReembolsoService getReciboReembolsoService() {
		return reciboReembolsoService;
	}
	public void setReciboReembolsoService(
			ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}
	
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public MotoristaService getMotoristaService() {
		return motoristaService;
	}
	public void setMotoristaService(MotoristaService motoristaService) {
		this.motoristaService = motoristaService;
	}
		
	public MeioTransporteRodoviarioService getMeioTransporteRodoviarioService() {
		return meioTransporteRodoviarioService;
	}
	public void setMeioTransporteRodoviarioService(
			MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
		this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
	}
	
	public UsuarioLMSService getUsuarioLMSService() {
		return usuarioLMSService;
	}
	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}
	
	public NotaDebitoNacionalService getNotaDebitoNacionalService() {
		return notaDebitoNacionalService;
	}
	public void setNotaDebitoNacionalService(
			NotaDebitoNacionalService notaDebitoNacionalService) {
		this.notaDebitoNacionalService = notaDebitoNacionalService;
	}
	public NotaFiscalServicoService getNotaFiscalServicoService() {
		return notaFiscalServicoService;
	}
	public void setNotaFiscalServicoService(
			NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}

	/*****************************************************************************************************
	 * Main Methods
	 *****************************************************************************************************/	
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) { 
		
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(ProcessoSinistro.class, true);
    	
		Comparator comparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				return 0;
			}    		
    	};
    	
    	ItemListConfig documentosConfig = new ItemListConfig() {
 
			public List initialize(Long masterId, Map parameters) {
				return getSinistroDoctoServicoService().findByIdProcessoSinistro(masterId);
			}
			
			public Integer getRowCount(Long masterId, Map parameters) {
				if (masterId==null) {
					return Integer.valueOf(0);
				}
				return getSinistroDoctoServicoService().getRowCountByIdProcessoSinistro(masterId);
			}

			public Map configItemDomainProperties() {
				return null;
			}
			
			public Object populateNewItemInstance(Map mapParameter, Object bean) {
				
				TypedFlatMap tfm = new TypedFlatMap(mapParameter);
				SinistroDoctoServico documento = (SinistroDoctoServico) bean;	
				ProcessoSinistro processoSinistro = (ProcessoSinistro)getMasterFromSession(getMasterId(mapParameter), true).getMaster();

				documento.setProcessoSinistro(processoSinistro);
				documento.setDoctoServico(doctoServicoService.findById(tfm.getLong("doctoServico.idDoctoServico")));
				documento.setTpPrejuizo(tfm.getDomainValue("tpPrejuizo"));
				documento.setVlPrejuizo(tfm.getBigDecimal("vlPrejuizo"));
				documento.setBlPrejuizoProprio(tfm.getDomainValue("blPrejuizoProprio"));
				
				return documento;
			}
    	};
    	
		config.addItemConfig(DOCUMENTOS_CONFIG_ALIAS, SinistroDoctoServico.class, documentosConfig, comparator);    	
		return config;
	}

	public Serializable storeProcessoSinistro(TypedFlatMap tfm) {
		
		Long idProcessoSinistro = tfm.getLong("idProcessoSinistro");
		
		if (idProcessoSinistro!=null)
			getProcessoSinistroService().validateIsProcessoSinistroFechado(idProcessoSinistro);
		
		MasterEntry master = getMasterFromSession(idProcessoSinistro, true);
		ProcessoSinistro processoSinistro = (ProcessoSinistro) master.getMaster();
		
		ItemList itens = getItemsFromSession(master, DOCUMENTOS_CONFIG_ALIAS);

    	processoSinistro.setIdProcessoSinistro(idProcessoSinistro);
    	processoSinistro.setNrProcessoSinistro(tfm.getString("nrProcessoSinistro"));
    	TipoSeguro tipoSeguro = tipoSeguroService.findById(tfm.getLong("tipoSeguro.idTipoSeguro"));
    	processoSinistro.setTipoSeguro(tipoSeguro);
    	
    	//LMS-6178
    	if(tipoSeguro != null && "N".equalsIgnoreCase(processoSinistro.getTipoSeguro().getBlEnvolveCarga()) && !itens.isEmpty()) {
        	throw new BusinessException("LMS-22034");
    	}
    	
    	Municipio municipio = new Municipio();
    	municipio.setIdMunicipio(tfm.getLong("municipio.idMunicipio"));
    	processoSinistro.setMunicipio(municipio);
    	
    	Moeda moeda = new Moeda();
    	moeda.setIdMoeda(tfm.getLong("moeda.idMoeda"));
    	processoSinistro.setMoeda(moeda);
    	TipoSinistro tipoSinistro = new TipoSinistro();
    	tipoSinistro.setIdTipoSinistro(tfm.getLong("tipoSinistro.idTipoSinistro"));
    	processoSinistro.setTipoSinistro(tipoSinistro);
    	
    	processoSinistro.setTpLocalSinistro(tfm.getDomainValue("localSinistro"));
    	processoSinistro.setDsLocalSinistro(tfm.getString("dsLocalSinistro"));
    	
    	if (tfm.getLong("aeroporto.idAeroporto") != null) {
	    	Aeroporto aeroporto = new Aeroporto();
	    	aeroporto.setIdAeroporto(tfm.getLong("aeroporto.idAeroporto"));
	    	processoSinistro.setAeroporto(aeroporto);
    	}
    	    	
    	if (tfm.getLong("filialSinistro.idFilial") != null) {
	    	Filial filial = new Filial();
	    	filial.setIdFilial(tfm.getLong("filialSinistro.idFilial"));
	    	processoSinistro.setFilial(filial);
    	}
    	
    	if (tfm.getLong("rodovia.idRodovia") != null) {
	    	Rodovia rodovia = new Rodovia();
	    	rodovia.setIdRodovia(tfm.getLong("rodovia.idRodovia"));
	    	processoSinistro.setRodovia(rodovia);
    	}
    	
    	//LMS-6178
    	processoSinistro.setDsComunicadoCorretora(tfm.getString("dsComunicadoCorretora"));
    	processoSinistro.setVlFranquia(tfm.getBigDecimal("vlFranquia"));    	
    	
    	if (tfm.getLong("meioTransporteRodoviario.idMeioTransporte") != null) {
	    	MeioTransporte meioTransporte = new MeioTransporte();
	    	meioTransporte.setIdMeioTransporte(tfm.getLong("meioTransporteRodoviario.idMeioTransporte"));
	    	processoSinistro.setMeioTransporte(meioTransporte);
    	}
    	
    	if (tfm.getLong("motorista.idMotorista") != null) {
	    	Motorista motorista = new Motorista();
	    	motorista.setIdMotorista(tfm.getLong("motorista.idMotorista"));
	    	processoSinistro.setMotorista(motorista);
    	}
    	
    	if (tfm.getLong("situacaoReembolso.idSituacaoReembolso") != null) {
    		SituacaoReembolso stReembolso = new SituacaoReembolso();
        	stReembolso.setIdSituacaoReembolso(tfm.getLong("situacaoReembolso.idSituacaoReembolso"));
        	processoSinistro.setSituacaoReembolso(stReembolso);
    	}    	
    	
    	processoSinistro.setDhSinistro(tfm.getDateTime("dhSinistro"));
    	processoSinistro.setNrKmSinistro(tfm.getInteger("nrKmSinistro"));
    	processoSinistro.setDsSinistro(tfm.getString("dsSinistro"));
    	processoSinistro.setNrBoletimOcorrencia(tfm.getLong("nrBoletimOcorrencia"));
    	    	
    	processoSinistro.setObSinistro(tfm.getString("obSinistro"));
    	    	
    	if(processoSinistro.getIdProcessoSinistro() == null) {
    		processoSinistro.setUsuarioAbertura(SessionUtils.getUsuarioLogado());    
    		processoSinistro.setDhAbertura(JTDateTimeUtils.getDataHoraAtual());
    	}
    	
		Serializable id = this.getProcessoSinistroService().storeCustom(processoSinistro, itens);
		updateMasterInSession(master);

		TypedFlatMap result = new TypedFlatMap();
		result.put("idProcessoSinistro", id);
		result.put("sgSimboloVlMercadoria", processoSinistro.getMoeda().getSiglaSimbolo());
		result.put("sgSimboloRecuperado", processoSinistro.getMoeda().getSiglaSimbolo());
		result.put("sgSimboloPrejuizo", processoSinistro.getMoeda().getSiglaSimbolo());
		result.put("sgSimboloPrejuizoProprio", processoSinistro.getMoeda().getSiglaSimbolo());
		result.put("sgSimboloIndenizado", processoSinistro.getMoeda().getSiglaSimbolo());
		result.put("sgSimboloReembolso", processoSinistro.getMoeda().getSiglaSimbolo());
		result.put("sgSimboloDifeIndenizadoReembolsado", processoSinistro.getMoeda().getSiglaSimbolo());
		result.put("sgSimboloDiferencaPagamentoCliente", processoSinistro.getMoeda().getSiglaSimbolo());		
		result.put("sgSimboloVlReceber", processoSinistro.getMoeda().getSiglaSimbolo());
		return result;
	}

    public ResultSetPage findPaginatedCustom(TypedFlatMap tfm) {
    	return getProcessoSinistroService().findPaginatedCustom(tfm);
    }
    
    public Integer getRowCountCustom(TypedFlatMap tfm) {
    	return getProcessoSinistroService().getRowCountCustom(tfm);
    }
    
    public TypedFlatMap findById(Long idProcessoSinistro) {
		
    	TypedFlatMap tfm = this.getProcessoSinistroService().findByManterProcessoSinistro(idProcessoSinistro);
    	
    	ProcessoSinistro processoSinistro = getProcessoSinistroService().findById(idProcessoSinistro);
    	
    	this.setaValoresMonetariosExibidosNaTela(tfm, processoSinistro);
    	
		putMasterInSession(processoSinistro); 
		
		return tfm;
    }
    
    public TypedFlatMap findAtualizaValoresDetalhamento(TypedFlatMap tfm) {
    	ProcessoSinistro processoSinistro = getProcessoSinistroService().findById(tfm.getLong("idProcessoSinistro"));
    	TypedFlatMap result = new TypedFlatMap();
		this.setaValoresMonetariosExibidosNaTela(result, processoSinistro);
		
		if (processoSinistro.getDhFechamento()!=null) {
			Integer tempoPagamento = Integer.valueOf(JTDateTimeUtils.getIntervalInDays(processoSinistro.getDhSinistro().toYearMonthDay(), processoSinistro.getDhFechamento().toYearMonthDay()));
			result.put("tempoPagamento", tempoPagamento);
			result.put("situacao", recursoMensagemService.findByChave("fechado", null));
			result.put("situacaoHidden", "F");
			result.put("dhFechamento", JTFormatUtils.format(processoSinistro.getDhFechamento().toDateTime(), "dd/MM/yyyy HH:mm"));
		} else {
			result.put("situacao", recursoMensagemService.findByChave("aberto", null));
			result.put("situacaoHidden", "A");
			result.put("dhFechamento", null);
		}
		
		return result;
    }
    
  	//LMS-6178 - Método para preencher os valores financeiros do sinistro que aparecem na tela
  	private void setaValoresMonetariosExibidosNaTela(TypedFlatMap tfm, ProcessoSinistro processoSinistro) {
  		// Valor Mercadoria
  		BigDecimal valorMercadoria = getProcessoSinistroService().findSomaValoresMercadoria(processoSinistro.getIdProcessoSinistro());
  		
  		//Valor Prejuizo
  		BigDecimal valorPrejuizo = getProcessoSinistroService().findSomaValoresPrejuizo(processoSinistro.getIdProcessoSinistro());
  		
  		//Valor do Prejuízo Próprio
  		BigDecimal valorPrejuizoProprio = getProcessoSinistroService().findSomaValoresPrejuizoProprio(processoSinistro.getIdProcessoSinistro());
  		
  		//Valor Indenizado
  		BigDecimal valorIndenizado = getProcessoSinistroService().findSomaValoresIndenizado(processoSinistro.getIdProcessoSinistro());
  		
  		//Valor Reembolso
  		BigDecimal valorReembolso = getProcessoSinistroService().findSomaValoresReembolso(processoSinistro.getIdProcessoSinistro());
  		  		
  		tfm.put("sgSimboloVlMercadoria", processoSinistro.getMoeda().getSiglaSimbolo());
  		tfm.put("vlMercadoria", valorMercadoria.toString());
  		
  		tfm.put("sgSimboloRecuperado", processoSinistro.getMoeda().getSiglaSimbolo());
  		tfm.put("vlRecuperado", valorMercadoria.subtract(valorPrejuizo).toString());
  		
  		tfm.put("sgSimboloPrejuizo", processoSinistro.getMoeda().getSiglaSimbolo());
  		tfm.put("vlPrejuizo", valorPrejuizo.toString());
  		
  		tfm.put("sgSimboloPrejuizoProprio", processoSinistro.getMoeda().getSiglaSimbolo());
  		tfm.put("vlPrejuizoProprio", valorPrejuizoProprio.toString());
  		
  		tfm.put("sgSimboloIndenizado", processoSinistro.getMoeda().getSiglaSimbolo());
  		tfm.put("vlIndenizado", valorIndenizado.toString());
  		
  		tfm.put("sgSimboloReembolso", processoSinistro.getMoeda().getSiglaSimbolo());
  		tfm.put("vlReembolso", valorReembolso.toString());
  		
  		tfm.put("sgSimboloDifeIndenizadoReembolsado", processoSinistro.getMoeda().getSiglaSimbolo());
  		tfm.put("vlDifeIndenizadoReembolsado", valorPrejuizo.subtract(valorIndenizado).subtract(valorReembolso).toString());
  		
  		tfm.put("sgSimboloDiferencaPagamentoCliente", processoSinistro.getMoeda().getSiglaSimbolo());
  		tfm.put("diferencaPagamentoCliente", valorPrejuizo.subtract(valorIndenizado).toString());
  		
  		tfm.put("sgSimboloVlReceber", processoSinistro.getMoeda().getSiglaSimbolo());
  		
  		BigDecimal valorReceber = valorPrejuizo.subtract(valorPrejuizoProprio).subtract(valorReembolso).subtract(processoSinistro.getVlFranquia());
  		
  		if(BigDecimal.ZERO.compareTo(valorReceber) > 0) {
  			valorReceber = BigDecimal.ZERO;
  			tfm.put("vlReceber", valorReceber.toString());
  		} else {
  			tfm.put("vlReceber", valorReceber);
  		}
  	}

    /*****************************************************************************************************
	 * Documentos Methods
	 *****************************************************************************************************/
    
    /**
     * Insere um documento da session
     * @param parameters
     * @return
     */
    public Serializable storeDocumento(TypedFlatMap parameters) {
    	return super.saveItemInstance(parameters, DOCUMENTOS_CONFIG_ALIAS);
    }
    
    /**
     * Busca os registros da session para a grid
     * e converte os registros não persisitidos (SinistroDoctoServico) em
     * TypedFlatMap para apresentação na grid
     * @param criteria
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResultSetPage findPaginatedDocumentos(TypedFlatMap criteria){
    	
    	ResultSetPage rsp = findPaginatedItemList(criteria, DOCUMENTOS_CONFIG_ALIAS);
    	
    	// Carrega os dados da grid
    	rsp.setList(this.getSinistroDoctoServicoService().findPopulateDataGridProcessoSinistro(rsp.getList()));
    	
    	return rsp;
    }
    
    /**
     * Persiste as informações (tpPrejuizo, vlPrejuizo) alteradas pela grid
     * @param tfm
     * @return
     */
    public Serializable storeGridDocumentos(TypedFlatMap tfm) {

    	final String tpPrejuizoName = "Tipo de prejuízo";
    	final String vlPrejuizoName = "Valor do prejuízo";
    	final String blPrejuizoProprioName = "Prejuízo próprio";
    	final String tpPrejuizoSemPrejuizo = "S";
    	final String tpPrejuizoParcial = "P";
    	
    	List list = tfm.getList("sinistroDoctoServico");
    	
    	if(list != null && !list.isEmpty()) {
	    	for (Iterator it = list.iterator(); it.hasNext();) {
	    		TypedFlatMap item = (TypedFlatMap) it.next();
	    		Long idSinistroDoctoServico = item.getLong("id");
	    		if(idSinistroDoctoServico != null && idSinistroDoctoServico > 0) {
	    			
	    			SinistroDoctoServico sinistroDoctoServico = this.sinistroDoctoServicoService.findById(idSinistroDoctoServico);
	    			
	    			if(sinistroDoctoServico != null) {
	    			
	    				BigDecimal vlPrejuizo = item.getBigDecimal("vlPrejuizo");
			        	DomainValue tpPrejuizo = item.getDomainValue("tpPrejuizo");
			        	BigDecimal vlMercadoria = item.getBigDecimal("vlMercadoriaHidden");
	    				DomainValue blPrejuizoProprio = item.getDomainValue("blPrejuizoProprio");
	    				
	    				if(!tpPrejuizoSemPrejuizo.equals(tpPrejuizo.getValue())) {
		    				getSinistroDoctoServicoService().validateDoctoServicoOutroProcesso(
		    						sinistroDoctoServico.getProcessoSinistro().getIdProcessoSinistro(), sinistroDoctoServico.getDoctoServico().getIdDoctoServico());
	    				}
			        	
			        	if(tpPrejuizo.getValue().isEmpty()) {
			        		throw new BusinessException("LMS-00001", new Object[] { tpPrejuizoName });
			        	}
			        	
			        	if(blPrejuizoProprio.getValue().isEmpty()) {
			        		throw new BusinessException("LMS-00001", new Object[] { blPrejuizoProprioName });
			        	}
			        	
			        	if(vlPrejuizo == null) {
			        		throw new BusinessException("LMS-00001", new Object[] { vlPrejuizoName });
			        	} else { 
			        		if(tpPrejuizoParcial.equals(tpPrejuizo.getValue()) &&
			        				(vlPrejuizo.compareTo(vlMercadoria == null ? BigDecimal.ZERO : vlMercadoria) > 0
		        					|| vlPrejuizo.compareTo(BigDecimal.ZERO) <= 0)) {
		        				throw new BusinessException("LMS-22038");
			        		}
			        	}
	    				
		    			MasterEntry master = getMasterFromSession(sinistroDoctoServico.getProcessoSinistro().getIdProcessoSinistro(), true);
		    			ItemList itens = getItemsFromSession(master, DOCUMENTOS_CONFIG_ALIAS);
		    			
		    			SinistroDoctoServico sinistroDoctoServicoSession = (SinistroDoctoServico)itens.findById(sinistroDoctoServico.getIdSinistroDoctoServico());
		    			
		    			sinistroDoctoServicoSession.setVlPrejuizo(vlPrejuizo);
			        	sinistroDoctoServicoSession.setTpPrejuizo(tpPrejuizo);
			        	sinistroDoctoServicoSession.setBlPrejuizoProprio(blPrejuizoProprio);
			        	
			        	updateMasterInSession(master);
			        	
			        	sinistroDoctoServico.setVlPrejuizo(vlPrejuizo);
			        	sinistroDoctoServico.setTpPrejuizo(tpPrejuizo);
			        	sinistroDoctoServico.setBlPrejuizoProprio(blPrejuizoProprio);
			        	
			        	this.sinistroDoctoServicoService.store(sinistroDoctoServico);
	    			}
	    		}
	    	}
    	}
    	return new HashMap();
    }
    
    /**
     * Insere os documentos vindos da popup "Selecionar vários documentos"
     * @param parameters
     * @return
     */
    public Serializable storeDocumentosPopup(TypedFlatMap parameters) {
    	
    	Long idProcessoSinistro = parameters.getLong("idProcessoSinistro");
    	List<String> idsDoctoServico = (List<String>)parameters.getList("idsDoctoServico");
    	
    	for (String idDoctoServico : idsDoctoServico) {
    		
    		parameters.put("doctoServico.idDoctoServico", Long.valueOf(idDoctoServico));
    		parameters.put("tpPrejuizo", new DomainValue("S"));
    		parameters.put("vlPrejuizo", BigDecimal.ZERO);
    		parameters.put("blPrejuizoProprio", new DomainValue("N"));
    		
    		this.saveItemInstance(parameters, DOCUMENTOS_CONFIG_ALIAS);
    	}
    	
    	return new HashMap();
    }
    
    /**
	 * Realiza as validações no documento de serviço
	 * @param map
	 * @return
	 */
	public TypedFlatMap validateDocumentoServico(TypedFlatMap map) {
    	Long idProcessoSinistro = map.getLong("idProcessoSinistro");
    	List<String> idsDoctoServico = (List<String>)map.getList("idsDoctoServico");
    	BigDecimal valorPrejuizo = map.getBigDecimal("vlPrejuizo");
    	String tpPrejuizo = map.getString("tpPrejuizo");
    	Boolean isPopup = map.getBoolean("isPopup");
    	
    	List<String> messages = getSinistroDoctoServicoService().validateDocumentoServico(idProcessoSinistro, valorPrejuizo, tpPrejuizo, idsDoctoServico);
    	
    	TypedFlatMap mapRetorno = new TypedFlatMap();
    	mapRetorno.put("messages", messages);
    	mapRetorno.put("idsDoctoServico", idsDoctoServico);
    	mapRetorno.put("isPopup", isPopup);
    	
    	return mapRetorno;
    }
	
	/**
	 * Calcula a quantidade de registros da grid
	 * @param tfm
	 * @return
	 */
	public Integer getRowCountDocumentos(TypedFlatMap tfm){
    	MasterEntry entry = getMasterFromSession(tfm.getLong("idProcessoSinistro"), true);

    	if (entry.getMasterId() == null) {
			return Integer.valueOf(0);
		}

    	return getRowCountItemList(tfm, DOCUMENTOS_CONFIG_ALIAS);
    }
	
	/**
	 * Remove um documento da session
	 * @param ids
	 */
	@SuppressWarnings("rawtypes")
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIdsDocumento(List ids) {
    	removeItemByIds(ids, DOCUMENTOS_CONFIG_ALIAS);
    }
    
    /*****************************************************************************************************
	 * Custos Adicionais Methods
	 *****************************************************************************************************/
    public Serializable storeCustosAdicionais(TypedFlatMap tfm) {

		MasterEntry master = getMasterFromSession(tfm.getLong("idProcessoSinistro"), true);
		ProcessoSinistro processoSinistro = (ProcessoSinistro) master.getMaster();
		getProcessoSinistroService().validateIsProcessoSinistroFechado(processoSinistro);
    	
    	CustoAdicionalSinistro custoAdicionalSinistro = new CustoAdicionalSinistro();
    	custoAdicionalSinistro.setIdCustoAdicionalSinistro(tfm.getLong("idCustoAdicionalSinistro"));
    	custoAdicionalSinistro.setDsCustoAdicional(tfm.getString("dsCustoAdicional"));
    	custoAdicionalSinistro.setDtCustoAdicional(tfm.getYearMonthDay("dtCustoAdicional"));
    	custoAdicionalSinistro.setDtReembolsado(tfm.getYearMonthDay("dtReembolsado"));
    	
    	Moeda moeda = new Moeda();
    	moeda.setIdMoeda(tfm.getLong("moeda.idMoeda"));
    	custoAdicionalSinistro.setMoeda(moeda);
    	
    	custoAdicionalSinistro.setProcessoSinistro(processoSinistro);
    	custoAdicionalSinistro.setVlCustoAdicional(tfm.getBigDecimal("vlCustoAdicional"));
    	custoAdicionalSinistro.setVlReembolsado(tfm.getBigDecimal("vlReembolsado"));
    	
    	Long idCustoAdicionalSinistro = (Long) this.custoAdicionalSinistroService.store(custoAdicionalSinistro);
    	getProcessoSinistroService().calculaVlDifeIndenizadoReembolsado(processoSinistro);
    	getProcessoSinistroService().store(processoSinistro);
    	updateMasterInSession(master);
    	
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("idCustoAdicionalSinistro", idCustoAdicionalSinistro);
    	result.put("siglaSimboloVlCusto", moeda.getSiglaSimbolo());
    	result.put("siglaSimboloVlReembolsado", moeda.getSiglaSimbolo());
    	return result;
    }
    
    public ResultSetPage findPaginatedCustoAdicional(TypedFlatMap tfm) {
    	return this.custoAdicionalSinistroService.findPaginatedCustom(tfm);
    }
    
    public Integer getRowCountCustoAdicional(TypedFlatMap tfm) {
    	return this.custoAdicionalSinistroService.getRowCountCustom(tfm);
    }
    
    public TypedFlatMap findCustoAdicionalById(Long idCustoAdicionalSinistro) {
    	CustoAdicionalSinistro custoAdicionalSinistro = this.custoAdicionalSinistroService.findById(idCustoAdicionalSinistro);
    	TypedFlatMap tfm = new TypedFlatMap();
    	tfm.put("idCustoAdicionalSinistro", custoAdicionalSinistro.getIdCustoAdicionalSinistro());
    	tfm.put("dsCustoAdicional", custoAdicionalSinistro.getDsCustoAdicional());
    	tfm.put("dtCustoAdicional", custoAdicionalSinistro.getDtCustoAdicional());
    	tfm.put("siglaSimboloVlCusto", custoAdicionalSinistro.getMoeda().getSiglaSimbolo());
    	tfm.put("vlCustoAdicional", custoAdicionalSinistro.getVlCustoAdicional());
    	tfm.put("moeda.idMoeda", custoAdicionalSinistro.getMoeda().getIdMoeda());
    	tfm.put("vlReembolsado", custoAdicionalSinistro.getVlReembolsado());
    	tfm.put("dtReembolsado", custoAdicionalSinistro.getDtReembolsado());
    	return tfm;
    }
    
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeCustosAdicionaisByIds(List ids) {
    	// alterando o processo de sinistro dos custos adicionais
    	if (ids!=null &&  !ids.isEmpty()) {
    		CustoAdicionalSinistro custoAdicionalSinistro = custoAdicionalSinistroService.findById((Long)ids.get(0));
    		MasterEntry master = getMasterFromSession(custoAdicionalSinistro.getProcessoSinistro().getIdProcessoSinistro(), true);
    		ProcessoSinistro processoSinistro = (ProcessoSinistro) master.getMaster();
    		getProcessoSinistroService().validateIsProcessoSinistroFechado(processoSinistro);
    		// remove o item
    		this.custoAdicionalSinistroService.removeByIds(ids);
    	    getProcessoSinistroService().calculaVlDifeIndenizadoReembolsado(processoSinistro);
	    	getProcessoSinistroService().store(processoSinistro);
	    	updateMasterInSession(master);
    	}    	
    }
    
    /*****************************************************************************************************
	 * Fotos Methods
	 *****************************************************************************************************/
    public Serializable storeFotos(TypedFlatMap tfm) {

    	ProcessoSinistro processoSinistro = getProcessoSinistroService().findById(tfm.getLong("idProcessoSinistro"));
    	getProcessoSinistroService().validateIsProcessoSinistroFechado(processoSinistro);
    	
    	FotoProcessoSinistro fotoProcessoSinistro = new FotoProcessoSinistro();
    	fotoProcessoSinistro.setIdFotoProcessoSinistro(tfm.getLong("idFotoProcessoSinistro"));
    	fotoProcessoSinistro.setDsFoto(tfm.getString("dsFoto"));
    	
    	Long idFoto = tfm.getLong("idFoto");
    	Foto foto = (idFoto == null) ? (new Foto()) : (this.fotoService.findById(idFoto));
    	
		try {
			foto.setFoto(Base64Util.decode(tfm.getString("foto")));
		} catch (IOException e) {
			log.error(e);
		}
		
		fotoProcessoSinistro.setFoto(foto);
    	fotoProcessoSinistro.setProcessoSinistro(processoSinistro);
    	this.fotoService.store(foto);
    	Long idFotoSinistro = (Long) this.fotoProcessoSinistroService.store(fotoProcessoSinistro);
    	
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("idFotoProcessoSinistro", idFotoSinistro);
    	result.put("idFoto", foto.getIdFoto());
    	return result;
    }
    
    public ResultSetPage findPaginatedFotos(TypedFlatMap tfm) {
    	ResultSetPage rsp = this.fotoProcessoSinistroService.findPaginatedCustom(tfm);
    	rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
    	return rsp;
    }
    
    public Integer getRowCountFotos(TypedFlatMap tfm) {
    	return this.fotoProcessoSinistroService.getRowCountCustom(tfm);
    }
    
    public TypedFlatMap findFotosById(Long idFotoProcessoSinistro) {
    	FotoProcessoSinistro fotoProcessoSinistro = this.fotoProcessoSinistroService.findById(idFotoProcessoSinistro);
    	TypedFlatMap tfm = new TypedFlatMap();
    	tfm.put("idFotoProcessoSinistro", fotoProcessoSinistro.getIdFotoProcessoSinistro());
    	tfm.put("idFoto", fotoProcessoSinistro.getFoto().getIdFoto());
    	tfm.put("dsFoto", fotoProcessoSinistro.getDsFoto());
    	tfm.put("foto", Base64Util.encode((byte[])fotoProcessoSinistro.getFoto().getFoto()));
    	return tfm;
    }
    
	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeFotosByIds(List ids) {
    	if (ids.size()>0) {
    		FotoProcessoSinistro fotoProcessoSinistro = fotoProcessoSinistroService.findById((Long)ids.get(0));
    		Long idProcessoSinistro = fotoProcessoSinistro.getProcessoSinistro().getIdProcessoSinistro();
        	getProcessoSinistroService().validateIsProcessoSinistroFechado(idProcessoSinistro);
    		fotoProcessoSinistroService.removeByIds(ids);
    	}
    }
	
    /**
     * Efetua o fechamento do processo de sinistro.
     * @param tfm
     */
    public TypedFlatMap executeEfetuarFechamento(TypedFlatMap tfm) {
    	MasterEntry master = getMasterFromSession(tfm.getLong("idProcessoSinistro"), true);
		ProcessoSinistro processoSinistro = (ProcessoSinistro)master.getMaster();
		processoSinistro.setDhFechamento(JTDateTimeUtils.getDataHoraAtual());
		processoSinistro.setDsJustificativaEncerramento(tfm.getString("dsJustificativaEncerramento"));
		processoSinistro.setUsuarioFechamento(SessionUtils.getUsuarioLogado());

		getProcessoSinistroService().store(processoSinistro);
		updateMasterInSession(master);
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("dhFechamento", processoSinistro.getDhFechamento());
		return result;
    }

    
    public TypedFlatMap findDadosFechamento(TypedFlatMap tfm) {
    	
    	MasterEntry master = getMasterFromSession(tfm.getLong("idProcessoSinistro"), true);
		ProcessoSinistro processoSinistro = (ProcessoSinistro)master.getMaster();
    	
		TypedFlatMap result = new TypedFlatMap();
		if (processoSinistro.getDhFechamento()==null) {
			result.put("nmUsuario", SessionUtils.getUsuarioLogado().getNmUsuario());

		} else {
			result.put("nmUsuario", processoSinistro.getUsuarioFechamento().getNmUsuario());
			result.put("dhFechamento", processoSinistro.getDhFechamento());
			result.put("dsJustificativaEncerramento", processoSinistro.getDsJustificativaEncerramento());
		}
		
		return result;		
    }

    
    public void updateDadosVersao(Long masterId, Integer versao, TypedFlatMap dados) {    	
    	MasterEntry master = getMasterFromSession(masterId, true);
    	ProcessoSinistro processoSinistro = (ProcessoSinistro)master.getMaster();
    	processoSinistro.setVersao(versao);
    	updateMasterInSession(master);
    }
    
    
    /*****************************************************************************************************
	 * Finders
	 *****************************************************************************************************/

    public List findComboMoeda(Map criteria) {
		List<TypedFlatMap>retorno = new ArrayList<TypedFlatMap>();
		List listMoedas = moedaService.findMoedaOrderBySgSimbolo(true);
		for (Iterator iter = listMoedas.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			Moeda moeda = (Moeda) iter.next();
			map.put("idMoeda", moeda.getIdMoeda());
			map.put("siglaSimbolo", moeda.getSiglaSimbolo());
			map.put("tpSituacao.value", moeda.getTpSituacao().getValue());
			retorno.add(map);
		}
		return retorno;
	}
	
	public List findComboTipoProcessoSinistro(TypedFlatMap tfm) {
		List<TypedFlatMap>retorno = new ArrayList<TypedFlatMap>();
		List tiposSinistro = this.tipoSinistroService.findOrderByDsTipo(tfm); 
		
		if (tiposSinistro != null && !tiposSinistro.isEmpty()) {
			for (Iterator iter = tiposSinistro.iterator(); iter.hasNext();) {
				TypedFlatMap map = new TypedFlatMap();
				TipoSinistro tipoSinistro = (TipoSinistro) iter.next();
				map.put("idTipoSinistro", tipoSinistro.getIdTipoSinistro());
	   			map.put("dsTipo", tipoSinistro.getDsTipo());
	   			map.put("tpSituacao.value", tipoSinistro.getTpSituacao().getValue());
	   			retorno.add(map);
			}
		}
		
		return retorno;
	}
	
	public List findComboTipoProcessoSinistroByTipoSeguro(TypedFlatMap tfm) {
		List<TypedFlatMap>retorno = new ArrayList<TypedFlatMap>();
		List tiposSinistro = this.tipoSinistroService.findTipoProcessoSinistroByTipoSeguro(tfm); 
		
		if (tiposSinistro != null && !tiposSinistro.isEmpty()) {
			for (Iterator iter = tiposSinistro.iterator(); iter.hasNext();) {
				TypedFlatMap map = new TypedFlatMap();
				TipoSinistro tipoSinistro = (TipoSinistro) iter.next();
				map.put("idTipoSinistro", tipoSinistro.getIdTipoSinistro());
	   			map.put("dsTipo", tipoSinistro.getDsTipo());
	   			map.put("tpSituacao.value", tipoSinistro.getTpSituacao().getValue());
	   			retorno.add(map);
			}
		}
		
		return retorno;
	}
	
	// LMS-6178
	public TypedFlatMap findCorretoraAndSeguradora(TypedFlatMap tfm) {				
		String data = JTDateTimeUtils.convertToUniversalDateTime(tfm.getDateTime("dhSinistro").toYearMonthDay());
				
		String corretora = apoliceSeguroService.findCorretaByTipoSeguro(tfm.getLong("idTipoSeguro"), data);
		String seguradora = apoliceSeguroService.findSeguradoraByTipoSeguro(tfm.getLong("idTipoSeguro"), data);
		
		TypedFlatMap result = new TypedFlatMap();
		
		if(!corretora.isEmpty())
			result.put("corretora", corretora);
		if(!seguradora.isEmpty())
			result.put("seguradora", seguradora);
		
		return result;
	}
	
	public List findComboTipoSeguro(TypedFlatMap tfm) {
		List<TypedFlatMap> retorno = new ArrayList<TypedFlatMap>();
		List tiposSeguro = this.tipoSeguroService.findOrderBySgTipo(tfm); 
		for (Iterator iter = tiposSeguro.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			TipoSeguro tipoSeguro = (TipoSeguro) iter.next();
			map.put("idTipoSeguro", tipoSeguro.getIdTipoSeguro());
			map.put("sgTipo", tipoSeguro.getSgTipo());
			map.put("tpSituacao.value", tipoSeguro.getTpSituacao().getValue());			
			retorno.add(map);
		}
		return retorno;
	}

	//LMS-6178
	public List findComboSituacaoReembolso(TypedFlatMap tfm) {
		List<TypedFlatMap> retorno = new ArrayList<TypedFlatMap>();
		List situacoesReembolso = this.situacaoReembolsoService.findOrderByDsReembolso(tfm);
		for (Iterator iter = situacoesReembolso.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			SituacaoReembolso situacaoReembolso = (SituacaoReembolso) iter.next();
			map.put("idSituacaoReembolso", situacaoReembolso.getIdSituacaoReembolso());
			map.put("dsSituacaoReembolso", situacaoReembolso.getDsSituacaoReembolso());
			map.put("blStatus.value", situacaoReembolso.getBlStatus().getValue());			
			retorno.add(map);
		}
		return retorno;
	}
	
	//LMS-6178
	public List findLookupMotorista(Map criteria) {
    	return motoristaService.findLookup(criteria);
	}

	public List findLookupRodovia(TypedFlatMap tfm) {
		return this.rodoviaService.findLookup(tfm);
	}
	
	public List findLookupAeroporto(TypedFlatMap tfm) {
		return this.aeroportoService.findLookupAeroporto(tfm);
	}
	
	public List findLookupUnidadeFederativa(Map criteria){
		return this.unidadeFederativaService.findLookup(criteria);
	}
	
	public List findComboReguladora(TypedFlatMap tfm){
		String tpSituacao = tfm.getString("tpSituacao");
		return this.reguladoraSeguroService.findCombo(tpSituacao);
	}

	public List findComboUnidadeFederativa(TypedFlatMap criteria){
		return this.unidadeFederativaService.findCombo(criteria);
	}
    
    public List findLookupFilial(TypedFlatMap map) {
    	return this.filialService.findLookupBySgFilial((String)map.get("sgFilial"), (String)map.get("tpAcesso"));
    }
    
    public List findLookupMunicipio(TypedFlatMap map) {
    	return this.municipioService.findLookup(map);
    }
    
    public List findLookupCliente(Map criteria){
    	return this.clienteService.findLookup(criteria);
    }
    
	public List findLookupControleCarga(TypedFlatMap map) {
		return this.controleCargaService.findLookup(map);
	}

	public List findLookupUsuarioFuncionario(TypedFlatMap tfm){

		ConsultarUsuarioLMSParam cup = new ConsultarUsuarioLMSParam();

		cup.setNrMatricula(tfm.getString("nrMatricula"));
		cup.setNmUsuario(tfm.getString("nmUsuario"));
		cup.setTpCategoriaUsuario(tfm.getString("tpCategoriaUsuario"));

		return usuarioLMSService.findLookupSistema(cup);
		
	}
	
    public List findManifestoByRNC(Long idManifesto) {
    	List lista = this.manifestoService.findManifestoByRNC(idManifesto);
    	return lista; 
    }
	
    public List findTipoManifesto(Map criteria) {
    	List<String>dominiosValidos = new ArrayList<String>();
    	dominiosValidos.add("EN");
    	dominiosValidos.add("VN");
    	dominiosValidos.add("VI");
    	dominiosValidos.add("EP");
    	List retorno = this.domainValueService.findByDomainNameAndValues("DM_TAG_MANIFESTO", dominiosValidos);
    	return retorno;
    }
    
    /**
     * Retorna a lista de tipos de docto de serviço para
     * combo de docto de serviço
     * @return
     */
	public List findTpDoctoServico() {
		String tpDocFat = (String)configuracoesFacade.getValorParametro("TP_DOCTO_SEG");

		String[] dm = tpDocFat.split(";");
        List dominiosValidos = Arrays.asList(dm);
	    List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
	    
		return retorno;
	}
	
	/**
     * FindLookup para filial do tipo de DoctoServico Escolhido.
     */ 
    public List findLookupServiceDocumentFilialCTR(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialCRT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialMDA(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialRRE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    public List findLookupServiceDocumentFilialNFT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialNFS(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    public List findLookupServiceDocumentFilialNTE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialNSE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialCTE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
	
    public List findLookupServiceDocumentFilialNDN(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
	/** 
     * Busca a filial baseado no documento de serviço
     * @param criteria
     * @return
     */
    public List findLookupFilialByDocumentoServico(Map criteria) {
    	
    	FilterList filter = new FilterList(getFilialService().findLookup(criteria)) {
			public Map filterItem(Object item) {
	    			Filial filial = (Filial)item;
	    			TypedFlatMap typedFlatMap = new TypedFlatMap();
		    		typedFlatMap.put("idFilial", filial.getIdFilial());
			    	typedFlatMap.put("sgFilial", filial.getSgFilial());
				return typedFlatMap;
			}
    	};
    	
    	return (List)filter.doFilter();
    }
    
    /**
     * FindLookup para a tag DoctoServico.
     */  
    public List findLookupServiceDocumentNumberCTE(Map criteria) {
    	return this.getConhecimentoService().findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNSE(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNTE(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNFS(Map criteria) {
    	return notaFiscalServicoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberCTR(Map criteria) {
    	return this.getConhecimentoService().findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberCRT(Map criteria) {    	
    	return this.getCtoInternacionalService().findLookup(criteria);
    }    
    public List findLookupServiceDocumentNumberMDA(Map criteria) {
    	return this.getMdaService().findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberRRE(Map criteria) {
    	return this.getReciboReembolsoService().findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNFT(Map criteria) {
    	return this.conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNDN(Map map) {
        return this.notaDebitoNacionalService.findLookup(map);
     }
	public List findLookupMeioTransporteRodoviario(Map map) {
		return this.meioTransporteService.findLookup(map);
	}
	/**
	 * Usado na lookup da aba de detalhamento
	 * @param tfm
	 * @return
	 */
	public List findLookupMeioTransporte(Map tfm) {
		TypedFlatMap t = new TypedFlatMap();
		Map p = (Map)tfm.get("meioTransporte");
		t.put("meioTransporte.nrFrota", 		p.get("nrFrota"));
		t.put("meioTransporte.nrIdentificador", p.get("nrIdentificador"));
		return meioTransporteRodoviarioService.findLookup(t);
	}
	
	// LMS-6178
	public TypedFlatMap findDataLookupByIdMeioTransporte(TypedFlatMap tfm){
		return meioTransporteService.findDataLookupByIdMeioTransporte(tfm);
	}
	
    public Map findManifestoComControleCargas(Long idDoctoServico) {
    	return manifestoService.findManifestoComControleCargas(idDoctoServico);
    }
    
    public List findLookupManifestoDocumentFilialEN(Map criteria) {
    	return findLookupFilialByManifesto(criteria);
    }

    public List findLookupManifestoDocumentFilialVN(Map criteria) {
    	return findLookupFilialByManifesto(criteria);
    }

    public List findLookupManifestoDocumentFilialVI(Map criteria) {
    	return findLookupFilialByManifesto(criteria);
    }
    
    public List findLookupManifestoDocumentFilialEP(Map criteria) {
    	return findLookupFilialByManifesto(criteria);
    }
    
    private List findLookupFilialByManifesto(Map criteria) {
    	List list = this.filialService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Filial filial = (Filial)iter.next();
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("idFilial", filial.getIdFilial());
    		tfm.put("sgFilial", filial.getSgFilial());
    		tfm.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
    		retorno.add(tfm);
    	}
    	return retorno;
    }


    public List findLookupManifestoDocumentNumberEN(TypedFlatMap criteria) {
    	criteria.put("tpManifestoEntrega", "EN");
    	criteria.put("manifesto.tpManifesto", "E");
    	List list = manifestoEntregaService.findLookupByTagManifesto(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		ManifestoEntrega manifestoEntrega = (ManifestoEntrega)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idManifestoEntrega", manifestoEntrega.getIdManifestoEntrega());
    		typedFlatMap.put("nrManifestoEntrega", manifestoEntrega.getNrManifestoEntrega());
    		typedFlatMap.put("idFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getIdFilial());
    		typedFlatMap.put("sgFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getSgFilial());
    		typedFlatMap.put("nmFantasiaFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }

    public List findLookupManifestoDocumentNumberEP(TypedFlatMap criteria) {
    	criteria.put("tpManifestoEntrega", "EP");
    	criteria.put("manifesto.tpManifesto", "E");
    	List list = manifestoEntregaService.findLookupByTagManifesto(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		ManifestoEntrega manifestoEntrega = (ManifestoEntrega)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idManifestoEntrega", manifestoEntrega.getIdManifestoEntrega());
    		typedFlatMap.put("nrManifestoEntrega", manifestoEntrega.getNrManifestoEntrega());
    		typedFlatMap.put("idFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getIdFilial());
    		typedFlatMap.put("sgFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getSgFilial());
    		typedFlatMap.put("nmFantasiaFilialManifesto", manifestoEntrega.getManifesto().getFilialByIdFilialOrigem().getPessoa().getNmFantasia());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }
    
    public List findLookupManifestoDocumentNumberVN(Map criteria) {
    	Map manifesto = (Map)criteria.get("manifesto");
    	manifesto.put("tpManifesto", "V");
    	manifesto.put("tpAbrangencia", "N");
    	return this.manifestoViagemNacionalService.findLookup(criteria);
    }

    public List findLookupManifestoDocumentNumberVI(Map criteria) {
    	Map manifesto = (Map)criteria.get("manifesto");
    	manifesto.put("tpManifesto", "V");
    	manifesto.put("tpAbrangencia", "I");
    	return this.manifestoInternacionalService.findLookup(criteria);
    }
    
    public TypedFlatMap getMoedaSessao() {
    	TypedFlatMap tfm = new TypedFlatMap();
    	Moeda moeda = SessionUtils.getMoedaSessao();
    	tfm.put("sgSimboloMoeda", moeda.getSiglaSimbolo());
    	tfm.put("idMoeda", moeda.getIdMoeda());
    	return tfm;
    }

	
	public Map validaValoresByTpPrejuizo(TypedFlatMap params){
		String tpPrejuizo = params.getString("tpPrejuizo");
		BigDecimal vlMercadoria = params.getBigDecimal("vlMercadoria");
		BigDecimal vlPrejuizo = params.getBigDecimal("vlPrejuizo");
		if (tpPrejuizo.equalsIgnoreCase("P")){
			if (!CompareUtils.le(vlPrejuizo, vlMercadoria)){
				String mensagemErro = configuracoesFacade.getMensagem("LMS-22009");
				params.put("mensagemErro", mensagemErro);
				return params;
			} else if (CompareUtils.eq(vlPrejuizo, vlMercadoria)){
				params.put("flagValoresIguais", "true");
			}
		}
		return params;
	}
	
	/**
	 * Faz a validacao do PCE.
	 *  
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap validatePCE(TypedFlatMap criteria) {
		Long idProcessoSinistro = criteria.getLong("idProcessoSinistro");
		
		MasterEntry master = getMasterFromSession(idProcessoSinistro, true);
		ProcessoSinistro processoSinistro = (ProcessoSinistro) master.getMaster();
    			
		ItemList itens = getItemsFromSession(master, DOCUMENTOS_CONFIG_ALIAS);
		
		return sinistroDoctoServicoService.validatePCE(itens);		
	}
	
	public TypedFlatMap isValidatedDHSinistro(TypedFlatMap criteria){	
		DateTime dataHora = criteria.getDateTime("dhSinistro");
		boolean isValidate = false;
		TypedFlatMap map= new TypedFlatMap(); 
		if(dataHora.compareTo(JTDateTimeUtils.getDataHoraAtual())>0){
			isValidate = true;
		}
		map.put("isValidate",isValidate);		
		return map;
	}
	
    public void removeById(TypedFlatMap tfm) {
    	getProcessoSinistroService().removeById(tfm);
	}
    
    public TypedFlatMap reabrirProcessoSinistroFechado(TypedFlatMap tfm) {
    	MasterEntry master = getMasterFromSession(tfm.getLong("idProcessoSinistro"), true);
    	
    	ProcessoSinistro processoSinistro = (ProcessoSinistro)master.getMaster();
    	
    	processoSinistro.setDhFechamento(null);
    	
    	this.getProcessoSinistroService().store(processoSinistro);
		
    	return new TypedFlatMap();
    }
    
    /**
     * LMS-6180 Retorna os registros de doctoServico com base nos filtros preenchidos
     * na popup de "Selecionar varios Documentos
     */
    public ResultSetPage findPaginatedDocumentosPopup(TypedFlatMap tfm){
    	ResultSetPage rsp = getProcessoSinistroService().findPaginatedDocumentosPopup(tfm);
    	
		if (rsp.getList() != null && !rsp.getList().isEmpty()) {

			List result = new ArrayList();

			for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
				Object[] row = (Object[]) iter.next();
				
				TypedFlatMap map = new TypedFlatMap();
				map.put("idDoctoServico",row[0]);
				map.put("tpDocumentoServico",row[1]);
				map.put("sgFilialDocumento",row[2]);
				map.put("nrDocumentoServico",row[3]);
				map.put("sgFilialDestino",row[4]);
				map.put("dataEmissao",row[5]);
				map.put("vlMercadoria",row[6]);
				map.put("qtVolumes",row[7]);
				map.put("peso",row[8]);
				map.put("remetente",row[9]);
				map.put("destinatario",row[10]);
				map.put("devedor",row[11]);
				
				result.add(map);
			}
			rsp.setList(result);
		}
    	
    	return rsp;
    }
    
    public Integer getRowCountDocumentosPopup(TypedFlatMap tfm){
    	return getProcessoSinistroService().getRowCountDocumentosPopup(tfm);
    }
    
    /**
     * LMS-6180 - Ao clicar no botão 'Selecionar todos os documentos localizados' 
     * Retorna todos os documentos da consulta para a popup de documentos
     */
    public TypedFlatMap findAllDocuments(TypedFlatMap tfm){
    	List<Long> list = getProcessoSinistroService().findAllDocuments(tfm);
    	TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("ids", list);
    	return retorno;
    }
}
