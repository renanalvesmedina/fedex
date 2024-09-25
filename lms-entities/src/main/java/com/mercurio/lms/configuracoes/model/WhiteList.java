package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.lms.expedicao.model.DoctoServico;

@Entity
@Table(name="WHITE_LIST")
@SequenceGenerator(name = "WHITE_LIST_SQ", sequenceName = "WHITE_LIST_SQ")
public class WhiteList implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID_WHITE_LIST")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WHITE_LIST_SQ")
	private Long idWhiteList;
	
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_CONTATO")
	private Contato contato;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_DOCTO_SERVICO")
	private DoctoServico doctoServico;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO")
	private UsuarioADSM usuario;
	
	@Column(name = "TP_ENVIO", length = 2, nullable=false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_ENVIO_WHITE_LIST") })
	private DomainValue tpEnvio;
	
	@Column(name = "TP_ORIGEM", length = 2, nullable=false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_ORIGEM_WHITE_LIST") })
	private DomainValue tpOrigem;
	
	
	@Column(name = "OB_WHITE_LIST", length = 255)
	private String obWhiteList;
	
	@Column(name = "ST_WHITE_LIST", length = 2, nullable=false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_STATUS") })
	private DomainValue situacaoWhiteList;
	
	@Column(name = "TP_WHITE_LIST", length = 2, nullable=false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_WHITE_LIST") })
	private DomainValue tpWhiteList;
	
	@Column(name = "NM_REMETENTE", length = 60)
	private String nmRemetente;
	
	@Column(name = "DS_EMAIL_REMETENTE", length = 60)
	private String dsEmailRemetente;

	public Long getIdWhiteList() {
		return idWhiteList;
	}

	public void setIdWhiteList(Long idWhiteList) {
		this.idWhiteList = idWhiteList;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) {
		this.contato = contato;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public UsuarioADSM getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioADSM usuario) {
		this.usuario = usuario;
	}

	public DomainValue getTpEnvio() {
		return tpEnvio;
	}

	public void setTpEnvio(DomainValue tpEnvio) {
		this.tpEnvio = tpEnvio;
	}

	public String getObWhiteList() {
		return obWhiteList;
	}

	public void setObWhiteList(String obWhiteList) {
		this.obWhiteList = obWhiteList;
	}

	public DomainValue getTpWhiteList() {
		return tpWhiteList;
	}

	public void setTpWhiteList(DomainValue tpWhiteList) {
		this.tpWhiteList = tpWhiteList;
	}

	public String getNmRemetente() {
		return nmRemetente;
	}

	public void setNmRemetente(String nmRemetente) {
		this.nmRemetente = nmRemetente;
	}

	public String getDsEmailRemetente() {
		return dsEmailRemetente;
	}

	public void setDsEmailRemetente(String dsEmailRemetente) {
		this.dsEmailRemetente = dsEmailRemetente;
	}

	public DomainValue getSituacaoWhiteList() {
		return situacaoWhiteList;
	}

	public void setSituacaoWhiteList(DomainValue situacaoWhiteList) {
		this.situacaoWhiteList = situacaoWhiteList;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append("idWhiteList", getIdWhiteList()).toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof WhiteList))
			return false;
		WhiteList castOther = (WhiteList) other;
		return new EqualsBuilder().append(this.getIdWhiteList(),
				castOther.getIdWhiteList()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdWhiteList()).toHashCode();
	}

	public DomainValue getTpOrigem() {
		return tpOrigem;
	}

	public void setTpOrigem(DomainValue tpOrigem) {
		this.tpOrigem = tpOrigem;
	}

}
