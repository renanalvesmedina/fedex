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
public class OcorrenciaPce implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final long ID_OCORR_PCE_COL_ABERT0_REG_PED_COL_AEROPORTO = 1;
	public static final long ID_OCORR_PCE_COL_ABERT0_REG_PED_COL_DIRETA = 3;
	public static final long ID_OCORR_PCE_COL_ABERT0_REG_PED_COL_DEVOLUCAO = 5;
	public static final long ID_OCORR_PCE_COL_ABERT0_REG_PED_COL_NORMAL = 7;
	public static final long ID_OCORR_PCE_COL_MANIF_EMISS_MANIF_COLETA = 9;
	public static final long ID_OCORR_PCE_COL_EXEC_EXECUCAO_COLETA = 10;
	public static final long ID_OCORR_PCE_COL_CANC_EXECUCAO_COLETA = 12;
	public static final long ID_OCORR_PCE_INIC_DESCARGA_INICIO_DESCARGA_RECEPCAO = 13;
	public static final long ID_OCORR_PCE_TERM_DESCARGA_TERMINO_DESCARGA_RECEPCAO = 14;
	public static final long ID_OCORR_PCE_NO_TERMINAL_EMISS_DOC_SERV = 15;
	public static final long ID_OCORR_PCE_NO_TERMINAL_GERAC_PRE_MANIF = 16;
	public static final long ID_OCORR_PCE_AGUARD_LIBERAC_VIAGEM_EMISS_MANIF_VIAGEM = 17;
	public static final long ID_OCORR_PCE_INIC_DESCARGA_INICIO_DESCARGA = 18;
	public static final long ID_OCORR_PCE_TERM_DESCARGA_TERMINO_DESCARGA = 19;
	public static final long ID_OCORR_PCE_CARGA_AGENDADA_REG_AGENDAMENTO = 20;
	public static final long ID_OCORR_PCE_GERACAO_RNC_EMISSAO_RNC = 23;
	public static final long ID_OCORR_PCE_ENCERRAMENTO_RNC_REGISTR_DISPOSICAO = 25;
	public static final long ID_OCORR_PCE_NA_ENTREGA_EMISS_MANIF_ENTREGA = 26;
	public static final long ID_OCORR_PCE_ENTREGA_REALIZADA_EXEC_ENTREGA = 27;
	public static final long ID_OCORR_PCE_ENTREGA_NAO_EXECUTADA_EXEC_ENTREGA = 28;
	public static final long ID_OCORR_PCE_REENTREGA_SOLICITADA_EXEC_ENTREGA = 29;
	public static final long ID_OCORR_PCE_ENTREGA_RECUSADA_EXEC_ENTREGA = 30;
	public static final long ID_OCORR_PCE_CAD_PROCESSO_CAD_PROC_SINISTRO = 31;
	public static final long ID_OCORR_PCE_GERAR_CARTA_OCORR_EMIT_CARTA_OCORR = 32;
	public static final long ID_OCORR_PCE_GERAR_CARTA_EMIT_CARTA_MERC_DISPOSICAO = 33;
	public static final long ID_OCORR_PCE_CARGA_REAGENDADA_REG_AGENDAMENTO = 34;
	public static final long ID_OCORR_PCE_GERAR_CARTA_EMIT_CARTA_MERC_APREEND_FISCALIZ = 38;
	public static final long ID_OCORR_PCE_PARA_ENTREGA_EMISS_CONTROLE_CARGA = 45;	
	public static final long ID_EVENTO_PCE_GERAR_RIM_INCLUIR_RIM = 34;
	public static final long ID_EVENTO_PCE_REG_OCORRENCIA_REG_OCORR_DOC_SERVICO = 35;

    /** identifier field */
    private Long idOcorrenciaPce;

    /** persistent field */
    private VarcharI18n dsOcorrenciaPce;

    /** persistent field */
    private DomainValue tpSituacao;

    private Long cdOcorrenciaPce;
    
    /** persistent field */
    private com.mercurio.lms.vendas.model.EventoPce eventoPce;

    /** persistent field */
    private List descritivoPces;
    
   	public String getOcorrenciaPceGrid() {
		if(this.cdOcorrenciaPce != null && this.dsOcorrenciaPce != null)
			return this.cdOcorrenciaPce
					+ " - "
					+ this.dsOcorrenciaPce.getValue(LocaleContextHolder
							.getLocale());
		return null;
	}

	public String getOcorrenciaPceCombo() {
		if(this.cdOcorrenciaPce != null && this.dsOcorrenciaPce != null)
			return this.cdOcorrenciaPce
					+ " - "
					+ this.dsOcorrenciaPce.getValue(LocaleContextHolder
							.getLocale());
		return null;
	}

    public Long getIdOcorrenciaPce() {
        return this.idOcorrenciaPce;
    }

    public void setIdOcorrenciaPce(Long idOcorrenciaPce) {
        this.idOcorrenciaPce = idOcorrenciaPce;
    }

    public Long getCdOcorrenciaPce() {
        return this.cdOcorrenciaPce;
    }
    
    public void setCdOcorrenciaPce(Long cdOcorrenciaPce) {
		this.cdOcorrenciaPce = cdOcorrenciaPce;
	}

    public VarcharI18n getDsOcorrenciaPce() {
		return dsOcorrenciaPce;
    }

	public void setDsOcorrenciaPce(VarcharI18n dsOcorrenciaPce) {
        this.dsOcorrenciaPce = dsOcorrenciaPce;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.vendas.model.EventoPce getEventoPce() {
        return this.eventoPce;
    }

    public void setEventoPce(com.mercurio.lms.vendas.model.EventoPce eventoPce) {
        this.eventoPce = eventoPce;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.DescritivoPce.class)     
    public List getDescritivoPces() {
        return this.descritivoPces;
    }

    public void setDescritivoPces(List descritivoPces) {
        this.descritivoPces = descritivoPces;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idOcorrenciaPce",
				getIdOcorrenciaPce()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OcorrenciaPce))
			return false;
        OcorrenciaPce castOther = (OcorrenciaPce) other;
		return new EqualsBuilder().append(this.getIdOcorrenciaPce(),
				castOther.getIdOcorrenciaPce()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOcorrenciaPce()).toHashCode();
    }

}
