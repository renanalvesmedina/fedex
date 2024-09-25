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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;

@Entity
@Table(name="ORDEM_SERVICO_ANEXO")
@SequenceGenerator(name = "ORDEM_SERVICO_ANEXO_SEQ", sequenceName = "ORDEM_SERVICO_ANEXO_SQ", allocationSize=1)
public class OrdemServicoAnexo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ID_ORDEM_SERVICO_ANEXO", nullable=false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDEM_SERVICO_ANEXO_SEQ")
	private Long idOrdemServicoAnexo;
	
	@Column(name="DS_ANEXO", length=240)
	private String dsAnexo;
    
	@Columns(columns = {@Column(name = "DH_INCLUSAO"), @Column(name = "DH_INCLUSAO_TZR") })
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
	private DateTime  dhInclusao;
	
	@Lob
	@Column(name = "DS_DOCUMENTO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.BinaryBlobUserType")
	private byte[] dcArquivo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_ORDEM_SERVICO", nullable = false)
    private OrdemServico ordemServico;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_PARCELA_PRECO")
	private ParcelaPreco parcelaPreco;

	/**
	 * Nome do arquivo anexo inserido no BLOB <tt>ds_documento</tt>
	 */
	@Formula("TRIM(UTL_RAW.CAST_TO_VARCHAR2(DBMS_LOB.SUBSTR(ds_documento, 1024)))")
	private String nmArquivo;

	public Long getIdOrdemServicoAnexo() {
		return idOrdemServicoAnexo;
	}
	public void setIdOrdemServicoAnexo(Long idOrdemServicoAnexo) {
		this.idOrdemServicoAnexo = idOrdemServicoAnexo;
	}

	public String getDsAnexo() {
		return dsAnexo;
	}
	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}
	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public byte[] getDcArquivo() {
		return dcArquivo;
	}
	public void setDcArquivo(byte[] dcArquivo) {
		this.dcArquivo = dcArquivo;
	}

	public OrdemServico getOrdemServico() {
		return ordemServico;
	}
	
	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}
	
	public ParcelaPreco getParcelaPreco() {
		return parcelaPreco;
	}
	
	public void setParcelaPreco(ParcelaPreco parcelaPreco) {
		this.parcelaPreco = parcelaPreco;
	}

	public String getNmArquivo() {
		return nmArquivo;
	}

}