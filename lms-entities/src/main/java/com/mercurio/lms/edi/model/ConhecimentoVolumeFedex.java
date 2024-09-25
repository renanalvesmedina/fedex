package com.mercurio.lms.edi.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * LMSA-6520: LMSA-6534
 */
@Entity
@Table(name = "VOLUME_CONHECIMENTO_FEDEX")
public class ConhecimentoVolumeFedex implements Serializable {
    
    private static final long serialVersionUID = -850565401243139530L;
    
    private Long idVolumeConhecimentoFedex;
    private ConhecimentoFedex conhecimentoFedex;
    private String codigoVolume;
    
    public ConhecimentoVolumeFedex() {
    }

    @Id
    @Column(name = "ID_VOLUME_CONHECIMENTO_FEDEX", nullable = false)
    public Long getIdVolumeConhecimentoFedex() {
        return idVolumeConhecimentoFedex;
    }

    public void setIdVolumeConhecimentoFedex(Long idVolumeConhecimentoFedex) {
        this.idVolumeConhecimentoFedex = idVolumeConhecimentoFedex;
    }

    @ManyToOne
    @JoinColumn(name="ID_CONHECIMENTO_FEDEX", nullable = true)
    @Transient
    public ConhecimentoFedex getConhecimentoFedex() {
        return conhecimentoFedex;
    }

    public void setConhecimentoFedex(ConhecimentoFedex conhecimentoFedex) {
        this.conhecimentoFedex = conhecimentoFedex;
    }

    @Column(name = "NR_CODIGO_VOLUME", length = 100)
    public String getCodigoVolume() {
        return codigoVolume;
    }

    public void setCodigoVolume(String codigoVolume) {
        this.codigoVolume = codigoVolume;
    }

    

}
