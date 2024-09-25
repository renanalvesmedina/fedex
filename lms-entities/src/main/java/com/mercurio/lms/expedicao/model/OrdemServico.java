package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

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

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;


@Entity
@Table(name="ORDEM_SERVICO")
@SequenceGenerator(name = "ORDEM_SERVICO_SQ", sequenceName = "ORDEM_SERVICO_SQ", allocationSize=1)
public class OrdemServico implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_ORDEM_SERVICO", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDEM_SERVICO_SQ")
	private Long idOrdemServico;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_REGISTRO", nullable = false)
	private Filial filialRegistro;

	@Column(name="NR_ORDEM_SERVICO", length=9, nullable = false)
	private Long nrOrdemServico;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_EXECUCAO", nullable = false)
	private Filial filialExecucao;

	@Column(name = "TP_SOLICITANTE", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_TIPO_SOLICITANTE_SERVICO") })
	private DomainValue tpSolicitante;
	
	@Column(name = "TP_SITUACAO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_ORDEM_SERVICO") })
	private DomainValue tpSituacao;
	
	@Column(name = "TP_DOCUMENTO", length = 3)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", 
		  parameters = { @Parameter(name = "domainName", value = "DM_TIPO_DOCTO_ORDEM_SERVICO") })
	private DomainValue tpDocumento;
	
	@Column(name="NM_SOLICITANTE", length=50, nullable = false)
	private String nmSolicitante;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE_TOMADOR", nullable = false)
	private Cliente clienteTomador;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MUNICIPIO_EXECUCAO", nullable = false)
	private Municipio municipioExecucao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO_REGISTRANTE", nullable = false)
	private UsuarioLMS usuarioRegistrante;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_DIVISAO_CLIENTE")
	private DivisaoCliente divisaoCliente;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_IE_TOMADOR")
	private InscricaoEstadual ieTomador;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MANIFESTO")
	private Manifesto manifesto;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MANIFESTO_COLETA")
	private ManifestoColeta manifestoColeta;

	@Column(name="DS_MOTIVO_REJEICAO", length=500)
	private String dsMotivoRejeicao;
	
	@Column(name="DT_SOLICITACAO", nullable=false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtSolicitacao;
	
	@OneToMany(mappedBy="ordemServico",fetch=FetchType.LAZY)	
	private List<OrdemServicoDocumento> ordemServicoDocumentos;

	@OneToMany(mappedBy="ordemServico",fetch=FetchType.LAZY)
	private List<OrdemServicoItem> ordemServicoItens;
	
	@OneToMany(mappedBy="ordemServico",fetch=FetchType.LAZY)
	private List<OrdemServicoAnexo> ordemServicoAnexos;

	public Long getIdOrdemServico() {
		return idOrdemServico;
	}
	public void setIdOrdemServico(Long idOrdemServico) {
		this.idOrdemServico = idOrdemServico;
	}

	public Filial getFilialRegistro() {
		return filialRegistro;
	}
	public void setFilialRegistro(Filial filialRegistro) {
		this.filialRegistro = filialRegistro;
	}

	public Long getNrOrdemServico() {
		return nrOrdemServico;
	}
	public void setNrOrdemServico(Long nrOrdemServico) {
		this.nrOrdemServico = nrOrdemServico;
	}

	public Filial getFilialExecucao() {
		return filialExecucao;
	}
	public void setFilialExecucao(Filial filialExecucao) {
		this.filialExecucao = filialExecucao;
	}

	public DomainValue getTpSolicitante() {
		return tpSolicitante;
	}
	public void setTpSolicitante(DomainValue tpSolicitante) {
		this.tpSolicitante = tpSolicitante;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public DomainValue getTpDocumento() {
		return tpDocumento;
	}
	public void setTpDocumento(DomainValue tpDocumento) {
		this.tpDocumento = tpDocumento;
	}

	public String getNmSolicitante() {
		return nmSolicitante;
	}
	public void setNmSolicitante(String nmSolicitante) {
		this.nmSolicitante = nmSolicitante;
	}

	public Cliente getClienteTomador() {
		return clienteTomador;
	}
	public void setClienteTomador(Cliente clienteTomador) {
		this.clienteTomador = clienteTomador;
	}

	public Municipio getMunicipioExecucao() {
		return municipioExecucao;
	}
	public void setMunicipioExecucao(Municipio municipioExecucao) {
		this.municipioExecucao = municipioExecucao;
	}

	public UsuarioLMS getUsuarioRegistrante() {
		return usuarioRegistrante;
	}
	public void setUsuarioRegistrante(UsuarioLMS usuarioRegistrante) {
		this.usuarioRegistrante = usuarioRegistrante;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}
	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public InscricaoEstadual getIeTomador() {
		return ieTomador;
	}
	public void setIeTomador(InscricaoEstadual ieTomador) {
		this.ieTomador = ieTomador;
	}

	public Manifesto getManifesto() {
		return manifesto;
	}
	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}

	public ManifestoColeta getManifestoColeta() {
		return manifestoColeta;
	}
	public void setManifestoColeta(ManifestoColeta manifestoColeta) {
		this.manifestoColeta = manifestoColeta;
	}

	public String getDsMotivoRejeicao() {
		return dsMotivoRejeicao;
	}
	public void setDsMotivoRejeicao(String dsMotivoRejeicao) {
		this.dsMotivoRejeicao = dsMotivoRejeicao;
	}

	public YearMonthDay getDtSolicitacao() {
		return dtSolicitacao;
	}
	public void setDtSolicitacao(YearMonthDay dtSolicitacao) {
		this.dtSolicitacao = dtSolicitacao;
	}

	public List<OrdemServicoDocumento> getOrdemServicoDocumentos() {
		return ordemServicoDocumentos;
	}
	public void setOrdemServicoDocumentos(List<OrdemServicoDocumento> ordemServicoDocumentos) {
		this.ordemServicoDocumentos = ordemServicoDocumentos;
	}

	public List<OrdemServicoItem> getOrdemServicoItens() {
		return ordemServicoItens;
	}
	public void setOrdemServicoItens(List<OrdemServicoItem> ordemServicoItens) {
		this.ordemServicoItens = ordemServicoItens;
	}

	public List<OrdemServicoAnexo> getOrdemServicoAnexos() {
		return ordemServicoAnexos;
	}
	public void setOrdemServicoAnexos(List<OrdemServicoAnexo> ordemServicoAnexos) {
		this.ordemServicoAnexos = ordemServicoAnexos;
	}	
}