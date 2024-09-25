package com.mercurio.lms.coleta.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoColeta;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class PedidoColeta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPedidoColeta;

    /** persistent field */
    private Long nrColeta;

    /** persistent field */
    private String nrDddCliente;
    
    /** persistent field */
    private String nrTelefoneCliente; 

    /** persistent field */
    private String nrCep;

    /** persistent field */
    private DateTime dhPedidoColeta;

    /** persistent field */
    private DateTime dhColetaDisponivel;

    /** persistent field */
    private YearMonthDay dtPrevisaoColeta;

    /** persistent field */
    private TimeOfDay hrLimiteColeta;

    /** persistent field */
    private DomainValue tpModoPedidoColeta;

    /** persistent field */
    private DomainValue tpPedidoColeta;

    /** persistent field */
    private DomainValue tpStatusColeta;

    /** persistent field */
    private String edColeta;

    /** persistent field */
    private String dsBairro;

    /** persistent field */
    private String nmSolicitante;

    /** persistent field */
    private String nmContatoCliente;

    /** persistent field */
    private BigDecimal vlTotalInformado;

    /** persistent field */
    private BigDecimal vlTotalVerificado;

    /** persistent field */
    private Integer qtTotalVolumesInformado;

    /** persistent field */
    private Integer qtTotalVolumesVerificado;

    /** persistent field */
    private BigDecimal psTotalInformado;

    /** persistent field */
    private BigDecimal psTotalVerificado;
    
    /** persistent field */
    private BigDecimal psTotalAforadoInformado;

    /** persistent field */
    private BigDecimal psTotalAforadoVerificado;    

    /** persistent field */
    private Boolean blClienteLiberadoManual;

    /** persistent field */
    private Boolean blAlteradoPosProgramacao;

    /** nullable persistent field */
    private String nrEndereco;

    /** nullable persistent field */
    private String dsComplementoEndereco;

    /** nullable persistent field */
    private String obPedidoColeta;
    
    /** nullable persistent field */
    private String cdColetaCliente;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.coleta.model.MilkRun milkRun;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cotacao cotacao;

    /** persistent field */
    private com.mercurio.lms.coleta.model.ManifestoColeta manifestoColeta;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialSolicitante;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialResponsavel;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaIntervaloCep rotaIntervaloCep;
    
    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;    
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoa;    

    /** persistent field */
    @SuppressWarnings("rawtypes")
	private List eventoColetas;

    /** persistent field */
    @SuppressWarnings("rawtypes")
	private List preAlertas;

    /** persistent field */
    @SuppressWarnings("rawtypes")
	private List servicoAdicionalColetas;

    /** persistent field */
    @SuppressWarnings("rawtypes")
	private List<PedidoColetaProduto> pedidoColetaProdutos;
    
    /** persistent field */
    @SuppressWarnings("rawtypes")
	private List detalheColetas;
    
    /** persistent field */
    private Integer versao;

	/** persistent field */
	private DomainValue situacaoAprovacao;

	/** persistent field */
	private Boolean blProdutoDiferenciado;
	
	/** persistent field */
	private Pendencia pendencia;
	
	/** persistent field */
	private String dsInfColeta;
	
	/** persistent field */
	private List<NotaCreditoColeta> notaCreditoColetas;

	/** persistent field */
	private Boolean blConfirmacaoVol;
	
	/** persistent field */
	private DateTime dhConfirmacaoVol;
	
	// LMSA-6786
	private Boolean blIntegracaoFedex;
	private DateTime dhIntegracaoFedex;
	
	/** persistent field */
    private Boolean blProdutoPerigoso;
    
    /** persistent field */
    private Boolean blProdutoControlado;

    private String nmClienteIntegracao;

	public PedidoColeta(){
		setBlConfirmacaoVol(false);
	}

    @SuppressWarnings("rawtypes")
	public PedidoColeta(Long idPedidoColeta, Long nrColeta, String nrDddCliente, String nrTelefoneCliente, String nrCep, DateTime dhPedidoColeta, DateTime dhColetaDisponivel, YearMonthDay dtPrevisaoColeta, TimeOfDay hrLimiteColeta, DomainValue tpModoPedidoColeta, DomainValue tpPedidoColeta, DomainValue tpStatusColeta, String edColeta, String dsBairro, String nmSolicitante, String nmContatoCliente, BigDecimal vlTotalInformado, BigDecimal vlTotalVerificado, Integer qtTotalVolumesInformado, Integer qtTotalVolumesVerificado, BigDecimal psTotalInformado, BigDecimal psTotalVerificado, BigDecimal psTotalAforadoInformado, BigDecimal psTotalAforadoVerificado, Boolean blClienteLiberadoManual, Boolean blAlteradoPosProgramacao, String nrEndereco, String dsComplementoEndereco, String obPedidoColeta, String cdColetaCliente, Municipio municipio, Usuario usuario, Moeda moeda, MilkRun milkRun, Cotacao cotacao, ManifestoColeta manifestoColeta, RotaColetaEntrega rotaColetaEntrega, Filial filialByIdFilialSolicitante, Filial filialByIdFilialResponsavel, RotaIntervaloCep rotaIntervaloCep, Cliente cliente, EnderecoPessoa enderecoPessoa, List eventoColetas, List preAlertas, List servicoAdicionalColetas, List pedidoColetaProdutos, List detalheColetas, Integer versao, DomainValue situacaoAprovacao, Boolean blProdutoDiferenciado, Pendencia pendencia, String dsInfColeta, List<NotaCreditoColeta> notaCreditoColetas, Boolean blConfirmacaoVol, DateTime dhConfirmacaoVol) {
        this.idPedidoColeta = idPedidoColeta;
        this.nrColeta = nrColeta;
        this.nrDddCliente = nrDddCliente;
        this.nrTelefoneCliente = nrTelefoneCliente;
        this.nrCep = nrCep;
        this.dhPedidoColeta = dhPedidoColeta;
        this.dhColetaDisponivel = dhColetaDisponivel;
        this.dtPrevisaoColeta = dtPrevisaoColeta;
        this.hrLimiteColeta = hrLimiteColeta;
        this.tpModoPedidoColeta = tpModoPedidoColeta;
        this.tpPedidoColeta = tpPedidoColeta;
        this.tpStatusColeta = tpStatusColeta;
        this.edColeta = edColeta;
        this.dsBairro = dsBairro;
        this.nmSolicitante = nmSolicitante;
        this.nmContatoCliente = nmContatoCliente;
        this.vlTotalInformado = vlTotalInformado;
        this.vlTotalVerificado = vlTotalVerificado;
        this.qtTotalVolumesInformado = qtTotalVolumesInformado;
        this.qtTotalVolumesVerificado = qtTotalVolumesVerificado;
        this.psTotalInformado = psTotalInformado;
        this.psTotalVerificado = psTotalVerificado;
        this.psTotalAforadoInformado = psTotalAforadoInformado;
        this.psTotalAforadoVerificado = psTotalAforadoVerificado;
        this.blClienteLiberadoManual = blClienteLiberadoManual;
        this.blAlteradoPosProgramacao = blAlteradoPosProgramacao;
        this.nrEndereco = nrEndereco;
        this.dsComplementoEndereco = dsComplementoEndereco;
        this.obPedidoColeta = obPedidoColeta;
        this.cdColetaCliente = cdColetaCliente;
        this.municipio = municipio;
        this.usuario = usuario;
        this.moeda = moeda;
        this.milkRun = milkRun;
        this.cotacao = cotacao;
        this.manifestoColeta = manifestoColeta;
        this.rotaColetaEntrega = rotaColetaEntrega;
        this.filialByIdFilialSolicitante = filialByIdFilialSolicitante;
        this.filialByIdFilialResponsavel = filialByIdFilialResponsavel;
        this.rotaIntervaloCep = rotaIntervaloCep;
        this.cliente = cliente;
        this.enderecoPessoa = enderecoPessoa;
        this.eventoColetas = eventoColetas;
        this.preAlertas = preAlertas;
        this.servicoAdicionalColetas = servicoAdicionalColetas;
        this.pedidoColetaProdutos = pedidoColetaProdutos;
        this.detalheColetas = detalheColetas;
        this.versao = versao;
        this.situacaoAprovacao = situacaoAprovacao;
        this.blProdutoDiferenciado = blProdutoDiferenciado;
        this.pendencia = pendencia;
        this.dsInfColeta = dsInfColeta;
        this.notaCreditoColetas = notaCreditoColetas;
        this.blConfirmacaoVol = blConfirmacaoVol;
        this.dhConfirmacaoVol = dhConfirmacaoVol;
    }

    public Boolean getBlProdutoDiferenciado() {
		return blProdutoDiferenciado;
	}

	public void setBlProdutoDiferenciado(Boolean blProdutoDiferenciado) {
		this.blProdutoDiferenciado = blProdutoDiferenciado;
	}	

	public DomainValue getSituacaoAprovacao() {
		return situacaoAprovacao;
	}
	
	public void setSituacaoAprovacao(DomainValue situacaoAprovacao) {
		this.situacaoAprovacao = situacaoAprovacao;
	}
	
    public Long getIdPedidoColeta() {
        return this.idPedidoColeta;
    }

    public void setIdPedidoColeta(Long idPedidoColeta) {
        this.idPedidoColeta = idPedidoColeta;
    }

    public Long getNrColeta() {
        return this.nrColeta;
    }

    public void setNrColeta(Long nrColeta) {
        this.nrColeta = nrColeta;
    }

    public String getNrDddCliente() {
        return this.nrDddCliente;
    }

    public void setNrDddCliente(String nrDddCliente) {
        this.nrDddCliente = nrDddCliente;
    }    
    
    public String getNrTelefoneCliente() {
        return this.nrTelefoneCliente;
    }

    public void setNrTelefoneCliente(String nrTelefoneCliente) {
        this.nrTelefoneCliente = nrTelefoneCliente;
    }

    public String getNrCep() {
        return this.nrCep;
    }

    public void setNrCep(String nrCep) {
        this.nrCep = nrCep;
    }

    public DateTime getDhColetaDisponivel() {
		return dhColetaDisponivel;
	}

	public void setDhColetaDisponivel(DateTime dhColetaDisponivel) {
		this.dhColetaDisponivel = dhColetaDisponivel;
	}

	public DateTime getDhPedidoColeta() {
		return dhPedidoColeta;
	}

	public void setDhPedidoColeta(DateTime dhPedidoColeta) {
		this.dhPedidoColeta = dhPedidoColeta;
	}

	public YearMonthDay getDtPrevisaoColeta() {
		return dtPrevisaoColeta;
	}

	public void setDtPrevisaoColeta(YearMonthDay dtPrevisaoColeta) {
		this.dtPrevisaoColeta = dtPrevisaoColeta;
	}

	public TimeOfDay getHrLimiteColeta() {
		return hrLimiteColeta;
	}

	public void setHrLimiteColeta(TimeOfDay hrLimiteColeta) {
		this.hrLimiteColeta = hrLimiteColeta;
	}

	public DomainValue getTpModoPedidoColeta() {
        return this.tpModoPedidoColeta;
    }

    public void setTpModoPedidoColeta(DomainValue tpModoPedidoColeta) {
        this.tpModoPedidoColeta = tpModoPedidoColeta;
    }

    public DomainValue getTpPedidoColeta() {
    	
        return this.tpPedidoColeta;
    }

    public void setTpPedidoColeta(DomainValue tpPedidoColeta) {
        this.tpPedidoColeta = tpPedidoColeta;
    }

    public DomainValue getTpStatusColeta() {
        return this.tpStatusColeta;
    }

    public void setTpStatusColeta(DomainValue tpStatusColeta) {
        this.tpStatusColeta = tpStatusColeta;
    }

    public String getEdColeta() {
        return this.edColeta;
    }

    public void setEdColeta(String edColeta) {
        this.edColeta = edColeta;
    }

    public String getDsBairro() {
        return this.dsBairro;
    }

    public void setDsBairro(String dsBairro) {
        this.dsBairro = dsBairro;
    }

    public String getNmSolicitante() {
        return this.nmSolicitante;
    }

    public void setNmSolicitante(String nmSolicitante) {
        this.nmSolicitante = nmSolicitante;
    }

    public String getNmContatoCliente() {
        return this.nmContatoCliente;
    }

    public void setNmContatoCliente(String nmContatoCliente) {
        this.nmContatoCliente = nmContatoCliente;
    }

    public BigDecimal getVlTotalInformado() {
        return this.vlTotalInformado;
    }

    public void setVlTotalInformado(BigDecimal vlTotalInformado) {
        this.vlTotalInformado = vlTotalInformado;
    }

    public BigDecimal getVlTotalVerificado() {
        return this.vlTotalVerificado;
    }

    public void setVlTotalVerificado(BigDecimal vlTotalVerificado) {
        this.vlTotalVerificado = vlTotalVerificado;
    }

    public Integer getQtTotalVolumesInformado() {
        return this.qtTotalVolumesInformado;
    }

    public void setQtTotalVolumesInformado(Integer qtTotalVolumesInformado) {
        this.qtTotalVolumesInformado = qtTotalVolumesInformado;
    }

    public Integer getQtTotalVolumesVerificado() {
        return this.qtTotalVolumesVerificado;
    }

    public void setQtTotalVolumesVerificado(Integer qtTotalVolumesVerificado) {
        this.qtTotalVolumesVerificado = qtTotalVolumesVerificado;
    }

    public BigDecimal getPsTotalInformado() {
        return this.psTotalInformado;
    }

    public void setPsTotalInformado(BigDecimal psTotalInformado) {
        this.psTotalInformado = psTotalInformado;
    }

    public BigDecimal getPsTotalVerificado() {
        return this.psTotalVerificado;
    }

    public void setPsTotalVerificado(BigDecimal psTotalVerificado) {
        this.psTotalVerificado = psTotalVerificado;
    }

    public BigDecimal getPsTotalAforadoInformado() {
        return this.psTotalAforadoInformado;
    }

    public void setPsTotalAforadoInformado(BigDecimal psTotalAforadoInformado) {
        this.psTotalAforadoInformado = psTotalAforadoInformado;
    }    
    
    public BigDecimal getPsTotalAforadoVerificado() {
        return this.psTotalAforadoVerificado;
    }

    public void setPsTotalAforadoVerificado(BigDecimal psTotalAforadoVerificado) {
        this.psTotalAforadoVerificado = psTotalAforadoVerificado;
    }    

    public Boolean getBlClienteLiberadoManual() {
        return this.blClienteLiberadoManual;
    }

    public void setBlClienteLiberadoManual(Boolean blClienteLiberadoManualment) {
        this.blClienteLiberadoManual = blClienteLiberadoManualment;
    }

    public Boolean getBlAlteradoPosProgramacao() {
        return this.blAlteradoPosProgramacao;
    }

    public void setBlAlteradoPosProgramacao(Boolean blAlteradoPosProgramacao) {
        this.blAlteradoPosProgramacao = blAlteradoPosProgramacao;
    }

    public String getNrEndereco() {
        return this.nrEndereco;
    }

    public void setNrEndereco(String nrEndereco) {
        this.nrEndereco = nrEndereco;
    }

    public String getDsComplementoEndereco() {
        return this.dsComplementoEndereco;
    }

    public void setDsComplementoEndereco(String dsComplementoEndereco) {
        this.dsComplementoEndereco = dsComplementoEndereco;
    }

    public String getObPedidoColeta() {
        return this.obPedidoColeta;
    }

    public void setObPedidoColeta(String obPedidoColeta) {
        this.obPedidoColeta = obPedidoColeta;
    }
        
    public String getCdColetaCliente() {
		return cdColetaCliente;
	}

	public void setCdColetaCliente(String cdColetaCliente) {
		this.cdColetaCliente = cdColetaCliente;
	}

	public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.coleta.model.MilkRun getMilkRun() {
        return this.milkRun;
    }

    public void setMilkRun(com.mercurio.lms.coleta.model.MilkRun milkRun) {
        this.milkRun = milkRun;
    }

    public com.mercurio.lms.vendas.model.Cotacao getCotacao() {
        return this.cotacao;
    }

    public void setCotacao(com.mercurio.lms.vendas.model.Cotacao cotacao) {
        this.cotacao = cotacao;
    }

    public com.mercurio.lms.coleta.model.ManifestoColeta getManifestoColeta() {
        return this.manifestoColeta;
    }

	public void setManifestoColeta(
			com.mercurio.lms.coleta.model.ManifestoColeta manifestoColeta) {
        this.manifestoColeta = manifestoColeta;
    }

    public com.mercurio.lms.municipios.model.RotaColetaEntrega getRotaColetaEntrega() {
        return this.rotaColetaEntrega;
    }

	public void setRotaColetaEntrega(
			com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega) {
        this.rotaColetaEntrega = rotaColetaEntrega;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialSolicitante() {
        return this.filialByIdFilialSolicitante;
    }

	public void setFilialByIdFilialSolicitante(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialSolicitante) {
        this.filialByIdFilialSolicitante = filialByIdFilialSolicitante;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialResponsavel() {
        return this.filialByIdFilialResponsavel;
    }

	public void setFilialByIdFilialResponsavel(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialResponsavel) {
        this.filialByIdFilialResponsavel = filialByIdFilialResponsavel;
    }

    @SuppressWarnings("rawtypes")
	@ParametrizedAttribute(type = com.mercurio.lms.coleta.model.EventoColeta.class)     
    public List getEventoColetas() {
        return this.eventoColetas;
    }

    @SuppressWarnings("rawtypes")
	public void setEventoColetas(List eventoColetas) {
        this.eventoColetas = eventoColetas;
    }

    @SuppressWarnings("rawtypes")
	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.PreAlerta.class)     
    public List getPreAlertas() {
        return this.preAlertas;
    }

    @SuppressWarnings("rawtypes")
	public void setPreAlertas(List preAlertas) {
        this.preAlertas = preAlertas;
    }

    @SuppressWarnings("rawtypes")
	@ParametrizedAttribute(type = com.mercurio.lms.coleta.model.ServicoAdicionalColeta.class)     
    public List getServicoAdicionalColetas() {
        return this.servicoAdicionalColetas;
    }

    @SuppressWarnings("rawtypes")
	public void setServicoAdicionalColetas(List servicoAdicionalColetas) {
        this.servicoAdicionalColetas = servicoAdicionalColetas;
    }

    @SuppressWarnings("rawtypes")
	@ParametrizedAttribute(type = com.mercurio.lms.coleta.model.PedidoColetaProduto.class)
	public List<PedidoColetaProduto> getPedidoColetaProdutos() {
		return this.pedidoColetaProdutos;
	}

	@SuppressWarnings("rawtypes")
	public void setPedidoColetaProdutos(List<PedidoColetaProduto> pedidoColetaProdutos) {
		this.pedidoColetaProdutos = pedidoColetaProdutos;
	}
    
    @SuppressWarnings("rawtypes")
	@ParametrizedAttribute(type = com.mercurio.lms.coleta.model.DetalheColeta.class)     
    public List getDetalheColetas() {
        return this.detalheColetas;
    }

    @SuppressWarnings("rawtypes")
	public void setDetalheColetas(List detalheColetas) {
        this.detalheColetas = detalheColetas;
    }
    
	public com.mercurio.lms.municipios.model.RotaIntervaloCep getRotaIntervaloCep() {
		return rotaIntervaloCep;
	}

	public void setRotaIntervaloCep(
			com.mercurio.lms.municipios.model.RotaIntervaloCep rotaIntervaloCep) {
		this.rotaIntervaloCep = rotaIntervaloCep;
	}

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }
    
	public com.mercurio.lms.configuracoes.model.EnderecoPessoa getEnderecoPessoa() {
		return enderecoPessoa;
	}

	public void setEnderecoPessoa(
			com.mercurio.lms.configuracoes.model.EnderecoPessoa enderecoPessoa) {
		this.enderecoPessoa = enderecoPessoa;
	}	
	
	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}    

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}
	
    public List<NotaCreditoColeta> getNotaCreditoColetas() {
        return notaCreditoColetas;
    }

    public void setNotaCreditoColetas(List<NotaCreditoColeta> notaCreditoColetas) {
        this.notaCreditoColetas = notaCreditoColetas;
    }

    public String getNmClienteIntegracao() {
        return nmClienteIntegracao;
    }

    public void setNmClienteIntegracao(String nmClienteIntegracao) {
        this.nmClienteIntegracao = nmClienteIntegracao;
    }

    @Override
    public String toString() {
		return new ToStringBuilder(this).append("idPedidoColeta",
				getIdPedidoColeta()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_PEDIDO_COLETA", idPedidoColeta)
				.append("ID_FILIAL_SOLICITANTE", filialByIdFilialSolicitante != null ? filialByIdFilialSolicitante.getIdFilial() : null)
				.append("ID_FILIAL_RESPONSAVEL", filialByIdFilialResponsavel != null ? filialByIdFilialResponsavel.getIdFilial() : null)
				.append("ID_CLIENTE", cliente != null ? cliente.getIdCliente() : null)
				.append("ID_MUNICIPIO", municipio != null ? municipio.getIdMunicipio() : null)
				.append("ID_USUARIO", usuario != null ? usuario.getIdUsuario() : null)
				.append("ID_MOEDA", moeda != null ? moeda.getIdMoeda() : null)
				.append("NR_COLETA", nrColeta)
				.append("NR_DDD_CLIENTE", nrDddCliente)
				.append("NR_TELEFONE_CLIENTE", nrTelefoneCliente)
				.append("NR_CEP", nrCep)
				.append("DH_PEDIDO_COLETA", dhPedidoColeta)
				.append("DH_COLETA_DISPONIVEL", dhColetaDisponivel)
				.append("DT_PREVISAO_COLETA", dtPrevisaoColeta)
				.append("HR_LIMITE_COLETA", hrLimiteColeta)
				.append("TP_MODO_PEDIDO_COLETA", tpModoPedidoColeta != null ? tpModoPedidoColeta.getValue() : null)
				.append("TP_PEDIDO_COLETA", tpPedidoColeta != null ? tpPedidoColeta.getValue() : null)
				.append("TP_STATUS_COLETA", tpStatusColeta != null ? tpStatusColeta.getValue() : null)
				.append("ED_COLETA", edColeta)
				.append("DS_BAIRRO", dsBairro)
				.append("NM_SOLICITANTE", nmSolicitante)
				.append("NM_CONTATO_CLIENTE", nmContatoCliente)
				.append("NR_VERSAO", versao)
				.append("VL_TOTAL_INFORMADO", vlTotalInformado)
				.append("VL_TOTAL_VERIFICADO", vlTotalVerificado)
				.append("QT_TOTAL_VOLUMES_INFORMADO", qtTotalVolumesInformado)
				.append("QT_TOTAL_VOLUMES_VERIFICADO", qtTotalVolumesVerificado)
				.append("PS_TOTAL_INFORMADO", psTotalInformado)
				.append("PS_TOTAL_VERIFICADO", psTotalVerificado)
				.append("PS_TOTAL_AFORADO_INFORMADO", psTotalAforadoInformado)
				.append("PS_TOTAL_AFORADO_VERIFICADO", psTotalAforadoVerificado)
				.append("BL_CLIENTE_LIBERADO_MANUAL", blClienteLiberadoManual)
				.append("BL_ALTERADO_POS_PROGRAMACAO", blAlteradoPosProgramacao)
				.append("ID_COTACAO", cotacao != null ? cotacao.getIdCotacao() : null)
				.append("ID_MILK_RUN", milkRun != null ? milkRun.getIdMilkRun() : null)
				.append("ID_MANIFESTO_COLETA", manifestoColeta != null ? manifestoColeta.getIdManifestoColeta() : null)
				.append("ID_ROTA_COLETA_ENTREGA", rotaColetaEntrega != null ? rotaColetaEntrega.getIdRotaColetaEntrega() : null)
				.append("ID_ROTA_INTERVALO_CEP", rotaIntervaloCep != null ? rotaIntervaloCep.getIdRotaIntervaloCep() : null)
				.append("NR_ENDERECO", nrEndereco)
				.append("DS_COMPLEMENTO_ENDERECO", dsComplementoEndereco)
				.append("OB_PEDIDO_COLETA", this.obPedidoColeta)
				.append("ID_ENDERECO_PESSOA", enderecoPessoa != null ? enderecoPessoa.getIdEnderecoPessoa() : null)
				.append("BL_PRODUTO_DIFERENCIADO", blProdutoDiferenciado)
				.append("TP_SITUACAO_APROVACAO", situacaoAprovacao != null ? situacaoAprovacao.getValue() : null)
				.append("ID_PENDENCIA", pendencia != null ? pendencia.getIdPendencia() : null)
				.append("DS_INF_COLETA", dsInfColeta)
				.append("BL_CONFIRMACAO_VOL", blConfirmacaoVol)
				.append("DH_CONFIRMACAO_VOL", dhConfirmacaoVol)
				.toString();
	}

	@Override
    public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof PedidoColeta))
			return false;
        PedidoColeta castOther = (PedidoColeta) other;
		return new EqualsBuilder().append(this.getIdPedidoColeta(),
				castOther.getIdPedidoColeta()).isEquals();
    }

	@Override
    public int hashCode() {
		return new HashCodeBuilder().append(getIdPedidoColeta()).toHashCode();
    }

    /**
     * Monta o endereço do PedidoColeta (Endereço, número e complemento). 
	 * 
     * @param bean
     * @return String 
     */
    public String getEnderecoComComplemento() {
		return PedidoColeta.getEnderecoComComplemento(edColeta, nrEndereco,
				dsComplementoEndereco, dsBairro);
    }
    
	public static String getEnderecoComComplemento(String edColeta,
			String nrEndereco, String dsComplementoEndereco, String dsBairro) {
        StringBuffer sb = new StringBuffer();
        if (!StringUtils.isBlank(edColeta)) {
            sb.append(edColeta);
        }
        
        if (!StringUtils.isBlank(nrEndereco)) {
        	sb.append(", ");
            sb.append(nrEndereco);
        }
        
        if (!StringUtils.isBlank(dsComplementoEndereco)) {
        	sb.append(", ");
            sb.append(dsComplementoEndereco);
        }

        if (!StringUtils.isBlank(dsBairro)) {
        	sb.append(" - ");
            sb.append(dsBairro);
        }
        return sb.toString();
    }

	public String getDsInfColeta() {
		return dsInfColeta;
	}

	public void setDsInfColeta(String dsInfoColeta) {
		this.dsInfColeta = dsInfoColeta;
	}

	public Boolean getBlConfirmacaoVol() {
		return blConfirmacaoVol;
	}

	public void setBlConfirmacaoVol(Boolean blConfirmacaoVol) {
		this.blConfirmacaoVol = blConfirmacaoVol;
	}

	public DateTime getDhConfirmacaoVol() {
		return dhConfirmacaoVol;
	}

	public void setDhConfirmacaoVol(DateTime dhConfirmacaoVol) {
		this.dhConfirmacaoVol = dhConfirmacaoVol;
	}

	public Boolean getBlIntegracaoFedex() {
		return blIntegracaoFedex;
	}

	public void setBlIntegracaoFedex(Boolean blIntegracaoFedex) {
		this.blIntegracaoFedex = blIntegracaoFedex;
	}

	public DateTime getDhIntegracaoFedex() {
		return dhIntegracaoFedex;
	}

	public void setDhIntegracaoFedex(DateTime dhIntegracaoFedex) {
		this.dhIntegracaoFedex = dhIntegracaoFedex;
	}

	public Boolean getBlProdutoPerigoso() {
		return blProdutoPerigoso;
	}

	public void setBlProdutoPerigoso(Boolean blProdutoPerigoso) {
		this.blProdutoPerigoso = blProdutoPerigoso;
	}

    public Boolean getBlProdutoControlado() {
        return blProdutoControlado;
    }

    public void setBlProdutoControlado(Boolean blProdutoControlado) {
        this.blProdutoControlado = blProdutoControlado;
    }

    public void addDetalheColetaeta(DetalheColeta detalheColeta){
        detalheColeta.setPedidoColeta(this);
        if(this.detalheColetas == null){
            this.detalheColetas = new ArrayList();
        }
        this.detalheColetas.add(detalheColeta);
    }

    public void addEventoColetaeta(EventoColeta eventoColeta){
        eventoColeta.setPedidoColeta(this);
        if(this.eventoColetas == null){
            this.eventoColetas = new ArrayList();
        }
        this.eventoColetas.add(eventoColeta);
    }
	
}