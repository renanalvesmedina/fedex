package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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

@Entity
@Table(name="MONITORAMENTO_DOC_ELETRONICO")
@SequenceGenerator(name="MONITORAMENTO_DOC_ELETRONICO_SEQ", sequenceName="MONITORAMENTO_DOC_ELETRONIC_SQ", allocationSize=1)
public class MonitoramentoDocEletronico implements Serializable {
	private static final long serialVersionUID = 2L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MONITORAMENTO_DOC_ELETRONICO_SEQ")
    @Column(name = "ID_MONITORAMENTO_DOC_ELETRONIC", nullable = false)
	private Long idMonitoramentoDocEletronic;
    @OneToOne
    @JoinColumn(name = "ID_DOCTO_SERVICO", nullable = false)
	private DoctoServico doctoServico;
    @Column(name="NR_CHAVE", length=15, nullable=true)
	private String nrChave;	
    @Column(name = "TP_SITUACAO_DOCUMENTO", length = 1, nullable = false)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_DOC_ELETRONICO") })
	private DomainValue tpSituacaoDocumento;
    @Column(name = "DS_OBSERVACAO", nullable=true, length=2000)
	private String dsObservacao;
    @Column(name="DS_SERIE_RPS", nullable=true, length=2)
	private String dsSerieRps;
    @Column(name="TP_DOCUMENTO_RPS", nullable=true, length=20)
	private String tpDocumentoRps;
    @Column(name="NR_DOCUMENTO_ELETRONICO", length=15, unique=true)
	private Long nrDocumentoEletronico;
    @Column(name = "ID_ENVIO_DOC_ELETRONICO_E", nullable = true)
	private Long idEnvioDocEletronicoE;
    @Column(name = "ID_ENVIO_DOC_ELETRONICO_C", nullable = true)
	private Long idEnvioDocEletronicoC;
    @Column(name = "ID_ENVIO_DOC_ELETRONICO_I", nullable = true)
	private Long idEnvioDocEletronicoI;
    @Column(name = "ID_ENVIO_DOC_ELETRONICO_A", nullable = true)
	private Long idEnvioDocEletronicoA;
    @Column(name = "NR_PROTOCOLO", nullable = true)
	private Long nrProtocolo;
    @Column(name = "NR_ENVIOS", nullable = true)
	private Long nrEnvios;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ID_CONTINGENCIA", nullable=true)
	private Contingencia contingencia;
    @Lob
    @Column(name="DS_DADOS_DOCUMENTO", nullable = true)
	private byte[] dsDadosDocumento;
    @Column(name = "DS_ENVIO_EMAIL", nullable=true, length=500)
	private String dsEnvioEmail;
    @Column(name = "BL_ENVIADO_CLIENTE", length = 1, nullable=true)
    @Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blEnviadoCliente;
    @Columns(columns = { @Column(name = "DH_ENVIO"),
            @Column(name = "DH_ENVIO_TZR") })
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhEnvio;
    @Column(name = "NR_FISCAL_RPS", nullable = true)
	private Long nrFiscalRps;
    @Columns(columns = { @Column(name = "DH_AUTORIZACAO"),
            @Column(name = "DH_AUTORIZACAO_TZR") })
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhAutorizacao;
    @Column(name="NR_CHAVE_ELETR_RPST_FDX", length=15, nullable=true)
    private String nrChaveEletrRpstFdx;

    @Column(name = "nr_cnf", nullable= true, length=8)
    private Long nrCnf;
    
	public Long getIdMonitoramentoDocEletronic() {
		return idMonitoramentoDocEletronic;
	}

	public void setIdMonitoramentoDocEletronic(Long idMonitoramentoDocEletronic) {
		this.idMonitoramentoDocEletronic = idMonitoramentoDocEletronic;
	}

    public DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public DomainValue getTpSituacaoDocumento() {
		return tpSituacaoDocumento;
	}
	
	/**
	 * Quando envia a Nota Fiscal Serviço Eletronica para a NDD (docKing), a
	 * situação é E - Enviado <br>
	 * Quando retorna da NDD é: 1 – A - Autorizado 2 – R - Rejeitado 3 – F -
	 * Arquivo mal formatado
	 * 
	 * @param tpSituacaoDocumento
	 */
	public void setTpSituacaoDocumento(DomainValue tpSituacaoDocumento) {
		this.tpSituacaoDocumento = tpSituacaoDocumento;
	}

	/**
	 * Mensagem de ERRO que retorna da NDD na tag <Mensagem>.
	 * 
	 * @return
	 */
	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public String getDsSerieRps() {
		return dsSerieRps;
	}

	public void setDsSerieRps(String dsSerieRps) {
		this.dsSerieRps = dsSerieRps;
	}
	
	public String getTpDocumentoRps() {
		return tpDocumentoRps;
	}
	
	public void setTpDocumentoRps(String tpDocumentoRps) {
		this.tpDocumentoRps = tpDocumentoRps;
	}
	
	public Long getNrDocumentoEletronico() {
		return nrDocumentoEletronico;
	}

	/**
	 * Número da nota fiscal gerado pela prefeitura
	 * 
	 * @param nrDocumentoEletronico
	 */
	public void setNrDocumentoEletronico(Long nrDocumentoEletronico) {
		this.nrDocumentoEletronico = nrDocumentoEletronico;
	}
	
	public String getNrChave() {
		return nrChave;
	}
	
	public void setNrChave(String nrChave) {
		this.nrChave = nrChave;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idMonitoramentoDocEletronic",
				getIdMonitoramentoDocEletronic()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MonitoramentoDocEletronico))
			return false;
        MonitoramentoDocEletronico castOther = (MonitoramentoDocEletronico) other;
		return new EqualsBuilder().append(
				this.getIdMonitoramentoDocEletronic(),
				castOther.getIdMonitoramentoDocEletronic()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMonitoramentoDocEletronic())
				.toHashCode();
    }

	public Long getIdEnvioDocEletronicoE() {
		return idEnvioDocEletronicoE;
	}

	public void setIdEnvioDocEletronicoE(Long idEnvioDocEletronicoE) {
		this.idEnvioDocEletronicoE = idEnvioDocEletronicoE;
}
	
	public Long getIdEnvioDocEletronicoC() {
		return idEnvioDocEletronicoC;
	}
	
	public void setIdEnvioDocEletronicoC(Long idEnvioDocEletronicoC) {
		this.idEnvioDocEletronicoC = idEnvioDocEletronicoC;
	}
	
	public Long getIdEnvioDocEletronicoI() {
		return idEnvioDocEletronicoI;
	}
	
	public void setIdEnvioDocEletronicoI(Long idEnvioDocEletronicoI) {
		this.idEnvioDocEletronicoI = idEnvioDocEletronicoI;
	}

	public Long getIdEnvioDocEletronicoA() {
		return idEnvioDocEletronicoA;
	}
	
	public void setIdEnvioDocEletronicoA(Long idEnvioDocEletronicoA) {
		this.idEnvioDocEletronicoA = idEnvioDocEletronicoA;
	}
	
	public Long getNrProtocolo() {
		return nrProtocolo;
	}

	public void setNrProtocolo(Long nrProtocolo) {
		this.nrProtocolo = nrProtocolo;
	}
	
	
	public Contingencia getContingencia() {
		return contingencia;
	}
	
	public byte[] getDsDadosDocumento() {
		return dsDadosDocumento;
	}

	public void setContingencia(Contingencia contingencia) {
		this.contingencia = contingencia;
	}


	public void setDsDadosDocumento(byte[] dsDadosDocumento) {
		this.dsDadosDocumento = dsDadosDocumento;
	}

	public String getDsEnvioEmail() {
		return dsEnvioEmail;
}

	public void setDsEnvioEmail(String dsEnvioEmail) {
		this.dsEnvioEmail = dsEnvioEmail;
	}

	public Boolean getBlEnviadoCliente() {
		return blEnviadoCliente;
	}

	public void setBlEnviadoCliente(Boolean blEnviadoCliente) {
		this.blEnviadoCliente = blEnviadoCliente;
	}

	public DateTime getDhEnvio() {
		return dhEnvio;
	}

	public void setDhEnvio(DateTime dhEnvio) {
		this.dhEnvio = dhEnvio;
	}

	public void setNrEnvios(Long nrEnvios) {
		this.nrEnvios = nrEnvios;
	}

	public Long getNrEnvios() {
		return nrEnvios;
	}
	public Long getNrFiscalRps() {
		return nrFiscalRps;
	}
	public void setNrFiscalRps(Long nrFiscalRps) {
		this.nrFiscalRps = nrFiscalRps;
	}

	public DateTime getDhAutorizacao() {
		return dhAutorizacao;
	}

	public void setDhAutorizacao(DateTime dhAutorizacao) {
		this.dhAutorizacao = dhAutorizacao;
	}
	
	public String getNrChaveEletrRpstFdx(){
	    return nrChaveEletrRpstFdx;
	}
	public void setNrChaveEletrRpstFdx(String nrChaveEletrRpstFdx){
	    this.nrChaveEletrRpstFdx = nrChaveEletrRpstFdx;
	}

	public Long getNrCnf() {
		return nrCnf;
	}

	public void setNrCnf(Long nrCnf) {
		this.nrCnf = nrCnf;
	}
}
