package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.workflow.model.Pendencia;

@Entity
@Table(name = "DESCONTO_RFC")
@SequenceGenerator(name = "DESCONTO_RFC_SQ", sequenceName = "DESCONTO_RFC_SQ", allocationSize = 1)
public class DescontoRfc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DESCONTO_RFC_SQ")
	@Column(name = "ID_DESCONTO_RFC", nullable = false)
	private Long idDescontoRfc;
	
	@Column(name="NR_DESCONTO_RFC", nullable = false, length = 10)
	private Long nrDescontoRfc;
		
	@Column(name = "NR_RECIBO_INDENIZACAO", length = 6)
	private Long nrReciboIndenizacao;
	
	@Column(name = "NR_IDENTIFICACAO_SEMI_REBOQUE", length = 25)
	private String nrIdentificacaoSemiReboque;	
	
	@Column(name = "OB_DESCONTO", length = 500)
	private String obDesconto;
	
	@Column(name = "VL_TOTAL_DESCONTO")
	private BigDecimal vlTotalDesconto;
	
	@Column(name = "VL_SALDO_DEVEDOR")
	private BigDecimal vlSaldoDevedor;
		
	@Column(name = "DT_INICIO_DESCONTO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtInicioDesconto;
			
	@Column(name = "QT_PARCELAS", length = 6)
	private Integer qtParcelas;
	
	@Column(name = "PC_DESCONTO")
	private BigDecimal pcDesconto;
	
	@Column(name = "QT_DIAS", length = 6)
	private Integer qtDias;
	
	@Column(name = "VL_FIXO_PARCELA")
	private BigDecimal vlFixoParcela;
	
	@Column(name = "BL_PARCELADO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blParcelado;
	
	@Column(name = "TP_SITUACAO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_STATUS_DESCONTO_RFC") })
	private DomainValue tpSituacao;
	
	@Column(name = "TP_OPERACAO", length = 2, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_TIPO_OPERACAO_PROPRIETARIO") })
	private DomainValue tpOperacao;
	
	@Column(name = "TP_SITUACAO_PENDENCIA", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_STATUS_WORKFLOW") })
	private DomainValue tpSituacaoPendencia;
		
	@Column(name = "DT_ATUALIZACAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtAtualizacao;
	
	@ManyToOne
	@JoinColumn(name = "ID_FILIAL", nullable = false)
	private Filial filial;	

	@ManyToOne
	@JoinColumn(name = "ID_TIPO_DESCONTO_RFC", nullable = false)
	private TipoDescontoRfc tipoDescontoRfc;
	
	@ManyToOne
	@JoinColumn(name = "ID_PROPRIETARIO", nullable = false)
	private Proprietario proprietario;
	
	@ManyToOne
	@JoinColumn(name = "ID_CONTROLE_CARGA")
	private ControleCarga controleCarga;
	
	@ManyToOne
	@JoinColumn(name = "ID_MEIO_TRANSPORTE")
	private MeioTransporte meioTransporte;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private UsuarioLMS usuario;
		
	@ManyToOne
	@JoinColumn(name = "ID_PENDENCIA")
	private Pendencia pendencia;
	
	@ManyToOne
	@JoinColumn(name = "ID_RECIBO_FRETE_CARRETEIRO")
	private ReciboFreteCarreteiro reciboFreteCarreteiro;	
	
	@OneToMany(mappedBy="descontoRfc", fetch=FetchType.LAZY, cascade=CascadeType.ALL)	
	private List<ParcelaDescontoRfc> listParcelaDescontoRfc;
	
	@Column(name = "BL_PRIORITARIO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_SIM_NAO") })
	private DomainValue blPrioritario;

	public Long getIdDescontoRfc() {
		return idDescontoRfc;
	}

	public void setIdDescontoRfc(Long idDescontoRfc) {
		this.idDescontoRfc = idDescontoRfc;
	}

	public Long getNrDescontoRfc() {
		return nrDescontoRfc;
	}

	public void setNrDescontoRfc(Long nrDescontoRfc) {
		this.nrDescontoRfc = nrDescontoRfc;
	}

	public Long getNrReciboIndenizacao() {
		return nrReciboIndenizacao;
	}

	public void setNrReciboIndenizacao(Long nrReciboIndenizacao) {
		this.nrReciboIndenizacao = nrReciboIndenizacao;
	}

	public String getNrIdentificacaoSemiReboque() {
		return nrIdentificacaoSemiReboque;
	}

	public void setNrIdentificacaoSemiReboque(String nrIdentificacaoSemiReboque) {
		this.nrIdentificacaoSemiReboque = nrIdentificacaoSemiReboque;
	}

	public String getObDesconto() {
		return obDesconto;
	}

	public void setObDesconto(String obDesconto) {
		this.obDesconto = obDesconto;
	}

	public BigDecimal getVlTotalDesconto() {
		return vlTotalDesconto;
	}

	public void setVlTotalDesconto(BigDecimal vlTotalDesconto) {
		this.vlTotalDesconto = vlTotalDesconto;
	}

	public BigDecimal getVlSaldoDevedor() {
		return vlSaldoDevedor;
	}

	public void setVlSaldoDevedor(BigDecimal vlSaldoDevedor) {
		this.vlSaldoDevedor = vlSaldoDevedor;
	}

	public YearMonthDay getDtInicioDesconto() {
		return dtInicioDesconto;
	}

	public void setDtInicioDesconto(YearMonthDay dtInicioDesconto) {
		this.dtInicioDesconto = dtInicioDesconto;
	}

	public Integer getQtParcelas() {
		return qtParcelas;
	}

	public void setQtParcelas(Integer qtParcelas) {
		this.qtParcelas = qtParcelas;
	}

	public BigDecimal getPcDesconto() {
		return pcDesconto;
	}

	public void setPcDesconto(BigDecimal pcDesconto) {
		this.pcDesconto = pcDesconto;
	}

	public Integer getQtDias() {
		return qtDias;
	}

	public void setQtDias(Integer qtDias) {
		this.qtDias = qtDias;
	}

	public BigDecimal getVlFixoParcela() {
		return vlFixoParcela;
	}

	public void setVlFixoParcela(BigDecimal vlFixoParcela) {
		this.vlFixoParcela = vlFixoParcela;
	}

	public DomainValue getBlParcelado() {
		return blParcelado;
	}

	public void setBlParcelado(DomainValue blParcelado) {
		this.blParcelado = blParcelado;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public DomainValue getTpOperacao() {
		return tpOperacao;
	}

	public void setTpOperacao(DomainValue tpOperacao) {
		this.tpOperacao = tpOperacao;
	}

	public DomainValue getTpSituacaoPendencia() {
		return tpSituacaoPendencia;
	}

	public void setTpSituacaoPendencia(DomainValue tpSituacaoPendencia) {
		this.tpSituacaoPendencia = tpSituacaoPendencia;
	}

	public YearMonthDay getDtAtualizacao() {
		return dtAtualizacao;
	}

	public void setDtAtualizacao(YearMonthDay dtAtualizacao) {
		this.dtAtualizacao = dtAtualizacao;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public TipoDescontoRfc getTipoDescontoRfc() {
		return tipoDescontoRfc;
	}

	public void setTipoDescontoRfc(TipoDescontoRfc tipoDescontoRfc) {
		this.tipoDescontoRfc = tipoDescontoRfc;
	}

	public Proprietario getProprietario() {
		return proprietario;
	}

	public void setProprietario(Proprietario proprietario) {
		this.proprietario = proprietario;
	}

	public ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public MeioTransporte getMeioTransporte() {
		return meioTransporte;
	}

	public void setMeioTransporte(MeioTransporte meioTransporte) {
		this.meioTransporte = meioTransporte;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public ReciboFreteCarreteiro getReciboFreteCarreteiro() {
		return reciboFreteCarreteiro;
	}

	public void setReciboFreteCarreteiro(ReciboFreteCarreteiro reciboFreteCarreteiro) {
		this.reciboFreteCarreteiro = reciboFreteCarreteiro;
	}

	public List<ParcelaDescontoRfc> getListParcelaDescontoRfc() {
		return listParcelaDescontoRfc;
	}

	public void setListParcelaDescontoRfc(
			List<ParcelaDescontoRfc> listParcelaDescontoRfc) {
		this.listParcelaDescontoRfc = listParcelaDescontoRfc;
	}
	
	public DomainValue getBlPrioritario() {
		return blPrioritario;
	}
	
	public void setBlPrioritario(DomainValue blPrioritario) {
		this.blPrioritario = blPrioritario;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idDescontoRfc == null) ? 0 : idDescontoRfc.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		
		if (!(obj instanceof DescontoRfc)){
			return false;
		}
		
		DescontoRfc castOther = (DescontoRfc) obj;
		
		return new EqualsBuilder().append(
				this.getIdDescontoRfc(), 
				castOther.getIdDescontoRfc()).isEquals();
	}
}