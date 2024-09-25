package com.mercurio.lms.tributos.model.param;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.ObservacaoICMSPessoa;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ObservacaoICMSPessoaService;
import com.mercurio.lms.configuracoes.model.service.TipoTributacaoIEService;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tributos.model.ObservacaoICMS;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.tributos.model.TipoTributacaoIcms;
import com.mercurio.lms.tributos.model.service.ObservacaoICMSService;
import com.mercurio.lms.tributos.model.service.TipoTributacaoIcmsService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
/**
 * Classe utilitária usada no cálculo do ICMS. A classe é baseada na classe utilitária
 * CalculoFrete do cálculo de frete. Recebe valores da rotina e adiciona eles na classe 
 * CalculoFrete.
 * 
 * @author Mickaël Jalbert
 * @since 01/06/2006
 *
 */
public class CalcularIcmsParam {
	/**
	 * Objeto de input
	 * */
	private CalculoFrete calculoFrete;
	/**
	 * Propriedade de input
	 * */
	private YearMonthDay dtEmissao;
	private UnidadeFederativaService unidadeFederativaService;
	private UnidadeFederativa ufOrigem;
	private UnidadeFederativa ufDestino;
	private UnidadeFederativa ufDestinatario;
	private ClienteService clienteService;
	private Cliente remetente;
	private Cliente destinatario;
	private Cliente responsavel;
	private Cliente recebedor;
	private DomainValueService domainValueService;
	private InscricaoEstadualService inscricaoEstadualService;
	private InscricaoEstadual ieRemetente;
	private InscricaoEstadual ieDestinatario;
	private InscricaoEstadual ieRecebedor;
	private TipoTributacaoIEService tipoTributacaoIEService;
	private TipoTributacaoIE tipoTributacaoIERemetente;
	private TipoTributacaoIE tipoTributacaoIEDestinatario;
	private TipoTributacaoIE tipoTributacaoIEResponsavel;
	private TipoTributacaoIcmsService tipoTributacaoIcmsService;
	private TipoTributacaoIcms tipoTributacaoIcms;
	private ObservacaoICMSPessoaService observacaoICMSPessoaService;
	private List<ObservacaoICMSPessoa> observacaoICMSPessoaRemetente; 
	private ObservacaoICMSService observacaoICMSService;
	private List<ObservacaoICMS> observacaoICMSTipoTributacao; 	
	private Long idTipoTributacaoExcecao;
	private String tpSituacaoTributariaTomador; 
	private Long idUfDestino;


	/**
	 * Parametro temporário
	 * */
	private BigDecimal pcEmbute;
	private Boolean blAceitaSubstituicao;
	private Boolean blEmbuteIcmsParcelasIE;
	private Boolean blEmbuteIcmsParcelas;
	private Boolean blImpDadosCalcCtrc;
	private Boolean blImprimeMemoCalcCte;
	private BigDecimal vlParcelaPedagioSemIncidencia;
	private Boolean blExisteRemetente;
	private Boolean blExisteDestinatario;
	private Boolean blExisteRecebedor;
	private Boolean blExisteResponsavel;
	private BigDecimal vlTotalParcelasComIcms;
	private String observacao;

	public CalcularIcmsParam(CalculoFrete calculoFrete) {
		super();
		this.calculoFrete = calculoFrete;
	}	

	public CalcularIcmsParam(CalculoFrete calculoFrete,
			UnidadeFederativaService unidadeFederativaService,
			ClienteService clienteService,
			InscricaoEstadualService inscricaoEstadualService,
			TipoTributacaoIEService tipoTributacaoIEService,
			ObservacaoICMSPessoaService observacaoICMSPessoaService,
			ObservacaoICMSService observacaoICMSService,
			DomainValueService domainValueService,
			TipoTributacaoIcmsService tipoTributacaoIcmsService,
			ConfiguracoesFacade configuracoesFacade) {
		this.calculoFrete = calculoFrete;

		initializeValue();

		this.unidadeFederativaService = unidadeFederativaService;
		this.clienteService = clienteService;
		this.inscricaoEstadualService = inscricaoEstadualService;
		this.tipoTributacaoIEService = tipoTributacaoIEService;
		this.observacaoICMSPessoaService = observacaoICMSPessoaService;
		this.observacaoICMSService = observacaoICMSService;
		this.domainValueService = domainValueService;
		this.tipoTributacaoIcmsService = tipoTributacaoIcmsService;
	}

	private void initializeValue() {
		ImpostoServico impostoServico = new ImpostoServico();
		calculoFrete.setTributo(impostoServico);
		calculoFrete.setBlSubcontratacao(Boolean.FALSE);
		if (calculoFrete.getObservacoes() == null) {
			calculoFrete.setObservacoes(new ArrayList<String>());
		}

		vlParcelaPedagioSemIncidencia = BigDecimal.ZERO;
		setPcAliquota(BigDecimal.ZERO);
		setVlIcmsTotal(BigDecimal.ZERO);
		setVlBase(BigDecimal.ZERO);
		setVlRetencaoST(BigDecimal.ZERO);
		setPcRetencaoST(BigDecimal.ZERO);
		calculoFrete.setVlTotalServicosAdicionais(BigDecimal.ZERO);
		setVlTotalParcelasComIcms(BigDecimal.ZERO);
		setVlParcelaPedagioSemIncidencia(BigDecimal.ZERO);
		setBlIncidenciaIcmsPedagio(Boolean.FALSE);

		blExisteRemetente = getIdRemetente() != null;
		blExisteDestinatario = getIdDestinatario() != null;
		blExisteRecebedor = getIdRecebedor() != null;
		blExisteResponsavel = getIdResponsavel() != null;
	}

	public Boolean getBlRedespacho() {
		return calculoFrete.getBlRedespacho();
	}

	public String getCdCtrcContratante() {
		return this.getCalculoFrete().getDadosCliente().getNrCtrcSubcontratante();
	}

	public YearMonthDay getDtEmissao() {
		
		if(isRecalculoFrete()){
			dtEmissao = calculoFrete.getDhEmissaoDocRecalculo().toYearMonthDay();
		}else{
		if (dtEmissao == null) {
			dtEmissao = JTDateTimeUtils.getDataAtual();
		}
		}
		return dtEmissao;
	}

	public void setDtEmissao(YearMonthDay dtEmissao){
		this.dtEmissao = dtEmissao;
	}

	public Boolean isRecalculoFrete(){
		if(calculoFrete.getRecalculoFrete() != null && calculoFrete.getRecalculoFrete()){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public Long getIdDestinatario() {
		return calculoFrete.getDadosCliente().getIdClienteDestinatario();
	}

	public Long getIdIeDestinatario() {
		return calculoFrete.getDadosCliente().getIdInscricaoEstadualDestinatario();
	}

	public Long getIdIeRecebedor() {
		return calculoFrete.getDadosCliente().getIdInscricaoEstadualRecebedor();
	}

	public Long getIdIeRemetente() {
		return calculoFrete.getDadosCliente().getIdInscricaoEstadualRemetente();
	}

	public Long getIdRemetente() {
		return calculoFrete.getDadosCliente().getIdClienteRemetente();
	}

	public Long getIdRecebedor() {
		return calculoFrete.getDadosCliente().getIdClienteRecebedor();
	}

	public Long getIdResponsavel() {
		if (calculoFrete.getDoctoServico() != null
				&& calculoFrete.getDoctoServico().getDevedorDocServs()!= null  
				&& !calculoFrete.getDoctoServico().getDevedorDocServs().isEmpty()
				&& calculoFrete.getDoctoServico().getDevedorDocServs().get(0).getCliente() != null) {
			return calculoFrete.getDoctoServico().getDevedorDocServs().get(0).getCliente().getIdCliente();
		} else {
			return null;
	}
	}

	public Long getIdUfRemetente() {
		return calculoFrete.getDadosCliente().getIdUfRemetente();
	}	
	
	public Long getIdUfDestinatario() {
			return getIdUfDestino();
		}
		
	public Long getIdUfDestino() {
		if (idUfDestino == null) {
		/*Solicitado pelo Joelson 10/07/2009*/
			idUfDestino = unidadeFederativaService.findUFByIdMunicipio(
					calculoFrete.getRestricaoRotaDestino().getIdMunicipio()).getIdUnidadeFederativa();
	}
		return idUfDestino;
	}

	public Long getIdUfOrigem() {
		return calculoFrete.getRestricaoRotaOrigem().getIdUnidadeFederativa();
	}
	
	public Long getIdUfFilialDestino() {
		return calculoFrete.getDadosCliente().getIdUfDestinatario();
	}

	public Long getIdUfFilialOrigem() {
		return calculoFrete.getDoctoServico().getFilialByIdFilialOrigem()
			.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();
	}	

	public UnidadeFederativa getUfFilialOrigem() {
		return unidadeFederativaService.findById(getIdUfFilialOrigem());
	}	

	public Long getIdUfFilialRemetente() {
		return calculoFrete.getDadosCliente().getIdUfFilialRemetente();
	}	
	
	public BigDecimal getPcDesconto() {
		return calculoFrete.getPcDesconto();
	}

	public String getTpConhecimento() {
		return calculoFrete.getTpConhecimento();
	}

	public String getTpFrete() {
		return calculoFrete.getTpFrete();
	}

	public void setCalculoFrete(CalculoFrete calculoFrete) {
		this.calculoFrete = calculoFrete;
	}

	public CalculoFrete getCalculoFrete() {
		return calculoFrete;
	}

	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public UnidadeFederativa getUfOrigem() {
		if (ufOrigem == null) {
			ufOrigem = unidadeFederativaService.findById(getIdUfOrigem());
		}
		return ufOrigem;
	}

	public UnidadeFederativa getUfDestino() {
		if (ufDestino == null) {
			ufDestino = unidadeFederativaService.findById(getIdUfDestino());
		}
		return ufDestino;
	}

	public UnidadeFederativa getUfDestinatario() {
		if (ufDestinatario == null) {
			ufDestinatario = unidadeFederativaService.findById(getIdUfDestinatario());
		}				
		return ufDestinatario;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}	

	public Cliente getRemetente() {
		if (remetente == null) {
			if (blExisteRemetente) {
				remetente = clienteService.findByIdComPessoa(getIdRemetente());
			}
		}
		return remetente;
	}

	public Cliente getDestinatario() {
		if (destinatario == null) {
			if (blExisteDestinatario) {
				destinatario = clienteService.findByIdComPessoa(getIdDestinatario());
			}
		}
		return destinatario;
	}

	public Pais getPaisDestinatario(){
		Pais pais = null;
		if(getDestinatario() != null){
			try {
				pais = getDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getPais();			
			} catch (LazyInitializationException e) {
				if(!Hibernate.isInitialized(getDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa())){
					UnidadeFederativa unidadeFederativa = unidadeFederativaService.findByIdPessoa(getDestinatario().getPessoa().getIdPessoa());
					if(unidadeFederativa != null){
						pais = unidadeFederativa.getPais();
		}
				}
			}
			
			return pais;			
		}
		return null;
	}	
	
	public Cliente getResponsavel() {
		if (responsavel == null) {
			responsavel = clienteService.findByIdComPessoa(getIdResponsavel());
		}	
		return responsavel;
	}

	public Cliente getRecebedor() {
		if (recebedor == null && blExisteRecebedor) {
			recebedor = clienteService.findByIdComPessoa(getIdRecebedor());
		}	
		return recebedor;
	}

	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

	public InscricaoEstadual getIeRemetente() {
		if (ieRemetente == null) {
			ieRemetente = inscricaoEstadualService.findById(getIdIeRemetente());
		}		
		return ieRemetente;
	}

	public InscricaoEstadual getIeDestinatario() {
		if (ieDestinatario == null) {
			ieDestinatario = inscricaoEstadualService.findById(getIdIeDestinatario());
		}		
		return ieDestinatario;
	}

	public InscricaoEstadual getIeRecebedor() {
		if (ieRecebedor == null) {
			ieRecebedor = inscricaoEstadualService.findById(getIdIeRecebedor());
		}		
		return ieRecebedor;
	}

	public void setTipoTributacaoIEService(TipoTributacaoIEService tipoTributacaoIEService) {
		this.tipoTributacaoIEService = tipoTributacaoIEService;
	}

	public TipoTributacaoIE getTipoTributacaoIERemetente() {
		if (tipoTributacaoIERemetente == null && blExisteRemetente) {
			tipoTributacaoIERemetente = tipoTributacaoIEService.findTiposTributacaoIEVigente(getIdIeRemetente(), getDtEmissao());
			// Caso não encontre um tipo de tributação vigente para a inscrição estadual, lança a exceção.
			if (tipoTributacaoIERemetente == null) {
				throw new BusinessException("LMS-23028", new Object[]{"remetente"});
			}
		}
		return tipoTributacaoIERemetente;
	}

	public TipoTributacaoIE getTipoTributacaoIEDestinatario() {
		if (tipoTributacaoIEDestinatario == null&& blExisteDestinatario) {
			tipoTributacaoIEDestinatario = tipoTributacaoIEService.findTiposTributacaoIEVigente(getIdIeDestinatario(), getDtEmissao());
			// Caso não encontre um tipo de tributação vigente para a inscrição estadual, lança a exceção.
			if (tipoTributacaoIEDestinatario == null) {
				throw new BusinessException("LMS-23028", new Object[]{"destinatario"});
			}
		}
		return tipoTributacaoIEDestinatario;
	}

	//LMS-3914
	public TipoTributacaoIE getTipoTributacaoIEResponsavel() {
		if (tipoTributacaoIEResponsavel == null && blExisteResponsavel) {
			InscricaoEstadual getIeResponsavel = inscricaoEstadualService.findByPessoaIndicadorPadrao(getIdResponsavel(), true); 
			
			tipoTributacaoIEResponsavel = tipoTributacaoIEService.findTiposTributacaoIEVigente(getIeResponsavel.getIdInscricaoEstadual(), getDtEmissao());
			// Caso não encontre um tipo de tributação vigente para a inscrição estadual, lança a exceção.
			if (tipoTributacaoIEResponsavel == null) {
				throw new BusinessException("LMS-23028", new Object[]{"responsavel"});
			}
		}
		return tipoTributacaoIEResponsavel;
	}

	public void setIdTipoTributacaoIcms(Long idTipoTributacaoIcms) {
		this.calculoFrete.setIdTipoTributacaoICMS(idTipoTributacaoIcms);
	}

	public Long getIdTipoTributacaoIcms() {
		return this.calculoFrete.getIdTipoTributacaoICMS();
	}

	public TipoTributacaoIcms getTipoTributacaoIcms() {
		if (tipoTributacaoIcms == null && getIdTipoTributacaoIcms() != null) {
			this.tipoTributacaoIcms = tipoTributacaoIcmsService.findById(getIdTipoTributacaoIcms());
		}
		return tipoTributacaoIcms;
	}

	public void addObIcms(List<String> obIcms) {
		if (obIcms != null && !obIcms.isEmpty()) {
			calculoFrete.getObservacoes().addAll(obIcms);
		}
	}

	public void addObIcms(String obIcms) {
		calculoFrete.getObservacoes().add(obIcms);
	}		

	public void setObIcms(List<String> obIcms) {
		calculoFrete.setObservacoes(obIcms);
	}

	public void clearObIcms() {
		calculoFrete.clearObservacoes();
	}
		
	public void addEmbLegaisMastersaf(List<String> embLegaisMastersaf) {
		if (embLegaisMastersaf != null && !embLegaisMastersaf.isEmpty()) {
			calculoFrete.getEmbLegaisMastersaf().addAll(embLegaisMastersaf);
		}
	}
	public void clearEmbLegaisMastersaf() {
		calculoFrete.clearEmbLegaisMastersaf();
	}
	public void addEmbLegalMastersaf(String embLegalMastersaf) {
		calculoFrete.getEmbLegaisMastersaf().add(embLegalMastersaf);
	}		

	public void setEmbLegaisMastersaf(List<String> embLegaisMastersaf) {
		calculoFrete.setEmbLegaisMastersaf(embLegaisMastersaf);
	}

	public void setVlDevido(BigDecimal vlDevido) {
		calculoFrete.setVlDevido(vlDevido);
	}
	
	public BigDecimal getVlDevido() {
		return calculoFrete.getVlDevido();
	}	
	
	

	public void setObservacaoICMSPessoaService(ObservacaoICMSPessoaService observacaoICMSPessoaService) {
		this.observacaoICMSPessoaService = observacaoICMSPessoaService;
	}	
	
	public List<String> getObservacaoICMSPessoaRemetente(String tpObservacaoIcms) {
		List<String> lstObservacao = new ArrayList<String>();

		observacaoICMSPessoaRemetente = observacaoICMSPessoaService.findVigenteByIe(getIdIeRemetente(), getDtEmissao(), tpObservacaoIcms);
		if (!observacaoICMSPessoaRemetente.isEmpty()) {
			for(ObservacaoICMSPessoa observacaoICMSPessoa : observacaoICMSPessoaRemetente) {
				lstObservacao.add(observacaoICMSPessoa.getObObservacaoICMSPessoa());
			}
		}

		return lstObservacao;
	}

	public void addAllEmbLegalMastersafByObservacaoICMSPessoaRemetente() {
		if (!observacaoICMSPessoaRemetente.isEmpty()) {
			for(ObservacaoICMSPessoa observacaoICMSPessoa : observacaoICMSPessoaRemetente) {
				//Incrementa lista do EmbLegalMastersaf
				addEmbLegalMastersaf(observacaoICMSPessoa.getCdEmbLegalMastersaf());
			}
		}
	}

	public void setObservacaoICMSService(ObservacaoICMSService observacaoICMSService) {
		this.observacaoICMSService = observacaoICMSService;
	}	

	public List<String> getObservacaoICMSTipoTributacao(String tpObservacaoIcms) {
		return this.getObservacaoICMSTipoTributacao(getIdUfFilialOrigem(), tpObservacaoIcms, Boolean.TRUE);
	}
	public List<String> getObservacaoICMSTipoTributacao(String tpObservacaoIcms, Boolean blValidateResult) {
		return this.getObservacaoICMSTipoTributacao(getIdUfFilialOrigem(), tpObservacaoIcms, blValidateResult);
	}
	public List<String> getObservacaoICMSTipoTributacao(Long idUfFilialOrigem, String tpObservacaoIcms, Boolean blValidateResult) {
		List<String> lstObservacao = new ArrayList<String>();

		observacaoICMSTipoTributacao = observacaoICMSService.findVigenteByTipoTributacao(idUfFilialOrigem, getIdTipoTributacaoIcms(), tpObservacaoIcms, getDtEmissao());
		if(observacaoICMSTipoTributacao != null && !observacaoICMSTipoTributacao.isEmpty()) {
			for(ObservacaoICMS observacaoICMS : observacaoICMSTipoTributacao) {
				lstObservacao.add(observacaoICMS.getObObservacaoICMS());
			}
		} else if (Boolean.TRUE.equals(blValidateResult)){
				throw new BusinessException("LMS-23025", new Object[]{getUfOrigem().getSgUnidadeFederativa(), getTipoTributacaoIcms().getDsTipoTributacaoIcms(), domainValueService.findDomainValueByValue("DM_TIPO_OBSERVACAO_ICMS", tpObservacaoIcms).getDescription().getValue()});
			}
		return lstObservacao;
	}

	public void addAllEmbLegalMastersafByObservacaoICMSTipoTributacao() {
		if(observacaoICMSTipoTributacao != null
				&& !observacaoICMSTipoTributacao.isEmpty()
		) {
			for(ObservacaoICMS observacaoICMS : observacaoICMSTipoTributacao) {
				//Incrementa lista do EmbLegalMastersaf
				addEmbLegalMastersaf(observacaoICMS.getCdEmbLegalMastersaf());
			}
		}
	}
	
	public BigDecimal getPcAliquota() {
		return calculoFrete.getTributo().getPcAliquota();
	}

	public void setPcAliquota(BigDecimal pcAliquota) {
		this.calculoFrete.getTributo().setPcAliquota(pcAliquota);
	}

	public BigDecimal getPcEmbute() {
		return pcEmbute;
	}

	public void setPcEmbute(BigDecimal pcEmbute) {
		this.pcEmbute = pcEmbute;
	}

	public Boolean getBlAceitaSubstituicao() {
		return blAceitaSubstituicao;
	}

	public void setBlAceitaSubstituicao(Boolean blAceitaSubstituicao) {
		this.blAceitaSubstituicao = blAceitaSubstituicao;
	}

	public Boolean getBlEmbuteIcmsParcelas() {
		return blEmbuteIcmsParcelas;
	}

	public void setBlEmbuteIcmsParcelas(Boolean blEmbuteIcmsParcelas) {
		this.blEmbuteIcmsParcelas = blEmbuteIcmsParcelas;
	}

	public Boolean getBlImpDadosCalcCtrc() {
		return blImpDadosCalcCtrc;
	}

	public void setBlImpDadosCalcCtrc(Boolean blImpDadosCalcCtrc) {
		this.blImpDadosCalcCtrc = blImpDadosCalcCtrc;
	}

	public Boolean getBlImprimeMemoCalcCte() {
		return blImprimeMemoCalcCte;
	}

	public void setBlImprimeMemoCalcCte(Boolean blImprimeMemoCalcCte) {
		this.blImprimeMemoCalcCte = blImprimeMemoCalcCte;
	}

	public BigDecimal getPcRetencaoST() {
		return calculoFrete.getPcRetensaoSituacaoTributaria();
	}

	public void setPcRetencaoST(BigDecimal pcRetencaoST) {
		this.calculoFrete.setPcRetensaoSituacaoTributaria(pcRetencaoST);
	}

	public Boolean getBlIncidenciaIcmsPedagio() {
		return calculoFrete.getBlIncideIcmsPedagio();
	}

	public void setBlIncidenciaIcmsPedagio(Boolean blIncidenciaIcmsPedagio) {
		this.calculoFrete.setBlIncideIcmsPedagio(blIncidenciaIcmsPedagio);
	}

	public BigDecimal getVlParcelaPedagioSemIncidencia() {
		return vlParcelaPedagioSemIncidencia;
	}

	public void setVlParcelaPedagioSemIncidencia(BigDecimal vlParcelaPedagioSemIncidencia) {
		this.vlParcelaPedagioSemIncidencia = vlParcelaPedagioSemIncidencia;
	}
	
	public void addVlParcelaPedagioSemIncidencia(BigDecimal vlParcelaPedagioSemIncidencia) {
		this.vlParcelaPedagioSemIncidencia = this.vlParcelaPedagioSemIncidencia.add(BigDecimalUtils.defaultBigDecimal(vlParcelaPedagioSemIncidencia));
	}


	public BigDecimal getVlBase() {
		return calculoFrete.getTributo().getVlBaseCalculo();
	}


	public void setVlBase(BigDecimal vlBase) {
		this.calculoFrete.getTributo().setVlBaseCalculo(vlBase);
	}


	public BigDecimal getVlTotalParcelas() {
		return this.calculoFrete.getVlTotalParcelas();
	}


	public void setVlTotalParcelas(BigDecimal vlTotalParcelas) {
		this.calculoFrete.setVlTotalParcelas(vlTotalParcelas);
	}	

	public void addVlTotalParcelas(BigDecimal vlTotalParcelas) {
		if (calculoFrete.getVlTotalParcelas() == null) {
			calculoFrete.setVlTotalParcelas(BigDecimal.ZERO);
		}
		setVlTotalParcelas(calculoFrete.getVlTotalParcelas().add(BigDecimalUtils.defaultBigDecimal(vlTotalParcelas)));
	}

	public BigDecimal getVlDesconto() {
		return calculoFrete.getVlDesconto();
	}


	public void setVlDesconto(BigDecimal vlDesconto) {
		this.calculoFrete.setVlDesconto(vlDesconto);
	}


	public BigDecimal getVlIcmsTotal() {
		return calculoFrete.getTributo().getVlImposto();
	}


	public void setVlIcmsTotal(BigDecimal vlIcmsTotal) {
		this.calculoFrete.getTributo().setVlImposto(vlIcmsTotal);
	}


	public BigDecimal getVlTotalServicosAdicionais() {
		return calculoFrete.getVlTotalServicosAdicionais();
	}


	public void setVlTotalServicosAdicionais(BigDecimal vlTotalServicosAdicionais) {
		this.calculoFrete.setVlTotalServicosAdicionais(vlTotalServicosAdicionais);
	}
	
	public void addVlTotalServicosAdicionais(BigDecimal vlTotalServicosAdicionais) {
		this.calculoFrete.setVlTotalServicosAdicionais(this.calculoFrete.getVlTotalServicosAdicionais().add(vlTotalServicosAdicionais));
	}


	public BigDecimal getVlRetencaoST() {
		return calculoFrete.getVlRetensaoSituacaoTributaria();
	}


	public void setVlRetencaoST(BigDecimal vlRetencaoST) {
		this.calculoFrete.setVlRetensaoSituacaoTributaria(vlRetencaoST);
	}


	public String getTpSituacaoTributariaRemetente() {
		if (blExisteRemetente && calculoFrete.getDadosCliente().getTpSituacaoTributariaRemetente() == null) {
			return getTipoTributacaoIERemetente().getTpSituacaoTributaria().getValue();
		} else {
			return calculoFrete.getDadosCliente().getTpSituacaoTributariaRemetente();
		}
	}
	
	public void setTpSituacaoTributariaRemetente(String tpSituacaoTributariaRemetente) {
		calculoFrete.getDadosCliente().setTpSituacaoTributariaRemetente(tpSituacaoTributariaRemetente);
	}	
	
	public String getTpSituacaoTributariaDestinatario() {
		if (blExisteDestinatario && calculoFrete.getDadosCliente().getTpSituacaoTributariaDestinatario() == null) {
			return getTipoTributacaoIEDestinatario().getTpSituacaoTributaria().getValue();			
		} else {
			return calculoFrete.getDadosCliente().getTpSituacaoTributariaDestinatario();
		}
	}
	
	public String getTpSituacaoTributariaResponsavel() {
		//LMS-3914
		if (blExisteResponsavel && calculoFrete.getDadosCliente().getTpSituacaoTributariaResponsavel() == null) {
			return getTipoTributacaoIEResponsavel().getTpSituacaoTributaria().getValue();
		} else {
			return calculoFrete.getDadosCliente().getTpSituacaoTributariaResponsavel();
		}
	}

	public String getTpSituacaoTributariaRecebedor() {
		if (blExisteRecebedor) {
			return calculoFrete.getDadosCliente().getTpSituacaoTributariaRecebedor();
		} else {
			return null;
		}
	}

	public Boolean getBlExisteDestinatario() {
		return blExisteDestinatario;
	}

	public Boolean getBlExisteRemetente() {
		return blExisteRemetente;
	}

	public Boolean getBlExisteResponsavel() {
		return blExisteResponsavel;
	}

	public Boolean getBlEmbuteIcmsParcelasIE() {
		return blEmbuteIcmsParcelasIE;
	}

	public void setBlEmbuteIcmsParcelasIE(Boolean blEmbuteIcmsParcelasIE) {
		this.blEmbuteIcmsParcelasIE = blEmbuteIcmsParcelasIE;
	}
	
	/**
	 * Informa se é uma cotação
	 */
	public Boolean getBlCotacao() {
		return (getCalculoFrete().getTpCalculo().equals(ConstantesExpedicao.CALCULO_COTACAO) || getCalculoFrete().getIdCotacao() != null);
	}

	public BigDecimal getVlTotalParcelasComIcms() {
		return vlTotalParcelasComIcms;
	}

	public void setVlTotalParcelasComIcms(BigDecimal vlTotalParcelasComIcms) {
		this.vlTotalParcelasComIcms = vlTotalParcelasComIcms;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Boolean getBlCtrcCotMac() {
		if(calculoFrete.getDoctoServico() == null 
				|| calculoFrete.getDoctoServico().getTpCtrcParceria() == null){
			return Boolean.FALSE;			
		}
		return "M".equals(calculoFrete.getDoctoServico().getTpCtrcParceria().getValue())  ;
	}

	public Long getIdTipoTributacaoExcecao() {
		return idTipoTributacaoExcecao;
	}

	public void setIdTipoTributacaoExcecao(Long idTipoTributacaoExcecao) {
		this.idTipoTributacaoExcecao = idTipoTributacaoExcecao;
	}

	public String getTpSituacaoTributariaTomador() {
		return tpSituacaoTributariaTomador;
	}

	public void setTpSituacaoTributariaTomador(String tpSituacaoTributariaTomador) {
		this.tpSituacaoTributariaTomador = tpSituacaoTributariaTomador;
	}

	public BigDecimal getVlComplementoIcms() {
		return calculoFrete.getVlComplementoIcms();
	}

}