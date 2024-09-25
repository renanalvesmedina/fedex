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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.municipios.model.Filial;

@Entity
@Table(name = "MANIFESTO_ELETRONICO")
@SequenceGenerator(name = "SQ_MANIFESTO_ELETRONICO", sequenceName = "MANIFESTO_ELETRONICO_SQ", allocationSize = 1)
public class ManifestoEletronico implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_MANIFESTO_ELETRONICO")
	@Column(name = "ID_MANIFESTO_ELETRONICO", nullable = false)
	private Long idManifestoEletronico;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_ORIGEM", nullable = false)
	private Filial filialOrigem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL_DESTINO", nullable = false)
	private Filial filialDestino;
	
	@Column(name = "TP_SITUACAO", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_SITUACAO_MANIFESTO_ELETRONICO") })
	private DomainValue tpSituacao;
	
	@Column(name="NR_MANIFESTO_ELETRONICO", length=9, nullable = false)
	private Long nrManifestoEletronico;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_CONTROLE_CARGA", nullable = false)
	private ControleCarga controleCarga;
	
	@Column(name="NR_CHAVE", length=44, nullable=false)
	private String nrChave;
	
	@Columns(columns = { @Column(name = "DH_EMISSAO"), @Column(name = "DH_EMISSAO_TZR ") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime dhEmissao;
	
	@Column(name = "DS_OBSERVACAO", length=500)
	private String dsObservacao;
	
	@Column(name = "DS_OBSERVACAO_SEFAZ", length=255)
	private String dsObservacaoSefaz;
	
	@Column(name = "ID_ENVIO_C", length=10)
	private Long idEnvioC;
	
	@Column(name = "ID_ENVIO_E", length=10)
	private Long idEnvioE;
	
	@Column(name = "ID_ENVIO_F", length=10)
	private Long idEnvioF;
	
	@Column(name = "NR_PROTOCOLO", length=15)
	private Long nrProtocolo;
	
	@Column(name = "CD_STATUS_ENCER", length=15)
	private Long cdStatusEncerramento;
	
	@Lob
	@Column(name="DS_DADOS")
	private byte[] dsDados;
        
        @Lob
	@Column(name="DS_DADOS_AUTORIZACAO")
	private byte[] dsDadosAutorizacao;
        
        @Lob
	@Column(name="DS_DADOS_CANCELAMENTO")
	private byte[] dsDadosCancelamento;
        
        @Lob
	@Column(name="DS_DADOS_ENCERRAMENTO")
	private byte[] dsDadosEncerramento;

	@Transient
	List<Conhecimento> conhecimentos;
	
	
	public Long getIdManifestoEletronico() {
		return idManifestoEletronico;
	}

	public void setIdManifestoEletronico(Long idManifestoEletronico) {
		this.idManifestoEletronico = idManifestoEletronico;
	}

	public Filial getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(Filial filialOrigem) {
		this.filialOrigem = filialOrigem;
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

	public Long getNrManifestoEletronico() {
		return nrManifestoEletronico;
	}

	public void setNrManifestoEletronico(Long nrManifestoEletronico) {
		this.nrManifestoEletronico = nrManifestoEletronico;
	}

	public ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public String getNrChave() {
		return nrChave;
	}

	public void setNrChave(String nrChave) {
		this.nrChave = nrChave;
	}

	public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}

	public String getDsObservacaoSefaz() {
		return dsObservacaoSefaz;
	}

	public void setDsObservacaoSefaz(String dsObservacaoSefaz) {
		this.dsObservacaoSefaz = dsObservacaoSefaz;
	}

	public Long getIdEnvioC() {
		return idEnvioC;
	}

	public void setIdEnvioC(Long idEnvioC) {
		this.idEnvioC = idEnvioC;
	}

	public Long getIdEnvioE() {
		return idEnvioE;
	}

	public void setIdEnvioE(Long idEnvioE) {
		this.idEnvioE = idEnvioE;
	}

	public Long getIdEnvioF() {
		return idEnvioF;
	}

	public void setIdEnvioF(Long idEnvioF) {
		this.idEnvioF = idEnvioF;
	}

	public Long getNrProtocolo() {
		return nrProtocolo;
	}

	public void setNrProtocolo(Long nrProtocolo) {
		this.nrProtocolo = nrProtocolo;
	}

	public byte[] getDsDados() {
		return dsDados;
	}

	public void setDsDados(byte[] dsDados) {
		this.dsDados = dsDados;
	}

	public List<Conhecimento> getConhecimentos() {
		return conhecimentos;
	}

	public void setConhecimentos(List<Conhecimento> conhecimentos) {
		this.conhecimentos = conhecimentos;
	}
	
	public Long getCdStatusEncerramento() {
		return cdStatusEncerramento;
	}

	public void setCdStatusEncerramento(Long cdStatusEncerramento) {
		this.cdStatusEncerramento = cdStatusEncerramento;
	}

        public byte[] getDsDadosAutorizacao() {
            return dsDadosAutorizacao;
        }

        public void setDsDadosAutorizacao(byte[] dsDadosAutorizacao) {
            this.dsDadosAutorizacao = dsDadosAutorizacao;
        }

        public byte[] getDsDadosCancelamento() {
            return dsDadosCancelamento;
        }

        public void setDsDadosCancelamento(byte[] dsDadosCancelamento) {
            this.dsDadosCancelamento = dsDadosCancelamento;
        }

        public byte[] getDsDadosEncerramento() {
            return dsDadosEncerramento;
        }

        public void setDsDadosEncerramento(byte[] dsDadosEncerramento) {
            this.dsDadosEncerramento = dsDadosEncerramento;
        }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_MANIFESTO_ELETRONICO", idManifestoEletronico)
				.append("ID_FILIAL_ORIGEM", filialOrigem != null ? filialOrigem.getIdFilial() : null)
				.append("ID_FILIAL_DESTINO", filialDestino != null ? filialDestino.getIdFilial() : null)
				.append("TP_SITUACAO", tpSituacao != null ? tpSituacao.getValue() : null)
				.append("NR_MANIFESTO_ELETRONICO", nrManifestoEletronico)
				.append("ID_CONTROLE_CARGA", controleCarga != null ? controleCarga.getIdControleCarga() : null)
				.append("NR_CHAVE", nrChave)
				.append("DH_EMISSAO", dhEmissao)
				.append("DS_OBSERVACAO", dsObservacao)
				.append("ID_ENVIO_E", idEnvioE)
				.append("ID_ENVIO_C", idEnvioC)
				.append("ID_ENVIO_F", idEnvioF)
				.append("NR_PROTOCOLO", nrProtocolo)
				.append("DS_DADOS", dsDados)
				.append("DS_OBSERVACAO_SEFAZ", dsObservacaoSefaz)
				.append("CD_STATUS_ENCER", cdStatusEncerramento)
				.toString();
	}

}
