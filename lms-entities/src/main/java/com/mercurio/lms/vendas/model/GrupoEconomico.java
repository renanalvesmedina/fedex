package com.mercurio.lms.vendas.model;

import java.io.Serializable;
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
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;

@Entity
@Table(name = "GRUPO_ECONOMICO")
@SequenceGenerator(name = "GRUPO_ECONOMICO_SEQ", sequenceName = "GRUPO_ECONOMICO_SQ", allocationSize = 1)
public class GrupoEconomico implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
	@Column(name = "ID_GRUPO_ECONOMICO", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GRUPO_ECONOMICO_SEQ")
	private Long idGrupoEconomico;

    @Column(name = "DS_GRUPO_ECONOMICO", nullable = false)
	private String dsGrupoEconomico;

    @Column(name = "TP_SITUACAO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_STATUS") })
	private DomainValue tpSituacao;
    
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CLIENTE_PRINCIPAL")
	private Cliente clientePrincipal;
    
    @Column(name = "DS_CODIGO", nullable = false)
   	private String dsCodigo;
    
    @ManyToOne
	@JoinColumn(name = "ID_USUARIO_INCLUSAO", nullable = false)
	private UsuarioLMS usuarioInclusao;
    
    @ManyToOne
	@JoinColumn(name = "ID_USUARIO_ALTERACAO", nullable = false)
	private UsuarioLMS usuarioAlteracao;
    
    @Columns(columns = { @Column(name = "DH_INCLUSAO", nullable = true), @Column(name = "DH_INCLUSAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;
    
    @Columns(columns = { @Column(name = "DH_ALTERACAO", nullable = true), @Column(name = "DH_ALTERACAO_TZR", nullable = true) })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAlteracao;
    
    @OneToMany(mappedBy="grupoEconomico",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private List<GrupoEconomicoCliente> gruposEconomicoCliente;
    
    public GrupoEconomico() {
	}
    
    public GrupoEconomico(Long id) {
		this.idGrupoEconomico = id;
	}

	public Long getIdGrupoEconomico() {
		return idGrupoEconomico;
	}

	public void setIdGrupoEconomico(Long idGrupoEconomico) {
		this.idGrupoEconomico = idGrupoEconomico;
	}

	public String getDsGrupoEconomico() {
		return dsGrupoEconomico;
	}

	public void setDsGrupoEconomico(String dsGrupoEconomico) {
		this.dsGrupoEconomico = dsGrupoEconomico;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public Cliente getClientePrincipal() {
		return clientePrincipal;
	}

	public void setClientePrincipal(Cliente clientePrincipal) {
		this.clientePrincipal = clientePrincipal;
	}

	public String getDsCodigo() {
		return dsCodigo;
	}

	public void setDsCodigo(String dsCodigo) {
		this.dsCodigo = dsCodigo;
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

	public String toString() {
		return new ToStringBuilder(this).append("idGrupoEconomico", getIdGrupoEconomico()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof GrupoEconomico))
			return false;
        GrupoEconomico castOther = (GrupoEconomico) other;
		return new EqualsBuilder().append(this.getIdGrupoEconomico(), castOther.getIdGrupoEconomico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdGrupoEconomico()).toHashCode();
    }
	
}
