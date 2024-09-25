package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class EventoPce implements Serializable {

	private static final long serialVersionUID = 1L;
	//EVENTOS DO PROCESSO: COLETA
	public static final long ID_EVENTO_PCE_REG_PED_COLETA_NORMAL = 1;
	public static final long ID_EVENTO_PCE_REG_PED_COLETA_DEVOLUCAO = 2;
	public static final long ID_EVENTO_PCE_REG_PED_COLETA_AEROPORTO = 3;
	public static final long ID_EVENTO_PCE_REG_PED_COLETA_DIRETA = 4;
	public static final long ID_EVENTO_PCE_EMISS_MANIF_COLETA = 5;
	public static final long ID_EVENTO_PCE_EXECUCAO_COLETA = 6;
	
	//EVENTOS DO PROCESSO: RECEPÇÃO
	public static final long ID_EVENTO_PCE_INICIO_DESCARGA_RECEPCAO = 7;
	public static final long ID_EVENTO_PCE_TERMINO_DESCARGA_RECEPCAO = 8;
	
	//EVENTOS DO PROCESSO: EXPEDIÇÃO
	public static final long ID_EVENTO_PCE_EMISS_DOC_SERVICO = 9;
	public static final long ID_EVENTO_PCE_EMISS_MANIF_VIAGEM = 11;
	
	//EVENTOS DO PROCESSO: CARREGAMENTO
	public static final long ID_EVENTO_PCE_GERACAO_PRE_MANIF = 10;
	
	//EVENTOS DO PROCESSO: DESCARGA
	public static final long ID_EVENTO_PCE_INICIO_DESCARGA = 12;
	public static final long ID_EVENTO_PCE_TERMINO_DESCARGA = 13;
	
	//EVENTOS DO PROCESSO: AGENDAMENTO
	public static final long ID_EVENTO_PCE_REGISTRO_AGENDAMENTO = 14;
	
	//EVENTOS DO PROCESSO: RNC
	public static final long ID_EVENTO_PCE_EMISSAO_RNC = 15;
	public static final long ID_EVENTO_PCE_REGISTRAR_DISPOSICAO = 16;

	//EVENTOS DO PROCESSO: ENTREGA
	public static final long ID_EVENTO_PCE_EMISS_CONTROLE_CARGA = 17;
	public static final long ID_EVENTO_PCE_EMISS_MANIF_ENTREGA = 18;
	public static final long ID_EVENTO_PCE_EXECUCAO_ENTREGA = 19;
	
	//EVENTOS DO PROCESSO: SINISTRO
	public static final long ID_EVENTO_PCE_CADASTRAR_PROC_SINISTRO = 20;
	public static final long ID_EVENTO_PCE_EMITIR_CARTA_OCORRENCIA = 23;
	
	//EVENTOS DO PROCESSO: MERCADORIA A DISPOSIÇÃO
	public static final long ID_EVENTO_PCE_INCLUSAO_MERC_DISPOSICAO = 21;
	public static final long ID_EVENTO_PCE_MERC_APREENDIDA_FISCALIZACAO = 22;

	//EVENTOS DO PROCESSO: INDENIZAÇÃO
	public static final long ID_EVENTO_PCE_INCLUIR_RIM = 23;
	
	//EVENTOS DO PROCESSO: PENDÊNCIA
	public static final long ID_EVENTO_PCE_REG_OCORR_DOC_SERVICO = 24;

	/** identifier field */
    private Long idEventoPce;

    /** persistent field */
    private VarcharI18n dsEventoPce;

    /** persistent field */
    private DomainValue tpSituacao;
    
    private Long cdEventoPce;

    /** persistent field */
    private com.mercurio.lms.vendas.model.ProcessoPce processoPce;

    /** persistent field */
    private List ocorrenciaPces;
    
    public String getEventoPceGrid() {
    	if(this.cdEventoPce != null && this.dsEventoPce != null)
			return this.cdEventoPce
					+ " - "
					+ this.dsEventoPce
							.getValue(LocaleContextHolder.getLocale());
		return null;
	}

    public String getEventoPceCombo() {
    	if(this.cdEventoPce != null && this.dsEventoPce != null)
			return this.cdEventoPce
					+ " - "
					+ this.dsEventoPce
							.getValue(LocaleContextHolder.getLocale());
		return null;
	}

    public Long getIdEventoPce() {
        return this.idEventoPce;
    }

    public void setIdEventoPce(Long idEventoPce) {
        this.idEventoPce = idEventoPce;
    }

    public Long getCdEventoPce() {
        return this.cdEventoPce;
    }
    
    public void setCdEventoPce(Long cdEventoPce) {
		this.cdEventoPce = cdEventoPce;
	}

    public VarcharI18n getDsEventoPce() {
		return dsEventoPce;
    }

	public void setDsEventoPce(VarcharI18n dsEventoPce) {
        this.dsEventoPce = dsEventoPce;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.vendas.model.ProcessoPce getProcessoPce() {
        return this.processoPce;
    }

	public void setProcessoPce(
			com.mercurio.lms.vendas.model.ProcessoPce processoPce) {
        this.processoPce = processoPce;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.OcorrenciaPce.class)     
    public List getOcorrenciaPces() {
        return this.ocorrenciaPces;
    }

    public void setOcorrenciaPces(List ocorrenciaPces) {
        this.ocorrenciaPces = ocorrenciaPces;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idEventoPce", getIdEventoPce()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EventoPce))
			return false;
        EventoPce castOther = (EventoPce) other;
		return new EqualsBuilder().append(this.getIdEventoPce(),
				castOther.getIdEventoPce()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEventoPce()).toHashCode();
    }
}
