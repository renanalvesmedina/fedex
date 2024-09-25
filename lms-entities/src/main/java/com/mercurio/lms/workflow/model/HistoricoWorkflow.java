package com.mercurio.lms.workflow.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "HISTORICO_WORKFLOW")
@SequenceGenerator(name = "HISTORICO_WORKFLOW_SEQ", sequenceName = "HISTORICO_WORKFLOW_SQ", allocationSize = 1)
public class HistoricoWorkflow implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_HISTORICO_WORKFLOW", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HISTORICO_WORKFLOW_SEQ")
	private Long idHistoricoWorkflow;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PENDENCIA", nullable = false)
	private Pendencia pendencia;

	@Column(name = "ID_PROCESSO", nullable = false)
	private Long idProcesso;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private UsuarioLMS usuario;

	@Column(name = "NM_TABELA", nullable = false, length = 60)
	private String nmTabela;

	@Column(name = "TP_CAMPO_WORKFLOW", length = 4)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TIPO_CAMPO_WORKFLOW") }) 
	private DomainValue tpCampoWorkflow;

	@Column(name = "DS_VL_ANTIGO", nullable = true, length = 1000)
	private String dsVlAntigo;

	@Column(name = "DS_VL_NOVO", nullable = true, length = 1000)
	private String dsVlNovo;

	@Columns(columns = { @Column(name = "DH_SOLICITACAO", nullable = false), @Column(name = "DH_SOLICITACAO_TZR", nullable = false) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhSolicitacao;

	@Column(name = "DS_OBSERVACAO", nullable = true, length = 1000)
	private String dsObservacao;

	public String toString() {
		return new ToStringBuilder(this).append("idFaturaCiaAereaAnexo", getIdHistoricoWorkflow()).toString();
	}

	public boolean equals(Object other) {
		if (this == other){
			return true;
		}
		
		if (!(other instanceof HistoricoWorkflow)){
			return false;
		}
		
		HistoricoWorkflow castOther = (HistoricoWorkflow) other;
		return new EqualsBuilder().append(this.getIdHistoricoWorkflow(), castOther.getIdHistoricoWorkflow()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdHistoricoWorkflow()).toHashCode();
	}

	public Long getIdHistoricoWorkflow() {
		return idHistoricoWorkflow;
	}

	public void setIdHistoricoWorkflow(Long idHistoricoWorkflow) {
		this.idHistoricoWorkflow = idHistoricoWorkflow;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public Long getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(Long idProcesso) {
		this.idProcesso = idProcesso;
	}

	public UsuarioLMS getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioLMS usuario) {
		this.usuario = usuario;
	}

	public String getNmTabela() {
		return nmTabela;
	}

	public void setNmTabela(String nmTabela) {
		this.nmTabela = nmTabela;
	}

	public String getDsVlAntigo() {
		return dsVlAntigo;
	}

	public void setDsVlAntigo(String dsVlAntigo) {
		this.dsVlAntigo = dsVlAntigo;
	}

	public String getDsVlNovo() {
		return dsVlNovo;
	}

	public void setDsVlNovo(String dsVlNovo) {
		this.dsVlNovo = dsVlNovo;
	}

	public DateTime getDhSolicitacao() {
		return dhSolicitacao;
	}

	public void setDhSolicitacao(DateTime dhSolicitacao) {
		this.dhSolicitacao = dhSolicitacao;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public DomainValue getTpCampoWorkflow() {
		return tpCampoWorkflow;
	}

	public void setTpCampoWorkflow(DomainValue tpCampoWorkflow) {
		this.tpCampoWorkflow = tpCampoWorkflow;
	}
}