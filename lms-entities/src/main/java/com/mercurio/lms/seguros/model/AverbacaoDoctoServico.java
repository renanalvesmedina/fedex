package com.mercurio.lms.seguros.model;

import java.io.Serializable;

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
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;

@Entity
@Table(name = "AVERBACAO_DOCTO_SERVICO")
@SequenceGenerator(name = "AVERBACAO_DOCTO_SERVICO_SQ", sequenceName = "AVERBACAO_DOCTO_SERVICO_SQ")
public class AverbacaoDoctoServico implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AVERBACAO_DOCTO_SERVICO_SQ")
	@Column(name = "ID_AVERBACAO_DOCTO_SERVICO", nullable = false)
	private Long idAverbacaoDoctoServico;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_DOCTO_SERVICO")
	private DoctoServico doctoServico;
	
	@Column(name = "NR_PROTOCOLO", length = 120)
	private String nrProtocolo;
	
	@Column(name = "NR_AVERBACAO", length = 40)
	private String nrAverbacao;

	@Column(name = "BL_AVERBADO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blAverbado;

	@Column(name = "DS_RETORNO", length = 2000)
	private String dsRetorno;

	@Column(name = "DC_RETORNO")
	private String dcRetorno;

	@Columns(columns = { @Column(name = "DH_ENVIO"),
			@Column(name = "DH_ENVIO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhEnvio;

	@Columns(columns = { @Column(name = "DH_INCLUSAO"),
			@Column(name = "DH_INCLUSAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhInclusao;

	@Column(name = "TP_WEBSERVICE", length = 1)
	private String tpWebservice;

	@Column(name = "TP_DESTINO", length = 10)
	private String tpDestino;
	
	@Column(name = "NR_ENVIO")
	private Integer nrEnvio;
        
        @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_MANIFESTO_ELETRONICO")
	private ManifestoEletronico manifestoEletronico;
            
    @Column(name = "BL_ENVIADO", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blEnviado; 
       
	public Long getIdAverbacaoDoctoServico() {
		return idAverbacaoDoctoServico;
	}

	public void setIdAverbacaoDoctoServico(Long idAverbacaoDoctoServico) {
		this.idAverbacaoDoctoServico = idAverbacaoDoctoServico;
	}

	public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public String getNrProtocolo() {
		return nrProtocolo;
	}

	public void setNrProtocolo(String nrProtocolo) {
		this.nrProtocolo = nrProtocolo;
	}

	public Boolean getBlAverbado() {
		return blAverbado;
	}

	public void setBlAverbado(Boolean blAverbado) {
		this.blAverbado = blAverbado;
	}

	public String getDsRetorno() {
		return dsRetorno;
	}

	public void setDsRetorno(String dsRetorno) {
		this.dsRetorno = dsRetorno;
	}

	public String getDcRetorno() {
		return dcRetorno;
	}

	public void setDcRetorno(String dcRetorno) {
		this.dcRetorno = dcRetorno;
	}

	public DateTime getDhEnvio() {
		return dhEnvio;
	}

	public void setDhEnvio(DateTime dhEnvio) {
		this.dhEnvio = dhEnvio;
	}

	public String getTpWebservice() {
		return tpWebservice;
	}

	public void setTpWebservice(String tpWebservice) {
		this.tpWebservice = tpWebservice;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}
	
	public String getTpDestino() {
		return tpDestino;
	}

	public void setTpDestino(String tpDestino) {
		this.tpDestino = tpDestino;
	}

	public String getNrAverbacao() {
		return nrAverbacao;
	}

	public void setNrAverbacao(String nrAverbacao) {
		this.nrAverbacao = nrAverbacao;
	}
        
        public ManifestoEletronico getManifestoEletronico() {
            return manifestoEletronico;
        }

	public Integer getNrEnvio() {
		return nrEnvio;
	}
        public void setManifestoEletronico(ManifestoEletronico manifestoEletronico) {
            this.manifestoEletronico = manifestoEletronico;
        }

	public void setNrEnvio(Integer nrEnvio) {
		this.nrEnvio = nrEnvio;
	}	
	
	public Boolean getBlEnviado() {
		return blEnviado;
	}

	public void setBlEnviado(Boolean blEnviado) {
		this.blEnviado = blEnviado;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id",
				getIdAverbacaoDoctoServico()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AverbacaoDoctoServico))
			return false;
		AverbacaoDoctoServico castOther = (AverbacaoDoctoServico) other;
		return new EqualsBuilder().append(this.getIdAverbacaoDoctoServico(),
				castOther.getIdAverbacaoDoctoServico()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAverbacaoDoctoServico())
				.toHashCode();
	}

}
