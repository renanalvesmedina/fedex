package com.mercurio.lms.entrega.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.expedicao.model.DoctoServico;

@Entity
@Table(name="DOCTO_SERVICO_IROAD")
@SequenceGenerator(name="DOCTO_SERVICO_IROAD_SEQ", sequenceName="DOCTO_SERVICO_IROAD_SQ", allocationSize=1)
public class DoctoServicoIroad implements Serializable {
	private static final long serialVersionUID = 2L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCTO_SERVICO_IROAD_SEQ")
    @Column(name = "ID_DOCTO_SERVICO_IROAD", nullable = false)
	private Long IdDoctoServicoIroad;
    
    @ManyToOne
    @JoinColumn(name = "ID_DOCTO_SERVICO", nullable = false)
	private DoctoServico doctoServico;
    
    @Column(name = "DS_ROTA_IROAD", nullable=true, length=2000)
    private String dsRotaIroad;
    
    @Column(name = "NR_SEQUENCIA_ROTA", nullable = true)
    private String nrSequenciaRota;
    
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private UsuarioLMS usuarioInclusao;
    
    @Columns(columns = { @Column(name = "DH_INCLUSAO"),
            @Column(name = "DH_INCLUSAO_TZR") })
    @Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeTZRUserType")
    private DateTime dhInclusao;

    public Long getIdDoctoServicoIroad() {
        return IdDoctoServicoIroad;
    }

    public void setIdDoctoServicoIroad(Long idDoctoServicoIroad) {
        IdDoctoServicoIroad = idDoctoServicoIroad;
    }

    public DoctoServico getDoctoServico() {
        return doctoServico;
    }

    public void setDoctoServico(DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public String getDsRotaIroad() {
        return dsRotaIroad;
    }

    public void setDsRotaIroad(String dsRotaIroad) {
        this.dsRotaIroad = dsRotaIroad;
    }

    public String getNrSequenciaRota() {
        return nrSequenciaRota;
    }

    public void setNrSequenciaRota(String nrSequenciaRota) {
        this.nrSequenciaRota = nrSequenciaRota;
    }

    public UsuarioLMS getUsuarioInclusao() {
        return usuarioInclusao;
    }

    public void setUsuarioInclusao(UsuarioLMS usuarioInclusao) {
        this.usuarioInclusao = usuarioInclusao;
    }

    public DateTime getDhInclusao() {
        return dhInclusao;
    }

    public void setDhInclusao(DateTime dhInclusao) {
        this.dhInclusao = dhInclusao;
    }
    
}
