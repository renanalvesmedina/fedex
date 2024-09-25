package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "COMISSAO_PROVISIONADA")
@SequenceGenerator(name = "COMISSAO_PROVISIONADA_SEQ", sequenceName = "COMISSAO_PROVISIONADA_SQ", allocationSize=1)
public class ComissaoProvisionada implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMISSAO_PROVISIONADA_SEQ")
	@Column(name = "ID_COMISSAO_PROVISIONADA", nullable = false)
	private Long idComissaoProvisionada;
	
	@ManyToOne
	@JoinColumn(name = "ID_TERRITORIO")
	private Territorio territorio;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private UsuarioLMS usuario;

	@ManyToOne
	@JoinColumn(name = "ID_CLIENTE")
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name = "ID_TERRITORIO_REMETENTE")
	private Territorio territorioRemetente;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_REMETENTE")
	private UsuarioLMS usuarioRemetente;
	
	@ManyToOne
	@JoinColumn(name = "ID_CLIENTE_REMETENTE")
	private Cliente clienteRemetente;
	
	@ManyToOne
	@JoinColumn(name = "ID_COMISSAO_GARANTIDA")
	private ComissaoGarantida comissaoGarantida;
	
	@ManyToOne
	@JoinColumn(name = "ID_CLIENTE_DESTINATARIO")
	private Cliente clienteDestinatario;
	
	@ManyToOne
	@JoinColumn(name = "ID_COMISSAO_CONQUISTA")
	private ComissaoConquista comissaoConquista;
	
	@Column(name = "NR_DOC_CONHECIMENTO")
	private BigDecimal nrDocConhecimento;
	
	@Column(name = "VL_DOC_RECEITA_LQD")
	private BigDecimal vlDocReceitaLqd;
	
	@Column(name = "DT_DOC_EMISSAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtDocEmissao;
	
	@Column(name = "DT_DOC_PAGAMENTO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtDocPagamento;
	
	@Column(name = "DT_ULTIMO_EMBARQUE")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtUltimoEmbarque;
	
	@Column(name = "TP_COMISSAO", length = 1, nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_COMISSAO") })
	private DomainValue tpComissao;
	
	@Column(name = "VL_COMISSAO_BASE")
	private BigDecimal vlComissaoBase;
	
	@Column(name = "VL_COMISSAO_PERCENTUAL")
	private BigDecimal vlComissaoPercentual;
	
	@Column(name = "VL_COMISSAO_CALCULADA")
	private BigDecimal vlComissaoCalculada;
	
	@Column(name = "VL_CARGO_LIMITE")
	private BigDecimal vlCargoLimite;
	
	@Column(name = "VL_CARGO_TOTAL_PAGAR")
	private BigDecimal vlCargoTotalPagar;
	
	@Column(name = "VL_CARGO_BONUS")
	private BigDecimal vlCargoBonus;
	
	@Column(name = "NR_DOC_NF_SERVICO")
	private BigDecimal nrDocNfServico;
	
	@Column(name = "NM_DOC_SERV_INELEGIVEL")
	private String nmDocServInelegivel;
	
	@Column(name = "DS_DOC_SRV_ADICIONAL")
	private String dsDocSrvAdicional;
	
	@Column(name = "TP_DOC_SERV", length = 1, nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_DOCUMENTO_SERVICO") })
	private DomainValue tpDocServ;
	
	@Column(name = "NM_DOC_SERV_COMISSAO_PAGA")
	private String nmDocServComissaoPaga;
	
	@Column(name = "NM_DOC_SERV_CANCELADO")
	private String nmDocServCancelado;
	
	@Column(name = "NM_DOC_SIT_CONHECIMENTO")
	private String nmDocSitConhecimento;
	
	@Column(name = "TP_DOC_FRETE")
	private String tpDocFrete;
	
	@Column(name = "VL_GNRDL")
	private BigDecimal vlGnrdl;
	
	@Column(name = "NM_GNRDL_PRCL_PRECO")
	private String nmGnrdlPrclPreco;
	
	@Column(name = "DS_GNRDL")
	private String dsGnrdl;
	
	@Column(name = "TP_GNRDL_SITUACAO")
	private String tpGnrdlSituacao;
	
	@Column(name = "NM_SITUACAO_COMISSAO")
	private String nmSituacaoComissao;
	
	@Column(name = "NR_MES_CALCULO")
	private Integer nrMesCalculo;
	
	@Column(name = "NR_ANO_CALCULO")
	private Integer nrAnoCalculo;
	
	@Column(name = "DT_EXECUCAO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtExecucao;
	
	@Column(name = "DS_AVISOS")
	private String dsAvisos;
	
	@Column(name = "DS_INCONSISTENCIAS")
	private String dsInconsistencias;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_INCLUSAO")
	private UsuarioLMS usuarioInclusao;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO_ALTERACAO")
	private UsuarioLMS usuarioAlteracao;

	@Columns(columns = { @Column(name = "DH_INCLUSAO"), @Column(name = "DH_INCLUSAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	@Columns(columns = { @Column(name = "DH_ALTERACAO"), @Column(name = "DH_ALTERACAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;

	public Territorio getTerritorio() {
		return territorio;
	}

	public void setTerritorio(Territorio territorio) {
		this.territorio = territorio;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Territorio getTerritorioRemetente() {
		return territorioRemetente;
	}

	public void setTerritorioRemetente(Territorio territorioRemetente) {
		this.territorioRemetente = territorioRemetente;
	}

	public UsuarioLMS getUsuarioRemetente() {
		return usuarioRemetente;
	}

	public void setUsuarioRemetente(UsuarioLMS usuarioRemetente) {
		this.usuarioRemetente = usuarioRemetente;
	}

	public Cliente getClienteRemetente() {
		return clienteRemetente;
	}

	public void setClienteRemetente(Cliente clienteRemetente) {
		this.clienteRemetente = clienteRemetente;
	}

	public ComissaoGarantida getComissaoGarantida() {
		return comissaoGarantida;
	}

	public void setComissaoGarantida(ComissaoGarantida comissaoGarantida) {
		this.comissaoGarantida = comissaoGarantida;
	}

	public Cliente getClienteDestinatario() {
		return clienteDestinatario;
	}

	public void setClienteDestinatario(Cliente clienteDestinatario) {
		this.clienteDestinatario = clienteDestinatario;
	}

	public ComissaoConquista getComissaoConquista() {
		return comissaoConquista;
	}

	public void setComissaoConquista(ComissaoConquista comissaoConquista) {
		this.comissaoConquista = comissaoConquista;
	}

	public BigDecimal getNrDocConhecimento() {
		return nrDocConhecimento;
	}

	public void setNrDocConhecimento(BigDecimal nrDocConhecimento) {
		this.nrDocConhecimento = nrDocConhecimento;
	}

	public BigDecimal getVlDocReceitaLqd() {
		return vlDocReceitaLqd;
	}

	public void setVlDocReceitaLqd(BigDecimal vlDocReceitaLqd) {
		this.vlDocReceitaLqd = vlDocReceitaLqd;
	}

	public YearMonthDay getDtDocEmissao() {
		return dtDocEmissao;
	}

	public void setDtDocEmissao(YearMonthDay dtDocEmissao) {
		this.dtDocEmissao = dtDocEmissao;
	}

	public YearMonthDay getDtDocPagamento() {
		return dtDocPagamento;
	}

	public void setDtDocPagamento(YearMonthDay dtDocPagamento) {
		this.dtDocPagamento = dtDocPagamento;
	}

	public YearMonthDay getDtUltimoEmbarque() {
		return dtUltimoEmbarque;
	}

	public void setDtUltimoEmbarque(YearMonthDay dtUltimoEmbarque) {
		this.dtUltimoEmbarque = dtUltimoEmbarque;
	}

	public DomainValue getTpComissao() {
		return tpComissao;
	}

	public void setTpComissao(DomainValue tpComissao) {
		this.tpComissao = tpComissao;
	}

	public BigDecimal getVlComissaoBase() {
		return vlComissaoBase;
	}

	public void setVlComissaoBase(BigDecimal vlComissaoBase) {
		this.vlComissaoBase = vlComissaoBase;
	}

	public BigDecimal getVlComissaoPercentual() {
		return vlComissaoPercentual;
	}

	public void setVlComissaoPercentual(BigDecimal vlComissaoPercentual) {
		this.vlComissaoPercentual = vlComissaoPercentual;
	}

	public BigDecimal getVlComissaoCalculada() {
		return vlComissaoCalculada;
	}

	public void setVlComissaoCalculada(BigDecimal vlComissaoCalculada) {
		this.vlComissaoCalculada = vlComissaoCalculada;
	}

	public BigDecimal getVlCargoLimite() {
		return vlCargoLimite;
	}

	public void setVlCargoLimite(BigDecimal vlCargoLimite) {
		this.vlCargoLimite = vlCargoLimite;
	}

	public BigDecimal getVlCargoTotalPagar() {
		return vlCargoTotalPagar;
	}

	public void setVlCargoTotalPagar(BigDecimal vlCargoTotalPagar) {
		this.vlCargoTotalPagar = vlCargoTotalPagar;
	}

	public BigDecimal getVlCargoBonus() {
		return vlCargoBonus;
	}

	public void setVlCargoBonus(BigDecimal vlCargoBonus) {
		this.vlCargoBonus = vlCargoBonus;
	}

	public BigDecimal getNrDocNfServico() {
		return nrDocNfServico;
	}

	public void setNrDocNfServico(BigDecimal nrDocNfServico) {
		this.nrDocNfServico = nrDocNfServico;
	}

	public String getNmDocServInelegivel() {
		return nmDocServInelegivel;
	}

	public void setNmDocServInelegivel(String nmDocServInelegivel) {
		this.nmDocServInelegivel = nmDocServInelegivel;
	}

	public String getDsDocSrvAdicional() {
		return dsDocSrvAdicional;
	}

	public void setDsDocSrvAdicional(String dsDocSrvAdicional) {
		this.dsDocSrvAdicional = dsDocSrvAdicional;
	}

	public DomainValue getTpDocServ() {
		return tpDocServ;
	}

	public void setTpDocServ(DomainValue tpDocServ) {
		this.tpDocServ = tpDocServ;
	}

	public String getNmDocServComissaoPaga() {
		return nmDocServComissaoPaga;
	}

	public void setNmDocServComissaoPaga(String nmDocServComissaoPaga) {
		this.nmDocServComissaoPaga = nmDocServComissaoPaga;
	}

	public String getNmDocServCancelado() {
		return nmDocServCancelado;
	}

	public void setNmDocServCancelado(String nmDocServCancelado) {
		this.nmDocServCancelado = nmDocServCancelado;
	}

	public String getNmDocSitConhecimento() {
		return nmDocSitConhecimento;
	}

	public void setNmDocSitConhecimento(String nmDocSitConhecimento) {
		this.nmDocSitConhecimento = nmDocSitConhecimento;
	}

	public String getTpDocFrete() {
		return tpDocFrete;
	}

	public void setTpDocFrete(String tpDocFrete) {
		this.tpDocFrete = tpDocFrete;
	}

	public BigDecimal getVlGnrdl() {
		return vlGnrdl;
	}

	public void setVlGnrdl(BigDecimal vlGnrdl) {
		this.vlGnrdl = vlGnrdl;
	}

	public String getNmGnrdlPrclPreco() {
		return nmGnrdlPrclPreco;
	}

	public void setNmGnrdlPrclPreco(String nmGnrdlPrclPreco) {
		this.nmGnrdlPrclPreco = nmGnrdlPrclPreco;
	}

	public String getDsGnrdl() {
		return dsGnrdl;
	}

	public void setDsGnrdl(String dsGnrdl) {
		this.dsGnrdl = dsGnrdl;
	}

	public String getTpGnrdlSituacao() {
		return tpGnrdlSituacao;
	}

	public void setTpGnrdlSituacao(String tpGnrdlSituacao) {
		this.tpGnrdlSituacao = tpGnrdlSituacao;
	}

	public String getNmSituacaoComissao() {
		return nmSituacaoComissao;
	}

	public void setNmSituacaoComissao(String nmSituacaoComissao) {
		this.nmSituacaoComissao = nmSituacaoComissao;
	}

	public Integer getNrMesCalculo() {
		return nrMesCalculo;
	}

	public void setNrMesCalculo(Integer nrMesCalculo) {
		this.nrMesCalculo = nrMesCalculo;
	}

	public Integer getNrAnoCalculo() {
		return nrAnoCalculo;
	}

	public void setNrAnoCalculo(Integer nrAnoCalculo) {
		this.nrAnoCalculo = nrAnoCalculo;
	}

	public YearMonthDay getDtExecucao() {
		return dtExecucao;
	}

	public void setDtExecucao(YearMonthDay dtExecucao) {
		this.dtExecucao = dtExecucao;
	}

	public String getDsAvisos() {
		return dsAvisos;
	}

	public void setDsAvisos(String dsAvisos) {
		this.dsAvisos = dsAvisos;
	}

	public String getDsInconsistencias() {
		return dsInconsistencias;
	}

	public void setDsInconsistencias(String dsInconsistencias) {
		this.dsInconsistencias = dsInconsistencias;
	}

	public UsuarioLMS getUsuarioInclusao() {
		return usuarioInclusao;
	}

	public void setUsuarioInclusao(UsuarioLMS usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

	public UsuarioLMS getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(UsuarioLMS usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}

	public Long getIdComissaoProvisionada() {
		return idComissaoProvisionada;
	}
	
	public void setIdComissaoProvisionada(Long idComissaoProvisionada) {
		this.idComissaoProvisionada = idComissaoProvisionada;
	}
}
