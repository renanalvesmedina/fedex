package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;

@Entity
@Table(name = "LIBERACAO_NOTA_NATURA")
@SequenceGenerator(name = "LIBERACAO_NOTA_NATURA_SQ", sequenceName = "LIBERACAO_NOTA_NATURA_SQ")
public class LiberacaoNotaNatura implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LIBERACAO_NOTA_NATURA_SQ")
	@Column(name = "ID_LIBERACAO_NOTA_NATURA", nullable = false)
	private Long idLiberacaoNotaNatura;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE", nullable = false)
	private Cliente cliente;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_DOCTO_SERVICO")
	private DoctoServico doctoServico;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_DESTINO")
	private Filial filialDestino;
	
	@Column(name = "TP_SITUACAO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_LIB_NOTA_NATURA") })
	private DomainValue tpSituacao;
	
	@Column(name = "TP_REGISTRO", length = 3)
	private Integer tpRegistro;
	
	@Column(name = "TP_ORDEM", length = 4)
	private String tpOrdem;
	
	@Column(name = "NR_NOTA_FISCAL", length = 9)
	private Integer nrNotaFiscal;
	
	@Column(name = "NR_PEDIDO", length = 10)
	private Long nrPedido;
	
	@Column(name = "DT_PEDIDO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtPedido;
	
	@Column(name = "TP_VOLUME", length = 5)
	private String tpVolume;
	
	@Column(name = "CD_MOTIVO", length = 3)
	private String cdMotivo;
	
	@Column(name = "CD_CODIGO_CN", length = 10)
	private String cdMotivoCn;
	
	@Column(name = "NM_NOME_CN", length = 35)
	private String nmNomeCn;
	
	@Column(name = "DS_ENDERECO_CN", length = 35)
	private String dsEnderecoCn;
	
	@Column(name = "DS_COMPLEMENTO", length = 40)
	private String dsComplemento;
	
	@Column(name = "DS_BAIRRO", length = 35)
	private String dsBairro;
	
	@Column(name = "DS_CEP", length = 10)
	private String dsCep;
	
	@Column(name = "DS_CIDADE", length = 25)
	private String dsCidade;
	
	@Column(name = "SG_ESTADO", length = 3)
	private String sgEstado;
	
	@Column(name = "SG_PAIS", length = 3)
	private String sgPais;
	
	@Column(name = "NR_TELEFONE_1", length = 16)
	private String nrTelefone1;
	
	@Column(name = "NR_TELEFONE_2", length = 16)
	private String nrTelefone2;
	
	@Column(name = "NR_TELEFONE_3", length = 16)
	private String nrTelefone3;
	
	@Column(name = "VL_FRETE")
	private BigDecimal vlFrete;
	
	@Column(name = "TP_SERVICO", length = 20)
	private String tpServico;
	
	@OneToMany
	@JoinColumns(value = {
			@JoinColumn(referencedColumnName = "NR_NOTA_FISCAL", name = "NR_NOTA_FISCAL", insertable = false, updatable = false),
			@JoinColumn(referencedColumnName = "ID_CLIENTE", name = "ID_CLIENTE", insertable = false, updatable = false) })
	private Collection<NotaFiscalConhecimento> notaFiscalConhecimento;

	public Long getIdLiberacaoNotaNatura() {
		return idLiberacaoNotaNatura;
	}

	public void setIdLiberacaoNotaNatura(Long idLiberacaoNotaNatura) {
		this.idLiberacaoNotaNatura = idLiberacaoNotaNatura;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public Filial getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(Filial filialDestino) {
		this.filialDestino = filialDestino;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public Integer getTpRegistro() {
		return tpRegistro;
	}

	public void setTpRegistro(Integer tpRegistro) {
		this.tpRegistro = tpRegistro;
	}

	public String getTpOrdem() {
		return tpOrdem;
	}

	public void setTpOrdem(String tpOrdem) {
		this.tpOrdem = tpOrdem;
	}

	public Integer getNrNotaFiscal() {
		return nrNotaFiscal;
	}

	public void setNrNotaFiscal(Integer nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}

	public Long getNrPedido() {
		return nrPedido;
	}

	public void setNrPedido(Long nrPedido) {
		this.nrPedido = nrPedido;
	}

	public YearMonthDay getDtPedido() {
		return dtPedido;
	}

	public void setDtPedido(YearMonthDay dtPedido) {
		this.dtPedido = dtPedido;
	}

	public String getTpVolume() {
		return tpVolume;
	}

	public void setTpVolume(String tpVolume) {
		this.tpVolume = tpVolume;
	}

	public String getCdMotivo() {
		return cdMotivo;
	}

	public void setCdMotivo(String cdMotivo) {
		this.cdMotivo = cdMotivo;
	}

	public String getCdMotivoCn() {
		return cdMotivoCn;
	}

	public void setCdMotivoCn(String cdMotivoCn) {
		this.cdMotivoCn = cdMotivoCn;
	}

	public String getNmNomeCn() {
		return nmNomeCn;
	}

	public void setNmNomeCn(String nmNomeCn) {
		this.nmNomeCn = nmNomeCn;
	}

	public String getDsEnderecoCn() {
		return dsEnderecoCn;
	}

	public void setDsEnderecoCn(String dsEnderecoCn) {
		this.dsEnderecoCn = dsEnderecoCn;
	}

	public String getDsComplemento() {
		return dsComplemento;
	}

	public void setDsComplemento(String dsComplemento) {
		this.dsComplemento = dsComplemento;
	}

	public String getDsBairro() {
		return dsBairro;
	}

	public void setDsBairro(String dsBairro) {
		this.dsBairro = dsBairro;
	}

	public String getDsCep() {
		return dsCep;
	}

	public void setDsCep(String dsCep) {
		this.dsCep = dsCep;
	}

	public String getDsCidade() {
		return dsCidade;
	}

	public void setDsCidade(String dsCidade) {
		this.dsCidade = dsCidade;
	}

	public String getSgEstado() {
		return sgEstado;
	}

	public void setSgEstado(String sgEstado) {
		this.sgEstado = sgEstado;
	}

	public String getSgPais() {
		return sgPais;
	}

	public void setSgPais(String sgPais) {
		this.sgPais = sgPais;
	}

	public String getNrTelefone1() {
		return nrTelefone1;
	}

	public void setNrTelefone1(String nrTelefone1) {
		this.nrTelefone1 = nrTelefone1;
	}

	public String getNrTelefone2() {
		return nrTelefone2;
	}

	public void setNrTelefone2(String nrTelefone2) {
		this.nrTelefone2 = nrTelefone2;
	}

	public String getNrTelefone3() {
		return nrTelefone3;
	}

	public void setNrTelefone3(String nrTelefone3) {
		this.nrTelefone3 = nrTelefone3;
	}

	public BigDecimal getVlFrete() {
		return vlFrete;
	}

	public void setVlFrete(BigDecimal vlFrete) {
		this.vlFrete = vlFrete;
	}

	public String getTpServico() {
		return tpServico;
	}

	public void setTpServico(String tpServico) {
		this.tpServico = tpServico;
	}

	public Collection<NotaFiscalConhecimento> getNotaFiscalConhecimento() {
		return notaFiscalConhecimento;
	}

	public void setNotaFiscalConhecimento(
			Collection<NotaFiscalConhecimento> notaFiscalConhecimento) {
		this.notaFiscalConhecimento = notaFiscalConhecimento;
	}

}
