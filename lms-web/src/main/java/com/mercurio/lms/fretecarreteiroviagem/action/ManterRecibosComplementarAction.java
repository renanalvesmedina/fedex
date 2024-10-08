package com.mercurio.lms.fretecarreteiroviagem.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.fretecarreteiroviagem.manterRecibosComplementarAction"
 */

public class ManterRecibosComplementarAction {
	
	private ReportExecutionManager reportExecutionManager;
	
	
	
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	/**
	 * Getter da service padr�o da tela.
	 * @return service ReciboFreteCarreteiroService.
	 */
	private ReciboFreteCarreteiroService getReciboFreteCarreteiroService() {
		return reciboFreteCarreteiroService;
	}
	
	private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
	private FilialService filialService;
	private ControleCargaService controleCargaService;
	private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
	private ProprietarioService proprietarioService;
	private ConfiguracoesFacade configuracoesFacade;
	
	private com.mercurio.lms.fretecarreteiroviagem.report.EmitirReciboService emitirReciboViagemService;
	private com.mercurio.lms.fretecarreteirocoletaentrega.report.EmitirReciboService emitirReciboColetaService;
	
	/**
	 * Retorna consulta paginada.
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
    	return getReciboFreteCarreteiroService().findPaginatedComplementar(criteria);
    }
	
	/**
	 * Retorna total de registros para pagina��o.
	 * @param criteria
	 * @return
	 */
    public Integer getRowCountCustom(TypedFlatMap criteria) {
    	return getReciboFreteCarreteiroService().getRowCountComplementar(criteria);
    }
    
    /**
     * Retorna TypedFlatMap com todos campos para o detalhamento.
     * @param id
     * @return
     */
    public TypedFlatMap findByIdCustom(java.lang.Long id) {
    	ReciboFreteCarreteiro rfc = getReciboFreteCarreteiroService().findByIdComplementar(id);
    	TypedFlatMap retorno = new TypedFlatMap();
    	
    	ReciboFreteCarreteiro rfcComplementado = rfc.getReciboComplementado();
    	Filial filialComplementado = rfcComplementado.getFilial();
    	retorno.put("reciboComplementado.idReciboFreteCarreteiro",rfcComplementado.getIdReciboFreteCarreteiro());
    	retorno.put("reciboComplementado.filial.sgFilial",filialComplementado.getSgFilial());
    	retorno.put("reciboComplementado.nrReciboFreteCarreteiro",rfcComplementado.getNrReciboFreteCarreteiro());
    	
    	retorno.put("idReciboFreteCarreteiro",rfc.getIdReciboFreteCarreteiro());
    	retorno.put("filial.idFilial",rfc.getFilial().getIdFilial());
    	retorno.put("filial.sgFilial",rfc.getFilial().getSgFilial());
    	retorno.put("filial.pessoa.nmFantasia",rfc.getFilial().getPessoa().getNmFantasia());
    	retorno.put("nrReciboFreteCarreteiro",rfc.getNrReciboFreteCarreteiro());
    	retorno.put("tpSituacaoRecibo.value",rfc.getTpSituacaoRecibo().getValue());
    	retorno.put("tpSituacaoRecibo.description",rfc.getTpSituacaoRecibo().getDescription());
    	retorno.put("tpReciboFreteCarreteiro.value",rfc.getTpReciboFreteCarreteiro().getValue());
    	
    	Pessoa proprietarioPessoa = rfc.getProprietario().getPessoa();
    	retorno.put("idProprietarioComp",proprietarioPessoa.getIdPessoa());
    	String nrIdentificacaoProprietario = FormatUtils.formatIdentificacao(
    			proprietarioPessoa.getTpIdentificacao(),proprietarioPessoa.getNrIdentificacao());
		retorno.put("proprietario.nrIdentificacao",nrIdentificacaoProprietario);
    	retorno.put("proprietario.nmPessoa",proprietarioPessoa.getNmPessoa());
    	
    	Motorista motorista = rfc.getMotorista();
    	
    	if (Hibernate.isInitialized(motorista) && motorista != null) {
			Pessoa motoristaPessoa = motorista.getPessoa();
	    	retorno.put("idMotoristaComp",motoristaPessoa.getIdPessoa());
	    	String nrIdentificacaoMotorista = FormatUtils.formatIdentificacao(
	    			motoristaPessoa.getTpIdentificacao(),motoristaPessoa.getNrIdentificacao());
			retorno.put("motorista.nrIdentificacao",nrIdentificacaoMotorista);
	    	retorno.put("motorista.nmPessoa",motoristaPessoa.getNmPessoa());
    	}
    	retorno.put("nrNfCarreteiro",rfc.getNrNfCarreteiro());
    	retorno.put("obReciboFreteCarreteiro",rfc.getObReciboFreteCarreteiro());
    	retorno.put("dtContabilizacao",rfc.getDtContabilizacao());
    	retorno.put("dtSugeridaPagto",rfc.getDtSugeridaPagto());
    	retorno.put("dtPagtoReal",rfc.getDtPagtoReal());
    	
    	MeioTransporte meioTransporte = rfc.getMeioTransporteRodoviario().getMeioTransporte();
    	retorno.put("idMeioTransporteComp",meioTransporte.getIdMeioTransporte());
		retorno.put("meioTransporte.nrFrota",meioTransporte.getNrFrota());
    	retorno.put("meioTransporte.nrIdentificador",meioTransporte.getNrIdentificador());
    	retorno.put("meioTransporte.dsMarca",meioTransporte.getModeloMeioTransporte().getMarcaMeioTransporte().getDsMarcaMeioTransporte());
    	retorno.put("meioTransporte.dsModelo",meioTransporte.getModeloMeioTransporte().getDsModeloMeioTransporte());
    	
    	if (rfc.getRelacaoPagamento() != null)
    		retorno.put("relacaoPagamento.nrRelacaoPagamento",rfc.getRelacaoPagamento().getNrRelacaoPagamento());
    	
    	ControleCarga controleCarga = rfc.getControleCarga();
		if (controleCarga != null) {
			retorno.put("idControleCargaComp",controleCarga.getIdControleCarga());
    		retorno.put("controleCarga.nrControleCarga",controleCarga.getNrControleCarga());
    		Filial filialCCByIdFilialOrigem = controleCarga.getFilialByIdFilialOrigem();
			retorno.put("controleCarga.idFilial",filialCCByIdFilialOrigem.getIdFilial());
        	retorno.put("controleCarga.sgFilial",filialCCByIdFilialOrigem.getSgFilial());
        	retorno.put("controleCarga.nmFilial",filialCCByIdFilialOrigem.getPessoa().getNmFantasia());
        	retorno.put("controleCarga.dhEvento",getReciboFreteCarreteiroService().findCCDhEvento(controleCarga.getIdControleCarga()));    	
    	}
    	
		retorno.put("dhGeracaoMovimento",rfc.getDhGeracaoMovimento());
		
		MoedaPais moedaPais = rfc.getMoedaPais();
		retorno.put("moedaPais.idMoedaPais",moedaPais.getIdMoedaPais());
    	retorno.put("vlBruto",rfc.getVlBruto());
		
    	retorno.put("vlSalarioContribuicao",rfc.getVlSalarioContribuicao());
    	retorno.put("pcAliquotaInss",rfc.getPcAliquotaInss());
    	retorno.put("vlOutrasFontes",rfc.getVlOutrasFontes());
    	retorno.put("vlApuradoInss",rfc.getVlInss());
    	retorno.put("pcAliquotaIssqn",rfc.getPcAliquotaIssqn());
    	retorno.put("vlIssqn",rfc.getVlIssqn());
    	retorno.put("pcAliquotaIrrf",rfc.getPcAliquotaIrrf());
    	retorno.put("vlIrrf",rfc.getVlIrrf());
    	
    	retorno.put("vlLiquido",rfc.getVlLiquido());
    	
    	return retorno;
    }
    
    public void removeById(java.lang.Long id) {
    	getReciboFreteCarreteiroService().removeById(id);
    }
    
    /**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	getReciboFreteCarreteiroService().removeByIds(ids);
    }
       
    public TypedFlatMap storeCustom(TypedFlatMap bean) {
    	TypedFlatMap tfm = new TypedFlatMap();
    	
    	ReciboFreteCarreteiro rfc = new ReciboFreteCarreteiro();
    	
    	ReciboFreteCarreteiro rfcDB = reciboFreteCarreteiroService.findById(bean.getLong("reciboComplementado.idReciboFreteCarreteiro"));
    	rfc.setManifestoViagemNacional(rfcDB.getManifestoViagemNacional());

    	rfc.setIdReciboFreteCarreteiro(bean.getLong("idReciboFreteCarreteiro"));
    	
    	Filial filial = new Filial();
    	MoedaPais moedaPais = new MoedaPais();
    	ReciboFreteCarreteiro rfcComplementado = new ReciboFreteCarreteiro();
    	Proprietario proprietario = new Proprietario();
    	Motorista motorista = new Motorista();
    	MeioTransporteRodoviario meioTransporteRodoviario =  new MeioTransporteRodoviario();
    	
    	filial.setIdFilial(bean.getLong("filial.idFilial"));
    	rfc.setFilial(filial);
    	
    	moedaPais.setIdMoedaPais(bean.getLong("moedaPais.idMoedaPais"));
    	rfc.setMoedaPais(moedaPais);
    	
    	
    	rfcComplementado.setIdReciboFreteCarreteiro(bean.getLong("reciboComplementado.idReciboFreteCarreteiro"));
    	rfc.setReciboComplementado(rfcComplementado);
    	
    	proprietario.setIdProprietario(bean.getLong("idProprietarioComp"));
    	rfc.setProprietario(proprietario);
    	
    	Long idControleCarga = bean.getLong("idControleCargaComp");
		if (idControleCarga != null) {
			ControleCarga controleCarga = new ControleCarga();
			controleCarga.setIdControleCarga(idControleCarga);
	    	rfc.setControleCarga(controleCarga);
		}    	
    	
    	motorista.setIdMotorista(bean.getLong("idMotoristaComp"));
    	rfc.setMotorista(motorista);
    	
    	meioTransporteRodoviario.setIdMeioTransporte(bean.getLong("idMeioTransporteComp"));
    	rfc.setMeioTransporteRodoviario(meioTransporteRodoviario);
    	
    	
    	rfc.setTpReciboFreteCarreteiro(bean.getDomainValue("tpReciboFreteCarreteiro"));
    	rfc.setVlBruto(bean.getBigDecimal("vlBruto"));
    	rfc.setBlAdiantamento(Boolean.FALSE);
    	rfc.setVlPremio(new BigDecimal(0));
    	rfc.setVlPostoPassagem(new BigDecimal(0));
    	rfc.setObReciboFreteCarreteiro(bean.getString("obReciboFreteCarreteiro"));
    	rfc.setNrNfCarreteiro(bean.getString("nrNfCarreteiro"));
    	rfc.setDtPagtoReal(bean.getYearMonthDay("dtPagtoReal"));
    	
    	ReciboFreteCarreteiro rfcStored = (ReciboFreteCarreteiro)getReciboFreteCarreteiroService().storeComplementar(rfc);
    	
    	tfm.put("idReciboFreteCarreteiro",rfcStored.getIdReciboFreteCarreteiro());
    	tfm.put("tpSituacaoRecibo.value",rfcStored.getTpSituacaoRecibo().getValue());
    	tfm.put("tpSituacaoRecibo.description",rfcStored.getTpSituacaoRecibo().getDescription().getValue());
		tfm.put("nrReciboFreteCarreteiro",rfcStored.getNrReciboFreteCarreteiro());
    	tfm.put("vlLiquido",rfcStored.getVlLiquido());
    	return tfm;
    }
    
    public TypedFlatMap storeCancelarRecibo(TypedFlatMap values) {
    	String tpSituacaoRecibo = values.getString("tpSituacaoRecibo.value");
    	if (!tpSituacaoRecibo.equals("GE") && !tpSituacaoRecibo.equals("EM"))
    		throw new BusinessException("LMS-24013");
    	
    	String dhGeracaoMovimento = values.getString("dhGeracaoMovimento");
    	if (tpSituacaoRecibo.equals("EM") && StringUtils.isNotBlank(dhGeracaoMovimento))
    		throw new BusinessException("LMS-24029");
    	
    	Long idReciboFreteCarreteiro = values.getLong("idReciboFreteCarreteiro");
    	ReciboFreteCarreteiro rfc = getReciboFreteCarreteiroService().storeCancelarRecibo(idReciboFreteCarreteiro);
    	
    	TypedFlatMap retorno = new TypedFlatMap();
    	retorno.put("tpSituacaoRecibo.value",rfc.getTpSituacaoRecibo().getValue());
    	retorno.put("tpSituacaoRecibo.description",rfc.getTpSituacaoRecibo().getDescription());
    	
    	return retorno;
    }
    
    /**
     * findLookup de filiais.
     * @param criteria
     * @return
     */
    public List findLookupFilial(Map criteria) {
		return filialService.findLookupFilial(criteria);
	}
    
    /**
     * findLookup de Meio de transporte.
     * @param criteria
     * @return
     */
    public List findLookupMeioTransporte(TypedFlatMap criteria) {
    	return meioTransporteRodoviarioService.findLookupWithProprietario(criteria);
    }
    
    /**
     * findLookup de Controle de Carga.
     * @param criteria
     * @return
     */
    public List findLookupControleCarga(Map criteria) {
    	return controleCargaService.findLookup(criteria);
    }
    
    /**
     * findLookup de Propriet�rio.
     * @param criteria
     * @return
     */
    public List findLookupProprietario(Map criteria) {
    	return proprietarioService.findLookup(criteria);
    }
	
    /**
     * findLookup de Recibo.
     * @param criteria
     * @return
     */
    public List findLookupRecibo(TypedFlatMap criteria) {
    	String tpRecibo = (String)criteria.get("tpReciboFreteCarreteiro");
		if (StringUtils.isNotBlank(tpRecibo)) {
			criteria.remove("tpReciboFreteCarreteiro");
			if (tpRecibo.equals("C")) {
				return getReciboFreteCarreteiroService().findLookupColetaEntrega(criteria);
			}
		}
    	return getReciboFreteCarreteiroService().findLookupViagem(criteria);
    }
    
    /**
     * Retorna para a tela a filial do usu�rio logado.
     * @return
     */
    public TypedFlatMap findFilialUsuarioLogado() {
    	TypedFlatMap retorno = new TypedFlatMap();
    	
    	Filial f = SessionUtils.getFilialSessao();
    	retorno.put("idFilial",f.getIdFilial());
    	retorno.put("sgFilial",f.getSgFilial());
    	retorno.put("pessoa.nmFantasia",f.getPessoa().getNmFantasia());
    	
    	return retorno;
    }
    
    public TypedFlatMap findMoedaPaisByIdFilial(TypedFlatMap data) {
    	TypedFlatMap retorno = new TypedFlatMap();
    	
    	Long idFilial = data.getLong("idFilial");
    	MoedaPais moedaPais = filialService.findMoedaPaisByIdFilial(idFilial);
    	
    	if (moedaPais != null) {
    		retorno.put("moedaPais.idMoedaPais",moedaPais.getIdMoedaPais());
    	}
    	
    	return retorno;
    }
    
    public String execute(TypedFlatMap parameters) throws Exception {
    	Long idRecibo = parameters.getLong("idReciboFreteCarreteiro");
    	if (idRecibo == null)
    		throw new IllegalArgumentException("O m�todo execute exige um long 'idReciboFreteCarreteiro' nos parametros.");
    	
    	String tpReciboFreteCarreteiro = parameters.getString("tpReciboFreteCarreteiro");
    	boolean blReemissao = false;
    	if (tpReciboFreteCarreteiro.equals("V")) {
    		blReemissao = reciboFreteCarreteiroService.storeValidateEmissaoReciboViagem(idRecibo);
    	} else {
    		blReemissao = reciboFreteCarreteiroService.storeValidateEmissaoReciboColetaEntrega(idRecibo);
    	}
    	parameters.put("blReemissao",new Boolean(blReemissao));
    	
    	Boolean blAdiantamento = reciboFreteCarreteiroService.validateIfBlAdiantamento(idRecibo);
    	parameters.put("blAdiantamento",blAdiantamento);
    	
    	String reportLocator;
    	if (tpReciboFreteCarreteiro.equals("V")) {
    		reportLocator = this.reportExecutionManager.generateReportLocator(emitirReciboViagemService, parameters);
    	} else if (tpReciboFreteCarreteiro.equals("C")) {
    		parameters = new TypedFlatMap();
    		parameters.put(idRecibo,new Object[]{new Boolean(blReemissao)});
    		reportLocator = this.reportExecutionManager.generateReportLocator(emitirReciboColetaService, parameters);
    	} else {
    		throw new IllegalArgumentException("tpReciboFreteCarreteiro inv�lido: "+tpReciboFreteCarreteiro);
    	}
    	
    	return reportLocator;
    }
    
    
    /**
	 * Retorna valores da combo de moeda a partir do pa�s do usu�rio logado.
	 * @return
	 */
	public List findMoedaByPais() {
		Pais p = SessionUtils.getPaisSessao();
		return configuracoesFacade.getMoedasPais(p.getIdPais(),Boolean.TRUE);
	}
    
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setMeioTransporteRodoviarioService(
			MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
		this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
	}
	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setReciboFreteCarreteiroService(
			ReciboFreteCarreteiroService reciboFreteCarreteiroService) {
		this.reciboFreteCarreteiroService = reciboFreteCarreteiroService;
	}

	public void setEmitirReciboColetaService(
			com.mercurio.lms.fretecarreteirocoletaentrega.report.EmitirReciboService emitirReciboColetaService) {
		this.emitirReciboColetaService = emitirReciboColetaService;
	}

	public void setEmitirReciboViagemService(
			com.mercurio.lms.fretecarreteiroviagem.report.EmitirReciboService emitirReciboViagemService) {
		this.emitirReciboViagemService = emitirReciboViagemService;
	}
	
}
