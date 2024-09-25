package com.mercurio.lms.fretecarreteiroviagem.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.AdiantamentoTrechoService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.model.AgenciaBancaria;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.configuracoes.model.ContaBancaria;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.contratacaoveiculos.model.Beneficiario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.PostoConveniado;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.fretecarreteiroviagem.report.EmitirReciboService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.fretecarreteiroviagem.manterRecibosAction"
 */

public class ManterRecibosAction {
	
	private EmitirReciboService emitirReciboService;

	public void setEmitirReciboService(EmitirReciboService emitirReciboService) {
		this.emitirReciboService = emitirReciboService;
	}
	
	private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
	private FilialService filialService;
	private ControleCargaService controleCargaService;
	private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
	private ProprietarioService proprietarioService;
	private ReportExecutionManager reportExecutionManager;
	private AdiantamentoTrechoService adiantamentoTrechoService;
	
	/**
	 * Retorna consulta paginada.
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
    	return reciboFreteCarreteiroService.findPaginatedViagem(criteria);
    }
	
	/**
	 * Retorna total de registros para pagina��o.
	 * @param criteria
	 * @return
	 */
    public Integer getRowCountCustom(TypedFlatMap criteria) {
    	return reciboFreteCarreteiroService.getRowCountViagem(criteria);
    }
       
    /**
     * Retorna TypedFlatMap com todos campos para o detalhamento.
     * @param id
     * 
     * @return
     */
    public TypedFlatMap findByIdCustom(java.lang.Long id) {
    	ReciboFreteCarreteiro rfc = reciboFreteCarreteiroService.findByIdCustom(id);

    	TypedFlatMap retorno = new TypedFlatMap();
    	
    	retorno.put("idReciboFreteCarreteiro",rfc.getIdReciboFreteCarreteiro());
    	
    	Filial filial = rfc.getFilial();
		retorno.put("filial.idFilial",filial.getIdFilial());
    	retorno.put("filial.sgFilial",filial.getSgFilial());
    	retorno.put("filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
    	
    	ReciboFreteCarreteiro reciboComplementado = rfc.getReciboComplementado();
		String nrRecibo = FormatUtils.formatLongWithZeros(rfc.getNrReciboFreteCarreteiro(),"0000000000");
		retorno.put("nrReciboFreteCarreteiro",nrRecibo);
		retorno.put("nrReciboFreteCarreteiro2",
    			nrRecibo + (reciboComplementado != null ? "C" : ""));
    	
    	DomainValue tpSituacaoRecibo = rfc.getTpSituacaoRecibo();
		retorno.put("tpSituacaoRecibo.value",tpSituacaoRecibo.getValue());
    	retorno.put("tpSituacaoRecibo.description",tpSituacaoRecibo.getDescription());
    	
    	retorno.put("blAdiantamento",rfc.getBlAdiantamento());
    	retorno.put("blComplementar",Boolean.valueOf(reciboComplementado != null));
    	
    	Pessoa proprietarioPessoa = rfc.getProprietario().getPessoa();
    	retorno.put("proprietario.idProprietario",proprietarioPessoa.getIdPessoa());
    	retorno.put("proprietario.pessoa.nmPessoa",proprietarioPessoa.getNmPessoa());
    	
    	String nrIdentificacaoProprietario = FormatUtils.formatIdentificacao(
    			proprietarioPessoa.getTpIdentificacao(),proprietarioPessoa.getNrIdentificacao());
		retorno.put("proprietario.pessoa.nrIdentificacaoFormatado",nrIdentificacaoProprietario);
    	
		Beneficiario beneficiario = rfc.getBeneficiario();
		Pessoa beneficiarioPessoa = null;
		if (beneficiario != null) {
			beneficiarioPessoa = beneficiario.getPessoa();
		} else {
			PostoConveniado postoConveniado = adiantamentoTrechoService.findPostoConveniadoByIdReciboFreteCarreteiro(rfc.getIdReciboFreteCarreteiro());
			
			if(postoConveniado != null){
				beneficiarioPessoa = postoConveniado.getPessoa();
			}
		}
			
		if (beneficiarioPessoa  != null) {
			retorno.put("beneficiario.idBeneficiario",beneficiarioPessoa.getIdPessoa());
	    	retorno.put("beneficiario.pessoa.nmPessoa",beneficiarioPessoa.getNmPessoa());
	    	
	    	String nrIdentificacaoBeneficiario = FormatUtils.formatIdentificacao(
	    			beneficiarioPessoa.getTpIdentificacao(),beneficiarioPessoa.getNrIdentificacao());
			retorno.put("beneficiario.pessoa.nrIdentificacaoFormatado",nrIdentificacaoBeneficiario);
		}
		
		ContaBancaria contaBancaria = rfc.getContaBancaria();
		if (contaBancaria != null) {
			AgenciaBancaria agenciaBancaria = contaBancaria.getAgenciaBancaria();
			Banco banco = agenciaBancaria.getBanco();
			retorno.put("contaBancaria.nrContaBancaria",contaBancaria.getNrContaBancaria());
			retorno.put("contaBancaria.dvContaBancaria",contaBancaria.getDvContaBancaria());
			retorno.put("agenciaBancaria.nrAgenciaBancaria",agenciaBancaria.getNrAgenciaBancaria());
			retorno.put("agenciaBancaria.nrDigito",agenciaBancaria.getNrDigito());
			retorno.put("banco.nrBanco",banco.getNrBanco());
		}
		
    	Pessoa motoristaPessoa = rfc.getMotorista().getPessoa();
    	retorno.put("idMotoristaComp",motoristaPessoa.getIdPessoa());
    	retorno.put("motorista.nmPessoa",motoristaPessoa.getNmPessoa());
    	
    	String nrIdentificacaoMotorista = FormatUtils.formatIdentificacao(
    			motoristaPessoa.getTpIdentificacao(),motoristaPessoa.getNrIdentificacao());
		retorno.put("motorista.nrIdentificacao",nrIdentificacaoMotorista);
    	
    	retorno.put("nrNfCarreteiro",rfc.getNrNfCarreteiro());
    	retorno.put("obReciboFreteCarreteiro",rfc.getObReciboFreteCarreteiro());
    	retorno.put("dhEmissao",rfc.getDhEmissao());
    	retorno.put("dtContabilizacao",rfc.getDtContabilizacao());
    	retorno.put("dtSugeridaPagto",rfc.getDtSugeridaPagto());
    	retorno.put("dtPagtoReal",rfc.getDtPagtoReal());
    	
    	if (rfc.getRelacaoPagamento() != null) {
    		retorno.put("relacaoPagamento.nrRelacaoPagamento",rfc.getRelacaoPagamento().getNrRelacaoPagamento());
    	}	
    	
    	MeioTransporte meioTransporte = rfc.getMeioTransporteRodoviario().getMeioTransporte();
		retorno.put("meioTransporte.nrFrota",meioTransporte.getNrFrota());
    	retorno.put("meioTranporte.nrIdentificador",meioTransporte.getNrIdentificador());
    	retorno.put("dsMarcaMeioTransporte",meioTransporte.getModeloMeioTransporte().getMarcaMeioTransporte().getDsMarcaMeioTransporte());
    	retorno.put("dsModeloMeioTransporte",meioTransporte.getModeloMeioTransporte().getDsModeloMeioTransporte());
    	
    	ControleCarga controleCarga = rfc.getControleCarga();
		if (controleCarga != null) {
    		retorno.put("controleCarga.nrControleCarga",controleCarga.getNrControleCarga());
        	retorno.put("controleCarga.idControleCarga",controleCarga.getIdControleCarga());
        	
        	Filial filialByIdFilialOrigem = controleCarga.getFilialByIdFilialOrigem();
			retorno.put("controleCarga.filialByIdFilialOrigem.idFilial",
        			filialByIdFilialOrigem.getIdFilial());
        	retorno.put("controleCarga.filialByIdFilialOrigem.sgFilial",
        			filialByIdFilialOrigem.getSgFilial());
        	retorno.put("controleCarga.filialByIdFilialOrigem.pessoa.nmFantasia",
        			filialByIdFilialOrigem.getPessoa().getNmFantasia());
        	retorno.put("controleCarga.dhEvento",reciboFreteCarreteiroService.findCCDhEvento(controleCarga.getIdControleCarga()));
        	
        	Filial filialByIdFilialDestino = controleCarga.getFilialByIdFilialDestino();
			if (filialByIdFilialDestino != null) {
        		retorno.put("controleCarga.filialByIdFilialDestino.sgFilial",
        				filialByIdFilialDestino.getSgFilial());
            	retorno.put("controleCarga.filialByIdFilialDestino.pessoa.nmFantasia",
            			filialByIdFilialDestino.getPessoa().getNmFantasia());
        	}
    	}
		retorno.put("dhGeracaoMovimento",rfc.getDhGeracaoMovimento());
		
		retorno.put("moedaPais.moeda.siglaSimbolo",rfc.getMoedaPais().getMoeda().getSiglaSimbolo());
    	
		if (rfc.getBlAdiantamento().equals(Boolean.TRUE)) {
			retorno.put("vlLiquido",rfc.getVlBruto());
			retorno.put("vlAdiantamento",rfc.getVlBruto());
		} else {	
			retorno.put("pcAliquotaInss",rfc.getPcAliquotaInss());
	    	retorno.put("pcAliquotaIssqn",rfc.getPcAliquotaIssqn());
	    	retorno.put("pcAliquotaIrrf",rfc.getPcAliquotaIrrf());
	    	
	    	retorno.put("vlSalarioContribuicao",rfc.getVlSalarioContribuicao());
	    	retorno.put("vlOutrasFontes",rfc.getVlOutrasFontes());
	    	retorno.put("vlApuradoInss",rfc.getVlInss());
	    	retorno.put("vlIssqn",rfc.getVlIssqn());
	    	retorno.put("vlIrrf",rfc.getVlIrrf());
	    	retorno.put("vlBruto",rfc.getVlBruto());
	    	retorno.put("vlPostoPassagem",rfc.getVlPostoPassagem());
	    	retorno.put("vlPremio",rfc.getVlPremio());
	    	retorno.put("vlDiaria",rfc.getVlDiaria());
	    	retorno.put("vlAdiantamento",reciboFreteCarreteiroService.findValorAdiantamento(rfc).setScale(2,BigDecimal.ROUND_DOWN));
	    	retorno.put("vlDesconto",rfc.getVlDesconto());
	    	retorno.put("vlLiquido",rfc.getVlLiquido());
		}    	
    	
    	return retorno;
    }
    
    /**
	 * Retorna consulta dos adiantamentos que est�o na terceira aba.
	 * @param criteria
	 * @return
	 */
	public List findGridAdiantamentos(TypedFlatMap criteria) {
    	return reciboFreteCarreteiroService.findGridAdiantamentos(criteria);
    }    
  
    public Serializable storeCustom(TypedFlatMap values) {
    	return reciboFreteCarreteiroService.storeCustom(values);
    }
    
    public TypedFlatMap storeCancelarRecibo(TypedFlatMap values) {    	
    	Long idReciboFreteCarreteiro = values.getLong("idReciboFreteCarreteiro");
    	List lComplementares = reciboFreteCarreteiroService.findRecibosComplementares(idReciboFreteCarreteiro);
    	if (lComplementares.size() > 0)
    		throw new BusinessException("LMS-24009");
    	
    	ReciboFreteCarreteiro rfc = reciboFreteCarreteiroService.storeCancelarRecibo(idReciboFreteCarreteiro);
    	TypedFlatMap retorno = new TypedFlatMap();
    	retorno.put("tpSituacaoRecibo.value",rfc.getTpSituacaoRecibo().getValue());
    	retorno.put("tpSituacaoRecibo.description",rfc.getTpSituacaoRecibo().getDescription());
    	return retorno;
    }
    
    public TypedFlatMap storeCancelarReciboAfterValidation(TypedFlatMap values) {
    	return reciboFreteCarreteiroService.storeCancelarReciboAfterValidation(values);
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
    
    public String execute(TypedFlatMap parameters) throws Exception {
    	Long idRecibo = parameters.getLong("idReciboFreteCarreteiro");
    	if (idRecibo == null)
    		throw new IllegalArgumentException("O m�todo execute exige um long 'idReciboFreteCarreteiro' nos parametros.");
    	
    	boolean blReemissao = reciboFreteCarreteiroService.storeValidateEmissaoReciboViagem(idRecibo);
    	parameters.put("blReemissao",Boolean.valueOf(blReemissao));
    	
    	Boolean blAdiantamento = reciboFreteCarreteiroService.validateIfBlAdiantamento(idRecibo);
    	parameters.put("blAdiantamento",blAdiantamento);
    	
    	return this.reportExecutionManager.generateReportLocator(this.emitirReciboService, parameters);
    }
    
	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public MeioTransporteRodoviarioService getMeioTransporteRodoviarioService() {
		return meioTransporteRodoviarioService;
	}
	public void setMeioTransporteRodoviarioService(
			MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
		this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
	}
	public ProprietarioService getProprietarioService() {
		return proprietarioService;
	}
	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}

	public void setReciboFreteCarreteiroService(ReciboFreteCarreteiroService reciboFreteCarreteiroService) {
		this.reciboFreteCarreteiroService = reciboFreteCarreteiroService;
	}

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setAdiantamentoTrechoService(AdiantamentoTrechoService adiantamentoTrechoService) {
		this.adiantamentoTrechoService = adiantamentoTrechoService;
	}
	
}
