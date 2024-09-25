package com.mercurio.lms.ppd.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.municipios.model.Filial;

@Entity
@Table(name = "PPD_RECIBOS")
@SequenceGenerator(name = "RECIBO_INDENIZACAO_SEQ", sequenceName = "PPD_RECIBOS_SQ")
public class PpdRecibo implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	/* RECIBO */
	private Long idRecibo;
	private YearMonthDay dtRecibo;
	private Long nrRecibo;
	private DomainValue tpIndenizacao;
	private DomainValue tpStatus;
	private BigDecimal vlIndenizacao;	
	private String nrSeguro;
	private DomainValue tpLocalidade;
	private String sgFilialLocal1;
	private String sgFilialLocal2;
	/* EVENTOS */
	private YearMonthDay dtProgramadaPagto;
	private YearMonthDay dtPagto;
	/* FAVORECIDO */
	private Long nrBanco;
	private Long nrAgencia;
	private String nrDigitoAgencia;
	private Long nrContaCorrente;
	private String nrDigitoContaCorrente;			
	/* CTRC */
	private String sgFilialOrigem;	
	private Long nrCtrc;			
	private YearMonthDay dtEmissaoCtrc;
	/* RNC */
	private String sgFilialRnc;
	private Long nrRnc;
	private YearMonthDay dtEmissaoRnc;	
	/* COMPOSIÇÃO DO DÉBITO */
	private String sgFilialComp1;
	private Integer pcFilialComp1;
	private String sgFilialComp2;
	private Integer pcFilialComp2;
	private String sgFilialComp3;
	private Integer pcFilialComp3;	
	/* OBSERVAÇÕES */
	private String obRecibo;
	/* POJOS */
	private PpdNaturezaProduto naturezaProduto;
	private Pessoa pessoa;
	private Filial filial;
	private PpdFormaPgto formaPgto;
    private Boolean blEmailPgto;
    private Boolean blSegurado;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECIBO_INDENIZACAO_SEQ")
	@Column(name = "ID_RECIBO", nullable = false)
	public Long getIdRecibo() {
		return idRecibo;
	}

	public void setIdRecibo(Long idRecibo) {
		this.idRecibo = idRecibo;
	}

	@Column(name = "DT_RECIBO", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType") 
	public YearMonthDay getDtRecibo() {
		return dtRecibo;
	}

	public void setDtRecibo(YearMonthDay dtRecibo) {
		this.dtRecibo = dtRecibo;
	}

	@Column(name = "NR_RECIBO_INDENIZACAO",nullable = false)
	public Long getNrRecibo() {
		return nrRecibo;
	}

	public void setNrRecibo(Long nrRecibo) {
		this.nrRecibo = nrRecibo;
	}

	@Column(name = "TP_INDENIZACAO", nullable = false, length = 1)	
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_PPD_TIPO_INDENIZACAO") })
	public DomainValue getTpIndenizacao() {
		return tpIndenizacao;
	}

	public void setTpIndenizacao(DomainValue tpIndenizacao) {
		this.tpIndenizacao = tpIndenizacao;
	}

	@Column(name = "TP_STATUS", nullable = false, length = 1)	
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_PPD_STATUS_INDENIZACAO") })
	public DomainValue getTpStatus() {
		return tpStatus;
	}

	public void setTpStatus(DomainValue tpStatus) {
		this.tpStatus = tpStatus;
	}

	@Column(name = "VL_INDENIZACAO", nullable = false)
	public BigDecimal getVlIndenizacao() {
		return vlIndenizacao;
	}

	public void setVlIndenizacao(BigDecimal vlIndenizacao) {
		this.vlIndenizacao = vlIndenizacao;
	}

	@Column(name = "NR_SEGURO", length = 25)
	public String getNrSeguro() {
		return nrSeguro;
	}

	public void setNrSeguro(String nrSeguro) {
		this.nrSeguro = nrSeguro;
	}

	@Column(name = "TP_LOCALIDADE", length = 1)	
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_PPD_LOCAL_SINISTRO") })
	public DomainValue getTpLocalidade() { 
		return tpLocalidade;
	}

	public void setTpLocalidade(DomainValue tpLocalidade) {
		this.tpLocalidade = tpLocalidade;
	}

	@Column(name = "SG_FILIAL_LOCAL1", length = 3)	
	public String getSgFilialLocal1() {
		return sgFilialLocal1;
	}

	public void setSgFilialLocal1(String sgFilialLocal1) {
		if(sgFilialLocal1 != null)
			this.sgFilialLocal1 = sgFilialLocal1.toUpperCase();
		else
			this.sgFilialLocal1 = null;
	}

	@Column(name = "SG_FILIAL_LOCAL2", length = 3)
	public String getSgFilialLocal2() {
		return sgFilialLocal2;
	}

	public void setSgFilialLocal2(String sgFilialLocal2) {
		if(sgFilialLocal2 != null)
			this.sgFilialLocal2 = sgFilialLocal2.toUpperCase();
		else
			this.sgFilialLocal2 = null;
	}

	@Column(name = "DT_PROGRAMADA_PAGAMENTO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType") 
	public YearMonthDay getDtProgramadaPagto() {
		return dtProgramadaPagto;
	}

	public void setDtProgramadaPagto(YearMonthDay dtProgramadaPagto) {
		this.dtProgramadaPagto = dtProgramadaPagto;
	}

	@Column(name = "DT_PAGAMENTO_EFETUADO")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType") 
	public YearMonthDay getDtPagto() {
		return dtPagto;
	}

	public void setDtPagto(YearMonthDay dtPagto) {
		this.dtPagto = dtPagto;
	}		

	@Column(name = "NR_BANCO")
	public Long getNrBanco() {
		return nrBanco;
	}

	public void setNrBanco(Long nrBanco) {
		this.nrBanco = nrBanco;
	}

	@Column(name = "NR_AGENCIA")
	public Long getNrAgencia() {
		return nrAgencia;
	}

	public void setNrAgencia(Long nrAgencia) {
		this.nrAgencia = nrAgencia;
	}

	@Column(name = "NR_DIGITO_AGENCIA",length = 2)
	public String getNrDigitoAgencia() {
		return nrDigitoAgencia;
	}

	public void setNrDigitoAgencia(String nrDigitoAgencia) {
		this.nrDigitoAgencia = nrDigitoAgencia;
	}

	@Column(name = "NR_CONTA_CORRENTE")
	public Long getNrContaCorrente() {
		return nrContaCorrente;
	}

	public void setNrContaCorrente(Long nrContaCorrente) {
		this.nrContaCorrente = nrContaCorrente;
	}

	@Column(name = "NR_DIGITO_CONTA_CORRENTE", length = 2)
	public String getNrDigitoContaCorrente() {
		return nrDigitoContaCorrente;
	}

	public void setNrDigitoContaCorrente(String nrDigitoContaCorrente) {
		this.nrDigitoContaCorrente = nrDigitoContaCorrente;
	}

	@Column(name = "NR_CTRC", nullable = false, length = 6)
	public Long getNrCtrc() {
		return nrCtrc;
	}

	public void setNrCtrc(Long nrCtrc) {
		this.nrCtrc = nrCtrc;
	}

	@Column(name = "DT_EMISSAO_CTRC")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType") 
	public YearMonthDay getDtEmissaoCtrc() {
		return dtEmissaoCtrc;
	}

	public void setDtEmissaoCtrc(YearMonthDay dtEmissaoCtrc) {
		this.dtEmissaoCtrc = dtEmissaoCtrc;
	}

	@Column(name = "SG_FILIAL_ORIGEM", nullable = false, length = 3)	
	public String getSgFilialOrigem() {
		return sgFilialOrigem;
	}

	public void setSgFilialOrigem(String sgFilialOrigem) {
		if(sgFilialOrigem != null)
			this.sgFilialOrigem = sgFilialOrigem.toUpperCase();
		else
			this.sgFilialOrigem = null;
	}

	@Column(name = "SG_FILIAL_RNC", length = 3)
	public String getSgFilialRnc() {
		return sgFilialRnc;
	}

	public void setSgFilialRnc(String sgFilialRnc) {
		if(sgFilialRnc != null)
			this.sgFilialRnc = sgFilialRnc.toUpperCase();
		else
			this.sgFilialRnc = null;
	}

	@Column(name = "NR_RNC", length = 6)
	public Long getNrRnc() {
		return nrRnc;
	}

	public void setNrRnc(Long nrRnc) {
		this.nrRnc = nrRnc;
	}

	@Column(name = "DT_EMISSAO_RNC")
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType") 
	public YearMonthDay getDtEmissaoRnc() {
		return dtEmissaoRnc;
	}

	public void setDtEmissaoRnc(YearMonthDay dtEmissaoRnc) {
		this.dtEmissaoRnc = dtEmissaoRnc;
	}

	@Column(name = "SG_FILIAL_COMP1")
	public String getSgFilialComp1() {
		return sgFilialComp1;
	}

	public void setSgFilialComp1(String sgFilialComp1) {
		if(sgFilialComp1 != null)
			this.sgFilialComp1 = sgFilialComp1.toUpperCase();
		else
			this.sgFilialComp1 = null;
	}

	@Column(name = "PC_FILIAL_COMP1")
	public Integer getPcFilialComp1() {
		return pcFilialComp1;
	}

	public void setPcFilialComp1(Integer pcFilialComp1) {
		this.pcFilialComp1 = pcFilialComp1;
	}

	@Transient
	public BigDecimal getVlFilialComp1() {
		if(this.getPcFilialComp1() == null || this.getSgFilialComp1() == null) {
			return null;
		} else {			
			BigDecimal vlrComp2 = getVlFilialComp2();
			BigDecimal vlrComp3 = getVlFilialComp3();
			if(vlrComp2 == null)
				vlrComp2 = BigDecimal.valueOf(0);
			if(vlrComp3 == null)
				vlrComp3 = BigDecimal.valueOf(0);			
			
			return this.getVlIndenizacao().subtract(vlrComp2.add(vlrComp3))
					.setScale(2,RoundingMode.HALF_EVEN);
		}
	}

	@Column(name = "SG_FILIAL_COMP2")
	public String getSgFilialComp2() {
		return sgFilialComp2;
	}	

	public void setSgFilialComp2(String sgFilialComp2) {
		if(sgFilialComp2 != null)
			this.sgFilialComp2 = sgFilialComp2.toUpperCase();
		else
			this.sgFilialComp2 = null;
	}

	@Column(name = "PC_FILIAL_COMP2")
	public Integer getPcFilialComp2() {
		return pcFilialComp2;
	}

	public void setPcFilialComp2(Integer pcFilialComp2) {
		this.pcFilialComp2 = pcFilialComp2;
	}

	@Transient
	public BigDecimal getVlFilialComp2() {
		if(this.getPcFilialComp2() == null) {
			return null;
		} else {
			return this.getVlIndenizacao()
					.multiply(BigDecimal.valueOf(this.getPcFilialComp2()))
					.divide(BigDecimal.valueOf(100))
					.setScale(2,RoundingMode.HALF_EVEN);  
		}
	}

	@Column(name = "SG_FILIAL_COMP3")
	public String getSgFilialComp3() {
		return sgFilialComp3;
	}

	public void setSgFilialComp3(String sgFilialComp3) {
		if(sgFilialComp3 != null)
			this.sgFilialComp3 = sgFilialComp3.toUpperCase();
		else
			this.sgFilialComp3 = null;
	}

	@Column(name = "PC_FILIAL_COMP3")
	public Integer getPcFilialComp3() {
		return pcFilialComp3;
	}

	public void setPcFilialComp3(Integer pcFilialComp3) {
		this.pcFilialComp3 = pcFilialComp3;
	}

	@Transient
	public BigDecimal getVlFilialComp3() {
		if(this.getPcFilialComp3() == null) {
			return null;
		} else {
			return this.getVlIndenizacao()
					.multiply(BigDecimal.valueOf(this.getPcFilialComp3()))
					.divide(BigDecimal.valueOf(100))
					.setScale(2,RoundingMode.HALF_EVEN);
		}
	}

	@Column(name="OB_RECIBO", length=500)
	public String getObRecibo() {
		return obRecibo;
	}

	public void setObRecibo(String obRecibo) {
		this.obRecibo = obRecibo;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_NATUREZA_PRODUTO")
	public PpdNaturezaProduto getNaturezaProduto() {
		return naturezaProduto;
	}	

	public void setNaturezaProduto(PpdNaturezaProduto naturezaProduto) {
		this.naturezaProduto = naturezaProduto;
	}		

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_PESSOA")
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FILIAL", nullable = false)
	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FORMA_PGTO")
	public PpdFormaPgto getFormaPgto() {
		return formaPgto;
	}

	public void setFormaPgto(PpdFormaPgto formaPgto) {
		this.formaPgto = formaPgto;
	}
	
	@Transient
	public String getNrReciboCompleto() {
		String nrRecibo = "0000" + this.getNrRecibo().toString();
		nrRecibo = nrRecibo.substring(nrRecibo.length() - 4);
		return this.getFilial().getSgFilial() + " " + nrRecibo;
	}
	
	@Column(name = "BL_EMAILPGTO")
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlEmailPgto() {
		return blEmailPgto;
	}
	
	public void setBlEmailPgto(Boolean blEmailPgto) {
		this.blEmailPgto = blEmailPgto;
	}

	@Column(name = "BL_SEGURADO")
	@Type(type="com.mercurio.adsm.core.model.hibernate.SimNaoType")
	public Boolean getBlSegurado() {
		return blSegurado;
	}
	
	public void setBlSegurado(Boolean blSegurado) {
		this.blSegurado = blSegurado;
	}

	@Transient
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
    	bean.put("idRecibo", this.getIdRecibo());
    	bean.put("dtRecibo", this.getDtRecibo());
    	bean.put("nrRecibo", this.getNrRecibo());
    	bean.put("tpIndenizacao", this.getTpIndenizacao().getValue());
		bean.put("dsTpIndenizacao", this.getTpIndenizacao()
				.getDescriptionAsString());
    	bean.put("tpStatus", this.getTpStatus().getValue());
    	bean.put("dsTpStatus", this.getTpStatus().getDescriptionAsString());
		bean.put("vlIndenizacao", this.getVlIndenizacao());		
		if(this.getTpLocalidade() != null) {
			bean.put("tpLocalidade", this.getTpLocalidade().getValue());
			bean.put("dsTpLocalidade", this.getTpLocalidade()
					.getDescriptionAsString());
			if(this.getTpLocalidade().getValue().equals("R")) {
				bean.put("dsFilialRota", this.getSgFilialLocal1() + " x "
						+ this.getSgFilialLocal2());
			} else {
				bean.put("dsFilialRota", this.getSgFilialLocal1());
			}
		}						
    	bean.put("sgFilialLocal1", this.getSgFilialLocal1());
    	bean.put("sgFilialLocal2", this.getSgFilialLocal2()); 
		bean.put("dtProgramadaPagto", this.getDtProgramadaPagto());
		bean.put("dtPagto", this.getDtPagto());
		bean.put("nrBanco", this.getNrBanco());
		bean.put("nrAgencia", this.getNrAgencia());
		bean.put("nrDigitoAgencia", this.getNrDigitoAgencia());
		if(this.getNrDigitoAgencia() != null && this.getNrAgencia() != null) {
			bean.put("nrAgenciaCompleta", this.getNrAgencia().toString() + "-"
					+ this.getNrDigitoAgencia());
		} else if(this.getNrAgencia() != null) {
			bean.put("nrAgenciaCompleta", this.getNrAgencia().toString());
		} else {
			bean.put("nrAgenciaCompleta", null);
		}
		bean.put("nrContaCorrente", this.getNrContaCorrente());
		bean.put("nrDigitoContaCorrente", this.getNrDigitoContaCorrente());
		if (this.getNrDigitoContaCorrente() != null
				&& this.getNrContaCorrente() != null) {
			bean.put("nrContaCorrenteCompleta", this.getNrContaCorrente()
					.toString() + "-" + this.getNrDigitoContaCorrente());
		} else if(this.getNrContaCorrente() != null) {
			bean.put("nrContaCorrenteCompleta", this.getNrContaCorrente()
					.toString());
		} else {
			bean.put("nrContaCorrenteCompleta", null);
		}
		bean.put("nrCtrc", this.getNrCtrc());		
		bean.put("dtEmissaoCtrc", this.getDtEmissaoCtrc());
		bean.put("sgFilialOrigem", this.getSgFilialOrigem());		
		bean.put("dsCtrc", this.getSgFilialOrigem() + " " + this.getNrCtrc());
		bean.put("sgFilialRnc", this.getSgFilialRnc());		
		bean.put("nrRnc", this.getNrRnc());
		bean.put("dtEmissaoRnc", this.getDtEmissaoRnc());
		bean.put("nrSeguro", this.getNrSeguro());		
		bean.put("sgFilialComp1", this.getSgFilialComp1());
		bean.put("sgFilialComp2", this.getSgFilialComp2());
		bean.put("sgFilialComp3", this.getSgFilialComp3());
		bean.put("pcFilialComp1", this.getPcFilialComp1());
		bean.put("pcFilialComp2", this.getPcFilialComp2());
		bean.put("pcFilialComp3", this.getPcFilialComp3());			
		bean.put("vlFilialComp1", this.getVlFilialComp1());
		bean.put("vlFilialComp2", this.getVlFilialComp2());
		bean.put("vlFilialComp3", this.getVlFilialComp3());
		bean.put("obRecibo", this.getObRecibo());			
		bean.put("blEmailPgto", this.getBlEmailPgto());
		bean.put("blSegurado", this.getBlSegurado());
		
		if(this.getSgFilialRnc() != null && this.getNrRnc() != null)
			bean.put("dsRnc", this.getSgFilialRnc() + " " + this.getNrRnc());
		
		if(this.getNaturezaProduto() != null) {			
			bean.putAll(this.getNaturezaProduto().getMapped());			
		}
		
		if(this.getFilial() != null) {
			bean.put("idFilial", this.getFilial().getIdFilial());
			bean.put("sgFilial", this.getFilial().getSgFilial());			
			if(this.getFilial().getPessoa() != null) {
				bean.put("nmFilial", this.getFilial().getPessoa()
						.getNmFantasia());
				bean.put("nmFantasia", this.getFilial().getPessoa()
						.getNmFantasia());
			}
		}
		
		if(this.getPessoa() != null) {
			bean.put("idPessoa", pessoa.getIdPessoa());
			bean.put("nrIdentificacao", pessoa.getNrIdentificacaoFormatado());
			bean.put("nrIdentificacaoUnformatted", pessoa.getNrIdentificacao()); 
			bean.put("nmPessoa", pessoa.getNmPessoa());
		}
		
		if(this.getFormaPgto() != null) {
			bean.put("idFormaPgto", this.getFormaPgto().getIdFormaPgto());
			bean.put("cdFormaPgto", this.getFormaPgto().getCdFormaPgto());
			bean.put("dsFormaPgto", this.getFormaPgto().getDsFormaPgto());
		}
		
		return bean;
	}
	
}
